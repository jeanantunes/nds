package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
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
	
	@XmlTransient
	@EmbeddedId
	@NFEExportType
	private ProdutoServicoPK produtoServicoPK;
	
	@Transient
	@XmlAttribute(name="nItem")
	private Long sequencia;
	
	@Embedded
	@XmlElement(name="prod")
	private ProdutoServico produtoServico;
	
	@Transient
	@XmlElement(name="imposto")
	private Impostos impostos;
	
	@Transient
	@XmlElement(name="infAdProd")
	private String infAdProd;
	
	public DetalheNotaFiscal() {
		super();
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

		this.infAdProd = produtoServico.getProdutoEdicao() != null ? "Ed. " + produtoServico.getProdutoEdicao().getNumeroEdicao().toString() : "";
	}

	public String getInfAdProd() {
		return infAdProd != null ? infAdProd : produtoServico.getProdutoEdicao() != null ? "Ed. " + produtoServico.getProdutoEdicao().getNumeroEdicao().toString() : "";
	}

	public Impostos getImpostos() {
		return impostos;
	}

	public void setImpostos(Impostos impostos) {
		this.impostos = impostos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getSequencia() == null) ? 0 : this.getSequencia().hashCode());
		result = prime * result + ((this.getProdutoServico() == null) ? 0 : this.getProdutoServico().hashCode());
		result = prime * result + ((this.getProdutoServicoPK() == null) ? 0 : this.getProdutoServicoPK().hashCode());
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
		DetalheNotaFiscal other = (DetalheNotaFiscal) obj;
		if (this.getSequencia() == null) {
			if (other.getSequencia() != null)
				return false;
		} else if (!this.getSequencia().equals(other.getSequencia()))
			return false;
		if (this.getProdutoServico() == null) {
			if (other.getProdutoServico() != null)
				return false;
		} else if (!this.getProdutoServico().equals(other.getProdutoServico()))
			return false;
		if (this.getProdutoServicoPK() == null) {
			if (other.getProdutoServicoPK() != null)
				return false;
		} else if (!this.getProdutoServicoPK().equals(other.getProdutoServicoPK()))
			return false;
		return true;
	}
	
	
	
}