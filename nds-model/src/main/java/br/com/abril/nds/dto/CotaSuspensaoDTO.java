package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class CotaSuspensaoDTO implements Serializable{

	private static final long serialVersionUID = 6095689901379670370L;
	
	private Long cota;
	private String nome;
	private Double vlrConsignado;
	private Double vlrReparte;
	private Boolean selecionado;
	private List<DividaDTO> dividas;	
	
	
	public CotaSuspensaoDTO(){
		
	}
	
	public CotaSuspensaoDTO(Long cota, String nome, Double vlrConsignado, Double vlrReparte, Boolean selecionado, List<DividaDTO> dividas) {
		super();
		this.cota = cota;
		this.nome = nome;
		this.vlrConsignado = vlrConsignado;
		this.vlrReparte = vlrReparte;
		this.selecionado = selecionado;
		this.dividas = dividas;
	}



	public Long getCota() {
		return cota;
	}



	public void setCota(Long cota) {
		this.cota = cota;
	}



	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public Double getVlrConsignado() {
		return vlrConsignado;
	}



	public void setVlrConsignado(Double vlrConsignado) {
		this.vlrConsignado = vlrConsignado;
	}



	public Double getVlrReparte() {
		return vlrReparte;
	}



	public void setVlrReparte(Double vlrReparte) {
		this.vlrReparte = vlrReparte;
	}



	public Boolean getSelecionado() {
		return selecionado;
	}



	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}



	public List<DividaDTO> getDividas() {
		return dividas;
	}



	public void setDividas(List<DividaDTO> dividas) {
		this.dividas = dividas;
	}
	
}
