package br.com.abril.nds.model.estoque;
import br.com.abril.nds.model.movimentacao.TipoOperacao;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoDiferenca {
	FALTA_DE,
	FALTA_EM,
	SOBRA_DE,
	SOBRA_EM;

	public TipoOperacao tipoOperacao;
}