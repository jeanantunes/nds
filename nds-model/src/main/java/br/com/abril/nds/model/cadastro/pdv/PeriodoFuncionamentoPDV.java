package br.com.abril.nds.model.cadastro.pdv;

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


@Entity
@Table(name = "PERIODO_FUNCIONAMENTO_PDV")
@SequenceGenerator(name="PERIODO_FUNCIONAMENTO_PDV_SEQ", initialValue = 1, allocationSize = 1)
public class PeriodoFuncionamentoPDV implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8100864998462332482L;

	@Id
	@GeneratedValue(generator = "PERIODO_FUNCIONAMENTO_PDV_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "PDV_ID")
	private PDV pdv;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "FUNCIONAMENTO_PDV")
	private TipoPeriodoFuncionamentoPDV tipoPeriodoFuncionamentoPDV;
	
	@Temporal(TemporalType.TIME)
	@Column(name = "HORARIO_INICIO")
	private Date horarioInicio;
	
	@Temporal(TemporalType.TIME)
	@Column(name = "HORARIO_FIM")
	private Date horarioFim;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PDV getPdv() {
		return pdv;
	}

	public void setPdv(PDV pdv) {
		this.pdv = pdv;
	}

	public TipoPeriodoFuncionamentoPDV getTipoPeriodoFuncionamentoPDV() {
		return tipoPeriodoFuncionamentoPDV;
	}

	public void setTipoPeriodoFuncionamentoPDV(
			TipoPeriodoFuncionamentoPDV tipoPeriodoFuncionamentoPDV) {
		this.tipoPeriodoFuncionamentoPDV = tipoPeriodoFuncionamentoPDV;
	}

	public Date getHorarioInicio() {
		return horarioInicio;
	}

	public void setHorarioInicio(Date horarioInicio) {
		this.horarioInicio = horarioInicio;
	}

	public Date getHorarioFim() {
		return horarioFim;
	}

	public void setHorarioFim(Date horarioFim) {
		this.horarioFim = horarioFim;
	}

}
