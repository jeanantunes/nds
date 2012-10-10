package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ValidacaoControleDeAprovacaoFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = -118815395351420119L;
	
	private String tipoMovimento;
	
	private String descricaoTipoMovimento;

	public String getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(String tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	public String getDescricaoTipoMovimento() {
		return descricaoTipoMovimento;
	}

	public void setDescricaoTipoMovimento(String descricaoTipoMovimento) {
		this.descricaoTipoMovimento = descricaoTipoMovimento;
	}

}
