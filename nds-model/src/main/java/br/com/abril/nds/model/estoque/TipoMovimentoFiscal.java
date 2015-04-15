package br.com.abril.nds.model.estoque;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.abril.nds.model.movimentacao.TipoMovimento;

@Entity
@DiscriminatorValue(value = "FISCAL")
public class TipoMovimentoFiscal extends TipoMovimento {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -747736389896930758L;
	
	@Column(name = "OPERACAO_ESTOQUE")
	@Enumerated(EnumType.STRING)
	private OperacaoEstoque operacaoEstoque;

	public TipoMovimentoFiscal() {
		
	}

	public OperacaoEstoque getOperacaoEstoque() {
		return operacaoEstoque;
	}

	public void setOperacaoEstoque(OperacaoEstoque operacaoEstoque) {
		this.operacaoEstoque = operacaoEstoque;
	}

}