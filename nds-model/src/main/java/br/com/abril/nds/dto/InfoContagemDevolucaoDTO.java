package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class InfoContagemDevolucaoDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private List<ContagemDevolucaoDTO> listaContagemDevolucao;
	
	private Integer qtdTotalRegistro;
	
	@Export(label="Valor Total Geral", alignWithHeader="Total R$", fontSize=9, columnType=ColumnType.MOEDA)
	private BigDecimal valorTotalGeral;
	
	public InfoContagemDevolucaoDTO() {}
	
	
	public InfoContagemDevolucaoDTO(
			List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		super();
		this.listaContagemDevolucao = listaContagemDevolucao;
	}


	public List<ContagemDevolucaoDTO> getListaContagemDevolucao() {
		return listaContagemDevolucao;
	}

	public void setListaContagemDevolucao(
			List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		this.listaContagemDevolucao = listaContagemDevolucao;
	}
	

	public Integer getQtdTotalRegistro() {
		return qtdTotalRegistro;
	}

	public void setQtdTotalRegistro(Integer qtdTotalRegistro) {
		this.qtdTotalRegistro = qtdTotalRegistro;
	}


	/**
	 * Obtém valorTotalGeral
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorTotalGeral() {
		return valorTotalGeral;
	}


	/**
	 * Atribuí valorTotalGeral
	 * @param valorTotalGeral 
	 */
	public void setValorTotalGeral(BigDecimal valorTotalGeral) {
		this.valorTotalGeral = valorTotalGeral;
	}

	

	
	
}