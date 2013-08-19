package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ENDERECO_TRANSPORTADOR")
@SequenceGenerator(name="ENDERECO_TRANSP_SEQ", initialValue = 1, allocationSize = 1)
public class EnderecoTransportador extends AssociacaoEndereco implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7280304169341563906L;

	@Id
	@GeneratedValue(generator = "ENDERECO_TRANSP_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TRANSPORTADOR_ID")
	private Transportador transportador;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Transportador getTransportador() {
		return transportador;
	}

	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}
}