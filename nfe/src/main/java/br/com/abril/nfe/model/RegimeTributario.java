package br.com.abril.nfe.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="REGIME_TRIBUTARIO")
public class RegimeTributario {

	private Long codigo;
	
	private String descricao;
	
	private RegimeTributario(Long codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}

	
}
