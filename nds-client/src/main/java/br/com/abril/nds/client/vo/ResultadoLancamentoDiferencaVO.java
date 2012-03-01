package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;

public class ResultadoLancamentoDiferencaVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5231724345676942233L;
	
	private TableModel<CellModel> tableModel;
	
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
	public ResultadoLancamentoDiferencaVO(TableModel<CellModel> tableModel,
										  BigDecimal qtdeTotalDiferencas,
										  String valorTotalDiferencas) {
		
		this.tableModel = tableModel;
		this.qtdeTotalDiferencas = qtdeTotalDiferencas;
		this.valorTotalDiferencas = valorTotalDiferencas;
	}

	/**
	 * @return the tableModel
	 */
	public TableModel<CellModel> getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(TableModel<CellModel> tableModel) {
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
