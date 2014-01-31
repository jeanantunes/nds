package br.com.abril.nds.model.fiscal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@DiscriminatorValue(value = "ESTOQUE")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrigemItemNotaFiscalEstoque extends OrigemItemNotaFiscal {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6208480018096488241L;
	
	@XmlTransient
	@Transient
	OrigemItem origem = OrigemItem.ESTOQUE;
	
	@OneToOne
	@JoinColumn(name="PRODUTO_EDICAO_ID")
	@XmlTransient
	private ProdutoEdicao produtoEdicao;	

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
	public OrigemItem getOrigem() {
		return origem;
	}

	public String obterOrigemItemNotaFiscal() {
		return this.getClass().getName();
	};
	
}