/**
 * DevolucaoEncalheBandeirasWSServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.icd.axis.client;

public class DevolucaoEncalheBandeirasWSServiceTestCase  {
   

    public void testDevolucaoEncalheBandeirasWSWSDL() throws Exception {
        javax.xml.rpc.ServiceFactory serviceFactory = javax.xml.rpc.ServiceFactory.newInstance();
        java.net.URL url = new java.net.URL(new br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSServiceLocator().getDevolucaoEncalheBandeirasWSAddress() + "?WSDL");
        javax.xml.rpc.Service service = serviceFactory.createService(url, new br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSServiceLocator().getServiceName());
       // assertTrue(service != null);
    }

    public void test1DevolucaoEncalheBandeirasWSInserirDevolucaoEncalheBandeiras() throws Exception {
        br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSSoapBindingStub binding;
        try {
            binding = (br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSSoapBindingStub)
                          new br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSServiceLocator().getDevolucaoEncalheBandeirasWS(true);
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new Exception("JAX-RPC ServiceException caught: " + jre);
        }
     //   assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        binding.inserirDevolucaoEncalheBandeiras(new java.lang.String(), new java.lang.String(), new java.lang.Integer(0), new java.lang.Integer(0), new java.lang.Integer(0), new java.lang.Integer(0), new java.lang.String(), new br.com.abril.icd.axis.client.ItemNotaEncalheBandeiraVO[0]);
        // TBD - validate results
    }
    
   
    
    public static String test2inserirNotasDevEncalheBandeiras() throws Exception {
		System.out.println("INICIANDO INCLUSAO DAS NOTAS DEVOLUCAO ENCALHE/BANDEIRAS.");

        br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSSoapBindingStub devolucaoEncalheBandeirasWS;
        try {
        	devolucaoEncalheBandeirasWS = (br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSSoapBindingStub)
                          new br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSServiceLocator().getDevolucaoEncalheBandeirasWS(true);
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new Exception("JAX-RPC ServiceException caught: " + jre);
        }
      //  assertNotNull("binding is null", devolucaoEncalheBandeirasWS);

        // Time out after a minute
        devolucaoEncalheBandeirasWS.setTimeout(60000);

		String ufDistribuidor = "RJ";
		String codDistribuidor = "001M";
		Integer tipoNota = 1;
		Integer numNota = 123;
		Integer codVolume = 1;
		Integer qtdSacosPaletes = 2;
		String nomeDestinoEncalhe = "DEVOLUCAO AO EDITOR";
		
		//Item 1
		ItemNotaEncalheBandeiraVO[] itensNotaEncalheBandeira = new ItemNotaEncalheBandeiraVO[3];
		ItemNotaEncalheBandeiraVO itemNotaEncalheBandeiraVO = new ItemNotaEncalheBandeiraVO();
		itemNotaEncalheBandeiraVO.setAnoSemanaRecolhimento("201109");
		itemNotaEncalheBandeiraVO.setCodPublicacaoProdin(31906001);
		itemNotaEncalheBandeiraVO.setExemplaresPacote(20);
		itemNotaEncalheBandeiraVO.setIndiceVolume(1);
		itemNotaEncalheBandeiraVO.setNumEdicaoProdin(70);
		itemNotaEncalheBandeiraVO.setPacotePadrao(10);	
		itemNotaEncalheBandeiraVO.setQtdExemplares(1);
		itemNotaEncalheBandeiraVO.setQtdExemplaresIrregular(1);
		itensNotaEncalheBandeira[0] = itemNotaEncalheBandeiraVO;
			
		//Item 2
		itemNotaEncalheBandeiraVO = new ItemNotaEncalheBandeiraVO();
		itemNotaEncalheBandeiraVO.setAnoSemanaRecolhimento("201109");
		itemNotaEncalheBandeiraVO.setCodPublicacaoProdin(31614001);
		itemNotaEncalheBandeiraVO.setExemplaresPacote(20);
		itemNotaEncalheBandeiraVO.setIndiceVolume(2);
		itemNotaEncalheBandeiraVO.setNumEdicaoProdin(31);
		itemNotaEncalheBandeiraVO.setPacotePadrao(10);	
		itemNotaEncalheBandeiraVO.setQtdExemplares(2);
		itemNotaEncalheBandeiraVO.setQtdExemplaresIrregular(2);
		itensNotaEncalheBandeira[1] = itemNotaEncalheBandeiraVO;
		
		//Item 3
		itemNotaEncalheBandeiraVO = new ItemNotaEncalheBandeiraVO();
		itemNotaEncalheBandeiraVO.setAnoSemanaRecolhimento("201109");
		itemNotaEncalheBandeiraVO.setCodPublicacaoProdin(31946001);
		itemNotaEncalheBandeiraVO.setExemplaresPacote(20);
		itemNotaEncalheBandeiraVO.setIndiceVolume(3);
		itemNotaEncalheBandeiraVO.setNumEdicaoProdin(16);
		itemNotaEncalheBandeiraVO.setPacotePadrao(10);	
		itemNotaEncalheBandeiraVO.setQtdExemplares(3);
		itemNotaEncalheBandeiraVO.setQtdExemplaresIrregular(3);
		itensNotaEncalheBandeira[2] = itemNotaEncalheBandeiraVO;
		devolucaoEncalheBandeirasWS.inserirDevolucaoEncalheBandeiras(ufDistribuidor, codDistribuidor, tipoNota, numNota, codVolume, qtdSacosPaletes, nomeDestinoEncalhe, itensNotaEncalheBandeira);
		
		System.out.println("FIM DA INCLUSAO DAS NOTAS DEVOLUCAO ENCALHE/BANDEIRAS.");
		return "FIM DA INCLUSAO DAS NOTAS DEVOLUCAO ENCALHE/BANDEIRAS.";
	}

}
