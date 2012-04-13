package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResultadoConsultaEncalheVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private TableModel<CellModelKeyValue<ConsultaEncalheVO>> tableModel;
	
	@Export(label="Qtde de Tipos de Produto Recolhidos no Primeiro Dia")
	private String qtdProdutoPrimeiroRecolhimento;
	
	@Export(label="Qtde de Exemplares Recolhidos no Primeiro Dia")
	private String qtdExemplarPrimeiroRecolhimento;
	
	@Export(label="Qtde de Tipos de Produto Recolhidos no Após Primeiro Dia")
	private String qtdProdutoDemaisRecolhimentos;
	
	@Export(label="Qtde de Exemplares Recolhidos Após Primeiro Dia")
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
