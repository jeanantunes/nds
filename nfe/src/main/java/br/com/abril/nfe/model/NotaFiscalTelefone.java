package br.com.abril.nfe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.abril.nfe.model.interfaces.Telefone;

@Entity
@Table(name="NOTA_FISCAL_TELEFONE")
public class NotaFiscalTelefone implements Serializable, Telefone {
	
	private static final long serialVersionUID = -1200376001600367932L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name = "TIPO_TELEFONE", nullable = false)
	private String tipoTelefone;
	
	@Column(name = "DDI", nullable = false)
	private Long DDI;
	
	@Column(name = "DDD", nullable = false)
	private Long DDD;
	
	@Column(name = "NUMERO", nullable = false)
	private Long numero;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoTelefone() {
		return tipoTelefone;
	}

	public void setTipoTelefone(String tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
	}

	public Long getDDI() {
		return DDI;
	}

	public void setDDI(Long dDI) {
		DDI = dDI;
	}

	public Long getDDD() {
		return DDD;
	}

	public void setDDD(Long dDD) {
		DDD = dDD;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	@Override
	public String toString() {
		return "NotaFiscalTelefone [id=" + id + ", tipoTelefone="
				+ tipoTelefone + ", DDI=" + DDI + ", DDD=" + DDD + ", numero="
				+ numero + "]";
	}
}
