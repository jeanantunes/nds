package br.com.abril.nds.model.financeiro;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.TipoCobranca;


@Entity
@DiscriminatorValue(value = "DEPOSITO")
public class CobrancaDeposito extends Cobranca {
	
	public CobrancaDeposito() {
		this.tipoCobranca = TipoCobranca.DEPOSITO;
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