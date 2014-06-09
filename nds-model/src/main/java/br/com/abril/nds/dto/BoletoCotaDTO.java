package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.util.TipoBaixaCobranca;

public class BoletoCotaDTO {

	private String nossoNumero;
	
	private Date dataEmissao;
	
	private Date dataVencimento;
	
	private Date dataPagamento;
	
	private BigDecimal encargos;
	
	private BigDecimal valor;
	
	private TipoBaixaCobranca tipoBaixa;
    
	private StatusCobranca statusCobranca;
	
	private StatusDivida statusDivida;
	
	private boolean boletoAntecipado;
	
	private boolean recebeCobrancaEmail;
	
	public BoletoCotaDTO(){
		
		super();
	}

	/**
	 * @param nossoNumero
	 * @param dataEmissao
	 * @param dataVencimento
	 * @param dataPagamento
	 * @param encargos
	 * @param valor
	 * @param tipoBaixa
	 * @param statusCobranca
	 * @param statusDivida
	 * @param boletoAntecipado
	 * @param recebeCobrancaEmail
	 */
	public BoletoCotaDTO(String nossoNumero, 
			             Date dataEmissao,
			             Date dataVencimento, 
			             Date dataPagamento, 
			             BigDecimal encargos,
			             BigDecimal valor, 
			             String tipoBaixa,
			             String statusCobranca, 
			             String statusDivida,
			             boolean boletoAntecipado, 
			             boolean recebeCobrancaEmail) {
		super();
		this.nossoNumero = nossoNumero;
		this.dataEmissao = dataEmissao;
		this.dataVencimento = dataVencimento;
		this.dataPagamento = dataPagamento;
		this.encargos = encargos;
		this.valor = valor;
		this.tipoBaixa = tipoBaixa!=null?TipoBaixaCobranca.valueOf(tipoBaixa):null;
		this.statusCobranca = statusCobranca!=null?StatusCobranca.valueOf(statusCobranca):null;
		this.statusDivida = statusDivida!=null?StatusDivida.valueOf(statusDivida):null;
		this.boletoAntecipado = boletoAntecipado;
		this.recebeCobrancaEmail = recebeCobrancaEmail;
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
	 * @return the dataEmissao
	 */
	public Date getDataEmissao() {
		return dataEmissao;
	}

	/**
	 * @param dataEmissao the dataEmissao to set
	 */
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
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

	/**
	 * @return the dataPagamento
	 */
	public Date getDataPagamento() {
		return dataPagamento;
	}

	/**
	 * @param dataPagamento the dataPagamento to set
	 */
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	/**
	 * @return the encargos
	 */
	public BigDecimal getEncargos() {
		return encargos;
	}

	/**
	 * @param encargos the encargos to set
	 */
	public void setEncargos(BigDecimal encargos) {
		this.encargos = encargos;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the tipoBaixa
	 */
	public TipoBaixaCobranca getTipoBaixa() {
		return tipoBaixa;
	}

	/**
	 * @param tipoBaixa the tipoBaixa to set
	 */
	public void setTipoBaixa(String tipoBaixa) {
		this.tipoBaixa = tipoBaixa!=null?TipoBaixaCobranca.valueOf(tipoBaixa):null;
	}

	/**
	 * @return the statusCobranca
	 */
	public StatusCobranca getStatusCobranca() {
		return statusCobranca;
	}
	
	/**
	 * @param statusCobranca the statusCobranca to set
	 */
	public void setStatusCobranca(String statusCobranca) {
		this.statusCobranca = statusCobranca!=null?StatusCobranca.valueOf(statusCobranca):null;
	}
	
	/**
	 * @return the statusDivida
	 */
	public StatusDivida getStatusDivida() {
		return statusDivida;
	}
	
	/**
	 * @param statusDivida the statusDivida to set
	 */
	public void setStatusDivida(String statusDivida) {
		this.statusDivida = statusDivida!=null?StatusDivida.valueOf(statusDivida):null;
	}

	/**
	 * @return the boletoAntecipado
	 */
	public boolean isBoletoAntecipado() {
		return boletoAntecipado;
	}

	/**
	 * @param boletoAntecipado the boletoAntecipado to set
	 */
	public void setBoletoAntecipado(boolean boletoAntecipado) {
		this.boletoAntecipado = boletoAntecipado;
	}

	/**
	 * @return the recebeCobrancaEmail
	 */
	public boolean isRecebeCobrancaEmail() {
		return recebeCobrancaEmail;
	}

	/**
	 * @param recebeCobrancaEmail the recebeCobrancaEmail to set
	 */
	public void setRecebeCobrancaEmail(boolean recebeCobrancaEmail) {
		this.recebeCobrancaEmail = recebeCobrancaEmail;
	}
}
