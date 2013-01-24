package br.com.abril.nds.dto.filtro;

@SuppressWarnings("serial")
public class FiltroConsultaFixacaoProdutoDTO extends FiltroDTO{
 
	private String codigoProduto;
	private String nomeProduto;
	private String classificacaoProduto;
	
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
	public String getClassificacaoProduto() {
		return classificacaoProduto;
	}
	public void setClassificacaoProduto(String classificacaoProduto) {
		this.classificacaoProduto = classificacaoProduto;
	}
	

	
}
