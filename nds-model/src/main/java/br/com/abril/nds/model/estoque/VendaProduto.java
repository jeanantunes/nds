package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;

@Entity
@Table(name = "VENDA_PRODUTO")
@SequenceGenerator(name="VENDA_PRODUTO_SEQ", initialValue = 1, allocationSize = 1)
public class VendaProduto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "VENDA_PRODUTO_SEQ")
	private Long id;
	
	@Column(name="DATA_VENDA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVenda;
	
	@Column(name="DATA_VENCIMENTO_DEBITO")
	@Temporal(TemporalType.DATE)
	private Date dataVencimentoDebito;
	
	@ManyToOne
	@JoinColumn(name = "ID_COTA")
	private Cota cota;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_VENDA_ENCALHE", nullable = false)
	private TipoVendaEncalhe tipoVenda;
	
	@ManyToOne
	@JoinColumn(name = "ID_PRODUTO_EDICAO")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name="QNT_PRODUTO")
	private BigDecimal qntProduto;
	
	@Column(name="VALOR_TOTAL_VENDA")
	private BigDecimal valorTotalVenda;
	
	@Column(name="USUARIO")
	private String usuario;
	
	@JoinColumn(name="ID_MOVIMENTO_ESTOQUE")
	@ManyToOne
	private MovimentoEstoque movimentoEstoque;
	
	@JoinColumn(name="ID_MOVIMENTO_FINANCEIRO")
	@ManyToOne
	private MovimentoFinanceiroCota movimentoFinanceiro;
	
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
	 * @return the tipoVenda
	 */
	public TipoVendaEncalhe getTipoVenda() {
		return tipoVenda;
	}

	/**
	 * @param tipoVenda the tipoVenda to set
	 */
	public void setTipoVenda(TipoVendaEncalhe tipoVenda) {
		this.tipoVenda = tipoVenda;
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

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the dataVencimentoDebito
	 */
	public Date getDataVencimentoDebito() {
		return dataVencimentoDebito;
	}

	/**
	 * @param dataVencimentoDebito the dataVencimentoDebito to set
	 */
	public void setDataVencimentoDebito(Date dataVencimentoDebito) {
		this.dataVencimentoDebito = dataVencimentoDebito;
	}

	/**
	 * @return the movimentoEstoque
	 */
	public MovimentoEstoque getMovimentoEstoque() {
		return movimentoEstoque;
	}

	/**
	 * @param movimentoEstoque the movimentoEstoque to set
	 */
	public void setMovimentoEstoque(MovimentoEstoque movimentoEstoque) {
		this.movimentoEstoque = movimentoEstoque;
	}

	/**
	 * @return the movimentoFinanceiro
	 */
	public MovimentoFinanceiroCota getMovimentoFinanceiro() {
		return movimentoFinanceiro;
	}

	/**
	 * @param movimentoFinanceiro the movimentoFinanceiro to set
	 */
	public void setMovimentoFinanceiro(MovimentoFinanceiroCota movimentoFinanceiro) {
		this.movimentoFinanceiro = movimentoFinanceiro;
	}
	
	
	
}
