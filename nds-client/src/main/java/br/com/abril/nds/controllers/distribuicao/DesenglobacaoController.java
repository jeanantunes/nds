<<<<<<< HEAD
package br.com.abril.nds.controllers.distribuicao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DesenglobaDTO;
import br.com.abril.nds.dto.filtro.FiltroDesenglobacaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.DesenglobacaoService;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/distribuicao/desenglobacao")
public class DesenglobacaoController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private DesenglobacaoService service;
	
	@Rules(Permissao.ROLE_DISTRIBUICAO_DESENGLOBACAO)
	@Path("/index")
	public void desenglobacao(){
		
	}
	
	@Post
	public void pesquisaPrincipal(FiltroDesenglobacaoDTO filtro, String sortorder, String sortname, int page, int rp ){
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		validarFiltroDesenglobacao(filtro);
		result.nothing();
	}

	private void validarFiltroDesenglobacao(FiltroDesenglobacaoDTO filtro) {
		if((filtro.getCotaDto().getNumeroCota() == null || filtro.getCotaDto().getNumeroCota() == 0) && 
				(filtro.getCotaDto().getNomePessoa() == null || filtro.getCotaDto().getNomePessoa().trim().isEmpty()))
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe um código ou nome.");
	}
	
	@Post
	@Path("/inserirEnglobacao")
	public void inserirEnglobacao(List<DesenglobaDTO> desenglobaDTO) {
		service.inserirDesenglobacao(desenglobaDTO);
		result.nothing();
	}
}
=======
package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.DesenglobaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DesenglobacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDesenglobacaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.DesenglobacaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
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
@Path("/distribuicao/desenglobacao")
public class DesenglobacaoController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private DesenglobacaoService service;
	
	@Rules(Permissao.ROLE_DISTRIBUICAO_DESENGLOBACAO)
	@Path("/index")
	public void desenglobacao(){
		
	}
	
	@Post
	public void pesquisaPrincipal(FiltroDesenglobacaoDTO filtro, String sortorder, String sortname, int page, int rp ){
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		validarFiltroDesenglobacao(filtro);
		session.setAttribute("filtroDesengloba", filtro);
		
		TableModel<CellModelKeyValue<DesenglobacaoDTO>> tableModel = montarCotasDesenglobadas(filtro);
		if (tableModel != null) {
			result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();			
		}
		
		result.nothing();
	}

	private TableModel<CellModelKeyValue<DesenglobacaoDTO>> montarCotasDesenglobadas(FiltroDesenglobacaoDTO dto) {
		List<DesenglobacaoDTO> cotasDesenglobadas = service.obterDesenglobacaoPorCota(dto.getCotaDto().getNumeroCota().longValue());
		dto.getPaginacao().setQtdResultadosTotal(cotasDesenglobadas.size());
		
		if (cotasDesenglobadas == null || cotasDesenglobadas.isEmpty()) {
			return null;
		}
		
		TableModel<CellModelKeyValue<DesenglobacaoDTO>> tableModel = new TableModel<CellModelKeyValue<DesenglobacaoDTO>>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(cotasDesenglobadas));
		tableModel.setPage(dto.getPaginacao().getPaginaAtual());
		tableModel.setTotal(dto.getPaginacao().getQtdResultadosTotal());
		return tableModel;
	}

	private void validarFiltroDesenglobacao(FiltroDesenglobacaoDTO filtro) {
		if((filtro.getCotaDto().getNumeroCota() == null || filtro.getCotaDto().getNumeroCota() == 0) && 
				(filtro.getCotaDto().getNomePessoa() == null || filtro.getCotaDto().getNomePessoa().trim().isEmpty()))
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe um código ou nome.");
	}
	
	@Post
	@Path("/inserirEnglobacao")
	public void inserirEnglobacao(List<DesenglobaVO> desenglobaDTO) {		
		boolean isOk = service.inserirDesenglobacao(desenglobaDTO, super.getUsuarioLogado());
		if (!isOk) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Porcentagem supera o limite de 100%");
		}
		result.nothing();
	}
	
	@Get
	public void exportar(FileType fileType) throws IOException {
		FiltroDesenglobacaoDTO filtro = (FiltroDesenglobacaoDTO) session.getAttribute("filtroDesengloba");
		List<DesenglobacaoDTO> cotasDesenglobadas = service.obterDesenglobacaoPorCota(filtro.getCotaDto().getNumeroCota().longValue());
			
			if(cotasDesenglobadas.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("ENGLOBACAO_DESENGLOBACAO", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					cotasDesenglobadas, DesenglobacaoDTO.class, this.httpResponse);
		
		result.nothing();
	}
}
>>>>>>> 03f1ca6c8da04a45696f13aca9cd81446f5232f7
