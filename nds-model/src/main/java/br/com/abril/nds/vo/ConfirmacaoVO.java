package br.com.abril.nds.vo;

import java.io.Serializable;

/**
 * Value Object para confirmações.
 * 
 * @author Discover Technology
 *
 */
public class ConfirmacaoVO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1526643197367343770L;
	
	private String mensagem;
	
	private boolean confirmado;
	
	/**
	 * Construtor padrão.
	 */
	public ConfirmacaoVO(boolean confirmado) {

		this.confirmado = confirmado;
	}
	
	/**
	 * Construtor
	 * @param mensagem
	 * @param confirmado
	 */
	public ConfirmacaoVO(String mensagem, boolean confirmado) {
        this.mensagem = mensagem;  
		this.confirmado = confirmado;
	}

	/**
	 * @return the mensagem
	 */
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * @param mensagem the mensagem to set
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * @return the confirmado
	 */
	public boolean isConfirmado() {
		return confirmado;
	}

	/**
	 * @param confirmado the confirmado to set
	 */
	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}
	
}
