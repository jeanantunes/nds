package br.com.abril.nds.client.listener;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.abril.nds.client.util.Constants;
import br.com.abril.nds.controllers.devolucao.ConferenciaEncalheController;


public class ControleSessionListener implements HttpSessionListener {


	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		
//		sessionEvent.getSession().setAttribute("X9 LISTENER", 
//				SecurityContextHolder.getContext().getAuthentication().getName());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		
		HttpSession session = sessionEvent.getSession();
		
		ServletContext context = session.getServletContext();
		
		removerTravaConferenciaCotaUsuario(context, session);

	}
	
	/**
	 * Remove a trava de cota conferida por usuario 
	 * quando a session do mesmo é destruída.
	 * 
	 * @param context
	 * @param session
	 */
	private void removerTravaConferenciaCotaUsuario(ServletContext context, HttpSession session) {
		
		String loginUsuarioLogado = 
			SecurityContextHolder.getContext().getAuthentication().getName() + session.getId();
		
		@SuppressWarnings("unchecked")
		Map<Integer, String> mapaCotaConferidaUsuario = (LinkedHashMap<Integer, String>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
		
		@SuppressWarnings("unchecked")
		Map<String, String> mapaLoginNomeUsuario = 
			(LinkedHashMap<String, String>) session.getServletContext().getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_LOGIN_NOME_USUARIO);

		
		ConferenciaEncalheController.removerTravaConferenciaCotaUsuario(session.getServletContext(), loginUsuarioLogado, mapaCotaConferidaUsuario, mapaLoginNomeUsuario);
		
	}

}
