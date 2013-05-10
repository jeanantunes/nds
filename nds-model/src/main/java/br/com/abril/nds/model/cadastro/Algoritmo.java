package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entidade que representa a tabela Algoritmo.
 * 
 * @author Discover Technology
 */
@Entity
@Table(name="ALGORITMO")
@SequenceGenerator(name="ALGORTIMO_SEQ", initialValue = 1, allocationSize = 1)
public class Algoritmo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4158811574730523034L;

	@Id
	@Column(name="ID")
	@GeneratedValue(generator = "ALGORTIMO_SEQ")
	private Long id;

	@Column(name="DESCRICAO")
	private String descricao;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="algoritmo")
	private List<Produto> listaProdutos;
	
	/**
	 * Construtor Padrão
	 */
	public Algoritmo() {
		
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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
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
		Algoritmo other = (Algoritmo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @return the listaProdutos
	 */
	public List<Produto> getListaProdutos() {
		return listaProdutos;
	}

	/**
	 * @param listaProdutos the listaProdutos to set
	 */
	public void setListaProdutos(List<Produto> listaProdutos) {
		this.listaProdutos = listaProdutos;
	}
	
}
