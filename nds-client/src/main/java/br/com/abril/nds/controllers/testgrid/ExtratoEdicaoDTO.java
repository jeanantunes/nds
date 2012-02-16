package br.com.abril.nds.controllers.testgrid;

import java.util.Date;

public class ExtratoEdicaoDTO {

	private int idExtratoEdicaoDTO;
	private Date data;
	private String movimento;
	private Long entrada;
	private String saida;
	private String principal;
	private String parcial;
	
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getIdExtratoEdicaoDTO() {
		return idExtratoEdicaoDTO;
	}

	public void setIdExtratoEdicaoDTO(int idExtratoEdicaoDTO) {
		this.idExtratoEdicaoDTO = idExtratoEdicaoDTO;
	}

	public String getMovimento() {
		return movimento;
	}

	public void setMovimento(String movimento) {
		this.movimento = movimento;
	}

	public Long getEntrada() {
		return entrada;
	}

	public void setEntrada(Long entrada) {
		this.entrada = entrada;
	}

	public String getSaida() {
		return saida;
	}

	public void setSaida(String saida) {
		this.saida = saida;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getParcial() {
		return parcial;
	}

	public void setParcial(String parcial) {
		this.parcial = parcial;
	}

	

	
}
