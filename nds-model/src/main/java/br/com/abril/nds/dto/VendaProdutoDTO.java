package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class VendaProdutoDTO implements Serializable {

	private static final long serialVersionUID = 2903790306949832310L;
	
	@Export(label = "Edição", alignment=Alignment.LEFT)
	private Long numEdicao;
	
	@Export(label = "Data Lançamento", alignment=Alignment.CENTER)
	private String dataLancamento;
	
	@Export(label = "Data Recolhimento", alignment=Alignment.CENTER)
	private String dataRecolhimento;
	
	@Export(label = "Reparte", alignment=Alignment.CENTER)
	private BigDecimal reparte;
	
	@Export(label = "Venda", alignment=Alignment.CENTER)
	private BigDecimal venda;
	
	@Export(label = "% Venda", alignment=Alignment.CENTER)
	private BigDecimal percentagemVenda;
	
	@Export(label = "Preço Capa", alignment=Alignment.CENTER)
	private BigDecimal precoCapa;
	
	@Export(label = "Total", alignment=Alignment.CENTER)
	private BigDecimal total;
	
	
	public VendaProdutoDTO() {
		
	}
	
	public VendaProdutoDTO(Long numEdicao, String dataLancamento, String dataRecolhimento, BigDecimal reparte, BigDecimal venda,  BigDecimal percentagemVenda, BigDecimal precoCapa, BigDecimal total) {
		super();
		this.numEdicao = numEdicao;
		this.dataLancamento = dataLancamento;
		this.dataRecolhimento = dataRecolhimento;
		this.reparte = reparte;
		this.venda = venda;
		this.percentagemVenda = percentagemVenda;
		this.precoCapa = precoCapa;
		this.total = total;		
	}
	
	

	public Long getNumEdicao() {
		return numEdicao;
	}

	public void setNumEdicao(Long numEdicao) {
		this.numEdicao = numEdicao;
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

	public BigDecimal getPercentagemVenda() {
		return percentagemVenda;
	}

	public void setPercentagemVenda(BigDecimal percentagemVenda) {
		this.percentagemVenda = percentagemVenda;
	}

	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}

	public BigDecimal getTotal() {
		return total.setScale(2,RoundingMode.HALF_DOWN);
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	
}
