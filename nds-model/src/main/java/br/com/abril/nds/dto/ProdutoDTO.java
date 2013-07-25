package br.com.abril.nds.dto;

public class ProdutoDTO {

	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long idProduto;

	private Long idClassificacaoProduto;


	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}
	
	public Long getIdClassificacaoProduto() {
		return idClassificacaoProduto;
	}

	public void setIdClassificacaoProduto(Long idClassificacaoProduto) {
		this.idClassificacaoProduto = idClassificacaoProduto;
	}
}
