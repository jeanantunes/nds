package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.dto.FornecedoresBandeiraDTO;

public class ImpressaoBandeiraVO implements Serializable  {

	
	
	
	private static final long serialVersionUID = -8669011482729203456L;
	
	

	private String 	nome;
	private Integer semana;
	private String 	codigoPracaNoProdin;
	private String 	praca;
	private String 	destino;
	private String 	canal;
	private String  volumes;
	
	
	
	public ImpressaoBandeiraVO(FornecedoresBandeiraDTO fornecedoresBandeiraDTO, String volumes) {
		super();
		this.nome = fornecedoresBandeiraDTO.getNome();
		this.semana =  fornecedoresBandeiraDTO.getSemana();
		this.codigoPracaNoProdin =  fornecedoresBandeiraDTO.getCodigoPracaNoProdin();
		this.praca = fornecedoresBandeiraDTO.getPraca();
		this.destino = "ENCALHE";
		if (fornecedoresBandeiraDTO.getNome() != null && fornecedoresBandeiraDTO.getNome().equalsIgnoreCase("FC")){
			this.canal = "VAREJO";
		} else {
			this.canal = "BANCA";
		}
		this.volumes = volumes;
			
	}
	

	public ImpressaoBandeiraVO(String nome, Integer semana,
			String codigoPracaNoProdin, String praca, String destino,
			String canal, String volumes) {
		super();
		this.nome = nome == null? "" : nome;
		this.semana = semana == null? 0 : semana;
		this.codigoPracaNoProdin = codigoPracaNoProdin == null? "" : codigoPracaNoProdin;
		this.praca = praca == null? "" : praca;
		this.destino = destino == null? "" : destino;
		this.canal = canal == null? "" : canal;
		this.volumes = volumes == null? "" : volumes;
	}


	
	
	public ImpressaoBandeiraVO(){
		
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getSemana() {
		return semana;
	}
	public void setSemana(Integer semana) {
		this.semana = semana;
	}
	public String getCodigoPracaNoProdin() {
		return codigoPracaNoProdin;
	}
	public void setCodigoPracaNoProdin(String codigoPracaNoProdin) {
		this.codigoPracaNoProdin = codigoPracaNoProdin;
	}
	public String getPraca() {
		return praca;
	}
	public void setPraca(String praca) {
		this.praca = praca;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getCanal() {
		return canal;
	}
	public void setCanal(String canal) {
		this.canal = canal;
	}



	public String getVolumes() {
		return volumes;
	}



	public void setVolumes(String volumes) {
		this.volumes = volumes;
	}

	
	
}
