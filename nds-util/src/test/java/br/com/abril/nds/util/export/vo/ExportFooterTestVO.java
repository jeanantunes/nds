package br.com.abril.nds.util.export.vo;

import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ExportFooterTestVO {
	
	@Export(label = "Total ", alignment = Alignment.RIGHT, alignWithHeader = "Column5", propertyToDynamicLabel = "dynamicProperty")
	private BigDecimal totalColumn5;
	
	@Export(label = "", alignment = Alignment.RIGHT, alignWithHeader = "Column6")
	private BigDecimal totalColumn6;
	
	@Export(label = "", alignment = Alignment.CENTER, alignWithHeader = "Column7")
	private Long totalColumn7;
	
	@Export(label = "", alignment = Alignment.CENTER, alignWithHeader = "Column8")
	private Long totalColumn8;
	
	@Export(label = "", alignment = Alignment.CENTER, alignWithHeader = "Column9")
	private Integer totalColumn9;
	
	@Export(label = "", alignment = Alignment.RIGHT, alignWithHeader = "Column10")
	private BigDecimal totalColumn10;
	
	private BigDecimal dynamicProperty;

	public ExportFooterTestVO(BigDecimal totalColumn5, BigDecimal totalColumn6,
							  Long totalColumn7, Long totalColumn8, Integer totalColumn9,
							  BigDecimal totalColumn10, BigDecimal dynamicProperty) {
		
		this.totalColumn5 = totalColumn5;
		this.totalColumn6 = totalColumn6;
		this.totalColumn7 = totalColumn7;
		this.totalColumn8 = totalColumn8;
		this.totalColumn9 = totalColumn9;
		this.totalColumn10 = totalColumn10;
		this.dynamicProperty = dynamicProperty;
	}

	public BigDecimal getTotalColumn5() {
		return totalColumn5;
	}

	public void setTotalColumn5(BigDecimal totalColumn5) {
		this.totalColumn5 = totalColumn5;
	}

	public BigDecimal getTotalColumn6() {
		return totalColumn6;
	}

	public void setTotalColumn6(BigDecimal totalColumn6) {
		this.totalColumn6 = totalColumn6;
	}

	public Long getTotalColumn7() {
		return totalColumn7;
	}

	public void setTotalColumn7(Long totalColumn7) {
		this.totalColumn7 = totalColumn7;
	}

	public Long getTotalColumn8() {
		return totalColumn8;
	}

	public void setTotalColumn8(Long totalColumn8) {
		this.totalColumn8 = totalColumn8;
	}

	public Integer getTotalColumn9() {
		return totalColumn9;
	}

	public void setTotalColumn9(Integer totalColumn9) {
		this.totalColumn9 = totalColumn9;
	}

	public BigDecimal getTotalColumn10() {
		return totalColumn10;
	}

	public void setTotalColumn10(BigDecimal totalColumn10) {
		this.totalColumn10 = totalColumn10;
	}

	/**
	 * @return the dynamicProperty
	 */
	public BigDecimal getDynamicProperty() {
		return dynamicProperty;
	}

	/**
	 * @param dynamicProperty the dynamicProperty to set
	 */
	public void setDynamicProperty(BigDecimal dynamicProperty) {
		this.dynamicProperty = dynamicProperty;
	}

}
