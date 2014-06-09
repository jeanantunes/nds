package br.com.abril.nds.dto;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;

public class MovimentosEstoqueCotaSaldoDTO {
	
	private List<MovimentoEstoqueCota> movimentosEstoqueCota;
	
	private Map<Long,BigInteger> produtoEdicaoQtdSaida;
	
	private Map<Long,BigInteger> produtoEdicaoQtdEntrada;

	/**
	 * @param movimentosEstoqueCota
	 * @param produtoEdicaoQtdSaida
	 * @param produtoEdicaoQtdEntrada
	 */
	public MovimentosEstoqueCotaSaldoDTO(
			List<MovimentoEstoqueCota> movimentosEstoqueCota,
			Map<Long, BigInteger> produtoEdicaoQtdSaida,
			Map<Long, BigInteger> produtoEdicaoQtdEntrada) {
		super();
		this.movimentosEstoqueCota = movimentosEstoqueCota;
		this.produtoEdicaoQtdSaida = produtoEdicaoQtdSaida;
		this.produtoEdicaoQtdEntrada = produtoEdicaoQtdEntrada;
	}

	/**
	 * @return the movimentosEstoqueCota
	 */
	public List<MovimentoEstoqueCota> getMovimentosEstoqueCota() {
		return movimentosEstoqueCota;
	}

	/**
	 * @param movimentosEstoqueCota the movimentosEstoqueCota to set
	 */
	public void setMovimentosEstoqueCota(
			List<MovimentoEstoqueCota> movimentosEstoqueCota) {
		this.movimentosEstoqueCota = movimentosEstoqueCota;
	}

	/**
	 * @return the produtoEdicaoQtdSaida
	 */
	public Map<Long, BigInteger> getProdutoEdicaoQtdSaida() {
		return produtoEdicaoQtdSaida;
	}

	/**
	 * @param produtoEdicaoQtdSaida the produtoEdicaoQtdSaida to set
	 */
	public void setProdutoEdicaoQtdSaida(Map<Long, BigInteger> produtoEdicaoQtdSaida) {
		this.produtoEdicaoQtdSaida = produtoEdicaoQtdSaida;
	}

	/**
	 * @return the produtoEdicaoQtdEntrada
	 */
	public Map<Long, BigInteger> getProdutoEdicaoQtdEntrada() {
		return produtoEdicaoQtdEntrada;
	}

	/**
	 * @param produtoEdicaoQtdEntrada the produtoEdicaoQtdEntrada to set
	 */
	public void setProdutoEdicaoQtdEntrada(
			Map<Long, BigInteger> produtoEdicaoQtdEntrada) {
		this.produtoEdicaoQtdEntrada = produtoEdicaoQtdEntrada;
	}

}
