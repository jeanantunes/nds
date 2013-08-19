package br.com.abril.nds.client.vo;

import java.io.Serializable;
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
	
	private String statusLancamento;

	
	
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

	public String getStatusLancamento() {
		return statusLancamento;
	}

	public void setStatusLancamento(String statusLancamento) {
		this.statusLancamento = statusLancamento;
	}
	
}
