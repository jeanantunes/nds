package br.com.abril.nds.dto.filtro;

import br.com.abril.nds.dto.ProdutoDTO;

public class FiltroHistoricoVendaDTO extends FiltroDTO {

	private static final long serialVersionUID = 6967582317485942551L;

	private ProdutoDTO produtoDto;
	private Long tipoClassificacaoProdutoId;
	private Long numeroEdicao;

	public ProdutoDTO getProdutoDto() {
		return produtoDto;
	}

	public void setProdutoDto(ProdutoDTO produtoDto) {
		this.produtoDto = produtoDto;
	}

	public Long getTipoClassificacaoProdutoId() {
		return tipoClassificacaoProdutoId;
	}

	public void setTipoClassificacaoProdutoId(Long classificacaoProduto) {
		this.tipoClassificacaoProdutoId = classificacaoProduto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

}
