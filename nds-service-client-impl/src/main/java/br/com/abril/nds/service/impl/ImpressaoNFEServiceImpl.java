package br.com.abril.nds.service.impl;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.DanfeDTO;
import br.com.abril.nds.dto.DanfeWrapper;
import br.com.abril.nds.dto.NotasCotasImpressaoNfeDTO;
import br.com.abril.nds.dto.ProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.DistribuidorTipoNotaFiscal;
import br.com.abril.nds.model.cadastro.DistribuidorTipoNotaFiscal.DistribuidorGrupoNotaFiscal;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.NotaFiscalTipoEmissao.NotaFiscalTipoEmissaoEnum;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.ImpressaoNFeRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ImpressaoNFEService;
import br.com.abril.nds.service.NFeService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.builders.DanfeBuilder;
import br.com.abril.nds.service.integracao.DistribuidorService;

/**
 * @author InfoA2
 */
@Service
public class ImpressaoNFEServiceImpl implements ImpressaoNFEService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImpressaoNFEServiceImpl.class);
	
	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private ImpressaoNFeRepository impressaoNFeRepository;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;
	
	@Autowired
	private NFeService nFeService;

	@Autowired 
	private NaturezaOperacaoRepository naturezaOperacaoRepository;

	@Autowired
    private RoteirizacaoRepository roteirizacaoRepository;

	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	@Transactional
	public List<ProdutoDTO> obterProdutosExpedicaoConfirmada(FiltroImpressaoNFEDTO filtro) {
		
		//Filtra os produtos pelos fornecedores do distribuidor
		List<Fornecedor> fornecedores = this.fornecedorService.obterFornecedores(SituacaoCadastro.ATIVO);
		List<Long> idsFornecedores = new ArrayList<Long>();
		for(Fornecedor f : fornecedores) {
			idsFornecedores.add(f.getId());
		}
		filtro.setIdsFornecedores(idsFornecedores);
		
		List<ProdutoDTO> produtosDTO = new ArrayList<ProdutoDTO>();
		List<Produto> produtos = impressaoNFeRepository.buscarProdutosParaImpressaoNFe(filtro);
		for(Produto p : produtos) {
			ProdutoDTO prod = new ProdutoDTO();
			prod.setCodigoProduto(p.getCodigo());
			prod.setNomeProduto(p.getNome());
			produtosDTO.add(prod);
		}
		
		return produtosDTO;
	}

	@Transactional
	public List<NotasCotasImpressaoNfeDTO> buscarCotasParaImpressaoNFe(FiltroImpressaoNFEDTO filtro) {

		List<NotasCotasImpressaoNfeDTO> cotas = null;
		
		/*
		if(!this.distribuidorRepository.obrigacaoFiscal()) {
			cotas = impressaoNFeRepository.buscarCotasParaImpressaoNotaEnvio(filtro);
		} else {
			cotas = impressaoNFeRepository.buscarCotasParaImpressaoNFe(filtro);
		}*/
		cotas = impressaoNFeRepository.buscarCotasParaImpressaoNFe(filtro);
				
		return cotas;
	}
	
	@Transactional
	public Integer buscarNFeParaImpressaoTotalQtd(FiltroImpressaoNFEDTO filtro) {
		
		if(!this.distribuidorRepository.obrigacaoFiscal()) {
			return impressaoNFeRepository.buscarCotasParaImpressaoNotaEnvioQtd(filtro);
		} else {
			return impressaoNFeRepository.buscarCotasParaImpressaoNFeQtd(filtro);
		}
		
	}

	@Transactional
	public List<NotaFiscal> buscarNotasParaImpressaoNFe(FiltroImpressaoNFEDTO filtro) {
		return impressaoNFeRepository.buscarNotasParaImpressaoNFe(filtro);
	}

	@Transactional
	public List<NotaEnvio> buscarNotasEnvioParaImpressaoNFe(FiltroImpressaoNFEDTO filtro) {
		return impressaoNFeRepository.buscarNotasEnvioParaImpressaoNFe(filtro);
	}

	@Override
	@Transactional
	public List<NotasCotasImpressaoNfeDTO> obterNotafiscalImpressao(FiltroImpressaoNFEDTO filtro) {
		
		NaturezaOperacao naturezaOperacao = naturezaOperacaoRepository.buscarPorId(filtro.getIdNaturezaOperacao());
		
		filtro.setNaturezaOperacao(naturezaOperacao);
		
		return this.impressaoNFeRepository.obterNotafiscalImpressao(filtro);
	}

	@Override
	@Transactional
	public byte[] imprimirNFe(FiltroImpressaoNFEDTO filtro) {
		
		LOGGER.info("Metodo responsavel pela impressão de NFE...");
		NaturezaOperacao naturezaOperacao = this.naturezaOperacaoRepository.obterNaturezaOperacao(filtro.getIdNaturezaOperacao());
		
		filtro.setNaturezaOperacao(naturezaOperacao);
		
		List<DanfeWrapper> listaDanfeWrapper = new ArrayList<DanfeWrapper>();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		InputStream logoDistribuidor = this.parametrosDistribuidorService.getLogotipoDistribuidor();
		  
		if(TipoAtividade.MERCANTIL.equals(distribuidor.getTipoAtividade())) {
			
			if(distribuidor.isPossuiRegimeEspecialDispensaInterna()) {
				
				for(DistribuidorTipoNotaFiscal dtnf : distribuidor.getTiposNotaFiscalDistribuidor()) {
					
					if(dtnf.getGrupoNotaFiscal().equals(DistribuidorGrupoNotaFiscal.NOTA_FISCAL_ENVIO_PARA_COTA)) {
						if(dtnf.getNaturezaOperacao().contains(naturezaOperacao)){
							if(!dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.DESOBRIGA_EMISSAO)) {
								
								LOGGER.info("obter informações para imprimir DANFE ou NECA... ");				

								List<NotaFiscal> notas = null;;
								
								notas = notasPorCotaFornecedor(filtro, naturezaOperacao, notas);
								
								for (NotaFiscal notaFiscal : notas) {
									
									DanfeDTO danfe = montarDanfe(notaFiscal);
									if(danfe != null) {										
										listaDanfeWrapper.add(new DanfeWrapper(danfe));
									}
								}
								
							}
						} 
					} else if (dtnf.getGrupoNotaFiscal().equals(DistribuidorGrupoNotaFiscal.NOTA_FISCAL_VENDA)) {
						if(dtnf.getNaturezaOperacao().contains(naturezaOperacao)){
							
							if(!dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.DESOBRIGA_EMISSAO)) {
								
								LOGGER.info("obter informações para imprimir DANFE ou NECA... ");				
								
								List<NotaFiscal> notas = null;;
								
								notas = notasPorCotaFornecedor(filtro, naturezaOperacao, notas);
								
								for (NotaFiscal notaFiscal : notas) {
									
									DanfeDTO danfe = montarDanfe(notaFiscal);
									if(danfe != null) {										
										listaDanfeWrapper.add(new DanfeWrapper(danfe));
									}
								}
								
							}
						}
					} else if (dtnf.getGrupoNotaFiscal().equals(DistribuidorGrupoNotaFiscal.NOTA_FISCAL_DEVOLUCAO_AO_FORNECEDOR)) {
						if(dtnf.getNaturezaOperacao().contains(naturezaOperacao)){
							
							if(!dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.DESOBRIGA_EMISSAO)) {
								
								LOGGER.info("obter informações para imprimir DANFE ou NECA... ");				
								
								List<NotaFiscal> notas = null;;
								
								notas = notasPorCotaFornecedor(filtro, naturezaOperacao, notas);
								
								for (NotaFiscal notaFiscal : notas) {
									
									DanfeDTO danfe = montarDanfe(notaFiscal);
									if(danfe != null) {										
										listaDanfeWrapper.add(new DanfeWrapper(danfe));
									}
								}
								
							}
						}
					}
				
				}
				
			} else {
				
				LOGGER.info("obter nota MERCANTIL ");
				
				// saber qual e modelo para gera MODELO 1 MODELO 2...

				List<NotaFiscal> notas = null;;
				
				notas = notasPorCotaFornecedor(filtro, naturezaOperacao, notas);
				
				for (NotaFiscal notaFiscal : notas) {
					
					DanfeDTO danfe = montarDanfe(notaFiscal);
					if(danfe != null) {						
						listaDanfeWrapper.add(new DanfeWrapper(danfe));
					}
				}
			}
		} else if(TipoAtividade.PRESTADOR_SERVICO.equals(distribuidor.getTipoAtividade()) || TipoAtividade.PRESTADOR_FILIAL.equals(distribuidor.getTipoAtividade())) {
			
			LOGGER.info("PRESTADOR_SERVICO ..... ");
			
			if(distribuidor.isPossuiRegimeEspecialDispensaInterna()){
				
				for(DistribuidorTipoNotaFiscal dtnf : distribuidor.getTiposNotaFiscalDistribuidor()){
					
					if(dtnf.getGrupoNotaFiscal().equals(DistribuidorGrupoNotaFiscal.NOTA_FISCAL_ENVIO_PARA_COTA)) {
						if(dtnf.getNaturezaOperacao().contains(naturezaOperacao)){
							if(!dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.DESOBRIGA_EMISSAO)) {
								
								LOGGER.info("obter notas para prestador filial ");				
								
								List<NotaFiscal> notas = null;;
								
								notas = notasPorCotaFornecedor(filtro, naturezaOperacao, notas);
								
								for (NotaFiscal notaFiscal : notas) {
									DanfeDTO danfe = montarDanfe(notaFiscal);
									if(danfe!=null) {
										listaDanfeWrapper.add(new DanfeWrapper(danfe));
									}
								}
								
							}
						} else {
							
							throw new ValidacaoException(TipoMensagem.ERROR, "O regime especial dispensa emissao para essa natureza de operação");
							
						}
					}
				}
				
			} else {
				
				List<NotaFiscal> notas = null; 
				
				notas = notasPorCotaFornecedor(filtro, naturezaOperacao, notas);
				
				for (NotaFiscal notaFiscal : notas) {
					DanfeDTO danfe = montarDanfe(notaFiscal);
					if(danfe!=null) {
						listaDanfeWrapper.add(new DanfeWrapper(danfe));
					}
				}
			}		
			
		} else {
			throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao comparar o tipo de atividade");
		}

		return DanfeBuilder.gerarDocumentoIreport(listaDanfeWrapper, false, obterDiretorioReports(), logoDistribuidor);
	}

	private List<NotaFiscal> notasPorCotaFornecedor(FiltroImpressaoNFEDTO filtro, NaturezaOperacao naturezaOperacao, List<NotaFiscal> notas) {
		switch (naturezaOperacao.getTipoDestinatario()) {
		
			case COTA:
			case DISTRIBUIDOR:
			                 
				notas = this.impressaoNFeRepository.buscarNotasParaImpressaoNFe(filtro);
				
				List<NotaFiscal> notasRoterizadas = ordenarNotasFiscaisPorRoteirizacao(notas);
				
				if(notasRoterizadas != null &&  !notasRoterizadas.isEmpty()) {					
					notas = notasRoterizadas;
				}
				
				break;
		    
			case FORNECEDOR:            
				notas = this.impressaoNFeRepository.buscarNotasParaImpressaoNFe(filtro);
				break;        
		}
		return notas;
	}

    /**
	* Ordena a lista de Notas de Fiscais por Roteirização.
	* 
	* @param notasFiscais - lista de notas fiscais
	* 
	* @return List<NotaFiscal>
	*/
	private List<NotaFiscal> ordenarNotasFiscaisPorRoteirizacao(final List<NotaFiscal> notasFiscais) {
	
		final Map<Integer, List<NotaFiscal>> mapaNotasFiscaisPorCota = new HashMap<Integer, List<NotaFiscal>>();
		
		for (final NotaFiscal nfe : notasFiscais) {
		
			final Integer numeroCota = nfe.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getCota().getNumeroCota();
		
			List<NotaFiscal> nfes = mapaNotasFiscaisPorCota.get(numeroCota);
		
			if (nfes == null) {
		
				nfes = new ArrayList<NotaFiscal>();
			}
			
			nfes.add(nfe);
		
			mapaNotasFiscaisPorCota.put(numeroCota, nfes);
		}
	
		final List<Integer> numerosCotaOrdenadosPelaRoteirizacao = roteirizacaoRepository.obterNumerosCotaOrdenadosRoteirizacao();
	
		final List<NotaFiscal> notasFiscaisOrdenadas = new ArrayList<>();
	
		for (final Integer numeroCota : numerosCotaOrdenadosPelaRoteirizacao) {
	
			if (mapaNotasFiscaisPorCota.containsKey(numeroCota)) {
	
				notasFiscaisOrdenadas.addAll(mapaNotasFiscaisPorCota.get(numeroCota));
				
			} 
		}
	
		return notasFiscaisOrdenadas;
	}
	
	private DanfeDTO montarDanfe(NotaFiscal notaFiscal) {
		
		DanfeDTO danfe = new DanfeDTO();
		
		DanfeBuilder.carregarDanfeDadosPrincipais(danfe, notaFiscal);
		
		DanfeBuilder.carregarDanfeDadosEmissor(danfe, notaFiscal);

		DanfeBuilder.carregarDanfeDadosDestinatario(danfe, notaFiscal);
		
		DanfeBuilder.carregarDadosItensDanfe(danfe, notaFiscal);
		
		DanfeBuilder.carregarDanfeDadosTributarios(danfe, notaFiscal);
		
		DanfeBuilder.carregarDanfeDadosTransportadora(danfe, notaFiscal);
		
		DanfeBuilder.carregarDadosDuplicatas(danfe, notaFiscal);
		
		ConsultaRoteirizacaoDTO roteirizacao = this.roteirizacao(notaFiscal); 
		
		if(roteirizacao != null) {			
			DanfeBuilder.carregarDanfeRoteirizacao(danfe, roteirizacao);
		}	
		
		return danfe;
	}

	private ConsultaRoteirizacaoDTO roteirizacao(NotaFiscal notaFiscal) {
		
        ConsultaRoteirizacaoDTO roteirizacaoDTO = null;
        
        if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario() != null ) {
        	
        	if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getCota() != null) {
        		
        		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
        		
        		filtro.setNumeroCota(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getCota().getNumeroCota());
        		
        		List<ConsultaRoteirizacaoDTO> listaRoteirizacaoDTO = this.roteirizacaoRepository.buscarRoteirizacao(filtro); 
        		
        		for (ConsultaRoteirizacaoDTO item : listaRoteirizacaoDTO){
        			
        			if (!item.getNomeBox().equals("Especial")){
        				
        				roteirizacaoDTO = item;
        				
        				return roteirizacaoDTO;
        			}
        		}
        	}    
        }
	    return null;		
	}
	
	protected URL obterDiretorioReports() {
		
		URL urlDanfe = Thread.currentThread().getContextClassLoader().getResource("reports");
		
		return urlDanfe;
	}
	
}
