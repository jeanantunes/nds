package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;

public class ConferenciaEncalheDTO implements Serializable {

	private static final long serialVersionUID = -6012294358522142934L;
	
	private Long idConferenciaEncalhe;
	
	private Long idProdutoEdicao;
	
	private String chamadaCapa;
	
	private String nomeFornecedor;
	
	private String nomeEditor;
	
	private int pacotePadrao;
	
	private boolean possuiBrinde;
	
	/**
	 * Quantidade apontada na conferência de encalhe
	 */	
	private BigDecimal qtdExemplar;

	/**
	 *  Quantidade informada. Refere-se a qtd do item da nota fiscal de entrada da cota.
	 */
	private BigDecimal qtdInformada;
	
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

	/**
	 * Tipo de chamada de encalhe deste produtoEdicao
	 */
	private TipoChamadaEncalhe tipoChamadaEncalhe;

	
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

	public BigDecimal getQtdInformada() {
		return qtdInformada;
	}

	public void setQtdInformada(BigDecimal qtdInformada) {
		this.qtdInformada = qtdInformada;
	}

	public String getChamadaCapa() {
		return chamadaCapa;
	}

	public void setChamadaCapa(String chamadaCapa) {
		this.chamadaCapa = chamadaCapa;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public String getNomeEditor() {
		return nomeEditor;
	}

	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	public int getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(int pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	public boolean isPossuiBrinde() {
		return possuiBrinde;
	}

	public void setPossuiBrinde(boolean possuiBrinde) {
		this.possuiBrinde = possuiBrinde;
	}

	public TipoChamadaEncalhe getTipoChamadaEncalhe() {
		return tipoChamadaEncalhe;
	}

	public void setTipoChamadaEncalhe(TipoChamadaEncalhe tipoChamadaEncalhe) {
		this.tipoChamadaEncalhe = tipoChamadaEncalhe;
	}
	
}