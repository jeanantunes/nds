package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class AddLoteRegiaoDTO implements Serializable {
	private static final long serialVersionUID = -2677928108381033792L;
	
	private Integer numeroCota;

	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
}
