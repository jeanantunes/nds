package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
/**
 * 
 * @author Diego Fernandes
 *
 */
@Entity
@Table(name="CHEQUE_IMAGEM")
public class ChequeImage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -651829146458321296L;
	
	@Id
	@Column(name="ID")
	private Long id;
	
	
	@OneToOne(orphanRemoval=true)
	@JoinColumn(name="ID", referencedColumnName="id",insertable=false, updatable=false)
	private Cheque cheque;
	
	@Lob
	@Column(name="IMAGE",nullable=false)
	private byte[] imagem;
	

	/**
	 * @return the imagem
	 */
	public byte[] getImagem() {
		return imagem;
	}

	/**
	 * @param imagem the imagem to set
	 */
	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	/**
	 * @return the cheque
	 */
	public Cheque getCheque() {
		return cheque;
	}

	/**
	 * @param cheque the cheque to set
	 */
	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + Arrays.hashCode(imagem);
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
		ChequeImage other = (ChequeImage) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (!Arrays.equals(imagem, other.imagem))
			return false;
		return true;
	}

	

}
