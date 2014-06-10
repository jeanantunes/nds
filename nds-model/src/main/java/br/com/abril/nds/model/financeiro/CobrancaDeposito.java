package br.com.abril.nds.model.financeiro;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.com.abril.nds.model.cadastro.TipoCobranca;


@Entity
@DiscriminatorValue(value = "DEPOSITO")
public class CobrancaDeposito extends Cobranca {
	
	public CobrancaDeposito() {
		this.tipoCobranca = TipoCobranca.DEPOSITO;
	}
		
	
}