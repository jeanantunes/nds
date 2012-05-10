package br.com.abril.nds.integracao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "parametro_sistema")

@NamedQuery(
		name="findAllNdsiParameters", 
		query= "SELECT p FROM NdsiParametroSistema p WHERE p.tipoParametroSistema LIKE :prefix")

public class NdsiParametroSistema implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "TIPO_PARAMETRO_SISTEMA", nullable = false, length = 255)
	private String tipoParametroSistema;

	@Column(name = "VALOR", nullable = false, length = 255)
	private String valor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoParametroSistema() {
		return tipoParametroSistema;
	}

	public void setTipoParametroSistema(String tipoParametroSistema) {
		this.tipoParametroSistema = tipoParametroSistema;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		NdsiParametroSistema other = (NdsiParametroSistema) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NdsiParametroSistema [id=" + id + ", tipoParametroSistema="
				+ tipoParametroSistema + ", valor=" + valor + "]";
	}

}
