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

	private Integer numeroPeriodo;
	
	private Integer numeroLancamento;
	
	private BigInteger reparte;
	
	private boolean destacarLinha;
	
    private Long idLancamento;
	

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
	 * @return the numeroPeriodo
	 */
	public Integer getNumeroPeriodo() {
		return numeroPeriodo;
	}

	/**
	 * @param numeroPeriodo the numeroPeriodo to set
	 */
	public void setNumeroPeriodo(Integer numeroPeriodo) {
		this.numeroPeriodo = numeroPeriodo;
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

	/**
	 * @return the destacarLinha
	 */
	public boolean isDestacarLinha() {
		return destacarLinha;
	}

	/**
	 * @param destacarLinha the destacarLinha to set
	 */
	public void setDestacarLinha(boolean destacarLinha) {
		this.destacarLinha = destacarLinha;
	}
    
    /**
     * @return the idLancamento
     */
    public Long getIdLancamento() {
        return idLancamento;
    }
    
    /**
     * @param idLancamento the idLancamento to set
     */
    public void setIdLancamento(Long idLancamento) {
        this.idLancamento = idLancamento;
    }
	
}
