package br.com.abril.nds.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.util.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Classe utilitária para gerenciamento de templates utilizando Velocity.
 * 
 * @author Discover Technology
 * 
 */
public abstract class TemplateManager {
	
	
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateManager.class);
    private static final Marker FATAL = MarkerFactory.getMarker("FATAL");
	
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
	public static List<AnexoEmail> getAnexosTemplate(TemplateNames templateNames){
		
		List<AnexoEmail> anexosTemplate = new ArrayList<AnexoEmail>();
		
		if(templateNames.getAnexos()!= null){
			
			AnexoEmail anexoEmail = null;
			
			for(String nomeAnexo : templateNames.getAnexos()){
				
				try{
					
					byte[] anexo = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream(nomeAnexo));
					
					anexoEmail = new AnexoEmail();
					anexoEmail.setNome(nomeAnexo);
					anexoEmail.setAnexo(anexo);
					
					anexosTemplate.add(anexoEmail);

				}catch (Exception e){
                    LOGGER.error(FATAL, "Erro na leitura de anexo para template de e-mail", e);
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
