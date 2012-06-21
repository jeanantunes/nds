package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ContribuicaoSocial implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7720053662091868121L;
	
	protected Integer cst;
	
	protected Double valorBaseCalculo;
	
	protected Float percentualAliquota;
	
	protected Double quantidadeVendida;
	
	protected Double valorAliquota;
	
	protected Double valor;

	/**
	 * @return the cst
	 */
	public Integer getCst() {
		return cst;
	}

	/**
	 * @param cst the cst to set
	 */
	public void setCst(Integer cst) {
		this.cst = cst;
	}

	/**
	 * @return the valorBaseCalculo
	 */
	public Double getValorBaseCalculo() {
		return valorBaseCalculo;
	}

	/**
	 * @param valorBaseCalculo the valorBaseCalculo to set
	 */
	public void setValorBaseCalculo(Double valorBaseCalculo) {
		this.valorBaseCalculo = valorBaseCalculo;
	}

	/**
	 * @return the percentualAliquota
	 */
	public Float getPercentualAliquota() {
		return percentualAliquota;
	}

	/**
	 * @param percentualAliquota the percentualAliquota to set
	 */
	public void setPercentualAliquota(Float percentualAliquota) {
		this.percentualAliquota = percentualAliquota;
	}

	/**
	 * @return the quantidadeVendida
	 */
	public Double getQuantidadeVendida() {
		return quantidadeVendida;
	}

	/**
	 * @param quantidadeVendida the quantidadeVendida to set
	 */
	public void setQuantidadeVendida(Double quantidadeVendida) {
		this.quantidadeVendida = quantidadeVendida;
	}

	/**
	 * @return the valorAliquota
	 */
	public Double getValorAliquota() {
		return valorAliquota;
	}

	/**
	 * @param valorAliquota the valorAliquota to set
	 */
	public void setValorAliquota(Double valorAliquota) {
		this.valorAliquota = valorAliquota;
	}

	/**
	 * @return the valor
	 */
	public Double getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(Double valor) {
		this.valor = valor;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ContribuicaoSocial ["
				+ (cst != null ? "cst=" + cst + ", " : "")
				+ (valorBaseCalculo != null ? "valorBaseCalculo="
						+ valorBaseCalculo + ", " : "")
				+ (percentualAliquota != null ? "percentualAliquota="
						+ percentualAliquota + ", " : "")
				+ (quantidadeVendida != null ? "quantidadeVendida="
						+ quantidadeVendida + ", " : "")
				+ (valorAliquota != null ? "valorAliquota=" + valorAliquota
						+ ", " : "") + (valor != null ? "valor=" + valor : "")
				+ "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cst == null) ? 0 : cst.hashCode());
		result = prime
				* result
				+ ((percentualAliquota == null) ? 0 : percentualAliquota
						.hashCode());
		result = prime
				* result
				+ ((quantidadeVendida == null) ? 0 : quantidadeVendida
						.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result
				+ ((valorAliquota == null) ? 0 : valorAliquota.hashCode());
		result = prime
				* result
				+ ((valorBaseCalculo == null) ? 0 : valorBaseCalculo.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ContribuicaoSocial other = (ContribuicaoSocial) obj;
		if (cst == null) {
			if (other.cst != null) {
				return false;
			}
		} else if (!cst.equals(other.cst)) {
			return false;
		}
		if (percentualAliquota == null) {
			if (other.percentualAliquota != null) {
				return false;
			}
		} else if (!percentualAliquota.equals(other.percentualAliquota)) {
			return false;
		}
		if (quantidadeVendida == null) {
			if (other.quantidadeVendida != null) {
				return false;
			}
		} else if (!quantidadeVendida.equals(other.quantidadeVendida)) {
			return false;
		}
		if (valor == null) {
			if (other.valor != null) {
				return false;
			}
		} else if (!valor.equals(other.valor)) {
			return false;
		}
		if (valorAliquota == null) {
			if (other.valorAliquota != null) {
				return false;
			}
		} else if (!valorAliquota.equals(other.valorAliquota)) {
			return false;
		}
		if (valorBaseCalculo == null) {
			if (other.valorBaseCalculo != null) {
				return false;
			}
		} else if (!valorBaseCalculo.equals(other.valorBaseCalculo)) {
			return false;
		}
		return true;
	}
	
	
	
	
	

}
