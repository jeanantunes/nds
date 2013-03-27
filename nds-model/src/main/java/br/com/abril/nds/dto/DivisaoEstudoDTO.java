package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

public class DivisaoEstudoDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8445940217521984235L;
    
    private Long numeroEstudoOriginal;
    private String codigoProduto;
    private String nomeProduto;
    private Long edicaoProduto;
    private Date dataDistribuicao;
    private Integer percentualDivisaoPrimeiroEstudo;
    private Integer percentualDivisaoSegundoEstudo;
    private Integer quantidadeReparte;
    private Long numeroPrimeiroEstudo;
    private Long repartePrimeiroEstudo;
    private Long numeroSegundoEstudo;
    private Long reparteSegundoEstudo;
    private Date dataLancamentoSegundoEstudo;

    public Long getNumeroEstudoOriginal() {
	return numeroEstudoOriginal;
    }

    public void setNumeroEstudoOriginal(Long numeroEstudoOriginal) {
	this.numeroEstudoOriginal = numeroEstudoOriginal;
    }

    public String getCodigoProduto() {
	return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
	this.codigoProduto = codigoProduto;
    }

    public String getNomeProduto() {
	return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
	this.nomeProduto = nomeProduto;
    }

    public Long getEdicaoProduto() {
	return edicaoProduto;
    }

    public void setEdicaoProduto(Long edicaoProduto) {
	this.edicaoProduto = edicaoProduto;
    }

    public Date getDataDistribuicao() {
	return dataDistribuicao;
    }

    public void setDataDistribuicao(Date dataDistribuicao) {
	this.dataDistribuicao = dataDistribuicao;
    }

    public Integer getPercentualDivisaoPrimeiroEstudo() {
	return percentualDivisaoPrimeiroEstudo;
    }

    public void setPercentualDivisaoPrimeiroEstudo(Integer percentualDivisaoPrimeiroEstudo) {
	this.percentualDivisaoPrimeiroEstudo = percentualDivisaoPrimeiroEstudo;
    }

    public Integer getPercentualDivisaoSegundoEstudo() {
	return percentualDivisaoSegundoEstudo;
    }

    public void setPercentualDivisaoSegundoEstudo(Integer percentualDivisaoSegundoEstudo) {
	this.percentualDivisaoSegundoEstudo = percentualDivisaoSegundoEstudo;
    }

    public Integer getQuantidadeReparte() {
	return quantidadeReparte;
    }

    public void setQuantidadeReparte(Integer quantidadeReparte) {
	this.quantidadeReparte = quantidadeReparte;
    }

    public Long getNumeroPrimeiroEstudo() {
	return numeroPrimeiroEstudo;
    }

    public void setNumeroPrimeiroEstudo(Long numeroPrimeiroEstudo) {
	this.numeroPrimeiroEstudo = numeroPrimeiroEstudo;
    }

    public Long getRepartePrimeiroEstudo() {
	return repartePrimeiroEstudo;
    }

    public void setRepartePrimeiroEstudo(Long repartePrimeiroEstudo) {
	this.repartePrimeiroEstudo = repartePrimeiroEstudo;
    }

    public Long getNumeroSegundoEstudo() {
	return numeroSegundoEstudo;
    }

    public void setNumeroSegundoEstudo(Long numeroSegundoEstudo) {
	this.numeroSegundoEstudo = numeroSegundoEstudo;
    }

    public Long getReparteSegundoEstudo() {
	return reparteSegundoEstudo;
    }

    public void setReparteSegundoEstudo(Long reparteSegundoEstudo) {
	this.reparteSegundoEstudo = reparteSegundoEstudo;
    }

    public Date getDataLancamentoSegundoEstudo() {
	return dataLancamentoSegundoEstudo;
    }

    public void setDataLancamentoSegundoEstudo(Date dataLancamentoSegundoEstudo) {
	this.dataLancamentoSegundoEstudo = dataLancamentoSegundoEstudo;
    }

}
