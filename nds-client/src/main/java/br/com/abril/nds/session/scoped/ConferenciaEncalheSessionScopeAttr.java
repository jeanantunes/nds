package br.com.abril.nds.session.scoped;

import java.io.Serializable;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class ConferenciaEncalheSessionScopeAttr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idBoxLogado;

	public Long getIdBoxLogado() {
		return idBoxLogado;
	}

	public void setIdBoxLogado(Long idBoxLogado) {
		this.idBoxLogado = idBoxLogado;
	}
}
