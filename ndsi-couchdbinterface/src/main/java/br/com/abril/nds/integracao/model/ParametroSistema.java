package br.com.abril.nds.integracao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PARAMETRO_SISTEMA")
public class ParametroSistema {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "VALOR", length = 255, nullable = false)
	private String valor;
	
	@Column(name = "TIPO_PARAMETRO_SISTEMA", length = 50, nullable = false, unique = true)
	private String tipoParametroSistema;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getValor() {
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public String getTipoParametroSistema() {
		return tipoParametroSistema;
	}
	
	public void setTipoParametroSistema(String tipoParametroSistema) {
		this.tipoParametroSistema = tipoParametroSistema;
	}
}