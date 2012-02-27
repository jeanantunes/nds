package br.com.abril.nds.model.planejamento;

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
@Table(name = "HISTORICO_LANCAMENTO")
public class HistoricoLancamento {
	
	@Id
	@Column(name = "ID")
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA", nullable = false)
	private Date data;
	@ManyToOne(optional = false)
	private Usuario responsavel;
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusLancamento status;
	@ManyToOne(optional = false)
	@JoinColumn(name = "LANCAMENTO_ID")
	private Lancamento lancamento;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public Usuario getResponsavel() {
		return responsavel;
	}
	
	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}
	
	public StatusLancamento getStatus() {
		return status;
	}
	
	public void setStatus(StatusLancamento status) {
		this.status = status;
	}
	
	public Lancamento getLancamento() {
		return lancamento;
	}
	
	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}
	
	
}
