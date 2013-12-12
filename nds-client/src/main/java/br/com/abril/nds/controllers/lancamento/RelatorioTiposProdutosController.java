package br.com.abril.nds.controllers.lancamento;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.RelatorioTiposProdutosDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioTiposProdutos;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("lancamento/relatorioTiposProdutos")
@Rules(Permissao.ROLE_LANCAMENTO_RELATORIO_TIPOS_PRODUTOS)
public class RelatorioTiposProdutosController extends BaseController {

	private static final String FILTRO_RELATORIO_TIPOS_PRODUTOS = "FILTRO_RELATORIO_TIPOS_PRODUTOS";
	
	@Autowired
	private TipoProdutoService tipoProdutoService; 
	
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
	public void index()
	{
		List<TipoProduto> listTipoProduto = tipoProdutoService.obterTiposProdutoDesc();
		result.include("listTipoProduto", listTipoProduto);
	}
	
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroRelatorioTiposProdutos filtro, String sortname, String sortorder, int rp, int page) {
		
		tratarFiltroPesquisa(filtro, sortorder, sortname, page, rp);
		
		Long total = this.tipoProdutoService.obterQunatidade(filtro);
		
		if (total != null && total > 0){
			
			List<RelatorioTiposProdutosDTO> list = this.tipoProdutoService.gerarRelatorio(filtro);
			result.use(FlexiGridJson.class).from(list).total(total.intValue()).page(page).serialize();
		} else {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
	}
	
	private void tratarFiltroPesquisa(FiltroRelatorioTiposProdutos filtro, 
										 String sortorder,String sortname, 
										 int page, int rp) {
		if (filtro != null) {

			PaginacaoVO paginacao = new PaginacaoVO();
			
			paginacao.setSortOrder(sortorder);
			paginacao.setQtdResultadosPorPagina(rp);
			paginacao.setPaginaAtual(page);
			
			filtro.setPaginacaoVO(paginacao);
			
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroRelatorioTiposProdutos.OrdenacaoColuna.values(), sortname));
		}

		FiltroRelatorioTiposProdutos filtroSession = 
				(FiltroRelatorioTiposProdutos) session.getAttribute(FILTRO_RELATORIO_TIPOS_PRODUTOS);

		if (filtroSession != null && !filtroSession.equals(filtro)) {

			filtro.getPaginacaoVO().setPaginaAtual(1);
		}

		this.session.setAttribute(FILTRO_RELATORIO_TIPOS_PRODUTOS, filtro);
}
	
	
	@Path("/exportar")
	public void exportar(FileType fileType) throws IOException {
		
		FiltroRelatorioTiposProdutos filtro = (FiltroRelatorioTiposProdutos) this.session.getAttribute(FILTRO_RELATORIO_TIPOS_PRODUTOS);
		
		filtro.getPaginacaoVO().setPaginaAtual(null);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(null);
		filtro.getPaginacaoVO().setQtdResultadosTotal(null);
		
		if(filtro.getTipoProduto()!= null){

			TipoProduto tipoProduto = tipoProdutoService.buscaPorId(filtro.getTipoProduto());
			
			if (tipoProduto != null) {
				
				filtro.setNomeTipoProduto(tipoProduto.getDescricao());
			}
		}
		
		List<RelatorioTiposProdutosDTO> list = this.tipoProdutoService.gerarRelatorio(filtro);
		
		FileExporter.to("relatorio-tipos-produtos", fileType).inHTTPResponse(
				this.getNDSFileHeader(), filtro, null,
				list, RelatorioTiposProdutosDTO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
}