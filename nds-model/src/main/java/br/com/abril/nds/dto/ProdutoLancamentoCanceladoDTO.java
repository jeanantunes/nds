package br.com.abril.nds.dto;

import java.math.BigInteger;
import java.util.Date;

public class ProdutoLancamentoCanceladoDTO {

	private String codigo;
	
	private String produto;
	
	private Long numeroEdicao;
	
	private BigInteger reparte;
	
	private Date dataLancamento;

	public ProdutoLancamentoCanceladoDTO(){}
	
	public ProdutoLancamentoCanceladoDTO(String codigo, String produto,
			Long numeroEdicao, BigInteger reparte, Date dataLancamento) {
		
		this.codigo = codigo;
		this.produto = produto;
		this.numeroEdicao = numeroEdicao;
		this.reparte = reparte;
		this.dataLancamento = dataLancamento;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the produto
	 */
	public String getProduto() {
		return produto;
	}

	/**
	 * @param produto the produto to set
	 */
	public void setProduto(String produto) {
		this.produto = produto;
	}

	/**
	 * @return the numeroEdicao
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the repartePrevisto
	 */
	public BigInteger getRepartePrevisto() {
		return reparte;
	}

	/**
	 * @param repartePrevisto the repartePrevisto to set
	 */
	public void setRepartePrevisto(BigInteger repartePrevisto) {
		this.reparte = repartePrevisto;
	}

	/**
	 * @return the dataLancamento
	 */
	public Date getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	
}
