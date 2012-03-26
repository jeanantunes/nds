package br.com.abril.nds.model.financeiro;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.com.abril.nds.model.cadastro.TipoCobranca;

@Entity
@DiscriminatorValue(value = "CHEQUE")
public class Cheque extends Cobranca {
	
	public Cheque() {
		this.tipoCobranca = TipoCobranca.CHEQUE;
	}
	
}