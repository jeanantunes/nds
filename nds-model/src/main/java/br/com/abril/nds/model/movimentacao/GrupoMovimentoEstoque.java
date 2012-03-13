package br.com.abril.nds.model.movimentacao;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum GrupoMovimentoEstoque  {
	
	RECEBIMENTO_FISICO(OperacaoEstoque.ENTRADA), 
	ENVIO_JORNALEIRO(OperacaoEstoque.SAIDA),
	RECEBIMENTO_ENCALHE(OperacaoEstoque.ENTRADA), 
	SOBRA_DE(OperacaoEstoque.ENTRADA), 
	SOBRA_EM(OperacaoEstoque.ENTRADA), 
	FALTA_DE(OperacaoEstoque.SAIDA), 
	FALTA_EM(OperacaoEstoque.SAIDA),
	RECEBIMENTO_REPARTE(OperacaoEstoque.ENTRADA),
	ENVIO_ENCALHE(OperacaoEstoque.SAIDA);
	
	private OperacaoEstoque operacaoEstoque;
	
	private GrupoMovimentoEstoque(OperacaoEstoque operacaoEstoque) {
		this.operacaoEstoque = operacaoEstoque;
	}
	
	public OperacaoEstoque getOperacaoEstoque() {
		return operacaoEstoque;
	}

}