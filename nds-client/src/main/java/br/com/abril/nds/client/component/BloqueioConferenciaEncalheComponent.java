package br.com.abril.nds.client.component;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.util.Constants;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.ConferenciaEncalheCotaUsuario;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConferenciaEncalheCotaUsuarioRepository;
import br.com.abril.nds.service.UsuarioService;

@Component
public class BloqueioConferenciaEncalheComponent {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BloqueioConferenciaEncalheComponent.class);
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ConferenciaEncalheCotaUsuarioRepository conferenciaEncalheCotaUsuarioRepository; 

	/**
	 * Se o usuário (session id) ja estiver conferindo alguma cota,
	 * não sera permitido que este inicie uma nova conferência.
	 * 
	 * @session
	 */
	@Transactional
	public void validarUsuarioConferindoCota(HttpSession session, Integer numeroCota) {
		
		String user = usuarioService.getUsuarioLogado().getLogin();
		
		ConferenciaEncalheCotaUsuario cuc = conferenciaEncalheCotaUsuarioRepository.obterPorNumeroCota(numeroCota);
			
		if(cuc == null || (cuc.getLogin().equals(user) && cuc.getSessionId().equals(session.getId()) && cuc.getNumeroCota().equals(numeroCota))) {
			return;
		}
		
		if(cuc.getLogin().equals(user) && !cuc.getSessionId().equals(session.getId())) {

			throw new ValidacaoException(TipoMensagem.WARNING, String.format("Não é possível executar Conferência de Encalhe simultânea para o usuário: %s!", cuc.getLogin()));
		} else {
			
			throw new ValidacaoException(TipoMensagem.WARNING
					, String.format("Não é possível executar mais de uma conferência de encalhe ao mesmo tempo. Usuário %s conferindo!", (cuc != null ? cuc.getLogin() : "Não identificado")));
		}
	}
	
	@Transactional
	private boolean existeTravaConferenciaCotaUsuario(final Integer numeroCota, HttpSession session) {
		
		final String user = usuarioService.getUsuarioLogado().getLogin();
		
		validarUsuarioConferindoCota(session, numeroCota);
		
		final ConferenciaEncalheCotaUsuario donoDoLockCotaConferida = conferenciaEncalheCotaUsuarioRepository.obterPorNumeroCota(numeroCota);
		
		if(donoDoLockCotaConferida == null) {
			
			return false;
		}
		
		if(donoDoLockCotaConferida != null && user.equals(donoDoLockCotaConferida.getLogin())) {
			
			return true;
		}
		
        String nomeUsuario = "Não identificado";
		
		throw new ValidacaoException(TipoMensagem.WARNING, 
                " Não é possível iniciar a conferência de encalhe para esta cota, "
                    + " a mesma esta sendo conferida pelo(a) usuário(a) [ " + (donoDoLockCotaConferida != null ? donoDoLockCotaConferida.getNomeUsuario() : nomeUsuario) +" ] ");
		
	}
	
	@Transactional
	public void removerTravaConferenciaCotaUsuario(HttpSession session) {
		
		conferenciaEncalheCotaUsuarioRepository.removerPorSessionId(session.getId());
	}
	
	public String getIdentificacaoUnicaUsuarioLogado(HttpSession session) {
		
		final String sessionID = session.getId();
		
		final String userLogin = (String) session.getAttribute(Constants.USER_LOGIN_PARA_TRAVA_CONFERENCIA);
		
		if (userLogin != null && !userLogin.isEmpty()) {
			
			return userLogin + sessionID;
		}
		
        return "Não Identificado";
	}

	@Transactional
	public void atribuirTravaConferenciaCotaUsuario(Integer numeroCota, HttpSession session) {
		
		definirIdentificaoDoUsuario(session);
		
		final String nomeUsuario = usuarioService.getUsuarioLogado().getNome();
		final String login = usuarioService.getUsuarioLogado().getLogin();
		
		if(!existeTravaConferenciaCotaUsuario(numeroCota, session)) {

			conferenciaEncalheCotaUsuarioRepository.removerPorLogin(login);
			ConferenciaEncalheCotaUsuario cuc = new ConferenciaEncalheCotaUsuario(login, nomeUsuario, session.getId(), numeroCota);
			try {
				
				conferenciaEncalheCotaUsuarioRepository.adicionar(cuc);
			} catch(Throwable e) {
				
				LOGGER.error("A Cota %s está sendo conferida.", e);
				throw new ValidacaoException(TipoMensagem.WARNING, String.format("A Cota %s está sendo conferida.", numeroCota));
			}
		}
	}

	@Transactional
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
			
			throw new ValidacaoException(TipoMensagem.WARNING , "Não foi possível identificar o usuário logado");
		}
		
		session.setAttribute(Constants.USER_LOGIN_PARA_TRAVA_CONFERENCIA, usuario.getLogin());
		session.setAttribute(Constants.USER_NAME_PARA_TRAVA_CONFERENCIA, nome);

	}

	@Transactional
	public void limparSessionsConferenciaCotaUsuario() {
		
		try {
			
			conferenciaEncalheCotaUsuarioRepository.removerTodos();
		} catch(DataIntegrityViolationException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, String.format("Impossível limpar as sessões no Banco de Dados."));
		}
	}
}