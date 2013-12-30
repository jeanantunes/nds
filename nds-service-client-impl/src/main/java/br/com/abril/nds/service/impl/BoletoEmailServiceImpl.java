package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.TipoArquivo;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.BoletoEmail;
import br.com.abril.nds.repository.BoletoEmailRepository;
import br.com.abril.nds.service.BoletoEmailService;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.BoletoEmail}
 * 
 * @author luiz.marcili
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
	
	@Autowired
	protected ConferenciaEncalheService conferenciaEncalheService;
	
	@Autowired
	protected DistribuidorService distribuidorService;
	
	/**
	 * Verifica se os parametros do Distribuidor permitem a emissão de documento por email
	 * 
	 * @param tipoDocumento
	 * @return boolean
	 */
	private boolean isEmiteDocumentoDistribuidor(TipoParametrosDistribuidorEmissaoDocumento tipoDocumento){
		
        Distribuidor distribuidor = this.distribuidorService.obter();
		
		List<ParametrosDistribuidorEmissaoDocumento> listaEmissaoDocumentosDistribuidor = distribuidor.getParametrosDistribuidorEmissaoDocumentos();
		
		if (listaEmissaoDocumentosDistribuidor == null){
			
			return false;
		}
		
		for (ParametrosDistribuidorEmissaoDocumento emissaoDocumentosDistribuidor : listaEmissaoDocumentosDistribuidor){
			
			if (emissaoDocumentosDistribuidor.isUtilizaEmail() && 
			    emissaoDocumentosDistribuidor.getTipoParametrosDistribuidorEmissaoDocumento().equals(tipoDocumento)){
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * verifica permissão de emissão de Slip de Cobrança para a Cota
	 * 
	 * @param cota
	 * @return boolean
	 */
	private boolean isEmiteSlip(Cota cota){
		
		boolean emissaoSlipDistribuidor = (this.isEmiteDocumentoDistribuidor(TipoParametrosDistribuidorEmissaoDocumento.SLIP) || 
                                           this.isEmiteDocumentoDistribuidor(TipoParametrosDistribuidorEmissaoDocumento.BOLETO_SLIP));
		
		if ((cota.getParametroDistribuicao() == null) || 
		    (cota.getParametroDistribuicao().getSlipEmail() == null && 
		     cota.getParametroDistribuicao().getBoletoSlipEmail() == null)){
			
			return emissaoSlipDistribuidor;
		}
		
		boolean emissaoSlipCota = (cota.getParametroDistribuicao().getSlipEmail()!=null?cota.getParametroDistribuicao().getSlipEmail():false)||
				                  (cota.getParametroDistribuicao().getBoletoSlipEmail()!=null?cota.getParametroDistribuicao().getBoletoSlipEmail():false);

		return emissaoSlipCota;
	}
	
	/**
	 * Verifica permissão de emissão de Boleto para a Cota
	 * 
	 * @param cota
	 * @return boolean
	 */
    private boolean isEmiteBoleto(Cota cota){
    	
        boolean emissaoBoletoDistribuidor = (this.isEmiteDocumentoDistribuidor(TipoParametrosDistribuidorEmissaoDocumento.BOLETO) || 
        		                             this.isEmiteDocumentoDistribuidor(TipoParametrosDistribuidorEmissaoDocumento.BOLETO_SLIP));
        
        if ((cota.getParametroDistribuicao() == null) || 
		    (cota.getParametroDistribuicao().getBoletoEmail() == null && 
		     cota.getParametroDistribuicao().getBoletoSlipEmail() == null)){
			
			return emissaoBoletoDistribuidor;
		}
		
		boolean emissaoBoletoCota = (cota.getParametroDistribuicao().getBoletoEmail()!=null?cota.getParametroDistribuicao().getBoletoEmail():false)||
				                    (cota.getParametroDistribuicao().getBoletoSlipEmail()!=null?cota.getParametroDistribuicao().getBoletoSlipEmail():false);

		return emissaoBoletoCota;
	}

    /**
     * Verifica permissão de emissão de Recibo da Cobrança para a Cota
     * 
     * @param cota
     * @return boolean
     */
    private boolean isEmiteRecibo(Cota cota){
    	
        boolean emissaoReciboDistribuidor = this.isEmiteDocumentoDistribuidor(TipoParametrosDistribuidorEmissaoDocumento.RECIBO);
        
        if ((cota.getParametroDistribuicao() == null) || 
		    (cota.getParametroDistribuicao().getReciboEmail() == null)){
			
			return emissaoReciboDistribuidor;
		}
		
		boolean emissaoSlipCota = cota.getParametroDistribuicao().getReciboEmail()!=null?cota.getParametroDistribuicao().getReciboEmail():false;
		
		return emissaoSlipCota;
    }
	
    /**
     * Obtem anexos do email de cobrança conforme parâmetros de Cobrança
     * 
     * @param cota
     * @param nossoNumero
     * @return List<AnexoEmail>
     */
	private List<AnexoEmail> obterAnexosEmailCobranca(Cota cota, String nossoNumero){
		
		List<AnexoEmail> anexosEmail = new ArrayList<AnexoEmail>();
		
	    if (this.isEmiteSlip(cota)){	
	
            byte[] anexoSlip = this.documentoCobrancaService.gerarSlipCobranca(nossoNumero, false, TipoArquivo.PDF);
            
            if (anexoSlip!=null){
		
		        anexosEmail.add(new AnexoEmail("Slip",anexoSlip,TipoAnexo.PDF));
            }
	    }
	
	    if (this.isEmiteBoleto(cota)){
		    
	    	byte[] anexoBoleto = this.documentoCobrancaService.gerarDocumentoCobranca(nossoNumero);
        
	    	if (anexoBoleto!=null){
                
	    		anexosEmail.add(new AnexoEmail("Cobranca",anexoBoleto,TipoAnexo.PDF));
	    	}
	    }	

	    if (this.isEmiteRecibo(cota)){
	       
	    	byte[] anexoRecibo = this.documentoCobrancaService.gerarReciboCobranca(nossoNumero);
	    
	    	if (anexoRecibo!=null){
	        
	    		anexosEmail.add(new AnexoEmail("Recibo",anexoRecibo,TipoAnexo.PDF));
	    	}	
	    }

		return anexosEmail;
	}

	/**
	 * Envia Documentos de cobrança
	 * 
	 * @param nossoNumero
	 * @param email
	 * @param listaAnexosEmail
	 * @throws AutenticacaoEmailException
	 */
	private void enviarDocumentosCobrancaEmail(String nossoNumero, String email, List<AnexoEmail> listaAnexosEmail) throws AutenticacaoEmailException {
		
		this.emailService.enviar("Cobrança", 
								 "Segue documento de cobrança em anexo.", 
								 new String[]{email}, 
								 listaAnexosEmail);		
	}
	
	/**
	 * Salva controle de emissao de boletos por email
	 * 
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
	 * 
	 * @return List<BoletoEmail>
	 */
	@Transactional
	@Override
	public List<BoletoEmail> buscarTodos() {
		
		return this.boletoEmailRepository.buscarTodos();
	}
	
	/**
	 * Remove do controle de envio de boletos por email os boletos nao enviados com datas anteriores à data de operação atual
	 * @param boletoEmail
	 */
	private void removerBoletoNaoEnviado(BoletoEmail boletoEmail){
		
		this.boletoEmailRepository.remover(boletoEmail);
	}
	
	/**
	 * Envia Cobrança por email - Controle de Envio de Boletos
	 * 
	 * @param boletoEmail
	 */
	@Override
	@Transactional
	public void enviarBoletoEmail(BoletoEmail boletoEmail) {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		if (boletoEmail.getCobranca().getDataEmissao().compareTo(dataOperacao) < 0){
			
			this.removerBoletoNaoEnviado(boletoEmail);
			
			return;
		}
		    
		Cota cota = boletoEmail.getCobranca().getCota();
		
		String nossoNumero = boletoEmail.getCobranca().getNossoNumero();
		
		String email = cota.getPessoa().getEmail();
		
		if (email == null || email.trim().isEmpty()){

		    return;
		}
	    
		if (this.gerarCobrancaService.aceitaEnvioEmail(cota, nossoNumero)) {
		
			try {
				
				List<AnexoEmail> anexosEmail = this.obterAnexosEmailCobranca(cota, nossoNumero);
				
				if (anexosEmail != null && !anexosEmail.isEmpty()){
					
					this.enviarDocumentosCobrancaEmail(nossoNumero, email, anexosEmail);
				}
			} 
			catch(AutenticacaoEmailException e){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao conectar-se com o servidor de e-mail. Boleto["+nossoNumero+"]");
	        }		
	        catch (Exception e) {

				throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
	        }
			finally{
				
				this.boletoEmailRepository.remover(boletoEmail);
			}
		}
	}
}
