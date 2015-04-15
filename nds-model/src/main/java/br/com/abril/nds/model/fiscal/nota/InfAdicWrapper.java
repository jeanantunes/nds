package br.com.abril.nds.model.fiscal.nota;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class InfAdicWrapper {
	
	@Column(name = "INFORMACOES_ADICIONAIS")
	@XmlElement(name="infCpl")
	private	String informacoesAdicionais;

	public String getInformacoesAdicionais() {
		return informacoesAdicionais;
	}

	public void setInformacoesAdicionais(String informacoesAdicionais) {
		this.informacoesAdicionais = informacoesAdicionais;
	}

}
