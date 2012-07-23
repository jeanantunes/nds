package br.com.abril.nds.strategy.importacao.input;

import java.io.Serializable;

public class HistoricoVendaInput implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String codigoProduto;
	
	private Integer numeroEdicao;
	
	private Integer numeroCota;
	
	private Integer quantidadeRecebidaProduto;
	
	private Integer quantidadeDevolvidaProduto;
	
	private Long idUsuario;

	
	
	/**
	 * @return the idUsuario
	 */
	public Long getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	/**
	 * @return the numeroEdicao
	 */
	public Integer getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Integer numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the quantidadeRecebidaProduto
	 */
	public Integer getQuantidadeRecebidaProduto() {
		return quantidadeRecebidaProduto;
	}

	/**
	 * @param quantidadeRecebidaProduto the quantidadeRecebidaProduto to set
	 */
	public void setQuantidadeRecebidaProduto(Integer quantidadeRecebidaProduto) {
		this.quantidadeRecebidaProduto = quantidadeRecebidaProduto;
	}

	/**
	 * @return the quantidadeDevolvidaProduto
	 */
	public Integer getQuantidadeDevolvidaProduto() {
		return quantidadeDevolvidaProduto;
	}

	/**
	 * @param quantidadeDevolvidaProduto the quantidadeDevolvidaProduto to set
	 */
	public void setQuantidadeDevolvidaProduto(Integer quantidadeDevolvidaProduto) {
		this.quantidadeDevolvidaProduto = quantidadeDevolvidaProduto;
	}
	
	
}
