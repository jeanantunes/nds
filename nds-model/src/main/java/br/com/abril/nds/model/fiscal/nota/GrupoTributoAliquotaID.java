package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class GrupoTributoAliquotaID implements Serializable {

	private static final long serialVersionUID = 5392503333835977858L;
	
	@ManyToOne
	private Tributo tributo;
	
	@ManyToOne
    private Aliquota  aliquota;

	public Tributo getTributo() {
		return tributo;
	}

	public void setTributo(Tributo tributo) {
		this.tributo = tributo;
	}

	public Aliquota getAliquota() {
		return aliquota;
	}

	public void setAliquota(Aliquota aliquota) {
		this.aliquota = aliquota;
	}		
}
