package br.com.abril.nds.controllers.lancamento;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.RelatorioTiposProdutosVO;
import br.com.abril.nds.dto.RelatorioTiposProdutosDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioTiposProdutos;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.RelatorioTiposProdutosService;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("lancamento/relatorioTiposProdutos")
public class RelatorioTiposProdutosController {

	private static final String FILTRO_RELATORIO_TIPOS_PRODUTOS = "FILTRO_RELATORIO_TIPOS_PRODUTOS";
	
	@Autowired
	private TipoProdutoService tipoProdutoService; 
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private RelatorioTiposProdutosService service;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private HttpSession session;
	
	
	private Result result;
	
	public RelatorioTiposProdutosController(Result result) {
		super();
		this.result = result;
	}

	
	@Path("/")
	@Rules(Permissao.ROLE_LANCAMENTO_RELATORIO_TIPOS_PRODUTOS)
	public void index()
	{
		List<TipoProduto> listTipoProduto = tipoProdutoService.obterTodosTiposProduto();
		result.include("listTipoProduto", listTipoProduto);
	}
	
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroRelatorioTiposProdutos filtro, String sortname, String sortorder, int rp, int page) {
		
//		FiltroRelatorioTiposProdutos filtroSession = (FiltroRelatorioTiposProdutos) session
//				.getAttribute(FILTRO_RELATORIO_TIPOS_PRODUTOS);
		
		PaginacaoVO vo = new PaginacaoVO();
		vo.setSortColumn(sortname);
		vo.setSortOrder(sortorder);
		vo.setQtdResultadosPorPagina(rp);
		vo.setPaginaAtual(page);
		
		filtro.setPaginacaoVO(vo);
		
		this.session.setAttribute(FILTRO_RELATORIO_TIPOS_PRODUTOS, filtro);
		
 		List<RelatorioTiposProdutosVO> list = this.convertList(service.gerarRelatorio(filtro));
		result.use(FlexiGridJson.class).from(list).total(filtro.getPaginacaoVO().getQtdResultadosTotal()).page(page).serialize();
	}
	
	
	@Path("/exportar")
	public void exportar(FileType fileType) throws IOException {
		
		FiltroRelatorioTiposProdutos filtro = (FiltroRelatorioTiposProdutos) this.session.getAttribute(FILTRO_RELATORIO_TIPOS_PRODUTOS);
		List<RelatorioTiposProdutosVO> list = this.convertList(service.gerarRelatorio(filtro));
		
		FileExporter.to("relatorio-tipos-produtos", fileType).inHTTPResponse(
				this.getNDSFileHeader(), null, null,
				list, RelatorioTiposProdutosVO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	
	private List<RelatorioTiposProdutosVO> convertList(List<RelatorioTiposProdutosDTO> listDTO) {
		
		List<RelatorioTiposProdutosVO> listVO = new ArrayList<RelatorioTiposProdutosVO>();
		
		if (listDTO != null) {
			for (RelatorioTiposProdutosDTO dto : listDTO) {
				listVO.add(new RelatorioTiposProdutosVO(dto));
			}
		}
		
		return listVO;
	}
	
	
	private NDSFileHeader getNDSFileHeader() {

		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		Distribuidor distribuidor = distribuidorService.obter();

		if (distribuidor != null) {
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}

		ndsFileHeader.setData(new Date());
		ndsFileHeader.setNomeUsuario(getUsuario().getNome());
		return ndsFileHeader;
	}
	
	
	// TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {

		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Jornaleiro da Silva");

		return usuario;
	}
}
