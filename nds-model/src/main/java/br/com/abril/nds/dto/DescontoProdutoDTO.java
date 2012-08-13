package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class DescontoProdutoDTO implements Serializable {

	private static final long serialVersionUID = -2744214706916950981L;

	private String codigoProduto;
	
	private Long edicaoProduto;
	
	private Integer quantidadeEdicoes; 
	
	private boolean hasCotaEspecifica;
	
	private BigDecimal descontoProduto;
	
	private List<Integer> cotas;
	
	private boolean descontoPredominante;

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
	 * @return the edicaoProduto
	 */
	public Long getEdicaoProduto() {
		return edicaoProduto;
	}

	/**
	 * @param edicaoProduto the edicaoProduto to set
	 */
	public void setEdicaoProduto(Long edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}

	/**
	 * @return the quantidadeEdicoes
	 */
	public Integer getQuantidadeEdicoes() {
		return quantidadeEdicoes;
	}

	/**
	 * @param quantidadeEdicoes the quantidadeEdicoes to set
	 */
	public void setQuantidadeEdicoes(Integer quantidadeEdicoes) {
		this.quantidadeEdicoes = quantidadeEdicoes;
	}
	
	/**
	 * @return the hasCotaEspecifica
	 */
	public boolean isHasCotaEspecifica() {
		return hasCotaEspecifica;
	}

	/**
	 * @param hasCotaEspecifica the hasCotaEspecifica to set
	 */
	public void setHasCotaEspecifica(boolean hasCotaEspecifica) {
		this.hasCotaEspecifica = hasCotaEspecifica;
	}

	/**
	 * @return the descontoProduto
	 */
	public BigDecimal getDescontoProduto() {
		return descontoProduto;
	}

	/**
	 * @param descontoProduto the descontoProduto to set
	 */
	public void setDescontoProduto(BigDecimal descontoProduto) {
		this.descontoProduto = descontoProduto;
	}

	/**
	 * @return the cotas
	 */
	public List<Integer> getCotas() {
		return cotas;
	}

	/**
	 * @param cotas the cotas to set
	 */
	public void setCotas(List<Integer> cotas) {
		this.cotas = cotas;
	}

	/**
	 * @return the descontoPredominante
	 */
	public boolean isDescontoPredominante() {
		return descontoPredominante;
	}

	/**
	 * @param descontoPredominante the descontoPredominante to set
	 */
	public void setDescontoPredominante(boolean descontoPredominante) {
		this.descontoPredominante = descontoPredominante;
	}
}
