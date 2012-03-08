package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "ENDERECO")
@SequenceGenerator(name="ENDERECO_SEQ", initialValue = 1, allocationSize = 1)
public class Endereco {

	@Id
	@GeneratedValue(generator = "ENDERECO_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "BAIRRO")
	private String bairro;
	@Column(name = "CEP")
	private String cep;
	@Column(name = "CIDADE")
	private String cidade;
	@Column(name = "COMPLEMENTO")
	private String complemento;
	@Column(name = "LOGRADOURO")
	private String logradouro;
	@Column(name = "NUMERO")
	private int numero;
	@Column(name = "UF")
	private String uf;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

}