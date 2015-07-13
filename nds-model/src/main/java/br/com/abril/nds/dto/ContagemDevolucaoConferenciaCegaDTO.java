package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ContagemDevolucaoConferenciaCegaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8901090065499301292L;

	private Long idProdutoEdicao;

	@Export(label = "Código")
	private String codigoProduto;

	@Export(label = "Produto")
	private String nomeProduto;

	@Export(label = "Edição")
	private Long numeroEdicao;

	@Export(label = "Preço Capa R$", columnType=ColumnType.MOEDA)
	private BigDecimal precoVenda;	

	@Export(label = "Exemplar Nota")
	private String exemplarNota;	
	
	/**
	 * @return the exemplarNota
	 */
	public String getExemplarNota() {
		return exemplarNota;
	}

	/**
	 * @param exemplarNota the exemplarNota to set
	 */
	public void setExemplarNota(String exemplarNota) {
		this.exemplarNota = exemplarNota;
	}

	/**
	 * @return the idProdutoEdicao
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
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
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * @param nomeProduto the nomeProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
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
	 * @return the precoVenda
	 */
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}
}