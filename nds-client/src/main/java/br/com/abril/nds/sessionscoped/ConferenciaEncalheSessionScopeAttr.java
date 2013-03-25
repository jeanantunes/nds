package br.com.abril.nds.sessionscoped;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class ConferenciaEncalheSessionScopeAttr {

	private Long idBoxLogado;

	public Long getIdBoxLogado() {
		return idBoxLogado;
	}

	public void setIdBoxLogado(Long idBoxLogado) {
		this.idBoxLogado = idBoxLogado;
	}
}
