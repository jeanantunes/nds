package br.com.abril.nds.client.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ProcessamentoNFEException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.fiscal.nota.StatusRetornado;
import br.com.abril.nds.util.StringUtil;
import br.inf.portalfiscal.nfe.v310.TEvento;
import br.inf.portalfiscal.nfe.v310.TNFe;
import br.inf.portalfiscal.nfe.v310.TNfeProc;
import br.inf.portalfiscal.nfe.v310.TProcEvento;
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
    private static final String XSD_PROC_NFE = "/procNFe_v";
    
    /**
     * Constante com caminho do arquivo do xsd do POJO TProcCancNFe
     */
    private static final String XSD_PROC_CANC_NFE = "/procEventoCancNFe_v";
    
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
    public static RetornoNFEDTO processarArquivoRetorno(final File arquivo, final String schemaPath, String tipoRetorno) throws ProcessamentoNFEException {
        
        if(tipoRetorno.equals("A")) {
        	return processarProcNfe(arquivo, schemaPath);
        } else if(tipoRetorno.equals("C")) {
        	return processarCancNfe(arquivo, schemaPath);
        } else if(tipoRetorno.equals("R")) {
        	return processarRejeitadoNfe(arquivo, schemaPath);
        }
        
        return null;
        /*
         
        
        try {
        	
        	if(validarSchemaXML(XSD_PROC_NFE, arquivo, schemaPath, null)) {
	        	
				context = JAXBContext.newInstance(TNfeProc.class);  
				unmarshaller = context.createUnmarshaller();  
				// TNfeProc nfeProc = unmarshaller.unmarshal(new StreamSource(arquivo), TNfeProc.class).getValue();  
				TNfeProc nfeProc = unmarshaller.unmarshal(new StreamSource(new StringReader(arquivo.getPath())), TNfeProc.class).getValue();
				retornoNFEDTO = NFEImportUtil.retornoNFeProcNFe(nfeProc);
	            
				return retornoNFEDTO;
				
	        } else if (validarSchemaXML(XSD_NFE, arquivo, schemaPath, null)) {
	        	
	        	context = JAXBContext.newInstance(TNFe.class);
	            unmarshaller = context.createUnmarshaller();
	            final TNFe nfe = (TNFe) unmarshaller.unmarshal(arquivo);
	            retornoNFEDTO = NFEImportUtil.retornoNFeAssinada(nfe);
	            
	            return retornoNFEDTO;
	            
	        } else if(validarSchemaXML(XSD_PROC_CANC_NFE, arquivo, schemaPath, "1.00")) {
	        	
	        	context = JAXBContext.newInstance(TProcEvento.class);
                unmarshaller = context.createUnmarshaller();
                TProcEvento retornoCancelamentoNFe = unmarshaller.unmarshal(new StreamSource(arquivo), TProcEvento.class).getValue();
                retornoNFEDTO = NFEImportUtil.retornoNFeEnvEvento(retornoCancelamentoNFe);
                
                return retornoNFEDTO;
                
	        } else {
	        	 throw new ValidacaoException(TipoMensagem.ERROR, "Erro com a geração do arquivo ");
	        }
	        
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidacaoException(TipoMensagem.ERROR, "Erro com a geração do arquivo ");
        }
        * 
         */
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
            
            Date dataRecebimento = new DateTime(nfeProc.getProtNFe().getInfProt().getDhRecbto()).toDate();
            
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
     * Obtém os dados de Retorno do Arquivo da NFe Cancelada.
     * 
     * @param procEventoNFe
     * @return
     * @throws ProcessamentoNFEException
     */
    private static RetornoNFEDTO retornoNFeEnvEvento(final TProcEvento procEventoNFe) throws ProcessamentoNFEException {
    	
        final RetornoNFEDTO retornoNFEDTO = new RetornoNFEDTO();
        
        if (procEventoNFe != null && procEventoNFe.getEvento() != null) {
            
        	TEvento evento = procEventoNFe.getEvento();
        		
        	evento.getInfEvento().getTpEvento();
    		final Long idNotaFiscal = null;
    		final String cpfCnpj = evento.getInfEvento().getCNPJ() != null && !StringUtil.isEmpty(evento.getInfEvento().getCNPJ()) ? evento.getInfEvento().getCNPJ() : evento.getInfEvento().getCPF();
    		final String chaveAcesso = evento.getInfEvento().getChNFe();
    		final Long protocolo = Long.parseLong(evento.getInfEvento().getDetEvento().getNProt());
    		
    		Date dataRecebimento = null;
			dataRecebimento = new DateTime(evento.getInfEvento().getDhEvento()).toDate();
    		
    		retornoNFEDTO.setNumeroNotaFiscal(idNotaFiscal);
    		retornoNFEDTO.setCpfCnpj(cpfCnpj);
    		retornoNFEDTO.setChaveAcesso(chaveAcesso);
    		retornoNFEDTO.setProtocolo(protocolo);
    		retornoNFEDTO.setDataRecebimento(dataRecebimento);
    		retornoNFEDTO.setMotivo(evento.getInfEvento().getDetEvento().getDescEvento() +" / "+ evento.getInfEvento().getDetEvento().getXJust());
    		retornoNFEDTO.setTpEvento(evento.getInfEvento().getTpEvento());

    		StatusRetornado status = StatusRetornado.AUTORIZADO;
    		switch (evento.getInfEvento().getTpEvento()) {
			case "110111":
				
				status = StatusRetornado.CANCELAMENTO_HOMOLOGADO;
				break;

			default:
				break;
			}
    		retornoNFEDTO.setStatus(status);
        	
        } else {
        	
            throw new ProcessamentoNFEException();
        }
        return retornoNFEDTO;
    }
    
    /**
     * Metodo por validar o schema de xml
     * @param tipoSchema
     * @param versao TODO
     * @param versao
     */
	public static boolean validarSchemaXML(final String tipoSchema, final File arquivo, final String schemaPath, String versao) {
		
		boolean retorno = false; 
		
		try {
			
			final String schemaFile = schemaPath+"xsdnfe/v"+ (versao != null ? versao : versaoNFE) + tipoSchema + (versao != null ? versao : versaoNFE) + ".xsd";
			final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			if(LOGGER.isDebugEnabled()) {
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
	
	private static RetornoNFEDTO processarProcNfe(final File arquivo, final String schemaPath) {
		
        JAXBContext context;
        Unmarshaller unmarshaller;
		
		try {
			
			context = JAXBContext.newInstance(TNfeProc.class);  
			unmarshaller = context.createUnmarshaller();  
			TNfeProc nfeProc = unmarshaller.unmarshal(new StreamSource(arquivo), TNfeProc.class).getValue();  
			//TNfeProc nfeProc = unmarshaller.unmarshal(new StreamSource(new StringReader(arquivo.getPath())), TNfeProc.class).getValue();
			return NFEImportUtil.retornoNFeProcNFe(nfeProc);
			
		} catch (Exception e){
			LOGGER.debug("Erro ao realizar o parser do arquivo de retorno: " + arquivo.getName());
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao realizar o parser do arquivo de retorno:");
		}
	}
	
	private static RetornoNFEDTO processarCancNfe(final File arquivo, final String schemaPath) {
		
        JAXBContext context;
        Unmarshaller unmarshaller;
		
		try {
			
			context = JAXBContext.newInstance(TProcEvento.class);
            unmarshaller = context.createUnmarshaller();
            TProcEvento retornoCancelamentoNFe = unmarshaller.unmarshal(new StreamSource(arquivo), TProcEvento.class).getValue();
            return NFEImportUtil.retornoNFeEnvEvento(retornoCancelamentoNFe);
            
		} catch (Exception e){
			LOGGER.debug("Erro ao realizar o parser do arquivo de cancelamento: " +arquivo.getName());
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao realizar o parser do arquivo de cancelamento:");
			
		}
	}
	
	private static RetornoNFEDTO processarRejeitadoNfe(final File arquivo, final String schemaPath) {
		
        JAXBContext context;
        Unmarshaller unmarshaller;
		
		try {
			
			context = JAXBContext.newInstance(TNFe.class);
            unmarshaller = context.createUnmarshaller();
            final TNFe nfe = (TNFe) unmarshaller.unmarshal(arquivo);
            return NFEImportUtil.retornoNFeAssinada(nfe);
            
		} catch (Exception e){
			LOGGER.debug("Erro ao realizar o parser do arquivo de cancelamento: " +arquivo.getName());
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao realizar o parser do arquivo de cancelamento:");
			
		}
	}
}