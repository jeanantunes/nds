package br.com.abril.nds.model.integracao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.enums.TipoParametroSistema;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "PARAMETRO_SISTEMA")
@SequenceGenerator(name="PARAM_SIS_SEQ", initialValue = 1, allocationSize = 1)
public class ParametroSistema {

	@Id
	@GeneratedValue(generator = "PARAM_SIS_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "VALOR", nullable = true)
	private String valor;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_PARAMETRO_SISTEMA", nullable = false)
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