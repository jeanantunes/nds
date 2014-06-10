package br.com.abril.nds.exception;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.enums.CodigoErro;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;

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
	
	private CodigoErro codigoErro;
	
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
	 * Construtor.
	 * 
	 * @param tipoMensagem - tipo de mensagem
	 * @param mensagem - mensagem
	 * @param codigoErro
	 */
	public ValidacaoException(TipoMensagem tipoMensagem, String mensagem, CodigoErro codigoErro) {
		this.codigoErro = codigoErro;
		this.validacao = new ValidacaoVO(tipoMensagem, mensagem);
	}
	
	/**
	 * Construtor.
	 * 
	 * @param tipoMensagem - tipo de mensagem
	 * @param mensagem - mensagem
	 * @param tratarValidacao - para tratamento de mensagem na resposta do erro em javascript
	 */
	public ValidacaoException(TipoMensagem tipoMensagem, String mensagem, boolean tratarValidacao) {
		
		this.validacao = new ValidacaoVO(tipoMensagem, mensagem,tratarValidacao);
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
	

	public CodigoErro getCodigoErro() {
		return codigoErro;
	}

	public void setCodigoErro(CodigoErro codigoErro) {
		this.codigoErro = codigoErro;
	}

	@Override
	public String getMessage() {
		
		if (this.validacao == null) {
			
			return "";
		}
		
		StringBuilder fullMessage = new StringBuilder();
		
		for (String message : this.validacao.getListaMensagens()) {
			
			fullMessage.append(message);
			fullMessage.append("\n");
		}
		
		return fullMessage.toString();
	}
}
