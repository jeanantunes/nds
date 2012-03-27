package br.com.abril.nds.model.financeiro;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.com.abril.nds.model.cadastro.TipoCobranca;

@Entity
@DiscriminatorValue(value = "CHEQUE")
public class CobrancaCheque extends Cobranca {
	
	public CobrancaCheque() {
		this.tipoCobranca = TipoCobranca.CHEQUE;
	}
	
}