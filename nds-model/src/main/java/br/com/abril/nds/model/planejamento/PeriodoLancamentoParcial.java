package br.com.abril.nds.model.planejamento;

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
	
	@Column(name = "LANCAMENTO", nullable = false)
	private Date lancamento;
	
	@Column(name = "RECOLHIMENTO", nullable = false)
	private Date recolhimento;

	@ManyToOne(optional = false)
	@JoinColumn(name = "LANCAMENTO_PARCIAL_ID")
	private LancamentoParcial lancamentoParcial;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private StatusLancamentoParcial status;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO", nullable = false)
	private TipoLancamentoParcial tipo;

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
	 * @return the lancamento
	 */
	public Date getLancamento() {
		return lancamento;
	}

	/**
	 * @param lancamento the lancamento to set
	 */
	public void setLancamento(Date lancamento) {
		this.lancamento = lancamento;
	}

	/**
	 * @return the recolhimento
	 */
	public Date getRecolhimento() {
		return recolhimento;
	}

	/**
	 * @param recolhimento the recolhimento to set
	 */
	public void setRecolhimento(Date recolhimento) {
		this.recolhimento = recolhimento;
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
	
	
}
