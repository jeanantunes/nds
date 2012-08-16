package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.Lancamento;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "PRODUTO_EDICAO", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"NUMERO_EDICAO", "PRODUTO_ID" })})
@SequenceGenerator(name="PROD_ED_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "PRODUTO")
public class ProdutoEdicao implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "PROD_ED_SEQ")
	@Column(name = "ID")
	protected Long id;
	
	//@Column(name = "CODIGO", nullable = false, unique = true)
	@Column(name = "CODIGO", nullable = true, length=8)
	protected String codigo;

	@Column(name  = "CODIGO_DE_BARRAS", nullable = true, length=18)
	protected String codigoDeBarras;
	
	@Column(name  = "CODIGO_DE_BARRAS_CORPORATIVO", nullable = true, length = 25)
	protected String codigoDeBarraCorporativo;

	@Column(name  = "NUMERO_EDICAO", nullable = false)
	protected Long numeroEdicao;
	
	@Column(name = "NOME_COMERCIAL", nullable = true, unique = false, length = 60)
	private String nomeComercial;
	
	@Column(name = "PRECO_VENDA", nullable = true)
	protected BigDecimal precoVenda;
	
	@Column(name = "PRECO_PREVISTO", nullable = true)
	protected BigDecimal precoPrevisto;
	
	@Column(name = "REPARTE_DISTRIBUIDO", nullable = true)
	protected BigInteger reparteDistribuido;
	
	@Column(name = "DESCONTO")
	protected BigDecimal desconto = BigDecimal.ZERO;
	
	@Column(name = "PACOTE_PADRAO", nullable = false)
	protected int pacotePadrao;
	
	@Column(name = "PEB", nullable = false)
	protected int peb;
	
	@Column(name = "PRECO_CUSTO")
	protected BigDecimal precoCusto;
	
	@Column(name = "PESO", nullable = false)
	protected Long peso;
	
	@Column(name = "BOLETIM_INFORMATIVO", nullable = true, length=2048)
	protected String boletimInformativo;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_ID")
	protected Produto produto;
	
	@OneToMany(mappedBy = "produtoEdicao")
	protected Set<Lancamento> lancamentos = new HashSet<Lancamento>();
	
	@OneToMany(mappedBy = "produtoEdicao", fetch=FetchType.LAZY)
	private Set<MovimentoEstoque> movimentoEstoques = new HashSet<MovimentoEstoque>();

	@OneToMany(mappedBy = "produtoEdicao", fetch=FetchType.LAZY)
	private Set<ChamadaEncalhe> chamadaEncalhes = new HashSet<ChamadaEncalhe>(); 

	@Column(name = "POSSUI_BRINDE", nullable = true)
	protected boolean possuiBrinde;
	
	@Embedded
	protected Brinde brinde;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="DESCONTO_LOGISTICA_ID", nullable=true)
	private DescontoLogistica descontoLogistica;
	
	/**
	 * Percentual de expectativa de venda do produto
	 */
	@Column(name = "EXPECTATIVA_VENDA")
	protected BigDecimal expectativaVenda;
	
	/**
	 * Flag indicando se o produto permite vale desconto
	 */
	@Column(name = "PERMITE_VALE_DESCONTO")
	protected boolean permiteValeDesconto;
	
	/**
	 * Flag indicando se o produto permite recolhimentos parciais
	 */
	@Column(name = "PARCIAL")
	private boolean parcial;

	@Column(name = "CHAMADA_CAPA", nullable = true, length = 255)
	private String chamadaCapa;
	
	@Column(name = "ATIVO", nullable = false)
	private boolean ativo = true;
	
	@Column(name = "DATA_DESATIVACAO", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date dataDesativacao;
	
	/**
	 * Flag que indica se o ProdutoEdicao foi criado atraves de interface de sistemas ou por cadastro
	 */
	@Column(name = "ORIGEM_INTERFACE", nullable = true)
	private Boolean origemInterface;
	
	@Column(name = "NUMERO_LANCAMENTO", nullable = true)
	private Integer numeroLancamento;
	
	/**
	 * Dimensões do produto (largura, etc)
	 */
	@Embedded
	private Dimensao dimensao;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}
	
	/**
	 * @return the precoPrevisto
	 */
	public BigDecimal getPrecoPrevisto() {
		return precoPrevisto;
	}

	/**
	 * @param precoPrevisto the precoPrevisto to set
	 */
	public void setPrecoPrevisto(BigDecimal precoPrevisto) {
		this.precoPrevisto = precoPrevisto;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}
	
	public BigDecimal getDesconto() {
		return desconto;
	}
	
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	
	public int getPacotePadrao() {
		return pacotePadrao;
	}
	
	public void setPacotePadrao(int pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
	
	public int getPeb() {
		return peb;
	}
	
	public void setPeb(int peb) {
		this.peb = peb;
	}
	
	public BigDecimal getPrecoCusto() {
		return precoCusto;
	}
	
	public void setPrecoCusto(BigDecimal precoCusto) {
		this.precoCusto = precoCusto;
	}
	
	public Long getPeso() {
		return peso;
	}
	
	public void setPeso(Long peso) {
		this.peso = peso;
	}
	
	public Produto getProduto() {
		return produto;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	

	public Set<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(Set<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}
	
	public boolean isPossuiBrinde() {
		return possuiBrinde;
	}
	
	public void setPossuiBrinde(boolean possuiBrinde) {
		this.possuiBrinde = possuiBrinde;
	}
	
	public BigDecimal getExpectativaVenda() {
		return expectativaVenda;
	}
	
	public void setExpectativaVenda(BigDecimal expectativaVenda) {
		this.expectativaVenda = expectativaVenda;
	}
	
	public boolean isPermiteValeDesconto() {
		return permiteValeDesconto;
	}
	
	public void setPermiteValeDesconto(boolean permiteValeDesconto) {
		this.permiteValeDesconto = permiteValeDesconto;
	}
	
	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
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
		ProdutoEdicao other = (ProdutoEdicao) obj;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return produto.toString() + "-" + numeroEdicao.toString();
	}

	/**
	 * @return the parcial
	 */
	public boolean isParcial() {
		return parcial;
	}

	/**
	 * @param parcial the parcial to set
	 */
	public void setParcial(boolean parcial) {
		this.parcial = parcial;
	}

	/**
	 * @return the brinde
	 */
	public Brinde getBrinde() {
		return brinde;
	}

	/**
	 * @param brinde the brinde to set
	 */
	public void setBrinde(Brinde brinde) {
		this.brinde = brinde;
	}

	/**
	 * @return the chamadaCapa
	 */
	public String getChamadaCapa() {
		return chamadaCapa;
	}

	/**
	 * @param chamadaCapa the chamadaCapa to set
	 */
	public void setChamadaCapa(String chamadaCapa) {
		this.chamadaCapa = chamadaCapa;
	}

	/**
	 * @return the dimensao
	 */
	public Dimensao getDimensao() {
		return dimensao;
	}

	/**
	 * @param dimensao the dimensao to set
	 */
	public void setDimensao(Dimensao dimensao) {
		this.dimensao = dimensao;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return ativo;
	}

	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the dataDesativacao
	 */
	public Date getDataDesativacao() {
		return dataDesativacao;
	}

	/**
	 * @param dataDesativacao the dataDesativacao to set
	 */
	public void setDataDesativacao(Date dataDesativacao) {
		this.dataDesativacao = dataDesativacao;
	}

	public Set<MovimentoEstoque> getMovimentoEstoques() {
		return movimentoEstoques;
	}

	public void setMovimentoEstoques(Set<MovimentoEstoque> movimentoEstoques) {
		this.movimentoEstoques = movimentoEstoques;
	}

	public Set<ChamadaEncalhe> getChamadaEncalhes() {
		return chamadaEncalhes;
	}

	public void setChamadaEncalhes(Set<ChamadaEncalhe> chamadaEncalhes) {
		this.chamadaEncalhes = chamadaEncalhes;
	}

	/**
	 * @return the nomeComercial
	 */
	public String getNomeComercial() {
		return nomeComercial;
	}

	/**
	 * @param nomeComercial the nomeComercial to set
	 */
	public void setNomeComercial(String nomeComercial) {
		this.nomeComercial = nomeComercial;
	}

	public Boolean getOrigemInterface() {
		return origemInterface;
	}

	public void setOrigemInterface(Boolean origemInterface) {
		this.origemInterface = origemInterface;
	}

	public DescontoLogistica getDescontoLogistica() {
		return descontoLogistica;
	}

	public void setDescontoLogistica(DescontoLogistica descontoLogistica) {
		this.descontoLogistica = descontoLogistica;
	}

	/**
	 * @return the reparteDistribuido
	 */
	public BigInteger getReparteDistribuido() {
		return reparteDistribuido;
	}

	/**
	 * @param reparteDistribuido the reparteDistribuido to set
	 */
	public void setReparteDistribuido(BigInteger reparteDistribuido) {
		this.reparteDistribuido = reparteDistribuido;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoDeBarraCorporativo
	 */
	public String getCodigoDeBarraCorporativo() {
		return codigoDeBarraCorporativo;
	}

	/**
	 * @param codigoDeBarraCorporativo the codigoDeBarraCorporativo to set
	 */
	public void setCodigoDeBarraCorporativo(String codigoDeBarraCorporativo) {
		this.codigoDeBarraCorporativo = codigoDeBarraCorporativo;
	}
	
	/**
	 * @return the boletimInformativo
	 */
	public String getBoletimInformativo() {
		return boletimInformativo;
	}

	/**
	 * @param boletimInformativo the boletimInformativo to set
	 */
	public void setBoletimInformativo(String boletimInformativo) {
		this.boletimInformativo = boletimInformativo;
	}

	/**
	 * @return the numeroLancamento
	 */
	public Integer getNumeroLancamento() {
		return numeroLancamento;
	}

	/**
	 * @param numeroLancamento the numeroLancamento to set
	 */
	public void setNumeroLancamento(Integer numeroLancamento) {
		this.numeroLancamento = numeroLancamento;
	}
	
}