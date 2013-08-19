package br.com.abril.nds.model.cadastro.garantia;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import br.com.abril.nds.model.cadastro.GarantiaCotaOutros;

@Entity
public class CotaGarantiaOutros extends CotaGarantia {

	private static final long serialVersionUID = -4792924313375422777L;
	
	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name="GARANTIA_ID",nullable=false)
	private List<GarantiaCotaOutros> outros;

	/**
	 * @return the outros
	 */
	public List<GarantiaCotaOutros> getOutros() {
		return outros;
	}

	/**
	 * @param outros the outros to set
	 */
	public void setOutros(List<GarantiaCotaOutros> outros) {
		this.outros = outros;
	}
}
