package br.com.abril.nds.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import net.vidageek.mirror.dsl.Mirror;

import org.apache.log4j.Logger;
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
	
	private String changes;

	private List<MenuDTO> menus;

	private static Logger LOGGER = Logger.getLogger(HomeController.class);	
	
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
	@Get
	@Path("/")
	public void index() {

		menus = new ArrayList<MenuDTO>();

		MenuDTO menuDTO = null;

		Map mapaMenus = geraMenus(getPermissoesUsuario());

		result.include("menus", mapaMenus);

		result.include("nomeUsuario", usuarioService.getNomeUsuarioLogado());
		result.include("versao", version);
		
		
		result.include("changes", readJenkins());
		
		
	}

	private String readJenkins() {
		StringBuilder builder = new StringBuilder();
		
		/*Document doc = null;
		try {
			doc = Jsoup.connect("http://177.71.255.76:8080/jenkins/job/deploy%20nds-client%20homolog/changes").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Elements newsHeadlines = doc.select("#main-panel");
		
		return newsHeadlines.html();*/
		return "";
	}

	/**
	 * Retorna a lista de permissões do usuário
	 * @return
	 */
	private List<Permissao> getPermissoesUsuario() {
		List<Permissao> permissoes = new ArrayList<Permissao>();
		for (GrantedAuthority grantedAuthority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			try {
				permissoes.add(Permissao.valueOf(grantedAuthority.getAuthority()));
			} catch (IllegalArgumentException e) {
				// Caso a permissão não exista, prossegue para a próxima permissão
				LOGGER.warn("Não foi encontrado a seguinte permissao: " + e);
				continue;
			}
		}
		return permissoes;
	}

	/**
	 * @param routes
	 */
	private String getUrlByPermission(Permissao permissao) {

		List<Route> routes = router.allRoutes();

		for (Route route : routes) {
			ResourceMethod resourceMethod = (ResourceMethod) new Mirror()
					.on(route).get().field("resourceMethod");
			Rules rule = resourceMethod.getMethod().getAnnotation(Rules.class);
			// Caso possua uma lista de regras, aplica as permissões de menus
			if (rule != null && rule.value() == permissao) {
				return route.getOriginalUri();
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
	private Map insereMenus(Permissao permissao, Map mapaMenus) {

		MenuDTO menuDTO = new MenuDTO(permissao.getPermissaoPai(),
				this.getUrlByPermission(permissao.getPermissaoPai()));

		if (mapaMenus.get(menuDTO) == null) {
			insereMenus(permissao, (Map) mapaMenus.get(new MenuDTO(permissao
					.getPermissaoPai().getPermissaoPai(), this
					.getUrlByPermission(permissao.getPermissaoPai()
							.getPermissaoPai()))));
		} else {
			((Map) mapaMenus.get(menuDTO)).put(
					new MenuDTO(permissao, this.getUrlByPermission(permissao)),
					new TreeMap());
		}
		return (Map) mapaMenus.get(menuDTO);
	}

}
