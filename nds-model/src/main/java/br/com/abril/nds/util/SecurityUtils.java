package br.com.abril.nds.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class SecurityUtils {
	
    private final static Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    static char SEP = File.separatorChar;
    static String trustStoreName = "cacerts";
    static String trustStorePath = null;
    static String trustStorePassword = "changeit";
    
    public static KeyStore openStore(String keyStoreType, Resource keyStoreResource, char[] passphrase) throws Exception {
    	logger.debug("Abrindo armazém {} ...", keyStoreResource.getFilename());
    	KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    	keyStore.load(keyStoreResource.getInputStream(), passphrase);
    	return keyStore;
    }
    
    public static KeyStore openStore(Resource keyStoreResource, char[] passphrase) throws Exception {
    	return openStore(KeyStore.getDefaultType(), keyStoreResource, passphrase);
    }

    public static KeyStore openStore(String keyStoreType, String storeLocation, char[] passphrase) throws Exception {
    	return openStore(keyStoreType, new FileSystemResource(storeLocation), passphrase);
    }

    public static KeyStore openStore(String storeLocation, char[] passphrase) throws Exception {
    	return openStore(new FileSystemResource(storeLocation), passphrase);
    }
    
    public static KeyStore openTrustStore(char[] passphrase) throws Exception {
    	return openTrustStore(trustStorePath, passphrase);
    }
    
    public static KeyStore openTrustStore(String trustStorePath, char[] passphrase) throws Exception {
    	StringBuilder storeLocation = new StringBuilder(System.getProperty("java.home"));
    	if (trustStorePath==null) {
        	storeLocation.append(SEP)
		    .append("lib")
		    .append(SEP)
			.append("security")
			.append(SEP);
    	}
    	else {
    		storeLocation.append(trustStorePath+"\\");
    	}
    	KeyStore trustStore = SecurityUtils.openStore(storeLocation.append(trustStoreName).toString(), passphrase);
    	return trustStore;
    }
    
    public static void installCertificate(String certificateLocation, String certificateName) throws Exception {
    	installCertificate(trustStorePath, certificateLocation, certificateName);
    }
    
    public static void installCertificate(String trustStorePath, String certificateLocation, String certificateName) throws Exception {
    	KeyStore trustStore = SecurityUtils.openTrustStore(trustStorePath, trustStorePassword.toCharArray());
	    File dir = new File(certificateLocation);
	    File file = new File(dir, certificateName+".cer");
	    
	    logger.debug("Abrindo certificado {} ...", file);
    	
	    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    	InputStream in = new FileInputStream(file);
    	X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
    	in.close();
    	
    	if (trustStore.containsAlias(certificateName)) {
    		logger.info("Certificado existente {}", trustStore.getCertificate(certificateName).getType());
    	}
    	
    	else {
    		trustStore.setCertificateEntry(certificateName, cert);
    		logger.info("Certificado CARREGADO {}", trustStore.getCertificate(certificateName).getType());
    	}
    }
    
    public static void main(String[] args) throws Exception {
    	String trustStorePath = null;
    	String certificateLocation = "";
    	String certificateName = "";
		if (args.length > 2) {
			trustStorePath = args[2];
			certificateLocation = args[1];
			certificateName = args[0];
		} 
		else if (args.length > 1) {
			certificateLocation = args[1];
			certificateName = args[0];
		}
		else {
		    System.out.println("Uso: java SecurityUtils <local do certificado> <nome do certificado> [local do cacerts a partir do JAVAHOME]");
		    return;
		}
		installCertificate(trustStorePath, certificateLocation, certificateName);
    }
}
