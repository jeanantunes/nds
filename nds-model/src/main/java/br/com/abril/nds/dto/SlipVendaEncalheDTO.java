package br.com.abril.nds.dto;

import java.util.List;

public class SlipVendaEncalheDTO {

	//CABEÇALHO SLIP
	private String numeroCota;
	private String nomeCota;
	private String numeroBox;
	private String descricaoBox;
	private String data;
	private String hora;
	private String usuario;
	private String numeroSlip;
	
	//TOTALIZAÇÕES
	private String quantidadeTotalVista;
	private String valorTotalVista;
	private String quantidadeTotalPrazo;
	private String valorTotalPrazo;
	private String quantidadeTotalGeral;
	private String valorTotalGeral;
	
	private List<ItemSlipVendaEncalheDTO> listaItensSlip;
	
	/**
	 * @return the listaItensSlip
	 */
	public List<ItemSlipVendaEncalheDTO> getListaItensSlip() {
		return listaItensSlip;
	}

	/**
	 * @param listaItensSlip the listaItensSlip to set
	 */
	public void setListaItensSlip(List<ItemSlipVendaEncalheDTO> listaItensSlip) {
		this.listaItensSlip = listaItensSlip;
	}

	/**
	 * @return the numeroSlip
	 */
	public String getNumeroSlip() {
		return numeroSlip;
	}

	/**
	 * @param numeroSlip the numeroSlip to set
	 */
	public void setNumeroSlip(String numeroSlip) {
		this.numeroSlip = numeroSlip;
	}

	public String getNumeroCota() {
		return numeroCota;
	}
	
	public void setNumeroCota(String numeroCota) {
		this.numeroCota = numeroCota;
	}
	
	public String getNomeCota() {
		return nomeCota;
	}
	
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	
	public String getNumeroBox() {
		return numeroBox;
	}
	
	public void setNumeroBox(String numeroBox) {
		this.numeroBox = numeroBox;
	}
	
	public String getDescricaoBox() {
		return descricaoBox;
	}
	
	public void setDescricaoBox(String descricaoBox) {
		this.descricaoBox = descricaoBox;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getHora() {
		return hora;
	}
	
	public void setHora(String hora) {
		this.hora = hora;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getQuantidadeTotalVista() {
		return quantidadeTotalVista;
	}

	public void setQuantidadeTotalVista(String quantidadeTotalVista) {
		this.quantidadeTotalVista = quantidadeTotalVista;
	}

	public String getValorTotalVista() {
		return valorTotalVista;
	}

	public void setValorTotalVista(String valorTotalVista) {
		this.valorTotalVista = valorTotalVista;
	}

	public String getQuantidadeTotalPrazo() {
		return quantidadeTotalPrazo;
	}

	public void setQuantidadeTotalPrazo(String quantidadeTotalPrazo) {
		this.quantidadeTotalPrazo = quantidadeTotalPrazo;
	}

	public String getValorTotalPrazo() {
		return valorTotalPrazo;
	}

	public void setValorTotalPrazo(String valorTotalPrazo) {
		this.valorTotalPrazo = valorTotalPrazo;
	}

	public String getQuantidadeTotalGeral() {
		return quantidadeTotalGeral;
	}

	public void setQuantidadeTotalGeral(String quantidadeTotalGeral) {
		this.quantidadeTotalGeral = quantidadeTotalGeral;
	}

	public String getValorTotalGeral() {
		return valorTotalGeral;
	}

	public void setValorTotalGeral(String valorTotalGeral) {
		this.valorTotalGeral = valorTotalGeral;
	}
}
