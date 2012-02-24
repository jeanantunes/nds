package br.com.abril.nds.model.fiscal;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.cadastro.Fornecedor;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@DiscriminatorValue(value = "FORNECEDOR")
public class NotaFiscalFornecedor extends NotaFiscal {

	@ManyToOne
	@JoinColumn(name = "FORNECEDOR_ID")
	private Fornecedor fornecedor;
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

}