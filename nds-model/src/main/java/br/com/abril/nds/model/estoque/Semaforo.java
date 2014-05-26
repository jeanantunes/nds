package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SEMAFORO")
public class Semaforo implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="NUMERO_COTA")
	private Integer numeroCota;
	
	@Column(name="ATUALIZANDO")
	private Boolean atualizando;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_OPERACAO")
	private Date dataOperacao;
	
	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public Boolean getAtualizando() {
		return atualizando;
	}

	public void setAtualizando(Boolean atualizando) {
		this.atualizando = atualizando;
	}

	public Date getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}
	
	
	
}
