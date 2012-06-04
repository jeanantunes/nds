package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ImpostoServico extends Imposto implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -135366586738774611L;

}
