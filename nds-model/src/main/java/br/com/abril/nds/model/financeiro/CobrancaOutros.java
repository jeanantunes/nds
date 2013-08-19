package br.com.abril.nds.model.financeiro;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.com.abril.nds.model.cadastro.TipoCobranca;

@Entity
@DiscriminatorValue(value = "OUTROS")
public class CobrancaOutros extends Cobranca {

	public CobrancaOutros(){
		
		this.tipoCobranca = TipoCobranca.OUTROS;
	}
}