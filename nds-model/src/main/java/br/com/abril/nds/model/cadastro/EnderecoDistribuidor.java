package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ENDERECO_DISTRIBUIDOR")
@SequenceGenerator(name="ENDERECO_DISTRIBUIDOR_SEQ", initialValue = 1, allocationSize = 1)
public class EnderecoDistribuidor extends AssociacaoEndereco {
	
	@Id
	@GeneratedValue(generator = "ENDERECO_DISTRIBUIDOR_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@OneToOne(optional = false)
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
