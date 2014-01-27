package br.com.abril.nds.dto.filtro;

import java.util.List;


public class FiltroRegiaoNMaioresRankingDTO extends FiltroDTO {

	private static final long serialVersionUID = -5820448469557821829L;

	private List<String> codigoProduto;
	private List<String> numeroEdicao;
	private Integer limitePesquisa;

	public List<String> getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(List<String> codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public int getLimitePesquisa() {
		return limitePesquisa;
	}

	public List<String> getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(List<String> numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public void setLimitePesquisa(Integer limitePesquisa) {
		this.limitePesquisa = limitePesquisa;
	}
}
