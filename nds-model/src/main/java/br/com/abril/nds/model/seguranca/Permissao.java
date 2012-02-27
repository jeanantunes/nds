package br.com.abril.nds.model.seguranca;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity	
@Table(name = "PERMISSAO")
@SequenceGenerator(name="PERMISSAO_SEQ", initialValue = 1, allocationSize = 1)
public class Permissao {

	@Id
	@GeneratedValue(generator = "PERMISSAO_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "NOME", nullable = false)
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
