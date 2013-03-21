package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.util.DateUtil;

public class EdicoesProdutosDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1296064141305343451L;
	
	private Long edicao;
	private PeriodicidadeProduto periodo;
	private BigInteger venda;
	private Date dtLancamento;
	private Date dtRecolhimento;
	private StatusLancamento status;
	private String capa;
	private String sel;
	private BigInteger reparte;
	private String nomeProduto;
	private String descricaoTipoClassificacao;
	private String codigoProduto;
	private String descricaoTipoSegmento;
	
	/* Campos para grid */
	private String dataLancamento;
	private String dataRecolhimento;
	
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	public PeriodicidadeProduto getPeriodo() {
		return periodo;
	}
	public void setPeriodo(PeriodicidadeProduto periodo) {
		this.periodo = periodo;
	}
	
	public BigInteger getVenda() {
		return venda;
	}
	public void setVenda(BigInteger venda) {
		this.venda = venda;
	}
	public Date getDtLancamento() {
		return dtLancamento;
	}
	public void setDtLancamento(Date dtLancamento) {
		this.dtLancamento = dtLancamento;
		this.dataLancamento  =DateUtil.formatarDataPTBR(dtLancamento);
	}
	public Date getDtRecolhimento() {
		return dtRecolhimento;
	}
	public void setDtRecolhimento(Date dtRecolhimento) {
		this.dtRecolhimento = dtRecolhimento;
		this.dataRecolhimento = DateUtil.formatarDataPTBR(dtRecolhimento);
	}
	public StatusLancamento getStatus() {
		return status;
	}
	public void setStatus(StatusLancamento status) {
		this.status = status;
	}
	public String getCapa() {
		return capa;
	}
	public void setCapa(String capa) {
		this.capa = capa;
	}
	public String getSel() {
		return sel;
	}
	public void setSel(String sel) {
		this.sel = sel;
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
	public BigInteger getReparte() {
		return reparte;
	}
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getDescricaoTipoClassificacao() {
		return descricaoTipoClassificacao;
	}
	public void setDescricaoTipoClassificacao(String descricaoTipoClassificacao) {
		this.descricaoTipoClassificacao = descricaoTipoClassificacao;
	}
	public String getDescricaoTipoSegmento() {
		return descricaoTipoSegmento;
	}
	public void setDescricaoTipoSegmento(String descricaoTipoSegmento) {
		this.descricaoTipoSegmento = descricaoTipoSegmento;
	}
	
	
	
	
}