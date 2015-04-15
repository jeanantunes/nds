package br.com.abril.nds.model.fiscal.nota;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="NFe")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoteNotasFiscais {

	private List<NotaFiscal> notasFiscais;
	
	LoteNotasFiscais(List<NotaFiscal> notasFiscais) {
		this.notasFiscais = notasFiscais;
	}

	public List<NotaFiscal> getNotasFiscais() {
		return notasFiscais;
	}
	
}