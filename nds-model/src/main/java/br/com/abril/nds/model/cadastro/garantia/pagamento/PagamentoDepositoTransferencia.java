package br.com.abril.nds.model.cadastro.garantia.pagamento;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.cadastro.Banco;

@Entity
@DiscriminatorValue(value="Depósito/Transferência")
public class PagamentoDepositoTransferencia extends PagamentoCaucaoLiquida {

	private static final long serialVersionUID = -8409545056918197568L;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_ID")
	private Banco banco;

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}
}