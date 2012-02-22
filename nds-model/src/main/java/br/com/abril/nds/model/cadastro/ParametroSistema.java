package br.com.abril.nds.model.cadastro;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "PARAMETRO_SISTEMA")
public class ParametroSistema {

	@Id
	private Long id;
	private String valor;
	@Enumerated(EnumType.STRING)
	private TipoParametroSistema tipoParametroSistema;
	
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
	
	public TipoParametroSistema getTipoParametroSistema() {
		return tipoParametroSistema;
	}
	
	public void setTipoParametroSistema(TipoParametroSistema tipoParametroSistema) {
		this.tipoParametroSistema = tipoParametroSistema;
	}

}