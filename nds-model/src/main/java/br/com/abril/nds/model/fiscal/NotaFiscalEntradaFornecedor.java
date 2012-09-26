package br.com.abril.nds.model.fiscal;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.cadastro.Fornecedor;

@Entity
@DiscriminatorValue(value = "FORNECEDOR")
public class NotaFiscalEntradaFornecedor extends NotaFiscalEntrada {

	private static final long serialVersionUID = 6425778418326004669L;


}