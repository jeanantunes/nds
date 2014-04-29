package br.com.abril.nds.model.fiscal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Fornecedor;

@Entity
@Table(name = "MOVIMENTO_FECHAMENTO_FISCAL_FORNECEDOR")
@DiscriminatorValue(value="FORNECEDOR")
public class MovimentoFechamentoFiscalFornecedor extends MovimentoFechamentoFiscal {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "FORNECEDOR_ID")
	private Fornecedor fornecedor;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
}