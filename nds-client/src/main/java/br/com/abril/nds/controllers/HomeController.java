package br.com.abril.nds.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import net.vidageek.mirror.dsl.Mirror;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.MenuDTO;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.UsuarioService;
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

	@Value("#{properties.version}")
	protected String version;
	
	@Autowired
	private final Router router;

	@Autowired
	private final HttpSession session;

	@Autowired
	private final Result result;

	@Autowired
	private UsuarioService usuarioService;
	
	private static Logger LOGGER = LoggerFactory.getLogger(HomeController.class);	
	
	/**
	 * @param router
	 * @param result
	 */
	public HomeController(Router router, Result result, HttpSession session) {
		this.router = router;
		this.result = result;
		this.session = session;
	}

	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Get
	@Path("/")
	public void index() {

		Map mapaMenus = geraMenus(getPermissoesUsuario());

		result.include("menus", mapaMenus);

		result.include("nomeUsuario", usuarioService.getNomeUsuarioLogado());
		result.include("versao", version);
		
		
		//result.include("changes", readJenkins());
		
		
	}

	@SuppressWarnings("unused")
	private String readJenkins() {
		
		Document doc = null;
		try {
			doc = Jsoup.connect("http://177.71.255.76:8080/jenkins/job/deploy%20nds-client%20homolog/changes").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage(), e);
		}
		Elements newsHeadlines = doc.select("#main-panel");
		
		return newsHeadlines.html();
	}

	/**
	 * Retorna a lista de permissões do usuário
	 * @return
	 */
	private List<Permissao> getPermissoesUsuario() {
		
		//alterado para garantir que a ordem do enum de permissões seja respeitada e evitar
		//que uma permissão filha seja carregada antes da permissão pai.
		Set<Permissao> permissoes = new TreeSet<Permissao>();
		for (GrantedAuthority grantedAuthority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			try {
				permissoes.add(Permissao.valueOf(grantedAuthority.getAuthority()));
			} catch (IllegalArgumentException e) {
				// Caso a permissão não exista, prossegue para a próxima permissão
				LOGGER.warn("Não foi encontrado a seguinte permissao: " + e);
				continue;
			}
		}
		return new ArrayList<>(permissoes);
	}

	/**
	 * @param routes
	 */
	private String getUrlByPermission(Permissao permissao) {

		List<Route> routes = router.allRoutes();
		
		for (Route route : routes) {
			
			ResourceMethod resourceMethod = 
				(ResourceMethod) new Mirror().on(route).get().field("resourceMethod");
			
			Rules rule = resourceMethod.getResource().getType().getAnnotation(Rules.class);
		
			if (rule != null && rule.value().equals(permissao)) {
				
				return resourceMethod.getResource().getType().getAnnotation(Path.class).value()[0];
			}
		}
		
		for (Route route : routes) {
			ResourceMethod resourceMethod = (ResourceMethod) new Mirror()
					.on(route).get().field("resourceMethod");
			Rules rule = resourceMethod.getMethod().getAnnotation(Rules.class);
			
			// Caso possua uma lista de regras, aplica as permissões de menus
			if (rule != null && rule.value() == permissao) {
				
				boolean pathIsNull = !resourceMethod.getMethod().isAnnotationPresent(Path.class);
				
				String pathBase = resourceMethod.getResource().getType().getAnnotation(Path.class).value()[0];
				
				String pathMetodo = pathIsNull ? resourceMethod.getMethod().getName() : resourceMethod.getMethod().getAnnotation(Path.class).value()[0];
				
				return pathBase + "/" + pathMetodo;
			}
		}
		
		return "";
	}

	/**
	 * Constrói um mapa de Permissões (utilizando como índice a permissão pai),
	 * gerando desta forma os menus e submenus do sistema
	 * 
	 * @return Map<Permissao, List<Permissao>>
	 */
	private Map<MenuDTO, List<MenuDTO>> geraMenus(
			Collection<Permissao> permissoes) {
		Map<MenuDTO, List<MenuDTO>> mapaMenus = new TreeMap<MenuDTO, List<MenuDTO>>();

		for (Permissao p : permissoes) {
			
			if(p.isPermissaoMenu() && !p.isPermissaoAlteracao())
				organizaMenus(p, p.getPermissaoPai(), mapaMenus);
		}

		return mapaMenus;
	}

	/**
	 * Organiza as permissões recursivamente (gerando menus e submenus a partir
	 * das permissões) Suporta menus e submenus
	 * 
	 * @param permissao
	 *            (permissão)
	 * @param permissaoPai
	 *            (permissão Pai)
	 * @param mapaMenus
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void organizaMenus(Permissao permissao, Permissao permissaoPai,
			Map mapaMenus) {
		if (permissaoPai == null) {
			mapaMenus.put(
					new MenuDTO(permissao, this.getUrlByPermission(permissao)),
					new TreeMap());
		} else {
			if (permissao.getPermissaoPai() != permissaoPai) {
				organizaMenus(permissao, permissao.getPermissaoPai(), mapaMenus);
			} else {
				insereMenus(permissao, mapaMenus);
			}
		}

	}

	/**
	 * Retorna o mapa de permissões do menu pai
	 * 
	 * @param mapaMenus
	 * @param permissao
	 * @return Map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map insereMenus(Permissao permissao, Map mapaMenus) {

		MenuDTO menuDTO = new MenuDTO(permissao.getPermissaoPai(),
				this.getUrlByPermission(permissao.getPermissaoPai()));

		Permissao paiDoPai = permissao.getPermissaoPai();
		
		Map mapa= (Map) mapaMenus.get(new MenuDTO(paiDoPai, this.getUrlByPermission(paiDoPai)));
		
		if (mapaMenus.get(menuDTO) == null) {
			insereMenus(permissao, mapa);
		} else {
			((Map) mapaMenus.get(menuDTO)).put(
					new MenuDTO(permissao, this.getUrlByPermission(permissao)),
					new TreeMap());
		}
		return (Map) mapaMenus.get(menuDTO);
	}

}
