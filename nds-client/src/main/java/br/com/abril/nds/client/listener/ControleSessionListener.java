package br.com.abril.nds.client.listener;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.abril.nds.client.util.Constants;
import br.com.abril.nds.controllers.devolucao.ConferenciaEncalheController;
import br.com.abril.nds.controllers.distribuicao.AnaliseEstudoController;


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
	}
	
	/**
	 * Remove a trava de cota conferida por usuario 
	 * quando a session do mesmo é destruída.
	 * 
	 * @param context
	 * @param session
	 */
	@SuppressWarnings("unchecked")
	private void removerTravaConferenciaCotaUsuario(ServletContext context, HttpSession session) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null) {
			
			return;
		}
		
		String autenticacaoUsuarioLogado = authentication.getName() + session.getId();
		
		Map<Integer, String> mapaCotaConferidaUsuario = 
			(LinkedHashMap<Integer, String>) context.getAttribute(
				Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
		
		Map<String, String> mapaLoginNomeUsuario = 
			(LinkedHashMap<String, String>) session.getServletContext().getAttribute(
				Constants.MAP_TRAVA_CONFERENCIA_COTA_LOGIN_NOME_USUARIO);
		
		ConferenciaEncalheController.removerTravaConferenciaCotaUsuario(
			session.getServletContext(), autenticacaoUsuarioLogado, 
				mapaCotaConferidaUsuario, mapaLoginNomeUsuario);
	}
	
	/**
	 * Remove a trava de análise de estudo por usuario. 
	 * 
	 * @param context
	 */
	private void removerTravaAnaliseEstudo(ServletContext context) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null) {
			
			return;
		}
		
		AnaliseEstudoController.desbloquearAnaliseEstudo(context, authentication.getName());
	}

}
