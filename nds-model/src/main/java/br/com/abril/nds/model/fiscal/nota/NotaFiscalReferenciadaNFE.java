package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;

@XmlType(name="refNF")
@XmlAccessorType(XmlAccessType.FIELD)
@Embeddable
public class NotaFiscalReferenciadaNFE implements Serializable {

	private static final long serialVersionUID = 2312305291449744935L;
	
	/**
	 * cUF
	 */
	@Column(name="CODIGO_UF", nullable=false, length=2)
	@NFEExport(secao = TipoSecao.B14, posicao = 0)
	@XmlElement(name="cUF")
	private Long codigoUF;
	
	/**
	 * AAMM
	 */
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_EMISSAO", nullable=false)
	@NFEExport(secao = TipoSecao.B14, posicao = 1, mascara="yyMM")
	@XmlTransient
	private Date dataEmissao;
	
	@Transient
	@XmlElement(name="AAMM")
	private String dataEmissaoXML;
	
	/**
	 * CNPJ
	 */
	@Column(name="CNPJ", nullable=false, length=14)
	@NFEExport(secao = TipoSecao.B14, posicao = 2, tamanho = 14)
	@XmlElement(name="CNPJ")
	private String cnpj;
	
	/**
	 * mod
	 */
	@Column(name="MODELO_DOCUMENTO_FISCAL",length=2, nullable=false)
	@NFEExport(secao = TipoSecao.B14, posicao = 3)
	@XmlElement(name="mod")
	private String modelo;
	
	/**
	 * serie
	 */
	@Column(name = "SERIE", length = 3, nullable = false)
	@NFEExport(secao = TipoSecao.B14, posicao = 4)
	@XmlElement(name="serie")
	private String serie;
	
	/**
	 * nNF
	 */
	@Column(name="NUMERO_DOCUMENTO_FISCAL", length=9, nullable=false)
	@NFEExport(secao = TipoSecao.B14, posicao = 5)
	@XmlElement(name="nNF")
	private Long numeroDocumentoFiscal;
	
	/**
	 * @return the codigoUF
	 */
	public Long getCodigoUF() {
		return codigoUF;
	}

	/**
	 * @param codigoUF the codigoUF to set
	 */
	public void setCodigoUF(Long codigoUF) {
		this.codigoUF = codigoUF;
	}

	/**
	 * @return the dataEmissao
	 */
	public Date getDataEmissao() {
		return dataEmissao;
	}

	/**
	 * @param dataEmissao the dataEmissao to set
	 */
	public void setDataEmissao(Date dataEmissao) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
		this.dataEmissao = dataEmissao;
		this.dataEmissaoXML = dataEmissao != null ? sdf.format(dataEmissao) : null;
	}

	/**
	 * @return the cnpj
	 */
	public String getCnpj() {
		return cnpj;
	}

	/**
	 * @param cnpj the cnpj to set
	 */
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	/**
	 * @return the modelo
	 */
	public String getModelo() {
		return modelo;
	}

	/**
	 * @param modelo the modelo to set
	 */
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	/**
	 * @return the serie
	 */
	public String getSerie() {
		return serie;
	}

	/**
	 * @param serie the serie to set
	 */
	public void setSerie(String serie) {
		this.serie = serie;
	}

	/**
	 * @return the numeroDocumentoFiscal
	 */
	public Long getNumeroDocumentoFiscal() {
		return numeroDocumentoFiscal;
	}

	/**
	 * @param numeroDocumentoFiscal the numeroDocumentoFiscal to set
	 */
	public void setNumeroDocumentoFiscal(Long numeroDocumentoFiscal) {
		this.numeroDocumentoFiscal = numeroDocumentoFiscal;
	}
}