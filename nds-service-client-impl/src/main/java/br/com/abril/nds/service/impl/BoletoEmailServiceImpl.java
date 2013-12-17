package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.BoletoEmail;
import br.com.abril.nds.repository.BoletoEmailRepository;
import br.com.abril.nds.service.BoletoEmailService;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.BoletoEmail}
 * 
 * @author Discover Technology
 */
@Service
public class BoletoEmailServiceImpl implements BoletoEmailService {
	
	
	@Autowired
	protected BoletoEmailRepository boletoEmailRepository;

	@Autowired
	protected BoletoService boletoService;
	
	@Autowired
	protected DocumentoCobrancaService documentoCobrancaService;
	
	@Autowired
	protected EmailService emailService;
	
	@Autowired
	protected GerarCobrancaService gerarCobrancaService;
	
	private void enviarDocumentosCobrancaEmail(String nossoNumero, String email) throws AutenticacaoEmailException {
		
		byte[] anexo = this.documentoCobrancaService.gerarDocumentoCobranca(nossoNumero);
		
		this.emailService.enviar("Cobrança", 
								 "Segue documento de cobrança em anexo.", 
								 new String[]{email}, 
								 new AnexoEmail("Cobranca",anexo,TipoAnexo.PDF));		
	}
	
	/**
	 * Salva controle de emissao de boletos por email
	 * @param listaNossoNumeroEnvioEmail
	 */
	@Transactional
	@Override
    public void salvarBoletoEmail(List<String> listaNossoNumeroEnvioEmail){
		
		if(listaNossoNumeroEnvioEmail==null || listaNossoNumeroEnvioEmail.isEmpty()){
			
			return;
		}
			
		for (String nossoNumero : listaNossoNumeroEnvioEmail){
				
			Boleto boleto = this.boletoService.obterBoletoPorNossoNumero(nossoNumero, null);
			
			if (boleto!=null){
				
				BoletoEmail bm = this.boletoEmailRepository.obterBoletoEmailPorCobranca(boleto.getId());
				
				if (bm==null){

					Cota cota = boleto.getCota();
					
					String email = cota.getPessoa().getEmail();
						
				    if (email == null || email.trim().isEmpty()){

					    continue;
					}
				    
				    if (!this.gerarCobrancaService.aceitaEnvioEmail(cota, nossoNumero)) {

				    	continue;
				    }

				    bm = new BoletoEmail();
				
				    bm.setCobranca(boleto);
				
				    this.boletoEmailRepository.merge(bm);
			    }
			}
		}
	}

	/**
	 * Obtem todos os boletos pendentes de envio por email
	 * @return List<BoletoEmail>
	 */
	@Transactional
	@Override
	public List<BoletoEmail> buscarTodos() {
		
		return this.boletoEmailRepository.buscarTodos();
	}
	
	/**
	 * Envia Cobrança por email - Controle de Envio de Boletos
	 * @param boletoEmail
	 */
	@Override
	@Transactional
	public void enviarBoletoEmail(BoletoEmail boletoEmail) {
		    
		Cota cota = boletoEmail.getCobranca().getCota();
		
		String nossoNumero = boletoEmail.getCobranca().getNossoNumero();
		
		String email = cota.getPessoa().getEmail();
		
		if (email == null || email.trim().isEmpty()){

		    return;
		}
	    
		if (this.gerarCobrancaService.aceitaEnvioEmail(cota, nossoNumero)) {
		
			try {
				
				this.enviarDocumentosCobrancaEmail(nossoNumero, email);
				
				this.boletoEmailRepository.remover(boletoEmail);
	    
	        } catch (AutenticacaoEmailException e) {
  
				throw new ValidacaoException(TipoMensagem.WARNING,
						"Erro ao tentar enviar Boleto["
								+ boletoEmail.getCobranca().getNossoNumero()
								+ "] por email. " + e.getMessage());
	        }
		}
	}
}
