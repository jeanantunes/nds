package br.com.abril.nds.model.seguranca;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity	
@Table(name = "GRUPO_PERMISSOES")
@SequenceGenerator(name="GRUPO_PERMISSOES_SEQ", initialValue = 1, allocationSize = 1)
public class GrupoPermissoes {

	@Id
	@GeneratedValue(generator = "GRUPO_PERMISSOES_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOME", nullable = false)
	private String nome;

	@ManyToMany(fetch=FetchType.LAZY)
	private Set<Permissao> permissoes = new HashSet<Permissao>();

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
