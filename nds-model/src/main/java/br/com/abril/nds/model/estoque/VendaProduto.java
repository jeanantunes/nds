package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "VENDA_PRODUTO")
@SequenceGenerator(name="VENDA_PRODUTO_SEQ", initialValue = 1, allocationSize = 1)
public class VendaProduto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "VENDA_PRODUTO_SEQ")
	private Long id;
	
	@Column(name="DATA_VENDA")
	@Temporal(TemporalType.DATE)
	private Date dataVenda;
	
	@Column(name="HORARIO_VENDA")
	@Temporal(TemporalType.TIME)
	private Date horarioVenda;
	
	@Column(name="DATA_VENCIMENTO_DEBITO")
	@Temporal(TemporalType.DATE)
	private Date dataVencimentoDebito;
	
	@ManyToOne
	@JoinColumn(name = "ID_COTA")
	private Cota cota;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_VENDA_ENCALHE", nullable = false)
	private TipoVendaEncalhe tipoVenda;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_COMERCIALIZACAO_VENDA")
	private FormaComercializacao tipoComercializacaoVenda;
	
	@ManyToOne
	@JoinColumn(name = "ID_PRODUTO_EDICAO")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name="QNT_PRODUTO")
	private BigInteger qntProduto;
	
	@Column(name="VALOR_TOTAL_VENDA")
	private BigDecimal valorTotalVenda;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@JoinTable(name="VENDA_PRODUTO_MOVIMENTO_ESTOQUE", 
			joinColumns={@JoinColumn(name="ID_VENDA_PRODUTO")}, 
			inverseJoinColumns={@JoinColumn(name="ID_MOVIMENTO_ESTOQUE")})
	@OneToMany(orphanRemoval=true)
	private Set<MovimentoEstoque> movimentoEstoque;
	
	@JoinTable(name="VENDA_PRODUTO_MOVIMENTO_FINANCEIRO", 
			joinColumns={@JoinColumn(name="ID_VENDA_PRODUTO")}, 
			inverseJoinColumns={@JoinColumn(name="ID_MOVIMENTO_FINANCEIRO")})
	@OneToMany(orphanRemoval=true)
	private Set<MovimentoFinanceiroCota> movimentoFinanceiro;
	
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
	public BigInteger getQntProduto() {
		return qntProduto;
	}

	/**
	 * @param qntProduto the qntProduto to set
	 */
	public void setQntProduto(BigInteger qntProduto) {
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
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
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
	 * @return the movimentoFinanceiro
	 */
	public Set<MovimentoFinanceiroCota> getMovimentoFinanceiro() {
		return movimentoFinanceiro;
	}

	/**
	 * @param movimentoFinanceiro the movimentoFinanceiro to set
	 */
	public void setMovimentoFinanceiro(
			Set<MovimentoFinanceiroCota> movimentoFinanceiro) {
		this.movimentoFinanceiro = movimentoFinanceiro;
	}

	/**
	 * @return the horarioVenda
	 */
	public Date getHorarioVenda() {
		return horarioVenda;
	}

	/**
	 * @param horarioVenda the horarioVenda to set
	 */
	public void setHorarioVenda(Date horarioVenda) {
		this.horarioVenda = horarioVenda;
	}

	/**
	 * @return the movimentoEstoque
	 */
	public Set<MovimentoEstoque> getMovimentoEstoque() {
		return movimentoEstoque;
	}

	/**
	 * @param movimentoEstoque the movimentoEstoque to set
	 */
	public void setMovimentoEstoque(Set<MovimentoEstoque> movimentoEstoque) {
		this.movimentoEstoque = movimentoEstoque;
	}

	/**
	 * @return the tipoComercializacaoVenda
	 */
	public FormaComercializacao getTipoComercializacaoVenda() {
		return tipoComercializacaoVenda;
	}

	/**
	 * @param tipoComercializacaoVenda the tipoComercializacaoVenda to set
	 */
	public void setTipoComercializacaoVenda(
			FormaComercializacao tipoComercializacaoVenda) {
		this.tipoComercializacaoVenda = tipoComercializacaoVenda;
	}
}
