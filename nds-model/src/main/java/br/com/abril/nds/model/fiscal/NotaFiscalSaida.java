package br.com.abril.nds.model.fiscal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
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

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "NF_SAIDA_SEQ")
	@Column(name = "ID")
	protected Long id;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	

}
