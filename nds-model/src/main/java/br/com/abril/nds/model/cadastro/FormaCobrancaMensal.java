package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(value = "M")
public class FormaCobrancaMensal extends FormaCobranca implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 526487374913437598L;
	
	@Column(name = "DIA_DO_MES")
	private Integer diaDoMes;

	public Integer getDiaDoMes() {
		return diaDoMes;
	}

	public void setDiaDoMes(Integer diaDoMes) {
		this.diaDoMes = diaDoMes;
	}

}
