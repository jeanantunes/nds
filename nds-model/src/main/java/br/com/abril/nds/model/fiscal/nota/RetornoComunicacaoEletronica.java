package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class RetornoComunicacaoEletronica implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5252804668668107697L;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_RETORNADO")
	private StatusRetornado statusRetornado;
		
	@Column(name = "PROTOCOLO", length = 20)
	private Long protocolo;
	
	@Column(name = "MOTIVO", length = 255)
	private String motivo;
	
	@Column(name = "DATA_RECEBIMENTO")
	private Date dataRecebimento;
	
	/**
	 * Construtor padrão.
	 */
	public RetornoComunicacaoEletronica() {
		
	}

	/**
	 * @return the status
	 */
	public StatusRetornado getStatusRetornado() {
		return statusRetornado;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatusRetornado(StatusRetornado statusRetornado) {
		this.statusRetornado = statusRetornado;
	}

	/**
	 * @return the protocolo
	 */
	public Long getProtocolo() {
		return protocolo;
	}

	/**
	 * @param protocolo the protocolo to set
	 */
	public void setProtocolo(Long protocolo) {
		this.protocolo = protocolo;
	}

	/**
	 * @return the motivo
	 */
	public String getMotivo() {
		return motivo;
	}

	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	/**
	 * @return the dataRecebimento
	 */
	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	/**
	 * @param dataRecebimento the dataRecebimento to set
	 */
	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}
			
}
