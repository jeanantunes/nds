package br.com.abril.nds.service.xml.nfe.signature.impl;

import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import br.com.abril.nds.service.xml.nfe.signature.SecurityCallBack;

@Service
public class Pkcs12SecurityHandlerBean extends AbstractSecurityHandlerBean {
    
	protected static final Logger logger = LoggerFactory.getLogger(Pkcs12SecurityHandlerBean.class);
	
	private String alias;
	private char[] password;
	
	public Pkcs12SecurityHandlerBean() {
	}

	public Pkcs12SecurityHandlerBean(String alias, String password) {
		this.alias = alias;
		this.password = password.toCharArray();
	}

	public void handle(Element sourceElement, Element elementToSign, SecurityCallBack action) {
		try {
			KeyStore.PrivateKeyEntry pkEntry = unlockPkEntry();
			PrivateKey privateKey = pkEntry.getPrivateKey(); //(PrivateKey) getKeyStore().getKey(alias, password);
			java.security.cert.Certificate certificate = pkEntry.getCertificate();
			action.doInSecurityContext(sourceElement, elementToSign, certificate, privateKey);
		} catch (Exception e) {
			throw new IllegalArgumentException("Impossivel recuperar credenciais", e);
		}
	}
	
	public X509Certificate getCertificate() throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
		return (X509Certificate) unlockPkEntry().getCertificate();
	}

	private KeyStore.PrivateKeyEntry unlockPkEntry() throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
		KeyStore keystore = getKeyStore();
		logger.debug("Pronto para recuperar a entrada {} no aramzem '{}'.", keystore, alias);
		KeyStore.PrivateKeyEntry pkEntry = (PrivateKeyEntry) keystore.getEntry(alias, new KeyStore.PasswordProtection(password));
		logger.debug("Recuperada a entrada usando o alias: '{}'.", alias);
		return pkEntry;
	}

	public void afterPropertiesSet() throws Exception {
		if (alias != null && !alias.isEmpty()) {
			if (password != null && password.length>0) {
				loadKeyStore();
			}
			else {
				logger.warn("Senha nao inicializada, armazem nao sera aberto agora.");
			}
		}
		else {
			logger.warn("Alias (apelido) nao inicializado, armazem nao sera aberto agora.");
		}
	}
	
	public void loadKeyStore() {
		try {
			KeyStore ksKeys = KeyStore.getInstance("PKCS12");
			ksKeys.load(getLocation().getInputStream(), password);
			super.setKeyStore(ksKeys);
			logger.info("Aberto armazem {} localizado em {}.", ksKeys, getLocation());
			if (!getKeyStore().isKeyEntry(alias)) {
				logger.warn("Nao existe chave particular para o alias '{}' em {}.", alias, getLocation());
				throw new RuntimeException("Nao existe chave particular no armazem designado.");
			}
		} catch (FileNotFoundException e) {
			logger.warn("Armazém não localizado em {}.", getLocation());
		} catch (Exception e) {
			logger.warn("Erro ao abrir armazém localizado em {}.", getLocation());
			throw new RuntimeException("Erro ao abrir armazém, ", e);
		}
		
	}
	
	@Override
	public void setKeyStore(KeyStore keyStore) {
		throw new IllegalArgumentException("Utilize alias, password e location para estabelecer um armazem.");
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}
}
