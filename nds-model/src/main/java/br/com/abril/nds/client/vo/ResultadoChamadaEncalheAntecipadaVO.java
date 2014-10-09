package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResultadoChamadaEncalheAntecipadaVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5231724345676942233L;
	
	private TableModel<CellModelKeyValue<ChamadaEncalheAntecipadaVO>> tableModel;
	
	@Export(label = "Total Cotas", alignWithHeader = "Cota", alignment = Alignment.CENTER)
	private BigDecimal qtdeTotalCotas;
	
	@Export(label = "Exemplares", alignWithHeader = "Qtde.Exemplares", alignment = Alignment.CENTER)
	private BigDecimal qntTotalExemplares;
	
	private boolean recolhimentoFinal;
	
	private String dataRecolhimentoPrevista;
	
	private String tipoChamadaEncalhe;
	
	/**
	 * Construtor padrão.
	 */
	public ResultadoChamadaEncalheAntecipadaVO() {}
	
	/**
	 * Construtor.
	 * 
	 * @param tableModel - table model
	 * @param qtdeTotalDiferencas - quantidade do total de diferenças
	 * @param valorTotalDiferencas - valor total das diferenças
	 */
	public ResultadoChamadaEncalheAntecipadaVO(TableModel<CellModelKeyValue<ChamadaEncalheAntecipadaVO>> tableModel,
										  BigDecimal qtdeTotalCotas,
										  BigDecimal qntTotalExemplares) {
		
		this.tableModel = tableModel;
		this.qtdeTotalCotas = qtdeTotalCotas;
		this.qntTotalExemplares = qntTotalExemplares;
	}

	/**
	 * @return the tableModel
	 */
	public TableModel<CellModelKeyValue<ChamadaEncalheAntecipadaVO>> getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(TableModel<CellModelKeyValue<ChamadaEncalheAntecipadaVO>> tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * @return the qtdeTotalCotas
	 */
	public BigDecimal getQtdeTotalCotas() {
		return qtdeTotalCotas;
	}

	/**
	 * @param qtdeTotalCotas the qtdeTotalCotas to set
	 */
	public void setQtdeTotalCotas(BigDecimal qtdeTotalCotas) {
		this.qtdeTotalCotas = qtdeTotalCotas;
	}

	/**
	 * @return the qntTotalExemplares
	 */
	public BigDecimal getQntTotalExemplares() {
		return qntTotalExemplares;
	}

	/**
	 * @param qntTotalExemplares the qntTotalExemplares to set
	 */
	public void setQntTotalExemplares(BigDecimal qntTotalExemplares) {
		this.qntTotalExemplares = qntTotalExemplares;
	}

	public boolean isRecolhimentoFinal() {
		return recolhimentoFinal;
	}

	public void setRecolhimentoFinal(boolean recolhimentoFinal) {
		this.recolhimentoFinal = recolhimentoFinal;
	}

	public String getDataRecolhimentoPrevista() {
		return dataRecolhimentoPrevista;
	}

	public void setDataRecolhimentoPrevista(String dataRecolhimentoPrevista) {
		this.dataRecolhimentoPrevista = dataRecolhimentoPrevista;
	}

	public String getTipoChamadaEncalhe() {
		return tipoChamadaEncalhe;
	}

	public void setTipoChamadaEncalhe(String tipoChamadaEncalhe) {
		this.tipoChamadaEncalhe = tipoChamadaEncalhe;
	}
		
}