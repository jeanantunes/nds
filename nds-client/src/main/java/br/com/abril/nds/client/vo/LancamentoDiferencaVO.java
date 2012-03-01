package br.com.abril.nds.client.vo;

import java.io.Serializable;

/**
 * Value Object para lançamento de diferença.
 * 
 * @author Discover Technology
 *
 */
public class LancamentoDiferencaVO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6329780427551941318L;

	private Integer id;
	
	private String codigoProduto;
	
	private String descricaoProduto;
	
	private String numeroEdicao;
	
	private String precoVenda;
	
	private String pacotePadrao;
	
	private String quantidade;
	
	private String tipoDiferenca;
	
	private String valorTotalDiferenca;
	
	/**
	 * Construtor padrão.
	 */
	public LancamentoDiferencaVO() {
		
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * @return the descricaoProduto
	 */
	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	/**
	 * @param descricaoProduto the descricaoProduto to set
	 */
	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	/**
	 * @return the numeroEdicao
	 */
	public String getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the precoVenda
	 */
	public String getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(String precoVenda) {
		this.precoVenda = precoVenda;
	}

	/**
	 * @return the pacotePadrao
	 */
	public String getPacotePadrao() {
		return pacotePadrao;
	}

	/**
	 * @param pacotePadrao the pacotePadrao to set
	 */
	public void setPacotePadrao(String pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	/**
	 * @return the quantidade
	 */
	public String getQuantidade() {
		return quantidade;
	}

	/**
	 * @param quantidade the quantidade to set
	 */
	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	/**
	 * @return the tipoDiferenca
	 */
	public String getTipoDiferenca() {
		return tipoDiferenca;
	}

	/**
	 * @param tipoDiferenca the tipoDiferenca to set
	 */
	public void setTipoDiferenca(String tipoDiferenca) {
		this.tipoDiferenca = tipoDiferenca;
	}

	/**
	 * @return the valorDiferenca
	 */
	public String getValorTotalDiferenca() {
		return valorTotalDiferenca;
	}

	/**
	 * @param valorDiferenca the valorDiferenca to set
	 */
	public void setValorTotalDiferenca(String valorTotalDiferenca) {
		this.valorTotalDiferenca = valorTotalDiferenca;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		LancamentoDiferencaVO other = (LancamentoDiferencaVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
