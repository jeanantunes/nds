package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResultadoConsultaEncalheVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private TableModel<CellModelKeyValue<ConsultaEncalheVO>> tableModel;
	
	private String valorReparte;
	
	private String valorEncalhe;
	
	private String valorVendaDia;
	
	private String valorDebitoCredito;
	
	private String valorPagar;	
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
	 * @return the valorEncalhe
	 */
	public String getValorEncalhe() {
		return valorEncalhe;
	}

	/**
	 * @param valorEncalhe the valorEncalhe to set
	 */
	public void setValorEncalhe(String valorEncalhe) {
		this.valorEncalhe = valorEncalhe;
	}

	/**
	 * @return the valorVendaDia
	 */
	public String getValorVendaDia() {
		return valorVendaDia;
	}

	/**
	 * @param valorVendaDia the valorVendaDia to set
	 */
	public void setValorVendaDia(String valorVendaDia) {
		this.valorVendaDia = valorVendaDia;
	}

	/**
	 * @return the valorDebitoCredito
	 */
	public String getValorDebitoCredito() {
		return valorDebitoCredito;
	}

	/**
	 * @param valorDebitoCredito the valorDebitoCredito to set
	 */
	public void setValorDebitoCredito(String valorDebitoCredito) {
		this.valorDebitoCredito = valorDebitoCredito;
	}

	/**
	 * @return the valorPagar
	 */
	public String getValorPagar() {
		return valorPagar;
	}

	/**
	 * @param valorPagar the valorPagar to set
	 */
	public void setValorPagar(String valorPagar) {
		this.valorPagar = valorPagar;
	}

	/**
	 * @return the valorReparte
	 */
	public String getValorReparte() {
		return valorReparte;
	}

	/**
	 * @param valorReparte the valorReparte to set
	 */
	public void setValorReparte(String valorReparte) {
		this.valorReparte = valorReparte;
	}

}
