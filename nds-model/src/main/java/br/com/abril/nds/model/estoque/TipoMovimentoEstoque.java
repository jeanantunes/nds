package br.com.abril.nds.model.estoque;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.abril.nds.model.movimentacao.TipoMovimento;

@Entity
@DiscriminatorValue(value = "ESTOQUE")
public class TipoMovimentoEstoque extends TipoMovimento {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -747736389896930758L;

	@Column(name = "INCIDE_DIVIDA")
	private boolean incideDivida;
	
	@Column(name = "INCIDE_JURAMENTADO")
	private boolean incideJuramentado;
	
	@Column(name = "GRUPO_MOVIMENTO_ESTOQUE")
	@Enumerated(EnumType.STRING)
	private GrupoMovimentoEstoque grupoMovimentoEstoque;
	
	@Column(name = "OPERACAO_ESTOQUE")
	@Enumerated(EnumType.STRING)
	private OperacaoEstoque operacaoEstoque;
	
	public TipoMovimentoEstoque() {
		
	}
	
	public TipoMovimentoEstoque(boolean incideDivida,
			GrupoMovimentoEstoque grupoMovimentoEstoque,
			OperacaoEstoque operacaoEstoque) {
		super();
		this.incideDivida = incideDivida;
		this.grupoMovimentoEstoque = grupoMovimentoEstoque;
		this.setOperacaoEstoque(operacaoEstoque);
	}

	public boolean isIncideDivida() {
		return incideDivida;
	}

	public void setIncideDivida(boolean incideDivida) {
		this.incideDivida = incideDivida;
	}

	public GrupoMovimentoEstoque getGrupoMovimentoEstoque() {
		return grupoMovimentoEstoque;
	}

	public void setGrupoMovimentoEstoque(
			GrupoMovimentoEstoque grupoMovimentoEstoque) {
		this.grupoMovimentoEstoque = grupoMovimentoEstoque;
		this.setOperacaoEstoque(grupoMovimentoEstoque.getOperacaoEstoque());
	}

	public OperacaoEstoque getOperacaoEstoque() {
		return operacaoEstoque;
	}

	public void setOperacaoEstoque(OperacaoEstoque operacaoEstoque) {
		this.operacaoEstoque = operacaoEstoque;
	}

	public boolean isIncideJuramentado() {
		return incideJuramentado;
	}

	public void setIncideJuramentado(boolean incideJuramentado) {
		this.incideJuramentado = incideJuramentado;
	}

}