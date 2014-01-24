package br.com.abril.nds.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliseHistogramaDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO.ModoTela;
import br.com.abril.nds.dto.filtro.FiltroHistogramaVendas;
import br.com.abril.nds.dto.filtro.FiltroHistoricoVendaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Brinde;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.Dimensao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SegmentacaoProduto;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BrindeRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.LancamentoParcialRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.ProdutoEdicao}
 * 
 * @author Discover Technology
 */
@Service
public class ProdutoEdicaoServiceImpl implements ProdutoEdicaoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoEdicaoServiceImpl.class);
	
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
	private BrindeRepository brindeRepository;
	
	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private DescontoProdutoEdicaoRepository descontoProdutoEdicaoRepository;
	
	@Autowired
	private ProdutoService pService;
	
	@Autowired
	private LancamentoService lService;	
	
	@Autowired
	private UsuarioService usuarioService;	
	
	@Autowired
	private DistribuidorService distribuidorService;	
	
	@Autowired
	private ParciaisService parciaisService;
	
	@Autowired
	private LancamentoParcialRepository lancamentoParcialRepository;

	@Autowired
	private PeriodoLancamentoParcialRepository periodoLancamentoParcialRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;
	
	@Value("${data_cabalistica}")
	private String dataCabalistica;
	
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
			String codigo, String nomeProduto, Long edicao, Date dataLancamento, boolean furado) {
		
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
						codigo, nomeProduto, edicao, dataLancamento, furado);
		
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
				Calendar dataOperacao = Calendar.getInstance();
				dataOperacao.setTime(distribuidorService.obterDataOperacaoDistribuidor());
				
				if(calendar.before(dataOperacao)){
					calendar = dataOperacao;
				}
				
				
			} catch (ParseException e) {
				return furoProdutoDTO;
			}
			
			List<Integer> listaDiasSemana = 
					this.distribuicaoFornecedorRepository.obterDiasSemanaDistribuicao(
							furoProdutoDTO.getCodigoProduto(), 
							furoProdutoDTO.getIdProdutoEdicao(), OperacaoDistribuidor.DISTRIBUICAO);
			
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
				StringUtils.leftPad(codigoProduto, 8, '0'), Long.parseLong(numeroEdicao));
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
	public List<ProdutoEdicao> obterProdutoPorCodigoNomeParaRecolhimento(String codigoNomeProduto, 
														 				 Integer numeroCota, 
														 				 Integer quantidadeRegistros) {
		
		if (codigoNomeProduto == null || codigoNomeProduto.trim().isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Codigo/nome produto é obrigatório.");
		}
		
		List<ProdutoEdicao> produtosEdicao = this.produtoEdicaoRepository.obterProdutoPorCodigoNome(
			codigoNomeProduto, numeroCota, quantidadeRegistros);
		
		List<ProdutoEdicao> produtosEdicaoValidos = new ArrayList<>();
		
		Date dataOperacaoDistribuidor = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		for (ProdutoEdicao produtoEdicao : produtosEdicao) {
			
			try {
				
				this.conferenciaEncalheService.isDataRecolhimentoValida(
					dataOperacaoDistribuidor, dataOperacaoDistribuidor, produtoEdicao.getId(), 
					conferenciaEncalheService.isCotaOperacaoDiferenciada(numeroCota));
				
				produtosEdicaoValidos.add(produtoEdicao);
				
			} catch (ValidacaoException e) {
				
				continue;
			}
		}
		
		return produtosEdicaoValidos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProdutoEdicaoDTO> pesquisarEdicoes(String codigoProduto, String nome,
			Intervalo<Date> dataLancamento, Intervalo<BigDecimal> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde,
			String sortorder, String sortname, int page, int maxResults) {
		
		final int initialResult = ((page * maxResults) - maxResults);
		
		List<ProdutoEdicaoDTO> edicoes = this.produtoEdicaoRepository.pesquisarEdicoes(codigoProduto, nome, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde, sortorder, sortname, initialResult, maxResults);
		
		return edicoes;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Integer countPesquisarEdicoes(String codigoProduto, String nome,
			Intervalo<Date> dataLancamento, Intervalo<BigDecimal> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde) {
		
		return this.produtoEdicaoRepository.countPesquisarEdicoes(codigoProduto, nome, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde);
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
			String contentType, InputStream imgInputStream, boolean istrac29) {
		
		ProdutoEdicao produtoEdicao = null;
		
		boolean indNovoProdutoEdicao = (dto.getId() == null);
		
		if (indNovoProdutoEdicao) {
			produtoEdicao = new ProdutoEdicao();
			produtoEdicao.setProduto(produtoRepository.obterProdutoPorCodigo(codigoProduto));
			produtoEdicao.setOrigem(Origem.MANUAL);
		} else {
			produtoEdicao = produtoEdicaoRepository.buscarPorId(dto.getId());
		}		
		
		
		
		// 01 ) Salvar/Atualizar o ProdutoEdicao:
		this.salvarProdutoEdicao(dto, produtoEdicao);
		
		
		// 02) Salvar imagem:
		if (imgInputStream != null) {
			
			// Verifica se o tipo do arquivo é imagem JPEG, PNG ou GIF:
			if(!FileType.JPEG.getContentType().equalsIgnoreCase(contentType) && 
					   !FileType.GIF.getContentType().equalsIgnoreCase(contentType)  && 
					   !FileType.PNG.getContentType().equalsIgnoreCase(contentType)) {
				throw new ValidacaoException(TipoMensagem.WARNING, 
						"O formato da imagem da capa não é válido!");
			}
			
			capaService.saveCapa(produtoEdicao.getId(), contentType, imgInputStream);
		}
		
		if ((!produtoEdicao.getOrigem().equals(br.com.abril.nds.model.Origem.INTERFACE)
				|| dto.getModoTela().equals(ModoTela.REDISTRIBUICAO)) ||  istrac29) {
		
			Usuario usuario = usuarioService.getUsuarioLogado();
			
			if(TipoLancamento.PARCIAL.equals(dto.getTipoLancamento())) {							
				this.salvarLancamentoParcial(dto, produtoEdicao,indNovoProdutoEdicao, usuario);
			} else {
				this.salvarLancamento(dto, produtoEdicao,indNovoProdutoEdicao, usuario);
			}
		}
		
		this.inserirDescontoProdutoEdicao(produtoEdicao, indNovoProdutoEdicao);
		
	}
	
	private void salvarLancamentoParcial(ProdutoEdicaoDTO dto,
			ProdutoEdicao produtoEdicao, boolean indNovoProdutoEdicao, Usuario usuario) {
		
		if(!indNovoProdutoEdicao) {
			for(Lancamento lancamento : produtoEdicao.getLancamentos() ) {
				if(!TipoLancamento.PARCIAL.equals(lancamento.getTipoLancamento()))
					lancamentoRepository.remover(lancamento);
			}
		}
		
		LancamentoParcial lancamentoParcial  = produtoEdicao.getLancamentoParcial();
		
		if ( lancamentoParcial == null ) {
			
			lancamentoParcial = new LancamentoParcial();
			
			lancamentoParcial.setProdutoEdicao(produtoEdicao);
			lancamentoParcial.setStatus(StatusLancamentoParcial.PROJETADO);		
		}
		else{
			
			if(lancamentoParcial.getPeriodos()!= null && !lancamentoParcial.getPeriodos().isEmpty()){
				
				if(lancamentoParcial.getPeriodos().size()>1){
					
					this.validarPeriodoLancamentoParcial(dto, produtoEdicao);
				}
				else{
					
					this.alterarPeriodoLancamentoParcial(dto, lancamentoParcial.getPeriodos().get(0), usuario);
				}
			}
		}
		
		lancamentoParcial.setLancamentoInicial(dto.getDataLancamentoPrevisto());
		lancamentoParcial.setRecolhimentoFinal(dto.getDataRecolhimentoPrevisto());
		
		lancamentoParcialRepository.merge(lancamentoParcial);
		
		if(lancamentoParcial.getPeriodos().isEmpty())
			parciaisService.gerarPeriodosParcias(produtoEdicao, 1, usuario);
		
		Lancamento periodo = lancamentoRepository.obterUltimoLancamentoDaEdicao(produtoEdicao.getId());
		
		periodo.setReparte(dto.getRepartePrevisto());
		periodo.setRepartePromocional(dto.getRepartePromocional());
		periodo.setUsuario(usuario);
		
		lancamentoRepository.merge(periodo);
	}

	private void alterarPeriodoLancamentoParcial(ProdutoEdicaoDTO dto,PeriodoLancamentoParcial periodoLancamentoParcial, Usuario usuario) {
		
		if(dto.getDataLancamentoPrevisto().compareTo(dto.getDataRecolhimentoPrevisto())>0){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Data lançamento previsto deve ser maior que a data recolhimento previsto.");
		}
		
		Lancamento lancamento = periodoLancamentoParcial.getLancamento();
		
		lancamento.setDataLancamentoDistribuidor(dto.getDataLancamentoPrevisto());
		lancamento.setDataLancamentoPrevista(dto.getDataLancamentoPrevisto());
		lancamento.setDataRecolhimentoDistribuidor(dto.getDataRecolhimentoPrevisto());
		lancamento.setDataRecolhimentoPrevista(dto.getDataRecolhimentoPrevisto());
		lancamento.setUsuario(usuario);
		
		lancamentoRepository.merge(lancamento);
	}

	private void validarPeriodoLancamentoParcial(ProdutoEdicaoDTO dto,ProdutoEdicao produtoEdicao) {
		
		PeriodoLancamentoParcial periodoInicial = periodoLancamentoParcialRepository.obterPrimeiroLancamentoParcial(produtoEdicao.getId());
		
		if(periodoInicial!= null && periodoInicial.getLancamento()!= null){
			
			if(periodoInicial.getLancamento().getDataLancamentoDistribuidor().compareTo(dto.getDataLancamentoPrevisto())<0){
				throw new ValidacaoException(TipoMensagem.WARNING,"Data lançamento previsto deve ser menor que a data de lançamento real.");
			}
		}
		
		PeriodoLancamentoParcial periodoFinal = periodoLancamentoParcialRepository.obterUltimoLancamentoParcial(produtoEdicao.getId());
		
		if(periodoFinal!= null && periodoFinal.getLancamento()!= null){
			
			if(periodoInicial.getLancamento().getDataRecolhimentoDistribuidor().compareTo(dto.getDataRecolhimentoPrevisto())>0){
				throw new ValidacaoException(TipoMensagem.WARNING,"Data recolhimento previsto deve ser menor que a data de recolhimento real.");
			}
		}
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
		/*VALIDAÇÃO FEITA POR EDICAO + LCTO*/
		//if (this.produtoEdicaoRepository.isNumeroEdicaoCadastrada(
				//dto.getCodigoProduto(), dto.getNumeroEdicao(), produtoEdicao.getId())) {
			//throw new ValidacaoException(TipoMensagem.ERROR, "Este número de edição já esta cadastrada para outra Edição!");
		//}
		
		
		
		/* Regra: Não deve existir duas Edições com o mesmo código de barra. */
		// Nota: Conforme conversado com o Cesar e Paulo Bacherini em 05/11/2012, dois produtos diferentes podem sim ter o mesmo código de barras
		/*List<ProdutoEdicao> lstPeCodBarra = 
				this.produtoEdicaoRepository.obterProdutoEdicaoPorCodigoDeBarra(
						dto.getCodigoDeBarras(), produtoEdicao.getId());
		if (lstPeCodBarra != null && !lstPeCodBarra.isEmpty()) {
		    // Nota: Caso exista, mas não se trate do MESMO produto edição
			for (ProdutoEdicao produtoEdicaoPorCodigoBarra : lstPeCodBarra) {
			
				if (produtoEdicaoPorCodigoBarra.getId() != produtoEdicao.getId()) {
				
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
		}*/
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
		
		if ((produtoEdicao.getOrigem().equals(br.com.abril.nds.model.Origem.MANUAL))) {
			// Campos exclusivos para o Distribuidor::
			
			// Identificação:
			produtoEdicao.setNumeroEdicao(dto.getNumeroEdicao());
			produtoEdicao.setNomeComercial(dto.getNomeComercialProduto());
			produtoEdicao.setCaracteristicaProduto(dto.getCaracteristicaProduto());
			produtoEdicao.setPrecoPrevisto(dto.getPrecoPrevisto());
			produtoEdicao.setPeb(dto.getPeb());	
			produtoEdicao.setGrupoProduto(dto.getGrupoProduto());
			
			// Reparte:
			produtoEdicao.setReparteDistribuido(repartePrevisto.add(repartePromocional));
				
			// Características do lançamento:
			// TODO: !!!colocar o select da categoria aqui!!!
			produtoEdicao.setCodigoDeBarraCorporativo(dto.getCodigoDeBarrasCorporativo());
				
			// Outros:
			produtoEdicao.setParcial(TipoLancamento.PARCIAL.equals(dto.getTipoLancamento()));	// Regime de Recolhimento;
				
			// Característica Física:
			produtoEdicao.setPeso(dto.getPeso());
			Dimensao dimEdicao = new Dimensao();
			dimEdicao.setLargura(dto.getLargura());
			dimEdicao.setComprimento(dto.getComprimento());
			dimEdicao.setEspessura(dto.getEspessura());
			produtoEdicao.setDimensao(dimEdicao);
				
			// Texto boletim informativo:
			produtoEdicao.setBoletimInformativo(dto.getBoletimInformativo());
			
			//Desconto Fornecedor x Distribuidor
			produtoEdicao.setDescricaoDesconto(dto.getDescricaoDesconto());
			produtoEdicao.setDesconto(dto.getDesconto());
			
			//Segmentação
			SegmentacaoProduto segm = produtoEdicao.getSegmentacao()!=null?produtoEdicao.getSegmentacao():new SegmentacaoProduto();
			segm.setClasseSocial(dto.getClasseSocial());
			segm.setSexo(dto.getSexo());
			segm.setFaixaEtaria(dto.getFaixaEtaria());
			segm.setTemaPrincipal(dto.getTemaPrincipal());
			segm.setTemaSecundario(dto.getTemaSecundario());
			
			produtoEdicao.setSegmentacao(segm);
			
		}
		
		//Campos editáveis, independente da Origem
		produtoEdicao.setPrecoVenda(dto.getPrecoVenda()); // View: Preço Capa - Real;
		produtoEdicao.setCodigoDeBarras(dto.getCodigoDeBarras());
		produtoEdicao.setChamadaCapa(dto.getChamadaCapa());
		produtoEdicao.setPeso(dto.getPeso());
		produtoEdicao.setPossuiBrinde(false);
		produtoEdicao.setBrinde(null);
		produtoEdicao.setPacotePadrao(dto.getPacotePadrao());
		
		if(dto.getIdBrinde()!=null && dto.isPossuiBrinde()){
			Brinde brinde = brindeRepository.buscarPorId(dto.getIdBrinde());
	        if (brinde!=null){ 
	        	produtoEdicao.setPossuiBrinde(true);
		        produtoEdicao.setBrinde(brinde);
	        }
		}
		
		if (produtoEdicao.getId() == null) {			
			// Salvar:
			
			Produto produto = this.produtoRepository.obterProdutoPorCodigo(dto.getCodigoProduto());
			
			produtoEdicao.setProduto(produto);
			
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
	 * @param indNovoProdutoEdicao 
	 * @param usuario 
	 */
	private void salvarLancamento(ProdutoEdicaoDTO dto, ProdutoEdicao produtoEdicao, boolean indNovoProdutoEdicao, Usuario usuario) {
		
		if(!indNovoProdutoEdicao && produtoEdicao.getLancamentoParcial() != null) {
			
			lancamentoParcialRepository.remover(produtoEdicao.getLancamentoParcial());			
		}
		
		Lancamento lancamento = null;
		if (produtoEdicao.getLancamentos().isEmpty() || ModoTela.REDISTRIBUICAO.equals(dto.getModoTela())) {
			lancamento = new Lancamento();
		} else {
			for (Lancamento lancto: produtoEdicao.getLancamentos()) {
				if (lancamento == null 
						|| DateUtil.isDataInicialMaiorDataFinal(lancto.getDataLancamentoDistribuidor(), lancamento.getDataLancamentoDistribuidor())) {
					lancamento = lancto;
				}
			}
		}
		
		lancamento.setNumeroLancamento(dto.getNumeroLancamento());
		lancamento.setTipoLancamento(dto.getTipoLancamento());
		
		if(dto.getDataLancamento()!=null){
		 lancamento.setDataLancamentoDistribuidor(dto.getDataLancamento());
		}
		lancamento.setDataLancamentoPrevista(dto.getDataLancamentoPrevisto());
		lancamento.setDataRecolhimentoPrevista(dto.getDataRecolhimentoPrevisto());
		
		BigInteger repartePrevisto = dto.getRepartePrevisto() == null 
				? BigInteger.ZERO : dto.getRepartePrevisto();
		BigInteger repartePromocional = dto.getRepartePromocional() == null 
				? BigInteger.ZERO : dto.getRepartePromocional();
		lancamento.setReparte(repartePrevisto);
		lancamento.setRepartePromocional(repartePromocional);
		lancamento.setUsuario(usuario);
		
		if (lancamento.getId() == null) {
			
			if (lancamento.getDataLancamentoDistribuidor() == null) {
				lancamento.setDataLancamentoDistribuidor(dto.getDataLancamentoPrevisto());
			}
			
			if (lancamento.getDataRecolhimentoDistribuidor() == null) {
				lancamento.setDataRecolhimentoDistribuidor(dto.getDataRecolhimentoPrevisto());
			}
			
			lancamento.setStatus(StatusLancamento.PLANEJADO);

			Date dtSysdate = new Date();
			lancamento.setDataCriacao(dtSysdate);
			lancamento.setDataStatus(dtSysdate);
			
			lancamento.setProdutoEdicao(produtoEdicao);
	
			lancamentoRepository.adicionar(lancamento);
			produtoEdicao.getLancamentos().add(lancamento);
		} else {			
			
			lancamentoRepository.alterar(lancamento);
		}
		
		produtoEdicaoRepository.alterar(produtoEdicao);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Map<String, String> isProdutoEdicaoValidoParaRemocao(Long idProdutoEdicao) throws Exception {

		Map<String, String> validacaoMap = new HashMap<String, String>();
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		if (produtoEdicao == null) {
			
			validacaoMap.put("edicaoInexistente", "Por favor, selecione uma Edição existente!");
		}
		
		Set<Lancamento> lancamentos = produtoEdicao.getLancamentos();
		
		for (Lancamento lancamento : lancamentos) {
									
			if (!(lancamento.getStatus().equals(StatusLancamento.PLANEJADO)
					|| lancamento.getStatus().equals(StatusLancamento.CONFIRMADO)
					|| lancamento.getStatus().equals(StatusLancamento.EM_BALANCEAMENTO)
					|| lancamento.getStatus().equals(StatusLancamento.FURO)) ) {
								
				validacaoMap.put("edicaoExpedida", "Produto balanceado, é necessário realizar o furo da publicação para realizar a exclusão!");
				
				return validacaoMap;
			}
			
			if(lancamento.getRecebimentos() != null && !lancamento.getRecebimentos().isEmpty() ) {
				
				validacaoMap.put("edicaoComNota", "Esta edição possui nota emitida e não pode ser excluida!");
				
				return validacaoMap;
			}
			
			if(lancamento.getStatus().equals(StatusLancamento.BALANCEADO))
				validacaoMap.put("edicaoEmBalanceamentoBalanceada", "Esta Edição possui lancamento já balanceado, é necessário realizar o Furo da Edição!");
						
			if(lancamento.getEstudo() != null) {
				validacaoMap.put("edicaoPossuiEstudo", "Esta edição já possui estudo!");
			}
			
			if(produtoEdicao.getEstoqueProduto() != null) {
				
				validacaoMap.put("edicaoPossuiEstoque", "Esta edição possui produtos em estoque e não pode ser excluida!");

				return validacaoMap;
			}
		}
		
		return validacaoMap;

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
			
			if(lancamento.getRecebimentos() != null && !lancamento.getRecebimentos().isEmpty() ) 
				throw new ValidacaoException(TipoMensagem.WARNING, "Esta edição possui nota emitida e não pode ser excluida!");
			
			
			if (!(lancamento.getStatus().equals(StatusLancamento.PLANEJADO)
					|| lancamento.getStatus().equals(StatusLancamento.CONFIRMADO)
					|| lancamento.getStatus().equals(StatusLancamento.EM_BALANCEAMENTO)
					|| lancamento.getStatus().equals(StatusLancamento.FURO)) ) {
								
				throw new ValidacaoException(TipoMensagem.WARNING, "Produto balanceado, é necessário realizar o furo da publicação para realizar a exclusão!");
				
			}
			
			if(lancamento.getStatus().equals(StatusLancamento.BALANCEADO))
				throw new ValidacaoException(TipoMensagem.WARNING, "Esta edição possui lancamento já balanceado, é necessário realizar o Furo da Edição!");
						
			if(lancamento.getEstudo() != null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Esta edição já possui estudo!");
			}
			
			if(produtoEdicao.getEstoqueProduto() != null) {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Esta edição possui produtos em estoque e não pode ser excluida!");
			}
		}
		
		try {
			
			for (Lancamento lancamento : lancamentos){				
				if (Origem.MANUAL.equals(produtoEdicao.getOrigem())) {
					
					this.lancamentoRepository.remover(lancamento);
				} else {
					
					lancamento.setStatus(StatusLancamento.CANCELADO);
					
					Usuario usuario = usuarioService.getUsuarioLogado();
					
					lancamento.setUsuario(usuario);
					
					if (lancamento.getPeriodoLancamentoParcial() != null) {

						lancamento.getPeriodoLancamentoParcial().setStatus(
								StatusLancamentoParcial.CANCELADO);
						periodoLancamentoParcialRepository.alterar(lancamento
								.getPeriodoLancamentoParcial());

						lancamento.getPeriodoLancamentoParcial()
								.getLancamentoParcial()
								.setStatus(StatusLancamentoParcial.CANCELADO);
						lancamentoParcialRepository.alterar(lancamento
								.getPeriodoLancamentoParcial()
								.getLancamentoParcial());
					}
					
					this.lancamentoRepository.alterar(lancamento);
				}
			}
			
			if (Origem.MANUAL.equals(produtoEdicao.getOrigem())) {
				
				if (produtoEdicao.getLancamentoParcial() != null) {
					this.lancamentoParcialRepository.remover(produtoEdicao
							.getLancamentoParcial());
				}
				this.produtoEdicaoRepository.remover(produtoEdicao);
				
			} else {
				
				produtoEdicao.setAtivo(false);
				this.produtoEdicaoRepository.alterar(produtoEdicao);
			}

		} catch (DataIntegrityViolationException e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Esta edição não pode ser excluida por estar associada em outras partes do sistema!");
			
		} catch (Exception e) {
			LOGGER.error("Ocorreu um erro ao tentar excluir a edição!", e);
			throw new ValidacaoException(TipoMensagem.ERROR, "Ocorreu um erro ao tentar excluir a edição!");
		}
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<ProdutoEdicao> buscarProdutoPorCodigoBarras(String codigoBarras){
		
		return produtoEdicaoRepository.obterProdutoEdicaoPorCodigoBarra(codigoBarras);
	}

	@Transactional(readOnly = true)
	@Override
	public ProdutoEdicaoDTO obterProdutoEdicaoDTO(String codigoProduto, String idProdutoEdicao, boolean redistribuicao, String situacaoProdutoEdicao) {
		Produto produto = pService.obterProdutoPorCodigo(codigoProduto);

		ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
		
		dto.setNomeProduto(produto.getNome());
		dto.setCodigoProduto(produto.getCodigo());
		dto.setFase(produto.getFase());
		dto.setPacotePadrao(produto.getPacotePadrao());
		dto.setPeso(produto.getPeso());

		String nomeFornecedor = "";
		if (produto.getFornecedor() != null 
				&& produto.getFornecedor().getJuridica() != null) {
			nomeFornecedor = produto.getFornecedor().getJuridica().getNomeFantasia();
		}
		
		dto.setNomeFornecedor(nomeFornecedor);
		
		if (idProdutoEdicao != null && Util.isLong(idProdutoEdicao)) {

			Long id = Long.valueOf(idProdutoEdicao);
			ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(id, false);
			
			dto.setId(id);
			dto.setNomeComercialProduto(produtoEdicao.getNomeComercial());
			dto.setCaracteristicaProduto(produtoEdicao.getCaracteristicaProduto());
			dto.setGrupoProduto(produtoEdicao.getGrupoProduto()!=null?produtoEdicao.getGrupoProduto():produto.getTipoProduto()!=null?produto.getTipoProduto().getGrupoProduto():null);
			dto.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			dto.setPacotePadrao(produtoEdicao.getPacotePadrao());
			dto.setPrecoPrevisto(produtoEdicao.getPrecoPrevisto());
			
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
            dto.setPrecoVenda(precoVenda);
			dto.setExpectativaVenda(produtoEdicao.getExpectativaVenda());
			dto.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
			dto.setCodigoDeBarrasCorporativo(produtoEdicao.getCodigoDeBarraCorporativo());
			dto.setChamadaCapa(produtoEdicao.getChamadaCapa());
			dto.setParcial(produtoEdicao.isParcial());
			dto.setPossuiBrinde(produtoEdicao.isPossuiBrinde());
			
			dto.setPeso(produtoEdicao.getPeso());
			dto.setBoletimInformativo(produtoEdicao.getBoletimInformativo());
			dto.setOrigemInterface(produtoEdicao.getOrigem().equals(br.com.abril.nds.model.Origem.INTERFACE));
			dto.setPeb(produtoEdicao.getPeb());
			dto.setEditor(produtoEdicao.getProduto().getEditor() != null ? produtoEdicao.getProduto().getEditor().getPessoaJuridica().getNome() : "");
			if (produtoEdicao.getBrinde() !=null) {
				dto.setDescricaoBrinde(produtoEdicao.getBrinde().getDescricao());
				dto.setIdBrinde(produtoEdicao.getBrinde().getId());
			}
			carregarInformacaoDimensaoProduto(dto, produtoEdicao);
			
			carregarInformacaoDesconto(produto, dto, produtoEdicao);
			
			carregarInformacaoLancamentos(dto, produtoEdicao);
			
			carregarSegmentacaoProdutoEdicao(dto, produtoEdicao);	
			
			/*
			 * A situacao da edicao vem da query principal devido a regra de furo
			 */
			dto.setStatusSituacao(situacaoProdutoEdicao);
			
		} else {
			
			obterProdutoEdicaoDTOManual(codigoProduto, produto, dto);
		}
		
		if (redistribuicao) {
			
			if (!(dto.getStatusSituacao().equals(StatusLancamento.EM_BALANCEAMENTO.name())
					|| dto.getStatusSituacao().equals(StatusLancamento.BALANCEADO.name())
					|| dto.getStatusSituacao().equals(StatusLancamento.EXPEDIDO.name()))) {
				
				throw new ValidacaoException(TipoMensagem.WARNING,
					"Situação do lançamento não permite cadastrar nova redistribuição!");
			}
			
			dto.setTipoLancamento(TipoLancamento.REDISTRIBUICAO);
			dto.setRepartePrevisto(null);
			dto.setRepartePromocional(null);
			dto.setDataLancamento(null);
			dto.setDataLancamentoPrevisto(null);
			dto.setNumeroLancamento(this.obterNumeroLancamento(idProdutoEdicao));
			dto.setModoTela(ModoTela.REDISTRIBUICAO);
			
		} else if (idProdutoEdicao != null) {
			
			dto.setModoTela(ModoTela.EDICAO);
			
		} else {
			
			dto.setNumeroLancamento(this.obterNumeroLancamento(idProdutoEdicao));
			
			dto.setModoTela(ModoTela.NOVO);
		}
				
		return dto;
	}

	private Integer obterNumeroLancamento(String idProdutoEdicao) {

		Integer ultimoNumeroLancamento = null;
		
		if (idProdutoEdicao != null) {
			
			ultimoNumeroLancamento = 
				lancamentoRepository.obterUltimoNumeroLancamento(Long.valueOf(idProdutoEdicao));
		}
		
		return (ultimoNumeroLancamento != null ? ultimoNumeroLancamento + 1 : 1);
	}

	private void carregarInformacaoDimensaoProduto(ProdutoEdicaoDTO dto, ProdutoEdicao produtoEdicao) {
		
		Dimensao dimEdicao = produtoEdicao.getDimensao();
		
		if (dimEdicao == null) {
			dto.setComprimento(0);
			dto.setEspessura(0);
			dto.setLargura(0);
		} else {
			dto.setComprimento(dimEdicao.getComprimento());
			dto.setEspessura(dimEdicao.getEspessura());
			dto.setLargura(dimEdicao.getLargura());
		}
	}

	private void carregarInformacaoDesconto(Produto produto,ProdutoEdicaoDTO dto, ProdutoEdicao produtoEdicao) {
	
		dto.setDesconto(BigDecimal.ZERO);
		
		if(!produtoEdicao.getOrigem().equals(Origem.INTERFACE)){
			
			if(produtoEdicao.getDesconto() != null ){
				
				dto.setDesconto(produtoEdicao.getDesconto());
				
				if(produtoEdicao.getDescricaoDesconto() != null && !"".equals(produtoEdicao.getDescricaoDesconto())){
					dto.setDescricaoDesconto(produtoEdicao.getDescricaoDesconto());
				}	
			}
			
		}else{
			
			if(produto.getDescontoLogistica()!=null ){
				dto.setDesconto( produto.getDescontoLogistica().getPercentualDesconto() );
				dto.setDescricaoDesconto(produto.getDescontoLogistica().getDescricao());
			}
		}
	}

	private void carregarInformacaoLancamentos(ProdutoEdicaoDTO dto,ProdutoEdicao produtoEdicao) {
		
		Lancamento uLancamento = lService.obterPrimeiroLancamentoDaEdicao(produtoEdicao.getId());

		if (uLancamento != null) {
			
			/*
			 * A situacao vem da query principal devido as regras de furo 
			 * 
			 * dto.setSituacaoLancamento(uLancamento.getStatus());
			 */
			dto.setNumeroLancamento(uLancamento.getNumeroLancamento());

			dto.setTipoLancamento(uLancamento.getTipoLancamento());
			
			Date dataLancamento = null;
			
			if(TipoLancamento.PARCIAL.equals(uLancamento.getTipoLancamento())){
				
				LancamentoParcial lancamentoParcial  = lancamentoParcialRepository.obterLancamentoPorProdutoEdicao(produtoEdicao.getId());
				
				if(lancamentoParcial!= null){
					
					dto.setDataLancamentoPrevisto(lancamentoParcial.getLancamentoInicial());	
					dto.setDataRecolhimentoPrevisto(lancamentoParcial.getRecolhimentoFinal());
				}else{
					
					dto.setDataLancamentoPrevisto(uLancamento.getDataLancamentoPrevista());
					dto.setDataRecolhimentoPrevisto(uLancamento.getDataRecolhimentoPrevista());
				}
				
				PeriodoLancamentoParcial primeiroPeriodo = periodoLancamentoParcialRepository.obterPrimeiroLancamentoParcial(produtoEdicao.getId());
				
				if(primeiroPeriodo!= null && primeiroPeriodo.getLancamento()!= null){
					dataLancamento = primeiroPeriodo.getLancamento().getDataLancamentoDistribuidor();
				}
				else{
					dataLancamento = uLancamento.getDataLancamentoDistribuidor();
				}
				
				PeriodoLancamentoParcial ultimoPeriodo = periodoLancamentoParcialRepository.obterUltimoLancamentoParcial(produtoEdicao.getId());
				
				if(ultimoPeriodo!= null && ultimoPeriodo.getLancamento()!= null){
					dto.setDataRecolhimentoReal(ultimoPeriodo.getLancamento().getDataRecolhimentoDistribuidor());
				}else{
					dto.setDataRecolhimentoReal(uLancamento.getDataRecolhimentoDistribuidor());
				}
				

			}else{
				
				dataLancamento = uLancamento.getDataLancamentoDistribuidor();
				dto.setDataLancamentoPrevisto(uLancamento.getDataLancamentoPrevista());

				dto.setDataRecolhimentoPrevisto(uLancamento.getDataRecolhimentoPrevista());
				dto.setDataRecolhimentoReal(uLancamento.getDataRecolhimentoDistribuidor());
			}

			dto.setDataLancamento(DateUtil.parseDataPTBR(this.dataCabalistica).compareTo(dataLancamento) == 0 ? null : dataLancamento);
			dto.setRepartePrevisto(uLancamento.getReparte());
			dto.setRepartePromocional(uLancamento.getRepartePromocional());
			
			int semanaRecolhimento = 
				SemanaUtil.obterNumeroSemana(uLancamento.getDataRecolhimentoDistribuidor(), 
											 this.distribuidorService.inicioSemana().getCodigoDiaSemana());
			
			dto.setSemanaRecolhimento(semanaRecolhimento);
		}
	}

	private void carregarSegmentacaoProdutoEdicao(ProdutoEdicaoDTO dto,ProdutoEdicao produtoEdicao) {
		
		SegmentacaoProduto segm = produtoEdicao.getSegmentacao();
		
		if(segm!=null){
			
			dto.setClasseSocial(segm.getClasseSocial());
			dto.setSexo(segm.getSexo());
			dto.setFaixaEtaria(segm.getFaixaEtaria());
			dto.setTemaPrincipal(segm.getTemaPrincipal());
			dto.setTemaSecundario(segm.getTemaSecundario());
		}
		else{
			
			segm = produtoEdicao.getProduto().getSegmentacao();
			
			if(segm!=null){
				dto.setClasseSocial(segm.getClasseSocial());
				dto.setSexo(segm.getSexo());
				dto.setFaixaEtaria(segm.getFaixaEtaria());
				dto.setTemaPrincipal(segm.getTemaPrincipal());
				dto.setTemaSecundario(segm.getTemaSecundario());
			}
		}
	}

	private void obterProdutoEdicaoDTOManual(String codigoProduto,Produto produto, ProdutoEdicaoDTO dto) {
		
		// Edição criada pelo Distribuidor:
		dto.setOrigemInterface(false);
		
		dto.setPeb(produto.getPeb());
				
		if (produto.getOrigem().equals(Origem.INTERFACE)
				&& produto.getDescontoLogistica()!= null){
			
			dto.setDesconto(produto.getDescontoLogistica().getPercentualDesconto());
			dto.setDescricaoDesconto(produto.getDescontoLogistica().getDescricao());
		}
		else{
			
			dto.setDesconto(produto.getDesconto());
			dto.setDescricaoDesconto(produto.getDescricaoDesconto());
		}
		
		Long ultimaEdicao = produtoEdicaoRepository.obterUltimoNumeroEdicao(codigoProduto);
		
		if (ultimaEdicao == null) {
			dto.setNumeroEdicao(1L);
		} else {
			dto.setNumeroEdicao(ultimaEdicao + 1);
		}
		
		dto.setGrupoProduto(produto.getTipoProduto()!=null?produto.getTipoProduto().getGrupoProduto():null);
		
		
	}

	@Override
	public ProdutoEdicao buscarPorID(Long idProdutoEdicao) {
		return produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
	}	
	
	@Transactional(readOnly = true)
	@Override
	public List<EdicoesProdutosDTO> obterHistoricoEdicoes(FiltroHistogramaVendas filtro) {
		
		return produtoEdicaoRepository.obterHistoricoEdicoes(filtro);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<AnaliseHistogramaDTO> obterBaseEstudoHistogramaPorFaixaVenda(FiltroHistogramaVendas filtro,String codigoProduto,String[] faixasVenda, String[] edicoes){
		
		List<AnaliseHistogramaDTO> list = new ArrayList<AnaliseHistogramaDTO>();
		
		AnaliseHistogramaDTO totalGeralDTO = new AnaliseHistogramaDTO();
		totalGeralDTO.setFaixaVenda("Total:");
		
		for (int i = 0; i < faixasVenda.length; i++) {
			String[] faixa = faixasVenda[i].split("-");
			AnaliseHistogramaDTO obj = produtoEdicaoRepository.obterBaseEstudoHistogramaPorFaixaVenda(filtro, codigoProduto, Integer.parseInt(faixa[0]), Integer.parseInt(faixa[1]), edicoes);
			obj.executeScaleValues();
			
			totalGeralDTO.setRepTotal(totalGeralDTO.getRepTotal().add(obj.getRepTotal()).setScale(2,BigDecimal.ROUND_FLOOR));
			totalGeralDTO.setRepMedio(totalGeralDTO.getRepMedio().add(obj.getRepMedio()).setScale(2,BigDecimal.ROUND_FLOOR));
			totalGeralDTO.setVdaTotal(totalGeralDTO.getVdaTotal().add(obj.getVdaTotal()).setScale(2,BigDecimal.ROUND_FLOOR));
			totalGeralDTO.setVdaMedio(totalGeralDTO.getVdaMedio().add(obj.getVdaMedio()).setScale(2,BigDecimal.ROUND_FLOOR));
			totalGeralDTO.setPercVenda(totalGeralDTO.getPercVenda().add(obj.getPercVenda()).setScale(2,BigDecimal.ROUND_FLOOR));
			totalGeralDTO.setEncalheMedio(totalGeralDTO.getEncalheMedio().add(obj.getEncalheMedio()).setScale(2,BigDecimal.ROUND_FLOOR));
			totalGeralDTO.setPartReparte(totalGeralDTO.getPartReparte().add(obj.getPartReparte()).setScale(2,BigDecimal.ROUND_FLOOR));
			totalGeralDTO.setPartVenda(totalGeralDTO.getPartVenda().add(obj.getPartVenda()).setScale(2,BigDecimal.ROUND_FLOOR));
			totalGeralDTO.setQtdeCotas(totalGeralDTO.getQtdeCotas().add(obj.getQtdeCotas()));
			totalGeralDTO.setCotasEsmagadas(totalGeralDTO.getCotasEsmagadas().add(obj.getCotasEsmagadas()).setScale(2,BigDecimal.ROUND_FLOOR));
			totalGeralDTO.setQtdeCotasSemVenda((totalGeralDTO.getQtdeCotasSemVenda().add(obj.getQtdeCotasSemVenda()).setScale(2,BigDecimal.ROUND_FLOOR)));
			totalGeralDTO.setVendaEsmagadas((totalGeralDTO.getVendaEsmagadas().add(obj.getVendaEsmagadas()).setScale(2,BigDecimal.ROUND_FLOOR)));
//			totalGeralDTO.setReparteDistribuido((totalGeralDTO.getReparteDistribuido().add(obj.getReparteDistribuido()).setScale(2,BigDecimal.ROUND_FLOOR)));
			
			totalGeralDTO.setQtdeCotasAtivas(totalGeralDTO.getQtdeCotasAtivas().add(obj.getQtdeCotasAtivas()));
			
			
			if(obj!=null){
				list.add(obj);
			}
		}
		
		if(list!=null && !list.isEmpty()){
			totalGeralDTO.setQtdeTotalCotasAtivas((cotaRepository.obterQuantidadeCotas(SituacaoCadastro.ATIVO)));
			totalGeralDTO.setReparteDistribuido(this.movimentoEstoqueService.obterReparteDistribuidoProduto(codigoProduto));
		}
		list.add(totalGeralDTO);  
		return list;
	}

	@Override
	@Transactional
	public BigDecimal obterPorcentualDesconto(ProdutoEdicao produtoEdicao) {
		
		BigDecimal porcentagemDesconto = null;
		
		switch (produtoEdicao.getOrigem()) {
		
			case MANUAL:
				
				porcentagemDesconto = 
					(produtoEdicao.getDesconto() != null) 
						? produtoEdicao.getDesconto() 
							: produtoEdicao.getProduto().getDesconto();
				
				if	((porcentagemDesconto == null 
						|| BigInteger.ZERO.equals(porcentagemDesconto.unscaledValue()))
						&& (Origem.INTERFACE.equals(produtoEdicao.getProduto().getOrigem()))) {
					
					DescontoLogistica descontoLogistica = 
						produtoEdicao.getProduto().getDescontoLogistica();
					
					if (descontoLogistica != null) {
						
						porcentagemDesconto = 
							produtoEdicao.getProduto().getDescontoLogistica().getPercentualDesconto();
					}
				}
				
				break;
			
			case INTERFACE:
				
				DescontoLogistica descontoLogistica = 
					(produtoEdicao.getDescontoLogistica() != null) 
						? produtoEdicao.getDescontoLogistica() 
							: produtoEdicao.getProduto().getDescontoLogistica();
				
				if (descontoLogistica != null) {
					
					porcentagemDesconto = descontoLogistica.getPercentualDesconto();
				}
				
				break;
				
			default:
				
				break;
		}
		
		if	(porcentagemDesconto == null 
				|| BigInteger.ZERO.equals(porcentagemDesconto.unscaledValue())) {
			
			throw new ValidacaoException(new ValidacaoVO(
				TipoMensagem.WARNING, 
					"O produto " + produtoEdicao.getProduto().getNome() 
						+ " não possui desconto! É necessario cadastrar um desconto para ele na tela de cadastro de produtos"));
		}
		
		return porcentagemDesconto;
	}

	@Override
	@Transactional
	public List<ProdutoEdicaoDTO> obterEdicoesProduto(FiltroHistoricoVendaDTO filtro) {
		return this.produtoEdicaoRepository.obterEdicoesProduto(filtro);
	}

	/**
	 * @param codigoBarra
	 * @return
	 * @see br.com.abril.nds.repository.ProdutoEdicaoRepository#obterPorCodigoBarraILike(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ItemAutoComplete> obterPorCodigoBarraILike(String codigoBarra) {
		return produtoEdicaoRepository.obterPorCodigoBarraILike(codigoBarra);
	}

}
