package br.com.abril.nds.model.financeiro;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "HISTORICO_ACUMULO_DIVIDA")
@SequenceGenerator(name = "HIST_ACUMULO_DIVIDA_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoAcumuloDivida {
	
	@Id
	@GeneratedValue(generator = "HIST_ACUMULO_DIVIDA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_INCLUSAO", nullable = false)
	private Date dataInclusao;
	
	@OneToOne
	@JoinColumn(name = "DIVIDA_ID", nullable = false)
	private Divida divida;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private StatusInadimplencia status;
	
	@ManyToOne
	@JoinColumn(name = "USUARIO_ID", nullable = false)
	private Usuario responsavel;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDataInclusao() {
		return dataInclusao;
	}
	
	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}
	
	public Divida getDivida() {
		return divida;
	}
	
	public void setDivida(Divida divida) {
		this.divida = divida;
	}
	
	public StatusInadimplencia getStatus() {
		return status;
	}
	
	public void setStatus(StatusInadimplencia status) {
		this.status = status;
	}
	
	public Usuario getResponsavel() {
		return responsavel;
	}
	
	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}	

}
