package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AnaliseEstudoDetalhesDTO {

    private BigInteger numeroEdicao;
    private String dataLancamentoFormatada;
    private BigDecimal reparte;
    private BigDecimal venda;
    private BigDecimal encalhe;
    private Integer numeroPeriodo;
    private Long idProdutoEdicao;
    private boolean parcial;
    private Integer ordemExibicao;
    
    public BigInteger getNumeroEdicao() {
        return numeroEdicao;
    }
    public void setNumeroEdicao(BigInteger numeroEdicao) {
        this.numeroEdicao = numeroEdicao;
    }
    public Date getDataLancamento() {
        try {
	    return new Date(new SimpleDateFormat("dd/MM/yyyy").parse(dataLancamentoFormatada).getTime());
	} catch (ParseException e) {
	    return null;
	}
    }
    public void setDataLancamento(Date dataLancamentoFormatada) {
        this.dataLancamentoFormatada = new SimpleDateFormat("dd/MM/yyyy").format(dataLancamentoFormatada);
    }
    public String getDataLancamentoFormatada() {
	return dataLancamentoFormatada;
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
    public BigDecimal getEncalhe() {
        return encalhe;
    }
    public void setEncalhe(BigDecimal encalhe) {
        this.encalhe = encalhe;
    }

    public Integer getNumeroPeriodo() {
        return numeroPeriodo;
    }

    public void setNumeroPeriodo(Integer numeroPeriodo) {
        this.numeroPeriodo = numeroPeriodo;
    }

    public Long getIdProdutoEdicao() {
        return idProdutoEdicao;
    }

    public void setIdProdutoEdicao(Long idProdutoEdicao) {
        this.idProdutoEdicao = idProdutoEdicao;
    }

    public boolean isParcial() {
        return parcial;
    }

    public void setParcial(boolean parcial) {
        this.parcial = parcial;
    }

    public Integer getOrdemExibicao() {
        return ordemExibicao;
    }

    public void setOrdemExibicao(Integer ordemExibicao) {
        this.ordemExibicao = ordemExibicao;
    }
}
