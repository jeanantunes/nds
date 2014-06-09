package br.com.abril.nds.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.Intervalo;

/**
 * Value Object para período.
 * 
 * @author Discover Technology
 * @deprecated usar {@link Intervalo}
 */
@Deprecated
public class PeriodoVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1534870525640865671L;
	
	private Date dataInicial;
	
	private Date dataFinal;
	
	/**
	 * Construtor padrão.
	 */
	public PeriodoVO() {
		
	}
	
	/**
	 * Construtor.
	 * 
	 * @param dataInicial - data inicial do período
	 * @param dataFinal - data final do período
	 */
	public PeriodoVO(Date dataInicial, Date dataFinal) {
		
		this.dataInicial = dataInicial;
		
		this.dataFinal = dataFinal;
	}

	/**
	 * @return the dataInicial
	 */
	public Date getDataInicial() {
		return dataInicial;
	}

	/**
	 * @param dataInicial the dataInicial to set
	 */
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	/**
	 * @return the dataFinal
	 */
	public Date getDataFinal() {
		return dataFinal;
	}

	/**
	 * @param dataFinal the dataFinal to set
	 */
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataFinal == null) ? 0 : dataFinal.hashCode());
		result = prime * result
				+ ((dataInicial == null) ? 0 : dataInicial.hashCode());
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
		PeriodoVO other = (PeriodoVO) obj;
		if (dataFinal == null) {
			if (other.dataFinal != null)
				return false;
		} else if (!dataFinal.equals(other.dataFinal))
			return false;
		if (dataInicial == null) {
			if (other.dataInicial != null)
				return false;
		} else if (!dataInicial.equals(other.dataInicial))
			return false;
		return true;
	} 

}
