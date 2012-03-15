package br.com.abril.nds.service;

import java.io.File;
import java.util.HashMap;

import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.util.TemplateManager.TemplateNames;

/**
 * Interface para envio de e-mails.
 * 
 * @author Discover Technology
 *
 */
public interface EmailService {
	
	/**
	 * Envia um e-mail apenas com mensagem.
	 * @param assunto
	 * @param mensagem
	 * @param destinatarios
	 * @throws AutenticacaoEmailException
	 */
	void enviar(String assunto, String mensagem, String[] destinatarios)throws AutenticacaoEmailException;
	
	/**
	 * Envia e-mail com mensagem e anexos
	 * @param assunto
	 * @param mensagem
	 * @param destinatarios
	 * @param anexo
	 * @throws AutenticacaoEmailException
	 */
	void enviar(String assunto, String mensagem, String[] destinatarios,File[] anexo)throws AutenticacaoEmailException;
	
	/**
	 * Envia e-mail de um determinado template
	 * @param assunto
	 * @param destinatarios
	 * @param template
	 * @param parametros
	 * @throws AutenticacaoEmailException
	 */
	void enviar(String assunto,String[] destinatarios,TemplateNames template,HashMap<String,Object> parametros)throws AutenticacaoEmailException;
	
	/**
	 * Envia e-mail de um determinado template, com anexos
	 * @param assunto
	 * @param destinatarios
	 * @param anexo
	 * @param template
	 * @param parametros
	 * @throws AutenticacaoEmailException
	 */
	void enviar(String assunto,String[] destinatarios,File[] anexo,TemplateNames template,HashMap<String,Object> parametros)throws AutenticacaoEmailException;
	
}
