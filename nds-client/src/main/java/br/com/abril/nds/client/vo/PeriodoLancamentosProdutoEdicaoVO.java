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

	private Date dataLancamentoPrevista;
	
	private Date dataRecolhimentoPrevista;

	public Date getDataLancamentoPrevista() {
		return dataLancamentoPrevista;
	}

	public void setDataLancamentoPrevista(Date dataLancamentoPrevista) {
		this.dataLancamentoPrevista = dataLancamentoPrevista;
	}

	public Date getDataRecolhimentoPrevista() {
		return dataRecolhimentoPrevista;
	}

	public void setDataRecolhimentoPrevista(Date dataRecolhimentoPrevista) {
		this.dataRecolhimentoPrevista = dataRecolhimentoPrevista;
	}
	
}
