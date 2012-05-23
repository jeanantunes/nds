package br.com.abril.nds.model.cadastro;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
	
	@Column(name = "CODIGO", nullable = false)
	private Long codigo;
	
	@ManyToMany
	@JoinTable(name = "EDITOR_FORNECEDOR", joinColumns = {@JoinColumn(name = "EDITOR_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "FORNECEDOR_ID")})
	private Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
	
	@OneToMany(mappedBy = "editor")
	private Set<EnderecoEditor> enderecos = new HashSet<EnderecoEditor>();

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
	
	public Long getCodigo() {
		return codigo;
	}
	
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public Set<Fornecedor> getFornecedores() {
		return fornecedores;
	}
	
	public void setFornecedores(Set<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		Editor other = (Editor) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	/**
	 * @return the enderecos
	 */
	public Set<EnderecoEditor> getEnderecos() {
		return enderecos;
	}

	/**
	 * @param enderecos the enderecos to set
	 */
	public void setEnderecos(Set<EnderecoEditor> enderecos) {
		this.enderecos = enderecos;
	}

}
