package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;

public class ResultadoConsultaEncalheVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private TableModel<CellModelKeyValue<ConsultaEncalheVO>> tableModel;
	private String qtdProdutoPrimeiroRecolhimento;
	private String qtdExemplarPrimeiroRecolhimento;
	private String qtdProdutoDemaisRecolhimentos;
	private String qtdExemplarDemaisRecolhimentos;
	

	/**
	 * Obtém tableModel
	 *
	 * @return TableModel<CellModelKeyValue<ConsultaEncalheVO>>
	 */
	public TableModel<CellModelKeyValue<ConsultaEncalheVO>> getTableModel() {
		return tableModel;
	}

	/**
	 * Atribuí tableModel
	 * @param tableModel 
	 */
	public void setTableModel(
			TableModel<CellModelKeyValue<ConsultaEncalheVO>> tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * Obtém qtdProdutoPrimeiroRecolhimento
	 *
	 * @return String
	 */
	public String getQtdProdutoPrimeiroRecolhimento() {
		return qtdProdutoPrimeiroRecolhimento;
	}

	/**
	 * Atribuí qtdProdutoPrimeiroRecolhimento
	 * @param qtdProdutoPrimeiroRecolhimento 
	 */
	public void setQtdProdutoPrimeiroRecolhimento(
			String qtdProdutoPrimeiroRecolhimento) {
		this.qtdProdutoPrimeiroRecolhimento = qtdProdutoPrimeiroRecolhimento;
	}

	/**
	 * Obtém qtdExemplarPrimeiroRecolhimento
	 *
	 * @return String
	 */
	public String getQtdExemplarPrimeiroRecolhimento() {
		return qtdExemplarPrimeiroRecolhimento;
	}

	/**
	 * Atribuí qtdExemplarPrimeiroRecolhimento
	 * @param qtdExemplarPrimeiroRecolhimento 
	 */
	public void setQtdExemplarPrimeiroRecolhimento(
			String qtdExemplarPrimeiroRecolhimento) {
		this.qtdExemplarPrimeiroRecolhimento = qtdExemplarPrimeiroRecolhimento;
	}

	/**
	 * Obtém qtdProdutoDemaisRecolhimentos
	 *
	 * @return String
	 */
	public String getQtdProdutoDemaisRecolhimentos() {
		return qtdProdutoDemaisRecolhimentos;
	}

	/**
	 * Atribuí qtdProdutoDemaisRecolhimentos
	 * @param qtdProdutoDemaisRecolhimentos 
	 */
	public void setQtdProdutoDemaisRecolhimentos(
			String qtdProdutoDemaisRecolhimentos) {
		this.qtdProdutoDemaisRecolhimentos = qtdProdutoDemaisRecolhimentos;
	}

	/**
	 * Obtém qtdExemplarDemaisRecolhimentos
	 *
	 * @return String
	 */
	public String getQtdExemplarDemaisRecolhimentos() {
		return qtdExemplarDemaisRecolhimentos;
	}

	/**
	 * Atribuí qtdExemplarDemaisRecolhimentos
	 * @param qtdExemplarDemaisRecolhimentos 
	 */
	public void setQtdExemplarDemaisRecolhimentos(
			String qtdExemplarDemaisRecolhimentos) {
		this.qtdExemplarDemaisRecolhimentos = qtdExemplarDemaisRecolhimentos;
	}

	
}
