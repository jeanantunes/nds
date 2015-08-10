package br.com.abril.nds.controllers.expedicao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ResultadoResumoExpedicaoVO;
import br.com.abril.nds.client.vo.ResumoExpedicaoBoxVO;
import br.com.abril.nds.client.vo.ResumoExpedicaoDetalheVO;
import br.com.abril.nds.client.vo.ResumoExpedicaoVO;
import br.com.abril.nds.client.vo.RetornoExpedicaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO.TipoPesquisaResumoExpedicao;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.ExpedicaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações/requisições referente a 
 * tela de resumo de expedição de produto/box 
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path(value="/expedicao")
@Rules(Permissao.ROLE_EXPEDICAO_RESUMO_EXPEDICAO)
public class ResumoExpedicaoController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private ExpedicaoService expedicaoService;
	
	@Autowired
	private DistribuidorService distribuidorService;  
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroResumo";

	/**
	 * Carrega as informações default da tela de pesquisa.
	 */
	@Get
	@Path("/")
	public void resumo(){
		
		Date dataLancamentoResumo = distribuidorService.obterDataOperacaoDistribuidor();
		
		result.include("dataLancamentoResumo", dataLancamentoResumo);
		
		carregarTiposResumo();
		
	}
	
	@Post
	@Path("/resumo/pesquisar/detalhe")
	public void detalharResumoExpedicaoDoBox(
			String sortorder, 
			String sortname, 
			int page, 
			int rp, 
			Integer codigoBox, 
			String dataLancamento) {
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		
		filtro.setDataLancamento(DateUtil.parseData(dataLancamento, "dd/MM/yyyy"));
		
		filtro.setCodigoBox(codigoBox);
		
		this.configurarPaginacaoPesquisaResumoProduto(filtro, sortorder, sortname, page, rp);
		
		this.tratarFiltro(filtro);
		
		List<ExpedicaoDTO> list = expedicaoService.obterResumoExpedicaoProdutosDoBox(filtro);
		
		List<ResumoExpedicaoDetalheVO> listaDetalheResumoExpedicaoDoBox = this.pesquisarDetalheResumoExpedicaoDoBox(filtro, list);
		
		Long quantidadeRegistros = expedicaoService.obterQuantidadeResumoExpedicaoProdutosDoBox(filtro);
		
		TableModel<CellModelKeyValue<ResumoExpedicaoDetalheVO>> tableModel = 
				new TableModel<CellModelKeyValue<ResumoExpedicaoDetalheVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDetalheResumoExpedicaoDoBox));
		tableModel.setTotal((quantidadeRegistros!= null)? quantidadeRegistros.intValue():0);
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		ExpedicaoDTO expedicaoDTO = this.expedicaoService.obterTotaisResumoExpedicaoProdutosDoBox(filtro);
		
		Map<String, Object> mapa = new TreeMap<String, Object>();
		mapa.put("resultado", tableModel);
		mapa.put("somaTotal", CurrencyUtil.formatarValor(expedicaoDTO.getValorFaturado()));
		
		result.use(CustomJson.class).from(mapa).serialize();
	}
	
	private List<ResumoExpedicaoDetalheVO> pesquisarDetalheResumoExpedicaoDoBox(FiltroResumoExpedicaoDTO filtro, List<ExpedicaoDTO> list){
		
		if (list == null || list.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<ResumoExpedicaoDetalheVO> listaLancamentosExpedidos = new LinkedList<ResumoExpedicaoDetalheVO>();
		
		ResumoExpedicaoDetalheVO resumoExpedicaoDetalheVO = null;
		
		for (ExpedicaoDTO expd  : list){
			
			BigDecimal precoDesconto = expd.getPrecoCapa();
			
			if (expd.getDesconto() != null) {
				precoDesconto = expd.getPrecoCapa().subtract( expd.getPrecoCapa().multiply( expd.getDesconto().divide(new BigDecimal("100")) ) );
			}
			
			resumoExpedicaoDetalheVO = new ResumoExpedicaoDetalheVO();
			
			resumoExpedicaoDetalheVO.setNomeFornecedor(expd.getRazaoSocial());
			resumoExpedicaoDetalheVO.setCodigoProduto(expd.getCodigoProduto());
			resumoExpedicaoDetalheVO.setDescricaoProduto(expd.getNomeProduto());
			resumoExpedicaoDetalheVO.setEdicaoProduto(getValor(expd.getNumeroEdicao()));
			resumoExpedicaoDetalheVO.setPrecoCapa(CurrencyUtil.formatarValor(expd.getPrecoCapa()));
			resumoExpedicaoDetalheVO.setPrecoDesconto(CurrencyUtil.formatarValor(precoDesconto));
			resumoExpedicaoDetalheVO.setReparte(getValor(expd.getQntReparte()));
			resumoExpedicaoDetalheVO.setValorFaturado(CurrencyUtil.formatarValor(expd.getValorFaturado()));
			resumoExpedicaoDetalheVO.setQntDiferenca(getValor(expd.getQntDiferenca()));
			
			listaLancamentosExpedidos.add(resumoExpedicaoDetalheVO);
		}
	
		return listaLancamentosExpedidos;
		

	}
	
	
	/**
	 * Pesquisa de resumo de expedição de edições agrupadas por produto.
	 * 
	 * @param filtro
	 * @param tipoPesquisa
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	@Post
	@Path("/resumo/pesquisar/produto")
	public void pesquisarResumoExpedicaoProduto(String dataLancamento, String sortorder, String sortname, int page, int rp){
		
		this.validarPrametroPesquisa(dataLancamento);
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		
		filtro.setDataLancamento(DateUtil.parseData(dataLancamento, "dd/MM/yyyy"));
		
		this.configurarPaginacaoPesquisaResumoProduto(filtro, sortorder, sortname, page, rp);
		
		this.tratarFiltro(filtro);
		
		this.pesquisarResumoProduto(filtro);
		
	}
	
	/**
	 * Pesquisa de resumo de expedição de edições agrupadas por box.
	 * 
	 * @param dataLancamento
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	@Post
	@Path("/resumo/pesquisar/box")
	public void pesquisarResumoExpedicaoBox(String dataLancamento,String sortorder, String sortname, int page, int rp){
		
		this.validarPrametroPesquisa(dataLancamento);
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		
		filtro.setDataLancamento(DateUtil.parseData(dataLancamento, "dd/MM/yyyy"));
		
		this.configurarPaginacaoPesquisaResumoBox(filtro, sortorder, sortname, page, rp);
		
		this.tratarFiltro(filtro);
		
		this.pesquisarResumoBox(filtro);
	}
	
	@Get
	@Path("/resumo/exportar")
	public void exportar(FileType fileType, TipoPesquisaResumoExpedicao tipoConsulta) throws IOException {
		
		if (fileType == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de arquivo não encontrado!");
		}
		
		if (tipoConsulta == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de consulta não encontrado!");
		}
		
		FiltroResumoExpedicaoDTO filtroSessao = 
			(FiltroResumoExpedicaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null) { 
				
			if (filtroSessao.getPaginacao() != null) {
			
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
			filtroSessao.setTipoConsulta(tipoConsulta);
		}
		
		if (TipoPesquisaResumoExpedicao.PRODUTO.equals(tipoConsulta)) {
			
			this.exportarResumoExpedicaoProduto(fileType, filtroSessao);
			
		} else if (TipoPesquisaResumoExpedicao.BOX.equals(tipoConsulta)) {
			
			this.exportarResumoExpedicaoBox(fileType, filtroSessao);
			
		} else {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de consulta inválido!");
		}
		result.nothing();
	}
	
	private void exportarResumoExpedicaoProduto(FileType fileType, 
												FiltroResumoExpedicaoDTO filtro) throws IOException {
		
		RetornoExpedicaoVO retornoExpedicaoVO  = getLancamentosExpedidosProduto(filtro);
		
		List<ResumoExpedicaoVO> listaLancamentosExpedidos = retornoExpedicaoVO.getResumosExpedicao(); 
			
		String valorTotalFaturadoFormatado = CurrencyUtil.formatarValor(retornoExpedicaoVO.getTotalValorFaturado());
		
		ResultadoResumoExpedicaoVO<ResumoExpedicaoVO> resultadoResumoExpedicao = 
			new ResultadoResumoExpedicaoVO<ResumoExpedicaoVO>(
				null, retornoExpedicaoVO.getTotalReparte().intValue(), valorTotalFaturadoFormatado);
		
		FileExporter.to("resumo-expedicao-produto", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtro, resultadoResumoExpedicao, 
				listaLancamentosExpedidos, ResumoExpedicaoVO.class, this.httpServletResponse);
	}
	
	private void exportarResumoExpedicaoBox(FileType fileType, 
											FiltroResumoExpedicaoDTO filtro) throws IOException {
		
		RetornoExpedicaoVO retornoExpedicaoVO  = getLancamentosExpedidosBox(filtro);
		
		List<ResumoExpedicaoBoxVO> listaLancamentosExpedidos = retornoExpedicaoVO.getResumosExpedicaoBox();
		
		String valorTotalFaturadoFormatado = CurrencyUtil.formatarValor(retornoExpedicaoVO.getTotalValorFaturado());
		
		ResultadoResumoExpedicaoVO<ResumoExpedicaoBoxVO> resultadoResumoExpedicao = 
			new ResultadoResumoExpedicaoVO<ResumoExpedicaoBoxVO>(
				null, retornoExpedicaoVO.getTotalReparte().intValue(), valorTotalFaturadoFormatado);
		
		FileExporter.to("resumo-expedicao-box", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtro, resultadoResumoExpedicao, 
				listaLancamentosExpedidos, ResumoExpedicaoBoxVO.class, this.httpServletResponse);
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroResumoExpedicaoDTO filtroResumoExpedicao) {

		FiltroResumoExpedicaoDTO filtroResumoExpedicaoSession = (FiltroResumoExpedicaoDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroResumoExpedicaoSession != null
				&& !filtroResumoExpedicaoSession.equals(filtroResumoExpedicao)) {

			filtroResumoExpedicao.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroResumoExpedicao);
	}
	
	/**
	 * Verifica se os parametros de pesquisas são validos.
	 * 
	 * @param dataLancamento
	 */
	private void validarPrametroPesquisa(String dataLancamento){
		
		
		if (dataLancamento == null || dataLancamento.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento do campo \"Data Lançamento\" é obrigatório." );
		}
		
		if (!DateUtil.isValidDate(dataLancamento, "dd/MM/yyyy")) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Data Lançamento inválida." );
		}
	}
	
	/**
	 * Monta os dados de pesquisa de box expedidos para exibição na tela.
	 * 
	 * @param page
	 */
	private void pesquisarResumoBox(FiltroResumoExpedicaoDTO filtro){

		RetornoExpedicaoVO retornoExpedicaoVO  = getLancamentosExpedidosBox(filtro);
		
		List<ResumoExpedicaoBoxVO> listaLancamentosExpedidosBox = retornoExpedicaoVO.getResumosExpedicaoBox();
		
		TableModel<CellModelKeyValue<ResumoExpedicaoBoxVO>> tableModel = 
			new TableModel<CellModelKeyValue<ResumoExpedicaoBoxVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentosExpedidosBox));
		
		tableModel.setPage(1);
		
		tableModel.setTotal(listaLancamentosExpedidosBox.size());

		String valorTotalFaturadoFormatado = CurrencyUtil.formatarValor(retornoExpedicaoVO.getTotalValorFaturado());
 
		ResultadoResumoExpedicaoVO<ResumoExpedicaoBoxVO> resultadoResumoExpedicao = 
			new ResultadoResumoExpedicaoVO<ResumoExpedicaoBoxVO>(
				tableModel, (retornoExpedicaoVO.getTotalReparte() == null)?0:retornoExpedicaoVO.getTotalReparte().intValue(), valorTotalFaturadoFormatado);

		result.use(Results.json()).withoutRoot().from(resultadoResumoExpedicao).recursive().serialize();
	}
	
	/**
	 * Obtém a lista de lançamentos expedidos pro box.
	 * 
	 * @param filtro - filtro
	 * @param qtdeTotalReparte - qtde total do reparte
	 * @param valorTotalFaturado - valor total faturado
	 * 
	 * @return Lista de ResumoExpedicaoBoxVO
	 */
	private RetornoExpedicaoVO getLancamentosExpedidosBox(FiltroResumoExpedicaoDTO filtro ) {
		
		List<ExpedicaoDTO> list = expedicaoService.obterResumoExpedicaoPorBox(filtro);
		
		if (list == null || list.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<ResumoExpedicaoBoxVO> listaLancamentosExpedidosBox = new LinkedList<ResumoExpedicaoBoxVO>();
		
		ResumoExpedicaoBoxVO resumoExpedicaoBoxVO = null;
		
		for (ExpedicaoDTO expd  : list){
			
			
			resumoExpedicaoBoxVO = new ResumoExpedicaoBoxVO();
			
			resumoExpedicaoBoxVO.setDataLancamento(DateUtil.formatarDataPTBR(expd.getDataLancamento()));
			resumoExpedicaoBoxVO.setCodigoBox(Util.nvl(expd.getCodigoBox(),"").toString());
			resumoExpedicaoBoxVO.setDescricaoBox(expd.getNomeBox());
			resumoExpedicaoBoxVO.setQntProduto(getValor(expd.getQntProduto()));
			resumoExpedicaoBoxVO.setReparte(getValor(expd.getQntReparte()));
			resumoExpedicaoBoxVO.setValorFaturado(CurrencyUtil.formatarValor(expd.getValorFaturado()));
			resumoExpedicaoBoxVO.setQntDiferenca(getValor(expd.getQntDiferenca()));
			
			listaLancamentosExpedidosBox.add(resumoExpedicaoBoxVO);
		}
		
		ExpedicaoDTO expedicaoDTO = this.expedicaoService.obterTotaisResumoExpedicaoPorProduto(filtro);
		
		RetornoExpedicaoVO retornoExpedicaoVO = new RetornoExpedicaoVO();
		
		retornoExpedicaoVO.setResumosExpedicaoBox(listaLancamentosExpedidosBox);
		retornoExpedicaoVO.setTotalReparte(expedicaoDTO.getQntReparte());
		retornoExpedicaoVO.setTotalValorFaturado(expedicaoDTO.getValorFaturado());
		
		return retornoExpedicaoVO;
	}
	
	/**
	 * Monta os dados de pesquisa de produtos expedidos para exibição na tela.
	 * 
	 * @param page
	 */
	private void pesquisarResumoProduto(FiltroResumoExpedicaoDTO filtro){
				
		RetornoExpedicaoVO retornoExpedicaoVO = getLancamentosExpedidosProduto(filtro); 
		
		List<ResumoExpedicaoVO> listaLancamentosExpedidos = retornoExpedicaoVO.getResumosExpedicao();
		
		Long quantidadeRegistros = expedicaoService.obterQuantidadeResumoExpedicaoPorProduto(filtro);
		
		TableModel<CellModelKeyValue<ResumoExpedicaoVO>> tableModel = new TableModel<CellModelKeyValue<ResumoExpedicaoVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentosExpedidos));

		tableModel.setTotal((quantidadeRegistros!= null)? quantidadeRegistros.intValue():0);

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		String valorTotalFaturadoFormatado = CurrencyUtil.formatarValor(retornoExpedicaoVO.getTotalValorFaturado());

		ResultadoResumoExpedicaoVO<ResumoExpedicaoVO> resultadoResumoExpedicao = 
			new ResultadoResumoExpedicaoVO<ResumoExpedicaoVO>(
				tableModel, (retornoExpedicaoVO.getTotalReparte() == null)?0: retornoExpedicaoVO.getTotalReparte().intValue(), valorTotalFaturadoFormatado);

		result.use(Results.json()).withoutRoot().from(resultadoResumoExpedicao).recursive().serialize();

	}
	
	/**
	 * Obtém a lista de lançamentos expedidos por produto.
	 * 
	 * @param filtro - filtro
	 * @param qtdeTotalReparte - qtde total do reparte
	 * @param valorTotalFaturado - valor total faturado
	 * 
	 * @return Lista de ResumoExpedicaoBoxVO
	 */
	private RetornoExpedicaoVO getLancamentosExpedidosProduto(FiltroResumoExpedicaoDTO filtro) {
		
		List<ExpedicaoDTO> list = expedicaoService.obterResumoExpedicaoPorProduto(filtro);
		
		if (list == null || list.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<ResumoExpedicaoVO> listaLancamentosExpedidos = new LinkedList<ResumoExpedicaoVO>();
		
		ResumoExpedicaoVO resumoExpedicaoVO = null;
		
		for (ExpedicaoDTO expd  : list) {
			
			resumoExpedicaoVO = new ResumoExpedicaoVO();
			
			resumoExpedicaoVO.setCodigoProduto(expd.getCodigoProduto());
			resumoExpedicaoVO.setDescricaoProduto(expd.getNomeProduto());
			resumoExpedicaoVO.setEdicaoProduto(getValor(expd.getNumeroEdicao()));
			resumoExpedicaoVO.setPrecoCapa(expd.getPrecoCapa() != null ? CurrencyUtil.formatarValor(expd.getPrecoCapa()) : "0");
			resumoExpedicaoVO.setReparte(expd.getQntReparte() == null ? BigInteger.ZERO: expd.getQntReparte());
			resumoExpedicaoVO.setQntDiferenca(expd.getQntDiferenca());
			
			BigDecimal valorDiferenca = expd.getPrecoCapa().multiply(new BigDecimal(expd.getQntDiferenca()));
			
			resumoExpedicaoVO.setValorFaturado(CurrencyUtil.formatarValor((expd.getValorFaturado() == null) ? valorDiferenca : expd.getValorFaturado().add(valorDiferenca)));
			
			listaLancamentosExpedidos.add(resumoExpedicaoVO);
		}
		
		ExpedicaoDTO expedicaoDTO = this.expedicaoService.obterTotaisResumoExpedicaoPorProduto(filtro);
		
		RetornoExpedicaoVO expedicaoVO = new RetornoExpedicaoVO();
		
		expedicaoVO.setResumosExpedicao(listaLancamentosExpedidos);
		expedicaoVO.setTotalReparte(expedicaoDTO.getQntReparte());
		expedicaoVO.setTotalValorFaturado(expedicaoDTO.getValorFaturado());
		
		return expedicaoVO;
	}
	
	/**
	 * Retorna um valor no formato de uma String
	 * @param valor
	 * @return
	 */
	private String getValor(Number valor ){
		
		return (valor!= null)  ?String.valueOf(valor.intValue()):"";
	}
	
	/**
	 * Configura paginação da lista de resumo de expedição por produto.
	 * 
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoPesquisaResumoProduto(FiltroResumoExpedicaoDTO filtro, 
														 String sortorder,String sortname, 
														 int page, int rp) {

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
			
			filtro.setOrdenacaoColunaProduto(Util.getEnumByStringValue(FiltroResumoExpedicaoDTO.OrdenacaoColunaProduto.values(), sortname));
			
		}
	}
	
	
	/**
	 * Configura paginação da lista de resumo se expedição por box.
	 * 
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoPesquisaResumoBox(FiltroResumoExpedicaoDTO filtro, 
														  String sortorder, String sortname,
														  int page, int rp) {

		if (filtro != null) {

			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColunaBox(Util.getEnumByStringValue(FiltroResumoExpedicaoDTO.OrdenacaoColunaBox.values(),sortname));
		}
	}
		
	/**
	 * Carrega o combo de Tipo de Consulta.
	 */
	private void carregarTiposResumo(){
		
		List<ItemDTO<TipoPesquisaResumoExpedicao, String>> listaTipoResumo = new ArrayList<ItemDTO<TipoPesquisaResumoExpedicao,String>>();
		
		listaTipoResumo.add(new ItemDTO<TipoPesquisaResumoExpedicao, String>(TipoPesquisaResumoExpedicao.BOX,TipoPesquisaResumoExpedicao.BOX.getNome()));
		listaTipoResumo.add( new ItemDTO<TipoPesquisaResumoExpedicao, String>(TipoPesquisaResumoExpedicao.PRODUTO,TipoPesquisaResumoExpedicao.PRODUTO.getNome()));
		
		result.include("listaTipoResumo",listaTipoResumo );
	}
	
	@Get
	@Path("/resumo/exportarDetalhes")
	public void exportarDetalhes(FileType fileType, String codigoBox, Date dataLancamento) throws IOException {
		
		if (fileType == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de arquivo não encontrado!");
		}
		
		FiltroResumoExpedicaoDTO filtroSessao = 
				(FiltroResumoExpedicaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
			
		if (filtroSessao != null) { 
				
			if (filtroSessao.getPaginacao() != null) {
			
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
			filtroSessao.setTipoConsulta(null);
		}

		List<ExpedicaoDTO> list = expedicaoService.obterResumoExpedicaoProdutosDoBox(filtroSessao);
		
		List<ResumoExpedicaoDetalheVO> listaDetalheResumoExpedicaoDoBox = pesquisarDetalheResumoExpedicaoDoBox(filtroSessao, list);
		
		FileExporter.to("consulta-detalhes-expedição-box", fileType)
			.inHTTPResponse(
					this.getNDSFileHeader(), 
					null, 
					null, 
					listaDetalheResumoExpedicaoDoBox, 
					ResumoExpedicaoDetalheVO.class, 
					this.httpServletResponse);
	}

}