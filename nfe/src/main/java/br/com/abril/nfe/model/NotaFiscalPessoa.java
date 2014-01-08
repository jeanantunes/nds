package br.com.abril.nfe.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.abril.nfe.model.interfaces.Endereco;
import br.com.abril.nfe.model.interfaces.Telefone;

@Entity
@Table(name="NOTA_FISCAL_PESSOA")
public abstract class NotaFiscalPessoa implements Serializable {
	
	private static final long serialVersionUID = -4659540498651547848L;
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name="NOME")
	private String nome;

	@Column(name="EMAIL")
	private String email;
	
	@Embedded
	private Telefone telefone;
	
	@Embedded
	private Endereco endereco;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
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
		NotaFiscalPessoa other = (NotaFiscalPessoa) obj;
		if (this.getId() == null) {
			if (other.id != null)
				return false;
		} else if (!this.getId().equals(other.id))
			return false;
		return true;
	}
}
