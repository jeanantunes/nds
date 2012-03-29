package br.com.abril.nds.dto;

import java.io.Serializable;

public class CotaSuspensaoDTO implements Serializable{

	private static final long serialVersionUID = 6095689901379670370L;
	
	private Long idCota;
	private Integer numCota;
	private String nome;
	private String vlrConsignado;
	private String vlrReparte;
	private String dividaAcumulada;
	private Long diasAberto;
	private Boolean selecionado;
	
	
	public CotaSuspensaoDTO(){
		
	}	
	
	public CotaSuspensaoDTO(Long idCota, Integer numCota, String nome, String vlrConsignado, String vlrReparte, String dividaAcumulada, Long diasAberto, Boolean selecionado) {
		super();
		this.idCota = idCota;
		this.numCota = numCota;
		this.nome = nome;
		this.vlrConsignado = vlrConsignado;
		this.vlrReparte = vlrReparte;
		this.dividaAcumulada = dividaAcumulada;
		this.diasAberto = diasAberto;
		this.selecionado = selecionado;
	}
		
	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}


	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getVlrConsignado() {
		return vlrConsignado;
	}

	public void setVlrConsignado(String vlrConsignado) {
		this.vlrConsignado = vlrConsignado;
	}

	public String getVlrReparte() {
		return vlrReparte;
	}

	public void setVlrReparte(String vlrReparte) {
		this.vlrReparte = vlrReparte;
	}

	public String getDividaAcumulada() {
		return dividaAcumulada;
	}

	public void setDividaAcumulada(String dividaAcumulada) {
		this.dividaAcumulada = dividaAcumulada;
	}

	public Long getDiasAberto() {
		return diasAberto;
	}

	public void setDiasAberto(Long diasAberto) {
		this.diasAberto = diasAberto;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Integer getNumCota() {
		return numCota;
	}

	public void setNumCota(Integer numCota) {
		this.numCota = numCota;
	}

	public enum Ordenacao{
		
		COTA("cota"),
		NOME("name"),
		VLR_CONSIGNADO("vlrConsignado"),
		VLR_REPARTE("vlrReparte");
		
		private String nomeColuna;
		
		private Ordenacao(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
		}
}
	
}
