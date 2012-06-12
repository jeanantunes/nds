package br.com.abril.nds.client.util;

import java.io.File;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

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
	
	/**
	 * Obtém os dados atualizados de Status do arquivo da NFe de Retorno.
	 * 
	 * @param arquivo
	 * @return
	 * @throws ProcessamentoNFEException
	 */
	@SuppressWarnings("unchecked")
	public static RetornoNFEDTO processarArquivoRetorno(File arquivo)
			throws ProcessamentoNFEException {

		RetornoNFEDTO retornoNFEDTO = null;
		
		JAXBContext context;
		Unmarshaller unmarshaller;
		
		Exception exception = null;
		try {
			context = JAXBContext.newInstance(TNFe.class);
			unmarshaller = context.createUnmarshaller();
			JAXBElement<TNFe> element = (JAXBElement<TNFe>) unmarshaller.unmarshal(arquivo);
			TNFe nfe = element.getValue();
			retornoNFEDTO = NFEImportUtil.retornoNFeAssinada(nfe);
		} catch (JAXBException e) {
			exception = e;
		}

		if (exception != null) {
			exception = null;
			try {
				context = JAXBContext.newInstance(TNfeProc.class);
				unmarshaller = context.createUnmarshaller();
				JAXBElement<TNfeProc> element = (JAXBElement<TNfeProc>) unmarshaller.unmarshal(arquivo);
				TNfeProc nfeProc = element.getValue();
				retornoNFEDTO = NFEImportUtil.retornoNFeProcNFe(nfeProc);
			} catch (JAXBException e) {
				exception = e;
			}
		}

		if (exception != null) {
			exception = null;
			try {
				context = JAXBContext.newInstance(TProcCancNFe.class);
				unmarshaller = context.createUnmarshaller();
				JAXBElement<TProcCancNFe> element = (JAXBElement<TProcCancNFe>) unmarshaller.unmarshal(arquivo);
				TProcCancNFe procCancNFe = element.getValue();
				retornoNFEDTO = NFEImportUtil.retornoNFeCancNFe(procCancNFe);
			} catch (JAXBException e) {
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
	private static RetornoNFEDTO retornoNFeAssinada(TNFe nfe) throws ProcessamentoNFEException {
		RetornoNFEDTO retornoNFEDTO = new RetornoNFEDTO();

		if (nfe != null && nfe.getInfNFe() != null
				&& nfe.getInfNFe().getIde() != null
				&& nfe.getInfNFe().getEmit() != null
				&& nfe.getInfNFe().getId() != null
				&& nfe.getInfNFe().getId().substring(3).length() != 44) {
			
			Long idNotaFiscal = Long.parseLong(nfe.getInfNFe().getIde().getCNF());
			String cpfCnpj = null;
			if (nfe.getInfNFe().getEmit().getCPF() != null) {
				cpfCnpj = nfe.getInfNFe().getEmit().getCPF();
			} else {
				cpfCnpj = nfe.getInfNFe().getEmit().getCNPJ();
			}
			String chaveAcesso = nfe.getInfNFe().getId().substring(3);
			Long protocolo = null;
			Date dataRecebimento = null;
			String motivo = null;
			Status status = null;

			retornoNFEDTO.setIdNotaFiscal(idNotaFiscal);
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
	private static RetornoNFEDTO retornoNFeProcNFe(TNfeProc nfeProc) throws ProcessamentoNFEException {
		RetornoNFEDTO retornoNFEDTO = new RetornoNFEDTO();

		if (nfeProc != null
				&& nfeProc.getNFe() != null
				&& nfeProc.getNFe().getInfNFe() != null
				&& nfeProc.getNFe().getInfNFe().getIde() != null
				&& nfeProc.getNFe().getInfNFe().getEmit() != null
				&& nfeProc.getNFe().getInfNFe().getId() != null
				&& nfeProc.getNFe().getInfNFe().getId().substring(3).length() != 44
				&& nfeProc.getProtNFe() != null
				&& nfeProc.getProtNFe().getInfProt() != null
				&& nfeProc.getProtNFe().getInfProt().getDhRecbto() != null) {

		Long idNotaFiscal = Long.parseLong(nfeProc.getNFe().getInfNFe().getIde().getCNF());
		String cpfCnpj = null; 
		if (nfeProc.getNFe().getInfNFe().getEmit().getCPF() != null) {
			cpfCnpj = nfeProc.getNFe().getInfNFe().getEmit().getCPF();
		} else {
			cpfCnpj = nfeProc.getNFe().getInfNFe().getEmit().getCNPJ();
		}
		String chaveAcesso = nfeProc.getNFe().getInfNFe().getId().substring(3);
		Long protocolo = Long.parseLong(nfeProc.getProtNFe().getInfProt().getNProt());
		Date dataRecebimento = nfeProc.getProtNFe().getInfProt().getDhRecbto().toGregorianCalendar().getTime();
		String motivo = nfeProc.getProtNFe().getInfProt().getXMotivo();
		Status status = Status.obterPeloCodigo(Integer.parseInt(nfeProc.getProtNFe().getInfProt().getCStat()));
				
		retornoNFEDTO.setIdNotaFiscal(idNotaFiscal);
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
	private static RetornoNFEDTO retornoNFeCancNFe(TProcCancNFe procCancNFe) throws ProcessamentoNFEException {
		RetornoNFEDTO retornoNFEDTO = new RetornoNFEDTO();

		if (procCancNFe != null
				&& procCancNFe.getRetCancNFe() != null
				&& procCancNFe.getRetCancNFe().getInfCanc() != null
				&& procCancNFe.getRetCancNFe().getInfCanc().getChNFe() != null
				&& procCancNFe.getRetCancNFe().getInfCanc().getChNFe().length() != 44
				&& procCancNFe.getRetCancNFe().getInfCanc().getDhRecbto() != null) {

		Long idNotaFiscal = null;
		String cpfCnpj = null; 
		String chaveAcesso = procCancNFe.getRetCancNFe().getInfCanc().getChNFe();
		Long protocolo = Long.parseLong(procCancNFe.getRetCancNFe().getInfCanc().getNProt());
		Date dataRecebimento = procCancNFe.getRetCancNFe().getInfCanc().getDhRecbto().toGregorianCalendar().getTime();
		String motivo = procCancNFe.getRetCancNFe().getInfCanc().getXMotivo();
		Status status = Status.obterPeloCodigo(Integer.parseInt(procCancNFe.getRetCancNFe().getInfCanc().getCStat()));
				
		retornoNFEDTO.setIdNotaFiscal(idNotaFiscal);
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
