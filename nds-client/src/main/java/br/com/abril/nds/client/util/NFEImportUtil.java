package br.com.abril.nds.client.util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ProcessamentoNFEException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.fiscal.nota.StatusRetornado;
import br.inf.portalfiscal.nfe.TProcCancNFe;
import br.inf.portalfiscal.nfe.v310.TNFe;
import br.inf.portalfiscal.nfe.v310.TNfeProc;
import br.inf.portalfiscal.nfe.v310.TRetCancNFe;

/**
 * Classe utilitária que importa os arquivos de nota fiscal.
 * 
 * @author Discover Technology
 * 
 */
public abstract class NFEImportUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NFEImportUtil.class);
    
    private static String versaoNFE = "3.10";
    
    /**
     * Constante com caminho do arquivo do xsd do POJO TNFe
     */
    private static final String XSD_NFE = "/nfe_v";
    
    /**
     * Constante com caminho do arquivo do xsd do POJO TNfeProc
     */
    private static final String XSD_PPROC_NFE = "/procNFe_v";
    
    /**
     * Constante com caminho do arquivo do xsd do POJO TProcCancNFe
     */
    private static final String XSD_PROC_CANC_NFE = "/retCancNFe_v";
    
    /**
     * Constante da quantidade de dígitos da chave acesso.
     */
    public static final int QTD_DIGITOS_CHAVE_ACESSO_NFE = 44;
    
    /**
     * Obtém os dados atualizados de Status do arquivo da NFe de Retorno.
     * 
     * @param arquivo
     * @return
     * @throws ProcessamentoNFEException
     * @throws SAXException
     */
    public static RetornoNFEDTO processarArquivoRetorno(final File arquivo, final String schemaPath) throws ProcessamentoNFEException {
        
        RetornoNFEDTO retornoNFEDTO = null;
        
        // validar o tipo de schema pertecente
        JAXBContext context;
        Unmarshaller unmarshaller;
        
        try {
        
	        if (validarSchemaXML(XSD_NFE, arquivo, schemaPath)) {
	        	context = JAXBContext.newInstance(TNFe.class);
	            unmarshaller = context.createUnmarshaller();
	            
	            final TNFe nfe = (TNFe) unmarshaller.unmarshal(arquivo);
	            retornoNFEDTO = NFEImportUtil.retornoNFeAssinada(nfe);
	            
	            return retornoNFEDTO;
	            
	        } else if(validarSchemaXML(XSD_PPROC_NFE, arquivo, schemaPath)) {
	        	
	        	//context = JAXBContext.newInstance(TNfeProc.class);
	            //unmarshaller = context.createUnmarshaller();
	        	//final TNfeProc nfeProc = (TNfeProc) unmarshaller.unmarshal(arquivo);
	            
				context = JAXBContext.newInstance(TNfeProc.class);  
			   
				unmarshaller = context.createUnmarshaller();  
			   
				TNfeProc nfeProc = unmarshaller.unmarshal(new StreamSource(arquivo), TNfeProc.class).getValue();  
			   
				retornoNFEDTO = NFEImportUtil.retornoNFeProcNFe(nfeProc);
	            
				return retornoNFEDTO;
				
	        } else if(validarSchemaXML(XSD_PROC_CANC_NFE, arquivo, schemaPath)) {
	        	
	        	context = JAXBContext.newInstance(TProcCancNFe.class);
                unmarshaller = context.createUnmarshaller();
                final TRetCancNFe retornoCancelamentoNFe = (TRetCancNFe) unmarshaller.unmarshal(arquivo);
                retornoNFEDTO = NFEImportUtil.retornoNFeCancNFe(retornoCancelamentoNFe);
                return retornoNFEDTO;
	        } else {
	        	 throw new ValidacaoException(TipoMensagem.ERROR, "Erro com a geração do arquivo ");
	        }
	        
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidacaoException(TipoMensagem.ERROR, "Erro com a geração do arquivo ");
        }
    }
    
    /**
     * Obtém os dados de Retorno do Arquivo da NFe Assinada.
     * 
     * @param nfe
     * @return
     * @throws ProcessamentoNFEException
     */
    private static RetornoNFEDTO retornoNFeAssinada(final TNFe nfe) throws ProcessamentoNFEException {
        final RetornoNFEDTO retornoNFEDTO = new RetornoNFEDTO();
        
        if (nfe != null && nfe.getInfNFe() != null && nfe.getInfNFe().getIde() != null
                && nfe.getInfNFe().getEmit() != null && nfe.getInfNFe().getId() != null
                && nfe.getInfNFe().getId().substring(3).length() == 44) {
            
            final Long idNotaFiscal = Long.parseLong(nfe.getInfNFe().getIde().getCNF());
            String cpfCnpj = null;
            if (nfe.getInfNFe().getEmit().getCPF() != null) {
                cpfCnpj = nfe.getInfNFe().getEmit().getCPF();
            } else {
                cpfCnpj = nfe.getInfNFe().getEmit().getCNPJ();
            }
            final String chaveAcesso = nfe.getInfNFe().getId().substring(3);
            final Long protocolo = null;
            final Date dataRecebimento = null;
            final String motivo = null;
            final StatusRetornado status = null;
            
            retornoNFEDTO.setNumeroNotaFiscal(idNotaFiscal);
            retornoNFEDTO.setCpfCnpj(cpfCnpj);
            retornoNFEDTO.setChaveAcesso(chaveAcesso);
            retornoNFEDTO.setProtocolo(protocolo);
            retornoNFEDTO.setDataRecebimento(dataRecebimento);
            retornoNFEDTO.setMotivo(motivo);
            retornoNFEDTO.setStatus(status);
        } else {
            throw new ProcessamentoNFEException();
        }
        
        return retornoNFEDTO;
    }
    
    /**
     * Obtém os dados de Retorno do Arquivo da NFe Protocolada.
     * 
     * @param nfeProc
     * @return
     * @throws ProcessamentoNFEException
     */
    private static RetornoNFEDTO retornoNFeProcNFe(final TNfeProc nfeProc) throws ProcessamentoNFEException {
        final RetornoNFEDTO retornoNFEDTO = new RetornoNFEDTO();
        
        if (nfeProc != null && nfeProc.getNFe() != null && nfeProc.getNFe().getInfNFe() != null
                && nfeProc.getNFe().getInfNFe().getIde() != null && nfeProc.getNFe().getInfNFe().getEmit() != null
                && nfeProc.getNFe().getInfNFe().getId() != null
                && nfeProc.getNFe().getInfNFe().getId().substring(3).length() == 44 && nfeProc.getProtNFe() != null
                && nfeProc.getProtNFe().getInfProt() != null && nfeProc.getProtNFe().getInfProt().getDhRecbto() != null) {
            
            final Long idNotaFiscal = Long.parseLong(nfeProc.getNFe().getInfNFe().getIde().getCNF());
            String cpfCnpj = null;
            if (nfeProc.getNFe().getInfNFe().getEmit().getCPF() != null) {
                cpfCnpj = nfeProc.getNFe().getInfNFe().getEmit().getCPF();
            } else {
                cpfCnpj = nfeProc.getNFe().getInfNFe().getEmit().getCNPJ();
            }
            final String chaveAcesso = nfeProc.getNFe().getInfNFe().getId().substring(3);
            final Long protocolo = Long.parseLong(nfeProc.getProtNFe().getInfProt().getNProt());
            
            Date dataRecebimento = null;
            SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
            
        	try {
        		dataRecebimento = dateParser.parse(nfeProc.getProtNFe().getInfProt().getDhRecbto());

        	} catch (ParseException e) {
        		throw new ValidacaoException(TipoMensagem.ERROR, "Erro na conversão da date recebida nfe");
        	}
            
            
            final String motivo = null;
            
            final StatusRetornado status = StatusRetornado.obterPeloCodigo(Integer.parseInt(nfeProc.getProtNFe().getInfProt().getCStat()));
            
            retornoNFEDTO.setNumeroNotaFiscal(idNotaFiscal);
            retornoNFEDTO.setCpfCnpj(cpfCnpj);
            retornoNFEDTO.setChaveAcesso(chaveAcesso);
            retornoNFEDTO.setProtocolo(protocolo);
            retornoNFEDTO.setDataRecebimento(dataRecebimento);
            retornoNFEDTO.setMotivo(motivo);
            retornoNFEDTO.setStatus(status);
            
        } else {
            throw new ProcessamentoNFEException();
        }
        return retornoNFEDTO;
    }
    
    /**
     * Obtém os dados de Retorno do Arquivo da NFe Cancelada.
     * 
     * @param procCancNFe
     * @return
     * @throws ProcessamentoNFEException
     */
    private static RetornoNFEDTO retornoNFeCancNFe(final TRetCancNFe procCancNFe) throws ProcessamentoNFEException {
        final RetornoNFEDTO retornoNFEDTO = new RetornoNFEDTO();
        
        if (procCancNFe != null && procCancNFe.getInfCanc() != null
                && procCancNFe.getInfCanc() != null
                && procCancNFe.getInfCanc().getChNFe() != null
                && procCancNFe.getInfCanc().getChNFe().length() == 44
                && procCancNFe.getInfCanc().getDhRecbto() != null && procCancNFe.getInfCanc() != null
                && procCancNFe.getInfCanc() != null) {
            
            final Long idNotaFiscal = null;
            final String cpfCnpj = null;
            final String chaveAcesso = procCancNFe.getInfCanc().getChNFe();
            final Long protocolo = Long.parseLong(procCancNFe.getInfCanc().getNProt());
            final Date dataRecebimento = procCancNFe.getInfCanc().getDhRecbto().toGregorianCalendar().getTime();
            final String motivo = procCancNFe.getInfCanc().getXMotivo();
            final StatusRetornado status = StatusRetornado.obterPeloCodigo(Integer.parseInt(procCancNFe.getInfCanc().getCStat()));
            
            retornoNFEDTO.setNumeroNotaFiscal(idNotaFiscal);
            retornoNFEDTO.setCpfCnpj(cpfCnpj);
            retornoNFEDTO.setChaveAcesso(chaveAcesso);
            retornoNFEDTO.setProtocolo(protocolo);
            retornoNFEDTO.setDataRecebimento(dataRecebimento);
            retornoNFEDTO.setMotivo(motivo);
            retornoNFEDTO.setStatus(status);
        } else {
            throw new ProcessamentoNFEException();
        }
        return retornoNFEDTO;
    }
    
    /**
     * Metodo por validar o schema de xml
     * 
     * @param versao
     * @param tipoSchema
     */
	public static boolean validarSchemaXML(final String tipoSchema, final File arquivo, final String schemaPàth) {
		
		boolean retorno = false; 
		
		
		try {
			
			final String schemaFile = schemaPàth+"xsdnfe/v"+ versaoNFE + tipoSchema + versaoNFE + ".xsd";
			//String xmlFile = "src/main/resources/xmlGerado.xml";
			final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			if(LOGGER.isDebugEnabled()){				
				LOGGER.debug("Schema: "+ schemaFile);
			}
			final Schema schema = factory.newSchema(new File(schemaFile));
			final Validator validator = schema.newValidator();
			validator.validate(new StreamSource(arquivo));
			
			return true;
			
		} catch (IOException | SAXException e) {
			LOGGER.error("Exception: ", e);
		}
		
		return retorno;
	}
}