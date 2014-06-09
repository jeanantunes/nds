package br.com.abril.nds.model.financeiro;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.com.abril.nds.model.cadastro.TipoCobranca;

@Entity
@DiscriminatorValue(value = "BOLETO_EM_BRANCO")
public class CobrancaBoletoEmBranco extends Cobranca {

	public CobrancaBoletoEmBranco(){
		
		this.tipoCobranca = TipoCobranca.BOLETO_EM_BRANCO;
	}
}