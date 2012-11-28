package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class VendaProdutoDTO implements Serializable {

	private static final long serialVersionUID = 2903790306949832310L;
	
	@Export(label = "Edição", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Long numEdicao;
	
	@Export(label = "Data Lançamento", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String dataLancamento;
	
	@Export(label = "Data Recolhimento", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private String dataRecolhimento;
	
	@Export(label = "Reparte", alignment=Alignment.CENTER, exhibitionOrder = 4)
	private BigInteger reparte;
	
	private BigInteger venda;
	
	
	private BigDecimal percentagemVenda;
	
	
	private BigDecimal precoCapa;
	
	private String chamadaCapa;
	
	private BigDecimal total;
	
	private BigDecimal encalhe;
	
	private String valorVendaFormatado;
	
	private String valorPrecoCapaFormatado;
	
	private String valorTotalFormatado;
	
	private String percentagemVendaFormatado;
	
	public VendaProdutoDTO() {
		
	}
	
	public VendaProdutoDTO(Long numEdicao, String dataLancamento, String dataRecolhimento, BigInteger reparte, BigInteger venda,  
			BigDecimal percentagemVenda, BigDecimal precoCapa, BigDecimal total, BigDecimal encalhe) {
		super();
		this.numEdicao = numEdicao;
		//this.dataLancamento = dataLancamento;
		//this.dataRecolhimento = dataRecolhimento;
		this.reparte = reparte;
		this.venda = venda;
		this.percentagemVenda = percentagemVenda;
		this.precoCapa = precoCapa;
		this.total = total;
		this.encalhe = encalhe;
	}
	
	public VendaProdutoDTO(Long numEdicao, Date dataLancamento, Date dataRecolhimento, BigInteger reparte, BigInteger venda,  
			Number percentagemVenda, Number precoCapa, String chamadaCapa, Number total) {
		super();
		this.numEdicao = numEdicao;
		this.dataLancamento = DateUtil.formatarData(dataLancamento, Constantes.DATE_PATTERN_PT_BR);
		this.dataRecolhimento = DateUtil.formatarData(dataRecolhimento, Constantes.DATE_PATTERN_PT_BR);
		this.reparte = reparte;
		this.venda = venda;
		if (percentagemVenda instanceof BigInteger) {
			this.percentagemVenda = new BigDecimal((BigInteger) percentagemVenda);
		} else {
			this.percentagemVenda = (BigDecimal) percentagemVenda;
		}
		if (precoCapa instanceof BigInteger) {
			this.precoCapa = new BigDecimal((BigInteger) precoCapa);
		} else {
			this.precoCapa = (BigDecimal) precoCapa;
		}
		this.chamadaCapa = chamadaCapa;
		if (total instanceof BigInteger) {
			this.total = new BigDecimal((BigInteger) total);
		} else {
			this.total = (BigDecimal) total;
		}
		
		this.percentagemVendaFormatado = CurrencyUtil.formatarValor(percentagemVenda);
		this.valorVendaFormatado = CurrencyUtil.formatarValor(venda);
		this.valorPrecoCapaFormatado = CurrencyUtil.formatarValor(precoCapa);
		this.valorTotalFormatado = CurrencyUtil.formatarValor(total);
		
	}
	
	

	public Long getNumEdicao() {
		return numEdicao;
	}

	public void setNumEdicao(Long numEdicao) {
		this.numEdicao = numEdicao;
	}

	/*public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento =  DateUtil.formatarData(dataLancamento, Constantes.DATE_PATTERN_PT_BR);
	}*/

	/*public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = DateUtil.formatarData(dataRecolhimento, Constantes.DATE_PATTERN_PT_BR);
	}*/
	
	/*public void setVenda(BigDecimal venda) {
		this.venda = venda;
		if (venda != null) {
			valorVendaFormatado = CurrencyUtil.formatarValor(venda);
		}
	}/
	
	@Export(label = "Venda", alignment=Alignment.CENTER, exhibitionOrder = 5)
	public String getValorVendaFormatado() {
		return valorVendaFormatado;
	}
	
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
		if (precoCapa != null) {
			valorPrecoCapaFormatado = CurrencyUtil.formatarValor(precoCapa);
		}
	}
	
	@Export(label = "Preço Capa", alignment=Alignment.CENTER, exhibitionOrder = 7)
	public String getValorPrecoCapaFormatado() {
		return valorPrecoCapaFormatado;
	}	

	public BigDecimal getPercentagemVenda() {
		return percentagemVenda;
	}

	public void setPercentagemVenda(BigDecimal percentagemVenda) {
		this.percentagemVenda = percentagemVenda;
		if (percentagemVenda != null) {
			percentagemVendaFormatado = CurrencyUtil.formatarValor(percentagemVenda);
		}
	}
	
	@Export(label = "% Venda", alignment=Alignment.CENTER, exhibitionOrder = 6)
	public String getPercentagemVendaFormatado() {
		return percentagemVendaFormatado;
	}
	
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
		if (total != null) {
			valorTotalFormatado = CurrencyUtil.formatarValor(total);
		}
	}
	
	@Export(label = "Total", alignment=Alignment.CENTER, exhibitionOrder = 8)
	public String getValorTotal() {
		return valorTotalFormatado;
	}

	public BigDecimal getEncalhe() {
		return encalhe;
	}

	public void setEncalhe(BigDecimal encalhe) {
		this.encalhe = encalhe;
	}

	/**
	 * @return the chamadaCapa
	 */
	public String getChamadaCapa() {
		return chamadaCapa;
	}

	/**
	 * @param chamadaCapa the chamadaCapa to set
	 */
	public void setChamadaCapa(String chamadaCapa) {
		this.chamadaCapa = (chamadaCapa != null) ? chamadaCapa : "";
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public BigInteger getVenda() {
		return venda;
	}

	public void setVenda(BigInteger venda) {
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
		return total;
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

	public String getValorVendaFormatado() {
		return valorVendaFormatado;
	}

	public void setValorVendaFormatado(String valorVendaFormatado) {
		this.valorVendaFormatado = valorVendaFormatado;
	}

	public String getValorPrecoCapaFormatado() {
		return valorPrecoCapaFormatado;
	}

	public void setValorPrecoCapaFormatado(String valorPrecoCapaFormatado) {
		this.valorPrecoCapaFormatado = valorPrecoCapaFormatado;
	}

	public String getValorTotalFormatado() {
		return valorTotalFormatado;
	}

	public void setValorTotalFormatado(String valorTotalFormatado) {
		this.valorTotalFormatado = valorTotalFormatado;
	}

	public String getPercentagemVendaFormatado() {
		return percentagemVendaFormatado;
	}

	public void setPercentagemVendaFormatado(String percentagemVendaFormatado) {
		this.percentagemVendaFormatado = percentagemVendaFormatado;
	}

	public String getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(String dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

}
