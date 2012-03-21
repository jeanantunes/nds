package br.com.abril.nds.model.movimentacao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CONTROLE_CONFERENCIA_ENCALHE")
@SequenceGenerator(name="CTRL_CONF_ENCALHE_SEQ", initialValue = 1, allocationSize = 1)
public class ControleConferenciaEncalhe {
	
	@Id
	@GeneratedValue(generator = "CTRL_CONF_ENCALHE_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "DATA", nullable = false)
	private Date data;
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private StatusOperacao status;
	
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
	
	public StatusOperacao getStatus() {
		return status;
	}
	
	public void setStatus(StatusOperacao status) {
		this.status = status;
	}
	

}
