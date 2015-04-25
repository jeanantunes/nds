package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.abril.nds.model.fiscal.nota.pk.NotaFiscalReferenciadaPK;
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
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NotaFiscalReferenciada implements Serializable {

	private static final long serialVersionUID = 2312305291449744935L;
	
	@EmbeddedId
	@NFEExportType
	@XmlTransient
	private NotaFiscalReferenciadaPK pk;
	
	/**
	 * refCTe
	 */
	@Column(name="CHAVE_ACESSO_CTE", length=44, nullable=false)
	@XmlElement(name="refNFe")
	private String chaveAcessoCTe;

	@Embedded
	@XmlElement(name="refNF")
	private NotaFiscalReferenciadaNFE notaFiscalReferenciadaNFE;
	
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

	public NotaFiscalReferenciadaNFE getNotaFiscalReferenciadaNFE() {
		return notaFiscalReferenciadaNFE;
	}

	public void setNotaFiscalReferenciadaNFE(
			NotaFiscalReferenciadaNFE notaFiscalReferenciadaNFE) {
		this.notaFiscalReferenciadaNFE = notaFiscalReferenciadaNFE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getChaveAcessoCTe() == null) ? 0 : this.getChaveAcessoCTe().hashCode());
		result = prime * result + ((this.getPk() == null) ? 0 : this.getPk().hashCode());
		return result;
	}

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
		return true;
	}

	@Override
	public String toString() {
		return "NotaFiscalReferenciada ["
				+ (pk != null ? "pk=" + pk + ", " : "")
				+ (chaveAcessoCTe != null ? "chaveAcessoCTe=" + chaveAcessoCTe: "") + "]";
	}
}