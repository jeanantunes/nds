package br.com.abril.nds.session.scoped;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.seguranca.ConferenciaEncalheUsuarioCota;
import br.com.abril.nds.repository.ConferenciaEncalheUsuarioCotaRepository;

public class NdsClientSessionListener implements HttpSessionListener {

	@Autowired
	private ConferenciaEncalheUsuarioCotaRepository conferenciaEncalheUsuarioCotaRepository;
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		synchronized ( this ) {
			HttpSession session = se.getSession();

			if ( session != null ) {
				ConferenciaEncalheUsuarioCota cuc = conferenciaEncalheUsuarioCotaRepository.obterPorSessionId(session.getId() );
				if ( cuc != null ) {

					conferenciaEncalheUsuarioCotaRepository.remover(cuc);

				}
			}
		}

	}

}