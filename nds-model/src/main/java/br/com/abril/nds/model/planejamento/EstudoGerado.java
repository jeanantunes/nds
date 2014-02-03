package br.com.abril.nds.model.planejamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESTUDO_GERADO")
public class EstudoGerado extends AbstractEstudo {
	
	@Column(name = "LIBERADO")
	private Boolean liberado;

	public Boolean isLiberado() {
		return liberado;
	}

	public void setLiberado(Boolean liberado) {
		this.liberado = liberado;
	}
	
}
