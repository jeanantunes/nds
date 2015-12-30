/**
 * DevolucaoEncalheBandeirasWSServiceClient.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.icd.axis.client;

import java.util.Calendar;
import java.util.List;

import br.com.abril.icd.axis.util.SemanaUtil;
import br.com.abril.nds.vo.ItemEncalheBandeiraVO;
import br.com.abril.nds.vo.NotaEncalheBandeiraVO;


public class DevolucaoEncalheBandeirasWSServiceClient {
    
	//private static final Logger LOGGER = LoggerFactory.getLogger(DevolucaoEncalheBandeirasWSServiceClient.class);
  


    public static String enviarNotasDevEncalheBandeiras(
    		NotaEncalheBandeiraVO nota,List<ItemEncalheBandeiraVO> itens,boolean homolog,String uf
    		) throws Exception {
      System.err.println("INICIANDO INCLUSAO DAS NOTAS DEVOLUCAO ENCALHE/BANDEIRAS.Ambiente "+(homolog ?"HOMOLOGACAO":"PRODUCAO"));

        br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSSoapBindingStub devolucaoEncalheBandeirasWS;
        try {
        	devolucaoEncalheBandeirasWS = (br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSSoapBindingStub)
                          new br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSServiceLocator().getDevolucaoEncalheBandeirasWS(homolog);
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
           System.err.println("JAX-RPC ServiceException caught: " + jre);
            throw new Exception("JAX-RPC ServiceException caught: " + jre);
            
        }
       

        // Time out after 1 minute
        devolucaoEncalheBandeirasWS.setTimeout(600000);

       
		String ufDistribuidor = uf;
		String codDistribuidor = "001M";
		Integer tipoNota = nota.getTipoNota();
		Integer numNota = nota.getNumNota();
		Integer codVolume = nota.getCodVolume();
		Integer qtdSacosPaletes =  nota.getQtdSacosPaletes();
		String nomeDestinoEncalhe = nota.getNomeDestinoEncalhe();
		
		//Item 1
		ItemNotaEncalheBandeiraVO[] itensNotaEncalheBandeira = new ItemNotaEncalheBandeiraVO[itens.size()];
		int i=0;
		
		for ( ItemEncalheBandeiraVO item:itens) {
		 
		ItemNotaEncalheBandeiraVO itemNotaEncalheBandeiraVO = new ItemNotaEncalheBandeiraVO();
		String anoSemana = SemanaUtil.obterAnoNumeroSemana(item.getDataRecolhimento(),Calendar.WEDNESDAY);
		
		itemNotaEncalheBandeiraVO.setAnoSemanaRecolhimento(anoSemana);
		itemNotaEncalheBandeiraVO.setCodPublicacaoProdin(item.getCodPublicacao());
		itemNotaEncalheBandeiraVO.setExemplaresPacote(item.getExemplaresPacote());
		itemNotaEncalheBandeiraVO.setIndiceVolume(item.getIndiceVolume());
		itemNotaEncalheBandeiraVO.setNumEdicaoProdin(item.getNumEdicao());
		itemNotaEncalheBandeiraVO.setPacotePadrao(item.getPacotePadrao());	
		itemNotaEncalheBandeiraVO.setQtdExemplares(item.getQtdExemplares());
		itemNotaEncalheBandeiraVO.setQtdExemplaresIrregular(item.getQtdExemplaresIrregular());
		itensNotaEncalheBandeira[i] = itemNotaEncalheBandeiraVO;
		i++;
		}
		
		devolucaoEncalheBandeirasWS.inserirDevolucaoEncalheBandeiras(ufDistribuidor, codDistribuidor, tipoNota, numNota, codVolume, qtdSacosPaletes, nomeDestinoEncalhe, itensNotaEncalheBandeira);
		
		System.err.println("FIM DA INCLUSAO DAS NOTAS DEVOLUCAO ENCALHE/BANDEIRAS.");
		return "FIM DA INCLUSAO DAS NOTAS DEVOLUCAO ENCALHE/BANDEIRAS.";
	}
}
