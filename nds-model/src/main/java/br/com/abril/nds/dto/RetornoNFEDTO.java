package br.com.abril.nds.dto;

import java.util.Date;

import br.com.abril.nds.model.fiscal.nota.Status;

/**
 * Value Object que representa os retornos dos arquivos de nota fiscal eletrônica . 
 *
 * @author Discover Technology
 *
 */
public class RetornoNFEDTO {

	private Long idNotaFiscal;
	private String cpfCnpj;
	private String chaveAcesso;
	private Status status;
	private Long protocolo;
	private String motivo;
	private Date dataRecebimento;

	/**
	 * @return the idNotaFiscal
	 */
	public Long getIdNotaFiscal() {
		return idNotaFiscal;
	}

	/**
	 * @param idNotaFiscal
	 *            the idNotaFiscal to set
	 */
	public void setIdNotaFiscal(Long idNotaFiscal) {
		this.idNotaFiscal = idNotaFiscal;
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
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Status status) {
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
}
