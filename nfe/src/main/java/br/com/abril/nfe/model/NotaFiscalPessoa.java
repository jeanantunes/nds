package br.com.abril.nfe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NOTA_FISCAL_PESSOA")
public class NotaFiscalPessoa implements Serializable, Pessoa {
	
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

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getNome() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Telefone getTelefone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Endereco getEndereco() {
		// TODO Auto-generated method stub
		return null;
	}
}
