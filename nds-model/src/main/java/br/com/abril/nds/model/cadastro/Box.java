package br.com.abril.nds.model.cadastro;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "BOX")
public class Box {

	@Id
	private Long id;
	private String codigo;
	@Enumerated(EnumType.STRING)
	private TipoBox tipoBox;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public TipoBox getTipoBox() {
		return tipoBox;
	}
	
	public void setTipoBox(TipoBox tipoBox) {
		this.tipoBox = tipoBox;
	}


}