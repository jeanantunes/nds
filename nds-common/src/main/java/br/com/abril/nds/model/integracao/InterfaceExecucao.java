package br.com.abril.nds.model.integracao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INTERFACE_EXECUCAO")
public class InterfaceExecucao implements Serializable {

	private static final long serialVersionUID = 1543068954906375787L;

	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NOME", nullable = false, length = 7, unique = true)
	private String nome;

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
}
