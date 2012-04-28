package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
}