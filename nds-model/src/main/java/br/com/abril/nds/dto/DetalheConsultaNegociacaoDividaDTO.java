package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.client.vo.CalculaParcelasVO;

public class DetalheConsultaNegociacaoDividaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String tipoNegociacao;
	private Integer qtdParcelas;
	private String tipoDePagamento;
	private String tipoFormaCobranca;
	private boolean negAvulsa;
	
	private String dataVencimento;
	private BigDecimal parcela;
	private BigDecimal valorEncargos;
	private BigDecimal parcelaTotal;
	private Integer ativaAoPagar;
	
	private String nomeBanco;
	private boolean isIsentaEncargos;
	private boolean isReceberPorEmail;
	
	private BigDecimal comissaoParaSaldoDivida;
		
	private List<CalculaParcelasVO> listParcelas;

	public String getTipoNegociacao() {
		return tipoNegociacao;
	}

	public void setTipoNegociacao(String tipoNegociacao) {
		this.tipoNegociacao = tipoNegociacao;
	}

	public Integer getQtdParcelas() {
		return qtdParcelas;
	}

	public void setQtdParcelas(Integer qtdParcelas) {
		this.qtdParcelas = qtdParcelas;
	}

	public String getTipoDePagamento() {
		return tipoDePagamento;
	}

	public void setTipoDePagamento(String tipoDePagamento) {
		this.tipoDePagamento = tipoDePagamento;
	}

	public String getTipoFormaCobranca() {
		return tipoFormaCobranca;
	}

	public void setTipoFormaCobranca(String tipoFormaCobranca) {
		this.tipoFormaCobranca = tipoFormaCobranca;
	}

	public boolean isNegAvulsa() {
		return negAvulsa;
	}

	public void setNegAvulsa(boolean negAvulsa) {
		this.negAvulsa = negAvulsa;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public BigDecimal getParcela() {
		return parcela;
	}

	public void setParcela(BigDecimal parcela) {
		this.parcela = parcela;
	}

	public BigDecimal getValorEncargos() {
		return valorEncargos;
	}

	public void setValorEncargos(BigDecimal valorEncargos) {
		this.valorEncargos = valorEncargos;
	}

	public BigDecimal getParcelaTotal() {
		return parcelaTotal;
	}

	public void setParcelaTotal(BigDecimal parcelaTotal) {
		this.parcelaTotal = parcelaTotal;
	}

	public Integer getAtivaAoPagar() {
		return ativaAoPagar;
	}

	public void setAtivaAoPagar(Integer ativaAoPagar) {
		this.ativaAoPagar = ativaAoPagar;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public boolean isIsentaEncargos() {
		return isIsentaEncargos;
	}

	public void setIsentaEncargos(boolean isIsentaEncargos) {
		this.isIsentaEncargos = isIsentaEncargos;
	}

	public boolean isReceberPorEmail() {
		return isReceberPorEmail;
	}

	public void setReceberPorEmail(boolean isReceberPorEmail) {
		this.isReceberPorEmail = isReceberPorEmail;
	}

	public List<CalculaParcelasVO> getListParcelas() {
		return listParcelas;
	}

	public void setListParcelas(List<CalculaParcelasVO> listParcelas) {
		this.listParcelas = listParcelas;
	}

	public BigDecimal getComissaoParaSaldoDivida() {
		return comissaoParaSaldoDivida;
	}

	public void setComissaoParaSaldoDivida(BigDecimal comissaoParaSaldoDivida) {
		this.comissaoParaSaldoDivida = comissaoParaSaldoDivida;
	}
	
}
