package br.com.abril.nds.dto.filtro;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.ProdutoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.util.ComponentesPDV;

public class FiltroHistoricoVendaDTO extends FiltroDTO {

	private static final long serialVersionUID = 6967582317485942551L;

	private ProdutoDTO produtoDto;
	private Long tipoClassificacaoProdutoId;
	private Long numeroEdicao;

	// Filtro Pesquisa por Histórico venda
	private boolean cotasAtivas;
	private BigInteger qtdReparteInicial;
	private BigInteger qtdReparteFinal;
	private Integer qtdVendaInicial;
	private Integer qtdVendaFinal;
	private BigDecimal percentualVenda;
	private CotaDTO cotaDto;
	private ComponentesPDV componentesPdf;
	private List<ProdutoEdicaoDTO> listProdutoEdicaoDTO;
	
	public ProdutoDTO getProdutoDto() {
		return produtoDto;
	}

	public void setProdutoDto(ProdutoDTO produtoDto) {
		this.produtoDto = produtoDto;
	}

	public Long getTipoClassificacaoProdutoId() {
		return tipoClassificacaoProdutoId;
	}

	public void setTipoClassificacaoProdutoId(Long tipoClassificacaoProdutoId) {
		this.tipoClassificacaoProdutoId = tipoClassificacaoProdutoId;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public boolean isCotasAtivas() {
		return cotasAtivas;
	}

	public void setCotasAtivas(boolean cotasAtivas) {
		this.cotasAtivas = cotasAtivas;
	}

	public BigInteger getQtdReparteInicial() {
		return qtdReparteInicial;
	}

	public void setQtdReparteInicial(BigInteger qtdReparteInicial) {
		this.qtdReparteInicial = qtdReparteInicial;
	}

	public BigInteger getQtdReparteFinal() {
		return qtdReparteFinal;
	}

	public void setQtdReparteFinal(BigInteger qtdReparteFinal) {
		this.qtdReparteFinal = qtdReparteFinal;
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

	public List<ProdutoEdicaoDTO> getListProdutoEdicaoDTO() {
		return listProdutoEdicaoDTO;
	}

	public void setListProdutoEdicaoDTO(List<ProdutoEdicaoDTO> listProdutoEdicaoDTO) {
		this.listProdutoEdicaoDTO = listProdutoEdicaoDTO;
	}
	
}
