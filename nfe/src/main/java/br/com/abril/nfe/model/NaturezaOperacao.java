package br.com.abril.nfe.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "NATUREZA_OPERACAO")
@SequenceGenerator(name = "NATUREZA_OPERACAO_SEQ", initialValue = 1, allocationSize = 1)
public class NaturezaOperacao {

	/**
	 * ID
	 */
	@Id
	@GeneratedValue(generator = "NATUREZA_OPERACAO_SEQ")
	private Long id;

	@Column(name = "NATUREZA_OPERACAO")
	private	String descricao;

	public Long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
