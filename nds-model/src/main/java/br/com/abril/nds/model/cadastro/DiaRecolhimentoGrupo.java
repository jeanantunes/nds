package br.com.abril.nds.model.cadastro;

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

import br.com.abril.nds.model.DiaSemana;

@Entity
@Table(name = "DIA_RECOLHIMENTO_GRUPO")
@SequenceGenerator(name="DIA_RECOLHIMENTO_GRUPO_SEQ", initialValue = 1, allocationSize = 1)
public class DiaRecolhimentoGrupo implements Serializable {

	private static final long serialVersionUID = 623253218002707208L;

	@Id
	@GeneratedValue(generator="DIA_RECOLHIMENTO_GRUPO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "DIA_SEMANA", nullable = false)
	private DiaSemana diaSemana;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="GRUPO_ID")
	private GrupoCota grupo;

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
	 * @return the diaSemana
	 */
	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	/**
	 * @param diaSemana the diaSemana to set
	 */
	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	/**
	 * @return the grupo
	 */
	public GrupoCota getGrupo() {
		return grupo;
	}

	/**
	 * @param grupo the grupo to set
	 */
	public void setGrupo(GrupoCota grupo) {
		this.grupo = grupo;
	}
	
	
}
