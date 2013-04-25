package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * DTO com informações de impressão de diferenças de estoque.
 * 
 * @author Discover Technology
 *
 */
public class ImpressaoDiferencaEstoqueDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8880460525015896929L;

	private ProdutoEdicao produtoEdicao;
	
	private BigInteger qtdeFaltas;
	
	private BigInteger qtdeSobras;
	
	/**
	 * Construtor padrão.
	 */
	public ImpressaoDiferencaEstoqueDTO() {
		
		
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public BigInteger getQtdeFaltas() {
		return qtdeFaltas;
	}

	public void setQtdeFaltas(BigInteger qtdeFaltas) {
		this.qtdeFaltas = qtdeFaltas;
	}

	public BigInteger getQtdeSobras() {
		return qtdeSobras;
	}

	public void setQtdeSobras(BigInteger qtdeSobras) {
		this.qtdeSobras = qtdeSobras;
	}
	
}
