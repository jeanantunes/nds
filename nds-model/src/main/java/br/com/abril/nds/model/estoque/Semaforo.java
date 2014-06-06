package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.seguranca.Usuario;

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
	
	@Enumerated(EnumType.STRING)
	@Column(name="STATUS_PROCESSO_ENCALHE")
	private StatusProcessoEncalhe statusProcessoEncalhe;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_OPERACAO")
	private Date dataOperacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA_INICIO")
	private Date dataInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA_FIM")
	private Date dataFim;

	@Column(name="ERROR_LOG", nullable = true)
	private String errorLog;
	
	@ManyToOne
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public StatusProcessoEncalhe getStatusProcessoEncalhe() {
		return statusProcessoEncalhe;
	}

	public void setStatusProcessoEncalhe(StatusProcessoEncalhe statusProcessoEncalhe) {
		this.statusProcessoEncalhe = statusProcessoEncalhe;
	}

	public Date getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(Date dataAtualizacao) {
		this.dataOperacao = dataAtualizacao;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
