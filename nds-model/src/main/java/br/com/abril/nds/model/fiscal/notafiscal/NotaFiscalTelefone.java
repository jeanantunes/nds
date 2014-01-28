package br.com.abril.nds.model.fiscal.notafiscal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NOTA_FISCAL_TELEFONE")
public class NotaFiscalTelefone implements Serializable {
	
	private static final long serialVersionUID = -1200376001600367932L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name = "TIPO_TELEFONE", nullable = false)
	private String tipoTelefone;
	
	@Column(name = "DDI", nullable = false)
	private String DDI;
	
	@Column(name = "DDD", nullable = false)
	private String DDD;
	
	@Column(name = "NUMERO", nullable = false)
	private String numero;
	
	@Column(name = "RAMAL", nullable = true)
	private String ramal;

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

	public String getDDI() {
		return DDI;
	}

	public void setDDI(String dDI) {
		DDI = dDI;
	}

	public String getDDD() {
		return DDD;
	}

	public void setDDD(String dDD) {
		DDD = dDD;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getRamal() {
		return ramal;
	}

	public void setRamal(String ramal) {
		this.ramal = ramal;
	}

	@Override
	public String toString() {
		return "NotaFiscalTelefone [id=" + id + ", tipoTelefone="
				+ tipoTelefone + ", DDI=" + DDI + ", DDD=" + DDD + ", numero="
				+ numero + "]";
	}
}