package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;

@Embeddable
@XmlType(namespace="InformacaoTransporte")
@XmlAccessorType(XmlAccessType.FIELD)
public class InformacaoTransporte implements Serializable {


	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1459121120584386961L;
	
	/**
	 * modFrete
	 */
	@Column(name="MODALIDADE_FRETE", length=1, nullable=true)
	@NFEExport(secao = TipoSecao.X, posicao = 0)
	@XmlElement(name="modFrete")
	private Integer modalidadeFrete;
	
	@XmlElement(name="transporta")
	private TransportadorWrapper transportadorWrapper;

	/**
	 * @return the modalidadeFrete
	 */
	public Integer getModalidadeFrete() {
		return modalidadeFrete;
	}
	
	/**
	 * @param modalidadeFrete the modalidadeFrete to set
	 */
	public void setModalidadeFrete(Integer modalidadeFrete) {
		this.modalidadeFrete = modalidadeFrete;
	}
	
	public TransportadorWrapper getTransportadorWrapper() {
		return transportadorWrapper;
	}

	public void setTransportadorWrapper(TransportadorWrapper transportadorWrapper) {
		this.transportadorWrapper = transportadorWrapper;
	}
	
}
