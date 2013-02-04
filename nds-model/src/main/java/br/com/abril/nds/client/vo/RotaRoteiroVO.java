package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.TipoCobranca;

public class RotaRoteiroVO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	private Long box;
	private Long rota;
	private Long roteiro;
	private TipoCobranca tipoCobranca;
	
	
	
	/**
	 * @return the tipoCobranca
	 */
	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}
	/**
	 * @param tipoCobranca the tipoCobranca to set
	 */
	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}
	/**
	 * @return the box
	 */
	/**
	 * @return the box
	 */
	public Long getBox() {
		return box;
	}
	/**
	 * @param box the box to set
	 */
	public void setBox(Long box) {
		this.box = box;
	}
	/**
	 * @return the rota
	 */
	public Long getRota() {
		return rota;
	}
	/**
	 * @param rota the rota to set
	 */
	public void setRota(Long rota) {
		this.rota = rota;
	}
	/**
	 * @return the roteiro
	 */
	public Long getRoteiro() {
		return roteiro;
	}
	/**
	 * @param roteiro the roteiro to set
	 */
	public void setRoteiro(Long roteiro) {
		this.roteiro = roteiro;
	}
	
	
}
