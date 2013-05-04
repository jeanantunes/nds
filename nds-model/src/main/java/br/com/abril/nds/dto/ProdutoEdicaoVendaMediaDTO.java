package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProdutoEdicaoVendaMediaDTO implements Serializable {

    private static final long serialVersionUID = -2108084689144884556L;

    private BigInteger id;
    private BigInteger numeroEdicao;
    private String codigoProduto;
    private String nome;
    private BigInteger periodo;
    private String dataLancamentoFormatada;
    private BigDecimal reparte;
    private BigDecimal venda;
    private BigDecimal percentualVenda;
    private String status;
    private String classificacao;
    
    public BigInteger getId() {
        return id;
    }
    public void setId(BigInteger id) {
        this.id = id;
    }
    public BigInteger getNumeroEdicao() {
        return numeroEdicao;
    }
    public void setNumeroEdicao(BigInteger numeroEdicao) {
        this.numeroEdicao = numeroEdicao;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public BigInteger getPeriodo() {
        return periodo;
    }
    public void setPeriodo(BigInteger periodo) {
        this.periodo = periodo;
    }
    public Date getDataLancamento() {
	try {
	    return new SimpleDateFormat("dd/MM/yyyy").parse(dataLancamentoFormatada);
	} catch (ParseException e) {
	    return null;
	}
    }
    public String getDataLancamentoFormatada() {
	return dataLancamentoFormatada;
    }
    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamentoFormatada = new SimpleDateFormat("dd/MM/yyyy").format(dataLancamento);
    }
    public void setDataLancamentoFormatada(String dataLancamentoFormatada) {
	this.dataLancamentoFormatada = dataLancamentoFormatada;
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getClassificacao() {
        return classificacao;
    }
    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }
    public String getCodigoProduto() {
        return codigoProduto;
    }
    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }
    public BigDecimal getPercentualVenda() {
	return percentualVenda;
    }
    public void setPercentualVenda(BigDecimal percentualVenda) {
	this.percentualVenda = percentualVenda;
    }
}
