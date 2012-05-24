package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 
 * @author Diego Fernandes
 *
 */
public class InformeEncalheDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2161743590665713176L;
	
	
	private Long idLancamento;
	
	private Long idProdutoEdicao;
	
	private Integer sequenciaMatriz;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long numeroEdicao;
	
	private String slogan;
	
	private String codigoDeBarras;
	
	private Double precoVenda;
	
	private Double desconto;
	
	private Double precoDesconto;
	
	private Calendar dataLancamento;
	
	private Calendar dataRecolhimento;

	/**
	 * @return the idLancamento
	 */
	public Long getIdLancamento() {
		return idLancamento;
	}

	/**
	 * @param idLancamento the idLancamento to set
	 */
	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
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

	/**
	 * @return the sequenciaMatriz
	 */
	public Integer getSequenciaMatriz() {
		return sequenciaMatriz;
	}

	/**
	 * @param sequenciaMatriz the sequenciaMatriz to set
	 */
	public void setSequenciaMatriz(Integer sequenciaMatriz) {
		this.sequenciaMatriz = sequenciaMatriz;
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
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * @param nomeProduto the descricaoProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
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
	 * @return the slogan
	 */
	public String getSlogan() {
		return slogan;
	}

	/**
	 * @param slogan the slogan to set
	 */
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	/**
	 * @return the codigoDeBarras
	 */
	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	/**
	 * @param codigoDeBarras the codigoDeBarras to set
	 */
	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	/**
	 * @return the precoVenda
	 */
	public Double getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(Double precoVenda) {
		this.precoVenda = precoVenda;
	}

	/**
	 * @return the desconto
	 */
	public Double getDesconto() {
		return desconto;
	}

	/**
	 * @param desconto the desconto to set
	 */
	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	/**
	 * @return the dataLancamento
	 */
	public Calendar getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(Calendar dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	/**
	 * @return the dataRecolhimento
	 */
	public Calendar getDataRecolhimento() {
		return dataRecolhimento;
	}

	/**
	 * @param dataRecolhimento the dataRecolhimento to set
	 */
	public void setDataRecolhimento(Calendar dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoDeBarras == null) ? 0 : codigoDeBarras.hashCode());
		result = prime * result
				+ ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		result = prime * result
				+ ((dataLancamento == null) ? 0 : dataLancamento.hashCode());
		result = prime
				* result
				+ ((dataRecolhimento == null) ? 0 : dataRecolhimento.hashCode());
		result = prime * result
				+ ((desconto == null) ? 0 : desconto.hashCode());
		result = prime
				* result
				+ ((nomeProduto == null) ? 0 : nomeProduto.hashCode());
		result = prime * result
				+ ((idLancamento == null) ? 0 : idLancamento.hashCode());
		result = prime * result
				+ ((idProdutoEdicao == null) ? 0 : idProdutoEdicao.hashCode());
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
		result = prime * result
				+ ((precoDesconto == null) ? 0 : precoDesconto.hashCode());
		result = prime * result
				+ ((precoVenda == null) ? 0 : precoVenda.hashCode());
		result = prime * result
				+ ((sequenciaMatriz == null) ? 0 : sequenciaMatriz.hashCode());
		result = prime * result + ((slogan == null) ? 0 : slogan.hashCode());
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
		InformeEncalheDTO other = (InformeEncalheDTO) obj;
		if (codigoDeBarras == null) {
			if (other.codigoDeBarras != null)
				return false;
		} else if (!codigoDeBarras.equals(other.codigoDeBarras))
			return false;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		if (dataLancamento == null) {
			if (other.dataLancamento != null)
				return false;
		} else if (!dataLancamento.equals(other.dataLancamento))
			return false;
		if (dataRecolhimento == null) {
			if (other.dataRecolhimento != null)
				return false;
		} else if (!dataRecolhimento.equals(other.dataRecolhimento))
			return false;
		if (desconto == null) {
			if (other.desconto != null)
				return false;
		} else if (!desconto.equals(other.desconto))
			return false;
		if (nomeProduto == null) {
			if (other.nomeProduto != null)
				return false;
		} else if (!nomeProduto.equals(other.nomeProduto))
			return false;
		if (idLancamento == null) {
			if (other.idLancamento != null)
				return false;
		} else if (!idLancamento.equals(other.idLancamento))
			return false;
		if (idProdutoEdicao == null) {
			if (other.idProdutoEdicao != null)
				return false;
		} else if (!idProdutoEdicao.equals(other.idProdutoEdicao))
			return false;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		if (precoDesconto == null) {
			if (other.precoDesconto != null)
				return false;
		} else if (!precoDesconto.equals(other.precoDesconto))
			return false;
		if (precoVenda == null) {
			if (other.precoVenda != null)
				return false;
		} else if (!precoVenda.equals(other.precoVenda))
			return false;
		if (sequenciaMatriz == null) {
			if (other.sequenciaMatriz != null)
				return false;
		} else if (!sequenciaMatriz.equals(other.sequenciaMatriz))
			return false;
		if (slogan == null) {
			if (other.slogan != null)
				return false;
		} else if (!slogan.equals(other.slogan))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InformeEncalheDTO ["
				+ (idLancamento != null ? "idLancamento=" + idLancamento + ", "
						: "")
				+ (idProdutoEdicao != null ? "idProdutoEdicao="
						+ idProdutoEdicao + ", " : "")
				+ (sequenciaMatriz != null ? "sequenciaMatriz="
						+ sequenciaMatriz + ", " : "")
				+ (codigoProduto != null ? "codigoProduto=" + codigoProduto
						+ ", " : "")
				+ (nomeProduto != null ? "descricaoProduto="
						+ nomeProduto + ", " : "")
				+ (numeroEdicao != null ? "numeroEdicao=" + numeroEdicao + ", "
						: "")
				+ (slogan != null ? "slogan=" + slogan + ", " : "")
				+ (codigoDeBarras != null ? "codigoDeBarras=" + codigoDeBarras
						+ ", " : "")
				+ (precoVenda != null ? "precoVenda=" + precoVenda + ", " : "")
				+ (desconto != null ? "desconto=" + desconto + ", " : "")
				+ (precoDesconto != null ? "precoDesconto=" + precoDesconto
						+ ", " : "")
				+ (dataLancamento != null ? "dataLancamento=" + dataLancamento
						+ ", " : "")
				+ (dataRecolhimento != null ? "dataRecolhimento="
						+ dataRecolhimento : "") + "]";
	}

	/**
	 * @return the precoDesconto
	 */
	public Double getPrecoDesconto() {
		return precoDesconto;
	}

	/**
	 * @param precoDesconto the precoDesconto to set
	 */
	public void setPrecoDesconto(Double precoDesconto) {
		this.precoDesconto = precoDesconto;
	}

}
