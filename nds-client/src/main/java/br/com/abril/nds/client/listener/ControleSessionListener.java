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


public class ControleSessionListener implements HttpSessionListener {


	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		
		HttpSession session = sessionEvent.getSession();
		
		ServletContext context = session.getServletContext();
		
		removerTravaCotaConferidaUsuario(context, session);

	}
	
	/**
	 * Remove a trava de cota conferida por usuario 
	 * quando a session do mesmo é destruída.
	 * 
	 * @param context
	 * @param session
	 */
	private void removerTravaCotaConferidaUsuario(ServletContext context, HttpSession session) {

		String sessionID = session.getId();
		
		@SuppressWarnings("unchecked")
		Map<Integer, String> mapaCotaConferidaUsuario = (LinkedHashMap<Integer, String>) context.getAttribute(Constants.MAP_COTA_CONFERIDA_USUARIO);
		
		if(mapaCotaConferidaUsuario == null || mapaCotaConferidaUsuario.isEmpty()) {
			return;
		}
		
		Set<Integer> cotasEmConferencia = mapaCotaConferidaUsuario.keySet();
	
		for(Integer numeroCota : cotasEmConferencia) {
			if( mapaCotaConferidaUsuario.get(numeroCota).equals(sessionID) ) {
				mapaCotaConferidaUsuario.remove(numeroCota);
			}
		}
		
		
	}

}
