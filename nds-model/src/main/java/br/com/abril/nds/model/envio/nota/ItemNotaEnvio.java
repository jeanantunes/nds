package br.com.abril.nds.model.envio.nota;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.planejamento.EstudoCota;

@Entity
@Table(name="NOTA_ENVIO_ITEM")
public class ItemNotaEnvio implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3215846434365577294L;
	
	@EmbeddedId
	private ItemNotaEnvioPK itemNotaEnvioPK;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	/**
	 * cProd
	 */
	@Column(name="CODIGO_PRODUTO", length=60, nullable=false)
	private String codigoProduto;
	
	@Column(name="SEQ_MATRIZ_LANCAMENTO")
	private Integer sequenciaMatrizLancamento;
	
	/**
	 * xProd
	 */
	@Column(name="PUBLICACAO", length=120, nullable=false)
	private String publicacao;
	
	@Column(name  = "NUMERO_EDICAO", nullable = false)
	protected Long numeroEdicao;
	
	@Column(name="REPARTE", precision=15, scale=2 , nullable=false)
	private BigInteger reparte;
	
	@Column(name="PRECO_CAPA", precision=18, scale=4, nullable=false)
	private BigDecimal precoCapa;
	
	@Column(name="DESCONTO", precision=18, scale=4, nullable=false)
	private BigDecimal desconto;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "ESTUDO_COTA_ID")
	private EstudoCota estudoCota;
	
	@OneToMany(mappedBy="itemNotaEnvio", cascade={CascadeType.MERGE, CascadeType.PERSIST})
	private List<MovimentoEstoqueCota> movimentosProdutoSemEstudo;
	
	@Column(name="FURO_PRODUTO_ID")
	private Long furoProduto;
	
	public Long getFuroProduto() {
		return furoProduto;
	}

	public void setFuroProduto(Long furoProduto) {
		this.furoProduto = furoProduto;
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
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}


	/**
	 * @return the publicacao
	 */
	public String getPublicacao() {
		return publicacao;
	}

	/**
	 * @param publicacao the publicacao to set
	 */
	public void setPublicacao(String publicacao) {
		this.publicacao = publicacao;
	}

	/**
	 * @return the numeroEdicao
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the reparte
	 */
	public BigInteger getReparte() {
		return reparte;
	}

	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	/**
	 * @return the precoCapa
	 */
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	/**
	 * @param precoCapa the precoCapa to set
	 */
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
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
	 * @return the itemNotaEnvioPK
	 */
	public ItemNotaEnvioPK getItemNotaEnvioPK() {
		return itemNotaEnvioPK;
	}

	/**
	 * @param itemNotaEnvioPK the itemNotaEnvioPK to set
	 */
	public void setItemNotaEnvioPK(ItemNotaEnvioPK itemNotaEnvioPK) {
		this.itemNotaEnvioPK = itemNotaEnvioPK;
	}

	public Integer getSequencia() {
		return this.itemNotaEnvioPK.getSequencia();
	}

	public EstudoCota getEstudoCota() {
		return estudoCota;
	}

	public void setEstudoCota(EstudoCota estudoCota) {
		this.estudoCota = estudoCota;
	}

	public List<MovimentoEstoqueCota> getMovimentosProdutoSemEstudo() {
		return movimentosProdutoSemEstudo;
	}

	public void setMovimentosProdutoSemEstudo(
			List<MovimentoEstoqueCota> movimentosProdutoSemEstudo) {
		this.movimentosProdutoSemEstudo = movimentosProdutoSemEstudo;
	}

	/**
	 * @return the sequenciaMatrizLancamento
	 */
	public Integer getSequenciaMatrizLancamento() {
		return sequenciaMatrizLancamento;
	}

	/**
	 * @param sequenciaMatrizLancamento the sequenciaMatrizLancamento to set
	 */
	public void setSequenciaMatrizLancamento(Integer sequenciaMatrizLancamento) {
		this.sequenciaMatrizLancamento = sequenciaMatrizLancamento;
	}
}
