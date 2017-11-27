package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CotaConsignadaDetalheCouchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer codigoDistribuidor;
    private Integer codigoCota;
    private Integer codigoPDV;
    private String dataLancamento;
    private Integer codigoPublicacao;
    private Integer numeroEdicao;
    private String codigoBarras;
    private String nomePublicacao;
    private Integer quantidadeReparte;
    private String nomeEditora;
    private BigDecimal precoCapa;
    private BigDecimal precoCusto;
    private String chamadaCapa;
    private Integer codigoEditora;

    public Integer getCodigoDistribuidor() {
        return codigoDistribuidor;
    }

    public void setCodigoDistribuidor(Integer codigoDistribuidor) {
        this.codigoDistribuidor = codigoDistribuidor;
    }

    public Integer getCodigoCota() {
        return codigoCota;
    }

    public void setCodigoCota(Integer codigoCota) {
        this.codigoCota = codigoCota;
    }

    public Integer getCodigoPDV() {
        return codigoPDV;
    }

    public void setCodigoPDV(Integer codigoPDV) {
        this.codigoPDV = codigoPDV;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Integer getCodigoPublicacao() {
        return codigoPublicacao;
    }

    public void setCodigoPublicacao(Integer codigoPublicacao) {
        this.codigoPublicacao = codigoPublicacao;
    }

    public Integer getNumeroEdicao() {
        return numeroEdicao;
    }

    public void setNumeroEdicao(Integer numeroEdicao) {
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

    public Integer getQuantidadeReparte() {
        return quantidadeReparte;
    }

    public void setQuantidadeReparte(Integer quantidadeReparte) {
        this.quantidadeReparte = quantidadeReparte;
    }

    public String getNomeEditora() {
        return nomeEditora;
    }

    public void setNomeEditora(String nomeEditora) {
        this.nomeEditora = nomeEditora;
    }

    public BigDecimal getPrecoCapa() {
        return precoCapa;
    }

    public void setPrecoCapa(BigDecimal precoCapa) {
        this.precoCapa = precoCapa;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public String getChamadaCapa() {
        return chamadaCapa;
    }

    public void setChamadaCapa(String chamadaCapa) {
        this.chamadaCapa = chamadaCapa;
    }

    public Integer getCodigoEditora() {
        return codigoEditora;
    }

    public void setCodigoEditora(Integer codigoEditora) {
        this.codigoEditora = codigoEditora;
    }

    @Override
    public String toString() {
        return "CotaConsignadaDetalheCouchDTO{" +
                "codigoDistribuidor=" + codigoDistribuidor +
                ", codigoCota=" + codigoCota +
                ", codigoPDV=" + codigoPDV +
                ", dataLancamento='" + dataLancamento + '\'' +
                ", codigoPublicacao=" + codigoPublicacao +
                ", numeroEdicao=" + numeroEdicao +
                ", codigoBarras='" + codigoBarras + '\'' +
                ", nomePublicacao='" + nomePublicacao + '\'' +
                ", quantidadeReparte=" + quantidadeReparte +
                ", nomeEditora='" + nomeEditora + '\'' +
                ", precoCapa=" + precoCapa +
                ", precoCusto=" + precoCusto +
                ", chamadaCapa='" + chamadaCapa + '\'' +
                '}';
    }
}