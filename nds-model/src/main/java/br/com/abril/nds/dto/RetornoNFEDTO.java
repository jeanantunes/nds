package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.fiscal.nota.StatusRetornado;

/**
 * Value Object que representa os retornos dos arquivos de nota fiscal eletrônica . 
 *
 * @author Discover Technology
 *
 */
public class RetornoNFEDTO implements Serializable {

	private static final long serialVersionUID = 9039066467977718266L;
	
	private Long idNota;
	
	private Long numeroNotaFiscal;
	
	private String cpfCnpj;
	
	private String chaveAcesso;
	
	private StatusRetornado status;
	
	private Long protocolo;
	
	private String motivo;
	
	private Date dataRecebimento;
	
	private String tpEvento;
	
	public Long getIdNota() {
		return idNota;
	}

	public void setIdNota(Long idNota) {
		this.idNota = idNota;
	}

	public Long getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	public void setNumeroNotaFiscal(Long numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	/**
	 * @return the chaveAcesso
	 */
	public String getChaveAcesso() {
		return chaveAcesso;
	}

	/**
	 * @param chaveAcesso
	 *            the chaveAcesso to set
	 */
	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

	/**
	 * @return the status
	 */
	public StatusRetornado getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(StatusRetornado status) {
		this.status = status;
	}

	/**
	 * @return the protocolo
	 */
	public Long getProtocolo() {
		return protocolo;
	}

	/**
	 * @param protocolo
	 *            the protocolo to set
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
	 * @param motivo
	 *            the motivo to set
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
	 * @param dataRecebimento
	 *            the dataRecebimento to set
	 */
	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	/**
	 * @return the cpfCnpj
	 */
	public String getCpfCnpj() {
		return cpfCnpj;
	}

	/**
	 * @param cpfCnpj the cpfCnpj to set
	 */
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getTpEvento() {
		return tpEvento;
	}

	public void setTpEvento(String tpEvento) {
		this.tpEvento = tpEvento;
	}
	
}