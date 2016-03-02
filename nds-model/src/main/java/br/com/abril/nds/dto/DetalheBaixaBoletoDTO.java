package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.financeiro.StatusBaixa;

public class DetalheBaixaBoletoDTO implements Serializable {

	private static final long serialVersionUID = 5030190910098228087L;

	private Integer numeroCota;
	
	private String nomeCota;
	
	private String nomeBanco;
	
	private String numeroConta;
	
	private String nossoNumero;
	
	private BigDecimal valorBoleto;
	
	private BigDecimal valorPago;
	
	private BigDecimal valorDiferenca;
	
	private Date dataVencimento;
	
	private Date dataEmissao;
	
	private String motivoRejeitado;
	
	private String motivoDivergencia;
	
	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	/**
	 * @return the nomeBanco
	 */
	public String getNomeBanco() {
		return nomeBanco;
	}

	/**
	 * @param nomeBanco the nomeBanco to set
	 */
	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	/**
	 * @return the numeroConta
	 */
	public String getNumeroConta() {
		return numeroConta;
	}

	/**
	 * @param numeroConta the numeroConta to set
	 */
	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}

	/**
	 * @return the nossoNumero
	 */
	public String getNossoNumero() {
		return nossoNumero;
	}

	/**
	 * @param nossoNumero the nossoNumero to set
	 */
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	/**
	 * @return the valorBoleto
	 */
	public BigDecimal getValorBoleto() {
		return valorBoleto;
	}

	/**
	 * @param valorBoleto the valorBoleto to set
	 */
	public void setValorBoleto(BigDecimal valorBoleto) {
		this.valorBoleto = valorBoleto;
	}

	/**
	 * @return the valorPago
	 */
	public BigDecimal getValorPago() {
		return valorPago;
	}

	/**
	 * @param valorPago the valorPago to set
	 */
	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	/**
	 * @return the valorDiferenca
	 */
	public BigDecimal getValorDiferenca() {
		return valorDiferenca;
	}

	/**
	 * @param valorDiferenca the valorDiferenca to set
	 */
	public void setValorDiferenca(BigDecimal valorDiferenca) {
		this.valorDiferenca = valorDiferenca;
	}

	/**
	 * @return the dataVencimento
	 */
	public Date getDataVencimento() {
		return dataVencimento;
	}

	/**
	 * @param dataVencimento the dataVencimento to set
	 */
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	/**
	 * @return the motivoRejeitado
	 */
	public String getMotivoRejeitado() {
		return motivoRejeitado;
	}

	/**
	 * @param motivoRejeitado the motivoRejeitado to set
	 */
	public void setMotivoRejeitado(StatusBaixa motivoRejeitado) {
		this.motivoRejeitado = motivoRejeitado.getDescricao();
	}

	/**
	 * @return the motivoDivergencia
	 */
	public String getMotivoDivergencia() {
		return motivoDivergencia;
	}

	/**
	 * @param motivoDivergencia the motivoDivergencia to set
	 */
	public void setMotivoDivergencia(StatusBaixa motivoDivergencia) {
		this.motivoDivergencia = motivoDivergencia.getDescricao();
	}
	
}
