package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.baixaboleto.TipoEmissaoDocumento;
import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.ImpressaoDividaService;
import br.com.abril.nds.util.Util;

@Service
public class ImpressaoDividaServiceImpl implements ImpressaoDividaService {
	
	@Autowired
	private DividaRepository dividaRepository;
	
	@Autowired
	private DocumentoCobrancaService documentoCobrancaService;
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	private FechamentoEncalheService fechamentoEncalheService;
	
	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Transactional
	@Override
	public byte[] gerarArquivoImpressao(String nossoNumero) {
		
		return documentoCobrancaService.gerarDocumentoCobranca(nossoNumero);
	}
	
	@Transactional
	@Override
	public byte[] gerarArquivoImpressao(final FiltroDividaGeradaDTO filtro, final boolean comSlip) {
		
	    //caso a impressão inclua boleto e slip deve-se verifcar se as cotas ausentes foram cobradas
	    if (comSlip){
    	    
    	    final Integer qtdCotasAusentes = this.fechamentoEncalheService.buscarTotalCotasAusentes(filtro.getDataMovimento(), true, filtro.getNumeroCota());
    	    
    	    if (qtdCotasAusentes != null && qtdCotasAusentes > 0){
    	        
    	        throw new ValidacaoException(TipoMensagem.ERROR, "Não é possível gerar a impressão. Ainda existem cotas pendentes de geração de cobrança.");
    	    }
	    }
	    
		filtro.setColunaOrdenacao(FiltroDividaGeradaDTO.ColunaOrdenacao.ROTEIRIZACAO);
		
		List<GeraDividaDTO> dividas = null;
		List<GeraDividaDTO> dividasImpressao_EnvioEmail = new ArrayList<>();
		
		if( TipoCobranca.BOLETO.equals(filtro.getTipoCobranca())){
			dividas = dividaRepository.obterDividasGeradas(filtro);
		} else {
			dividas = dividaRepository.obterDividasGeradasSemBoleto(filtro);
		}
		
		if(dividas.isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "Não há dívidas a serem impressas.");
		}
		
//		String numeroCotasSemEmail = "";
		String numeroCotasNaoRecebemEmail = "";
		
		for (GeraDividaDTO dto : dividas) {
			
			Cota cota = cotaRepository.buscarCotaPorID(dto.getIdCota());
			
			if(filtro.getNumeroCota() == null){
				if(cota.getParametroDistribuicao().getUtilizaDocsParametrosDistribuidor()){
					if(filtro.isDistribEnviaEmail()){
						dividasImpressao_EnvioEmail.add(dto);
    				}
				}else{
					if((!Util.validarBoolean(cota.getParametroDistribuicao().getBoletoImpresso())) || (!Util.validarBoolean(cota.getParametroDistribuicao().getReciboImpresso()))){
						if(numeroCotasNaoRecebemEmail.isEmpty()){
							numeroCotasNaoRecebemEmail = cota.getNumeroCota().toString();
						}else{
							numeroCotasNaoRecebemEmail += ", "+ cota.getNumeroCota().toString();
						}
						continue;
    				}else{
    					if((Util.validarBoolean(cota.getParametroDistribuicao().getBoletoEmail())) || (Util.validarBoolean(cota.getParametroDistribuicao().getReciboEmail()))){
    						dividasImpressao_EnvioEmail.add(dto);
    					}
    				}
				}
			}
		}
		
		if(!dividasImpressao_EnvioEmail.isEmpty()){
			for (GeraDividaDTO dividaDTO : dividasImpressao_EnvioEmail) {
				enviarArquivoPorEmail(dividaDTO.getNossoNumero());
			}
		}
		
		String mensagem = "";
		
//		if (!numeroCotasSemEmail.isEmpty()) {
//			mensagem = "E-mail enviado com sucesso.\n As cotas abaixo não possuem e-mail cadastrado: \n "+ numeroCotasSemEmail +"\n";
//			mensagem += "-------------------------- \n";
//		}
		
		if(!numeroCotasNaoRecebemEmail.isEmpty()){
			mensagem += "As Cotas abaixo não recebem e-mail: \n"+ numeroCotasNaoRecebemEmail;
		}
		
		filtro.setMensagemValidacaoImpressao(mensagem);
		
		final List<PoliticaCobranca> politicasCobranca = politicaCobrancaRepository.obterPoliticasCobranca(Arrays.asList(TipoCobranca.BOLETO, TipoCobranca.BOLETO_EM_BRANCO));
		
		if (comSlip){
			return documentoCobrancaService.gerarDocumentoCobrancaComSlip(dividas, filtro, politicasCobranca, filtro.getDataMovimento());
		} else {
		    
		    return documentoCobrancaService.gerarDocumentoCobranca(dividas, filtro.getTipoCobranca());
		}
	}

	@Transactional
	@Override
	public void enviarArquivoPorEmail(String nossoNumero) {
		
		documentoCobrancaService.enviarDocumentoCobrancaPorEmail(nossoNumero);
	}
	
	
	@Transactional(readOnly=true)
	@Override
	public List<GeraDividaDTO> obterDividasGeradas(FiltroDividaGeradaDTO filtro) {
	
		List<GeraDividaDTO> dividasGeradas = dividaRepository.obterDividasGeradas(filtro);
		
		for(GeraDividaDTO umaDividaGerada : dividasGeradas){
		    
		    
		    umaDividaGerada.setSuportaEmail( gerarCobrancaService.aceitaEmissaoDocumento(umaDividaGerada.getIdCota(), TipoEmissaoDocumento.EMAIL_BOLETO_RECIBO));
		   
		}
			
		return dividasGeradas;
	}
	
	@Transactional(readOnly=true)
	@Override
	public Long obterQuantidadeDividasGeradas(FiltroDividaGeradaDTO filtro) {
		
		return dividaRepository.obterQuantidadeRegistroDividasGeradas(filtro);
	}
	
	@Transactional(readOnly=true)
	@Override
	public Boolean validarDividaGerada(Date dataMovimento) {
		
		Long quantidadeRegistro = dividaRepository.obterQunatidadeDividaGeradas(dataMovimento);
		
		return (quantidadeRegistro == null || quantidadeRegistro == 0) ? Boolean.FALSE : Boolean.TRUE;
	}

}
