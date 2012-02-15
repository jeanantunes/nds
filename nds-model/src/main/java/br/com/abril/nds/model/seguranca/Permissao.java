package br.com.abril.nds.model.seguranca;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity	
@Table(name = "PERMISSAO")
public class Permissao {

	@Id
	private Long id;
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
