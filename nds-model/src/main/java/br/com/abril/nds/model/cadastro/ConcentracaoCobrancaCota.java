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
@Table(name = "CONCENTRACAO_COBRANCA_COTA")
@SequenceGenerator(name = "CONCENTRACAO_COBRANCA_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class ConcentracaoCobrancaCota implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2853089015516318425L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "CONCENTRACAO_COBRANCA_COTA_SEQ")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FORMA_COBRANCA_ID")
	private FormaCobranca formaCobranca;

	@Column(name = "DIA_SEMANA", nullable = false)
	private int codigoDiaSemana;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FormaCobranca getFormaCobranca() {
		return formaCobranca;
	}

	public void setFormaCobranca(FormaCobranca formaCobranca) {
		this.formaCobranca = formaCobranca;
	}

	public DiaSemana getDiaSemana() {
		return DiaSemana.getByCodigoDiaSemana(codigoDiaSemana);
	}
	
	public void setDiaSemana(DiaSemana diaSemana) {
		this.codigoDiaSemana = diaSemana.getCodigoDiaSemana();
	}
}
