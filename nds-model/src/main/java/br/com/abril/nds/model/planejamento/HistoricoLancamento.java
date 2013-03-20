package br.com.abril.nds.model.planejamento;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.HistoricoEdicao;

@Entity
@Table(name = "HISTORICO_LANCAMENTO")
@SequenceGenerator(name = "HIST_LANC_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoLancamento extends HistoricoEdicao implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 943959304324380002L;

	@Id
	@GeneratedValue(generator = "HIST_LANC_SEQ")
	@Column(name = "ID")
	private Long id;
	
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
