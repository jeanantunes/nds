package br.com.abril.nds.dto;

import java.io.Serializable;

public class BandeirasDTO  implements Serializable{

	private static final long serialVersionUID = -7847418746083619011L;
	
	private String codProduto;
	private String nomeProduto;
	private Long edProduto;
	private Integer pctPadrao;
	
	public BandeirasDTO() {
		
	}
	
	public BandeirasDTO(String codProduto, String nomeProduto,
			Long edProduto, Integer pctPadrao) {
		super();
		this.codProduto = codProduto;
		this.nomeProduto = nomeProduto;
		this.edProduto = edProduto;
		this.pctPadrao = pctPadrao;
	}

	public String getCodProduto() {
		return codProduto;
	}
	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public Long getedProduto() {
		return edProduto;
	}
	public void setedProduto(Long edProduto) {
		this.edProduto = edProduto;
	}
	public Integer getPctPadrao() {
		return pctPadrao;
	}
	public void setPctPadrao(Integer pctPadrao) {
		this.pctPadrao = pctPadrao;
	}	
}
