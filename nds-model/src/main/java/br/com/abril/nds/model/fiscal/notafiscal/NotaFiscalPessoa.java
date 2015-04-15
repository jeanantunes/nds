package br.com.abril.nds.model.fiscal.notafiscal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name="NOTA_FISCAL_PESSOA")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TIPO_PESSOA", discriminatorType=DiscriminatorType.STRING)
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
	
	@Column(name="ID_PESSOA_ORIGINAL")
	private Long idPessoaOriginal;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Long getIdPessoaOriginal() {
		return idPessoaOriginal;
	}

	public void setIdPessoaOriginal(Long idPessoaOriginal) {
		this.idPessoaOriginal = idPessoaOriginal;
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
