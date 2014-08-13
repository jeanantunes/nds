package br.com.abril.nds.util.export.vo;

import java.math.BigDecimal;
import java.util.Map;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ExportFooterVerticalTestVO {
	
	@Export(label = "Total R$", alignment = Alignment.RIGHT, printVertical = true)
	private Map<String, BigDecimal> footerMap;
	
	public ExportFooterVerticalTestVO(Map<String, BigDecimal> footerMap) {
		
		this.footerMap = footerMap;
	}

	/**
	 * @return the footerMap
	 */
	public Map<String, BigDecimal> getFooterMap() {
		return footerMap;
	}

	/**
	 * @param footerMap the footerMap to set
	 */
	public void setFooterMap(Map<String, BigDecimal> footerMap) {
		this.footerMap = footerMap;
	}

}
