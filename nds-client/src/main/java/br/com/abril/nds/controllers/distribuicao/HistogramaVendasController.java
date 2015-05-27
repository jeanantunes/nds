package br.com.abril.nds.controllers.distribuicao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.lightcouch.NoDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliseHistogramaDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.RodapeHistogramaVendaDTO;
import br.com.abril.nds.dto.filtro.FiltroHistogramaVendas;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.InformacoesProdutoService;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RegiaoService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.UfEnum;
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

@Resource
@Path("/distribuicao/histogramaVendas")
@Rules(Permissao.ROLE_DISTRIBUICAO_HISTOGRAMA_VENDAS)
public class HistogramaVendasController extends BaseController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroHistogramaVendas";
	private static final String HISTOGRAMA_SESSION_ATTRIBUTE = "resultadoHistogramaVendas";
	private String[] faixaVendaInicial = {"0-0","1-4","5-9","10-19","20-49","50-9999999"};
	
    private static final Logger LOGGER = LoggerFactory.getLogger(HistogramaVendasController.class);

	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;	
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private CapaService capaService;
	
	@Autowired
	private PdvService pdvService;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private HttpServletResponse httpResponse;

	@Autowired
	private EstoqueProdutoService estoqueProdutoService;
	
	@Autowired
	private InformacoesProdutoService infoProdService;
	
	@Autowired
	private TipoClassificacaoProdutoService tipoClassificacaoProdutoService;
	
	@Autowired
	private ProdutoService produtoService;
	
    @Path("/")
	public void index(){
		
		result.include("componenteList", ComponentesPDV.values());
		this.carregarComboClassificacao();		
	}
	
	@Autowired
	private RegiaoService regiaoService;
	
	private static String REPARTE_TOTAL = "reparteTotal";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Post
	@Path("/carregarElementos")
	public void carregarElementos(String componente){
		List<ItemDTO<Long, String>> resultList = new ArrayList<ItemDTO<Long, String>>();
		
		switch (ComponentesPDV.values()[Integer.parseInt(componente)]) {
		case TIPO_PONTO_DE_VENDA:
			for(TipoPontoPDV tipo:pdvService.obterTiposPontoPDVPrincipal()){
				resultList.add(new ItemDTO(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;
		case AREA_DE_INFLUENCIA:
			for(AreaInfluenciaPDV tipo:pdvService.obterAreasInfluenciaPDV()){
				resultList.add(new ItemDTO(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;

		case BAIRRO:
			for(String tipo:enderecoService.obterBairrosCotas()){
				resultList.add(new ItemDTO(tipo,tipo));
			}
			break;
		case DISTRITO:
			for(UfEnum tipo:UfEnum.values()){
				resultList.add(new ItemDTO(tipo.getSigla(),tipo.getSigla()));
			}
			break;
		case GERADOR_DE_FLUXO:
			for(TipoGeradorFluxoPDV tipo:pdvService.obterTodosTiposGeradorFluxoOrdenado()){
				resultList.add(new ItemDTO(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;
		case COTAS_A_VISTA:
			
			break;
		case COTAS_NOVAS_RETIVADAS:
			
			break;
		case REGIAO:
			for (RegiaoDTO regiao : regiaoService.buscarRegiao()) {
				resultList.add(new ItemDTO(regiao.getIdRegiao(), regiao.getNomeRegiao()));
			}
			break;
		default:
			break;
		}
		
		result.use(Results.json()).from(resultList, "result").recursive().serialize();
	}
	
	@Path("/getCapaEdicaoJson")
	public File getCapaEdicaoJson(String codigoProduto,String numeroEdicao){
		
		InputStream att = null;
		File file = new File("temp"+FileType.JPG.getExtension());
		try {
			att = capaService.getCapaInputStream(codigoProduto,Long.parseLong(numeroEdicao));
				 
					// write the inputStream to a FileOutputStream
					OutputStream out = new FileOutputStream(file);
				 
					int read = 0;
					byte[] bytes = new byte[1024];
				 
					while ((read = att.read(bytes)) != -1) {
						out.write(bytes, 0, read);
					}
				 
					att.close();
					out.flush();
					out.close();
			 
		} catch (NumberFormatException e) {
            LOGGER.debug("Erro na conversão de tipos.", e);
		}
		catch (Exception e) {
			if(e instanceof NoDocumentException){
                throw new ValidacaoException(TipoMensagem.WARNING, "Capa não encontrada para a edição.");
			}
		}
		
		return file;
	}
	
	@Post
	@Path("/analiseHistograma")
	public void  analiseHistograma(String edicoes,String segmento,
			String codigoProduto,String nomeProduto,String labelComponente,
			String labelElemento,String classificacaoLabel){
		
		String[] nrEdicoes = edicoes.split(",");
		
		
		String enumeratedList = null;
		
		if(nrEdicoes.length==1){
			enumeratedList = nrEdicoes[0];
		}else{
			enumeratedList = StringUtils.join(nrEdicoes, " - ");
		}
		
		List<Long> eds = new ArrayList<Long>();
		for (int j = 0; j < nrEdicoes.length; j++) {
			
			eds.add(Long.parseLong(nrEdicoes[j]));
		}
		
		BigInteger totalDistrib = this.estoqueProdutoService.buscarQtdEstoquePorProduto(
				codigoProduto, eds);
		
		int reparteTotalDistribuidor = 0;
		if (totalDistrib != null){
			reparteTotalDistribuidor = totalDistrib.intValue();
		}
		
		result.include("filtroUtilizado", getFiltroSessao());
		result.include("listaEdicoes", enumeratedList);
		result.include("segmentoLabel", segmento);
		result.include("codigoLabel", codigoProduto);
		result.include("nomeProduto", nomeProduto);
		
		
		result.include("labelComponente", labelComponente);
		result.include("labelElemento", labelElemento);
		result.include("classificacaoLabel", classificacaoLabel);
		
		// informações do resumo do histograma (parte inferior da tela)
		this.session.setAttribute(REPARTE_TOTAL, reparteTotalDistribuidor);
		result.include("reparteTotalDistribuidor", reparteTotalDistribuidor);
		
		
		
        // Pesquisar base de estudo e salvar em sessão
		List<AnaliseHistogramaDTO> list = produtoEdicaoService.obterBaseEstudoHistogramaPorFaixaVenda(getFiltroSessao(),codigoProduto, faixaVendaInicial, nrEdicoes);
	
		session.setAttribute(HISTOGRAMA_SESSION_ATTRIBUTE, list);
	}
	
	@SuppressWarnings("unchecked")
	@Post
	@Path("/populateHistograma")
	public void popularHistograma(String edicoes,String faixasVenda,String codigoProduto){
		List<AnaliseHistogramaDTO> list = null;
		if(edicoes==null){
			list = (List<AnaliseHistogramaDTO>)session.getAttribute(HISTOGRAMA_SESSION_ATTRIBUTE);
		}else{
			list = produtoEdicaoService.obterBaseEstudoHistogramaPorFaixaVenda(getFiltroSessao(),codigoProduto, faixasVenda.split(","), edicoes.split(","));
			
			session.setAttribute(HISTOGRAMA_SESSION_ATTRIBUTE, list);
		}
		
			TableModel<CellModelKeyValue<AnaliseHistogramaDTO>> tableModel = new TableModel<CellModelKeyValue<AnaliseHistogramaDTO>>();
			
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(list));
			
			tableModel.setPage(1);
			
			tableModel.setTotal(list.size());
			result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Post
	@Path("/consultar")
	public void pesquisarFiltro(FiltroHistogramaVendas filtro, Long classificacaoId, String sortorder, String sortname, int page, int rp) {
		
		filtro.setIdTipoClassificacaoProduto(classificacaoId);
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		filtro.setOrdemColuna(Util.getEnumByStringValue(FiltroHistogramaVendas.OrdemColuna.values(), sortname));
		
		tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<EdicoesProdutosDTO>> tableModel = efetuarConsultaEdicoesDoProdutos(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	
	private TableModel<CellModelKeyValue<EdicoesProdutosDTO>> efetuarConsultaEdicoesDoProdutos(
			FiltroHistogramaVendas filtro) {
		
		if(filtro.getCodigo() != null){
			Produto produto = produtoService.obterProdutoPorCodigo(filtro.getCodigo());
			filtro.setIdProduto(produto.getId());
			filtro.setCodigo(produto.getCodigoICD());
		}
		
		List<EdicoesProdutosDTO> list = produtoEdicaoService.obterHistoricoEdicoes(filtro);
		
		if (list==null || list.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"Nenhum registro encontrado.");
		}
		
		TableModel<CellModelKeyValue<EdicoesProdutosDTO>> tableModel = new TableModel<CellModelKeyValue<EdicoesProdutosDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(list));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	
	
        

	@SuppressWarnings("unchecked")
	@Get
	public void exportar(FileType fileType, String abrangenciaDistribuicao, String abrangenciaVenda, String eficienciaVenda) throws IOException {
		
		List<AnaliseHistogramaDTO> lista = (List<AnaliseHistogramaDTO>)session.getAttribute(HISTOGRAMA_SESSION_ATTRIBUTE);
		
		if (lista==null || lista.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "A última pesquisa realizada não obteve resultado.");
		}
        AnaliseHistogramaDTO footer = lista.get(lista.size() - 1);
		
		if(fileType.equals(FileType.XLS)){
			RodapeHistogramaVendaDTO rodapeDTO = montarRodapeParaXLS(footer, abrangenciaDistribuicao, abrangenciaVenda, eficienciaVenda);
			
            FileExporter.to("Histórico_de_venda_por_faixa", fileType).inHTTPResponse(
					this.getNDSFileHeader(), 
					getFiltroSessao(), 
					rodapeDTO, 
					lista,
					AnaliseHistogramaDTO.class, this.httpResponse);
		}
		result.nothing();
	}

	private RodapeHistogramaVendaDTO montarRodapeParaXLS(AnaliseHistogramaDTO footer, String abrangenciaDistribuicao, String abrangenciaVenda, String eficienciaVenda) {
		
		String qtdeTotalCotasAtivasFormatada = footer.getQtdeTotalCotasAtivas().toString();
		String cotasProduto = footer.getQtdeCotas().toString();
		String cotaEsmagas = footer.getCotasEsmagadas().toString();
		String vendaEsmagada = footer.getVendaEsmagadas().setScale(0).toString();		
		String vendaTotal = footer.getVdaTotal().setScale(0, BigDecimal.ROUND_FLOOR) .toString();
		String reparteTotalFormatado = footer.getRepTotal().setScale(0, BigDecimal.ROUND_FLOOR).toString() ;
		RodapeHistogramaVendaDTO rodapeDTO = new RodapeHistogramaVendaDTO(qtdeTotalCotasAtivasFormatada, 
				cotasProduto, 
				cotaEsmagas,
				vendaEsmagada, 
				this.session.getAttribute(REPARTE_TOTAL).toString(),
				reparteTotalFormatado,
				vendaTotal,
				eficienciaVenda.concat("%"),
				abrangenciaDistribuicao.concat("%"),
				abrangenciaVenda.concat("%"),
				footer.getRepMedio().toString(),
				footer.getVdaMedio().toString(),
				footer.getEncalheMedio().toString());
		return rodapeDTO;
	}
	
	

	private void tratarFiltro(FiltroHistogramaVendas filtroAtual)throws ValidacaoException {

        if (StringUtils.isEmpty(filtroAtual.getCodigo())) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Favor informar um código ou nome de produto.");
		}
		
		FiltroHistogramaVendas filtroSession = (FiltroHistogramaVendas) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		 
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}

	private FiltroHistogramaVendas getFiltroSessao(){
		FiltroHistogramaVendas filtroSession = (FiltroHistogramaVendas) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		return filtroSession;
	}
	
	private void carregarComboClassificacao(){
		List<ItemDTO<Long,String>> comboClassificacao =  new ArrayList<ItemDTO<Long,String>>();
		List<TipoClassificacaoProduto> classificacoes = infoProdService.buscarClassificacao();
		
		for (TipoClassificacaoProduto tipoClassificacaoProduto : classificacoes) {
			comboClassificacao.add(new ItemDTO<Long,String>(tipoClassificacaoProduto.getId(), tipoClassificacaoProduto.getDescricao()));
		}
		result.include("listaClassificacao",comboClassificacao);		
	}
	
	public ProdutoEdicaoService getProdutoEdicaoService() {
		return produtoEdicaoService;
	}


	public void setProdutoEdicaoService(ProdutoEdicaoService produtoEdicaoService) {
		this.produtoEdicaoService = produtoEdicaoService;
	}

	public CapaService getCapaService() {
		return capaService;
	}

	public void setCapaService(CapaService capaService) {
		this.capaService = capaService;
	}

	public PdvService getPdvService() {
		return pdvService;
	}

	public void setPdvService(PdvService pdvService) {
		this.pdvService = pdvService;
	}

	public EnderecoService getEnderecoService() {
		return enderecoService;
	}

	public void setEnderecoService(EnderecoService enderecoService) {
		this.enderecoService = enderecoService;
	}

	public HttpServletResponse getHttpResponse() {
		return httpResponse;
	}

	public void setHttpResponse(HttpServletResponse httpResponse) {
		this.httpResponse = httpResponse;
	}
}
