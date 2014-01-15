package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name="aliquota")
public class Aliquota implements Serializable {

	private static final long serialVersionUID = 5392503333835977858L;
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name="VALOR")
	private BigDecimal valor;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "pk.aliquota", cascade=CascadeType.ALL)
	Set<GrupoTributoAliquota> aliquotas = new HashSet<GrupoTributoAliquota>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public Set<GrupoTributoAliquota> getAliquotas() {
		return aliquotas;
	}

	public void setAliquotas(Set<GrupoTributoAliquota> aliquotas) {
		this.aliquotas = aliquotas;
	}
}
