package br.com.abril.nds.dto.filtro;

import java.math.BigDecimal;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.ProdutoDTO;

public class FiltroHistoricoVendaDTO extends FiltroDTO {

	private static final long serialVersionUID = 6967582317485942551L;

	private ProdutoDTO produtoDto;
	private Long tipoClassificacaoProdutoId;
	private Long numeroEdicao;

	// Filtro Pesquisa por Histórico venda
	private Integer qtdRepartInicial;
	private Integer qtdRepartFinal;
	private Integer qtdVendaInicial;
	private Integer qtdVendaFinal;
	private BigDecimal percentualVenda;
	private CotaDTO cotaDto;
	
	
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
