package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Classe que possui informações sobre dimensão.
 * 
 * @author Discover Technology
 *
 */
@Embeddable
public class Dimensao implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4393730062108434251L;

	@Column(name = "LARGURA", nullable = true)
	private Float largura;
	
	@Column(name = "COMPRIMENTO", nullable = true)
	private Float comprimento;
	
	@Column(name = "ESPESSURA", nullable = true)
	private Float espessura;
	
	/**
	 * Construtor.
	 */
	public Dimensao() {
		
	}

	/**
	 * @return the largura
	 */
	public Float getLargura() {
		return largura;
	}

	/**
	 * @param largura the largura to set
	 */
	public void setLargura(Float largura) {
		this.largura = largura;
	}

	/**
	 * @return the comprimento
	 */
	public Float getComprimento() {
		return comprimento;
	}

	/**
	 * @param comprimento the comprimento to set
	 */
	public void setComprimento(Float comprimento) {
		this.comprimento = comprimento;
	}

	/**
	 * @return the espessura
	 */
	public Float getEspessura() {
		return espessura;
	}

	/**
	 * @param espessura the espessura to set
	 */
	public void setEspessura(Float espessura) {
		this.espessura = espessura;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comprimento == null) ? 0 : comprimento.hashCode());
		result = prime * result
				+ ((espessura == null) ? 0 : espessura.hashCode());
		result = prime * result + ((largura == null) ? 0 : largura.hashCode());
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
		Dimensao other = (Dimensao) obj;
		if (comprimento == null) {
			if (other.comprimento != null)
				return false;
		} else if (!comprimento.equals(other.comprimento))
			return false;
		if (espessura == null) {
			if (other.espessura != null)
				return false;
		} else if (!espessura.equals(other.espessura))
			return false;
		if (largura == null) {
			if (other.largura != null)
				return false;
		} else if (!largura.equals(other.largura))
			return false;
		return true;
	}
	
}
