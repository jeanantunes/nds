package br.com.abril.nds.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "DIRETORIO")
@SequenceGenerator(name = "DIRETORIO_SEQ", initialValue = 1, allocationSize = 1)
public class DiretorioTransferenciaArquivo implements Serializable {

	private static final long serialVersionUID = 6957825190606620081L;

	@Id
	@GeneratedValue(generator = "DIRETORIO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NOME_DIRETORIO", nullable = false)
	private String nomeDiretorio;
	
	@Column(name = "ENDERECO_DIRETORIO", nullable = false)
	private String enderecoDiretorio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeDiretorio() {
		return nomeDiretorio;
	}

	public void setNomeDiretorio(String nomeDiretorio) {
		this.nomeDiretorio = nomeDiretorio;
	}

	public String getEnderecoDiretorio() {
		return enderecoDiretorio;
	}

	public void setEnderecoDiretorio(String enderecoDiretorio) {
		this.enderecoDiretorio = enderecoDiretorio;
	}
	
}
