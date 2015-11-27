package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroRoteirizacao implements Serializable {

	private static final long serialVersionUID = -3783996689743491442L;
	
	private Long boxId;
	private Long roteiroId; 
	private Long rotaId;
	private Integer numeroCota;
	
	public Long getBoxId() {
		return boxId;
	}
	
	public void setBoxId(Long boxId) {
		this.boxId = boxId;
	}
	
	public Long getRoteiroId() {
		return roteiroId;
	}
	
	public void setRoteiroId(Long roteiroId) {
		this.roteiroId = roteiroId;
	}
	
	public Long getRotaId() {
		return rotaId;
	}
	
	public void setRotaId(Long rotaId) {
		this.rotaId = rotaId;
	}
	
	public Integer getNumeroCota() {
		return numeroCota;
	}
	
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
}