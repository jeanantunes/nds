package br.com.abril.nds.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.abril.nds.service.ProdutoEdicaoService;
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
				"codigoProduto", 0, 5);
	}
	
	@Override
	@Transactional
	public void salvarProdutoEdicao(ProdutoEdicaoDTO dto, String codigoProduto, String contentType, InputStream imgInputStream) {
		
		// TODO: REFACTORING: após testar e validar, separa em métodos privados
		// o save para ProdutoEdicao e Lancamento (melhorar a legibilidade do código).
		
		ProdutoEdicao produtoEdicao = null;
		Lancamento lancamento = 
				//null;
				new Lancamento();
		if (dto.getId() == null) {
			
			// Novo ProdutoEdicao - create:
			produtoEdicao = new ProdutoEdicao();
			produtoEdicao.setProduto(produtoRepository.obterProdutoPorCodigo(codigoProduto));
			
			/*
			 * TODO: REMOVER POSTERIORMENTE
			lancamento = new Lancamento();
			produtoEdicao.getLancamentos().add(lancamento);
			 */
		} else {
			
			// ProdutoEdicao existente - update:
			produtoEdicao = produtoEdicaoRepository.buscarPorId(dto.getId());
			
			/*
			 * TODO: REMOVER POSTERIORMENTE
			lancamento = lancamentoRepository.obterUltimoLancamentoDaEdicao(produtoEdicao.getId());
			 */
			
			/*
			 * Regra: Os campos abaixos só podem ser alterados caso a Edição
			 * ainda não tenha sido publicado pelo distribuidor:
			 * - Código da Edição;
			 * - Número da Edição;
			 * 
			 * Alteração: "Data de Lançamento do Distribuidor" > "Data 'de hoje'"
			 */
			if (!produtoEdicaoRepository.isProdutoEdicaoJaPublicada(produtoEdicao.getId())) {
				
				// Campo: Código do ProdutoEdicao:
				if (!produtoEdicao.getCodigo().equals(dto.getCodigoProduto())) {
					throw new ValidacaoException(TipoMensagem.ERROR, "Não é permitido alterar o código de uma Edição já publicada!");
				}

				// Campo: Número do ProdutoEdicao:
				if (!produtoEdicao.getNumeroEdicao().equals(dto.getNumeroEdicao())) {
					throw new ValidacaoException(TipoMensagem.ERROR, "Não é permitido alterar o número de uma Edição já publicada!");
				}
			}
		}		
		
		
		/* Regra: Se não existir nenhuma edição associada ao produto, salvar n. 1 */
		if (!this.produtoEdicaoRepository.hasProdutoEdicao(produtoEdicao.getProduto())) {
			produtoEdicao.setNumeroEdicao(Long.valueOf(1));
		}
		
		// Campos a serem persistidos e/ou alterados:
		
		// Identificação:
		produtoEdicao.setCodigo(dto.getCodigoProduto());	// View: Codigo da Edição;
		produtoEdicao.setNomeComercial(dto.getNomeComercialProduto());
		produtoEdicao.setNumeroEdicao(dto.getNumeroEdicao());
		produtoEdicao.setPacotePadrao(dto.getPacotePadrao());
		lancamento.setTipoLancamento(dto.getTipoLancamento());
		
		// Preço de capa:
		produtoEdicao.setPrecoPrevisto(dto.getPrecoPrevisto());
		
		// Data lançamento:
		lancamento.setDataLancamentoPrevista(dto.getDataLancamentoPrevisto());
		lancamento.setDataLancamentoDistribuidor(dto.getDataLancamento());	// Data Lançamento Real;
		
		// Reparte:
		BigDecimal repartePrevisto = dto.getRepartePrevisto();
		BigDecimal repartePromocional = dto.getRepartePromocional();
		lancamento.setReparte(repartePrevisto);
		produtoEdicao.setReparteDistribuido(repartePrevisto.add(repartePromocional));
		lancamento.setRepartePromocional(repartePromocional);
		
		// Características do lançamento:
		// TODO: !!!colocar o select da categoria aqui!!!
		produtoEdicao.setCodigoDeBarras(dto.getCodigoDeBarras());
		produtoEdicao.setCodigoDeBarraCorporativo(dto.getCodigoDeBarrasCorporativo());
		
		// Tipos de desconto:
		produtoEdicao.setDesconto(dto.getDesconto());
		
		// Outros:
		produtoEdicao.setChamadaCapa(dto.getChamadaCapa());
		produtoEdicao.setParcial(dto.isParcial());	// Regime de Recolhimento;
		produtoEdicao.setPossuiBrinde(dto.isPossuiBrinde());
		
		// Característica Física:
		produtoEdicao.setPeso(dto.getPeso());
		Dimensao dimEdicao = new Dimensao();
		dimEdicao.setLargura(dto.getLargura());
		dimEdicao.setComprimento(dto.getComprimento());
		dimEdicao.setEspessura(dto.getEspessura());
		produtoEdicao.setDimensao(dimEdicao);
		
		// Texto boletim informativo:
		produtoEdicao.setBoletimInformativo(dto.getBoletimInformativo());
		
		
		Dimensao d = new Dimensao();
		d.setLargura(dto.getLargura());
		d.setComprimento(dto.getComprimento());
		d.setEspessura(dto.getEspessura());
		
		if (produtoEdicao.getId() == null) {
			
			// Campos a serem persistidos:
			// Preço de capa:
			produtoEdicao.setPrecoVenda(dto.getPrecoVenda());	// View: Preço real;
			
			// Salvar:
			produtoEdicaoRepository.adicionar(produtoEdicao);
		} else {
			
			// Atualizar:
			produtoEdicaoRepository.alterar(produtoEdicao);
		}
		
		/*
		 * TODO: Se na alteração de uma Edição, alguns dos dados do lançamento
		 * for alterado, qual o procedimento a ser tomado?
		 * - Editar o lançamento mais recente (o que foi exibido na tela para o usuário)?
		 * - Criar um novo lançamento?
		 * 
		 * Caso seja criar um lancamento novo, alterar todas as regras de 
		 * criação e persistencia
		 * (irá tornar este código menor).
		 */
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
		lancamentoRepository.adicionar(lancamento);
		
		// Atualizar:
		produtoEdicao.getLancamentos().add(lancamento);
		produtoEdicaoRepository.alterar(produtoEdicao);
		
		
		// Salvar imagem:
		if (imgInputStream != null) {
			capaService.saveCapa(produtoEdicao.getId(), contentType, imgInputStream);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public void excluirProdutoEdicao(Long idProdutoEdicao) {
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		if (produtoEdicao == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Por favor, selecione uma Edição existente!");
		}
		
		/* Regra: Se a Edição for originária da Interface, não pode ser excluida! */
		if (produtoEdicao.getOrigemInterface()) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Esta Edição não pode ser excluida por ser originária da INTERFACE!");
		}
		
		// TODO: 
		/* Regra: Não excluir se existir referencias desta edição em outras tabelas. */
		//throw new ValidacaoException(TipoMensagem.ERROR, "Esta Edição não pode ser excluida por estar associada em outras partes do sistema!");
		
		
		produtoEdicaoRepository.remover(produtoEdicao);
	}
	
	
	/**
	 * Transforma o valor em formato String para BigDecimal.<br>
	 * Caso o valor não for válido, irá retornar 0 (zero).
	 * 
	 * @param valor
	 * @return
	 */
	private BigDecimal converterValor(String valor) {
		
		BigDecimal nValor = null;
		try {
			nValor = (new BigDecimal(valor)).setScale(2);
		} catch (Exception e) {
			nValor = BigDecimal.ZERO.setScale(2);
		}
		
		return nValor;
	}
	
}
