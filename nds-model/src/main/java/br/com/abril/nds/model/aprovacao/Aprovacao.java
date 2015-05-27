package br.com.abril.nds.model.aprovacao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.seguranca.Usuario;

@MappedSuperclass
public abstract class Aprovacao implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -225924432753902717L;

	@ManyToOne(fetch=FetchType.LAZY, optional = true)
	@JoinColumn(name = "APROVADOR_ID")
	private Usuario aprovador;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private StatusAprovacao status = StatusAprovacao.PENDENTE;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_APROVACAO")
	private Date dataAprovacao;
	
	@Column(name = "APROVADO_AUTOMATICAMENTE", nullable = true)
	private boolean aprovadoAutomaticamente;
	
	@Column(name = "MOTIVO", nullable = true)
	private String motivo;
	
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

	public boolean isAprovadoAutomaticamente() {
		return aprovadoAutomaticamente;
	}

	public void setAprovadoAutomaticamente(boolean aprovadoAutomaticamente) {
		this.aprovadoAutomaticamente = aprovadoAutomaticamente;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
}
