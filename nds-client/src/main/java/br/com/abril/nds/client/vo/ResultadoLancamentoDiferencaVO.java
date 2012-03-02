package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;

public class ResultadoLancamentoDiferencaVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5231724345676942233L;
	
	private TableModel<CellModelKeyValue<LancamentoDiferencaVO>> tableModel;
	
	private BigDecimal qtdeTotalDiferencas;
	
	private String valorTotalDiferencas;
	
	/**
	 * Construtor padrão.
	 */
	public ResultadoLancamentoDiferencaVO() {
		
		
	}
	
	/**
	 * Construtor.
	 * 
	 * @param tableModel - table model
	 * @param qtdeTotalDiferencas - quantidade do total de diferenças
	 * @param valorTotalDiferencas - valor total das diferenças
	 */
	public ResultadoLancamentoDiferencaVO(TableModel<CellModelKeyValue<LancamentoDiferencaVO>> tableModel,
										  BigDecimal qtdeTotalDiferencas,
										  String valorTotalDiferencas) {
		
		this.tableModel = tableModel;
		this.qtdeTotalDiferencas = qtdeTotalDiferencas;
		this.valorTotalDiferencas = valorTotalDiferencas;
	}

	/**
	 * @return the tableModel
	 */
	public TableModel<CellModelKeyValue<LancamentoDiferencaVO>> getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(TableModel<CellModelKeyValue<LancamentoDiferencaVO>> tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * @return the qtdeTotalDiferencas
	 */
	public BigDecimal getQtdeTotalDiferencas() {
		return qtdeTotalDiferencas;
	}

	/**
	 * @param qtdeTotalDiferencas the qtdeTotalDiferencas to set
	 */
	public void setQtdeTotalDiferencas(BigDecimal qtdeTotalDiferencas) {
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
