package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.abril.nds.model.fiscal.nota.pk.MovimentoEstoqueNotaPK;

public class MovimentoEstoqueNota implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5224081258865735017L;

	
	@EmbeddedId
	private MovimentoEstoqueNotaPK pk;

	/**
	 * @return the pk
	 */
	public MovimentoEstoqueNotaPK getPk() {
		return pk;
	}

	/**
	 * @param pk the pk to set
	 */
	public void setPk(MovimentoEstoqueNotaPK pk) {
		this.pk = pk;
	}
	
}
