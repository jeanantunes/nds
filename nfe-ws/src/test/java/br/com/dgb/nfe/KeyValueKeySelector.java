package br.com.dgb.nfe;

import java.security.Key;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;

public class KeyValueKeySelector extends KeySelector {

	public KeySelectorResult select(KeyInfo keyInfo,  KeySelector.Purpose purpose, AlgorithmMethod method,  XMLCryptoContext context) throws KeySelectorException {

	    if (keyInfo == null) {
	      throw new KeySelectorException("Null KeyInfo object!");
	    }

	    SignatureMethod sm = (SignatureMethod) method;
	    List list = keyInfo.getContent();

	    for (int i = 0; i < list.size(); i++) {

	        XMLStructure xmlStructure = (XMLStructure) list.get(i);

	        PublicKey pk = null;

	        if(xmlStructure instanceof X509Data){                   
	              for (Object data : ((X509Data) xmlStructure).getContent()) {
	                  if (data instanceof X509Certificate) {
	                      System.out.println("x509Certificate");
	                      pk = ((X509Certificate)data).getPublicKey();
	                      System.out.println( ((X509Certificate) data).getSubjectDN().getName() );
	                      // make sure algorithm is compatible with method
	                      if (algEquals(sm.getAlgorithm(),pk.getAlgorithm())) {
	                          return new SimpleKeySelectorResult(pk);
	                      }
	                  }
	              }

	        }
	    }
	    throw new KeySelectorException("No KeyValue element     found!");
	  }

	  public boolean algEquals(String algURI, String algName) {
	    if (algName.equalsIgnoreCase("DSA") &&
	        algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1)) {
	      return true;
	    } else if (algName.equalsIgnoreCase("RSA") &&
	        algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1)) {
	      return true;
	    } else {
	      return false;
	    }
	  }


	  public class SimpleKeySelectorResult implements KeySelectorResult {
	        private PublicKey pk;

	        SimpleKeySelectorResult(PublicKey pk) {
	                this.pk = pk;
	        }

	        @Override
	        public Key getKey() {
	                return this.pk;
	        }
	}
}

