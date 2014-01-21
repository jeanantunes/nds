package br.com.abril.nds.model.fiscal.notafiscal.signature;

import java.security.KeyStore;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

public abstract class AbstractSecurityHandlerBean implements SecurityHandler, InitializingBean {
	
	private KeyStore keyStore;
	private Resource location;
	
	protected KeyStore getKeyStore() {
		return keyStore;
	}
	
	public void setKeyStore(KeyStore keyStore) {
		this.keyStore = keyStore;
	}
	
	public Resource getLocation() {
		return location;
	}
	public void setLocation(Resource location) {
		this.location = location;
	}
}
