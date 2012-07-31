package br.com.abril.nds.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Dimensao;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;

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
	private MatrizLancamentoService matrizLancamentoService;
	
	@Override
	@Transactional(readOnly = true)
	public ProdutoEdicao obterProdutoEdicao(Long idProdutoEdicao) {
		
		if (idProdutoEdicao == null || Long.valueOf(0).equals(idProdutoEdicao)) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, 
					"Código de identificação da Edição é inválida!"));
		}
		
		return produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
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
	public List<ProdutoEdicaoDTO> pesquisarEdicoes(ProdutoEdicaoDTO dto,
			String sortorder, String sortname, int page, int maxResults) {
		
		final int initialResult = ((page * maxResults) - maxResults);
		return this.produtoEdicaoRepository.pesquisarEdicoes(dto, sortorder,
				sortname, initialResult, maxResults);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long countPesquisarEdicoes(ProdutoEdicaoDTO dto) {
		
		return this.produtoEdicaoRepository.countPesquisarEdicoes(dto);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ProdutoEdicaoDTO> pesquisarUltimasEdicoes(ProdutoEdicaoDTO dto,
			int maxResults) {
		
		return this.produtoEdicaoRepository.pesquisarEdicoes(dto, "DESC",
				"numeroEdicao", 0, 5);
	}
	
	@Override
	@Transactional
	public void salvarProdutoEdicao(ProdutoEdicaoDTO dto, String codigoProduto, 
			String contentType, InputStream imgInputStream) {
		
		ProdutoEdicao produtoEdicao = null;
		if (dto.getId() == null) {
			
			// Novo ProdutoEdicao - create:
			produtoEdicao = new ProdutoEdicao();
			produtoEdicao.setProduto(produtoRepository.obterProdutoPorCodigo(codigoProduto));
			produtoEdicao.setOrigemInterface(Boolean.FALSE);
		} else {
			
			// ProdutoEdicao existente - update:
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
			
			// Campo: Código do ProdutoEdicao:
			if (!produtoEdicao.getCodigo().equals(dto.getCodigoProduto())) {
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
		
		BigDecimal repartePrevisto = dto.getRepartePrevisto() == null 
				? BigDecimal.ZERO : dto.getRepartePrevisto();
		BigDecimal repartePromocional = dto.getRepartePromocional() == null 
				? BigDecimal.ZERO : dto.getRepartePromocional();
		if (!produtoEdicao.getOrigemInterface().booleanValue()) {
			// Campos exclusivos para o Distribuidor::
			
			// Identificação:
			produtoEdicao.setCodigo(dto.getCodigoProduto());	// View: Codigo da Edição;
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
			
			// Tipos de desconto:
			produtoEdicao.setDesconto(dto.getDesconto());
			
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
		if (produtoEdicao.getOrigemInterface().booleanValue()) {
			return;
		}
		
		
		Lancamento lancamento = new Lancamento();
		
		// Identificação:
		lancamento.setTipoLancamento(dto.getTipoLancamento());
		
		// Data lançamento:
		lancamento.setDataLancamentoPrevista(dto.getDataLancamentoPrevisto());
		lancamento.setDataLancamentoDistribuidor(dto.getDataLancamento() == null 
				? dto.getDataLancamentoPrevisto() : dto.getDataLancamento());	// Data Lançamento Real;
		
		// Reparte:
		BigDecimal repartePrevisto = dto.getRepartePrevisto() == null 
				? BigDecimal.ZERO : dto.getRepartePrevisto();
		BigDecimal repartePromocional = dto.getRepartePromocional() == null 
				? BigDecimal.ZERO : dto.getRepartePromocional();
		lancamento.setReparte(repartePrevisto);
		lancamento.setRepartePromocional(repartePromocional);
		
		// Cálcular as datas de Recolhimento:
		int peb = produtoEdicao.getProduto().getPeb();
		Calendar calPeb = Calendar.getInstance();
		calPeb.setTime(lancamento.getDataLancamentoPrevista());
		calPeb.add(Calendar.DAY_OF_MONTH, peb);
		Date dtPeb = calPeb.getTime();
		lancamento.setDataRecolhimentoDistribuidor(dtPeb);
		lancamento.setDataRecolhimentoPrevista(dtPeb);
		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setStatus(StatusLancamento.PLANEJADO);
		lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
		
		Date dtSysdate = new Date();
		lancamento.setDataCriacao(dtSysdate);
		lancamento.setDataStatus(dtSysdate);
		
		// Salvar:
		lancamentoRepository.adicionar(lancamento);
		
		// Atualizar:
		produtoEdicao.getLancamentos().add(lancamento);
		produtoEdicaoRepository.alterar(produtoEdicao);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void excluirProdutoEdicao(Long idProdutoEdicao) throws UniqueConstraintViolationException {
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		if (produtoEdicao == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Por favor, selecione uma Edição existente!");
		}

		List<Long> idsLancamento = new ArrayList<Long>();
		
		for (Lancamento lancamento : produtoEdicao.getLancamentos()) {
			
			if (!(lancamento.getStatus().equals(StatusLancamento.PLANEJADO)
					|| lancamento.getStatus().equals(StatusLancamento.CONFIRMADO))) {
				
				throw new ValidacaoException(TipoMensagem.ERROR, "Esta Edição não pode ser excluida por ter lancamentos em balanceamento ou já balanceados!");
			}
			
			idsLancamento.add(lancamento.getId());
		}

		try {

			if (!idsLancamento.isEmpty()) {

				int arraySize = idsLancamento.size();
				
				Long[] ids = idsLancamento.toArray(new Long[arraySize]);
				
				this.lancamentoRepository.removerPorId(ids);
			}

			this.produtoEdicaoRepository.remover(produtoEdicao);

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
	
}
