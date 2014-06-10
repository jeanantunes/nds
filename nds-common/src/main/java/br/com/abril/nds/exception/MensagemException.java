package br.com.abril.nds.exception;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Value Object para mensagens de validação do sistema utilizada onde nao aplica rollback.
 * 
 * @author Discover Technology
 *
 */
public class MensagemException extends RuntimeException implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 7275899052575039021L;

	private ValidacaoVO validacao;	
	
	private String url;
	
	/**
	 * Construtor.
	 * 
	 * @param tipoMensagem - tipo de mensagem
	 * @param mensagem - mensagem
	 */
	public MensagemException(TipoMensagem tipoMensagem, String mensagem) {
		
		this.validacao = new ValidacaoVO(tipoMensagem, mensagem);
	}
	
	/**
	 * Construtor.
	 * 
	 * @param tipoMensagem - tipo de mensagem
	 * @param mensagem - mensagem
	 * @param tratarValidacao - para tratamento de mensagem na resposta do erro em javascript
	 */
	public MensagemException(TipoMensagem tipoMensagem, String mensagem, boolean tratarValidacao) {
		
		this.validacao = new ValidacaoVO(tipoMensagem, mensagem,tratarValidacao);
	}

	
	/**
	 * 
	 * @param tipoMensagem
	 * @param listaMensagens
	 */
	public MensagemException (TipoMensagem tipoMensagem, List<String> listaMensagens){
		this.validacao = new ValidacaoVO(tipoMensagem, listaMensagens);
	}
	/**
	 * Construtor.
	 * 
	 * @param validacao - objeto de validação
	 */
	public MensagemException(ValidacaoVO validacao) {
		
		this.validacao = validacao;
	}
	
	/**
	 * Construtor.
	 * 
	 * @param url - url para forward
	 * @param tipoMensagem - tipo de mensagem
	 * @param mensagem - mensagem
	 */
	public MensagemException(String url, TipoMensagem tipoMensagem, String mensagem) {
		
		this.url = url; 
		this.validacao = new ValidacaoVO(tipoMensagem, mensagem);
	}
	
	/**
	 * Construtor.
	 * 
	 * @param url - url para forward
	 * @param validacao - objeto de validação
	 */
	public MensagemException(String url, ValidacaoVO validacao) {
		
		this.url = url;
		this.validacao = validacao;
	}
	
	/**
	 * Construtor padrão.
	 */
	public MensagemException() { }
	
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
