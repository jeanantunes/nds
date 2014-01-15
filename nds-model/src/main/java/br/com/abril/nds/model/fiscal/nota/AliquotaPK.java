package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class AliquotaPK implements Serializable {
	
	private static final long serialVersionUID = -757289195616914832L;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ALIQUOTA_ID", insertable=false, updatable=false)
	private Aliquota aliquota;

	public Aliquota getAliquota() {
		return aliquota;
	}

	public void setAliquota(Aliquota aliquota) {
		this.aliquota = aliquota;
	}
}

