package br.com.abril.nfe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NOTA_FISCAL_PESSOA")
public class NotaFiscalPessoa implements Serializable {
	
	private static final long serialVersionUID = -4659540498651547848L;
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name = "NUMERO_TELEFONE", nullable = false)
	private String numeroTelefone;
	
	@Column(name = "RAMAL", nullable = true)
	private String ramal;
	
	@Column(name = "DDD")
	private String ddd;
	
	@Column(name = "EMAIL")
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroTelefone() {
		return numeroTelefone;
	}

	public void setNumeroTelefone(String numeroTelefone) {
		this.numeroTelefone = numeroTelefone;
	}

	public String getRamal() {
		return ramal;
	}

	public void setRamal(String ramal) {
		this.ramal = ramal;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "NotaFiscalPessoa [id=" + id + ", numeroTelefone="
				+ numeroTelefone + ", ramal=" + ramal + ", ddd=" + ddd
				+ ", email=" + email + "]";
	}
}
