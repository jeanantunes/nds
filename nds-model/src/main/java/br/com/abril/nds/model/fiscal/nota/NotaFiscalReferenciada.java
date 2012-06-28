package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	private NotaFiscalReferenciadaPK pk;
	
	/**
	 * cUF
	 */
	@Column(name="CODIGO_UF", nullable=false, length=2)
	@NFEExport(secao = TipoSecao.B14, posicao = 0)
	private Integer codigoUF;
	
	/**
	 * AAMM
	 */
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_EMISSAO", nullable=false)
	@NFEExport(secao = TipoSecao.B14, posicao = 1, mascara="yyMM")
	private Date dataEmissao;
	
	/**
	 * CNPJ
	 */
	@Column(name="CNPJ", nullable=false, length=14)
	@NFEExport(secao = TipoSecao.B14, posicao = 2, tamanho = 14)
	private String cnpj;
	
	/**
	 * mod
	 */
	@Column(name="MODELO_DOCUMENTO_FISCAL",length=2, nullable=false)
	@NFEExport(secao = TipoSecao.B14, posicao = 3)
	private Integer modelo;
	
	/**
	 * serie
	 */
	@Column(name = "SERIE", length = 3, nullable = false)
	@NFEExport(secao = TipoSecao.B14, posicao = 4)
	private Integer serie;
	
	/**
	 * nNF
	 */
	@Column(name="NUMERO_DOCUMENTO_FISCAL", length=9, nullable=false)
	@NFEExport(secao = TipoSecao.B14, posicao = 5)
	private Long numeroDocumentoFiscal;
	
	
	/**
	 * refCTe
	 */
	@Column(name="CHAVE_ACESSO_CTE", length=44, nullable=false)
	private BigInteger chaveAcessoCTe;
	
	
	
	public NotaFiscalReferenciada() {		
		pk =  new NotaFiscalReferenciadaPK();
	}
	
	
	public NotaFiscalReferenciada(NotaFiscalReferenciadaPK pk) {
		super();
		this.pk = pk;
	}
	
	
	public NotaFiscalReferenciada(NotaFiscal notaFiscal,
			BigInteger chaveAcesso){
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
	public Integer getCodigoUF() {
		return codigoUF;
	}

	/**
	 * @param codigoUF the codigoUF to set
	 */
	public void setCodigoUF(Integer codigoUF) {
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
	public Integer getModelo() {
		return modelo;
	}

	/**
	 * @param modelo the modelo to set
	 */
	public void setModelo(Integer modelo) {
		this.modelo = modelo;
	}

	/**
	 * @return the serie
	 */
	public Integer getSerie() {
		return serie;
	}

	/**
	 * @param serie the serie to set
	 */
	public void setSerie(Integer serie) {
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
	public BigInteger getChaveAcessoCTe() {
		return chaveAcessoCTe;
	}


	/**
	 * @param chaveAcessoCTe the chaveAcessoCTe to set
	 */
	public void setChaveAcessoCTe(BigInteger chaveAcessoCTe) {
		this.chaveAcessoCTe = chaveAcessoCTe;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NotaFiscalReferenciada ["
				+ (pk != null ? "pk=" + pk + ", " : "")
				+ (codigoUF != null ? "codigoUF=" + codigoUF + ", " : "")
				+ (dataEmissao != null ? "dataEmissao=" + dataEmissao + ", "
						: "")
				+ (cnpj != null ? "cnpj=" + cnpj + ", " : "")
				+ (modelo != null ? "modelo=" + modelo + ", " : "")
				+ (serie != null ? "serie=" + serie + ", " : "")
				+ (numeroDocumentoFiscal != null ? "numeroDocumentoFiscal="
						+ numeroDocumentoFiscal + ", " : "")
				+ (chaveAcessoCTe != null ? "chaveAcessoCTe=" + chaveAcessoCTe
						: "") + "]";
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((chaveAcessoCTe == null) ? 0 : chaveAcessoCTe.hashCode());
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
		result = prime * result
				+ ((codigoUF == null) ? 0 : codigoUF.hashCode());
		result = prime * result
				+ ((dataEmissao == null) ? 0 : dataEmissao.hashCode());
		result = prime * result + ((modelo == null) ? 0 : modelo.hashCode());
		result = prime
				* result
				+ ((numeroDocumentoFiscal == null) ? 0 : numeroDocumentoFiscal
						.hashCode());
		result = prime * result + ((pk == null) ? 0 : pk.hashCode());
		result = prime * result + ((serie == null) ? 0 : serie.hashCode());
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
		if (chaveAcessoCTe == null) {
			if (other.chaveAcessoCTe != null) {
				return false;
			}
		} else if (!chaveAcessoCTe.equals(other.chaveAcessoCTe)) {
			return false;
		}
		if (cnpj == null) {
			if (other.cnpj != null) {
				return false;
			}
		} else if (!cnpj.equals(other.cnpj)) {
			return false;
		}
		if (codigoUF == null) {
			if (other.codigoUF != null) {
				return false;
			}
		} else if (!codigoUF.equals(other.codigoUF)) {
			return false;
		}
		if (dataEmissao == null) {
			if (other.dataEmissao != null) {
				return false;
			}
		} else if (!dataEmissao.equals(other.dataEmissao)) {
			return false;
		}
		if (modelo == null) {
			if (other.modelo != null) {
				return false;
			}
		} else if (!modelo.equals(other.modelo)) {
			return false;
		}
		if (numeroDocumentoFiscal == null) {
			if (other.numeroDocumentoFiscal != null) {
				return false;
			}
		} else if (!numeroDocumentoFiscal.equals(other.numeroDocumentoFiscal)) {
			return false;
		}
		if (pk == null) {
			if (other.pk != null) {
				return false;
			}
		} else if (!pk.equals(other.pk)) {
			return false;
		}
		if (serie == null) {
			if (other.serie != null) {
				return false;
			}
		} else if (!serie.equals(other.serie)) {
			return false;
		}
		return true;
	}

}
