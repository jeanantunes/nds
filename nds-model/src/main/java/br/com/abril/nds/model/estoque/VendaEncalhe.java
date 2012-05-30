package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "VENDA_ENCALHE")
@SequenceGenerator(name="VENDA_ENCALHE_SEQ", initialValue = 1, allocationSize = 1)
public class VendaEncalhe implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "VENDA_ENCALHE_SEQ")
	private Long id;
	
	@Column(name="DATA_VENDA")
	@Temporal(TemporalType.DATE)
	private Date dataVenda;
	
	@ManyToOne
	@JoinColumn(name = "ID_COTA")
	private Cota cota;
	
	@ManyToOne
	@JoinColumn(name = "ID_PRODUTO_EDICAO")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name="QNT_PRODUTO")
	private BigDecimal qntProduto;
	
	@Column(name="VALOR_TOTAL_VENDA")
	private BigDecimal valorTotalVenda;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the dataVenda
	 */
	public Date getDataVenda() {
		return dataVenda;
	}

	/**
	 * @param dataVenda the dataVenda to set
	 */
	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}

	/**
	 * @return the cota
	 */
	public Cota getCota() {
		return cota;
	}

	/**
	 * @param cota the cota to set
	 */
	public void setCota(Cota cota) {
		this.cota = cota;
	}

	/**
	 * @return the produtoEdicao
	 */
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	/**
	 * @param produtoEdicao the produtoEdicao to set
	 */
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	/**
	 * @return the qntProduto
	 */
	public BigDecimal getQntProduto() {
		return qntProduto;
	}

	/**
	 * @param qntProduto the qntProduto to set
	 */
	public void setQntProduto(BigDecimal qntProduto) {
		this.qntProduto = qntProduto;
	}

	/**
	 * @return the valorTotalVenda
	 */
	public BigDecimal getValorTotalVenda() {
		return valorTotalVenda;
	}

	/**
	 * @param valorTotalVenda the valorTotalVenda to set
	 */
	public void setValorTotalVenda(BigDecimal valorTotalVenda) {
		this.valorTotalVenda = valorTotalVenda;
	}
	
	
}
