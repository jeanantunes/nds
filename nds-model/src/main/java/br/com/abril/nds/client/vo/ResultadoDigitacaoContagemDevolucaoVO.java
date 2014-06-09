package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;

public class ResultadoDigitacaoContagemDevolucaoVO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private TableModel<CellModelKeyValue<DigitacaoContagemDevolucaoVO>> tableModel;
	
	private String valorTotal;
	
	/**
	 * Construtor padrão.
	 */
	public ResultadoDigitacaoContagemDevolucaoVO() {}
	
	/**
	 * Construtor
	 * 
	 * @param tableModel
	 * @param qtdeTotalReparte
	 * @param valorTotalFaturado
	 */
	public ResultadoDigitacaoContagemDevolucaoVO(
			TableModel<CellModelKeyValue<DigitacaoContagemDevolucaoVO>> tableModel,
			String valorTotal) {

		this.tableModel = tableModel;
		this.valorTotal = valorTotal;
	}


	/**
	 * @return the tableModel
	 */
	public TableModel<CellModelKeyValue<DigitacaoContagemDevolucaoVO>> getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(
			TableModel<CellModelKeyValue<DigitacaoContagemDevolucaoVO>> tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * @return the valorTotal
	 */
	public String getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}

	
	
}
