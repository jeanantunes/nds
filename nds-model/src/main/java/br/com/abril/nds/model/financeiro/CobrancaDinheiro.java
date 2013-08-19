package br.com.abril.nds.model.financeiro;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.com.abril.nds.model.cadastro.TipoCobranca;

@Entity
@DiscriminatorValue(value = "DINHEIRO")
public class CobrancaDinheiro extends Cobranca {
	
	public CobrancaDinheiro() {
		this.tipoCobranca = TipoCobranca.DINHEIRO;
	}
	
}