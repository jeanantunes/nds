package br.com.abril.nds.model.estoque;
import br.com.abril.nds.model.movimentacao.GrupoMovimentoEstoque;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoDiferenca {
	
	FALTA_DE(GrupoMovimentoEstoque.FALTA_DE, "Falta de"),
	FALTA_EM(GrupoMovimentoEstoque.FALTA_EM, "Falta em"),
	SOBRA_DE(GrupoMovimentoEstoque.SOBRA_DE, "Sobra de"),
	SOBRA_EM(GrupoMovimentoEstoque.SOBRA_EM, "Sobra em");
	
	private GrupoMovimentoEstoque tipoMovimentoEstoque;
	
	private String descricao;
	
	private TipoDiferenca(GrupoMovimentoEstoque tipoMovimentoEstoque, String descricao) {
		
		this.tipoMovimentoEstoque = tipoMovimentoEstoque;
		this.descricao = descricao;
	}
	
	public GrupoMovimentoEstoque getTipoMovimentoEstoque() {
		
		return tipoMovimentoEstoque;
	}

	public String getDescricao() {
		return descricao;
	}
	
}