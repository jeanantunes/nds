package br.com.abril.nds.dto.filtro;

@SuppressWarnings("serial")
public class FiltroConsultaFixacaoCotaDTO extends FiltroDTO{
 
	private String cota;
	private String nomeCota;
	
	public String getCota() {
		return cota;
	}
	public void setCota(String cota) {
		this.cota = cota;
	}
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	
	
	
}
