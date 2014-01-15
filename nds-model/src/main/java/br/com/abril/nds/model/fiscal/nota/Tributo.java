package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="TRIBUTO")
public class Tributo implements Serializable {

	private static final long serialVersionUID = 5392503333835977858L;
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name="DESCRICAO")
	private String descricao;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="pk.tributo", cascade=CascadeType.ALL)
	Set<GrupoTributoAliquota> grupoTributos = new HashSet<GrupoTributoAliquota>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Set<GrupoTributoAliquota> getGrupoTributos() {
		return grupoTributos;
	}

	public void setGrupoTributos(Set<GrupoTributoAliquota> grupoTributos) {
		this.grupoTributos = grupoTributos;
	}
}
