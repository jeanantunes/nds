package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.NotaPromissoria;

public class NotaPromissoriaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 115720324825344999L;
	
	
	
	
	private String nomeBeneficiario;
	private String documentoBeneficiario;
	private NotaPromissoria notaPromissoria;
	private String pracaPagamento;
	private String nomeEmitente;
	private String documentoEmitente;
	private Endereco enderecoEmitente;
	
	/**
	 * @return the nomeBeneficiario
	 */
	public String getNomeBeneficiario() {
		return nomeBeneficiario;
	}
	/**
	 * @param nomeBeneficiario the nomeBeneficiario to set
	 */
	public void setNomeBeneficiario(String nomeBeneficiario) {
		this.nomeBeneficiario = nomeBeneficiario;
	}
	
	/**
	 * @return the pracaPagamento
	 */
	public String getPracaPagamento() {
		return pracaPagamento;
	}
	/**
	 * @param pracaPagamento the pracaPagamento to set
	 */
	public void setPracaPagamento(String pracaPagamento) {
		this.pracaPagamento = pracaPagamento;
	}
	
	/**
	 * @return the documentoEmitente
	 */
	public String getDocumentoEmitente() {
		return documentoEmitente;
	}
	/**
	 * @param documentoEmitente the documentoEmitente to set
	 */
	public void setDocumentoEmitente(String documentoEmitente) {
		this.documentoEmitente = documentoEmitente;
	}
	/**
	 * @return the enderecoEmitente
	 */
	public Endereco getEnderecoEmitente() {
		return enderecoEmitente;
	}
	/**
	 * @param enderecoEmitente the enderecoEmitente to set
	 */
	public void setEnderecoEmitente(Endereco enderecoEmitente) {
		this.enderecoEmitente = enderecoEmitente;
	}
	/**
	 * @return the notaPromissoria
	 */
	public NotaPromissoria getNotaPromissoria() {
		return notaPromissoria;
	}
	/**
	 * @param notaPromissoria the notaPromissoria to set
	 */
	public void setNotaPromissoria(NotaPromissoria notaPromissoria) {
		this.notaPromissoria = notaPromissoria;
	}
	/**
	 * @return the documentoBeneficiario
	 */
	public String getDocumentoBeneficiario() {
		return documentoBeneficiario;
	}
	/**
	 * @param documentoBeneficiario the documentoBeneficiario to set
	 */
	public void setDocumentoBeneficiario(String documentoBeneficiario) {
		this.documentoBeneficiario = documentoBeneficiario;
	}
	/**
	 * @return the nomeEmitente
	 */
	public String getNomeEmitente() {
		return nomeEmitente;
	}
	/**
	 * @param nomeEmitente the nomeEmitente to set
	 */
	public void setNomeEmitente(String nomeEmitente) {
		this.nomeEmitente = nomeEmitente;
	}
	
	
	

}
