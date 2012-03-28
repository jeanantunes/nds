package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ROTEIRO")
@SequenceGenerator(name="ROTEIRO_SEQ", initialValue = 1, allocationSize = 1)
public class Roteiro {
	
	@Id
	@GeneratedValue(generator = "ROTEIRO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name="DESCRICAO_ROTEIRO")
	private String descricaoRoteiro;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getDescricaoRoteiro() {
		return descricaoRoteiro;
	}


	public void setDescricaoRoteiro(String descricaoRoteiro) {
		this.descricaoRoteiro = descricaoRoteiro;
	}
	
	
}
