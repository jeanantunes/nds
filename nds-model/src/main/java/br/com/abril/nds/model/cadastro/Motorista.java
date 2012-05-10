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
@Table(name = "MOTORISTA")
@SequenceGenerator(name="MOTORISTA_SEQ", initialValue = 1, allocationSize = 1)
public class Motorista implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -216357895553301331L;
	
	@Id
	@GeneratedValue(generator = "MOTORISTA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NOME")
	private String nome;
	
	@Column(name = "CNH")
	private String cnh;
	
	@ManyToOne
	@JoinColumn(name = "TRANSPORTADOR_ID")
	private Transportador transportador;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCnh() {
		return cnh;
	}

	public void setCnh(String cnh) {
		this.cnh = cnh;
	}

	public Transportador getTransportador() {
		return transportador;
	}

	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Motorista other = (Motorista) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}