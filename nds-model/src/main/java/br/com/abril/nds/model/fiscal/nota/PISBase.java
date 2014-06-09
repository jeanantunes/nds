package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class PISBase extends ContribuicaoSocial implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3126540075644429610L;

}
