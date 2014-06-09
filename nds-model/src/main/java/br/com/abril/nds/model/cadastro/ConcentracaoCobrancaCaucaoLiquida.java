package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.DiaSemana;

@Entity
@Table(name = "CONCENTRACAO_COBRANCA_CAUCAO_LIQUIDA")
@SequenceGenerator(name = "CONCENTRACAO_COBRANCA_CAUCAO_LIQUIDA_SEQ", initialValue = 1, allocationSize = 1)
public class ConcentracaoCobrancaCaucaoLiquida implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2853089015516318488L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "CONCENTRACAO_COBRANCA_CAUCAO_LIQUIDA_SEQ")
	private Long id;

	@Column(name = "DIA_SEMANA", nullable = false)
	private int codigoDiaSemana;
	
	@ManyToOne
	@JoinColumn(name = "FORMA_COBRANCA_CAUCAO_LIQUIDA_ID")
	private FormaCobrancaCaucaoLiquida formaCobrancaCaucaoLiquida;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DiaSemana getDiaSemana() {
		return DiaSemana.getByCodigoDiaSemana(codigoDiaSemana);
	}
	
	public void setDiaSemana(DiaSemana diaSemana) {
		this.codigoDiaSemana = diaSemana.getCodigoDiaSemana();
	}

	public int getCodigoDiaSemana() {
		return codigoDiaSemana;
	}

	public void setCodigoDiaSemana(int codigoDiaSemana) {
		this.codigoDiaSemana = codigoDiaSemana;
	}

	public FormaCobrancaCaucaoLiquida getFormaCobrancaCaucaoLiquida() {
		return formaCobrancaCaucaoLiquida;
	}

	public void setFormaCobrancaCaucaoLiquida(
			FormaCobrancaCaucaoLiquida formaCobrancaCaucaoLiquida) {
		this.formaCobrancaCaucaoLiquida = formaCobrancaCaucaoLiquida;
	}
	
	
}
