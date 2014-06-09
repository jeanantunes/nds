package br.com.abril.nds.model.financeiro;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.com.abril.nds.model.cadastro.TipoCobranca;

@Entity
@DiscriminatorValue(value = "TRANSFERENCIA_BANCARIA")
public class CobrancaTransferenciaBancaria extends Cobranca {
	
	public CobrancaTransferenciaBancaria() {
		this.tipoCobranca = TipoCobranca.TRANSFERENCIA_BANCARIA;
	}
		
	
}