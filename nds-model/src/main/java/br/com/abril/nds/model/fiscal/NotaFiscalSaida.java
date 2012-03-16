package br.com.abril.nds.model.fiscal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "NOTA_FISCAL_SAIDA")
@SequenceGenerator(name="NF_SAIDA_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class NotaFiscalSaida extends NotaFiscal {
	
	@Id
	@GeneratedValue(generator = "NF_SAIDA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_NOTA_FISCAL", nullable = false)
	private StatusNotaFiscalSaida status;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public StatusNotaFiscalSaida getStatus() {
		return status;
	}
	
	public void setStatus(StatusNotaFiscalSaida status) {
		this.status = status;
	}
	
	

}
