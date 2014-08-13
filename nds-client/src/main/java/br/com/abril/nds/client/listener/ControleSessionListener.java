package br.com.abril.nds.client.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.abril.nds.client.component.BloqueioConferenciaEncalheComponent;
import br.com.abril.nds.controllers.devolucao.MatrizRecolhimentoController;
import br.com.abril.nds.controllers.distribuicao.HistogramaPosEstudoController;
import br.com.abril.nds.controllers.lancamento.MatrizLancamentoController;


public class ControleSessionListener implements HttpSessionListener {


	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		
		HttpSession session = sessionEvent.getSession();
		
		ServletContext context = session.getServletContext();
		
		removerTravaConferenciaCotaUsuario(context, session);
		
		removerTravaAnaliseEstudo(context);
		
		removerTravaMatrizRecolhimento(context);
		
		removerTravaMatrizLancamento(context);
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
	private void removerTravaConferenciaCotaUsuario(ServletContext context, HttpSession session) {
		
		  ApplicationContext ctx = 
	                WebApplicationContextUtils.
	                      getWebApplicationContext(session.getServletContext());
		
		BloqueioConferenciaEncalheComponent bloqueioConferenciaEncalheComponent = ctx.getBean(BloqueioConferenciaEncalheComponent.class);
		
		bloqueioConferenciaEncalheComponent.removerTravaConferenciaCotaUsuario(session);
		
	}
	
	/**
	 * Remove a trava de análise de estudo por usuario. 
	 * 
	 * @param context
	 */
	private void removerTravaAnaliseEstudo(ServletContext context) {
		
	    String usuario = this.getUsuario();
	    
	    if (usuario == null) {
	        
	        return;
	    }
	    
		HistogramaPosEstudoController.desbloquearAnaliseEstudo(context, usuario);
	}
	
	/**
     * Remove a trava da matriz de recolhimento. 
     * 
     * @param context
     */
    private void removerTravaMatrizRecolhimento(ServletContext context) {
        
        String usuario = this.getUsuario();
        
        if (usuario == null) {
            
            return;
        }

        MatrizRecolhimentoController.desbloquearMatrizRecolhimento(context, usuario);
    }
    
    /**
     * Remove a trava da matriz de lançamento. 
     * 
     * @param context
     */
    private void removerTravaMatrizLancamento(ServletContext context) {
        
        String usuario = this.getUsuario();
        
        if (usuario == null) {
            
            return;
        }

        MatrizLancamentoController.desbloquearMatrizLancamento(context, usuario);
    }
	
}
