package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroModalFinanceiro implements Serializable{
	private Integer idVencimento; 
	private String vrMinimo;
	private boolean isSugereSuspensao;
	private Integer qtdDividaEmAberto;
	private String vrDividaEmAberto;
	
	
	public Integer getIdVencimento() {
		return idVencimento;
	}
	public void setIdVencimento(Integer idVencimento) {
		this.idVencimento = idVencimento;
	}
	public String getVrMinimo() {
		return vrMinimo;
	}
	public void setVrMinimo(String vrMinimo) {
		this.vrMinimo = vrMinimo;
	}
	public boolean getIsSugereSuspensao() {
		return isSugereSuspensao;
	}
	public void setIsSugereSuspensao(boolean isSugereSuspensao) {
		this.isSugereSuspensao = isSugereSuspensao;
	}
	public Integer getQtdDividaEmAberto() {
		return qtdDividaEmAberto;
	}
	public void setQtdDividaEmAberto(Integer qtdDividaEmAberto) {
		this.qtdDividaEmAberto = qtdDividaEmAberto;
	}
	public String getVrDividaEmAberto() {
		return vrDividaEmAberto;
	}
	public void setVrDividaEmAberto(String vrDividaEmAberto) {
		this.vrDividaEmAberto = vrDividaEmAberto;
	}
	
}
