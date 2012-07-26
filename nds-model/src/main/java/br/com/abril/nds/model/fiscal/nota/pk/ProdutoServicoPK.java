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
public class ProdutoServicoPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5850135615091890662L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "NOTA_FISCAL_ID")
	private NotaFiscal notaFiscal;
	
	/**
	 * nItem
	 */
	@Column(name = "SEQUENCIA", nullable = false)
	@NFEExport(secao = TipoSecao.H, posicao = 0)
	private Integer sequencia;

	public ProdutoServicoPK() {
	}
	
	
	public ProdutoServicoPK(NotaFiscal notaFiscal, Integer sequencia) {
		super();
		this.notaFiscal = notaFiscal;
		this.sequencia = sequencia;
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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((notaFiscal == null) ? 0 : notaFiscal.hashCode());
		result = prime * result
				+ ((sequencia == null) ? 0 : sequencia.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoServicoPK other = (ProdutoServicoPK) obj;
		if (notaFiscal == null) {
			if (other.notaFiscal != null)
				return false;
		} else if (!notaFiscal.equals(other.notaFiscal))
			return false;
		if (sequencia == null) {
			if (other.sequencia != null)
				return false;
		} else if (!sequencia.equals(other.sequencia))
			return false;
		return true;
	}

	/**
	 * @return the sequencia
	 */
	public Integer getSequencia() {
		return sequencia;
	}

	/**
	 * @param sequencia the sequencia to set
	 */
	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}
	
}
