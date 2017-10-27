package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class CotaConsignadaCouchDTO implements Serializable{

	private static final long serialVersionUID = -3394108381909970449L;
	
	private String _id;
	
	private String _rev;
	
	private List<CotaConsignadaDetalheCouchDTO> cotaConsignadaDetalhes;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_rev() {
		return _rev;
	}

	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	public List<CotaConsignadaDetalheCouchDTO> getCotaConsignadaDetalhes() {
		return cotaConsignadaDetalhes;
	}

	public void setCotaConsignadaDetalhes(List<CotaConsignadaDetalheCouchDTO> cotaConsignadaDetalhes) {
		this.cotaConsignadaDetalhes = cotaConsignadaDetalhes;
	}

}
