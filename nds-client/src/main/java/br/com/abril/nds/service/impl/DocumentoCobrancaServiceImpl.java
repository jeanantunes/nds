package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class DocumentoCobrancaServiceImpl implements DocumentoCobrancaService {

	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private EmailService emailService;
	
	
	@Override
	@Transactional
	public byte[] gerarDocumentoCobranca(String nossoNumero) {
		
		Cobranca cobranca = this.cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
		
		byte[] retorno = null;
	
		try {
			
			switch (cobranca.getTipoCobranca()) {
				case BOLETO:
					retorno = boletoService.gerarImpressaoBoleto(nossoNumero);
				default:
					
			}
			
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar arquivo de cobrança para nosso número: " + nossoNumero + " - " + e.getMessage());
		}
		
		this.cobrancaRepository.incrementarVia(nossoNumero);
		
		return retorno;
	}
	
	@Transactional
	public void enviarDocumentoCobrancaPorEmail(String nossoNumero){
		
		Cobranca cobranca = this.cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
		
		try {
			switch (cobranca.getTipoCobranca()) {
				case BOLETO:

					boletoService.enviarBoletoEmail(nossoNumero);
					break;

				default:
					enviarDocumentoPorEmail(cobranca);
				
			}
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao enviar e-mail de arquivo de cobrança para nosso número: " + nossoNumero + " - " + e.getMessage());
		}
		
		this.cobrancaRepository.incrementarVia(nossoNumero);
	}
	
	public byte[] gerarDocumentoCobranca(List<GeraDividaDTO> dividas, TipoCobranca tipoCobranca) {
		
		List<String> listNossoNumero = getNossoNumerosBoleto(dividas); 
		
		if(TipoCobranca.BOLETO.equals(tipoCobranca)){
			try {
				return boletoService.gerarImpressaoBoletos(listNossoNumero);
			} catch (IOException e) {
				throw new ValidacaoException(TipoMensagem.ERROR, e.getMessage());
			}
		}
		
		return null;
		
	}
	
	private List<String> getNossoNumerosBoleto(List<GeraDividaDTO> dividas){
		
		List<String> list = new ArrayList<String>();
		
		for(GeraDividaDTO dto : dividas){
			list.add(dto.getNossoNumero());
		}
		
		return list;
	}
	
	/**
	 * Envia um tipo de cobrança por email.
	 * @param cobranca
	 * @throws AutenticacaoEmailException
	 */
	private void enviarDocumentoPorEmail(Cobranca cobranca) throws AutenticacaoEmailException {

		Distribuidor distribuidor = distribuidorService.obter();
		
		String assunto=(distribuidor.getPoliticaCobranca()!=null
						? distribuidor.getPoliticaCobranca().getAssuntoEmailCobranca():"");
		
		String mensagem=(distribuidor.getPoliticaCobranca()!=null
						? distribuidor.getPoliticaCobranca().getMensagemEmailCobranca():"");
		
		String emailCota = cobranca.getCota().getPessoa().getEmail();
		String[] destinatarios = new String[]{emailCota};
		
		File anexo = null; //Obter do Ireport
		
		/*emailService.enviar(assunto,mensagem,destinatarios,anexo);*/
	}
}
