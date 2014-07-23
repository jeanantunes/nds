package br.com.abril.nds.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.Constants;
import br.com.abril.nds.client.vo.CotaBloqueadaVO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cotaBloqueada")
public class CotaBloqueadaController extends BaseController {
	
	@Autowired
	private HttpSession session;
	
	private Result result;
	
	@Autowired
	private CotaService cotaService;

	public CotaBloqueadaController(Result result) {
		
		super();
		
		this.result = result;
	}

	@Path("/")
	public void index() {
		
	}
	
	private Map<Integer, String> obterCotasEmConferenciaEncalhe(){
		
		synchronized (this.session.getServletContext()) {
			
			@SuppressWarnings("unchecked")
			Map<Integer, String> mapaCotaConferidaUsuario = (LinkedHashMap<Integer, String>) session.getServletContext().getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
			
			return mapaCotaConferidaUsuario;
		}
	}

	@Post
	@Path("/existeConferenciaEncalheEmAndamento")
	public void existeConferenciaEncalheEmAndamento(){
		
		boolean existeConferenciaEmAndamento = false;
		
		Map<Integer, String> mapaCotaConferidaUsuario = this.obterCotasEmConferenciaEncalhe();
		
		existeConferenciaEmAndamento = (mapaCotaConferidaUsuario!=null && mapaCotaConferidaUsuario.size() > 0);
		
		result.use(Results.json()).from(existeConferenciaEmAndamento , "result").recursive().serialize();
	}
	
	@SuppressWarnings("unchecked")
	private String obterNomeUsuarioLockandoCota(String donoDoLockCotaConferida) {
		
		final Map<String, String> mapaLoginNomeUsuario = (LinkedHashMap<String, String>) this.session.getServletContext()
				.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_LOGIN_NOME_USUARIO);

		String nomeUsuario = "Não identificado";

		if (mapaLoginNomeUsuario != null
				&& mapaLoginNomeUsuario.get(donoDoLockCotaConferida) != null) {
			nomeUsuario = mapaLoginNomeUsuario.get(donoDoLockCotaConferida);
		}
		
		return nomeUsuario;

	}
	
	@Post
	@Path("/obterCotasBloqueadas")
	public void obterCotasBloqueadas(){
		
		Map<Integer, String> mapaCotaConferidaUsuario = this.obterCotasEmConferenciaEncalhe();
		
		List<CotaBloqueadaVO> cbsVO = new ArrayList<CotaBloqueadaVO>();
		
		if (mapaCotaConferidaUsuario!=null && !mapaCotaConferidaUsuario.isEmpty()){
		
			for ( Entry<Integer, String> entry : mapaCotaConferidaUsuario.entrySet()){
				
				Cota c = this.cotaService.obterPorNumeroDaCota(entry.getKey());
				
				String nomeUsuario = obterNomeUsuarioLockandoCota(entry.getValue());
				
                cbsVO.add(new CotaBloqueadaVO(nomeUsuario,
						                      c.getNumeroCota(),
						                      c.getPessoa().getNome()));
				
			}
		}
		
		TableModel<CellModelKeyValue<CotaBloqueadaVO>> tableModel = new TableModel<CellModelKeyValue<CotaBloqueadaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(cbsVO));
		
		tableModel.setPage(1);
		
		tableModel.setTotal(cbsVO.size());

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
}
