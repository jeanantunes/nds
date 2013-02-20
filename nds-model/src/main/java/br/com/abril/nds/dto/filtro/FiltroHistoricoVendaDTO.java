package br.com.abril.nds.dto.filtro;

import java.math.BigDecimal;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.ProdutoDTO;
import br.com.abril.nds.util.ComponentesPDV;

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
	private ComponentesPDV componentesPdf;
	
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

	public Integer getQtdRepartInicial() {
		return qtdRepartInicial;
	}

	public void setQtdRepartInicial(Integer qtdRepartInicial) {
		this.qtdRepartInicial = qtdRepartInicial;
	}

	public Integer getQtdRepartFinal() {
		return qtdRepartFinal;
	}

	public void setQtdRepartFinal(Integer qtdRepartFinal) {
		this.qtdRepartFinal = qtdRepartFinal;
	}

	public Integer getQtdVendaInicial() {
		return qtdVendaInicial;
	}

	public void setQtdVendaInicial(Integer qtdVendaInicial) {
		this.qtdVendaInicial = qtdVendaInicial;
	}

	public Integer getQtdVendaFinal() {
		return qtdVendaFinal;
	}

	public void setQtdVendaFinal(Integer qtdVendaFinal) {
		this.qtdVendaFinal = qtdVendaFinal;
	}

	public BigDecimal getPercentualVenda() {
		return percentualVenda;
	}

	public void setPercentualVenda(BigDecimal percentualVenda) {
		this.percentualVenda = percentualVenda;
	}

	public CotaDTO getCotaDto() {
		return cotaDto;
	}

	public void setCotaDto(CotaDTO cotaDto) {
		this.cotaDto = cotaDto;
	}

	public ComponentesPDV getComponentesPdf() {
		return componentesPdf;
	}

	public void setComponentesPdf(ComponentesPDV componentesPdf) {
		this.componentesPdf = componentesPdf;
	}
	

}
