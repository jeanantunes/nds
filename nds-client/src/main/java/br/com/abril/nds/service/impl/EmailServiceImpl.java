package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.TemplateManager;
import br.com.abril.nds.util.TemplateManager.TemplateNames;

/**
 * 
 * Classe de implementação de envio de e-mails
 * 
 * @author Discover Technology
 *
 */

@Service
public class EmailServiceImpl implements EmailService {
	
	private JavaMailSenderImpl mailSender;
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	

	public EmailServiceImpl() {
		mailSender = new JavaMailSenderImpl();
	}
	
	@Override
	@Transactional(readOnly=true)
	public void enviar(String assunto, String mensagem, String[] destinatarios)throws AutenticacaoEmailException {
		
		this.enviarEmail(assunto, mensagem, destinatarios, null,false);
	}
	
	@Override
	@Transactional(readOnly=true)
	public void enviar(String assunto, String mensagem, String[] destinatarios,List<AnexoEmail> anexos) throws AutenticacaoEmailException {
		
		this.enviarEmail(assunto, mensagem, destinatarios, anexos,false);
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public void enviar(String assunto, String mensagem, String[] destinatarios,AnexoEmail anexo) throws AutenticacaoEmailException {
		
		this.enviarEmail(assunto, mensagem, destinatarios, Arrays.asList(anexo),false);
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public void enviar(String assunto,String[] destinatarios,TemplateNames template,HashMap<String,Object> parametros) throws AutenticacaoEmailException {
		
		String mensagem = TemplateManager.getTemplate(template, parametros);
		
		List<AnexoEmail> anexosTemplate = TemplateManager.getAnexosTemplate(template);
		
		enviarEmail(assunto, mensagem, destinatarios, anexosTemplate,true);
	}
	
	@Override
	@Transactional(readOnly=true)
	public void enviar(String assunto, String[] destinatarios, AnexoEmail[] anexo,TemplateNames template, HashMap<String, Object> parametros)throws AutenticacaoEmailException {
		
		String mensagem = TemplateManager.getTemplate(template, parametros);
		
		List<AnexoEmail> anexosTemplate = TemplateManager.getAnexosTemplate(template);
		
		List<AnexoEmail> anexos = new ArrayList<AnexoEmail>();
	
		if(anexosTemplate!= null && !anexosTemplate.isEmpty()){
			anexos.addAll(anexosTemplate);
		}
		
		anexos.addAll(Arrays.asList(anexo));
		
		enviarEmail(assunto, mensagem, destinatarios,anexos,true);
	}
	
	
	/**
	 * Envia email conforme os parametros informados
	 * @param assunto
	 * @param mensagem
	 * @param destinatarios
	 * @param anexos
	 * @param isHtml
	 * @throws AutenticacaoEmailException
	 */
	private void enviarEmail(String assunto, String mensagem, String[] destinatarios,List<AnexoEmail> anexos, boolean isHtml) throws AutenticacaoEmailException{
		
		autenticarSmtp();
		
		MimeMessage message = mailSender.createMimeMessage();
		
		try {
			
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true,"UTF-8");
			mimeMessageHelper.setSubject(assunto);
			mimeMessageHelper.setTo(destinatarios);
			mimeMessageHelper.setText(mensagem,isHtml);
			mimeMessageHelper.setFrom(mailSender.getUsername());
			
			if(anexos!= null && !anexos.isEmpty()){
				
				for(AnexoEmail anexo : anexos){
					
					ByteArrayResource byteArrayResource = new ByteArrayResource(anexo.getAnexo());
					
					mimeMessageHelper.addAttachment(anexo.getNome(),byteArrayResource);
					
				}
			}
			
			mailSender.send(message);
			
		} catch (Exception e) {
			throw new AutenticacaoEmailException(e);
		}
	}
	
	/**
	 * Obtem os parametros de autenticação de smtp do email do sistema
	 * @throws AutenticacaoEmailException
	 */
	private void autenticarSmtp() throws AutenticacaoEmailException{
		
		ParametroSistema host = parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.EMAIL_HOST);
		validarParametrosAutenticacao(host, TipoParametroSistema.EMAIL_HOST);
		
		ParametroSistema porta = parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.EMAIL_PORTA);
		validarParametrosAutenticacao(porta, TipoParametroSistema.EMAIL_PORTA);
		
		ParametroSistema senha =  parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.EMAIL_SENHA);
		validarParametrosAutenticacao(senha, TipoParametroSistema.EMAIL_SENHA);
		
		ParametroSistema smtp = parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.EMAIL_PROTOCOLO);
		validarParametrosAutenticacao(smtp, TipoParametroSistema.EMAIL_PROTOCOLO);
		
		ParametroSistema usuario = parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.EMAIL_USUARIO);
		validarParametrosAutenticacao(usuario, TipoParametroSistema.EMAIL_USUARIO);
		
		mailSender.setPort(Integer.valueOf(porta.getValor()));
		mailSender.setHost(host.getValor());
		mailSender.setPassword(senha.getValor());
		mailSender.setUsername(usuario.getValor());
		mailSender.setProtocol(smtp.getValor());
		
		mailSender.getJavaMailProperties().setProperty("mail.smtps.auth", "true");
		mailSender.getJavaMailProperties().setProperty("mail.smtps.starttls.enable", "true");
	}
	
	/**
	 * Valida os parametros  de autenticação de envio de e-mail
	 * @param parametroSistema
	 * @param tipoParametroSistema
	 * @throws AutenticacaoEmailException
	 */
	private void validarParametrosAutenticacao(ParametroSistema parametroSistema,TipoParametroSistema tipoParametroSistema) throws AutenticacaoEmailException{
		
		if(parametroSistema == null || parametroSistema.getValor().isEmpty()){
			throw new AutenticacaoEmailException(getMensagemDeErroAutenticacao(tipoParametroSistema));
		}
	}
	
	/**
	 * Retorna mensagem de erro de parametro invalido para autenticação de email
	 * @param parametroSistema
	 * @return
	 */
	private String getMensagemDeErroAutenticacao(TipoParametroSistema parametroSistema){
		
		StringBuilder msg = new StringBuilder();
		msg.append("Erro na configuração dos parâmetros de envio de e-mail. Parâmetro ");
		
		switch (parametroSistema) {
			case EMAIL_HOST:
				msg.append("[HOST]");
				break;
			
			case EMAIL_SENHA:
				msg.append("[SENHA]");
				break;
				
			case EMAIL_PORTA:
				msg.append("[PORTA]");
				break;
				
			case EMAIL_PROTOCOLO:
				msg.append("[PROTOCOLO]");
				break;
				
			case EMAIL_USUARIO:
				msg.append("[USUARIO]");
				break;
				
			default: return null;
		}
		
		msg.append("não foi configurado no sistema!");
		
		return msg.toString();
	}
	
}
