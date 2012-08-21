package br.com.abril.nds.exception;

import java.io.Serializable;
import java.util.List;

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
	
	private String url;
	
	/**
	 * Construtor.
	 * 
	 * @param tipoMensagem - tipo de mensagem
	 * @param mensagem - mensagem
	 */
	public ValidacaoException(TipoMensagem tipoMensagem, String mensagem) {
		
		this.validacao = new ValidacaoVO(tipoMensagem, mensagem);
	}

	
	/**
	 * 
	 * @param tipoMensagem
	 * @param listaMensagens
	 */
	public ValidacaoException (TipoMensagem tipoMensagem, List<String> listaMensagens){
		this.validacao = new ValidacaoVO(tipoMensagem, listaMensagens);
	}
	/**
	 * Construtor.
	 * 
	 * @param validacao - objeto de validação
	 */
	public ValidacaoException(ValidacaoVO validacao) {
		
		this.validacao = validacao;
	}
	
	/**
	 * Construtor.
	 * 
	 * @param url - url para forward
	 * @param tipoMensagem - tipo de mensagem
	 * @param mensagem - mensagem
	 */
	public ValidacaoException(String url, TipoMensagem tipoMensagem, String mensagem) {
		
		this.url = url; 
		this.validacao = new ValidacaoVO(tipoMensagem, mensagem);
	}
	
	/**
	 * Construtor.
	 * 
	 * @param url - url para forward
	 * @param validacao - objeto de validação
	 */
	public ValidacaoException(String url, ValidacaoVO validacao) {
		
		this.url = url;
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

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
}
