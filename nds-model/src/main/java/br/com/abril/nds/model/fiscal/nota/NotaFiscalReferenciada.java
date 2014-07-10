package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.abril.nds.model.fiscal.nota.pk.NotaFiscalReferenciadaPK;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;

/**
 * NFref Grupo de informação das NF/NFe referenciadas.<br/> Grupo com as informações
 * das NF/NFe /NF de produtor/ Cupom Fiscal referenciadas. Esta informação será
 * utilizada nas hipóteses previstas na legislação. (Ex.: Devolução de
 * Mercadorias, Substituição de NF cancelada, Complementação de NF, etc.).
 * (v.2.0)
 * 
 * 
 * @author Diego Fernandes
 * 
 */
@Entity
@Table(name = "NOTA_FISCAL_REFERENCIADA")
public class NotaFiscalReferenciada implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2312305291449744935L;
	
	@EmbeddedId
	@NFEExportType
	@XmlTransient
	private NotaFiscalReferenciadaPK pk;
	
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
	@XmlElement(name="cAAMMUF")
	private Date dataEmissao;
	
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
	 * refCTe
	 */
	@Column(name="CHAVE_ACESSO_CTE", length=44, nullable=false)
	@XmlElement(name="refCTe")
	private String chaveAcessoCTe;
	
	public NotaFiscalReferenciada() {		
		pk =  new NotaFiscalReferenciadaPK();
	}
	
	
	public NotaFiscalReferenciada(NotaFiscalReferenciadaPK pk) {
		super();
		this.pk = pk;
	}
	
	
	public NotaFiscalReferenciada(NotaFiscal notaFiscal, String chaveAcesso){
		pk =  new NotaFiscalReferenciadaPK(notaFiscal,chaveAcesso);
	
	}

	/**
	 * @return the pk
	 */
	public NotaFiscalReferenciadaPK getPk() {
		return pk;
	}

	/**
	 * @param pk the pk to set
	 */
	public void setPk(NotaFiscalReferenciadaPK pk) {
		this.pk = pk;
	}

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
		this.dataEmissao = dataEmissao;
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


	/**
	 * @return the chaveAcessoCTe
	 */
	public String getChaveAcessoCTe() {
		return chaveAcessoCTe;
	}


	/**
	 * @param chaveAcessoCTe the chaveAcessoCTe to set
	 */
	public void setChaveAcessoCTe(String chaveAcessoCTe) {
		this.chaveAcessoCTe = chaveAcessoCTe;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getChaveAcessoCTe() == null) ? 0 : this.getChaveAcessoCTe().hashCode());
		result = prime * result + ((this.getCnpj() == null) ? 0 : this.getCnpj().hashCode());
		result = prime * result + ((this.getCodigoUF() == null) ? 0 : this.getCodigoUF().hashCode());
		result = prime * result + ((this.getDataEmissao() == null) ? 0 : this.getDataEmissao().hashCode());
		result = prime * result + ((this.getModelo() == null) ? 0 : this.getModelo().hashCode());
		result = prime * result + ((this.getNumeroDocumentoFiscal() == null) ? 0 : this.getNumeroDocumentoFiscal().hashCode());
		result = prime * result + ((this.getPk() == null) ? 0 : this.getPk().hashCode());
		result = prime * result + ((this.getSerie() == null) ? 0 : this.getSerie().hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		NotaFiscalReferenciada other = (NotaFiscalReferenciada) obj;
		if (this.getChaveAcessoCTe() == null) {
			if (other.getChaveAcessoCTe() != null) {
				return false;
			}
		} else if (!this.getChaveAcessoCTe().equals(other.getChaveAcessoCTe())) {
			return false;
		}
		if (this.getCnpj() == null) {
			if (other.getCnpj() != null) {
				return false;
			}
		} else if (!this.getCnpj().equals(other.getCnpj())) {
			return false;
		}
		if (this.getCodigoUF() == null) {
			if (other.getCodigoUF() != null) {
				return false;
			}
		} else if (!this.getCodigoUF().equals(other.getCodigoUF())) {
			return false;
		}
		if (this.getDataEmissao() == null) {
			if (other.getDataEmissao() != null) {
				return false;
			}
		} else if (!this.getDataEmissao().equals(other.getDataEmissao())) {
			return false;
		}
		if (this.getModelo() == null) {
			if (other.getModelo() != null) {
				return false;
			}
		} else if (!this.getModelo().equals(other.getModelo())) {
			return false;
		}
		if (this.getNumeroDocumentoFiscal() == null) {
			if (other.getNumeroDocumentoFiscal() != null) {
				return false;
			}
		} else if (!this.getNumeroDocumentoFiscal().equals(other.getNumeroDocumentoFiscal())) {
			return false;
		}
		if (this.getPk() == null) {
			if (other.getPk() != null) {
				return false;
			}
		} else if (!this.getPk().equals(other.getPk())) {
			return false;
		}
		if (this.getSerie() == null) {
			if (other.getSerie() != null) {
				return false;
			}
		} else if (!this.getSerie().equals(other.getSerie())) {
			return false;
		}
		return true;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NotaFiscalReferenciada ["
				+ (pk != null ? "pk=" + pk + ", " : "")
				+ (codigoUF != null ? "codigoUF=" + codigoUF + ", " : "")
				+ (dataEmissao != null ? "dataEmissao=" + dataEmissao + ", ": "")
				+ (cnpj != null ? "cnpj=" + cnpj + ", " : "")
				+ (modelo != null ? "modelo=" + modelo + ", " : "")
				+ (serie != null ? "serie=" + serie + ", " : "")
				+ (numeroDocumentoFiscal != null ? "numeroDocumentoFiscal="
				+ numeroDocumentoFiscal + ", " : "")
				+ (chaveAcessoCTe != null ? "chaveAcessoCTe=" + chaveAcessoCTe: "") + "]";
	}

}
