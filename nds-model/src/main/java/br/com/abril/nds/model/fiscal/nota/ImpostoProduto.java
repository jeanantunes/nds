package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ImpostoProduto extends Imposto implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 536918061402194564L;

	

}
