package br.com.abril.nds.model.integracao.icd;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PRACA")
public class Praca {

	@Id
	@Column(name="COD_PRACA")
	private Long codPraca;

	public Long getCodPraca() {
		return codPraca;
	}

	public void setCodPraca(Long codPraca) {
		this.codPraca = codPraca;
	}
	
}
