package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
public class ProdutoEdicao {

	private Long id;
	private BigDecimal precoVenda;
	private BigDecimal desconto;
	private int pacotePadrao;
	private int peb;
	private BigDecimal precoCusto;
	private BigDecimal peso;
	public Produto produto;

	public ProdutoEdicao(){

	}

}