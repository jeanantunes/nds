package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class PeriodoParcialDTO implements Serializable {

	private static final long serialVersionUID = 7240165519168307608L;
	
	private Long idProdutoEdicao;
	
	@Export(label = "Período", alignment=Alignment.CENTER)
	private String periodo;
	
	@Export(label = "Data Lancamento", alignment=Alignment.CENTER)
	private String dataLancamento;
	
	@Export(label = "Data Recolhimento", alignment=Alignment.CENTER)
	private String dataRecolhimento;
	
	@Export(label = "Reparte", alignment=Alignment.CENTER)
	private String reparte;
	
	@Export(label = "Encalhe", alignment=Alignment.CENTER)
	private String encalhe;
	
	@Export(label = "Vendas", alignment=Alignment.CENTER)
	private String vendas;
	
	@Export(label = "Venda Acumulada", alignment=Alignment.CENTER)
	private String vendaAcumulada;
	
	@Export(label = "% Venda", alignment=Alignment.CENTER)
	private String percVenda;
	
	private Long idLancamento;
	
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Long periodo) {
		this.periodo = periodo + "º";
	}
	public String getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = DateUtil.formatarData(dataLancamento, Constantes.DATE_PATTERN_PT_BR);
	}
	public String getDataRecolhimento() {
		return dataRecolhimento;
	}
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = DateUtil.formatarData(dataRecolhimento, Constantes.DATE_PATTERN_PT_BR);;
	}
	public String getReparte() {
		return reparte;
	}
	public void setReparte(BigDecimal reparte) {
		if(reparte==null)
			this.reparte = "";
		else
			this.reparte = reparte.toBigInteger().toString();
	}
	public String getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(BigDecimal encalhe) {
		if(encalhe==null)
			this.encalhe = "";
		else
			this.encalhe = encalhe.toBigInteger().toString();
	}
	public String getVendas() {
		return vendas;
	}
	public void setVendas(BigDecimal vendas) {
		if(vendas==null)
			this.vendas = "";
		else
			this.vendas = vendas.toBigInteger().toString();
	}
	public String getVendaAcumulada() {
		return vendaAcumulada;
	}
	public void setVendaAcumulada(BigDecimal vendaAcumulada) {
		if(vendaAcumulada==null || vendas==null || vendas.isEmpty())
			this.vendaAcumulada = "";
		else 
			this.vendaAcumulada = vendaAcumulada.toBigInteger().toString();
	}
	public String getPercVenda() {
		return percVenda;
	}
	public void setPercVenda(BigDecimal percVenda) {
		if(percVenda==null)
			this.percVenda = "";
		else
			this.percVenda = percVenda.toBigInteger().toString() + "%";
	}
	/**
	 * @return the idLancamento
	 */
	public Long getIdLancamento() {
		return idLancamento;
	}
	/**
	 * @param idLancamento the idLancamento to set
	 */
	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}
	/**
	 * @return the idProdutoEdicao
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}
	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}
	
		
}
