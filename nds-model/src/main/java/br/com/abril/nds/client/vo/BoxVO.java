package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Box;

public class BoxVO implements Serializable {

	private static final long serialVersionUID = 7731147792700524821L;

	private Box box;
	
	private boolean emUso = false;

	public BoxVO(Box box, boolean emUso) {
		this.box = box;
		this.emUso = emUso;
	}
	
	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public boolean isEmUso() {
		return emUso;
	}

	public void setEmUso(boolean emUso) {
		this.emUso = emUso;
	}
	
}
