package br.com.abril.nds.controllers.distribuicao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliseHistogramaDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroHistogramaVendas;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.UfEnum;
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
public class HistogramaVendasController extends BaseController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroHistogramaVendas";
	private static final String HISTOGRAMA_SESSION_ATTRIBUTE = "resultadoHistogramaVendas";
	
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
	
	@Rules(Permissao.ROLE_DISTRIBUICAO_HISTOGRAMA_VENDAS)
	public void index(){
		
		result.include("componenteList", ComponentesPDV.values());
		
	}
	
	@Post
	@Path("/carregarElementos")
	public void carregarElementos(String componente){
		List<ItemDTO<Long, String>> resultList = new ArrayList<ItemDTO<Long, String>>();
		
		switch (ComponentesPDV.values()[Integer.parseInt(componente)]) {
		case TipoPontodeVenda:
			for(TipoPontoPDV tipo:pdvService.obterTiposPontoPDVPrincipal()){
				resultList.add(new ItemDTO(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;
		case Area_de_Influência:
			for(AreaInfluenciaPDV tipo:pdvService.obterAreasInfluenciaPDV()){
				resultList.add(new ItemDTO(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;

		case Bairro:
			for(String tipo:enderecoService.obterBairrosCotas()){
				resultList.add(new ItemDTO(tipo,tipo));
			}
			break;
		case Distrito:
			for(UfEnum tipo:UfEnum.values()){
				resultList.add(new ItemDTO(tipo.getSigla(),tipo.getSigla()));
			}
			break;
		case GeradorDeFluxo:
			for(TipoGeradorFluxoPDV tipo:pdvService.obterTodosTiposGeradorFluxo()){
				resultList.add(new ItemDTO(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;
		case CotasAVista:
			
			break;
		case CotasNovasRetivadas:
			
			break;
		case Região:
			//todo: EMS 2004
			break;
		default:
			break;
		}
		
		
		result.use(Results.json()).from(resultList, "result").recursive().serialize();
	}
	
	@Path("/getCapaEdicaoJson")
	public File getCapaEdicaoJson(String codigoProduto,String numeroEdicao){
		
		InputStream att = null;
		File file = new File("temp"+CapaService.DEFAULT_EXTENSION);
		try {
			att = capaService.getCapaInputStream(codigoProduto,Long.parseLong(numeroEdicao));
//			att = capaService.getCapaInputStream("00000000",Long.parseLong("0114"));
			
				 
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
			e.printStackTrace();
		}
		catch (Exception e) {
			if(e instanceof NoDocumentException){
				throw new ValidacaoException(TipoMensagem.WARNING,"Capa não encontrada para a edição.");
			}
		}
		
		return file;
	}
	
	@Post
	@Path("/analiseHistograma")
	public void  analiseHistograma(String edicoes,String segmento,String codigoProduto,String nomeProduto,String[] faixasVenda
			,String labelComponente,String labelElemento){
		String[] nrEdicoes = edicoes.split(",");
		
		String enumeratedList = null;
		
		if(nrEdicoes.length==1){
			enumeratedList = nrEdicoes[0];
		}else{
			enumeratedList = StringUtils.join(nrEdicoes, " - ");
		}
		
		
		result.include("filtroUtilizado", getFiltroSessao());
		result.include("listaEdicoes", enumeratedList);
		result.include("segmentoLabel", segmento);
		result.include("produtoLabel", codigoProduto);
		result.include("nomeProduto", nomeProduto);
		
		result.include("labelComponente", labelComponente);
		result.include("labelElemento", labelElemento);
		
		
		//Pesquisar base de estudo e salvar em sessão
//		List list = new ArrayList();
		List<AnaliseHistogramaDTO> list = produtoEdicaoService.obterBaseEstudoHistogramaPorFaixaVenda(getFiltroSessao(),codigoProduto, faixasVenda, nrEdicoes);
		
	
		session.setAttribute(HISTOGRAMA_SESSION_ATTRIBUTE, list);
	}
	
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
	public void pesquisarFiltro(FiltroHistogramaVendas filtro, String sortorder, String sortname, int page, int rp) {
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<EdicoesProdutosDTO>> tableModel = efetuarConsultaEdicoesDoProdutos(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	
	private TableModel<CellModelKeyValue<EdicoesProdutosDTO>> efetuarConsultaEdicoesDoProdutos(
			FiltroHistogramaVendas filtro) {
		
		List<EdicoesProdutosDTO> list = produtoEdicaoService.obterHistoricoEdicoes(filtro);
		
		TableModel<CellModelKeyValue<EdicoesProdutosDTO>> tableModel = new TableModel<CellModelKeyValue<EdicoesProdutosDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(list));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(list.size());
		
		
		return tableModel;
	}

	@Get
	public void exportar(FileType fileType) throws IOException {
		
		List<AnaliseHistogramaDTO> lista = (List<AnaliseHistogramaDTO>)session.getAttribute(HISTOGRAMA_SESSION_ATTRIBUTE);
		
		if (lista==null || lista.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"A última pesquisa realizada não obteve resultado.");
		}

		FileExporter.to("Histórico_de_venda_por_faixa", fileType).inHTTPResponse(
				this.getNDSFileHeader(), getFiltroSessao(), null, lista,
				AnaliseHistogramaDTO.class, this.httpResponse);
		
		result.nothing();
	}
	

	private void tratarFiltro(FiltroHistogramaVendas filtroAtual) {

		if(StringUtils.isNotEmpty(filtroAtual.getEdicao()) && (StringUtils.isEmpty(filtroAtual.getCodigo()) || StringUtils.isNotEmpty(filtroAtual.getProduto()) )){ 
			throw new ValidacaoException(TipoMensagem.WARNING,"Favor informar um código ou nome de produto.");
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

