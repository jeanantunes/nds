package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EDITOR")
@SequenceGenerator(name="EDITOR_SEQ", initialValue = 1, allocationSize = 1)
public class Editor {
	
	@Id
	@GeneratedValue(generator = "EDITOR_SEQ")
	@Column(name = "ID", nullable = false)
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
