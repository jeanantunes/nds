package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.movimentacao.MovimentoEstoque;

/**
 * Data Transfer Object para diferenças de estoque (faltas e sobras).
 * 
 * @author Discover Technology
 *
 */
public class DiferencaDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7637417031868618075L;
	
	private ProdutoEdicao produtoEdicao;

	private MovimentoEstoque movimentoEstoque;
	
	private NotaFiscal notaFiscal;
	
	public DiferencaDTO(ProdutoEdicao produtoEdicao, 
						MovimentoEstoque movimentoEstoque, 
						NotaFiscal notaFiscal) {
		
		this.produtoEdicao = produtoEdicao;
		
		this.movimentoEstoque = movimentoEstoque;
		
		this.notaFiscal = notaFiscal;
	}
	
	public DiferencaDTO(ProdutoEdicao produtoEdicao, 
						MovimentoEstoque movimentoEstoque) {

		this.produtoEdicao = produtoEdicao;
		
		this.movimentoEstoque = movimentoEstoque;
	}

	/**
	 * @return the produtoEdicao
	 */
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	/**
	 * @param produtoEdicao the produtoEdicao to set
	 */
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	/**
	 * @return the movimentoEstoque
	 */
	public MovimentoEstoque getMovimentoEstoque() {
		return movimentoEstoque;
	}

	/**
	 * @param movimentoEstoque the movimentoEstoque to set
	 */
	public void setMovimentoEstoque(MovimentoEstoque movimentoEstoque) {
		this.movimentoEstoque = movimentoEstoque;
	}

	/**
	 * @return the notaFiscal
	 */
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	/**
	 * @param notaFiscal the notaFiscal to set
	 */
	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

}
