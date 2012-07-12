package br.com.abril.nds.controllers;

import java.util.ArrayList;
import java.util.List;

import net.vidageek.mirror.dsl.Mirror;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.MenuDTO;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.http.route.Route;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Resource
@Path("/")
public class HomeController {

	@Autowired
	private final Router router;

	@Autowired
	private final Result result;

	private List<MenuDTO> menus;
	
	/**
	 * @param router
	 * @param result
	 */
	public HomeController(Router router, Result result) {
		this.router = router;
		this.result = result;
	}

	/**
	 * 
	 */
	@Get
	@Path("/")
	public void index() {

		List<Route> routes = router.allRoutes();

		menus = new ArrayList<MenuDTO>();
		
		MenuDTO menuDTO = null;
		
		for (Route route : routes) {
			//String verb = route.allowedMethods().toArray()[0].toString();
			String uri = route.getOriginalUri();
			ResourceMethod resourceMethod = (ResourceMethod) new Mirror().on(route).get().field("resourceMethod");
			//String controllerName = resourceMethod.getMethod().getDeclaringClass().getSimpleName();
			//String action = resourceMethod.getMethod().getName();
			
			Rules rule = resourceMethod.getMethod().getAnnotation(Rules.class);
			// Caso possua uma lista de regras, aplica as permissões de menus
			if (rule!= null) {
				Permissao permissao = rule.value();

				menuDTO = new MenuDTO();
				menuDTO.setPermissao(permissao);
				menuDTO.setUrl(uri);

				menus.add(menuDTO);
				
			}
			
		}

		result.include("menus", menus);
		
	}

}
