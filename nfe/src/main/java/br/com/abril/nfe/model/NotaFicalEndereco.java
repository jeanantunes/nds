package br.com.abril.nfe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NOTA_FISCAL_ENDERECO")
public class NotaFicalEndereco implements Serializable {
	
	private static final long serialVersionUID = 7384512437561238172L;

	@Id
	@GeneratedValue(generator="NOTA_FISCAL_ENDERECO_ID")
	private Long id;
	
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
	
	@Column(name = "CODIGO_UF", length=2)
	private String codigoUf;

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

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCodigoUf() {
		return codigoUf;
	}

	public void setCodigoUf(String codigoUf) {
		this.codigoUf = codigoUf;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Endereco [id=" + id + ", logradouro=" + logradouro
				+ ", numero=" + numero + ", uf=" + uf + ", cidade=" + cidade
				+ ", complemento=" + complemento + ", bairro=" + bairro
				+ ", cep=" + cep + ", codigoUf=" + codigoUf + "]";
	}
	
}
