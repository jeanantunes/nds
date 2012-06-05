package br.com.abril.nds.dto;

import java.io.Serializable;

public class DocumentoConferenciaEncalheDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private byte[] documentoSlip;
	
	private byte[] documentoCobranca;

	/**
	 * Obtém documentoSlip
	 *
	 * @return byte[]
	 */
	public byte[] getDocumentoSlip() {
		return documentoSlip;
	}

	/**
	 * Atribuí documentoSlip
	 * @param documentoSlip 
	 */
	public void setDocumentoSlip(byte[] documentoSlip) {
		this.documentoSlip = documentoSlip;
	}

	/**
	 * Obtém documentoCobranca
	 *
	 * @return byte[]
	 */
	public byte[] getDocumentoCobranca() {
		return documentoCobranca;
	}

	/**
	 * Atribuí documentoCobranca
	 * @param documentoCobranca 
	 */
	public void setDocumentoCobranca(byte[] documentoCobranca) {
		this.documentoCobranca = documentoCobranca;
	}

	
}
