package br.com.abril.nds.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RepartePDVDTO implements Serializable{
	
	private Long id;
	private Integer reparte;
	private Long  codigoPdv;
	private String nomePDV;
	private String nomeCota;
	private Integer numeroCota;
	private String codigoProduto;
	private String nomeProduto;
	private String classificacaoProduto;
	private String telefone;
	private String endereco;
	
	
	
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getReparte() {
		return reparte;
	}
	public void setReparte(Integer reparte) {
		this.reparte = reparte;
	}
	public Long getCodigoPdv() {
		return codigoPdv;
	}
	public void setCodigoPdv(Long codigoPdv) {
		this.codigoPdv = codigoPdv;
	}
	public String getNomePDV() {
		return nomePDV;
	}
	public void setNomePDV(String nomePDV) {
		this.nomePDV = nomePDV;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public String getClassificacaoProduto() {
		return classificacaoProduto;
	}
	public void setClassificacaoProduto(String classificacaoProduto) {
		this.classificacaoProduto = classificacaoProduto;
	}

}

