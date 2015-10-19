package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Depara;

public class DeparaVO implements Serializable {

	private static final long serialVersionUID = 7731147792700524821L;

	private Depara depara;
	
	private boolean emUso = false;

	public DeparaVO(Depara depara, boolean emUso) {
		this.depara = depara;
		this.emUso = emUso;
	}
	
	public Depara getDepara() {
		return depara;
	}

	public void setDepara(Depara depara) {
		this.depara = depara;
	}

	public boolean isEmUso() {
		return emUso;
	}

	public void setEmUso(boolean emUso) {
		this.emUso = emUso;
	}
	
}
