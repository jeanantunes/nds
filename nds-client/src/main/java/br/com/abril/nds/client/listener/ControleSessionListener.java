package br.com.abril.nds.client.listener;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import br.com.abril.nds.client.util.Constants;
import br.com.abril.nds.controllers.devolucao.ConferenciaEncalheController;


public class ControleSessionListener implements HttpSessionListener {


	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {}

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

		String sessionID = session.getId();
		
		@SuppressWarnings("unchecked")
		Map<Integer, String> mapaCotaConferidaUsuario = (LinkedHashMap<Integer, String>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
		
		@SuppressWarnings("unchecked")
		Map<String, String> mapaSessionIDNomeUsuario = 
			(LinkedHashMap<String, String>) session.getServletContext().getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_SESSION_ID_NOME_USUARIO);

		
		ConferenciaEncalheController.removerTravaConferenciaCotaUsuario(sessionID, mapaCotaConferidaUsuario, mapaSessionIDNomeUsuario);
		
	}

}
