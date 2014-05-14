package br.com.abril.nds.service.impl;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.NfeVO;
import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.NfeImpressaoDTO;
import br.com.abril.nds.dto.NfeImpressaoWrapper;
import br.com.abril.nds.dto.QuantidadePrecoItemNotaDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.DistribuidorTipoNotaFiscal;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.NotaFiscalTipoEmissao.NotaFiscalTipoEmissaoEnum;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoImpressaoNENECADANFE;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.model.cadastro.TributoAliquota;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscal;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.Identificacao.ProcessoEmissao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ItemNotaFiscalSaidaRepository;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.repository.NotaEnvioRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.service.FTFService;
import br.com.abril.nds.service.MonitorNFEService;
import br.com.abril.nds.service.NFeService;
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
import br.com.abril.nds.service.builders.NecaBuilder;
import br.com.abril.nds.service.builders.NotaFiscalBuilder;
import br.com.abril.nds.service.builders.NotaFiscalEstoqueProdutoBuilder;
import br.com.abril.nds.service.builders.NotaFiscalTransportadorBuilder;
import br.com.abril.nds.service.builders.NotaFiscalValoresCalculadosBuilder;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;

@Service
public class NFeServiceImpl implements NFeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NFeServiceImpl.class);
	
	@Autowired
	protected NotaFiscalRepository notaFiscalRepository;
	
	@Autowired
	protected NotaEnvioRepository notaEnvioRepository;
	
	@Autowired
	protected MonitorNFEService monitorNFEService;
	
	@Autowired
	protected ParametrosDistribuidorService parametrosDistribuidorService;

	@Autowired
	protected ItemNotaFiscalEntradaRepository itemNotaFiscalEntradaRepository;

	@Autowired
	protected ItemNotaFiscalSaidaRepository itemNotaFiscalSaidaRepository;
	
	@Autowired
	protected DistribuidorRepository distribuidorRepository;
	
	@Autowired 
	private NaturezaOperacaoRepository naturezaOperacaoRepository;
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private FTFService ftfService;

	@Autowired
    private TransportadorService transportadorService;
	
	@Autowired
	private NotaFiscalService notaFiscalService;
	
	@Autowired
	private CotaRepository cotaRepository;

	// Trava para evitar duplicidade ao gerar notas por mais de um usuario simultaneamente
    // O HashMap suporta mais detalhes e pode ser usado futuramente para restricoes mais finas
    private static final Map<String, Object> TRAVA_GERACAO_NFe = new HashMap<>();
	
    /**
     * Obtém os arquivos das DANFE relativas as NFes passadas como parâmetro.
     * 
     * @param listaNfeImpressao
     * @param indEmissaoDepec
     * 
     * @return byte[] - Bytes das DANFES
     */
    @Override
    @Transactional
    public byte[] obterDanfesPDF(final List<NotaFiscal> listaNfeImpressao, final boolean indEmissaoDepec) {
        
        final List<NfeVO> nfes = new ArrayList<NfeVO>();
        for(final NotaFiscal nf : listaNfeImpressao) {
            final NfeVO nfe = new NfeVO();
            nfe.setIdNotaFiscal(nf.getId());
            nfes.add(nfe);
        }
        
        return monitorNFEService.obterDanfes(nfes, indEmissaoDepec);
        
    }
    
    @Override
    @Transactional
    public byte[] obterNEsPDF(final List<NotaEnvio> listaNfeImpressaoNE, final boolean dispensaEmissaoNFe, final Intervalo<Date> intervaloLancamento) throws Exception {
        
        final List<NfeImpressaoWrapper> listaNEWrapper = new ArrayList<NfeImpressaoWrapper>();
        
        for(final NotaEnvio ne :  listaNfeImpressaoNE) {
            
            final NfeImpressaoDTO nfeImpressao = obterDadosNENECA(ne);
            
            if(nfeImpressao!=null) {
                
                if(intervaloLancamento != null) {
                    nfeImpressao.setDataLancamentoDeAte(this.getStringDataDeAte(intervaloLancamento));
                }
                
                listaNEWrapper.add(new NfeImpressaoWrapper(nfeImpressao));
            }
            
        }
        
        try {
            
            return gerarDocumentoIreportNE(listaNEWrapper, dispensaEmissaoNFe, false);
            
        } catch(final Exception e) {
            LOGGER.error("Falha na geração dos arquivos NE!" + e.getMessage(), e);
            throw new RuntimeException("Falha na geração dos arquivos NE!", e);
		}
	}
	
	@Transactional
	public NotaFiscal obterNotaFiscalPorId(NotaFiscal notaFiscal) {
		return notaFiscalRepository.buscarPorId(notaFiscal.getId());
	}
	
	@Transactional
	public NotaEnvio obterNotaEnvioPorId(NotaEnvio notaEnvio) {
		return notaEnvioRepository.buscarPorId(notaEnvio.getNumero());
	}
	
	@Transactional
	public NotaFiscal mergeNotaFiscal(final NotaFiscal notaFiscal) {
		return notaFiscalRepository.merge(notaFiscal);
	}
	
	@Transactional
	public NotaEnvio mergeNotaEnvio(final NotaEnvio notaEnvio) {
		return notaEnvioRepository.merge(notaEnvio);
	}
	
	private NfeImpressaoDTO obterDadosNENECA(final NotaEnvio ne) {
		final NfeImpressaoDTO nfeImpressao = new NfeImpressaoDTO();

		//TODO: concluir
		final NotaEnvio notaEnvio = notaEnvioRepository.buscarPorId(ne.getNumero()); 

		if(notaEnvio == null) {
			return null;
		}
		
		NecaBuilder.carregarNEDadosPrincipais(nfeImpressao, notaEnvio);
		
		NecaBuilder.carregarNEDadosEmissor(nfeImpressao, notaEnvio);
		
		NecaBuilder.carregarNEDadosDestinatario(nfeImpressao, notaEnvio);
		
		NecaBuilder.carregarNEDadosItens(nfeImpressao, notaEnvio);
		
		return nfeImpressao;
		
	}
	
    /*
     * TODO : Sem a modelagem do conceito de duplicatas no sistema, refatorar
     * após modelagem de dados e EMS relativa a calculo de duplicatas.
     */
	protected URL obterDiretorioReports() {

		final URL urlDanfe = Thread.currentThread().getContextClassLoader().getResource("/reports/");

		return urlDanfe;
	}

	private byte[] gerarDocumentoIreportNE(final List<NfeImpressaoWrapper> list, boolean dispensaEmissaoNFe, final boolean indEmissaoDepec) throws Exception, URISyntaxException {

		final JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

		final URL diretorioReports = obterDiretorioReports();
		
		final TipoImpressaoNENECADANFE nenecaDANFE = distribuidorRepository.tipoImpressaoNENECADANFE();
		
		String path = diretorioReports.toURI().getPath();
		
		switch (nenecaDANFE) {
			case MODELO_1:
				
				path = path.concat("ne_modelo1_wrapper.jasper");
				break;
	
			case MODELO_2:
				path = path.concat("ne_modelo2_wrapper.jasper");
				break;	
				
			case DANFE:
				if(dispensaEmissaoNFe) {
					throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração do documento da NE. O Tipo de documento deve ser Nota de Envio.");
				}
				path = path.concat("danfeWrapper.jasper");
				break;	
			default:
				throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração do documento da NE");
		}

		Map<String, Object> parameters = new HashMap<String, Object>();

		InputStream inputStream = parametrosDistribuidorService.getLogotipoDistribuidor();
		
		if(inputStream == null) {
			inputStream = new ByteArrayInputStream(new byte[0]);
		}
		
		parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
		parameters.put("IND_EMISSAO_DEPEC", indEmissaoDepec);
		parameters.put("LOGO_DISTRIBUIDOR", inputStream);

		return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}
	
	private String getStringDataDeAte(final Intervalo<Date> intervalo) {
		
		String dataRecolhimento = null;
		if(intervalo.getDe().equals(intervalo.getAte())){
			dataRecolhimento =  DateUtil.formatarDataPTBR(intervalo.getDe());
		}else{
			dataRecolhimento = "De "+ DateUtil.formatarDataPTBR(intervalo.getDe()) + " até " + DateUtil.formatarDataPTBR(intervalo.getAte());
		}
		
		return dataRecolhimento;
	}
	
	
	@Override
	@Transactional
	public synchronized List<CotaExemplaresDTO> busca(final Intervalo<Integer> intervaloBox,
			final Intervalo<Integer> intervalorCota,
			final Intervalo<Date> intervaloDateMovimento,
			final List<Long> listIdFornecedor, final Long idTipoNotaFiscal, final Long idRoteiro, final Long idRota,
			final String sortname, final String sortorder, final Integer resultsPage, final Integer page, final SituacaoCadastro situacaoCadastro) {
		
		if (TRAVA_GERACAO_NFe.get("NFesSendoGeradas") != null) {

            throw new ValidacaoException(TipoMensagem.WARNING, "Notas de envio sendo geradas por outro usuário, tente novamente mais tarde.");
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
			if(distribuidor.getDataLimiteVigenciaRegimeEspecial() == null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Favor informar a data limite de vigincia do regime especial!" );
			}
		
			if(new Date().getTime() > distribuidor.getDataLimiteVigenciaRegimeEspecial().getTime()){
				throw new ValidacaoException(TipoMensagem.WARNING, "A data limite de vigincia do regime especial expirou!" );
			}
		}
		
		NaturezaOperacao naturezaOperacao = this.naturezaOperacaoRepository.obterNaturezaOperacao(filtro.getIdNaturezaOperacao());
		Map<String, ParametroSistema> parametrosSistema = parametroSistemaRepository.buscarParametroSistemaGeralMap();
		
		switch (naturezaOperacao.getTipoDestinatario()) {
		
			case COTA:
			case DISTRIBUIDOR:
				
				// obter as cotas que estão na tela pelo id das cotas
				List<Cota> cotas = this.notaFiscalRepository.obterConjuntoCotasNotafiscal(filtro);
				
				if(!distribuidor.isPossuiRegimeEspecialDispensaInterna()) {
					
					this.gerarNotasFiscaisCotas(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema, cotas);
					
				} else {
					
					this.gerarNotasConsolidadas(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema, cotas);
				}
				
				break;
				
			case FORNECEDOR:			
				this.gerarNotasFiscaisFornecedorEstoque(filtro, distribuidor, naturezaOperacao);
				break;
	
			default:
				throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de Destinatário não especificado");
				
		}
		
		if(notas == null || notas.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foram encontrados itens para gerar nota fiscal.");
		}
		
		for (NotaFiscal notaFiscal : notas) {
			notaFiscalRepository.adicionar(notaFiscal);
		}
		
		ParametroSistema ps = parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.NFE_INFORMACOES_TIPO_EMISSOR);
		if (ProcessoEmissao.EMISSAO_NFE_APLICATIVO_CONTRIBUINTE.equals(ProcessoEmissao.valueOf(ps.getValor()))
				&& TipoAtividade.PRESTADOR_FILIAL.equals(distribuidor.getTipoAtividade())) {
			
			this.ftfService.gerarFtf(notas);
			
			throw new ValidacaoException(TipoMensagem.WARNING, "FTF gerado");
		} else {
			
			this.notaFiscalService.exportarNotasFiscais(notas);
		}
		
		return notas;
	}

	private void gerarNotasConsolidadas(FiltroNFeDTO filtro, List<NotaFiscal> notas,
			Distribuidor distribuidor, NaturezaOperacao naturezaOperacao,
			Map<String, ParametroSistema> parametrosSistema, List<Cota> cotas) {
		boolean notasGeradas = false;
		List<Cota> cotasContribuinteEmitente = new ArrayList<Cota>();
		for(DistribuidorTipoNotaFiscal dtnf : distribuidor.getTiposNotaFiscalDistribuidor()) {
			if(dtnf.getNaturezaOperacao().contains(naturezaOperacao)) {
				if(dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.DESOBRIGA_EMISSAO)) {
					
					for (Cota cota : cotas) {
						
						if(cota.getParametrosCotaNotaFiscalEletronica() != null 
								&& (cota.getParametrosCotaNotaFiscalEletronica().isContribuinteICMS() != null 
								&& cota.getParametrosCotaNotaFiscalEletronica().isContribuinteICMS()) 
								|| (cota.getParametrosCotaNotaFiscalEletronica().isEmiteNotaFiscalEletronica() != null 
								&& cota.getParametrosCotaNotaFiscalEletronica().isEmiteNotaFiscalEletronica())) {
							
							cotasContribuinteEmitente.add(cota);
						}
					}
					
					if(cotasContribuinteEmitente.isEmpty()){
						throw new ValidacaoException(TipoMensagem.ERROR, "O regime especial dispensa emissao para essa natureza de operação");
					} else {
						this.gerarNotasFiscaisCotas(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema, cotasContribuinteEmitente);
						notasGeradas = true;
						break;
					}
				}
				
				if(dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.CONSOLIDA_EMISSAO_A_JORNALEIROS_DIVERSOS)) {			
					this.gerarNotaFiscalUnificada(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema);
					notasGeradas = true;
					break;
				} else {
					this.gerarNotasFiscaisCotas(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema, cotas);
					notasGeradas = true;
					break;
				}
				
			}
		}
		
		if(!notasGeradas) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Natureza de Operação não está configurada adequadamente para o Regime Especial.");
		}
	}
		
	private void validarFiltroNFe(final FiltroNFeDTO filtro) {
		
		if(filtro.getDataInicial() == null || filtro.getDataFinal() == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "As datas inicial e final não podem ser nulas.");
		} 
		
		if(filtro.getDataFinal().getTime() < filtro.getDataInicial().getTime()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "A data inicial não pode ser maior que a da final.");
		}
	}
	
	private void gerarNotasFiscaisFornecedorEstoque(FiltroNFeDTO filtro, Distribuidor distribuidor, NaturezaOperacao naturezaOperacao) {

		// obter as cotas que estão na tela pelo id das cotas
		List<EstoqueProduto> estoques = this.notaFiscalRepository.obterConjuntoFornecedorNotafiscal(filtro);
		
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
			List<EstoqueProduto> estoqueProdutos = this.notaFiscalRepository.obterEstoques(filtro);
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
	public void gerarNotasFiscaisFornecedor(final FiltroNFeDTO filtro,
			final List<NotaFiscal> notasFiscais, final Distribuidor distribuidor, final NaturezaOperacao naturezaOperacao, 
			final Map<String, ParametroSistema> parametrosSistema, final List<Fornecedor> fornecedores) {
		
		List<Transportador> transportadores = this.transportadorService.buscarTransportadores();
		
		final Map<String, TributoAliquota> tributoRegimeTributario = new HashMap<String, TributoAliquota>();
		
		for(final TributoAliquota tributo : distribuidor.getRegimeTributarioTributoAliquota()){
			tributoRegimeTributario.put(tributo.getNomeTributo(), tributo);
		}
		
		for (final Fornecedor fornecedor : fornecedores) {
			
			final NotaFiscal notaFiscal = new NotaFiscal();
			naturezaOperacao.setNotaFiscalNumeroNF(naturezaOperacao.getNotaFiscalNumeroNF() + 1);
			naturezaOperacaoRepository.merge(naturezaOperacao);
			
			final Usuario usuario = this.usuarioService.getUsuarioLogado();
			
			notaFiscal.setUsuario(usuario);
			
			NotaFiscalBuilder.popularDadosEmissor(notaFiscal, distribuidor, filtro);
			
			NotaFiscalTransportadorBuilder.montarTransportador(notaFiscal, naturezaOperacao, transportadores);
			
			NotaFiscalBuilder.montarHeaderNotaFiscal(notaFiscal, parametrosSistema);
			
			EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, fornecedor);
			
			NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
			
			// obter os movimentos de cada cota
			filtro.setIdCota(fornecedor.getId());
			final List<MovimentoFechamentoFiscal> movimentosFechamentosFiscais = this.notaFiscalRepository.obterMovimentosFechamentosFiscaisFornecedor(filtro);
			for (MovimentoFechamentoFiscal movimentoFechamentoFiscal : movimentosFechamentosFiscais) {
				ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoFechamentoFiscal, tributoRegimeTributario);
			}
			
			int parametro = Integer.valueOf(parametrosSistema.get("NFE_LIMITAR_QTDE_ITENS").getValor());
			
			if(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size() > parametro){
				List<List<DetalheNotaFiscal>> listaItens = new ArrayList<>();
				
				int tamanho = (int) notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size() / parametro;
				
				for (int i = parametro+1; i % parametro != 0 && notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size() > i; i++) {
					listaItens.subList(tamanho, notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size());
					listaItens.add(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal());
				}
			}
			
			//FIXME: Ajustar o valor do campo para valores parametrizados
			notaFiscal.getNotaFiscalInformacoes().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
			FaturaBuilder.montarFaturaNotaFiscal(notaFiscal, movimentosFechamentosFiscais);
			NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notaFiscal);
			notasFiscais.add(notaFiscal);
		}
	}
	
	// metodo responsavel pelo dados do distribuidor da nota
	private Distribuidor obterInformacaoDistribuidor(){
		return distribuidorRepository.obter();
	}
	

	@Override
	@Transactional
	public void gerarNotasFiscaisCotas(final FiltroNFeDTO filtro,
			final List<NotaFiscal> notasFiscais, final Distribuidor distribuidor, final NaturezaOperacao naturezaOperacao, 
			final Map<String, ParametroSistema> parametrosSistema, final List<Cota> cotas) {
		
		List<Transportador> transportadores = this.transportadorService.buscarTransportadores();
		
		final Map<String, TributoAliquota> tributoRegimeTributario = new HashMap<String, TributoAliquota>();
		
		for(final TributoAliquota tributo : distribuidor.getRegimeTributarioTributoAliquota()){
			tributoRegimeTributario.put(tributo.getNomeTributo(), tributo);
		}
		
		for (final Cota cota : cotas) {
			
			final NotaFiscal notaFiscal = new NotaFiscal();
			naturezaOperacao.setNotaFiscalNumeroNF(naturezaOperacao.getNotaFiscalNumeroNF() + 1);
			naturezaOperacaoRepository.merge(naturezaOperacao);
			
			final Usuario usuario = this.usuarioService.getUsuarioLogado();
			
			notaFiscal.setUsuario(usuario);
			
			NotaFiscalBuilder.popularDadosEmissor(notaFiscal, distribuidor, filtro);
			
			NotaFiscalTransportadorBuilder.montarTransportador(notaFiscal, naturezaOperacao, transportadores);
			
			NotaFiscalBuilder.montarHeaderNotaFiscal(notaFiscal, parametrosSistema);
			
			EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, cota);
			
			NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
			
			// obter os movimentos de cada cota
			filtro.setIdCota(cota.getId());
			final List<MovimentoEstoqueCota> movimentosEstoqueCota = this.notaFiscalRepository.obterMovimentosEstoqueCota(filtro);
			for (MovimentoEstoqueCota movimentoEstoqueCota : movimentosEstoqueCota) {
				ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoEstoqueCota, tributoRegimeTributario);
			}
			
			int parametro = Integer.valueOf(parametrosSistema.get("NFE_LIMITAR_QTDE_ITENS").getValor());
			
			if(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size() > parametro){
				List<List<DetalheNotaFiscal>> listaItens = new ArrayList<>();
				
				int tamanho = (int) notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size() / parametro;
				
				for (int i = parametro+1; i % parametro != 0 && notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size() > i; i++) {
					listaItens.subList(tamanho, notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size());
					listaItens.add(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal());
				}
			}
			
			//FIXME: Ajustar o valor do campo para valores parametrizados
			notaFiscal.getNotaFiscalInformacoes().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
			FaturaBuilder.montarFaturaNotaFiscal(notaFiscal, movimentosEstoqueCota);
			NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notaFiscal);
			notasFiscais.add(notaFiscal);
		}
	}

	private void gerarNotaFiscalUnificada(FiltroNFeDTO filtro, List<NotaFiscal> notasFiscais, Distribuidor distribuidor, NaturezaOperacao naturezaOperacao, Map<String, ParametroSistema> parametrosSistema) {
		
		// obter as cotas que estão na tela pelo id das cotas
		final NotaFiscal notaFiscal = new NotaFiscal();
		final List<Transportador> transportadores = this.transportadorService.buscarTransportadores();
		naturezaOperacao.setNotaFiscalNumeroNF(naturezaOperacao.getNotaFiscalNumeroNF() + 1);
		naturezaOperacaoRepository.merge(naturezaOperacao);
		
		List<Cota> cotas = this.notaFiscalRepository.obterConjuntoCotasNotafiscal(filtro);
		notaFiscal.setUsuario(usuarioService.getUsuarioLogado());
		
		Map<String, TributoAliquota> tributoAliquota = new HashMap<String, TributoAliquota>();
		
		for(final TributoAliquota tributo : distribuidor.getRegimeTributarioTributoAliquota()){
			tributoAliquota.put(tributo.getNomeTributo(), tributo);
		}
		
		
		NotaFiscalBuilder.popularDadosEmissor(notaFiscal, distribuidor, filtro);
		NotaFiscalBuilder.popularDadosTransportadora(notaFiscal, distribuidor, filtro);
		EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, distribuidor);
		NotaFiscalBuilder.montarHeaderNotaFiscal(notaFiscal, distribuidor, parametrosSistema);
		NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
		for (final Cota cota : cotas) {
			
			// FIX arrumar endereco
			// obter os movimentos de cada cota
			filtro.setIdCota(cota.getId());
			final List<MovimentoEstoqueCota> movimentosEstoqueCota = this.notaFiscalRepository.obterMovimentosEstoqueCota(filtro);
			for (final MovimentoEstoqueCota movimentoEstoqueCota : movimentosEstoqueCota) {
				ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoEstoqueCota, tributoAliquota);
			}
			FaturaBuilder.montarFaturaNotaFiscal(notaFiscal, movimentosEstoqueCota);
			NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notaFiscal);
		}
		
		notaFiscal.getNotaFiscalInformacoes().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
		
		//FIXME: Ajustar o transportador Principal
		if(transportadores == null || transportadores.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Não foi .");
		} else {			
			NotaFiscalTransportadorBuilder.montarTransportador(notaFiscal, naturezaOperacao, transportadores);
		}
		
		notasFiscais.add(notaFiscal);
	}
	
	@Override
	@Transactional
	public List<CotaExemplaresDTO> consultaCotaExemplaresSumarizados(final FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao) {
		
		Distribuidor distribuidor = this.obterInformacaoDistribuidor();
		
		if(!distribuidor.isPossuiRegimeEspecialDispensaInterna()) {
			
			return notaFiscalService.consultaCotaExemplaresSumarizados(filtro, naturezaOperacao);
		} else {
			
			return this.listaRegimeEspecial(filtro, naturezaOperacao, distribuidor);	
		}
	}

	private List<CotaExemplaresDTO> listaRegimeEspecial(final FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao, Distribuidor distribuidor) {
		List<CotaExemplaresDTO> cotasContribuinteEmitente = new ArrayList<CotaExemplaresDTO>();
		
		List<CotaExemplaresDTO> cotas =  notaFiscalService.consultaCotaExemplaresSumarizados(filtro, null);
		
		for(DistribuidorTipoNotaFiscal dtnf : distribuidor.getTiposNotaFiscalDistribuidor()) {
			if(dtnf.getNaturezaOperacao().contains(naturezaOperacao)) {
				if(dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.DESOBRIGA_EMISSAO)) {
					for (CotaExemplaresDTO cota : cotas) {
						if((cota.isContribuinteICMS() != null && cota.isContribuinteICMS()) || (cota.isEmiteNotaFiscalEletronica() != null && cota.isEmiteNotaFiscalEletronica() )){
							cotasContribuinteEmitente.add(cota);
						}
					}
				}
			}
		}
		return cotasContribuinteEmitente;
	}

	@Override
	@Transactional
	public Long consultaCotaExemplareSumarizadoQtd(final FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao) {
		return notaFiscalService.consultaCotaExemplaresSumarizadoQtd(filtro, naturezaOperacao);
	}

	@Override
	@Transactional(readOnly=true)
	public List<FornecedorExemplaresDTO> consultaFornecedorExemplarSumarizado(final FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao) {
		return notaFiscalService.consultaFornecedorExemplaresSumarizados(filtro, naturezaOperacao);
	}

	@Override
	@Transactional
	public Long consultaFornecedorExemplaresSumarizadosQtd(final FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao) {
		return notaFiscalService.consultaFornecedorExemplaresSumarizadosQtd(filtro, naturezaOperacao);
	}
	
	@Override
	@Transactional
	public void gerarNotasFiscaisCotasEncalhe(final List<NotaFiscal> notasFiscais, final Distribuidor distribuidor, final NaturezaOperacao naturezaOperacao, 
			final Map<String, ParametroSistema> parametrosSistema, final List<Cota> cotas) {
		
		List<Transportador> transportadores = this.transportadorService.buscarTransportadores();
		
		final Map<String, TributoAliquota> tributoRegimeTributario = new HashMap<String, TributoAliquota>();
		
		for(final TributoAliquota tributo : distribuidor.getRegimeTributarioTributoAliquota()){
			tributoRegimeTributario.put(tributo.getNomeTributo(), tributo);
		}
		
		for (final Cota cota : cotas) {
			
			final NotaFiscal notaFiscal = new NotaFiscal();
			naturezaOperacao.setNotaFiscalNumeroNF(naturezaOperacao.getNotaFiscalNumeroNF() + 1);
			naturezaOperacaoRepository.merge(naturezaOperacao);
			
			final Usuario usuario = this.usuarioService.getUsuarioLogado();
			
			notaFiscal.setUsuario(usuario);
			
			NotaFiscalBuilder.popularDadosDistribuidor(notaFiscal, distribuidor);
			
			NotaFiscalTransportadorBuilder.montarTransportador(notaFiscal, naturezaOperacao, transportadores);
			
			NotaFiscalBuilder.montarHeaderNotaFiscal(notaFiscal, parametrosSistema);
			
			EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, cota);
			
			NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
			
			// obter os movimentos de cada cota
			FiltroNFeDTO filtro = new FiltroNFeDTO();
			filtro.setIdCota(cota.getId());
			final List<MovimentoEstoqueCota> movimentosEstoqueCota = this.notaFiscalRepository.obterMovimentosEstoqueCota(filtro);
			for (MovimentoEstoqueCota movimentoEstoqueCota : movimentosEstoqueCota) {
				ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoEstoqueCota, tributoRegimeTributario);
			}
			
			//FIXME: Ajustar o valor do campo para valores parametrizados
			notaFiscal.getNotaFiscalInformacoes().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
			FaturaBuilder.montarFaturaNotaFiscal(notaFiscal, movimentosEstoqueCota);
			NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notaFiscal);
			notasFiscais.add(notaFiscal);
		}
	}
}
