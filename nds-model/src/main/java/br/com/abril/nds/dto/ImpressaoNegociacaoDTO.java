package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ImpressaoNegociacaoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6806116191265581159L;

	private Integer numeroCota;
	
	private String nomeCota;
	
	private BigDecimal totalDividaSelecionada;
	
	private BigDecimal comissaoAtualCota;
	
	private BigDecimal comissaoParaPagamento;
	
	private BigDecimal comissaoCotaEnquantoHouverSaldo;
	
	private String frequenciaPagamento;
	
	private String numeroBanco;
	
	private String nomeBanco;
	
	private Long agenciaBanco;
	
	private Long contaBanco;
	
	private boolean recebePorEmail;
	
	private boolean negociacaoAvulsa;
	
	private boolean isentaEncargos;
	
	private List<ImpressaoNegociacaoParecelaDTO> parcelasCheques;

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public BigDecimal getTotalDividaSelecionada() {
		return totalDividaSelecionada;
	}

	public void setTotalDividaSelecionada(BigDecimal totalDividaSelecionada) {
		this.totalDividaSelecionada = totalDividaSelecionada;
	}

	public BigDecimal getComissaoAtualCota() {
		return comissaoAtualCota;
	}

	public void setComissaoAtualCota(BigDecimal comissaoAtualCota) {
		this.comissaoAtualCota = comissaoAtualCota;
	}

	public BigDecimal getComissaoParaPagamento() {
		return comissaoParaPagamento;
	}

	public void setComissaoParaPagamento(BigDecimal comissaoParaPagamento) {
		this.comissaoParaPagamento = comissaoParaPagamento;
	}

	public BigDecimal getComissaoCotaEnquantoHouverSaldo() {
		return comissaoCotaEnquantoHouverSaldo;
	}

	public void setComissaoCotaEnquantoHouverSaldo(
			BigDecimal comissaoCotaEnquantoHouverSaldo) {
		this.comissaoCotaEnquantoHouverSaldo = comissaoCotaEnquantoHouverSaldo;
	}

	public String getFrequenciaPagamento() {
		return frequenciaPagamento;
	}

	public void setFrequenciaPagamento(String frequenciaPagamento) {
		this.frequenciaPagamento = frequenciaPagamento;
	}

	public String getNumeroBanco() {
		return numeroBanco;
	}

	public void setNumeroBanco(String numeroBanco) {
		this.numeroBanco = numeroBanco;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public Long getAgenciaBanco() {
		return agenciaBanco;
	}

	public void setAgenciaBanco(Long agenciaBanco) {
		this.agenciaBanco = agenciaBanco;
	}

	public Long getContaBanco() {
		return contaBanco;
	}

	public void setContaBanco(Long contaBanco) {
		this.contaBanco = contaBanco;
	}

	public boolean getRecebePorEmail() {
		return recebePorEmail;
	}

	public void setRecebePorEmail(boolean recebePorEmail) {
		this.recebePorEmail = recebePorEmail;
	}

	public boolean getNegociacaoAvulsa() {
		return negociacaoAvulsa;
	}

	public void setNegociacaoAvulsa(boolean negociacaoAvulsa) {
		this.negociacaoAvulsa = negociacaoAvulsa;
	}

	public boolean getIsentaEncargos() {
		return isentaEncargos;
	}

	public void setIsentaEncargos(boolean isentaEncargos) {
		this.isentaEncargos = isentaEncargos;
	}

	public List<ImpressaoNegociacaoParecelaDTO> getParcelasCheques() {
		return parcelasCheques;
	}

	public void setParcelasCheques(
			List<ImpressaoNegociacaoParecelaDTO> parcelasCheques) {
		this.parcelasCheques = parcelasCheques;
	}
}