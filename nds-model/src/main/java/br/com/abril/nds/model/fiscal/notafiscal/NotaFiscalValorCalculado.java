package br.com.abril.nds.model.fiscal.notafiscal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="NOTA_FISCAL_VALOR_CALCULADO")
@XmlAccessorType(XmlAccessType.FIELD)
public class NotaFiscalValorCalculado implements Serializable {
	
	private static final long serialVersionUID = -5476612050904249652L;
	
	@Id
	@GeneratedValue()
	@Column(name="ID")
	@XmlTransient
	private Long id;
	
	@XmlElement(name="ICMSTot")
	private ValoresCalculadosWrapper valoresCalculados;
	
	public ValoresCalculadosWrapper getValoresCalculados() {
		return valoresCalculados;
	}

	public void setValoresCalculados(ValoresCalculadosWrapper valoresCalculados) {
		this.valoresCalculados = valoresCalculados;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
