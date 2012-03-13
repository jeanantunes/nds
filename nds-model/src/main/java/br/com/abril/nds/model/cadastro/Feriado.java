package br.com.abril.nds.model.cadastro;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Cadastro de Feriados
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "FERIADO")
@SequenceGenerator(name="FERIADO_SEQ", initialValue = 1, allocationSize = 1)
public class Feriado {

	@Id
	@GeneratedValue(generator = "FERIADO_SEQ")
	@Column(name = "ID")
	private Long id;
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA", nullable = false, unique = true)
	private Date data;
	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}