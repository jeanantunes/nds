package br.com.abril.nds.model.cadastro;

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

import br.com.abril.nds.model.DiaSemana;

@Entity
@Table(name = "CONCENTRACAO_COBRANCA_COTA")
@SequenceGenerator(name = "CONCENTRACAO_COBRANCA_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class ConcentracaoCobrancaCota {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "CONCENTRACAO_COBRANCA_COTA_SEQ")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "PARAM_COBRANCA_COTA_ID")
	private ParametroCobrancaCota parametroCobrancaCota;

	@Column(name = "DIA_SEMANA")
	@Enumerated(EnumType.STRING)
	private DiaSemana diaSemana;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ParametroCobrancaCota getParametroCobrancaCota() {
		return parametroCobrancaCota;
	}

	public void setParametroCobrancaCota(
			ParametroCobrancaCota parametroCobrancaCota) {
		this.parametroCobrancaCota = parametroCobrancaCota;
	}

	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

}
