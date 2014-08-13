package br.com.abril.nds.util.export.vo;

import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ExportFooterSmallTestVO {

	@Export(label = "Total", alignment = Alignment.RIGHT, alignWithHeader = "Column3")
	private BigDecimal totalColumn3;
	
	@Export(label = "", alignment = Alignment.RIGHT, alignWithHeader = "Column4")
	private BigDecimal totalColumn4;
	
	@Export(label = "", alignment = Alignment.CENTER, alignWithHeader = "Column5")
	private Long totalColumn5;

	public ExportFooterSmallTestVO(BigDecimal totalColumn3,
								   BigDecimal totalColumn4, Long totalColumn5) {
		
		this.totalColumn3 = totalColumn3;
		this.totalColumn4 = totalColumn4;
		this.totalColumn5 = totalColumn5;
	}

	/**
	 * @return the totalColumn3
	 */
	public BigDecimal getTotalColumn3() {
		return totalColumn3;
	}

	/**
	 * @param totalColumn3 the totalColumn3 to set
	 */
	public void setTotalColumn3(BigDecimal totalColumn3) {
		this.totalColumn3 = totalColumn3;
	}

	/**
	 * @return the totalColumn4
	 */
	public BigDecimal getTotalColumn4() {
		return totalColumn4;
	}

	/**
	 * @param totalColumn4 the totalColumn4 to set
	 */
	public void setTotalColumn4(BigDecimal totalColumn4) {
		this.totalColumn4 = totalColumn4;
	}

	/**
	 * @return the totalColumn5
	 */
	public Long getTotalColumn5() {
		return totalColumn5;
	}

	/**
	 * @param totalColumn5 the totalColumn5 to set
	 */
	public void setTotalColumn5(Long totalColumn5) {
		this.totalColumn5 = totalColumn5;
	}
	
}
