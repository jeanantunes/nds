package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ENDERECO_FIADOR")
@SequenceGenerator(name="ENDERECO_FIADOR_SEQ", initialValue = 1, allocationSize = 1)
public class EnderecoFiador extends AssociacaoEndereco {

	@Id
	@GeneratedValue(generator = "ENDERECO_FIADOR_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "FIADOR_ID")
	private Fiador fiador;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Fiador getFiador() {
		return fiador;
	}

	public void setFiador(Fiador fiador) {
		this.fiador = fiador;
	}
}