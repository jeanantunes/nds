package br.com.abril.nds.client.util;

import java.io.File;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.exception.ProcessamentoNFEException;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.inf.portalfiscal.nfe.TNFe;
import br.inf.portalfiscal.nfe.TNfeProc;
import br.inf.portalfiscal.nfe.TProcCancNFe;

/**
 * Classe utilitária que importa os arquivos de nota fiscal.
 * 
 * @author Discover Technology
 * 
 */
public class NFEImportUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NFEImportUtil.class);
    
    /**
     * Constante com caminho do arquivo do xsd do POJO TNFe
     */
    private static final String XSD_NFE = "xsdnfe/nfe_v2.00.xsd";
    
    /**
     * Constante com caminho do arquivo do xsd do POJO TNfeProc
     */
    private static final String XSD_PPROC_NFE = "xsdnfe/procNFe_v2.00.xsd";
    
    /**
     * Constante com caminho do arquivo do xsd do POJO TProcCancNFe
     */
    private static final String XSD_PROC_CANC_NFE = "xsdnfe/procCancNFe_v2.00.xsd";
    
    /**
     * Constante da quantidade de dígitos da chave acesso.
     */
    public static final int QTD_DIGITOS_CHAVE_ACESSO_NFE = 44;
    
    private NFEImportUtil() {
        
    }
    
    /**
     * Obtém os dados atualizados de Status do arquivo da NFe de Retorno.
     * 
     * @param arquivo
     * @return
     * @throws ProcessamentoNFEException
     * @throws SAXException
     */
    public static RetornoNFEDTO processarArquivoRetorno(final File arquivo) throws ProcessamentoNFEException {
        
        RetornoNFEDTO retornoNFEDTO = null;
        
        JAXBContext context;
        Unmarshaller unmarshaller;
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(javax.xml.XMLConstants.DEFAULT_NS_PREFIX);
        Schema schema;
        
        Exception exception = null;
        
        try {
            context = JAXBContext.newInstance(TNFe.class);
            unmarshaller = context.createUnmarshaller();
            
            schema = schemaFactory.newSchema(Thread.currentThread().getContextClassLoader().getResource(NFEImportUtil.XSD_NFE));
            unmarshaller.setSchema(schema);
            
            final TNFe nfe = (TNFe) unmarshaller.unmarshal(arquivo);
            
            retornoNFEDTO = NFEImportUtil.retornoNFeAssinada(nfe);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            exception = e;
        }
        
        if (exception != null) {
            exception = null;
            try {
                context = JAXBContext.newInstance(TNfeProc.class);
                unmarshaller = context.createUnmarshaller();
                
                schema = schemaFactory.newSchema(Thread.currentThread().getContextClassLoader().getResource(
                        NFEImportUtil.XSD_PPROC_NFE));
                unmarshaller.setSchema(schema);
                
                final TNfeProc nfeProc = (TNfeProc) unmarshaller.unmarshal(arquivo);
                retornoNFEDTO = NFEImportUtil.retornoNFeProcNFe(nfeProc);
            } catch (final Exception e) {
                LOGGER.error(e.getMessage(), e);
                exception = e;
            }
        }
        
        if (exception != null) {
            exception = null;
            try {
                context = JAXBContext.newInstance(TProcCancNFe.class);
                unmarshaller = context.createUnmarshaller();
                
                schema = schemaFactory.newSchema(Thread.currentThread().getContextClassLoader().getResource(
                        NFEImportUtil.XSD_PROC_CANC_NFE));
                unmarshaller.setSchema(schema);
                
                final TProcCancNFe procCancNFe = (TProcCancNFe) unmarshaller.unmarshal(arquivo);
                retornoNFEDTO = NFEImportUtil.retornoNFeCancNFe(procCancNFe);
            } catch (final Exception e) {
                LOGGER.error(e.getMessage(), e);
                exception = e;
            }
        }
        
        if (exception != null) {
            throw new ProcessamentoNFEException();
        }
        
        return retornoNFEDTO;
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
            final Status status = null;
            
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
            final Date dataRecebimento = nfeProc.getProtNFe().getInfProt().getDhRecbto().toGregorianCalendar()
                    .getTime();
            final String motivo = null;
            final Status status = Status
                    .obterPeloCodigo(Integer.parseInt(nfeProc.getProtNFe().getInfProt().getCStat()));
            
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
    private static RetornoNFEDTO retornoNFeCancNFe(final TProcCancNFe procCancNFe) throws ProcessamentoNFEException {
        final RetornoNFEDTO retornoNFEDTO = new RetornoNFEDTO();
        
        if (procCancNFe != null && procCancNFe.getRetCancNFe() != null
                && procCancNFe.getRetCancNFe().getInfCanc() != null
                && procCancNFe.getRetCancNFe().getInfCanc().getChNFe() != null
                && procCancNFe.getRetCancNFe().getInfCanc().getChNFe().length() == 44
                && procCancNFe.getRetCancNFe().getInfCanc().getDhRecbto() != null && procCancNFe.getCancNFe() != null
                && procCancNFe.getCancNFe().getInfCanc() != null) {
            
            final Long idNotaFiscal = null;
            final String cpfCnpj = null;
            final String chaveAcesso = procCancNFe.getRetCancNFe().getInfCanc().getChNFe();
            final Long protocolo = Long.parseLong(procCancNFe.getRetCancNFe().getInfCanc().getNProt());
            final Date dataRecebimento = procCancNFe.getRetCancNFe().getInfCanc().getDhRecbto().toGregorianCalendar().getTime();
            final String motivo = procCancNFe.getCancNFe().getInfCanc().getXJust();
            final Status status = Status.obterPeloCodigo(Integer.parseInt(procCancNFe.getRetCancNFe().getInfCanc().getCStat()));
            
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
    
}