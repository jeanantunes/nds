package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResultadoDiferencaVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5231724345676942233L;
	
	private TableModel<CellModelKeyValue<DiferencaVO>> tableModel;
	
	@Export(label = "Total", alignment=Alignment.RIGHT, alignWithHeader = "Exemplar")
	private BigInteger qtdeTotalDiferencas;
	
	@Export(label = "",alignment=Alignment.RIGHT, alignWithHeader = "Total R$")
	private String valorTotalDiferencas;
	
	/**
	 * Construtor padrão.
	 */
	public ResultadoDiferencaVO() {
		
		
	}
	
	/**
	 * Construtor.
	 * 
	 * @param tableModel - table model
	 * @param qtdeTotalDiferencas - quantidade do total de diferenças
	 * @param valorTotalDiferencas - valor total das diferenças
	 */
	public ResultadoDiferencaVO(TableModel<CellModelKeyValue<DiferencaVO>> tableModel,
										BigInteger qtdeTotalDiferencas,
										  String valorTotalDiferencas) {
		
		this.tableModel = tableModel;
		this.qtdeTotalDiferencas = qtdeTotalDiferencas;
		this.valorTotalDiferencas = valorTotalDiferencas;
	}

	/**
	 * @return the tableModel
	 */
	public TableModel<CellModelKeyValue<DiferencaVO>> getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(TableModel<CellModelKeyValue<DiferencaVO>> tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * @return the qtdeTotalDiferencas
	 */
	public BigInteger getQtdeTotalDiferencas() {
		return qtdeTotalDiferencas;
	}

	/**
	 * @param qtdeTotalDiferencas the qtdeTotalDiferencas to set
	 */
	public void setQtdeTotalDiferencas(BigInteger qtdeTotalDiferencas) {
		this.qtdeTotalDiferencas = qtdeTotalDiferencas;
	}

	/**
	 * @return the valorTotalDiferencas
	 */
	public String getValorTotalDiferencas() {
		return valorTotalDiferencas;
	}

	/**
	 * @param valorTotalDiferencas the valorTotalDiferencas to set
	 */
	public void setValorTotalDiferencas(String valorTotalDiferencas) {
		this.valorTotalDiferencas = valorTotalDiferencas;
	}

}
