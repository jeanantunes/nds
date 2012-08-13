package br.com.abril.nds.model.fiscal.nota;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;

/**
 * Item da Nota
 * @author Diego Fernandes
 */
public class ItemNotaFiscal {
	
	private long idProdutoEdicao;
	
	private BigInteger quantidade;
	
	private BigDecimal valorUnitario;
	
	private String cstICMS;
	
	private List<MovimentoEstoqueCota> listaMovimentoEstoqueCota; 
	
	public ItemNotaFiscal() {
	}

	public ItemNotaFiscal(long idProdutoEdicao, BigInteger quantidade,
			BigDecimal valorUnitario, String cstICMS) {
		super();
		this.idProdutoEdicao = idProdutoEdicao;
		this.quantidade = quantidade;
		this.valorUnitario = valorUnitario;
		this.cstICMS = cstICMS;
	}

	/**
	 * @return the idProdutoEdicao
	 */
	public long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	/**
	 * @return the quantidade
	 */
	public BigInteger getQuantidade() {
		return quantidade;
	}

	/**
	 * @param quantidade the quantidade to set
	 */
	public void setQuantidade(BigInteger quantidade) {
		this.quantidade = quantidade;
	}

	/**
	 * @return the valorUnitario
	 */
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	/**
	 * @param valorUnitario the valorUnitario to set
	 */
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	/**
	 * @return the cstICMS
	 */
	public String getCstICMS() {
		return cstICMS;
	}

	/**
	 * @param cstICMS the cstICMS to set
	 */
	public void setCstICMS(String cstICMS) {
		this.cstICMS = cstICMS;
	}

	/**
	 * @return the listaMovimentoEstoqueCota
	 */
	public List<MovimentoEstoqueCota> getListaMovimentoEstoqueCota() {
		return listaMovimentoEstoqueCota;
	}

	/**
	 * @param listaMovimentoEstoqueCota the listaMovimentoEstoqueCota to set
	 */
	public void setListaMovimentoEstoqueCota(
			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota) {
		this.listaMovimentoEstoqueCota = listaMovimentoEstoqueCota;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (idProdutoEdicao ^ (idProdutoEdicao >>> 32));
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
		ItemNotaFiscal other = (ItemNotaFiscal) obj;
		if (idProdutoEdicao != other.idProdutoEdicao)
			return false;
		return true;
	}
	
}
