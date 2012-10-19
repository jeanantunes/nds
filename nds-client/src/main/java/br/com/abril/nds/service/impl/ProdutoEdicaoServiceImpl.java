package br.com.abril.nds.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Brinde;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Dimensao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.ProdutoEdicao}
 * 
 * @author Discover Technology
 */
@Service
public class ProdutoEdicaoServiceImpl implements ProdutoEdicaoService {

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private DistribuicaoFornecedorRepository distribuicaoFornecedorRepository;
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private CapaService capaService;
	
	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private DescontoProdutoEdicaoRepository descontoProdutoEdicaoRepository;
	
	@Autowired
	private ProdutoService pService;
	
	@Autowired
	private LancamentoService lService;	
	
	@Override
	@Transactional(readOnly = true)
	public ProdutoEdicao obterProdutoEdicao(Long idProdutoEdicao, boolean indCarregaFornecedores) {
		
		if (idProdutoEdicao == null || Long.valueOf(0).equals(idProdutoEdicao)) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, 
					"Código de identificação da Edição é inválida!"));
		}

		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);

		
		if(indCarregaFornecedores) {
			produtoEdicao.getProduto().getFornecedor().getJuridica();
			produtoEdicao.getProduto().getFornecedores();
		}
		
		return produtoEdicao;
	}	
	
	@Override
	@Transactional(readOnly = true)
 	public List<ProdutoEdicao> obterProdutoEdicaoPorNomeProduto(String nomeProduto) {
		return produtoEdicaoRepository.obterProdutoEdicaoPorNomeProduto(nomeProduto);
	}

	@Override
	@Transactional(readOnly = true)
	public FuroProdutoDTO obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
			String codigo, String nomeProduto, Long edicao, Date dataLancamento) {
		
		List<String> mensagensValidacao = new ArrayList<String>();
		
		if (codigo == null || codigo.isEmpty()){
			mensagensValidacao.add("Código é obrigatório.");
		}
		
		if (edicao == null){
			mensagensValidacao.add("Edição é obrigatório.");
		}
		
		if (dataLancamento == null){
			mensagensValidacao.add("Data Lançamento é obrigatório.");
		}
		
		if (!mensagensValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
		}
		
		FuroProdutoDTO furoProdutoDTO = produtoEdicaoRepository.
				obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
						codigo, nomeProduto, edicao, dataLancamento);
		
		if (furoProdutoDTO != null){
			//buscar path de imagens
			ParametroSistema parametroSistema = 
					this.parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_IMAGENS_CAPA);
			
			if (parametroSistema != null){
				furoProdutoDTO.setPathImagem(parametroSistema.getValor() + furoProdutoDTO.getPathImagem());
			}
			
			//buscar proxima data para lançamento
			
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(new SimpleDateFormat(furoProdutoDTO.DATE_PATTERN_PT_BR).parse(furoProdutoDTO.getNovaData()));
			} catch (ParseException e) {
				return furoProdutoDTO;
			}
			
			List<Integer> listaDiasSemana = 
					this.distribuicaoFornecedorRepository.obterDiasSemanaDistribuicao(
							furoProdutoDTO.getCodigoProduto(), 
							furoProdutoDTO.getIdProdutoEdicao());
			
			if (listaDiasSemana != null && !listaDiasSemana.isEmpty()){
				int diaSemana = -1;
				for (Integer dia : listaDiasSemana){
					if (dia > calendar.get(Calendar.DAY_OF_WEEK)){
						diaSemana = dia;
						break;
					}
				}
				
				if (diaSemana == -1){
					diaSemana = listaDiasSemana.get(0);
				}
				
				while (calendar.get(Calendar.DAY_OF_WEEK) != diaSemana){
					calendar.add(Calendar.DAY_OF_MONTH, 1);
				}
				
				furoProdutoDTO.setNovaData(
						new SimpleDateFormat(furoProdutoDTO.DATE_PATTERN_PT_BR).format(calendar.getTime()));
			}
		}
		
		return furoProdutoDTO;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ProdutoEdicao obterProdutoEdicaoPorCodProdutoNumEdicao(String codigoProduto, String numeroEdicao) {

		List<String> mensagensValidacao = new ArrayList<String>();
		
		if (codigoProduto == null || codigoProduto.isEmpty()) {
			
			mensagensValidacao.add("Código é obrigatório.");
		}
		
		if (numeroEdicao == null || numeroEdicao.isEmpty()) {
			
			mensagensValidacao.add("Número edição é obrigatório.");
		}

		if (!Util.isLong(numeroEdicao)) {

			mensagensValidacao.add("Número edição é inválido.");
		}
		
		if (!mensagensValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
		}
		
		return produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
				codigoProduto, Long.parseLong(numeroEdicao));
	}

	@Override
	@Transactional
	public List<ProdutoEdicao> obterProdutosEdicaoPorCodigoProduto(
			String codigoProduto) {
		return produtoEdicaoRepository.obterProdutosEdicaoPorCodigoProduto(codigoProduto);
	}

	@Override
	@Transactional
	public void alterarProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicaoRepository.alterar(produtoEdicao);		
	}
	
	@Transactional(readOnly = true)
	public List<ProdutoEdicao> obterProdutoPorCodigoNome(String codigoNomeProduto) {
		
		if (codigoNomeProduto == null || codigoNomeProduto.trim().isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Codigo/nome produto é obrigatório.");
		}
		
		return this.produtoEdicaoRepository.obterProdutoPorCodigoNome(codigoNomeProduto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProdutoEdicaoDTO> pesquisarEdicoes(String codigoProduto, String nomeComercial,
			Intervalo<Date> dataLancamento, Intervalo<BigDecimal> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde,
			String sortorder, String sortname, int page, int maxResults) {
		
		final int initialResult = ((page * maxResults) - maxResults);
		return this.produtoEdicaoRepository.pesquisarEdicoes(codigoProduto, nomeComercial, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde, sortorder, sortname, initialResult, maxResults);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long countPesquisarEdicoes(String codigoProduto, String nomeComercial,
			Intervalo<Date> dataLancamento, Intervalo<BigDecimal> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde) {
		
		return this.produtoEdicaoRepository.countPesquisarEdicoes(codigoProduto, nomeComercial, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ProdutoEdicaoDTO> pesquisarUltimasEdicoes(String codigoProduto,
			int maxResults) {
		
		return this.produtoEdicaoRepository.pesquisarEdicoes(codigoProduto,null,null,null,null,null,false, "DESC",
				"numeroEdicao", 0, 5);
	}
	
	/**
	 * Insere os dados de desconto relativos ao produto edição em questão.
	 * 
	 * @param produtoEdicao
	 * @param indNovoProdutoEdicao
	 */
	private void inserirDescontoProdutoEdicao(ProdutoEdicao produtoEdicao, boolean indNovoProdutoEdicao) {

		Produto produto = produtoEdicao.getProduto();
		
		GrupoProduto grupoProduto = produto.getTipoProduto().getGrupoProduto();
		
		if(!indNovoProdutoEdicao || GrupoProduto.OUTROS.equals( grupoProduto )) {
			return;
		}
		
		Fornecedor fornecedor = produto.getFornecedor();
		
		Set<Fornecedor> conjuntoFornecedor = new HashSet<Fornecedor>();
		
		conjuntoFornecedor.add(fornecedor);
		
		
		
		Set<DescontoProdutoEdicao> conjuntoDescontoProdutoEdicaoEspecifico = 
				descontoProdutoEdicaoRepository.obterDescontoProdutoEdicao(TipoDesconto.ESPECIFICO, fornecedor, null);
		
		if(conjuntoDescontoProdutoEdicaoEspecifico!=null && !conjuntoDescontoProdutoEdicaoEspecifico.isEmpty()) {
			
			
			for(DescontoProdutoEdicao descontoEspecifico : conjuntoDescontoProdutoEdicaoEspecifico) {
				
				Cota cota = descontoEspecifico.getCota();
				
				descontoService.processarDescontoCota(cota, conjuntoFornecedor, descontoEspecifico.getDesconto());
				
			}
			
			
		}
		
		Set<DescontoProdutoEdicao> conjuntoDescontoProdutoEdicaoGeral = 
				descontoProdutoEdicaoRepository.obterDescontoProdutoEdicao(TipoDesconto.GERAL, fornecedor, null);
		
		if(conjuntoDescontoProdutoEdicaoGeral!=null && !conjuntoDescontoProdutoEdicaoGeral.isEmpty()) {
			
			
			for(DescontoProdutoEdicao descontoGeral : conjuntoDescontoProdutoEdicaoGeral) {
				
				descontoService.processarDescontoDistribuidor(conjuntoFornecedor, descontoGeral.getDesconto());
				
			}
			
			
		}
		
	}
	
	@Override
	@Transactional
	public void salvarProdutoEdicao(ProdutoEdicaoDTO dto, String codigoProduto, 
			String contentType, InputStream imgInputStream) {
		
		ProdutoEdicao produtoEdicao = null;
		
		boolean indNovoProdutoEdicao = (dto.getId() == null);
		
		if (indNovoProdutoEdicao) {
			produtoEdicao = new ProdutoEdicao();
			produtoEdicao.setProduto(produtoRepository.obterProdutoPorCodigo(codigoProduto));
			produtoEdicao.setOrigemInterface(Boolean.FALSE);
		} else {
			produtoEdicao = produtoEdicaoRepository.buscarPorId(dto.getId());
		}		
		
		
		// 01 ) Salvar/Atualizar o ProdutoEdicao:
		this.salvarProdutoEdicao(dto, produtoEdicao);
		
		
		// 02) Salvar imagem:
		if (imgInputStream != null) {
			
			// Verifica se o tipo do arquivo é imagem JPEG:
			if (!contentType.toLowerCase().matches("image/[p]?jpeg")) {
				throw new ValidacaoException(TipoMensagem.ERROR, 
						"O formato da imagem da capa não é válido!");
			}
			
			capaService.saveCapa(produtoEdicao.getId(), contentType, imgInputStream);
		}
		
		// 03) Salvar/Atualizar o lancamento:
		this.salvarLancamento(dto, produtoEdicao);
		
		this.inserirDescontoProdutoEdicao(produtoEdicao, indNovoProdutoEdicao);
		
	}
	
	/**
	 * Aplica todas as regras de validação para o cadastro de uma Edição.
	 * 
	 * @param dto
	 * @param produtoEdicao
	 */
	private void validarProdutoEdicao(ProdutoEdicaoDTO dto, ProdutoEdicao produtoEdicao) {
		
		/*
		 * Regra: Os campos abaixos só podem ser alterados caso a Edição
		 * ainda não tenha sido publicado pelo distribuidor:
		 * - Código da Edição;
		 * - Número da Edição;
		 * 
		 * Alteração: "Data de Lançamento do Distribuidor" > "Data 'de hoje'"
		 */
		if (produtoEdicaoRepository.isProdutoEdicaoJaPublicada(produtoEdicao.getId())) {
			

			if (produtoEdicao.getProduto().getCodigo()!=null && !produtoEdicao.getProduto().getCodigo().equals(dto.getCodigoProduto())) {
				throw new ValidacaoException(TipoMensagem.ERROR, 
						"Não é permitido alterar o código de uma Edição já publicada!");
			}

			// Campo: Número do ProdutoEdicao:
			if (!produtoEdicao.getNumeroEdicao().equals(dto.getNumeroEdicao())) {
				throw new ValidacaoException(TipoMensagem.ERROR, 
						"Não é permitido alterar o número de uma Edição já publicada!");
			}
		}
		
		/* Regra: Se não existir nenhuma edição associada ao produto, salvar n. 1 */
		if (!this.produtoEdicaoRepository.hasProdutoEdicao(produtoEdicao.getProduto())) {
			produtoEdicao.setNumeroEdicao(Long.valueOf(1));
		}
		
		/* Regra: Não deve existir dois número de edição para o mesmo grupo de Edições: */
		if (this.produtoEdicaoRepository.isNumeroEdicaoCadastrada(
				dto.getCodigoProduto(), dto.getNumeroEdicao(), produtoEdicao.getId())) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Este número de edição já esta cadastrada para outra Edição!");
		}
		
		/* Regra: Não deve existir duas Edições com o mesmo código de barra. */
		List<ProdutoEdicao> lstPeCodBarra = 
				this.produtoEdicaoRepository.obterProdutoEdicaoPorCodigoDeBarra(
						dto.getCodigoDeBarras(), produtoEdicao.getId());
		if (lstPeCodBarra != null && !lstPeCodBarra.isEmpty()) {
			
			ProdutoEdicao peCodBarra = lstPeCodBarra.get(0);
			StringBuilder msg = new StringBuilder();
			msg.append("O Produto '");
			msg.append(peCodBarra.getProduto().getNome());
			msg.append("' - Edição º");
			msg.append(peCodBarra.getNumeroEdicao());
			msg.append(" já esta cadastrado com este código de barra!");
			
			throw new ValidacaoException(TipoMensagem.ERROR, msg.toString());
		}		
	}
	
	/**
	 * Salva ou atualiza um ProdutoEdicao.<br>.
	 * Os campos permitidos no cenário de gravação ou alteração de um 
	 * ProdutoEdição criado por um Distribuidor:
	 * <ul>
	 * <li>Imagem da Capa;</li>
	 * <li>Código do ProdutoEdição;</li>
	 * <li>Nome Comercial do ProdutoEdição;</li>
	 * <li>Número da Edição;</li>
	 * <li>Pacote Padrão;</li>
	 * <li>Tipo de Lançamento;</li>
	 * <li>Preço da Capa (Previsto);</li>
	 * <li>Data de Lançamento (Previsto);</li>
	 * <li>Reparte Previsto;</li>
	 * <li>Reparte Promocional;</li>
	 * <li>Categoria;</li>
	 * <li>Código de Barras;</li>
	 * <li>Código de Barras Corporativo;</li>
	 * <li>Desconto;</li>
	 * <li>Chamada da Capa;</li>
	 * <li>Regime de Recolhimento (Parcial);</li>
	 * <li>Brinde;</li>
	 * <li>Boletim Informativo;</li>
	 * </ul>
	 * <br> 
	 * Os campos permitidos no cenário de alteração de um ProdutoEdição 
	 * vindo da Interface:
	 * <ul>
	 * <li>Imagem da Capa;</li>
	 * <li>Preço da Capa (Real);</li>
	 * <li>Código de Barras;</li>
	 * <li>Chamada da Capa;</li>
	 * <li>Brinde;</li>
	 * <li>Peso;</li>
	 * </ul>
	 * 
	 * @param dto
	 * @param produtoEdicao
	 */
	private void salvarProdutoEdicao(ProdutoEdicaoDTO dto, ProdutoEdicao produtoEdicao) {
		
		// 01) Validações:
		this.validarProdutoEdicao(dto, produtoEdicao);
		
		
		// 02) Campos a serem persistidos e/ou alterados:
		
		BigInteger repartePrevisto = (dto.getRepartePrevisto() == null) 
				? BigInteger.ZERO : dto.getRepartePrevisto();
		
		BigInteger repartePromocional = (dto.getRepartePromocional() == null) 
				? BigInteger.ZERO : dto.getRepartePromocional();
		
		boolean origemManual = (produtoEdicao.getOrigemInterface() == null) ? true :  !produtoEdicao.getOrigemInterface().booleanValue();
		
		if (origemManual) {
			// Campos exclusivos para o Distribuidor::
			
			// Identificação:
			produtoEdicao.setNomeComercial(dto.getNomeComercialProduto());
			produtoEdicao.setNumeroEdicao(dto.getNumeroEdicao());
			produtoEdicao.setPacotePadrao(dto.getPacotePadrao());
			
			// Preço de capa:
			produtoEdicao.setPrecoPrevisto(dto.getPrecoPrevisto());
			
			// Reparte:
			produtoEdicao.setReparteDistribuido(repartePrevisto.add(repartePromocional));
			
			// Características do lançamento:
			// TODO: !!!colocar o select da categoria aqui!!!
			produtoEdicao.setCodigoDeBarraCorporativo(dto.getCodigoDeBarrasCorporativo());
			
			// Outros:
			produtoEdicao.setChamadaCapa(dto.getChamadaCapa());
			produtoEdicao.setParcial(dto.isParcial());	// Regime de Recolhimento;
			
			// Característica Física:
			produtoEdicao.setPeso(dto.getPeso());
			Dimensao dimEdicao = new Dimensao();
			dimEdicao.setLargura(dto.getLargura());
			dimEdicao.setComprimento(dto.getComprimento());
			dimEdicao.setEspessura(dto.getEspessura());
			produtoEdicao.setDimensao(dimEdicao);
			
			produtoEdicao.getProduto().setNomeComercial(dto.getNomeComercial());
			
			// Texto boletim informativo:
			produtoEdicao.setBoletimInformativo(dto.getBoletimInformativo());
		} else {
			// Campos exclusivos para a Interface:
			
			// Preço de capa:
			produtoEdicao.setPrecoVenda(dto.getPrecoVenda());	// View: Preço real;
		}
		
		// Campos comuns para o Distribuidor e Interface:
		
		// Características do lançamento:
		produtoEdicao.setCodigoDeBarras(dto.getCodigoDeBarras());
		
		// Outros:
		produtoEdicao.setChamadaCapa(dto.getChamadaCapa());
		produtoEdicao.setPossuiBrinde(dto.isPossuiBrinde());
		if(produtoEdicao.getBrinde()==null){
			produtoEdicao.setBrinde(new Brinde());
		}
		produtoEdicao.getBrinde().setDescricao(dto.getDescricaoBrinde());
		
		// Característica Física:
		produtoEdicao.setPeso(dto.getPeso());
		
		produtoEdicao.setNumeroLancamento(dto.getNumeroLancamento());
		
		if (produtoEdicao.getId() == null) {			
			// Salvar:
			produtoEdicaoRepository.adicionar(produtoEdicao);
		} else {			
			// Atualizar:
			produtoEdicaoRepository.alterar(produtoEdicao);
		}
	}
	
	/**
	 * Salva um novo lançamento.
	 * 
	 * @param dto
	 * @param produtoEdicao
	 */
	private void salvarLancamento(ProdutoEdicaoDTO dto, ProdutoEdicao produtoEdicao) {
		
		// Só pode alterar quando o ProdutoEdicao for criado pelo Distribuidor:
		
		boolean origemInterface = (produtoEdicao.getOrigemInterface() == null) ?  false : produtoEdicao.getOrigemInterface().booleanValue();
		
		if (origemInterface) {
			return;
		}
		
		Lancamento lancamento = null;
		if (produtoEdicao.getLancamentos().isEmpty()) {
			lancamento = new Lancamento();
		} else {
			for (Lancamento lancto: produtoEdicao.getLancamentos()) {
				if (lancamento == null 
						|| DateUtil.isDataInicialMaiorDataFinal(lancto.getDataLancamentoDistribuidor(), lancamento.getDataLancamentoDistribuidor())) {
					lancamento = lancto;
				}
			}
		}
		
		lancamento.setTipoLancamento(dto.getTipoLancamento());
		
		lancamento.setDataLancamentoPrevista(dto.getDataLancamentoPrevisto());
		lancamento.setDataRecolhimentoPrevista(dto.getDataRecolhimentoPrevisto());
		
		BigInteger repartePrevisto = dto.getRepartePrevisto() == null 
				? BigInteger.ZERO : dto.getRepartePrevisto();
		BigInteger repartePromocional = dto.getRepartePromocional() == null 
				? BigInteger.ZERO : dto.getRepartePromocional();
		lancamento.setReparte(repartePrevisto);
		lancamento.setRepartePromocional(repartePromocional);
		
		if (produtoEdicao.getLancamentos().isEmpty()) {
			lancamento.setDataLancamentoDistribuidor(dto.getDataLancamento() == null 
					? dto.getDataLancamentoPrevisto() : dto.getDataLancamento());	// Data Lançamento Real;
			lancamento.setDataRecolhimentoDistribuidor(dto.getDataRecolhimentoDistribuidor());

			lancamento.setStatus(StatusLancamento.PLANEJADO);
			lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);

			Date dtSysdate = new Date();
			lancamento.setDataCriacao(dtSysdate);
			lancamento.setDataStatus(dtSysdate);
			
			lancamento.setProdutoEdicao(produtoEdicao);
	
			lancamentoRepository.adicionar(lancamento);
			produtoEdicao.getLancamentos().add(lancamento);
		} else {			
			if(lancamento.getStatus() == StatusLancamento.EXCLUIDO){
				lancamento.setStatus(StatusLancamento.PLANEJADO);
			}
			lancamentoRepository.alterar(lancamento);
		}
		
		produtoEdicaoRepository.alterar(produtoEdicao);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void excluirProdutoEdicao(Long idProdutoEdicao) throws UniqueConstraintViolationException {
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		if (produtoEdicao == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Por favor, selecione uma Edição existente!");
		}

		Set<Lancamento> lancamentos = produtoEdicao.getLancamentos();
		
		for (Lancamento lancamento : lancamentos) {
			
			if (!(lancamento.getStatus().equals(StatusLancamento.PLANEJADO)
					|| lancamento.getStatus().equals(StatusLancamento.CONFIRMADO))) {
				
				throw new ValidacaoException(TipoMensagem.ERROR, "Esta Edição não pode ser excluida por ter lancamentos em balanceamento ou já balanceados!");
			}
		}

		try {
			
			for (Lancamento lancamento : lancamentos){
				
				lancamento.setStatus(StatusLancamento.CANCELADO);
				
				this.lancamentoRepository.alterar(lancamento);
			}
			
			produtoEdicao.setAtivo(false);
			
			this.produtoEdicaoRepository.alterar(produtoEdicao);

		} catch (DataIntegrityViolationException e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Esta Edição não pode ser excluida por estar associada em outras partes do sistema!");
			
		} catch (Exception e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Ocorreu um erro ao tentar excluir a edição!");
		}
	}
	
	@Transactional(readOnly = true)
	@Override
	public ProdutoEdicao buscarProdutoPorCodigoBarras(String codigoBarras){
		
		return produtoEdicaoRepository.obterProdutoEdicaoPorCodigoBarra(codigoBarras);
	}

	@Transactional(readOnly = true)
	@Override
	public ProdutoEdicaoDTO obterProdutoEdicaoDTO(String codigoProduto, String idProdutoEdicao) {
		Produto produto = pService.obterProdutoPorCodigo(codigoProduto);

		ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
		dto.setNomeProduto(produto.getNome());
		dto.setCodigoProduto(produto.getCodigo());
		
		String nomeFornecedor = "";
		if (produto.getFornecedor() != null 
				&& produto.getFornecedor().getJuridica() != null) {
			nomeFornecedor = produto.getFornecedor().getJuridica().getNomeFantasia();
		}
		
		dto.setNomeFornecedor(nomeFornecedor);
		dto.setFase(produto.getFase());
		dto.setPacotePadrao(produto.getPacotePadrao());
		dto.setPeso(produto.getPeso());
		dto.setDescricaoDesconto("");
		dto.setNomeComercial(produto.getNomeComercial());
		dto.setDesconto(produto.getDescontoLogistica() == null 
				? BigDecimal.ZERO : BigDecimal.valueOf(
						produto.getDescontoLogistica().getPercentualDesconto()));

		if (idProdutoEdicao != null && Util.isLong(idProdutoEdicao)) {

			Long id = Long.valueOf(idProdutoEdicao);
			ProdutoEdicao pe = this.obterProdutoEdicao(id, false);

			dto.setId(id);
			
			dto.setNomeComercialProduto(pe.getNomeComercial());
			dto.setNumeroEdicao(pe.getNumeroEdicao());
			dto.setPacotePadrao(pe.getPacotePadrao());
			dto.setPrecoPrevisto(pe.getPrecoPrevisto());
			BigDecimal precoVenda = pe.getPrecoVenda();
            dto.setPrecoVenda(precoVenda);
			dto.setExpectativaVenda(pe.getExpectativaVenda());
			dto.setCodigoDeBarras(pe.getCodigoDeBarras());
			dto.setCodigoDeBarrasCorporativo(pe.getCodigoDeBarraCorporativo());
			dto.setChamadaCapa(pe.getChamadaCapa());
			dto.setParcial(pe.isParcial());
			dto.setPossuiBrinde(pe.isPossuiBrinde());
			
			BigDecimal percentualDesconto = Util.nvl(pe.getProduto().getDesconto(), BigDecimal.ZERO);
			BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, percentualDesconto);
			dto.setDesconto(valorDesconto);

			dto.setPeso(pe.getPeso());
			dto.setBoletimInformativo(pe.getBoletimInformativo());
			dto.setOrigemInterface(pe.getOrigemInterface());
			dto.setNumeroLancamento(pe.getNumeroLancamento());
			dto.setPeb(pe.getPeb());
			dto.setEditor(pe.getProduto().getEditor().getPessoaJuridica().getNome());
			if (pe.getBrinde() !=null) {
				dto.setDescricaoBrinde(pe.getBrinde().getDescricao());
			}
			Dimensao dimEdicao = pe.getDimensao();
			if (dimEdicao == null) {
				dto.setComprimento(0);
				dto.setEspessura(0);
				dto.setLargura(0);
			} else {
				dto.setComprimento(dimEdicao.getComprimento());
				dto.setEspessura(dimEdicao.getEspessura());
				dto.setLargura(dimEdicao.getLargura());
			}
			
			Lancamento uLancamento = lService.obterUltimoLancamentoDaEdicao(pe.getId());
			if (uLancamento != null) {
				dto.setSituacaoLancamento(uLancamento.getStatus());
				dto.setTipoLancamento(uLancamento.getTipoLancamento());
				if(uLancamento.getStatus() != StatusLancamento.EXCLUIDO){					
					dto.setDataLancamento(uLancamento.getDataLancamentoDistribuidor());
				}				
				dto.setDataLancamentoPrevisto(uLancamento.getDataLancamentoPrevista());
				dto.setRepartePrevisto(uLancamento.getReparte());
				dto.setRepartePromocional(uLancamento.getRepartePromocional());
				dto.setDataRecolhimentoPrevisto(uLancamento.getDataRecolhimentoPrevista());
				dto.setDataRecolhimentoReal(uLancamento.getDataRecolhimentoDistribuidor());
				dto.setSemanaRecolhimento(DateUtil.obterNumeroSemanaNoAno(uLancamento.getDataRecolhimentoDistribuidor()));
			}
		} else {
			
			// Edição criada pelo Distribuidor:
			dto.setOrigemInterface(false);
			
			dto.setPeb(produto.getPeb());
			
			Long ultimaEdicao = produtoEdicaoRepository.obterUltimoNumeroEdicao(codigoProduto);
			
			if (ultimaEdicao == null) {
				dto.setNumeroEdicao(1L);
			} else {
				dto.setNumeroEdicao(ultimaEdicao + 1);
			}
		}
		
		/* 
		 * Regra: Se não houver edições já cadatradas para este produto, deve-se
		 * obrigar a cadastrar o número 1. 
		 */
		
		Long qtdEdicoes = this.countPesquisarEdicoes(codigoProduto, null, null, null, null, null, false);
		if (qtdEdicoes == 0 || Long.valueOf(0).equals(qtdEdicoes)) {
			dto.setNumeroEdicao(1L);
		}
		
		return dto;
	}
	
}
