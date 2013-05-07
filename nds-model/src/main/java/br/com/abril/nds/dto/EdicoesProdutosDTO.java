package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.util.DateUtil;

public class EdicoesProdutosDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1296064141305343451L;
	
	private BigInteger edicao;
	private String periodo;
	private BigDecimal venda;
	private Date dtLancamento;
	private Date dtRecolhimento;
	private String status;
	private String capa;
	private String sel;
	private BigDecimal reparte;
	private String nomeProduto;
	private String descricaoTipoClassificacao;
	private String codigoProduto;
	private String descricaoTipoSegmento;
	private Long produtoEdicaoId;
	
	/* Campos para grid */
	private String dataLancamento;
	private String dataRecolhimento;
	
	public BigInteger getEdicao() {
		return edicao;
	}
	public void setEdicao(BigInteger edicao) {
		this.edicao = edicao;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	public BigDecimal getVenda() {
		return venda;
	}
	public void setVenda(BigDecimal venda) {
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
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
	public BigDecimal getReparte() {
		return reparte;
	}
	public void setReparte(BigDecimal reparte) {
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
	public Long getProdutoEdicaoId() {
	    return produtoEdicaoId;
	}
	public void setProdutoEdicaoId(Long produtoEdicaoId) {
	    this.produtoEdicaoId = produtoEdicaoId;
	}	
}