package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author Discover Technology
 *
 */
public class PeriodoLancamentosProdutoEdicaoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8430582342818903553L;

	private Date dataLancamentoDistribuidor;
	
	private Date dataLancamentoPrevista;
	
	private Date dataRecolhimentoDistribuidor;
	
	private Date dataRecolhimentoPrevista;
	
	private String status;

	private Integer numeroLancamento;
	
	private BigInteger reparte;
	
	
	public Date getDataLancamentoDistribuidor() {
		return dataLancamentoDistribuidor;
	}

	public void setDataLancamentoDistribuidor(Date dataLancamentoDistribuidor) {
		this.dataLancamentoDistribuidor = dataLancamentoDistribuidor;
	}

	public Date getDataLancamentoPrevista() {
		return dataLancamentoPrevista;
	}

	public void setDataLancamentoPrevista(Date dataLancamentoPrevista) {
		this.dataLancamentoPrevista = dataLancamentoPrevista;
	}

	public Date getDataRecolhimentoDistribuidor() {
		return dataRecolhimentoDistribuidor;
	}

	public void setDataRecolhimentoDistribuidor(Date dataRecolhimentoDistribuidor) {
		this.dataRecolhimentoDistribuidor = dataRecolhimentoDistribuidor;
	}
	
	public Date getDataRecolhimentoPrevista() {
		return dataRecolhimentoPrevista;
	}

	public void setDataRecolhimentoPrevista(Date dataRecolhimentoPrevista) {
		this.dataRecolhimentoPrevista = dataRecolhimentoPrevista;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the numeroLancamento
	 */
	public Integer getNumeroLancamento() {
		return numeroLancamento;
	}

	/**
	 * @param numeroLancamento the numeroLancamento to set
	 */
	public void setNumeroLancamento(Integer numeroLancamento) {
		this.numeroLancamento = numeroLancamento;
	}

	/**
	 * @return the reparte
	 */
	public BigInteger getReparte() {
		return reparte;
	}

	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	
}
