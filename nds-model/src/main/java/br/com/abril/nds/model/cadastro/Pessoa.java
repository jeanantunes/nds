package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "PESSOA")
@SequenceGenerator(name="PESSOA_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Pessoa implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9110541603032302785L;

	@Id
	@GeneratedValue(generator = "PESSOA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "EMAIL")
	private String email;
	
	@OneToMany(mappedBy = "pessoa", cascade=CascadeType.MERGE)
	public List<Endereco> enderecos = new ArrayList<Endereco>();
	
	@OneToMany(mappedBy = "pessoa", cascade=CascadeType.MERGE)
	public List<Telefone> telefones = new ArrayList<Telefone>();
	
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

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	/**
	 * 
	 * @return Nome da Pessoa
	 */
	public abstract String getNome();
	
	/**
	 * 
	 * @return Documento que identifica a pessoa(CPF, CNPJ, etc..)
	 */
	public abstract String getDocumento();
}