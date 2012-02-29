package br.com.abril.nds.model.aprovacao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.seguranca.Usuario;

@MappedSuperclass
public abstract class Aprovacao {
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "APROVADOR_ID")
	private Usuario aprovador;
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private StatusAprovacao status = StatusAprovacao.PENDENTE;
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_APROVACAO")
	private Date dataAprovacao;
	@Column(name = "APROVACAO_AUTOMATICA", nullable = false)
	private boolean aprovacaoAutomatica;
	
	public Usuario getAprovador() {
		return aprovador;
	}
	
	public void setAprovador(Usuario aprovador) {
		this.aprovador = aprovador;
	}
	
	public StatusAprovacao getStatus() {
		return status;
	}
	
	public void setStatus(StatusAprovacao status) {
		this.status = status;
	}
	
	public Date getDataAprovacao() {
		return dataAprovacao;
	}
	
	public void setDataAprovacao(Date dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}
	
	public boolean isAprovacaoAutomatica() {
		return aprovacaoAutomatica;
	}
	
	public void setAprovacaoAutomatica(boolean aprovacaoAutomatica) {
		this.aprovacaoAutomatica = aprovacaoAutomatica;
	}

}
