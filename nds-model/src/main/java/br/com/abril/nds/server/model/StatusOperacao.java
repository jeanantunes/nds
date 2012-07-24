package br.com.abril.nds.server.model;

import java.io.Serializable;
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
@Table(name = "STATUS_OPERACAO")
@SequenceGenerator(name="STATUS_OPERACAO_SEQ", initialValue = 1, allocationSize = 1)
public class StatusOperacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2189955082200222794L;

	@Id
	@GeneratedValue(generator = "STATUS_OPERACAO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "DATA")
	private Date data;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private Status status;

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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}