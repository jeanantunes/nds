package br.com.abril.nds.model.fiscal;
import javax.persistence.Entity;

import br.com.abril.nds.model.cadastro.Fornecedor;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
public class NotaFiscalFornecedor extends NotaFiscal {

	public Fornecedor fornecedor;

	public NotaFiscalFornecedor(){

	}

}