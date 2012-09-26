package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "BOX")
@SequenceGenerator(name="BOX_SEQ", initialValue = 1, allocationSize = 1)
public class Box implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8925902448339516787L;
	
	@Id
	@GeneratedValue(generator = "BOX_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CODIGO", nullable = false,unique=true, length=4)	
	private Integer codigo;
	
	@Enumerated(EnumType.STRING)	
	@Column(name = "TIPO_BOX", nullable = false)
	private TipoBox tipoBox;
	
	@Column(name="NOME")
	private String nome;
	
	@OneToMany(mappedBy = "box")
	private Set<Cota> cotas = new HashSet<Cota>();
	
	@OneToMany(mappedBy ="box")
	private Set<Roteiro> roteiros;

	public Set<Cota> getCotas() {
		return cotas;
	}

	public void setCotas(Set<Cota> cotas) {
		this.cotas = cotas;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getCodigo() {
		return codigo;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public TipoBox getTipoBox() {
		return tipoBox;
	}
	
	public void setTipoBox(TipoBox tipoBox) {
		this.tipoBox = tipoBox;
	}

	/**
	 * @return the roteiros
	 */
	public Set<Roteiro> getRoteiros() {
		return roteiros;
	}

	/**
	 * @param roteiros the roteiros to set
	 */
	public void setRoteiros(Set<Roteiro> roteiros) {
		this.roteiros = roteiros;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((cotas == null) ? 0 : cotas.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((roteiros == null) ? 0 : roteiros.hashCode());
		result = prime * result + ((tipoBox == null) ? 0 : tipoBox.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Box other = (Box) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (cotas == null) {
			if (other.cotas != null)
				return false;
		} else if (!cotas.equals(other.cotas))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;		
		if (roteiros == null) {
			if (other.roteiros != null)
				return false;
		} else if (!roteiros.equals(other.roteiros))
			return false;
		if (tipoBox != other.tipoBox)
			return false;
		return true;
	}
}