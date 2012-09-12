package br.com.abril.nds.controllers.estoque;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ConfirmacaoVO;
import br.com.abril.nds.client.vo.DiferencaVO;
import br.com.abril.nds.client.vo.DiferencaVO.TipoDirecionamentoDiferenca;
import br.com.abril.nds.client.vo.ItemDiferencaVO;
import br.com.abril.nds.client.vo.RateioCotaVO;
import br.com.abril.nds.client.vo.ResultadoDiferencaVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO.OrdenacaoColunaConsulta;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO.OrdenacaoColunaLancamento;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.EstudoCotaService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações referentes às
 * telas de consulta de diferenças e lançamento de diferenças.
 * 
 * @author Discover Technology
 */
@SuppressWarnings("deprecation")
@Resource
@Path("/estoque/diferenca")
public class DiferencaEstoqueController {

	private Result result;
	
	private HttpSession httpSession;
	
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private EstudoCotaService estudoCotaService; 
	
	@Autowired
	private EstudoService estudoService;
	
	@Autowired
	private MovimentoEstoqueCotaService movimentoEstoqueCotaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private static final String FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE = "filtroPesquisaLancamento";
	
	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaConsultaFaltasSobras";
	
	private static final String LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE = "listaNovasDiferencas";
	
	private static final String LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE = "listaNovasDiferencasVO";
	
	private static final String LISTA_DIFERENCAS_PESQUISADAS_SESSION_ATTRIBUTE = "listaDiferencasPesquisadas";
	
	private static final String MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE = "mapaRateiosCadastrados";
	
	private static final String MODO_INCLUSAO_SESSION_ATTRIBUTE = "modoInclusaoDiferenca";
	
	public DiferencaEstoqueController(Result result, 
								 	  HttpSession httpSession,
								 	  HttpServletResponse httpServletResponse) {
		
		this.result = result;
		this.httpSession = httpSession;
		this.httpServletResponse = httpServletResponse;
	}

	@Get
	public void exportar(FileType fileType) throws IOException {
				
		if (fileType == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de arquivo não encontrado!");
		}
		
		FiltroConsultaDiferencaEstoqueDTO filtroSessao = this.obterFiltroParaExportacao();
		
		List<Diferenca> listaDiferencas = diferencaEstoqueService.obterDiferencas(filtroSessao);
		
		List<DiferencaVO> listaConsultaDiferenca = new LinkedList<DiferencaVO>();
		
		BigInteger qtdeTotalDiferencas = BigInteger.ZERO;
		BigDecimal valorTotalDiferencas = BigDecimal.ZERO;
	
		for (Diferenca diferenca : listaDiferencas) {
			
			DiferencaVO consultaDiferencaVO = new DiferencaVO();
			
			consultaDiferencaVO.setId(diferenca.getId());
			
			consultaDiferencaVO.setDataLancamento(
				DateUtil.formatarData(
					diferenca.getMovimentoEstoque().getData(), Constantes.DATE_PATTERN_PT_BR));
			
			consultaDiferencaVO.setCodigoProduto(diferenca.getProdutoEdicao().getProduto().getCodigo());
			
			consultaDiferencaVO.setDescricaoProduto(diferenca.getProdutoEdicao().getProduto().getNome());
			
			consultaDiferencaVO.setDescricaoProduto(diferenca.getProdutoEdicao().getProduto().getNome());
			consultaDiferencaVO.setNumeroEdicao(diferenca.getProdutoEdicao().getNumeroEdicao().toString());
			
			consultaDiferencaVO.setPrecoVenda(
				CurrencyUtil.formatarValor(diferenca.getProdutoEdicao().getPrecoVenda()));
			
			consultaDiferencaVO.setTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());
			
			if (diferenca.getItemRecebimentoFisico() != null) {
				consultaDiferencaVO.setNumeroNotaFiscal(
					diferenca.getItemRecebimentoFisico().getItemNotaFiscal().getNotaFiscal().getNumero().toString());
			} else {
				consultaDiferencaVO.setNumeroNotaFiscal(" - ");
			}
			
			consultaDiferencaVO.setQuantidade(diferenca.getQtde());
			
			consultaDiferencaVO.setStatusAprovacao(
				diferenca.getMovimentoEstoque().getStatus().getDescricaoAbreviada());
			
			consultaDiferencaVO.setMotivoAprovacao(diferenca.getMovimentoEstoque().getMotivo());
			
			consultaDiferencaVO.setValorTotalDiferenca(
				CurrencyUtil.formatarValor(diferenca.getValorTotalDiferenca()));
			
			listaConsultaDiferenca.add(consultaDiferencaVO);
			
			qtdeTotalDiferencas = 
				qtdeTotalDiferencas.add(diferenca.getQtde());
			
			valorTotalDiferencas = valorTotalDiferencas.add(diferenca.getValorTotalDiferenca());
		}
		
		String valorTotalDiferencasFormatado = 
			CurrencyUtil.formatarValorComSimbolo(valorTotalDiferencas);
		
		ResultadoDiferencaVO resultadoDiferencaVO = 
			new ResultadoDiferencaVO(null, qtdeTotalDiferencas, valorTotalDiferencasFormatado);
		
		FileExporter.to("consulta-faltas-sobras", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtroSessao, resultadoDiferencaVO, 
				listaConsultaDiferenca, DiferencaVO.class, this.httpServletResponse);
	}
	
	@Get
	@Rules(Permissao.ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS)
	public void lancamento() {
		
		this.carregarCombosLancamento();
		
		this.limparSessao();
		
		result.include("dataAtual", DateUtil.formatarDataPTBR(new Date()));
	}
	
	@Post
	@Path("/lancamento/pesquisa")
	public void pesquisarLancamentos(String dataMovimentoFormatada, TipoDiferenca tipoDiferenca, 
									 String sortorder, String sortname, int page, int rp) {
		
		this.validarEntradaDadosPesquisaLancamentos(dataMovimentoFormatada);

		Date dataMovimento = DateUtil.parseDataPTBR(dataMovimentoFormatada);
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = 
			this.carregarFiltroPesquisaLancamentos(
				dataMovimento, tipoDiferenca, sortorder, sortname, page, rp);
		
		List<Diferenca> listaLancamentoDiferencas = 
			this.diferencaEstoqueService.obterDiferencasLancamento(filtro);
		
		if (listaLancamentoDiferencas == null || listaLancamentoDiferencas.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			
		} else {
			
			Long qtdeTotalRegistros = this.diferencaEstoqueService.obterTotalDiferencasLancamento(filtro);
			
			this.processarDiferencasLancamento(listaLancamentoDiferencas, filtro, qtdeTotalRegistros.intValue());
		}
	}
	
	@Post
	@Path("/lancamento/pesquisa/novos")
	@SuppressWarnings("unchecked")
	public void pesquisarLancamentosNovos(Date dataMovimento, TipoDiferenca tipoDiferenca,
										  String sortorder, String sortname, Integer page, Integer rp) {
		
		Set<Diferenca> listaNovasDiferencas = (Set<Diferenca>)
				this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
		
		if (listaNovasDiferencas != null 
				&& !listaNovasDiferencas.isEmpty()) {

			FiltroLancamentoDiferencaEstoqueDTO filtro = 
				this.carregarFiltroPesquisaLancamentos(
					dataMovimento, tipoDiferenca, sortorder, sortname, page, rp);
			
			processarDiferencasLancamentoNovos(listaNovasDiferencas, filtro);
			
		} else {
			
			ResultadoDiferencaVO resultadoDiferencaVO = new ResultadoDiferencaVO();
			
			TableModel<CellModelKeyValue<DiferencaVO>> tableModel =
					new TableModel<CellModelKeyValue<DiferencaVO>>();
				
			tableModel.setRows(new ArrayList<CellModelKeyValue<DiferencaVO>>());

			tableModel.setTotal(0);
			
			tableModel.setPage(1);
			
			resultadoDiferencaVO.setTableModel(tableModel);
			
			resultadoDiferencaVO.setQtdeTotalDiferencas(BigInteger.ZERO);
			
			resultadoDiferencaVO.setValorTotalDiferencas("");
			
			result.use(Results.json()).from(resultadoDiferencaVO, "result").recursive().serialize();
		}
	}
	
	@Post
	@Path("/lancamento/novo")
	@SuppressWarnings("unchecked")
	public void carregarNovasDiferencas(String dataMovimentoFormatada, TipoDiferenca tipoDiferenca) {
		
		List<DiferencaVO> listaNovasDiferencas = new ArrayList<DiferencaVO>();
		
		List<DiferencaVO> listaDiferencasCadastradas =
			(List<DiferencaVO>) this.httpSession.getAttribute(LISTA_DIFERENCAS_PESQUISADAS_SESSION_ATTRIBUTE);
		
		int qtdeDiferencasCadastradas = 0;
		
		if (listaDiferencasCadastradas != null) {
			
			qtdeDiferencasCadastradas = listaDiferencasCadastradas.size();
		}
		
		int qtdeInicialPadrao = qtdeDiferencasCadastradas + 50;
		
		for (int indice = listaNovasDiferencas.size(); indice < qtdeInicialPadrao; indice++) {
			
			DiferencaVO diferenca = new DiferencaVO();
			
			diferenca.setId(Long.valueOf(indice));
			
			diferenca.setDataLancamento(dataMovimentoFormatada);
			
			diferenca.setTipoDiferenca(tipoDiferenca.getDescricao());
			
			listaNovasDiferencas.add(diferenca);
		}
		
		TableModel<CellModelKeyValue<DiferencaVO>> tableModel =
			new TableModel<CellModelKeyValue<DiferencaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNovasDiferencas));
		
		tableModel.setTotal(qtdeInicialPadrao);
		
		tableModel.setPage(1);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	@Post
	@Path("/lancamento/cadastrarNovasDiferencas")
	public void cadastrarNovasDiferencas(TipoDiferenca tipoDiferenca, 
										 boolean lancamentoPorCota, 
										 boolean direcionadoParaEstoque,
										 boolean redirecionarProdutosEstoque,
										 String codigoProduto,
										 Integer edicaoProduto, 
										 Integer numeroCota,
										 BigInteger diferenca,
										 BigInteger reparteAtual,
										 BigInteger qntReparteRateio,
										 Date dataNotaEnvio,
										 List<RateioCotaVO> rateioCotas,
										 List<ItemDiferencaVO> diferencasProdutos) {
		
		if (tipoDiferenca == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O preenchimento do campo [Tipo de Diferença] é obrigatório!");
		}
		
		if(lancamentoPorCota){
			
			validarLancamentoPorCota(numeroCota, dataNotaEnvio,null);
			
			//incluir diferença lançamento por cota
		}
		else{
			
			if(!direcionadoParaEstoque){
				
				if(TipoDiferenca.FALTA_EM.equals(tipoDiferenca)){
					
					if(qntReparteRateio.compareTo(diferenca) < 0){
						throw new ValidacaoException(TipoMensagem.WARNING,"O quantidade total de diferença de produto deve ser direcionada a(s) cota(s)!");
					}
				}
					
				DiferencaVO diferencaVO = obterDiferencaVO(tipoDiferenca,codigoProduto,edicaoProduto, qntReparteRateio, reparteAtual);
				diferencaVO.setTipoDirecionamento(TipoDirecionamentoDiferenca.COTA);
				
				incluirDiferencaEstoque(diferencaVO, tipoDiferenca);
				
				cadastrarRateioCotas(rateioCotas, diferencaVO);
				
				if(redirecionarProdutosEstoque){
					
					BigInteger diferencaEstque = diferenca.subtract(qntReparteRateio);
					
					DiferencaVO diferencaEstoque = obterDiferencaVO(tipoDiferenca,codigoProduto,edicaoProduto, diferencaEstque, reparteAtual);
					diferencaEstoque.setTipoDirecionamento(TipoDirecionamentoDiferenca.ESTOQUE);
					
					incluirDiferencaEstoque(diferencaEstoque,tipoDiferenca);
				}
						
			}
			else{
				
				DiferencaVO diferencaVO = obterDiferencaVO(tipoDiferenca,codigoProduto,edicaoProduto, diferenca, reparteAtual);
				diferencaVO.setTipoDirecionamento(TipoDirecionamentoDiferenca.ESTOQUE);
					
				incluirDiferencaEstoque(diferencaVO,tipoDiferenca);		
			}
		}
		
		result.use(Results.json()).from("").serialize();
	}

	private DiferencaVO obterDiferencaVO(TipoDiferenca tipoDiferenca,String codigoProduto,Integer edicaoProduto, BigInteger diferenca, BigInteger reparteAtual) {
		
		DiferencaVO diferencaVO =  new DiferencaVO();
		
		diferencaVO.setCodigoProduto(codigoProduto);
		diferencaVO.setNumeroEdicao(Util.nvl(edicaoProduto,"").toString());
		diferencaVO.setQuantidade(diferenca);
		diferencaVO.setQtdeEstoqueAtual(reparteAtual);
		diferencaVO.setTipoDiferenca(tipoDiferenca.getDescricao());
		
		return diferencaVO;
	}


	@SuppressWarnings("unchecked")
	private Long incluirDiferencaEstoque(DiferencaVO diferencaVO, TipoDiferenca tipoDiferenca) {
		
		Set<DiferencaVO> diferencasNovas = new HashSet<DiferencaVO>();
		diferencasNovas.add(diferencaVO);
		
		this.validarProdutoDuplicadoLancamento(diferencasNovas);
		
		Set<DiferencaVO> listaNovasDiferencasVO = 
				(HashSet<DiferencaVO>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
			
		if (listaNovasDiferencasVO == null) {
			
			listaNovasDiferencasVO = new HashSet<DiferencaVO>();
		}
		
		Long id = 0L;
		
		for (DiferencaVO diferencaVOs : listaNovasDiferencasVO) {
			diferencaVOs.setId(id);
			id++;
		}
		
		diferencaVO.setId(id);
		
		listaNovasDiferencasVO.add(diferencaVO);
		
		Set<Diferenca> listaDiferencas = (Set<Diferenca>)
				this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
			
		if (listaDiferencas == null) {
			
			listaDiferencas = new HashSet<Diferenca>();
		}
		
		id = 0L;
		
		for (Diferenca diferenca : listaDiferencas) {
			diferenca.setId(id);
			id++;
		}
		
		Diferenca diferenca = this.obterDiferenca(diferencaVO, new Date(), tipoDiferenca, id); 
		
		listaDiferencas.add(diferenca);
		
		this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE, listaNovasDiferencasVO);
		
		this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE, listaDiferencas);
		
		this.httpSession.setAttribute(MODO_INCLUSAO_SESSION_ATTRIBUTE, true);
		
		return diferenca.getId();
	}
	
	private Diferenca obterDiferenca(DiferencaVO diferencaVO, 
									Date dataMovimento ,
									TipoDiferenca tipoDiferenca,
									Long idDiferenca){
		
		this.validarNovaDiferenca(diferencaVO, dataMovimento, tipoDiferenca);
		
		Diferenca diferenca = new Diferenca();
		
		diferenca.setId(idDiferenca);
		
		ProdutoEdicao produtoEdicao =
			this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
				diferencaVO.getCodigoProduto(), diferencaVO.getNumeroEdicao());
		
		diferenca.setProdutoEdicao(produtoEdicao);
		diferenca.setQtde(diferencaVO.getQuantidade());
		diferenca.setStatusConfirmacao(StatusConfirmacao.PENDENTE);
		diferenca.setTipoDiferenca(tipoDiferenca);
		diferenca.setAutomatica(false);
		
		BigDecimal valorTotalDiferenca = BigDecimal.ZERO;
		
		if (TipoDiferenca.FALTA_DE.equals(tipoDiferenca)
				|| TipoDiferenca.SOBRA_DE.equals(tipoDiferenca)) {
			
			valorTotalDiferenca =
				produtoEdicao.getPrecoVenda().multiply(new BigDecimal(produtoEdicao.getPacotePadrao()))
					.multiply( new BigDecimal( diferenca.getQtde() ) );
			
		} else if (TipoDiferenca.FALTA_EM.equals(tipoDiferenca)
						|| TipoDiferenca.SOBRA_EM.equals(tipoDiferenca)) {
			
			valorTotalDiferenca =
				produtoEdicao.getPrecoVenda().multiply(new BigDecimal( diferenca.getQtde() ) );
		}
		
		diferenca.setValorTotalDiferenca(valorTotalDiferenca);
		
		return diferenca;
	}

	private void validarLancamentoPorCota(Integer numeroCota,Date dataNotaEnvio, List<ItemDiferencaVO> diferencasProdutos) {
		
		if (dataNotaEnvio == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Nota de Envio] é obrigatório!");
		}
		
		if(numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Cota] é obrigatório!");
		}
		
		validarDiferencaProduto(diferencasProdutos);
	}

	private void validarDiferencaProduto(List<ItemDiferencaVO> diferencasProdutos) {
				
		if(diferencasProdutos == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Não foi informado nenhum produto para diferença!");
		}
		
		boolean diferencainformada = false;
		
		for(ItemDiferencaVO item : diferencasProdutos){
			if(item.getDiferenca()!= null){
				diferencainformada = true;
				return ;
			}
		}
		
		if(!diferencainformada){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Não foi informado nenhum diferença para os produtos!");
		}
	}

	
	@Get
	@Rules(Permissao.ROLE_ESTOQUE_CONSULTA_FALTAS_SOBRAS)
	public void consulta() {
		this.carregarCombosConsulta();
		
		result.include("dataAtual", new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).format(new Date()));
	}
	
	@Post
	@Path("/pesquisarDiferencas")
	public void pesquisarDiferencas(String codigoProduto, 
									Long idFornecedor, String dataInicial,
									String dataFinal, TipoDiferenca tipoDiferenca, 
									Integer numeroCota, String nomeCota,
									String sortorder, String sortname,
									int page, int rp) {
		
		this.validarEntradaDadosPesquisa(codigoProduto, idFornecedor,
										 dataInicial, dataFinal, tipoDiferenca);
		
		FiltroConsultaDiferencaEstoqueDTO filtro =
			this.carregarFiltroPesquisa(codigoProduto, idFornecedor,
										dataInicial, dataFinal, tipoDiferenca, numeroCota, nomeCota,
										sortorder, sortname, page, rp);
		
		List<Diferenca> listaDiferencas =
			diferencaEstoqueService.obterDiferencas(filtro);
		
		if (listaDiferencas == null || listaDiferencas.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {
			
			this.processarDiferencas(listaDiferencas, filtro);
		}
	}
	
	@Post
	@Path("/lancamento/rateio")
	@SuppressWarnings("unchecked")
	public void carregarRateio(Long idDiferenca) {

		List<RateioCotaVO> listaNovosRateiosCota = new ArrayList<RateioCotaVO>();
		
		Map<Long, List<RateioCotaVO>> mapaRateiosCadastrados =
			(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		List<RateioCotaVO> listaRateiosCadastrados = new ArrayList<RateioCotaVO>();
		
		if (mapaRateiosCadastrados == null) {
			
			mapaRateiosCadastrados = new HashMap<Long, List<RateioCotaVO>>();
			
		} else {
			
			listaRateiosCadastrados = mapaRateiosCadastrados.get(idDiferenca);
		}
		
		if (listaRateiosCadastrados != null && !listaRateiosCadastrados.isEmpty()) {
			
			listaNovosRateiosCota.addAll(listaRateiosCadastrados);
		}
		
		int qtdeInicialPadrao = listaNovosRateiosCota.size() + 50;
		
		for (int indice = listaNovosRateiosCota.size(); indice < qtdeInicialPadrao; indice++) {
			
			RateioCotaVO rateioCota = new RateioCotaVO();
			
			rateioCota.setId(Long.valueOf(indice));
			
			rateioCota.setIdDiferenca(idDiferenca);
			
			listaNovosRateiosCota.add(rateioCota);
		}
		
		TableModel<CellModelKeyValue<RateioCotaVO>> tableModel =
			new TableModel<CellModelKeyValue<RateioCotaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNovosRateiosCota));
		
		tableModel.setTotal(qtdeInicialPadrao);
		
		tableModel.setPage(1);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	@Post
	@Path("/lancamento/rateio/validarEstudo")
	public void verificarExistenciaEstudo(Long idDiferenca) {
		
		DiferencaVO diferencaVO = this.obterDiferencaPorId(idDiferenca);
		
		Date dataMovimento = DateUtil.parseDataPTBR(diferencaVO.getDataLancamento());
		
		ProdutoEdicao produtoEdicao =
			this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
				diferencaVO.getCodigoProduto(), diferencaVO.getNumeroEdicao());
		
		Estudo estudo =
			this.estudoService.obterEstudoDoLancamentoPorDataProdutoEdicao(dataMovimento, produtoEdicao.getId());
		
		if (estudo == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe Estudo na data deste lançamento!");
		}

		result.use(Results.json()).from("", "result").serialize();
	}
	
	@Post
	@Path("/lancamento/cadastrarRateioCotas")
	@SuppressWarnings("unchecked")
	public void cadastrarRateioCotas(List<RateioCotaVO> listaNovosRateios, DiferencaVO diferencaVO) {
		
		validarNovosRateios(listaNovosRateios, diferencaVO);
		
		Map<Long, List<RateioCotaVO>> mapaRateiosCadastrados =
			(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		List<RateioCotaVO> listaRateiosCadastrados = null;
		
		if (mapaRateiosCadastrados == null) {

			mapaRateiosCadastrados = new HashMap<Long, List<RateioCotaVO>>();

		} else {
			
			listaRateiosCadastrados = mapaRateiosCadastrados.get(diferencaVO.getId());
		}
		
		if (listaRateiosCadastrados == null || listaRateiosCadastrados.isEmpty()) {
			
			listaRateiosCadastrados = new ArrayList<RateioCotaVO>();
		}
		
		for (RateioCotaVO rateioCotaVO : listaNovosRateios) {
			
			rateioCotaVO.setIdDiferenca(diferencaVO.getId());
			
			this.validarNovoRateio(rateioCotaVO,diferencaVO);

			if (!listaRateiosCadastrados.contains(rateioCotaVO)) {
				
				listaRateiosCadastrados.add(rateioCotaVO);
			}
		}
		
		mapaRateiosCadastrados.put(diferencaVO.getId(), listaRateiosCadastrados);
		
		this.httpSession.setAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE, mapaRateiosCadastrados);
		
		//result.use(Results.json()).from("", "result").serialize();
	}

	private void validarNovosRateios(List<RateioCotaVO> listaNovosRateios,DiferencaVO diferencaVO) {
		
		if (listaNovosRateios == null 
				|| listaNovosRateios.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha os dados para o rateio!");
		}
		
		this.validarPreenchimentoNovosRateios(listaNovosRateios);
		
		this.validarCotasDuplicadasRateio(listaNovosRateios);
		
		this.validarSomaQuantidadeRateio(listaNovosRateios, diferencaVO);
	}
	
	@Post
	@Path("/lancamento/limparSessao")
	@SuppressWarnings("unchecked")
	public void limparDadosSessao(boolean confirmado) {
	
		Set<Diferenca> listaNovasDiferencas =
			(Set<Diferenca>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
		
		Map<Long, List<RateioCotaVO>> mapaRateiosCadastrados =
			(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		if (!confirmado) {
		
			if ((listaNovasDiferencas != null && !listaNovasDiferencas.isEmpty())
					|| (mapaRateiosCadastrados != null && !mapaRateiosCadastrados.isEmpty())) {
	
				result.use(Results.json()).from(new ConfirmacaoVO(false), "result").serialize();
				
				return;
			}
		}
		
		this.limparSessao();
			
		result.use(Results.json()).from(new ConfirmacaoVO(true), "result").serialize();
	}
	
	/**
	 * Método responsável por carregar todos os combos da tela de consulta.
	 */
	public void carregarCombosConsulta() {
		this.carregarComboTiposDiferenca();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo =
			this.carregarComboFornecedores(null);
		
		result.include("listaFornecedores", listaFornecedoresCombo);
	}
	
	
	@Post
	@Path("/lancamento/excluir")
	public void excluirFaltaSobra(Long idDiferenca){
		
		excluirDiferenca(idDiferenca);
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = 
			(FiltroLancamentoDiferencaEstoqueDTO) 
				this.httpSession.getAttribute(FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE);
		
		this.pesquisarLancamentosNovos(
			filtro.getDataMovimento(), filtro.getTipoDiferenca(), filtro.getPaginacao().getSortOrder(), 
				filtro.getOrdenacaoColuna().toString(), filtro.getPaginacao().getPaginaAtual(), 
					filtro.getPaginacao().getQtdResultadosPorPagina());
	}

	@SuppressWarnings("unchecked")
	private void excluirDiferenca(Long idDiferenca) {
		
		Set<Diferenca> listaNovasDiferencas = (Set<Diferenca>) 
				this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);

		if (listaNovasDiferencas != null) {
			
			Diferenca diferencaARemover = null;
			
			for (Diferenca diferenca : listaNovasDiferencas) {
				
				if (diferenca.getId().equals(idDiferenca)) {
					
					diferencaARemover = diferenca;
					
					break;
				}
			}
			
			if (diferencaARemover != null) {
				
				listaNovasDiferencas.remove(diferencaARemover);
			}
		}
		
		this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE, listaNovasDiferencas);
		
		Set<DiferencaVO> listaNovasDiferencasVO = (Set<DiferencaVO>)
			this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
		
		if (listaNovasDiferencasVO != null) {
			
			DiferencaVO diferencaVOARemover = null;
			
			for (DiferencaVO diferencaVO : listaNovasDiferencasVO) {
				
				if (diferencaVO.getId().equals(idDiferenca)) {
					
					diferencaVOARemover = diferencaVO;
					break;
				}
			}
			
			if (diferencaVOARemover != null) {
				
				listaNovasDiferencasVO.remove(diferencaVOARemover);
			}
		}
		
		this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE, listaNovasDiferencasVO);
	}

	@Post
	@SuppressWarnings("unchecked")
	public void confirmarLancamentos() {
		
		Boolean modoInclusao = (Boolean) this.httpSession.getAttribute(MODO_INCLUSAO_SESSION_ATTRIBUTE);
		
		Set<Diferenca> listaNovasDiferencas =
			(Set<Diferenca>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
		
		if (modoInclusao != null 
				&& modoInclusao
				&& (listaNovasDiferencas == null
						|| listaNovasDiferencas.isEmpty())) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há lançamentos a confirmar!");
		}
		
		 Map<Long, List<RateioCotaVO>> mapaRateioCotas =
			(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		FiltroLancamentoDiferencaEstoqueDTO filtroPesquisa =
			(FiltroLancamentoDiferencaEstoqueDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE);
		
		this.diferencaEstoqueService.efetuarAlteracoes(
			listaNovasDiferencas, mapaRateioCotas, filtroPesquisa, this.getUsuario().getId());
		
		result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
		
		this.limparSessao();
	}
	
	/**
	 * Método responsável por carregar todos os combos da tela de lançamento.
	 */
	private void carregarCombosLancamento() {
		
		this.carregarComboTiposDiferenca();
	}
	
	/**
	 * Método responsável por carregar o combo de tipos de diferença.
	 */
	private void carregarComboTiposDiferenca() {

		List<ItemDTO<TipoDiferenca, String>> listaTiposDiferenca =
			new ArrayList<ItemDTO<TipoDiferenca, String>>();
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.FALTA_DE, TipoDiferenca.FALTA_DE.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.FALTA_EM, TipoDiferenca.FALTA_EM.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.SOBRA_DE, TipoDiferenca.SOBRA_DE.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.SOBRA_EM, TipoDiferenca.SOBRA_EM.getDescricao())
		);
		
		result.include("listaTiposDiferenca", listaTiposDiferenca);
	}
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private List<ItemDTO<Long, String>> carregarComboFornecedores(String codigoProduto) {
		
		List<Fornecedor> listaFornecedor =
			fornecedorService.obterFornecedoresPorProduto(codigoProduto, GrupoFornecedor.PUBLICACAO);
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo =
			new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(
				new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial())
			);
		}
			
		return listaFornecedoresCombo;
	}
	
	@Post
	@Path("/pesquisarFonecedores")
	public void pesquisarFonecedores(String codigoProduto) {
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = 
			carregarComboFornecedores(codigoProduto);
		
		result.use(Results.json()).from(listaFornecedoresCombo, "result").recursive().serialize();
	}
	
	/*
	 * Obtém o filtro de pesquisa para exportação.
	 */
	private FiltroConsultaDiferencaEstoqueDTO obterFiltroParaExportacao() {
		
		FiltroConsultaDiferencaEstoqueDTO filtroSessao =
			(FiltroConsultaDiferencaEstoqueDTO) 
				this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null) {
			
			if (filtroSessao.getPaginacao() != null) {
				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
			if (filtroSessao.getCodigoProduto() != null) {
		
				Produto produto = this.produtoService.obterProdutoPorCodigo(filtroSessao.getCodigoProduto());
				
				if (produto != null) {
					
					filtroSessao.setNomeProduto(produto.getNome());
				}
			}
			
			if (filtroSessao.getIdFornecedor() != null) {
				
				Fornecedor fornecedor =
					this.fornecedorService.obterFornecedorPorId(filtroSessao.getIdFornecedor());
				
				if (fornecedor != null) {
					
					filtroSessao.setNomeFornecedor(fornecedor.getJuridica().getRazaoSocial());
				}
			}
		}
		
		return filtroSessao;
	}
	
	/*
	 * Processa o resultado das diferenças para lançamento.
	 *  
	 * @param listaDiferencas - lista de diferenças
	 * @param filtro - filtro da pesquisa
	 */
	private void processarDiferencasLancamento(List<Diferenca> listaDiferencas, 
											   FiltroLancamentoDiferencaEstoqueDTO filtro,
											   Integer qtdeTotalRegistros) {

		List<DiferencaVO> listaLancamentosDiferenca = new LinkedList<DiferencaVO>();
		
		BigInteger qtdeTotalDiferencas = BigInteger.ZERO;
		
		BigDecimal valorTotalDiferencas = BigDecimal.ZERO;
		
		for (Diferenca diferenca : listaDiferencas) {
			
			ProdutoEdicao produtoEdicao = diferenca.getProdutoEdicao();
			
			Produto produto = produtoEdicao.getProduto();
			
			DiferencaVO lancamentoDiferenca = new DiferencaVO();
			
			if (diferenca.getId() != null) {
				
				lancamentoDiferenca.setId(diferenca.getId());
			}
			
			lancamentoDiferenca.setCodigoProduto(produto.getCodigo());
			lancamentoDiferenca.setDescricaoProduto(produto.getDescricao());
			lancamentoDiferenca.setNumeroEdicao(produtoEdicao.getNumeroEdicao().toString());
			
			lancamentoDiferenca.setPrecoVenda(CurrencyUtil.formatarValor(produtoEdicao.getPrecoVenda()));
			
			lancamentoDiferenca.setPacotePadrao(String.valueOf(produtoEdicao.getPacotePadrao()));
			lancamentoDiferenca.setQuantidade(diferenca.getQtde());
			lancamentoDiferenca.setTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());
			lancamentoDiferenca.setAutomatica(diferenca.isAutomatica());
			
			if (diferenca.getMovimentoEstoque() != null) {
			
				Date dataLancamento = diferenca.getMovimentoEstoque().getData();
				
				if (dataLancamento != null) {
					
					lancamentoDiferenca.setDataLancamento(DateUtil.formatarDataPTBR(dataLancamento));
				}
			}

			lancamentoDiferenca.setValorTotalDiferenca(
				CurrencyUtil.formatarValor(diferenca.getValorTotalDiferenca()));
			
			listaLancamentosDiferenca.add(lancamentoDiferenca);
			
			qtdeTotalDiferencas = 
				qtdeTotalDiferencas.add(diferenca.getQtde());
			
			valorTotalDiferencas = valorTotalDiferencas.add(diferenca.getValorTotalDiferenca());
		}
		
		TableModel<CellModelKeyValue<DiferencaVO>> tableModel =
			new TableModel<CellModelKeyValue<DiferencaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentosDiferenca));

		tableModel.setTotal(qtdeTotalRegistros);
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		String valorTotalDiferencasFormatado = 
			CurrencyUtil.formatarValorComSimbolo(valorTotalDiferencas);
		
		ResultadoDiferencaVO resultadoLancamentoDiferenca = 
			new ResultadoDiferencaVO(tableModel, qtdeTotalDiferencas, valorTotalDiferencasFormatado);
		
		this.httpSession.setAttribute(
			LISTA_DIFERENCAS_PESQUISADAS_SESSION_ATTRIBUTE, listaLancamentosDiferenca);
	
		result.use(Results.json()).from(resultadoLancamentoDiferenca, "result").recursive().serialize();
	}
	
	/*
	 * Processa o resultado das novas diferenças para lançamento.
	 *  
	 * @param listaDiferencas - lista de diferenças
	 * @param filtro - filtro da pesquisa
	 */
	private void processarDiferencasLancamentoNovos(Set<Diferenca> listaDiferencas, 
												   	FiltroLancamentoDiferencaEstoqueDTO filtro) {

		List<DiferencaVO> listaLancamentosDiferenca = processarListaDiferencas(listaDiferencas); 
		
		BigInteger qtdeTotalDiferencas = BigInteger.ZERO;
		
		BigDecimal valorTotalDiferencas = BigDecimal.ZERO;
		
		for(DiferencaVO diferenca : listaLancamentosDiferenca){
			
			qtdeTotalDiferencas = qtdeTotalDiferencas.add(diferenca.getQuantidade());
				
			valorTotalDiferencas = valorTotalDiferencas.add( diferenca.getVlTotalDiferenca());
		}
		
		List<DiferencaVO> listaLancamentosDiferencaPaginada =
			PaginacaoUtil.paginarEOrdenarEmMemoria(
				listaLancamentosDiferenca, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());
		
		TableModel<CellModelKeyValue<DiferencaVO>> tableModel =
			new TableModel<CellModelKeyValue<DiferencaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentosDiferencaPaginada));

		tableModel.setTotal(listaLancamentosDiferenca.size());
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		String valorTotalDiferencasFormatado = 
			CurrencyUtil.formatarValorComSimbolo(valorTotalDiferencas);
		
		ResultadoDiferencaVO resultadoLancamentoDiferenca = 
			new ResultadoDiferencaVO(tableModel, qtdeTotalDiferencas, valorTotalDiferencasFormatado);
		
		this.httpSession.setAttribute(
			LISTA_DIFERENCAS_PESQUISADAS_SESSION_ATTRIBUTE, listaLancamentosDiferencaPaginada);
	
		result.use(Results.json()).from(resultadoLancamentoDiferenca, "result").recursive().serialize();
	}
	
	private List<DiferencaVO> processarListaDiferencas(Set<Diferenca> listaDiferencas){
		
		List<DiferencaVO> listaLancamentosDiferenca = new LinkedList<DiferencaVO>();
		
		for (Diferenca diferenca : listaDiferencas) {
			
			ProdutoEdicao produtoEdicao = diferenca.getProdutoEdicao();
			
			Produto produto = produtoEdicao.getProduto();
			
			DiferencaVO lancamentoDiferenca = new DiferencaVO();
			
			if (diferenca.getId() != null) {
				
				lancamentoDiferenca.setId(diferenca.getId());
			}
			
			lancamentoDiferenca.setCodigoProduto(produto.getCodigo());
			lancamentoDiferenca.setDescricaoProduto(produto.getDescricao());
			lancamentoDiferenca.setNumeroEdicao(produtoEdicao.getNumeroEdicao().toString());
			
			lancamentoDiferenca.setPrecoVenda(CurrencyUtil.formatarValor(produtoEdicao.getPrecoVenda()));
			
			lancamentoDiferenca.setPacotePadrao(String.valueOf(produtoEdicao.getPacotePadrao()));
			lancamentoDiferenca.setQuantidade(diferenca.getQtde());
			lancamentoDiferenca.setTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());
			
			if (diferenca.getMovimentoEstoque() != null) {
				
				Date dataLancamento = diferenca.getMovimentoEstoque().getData();
				
				if (dataLancamento != null) {
					
					lancamentoDiferenca.setDataLancamento(DateUtil.formatarDataPTBR(dataLancamento));
				}
			}
			
			lancamentoDiferenca.setValorTotalDiferenca(
				CurrencyUtil.formatarValor(diferenca.getValorTotalDiferenca()));
			
			lancamentoDiferenca.setVlTotalDiferenca(diferenca.getValorTotalDiferenca());
			
			lancamentoDiferenca.setCadastrado(true);
			
			listaLancamentosDiferenca.add(lancamentoDiferenca);
		}
		
		return listaLancamentosDiferenca;
	}
	
	/*
	 * Processa o resultado das diferenças.
	 *  
	 * @param listaDiferencas - lista de diferenças
	 * @param filtro - filtro da pesquisa
	 */
	private void processarDiferencas(List<Diferenca> listaDiferencas,
									 FiltroConsultaDiferencaEstoqueDTO filtro) {

		List<DiferencaVO> listaConsultaDiferenca = new LinkedList<DiferencaVO>();
		
		BigInteger qtdeTotalDiferencas = BigInteger.ZERO;
		BigDecimal valorTotalDiferencas = BigDecimal.ZERO;
		
		int quantidadeRegistros = diferencaEstoqueService.obterTotalDiferencas(filtro).intValue();
		
		for (Diferenca diferenca : listaDiferencas) {
			
			DiferencaVO consultaDiferencaVO = new DiferencaVO();
			
			consultaDiferencaVO.setId(diferenca.getId());
			
			consultaDiferencaVO.setDataLancamento(
				DateUtil.formatarData(diferenca.getMovimentoEstoque().getData(),
									  Constantes.DATE_PATTERN_PT_BR));
			
			consultaDiferencaVO.setCodigoProduto(diferenca.getProdutoEdicao().getProduto().getCodigo());
			consultaDiferencaVO.setDescricaoProduto(diferenca.getProdutoEdicao().getProduto().getNome());
			consultaDiferencaVO.setNumeroEdicao(diferenca.getProdutoEdicao().getNumeroEdicao().toString());
			
			consultaDiferencaVO.setPrecoVenda(
				CurrencyUtil.formatarValor(diferenca.getProdutoEdicao().getPrecoVenda()));
			
			consultaDiferencaVO.setTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());
			
			if (diferenca.getItemRecebimentoFisico() != null) {
				consultaDiferencaVO.setNumeroNotaFiscal(
					diferenca.getItemRecebimentoFisico().getItemNotaFiscal().getNotaFiscal().getNumero().toString());
			} else {
				consultaDiferencaVO.setNumeroNotaFiscal(" - ");
			}
			
			consultaDiferencaVO.setQuantidade(diferenca.getQtde());
			
			
			consultaDiferencaVO.setStatusAprovacao(
					(diferenca.getLancamentoDiferenca() != null) ?
						diferenca.getLancamentoDiferenca().getStatus().getDescricaoAbreviada() : "");
			
			consultaDiferencaVO.setMotivoAprovacao(diferenca.getMovimentoEstoque().getMotivo());
			
			consultaDiferencaVO.setValorTotalDiferenca(
				CurrencyUtil.formatarValor(diferenca.getValorTotalDiferenca()));
			
			consultaDiferencaVO.setTipoEstoque(diferenca.getTipoEstoque().getDescricao());
			
			listaConsultaDiferenca.add(consultaDiferencaVO);
			
			Fornecedor fornecedor = fornecedorService.obterFornecedorUnico(diferenca.getProdutoEdicao().getProduto().getCodigo());
			
			if(fornecedor != null)
				consultaDiferencaVO.setFornecedor(fornecedor.getJuridica().getNomeFantasia());
			
			qtdeTotalDiferencas = 
				qtdeTotalDiferencas.add(diferenca.getQtde());
			
			valorTotalDiferencas = valorTotalDiferencas.add(diferenca.getValorTotalDiferenca());
		}
		
		TableModel<CellModelKeyValue<DiferencaVO>> tableModel =
			new TableModel<CellModelKeyValue<DiferencaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaConsultaDiferenca));
		
		tableModel.setTotal(quantidadeRegistros);
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		String valorTotalDiferencasFormatado = 
			CurrencyUtil.formatarValorComSimbolo(valorTotalDiferencas);
		
		ResultadoDiferencaVO resultadoDiferencaVO = 
			new ResultadoDiferencaVO(tableModel, qtdeTotalDiferencas, valorTotalDiferencasFormatado);
	
		result.use(Results.json()).withoutRoot().from(resultadoDiferencaVO).recursive().serialize();
	}
	
	/*
	 * Carrega o filtro da pesquisa de lançamento de diferenças.
	 * 
	 * @param dataMovimento - data do movimento
	 * @param tipoDiferenca - tipo de diferença
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 * 
	 * @return Filtro
	 */
	private FiltroLancamentoDiferencaEstoqueDTO carregarFiltroPesquisaLancamentos(Date dataMovimento, 
																				  TipoDiferenca tipoDiferenca,
																				  String sortorder, 
																				  String sortname, 
																				  int page, 
																				  int rp) {
		
		FiltroLancamentoDiferencaEstoqueDTO filtroAtual = 
			new FiltroLancamentoDiferencaEstoqueDTO(dataMovimento, tipoDiferenca);
		
		this.configurarPaginacaoPesquisaLancamentos(filtroAtual, sortorder, sortname, page, rp);
		
		FiltroLancamentoDiferencaEstoqueDTO filtroSessao =
			(FiltroLancamentoDiferencaEstoqueDTO) 
				this.httpSession.getAttribute(FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
		
			filtroAtual.getPaginacao().setPaginaAtual(1);
			
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE, filtroAtual);
		
		return filtroAtual;
	}
	
	/*
	 * Carrega o filtro da pesquisa de consulta de diferenças.
	 * 
	 * @param codigoProduto - código do produto
	 * @param idFornecedor - identificador do fornecedor
	 * @param dataInicial - data de movimento inicial
	 * @param dataFinal - data de movimento final
	 * @param tipoDiferenca - tipo de diferença
	 * @param numeroCota - numero da cota
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 * 
	 * @return Filtro
	 */
	private FiltroConsultaDiferencaEstoqueDTO carregarFiltroPesquisa(String codigoProduto, 
																	 Long idFornecedor, String dataInicial,
																	 String dataFinal, TipoDiferenca tipoDiferenca, 
																	 Integer numeroCota, String nomeCota,
																	 String sortorder, String sortname,
																	 int page, int rp) {
		
		FiltroConsultaDiferencaEstoqueDTO filtroAtual =  new FiltroConsultaDiferencaEstoqueDTO();
		
		filtroAtual.setCodigoProduto(codigoProduto);
		filtroAtual.setIdFornecedor(idFornecedor);
		filtroAtual.setNumeroCota(numeroCota);
		filtroAtual.setNomeCota(nomeCota);
		
		if (!dataInicial.trim().isEmpty() && dataFinal.isEmpty()) {
			
			dataFinal = DateUtil.formatarDataPTBR(new Date());
		}
		
		filtroAtual.setPeriodoVO(
				new PeriodoVO(DateUtil.parseData(dataInicial, Constantes.DATE_PATTERN_PT_BR),
							  DateUtil.parseData(dataFinal, Constantes.DATE_PATTERN_PT_BR)));
		
		
		filtroAtual.setTipoDiferenca(tipoDiferenca);
		
		this.configurarPaginacaoPesquisa(filtroAtual, sortorder, sortname, page, rp);
		
		FiltroConsultaDiferencaEstoqueDTO filtroSessao =
			(FiltroConsultaDiferencaEstoqueDTO) 
				this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
		
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtroAtual);
		
		return filtroAtual;
	}
	
	/*
	 * Configura a paginação do filtro de pesquisa de lançamentos.
	 * 
	 * @param filtro - filtro da pesquisa
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 */
	private void configurarPaginacaoPesquisaLancamentos(FiltroLancamentoDiferencaEstoqueDTO filtro, 
														String sortorder,
														String sortname, 
														int page, 
														int rp) {
		
		if (filtro != null) {
			
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
	
			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaLancamento.values(), sortname));
		}
	}
	
	/*
	 * Configura a paginação do filtro de pesquisa.
	 * 
	 * @param filtro - filtro da pesquisa
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 */
	private void configurarPaginacaoPesquisa(FiltroConsultaDiferencaEstoqueDTO filtro, 
											 String sortorder,
											 String sortname, 
											 int page, 
											 int rp) {
		
		if (filtro != null) {
			
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
	
			filtro.setPaginacao(paginacao);
			
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaConsulta.values(), sortname));
		}
	}
	
	/*
	 * Valida a entrada de dados para pesquisa de lançamentos de diferença de estoque.
	 * 
	 * @param dataMovimentoFormatada - data de movimento formatado
	 */
	private void validarEntradaDadosPesquisaLancamentos(String dataMovimentoFormatada) {
		
		if (dataMovimentoFormatada == null 
				|| dataMovimentoFormatada.trim().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O preenchimento do campo [Data de Movimento] é obrigatório!");
		}
		
		if (!DateUtil.isValidDatePTBR(dataMovimentoFormatada)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Data de Movimento inválida");
		}
	}
	
	/*
	 * Valida a entrada de dados para pesquisa de diferença de estoque.
	 * 
	 * @param codigoProduto
	 * @param numeroEdicao
	 * @param idFornecedor
	 * @param dataInicial
	 * @param dataFinal
	 * @param tipoDiferenca
	 * 
	 */
	private void validarEntradaDadosPesquisa(String codigoProduto, 
											 Long idFornecedor, String dataInicial,
											 String dataFinal, TipoDiferenca tipoDiferenca) {
			
		if (dataInicial != null && !dataInicial.trim().isEmpty()
				&& !DateUtil.isValidDatePTBR(dataInicial)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Data Inicial inválida");
		}
		
		if (dataFinal != null && !dataFinal.trim().isEmpty()
				&& !DateUtil.isValidDatePTBR(dataFinal)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Data Final inválida");
		}
		
		if (!dataFinal.trim().isEmpty() && dataInicial.isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O preenchimento do campo [Data Inicial] é obrigatório!");
		}
		
		if (!dataInicial.trim().isEmpty() && dataFinal.isEmpty()) {
			
			if (DateUtil.parseDataPTBR(dataInicial).compareTo(new Date()) == 1) {
				
				throw new ValidacaoException(
					TipoMensagem.WARNING, "O campo [Data Inicial] não deve ser maior que a data do dia!");
			}
		}
		
		if (DateUtil.isDataInicialMaiorDataFinal(DateUtil.parseDataPTBR(dataInicial),
												 DateUtil.parseDataPTBR(dataFinal))) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O campo [Data Incial] não deve ser maior que o campo [Data Final]!");
		}
		
		if ((codigoProduto == null || codigoProduto.trim().isEmpty())  
				&& (dataInicial == null || dataInicial.trim().isEmpty())
				&& (dataFinal == null || dataFinal.trim().isEmpty())
				&& idFornecedor == null && tipoDiferenca == null) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING,
					"Para realizar a pesquisa é necessário informar um ou mais filtro(s) da pesquisa!");
		}
		
	}
	
	/**
	 * Valida a entrada de dados para pesquisa de lançamentos de diferença de estoque.
	 * 
	 * @param dataMovimentoFormatada - data de movimento formatado
	 * @param tipoDiferenca - tipo de diferença
	 */
	@Post
	@Path("/lancamento/novo/validar")
	public void validarEntradaDadosNovoLancamento(String dataMovimentoFormatada, TipoDiferenca tipoDiferenca) {
		
		if (dataMovimentoFormatada == null 
				|| dataMovimentoFormatada.trim().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O preenchimento do campo [Data de Movimento] é obrigatório!");
		}
		
		if (tipoDiferenca == null) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O preenchimento do campo [Tipo de Diferença] é obrigatório!");
		}
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	
	/*
	 * Valida o preenchimentos dos novos rateios.
	 * 
	 * @param listaNovosRateios - lista dos novos rateios
	 */
	private void validarPreenchimentoNovosRateios(List<RateioCotaVO> listaNovosRateios) {

		List<Long> linhasComErro = new ArrayList<Long>();

		for (RateioCotaVO rateioCotaVO : listaNovosRateios) {
			
			boolean rateioInvalido = false;
			
			if (rateioCotaVO.getNumeroCota() == null) {
				
				rateioInvalido = true;
			}
			
			if (rateioCotaVO.getNomeCota() == null
					|| rateioCotaVO.getNomeCota().trim().isEmpty()) {
				
				rateioInvalido = true;
			}
			
			if (rateioCotaVO.getReparteCota() == null) {
				
				rateioInvalido = true;
			}
			
			if (rateioCotaVO.getQuantidade() == null
					|| BigDecimal.ZERO.equals(rateioCotaVO.getQuantidade())) {
				
				rateioInvalido = true;
			}
			
			if (rateioInvalido) {
				
				linhasComErro.add(rateioCotaVO.getId());
			}
		}
		
		if (!linhasComErro.isEmpty()) {
			
			ValidacaoVO validacao = 
				new ValidacaoVO(TipoMensagem.WARNING, "Existe(m) rateio(s) preenchido(s) incorretamente!");
			
			validacao.setDados(linhasComErro);
			
			throw new ValidacaoException(validacao);
		}
	}
	
	/*
	 * Efetua a validação da somatória das quantidades 
	 * do rateio x quantidade da diferença.
	 * 
	 * @param listaNovosRateios - lista do nos rateios
	 * @param idDiferenca - id da diferença
	 */
	private void validarSomaQuantidadeRateio(List<RateioCotaVO> listaNovosRateios, DiferencaVO diferencaVO ) {
		
		BigInteger somaQtdeRateio = BigInteger.ZERO;
		
		for (RateioCotaVO rateioCotaVO : listaNovosRateios) {
			
			somaQtdeRateio = somaQtdeRateio.add(rateioCotaVO.getQuantidade());
		}
		
		if (somaQtdeRateio.compareTo(diferencaVO.getQuantidade()) > 0) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "A somatória das quantidades de rateio (" 
					+ somaQtdeRateio + ") é maior que a quantidade da diferença (" 
					+ diferencaVO.getQuantidade() + ")!");
		}
	}
	
	/*
	 * Valida se há cotas duplicadas no rateio.
	 * 
	 * @param listaNovosRateios - lista dos novos rateios
	 */
	@SuppressWarnings("unchecked")
	private void validarCotasDuplicadasRateio(List<RateioCotaVO> listaNovosRateios) {
		
		if (listaNovosRateios == null) {
			
			return;
		}
		
		Collections.sort(listaNovosRateios, new BeanComparator("numeroCota"));
		
		List<Long> linhasComErro = new ArrayList<Long>();
		
		RateioCotaVO ultimoRateioCotaVO = null;
		
		for (RateioCotaVO rateioCotaVO : listaNovosRateios) {
			
			Integer numeroCota = rateioCotaVO.getNumeroCota();
			
			if (numeroCota == null) {
				
				continue;
			}
			
			if (ultimoRateioCotaVO != null) {
				
				if (numeroCota.equals(ultimoRateioCotaVO.getNumeroCota())) {
					
					linhasComErro.add(ultimoRateioCotaVO.getId());
					linhasComErro.add(rateioCotaVO.getId());
				}
			}
			
			ultimoRateioCotaVO = rateioCotaVO;
		}
		
		if (!linhasComErro.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, "Existem cotas duplicadas para o rateio!");
			
			validacao.setDados(linhasComErro);
		
			throw new ValidacaoException(validacao);
		}
	}
	
	/*
	 * Valida o cadastro de um novo rateio.
	 * 
	 * @param novoRateioCota - novo rateio
	 */
	private void validarNovoRateio(RateioCotaVO novoRateioCota, DiferencaVO diferencaVO) {
		
		List<Long> linhasComErro = new ArrayList<Long>();
		
		List<String> listaMensagensErro = new ArrayList<String>();
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(novoRateioCota.getNumeroCota());
		
		if (cota == null) {
			
			linhasComErro.add(novoRateioCota.getId());
			
			listaMensagensErro.add("Cota inválida: Número [" + novoRateioCota.getNumeroCota() + "] - Nome [" + novoRateioCota.getNomeCota() + " ]");
		}
		
		TipoDiferenca tipoDiferenca =
			Util.getEnumByStringValue(TipoDiferenca.values(), diferencaVO.getTipoDiferenca());
		
		if (TipoDiferenca.FALTA_DE.equals(tipoDiferenca)
				|| TipoDiferenca.FALTA_EM.equals(tipoDiferenca)) {
			
			if (novoRateioCota.getReparteCota() == null
					|| novoRateioCota.getQuantidade().compareTo(novoRateioCota.getReparteCota()) > 0) {
				
				linhasComErro.add(novoRateioCota.getId());
				
				listaMensagensErro.add("Quantidade do rateio para o tipo de diferença '" +
					tipoDiferenca.getDescricao() + "' não pode ser maior que a quantidade do reparte da cota!");
			}
		}
		
		if (!linhasComErro.isEmpty() && !listaMensagensErro.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaMensagensErro);
		
			validacao.setDados(linhasComErro);
		
			throw new ValidacaoException(validacao);
		}
	}
	
	private void validarNovaDiferenca(DiferencaVO diferenca, 
			  						  Date dataMovimento,
			  						  TipoDiferenca tipoDiferenca){
		
		List<String> listaMensagensErro = new ArrayList<String>();
		
		ProdutoEdicao produtoEdicao =
			this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
				diferenca.getCodigoProduto(), diferenca.getNumeroEdicao());
		
		if (produtoEdicao == null) {
			
			listaMensagensErro.add("Produto inválido: Código [" + diferenca.getCodigoProduto() + "] - Edição [" + diferenca.getNumeroEdicao() + " ]");
		}

		if (TipoDiferenca.FALTA_DE.equals(tipoDiferenca)
				|| TipoDiferenca.FALTA_EM.equals(tipoDiferenca)) {
		
			if (diferenca.getQtdeEstoqueAtual() == null 
					|| (diferenca.getQuantidade().compareTo(diferenca.getQtdeEstoqueAtual()) > 0)) {
				
				listaMensagensErro.add(
					"Quantidade de Exemplares para o tipo de diferença '" + tipoDiferenca.getDescricao() 
						+ "' não pode ser maior que a Quantidade em Estoque do produto!");
			}
		}
		
		if (!this.diferencaEstoqueService.validarDataLancamentoDiferenca(
				dataMovimento, produtoEdicao.getId(), tipoDiferenca)) {
			
			listaMensagensErro.add("Prazo para lançamento de diferença esgotado para o Produto: Código [" + diferenca.getCodigoProduto() + "] - Edição [" + diferenca.getNumeroEdicao() + " ]");
		}
		
		if (!listaMensagensErro.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaMensagensErro);
		
			throw new ValidacaoException(validacao);
		}
	}
		
	@SuppressWarnings("unchecked")
	private void validarProdutoDuplicadoLancamento(Set<DiferencaVO> listaNovasDiferencas){
		
		List<DiferencaVO> listaDiferencas = new ArrayList<DiferencaVO>();
		
		if (listaNovasDiferencas == null) {
			
			return;
		}
	
		Set<DiferencaVO> listaDiferencasCadastradas =
			(Set<DiferencaVO>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
		
		if (listaDiferencasCadastradas != null) {
			listaDiferencas.addAll(listaNovasDiferencas);
			listaDiferencas.addAll(listaDiferencasCadastradas);
		}
		
		ComparatorChain comparatorChain = new ComparatorChain();
		
		comparatorChain.addComparator(new BeanComparator("codigoProduto"));
		comparatorChain.addComparator(new BeanComparator("numeroEdicao"));
		comparatorChain.addComparator(new BeanComparator("tipoDirecionamento"));
		
		Collections.sort(listaDiferencas, comparatorChain);
		
		List<Long> linhasComErro = new ArrayList<Long>();
		
		DiferencaVO ultimaDiferencaVO = null;
		
		for (DiferencaVO diferencaVO : listaDiferencas) {
			
			if (ultimaDiferencaVO != null) {
				
				if (diferencaVO.getCodigoProduto().trim().equalsIgnoreCase(ultimaDiferencaVO.getCodigoProduto())
						&& diferencaVO.getNumeroEdicao().trim().equalsIgnoreCase(ultimaDiferencaVO.getNumeroEdicao())
						&& diferencaVO.getTipoDirecionamento().equals(ultimaDiferencaVO.getTipoDirecionamento())) {
					
					if (!ultimaDiferencaVO.isCadastrado()) {
						
						linhasComErro.add(ultimaDiferencaVO.getId());
					}
					
					if (!diferencaVO.isCadastrado()) {
						
						linhasComErro.add(diferencaVO.getId());
					}
				}
			}
			
			ultimaDiferencaVO = diferencaVO;
		}
		
		if (!linhasComErro.isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Existem produtos duplicados para o lançamento!");
		}
	}
		
	/*
	 * Obtém uma diferença pesquisada pelo seu id.
	 * 
	 * @param idDiferenca - id da diferença
	 * 
	 * @return DiferencaVO
	 */
	@SuppressWarnings("unchecked")
	private DiferencaVO obterDiferencaPorId(Long idDiferenca) {
		
		List<DiferencaVO> listaDiferencas =
			(List<DiferencaVO>) this.httpSession.getAttribute(LISTA_DIFERENCAS_PESQUISADAS_SESSION_ATTRIBUTE);
		
		if (listaDiferencas == null || listaDiferencas.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existem lançamentos de diferença");
		}
		
		for (DiferencaVO diferenca : listaDiferencas) {
			
			if (diferenca.getId().equals(idDiferenca)) {
				
				return diferenca;
			}
		}
		
		return null;
	}
	
	/*
	 * Limpa os dados da sessão.
	 */
	private void limparSessao() {
	
		this.httpSession.removeAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
		
		this.httpSession.removeAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
		
		this.httpSession.removeAttribute(LISTA_DIFERENCAS_PESQUISADAS_SESSION_ATTRIBUTE);
		
		this.httpSession.removeAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		this.httpSession.removeAttribute(MODO_INCLUSAO_SESSION_ATTRIBUTE);
	}
	
	/*
	 * Obtém os dados do cabeçalho de exportação.
	 * 
	 * @return NDSFileHeader
	 */
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
	}
	
	//TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		
		usuario.setNome("Jornaleiro da Silva");
		
		return usuario;
	}
	
	@Post
	@Path("/lancamento/rateio/buscarDiferenca")
	public void buscarDiferenca(Long idDiferenca){
		
		DiferencaVO diferencaVO = null;
		
		if (idDiferenca == null){
			
			diferencaVO = new DiferencaVO();
//			diferencaVO.setNumeroEdicao("2");
//			diferencaVO.setPrecoVenda("20.34");
//			diferencaVO.setDescricaoProduto("tchans");
//			diferencaVO.setQuantidade(BigDecimal.TEN);
//			diferencaVO.setCodigoProduto("códigueta");
		} else {
			//TODO
			diferencaVO = new DiferencaVO();
			this.diferencaEstoqueService.obterDiferenca(idDiferenca);
		}
		
		result.use(Results.json()).from(diferencaVO, "result").recursive().serialize();
	}
	
	@Post
	@Path("/lancamento/rateio/buscarReparteCotaPreco")
	public void buscarReparteCotaPreco(Long idProdutoEdicao, Integer numeroCota){
		
		
		if(numeroCota == null)
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Cota deve ser informada."));
				
		Long qtde = movimentoEstoqueCotaService.obterQuantidadeReparteProdutoCota(idProdutoEdicao, numeroCota);
				
		ProdutoEdicao pe = produtoEdicaoService.obterProdutoEdicao(idProdutoEdicao);
		
		Object[] dados = new Object[2];
		dados[0] = qtde;
		dados[1] = CurrencyUtil.formatarValor(pe.getPrecoVenda());
		
		result.use(Results.json()).from(dados, "result").serialize();
	}
	
	@Post
	@Path("/lancamento/rateio/buscarPrecoProdutoEdicao")
	public void buscarPrecoProdutoEdicao(String codigoProduto, Integer numeroEdicao){
		
		ProdutoEdicao pe = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao.toString());
		
		if(pe == null)
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Edição não encontrada."));
		
		Long qtde = movimentoEstoqueCotaService.obterQuantidadeReparteProdutoCota(pe.getId(), null);
				
		Object[] dados = new Object[3];
		dados[0] = qtde;
		dados[1] = CurrencyUtil.formatarValor(pe.getPrecoVenda());
		dados[2] = pe.getId();
		
		result.use(Results.json()).from(dados, "result").serialize();
	}
	
	@Post
	@Path("/lancamento/rateio/buscarProdutosCotaNota")
	public void	buscarProdutosCotaNota(Date dateNotaEnvio, Integer numeroCota){
		
		//TODO Aguardando o desenvolvimento da EMS 191
		List<DiferencaVO> prods = new ArrayList<DiferencaVO>();
		
		DiferencaVO diferencaVO = new DiferencaVO();
		diferencaVO.setCodigoProduto("123");
		diferencaVO.setDescricaoProduto("nome produto");
		diferencaVO.setNumeroEdicao("2");
		diferencaVO.setPrecoVenda("12,98");
		diferencaVO.setQuantidade(BigInteger.TEN);
		
		prods.add(diferencaVO);
		
		diferencaVO = new DiferencaVO();
		diferencaVO.setCodigoProduto("123");
		diferencaVO.setDescricaoProduto("nome produto");
		diferencaVO.setNumeroEdicao("2");
		diferencaVO.setPrecoVenda("12.98");
		diferencaVO.setQuantidade(BigInteger.TEN);
		
		prods.add(diferencaVO);
		
		result.use(FlexiGridJson.class).from(prods).total(prods.size()).page(1).serialize();
	}
	
	@Post
	public void obterDetalhes(Long idDiferenca) {
		result.use(Results.json()).from("", "result").serialize();
	}
}
