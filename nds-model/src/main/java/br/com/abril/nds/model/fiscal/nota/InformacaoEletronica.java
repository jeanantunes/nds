package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class InformacaoEletronica implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1437799836223424413L;
	
	/**
	 * Chave de acesso gerada pelo sistema
	 */
	@Column(name = "CHAVE_ACESSO", length = 44)
	private String chaveAcesso;
	
	/**
	 * Informações do retorno da comunicação eletrônica
	 */
	@Embedded
	private RetornoComunicacaoEletronica retornoComunicacaoEletronica;
	
	/**
	 * Construtor padrão.
	 */
	public InformacaoEletronica() {
		
	}

	/**
	 * @return the retornoComunicacaoEletronica
	 */
	public RetornoComunicacaoEletronica getRetornoComunicacaoEletronica() {
		return retornoComunicacaoEletronica;
	}

	/**
	 * @param retornoComunicacaoEletronica the retornoComunicacaoEletronica to set
	 */
	public void setRetornoComunicacaoEletronica(
			RetornoComunicacaoEletronica retornoComunicacaoEletronica) {
		this.retornoComunicacaoEletronica = retornoComunicacaoEletronica;
	}

	/**
	 * @return the chaveAcesso
	 */
	public String getChaveAcesso() {
		return chaveAcesso;
	}

	/**
	 * @param chaveAcesso the chaveAcesso to set
	 */
	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

}
