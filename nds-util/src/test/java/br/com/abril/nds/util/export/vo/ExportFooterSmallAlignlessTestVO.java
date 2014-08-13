package br.com.abril.nds.util.export.vo;

import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ExportFooterSmallAlignlessTestVO {

	@Export(label = "Total", alignment = Alignment.RIGHT)
	private BigDecimal totalColumn1;
	
	@Export(label = "Total 2", alignment = Alignment.RIGHT)
	private BigDecimal totalColumn2;
	
	@Export(label = "Total 3", alignment = Alignment.CENTER)
	private Long totalColumn3;

	public ExportFooterSmallAlignlessTestVO(BigDecimal totalColumn1, BigDecimal totalColumn2, Long totalColumn3) {
		
		this.totalColumn1 = totalColumn1;
		this.totalColumn2 = totalColumn2;
		this.totalColumn3 = totalColumn3;
	}

	/**
	 * @return the totalColumn1
	 */
	public BigDecimal getTotalColumn1() {
		return totalColumn1;
	}

	/**
	 * @param totalColumn1 the totalColumn1 to set
	 */
	public void setTotalColumn1(BigDecimal totalColumn1) {
		this.totalColumn1 = totalColumn1;
	}

	/**
	 * @return the totalColumn2
	 */
	public BigDecimal getTotalColumn2() {
		return totalColumn2;
	}

	/**
	 * @param totalColumn2 the totalColumn2 to set
	 */
	public void setTotalColumn2(BigDecimal totalColumn2) {
		this.totalColumn2 = totalColumn2;
	}

	/**
	 * @return the totalColumn3
	 */
	public Long getTotalColumn3() {
		return totalColumn3;
	}

	/**
	 * @param totalColumn3 the totalColumn3 to set
	 */
	public void setTotalColumn3(Long totalColumn3) {
		this.totalColumn3 = totalColumn3;
	}
	
}
