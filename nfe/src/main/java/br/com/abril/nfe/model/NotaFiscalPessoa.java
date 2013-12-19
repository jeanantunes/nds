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
	
	@Column(name = "HORA_SAIDA")
	private	String horaSaida;

	@Column(name = "DATA_EMISSAO", nullable = false)
	private Date dataEmissao;
	
	@Column(name = "DATA_EXPEDICAO", nullable = false)
	private Date dataEntradaSaida;
	
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
}
