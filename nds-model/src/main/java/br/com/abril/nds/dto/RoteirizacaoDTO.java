package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class RoteirizacaoDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2456984121L;
	
	List<BoxRoteirizacaoDTO> listaBox;
	List<RoteiroRoteirizacaoDTO> listaRoteiros;
	List<RotaRoteirizacaoDTO> listaRotas;
	List<CotaRoteirizacaoDTO> listaCotas;
	
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
	public List<RoteiroRoteirizacaoDTO> getListaRoteiros() {
		return listaRoteiros;
	}
	/**
	 * @param listaRoteiros the listaRoteiros to set
	 */
	public void setListaRoteiros(List<RoteiroRoteirizacaoDTO> listaRoteiros) {
		this.listaRoteiros = listaRoteiros;
	}
	/**
	 * @return the listaRotas
	 */
	public List<RotaRoteirizacaoDTO> getListaRotas() {
		return listaRotas;
	}
	/**
	 * @param listaRotas the listaRotas to set
	 */
	public void setListaRotas(List<RotaRoteirizacaoDTO> listaRotas) {
		this.listaRotas = listaRotas;
	}
	/**
	 * @return the listaCotas
	 */
	public List<CotaRoteirizacaoDTO> getListaCotas() {
		return listaCotas;
	}
	/**
	 * @param listaCotas the listaCotas to set
	 */
	public void setListaCotas(List<CotaRoteirizacaoDTO> listaCotas) {
		this.listaCotas = listaCotas;
	}
	
}
