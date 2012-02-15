package br.com.abril.nds.model.estoque;
import br.com.abril.nds.model.movimentacao.TipoMovimentoEstoque;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoDiferenca {
	
	FALTA_DE(TipoMovimentoEstoque.FALTA_DE),
	FALTA_EM(TipoMovimentoEstoque.FALTA_EM),
	SOBRA_DE(TipoMovimentoEstoque.SOBRA_DE),
	SOBRA_EM(TipoMovimentoEstoque.SOBRA_EM);
	
	private TipoMovimentoEstoque tipoMovimentoEstoque;
	
	private TipoDiferenca(TipoMovimentoEstoque tipoMovimentoEstoque) {
		this.tipoMovimentoEstoque = tipoMovimentoEstoque;
	}
	
	public TipoMovimentoEstoque getTipoMovimentoEstoque() {
		return tipoMovimentoEstoque;
	}

	
}