package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Classe que possui informações sobre brinde.
 * 
 * @author Discover Technology
 *
 */
@Entity
@Table(name="BRINDE")
@SequenceGenerator(name="BRINDE_SEQ",allocationSize=1,initialValue=1)
public class Brinde implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -822721180874393822L;

	@Id
	@GeneratedValue(generator="BRINDE_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Column(name = "CODIGO", nullable = true)
	private Integer codigo;
	
	@Column(name = "DESCRICAO_BRINDE", nullable = true)
	private String descricao;
	
	@Column(name = "VENDE_BRINDE_SEPARADO", nullable = true)
	private Boolean permiteVendaSeparada;
	
	@OneToMany(mappedBy = "brinde")
	protected List<ProdutoEdicao> listaProdutoEdicao = new ArrayList<ProdutoEdicao>();
	
	/**
	 * Construtor.
	 */
	public Brinde() {
		
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
	
	/**
	 * @return the codigo
	 */
	public Integer getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the permiteVendaSeparada
	 */
	public Boolean getPermiteVendaSeparada() {
		return permiteVendaSeparada;
	}

	/**
	 * @param permiteVendaSeparada the permiteVendaSeparada to set
	 */
	public void setPermiteVendaSeparada(Boolean permiteVendaSeparada) {
		this.permiteVendaSeparada = permiteVendaSeparada;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime
				* result
				+ ((permiteVendaSeparada == null) ? 0 : permiteVendaSeparada
						.hashCode());
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
		Brinde other = (Brinde) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (permiteVendaSeparada == null) {
			if (other.permiteVendaSeparada != null)
				return false;
		} else if (!permiteVendaSeparada.equals(other.permiteVendaSeparada))
			return false;
		return true;
	}
	
}
