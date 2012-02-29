package br.com.abril.nds.controllers.exception;

import java.io.Serializable;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.util.TipoMensagem;

/**
 * Value Object para mensagens de validação do sistema.
 * 
 * @author Discover Technology
 *
 */
public class ValidacaoException extends RuntimeException implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 7275899052575039020L;

	private ValidacaoVO validacao;	
	
	/**
	 * Construtor.
	 * 
	 * @param tipoMensagem
	 * @param mensagem
	 */
	public ValidacaoException(TipoMensagem tipoMensagem, String mensagem) {
		
		this.validacao = new ValidacaoVO(tipoMensagem, mensagem);
	}

	/**
	 * Construtor.
	 * 
	 * @param validacao
	 */
	public ValidacaoException(ValidacaoVO validacao) {
		
		this.validacao = validacao;
	}
	
	/**
	 * Construtor padrão.
	 */
	public ValidacaoException() { }
	
	/**
	 * @return the validacao
	 */
	public ValidacaoVO getValidacao() {
		return validacao;
	}
}
