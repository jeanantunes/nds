package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import br.com.abril.nds.model.fiscal.nota.pk.ProdutoServicoPK;
import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;

@Entity
@Table(name = "NOTA_FISCAL_PRODUTO_SERVICO")
@XmlType(name="det")
@XmlAccessorType(XmlAccessType.FIELD)
public class DetalheNotaFiscal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1903373644705052365L;
	
	@EmbeddedId
	@NFEExportType
	private ProdutoServicoPK produtoServicoPK;
	
	@Transient
	@XmlAttribute(name="nItem")
	private Long sequencia;
	
	@JoinColumn(name="NOTA_FISCAL_ID")
	private NotaFiscal notaFiscal;
	
	@Embedded
	@XmlElement(name="prod")
	private ProdutoServico produtoServico;
	
	/**
	 * Encargos financeiros
	 */
	@OneToOne(optional = false, mappedBy = "detalheNotaFiscal")
	@PrimaryKeyJoinColumn
	@NFEExportType
	@XmlElement(name="imposto")
	private EncargoFinanceiro encargoFinanceiro;
	
	public DetalheNotaFiscal() {
		super();
	}
	
	public DetalheNotaFiscal(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}

	/**
	 * Getters e Setters 
	 */
	

	/**
	 * @return the produtoServicoPK
	 */
	public ProdutoServicoPK getProdutoServicoPK() {
		return produtoServicoPK;
	}

	/**
	 * @param produtoServicoPK the produtoServicoPK to set
	 */
	public void setProdutoServicoPK(ProdutoServicoPK produtoServicoPK) {
		this.produtoServicoPK = produtoServicoPK;
	}
	
	public Long getSequencia() {
		return sequencia;
	}

	public void setSequencia(Long sequencia) {
		this.sequencia = sequencia;
	}
	
	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}
	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}
	

	/**
	 * @return the encargoFinanceiro
	 */
	public EncargoFinanceiro getEncargoFinanceiro() {
		return encargoFinanceiro;
	}

	/**
	 * @param encargoFinanceiro the encargoFinanceiro to set
	 */
	public void setEncargoFinanceiro(EncargoFinanceiro encargoFinanceiro) {
		this.encargoFinanceiro = encargoFinanceiro;
	}
	
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

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
		result = prime
				* result
				+ ((produtoServicoPK == null) ? 0 : produtoServicoPK.hashCode());
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
		DetalheNotaFiscal other = (DetalheNotaFiscal) obj;
		if (produtoServicoPK == null) {
			if (other.produtoServicoPK != null)
				return false;
		} else if (!produtoServicoPK.equals(other.produtoServicoPK))
			return false;
		return true;
	}
}