package br.com.abril.nds.client.vo;

public class ProdutoEdicaoFechadaVO {

	private String codigoProduto;
	
	private Long edicaoProduto;
	
	private boolean parcial;
	
	private Long idProdutoEdicao;

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
	 * @return the parcial
	 */
	public boolean isParcial() {
		return parcial;
	}

	/**
	 * @param parcial the parcial to set
	 */
	public void setParcial(boolean parcial) {
		this.parcial = parcial;
	}

	/**
	 * @return the idProdutoEdicao
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idProdutoEdicao == null) ? 0 : idProdutoEdicao.hashCode());
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
		ProdutoEdicaoFechadaVO other = (ProdutoEdicaoFechadaVO) obj;
		if (idProdutoEdicao == null) {
			if (other.idProdutoEdicao != null)
				return false;
		} else if (!idProdutoEdicao.equals(other.idProdutoEdicao))
			return false;
		return true;
	}
	
	
	
}
