package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
@Exportable
public class FooterTotalFornecedorVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4248910091629744306L;
	
	
	@Export(label = "Totais R$", printVertical = true, columnType = ColumnType.MOEDA)
	private HashMap<String, BigDecimal> totais;
	
	public FooterTotalFornecedorVO() {
	}


	public FooterTotalFornecedorVO(HashMap<String, BigDecimal> totais) {
		super();
		this.totais = totais;
	}


	public HashMap<String, BigDecimal> getTotais() {
		return totais;
	}


	public void setTotais(HashMap<String, BigDecimal> totais) {
		this.totais = totais;
	}

}
