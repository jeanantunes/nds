package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class InformacaoAdicional implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -519366092423825376L;
	
	
	/**
	 * infCpl
	 * Informações Complementares de interesse do Contribuinte
	 */
	@Column(name="INF_CPL", nullable=true, length=5000)	
	@NFEExport(secao = TipoSecao.Z, posicao = 1, tamanho = 5000)
	private String informacoesComplementares;


	/**
	 * @return the informacoesComplementares
	 */
	public String getInformacoesComplementares() {
		return informacoesComplementares;
	}


	/**
	 * @param informacoesComplementares the informacoesComplementares to set
	 */
	public void setInformacoesComplementares(String informacoesComplementares) {
		this.informacoesComplementares = informacoesComplementares;
	}

}
