package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class PeriodoParcialDTO implements Serializable {

	private static final long serialVersionUID = 7240165519168307608L;
	
	private Long idProdutoEdicao;
	
	@Export(label="Período",alignment=Alignment.CENTER,widthPercent=5)
	private Integer numeroPeriodo;
		
	@Export(label = "Data Lancamento", alignment=Alignment.CENTER,widthPercent=10)
	private String dataLancamento;
	
	@Export(label = "Data Recolhimento", alignment=Alignment.CENTER,widthPercent=10)
	private String dataRecolhimento;
	
	@Export(label = "Reparte", alignment=Alignment.CENTER)
	private BigInteger reparte = BigInteger.ZERO;
	
	@Export(label = "Encalhe", alignment=Alignment.CENTER)
	private BigInteger encalhe = BigInteger.ZERO;
	
	@Export(label = "Vendas", alignment=Alignment.CENTER)
	private BigInteger vendas = BigInteger.ZERO;
	
	@Export(label = "Venda Acumulada", alignment=Alignment.CENTER)
	private BigInteger vendaAcumulada = BigInteger.ZERO;
	
	@Export(label = "% Venda", alignment=Alignment.CENTER)
	private BigDecimal percVenda = BigDecimal.ZERO;
	
	@Export(label = "Suplementação", alignment=Alignment.CENTER)
	private BigInteger suplementacao = BigInteger.ZERO;
	
	@Export(label = "Venda CE", alignment=Alignment.CENTER)
	private Long vendaCE =0L;
	
	@Export(label = "Venda Acum.", alignment=Alignment.CENTER)
	private BigInteger reparteAcum =BigInteger.ZERO;
	
	@Export(label = "% Venda Acum.", alignment=Alignment.CENTER)
	private BigDecimal percVendaAcumulada = BigDecimal.ZERO;
	
	@Export(label = "Data Lancamento Prevista", alignment=Alignment.CENTER,widthPercent=10)
	private String dataLancamentoPrevista;
	
	@Export(label = "Data Recolhimento Prevista", alignment=Alignment.CENTER,widthPercent=10)
	private String dataRecolhimentoPrevista;
	
	private Long idLancamento;
	
	private String statusLancamento;
	
	private Long idPeriodo;
	
	private boolean geradoPorInterface;
	
	private Origem origem;
	
	public String getDataLancamentoPrevista() {
		return dataLancamentoPrevista;
	}
	public void setDataLancamentoPrevista(Date dataLancamentoPrevista) {
		this.dataLancamentoPrevista = DateUtil.formatarData(dataLancamentoPrevista, Constantes.DATE_PATTERN_PT_BR);
	}
	public String getDataRecolhimentoPrevista() {
		return dataRecolhimentoPrevista;
	}
	public void setDataRecolhimentoPrevista(Date dataRecolhimentoPrevista) {
		this.dataRecolhimentoPrevista = DateUtil.formatarData(dataRecolhimentoPrevista, Constantes.DATE_PATTERN_PT_BR);
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

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}
	public BigInteger getReparte() {
		return reparte;
	}
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	public BigInteger getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(BigInteger encalhe) {
		this.encalhe = encalhe;
	}
	public BigInteger getVendas() {
		return vendas;
	}
	public void setVendas(BigInteger vendas) {
		this.vendas = vendas;
	}
	public BigInteger getVendaAcumulada() {
		return vendaAcumulada;
	}
	public void setVendaAcumulada(BigInteger vendaAcumulada) {
		this.vendaAcumulada = vendaAcumulada;
	}
	public BigDecimal getPercVenda() {
		return percVenda;
	}
	public void setPercVenda(BigDecimal percVenda) {
		this.percVenda = percVenda;
	}
	public BigInteger getSuplementacao() {
		return suplementacao;
	}
	public void setSuplementacao(BigInteger suplementacao) {
		this.suplementacao = suplementacao;
	}
	public Long getVendaCE() {
		return vendaCE;
	}
	public void setVendaCE(Long vendaCE) {
		this.vendaCE = vendaCE;
	}
	public BigInteger getReparteAcum() {
		return reparteAcum;
	}
	public void setReparteAcum(BigInteger reparteAcum) {
		this.reparteAcum = reparteAcum;
	}
	public BigDecimal getPercVendaAcumulada() {
		return percVendaAcumulada;
	}
	public void setPercVendaAcumulada(BigDecimal percVendaAcumulada) {
		this.percVendaAcumulada = percVendaAcumulada;
	}
	public Long getIdLancamento() {
		return idLancamento;
	}
	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}
	public String getStatusLancamento() {
		return statusLancamento;
	}
	public void setStatusLancamento(String statusLancamento) {
		this.statusLancamento = statusLancamento;
	}
	public Long getIdPeriodo() {
		return idPeriodo;
	}
	public void setIdPeriodo(Long idPeriodo) {
		this.idPeriodo = idPeriodo;
	}
	public boolean isGeradoPorInterface() {
		return geradoPorInterface;
	}
	public void setGeradoPorInterface(boolean geradoPorInterface) {
		this.geradoPorInterface = geradoPorInterface;
	}
	/**
	 * @return the origem
	 */
	public Origem getOrigem() {
		return this.origem;
	}


	/**
	 * @param origem the origem to set
	 */
	public void setOrigem(String origem) {
		
		this.origem =  Origem.valueOf(origem);
		
		if(origem!=null && Origem.INTERFACE.equals(origem)) {
			this.geradoPorInterface = true;
		} else {
			this.geradoPorInterface = false;
		}
		
	}
	public Integer getNumeroPeriodo() {
		return numeroPeriodo;
	}
	public void setNumeroPeriodo(Integer numeroPeriodo) {
		this.numeroPeriodo = numeroPeriodo;
	}
	
	
}
