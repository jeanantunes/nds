package br.com.abril.nds.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.QuantidadePrecoItemNotaDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.DistribuidorTipoNotaFiscal;
import br.com.abril.nds.model.cadastro.NotaFiscalTipoEmissao.NotaFiscalTipoEmissaoEnum;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.model.cadastro.TributoAliquota;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.ProcessoEmissao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalBase;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.repository.NotaFiscalNdsRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.ProdutoServicoRepository;
import br.com.abril.nds.repository.SerieRepository;
import br.com.abril.nds.service.FTFService;
import br.com.abril.nds.service.GeracaoNFeService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.TransportadorService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.builders.EmitenteDestinatarioBuilder;
import br.com.abril.nds.service.builders.FaturaBuilder;
import br.com.abril.nds.service.builders.FaturaEstoqueProdutoNotaFiscalBuilder;
import br.com.abril.nds.service.builders.ItemNotaFiscalBuilder;
import br.com.abril.nds.service.builders.ItemNotaFiscalEstoqueProdutoBuilder;
import br.com.abril.nds.service.builders.NaturezaOperacaoBuilder;
import br.com.abril.nds.service.builders.NotaFiscalBuilder;
import br.com.abril.nds.service.builders.NotaFiscalEstoqueProdutoBuilder;
import br.com.abril.nds.service.builders.NotaFiscalTransportadorBuilder;
import br.com.abril.nds.service.builders.NotaFiscalValoresCalculadosBuilder;
import br.com.abril.nds.util.Intervalo;

@Service
public class GeracaoNFeServiceImpl implements GeracaoNFeService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(GeracaoNFeServiceImpl.class);
	
	@Autowired
	private NotaFiscalService notaFiscalService;

	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	@Autowired
	private ProdutoServicoRepository produtoServicoRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired 
	private SerieRepository serieRepository;
	
	@Autowired 
	private NaturezaOperacaoRepository naturezaOperacaoRepository;
	
	@Autowired 
	private NotaFiscalNdsRepository notaFiscalNdsRepository; 
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
    private ParametrosDistribuidorService parametrosDistribuidorService;
	
	@Autowired
	private FTFService ftfService;

	@Autowired
    private TransportadorService transportadorService;
	
	// Trava para evitar duplicidade ao gerar notas por mais de um usuario simultaneamente
    // O HashMap suporta mais detalhes e pode ser usado futuramente para restricoes mais finas
    private static final Map<String, Object> TRAVA_GERACAO_NFe = new HashMap<>();
	
	@Override
	@Transactional
	public synchronized List<CotaExemplaresDTO> busca(Intervalo<Integer> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento,
			List<Long> listIdFornecedor, Long idTipoNotaFiscal, Long idRoteiro, Long idRota,
			String sortname, String sortorder, Integer resultsPage, Integer page, SituacaoCadastro situacaoCadastro) {
		
		if (TRAVA_GERACAO_NFe.get("NFesSendoGeradas") != null) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Notas de envio sendo geradas por outro usuário, tente novamente mais tarde.");
        }
        
        TRAVA_GERACAO_NFe.put("NFesSendoGeradas", true);
        
        List<CotaExemplaresDTO> listaCotaExemplares = null; 
        		
        try {
        	
			Set<NaturezaOperacao> naturezasOperacoes = new HashSet<NaturezaOperacao>();
			naturezasOperacoes.add(this.naturezaOperacaoRepository.buscarPorId(idTipoNotaFiscal));
			
			List<SituacaoCadastro> situacoesCadastro = null;
			
			if (situacaoCadastro != null){
				situacoesCadastro = new ArrayList<SituacaoCadastro>();
				situacoesCadastro.add(situacaoCadastro);
			}
			
			Set<Long> idsCotasDestinatarias = new HashSet<>();
			idsCotasDestinatarias.addAll(this.cotaRepository.obterIdCotasEntre(intervalorCota, intervaloBox, situacoesCadastro, idRoteiro, idRota, null, null, null, null));
			
			ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal = new ConsultaLoteNotaFiscalDTO();
			
			dadosConsultaLoteNotaFiscal.setTipoNotaFiscal(naturezasOperacoes);
			dadosConsultaLoteNotaFiscal.setPeriodoMovimento(intervaloDateMovimento);
			dadosConsultaLoteNotaFiscal.setIdsCotasDestinatarias(idsCotasDestinatarias);
			dadosConsultaLoteNotaFiscal.setListaIdFornecedores(listIdFornecedor);
			
			Map<Cota, QuantidadePrecoItemNotaDTO> cotasTotalItens = this.notaFiscalService.obterTotalItensNotaFiscalPorCotaEmLote(dadosConsultaLoteNotaFiscal);
			
			listaCotaExemplares = new ArrayList<CotaExemplaresDTO>();
			
			for (Entry<Cota, QuantidadePrecoItemNotaDTO> entry : cotasTotalItens.entrySet()) {
				
				CotaExemplaresDTO cotaExemplares = new CotaExemplaresDTO();
				
				cotaExemplares.setIdCota(entry.getKey().getId());
				cotaExemplares.setExemplares(entry.getValue().getQuantidade());
				cotaExemplares.setNomeCota(entry.getKey().getPessoa().getNome());
				cotaExemplares.setNumeroCota(entry.getKey().getNumeroCota());
				cotaExemplares.setTotal(entry.getValue().getPreco());
				cotaExemplares.setTotalDesconto(entry.getValue().getPrecoComDesconto());
				
				listaCotaExemplares.add(cotaExemplares);
				
			}
			
        } catch (Exception e) {
        	LOGGER.error("", e);
        	throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar NF-e.");
        	
        } finally {
            TRAVA_GERACAO_NFe.remove("NFesSendoGeradas");
        }
		
        return listaCotaExemplares;
        
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.GeracaoNFeService#gerarNotaFiscal(br.com.abril.nds.util.Intervalo, br.com.abril.nds.util.Intervalo, br.com.abril.nds.util.Intervalo, java.util.List, java.util.List, java.lang.Long)
	 */
	@Override
	@Transactional(rollbackFor=Throwable.class)
	public synchronized List<NotaFiscal> gerarNotaFiscal(FiltroNFeDTO filtro) throws FileNotFoundException, IOException {
		
		/**
		 * metodo para gerar nota.
		 */
		this.validarFiltroNFe(filtro);
		List<NotaFiscal> notas = new ArrayList<NotaFiscal>();
		Distribuidor distribuidor = this.obterInformacaoDistribuidor();
		
		if(distribuidor.isPossuiRegimeEspecialDispensaInterna()){
			if(new Date().getTime() < distribuidor.getDataLimiteVigenciaRegimeEspecial().getTime()){
				throw new ValidacaoException(TipoMensagem.WARNING, "A data limite de vigincia do regime especial expirou!" );
			}
		}
		
		NaturezaOperacao naturezaOperacao = this.naturezaOperacaoRepository.obterNaturezaOperacao(filtro.getIdNaturezaOperacao());
		Map<String, ParametroSistema> parametrosSistema = parametroSistemaRepository.buscarParametroSistemaGeralMap();
		
		switch (naturezaOperacao.getTipoDestinatario()) {
		
			case COTA:
			case DISTRIBUIDOR:
				
				if(!distribuidor.isPossuiRegimeEspecialDispensaInterna()) {
					
					this.gerarNotasFiscaisCotas(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema);
					
				} else {
					//
					boolean notaGerada = false;
					
					for(DistribuidorTipoNotaFiscal dtnf : distribuidor.getTiposNotaFiscalDistribuidor()) {
						if(dtnf.getNaturezaOperacao().contains(naturezaOperacao)) {
							if(dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.DESOBRIGA_EMISSAO)) {									
								throw new ValidacaoException(TipoMensagem.ERROR, "O regime especial dispensa emissao para essa natureza de operação");
							}
							
							if(dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.CONSOLIDA_EMISSAO_A_JORNALEIROS_DIVERSOS)) {			
								this.gerarNotaFiscalUnificada(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema);
							} else {
								this.gerarNotasFiscaisCotas(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema);
							}
							
							notaGerada = true;
							break;
						}
					}
					
					if(!notaGerada) {
						throw new ValidacaoException(TipoMensagem.ERROR, "Natureza de Operação não está configurada adequadamente para o Regime Especial.");
					}
				}
				
				break;
				
			case FORNECEDOR:			
				this.gerarNotasFiscaisFornecedor(filtro, distribuidor, naturezaOperacao);
				break;
	
			default:
				throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de Destinatário não especificado");
				
		}
		
		if(notas == null || notas.isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foram encontrados itens para gerar nota.");
		
		for (NotaFiscal notaFiscal : notas) {
			notaFiscalRepository.adicionar(notaFiscal);
		}
		
		ParametroSistema ps = parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.NFE_INFORMACOES_TIPO_EMISSOR);
		if (ProcessoEmissao.EMISSAO_NFE_APLICATIVO_CONTRIBUINTE.equals(ProcessoEmissao.valueOf(ps.getValor()))) {
			this.ftfService.gerarFtf(notas, notas.get(0).getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getId());
			
			throw new ValidacaoException(TipoMensagem.WARNING, "FTF gerado");
		} else {
			
			this.notaFiscalService.exportarNotasFiscais(notas);
		}
		
		return notas;
	}

	private void validarFiltroNFe(FiltroNFeDTO filtro) {
		
		if(filtro.getDataInicial() == null || filtro.getDataFinal() == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "As datas inicial e final não podem ser nulas.");
		} 
		
		if(filtro.getDataFinal().getTime() < filtro.getDataInicial().getTime()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "A data inicial não pode ser maior que a da final.");
		}
	}

	private void gerarNotasFiscaisCotas(FiltroNFeDTO filtro,
			List<NotaFiscal> notasFiscais, Distribuidor distribuidor, NaturezaOperacao naturezaOperacao, Map<String, ParametroSistema> parametrosSistema) {
		
		// obter as cotas que estão na tela pelo id das cotas
		List<Cota> cotas = this.notaFiscalNdsRepository.obterConjuntoCotasNotafiscal(filtro);
		
		List<Transportador> transportadores = this.transportadorService.buscarTransportadores();
		
		Map<String, TributoAliquota> tributoRegimeTributario = new HashMap<String, TributoAliquota>();
		
		for(TributoAliquota tributo : distribuidor.getRegimeTributario().getTributosAliquotas()){
			tributoRegimeTributario.put(tributo.getTributo().getNome(), tributo);
		}
		
		for (Cota cota : cotas) {
			
			NotaFiscal notaFiscal = new NotaFiscal();
			naturezaOperacao.setNotaFiscalNumeroNF(naturezaOperacao.getNotaFiscalNumeroNF() + 1);
			naturezaOperacaoRepository.merge(naturezaOperacao);
			
			notaFiscal.setUsuario(usuarioService.getUsuarioLogado());
			
			NotaFiscalBuilder.popularDadosDistribuidor(notaFiscal, distribuidor, filtro);
			
			NotaFiscalTransportadorBuilder.montarTransportador(notaFiscal, naturezaOperacao, transportadores);
			
			NotaFiscalBuilder.montarHeaderNotaFiscal(notaFiscal, cota, parametrosSistema);
			
			EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, cota);
			
			NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
			
			// obter os movimentos de cada cota
			filtro.setIdCota(cota.getId());
			List<MovimentoEstoqueCota> movimentosEstoqueCota = this.notaFiscalNdsRepository.obterMovimentosEstoqueCota(filtro);
			for (MovimentoEstoqueCota movimentoEstoqueCota : movimentosEstoqueCota) {
				ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoEstoqueCota, tributoRegimeTributario);
			}
			
			//FIXME: Ajustar o valor do campo para valores parametrizados
			notaFiscal.getNotaFiscalInformacoes().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
			FaturaBuilder.montarFaturaNotaFiscal(notaFiscal, movimentosEstoqueCota);
			NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notaFiscal, cota);
			notasFiscais.add(notaFiscal);
		}
	}
	
	private void gerarNotaFiscalUnificada(FiltroNFeDTO filtro, List<NotaFiscal> notasFiscais, Distribuidor distribuidor, NaturezaOperacao naturezaOperacao, Map<String, ParametroSistema> parametrosSistema) {
		
		// obter as cotas que estão na tela pelo id das cotas
		NotaFiscal notaFiscal = new NotaFiscal();
		List<Transportador> transportadores = this.transportadorService.buscarTransportadores();
		naturezaOperacao.setNotaFiscalNumeroNF(naturezaOperacao.getNotaFiscalNumeroNF() + 1);
		naturezaOperacaoRepository.merge(naturezaOperacao);
		
		List<Cota> cotas = this.notaFiscalNdsRepository.obterConjuntoCotasNotafiscal(filtro);
		notaFiscal.setUsuario(usuarioService.getUsuarioLogado());
		
		Map<String, TributoAliquota> tributoAliquota = new HashMap<String, TributoAliquota>();
		
		for(TributoAliquota tributo : distribuidor.getRegimeTributario().getTributosAliquotas()){
			tributoAliquota.put(tributo.getTributo().getNome(), tributo);
		}
		
		
		NotaFiscalBuilder.popularDadosDistribuidor(notaFiscal, distribuidor, filtro);
		NotaFiscalBuilder.popularDadosTransportadora(notaFiscal, distribuidor, filtro);
		EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, distribuidor);
		NotaFiscalBuilder.montarHeaderNotaFiscal(notaFiscal, distribuidor, parametrosSistema);
		NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
		for (Cota cota : cotas) {
			
			// FIX arrumar endereco
			// obter os movimentos de cada cota
			filtro.setIdCota(cota.getId());
			List<MovimentoEstoqueCota> movimentosEstoqueCota = this.notaFiscalNdsRepository.obterMovimentosEstoqueCota(filtro);
			for (MovimentoEstoqueCota movimentoEstoqueCota : movimentosEstoqueCota) {
				ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoEstoqueCota, tributoAliquota);
			}
			FaturaBuilder.montarFaturaNotaFiscal(notaFiscal, movimentosEstoqueCota);
			NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notaFiscal, cota);
		}
		
		notaFiscal.getNotaFiscalInformacoes().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
		
		//FIXME: Ajustar o transportador Principal
		if(transportadores.isEmpty() || transportadores == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Não foi .");
		}else {			
			NotaFiscalTransportadorBuilder.montarTransportador(notaFiscal, naturezaOperacao, transportadores);
		}
		
		notasFiscais.add(notaFiscal);
	}
	
	private void gerarNotasFiscaisFornecedor(FiltroNFeDTO filtro, Distribuidor distribuidor, NaturezaOperacao naturezaOperacao) {

		// obter as cotas que estão na tela pelo id das cotas
		List<EstoqueProduto> estoques = this.notaFiscalNdsRepository.obterConjuntoFornecedorNotafiscal(filtro);
		
		Map<String, TributoAliquota> tributoRegimeTributario = new HashMap<String, TributoAliquota>();
		
		for(TributoAliquota tributo : distribuidor.getRegimeTributario().getTributosAliquotas()){
			tributoRegimeTributario.put(tributo.getTributo().getNome(), tributo);
		}
		
		
		for (EstoqueProduto estoque : estoques) {
			NotaFiscal notaFiscal = new NotaFiscal();
			
			// popular distribuidor
			NotaFiscalEstoqueProdutoBuilder.popularDadosDistribuidor(notaFiscal, distribuidor, filtro);
			
			// popular header
			NotaFiscalEstoqueProdutoBuilder.montarHeaderNotaFiscal(notaFiscal, estoque, distribuidor);
			
			EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, estoque);
			
			NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
			
			// obter os estoques
			filtro.setIdCota(estoque.getId());
			List<EstoqueProduto> estoqueProdutos = this.notaFiscalNdsRepository.obterEstoques(filtro);
			for (EstoqueProduto estoqueProduto : estoqueProdutos) {
				
				ItemNotaFiscalEstoqueProdutoBuilder.montaItemNotaFiscal(notaFiscal, estoqueProduto, tributoRegimeTributario);
			}
			
			FaturaEstoqueProdutoNotaFiscalBuilder.montarFaturaEstoqueProdutoNotaFiscal(notaFiscal, estoqueProdutos);
			
			NotaFiscalValoresCalculadosBuilder.montarValoresCalculadosEstoqueProduto(notaFiscal, estoque);
			
			notaFiscal.getNotaFiscalInformacoes().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
		}	
	}
	
	@Override
	@Transactional
	public List<CotaExemplaresDTO> consultaCotaExemplaresSumarizados(FiltroNFeDTO filtro) {
		return notaFiscalService.consultaCotaExemplareSumarizado(filtro);
	}

	@Override
	@Transactional
	public Long consultaCotaExemplareSumarizadoQtd(FiltroNFeDTO filtro) {
		return notaFiscalService.consultaCotaExemplareSumarizadoQtd(filtro);
	}
	
	// metodo responsavel pelo dados do distribuidor da nota
	public Distribuidor obterInformacaoDistribuidor(){
		return distribuidorRepository.obter();
	}
	
	public Long serieNotaFiscal (NotaFiscalBase notaFiscal){
		return serieRepository.obterNumeroSerieNota();
	}

	@Override
	public List<FornecedorExemplaresDTO> consultaFornecedorExemplarSumarizado(FiltroNFeDTO filtro) {
		return notaFiscalService.consultaFornecedorExemplarSumarizado(filtro);
	}

	@Override
	public Long consultaFornecedorExemplaresSumarizadosQtd(FiltroNFeDTO filtro) {
		return notaFiscalService.consultaFornecedorExemplaresSumarizadosQtd(filtro);
	}
	
}
