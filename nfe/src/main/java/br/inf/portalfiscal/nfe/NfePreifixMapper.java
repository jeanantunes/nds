package br.inf.portalfiscal.nfe;

import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;

public class NfePreifixMapper extends NamespacePrefixMapper {

	private static final String URI ="http://www.portalfiscal.inf.br/nfe";

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {

    	if(URI.equals(namespaceUri)) {
    		return "";
    	} 

    	if ("".equals(namespaceUri)) {
            return "cumpade";
        }
    	
    	return suggestion;
    }
    
    @Override
    public String[] getPreDeclaredNamespaceUris() {
    	return new String[] { URI };
    }
	
    public String getNamespaceURI(String prefix) {
       if("xmlns".equals(prefix)) {
          return  "";
       }
       return "";
    }
    
}
