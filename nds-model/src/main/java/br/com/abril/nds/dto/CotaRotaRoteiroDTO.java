package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * 
 * @author Diego Fernandes
 *
 */
@Exportable
public class CotaRotaRoteiroDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6933180219261652814L;
	
	@Export( label = "Cota")
	private Integer numeroCota;
	
	@Export( label = "Nome")
	private String nomeCota;
	
	@Export( label = "Rota")
	private String rota;
	
	@Export( label = "Roteiro")
	private String roteiro;
	
	
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	public String getRota() {
		return rota;
	}
	public void setRota(String rota) {
		this.rota = rota;
	}
	public String getRoteiro() {
		return roteiro;
	}
	public void setRoteiro(String roteiro) {
		this.roteiro = roteiro;
	}
	
}
