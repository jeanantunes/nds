package br.com.abril.nds.dto;


/**
 * 
 * @author - InfoA2 - Samuel Mendes
 * 
 * <h1> DTO será utilizado para exibição do grid (Produtos Recebidos) dentro do sub menu Excecao Segmento Parciais  </h1>
 * 
 */

public class ProdutoRecebidoDTO extends UsuarioLogDTO {

	private static final long serialVersionUID = 3943113104970461137L;

	private String codigoProduto;
	private String nomeProduto;

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
}
