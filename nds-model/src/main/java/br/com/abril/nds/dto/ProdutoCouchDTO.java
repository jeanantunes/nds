package br.com.abril.nds.dto;

import java.util.Date;

public class ProdutoCouchDTO {
	
	private String codigoProduto;
	private String numeroEdicao;
	private String codigoBarrasProduto;
	private String nomeProduto;
	private String reparte;
	private String nomeEditora;
	private String precoCapa;
	private String precoCusto;
	private String chamadaCapa;
	private Date dataLancamento;
	private Date dataPrimeiroLancamentoParcial;
	private Date dataRecolhimento;
	
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getNumeroEdicao() {
		return numeroEdicao;
	}
	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	public String getCodigoBarrasProduto() {
		return codigoBarrasProduto;
	}
	public void setCodigoBarrasProduto(String codigoBarrasProduto) {
		this.codigoBarrasProduto = codigoBarrasProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public String getReparte() {
		return reparte;
	}
	public void setReparte(String reparte) {
		this.reparte = reparte;
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
	public Date getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	public Date getDataPrimeiroLancamentoParcial() {
		return dataPrimeiroLancamentoParcial;
	}
	public void setDataPrimeiroLancamentoParcial(Date dataPrimeiroLancamentoParcial) {
		this.dataPrimeiroLancamentoParcial = dataPrimeiroLancamentoParcial;
	}
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
	
	

}
