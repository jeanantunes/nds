package br.com.abril.nds.dto.integracao.efacil;

import java.io.Serializable;

public class CotaConsignadaCorpo implements Serializable{


    private static final long serialVersionUID = -1776399731905267248L;
    private String codigoPublicacao;
    private String numeroEdicao;
    private String codigoBarras;
    private String nomePublicacao;
    private String quantidadeReparte;
    private String codigoEditora;
    private String nomeEditora;
    private String precoCapa;
    private String precoCusto;
    private String chamadaCapa;
    private String dataLancamento;
    private String dataPrimeiroLancamentoParcial;

    public String getCodigoPublicacao() {
        return codigoPublicacao;
    }

    public void setCodigoPublicacao(String codigoPublicacao) {
        this.codigoPublicacao = codigoPublicacao;
    }

    public String getNumeroEdicao() {
        return numeroEdicao;
    }

    public void setNumeroEdicao(String numeroEdicao) {
        this.numeroEdicao = numeroEdicao;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNomePublicacao() {
        return nomePublicacao;
    }

    public void setNomePublicacao(String nomePublicacao) {
        this.nomePublicacao = nomePublicacao;
    }

    public String getQuantidadeReparte() {
        return quantidadeReparte;
    }

    public void setQuantidadeReparte(String quantidadeReparte) {
        this.quantidadeReparte = quantidadeReparte;
    }

    public String getCodigoEditora() {
        return codigoEditora;
    }

    public void setCodigoEditora(String codigoEditora) {
        this.codigoEditora = codigoEditora;
    }

    public String getNomeEditora() {
        return nomeEditora;
    }

    public void setNomeEditora(String nomeEditora) {
        this.nomeEditora = nomeEditora;
    }

    public String getPrecoCapa() {
        return precoCapa;
    }

    public void setPrecoCapa(String precoCapa) {
        this.precoCapa = precoCapa;
    }

    public String getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(String precoCusto) {
        this.precoCusto = precoCusto;
    }

    public String getChamadaCapa() {
        return chamadaCapa;
    }

    public void setChamadaCapa(String chamadaCapa) {
        this.chamadaCapa = chamadaCapa;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getDataPrimeiroLancamentoParcial() {
        return dataPrimeiroLancamentoParcial;
    }

    public void setDataPrimeiroLancamentoParcial(String dataPrimeiroLancamentoParcial) {
        this.dataPrimeiroLancamentoParcial = dataPrimeiroLancamentoParcial;
    }
}
