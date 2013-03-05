package br.com.abril.nds.dto.filtro;

import br.com.abril.nds.dto.ProdutoDTO;

public class FiltroAnaliseEstudoDTO extends FiltroDTO {

	private static final long serialVersionUID = -7460175679601254408L;

	private Integer numeroEstudo;
	private ProdutoDTO produtoDto;
	private Long numeroEdicao;
	private Long idTipoClassificacaoProduto;

	public Integer getNumeroEstudo() {
		return numeroEstudo;
	}

	public void setNumeroEstudo(Integer numeroEstudo) {
		this.numeroEstudo = numeroEstudo;
	}

	public ProdutoDTO getProdutoDto() {
		return produtoDto;
	}

	public void setProdutoDto(ProdutoDTO produtoDto) {
		this.produtoDto = produtoDto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public Long getIdTipoClassificacaoProduto() {
		return idTipoClassificacaoProduto;
	}

	public void setIdTipoClassificacaoProduto(Long idTipoClassificacaoProduto) {
		this.idTipoClassificacaoProduto = idTipoClassificacaoProduto;
	}

}
