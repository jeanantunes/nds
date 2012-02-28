package br.com.abril.nds.model.estoque;
import br.com.abril.nds.model.movimentacao.DominioTipoMovimento;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoDiferenca {
	
	FALTA_DE(DominioTipoMovimento.FALTA_DE, "Falta de"),
	FALTA_EM(DominioTipoMovimento.FALTA_EM, "Falta em"),
	SOBRA_DE(DominioTipoMovimento.SOBRA_DE, "Sobra de"),
	SOBRA_EM(DominioTipoMovimento.SOBRA_EM, "Sobra em");
	
	private DominioTipoMovimento tipoMovimentoEstoque;
	
	private String descricao;
	
	private TipoDiferenca(DominioTipoMovimento tipoMovimentoEstoque, String descricao) {
		
		this.tipoMovimentoEstoque = tipoMovimentoEstoque;
		this.descricao = descricao;
	}
	
	public DominioTipoMovimento getTipoMovimentoEstoque() {
		
		return tipoMovimentoEstoque;
	}

	public String getDescricao() {
		return descricao;
	}
	
}