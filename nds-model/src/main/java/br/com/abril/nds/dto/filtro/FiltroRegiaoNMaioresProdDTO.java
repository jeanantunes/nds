package br.com.abril.nds.dto.filtro;


public class FiltroRegiaoNMaioresProdDTO extends FiltroDTO {

	private static final long serialVersionUID = 3054966002165075000L;
	
	private String codigoProduto;
	private String nome;
	private Long idTipoClassificacaoProduto;
	
	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getIdTipoClassificacaoProduto() {
		return idTipoClassificacaoProduto;
	}

	public void setIdTipoClassificacaoProduto(Long idTipoClassificacaoProduto) {
		this.idTipoClassificacaoProduto = idTipoClassificacaoProduto;
	}
}
