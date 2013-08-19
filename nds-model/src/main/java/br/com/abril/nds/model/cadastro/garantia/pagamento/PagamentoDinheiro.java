package br.com.abril.nds.model.cadastro.garantia.pagamento;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="Dinheiro")
public class PagamentoDinheiro extends PagamentoCaucaoLiquida {

	private static final long serialVersionUID = -8855377619940586246L;

}
