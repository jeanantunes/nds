package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.DateUtil;

public class DivisaoEstudoDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8445940217521984235L;

    private Long numeroEstudoOriginal;
    private String codigoProduto;
    private String nomeProduto;
    private Long edicaoProduto;
    private String dataDistribuicao;
    private Integer percentualDivisaoPrimeiroEstudo;
    private Integer percentualDivisaoSegundoEstudo;
    private Integer quantidadeReparte;
    private Long numeroPrimeiroEstudo;
    private Long numeroSegundoEstudo;
    private BigInteger repartePrimeiroEstudo;
    private BigInteger reparteSegundoEstudo;
    private String dataLancamentoPrimeiroEstudo;
    private String dataLancamentoSegundoEstudo;
    private Date dataDistribuicaoParaPesquisa;

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

    public String getDataDistribuicao() {
	return dataDistribuicao;
    }

    public void setDataDistribuicao(String dataDistribuicao) {
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

    public BigInteger getRepartePrimeiroEstudo() {
	return repartePrimeiroEstudo;
    }

    public void setRepartePrimeiroEstudo(BigInteger repartePrimeiroEstudo) {
	this.repartePrimeiroEstudo = repartePrimeiroEstudo;
    }

    public Long getNumeroSegundoEstudo() {
	return numeroSegundoEstudo;
    }

    public void setNumeroSegundoEstudo(Long numeroSegundoEstudo) {
	this.numeroSegundoEstudo = numeroSegundoEstudo;
    }

    public BigInteger getReparteSegundoEstudo() {
	return reparteSegundoEstudo;
    }

    public void setReparteSegundoEstudo(BigInteger reparteSegundoEstudo) {
	this.reparteSegundoEstudo = reparteSegundoEstudo;
    }

    public String getDataLancamentoPrimeiroEstudo() {
	return dataLancamentoPrimeiroEstudo;
    }

    public void setDataLancamentoPrimeiroEstudo(String dataLancamentoPrimeiroEstudo) {
	this.dataLancamentoPrimeiroEstudo = dataLancamentoPrimeiroEstudo;
    }

    public String getDataLancamentoSegundoEstudo() {
	return dataLancamentoSegundoEstudo;
    }

    public void setDataLancamentoSegundoEstudo(String dataLancamentoSegundoEstudo) {
	this.dataLancamentoSegundoEstudo = dataLancamentoSegundoEstudo;
    }

	public Date getDataDistribuicaoParaPesquisa() {
		return dataDistribuicaoParaPesquisa;
	}

	public void setDataDistribuicaoParaPesquisa(String data) {
		this.dataDistribuicaoParaPesquisa = DateUtil.parseDataPTBR(data);
	}
}
