package br.com.abril.nds.model.distribuicao;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "RANKING_SEGMENTO")
@SequenceGenerator(name = "RANKING_SEGMENTO_SEQ", initialValue = 1, allocationSize = 1)
public class RankingSegmento {

	@Id
	@GeneratedValue(generator = "RANKING_SEGMENTO_SEQ")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TIPO_SEGMENTO_PRODUTO_ID")
	private TipoSegmentoProduto tipoSegmentoProduto;
	
	@Column(name = "SEGMENTO_DESCRICAO")
	private String descricaoSegmentoProduto;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name = "QTDE")
	private BigDecimal quantidade;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "RANKING_SEGMENTOS_GERADOS_ID")
	private RankingSegmentosGerados rankingSegmentosGerados;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public TipoSegmentoProduto getTipoSegmentoProduto() {
		return tipoSegmentoProduto;
	}

	public void setTipoSegmentoProduto(TipoSegmentoProduto tipoSegmentoProduto) {
		this.tipoSegmentoProduto = tipoSegmentoProduto;
	}

	public String getDescricaoSegmentoProduto() {
		return descricaoSegmentoProduto;
	}

	public void setDescricaoSegmentoProduto(String descricaoSegmentoProduto) {
		this.descricaoSegmentoProduto = descricaoSegmentoProduto;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public RankingSegmentosGerados getRankingSegmentosGerados() {
		return rankingSegmentosGerados;
	}

	public void setRankingSegmentosGerados(
			RankingSegmentosGerados rankingSegmentosGerados) {
		this.rankingSegmentosGerados = rankingSegmentosGerados;
	}
}
