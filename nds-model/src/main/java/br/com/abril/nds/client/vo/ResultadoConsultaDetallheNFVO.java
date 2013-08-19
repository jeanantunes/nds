package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;

/**
 * Classe responsável pelo resultado da consulta de detalhe da Nota Fiscal. 
 * 
 * @author Discover Technology
 *
 */
public class ResultadoConsultaDetallheNFVO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1674801054865363250L;

	private String totalExemplares;
	
	private String totalSumarizado;
	
	private String totalSumarizadoComDesconto;
	
	private TableModel<CellModel> tableModel;
	
	/**
	 * Construtor.
	 * 
	 * @param tableModel - table model
	 * @param totalExemplares - total de exemplares
	 * @param totalSumarizado - total sumarizado
	 */
	public ResultadoConsultaDetallheNFVO(TableModel<CellModel> tableModel, String totalExemplares, String totalSumarizado, String totalSumarizadoComDesconto) {
		
		this.tableModel = tableModel;
		this.totalExemplares = totalExemplares;
		this.totalSumarizado = totalSumarizado;
	    this.totalSumarizadoComDesconto = totalSumarizadoComDesconto;
	}

	/**
	 * Construtor padrão.
	 */
	public ResultadoConsultaDetallheNFVO() {
		
	}
	
	/**
	 * @return the totalExemplares
	 */
	public String getTotalExemplares() {
		return totalExemplares;
	}

	/**
	 * @param totalExemplares the totalExemplares to set
	 */
	public void setTotalExemplares(String totalExemplares) {
		this.totalExemplares = totalExemplares;
	}

	/**
	 * @return the totalSumarizado
	 */
	public String getTotalSumarizado() {
		return totalSumarizado;
	}

	/**
	 * @param totalSumarizado the totalSumarizado to set
	 */
	public void setTotalSumarizado(String totalSumarizado) {
		this.totalSumarizado = totalSumarizado;
	}

	public String getTotalSumarizadoComDesconto() {
		return totalSumarizadoComDesconto;
	}

	public void setTotalSumarizadoComDesconto(String totalSumarizadoComDesconto) {
		this.totalSumarizadoComDesconto = totalSumarizadoComDesconto;
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
	
}
