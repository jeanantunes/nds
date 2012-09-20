package br.com.abril.nds.model.envio.nota;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
@Entity
@Table(name="NOTA_ENVIO_ITEM")
public class ItemNotaEnvio implements Serializable {

	/**
	 * 
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
	
	/**
	 * xProd
	 */
	@Column(name="PUBLICACAO", length=120, nullable=false)
	private String publicacao;
	
	@Column(name  = "NUMERO_EDICAO", nullable = false)
	protected Long numeroEdicao;
	
	
	@Column(name="REPARTE", precision=15, scale=2 , nullable=false)
	private BigInteger reparte;
	
	@Column(name="PRECO_CAPA",precision=21, scale=2 , nullable=false)
	private BigDecimal precoCapa;
	
	
	@Column(name="DESCONTO",precision=21, scale=2 , nullable=false)
	private BigDecimal desconto;
	
	
	@ManyToMany
	@JoinTable( joinColumns = {			
			@JoinColumn(name = "NOTA_ENVIO_ITEM_SEQUENCIA", referencedColumnName="SEQUENCIA"),
			@JoinColumn(name = "NOTA_ENVIO_ID", referencedColumnName="NOTA_ENVIO_ID")
		},
			inverseJoinColumns = {
			@JoinColumn(name = "MOVIMENTO_ESTOQUE_COTA_ID", referencedColumnName="ID")
	})
	private List<MovimentoEstoqueCota> listaMovimentoEstoqueCota;


	


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
	 * @return the listaMovimentoEstoqueCota
	 */
	public List<MovimentoEstoqueCota> getListaMovimentoEstoqueCota() {
		return listaMovimentoEstoqueCota;
	}


	/**
	 * @param listaMovimentoEstoqueCota the listaMovimentoEstoqueCota to set
	 */
	public void setListaMovimentoEstoqueCota(
			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota) {
		this.listaMovimentoEstoqueCota = listaMovimentoEstoqueCota;
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

}
