package br.com.abril.nds.model.financeiro;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.com.abril.nds.model.cadastro.TipoCobranca;

@Entity
@DiscriminatorValue(value = "DINHEIRO")
public class Dinheiro extends Cobranca {
	
	public Dinheiro() {
		this.tipoCobranca = TipoCobranca.DINHEIRO;
	}
	
}