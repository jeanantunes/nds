package br.com.abril.nds.model.fiscal.notafiscal.signature;

import java.security.Provider;

import javax.xml.crypto.dsig.XMLSignatureFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class DOMNFeSigantureFactoryBean implements InitializingBean, FactoryBean<XMLSignatureFactory> {

	private static final Logger logger = LoggerFactory.getLogger(DOMNFeSigantureFactoryBean.class);
	
	public static final String DEFAULT_PROVIDER_CLASS_NAME = "org.jcp.xml.dsig.internal.dom.XMLDSigRI";
	public static final String DEFAULT_PROVIDER_NAME = "jsr105Provider";
	
	private String providerClassName = "";
	private String providerName = "";
    
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	
	public String getProviderClassName() {
		return providerClassName;
	}
	public void setProviderClassName(String providerClassName) {
		this.providerClassName = providerClassName;
	}
	
	public void afterPropertiesSet() throws Exception {
		if (getProviderName().isEmpty()) {
			setProviderName(DEFAULT_PROVIDER_NAME);
			logger.warn("Provider name nao definido, usando {}", getProviderName());
		}
		if (getProviderClassName().isEmpty()) {
			setProviderClassName(DEFAULT_PROVIDER_CLASS_NAME);
			logger.warn("Provider name nao definido, usando {}", getProviderClassName());
		}
		providerName = System.getProperty(getProviderName(), getProviderClassName());
	}
	
	public XMLSignatureFactory getObject() throws Exception {
		logger.debug("Usando o provider com nome {}.", providerName);
		Provider provider = (Provider) Class.forName(providerName).newInstance();
		XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM", provider);
		logger.debug("A instancia de XMLSignatureFactory {}.", xmlSignatureFactory);
		return xmlSignatureFactory;
	}
	
	public Class<?> getObjectType() {
		return XMLSignatureFactory.class;
	}
	
	public boolean isSingleton() {
		return false;
	}
}
