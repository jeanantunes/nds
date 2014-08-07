package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TELEFONE_DISTRIBUIDOR")
@SequenceGenerator(name="TELEFONE_DISTRIBUIDOR_SEQ", initialValue = 1, allocationSize = 1)
public class TelefoneDistribuidor extends AssociacaoTelefone {

	@Id
	@GeneratedValue(generator = "TELEFONE_DISTRIBUIDOR_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	@JoinColumn(name = "DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Distribuidor getDistribuidor() {
		return distribuidor;
	}
	
	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}
	
}