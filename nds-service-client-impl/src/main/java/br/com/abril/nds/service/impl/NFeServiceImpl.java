package br.com.abril.nds.service.impl;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import br.com.abril.nds.dto.NotaFiscalDTO;
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
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoFiscal;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscal;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.NotaFiscalTipoEmissaoRegimeEspecial;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.TipoEmitente;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.Identificacao.ProcessoEmissao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.TipoAmbiente;
import br.com.abril.nds.model.fiscal.nota.InfAdicWrapper;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalReferenciada;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalReferenciadaNFE;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalReferenciadaWrapper;
import br.com.abril.nds.model.fiscal.nota.pk.NotaFiscalReferenciadaPK;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
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
				
				if (filtro.getDataInicial() == null || filtro.getDataFinal() == null) {
					throw new ValidacaoException(TipoMensagem.WARNING, "O intervalo de datas não pode ser nula!");
				}
				
				List<TipoMovimento> itensMovimentosFiscais = notaFiscalService.obterMovimentosFiscaisNaturezaOperacao(naturezaOperacao);
				
				List<Cota> cotas = null;
				if(itensMovimentosFiscais.size() > 0) {
					
					if(naturezaOperacao.isNotaFiscalDevolucaoSimbolica()) {
						
						filtro.setNotaFiscalDevolucaoSimbolica(true);
					} else if(naturezaOperacao.isNotaFiscalVendaConsignado()) {
						
						filtro.setNotaFiscalVendaConsignado(true);
					}
					
					cotas = this.notaFiscalRepository.obterConjuntoCotasNotafiscalMFF(filtro);
				} else {
				
					cotas = this.notaFiscalRepository.obterConjuntoCotasNotafiscalMEC(filtro);
				}
				
				if (cotas == null || cotas.isEmpty()) {
					throw new ValidacaoException(TipoMensagem.WARNING, "Não existem itens a serem gerados para o filtro.");
				}
				
				if(!distribuidor.isPossuiRegimeEspecialDispensaInterna()) {
					
					this.gerarNotasFiscaisCotas(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema, cotas);
					
				} else {
					
					this.gerarNotasConsolidadas(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema, cotas);
				}
				
				break;
				
			case FORNECEDOR:		
				
				List<Fornecedor> fornecedores = this.notaFiscalRepository.obterConjuntoFornecedoresNotafiscal(filtro);
				
				if(naturezaOperacao.getTipoMovimento() != null) {
					
					if(naturezaOperacao.getTipoMovimento().get(0) != null) {
						
						if(naturezaOperacao.getTipoMovimento().get(0) instanceof TipoMovimentoEstoque) {
							
							this.gerarNotasFiscaisFornecedorMovimentoEstoque(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema, fornecedores);
							
						} else if(naturezaOperacao.getTipoMovimento().get(0) instanceof TipoMovimentoFiscal) {
							
							this.gerarNotasFiscaisFornecedor(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema, fornecedores);
							
						}
						
					} else {
						
						throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de Movimento não suportado para geração de NF-e.");
					}
					
				} else {
				
					this.gerarNotasFiscaisFornecedorEstoque(filtro, distribuidor, naturezaOperacao);
				}
				break;
	
			default:
				throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de Destinatário não especificado");
				
		}
		
		if(notas == null || notas.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foram encontrados itens para gerar nota fiscal.");
		}
		
		try {
			
			for (NotaFiscal notaFiscal : notas) {
				notaFiscalRepository.adicionar(notaFiscal);
			}
		} catch(Exception e) {
			
			LOGGER.error("Erro ao salvar NF-e.", e);
			throw e;
		}
		
		ParametroSistema ps = parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.NFE_INFORMACOES_TIPO_EMISSOR);
		
		if (ProcessoEmissao.EMISSAO_NFE_APLICATIVO_CONTRIBUINTE.equals(ProcessoEmissao.valueOf(ps.getValor())) && TipoAtividade.PRESTADOR_FILIAL.equals(distribuidor.getTipoAtividade())) {
			
			this.ftfService.gerarFtf(notas);
//			throw new ValidacaoException(TipoMensagem.ERROR, "Não gravar!!!!");
		} else {
			
			this.notaFiscalService.exportarNotasFiscais(notas);
		}
		
		if(parametrosSistema.get("NFE_INFORMACOES_AMBIENTE").getValor().equals(TipoAmbiente.HOMOLOGACAO)) {			
			throw new ValidacaoException(TipoMensagem.ERROR, "Não gravar!!!!");
		}
		
		return notas;
	}

	private void gerarNotasFiscaisFornecedorMovimentoEstoque(FiltroNFeDTO filtro, List<NotaFiscal> notasFiscais, 
			Distribuidor distribuidor, 
			NaturezaOperacao naturezaOperacao, 
			Map<String, ParametroSistema> parametrosSistema, 
			List<Fornecedor> fornecedores) {
		
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
			
			NotaFiscalBuilder.popularDadosEmissor(notaFiscal, distribuidor);
			
			NotaFiscalTransportadorBuilder.montarTransportador(notaFiscal, naturezaOperacao, transportadores);
			
			NotaFiscalBuilder.montarHeaderNotaFiscal(notaFiscal, parametrosSistema);
			
			EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, fornecedor);
			
			NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
			
			montaChaveAcesso(notaFiscal);
			
			notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setDigitoVerificadorChaveAcesso(Long.valueOf(notaFiscal.getNotaFiscalInformacoes().getIdNFe().substring(46, 47)));
			
			// obter os movimentos de cada cota
			filtro.setIdFornecedor(fornecedor.getId());
			List<MovimentoEstoque> movimentosEstoque = this.notaFiscalRepository.obterMovimentosEstoqueFornecedor(filtro);
			for (MovimentoEstoque movimentoEstoque : movimentosEstoque) {
				ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoEstoque, tributoRegimeTributario);
			}
			
			List<NotaFiscal> notasFiscaisSubdivididas = subdividirNotasFiscaisPorLimiteItens(parametrosSistema, notaFiscal, naturezaOperacao, distribuidor, transportadores);
			
			if(notasFiscaisSubdivididas != null && !notasFiscaisSubdivididas.isEmpty()) {
				
				for(NotaFiscal notasFiscalSubdividida : notasFiscaisSubdivididas) {
					
					notasFiscalSubdividida.getNotaFiscalInformacoes().getInfAdicWrapper().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
					FaturaBuilder.montarFaturaNotaFiscal(notasFiscalSubdividida);
					NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notasFiscalSubdividida);
					notasFiscais.add(notasFiscalSubdividida);
				}
			} else {
			
				//FIXME: Ajustar o valor do campo para valores parametrizados
				if(notaFiscal.getNotaFiscalInformacoes().getInfAdicWrapper() == null) {
					InfAdicWrapper infAdicWrapper = new InfAdicWrapper();
					notaFiscal.getNotaFiscalInformacoes().setInfAdicWrapper(infAdicWrapper);
				}
				
				notaFiscal.getNotaFiscalInformacoes().getInfAdicWrapper().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
				FaturaBuilder.montarFaturaNotaFiscal(notaFiscal);
				NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notaFiscal);
				notasFiscais.add(notaFiscal);
			}
		}	
		
	}

	private void gerarNotasConsolidadas(FiltroNFeDTO filtro, List<NotaFiscal> notas,
			Distribuidor distribuidor, NaturezaOperacao naturezaOperacao,
			Map<String, ParametroSistema> parametrosSistema, List<Cota> cotas) {
		
		List<CotaExemplaresDTO> cotasDTO = this.listaRegimeEspecial(filtro, naturezaOperacao, distribuidor);
		
		Map<Long, CotaExemplaresDTO> cotasDTOMap = new HashMap<>();		
		for(CotaExemplaresDTO cotaDTO : cotasDTO) {
			cotasDTOMap.put(cotaDTO.getIdCota(), cotaDTO);
		}
		
		boolean notasGeradas = false;
		
		List<Cota> cotasContribuintesExigemNFe = new ArrayList<Cota>();		
		List<Cota> cotasNaoContribuintesNaoExigemNFe = new ArrayList<Cota>();
		
		try {
			
			for(DistribuidorTipoNotaFiscal dtnf : distribuidor.getTiposNotaFiscalDistribuidor()) {
				
				if(dtnf.getNaturezaOperacao().contains(naturezaOperacao)) {
					
					for (Cota cota : cotas) {
						
						if(cota.getParametrosCotaNotaFiscalEletronica() != null 
								&& (cota.getParametrosCotaNotaFiscalEletronica().isContribuinteICMS()  
										|| cota.getParametrosCotaNotaFiscalEletronica().isExigeNotaFiscalEletronica())
								&& (cotasDTOMap != null && cotasDTOMap.get(cota.getId()) != null && cotasDTOMap.get(cota.getId()).isContribuinteICMSExigeNFe())) {
							
							cotasContribuintesExigemNFe.add(cota);
						} else {
							
							cotasNaoContribuintesNaoExigemNFe.add(cota);
						}
					}
					
					if(dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.DESOBRIGA_EMISSAO)) {
						
						if(cotasContribuintesExigemNFe.isEmpty()) {
							
							throw new ValidacaoException(TipoMensagem.ERROR, "O regime especial dispensa emissao para essa natureza de operação");
						} else {
							
							this.gerarNotasFiscaisCotas(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema, cotasContribuintesExigemNFe);
							notasGeradas = true;
							break;
						}
					} else if(dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.CONSOLIDA_EMISSAO_A_JORNALEIROS_DIVERSOS)
							&& filtro.getNotaFiscalTipoEmissao() != null
								&& !filtro.getNotaFiscalTipoEmissao().equals(NotaFiscalTipoEmissaoRegimeEspecial.COTA_CONTRIBUINTE_EXIGE_NFE)) {		
						
						this.gerarNotaFiscalUnificada(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema, cotasNaoContribuintesNaoExigemNFe);
						notasGeradas = true;
						if(filtro.getNotaFiscalTipoEmissao() != null
								&& filtro.getNotaFiscalTipoEmissao().equals(NotaFiscalTipoEmissaoRegimeEspecial.CONSOLIDADO)) {
							break;
						}
					} else if(dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.CONSOLIDA_EMISSAO_POR_DESTINATARIO)
							&& filtro.getNotaFiscalTipoEmissao() != null
								&& !filtro.getNotaFiscalTipoEmissao().equals(NotaFiscalTipoEmissaoRegimeEspecial.COTA_CONTRIBUINTE_EXIGE_NFE)) {
						
						this.gerarNotasFiscaisCotas(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema, cotasNaoContribuintesNaoExigemNFe);
						notasGeradas = true;
						if(filtro.getNotaFiscalTipoEmissao() != null
								&& filtro.getNotaFiscalTipoEmissao().equals(NotaFiscalTipoEmissaoRegimeEspecial.CONSOLIDADO)) {
							break;
						}
					}
					
					this.gerarNotasFiscaisCotas(filtro, notas, distribuidor, naturezaOperacao, parametrosSistema, cotasContribuintesExigemNFe);
					if(notas != null && !notas.isEmpty() ) {
						notasGeradas = true;
					}
					
				}
			}
		} catch (Exception e) {
			
			LOGGER.error("Erro ao Gerar NF-e", e);
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao Gerar NF-e");
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
			
			if(notaFiscal.getNotaFiscalInformacoes().getInfAdicWrapper() == null) {
				InfAdicWrapper infAdicWrapper = new InfAdicWrapper();
				notaFiscal.getNotaFiscalInformacoes().setInfAdicWrapper(infAdicWrapper);
			}
			
			notaFiscal.getNotaFiscalInformacoes().getInfAdicWrapper().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
		}	
	}

	@Override
	@Transactional
	public void gerarNotasFiscaisFornecedor(final FiltroNFeDTO filtro,
			final List<NotaFiscal> notasFiscais, 
			final Distribuidor distribuidor, 
			final NaturezaOperacao naturezaOperacao, 
			final Map<String, ParametroSistema> parametrosSistema, 
			final List<Fornecedor> fornecedores) {
		
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
			
			NotaFiscalBuilder.popularDadosEmissor(notaFiscal, distribuidor);
			
			NotaFiscalTransportadorBuilder.montarTransportador(notaFiscal, naturezaOperacao, transportadores);
			
			NotaFiscalBuilder.montarHeaderNotaFiscal(notaFiscal, parametrosSistema);
			
			EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, fornecedor);
			
			NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
			
			montaChaveAcesso(notaFiscal);
			
			// obter os movimentos de fechamentos fiscais
			filtro.setIdCota(fornecedor.getId());
			
			
			final List<MovimentoFechamentoFiscal> movimentosFechamentosFiscais = this.notaFiscalRepository.obterMovimentosFechamentosFiscaisFornecedor(filtro);
			
			for (MovimentoFechamentoFiscal movimentoFechamentoFiscal : movimentosFechamentosFiscais) {
				ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoFechamentoFiscal, tributoRegimeTributario);
			}
			
			List<NotaFiscal> notasFiscaisSubdivididas = subdividirNotasFiscaisPorLimiteItens(parametrosSistema, notaFiscal, naturezaOperacao, distribuidor, transportadores);
			
			if(notasFiscaisSubdivididas != null && !notasFiscaisSubdivididas.isEmpty()) {
				
				for(NotaFiscal notasFiscalSubdividida : notasFiscaisSubdivididas) {
					
					notasFiscalSubdividida.getNotaFiscalInformacoes().getInfAdicWrapper().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
					FaturaBuilder.montarFaturaNotaFiscal(notasFiscalSubdividida);
					NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notasFiscalSubdividida);
					notasFiscais.add(notasFiscalSubdividida);
					
				}
				
			} else {
				
				if(notaFiscal.getNotaFiscalInformacoes().getInfAdicWrapper() == null) {
					InfAdicWrapper infAdicWrapper = new InfAdicWrapper();
					notaFiscal.getNotaFiscalInformacoes().setInfAdicWrapper(infAdicWrapper);
				}
				
				notaFiscal.getNotaFiscalInformacoes().getInfAdicWrapper().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
				FaturaBuilder.montarFaturaNotaFiscal(notaFiscal);
				NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notaFiscal);
				notasFiscais.add(notaFiscal);
				
			}
			
		}
		
		if(naturezaOperacao.isGerarNotasReferenciadas()) {
			
			this.gerarNotaFiscalReferenciada(notasFiscais, naturezaOperacao.getTipoDestinatario());
		}
		
	}

	private List<NotaFiscal> subdividirNotasFiscaisPorLimiteItens(final Map<String, ParametroSistema> parametrosSistema, 
			final NotaFiscal notaFiscal, 
			NaturezaOperacao naturezaOperacao, 
			Distribuidor distribuidor,
			List<Transportador> transportadores) {
		
		int limiteQtdItens = Integer.valueOf(parametrosSistema.get("NFE_LIMITAR_QTDE_ITENS").getValor());
		
		if(limiteQtdItens == 0) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Parâmetro de Limite de Itens por NF-e configurado incorretamente.");
		}
		
		List<NotaFiscal> notasFiscaisSubdivididas = new ArrayList<NotaFiscal>();
		
		if(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size() > limiteQtdItens) {
			
			int qtdNotasSubdivididas = (int) notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size() / limiteQtdItens;
			
			List<List<DetalheNotaFiscal>> listaSubdivididaItensNotas = new ArrayList<>();
			
			if(qtdNotasSubdivididas == 1) {
				
				listaSubdivididaItensNotas.add(new ArrayList<DetalheNotaFiscal>(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().subList(limiteQtdItens + 1, notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size())));
				
				List<DetalheNotaFiscal> itensForaPrimeiraNota = notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().subList(limiteQtdItens + 1, notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size());
				
				notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().removeAll(itensForaPrimeiraNota);
				
			} else if(qtdNotasSubdivididas > 1) {
				
				int lastIndex = 0;
				for(int i=1; i < qtdNotasSubdivididas; i++) {
					lastIndex = i;
					listaSubdivididaItensNotas.add(new ArrayList<DetalheNotaFiscal>(
							notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().subList(
									limiteQtdItens * i, (limiteQtdItens * (i + 1)))));
				}
				
				listaSubdivididaItensNotas.add(new ArrayList<DetalheNotaFiscal>(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().subList(qtdNotasSubdivididas * lastIndex, notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size())));
				
				List<DetalheNotaFiscal> itensPrimeiraNota = notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().subList(0, limiteQtdItens);
				
				notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().retainAll(itensPrimeiraNota);
				
			}
			
			notasFiscaisSubdivididas.add(notaFiscal);
			
			for(List<DetalheNotaFiscal> detalhesNotaFiscal : listaSubdivididaItensNotas) {
				
				final NotaFiscal notaFiscalSubdividida = new NotaFiscal();
				naturezaOperacao.setNotaFiscalNumeroNF(naturezaOperacao.getNotaFiscalNumeroNF() + 1);
				naturezaOperacaoRepository.merge(naturezaOperacao);
				
				final Usuario usuario = this.usuarioService.getUsuarioLogado();
				
				notaFiscal.setUsuario(usuario);
				
				NotaFiscalBuilder.popularDadosEmissor(notaFiscalSubdividida, distribuidor);
				
				NotaFiscalTransportadorBuilder.montarTransportador(notaFiscalSubdividida, naturezaOperacao, transportadores);
				
				NotaFiscalBuilder.montarHeaderNotaFiscal(notaFiscalSubdividida, parametrosSistema);
				
				notaFiscalSubdividida.getNotaFiscalInformacoes().setDetalhesNotaFiscal(detalhesNotaFiscal);
				
				notasFiscaisSubdivididas.add(notaFiscalSubdividida);
				
			}
			
			return notasFiscaisSubdivididas;
		}
		
		return null;
	}
	
	// metodo responsavel pelo dados do distribuidor da nota
	private Distribuidor obterInformacaoDistribuidor(){
		return distribuidorRepository.obter();
	}
	

	@Override
	@Transactional
	public void gerarNotasFiscaisCotas(final FiltroNFeDTO filtro,
			final List<NotaFiscal> notasFiscais, 
			final Distribuidor distribuidor, 
			final NaturezaOperacao naturezaOperacao, 
			final Map<String, ParametroSistema> parametrosSistema, 
			final List<Cota> cotas) {
		
		List<Transportador> transportadores = this.transportadorService.buscarTransportadores();
		
		final Map<String, TributoAliquota> tributoRegimeTributario = new HashMap<String, TributoAliquota>();
		
		for(final TributoAliquota tributo : distribuidor.getRegimeTributarioTributoAliquota()){
			tributoRegimeTributario.put(tributo.getNomeTributo(), tributo);
		}
		
		for (final Cota cota : cotas) {
			
			NotaFiscal notaFiscal = new NotaFiscal();
			naturezaOperacao.setNotaFiscalNumeroNF(naturezaOperacao.getNotaFiscalNumeroNF() + 1);
			naturezaOperacaoRepository.merge(naturezaOperacao);
			
			notaFiscal.setUsuario(usuarioService.getUsuarioLogado());
			
			Map<String, TributoAliquota> tributoAliquota = new HashMap<String, TributoAliquota>();
			
			for(final TributoAliquota tributo : distribuidor.getRegimeTributarioTributoAliquota()){
				tributoAliquota.put(tributo.getNomeTributo(), tributo);
			}
			
			final Usuario usuario = this.usuarioService.getUsuarioLogado();
			
			notaFiscal.setUsuario(usuario);
			
			NotaFiscalBuilder.popularDadosEmissor(notaFiscal, distribuidor);
			
			NotaFiscalTransportadorBuilder.montarTransportador(notaFiscal, naturezaOperacao, transportadores);
			
			NotaFiscalBuilder.montarHeaderNotaFiscal(notaFiscal, parametrosSistema);
			
			EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, cota);
			
			NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
			
			montaChaveAcesso(notaFiscal);
			
			//notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setDigitoVerificadorChaveAcesso(6L);
			notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setDigitoVerificadorChaveAcesso(Long.valueOf(notaFiscal.getNotaFiscalInformacoes().getIdNFe().substring(46, 47)));
			
			if(notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica() == null) {
				notaFiscal.getNotaFiscalInformacoes().setInformacaoEletronica(new InformacaoEletronica());
			}
			
			notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica().setChaveAcesso(notaFiscal.getNotaFiscalInformacoes().getIdNFe().substring(3, 47));
			
			// obter os movimentos da cota
			final List<MovimentoFechamentoFiscal> movimentosFechamentoFiscal = new ArrayList<>();
			final List<MovimentoEstoqueCota> movimentosEstoqueCota = new ArrayList<>();
			
			List<TipoMovimento> itensMovimentosFiscais = notaFiscalService.obterMovimentosFiscaisNaturezaOperacao(naturezaOperacao);
				
			filtro.setIdCota(cota.getId());
			if(itensMovimentosFiscais.size() > 0) {
				
				movimentosFechamentoFiscal.addAll(this.notaFiscalRepository.obterMovimentosFechamentosFiscaisCota(filtro));
			} else {
				
				movimentosEstoqueCota.addAll(this.notaFiscalRepository.obterMovimentosEstoqueCota(filtro));
			}
			
			if(itensMovimentosFiscais.size() > 0) {
				
				for (final MovimentoFechamentoFiscal movimentoFechamentoFiscal : movimentosFechamentoFiscal) {
					
					ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoFechamentoFiscal, tributoAliquota);
				}
			} else {
				
				for (final MovimentoEstoqueCota movimentoEstoqueCota : movimentosEstoqueCota) {
					
					ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoEstoqueCota, tributoAliquota);
				}
			}			
			
			List<NotaFiscal> notasFiscaisSubdivididas = subdividirNotasFiscaisPorLimiteItens(parametrosSistema, notaFiscal, naturezaOperacao, distribuidor, transportadores);
			
			if(notasFiscaisSubdivididas != null && !notasFiscaisSubdivididas.isEmpty()) {
				
				for(NotaFiscal notasFiscalSubdividida : notasFiscaisSubdivididas) {
					
					notasFiscalSubdividida.getNotaFiscalInformacoes().getInfAdicWrapper().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
					FaturaBuilder.montarFaturaNotaFiscal(notasFiscalSubdividida);
					NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notasFiscalSubdividida);
					notasFiscais.add(notasFiscalSubdividida);
				}
			} else {
			
				if(notaFiscal.getNotaFiscalInformacoes().getInfAdicWrapper() == null) {
					InfAdicWrapper infAdicWrapper = new InfAdicWrapper();
					notaFiscal.getNotaFiscalInformacoes().setInfAdicWrapper(infAdicWrapper);
				}
				
				notaFiscal.getNotaFiscalInformacoes().getInfAdicWrapper().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
				
				FaturaBuilder.montarFaturaNotaFiscal(notaFiscal);
				NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notaFiscal);
				notasFiscais.add(notaFiscal);
			}
		}
		
		if(naturezaOperacao.isGerarNotasReferenciadas()) {
			
			this.gerarNotaFiscalReferenciada(notasFiscais, naturezaOperacao.getTipoDestinatario());
		}
		
	}

	private void gerarNotaFiscalUnificada(FiltroNFeDTO filtro, List<NotaFiscal> notasFiscais, Distribuidor distribuidor, 
			NaturezaOperacao naturezaOperacao, Map<String, ParametroSistema> parametrosSistema, List<Cota> cotas) {
		
		// obter as cotas que estão na tela pelo id das cotas
		final NotaFiscal notaFiscal = new NotaFiscal();
		final List<Transportador> transportadores = this.transportadorService.buscarTransportadores();
		naturezaOperacao.setNotaFiscalNumeroNF(naturezaOperacao.getNotaFiscalNumeroNF() + 1);
		naturezaOperacaoRepository.merge(naturezaOperacao);
		
		notaFiscal.setUsuario(usuarioService.getUsuarioLogado());
		
		Map<String, TributoAliquota> tributoAliquota = new HashMap<String, TributoAliquota>();
		
		for(final TributoAliquota tributo : distribuidor.getRegimeTributarioTributoAliquota()){
			tributoAliquota.put(tributo.getNomeTributo(), tributo);
		}
		
		NotaFiscalBuilder.popularDadosEmissor(notaFiscal, distribuidor);
		
		NotaFiscalBuilder.popularDadosTransportadora(notaFiscal, distribuidor);
		
		EmitenteDestinatarioBuilder.montarEnderecoEmitenteDestinatario(notaFiscal, distribuidor);
		
		NotaFiscalTransportadorBuilder.montarTransportador(notaFiscal, naturezaOperacao, transportadores);
		
		NotaFiscalBuilder.montarHeaderNotaFiscal(notaFiscal, distribuidor, parametrosSistema);
		
		NaturezaOperacaoBuilder.montarNaturezaOperacao(notaFiscal, naturezaOperacao);
		
		final List<MovimentoFechamentoFiscal> movimentosFechamentoFiscal = new ArrayList<>();
		final List<MovimentoEstoqueCota> movimentosEstoqueCota = new ArrayList<>();
		
		List<TipoMovimento> itensMovimentosFiscais = notaFiscalService.obterMovimentosFiscaisNaturezaOperacao(naturezaOperacao);
		for (final Cota cota : cotas) {
			
			filtro.setIdCota(cota.getId());
			if(itensMovimentosFiscais.size() > 0) {
				
				movimentosFechamentoFiscal.addAll(this.notaFiscalRepository.obterMovimentosFechamentosFiscaisCota(filtro));
			} else {
				
				movimentosEstoqueCota.addAll(this.notaFiscalRepository.obterMovimentosEstoqueCota(filtro));
			}
		}
		
		if(itensMovimentosFiscais.size() > 0) {
			
			for (final MovimentoFechamentoFiscal movimentoFechamentoFiscal : movimentosFechamentoFiscal) {
				
				ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoFechamentoFiscal, tributoAliquota);
			}
		} else {
			
			for (final MovimentoEstoqueCota movimentoEstoqueCota : movimentosEstoqueCota) {
				
				ItemNotaFiscalBuilder.montaItemNotaFiscal(notaFiscal, movimentoEstoqueCota, tributoAliquota);
			}
		}
		
		List<NotaFiscal> notasFiscaisSubdivididas = subdividirNotasFiscaisPorLimiteItens(parametrosSistema, notaFiscal, naturezaOperacao, distribuidor, transportadores);
		
		if(notasFiscaisSubdivididas != null && !notasFiscaisSubdivididas.isEmpty()) {
			
			for(NotaFiscal notasFiscalSubdividida : notasFiscaisSubdivididas) {
				
				notasFiscalSubdividida.getNotaFiscalInformacoes().getInfAdicWrapper().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
				FaturaBuilder.montarFaturaNotaFiscal(notasFiscalSubdividida);
				NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notasFiscalSubdividida);
				notasFiscais.add(notasFiscalSubdividida);
			}
		} else {
			
			if(notaFiscal.getNotaFiscalInformacoes().getInfAdicWrapper() == null) {
				InfAdicWrapper infAdicWrapper = new InfAdicWrapper();
				notaFiscal.getNotaFiscalInformacoes().setInfAdicWrapper(infAdicWrapper);
			}
			
			notaFiscal.getNotaFiscalInformacoes().getInfAdicWrapper().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
			FaturaBuilder.montarFaturaNotaFiscal(notaFiscal);
			NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notaFiscal);
			notasFiscais.add(notaFiscal);
		}
		
		if(naturezaOperacao.isGerarNotasReferenciadas()) {
			
			this.gerarNotaFiscalReferenciada(notasFiscais, naturezaOperacao.getTipoDestinatario());
		}
		
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
		
		List<CotaExemplaresDTO> cotas = notaFiscalService.consultaCotaExemplaresSumarizados(filtro, naturezaOperacao);
		
		if(cotas == null || cotas.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foram encontrados itens para gerar nota fiscal.");
		}
		
		for(DistribuidorTipoNotaFiscal dtnf : distribuidor.getTiposNotaFiscalDistribuidor()) {
			if(dtnf.getNaturezaOperacao().contains(naturezaOperacao)) {
				
				if(dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.DESOBRIGA_EMISSAO)
						|| (filtro.getNotaFiscalTipoEmissao() != null 
							&& filtro.getNotaFiscalTipoEmissao().equals(NotaFiscalTipoEmissaoRegimeEspecial.COTA_CONTRIBUINTE_EXIGE_NFE))) {
					for (CotaExemplaresDTO cota : cotas) {
						if((naturezaOperacao.isGerarCotaContribuinteICMS()
								&& ((cota.isContribuinteICMS() != null && cota.isContribuinteICMS()))) && cota.isContribuinteICMSExigeNFe()) {
							cotasContribuinteEmitente.add(cota);
						}  else if((!naturezaOperacao.isGerarCotaContribuinteICMS()
								&& ((cota.isContribuinteICMS() != null && !cota.isContribuinteICMS()))
								&& (naturezaOperacao.isGerarCotaExigeNFe() 
										|| (cota.isExigeNotaFiscalEletronica() != null && cota.isExigeNotaFiscalEletronica() )))
								&& cota.isContribuinteICMSExigeNFe()) {
							cotasContribuinteEmitente.add(cota);
						} else if(((cota.isContribuinteICMS() != null && !cota.isContribuinteICMS()))
								&& (naturezaOperacao.isGerarCotaExigeNFe() 
										|| (cota.isExigeNotaFiscalEletronica() != null && cota.isExigeNotaFiscalEletronica() ))
								&& cota.isContribuinteICMSExigeNFe()) {
							cotasContribuinteEmitente.add(cota);
						}
					}
				} else if(filtro.getNotaFiscalTipoEmissao() != null && filtro.getNotaFiscalTipoEmissao().equals(NotaFiscalTipoEmissaoRegimeEspecial.CONSOLIDADO)) {
					for (CotaExemplaresDTO cota : cotas) {
						if(naturezaOperacao.isGerarCotaNaoExigeNFe() 
								&& (cota.isContribuinteICMS() == null || !cota.isContribuinteICMS()) 
								&& (cota.isExigeNotaFiscalEletronica() == null || !cota.isExigeNotaFiscalEletronica() )) {
							cota.setNotaFiscalConsolidada(true);
							cotasContribuinteEmitente.add(cota);
						} else if((!naturezaOperacao.isGerarCotaContribuinteICMS()
								&& ((cota.isContribuinteICMS() != null && !cota.isContribuinteICMS()))
								&& (naturezaOperacao.isGerarCotaExigeNFe() 
										|| (cota.isExigeNotaFiscalEletronica() != null && cota.isExigeNotaFiscalEletronica() ))
								&& !cota.isContribuinteICMSExigeNFe() )) {
							cota.setNotaFiscalConsolidada(true);
							cotasContribuinteEmitente.add(cota);
						} else if(((cota.isContribuinteICMS() != null && !cota.isContribuinteICMS()))
								&& (naturezaOperacao.isGerarCotaExigeNFe() 
										|| (cota.isExigeNotaFiscalEletronica() != null && cota.isExigeNotaFiscalEletronica() )) 
								&& !cota.isContribuinteICMSExigeNFe()) {
							cota.setNotaFiscalConsolidada(true);
							cotasContribuinteEmitente.add(cota);
						} else if(((cota.isExigeNotaFiscalEletronica() != null && cota.isExigeNotaFiscalEletronica()))
								&& (naturezaOperacao.isGerarCotaExigeNFe() 
										|| (cota.isExigeNotaFiscalEletronica() != null && cota.isExigeNotaFiscalEletronica() )) 
								&& !cota.isContribuinteICMSExigeNFe()) {
							cota.setNotaFiscalConsolidada(true);
							cotasContribuinteEmitente.add(cota);
						}
					}
				} else if(filtro.getNotaFiscalTipoEmissao() != null && filtro.getNotaFiscalTipoEmissao().equals(NotaFiscalTipoEmissaoRegimeEspecial.AMBOS)) {
					for (CotaExemplaresDTO cota : cotas) {
						if(naturezaOperacao.isGerarCotaNaoExigeNFe() 
								&& (cota.isContribuinteICMS() == null || !cota.isContribuinteICMS()) 
								&& (cota.isExigeNotaFiscalEletronica() == null || !cota.isExigeNotaFiscalEletronica() )) {
							cota.setNotaFiscalConsolidada(true);
							cotasContribuinteEmitente.add(cota);
						} else if((naturezaOperacao.isGerarCotaContribuinteICMS()
								&& ((cota.isContribuinteICMS() != null && cota.isContribuinteICMS())))) {
							cotasContribuinteEmitente.add(cota);
						} else if((!naturezaOperacao.isGerarCotaContribuinteICMS()
								&& ((cota.isContribuinteICMS() != null && !cota.isContribuinteICMS()))
								&& (naturezaOperacao.isGerarCotaExigeNFe() 
										|| (cota.isExigeNotaFiscalEletronica() != null && cota.isExigeNotaFiscalEletronica() ))
								&& cota.isContribuinteICMSExigeNFe() )) {
							cotasContribuinteEmitente.add(cota);
						} else if((!naturezaOperacao.isGerarCotaContribuinteICMS()
								&& ((cota.isContribuinteICMS() != null && !cota.isContribuinteICMS()))
								&& (naturezaOperacao.isGerarCotaExigeNFe() 
										|| (cota.isExigeNotaFiscalEletronica() != null && cota.isExigeNotaFiscalEletronica() ))
								&& !cota.isContribuinteICMSExigeNFe() )) {
							cota.setNotaFiscalConsolidada(true);
							cotasContribuinteEmitente.add(cota);
						} else if(((cota.isContribuinteICMS() != null && !cota.isContribuinteICMS()))
								&& (naturezaOperacao.isGerarCotaExigeNFe() 
										|| (cota.isExigeNotaFiscalEletronica() != null && cota.isExigeNotaFiscalEletronica() )) 
								&& cota.isContribuinteICMSExigeNFe()) {
							cotasContribuinteEmitente.add(cota);
						} else if(((cota.isContribuinteICMS() != null && !cota.isContribuinteICMS()))
								&& (naturezaOperacao.isGerarCotaExigeNFe() 
										|| (cota.isExigeNotaFiscalEletronica() != null && cota.isExigeNotaFiscalEletronica() )) 
								&& !cota.isContribuinteICMSExigeNFe()) {
							cota.setNotaFiscalConsolidada(true);
							cotasContribuinteEmitente.add(cota);
						}
					}
					
					break;
				}
			}
		}
		return cotasContribuinteEmitente;
	}

	@Override
	@Transactional
	public Long consultaCotaExemplareSumarizadoQtd(final FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao) {
		
		Distribuidor distribuidor = this.obterInformacaoDistribuidor();
		
		if(!distribuidor.isPossuiRegimeEspecialDispensaInterna()) {
			
			return notaFiscalService.consultaCotaExemplaresSumarizadoQtd(filtro, naturezaOperacao);
		} else {
			
			filtro.setPaginacaoVO(null);			
			return (long) this.listaRegimeEspecial(filtro, naturezaOperacao, distribuidor).size();	
		}
		
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
			
			if(notaFiscal.getNotaFiscalInformacoes().getInfAdicWrapper() == null) {
				InfAdicWrapper infAdicWrapper = new InfAdicWrapper();
				notaFiscal.getNotaFiscalInformacoes().setInfAdicWrapper(infAdicWrapper);
			}
			
			notaFiscal.getNotaFiscalInformacoes().getInfAdicWrapper().setInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
			FaturaBuilder.montarFaturaNotaFiscal(notaFiscal);
			NotaFiscalValoresCalculadosBuilder.montarValoresCalculados(notaFiscal);
			notasFiscais.add(notaFiscal);
		}
	}
	
	@Override
	@Transactional
	public NaturezaOperacao regimeEspecialParaCota(Cota cota){
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		if(distribuidor.isPossuiRegimeEspecialDispensaInterna()) {
			if(cota.getParametrosCotaNotaFiscalEletronica() != null) {				
				if(cota.getParametrosCotaNotaFiscalEletronica().isContribuinteICMS()) {
					return this.naturezaOperacaoRepository.obterNaturezaOperacao(distribuidor.getTipoAtividade(), TipoEmitente.COTA, TipoDestinatario.DISTRIBUIDOR, TipoOperacao.SAIDA, false, false, null);
				} else {//if(cota.getParametrosCotaNotaFiscalEletronica().isExigeNotaFiscalEletronica()){
					return this.naturezaOperacaoRepository.obterNaturezaOperacao(distribuidor.getTipoAtividade(), TipoEmitente.COTA, TipoDestinatario.DISTRIBUIDOR, TipoOperacao.ENTRADA, false, false, null);
				}
			} else {
				return this.naturezaOperacaoRepository.obterNaturezaOperacao(distribuidor.getTipoAtividade(), TipoEmitente.COTA, TipoDestinatario.DISTRIBUIDOR, TipoOperacao.ENTRADA, false, false, null);
			}
		} else {
        	
			if(cota.getParametrosCotaNotaFiscalEletronica() != null) {
				if(cota.getParametrosCotaNotaFiscalEletronica().isContribuinteICMS()) {
	               return this.naturezaOperacaoRepository.obterNaturezaOperacao(distribuidor.getTipoAtividade(), TipoEmitente.COTA, TipoDestinatario.FORNECEDOR, TipoOperacao.SAIDA, false, false, null);
	            } else {//if(cota.getParametrosCotaNotaFiscalEletronica().isExigeNotaFiscalEletronica()){
	            	return this.naturezaOperacaoRepository.obterNaturezaOperacao(distribuidor.getTipoAtividade(), TipoEmitente.COTA, TipoDestinatario.DISTRIBUIDOR, TipoOperacao.ENTRADA, false, false, null);
	            } 
	        } else {
            	return this.naturezaOperacaoRepository.obterNaturezaOperacao(distribuidor.getTipoAtividade(), TipoEmitente.COTA, TipoDestinatario.DISTRIBUIDOR, TipoOperacao.ENTRADA, false, false, null);
	        }
		}	
    }
	
	private void gerarNotaFiscalReferenciada(List<NotaFiscal> notaFiscais, TipoDestinatario tipoDestinatario) {
		
		Set<NotaFiscalReferenciada> listaNotaFiscalReferenciadas = new HashSet<>();
		
		NotaFiscalReferenciada notaReferenciada = null;
		
		for (NotaFiscal notaFiscal : notaFiscais) {
			
			List<Long> produtoEdicoesIds = new ArrayList<>();
 			
			for (DetalheNotaFiscal detalhe : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
				produtoEdicoesIds.add(detalhe.getProdutoServico().getProdutoEdicao().getId());
			}
			
			List<NotaFiscalDTO> notaFiscalDTOs = this.notaFiscalRepository.obterNotasPeloItensNotas(produtoEdicoesIds, tipoDestinatario);				

			NotaFiscalReferenciadaWrapper notaFiscalReferenciadaWrapper = null;
			if(notaFiscalDTOs != null ) {
				notaFiscalReferenciadaWrapper = new NotaFiscalReferenciadaWrapper();
				for (NotaFiscalDTO notaFiscalDTO : notaFiscalDTOs) {
					notaReferenciada = new NotaFiscalReferenciada();
					if(notaReferenciada.getNotaFiscalReferenciadaNFE() == null){
						NotaFiscalReferenciadaNFE notaFiscalReferenciadaNFE = new NotaFiscalReferenciadaNFE();
						notaReferenciada.setNotaFiscalReferenciadaNFE(notaFiscalReferenciadaNFE);
						
					}
					
					notaReferenciada.getNotaFiscalReferenciadaNFE().setCodigoUF(notaFiscalDTO.getCodigoUF());
					notaReferenciada.getNotaFiscalReferenciadaNFE().setCnpj(notaFiscalDTO.getCnpj());
					notaReferenciada.getNotaFiscalReferenciadaNFE().setDataEmissao(notaFiscalDTO.getDataEmissao());
					notaReferenciada.getNotaFiscalReferenciadaNFE().setModelo(notaFiscalDTO.getModelo());
					notaReferenciada.getNotaFiscalReferenciadaNFE().setNumeroDocumentoFiscal(notaFiscalDTO.getNumero());
					notaReferenciada.getNotaFiscalReferenciadaNFE().setSerie(String.valueOf(notaFiscalDTO.getSerie()));
					
					NotaFiscalReferenciadaPK pk = new NotaFiscalReferenciadaPK();
					pk.setChaveAcesso(notaFiscalDTO.getChaveAcesso());
					pk.setNotaFiscal(notaFiscal);
					notaReferenciada.setPk(pk);
					listaNotaFiscalReferenciadas.add(notaReferenciada);
						
					notaFiscalReferenciadaWrapper.setListReferenciadas(new ArrayList<>(listaNotaFiscalReferenciadas));
				}
				
				notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setNotaFiscalReferenciada(notaFiscalReferenciadaWrapper);
			}			

		}
		
	}
	
	private static void montaChaveAcesso(NotaFiscal notaFiscal) {
		
		
		String mes = recuperarMes(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getDataEmissao());
		String ano = recuperarAno(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getDataEmissao());
		
		String mesFormatado = lpadTo(mes, 2, '0');;
		String anoFormatado = ano.substring(2, 4);
		
		String anoMes = anoFormatado + mesFormatado;
		
		StringBuilder chave = new StringBuilder();  
        chave.append(lpadTo(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getCodigoUf().toString(), 2, '0'));  
        chave.append(lpadTo(anoMes, 4, '0'));  
        chave.append(lpadTo(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getDocumento().getDocumento().toString().replaceAll("\\D",""), 14, '0'));  
        chave.append(lpadTo(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getModeloDocumentoFiscal().toString(), 2, '0'));  
        chave.append(lpadTo(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getSerie().toString(), 3, '0'));  
        chave.append(lpadTo(String.valueOf(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNumeroDocumentoFiscal()), 9, '0'));  
        chave.append(lpadTo(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getTipoEmissao().getIntValue().toString(), 1, '0'));  
        chave.append(lpadTo(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getCodigoNF(), 8, '0'));  
        chave.append(gerarChaveAcesso(chave.toString()));
		
        chave.insert(0, "NFe");
		
		notaFiscal.getNotaFiscalInformacoes().setIdNFe(chave.toString());
		
	}
	
	private static String recuperarMes(Date data) {
		GregorianCalendar dataCal = new GregorianCalendar();  
		dataCal.setTime(data);  
		return String.valueOf(dataCal.get(Calendar.MONTH)+1);  
	}
	
	private static String recuperarAno(Date data) {
		GregorianCalendar dataCal = new GregorianCalendar();  
		dataCal.setTime(data);  
		return String.valueOf(dataCal.get(Calendar.YEAR));  
	}
	
	public static String lpadTo(String input, int width, char ch) {  
        String strPad = "";  
  
        StringBuffer sb = new StringBuffer(input.trim());  
        
        while (sb.length() < width)  
            sb.insert(0,ch);  
        strPad = sb.toString();  
          
        if (strPad.length() > width) {  
            strPad = strPad.substring(0,width);  
        }
        
        return strPad;  
	}  
	
	public static int gerarChaveAcesso(String chave) {  
        int total = 0;  
        int peso = 2;  
              
        for (int i = 0; i < chave.length(); i++) {  
            total += (chave.charAt((chave.length()-1) - i) - '0') * peso;  
            peso ++;  
            if (peso == 10)  
                peso = 2;  
        }  
        int resto = total % 11;  
        return (resto == 0 || resto == 1) ? 0 : (11 - resto);  
    }

}
