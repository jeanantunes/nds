package br.com.abril.nds.model.cadastro;

import java.util.List;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
public class Produto {

	private int periodicidade;
	private String nome;
	private String descricao;
	public List<Fornecedor> fornecedores;
	public TipoProduto tipoProduto;

	public Produto(){

	}

}