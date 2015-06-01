package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.abril.nds.model.seguranca.Usuario;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "BOX")
@SequenceGenerator(name="BOX_SEQ", initialValue = 1, allocationSize = 1)
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
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
	
	@OneToOne(mappedBy="box")
	@JoinColumn(name="ROTEIRIZACAO_ID", unique=true)
	private Roteirizacao roteirizacao;

	@OneToMany//Muitos usuários devido ao problema de lock de box
	private List<Usuario> usuarios = new ArrayList<Usuario>();
	
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

	public Roteirizacao getRoteirizacao() {
		return roteirizacao;
	}

	public void setRoteirizacao(Roteirizacao roteirizacao) {
		this.roteirizacao = roteirizacao;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((this.getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((this.getNome() == null) ? 0 : getNome().hashCode());
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
		if (this.getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!this.getCodigo().equals(other.getCodigo()))
			return false;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getNome() == null) {
			if (other.getNome() != null)
				return false;
		} else if (!this.getNome().equals(other.getNome()))
			return false;
		
		return true;
	}
}