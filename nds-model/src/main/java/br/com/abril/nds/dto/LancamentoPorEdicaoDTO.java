package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class LancamentoPorEdicaoDTO implements Serializable  {
	
	
	private static final long serialVersionUID = -4125231140420830266L;
	
	@Export(label = "Data Lançamento", alignment=Alignment.CENTER)
	private String dataLancamento;
	
	@Export(label = "Data Recolhimento", alignment=Alignment.CENTER)
	private String dataRecolhimento;
	
	@Export(label = "Reparte", alignment=Alignment.CENTER)
	private BigDecimal reparte;
	
	@Export(label = "Encalhe", alignment=Alignment.CENTER)
	private BigDecimal encalhe;
	
	@Export(label = "Venda", alignment=Alignment.CENTER)
	private BigDecimal venda;
	
	@Export(label = "Total", alignment=Alignment.CENTER)
	private BigDecimal total;
	
	
	
	
	public LancamentoPorEdicaoDTO() {
		
	}
	
	public LancamentoPorEdicaoDTO(String dataLancamento, String dataRecolhimento, BigDecimal reparte, BigDecimal venda,  
			BigDecimal total, BigDecimal encalhe) {
		super();
		this.dataLancamento = dataLancamento;
		this.dataRecolhimento = dataRecolhimento;
		this.reparte = reparte;
		this.venda = venda;
		this.total = total;
	}
	
	

	public String getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento =  DateUtil.formatarData(dataLancamento, Constantes.DATE_PATTERN_PT_BR);
	}

	public String getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = DateUtil.formatarData(dataRecolhimento, Constantes.DATE_PATTERN_PT_BR);
	}

	public BigDecimal getReparte() {
		return reparte;
	}

	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}

	public BigDecimal getVenda() {
		return venda;
	}

	public void setVenda(BigDecimal venda) {
		this.venda = venda;
	}

	public BigDecimal getTotal() {
		return total.setScale(2,RoundingMode.HALF_DOWN);
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public BigDecimal getEncalhe() {
		return encalhe;
	}

	public void setEncalhe(BigDecimal encalhe) {
		this.encalhe = encalhe;
	}
	
}
