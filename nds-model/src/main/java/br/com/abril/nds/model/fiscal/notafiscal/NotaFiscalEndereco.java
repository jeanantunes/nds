package br.com.abril.nds.model.fiscal.notafiscal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEConditions;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhen;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhens;

@Entity
@Table(name="NOTA_FISCAL_ENDERECO")
@XmlAccessorType(XmlAccessType.FIELD)
public class NotaFiscalEndereco implements Serializable {
	
	private static final long serialVersionUID = 7384512437561238172L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	@XmlTransient
	private Long id;
	
	@Column(name = "TIPO_LOGRADOURO", length=60)	
	private String tipoLogradouro;
	
	@Column(name = "LOGRADOURO", length=60)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao = TipoSecao.C05, posicao = 0, tamanho = 60)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao = TipoSecao.E05, posicao = 0, tamanho = 60))
	})
	@XmlElement(name="xLgr")
	private String logradouro;
	
	@Column(name = "NUMERO", nullable = true, length=60)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao=TipoSecao.C05, posicao=1, tamanho=60)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao=TipoSecao.E05, posicao=1 , tamanho=60))
	})
	@XmlElement(name="nro")
	private String numero;
	
	@Column(name = "COMPLEMENTO", length=60)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao=TipoSecao.C05, posicao=2, tamanho=60)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao=TipoSecao.E05, posicao=2 , tamanho=60))
	})
	@XmlElement(name="xCpl")
	private String complemento;
	
	@Column(name = "BAIRRO", length=60)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao=TipoSecao.C05, posicao=3, tamanho=60)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao=TipoSecao.E05, posicao=3 , tamanho=60))
	})
	@XmlElement(name="xBairro")
	private String bairro;

	@Column(name = "CODIGO_CIDADE_IBGE", nullable = true, length=7)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao = TipoSecao.B, posicao = 11)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao = TipoSecao.C05, posicao = 4)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao = TipoSecao.E05, posicao = 4))
	})
	@XmlElement(name="cMun")
	private Integer codigoCidadeIBGE;
	
	@Column(name = "CIDADE", length=60)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao=TipoSecao.C05, posicao=5, tamanho=60)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao=TipoSecao.E05, posicao=5 , tamanho=60))
	})
	@XmlElement(name="xMun")
	private String cidade;
	
	@Column(name = "UF", length=2)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao = TipoSecao.C05, posicao = 6, tamanho = 2)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao = TipoSecao.E05, posicao = 6, tamanho = 2))
	})
	@XmlElement(name="UF")
	private String uf;
	

	@Column(name = "CEP", length=9)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_EMITENTE, export = @NFEExport(secao=TipoSecao.C05, posicao=7, tamanho=8)),
			@NFEWhen(condition = NFEConditions.IDENTIFICACAO_DESTINATARIO, export = @NFEExport(secao=TipoSecao.E05, posicao=7 , tamanho=8))
	})
	@XmlElement(name="CEP")
	private String cep;
	
	@XmlTransient
	@Column(name="CODIGO_UF")
	private Long codigoUf;
	
	@Column(name = "CODIGO_PAIS", length=20)
	@XmlElement(name="cPais")
	private Long codigoPais;
	
	@Column(name = "PAIS", length=60)
	@XmlElement(name="xPais")
	private String pais;

	@Transient
	@XmlElement(name="fone")
	private String foneXML;
	
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

	public Long getCodigoUf() {
		return codigoUf;
	}

	public void setCodigoUf(Long codigoUf) {
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

	public Long getCodigoPais() {
		return codigoPais;
	}

	public void setCodigoPais(Long codigoPais) {
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
		NotaFiscalEndereco other = (NotaFiscalEndereco) obj;
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
