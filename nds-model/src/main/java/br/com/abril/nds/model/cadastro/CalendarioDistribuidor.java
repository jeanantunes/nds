package br.com.abril.nds.model.cadastro;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "CALENDARIO_DISTRIBUIDOR")
@SequenceGenerator(name="CAL_DISTRIB_SEQ", initialValue = 1, allocationSize = 1)
public class CalendarioDistribuidor {

	@Id
	@GeneratedValue(generator = "CAL_DISTRIB_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "DATA", nullable = false)
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