package br.com.abril.nds.model.movimentacao;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public class TipoMovimento {

	private Long id;
	private String descricao;
	private boolean aprovacaoAutomatica;
	private boolean incideDivida;
	public TipoMovimentoEstoque tipoMovimentoEstoque;

	public TipoMovimento(){

	}

}