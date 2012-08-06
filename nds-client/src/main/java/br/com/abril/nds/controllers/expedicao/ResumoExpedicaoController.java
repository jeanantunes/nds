package br.com.abril.nds.controllers.expedicao;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ConsultaNotaFiscalVO;
import br.com.abril.nds.client.vo.ResultadoResumoExpedicaoVO;
import br.com.abril.nds.client.vo.ResumoExpedicaoBoxVO;
import br.com.abril.nds.client.vo.ResumoExpedicaoDetalheVO;
import br.com.abril.nds.client.vo.ResumoExpedicaoVO;
import br.com.abril.nds.client.vo.RetornoExpedicaoVO;
import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO.TipoPesquisaResumoExpedicao;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.ExpedicaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
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
public class ResumoExpedicaoController {
	
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
	public void resumo(){
		
		carregarTiposResumo();
	}
	
	@Post
	@Path("/resumo/pesquisar/detalhe")
	public void detalharResumoExpedicaoDoBox(Integer codigoBox, String dataLancamento) {
	
		Date dataLancntoDistribuidor = DateUtil.parseDataPTBR(dataLancamento);
		
		List<ResumoExpedicaoDetalheVO> listaDetalhe = new ArrayList<ResumoExpedicaoDetalheVO>();

		int contador = 0;
		
		while(contador++<10) {

			ResumoExpedicaoDetalheVO resumoExpedicaoDetalhe = new ResumoExpedicaoDetalheVO();
			
			resumoExpedicaoDetalhe.setCodigoProduto(""+contador);
			resumoExpedicaoDetalhe.setDescricaoProduto("produto_"+contador);
			resumoExpedicaoDetalhe.setEdicaoProduto(""+contador);
			resumoExpedicaoDetalhe.setPrecoCapa(""+contador);
			resumoExpedicaoDetalhe.setReparte("100");
			resumoExpedicaoDetalhe.setValorFaturado("100");
			resumoExpedicaoDetalhe.setQntDiferenca("100");
			resumoExpedicaoDetalhe.setNomeFornecedor("Fornecedor " + contador);
			
			listaDetalhe.add(resumoExpedicaoDetalhe);

		}

		TableModel<CellModelKeyValue<ResumoExpedicaoDetalheVO>> tableModel = new TableModel<CellModelKeyValue<ResumoExpedicaoDetalheVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDetalhe));
		tableModel.setPage(1);
		tableModel.setTotal(listaDetalhe.size());

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
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
		
		listaLancamentosExpedidosBox = 
			ordenarEmMemoria(
				listaLancamentosExpedidosBox, filtro.getPaginacao(), filtro.getOrdenacaoColunaBox().toString());
		
		TableModel<CellModelKeyValue<ResumoExpedicaoBoxVO>> tableModel = 
			new TableModel<CellModelKeyValue<ResumoExpedicaoBoxVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentosExpedidosBox));
		
		tableModel.setPage(1);
		
		tableModel.setTotal(listaLancamentosExpedidosBox.size());

		String valorTotalFaturadoFormatado = CurrencyUtil.formatarValor(retornoExpedicaoVO.getTotalValorFaturado());
 
		ResultadoResumoExpedicaoVO<ResumoExpedicaoBoxVO> resultadoResumoExpedicao = 
			new ResultadoResumoExpedicaoVO<ResumoExpedicaoBoxVO>(
				tableModel, retornoExpedicaoVO.getTotalReparte().intValue(), valorTotalFaturadoFormatado);

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
		
		BigDecimal qtdeTotalReparte = BigDecimal.ZERO;
		BigDecimal valorTotalFaturado = BigDecimal.ZERO;
		
		List<ResumoExpedicaoBoxVO> listaLancamentosExpedidosBox = new LinkedList<ResumoExpedicaoBoxVO>();
		
		ResumoExpedicaoBoxVO resumoExpedicaoBoxVO = null;
		
		for (ExpedicaoDTO expd  : list){
			
			resumoExpedicaoBoxVO = new ResumoExpedicaoBoxVO();
			resumoExpedicaoBoxVO.setCodigoBox(expd.getCodigoBox());
			resumoExpedicaoBoxVO.setDescricaoBox(expd.getNomeBox());
			resumoExpedicaoBoxVO.setQntProduto(getValor(expd.getQntProduto()));
			resumoExpedicaoBoxVO.setReparte(getValor(expd.getQntReparte()));
			resumoExpedicaoBoxVO.setValorFaturado(CurrencyUtil.formatarValor(expd.getValorFaturado()));
			resumoExpedicaoBoxVO.setQntDiferenca(getValor(expd.getQntDiferenca()));
			
			valorTotalFaturado = valorTotalFaturado.add(expd.getValorFaturado());
			qtdeTotalReparte = qtdeTotalReparte.add(expd.getQntReparte());
			
			listaLancamentosExpedidosBox.add(resumoExpedicaoBoxVO);
		}
		
		RetornoExpedicaoVO retornoExpedicaoVO = new RetornoExpedicaoVO();
		retornoExpedicaoVO.setResumosExpedicaoBox(listaLancamentosExpedidosBox);
		retornoExpedicaoVO.setTotalReparte(qtdeTotalReparte);
		retornoExpedicaoVO.setTotalValorFaturado(valorTotalFaturado);
		
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
		
		TableModel<CellModelKeyValue<ResumoExpedicaoVO>> tableModel = 
			new TableModel<CellModelKeyValue<ResumoExpedicaoVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentosExpedidos));

		tableModel.setTotal((quantidadeRegistros!= null)? quantidadeRegistros.intValue():0);

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		String valorTotalFaturadoFormatado = CurrencyUtil.formatarValor(retornoExpedicaoVO.getTotalValorFaturado());

		ResultadoResumoExpedicaoVO<ResumoExpedicaoVO> resultadoResumoExpedicao = 
			new ResultadoResumoExpedicaoVO<ResumoExpedicaoVO>(
				tableModel, retornoExpedicaoVO.getTotalReparte().intValue(), valorTotalFaturadoFormatado);

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
		
		BigDecimal qtdeTotalReparte = BigDecimal.ZERO;
		BigDecimal valorTotalFaturado = BigDecimal.ZERO;
		
		ResumoExpedicaoVO resumoExpedicaoVO = null;
		
		for (ExpedicaoDTO expd  : list){
			
			resumoExpedicaoVO = new ResumoExpedicaoVO();
			
			resumoExpedicaoVO.setCodigoProduto(expd.getCodigoProduto());
			resumoExpedicaoVO.setDescricaoProduto(expd.getNomeProduto());
			resumoExpedicaoVO.setEdicaoProduto(getValor(expd.getNumeroEdicao()));
			resumoExpedicaoVO.setPrecoCapa(CurrencyUtil.formatarValor(expd.getPrecoCapa()));
			resumoExpedicaoVO.setReparte(getValor(expd.getQntReparte()));
			resumoExpedicaoVO.setValorFaturado(CurrencyUtil.formatarValor(expd.getValorFaturado()));
			resumoExpedicaoVO.setQntDiferenca(getValor(expd.getQntDiferenca()));
			
			valorTotalFaturado = valorTotalFaturado.add(expd.getValorFaturado());
			qtdeTotalReparte = qtdeTotalReparte.add(expd.getQntReparte());
			
			listaLancamentosExpedidos.add(resumoExpedicaoVO);
		}
		
		RetornoExpedicaoVO expedicaoVO = new RetornoExpedicaoVO();
		expedicaoVO.setResumosExpedicao(listaLancamentosExpedidos);
		expedicaoVO.setTotalReparte(qtdeTotalReparte);
		expedicaoVO.setTotalValorFaturado(valorTotalFaturado);
		
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
	 * Ordena a lista de resumo de lançamentos agrupadas por box.
	 * @param listaAOrdenar
	 * @param paginacao
	 * @param nomeAtributoOrdenacao
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	private <T extends Object> List<T> ordenarEmMemoria(List<T> listaAOrdenar, 
														PaginacaoVO paginacao, 
														String nomeAtributoOrdenacao) {
		
		Collections.sort(listaAOrdenar, new BeanComparator(nomeAtributoOrdenacao));
		
		if (Ordenacao.DESC.equals(paginacao.getOrdenacao())) {
			
			Collections.reverse(listaAOrdenar);
		}
		
		return listaAOrdenar;
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
	
	//TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		
		usuario.setNome("Jornaleiro da Silva");
		
		return usuario;
	}
	
	@Get
	@Path("/resumo/exportarDetalhes")
	public void exportarDetalhes(FileType fileType, String codigoBox, Date dataLancamento) throws IOException {
		
		if (fileType == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de arquivo não encontrado!");
		}
		
		List<ResumoExpedicaoDetalheVO> listaDetalhe = new ArrayList<ResumoExpedicaoDetalheVO>();

		int contador = 0;
		
		while(contador++<10) {

			ResumoExpedicaoDetalheVO resumoExpedicaoDetalhe = new ResumoExpedicaoDetalheVO();
			
			resumoExpedicaoDetalhe.setCodigoProduto(""+contador);
			resumoExpedicaoDetalhe.setDescricaoProduto("produto_"+contador);
			resumoExpedicaoDetalhe.setEdicaoProduto(""+contador);
			resumoExpedicaoDetalhe.setPrecoCapa(""+contador);
			resumoExpedicaoDetalhe.setReparte("100");
			resumoExpedicaoDetalhe.setValorFaturado("100");
			resumoExpedicaoDetalhe.setQntDiferenca("100");
			resumoExpedicaoDetalhe.setNomeFornecedor("Fornecedor " + contador);
			
			listaDetalhe.add(resumoExpedicaoDetalhe);

		}
		
		FileExporter.to("consulta-detalhes-expedição-box", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), null, null, 
					listaDetalhe, ResumoExpedicaoDetalheVO.class, this.httpServletResponse);
	}
}
