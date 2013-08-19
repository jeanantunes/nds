package br.com.abril.nds.dto;

import java.io.Serializable;

public class RotaQtdeDTO implements Serializable{

	private static final long serialVersionUID = -1688840147110250174L;
	
	private String codigo;
	private Integer qtde;
	
	public RotaQtdeDTO(String codigo, Integer qtde) {
		this.setCodigo(codigo);
		this.setQtde(qtde);
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Integer getQtde() {
		return qtde;
	}

	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	
}
