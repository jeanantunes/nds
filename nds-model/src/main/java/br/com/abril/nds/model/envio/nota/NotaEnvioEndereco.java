package br.com.abril.nds.model.envio.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NOTA_ENVIO_ENDERECO")
public class NotaEnvioEndereco implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2918742126843644248L;

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
	
	@Column(name = "COMPLEMENTO", length=60)
	private String complemento;
	
	@Column(name = "BAIRRO", length=60)
	private String bairro;

	@Column(name = "CODIGO_CIDADE_IBGE", nullable = true, length=7)
	private Integer codigoCidadeIBGE;
	
	@Column(name = "CIDADE", length=60)
	private String cidade;
	
	@Column(name = "UF", length=2)
	private String uf;
	
	@Column(name = "CEP", length=9)
	private String cep;
	
	@Column(name="CODIGO_UF")
	private Integer codigoUf;
	
	@Column(name = "CODIGO_PAIS", length=20)
	private Integer codigoPais;
	
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

	public String getTipoLogradouro() {
		return tipoLogradouro;
	}

	public void setTipoLogradouro(String tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	public Integer getCodigoUf() {
		return codigoUf;
	}

	public void setCodigoUf(Integer codigoUf) {
		this.codigoUf = codigoUf;
	}

	public Integer getCodigoCidadeIBGE() {
		return codigoCidadeIBGE;
	}

	public void setCodigoCidadeIBGE(Integer codigoCidadeIBGE) {
		this.codigoCidadeIBGE = codigoCidadeIBGE;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Integer getCodigoPais() {
		return codigoPais;
	}

	public void setCodigoPais(Integer codigoPais) {
		this.codigoPais = codigoPais;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getCep() == null) ? 0 : this.getCep().hashCode());
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
		NotaEnvioEndereco other = (NotaEnvioEndereco) obj;
		if (this.getCep() == null) {
			if (other.getCep() != null)
				return false;
		} else if (!this.getCep().equals(other.getCep()))
			return false;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}
}