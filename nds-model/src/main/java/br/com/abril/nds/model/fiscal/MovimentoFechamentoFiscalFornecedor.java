package br.com.abril.nds.model.fiscal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Fornecedor;

@Entity
@Table(name = "MOVIMENTO_FECHAMENTO_FISCAL_FORNECEDOR")
@DiscriminatorValue(value="FORNECEDOR")
public class MovimentoFechamentoFiscalFornecedor extends MovimentoFechamentoFiscal {
	
	private static final long serialVersionUID = 1L;
	
	@OneToOne
	@JoinColumn(name = "FORNECEDOR_ID")
	private Fornecedor fornecedor;

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
}