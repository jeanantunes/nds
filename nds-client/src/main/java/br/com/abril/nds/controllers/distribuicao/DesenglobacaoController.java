package br.com.abril.nds.controllers.distribuicao;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.filtro.FiltroClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroDesenglobacaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/distribuicao/desenglobacao")
public class DesenglobacaoController extends BaseController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroDesenglobacaoDTO";
	
	private static final ValidacaoVO VALIDACAO_VO_MENSAGEM_SUCESSO = new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso.");
	
	@Autowired()
	private Result result;

	@Autowired
	private HttpSession session;

	@Autowired
	private HttpServletResponse httpResponse; 
	
	@Rules(Permissao.ROLE_DISTRIBUICAO_DESENGLOBACAO)
	@Path("/index")
	public void desenglobacao(){
		
	}
	
	@Post()
	public void pesquisaPrincipal(FiltroDesenglobacaoDTO filtro, String sortorder, String sortname, int page, int rp ){
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		validarFiltroDesenglobacao(filtro);
		
		result.nothing();
		
	}

	private void validarFiltroDesenglobacao(FiltroDesenglobacaoDTO filtro) {
		if((filtro.getCotaDto().getNumeroCota() == null || filtro.getCotaDto().getNumeroCota() == 0) && 
				(filtro.getCotaDto().getNomePessoa() == null || filtro.getCotaDto().getNomePessoa().trim().isEmpty())) 
				//&&(filtro.getCotaDto().getStatus() == null))
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe um código ou nome ou status da cota.");		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private TableModel configurarTableModelComPaginacao( List listaDto, TableModel tableModel, FiltroDTO filtro){
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDto));
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	
	private void guardarFiltroNaSession(FiltroDesenglobacaoDTO filtro) {
		
		FiltroDesenglobacaoDTO filtroSession = (FiltroDesenglobacaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)){
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
}
