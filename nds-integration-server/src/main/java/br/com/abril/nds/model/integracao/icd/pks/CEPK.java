package br.com.abril.nds.model.integracao.icd.pks;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CEPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "COD_DISTRIBUIDOR")
	private Long codigoDistribuidor;

	@Column(name = "NUM_CHAMADA_ENCALHE_CHEN")
	private Date numeroChamadaEncalhe;

	/**
	 * @return the codigoDistribuidor
	 */
	public Long getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	/**
	 * @param codigoDistribuidor the codigoDistribuidor to set
	 */
	public void setCodigoDistribuidor(Long codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	/**
	 * @return
	 */
	public Date getNumeroChamadaEncalhe() {
		return numeroChamadaEncalhe;
	}

	/**
	 * @param numeroChamadaEncalhe
	 */
	public void setNumeroChamadaEncalhe(Date numeroChamadaEncalhe) {
		this.numeroChamadaEncalhe = numeroChamadaEncalhe;
	}

}