package br.com.abril.nds.model.fiscal.notafiscal.signature;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class SimpleSecurityHandlerBean extends AbstractSecurityHandlerBean {
	
	protected static final Logger logger = LoggerFactory.getLogger(SimpleSecurityHandlerBean.class);
	
	private String alias;
	private char[] password;
	
	public SimpleSecurityHandlerBean() {
	
	}

	public SimpleSecurityHandlerBean(String alias, String password) {
		this.alias = alias;
		this.password = password.toCharArray();
	}

	public void handle(Element parentElement, Element elementToSign, SecurityCallBack action) {
		PrivateKey privateKey = null;
		Certificate certificate = null;
		try {
			if (getKeyStore() != null) {
				logger.debug("Recuperando credenciais de armazém tipo {}.", getKeyStore().getType());
				if (getKeyStore().containsAlias(alias)) {
					privateKey = (PrivateKey) getKeyStore().getKey(alias, password);
					logger.debug("Chave particular recuperada no formato: {}.", privateKey.getFormat());
					certificate = getKeyStore().getCertificate(alias);
					logger.debug("Certificado recuperado: {}.", ((X509Certificate) certificate).getSubjectDN());
				} else {
					throw new IllegalArgumentException(
							"Armazem configurado pelo bean 'keyStore' não contem o certificado 'alias'. "
							+ "Tente outro 'alias' para evitar a criacao do bean 'keyStore', formando o sistema a usar o armazem principal.");
				}
			} else {
				logger.debug("Recuperando credenciais da primeira chave do armazem principal em {}.", System.getProperty("javax.net.ssl.keyStore"));
				KeyStore ksKeys = KeyStore.getInstance(System.getProperty("javax.net.ssl.keyStoreType"));
				ksKeys.load(new FileInputStream(System.getProperty("javax.net.ssl.keyStore")), System.getProperty("javax.net.ssl.keyStorePassword")
						.toCharArray());
				Enumeration<String> aliases = ksKeys.aliases();
				if (aliases.hasMoreElements()) {
					String transportAlias = aliases.nextElement();
					certificate = ksKeys.getCertificate(transportAlias);
					logger.debug("Certificado: {}.", ((X509Certificate) certificate).getSubjectDN());
					privateKey = (PrivateKey) ksKeys.getKey(transportAlias, password);
				} else {
					throw new IllegalArgumentException("Armaz�m principal n�o cont�m um certificado v�lido.");
				}
			}
		} catch (Exception e) {
			logger.error("Impossivel recuperar credenciais", e);
		}
		action.doInSecurityContext(parentElement, elementToSign, certificate, privateKey);
	}
	
	public void afterPropertiesSet() throws Exception {
		if (alias == null) {
			alias = "teste";
			logger.warn("Utilzando apelido 'teste' para localizar chave particular no armazem de clientes");
		}
		if (password == null) {
			password = "teste".toCharArray();
			logger.warn("Utilzando senha 'teste' para abrir chave particular no armazem de clientes");
		}
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public void setPassword(char[] password) {
		this.password = password;
	}
}
