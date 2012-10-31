package br.com.abril.nds.model.planejamento;

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

/**
 * Cadastro de período de lançamento parcial,
 * com a definição de um período de lançamento e 
 * recolhimento definido respeitando os limites
 * de data do lancamento parcial 
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "PERIODO_LANCAMENTO_PARCIAL")
@SequenceGenerator(name = "PERIODO_LANCAMENTO_PARCIAL_SEQ", initialValue = 1, allocationSize = 1)
public class PeriodoLancamentoParcial {
	
	@Id
	@GeneratedValue(generator = "PERIODO_LANCAMENTO_PARCIAL_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "LANCAMENTO_PARCIAL_ID")
	private LancamentoParcial lancamentoParcial;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private StatusLancamentoParcial status;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO", nullable = false)
	private TipoLancamentoParcial tipo;

	@OneToOne(optional = false)
	@JoinColumn(name = "LANCAMENTO_ID")
	private Lancamento lancamento;
	
	/** Número do */
	@Column(name = "NUMERO_PERIODO", nullable = false)
	private Integer numeroPeriodo;
	

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the lancamentoParcial
	 */
	public LancamentoParcial getLancamentoParcial() {
		return lancamentoParcial;
	}

	/**
	 * @param lancamentoParcial the lancamentoParcial to set
	 */
	public void setLancamentoParcial(LancamentoParcial lancamentoParcial) {
		this.lancamentoParcial = lancamentoParcial;
	}

	/**
	 * @return the status
	 */
	public StatusLancamentoParcial getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusLancamentoParcial status) {
		this.status = status;
	}

	/**
	 * @return the tipo
	 */
	public TipoLancamentoParcial getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(TipoLancamentoParcial tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the lancamento
	 */
	public Lancamento getLancamento() {
		return lancamento;
	}

	/**
	 * @param lancamento the lancamento to set
	 */
	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}
	
	public Integer getNumeroPeriodo() {
		return numeroPeriodo;
	}

	public void setNumeroPeriodo(Integer numeroPeriodo) {
		this.numeroPeriodo = numeroPeriodo;
	}
	
}
