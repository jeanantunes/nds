package br.com.abril.nds.client.component;

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
	public void validarUsuarioConferindoCota(HttpSession session) {
		
		String userSessionID = getIdentificacaoUnicaUsuarioLogado(session);
		
		final ServletContext context = session.getServletContext();
		
		@SuppressWarnings("unchecked")
		final Map<Integer, String> mapaCotaConferidaUsuario = 
			(LinkedHashMap<Integer, String>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
		
		if (mapaCotaConferidaUsuario != null && mapaCotaConferidaUsuario.containsValue(userSessionID)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não é possível executar mais de uma conferência de encalhe ao mesmo tempo!");
		}
	}
	
	private void verificarTravaConferenciaCotaUsuario(final Integer numeroCota, HttpSession session) {
		
		final String userSessionID = getIdentificacaoUnicaUsuarioLogado(session);
		
		final ServletContext context = session.getServletContext();
		
		@SuppressWarnings("unchecked")
		final
		Map<Integer, String> mapaCotaConferidaUsuario = (LinkedHashMap<Integer, String>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
		
		@SuppressWarnings("unchecked")
		final
		Map<String, String> mapaLoginNomeUsuario = (LinkedHashMap<String, String>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_LOGIN_NOME_USUARIO);
		
			
		if(mapaCotaConferidaUsuario==null || mapaCotaConferidaUsuario.isEmpty()) {
			return;
		} 
		
		validarUsuarioConferindoCota(session);
		
		if(!mapaCotaConferidaUsuario.containsKey(numeroCota)) {
			return;
		}
		
		final String donoDoLockCotaConferida = mapaCotaConferidaUsuario.get(numeroCota);
		
		if(userSessionID.equals(donoDoLockCotaConferida)) {
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
		
		final String userSessionID = this.getIdentificacaoUnicaUsuarioLogado(session);
		
		verificarTravaConferenciaCotaUsuario(numeroCota, session);

		final ServletContext context = session.getServletContext();

		@SuppressWarnings("unchecked")
		Map<Integer, String> mapaCotaConferidaUsuario = 
			(LinkedHashMap<Integer, String>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);

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

		mapaLoginNomeUsuario.put(userSessionID, (String) session.getAttribute(Constants.USER_NAME_PARA_TRAVA_CONFERENCIA));
		
		mapaCotaConferidaUsuario.put(numeroCota, userSessionID);
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

	
	
}
