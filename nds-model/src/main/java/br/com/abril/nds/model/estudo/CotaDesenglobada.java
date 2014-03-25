package br.com.abril.nds.model.estudo;

import java.util.List;

public class CotaDesenglobada {

	private Long id;
	private List<CotaEnglobada> cotasEnglobadas;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<CotaEnglobada> getCotasEnglobadas() {
		return cotasEnglobadas;
	}
	public void setCotasEnglobadas(List<CotaEnglobada> cotasEnglobadas) {
		this.cotasEnglobadas = cotasEnglobadas;
	}
	
}
