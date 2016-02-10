package br.com.abril.nds.client.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.abril.nds.client.component.BloqueioConferenciaEncalheComponent;
import br.com.abril.nds.controllers.devolucao.MatrizRecolhimentoController;
import br.com.abril.nds.controllers.distribuicao.HistogramaPosEstudoController;
import br.com.abril.nds.controllers.lancamento.MatrizLancamentoController;


public class ControleSessionListener implements HttpSessionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ControleSessionListener.class);
	
	public static final String USUARIO_LOGADO="usuario_logado";
	
	public static Map <String,List> usuariosLogado=new HashMap();
	
	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		
		
	
	 }
	

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		
		HttpSession session = sessionEvent.getSession();
		
		removerTravaConferenciaCotaUsuario(session);
		
		removerTravaAnaliseEstudo(session);
		
		removerTravaMatrizRecolhimento(session);
		
		removerTravaMatrizLancamento(session);
		
		  String usuario= this.getUsuario();
	       
	        if (usuario == null) {
	            
	       	 usuario = (String) session.getAttribute(USUARIO_LOGADO);
	       }
	       
	       if (usuario != null) {
	         
	        	      usuariosLogado.remove(usuario);
	        	   
	        
	      }
		
			
		
	}

    private String getUsuario() {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
        	
            
            return null;
        }
        
        return authentication.getName();
    }
	
	/**
	 * Remove a trava de cota conferida por usuario 
	 * quando a session do mesmo é destruída.
	 * 
	 * @param context
	 * @param session
	 */
	private void removerTravaConferenciaCotaUsuario(HttpSession session) {
		
	 try {
			
		  ApplicationContext ctx = 
	                WebApplicationContextUtils.
	                      getWebApplicationContext(session.getServletContext());
		
		BloqueioConferenciaEncalheComponent bloqueioConferenciaEncalheComponent = ctx.getBean(BloqueioConferenciaEncalheComponent.class);
		
		bloqueioConferenciaEncalheComponent.removerTravaConferenciaCotaUsuario(session);
		
	 } catch (Exception e ) {
		 
		  e.printStackTrace();
		  
	 }
	 
		
	}
	
	/**
	 * Remove a trava de análise de estudo por usuario. 
	 * 
	 * @param context
	 */
	private void removerTravaAnaliseEstudo(HttpSession session) {
		
	    String usuario = this.getUsuario();
	    
	    
        if (usuario == null) {
            
       	 usuario = (String) session.getAttribute(USUARIO_LOGADO);
       }
       
       if (usuario == null) {
           
    	   LOGGER.warn("AVISO: sessao encerrando e nao foi feito desbloqueios pois nao foi possivel obter o usuario corrente");
      	  return;
      }
	    
		HistogramaPosEstudoController.desbloquearAnaliseEstudo(session,usuario);
	}
	
	/**
     * Remove a trava da matriz de recolhimento. 
     * 
     * @param context
     */
    private void removerTravaMatrizRecolhimento(HttpSession session) {
        
        String usuario= this.getUsuario();
       
        if (usuario == null) {
            
       	 usuario = (String) session.getAttribute(USUARIO_LOGADO);
       }
       
       if (usuario == null) {
           
    	   LOGGER.warn("AVISO: sessao encerrando e nao foi feito desbloqueios pois nao foi possivel obter o usuario corrente");
      	  return;
      }

    
        MatrizRecolhimentoController.desbloquearMatrizRecolhimento(session.getServletContext(),usuario);
    }
    
    /**
     * Remove a trava da matriz de lançamento. 
     * 
     * @param context
     */
    private void removerTravaMatrizLancamento(HttpSession session) {
        
    	ServletContext context = session.getServletContext();
    	
        String usuario = this.getUsuario();
        
        if (usuario == null) {
            
        	 usuario = (String) session.getAttribute(USUARIO_LOGADO);
        }
        
        if (usuario == null) {
            
        	LOGGER.warn("AVISO: sessao encerrando e nao foi feito desbloqueios pois nao foi possivel obter o usuario corrente");
       	  return;
       }


        MatrizLancamentoController.desbloquearMatrizLancamento(context, usuario);
    }
	
}
