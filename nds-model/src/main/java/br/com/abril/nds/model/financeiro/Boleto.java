package br.com.abril.nds.model.financeiro;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.TipoCobranca;

/**
 * @author luiz.marcili
 * @version 1.0
 * @created 02-mar-2012 09:25:00
 */
@Entity
@DiscriminatorValue(value = "BOLETO")
public class Boleto extends Cobranca {
	
	public Boleto() {
		this.tipoCobranca = TipoCobranca.BOLETO;
	}
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "BANCO_ID")
	private Banco banco;
	
	public Banco getBanco() {
		return banco;
	}
	
	public void setBanco(Banco banco) {
		this.banco = banco;
	}
		
	
}