package br.com.abril.nds.model.integracao.icd;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ITEM_NOTA_FISCAL_ENCALHE")
public class IcdNotaFiscalEncalheItem {

	@Id
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
