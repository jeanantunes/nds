package br.com.abril.nfe.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NATUREZA_OPERACAO")
public class NaturezaOperacaoNds {
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(name = "NATUREZA_OPERACAO")
	private	String descricao;

	@Column(name="CFOP_ESTADO")
	private String cfopEstado;

	@Column(name="CFOP_FORA_ESTADO")
	private String cfopForaEstado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCfopEstado() {
		return cfopEstado;
	}

	public void setCfopEstado(String cfopEstado) {
		this.cfopEstado = cfopEstado;
	}

	public String getCfopForaEstado() {
		return cfopForaEstado;
	}


	public void setCfopForaEstado(String cfopForaEstado) {
		this.cfopForaEstado = cfopForaEstado;
	}

	@Override
	public String toString() {
		return "NaturezaOperacao [id=" + id + ", descricao=" + descricao
				+ ", cfopForaEstado=" + cfopForaEstado + ", cfopEstado=" + cfopEstado + "]";
	}
	
}