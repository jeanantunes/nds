package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

//@Entity
//@Table(name="GRUPO_TRIBUTO")
public class GrupoTributo implements Serializable {
/*
	private static final long serialVersionUID = 5392503333835977858L;
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name="DESCRICAO")
	private String descricao;
	
	@OneToMany
	private List<Aliquota> aliquotas;

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

	public List<Aliquota> getAliquotas() {
		return aliquotas;
	}

	public void setAliquotas(List<Aliquota> aliquotas) {
		this.aliquotas = aliquotas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
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
		GrupoTributo other = (GrupoTributo) obj;
		if (this.getId()== null) {
			if (other.id != null)
				return false;
		} else if (!this.getId().equals(other.id))
			return false;
		return true;
	}*/
}
