package br.com.abril.nds.model.fiscal.notafiscal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="NOTA_FISCAL_TELEFONE")
public class NotaFiscalTelefone implements Serializable {
	
	private static final long serialVersionUID = -1200376001600367932L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name = "TIPO_TELEFONE", nullable = true)
	private String tipoTelefone;
	
	@Column(name = "DDI", nullable = true)
	private String DDI;
	
	@Column(name = "DDD", nullable = true)
	private String DDD;
	
	@Column(name = "NUMERO", nullable = true)
	private String numero;
	
	@Column(name = "RAMAL", nullable = true)
	private String ramal;

	@XmlTransient
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlTransient
	public String getTipoTelefone() {
		return tipoTelefone;
	}

	public void setTipoTelefone(String tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
	}

	@XmlTransient
	public String getDDI() {
		return DDI;
	}

	public void setDDI(String dDI) {
		DDI = dDI;
	}

	@XmlTransient
	public String getDDD() {
		return DDD;
	}

	public void setDDD(String dDD) {
		DDD = dDD;
	}

	@XmlTransient
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	@XmlTransient
	public String getRamal() {
		return ramal;
	}

	public void setRamal(String ramal) {
		this.ramal = ramal;
	}
	
	@Override
	public String toString() {
		return "NotaFiscalTelefone [id=" + id + ", tipoTelefone=" + tipoTelefone + ", DDI=" + DDI + ", DDD=" + DDD + ", numero=" + numero + "]";
	}
}