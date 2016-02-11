package br.com.abril.nds.controllers;

import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import net.vidageek.mirror.dsl.Mirror;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.listener.ControleSessionListener;
import br.com.abril.nds.dto.MenuDTO;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.GeracaoNotaEnvioService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.Intervalo;
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
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
  
    
    @Value("#{properties.version}")
    protected String version;
    
    @Value("#{properties.changelog}")
    protected String changeLog;
    
    @Autowired
    private final Router router;
    
    @Autowired
    private final Result result;
    
    @Autowired
    private UsuarioService usuarioService;
    
    
    @Autowired
	private HttpSession session;
    
    
    /**
     * @param router
     * @param result
     */
    public HomeController(final Router router, final Result result) {
        this.router = router;
        this.result = result;
        
    }
    
    /**
     * 
     */
    @SuppressWarnings("rawtypes")
    @Get
    @Path("/")
    public void index() {
        
        final Map mapaMenus = geraMenus(getPermissoesUsuario());
        
        
        // guardar informacao do usuario que esta logando 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null) {
        	
        	session.setAttribute(ControleSessionListener.USUARIO_LOGADO,authentication.getName());
            
        }
            
        String remoteAddress = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();
        
        List l = ControleSessionListener.usuariosLogado.get(authentication.getName());
        if ( l == null ) {
          l = new LinkedList();
          l.add(remoteAddress+"->"+new Date());
          ControleSessionListener.usuariosLogado.put(authentication.getName(), l );
        }
        else {
	        l.add(remoteAddress+"->"+new Date());
	        ControleSessionListener.usuariosLogado.put(authentication.getName(), l);
        }
       
       
        result.include("menus", mapaMenus);
        result.include("nomeUsuario", usuarioService.getNomeUsuarioLogado());
        result.include("versao", version);
        if ( "admin".equals(authentication.getName()))
           result.include("changes", obterInformacoesDoSistema());
        		
        		 
        		 
         		
    }
    
    public String obterInformacoesDoSistema() {
    try {
    				
		URL urlClass = this.getClass().getProtectionDomain().getCodeSource().getLocation();
		 
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	     
    
     	java.nio.file.Path path = java.nio.file.Paths.get(urlClass.toURI());
     	
     	BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
        
     	Date dts = new Date(attr.lastModifiedTime().toMillis());
        
        String  dt = format.format(dts);

        return "Build:"+dt+" Versao:"+version+"</br>Usuarios Logado ("+ControleSessionListener.usuariosLogado.size()+"):</br>"+
		    ControleSessionListener.usuariosLogado.toString()
		   .replaceAll("],","</br> ")
		   .replaceAll("\\{","")
		    .replaceAll("\\}","")+
		    "</br>Alteracoes:</br>"+changeLog;
         
         
    	} catch ( Exception ee ) {
    		LOGGER.error("Erro obtendo informacoes do sistema",ee);
    	}
    	return "Erro obtendo informacoes do sistema";
    }
    
    /**
     * Retorna a lista de permissões do usuário
     * 
     * @return
     */
    private List<Permissao> getPermissoesUsuario() {
        
        // alterado para garantir que a ordem do enum de permissões seja
        // respeitada e evitar
        // que uma permissão filha seja carregada antes da permissão pai.
        final Set<Permissao> permissoes = new TreeSet<Permissao>();
        for (final GrantedAuthority grantedAuthority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            try {
            	
            	if ( grantedAuthority != null && grantedAuthority.getAuthority() != null )
                   permissoes.add(Permissao.valueOf(grantedAuthority.getAuthority()));
            } catch (final IllegalArgumentException e) {
                LOGGER.warn("Não foi encontrado a seguinte permissao: " + grantedAuthority.getAuthority(), e);
                continue;
            }
        }
        return new ArrayList<>(permissoes);
    }
    
    /**
     * @param routes
     */
    private String getUrlByPermission(final Permissao permissao) {
        
        final List<Route> routes = router.allRoutes();
        
        for (final Route route : routes) {
            
            final ResourceMethod resourceMethod = (ResourceMethod) new Mirror().on(route).get().field("resourceMethod");
            
            final Rules rule = resourceMethod.getResource().getType().getAnnotation(Rules.class);
            
            if (rule != null && rule.value().equals(permissao)) {
                
                return resourceMethod.getResource().getType().getAnnotation(Path.class).value()[0];
            }
        }
        
        for (final Route route : routes) {
            final ResourceMethod resourceMethod = (ResourceMethod) new Mirror().on(route).get().field("resourceMethod");
            final Rules rule = resourceMethod.getMethod().getAnnotation(Rules.class);
            
            // Caso possua uma lista de regras, aplica as permissões de menus
            if (rule != null && rule.value() == permissao) {
                
                final boolean pathIsNull = !resourceMethod.getMethod().isAnnotationPresent(Path.class);
                
                final String pathBase = resourceMethod.getResource().getType().getAnnotation(Path.class).value()[0];
                
                final String pathMetodo = pathIsNull ? resourceMethod.getMethod().getName() : resourceMethod
                        .getMethod().getAnnotation(Path.class).value()[0];
                
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
    private Map<MenuDTO, List<MenuDTO>> geraMenus(final Collection<Permissao> permissoes) {
        final Map<MenuDTO, List<MenuDTO>> mapaMenus = new TreeMap<MenuDTO, List<MenuDTO>>();
        
        for (final Permissao p : permissoes) {
            
        	if(Permissao.ROLE_DISTRIBUICAO_HISTOGRAMA_POS_ESTUDO.equals(p)){
        		continue;
        	}
        	
            if (p.isPermissaoMenu() && !p.isPermissaoAlteracao()) {
                organizaMenus(p, p.getPermissaoPai(), mapaMenus);
            }
        }
        
        return mapaMenus;
    }
    
    /**
     * Organiza as permissões recursivamente (gerando menus e submenus a partir
     * das permissões) Suporta menus e submenus
     * 
     * @param permissao (permissão)
     * @param permissaoPai (permissão Pai)
     * @param mapaMenus
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void organizaMenus(final Permissao permissao, final Permissao permissaoPai, final Map mapaMenus) {
        if (permissaoPai == null) {
            mapaMenus.put(new MenuDTO(permissao, this.getUrlByPermission(permissao)), new TreeMap());
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
    private Map insereMenus(final Permissao permissao, final Map mapaMenus) {
        
        final MenuDTO menuDTO = new MenuDTO(permissao.getPermissaoPai(), this.getUrlByPermission(permissao.getPermissaoPai()));
        
        final Permissao paiDoPai = permissao.getPermissaoPai();
        
        final Map mapa = (Map) mapaMenus.get(new MenuDTO(paiDoPai, this.getUrlByPermission(paiDoPai)));
        
        if (mapaMenus.get(menuDTO) == null) {
            insereMenus(permissao, mapa);
        } else {
            ((Map) mapaMenus.get(menuDTO)).put(new MenuDTO(permissao, this.getUrlByPermission(permissao)), new TreeMap());
        }
        return (Map) mapaMenus.get(menuDTO);
    }
    
}
