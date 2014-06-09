package br.com.abril.nds.model.envio.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Embeddable
public class ItemNotaEnvioPK implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3070206919433825685L;




	@ManyToOne(optional=false)
	@JoinColumn(name="NOTA_ENVIO_ID")
	private NotaEnvio notaEnvio;
	
	
	
	
	@Column(name="SEQUENCIA", nullable=false)
	private Integer sequencia;

	public ItemNotaEnvioPK() {
	}


	public ItemNotaEnvioPK(NotaEnvio notaEnvio, Integer sequencia) {
		super();
		this.notaEnvio = notaEnvio;
		this.sequencia = sequencia;
	}




	/**
	 * @return the notaEnvio
	 */
	public NotaEnvio getNotaEnvio() {
		return notaEnvio;
	}




	/**
	 * @param notaEnvio the notaEnvio to set
	 */
	public void setNotaEnvio(NotaEnvio notaEnvio) {
		this.notaEnvio = notaEnvio;
	}




	/**
	 * @return the sequencia
	 */
	public Integer getSequencia() {
		return sequencia;
	}




	/**
	 * @param sequencia the sequencia to set
	 */
	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((notaEnvio == null) ? 0 : notaEnvio.hashCode());
		result = prime * result
				+ ((sequencia == null) ? 0 : sequencia.hashCode());
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
		ItemNotaEnvioPK other = (ItemNotaEnvioPK) obj;
		if (notaEnvio == null) {
			if (other.notaEnvio != null) {
				return false;
			}
		} else if (!notaEnvio.equals(other.notaEnvio)) {
			return false;
		}
		if (sequencia == null) {
			if (other.sequencia != null) {
				return false;
			}
		} else if (!sequencia.equals(other.sequencia)) {
			return false;
		}
		return true;
	}
	
}
