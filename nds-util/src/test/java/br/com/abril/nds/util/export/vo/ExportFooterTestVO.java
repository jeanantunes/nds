package br.com.abril.nds.util.export.vo;

import java.math.BigDecimal;
import java.util.Map;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class ExportFooterTestVO {
	
	@Export(label = "Total", alignment = Alignment.RIGHT, alignWithHeader = "Column5")
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
	
	@Export(label = "Total R$", alignment = Alignment.RIGHT)
	private Map<String, BigDecimal> footerMap;

	public ExportFooterTestVO(BigDecimal totalColumn5, BigDecimal totalColumn6,
							  Long totalColumn7, Long totalColumn8, Integer totalColumn9,
							  BigDecimal totalColumn10, Map<String, BigDecimal> footerMap) {
		
		this.totalColumn5 = totalColumn5;
		this.totalColumn6 = totalColumn6;
		this.totalColumn7 = totalColumn7;
		this.totalColumn8 = totalColumn8;
		this.totalColumn9 = totalColumn9;
		this.totalColumn10 = totalColumn10;
		this.footerMap = footerMap;
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

	public Map<String, BigDecimal> getFooterMap() {
		return footerMap;
	}

	public void setFooterMap(Map<String, BigDecimal> footerMap) {
		this.footerMap = footerMap;
	}

}
