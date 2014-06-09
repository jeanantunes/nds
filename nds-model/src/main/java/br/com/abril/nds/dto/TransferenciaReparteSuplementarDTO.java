package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.model.estoque.EstoqueProdutoCota;

public class TransferenciaReparteSuplementarDTO implements Serializable {

	private static final long serialVersionUID = 2317139321980007724L;

	private EstoqueProdutoCota estoqueProdutoCota;
	
	private BigInteger quantidadeTransferir;

	/**
	 * @return the estoqueProdutoCota
	 */
	public EstoqueProdutoCota getEstoqueProdutoCota() {
		return estoqueProdutoCota;
	}

	/**
	 * @param estoqueProdutoCota the estoqueProdutoCota to set
	 */
	public void setEstoqueProdutoCota(EstoqueProdutoCota estoqueProdutoCota) {
		this.estoqueProdutoCota = estoqueProdutoCota;
	}

	/**
	 * @return the quantidadeTransferir
	 */
	public BigInteger getQuantidadeTransferir() {
		return quantidadeTransferir;
	}

	/**
	 * @param quantidadeTransferir the quantidadeTransferir to set
	 */
	public void setQuantidadeTransferir(BigInteger quantidadeTransferir) {
		this.quantidadeTransferir = quantidadeTransferir;
	}
}
