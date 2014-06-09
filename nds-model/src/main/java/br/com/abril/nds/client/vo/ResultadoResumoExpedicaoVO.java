package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResultadoResumoExpedicaoVO<T> implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private TableModel<CellModelKeyValue<T>> tableModel;
	
	@Export(label = "Total", alignWithHeader = "Reparte", alignment = Alignment.CENTER)
	private int qtdeTotalReparte;
	
	@Export(label = "", alignWithHeader = "Valor Faturado R$", alignment = Alignment.RIGHT)
	private String valorTotalFaturado;
	
	/**
	 * Construtor padrão.
	 */
	public ResultadoResumoExpedicaoVO() {}
	
	/**
	 * Construtor
	 * 
	 * @param tableModel
	 * @param qtdeTotalReparte
	 * @param valorTotalFaturado
	 */
	public ResultadoResumoExpedicaoVO(TableModel<CellModelKeyValue<T>> tableModel,
									  int qtdeTotalReparte, 
									  String valorTotalFaturado) {

		this.tableModel = tableModel;
		this.qtdeTotalReparte = qtdeTotalReparte;
		this.valorTotalFaturado = valorTotalFaturado;
	}

	/**
	 * @return the tableModel
	 */
	public TableModel<CellModelKeyValue<T>> getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(
			TableModel<CellModelKeyValue<T>> tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * @return the qtdeTotalReparte
	 */
	public int getQtdeTotalReparte() {
		return qtdeTotalReparte;
	}

	/**
	 * @param qtdeTotalReparte the qtdeTotalReparte to set
	 */
	public void setQtdeTotalReparte(int qtdeTotalReparte) {
		this.qtdeTotalReparte = qtdeTotalReparte;
	}

	/**
	 * @return the valorTotalFaturado
	 */
	public String getValorTotalFaturado() {
		return valorTotalFaturado;
	}

	/**
	 * @param valorTotalFaturado the valorTotalFaturado to set
	 */
	public void setValorTotalFaturado(String valorTotalFaturado) {
		this.valorTotalFaturado = valorTotalFaturado;
	}
	
	
}
