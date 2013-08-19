package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Data Transfer Object para estoque físico do produto no distribuidor.
 * 
 * @author Discover Technology
 *
 */
public class EstoqueFisicoProdutoDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2565984667856751682L;
	
	private String codigoProduto;
	
	private Long numeroEdicao;
	
	private BigDecimal qtdeFisico;
	
	private Date dataExpedicaoNF;
	
	/**
	 * Construtor padrão.
	 */
	public EstoqueFisicoProdutoDTO() {
		
	}

	/**
	 * Construtor.
	 * 
	 * @param codigoProduto
	 * @param numeroEdicao
	 * @param qtdeFisico
	 * @param dataExpedicaoNF
	 */
	public EstoqueFisicoProdutoDTO(String codigoProduto, 
								   Long numeroEdicao, 
								   BigDecimal qtdeFisico, 
								   Date dataExpedicaoNF) {
		
		this.codigoProduto = codigoProduto;
		this.numeroEdicao = numeroEdicao;
		this.qtdeFisico = qtdeFisico;
		this.dataExpedicaoNF = dataExpedicaoNF;
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
	 * @return the qtdeFisico
	 */
	public BigDecimal getQtdeFisico() {
		return qtdeFisico;
	}

	/**
	 * @param qtdeFisico the qtdeFisico to set
	 */
	public void setQtdeFisico(BigDecimal qtdeFisico) {
		this.qtdeFisico = qtdeFisico;
	}

	/**
	 * @return the dataExpedicaoNF
	 */
	public Date getDataExpedicaoNF() {
		return dataExpedicaoNF;
	}

	/**
	 * @param dataExpedicaoNF the dataExpedicaoNF to set
	 */
	public void setDataExpedicaoNF(Date dataExpedicaoNF) {
		this.dataExpedicaoNF = dataExpedicaoNF;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		result = prime * result
				+ ((dataExpedicaoNF == null) ? 0 : dataExpedicaoNF.hashCode());
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
		result = prime * result
				+ ((qtdeFisico == null) ? 0 : qtdeFisico.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EstoqueFisicoProdutoDTO other = (EstoqueFisicoProdutoDTO) obj;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		if (dataExpedicaoNF == null) {
			if (other.dataExpedicaoNF != null)
				return false;
		} else if (!dataExpedicaoNF.equals(other.dataExpedicaoNF))
			return false;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		if (qtdeFisico == null) {
			if (other.qtdeFisico != null)
				return false;
		} else if (!qtdeFisico.equals(other.qtdeFisico))
			return false;
		return true;
	}
	
}
