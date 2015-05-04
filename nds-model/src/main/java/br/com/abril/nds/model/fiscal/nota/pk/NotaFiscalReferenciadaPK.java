package br.com.abril.nds.model.fiscal.nota.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;

@Embeddable
public class NotaFiscalReferenciadaPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4373797568382146166L;
	
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "NOTA_FISCAL_ID", insertable=false, updatable=false)
	private NotaFiscal notaFiscal;
	
	/**
	 * refNFe
	 */
	@Column(name="CHAVE_ACESSO", length=44, nullable=false)
	@NFEExport(secao = TipoSecao.B13, posicao = 0, tamanho = 44)
	private String chaveAcesso;
	
	public NotaFiscalReferenciadaPK() {
	}
	
	

	public NotaFiscalReferenciadaPK(NotaFiscal notaFiscal, String chaveAcesso) {
		super();
		this.notaFiscal = notaFiscal;
		this.chaveAcesso = chaveAcesso;
	}



	/**
	 * @return the notaFiscal
	 */
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	/**
	 * @param notaFiscal the notaFiscal to set
	 */
	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	/**
	 * @return the chaveAcesso
	 */
	public String getChaveAcesso() {
		return chaveAcesso;
	}

	/**
	 * @param chaveAcesso the chaveAcesso to set
	 */
	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NotaFiscalReferenciadaPK ["
				+ (notaFiscal != null ? "notaFiscal=" + notaFiscal + ", " : "")
				+ (chaveAcesso != null ? "chaveAcesso=" + chaveAcesso : "")
				+ "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((chaveAcesso == null) ? 0 : chaveAcesso.hashCode());
		result = prime * result
				+ ((notaFiscal == null) ? 0 : notaFiscal.hashCode());
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
		NotaFiscalReferenciadaPK other = (NotaFiscalReferenciadaPK) obj;
		if (chaveAcesso == null) {
			if (other.chaveAcesso != null) {
				return false;
			}
		} else if (!chaveAcesso.equals(other.chaveAcesso)) {
			return false;
		}
		if (notaFiscal == null) {
			if (other.notaFiscal != null) {
				return false;
			}
		} else if (!notaFiscal.equals(other.notaFiscal)) {
			return false;
		}
		return true;
	}
	
	
	
	
}
