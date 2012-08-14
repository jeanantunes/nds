package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
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
	
	@Export(label = "Suplementação", alignment=Alignment.CENTER)
	private String suplementacao;
	
	@Export(label = "Venda CE", alignment=Alignment.CENTER)
	private String vendaCE;
	
	@Export(label = "% Venda Acum.", alignment=Alignment.CENTER)
	private String percVendaAcumulada;
	
	@Export(label = "% Venda Acum.", alignment=Alignment.CENTER)
	private String reparteAcum;
		
	private Long idLancamento;
	
	private boolean geradoPorInterface;
	
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
	public void setReparte(BigInteger reparte) {
		if(reparte==null)
			this.reparte = "";
		else
			this.reparte = reparte.toString();
	}
	public String getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(BigInteger encalhe) {
		if(encalhe==null)
			this.encalhe = "";
		else
			this.encalhe = encalhe.toString();
	}
	public String getVendas() {
		return vendas;
	}
	public void setVendas(BigInteger vendas) {
		if(vendas==null)
			this.vendas = "";
		else
			this.vendas = vendas.toString();
	}
	public String getVendaAcumulada() {
		return vendaAcumulada;
	}
	public void setVendaAcumulada(BigInteger vendaAcumulada) {
		if(vendaAcumulada==null || vendas==null || vendas.isEmpty())
			this.vendaAcumulada = "";
		else 
			this.vendaAcumulada = vendaAcumulada.toString();
	}
	public String getPercVenda() {
		return percVenda;
	}
	public void setPercVenda(BigInteger percVenda) {
		if(percVenda==null)
			this.percVenda = "";
		else
			this.percVenda = percVenda.toString() + "%";
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
	/**
	 * @return the suplementacao
	 */
	public String getSuplementacao() {
		return suplementacao;
	}
	/**
	 * @param suplementacao the suplementacao to set
	 */
	public void setSuplementacao(BigInteger suplementacao) {
		if(suplementacao==null)
			this.suplementacao = "";
		else
			this.suplementacao = suplementacao.toString();
	}
	/**
	 * @return the vendaCE
	 */
	public String getVendaCE() {
		return vendaCE;
	}
	/**
	 * @param vendaCE the vendaCE to set
	 */
	public void setVendaCE(BigInteger vendaCE) {
		if(vendaCE==null)
			this.vendaCE = "";
		else
		this.vendaCE = vendaCE.toString();
	}
	/**
	 * @return the percVendaAcumulada
	 */
	public String getPercVendaAcumulada() {
		return percVendaAcumulada;
	}
	/**
	 * @param percVendaAcumulada the percVendaAcumulada to set
	 */
	public void setPercVendaAcumulada(BigInteger percVendaAcumulada) {
		if(percVendaAcumulada==null)
			this.percVendaAcumulada = "";
		else
		this.percVendaAcumulada = percVendaAcumulada.toString() + "%";
	}
	/**
	 * @return the reparteAcum
	 */
	public String getReparteAcum() {
		return reparteAcum;
	}
	/**
	 * @param reparteAcum the reparteAcum to set
	 */
	public void setReparteAcum(BigInteger reparteAcum) {
		if(reparteAcum==null)
			this.reparteAcum = "";
		else
			this.reparteAcum = reparteAcum.toString();
	}
	/**
	 * @return the geradoPorInterface
	 */
	public boolean isGeradoPorInterface() {
		return geradoPorInterface;
	}
	/**
	 * @param geradoPorInterface the geradoPorInterface to set
	 */
	public void setGeradoPorInterface(boolean geradoPorInterface) {
		this.geradoPorInterface = geradoPorInterface;
	}		
}
