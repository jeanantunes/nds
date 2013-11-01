package br.com.abril.nds.controllers.distribuicao;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CaracteristicaDistribuicaoDTO;
import br.com.abril.nds.dto.CaracteristicaDistribuicaoSimplesDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaCaracteristicaDistribuicaoDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaCaracteristicaDistribuicaoSimplesDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.*;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.view.Results;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

@Resource
@Path("/distribuicao/caracteristicaDistribuicao")
public class CaracteristicaDistribuicaoController extends BaseController{
	
	private static final String FILTRO_DETALHE_SESSION_ATTRIBUTE = "filtroDetalhe";
	private static final String FILTRO_SIMPLES_SESSION_ATTRIBUTE = "filtroSimples";
	
	@Autowired
	private CaracteristicaDistribuicaoService caracteristicaDistribuicaoService;
	
	@Autowired
	private CapaService capaService;
	
	@Autowired
	private TipoSegmentoProdutoService tipoSegmentoProdutoService;
	
	@Autowired
	private BrindeService brindeService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
	private Result result;
	
	@Autowired
	HttpSession session;
	
	@SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private ServletContext servletContext;
	
	
	@Path("/")
	@Rules(Permissao.ROLE_DISTRIBUICAO_CARACTERISTICA_DISTRIBUICAO)
	public void index(){
		result.include("classificacoes",caracteristicaDistribuicaoService.obterClassificacoesProduto());
		result.include("segmentos",tipoSegmentoProdutoService.obterTipoSegmentoProduto());
		result.include("brindes",brindeService.obterBrindes());
	}
	
	@Path("/pesquisarSimples")
	@Post	   
	public void pesquisarSimples(FiltroConsultaCaracteristicaDistribuicaoSimplesDTO filtro, String sortorder, String sortname, int page, int rp){
		if(session.getAttribute(FILTRO_SIMPLES_SESSION_ATTRIBUTE)==null){
			this.session.setAttribute(FILTRO_SIMPLES_SESSION_ATTRIBUTE, filtro);
		}
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		tratarFiltroSimples(filtro);
		List<CaracteristicaDistribuicaoSimplesDTO> resultado =caracteristicaDistribuicaoService.buscarComFiltroSimples(filtro);
		
		if(resultado.isEmpty()){
			 throw new ValidacaoException(TipoMensagem.WARNING, "Não Foram encontrados resultados para a pesquisa");
	    }
		   
		   TableModel<CellModelKeyValue<CaracteristicaDistribuicaoSimplesDTO>> tableModelPesquisaDetalhe = montarTableModelPesquisaSimples(filtro);
			result.use(Results.json()).withoutRoot().from(tableModelPesquisaDetalhe).recursive().serialize();
	}
	
	
	
	@Path("/pesquisarDetalhe")
	@Post	   
	public void pesquisarDetalhe(FiltroConsultaCaracteristicaDistribuicaoDetalheDTO filtro, String sortorder, String sortname, int page, int rp){
		if(session.getAttribute(FILTRO_DETALHE_SESSION_ATTRIBUTE)==null){
			this.session.setAttribute(FILTRO_DETALHE_SESSION_ATTRIBUTE, filtro);
		}
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		filtro.setOrdemColuna(Util.getEnumByStringValue(FiltroConsultaCaracteristicaDistribuicaoDetalheDTO.OrdemColuna.values(), sortname));
		tratarFiltroDetalhe(filtro);
		
		if(StringUtils.isNotBlank(filtro.getCodigoProduto())){
			Produto produto = produtoService.obterProdutoPorCodigo(filtro.getCodigoProduto());
			filtro.setCodigoProduto(produto.getCodigoICD());
		}
		   
	   TableModel<CellModelKeyValue<CaracteristicaDistribuicaoDTO>> tableModelPesquisaDetalhe = montarTableModelPesquisaDetalhe(filtro);
	   result.use(Results.json()).withoutRoot().from(tableModelPesquisaDetalhe).recursive().serialize();
	}
	  
	private TableModel<CellModelKeyValue<CaracteristicaDistribuicaoDTO>> montarTableModelPesquisaDetalhe(FiltroConsultaCaracteristicaDistribuicaoDetalheDTO filtro) {
		
		List<CaracteristicaDistribuicaoDTO> resultadoPesquisa = caracteristicaDistribuicaoService.buscarComFiltroCompleto(filtro);
		
		if(resultadoPesquisa.isEmpty()){
			 throw new ValidacaoException(TipoMensagem.WARNING, "Não Foram encontrados resultados para a pesquisa");
	    }
		
		TableModel<CellModelKeyValue<CaracteristicaDistribuicaoDTO>> tableModel = new TableModel<>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPesquisa));
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	
	
	private TableModel<CellModelKeyValue<CaracteristicaDistribuicaoSimplesDTO>> montarTableModelPesquisaSimples(FiltroConsultaCaracteristicaDistribuicaoSimplesDTO filtro) {
		
		List<CaracteristicaDistribuicaoSimplesDTO> resultadoPesquisa = caracteristicaDistribuicaoService.buscarComFiltroSimples(filtro);
		
		TableModel<CellModelKeyValue<CaracteristicaDistribuicaoSimplesDTO>> tableModel = new TableModel<>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPesquisa));
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	private void tratarFiltroDetalhe(FiltroConsultaCaracteristicaDistribuicaoDetalheDTO filtroAtual) {

		FiltroConsultaCaracteristicaDistribuicaoDetalheDTO filtroSession = (FiltroConsultaCaracteristicaDistribuicaoDetalheDTO) session
				.getAttribute(FILTRO_DETALHE_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_DETALHE_SESSION_ATTRIBUTE, filtroAtual);
	}
	
	private void tratarFiltroSimples(FiltroConsultaCaracteristicaDistribuicaoSimplesDTO filtroAtual) {

		FiltroConsultaCaracteristicaDistribuicaoSimplesDTO filtroSession = (FiltroConsultaCaracteristicaDistribuicaoSimplesDTO) session
				.getAttribute(FILTRO_SIMPLES_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SIMPLES_SESSION_ATTRIBUTE, filtroAtual);
	}
	
	
	@Path("/exportarDetalhe")
	@Get
	public void exportar(FileType fileType, String tipoExportacao) throws IOException {
        Class<CaracteristicaDistribuicaoDTO> clazz;
		List<CaracteristicaDistribuicaoDTO> resultadoPesquisaDetalhe;
		FiltroConsultaCaracteristicaDistribuicaoDetalheDTO filtroDetalhe;
		
		filtroDetalhe=(FiltroConsultaCaracteristicaDistribuicaoDetalheDTO)  session.getAttribute(FILTRO_DETALHE_SESSION_ATTRIBUTE);
		if(filtroDetalhe!=null){
			resultadoPesquisaDetalhe = caracteristicaDistribuicaoService.buscarComFiltroCompleto(filtroDetalhe) ;
			clazz = CaracteristicaDistribuicaoDTO.class;
			FileExporter.to("caracteristica_distribuicao", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, resultadoPesquisaDetalhe, clazz, this.httpResponse);
		}
		
		result.nothing();
		
	}
	
	
	@Path("/exportarSimples")
	@Get
	public void exportarSimples(FileType fileType, String tipoExportacao) throws IOException {
        Class<CaracteristicaDistribuicaoSimplesDTO> clazz;
		List<CaracteristicaDistribuicaoSimplesDTO> resultadoPesquisaSimples;
		FiltroConsultaCaracteristicaDistribuicaoSimplesDTO filtroSimples;
		
		filtroSimples=(FiltroConsultaCaracteristicaDistribuicaoSimplesDTO)  session.getAttribute(FILTRO_SIMPLES_SESSION_ATTRIBUTE);
		if(filtroSimples!=null){
			resultadoPesquisaSimples = caracteristicaDistribuicaoService.buscarComFiltroSimples(filtroSimples) ;
			clazz = CaracteristicaDistribuicaoSimplesDTO.class;
			FileExporter.to("caracteristica_distribuicao", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, resultadoPesquisaSimples, clazz, this.httpResponse);
		}
		
		result.nothing();
		
	}
	
	@Path("/exibirCapa")
	@Post
	public File exibirCapa(String codProduto, Long numeroEdicao){
		  InputStream att;
		  File file = new File("temp"+CapaService.DEFAULT_EXTENSION);
		  try {
		   att = capaService.getCapaInputStream(codProduto,numeroEdicao);
		     
		     // write the inputStream to a FileOutputStream
		     OutputStream out = new FileOutputStream(file);
		     
		     int read;
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
			  file = new File((servletContext.getRealPath("") + "/images/no_image.jpeg"));
		  }
		  
		  return file;
	 }
	
	
}
