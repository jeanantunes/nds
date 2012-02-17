package br.com.abril.nds.model.estoque;
import br.com.abril.nds.model.movimentacao.TipoMovimentoEstoque;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoDiferenca {
	
	FALTA_DE(TipoMovimentoEstoque.FALTA_DE, "Falta de"),
	FALTA_EM(TipoMovimentoEstoque.FALTA_EM, "Falta em"),
	SOBRA_DE(TipoMovimentoEstoque.SOBRA_DE, "Sobra de"),
	SOBRA_EM(TipoMovimentoEstoque.SOBRA_EM, "Sobra em");
	
	private TipoMovimentoEstoque tipoMovimentoEstoque;
	
	private String descricao;
	
	private TipoDiferenca(TipoMovimentoEstoque tipoMovimentoEstoque, String descricao) {
		
		this.tipoMovimentoEstoque = tipoMovimentoEstoque;
		this.descricao = descricao;
	}
	
	public TipoMovimentoEstoque getTipoMovimentoEstoque() {
		
		return tipoMovimentoEstoque;
	}

	public String getDescricao() {
		return descricao;
	}
	
}