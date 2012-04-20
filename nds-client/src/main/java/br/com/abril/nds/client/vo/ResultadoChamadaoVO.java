package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResultadoChamadaoVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5231724345676942233L;
	
	private TableModel<CellModelKeyValue<ChamadaoVO>> tableModel;
	
	private Long qtdProdutosTotal;
	
	private BigDecimal qtdExemplaresTotal;
	
	private BigDecimal valorTotal;
	
	/**
	 * Construtor padrão.
	 */
	public ResultadoChamadaoVO() {
		
		
	}
	
	/**
	 * Construtor.
	 * 
	 * @param tableModel - table model
	 * @param qtdProdutosTotal - quantidade total de produtod
	 * @param qtdExemplaresTotal - quantidade total de exemplares
	 * @param valorTotal - valor total
	 */
	public ResultadoChamadaoVO(TableModel<CellModelKeyValue<ChamadaoVO>> tableModel,
							   Long qtdProdutosTotal,
							   BigDecimal qtdExemplaresTotal,
							   BigDecimal valorTotal) {
		
		this.tableModel = tableModel;
		this.qtdProdutosTotal = qtdProdutosTotal;
		this.qtdExemplaresTotal = qtdExemplaresTotal;
		this.valorTotal = valorTotal;
	}

	/**
	 * @return the tableModel
	 */
	public TableModel<CellModelKeyValue<ChamadaoVO>> getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(TableModel<CellModelKeyValue<ChamadaoVO>> tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * @return the qtdProdutosTotal
	 */
	public Long getQtdProdutosTotal() {
		return qtdProdutosTotal;
	}

	/**
	 * @param qtdProdutosTotal the qtdProdutosTotal to set
	 */
	public void setQtdProdutosTotal(Long qtdProdutosTotal) {
		this.qtdProdutosTotal = qtdProdutosTotal;
	}

	/**
	 * @return the qtdExemplaresTotal
	 */
	public BigDecimal getQtdExemplaresTotal() {
		return qtdExemplaresTotal;
	}

	/**
	 * @param qtdExemplaresTotal the qtdExemplaresTotal to set
	 */
	public void setQtdExemplaresTotal(BigDecimal qtdExemplaresTotal) {
		this.qtdExemplaresTotal = qtdExemplaresTotal;
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
	}

}
