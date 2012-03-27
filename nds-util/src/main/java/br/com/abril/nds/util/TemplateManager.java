package br.com.abril.nds.util;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * Classe utilitária para gerenciamento de templates
 * utilizando Velocity.
 * 
 * @author Discover Technology
 *
 */
public abstract class TemplateManager {
	
	
	private static Logger logger = Logger.getLogger(TemplateManager.class);
	
	/**
	 * Enum para os nomes dos templates.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum TemplateNames {

		TESTE("template/templateTeste.vm","template/logo_sistema.png","template/logo_siste1.png");
		
		private String nome;
		private String[] anexos;
		
		/**
		 * Construtor.
		 * 
		 * @param nome - nome do template
		 */
		private TemplateNames(String nome,String... anexos) {
			this.nome = nome;
			this.anexos = anexos;
		}
		
		/**
		 * Obtém o nome do template
		 * 
		 * @return String
		 */
		public String getNome() {
			return this.nome;
		}
		
		public String[] getAnexos() {
			return anexos;
		}
	}
	
	/**
	 * Retorna uma lista de anexos referente a um determinado template
	 * @param templateNames
	 * @return List<File>
	 */
	public static List<File> getAnexosTemplate(TemplateNames templateNames){
		
		List<File> anexosTemplate = new ArrayList<File>();
		
		if(templateNames.getAnexos()!= null){
			
			File file = null;
			
			for(String nomeAnexo : templateNames.getAnexos()){
				
				try{
					
					URL url  =Thread.currentThread().getContextClassLoader().getResource(nomeAnexo);
					
					file = new File (url.toURI());
					
					if(file.exists()){
						anexosTemplate.add(file);
					}

				}catch (Exception e){
					logger.fatal("Erro na leitura de anexo para template de e-mail", e);
				}
				
			}
		}
		return anexosTemplate;
	}
	
	/**
	 * Obtém o template.
	 * 
	 * @param nomeTemplate - o nome do template
	 * @param parametros - os parametros usados no template
	 * 
	 * @return String
	 */
	public static String getTemplate(TemplateNames nomeTemplate, Map<String, Object> parametros) {
		
        StringWriter writer = new StringWriter();
		
		try {
	        VelocityEngine velocityEngine = new VelocityEngine();
	        
	        velocityEngine.setProperty("resource.loader", "class");

	        velocityEngine.setProperty(
	        	"class.resource.loader.class", 
	        		"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
	        
	        velocityEngine.init();
	        
	        Template template = velocityEngine.getTemplate(nomeTemplate.getNome());

	        template.merge(new VelocityContext(parametros), writer);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return writer.toString();
	}

}
