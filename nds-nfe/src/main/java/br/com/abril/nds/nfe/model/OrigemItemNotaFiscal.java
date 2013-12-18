package br.com.abril.nds.nfe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.abril.nfe.enums.TipoAtividade;

public class OrigemItemNotaFiscal  implements Serializable {
	
	private static final long serialVersionUID = 4968512055654760321L;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_ATIVIDADE")
	private TipoAtividade tipoAtividade;

	public TipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividade tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}
	
	
	
}
