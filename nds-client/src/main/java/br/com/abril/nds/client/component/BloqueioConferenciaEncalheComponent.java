package br.com.abril.nds.client.component;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.client.util.Constants;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.UsuarioService;

@Component
public class BloqueioConferenciaEncalheComponent {
	
	@Autowired
	private UsuarioService usuarioService;

	/**
	 * Se o usuário (session id) ja estiver conferindo alguma cota,
	 * não sera permitido que este inicie uma nova conferência.
	 * 
	 * @session
	 */
	public void validarUsuarioConferindoCota(HttpSession session, Integer numeroCota) {
		
//		String userSessionID = getIdentificacaoUnicaUsuarioLogado(session);
		String user = usuarioService.getUsuarioLogado().getLogin();
		
		final ServletContext context = session.getServletContext();
		
		@SuppressWarnings("unchecked")
		final Map<Integer, CotaUsuarioConferencia> mapaCotaConferidaUsuario = (LinkedHashMap<Integer, CotaUsuarioConferencia>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
		
		if (mapaCotaConferidaUsuario != null && (mapaCotaConferidaUsuario.containsKey(numeroCota))) {
			
			CotaUsuarioConferencia cuc = mapaCotaConferidaUsuario.get(numeroCota); 
			if(cuc.getLogin().equals(user) && cuc.getSessionId().equals(session.getId())) {

				throw new ValidacaoException(TipoMensagem.WARNING, String.format("Não é possível executar Conferência de Encalhe simultânea para o usuário: %s!", user));
			} else {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Não é possível executar mais de uma conferência de encalhe ao mesmo tempo!");
			}
		}
	}
	
	private void verificarTravaConferenciaCotaUsuario(final Integer numeroCota, HttpSession session) {
		
//		final String userSessionID = getIdentificacaoUnicaUsuarioLogado(session);
		final String user = usuarioService.getNomeUsuarioLogado();
		
		final ServletContext context = session.getServletContext();
		
		@SuppressWarnings("unchecked")
		final
		Map<Integer, CotaUsuarioConferencia> mapaCotaConferidaUsuario = (LinkedHashMap<Integer, CotaUsuarioConferencia>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
		
		@SuppressWarnings("unchecked")
		final
		Map<String, String> mapaLoginNomeUsuario = (LinkedHashMap<String, String>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_LOGIN_NOME_USUARIO);
		
			
		if(mapaCotaConferidaUsuario == null || mapaCotaConferidaUsuario.isEmpty()) {
			return;
		} 
		
		validarUsuarioConferindoCota(session, numeroCota);
		
		if(!mapaCotaConferidaUsuario.containsKey(numeroCota)) {
			return;
		}
		
		final CotaUsuarioConferencia donoDoLockCotaConferida = mapaCotaConferidaUsuario.get(numeroCota);
		
		if(user.equals(donoDoLockCotaConferida)) {
			return;
		}
		
        String nomeUsuario = "Não identificado";
		
		if(mapaLoginNomeUsuario != null && mapaLoginNomeUsuario.get(donoDoLockCotaConferida) != null) {
			nomeUsuario = mapaLoginNomeUsuario.get(donoDoLockCotaConferida);
		}
		
		throw new ValidacaoException(TipoMensagem.WARNING, 
                " Não é possível iniciar a conferência de encalhe para esta cota, "
                    + " a mesma esta sendo conferida pelo(a) usuário(a) [ " + nomeUsuario + " ] ");
	
		
	}
	
	public void removerTravaConferenciaCotaUsuario(HttpSession session) {
		
		final String userSessionID = getIdentificacaoUnicaUsuarioLogado(session);
		
		@SuppressWarnings("unchecked")
		final
		Map<Integer, String> mapaCotaConferidaUsuario = (LinkedHashMap<Integer, String>) session.getServletContext().getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
		
		@SuppressWarnings("unchecked")
		final
		Map<String, String> mapaLoginNomeUsuario = 
			(LinkedHashMap<String, String>) session.getServletContext().getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_LOGIN_NOME_USUARIO);

		
		if(mapaLoginNomeUsuario != null) {
			mapaLoginNomeUsuario.remove(userSessionID);
		}
		
		if(mapaCotaConferidaUsuario == null || mapaCotaConferidaUsuario.isEmpty()) {
			return;
		}
		
		final Set<Integer> cotasEmConferencia = new HashSet<>(mapaCotaConferidaUsuario.keySet()) ;

		for(final Integer numeroCota : cotasEmConferencia) {
			if( mapaCotaConferidaUsuario.get(numeroCota).equals(userSessionID) ) {
				mapaCotaConferidaUsuario.remove(numeroCota);
			}
		}
	
	}
	
	public String getIdentificacaoUnicaUsuarioLogado(HttpSession session) {
		
		final String sessionID = session.getId();
		
		final String userLogin = (String) session.getAttribute(Constants.USER_LOGIN_PARA_TRAVA_CONFERENCIA);
		
		if (userLogin != null && !userLogin.isEmpty()) {
			
			return userLogin + sessionID;
		}
		
        return "Não Identificado";
	}

	public void atribuirTravaConferenciaCotaUsuario(Integer numeroCota, HttpSession session) {
		
		definirIdentificaoDoUsuario(session);
		
//		final String userSessionID = this.getIdentificacaoUnicaUsuarioLogado(session);
		final String user = usuarioService.getUsuarioLogado().getLogin();
		
		verificarTravaConferenciaCotaUsuario(numeroCota, session);

		final ServletContext context = session.getServletContext();

		@SuppressWarnings("unchecked")
		Map<Integer, CotaUsuarioConferencia> mapaCotaConferidaUsuario = 
			(LinkedHashMap<Integer, CotaUsuarioConferencia>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);

		@SuppressWarnings("unchecked")
		Map<String, String> mapaLoginNomeUsuario = 
			(LinkedHashMap<String, String>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_LOGIN_NOME_USUARIO);

		
		if(mapaCotaConferidaUsuario == null) {
			mapaCotaConferidaUsuario = new LinkedHashMap<>();
			context.setAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO, mapaCotaConferidaUsuario);
		}
		

		if(mapaLoginNomeUsuario == null) {
			mapaLoginNomeUsuario = new LinkedHashMap<>();
			context.setAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_LOGIN_NOME_USUARIO, mapaLoginNomeUsuario);
		}

		mapaLoginNomeUsuario.put(user, (String) session.getAttribute(Constants.USER_NAME_PARA_TRAVA_CONFERENCIA));
		
		mapaCotaConferidaUsuario.put(numeroCota, new CotaUsuarioConferencia(user, session.getId(), numeroCota));
	}

	
	private void definirIdentificaoDoUsuario(HttpSession session) {

		final Usuario usuario = usuarioService.getUsuarioLogado();
		
		if(usuario == null) {
            throw new ValidacaoException(TipoMensagem.WARNING ,"Não foi possível identificar o usuário logado");
		}
		
		String nome = "Não Identificado";
		
		if(usuario.getNome() != null && !usuario.getNome().isEmpty()) {
			nome = usuario.getNome();
		}
		
		if(usuario.getLogin() == null || usuario.getLogin().isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING ,"Não foi possível identificar o usuário logado");
		}
		
		session.setAttribute(Constants.USER_LOGIN_PARA_TRAVA_CONFERENCIA, usuario.getLogin());
		session.setAttribute(Constants.USER_NAME_PARA_TRAVA_CONFERENCIA, nome);

	}

	class CotaUsuarioConferencia implements Serializable {
		
		private String login;
		
		private String sessionId;
		
		private Integer numeroCota;
		
		CotaUsuarioConferencia(String login, String sessionId, Integer numeroCota) throws IllegalArgumentException {
			this.login = login;
			this.sessionId = sessionId;
			this.numeroCota = numeroCota;
		}

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getSessionId() {
			return sessionId;
		}

		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}

		public Integer getNumeroCota() {
			return numeroCota;
		}

		public void setNumeroCota(Integer numeroCota) {
			this.numeroCota = numeroCota;
		}
		
		
	}
	
}