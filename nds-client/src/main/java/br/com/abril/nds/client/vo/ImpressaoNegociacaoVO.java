package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ImpressaoNegociacaoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6806116191265581159L;

	@Export(label = "Cota")
	private Integer numeroCota;
	
	@Export(label = "Nome")
	private String nomeCota;
	
	@Export(label = "Dívida Selecionada")
	private BigDecimal totalDividaSelecionada;
	
	@Export(label = "Forma de Pagamento")
	private String formaPagamento;
	
	@Export(label = "Comissão da Cota Atual")
	private BigDecimal comissaoAtualCota;
	
	@Export(label = "Comissão da Cota para pagamento da dívida")
	private BigDecimal comissaoParaPagamento;
	
	@Export(label = "Comissão da Cota enquanto houver saldo de dívida")
	private BigDecimal comissaoCotaEnquantoHouverSaldo;
	
	@Export(label = "Pagamento em")
	private Integer quantidadeParcelas;
	
	@Export(label = "Tipo de Pagamento")
	private String tipoPagamento;
	
	@Export(label = "Frequência Pagamento")
	private String frequenciaPagamento;
	
	@Export(label = "Num. Banco")
	private String numeroBanco;
	
	@Export(label = "Nome")
	private String nomeBanco;
	
	@Export(label = "Agência")
	private Long agenciaBanco;
	
	@Export(label = "Conta")
	private Long contaBanco;
	
	@Export(label = "Recebe por E-mail")
	private String recebePorEmail;
	
	@Export(label = "Negociação Avlusa")
	private String negociacaoAvulsa;
	
	@Export(label = "Isenta Encargos")
	private String isentaEncargos;

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

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
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

	public Integer getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public String getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
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

	public String getRecebePorEmail() {
		return recebePorEmail;
	}

	public void setRecebePorEmail(String recebePorEmail) {
		this.recebePorEmail = recebePorEmail;
	}

	public String getNegociacaoAvulsa() {
		return negociacaoAvulsa;
	}

	public void setNegociacaoAvulsa(String negociacaoAvulsa) {
		this.negociacaoAvulsa = negociacaoAvulsa;
	}

	public String getIsentaEncargos() {
		return isentaEncargos;
	}

	public void setIsentaEncargos(String isentaEncargos) {
		this.isentaEncargos = isentaEncargos;
	}
}