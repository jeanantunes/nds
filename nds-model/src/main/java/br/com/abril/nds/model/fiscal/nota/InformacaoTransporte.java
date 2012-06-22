package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class InformacaoTransporte implements Serializable {


	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1459121120584386961L;
	
	/**
	 * modFrete
	 */
	@Column(name="MODALIDADE_FRENTE", length=1, nullable=false)
	private Integer modalidadeFrente;
	
	

}
