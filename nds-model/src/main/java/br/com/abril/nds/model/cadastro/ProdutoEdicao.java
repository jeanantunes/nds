
package br.com.abril.nds.model.cadastro;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoReparte;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;

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
	
	@Column(name  = "CODIGO_DE_BARRAS", nullable = true, length=18)
	protected String codigoDeBarras;
	
	@Column(name  = "CODIGO_DE_BARRAS_CORPORATIVO", nullable = true, length = 25)
	protected String codigoDeBarraCorporativo;

	@Column(name  = "NUMERO_EDICAO", nullable = false)
	protected Long numeroEdicao;
	
	@Column(name = "NOME_COMERCIAL", nullable = true, unique = false, length = 60)
	private String nomeComercial;
	
	@Column(name = "PRECO_VENDA", nullable = true, precision=18, scale=4)
	protected BigDecimal precoVenda;
	
	@Column(name = "PRECO_PREVISTO", nullable = true, precision=18, scale=4)
	protected BigDecimal precoPrevisto;
	
	@Column(name = "REPARTE_DISTRIBUIDO", nullable = true)
	protected BigInteger reparteDistribuido;
	
	@Column(name = "PACOTE_PADRAO", nullable = false)
	protected Integer pacotePadrao;
	
	@Column(name = "PEB", nullable = false)
	protected Integer peb;
	
	@Column(name = "CARACTERISTICA_PRODUTO")
	protected String caracteristicaProduto;
	
	@Column(name = "PRECO_CUSTO", precision=18, scale=4)
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
	private Set<MovimentoEstoqueCota> movimentoEstoqueCotas = new HashSet<MovimentoEstoqueCota>();

	@OneToMany(mappedBy = "produtoEdicao", fetch=FetchType.LAZY)
	private Set<ChamadaEncalhe> chamadaEncalhes = new HashSet<ChamadaEncalhe>(); 

	@Column(name = "POSSUI_BRINDE", nullable = true)
	protected Boolean possuiBrinde;
	
	@ManyToOne(cascade = { CascadeType.MERGE , CascadeType.PERSIST } )
	@JoinColumn(name = "BRINDE_ID")
	private Brinde brinde;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="DESCONTO_LOGISTICA_ID", nullable=true)
	private DescontoLogistica descontoLogistica;
	
	/**
	 * Percentual de expectativa de venda do produto
	 */
	@Column(name = "EXPECTATIVA_VENDA", precision=18, scale=4)
	protected BigDecimal expectativaVenda;
	
	/**
	 * Flag indicando se o produto permite vale desconto
	 */
	@Column(name = "PERMITE_VALE_DESCONTO")
	protected Boolean permiteValeDesconto;
	
	/**
	 * Flag indicando se o produto permite recolhimentos parciais
	 */
	@Column(name = "PARCIAL")
	private Boolean parcial;

	@Column(name = "CHAMADA_CAPA", nullable = true, length = 255)
	private String chamadaCapa;
	
	@Column(name = "ATIVO", nullable = false)
	private Boolean ativo = true;
	
	@Column(name = "DATA_DESATIVACAO", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date dataDesativacao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ORIGEM", nullable = false)
	private Origem origem;
	
	@Column(name = "NUMERO_LANCAMENTO", nullable = true)
	private Integer numeroLancamento;
	
	/**
	 * Dimensões do produto (largura, etc)
	 */
	@Embedded
	private Dimensao dimensao;
	
	@OneToMany(mappedBy = "produtoEdicao")
	private List<Diferenca> diferencas;
	
	@OneToMany(mappedBy = "produtoEdicao")
	protected Set<FechamentoDiarioLancamentoReparte> historicoMovimentoRepartes;
	
	/**
	 * Desconto aplicado no cadastro de Tipo Desconto Cota
	 */
	@OneToOne(optional = true)
	@JoinColumn(name = "DESCONTO_ID")
	private Desconto descontoProdutoEdicao;

	@Enumerated(EnumType.STRING)
	@Column(name = "GRUPO_PRODUTO")
	private GrupoProduto grupoProduto;
	
	@Embedded
	private SegmentacaoProduto segmentacao;

	@Column(name="DESCONTO", precision=18, scale=4)
	private BigDecimal desconto;
	
	@Column(name="DESCRICAO_DESCONTO")
	private String descricaoDesconto;
	
	@OneToOne(mappedBy = "produtoEdicao", optional=true,fetch=FetchType.LAZY)
	private LancamentoParcial lancamentoParcial;
	
    @OneToOne(mappedBy = "produtoEdicao")
    private EstoqueProduto estoqueProduto;

	public ProdutoEdicao() {
	}
	
	public ProdutoEdicao(Long id) {
		this.id = id;
	}

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
	
	public Integer getPacotePadrao() {
		return pacotePadrao;
	}
	
	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
	
	public Integer getPeb() {
		return peb;
	}
	
	public void setPeb(Integer peb) {
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

	public String getCaracteristicaProduto() {
		return caracteristicaProduto;
	}

	public void setCaracteristicaProduto(String caracteristicaProduto) {
		this.caracteristicaProduto = caracteristicaProduto;
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
	
	public Boolean isPossuiBrinde() {
		return possuiBrinde;
	}
	
	public void setPossuiBrinde(Boolean possuiBrinde) {
		this.possuiBrinde = possuiBrinde;
	}
	
	public BigDecimal getExpectativaVenda() {
		return expectativaVenda;
	}
	
	public void setExpectativaVenda(BigDecimal expectativaVenda) {
		this.expectativaVenda = expectativaVenda;
	}
	
	public Boolean isPermiteValeDesconto() {
		return permiteValeDesconto;
	}
	
	public void setPermiteValeDesconto(Boolean permiteValeDesconto) {
		this.permiteValeDesconto = permiteValeDesconto;
	}
	
	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		
		if (codigoDeBarras != null && !"".equals(codigoDeBarras) && new BigInteger(codigoDeBarras).compareTo(BigInteger.ZERO) > 0){
			
			//evita que sejam gravados zeros na frente do código de barras, vide trac 677
			if (codigoDeBarras.startsWith("0")){
				
				int indexUltimoZero = 0;
				
				while (codigoDeBarras.charAt(indexUltimoZero) == '0'){
					indexUltimoZero++;
				}
				
				codigoDeBarras = codigoDeBarras.substring(
						indexUltimoZero, 
						codigoDeBarras.length());
			}
		}
		
		this.codigoDeBarras = codigoDeBarras;
	}
	
	/**
	 * @return the parcial
	 */
	public Boolean isParcial() {
		return parcial;
	}

	/**
	 * @param parcial the parcial to set
	 */
	public void setParcial(Boolean parcial) {
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
	public Boolean isAtivo() {
		return ativo;
	}

	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(Boolean ativo) {
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

	/**
	 * @return the origem
	 */
	public Origem getOrigem() {
		return origem;
	}

	/**
	 * @param origem the origem to set
	 */
	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public List<Diferenca> getDiferencas() {
		return diferencas;
	}

	public void setDiferencas(List<Diferenca> diferencas) {
		this.diferencas = diferencas;
	}

	public Set<FechamentoDiarioLancamentoReparte> getHistoricoMovimentoRepartes() {
		return historicoMovimentoRepartes;
	}

	public void setHistoricoMovimentoRepartes(
			Set<FechamentoDiarioLancamentoReparte> historicoMovimentoRepartes) {
		this.historicoMovimentoRepartes = historicoMovimentoRepartes;
	}

	public Desconto getDescontoProdutoEdicao() {
		return descontoProdutoEdicao;
	}

	public void setDescontoProdutoEdicao(Desconto descontoProdutoEdicao) {
		this.descontoProdutoEdicao = descontoProdutoEdicao;
	}
	
	public SegmentacaoProduto getSegmentacao() {
		return segmentacao;
	}

	public void setSegmentacao(SegmentacaoProduto segmentacao) {
		this.segmentacao = segmentacao;

	}

	public GrupoProduto getGrupoProduto() {
		return grupoProduto;
	}

	public void setGrupoProduto(GrupoProduto grupoProduto) {
		this.grupoProduto = grupoProduto;
	}

	/**
	 * @return the desconto
	 */
	public BigDecimal getDesconto() {
		return desconto;
	}

	/**
	 * @param desconto the desconto to set
	 */
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	/**
	 * @return the descricaoDesconto
	 */
	public String getDescricaoDesconto() {
		return descricaoDesconto;
	}

	/**
	 * @param descricaoDesconto the descricaoDesconto to set
	 */
	public void setDescricaoDesconto(String descricaoDesconto) {
		this.descricaoDesconto = descricaoDesconto;
	}

	
	public LancamentoParcial getLancamentoParcial() {
		return lancamentoParcial;
	}

	public void setLancamentoParcial(LancamentoParcial lancamentoParcial) {
		this.lancamentoParcial = lancamentoParcial;
	}

	/**
	 * @return the estoqueProduto
	 */
	public EstoqueProduto getEstoqueProduto() {
		return estoqueProduto;
	}

	/**
	 * @param estoqueProduto the estoqueProduto to set
	 */
	public void setEstoqueProduto(EstoqueProduto estoqueProduto) {
		this.estoqueProduto = estoqueProduto;
	}

	public Boolean getPossuiBrinde() {
		return possuiBrinde;
	}

	public Boolean getPermiteValeDesconto() {
		return permiteValeDesconto;
	}

	public Boolean getParcial() {
		return parcial;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	
	public Set<MovimentoEstoqueCota> getMovimentoEstoqueCotas() {
		return movimentoEstoqueCotas;
	}

	public void setMovimentoEstoqueCotas(
			Set<MovimentoEstoqueCota> movimentoEstoquesCotas) {
		this.movimentoEstoqueCotas = movimentoEstoquesCotas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ativo == null) ? 0 : ativo.hashCode());
		result = prime
				* result
				+ ((boletimInformativo == null) ? 0 : boletimInformativo
						.hashCode());
		result = prime * result + ((brinde == null) ? 0 : brinde.hashCode());
		result = prime
				* result
				+ ((caracteristicaProduto == null) ? 0 : caracteristicaProduto
						.hashCode());
		result = prime * result
				+ ((chamadaCapa == null) ? 0 : chamadaCapa.hashCode());
		result = prime * result
				+ ((chamadaEncalhes == null) ? 0 : chamadaEncalhes.hashCode());
		result = prime
				* result
				+ ((codigoDeBarraCorporativo == null) ? 0
						: codigoDeBarraCorporativo.hashCode());
		result = prime * result
				+ ((codigoDeBarras == null) ? 0 : codigoDeBarras.hashCode());
		result = prime * result
				+ ((dataDesativacao == null) ? 0 : dataDesativacao.hashCode());
		result = prime * result
				+ ((desconto == null) ? 0 : desconto.hashCode());
		result = prime
				* result
				+ ((descontoLogistica == null) ? 0 : descontoLogistica
						.hashCode());
		result = prime
				* result
				+ ((descontoProdutoEdicao == null) ? 0 : descontoProdutoEdicao
						.hashCode());
		result = prime
				* result
				+ ((descricaoDesconto == null) ? 0 : descricaoDesconto
						.hashCode());
		result = prime * result
				+ ((diferencas == null) ? 0 : diferencas.hashCode());
		result = prime * result
				+ ((dimensao == null) ? 0 : dimensao.hashCode());
		result = prime * result
				+ ((estoqueProduto == null) ? 0 : estoqueProduto.hashCode());
		result = prime
				* result
				+ ((expectativaVenda == null) ? 0 : expectativaVenda.hashCode());
		result = prime * result
				+ ((grupoProduto == null) ? 0 : grupoProduto.hashCode());
		result = prime
				* result
				+ ((historicoMovimentoRepartes == null) ? 0
						: historicoMovimentoRepartes.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((lancamentoParcial == null) ? 0 : lancamentoParcial
						.hashCode());
		result = prime * result
				+ ((lancamentos == null) ? 0 : lancamentos.hashCode());
		result = prime
				* result
				+ ((movimentoEstoques == null) ? 0 : movimentoEstoques
						.hashCode());
		result = prime
				* result
				+ ((movimentoEstoqueCotas == null) ? 0 : movimentoEstoqueCotas
						.hashCode());
		result = prime * result
				+ ((nomeComercial == null) ? 0 : nomeComercial.hashCode());
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
		result = prime
				* result
				+ ((numeroLancamento == null) ? 0 : numeroLancamento.hashCode());
		result = prime * result + ((origem == null) ? 0 : origem.hashCode());
		result = prime * result
				+ ((pacotePadrao == null) ? 0 : pacotePadrao.hashCode());
		result = prime * result + ((parcial == null) ? 0 : parcial.hashCode());
		result = prime * result + ((peb == null) ? 0 : peb.hashCode());
		result = prime
				* result
				+ ((permiteValeDesconto == null) ? 0 : permiteValeDesconto
						.hashCode());
		result = prime * result + ((peso == null) ? 0 : peso.hashCode());
		result = prime * result
				+ ((possuiBrinde == null) ? 0 : possuiBrinde.hashCode());
		result = prime * result
				+ ((precoCusto == null) ? 0 : precoCusto.hashCode());
		result = prime * result
				+ ((precoPrevisto == null) ? 0 : precoPrevisto.hashCode());
		result = prime * result
				+ ((precoVenda == null) ? 0 : precoVenda.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime
				* result
				+ ((reparteDistribuido == null) ? 0 : reparteDistribuido
						.hashCode());
		result = prime * result
				+ ((segmentacao == null) ? 0 : segmentacao.hashCode());
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
		if (ativo == null) {
			if (other.ativo != null)
				return false;
		} else if (!ativo.equals(other.ativo))
			return false;
		if (boletimInformativo == null) {
			if (other.boletimInformativo != null)
				return false;
		} else if (!boletimInformativo.equals(other.boletimInformativo))
			return false;
		if (brinde == null) {
			if (other.brinde != null)
				return false;
		} else if (!brinde.equals(other.brinde))
			return false;
		if (caracteristicaProduto == null) {
			if (other.caracteristicaProduto != null)
				return false;
		} else if (!caracteristicaProduto.equals(other.caracteristicaProduto))
			return false;
		if (chamadaCapa == null) {
			if (other.chamadaCapa != null)
				return false;
		} else if (!chamadaCapa.equals(other.chamadaCapa))
			return false;
		if (chamadaEncalhes == null) {
			if (other.chamadaEncalhes != null)
				return false;
		} else if (!chamadaEncalhes.equals(other.chamadaEncalhes))
			return false;
		if (codigoDeBarraCorporativo == null) {
			if (other.codigoDeBarraCorporativo != null)
				return false;
		} else if (!codigoDeBarraCorporativo
				.equals(other.codigoDeBarraCorporativo))
			return false;
		if (codigoDeBarras == null) {
			if (other.codigoDeBarras != null)
				return false;
		} else if (!codigoDeBarras.equals(other.codigoDeBarras))
			return false;
		if (dataDesativacao == null) {
			if (other.dataDesativacao != null)
				return false;
		} else if (!dataDesativacao.equals(other.dataDesativacao))
			return false;
		if (desconto == null) {
			if (other.desconto != null)
				return false;
		} else if (!desconto.equals(other.desconto))
			return false;
		if (descontoLogistica == null) {
			if (other.descontoLogistica != null)
				return false;
		} else if (!descontoLogistica.equals(other.descontoLogistica))
			return false;
		if (descontoProdutoEdicao == null) {
			if (other.descontoProdutoEdicao != null)
				return false;
		} else if (!descontoProdutoEdicao.equals(other.descontoProdutoEdicao))
			return false;
		if (descricaoDesconto == null) {
			if (other.descricaoDesconto != null)
				return false;
		} else if (!descricaoDesconto.equals(other.descricaoDesconto))
			return false;
		if (diferencas == null) {
			if (other.diferencas != null)
				return false;
		} else if (!diferencas.equals(other.diferencas))
			return false;
		if (dimensao == null) {
			if (other.dimensao != null)
				return false;
		} else if (!dimensao.equals(other.dimensao))
			return false;
		if (estoqueProduto == null) {
			if (other.estoqueProduto != null)
				return false;
		} else if (!estoqueProduto.equals(other.estoqueProduto))
			return false;
		if (expectativaVenda == null) {
			if (other.expectativaVenda != null)
				return false;
		} else if (!expectativaVenda.equals(other.expectativaVenda))
			return false;
		if (grupoProduto != other.grupoProduto)
			return false;
		if (historicoMovimentoRepartes == null) {
			if (other.historicoMovimentoRepartes != null)
				return false;
		} else if (!historicoMovimentoRepartes
				.equals(other.historicoMovimentoRepartes))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lancamentoParcial == null) {
			if (other.lancamentoParcial != null)
				return false;
		} else if (!lancamentoParcial.equals(other.lancamentoParcial))
			return false;
		if (lancamentos == null) {
			if (other.lancamentos != null)
				return false;
		} else if (!lancamentos.equals(other.lancamentos))
			return false;
		if (movimentoEstoques == null) {
			if (other.movimentoEstoques != null)
				return false;
		} else if (!movimentoEstoques.equals(other.movimentoEstoques))
			return false;
		if (movimentoEstoqueCotas == null) {
			if (other.movimentoEstoqueCotas != null)
				return false;
		} else if (!movimentoEstoqueCotas.equals(other.movimentoEstoqueCotas))
			return false;
		if (nomeComercial == null) {
			if (other.nomeComercial != null)
				return false;
		} else if (!nomeComercial.equals(other.nomeComercial))
			return false;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		if (numeroLancamento == null) {
			if (other.numeroLancamento != null)
				return false;
		} else if (!numeroLancamento.equals(other.numeroLancamento))
			return false;
		if (origem != other.origem)
			return false;
		if (pacotePadrao == null) {
			if (other.pacotePadrao != null)
				return false;
		} else if (!pacotePadrao.equals(other.pacotePadrao))
			return false;
		if (parcial == null) {
			if (other.parcial != null)
				return false;
		} else if (!parcial.equals(other.parcial))
			return false;
		if (peb == null) {
			if (other.peb != null)
				return false;
		} else if (!peb.equals(other.peb))
			return false;
		if (permiteValeDesconto == null) {
			if (other.permiteValeDesconto != null)
				return false;
		} else if (!permiteValeDesconto.equals(other.permiteValeDesconto))
			return false;
		if (peso == null) {
			if (other.peso != null)
				return false;
		} else if (!peso.equals(other.peso))
			return false;
		if (possuiBrinde == null) {
			if (other.possuiBrinde != null)
				return false;
		} else if (!possuiBrinde.equals(other.possuiBrinde))
			return false;
		if (precoCusto == null) {
			if (other.precoCusto != null)
				return false;
		} else if (!precoCusto.equals(other.precoCusto))
			return false;
		if (precoPrevisto == null) {
			if (other.precoPrevisto != null)
				return false;
		} else if (!precoPrevisto.equals(other.precoPrevisto))
			return false;
		if (precoVenda == null) {
			if (other.precoVenda != null)
				return false;
		} else if (!precoVenda.equals(other.precoVenda))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		if (reparteDistribuido == null) {
			if (other.reparteDistribuido != null)
				return false;
		} else if (!reparteDistribuido.equals(other.reparteDistribuido))
			return false;
		if (segmentacao == null) {
			if (other.segmentacao != null)
				return false;
		} else if (!segmentacao.equals(other.segmentacao))
			return false;
		return true;
	}

	
}