package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ConferenciaEncalheDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6012294358522142934L;
	
	private Long idConferenciaEncalhe;
	
	private Long idProdutoEdicao;
	
	private BigDecimal qtdExemplar;
	
	private BigDecimal qtdRecebida;
	
	private String codigoDeBarras;
	
	private Date dataRecolhimento;
	
	private Integer codigoSM;
	
	private String codigo;
	
	private String nomeProduto;
	
	private Long numeroEdicao;
	
	private BigDecimal precoCapa;
	
	private BigDecimal desconto;
	
	private BigDecimal valorTotal;
	
	private Integer dia;
	
	private String observacao;
	
	private boolean juramentada;

	public Long getIdConferenciaEncalhe() {
		return idConferenciaEncalhe;
	}

	public void setIdConferenciaEncalhe(Long idConferenciaEncalhe) {
		this.idConferenciaEncalhe = idConferenciaEncalhe;
	}

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	public BigDecimal getQtdExemplar() {
		return qtdExemplar;
	}

	public void setQtdExemplar(BigDecimal qtdExemplar) {
		this.qtdExemplar = qtdExemplar;
	}

	public BigDecimal getQtdRecebida() {
		return qtdRecebida;
	}

	public void setQtdRecebida(BigDecimal qtdRecebida) {
		this.qtdRecebida = qtdRecebida;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	public Integer getCodigoSM() {
		return codigoSM;
	}

	public void setCodigoSM(Integer codigoSM) {
		this.codigoSM = codigoSM;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public boolean isJuramentada() {
		return juramentada;
	}

	public void setJuramentada(boolean juramentada) {
		this.juramentada = juramentada;
	}
}