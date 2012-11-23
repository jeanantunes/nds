package br.com.abril.nds.integracao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INTERFACE_EXECUCAO")
public class InterfaceExecucao implements Serializable {

	private static final long serialVersionUID = 885046823449470989L;

	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NOME", nullable = false, length = 7, unique = true)
	private String nome;
	
	@Column(name = "MASCARA_ARQUIVO", nullable = false, length = 50)
	private String mascaraArquivo;

	
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

	public String getMascaraArquivo() {
		return mascaraArquivo;
	}

	public void setMascaraArquivo(String mascaraArquivo) {
		this.mascaraArquivo = mascaraArquivo;
	}
}
