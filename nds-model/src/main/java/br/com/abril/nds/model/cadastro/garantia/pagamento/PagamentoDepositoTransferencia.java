package br.com.abril.nds.model.cadastro.garantia.pagamento;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="Depósito/Transferência")
public class PagamentoDepositoTransferencia extends PagamentoCaucaoLiquida {

	private static final long serialVersionUID = -8409545056918197568L;

}
