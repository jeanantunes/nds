<<<<<<< HEAD
package br.com.abril.nds.model.planejamento;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "HISTORICO_LANCAMENTO")
@SequenceGenerator(name = "HIST_LANC_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoLancamento implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 943959304324380002L;

	@Id
	@GeneratedValue(generator = "HIST_LANC_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "STATUS_ANTIGO", nullable = true)
	@Enumerated(EnumType.STRING)
	private StatusLancamento statusAntigo;
	
	@Column(name = "STATUS_NOVO", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusLancamento statusNovo;

	@ManyToOne(optional = false)
	@JoinColumn(name = "LANCAMENTO_ID")
	private Lancamento lancamento;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_EDICAO", nullable = false)
	private Date dataEdicao;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario responsavel;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_EDICAO", nullable = false)
	private TipoEdicao tipoEdicao;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the statusNovo
	 */
	public StatusLancamento getStatusNovo() {
		return statusNovo;
	}

	/**
	 * @param statusNovo the statusNovo to set
	 */
	public void setStatusNovo(StatusLancamento statusNovo) {
		this.statusNovo = statusNovo;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}
	
	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	/**
	 * @return the statusAntigo
	 */
	public StatusLancamento getStatusAntigo() {
		return statusAntigo;
	}

	/**
	 * @param statusAntigo the statusAntigo to set
	 */
	public void setStatusAntigo(StatusLancamento statusAntigo) {
		this.statusAntigo = statusAntigo;
	}

	/**
	 * @return the dataEdicao
	 */
	public Date getDataEdicao() {
		return dataEdicao;
	}

	/**
	 * @param dataEdicao the dataEdicao to set
	 */
	public void setDataEdicao(Date dataEdicao) {
		this.dataEdicao = dataEdicao;
	}

	/**
	 * @return the responsavel
	 */
	public Usuario getResponsavel() {
		return responsavel;
	}

	/**
	 * @param responsavel the responsavel to set
	 */
	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	/**
	 * @return the tipoEdicao
	 */
	public TipoEdicao getTipoEdicao() {
		return tipoEdicao;
	}

	/**
	 * @param tipoEdicao the tipoEdicao to set
	 */
	public void setTipoEdicao(TipoEdicao tipoEdicao) {
		this.tipoEdicao = tipoEdicao;
	}
	
}
=======
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
>>>>>>> fase2
