package br.com.abril.nfe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.abril.nfe.model.interfaces.Endereco;

@Entity
@Table(name="NOTA_FISCAL_ENDERECO")
public class NotaFicalEndereco implements Serializable, Endereco {
	
	private static final long serialVersionUID = 7384512437561238172L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name = "TIPO_LOGRADOURO", length=60)
	private String tipoLogradouro;
	
	@Column(name = "LOGRADOURO", length=60)
	private String logradouro;
	
	@Column(name = "NUMERO", nullable = true, length=60)
	private String numero;

	@Column(name = "UF", length=2)
	private String uf;
	
	@Column(name = "CIDADE", length=60)
	private String cidade;
	
	@Column(name = "COMPLEMENTO", length=60)
	private String complemento;
	
	@Column(name = "BAIRRO", length=60)
	private String bairro;
	
	@Column(name = "CEP", length=9)
	private String cep;
	
	@Column(name = "PAIS", length=60)
	private String pais;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
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

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCEP() {
		return cep;
	}

	public void setCEP(String cep) {
		this.cep = cep;
	}

	@Override
	public String getTipoLogradouro() {
		return this.tipoLogradouro;
	}

	@Override
	public String getUF() {
		return this.uf;
	}

	@Override
	public String getPais() {
		return this.pais;
	}
	
}
