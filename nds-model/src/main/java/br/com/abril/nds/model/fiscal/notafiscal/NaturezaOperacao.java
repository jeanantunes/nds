package br.com.abril.nds.model.fiscal.notafiscal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Embeddable
public class NaturezaOperacao {

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(name = "NATUREZA_OPERACAO")
	private	String descricao;

	@Column(name="CFOP_ESTADO")
	private String cfopEstado;

	@Column(name="CFOP_OUTROS_ESTADO")
	private String cfopOutrosEstado;

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
		return cfopOutrosEstado;
	}


	public void setCfopForaEstado(String cfopForaEstado) {
		this.cfopOutrosEstado = cfopForaEstado;
	}

	public String getCfopOutrosEstado() {
		return cfopOutrosEstado;
	}

	public void setCfopOutrosEstado(String cfopOutrosEstado) {
		this.cfopOutrosEstado = cfopOutrosEstado;
	}
	
	@Override
	public String toString() {
		return "NaturezaOperacao [id=" + id + ", descricao=" + descricao
				+ ", cfopForaEstado=" + cfopOutrosEstado + ", cfopEstado=" + cfopEstado + "]";
	}
	
}