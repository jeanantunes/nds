package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * DTO que representa a relação das cotas com diferenças, de acordo com os rateios.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class DetalheDiferencaCotaDTO implements Serializable {

	private static final long serialVersionUID = -3799374576523516217L;

	@Export(label = "Total Exemplares", alignment=Alignment.CENTER)
	private BigInteger totalExemplares;

	@Export(label = "Valor Total", alignment=Alignment.CENTER, alignWithHeader="Total R$")
	private String valorTotalFormatado;

	private BigDecimal valorTotal;
	
	private Long quantidadeTotalRegistrosDiferencaCota;
	
	private List<RateioDiferencaCotaDTO> detalhesDiferenca;
	
	private TableModel<CellModelKeyValue<RateioDiferencaCotaDTO>> tableModel;

	/**
	 * @return the valorTotalFormatado
	 */
	public String getValorTotalFormatado() {
		return valorTotalFormatado;
	}

	/**
	 * @param valorTotalFormatado the valorTotalFormatado to set
	 */
	public void setValorTotalFormatado(String valorTotalFormatado) {
		this.valorTotalFormatado = valorTotalFormatado;
	}

	/**
	 * @return the totalExemplares
	 */
	public BigInteger getTotalExemplares() {
		return totalExemplares;
	}

	/**
	 * @param totalExemplares the totalExemplares to set
	 */
	public void setTotalExemplares(BigInteger totalExemplares) {
		this.totalExemplares = totalExemplares;
	}

	/**
	 * @return the valorTotal
	 */
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(BigDecimal valorTotal) {
		
		this.valorTotal = valorTotal;

		this.valorTotalFormatado = CurrencyUtil.formatarValor(valorTotal);
	}

	/**
	 * @return the quantidadeTotalRegistrosDiferencaCota
	 */
	public Long getQuantidadeTotalRegistrosDiferencaCota() {
		return quantidadeTotalRegistrosDiferencaCota;
	}

	/**
	 * @param quantidadeTotalRegistrosDiferencaCota the quantidadeTotalRegistrosDiferencaCota to set
	 */
	public void setQuantidadeTotalRegistrosDiferencaCota(
			Long quantidadeTotalRegistrosDiferencaCota) {
		this.quantidadeTotalRegistrosDiferencaCota = quantidadeTotalRegistrosDiferencaCota;
	}

	/**
	 * @return the detalhesDiferenca
	 */
	public List<RateioDiferencaCotaDTO> getDetalhesDiferenca() {
		return detalhesDiferenca;
	}

	/**
	 * @param detalhesDiferenca the detalhesDiferenca to set
	 */
	public void setDetalhesDiferenca(List<RateioDiferencaCotaDTO> detalhesDiferenca) {
		this.detalhesDiferenca = detalhesDiferenca;
	}

	/**
	 * @return the tableModel
	 */
	public TableModel<CellModelKeyValue<RateioDiferencaCotaDTO>> getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(
			TableModel<CellModelKeyValue<RateioDiferencaCotaDTO>> tableModel) {
		this.tableModel = tableModel;
	}
}
