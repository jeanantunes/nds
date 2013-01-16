package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroCotaBaseDTO implements Serializable {

	private static final long serialVersionUID = 2433782322485644813L;
	
	private Integer numeroCota;	
	private String nomeCota;
	private String tipoPDV;
	private String bairro;
	private String cidade;
	private String geradorDeFluxo;
	private String areaInfluencia;
	

	public Integer getNumeroCota() {
		return numeroCota;
	}


	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}


	public String getNomeCota() {
		return nomeCota;
	}


	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}


	public String getTipoPDV() {
		return tipoPDV;
	}


	public void setTipoPDV(String tipoPDV) {
		this.tipoPDV = tipoPDV;
	}


	public String getBairro() {
		return bairro;
	}


	public void setBairro(String bairro) {
		this.bairro = bairro;
	}


	public String getCidade() {
		return cidade;
	}


	public void setCidade(String cidade) {
		this.cidade = cidade;
	}


	public String getGeradorDeFluxo() {
		return geradorDeFluxo;
	}


	public void setGeradorDeFluxo(String geradorDeFluxo) {
		this.geradorDeFluxo = geradorDeFluxo;
	}


	public String getAreaInfluencia() {
		return areaInfluencia;
	}


	public void setAreaInfluencia(String areaInfluencia) {
		this.areaInfluencia = areaInfluencia;
	}
	
}
