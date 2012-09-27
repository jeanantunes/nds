package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class RoteirizacaoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private List<BoxRoteirizacaoDTO> listaBox;
	
	private List<RoteiroRoteirizacaoDTO> listaRoteiro;
	
	private List<RotaRoteirizacaoDTO> listaRota;
	
	private List<PdvRoteirizacaoDTO> listaPdv;
	
	/**
	 * @return the listaBox
	 */
	public List<BoxRoteirizacaoDTO> getListaBox() {
		return listaBox;
	}
	
	/**
	 * @param listaBox the listaBox to set
	 */
	public void setListaBox(List<BoxRoteirizacaoDTO> listaBox) {
		this.listaBox = listaBox;
	}
	
	/**
	 * @return the listaRoteiros
	 */
	public List<RoteiroRoteirizacaoDTO> getListaRoteiro() {
		return listaRoteiro;
	}
	
	/**
	 * @param listaRoteiros the listaRoteiros to set
	 */
	public void setListaRoteiros(List<RoteiroRoteirizacaoDTO> listaRoteiro) {
		this.listaRoteiro = listaRoteiro;
	}
	
	/**
	 * @return the listaRotas
	 */
	public List<RotaRoteirizacaoDTO> getListaRota() {
		return listaRota;
	}
	
	/**
	 * @param listaRotas the listaRotas to set
	 */
	public void setListaRotas(List<RotaRoteirizacaoDTO> listaRota) {
		this.listaRota = listaRota;
	}
	
	/**
	 * @return the listaPdvs
	 */
	public List<PdvRoteirizacaoDTO> getListaPdv() {
		return listaPdv;
	}
	
	/**
	 * @param listaPdvs the listaPdvs to set
	 */
	public void setListaPdvs(List<PdvRoteirizacaoDTO> listaPdv) {
		this.listaPdv = listaPdv;
	}

}
