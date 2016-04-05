package br.com.abril.nds.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliseHistogramaDTO;
import br.com.abril.nds.dto.AnaliseHistoricoDTO;
import br.com.abril.nds.dto.DataCEConferivelDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO.ModoTela;
import br.com.abril.nds.dto.filtro.FiltroHistogramaVendas;
import br.com.abril.nds.dto.filtro.FiltroHistoricoVendaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.HistoricoAlteracaoPrecoVenda;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Brinde;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.Dimensao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SegmentacaoProduto;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BrindeRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.HistoricoAlteracaoPrecoVendaRepository;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.LancamentoParcialRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.FuroProdutoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;



/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.ProdutoEdicao}
 * 
 * @author Discover Technology
 */
@Service
public class ProdutoEdicaoServiceImpl implements ProdutoEdicaoService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoEdicaoServiceImpl.class);
    
    @Autowired
    private ProdutoEdicaoRepository produtoEdicaoRepository;
    
    @Autowired
    private DistribuicaoFornecedorRepository distribuicaoFornecedorRepository;
    
    @Autowired
    private ParametroSistemaRepository parametroSistemaRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private LancamentoRepository lancamentoRepository;
    
    @Autowired
    private CapaService capaService;
    
    @Autowired
    private BrindeRepository brindeRepository;
    
    @Autowired
    private DescontoService descontoService;
    
    @Autowired
    private DescontoProdutoEdicaoRepository descontoProdutoEdicaoRepository;
    
    @Autowired
    private ProdutoService pService;
    
    @Autowired
    private LancamentoService lService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private FuroProdutoService furoProdutoService;
    
    @Autowired
    private LancamentoParcialRepository lancamentoParcialRepository;
    
    @Autowired
    private PeriodoLancamentoParcialRepository periodoLancamentoParcialRepository;
    
    @Autowired
    private CotaRepository cotaRepository;
    
    @Autowired
    private MovimentoEstoqueService movimentoEstoqueService;
    
    @Autowired
    private CotaService cotaService;
    
    @Autowired
    private CalendarioService calendarioService;
    
    @Autowired
    private ItemNotaFiscalEntradaRepository itemNotaFiscalEntradaRepository;
    
    @Autowired
    private MovimentoEstoqueCotaService movimentoEstoqueCotaService;
    
    @Autowired
    private HistoricoAlteracaoPrecoVendaRepository historicoAlteracaoPrecoVendaRepository;
    
    @Autowired
    private EstoqueProdutoService estoqueProdutoService;
    
    @Value("${data_cabalistica}")
    private String dataCabalistica;
    
    private static final BigDecimal CEM = BigDecimal.TEN.multiply(BigDecimal.TEN);
    
    @Override
    @Transactional(readOnly = true)
    public ProdutoEdicao obterProdutoEdicao(final Long idProdutoEdicao, final boolean indCarregaFornecedores) {
        
        if (idProdutoEdicao == null || Long.valueOf(0).equals(idProdutoEdicao)) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
                    "Código de identificação da Edição é inválida!"));
        }
        
        final ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
        
        if(indCarregaFornecedores) {
            produtoEdicao.getProduto().getFornecedor().getJuridica();
            produtoEdicao.getProduto().getFornecedores();
        }
        
        return produtoEdicao;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoEdicao> obterProdutoEdicaoPorNomeProduto(final String nomeProduto) {
        return produtoEdicaoRepository.obterProdutoEdicaoPorNomeProduto(nomeProduto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public FuroProdutoDTO obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
            final String codigo, final String nomeProduto, final Long edicao, final Date dataLancamento, final boolean furado) {
        
        final List<String> mensagensValidacao = new ArrayList<String>();
        
        if (codigo == null || codigo.isEmpty()){
            mensagensValidacao.add("Código é obrigatório.");
        }
        
        if (edicao == null){
            mensagensValidacao.add("Edição é obrigatório.");
        }
        
        if (dataLancamento == null){
            mensagensValidacao.add("Data Lançamento é obrigatório.");
        }
        
        if (!mensagensValidacao.isEmpty()){
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
        }
        
        final FuroProdutoDTO furoProdutoDTO = produtoEdicaoRepository.
                obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
                        codigo, nomeProduto, edicao, dataLancamento, furado);
        
        if (furoProdutoDTO != null){
            //buscar path de imagens
            final ParametroSistema parametroSistema =
                    parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_IMAGENS_CAPA);
            
            if (parametroSistema != null){
                furoProdutoDTO.setPathImagem(parametroSistema.getValor() + furoProdutoDTO.getPathImagem());
            }
            
            // buscar proxima data para lançamento
            
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(new SimpleDateFormat(furoProdutoDTO.DATE_PATTERN_PT_BR).parse(furoProdutoDTO.getNovaData()));
                final Calendar dataOperacao = Calendar.getInstance();
                dataOperacao.setTime(distribuidorService.obterDataOperacaoDistribuidor());
                
                if(calendar.before(dataOperacao)){
                    calendar = dataOperacao;
                }
                
                
            } catch (final ParseException e) {
                LOGGER.debug(e.getMessage(), e);
                return furoProdutoDTO;
            }
            
            final List<Integer> listaDiasSemana =
                    distribuicaoFornecedorRepository.obterDiasSemanaDistribuicao(
                            furoProdutoDTO.getCodigoProduto(),
                            furoProdutoDTO.getIdProdutoEdicao(), OperacaoDistribuidor.DISTRIBUICAO);
            
            if (listaDiasSemana != null && !listaDiasSemana.isEmpty()){
                int diaSemana = -1;
                for (final Integer dia : listaDiasSemana){
                    if (dia > calendar.get(Calendar.DAY_OF_WEEK)){
                        diaSemana = dia;
                        break;
                    }
                }
                
                if (diaSemana == -1){
                	
                    diaSemana = listaDiasSemana.get(0);
                }
                
                while ((calendar.get(Calendar.DAY_OF_WEEK) != diaSemana)){
                	
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }

                calendar.setTime(this.furoProdutoService.obterProximaDataDiaOperante(codigo,furoProdutoDTO.getIdProdutoEdicao(),calendar.getTime()));

                furoProdutoDTO.setNovaData(
                        new SimpleDateFormat(furoProdutoDTO.DATE_PATTERN_PT_BR).format(calendar.getTime()));
            }
        }
        
        return furoProdutoDTO;
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProdutoEdicao obterProdutoEdicaoPorCodProdutoNumEdicao(final String codigoProduto, final String numeroEdicao) {
        
        final List<String> mensagensValidacao = new ArrayList<String>();
        
        if (codigoProduto == null || codigoProduto.isEmpty()) {
            
            mensagensValidacao.add("Código é obrigatório.");
        }
        
        if (numeroEdicao == null || numeroEdicao.isEmpty()) {
            
            mensagensValidacao.add("Número edição é obrigatório.");
        }
        
        if (!Util.isLong(numeroEdicao)) {
            
            mensagensValidacao.add("Número edição é inválido.");
        }
        
        if (!mensagensValidacao.isEmpty()){
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
        }
        
        return produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
                StringUtils.leftPad(codigoProduto, 8, '0'), Long.parseLong(numeroEdicao));
    }
    
    @Override
    @Transactional
    public List<ProdutoEdicao> obterProdutosEdicaoPorCodigoProduto(
            final String codigoProduto) {
        return produtoEdicaoRepository.obterProdutosEdicaoPorCodigoProduto(codigoProduto);
    }
    
    @Override
    @Transactional
    public void alterarProdutoEdicao(final ProdutoEdicao produtoEdicao) {
        produtoEdicaoRepository.alterar(produtoEdicao);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoEdicao> obterProdutoPorCodigoNomeParaRecolhimento(
    		final String codigoNomeProduto,
            final Integer numeroCota,
            final Integer quantidadeRegistros,
            final Map<Long, DataCEConferivelDTO> mapaDataCEConferivel) {
        
    	if(mapaDataCEConferivel == null || mapaDataCEConferivel.isEmpty()) {
    		return new ArrayList<ProdutoEdicao>();
    	}
    	
        if (codigoNomeProduto == null || codigoNomeProduto.trim().isEmpty()){
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Codigo/nome produto é obrigatório.");
        }
        
        boolean indAceitaRecolhimentoParcialAtraso = distribuidorService.distribuidorAceitaRecolhimentoParcialAtraso();
        
        final List<ProdutoEdicao> produtosEdicao = produtoEdicaoRepository.obterProdutoPorCodigoNomeCodigoSM(null,
                codigoNomeProduto, numeroCota, quantidadeRegistros, mapaDataCEConferivel, null, indAceitaRecolhimentoParcialAtraso);
        
        return produtosEdicao;

    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoEdicaoDTO> pesquisarEdicoes(final String codigoProduto, final String nome, final Intervalo<Date> dataLancamento, final Intervalo<Double> preco, final StatusLancamento statusLancamento,
            final String codigoDeBarras, final boolean brinde, final String sortorder, final String sortname, final int page, final int maxResults) {
        
        final int initialResult = ((page * maxResults) - maxResults);
        
        final List<ProdutoEdicaoDTO> edicoes = produtoEdicaoRepository.pesquisarEdicoes(codigoProduto, nome, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde, sortorder, sortname, initialResult, maxResults);
        
        return edicoes;
    }
    
    //Parse por conta do merge.
    @Override
    @Transactional(readOnly = true)
    public Long countPesquisarEdicoes(final String codigoProduto, final String nome, final Intervalo<Date> dataLancamento, final Intervalo<Double> preco,
            final StatusLancamento statusLancamento, final String codigoDeBarras, final boolean brinde) {
        
        final Integer count = produtoEdicaoRepository.countPesquisarEdicoes(codigoProduto, nome, dataLancamento, preco, statusLancamento, codigoDeBarras, brinde);
        
        return Long.valueOf(count);
    }
    
	                                        /**
     * Insere os dados de desconto relativos ao produto edição em questão.
     * 
     * @param produtoEdicao
     * @param indNovoProdutoEdicao
     */
    private void inserirDescontoProdutoEdicao(final ProdutoEdicao produtoEdicao, final boolean indNovoProdutoEdicao) {
        
        final Produto produto = produtoEdicao.getProduto();
        
        final GrupoProduto grupoProduto = produto.getTipoProduto().getGrupoProduto();
        
        if(!indNovoProdutoEdicao || GrupoProduto.OUTROS.equals( grupoProduto )) {
            return;
        }
        
        final Fornecedor fornecedor = produto.getFornecedor();
        
        final Set<Fornecedor> conjuntoFornecedor = new HashSet<Fornecedor>();
        
        conjuntoFornecedor.add(fornecedor);
        
        
        
        final Set<DescontoProdutoEdicao> conjuntoDescontoProdutoEdicaoEspecifico =
                descontoProdutoEdicaoRepository.obterDescontoProdutoEdicao(TipoDesconto.ESPECIFICO, fornecedor, null);
        
        if(conjuntoDescontoProdutoEdicaoEspecifico!=null && !conjuntoDescontoProdutoEdicaoEspecifico.isEmpty()) {
            
            
            for(final DescontoProdutoEdicao descontoEspecifico : conjuntoDescontoProdutoEdicaoEspecifico) {
                
                final Cota cota = descontoEspecifico.getCota();
                
                descontoService.processarDescontoCota(cota, conjuntoFornecedor, descontoEspecifico.getDesconto());
                
            }
            
            
        }
        
        final Set<DescontoProdutoEdicao> conjuntoDescontoProdutoEdicaoGeral =
                descontoProdutoEdicaoRepository.obterDescontoProdutoEdicao(TipoDesconto.GERAL, fornecedor, null);
        
        if(conjuntoDescontoProdutoEdicaoGeral!=null && !conjuntoDescontoProdutoEdicaoGeral.isEmpty()) {
            
            
            for(final DescontoProdutoEdicao descontoGeral : conjuntoDescontoProdutoEdicaoGeral) {
                
                descontoService.processarDescontoDistribuidor(conjuntoFornecedor, descontoGeral.getDesconto());
                
            }
            
            
        }
        
    }
    
    private boolean isDataRecolhimentoAlterada(ProdutoEdicaoDTO dto, Lancamento lancamento,ProdutoEdicaoDTO dtoAnterior) {
    	
    	if ( dtoAnterior != null && dto.getDataRecolhimentoDistribuidor() != null &&
    			dto.getDataRecolhimentoDistribuidor().equals(dtoAnterior.getDataRecolhimentoDistribuidor()))
    		return false;
    	if(lancamento != null && lancamento.getPeriodoLancamentoParcial() != null) {
            
    		final PeriodoLancamentoParcial ultimoPeriodo = periodoLancamentoParcialRepository.obterUltimoLancamentoParcial(dto.getId());
            
    		if(ultimoPeriodo != null) {
    			
    			Lancamento ulancamento = ultimoPeriodo.getLancamentoPeriodoParcial();
    			
    			Date dataUltimoRecolhimento = null;
    			if(ultimoPeriodo != null && ulancamento != null) {
    				dataUltimoRecolhimento = ulancamento.getDataRecolhimentoDistribuidor();
    			} else {
    				dataUltimoRecolhimento = lancamento.getDataRecolhimentoDistribuidor();
    			}
    			
    			if( dto.getDataRecolhimentoDistribuidor() != null && dto.getDataRecolhimentoDistribuidor().compareTo(dataUltimoRecolhimento) == 0 ) {
    				return false;
    			}
    		}
    	
    	} else if( dto.getDataRecolhimentoDistribuidor() != null && dto.getDataRecolhimentoDistribuidor().compareTo(lancamento.getDataRecolhimentoDistribuidor()) == 0 ) {
			return false;
    	}
		
		return true;
    	
    }
    
    private boolean isDataLancamentoAlterada(ProdutoEdicaoDTO dto, Lancamento lancamento,ProdutoEdicaoDTO dtoAnterior)  {

    	if (dtoAnterior != null &&  dto.getDataLancamento() != null  && dto.getDataLancamento().equals(dtoAnterior.getDataLancamento()))
    		return false;
		if( dto.getDataLancamento() != null && 
			dto.getDataLancamento().compareTo(lancamento.getDataLancamentoDistribuidor()) == 0 ) {
			return false;
		}
		
		return true;

    }
    
    private boolean isDataLancamentoPrevistoAlterada(ProdutoEdicaoDTO dto, Lancamento lancamento,ProdutoEdicaoDTO dtoAnterior)  {

    	if ( dtoAnterior != null && dto.getDataLancamentoPrevisto() != null &&  dto.getDataLancamentoPrevisto().equals(dtoAnterior.getDataLancamentoPrevisto()))
    			return false;
		if( dto.getDataLancamentoPrevisto() != null && 
			dto.getDataLancamentoPrevisto().compareTo(lancamento.getDataLancamentoPrevista()) == 0 ) {
			return false;
		}
		
		return true;

    }
    
    /**
     * Verifica se data de lancamento é dia operante e feriado com operação
     * 
     * @param data
     * @param idFornecedor
     * @return boolean
     */
    private boolean dataLancamentoValida(Date data, Long idFornecedor){
    	
    	return this.calendarioService.isDiaOperante(data, idFornecedor, OperacaoDistribuidor.DISTRIBUICAO);
    }

    @Transactional(readOnly=true)
	public List<String> validarDadosBasicosEdicao(ProdutoEdicaoDTO dto, String codigoProduto,ProdutoEdicaoDTO dtoAnterior) {
    	
    	boolean indDataLancamentoPrevistoAlterada = true;
    	
    	boolean indDataLancamentoAlterada = true;
    	
    	boolean indDataRecolhimentoAlterada = true;
    	
    	if(dto.getId()!=null) {
    		
    		Lancamento lancamento = lService.obterPrimeiroLancamentoDaEdicao(dto.getId());
        	
    		if(lancamento != null) {
    			
    			    indDataLancamentoPrevistoAlterada = isDataLancamentoPrevistoAlterada(dto, lancamento,dtoAnterior);
    		
    				indDataLancamentoAlterada = isDataLancamentoAlterada(dto, lancamento,dtoAnterior);
    						
        			indDataRecolhimentoAlterada = isDataRecolhimentoAlterada(dto, lancamento,dtoAnterior);
        	}
    	}
    	
		List<String> listaMensagens = new ArrayList<String>();
						
		ProdutoEdicao pe = null;
		
		if(codigoProduto == null) {
			
            listaMensagens.add("Código do produto inválido!");
		}
			
		Date dataLancDistribuidor = dto.getDataLancamento();
		
		Date dataRecDistribuidor = dto.getDataRecolhimentoDistribuidor();
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		if(indDataLancamentoPrevistoAlterada) {

            if(dto.getDataLancamentoPrevisto()!=null) {

            	boolean dataValida = this.dataLancamentoValida(dto.getDataLancamentoPrevisto(), dto.getIdFornecedor());

	            if (!dataValida){
	            	
	            	throw new ValidacaoException(TipoMensagem.WARNING, "A data de lançamento previsto deve ser dia útil e operante!");
	            }
			}      
		}
		
		if(indDataLancamentoAlterada || indDataRecolhimentoAlterada) {

			if(dataLancDistribuidor!= null && dataRecDistribuidor != null) {
				
				if(dataLancDistribuidor.compareTo(dataRecDistribuidor) >= 0 ) {
					
		            listaMensagens.add("Data de recolhimento distribuidor deve ser maior que a data lançamento distribuidor!");
				}
			}
		}
		
		if(indDataLancamentoAlterada) {

			if(dto.getDataLancamento()!=null) {

	            if(dto.getDataLancamentoPrevisto()!=null) {

	            	boolean dataValida = this.dataLancamentoValida(dto.getDataLancamento(), dto.getIdFornecedor());

		            if (!dataValida){
		            	
		            	// listaMensagens.add("A data de lançamento previsto deve ser dia útil e operante!");
		            	
		            	throw new ValidacaoException(TipoMensagem.WARNING, "A data de lançamento previsto deve ser dia útil e operante!");
		            }
				}       
			}

			if(dataLancDistribuidor != null && dataLancDistribuidor.compareTo(dataOperacao) <= 0 ) {
				
	            listaMensagens.add("Data de lançamento distribuidor deve ser maior que a data de operação do sistema!");
			}
		}
		
		if(indDataRecolhimentoAlterada) {
			
			if(dataRecDistribuidor!=null) {
				
	            if (!this.calendarioService.isDiaOperante(dataRecDistribuidor, dto.getIdFornecedor(), OperacaoDistribuidor.RECOLHIMENTO)) {
	            	
	                listaMensagens.add("Data de recolhimento deve ser um dia operante!");
				}
			}
			
			boolean indMatrizRecolhimentoConfirmada = lancamentoRepository.existeMatrizRecolhimentoConfirmado(dto.getDataRecolhimentoDistribuidor());
			
			if(indMatrizRecolhimentoConfirmada) {
				
	            listaMensagens.add("Escolha outra data de recolhimento. Matriz de recolhimento já confirmada nesta data!");
			}


			if(dataRecDistribuidor != null && dataRecDistribuidor.compareTo(dataOperacao) <= 0 ) {
				
	            listaMensagens.add("Data de recolhimento distribuidor deve ser maior que a data de operação do sistema!");
			}
		}

		if(dto.getId()!=null) {

			pe = obterProdutoEdicao(dto.getId(), false);
			
			if(pe == null) {
				
                listaMensagens.add("Produto Edição inválido!");
			}
		}
		
		return listaMensagens;
	}
    
    @Override
    @Transactional
    public void salvarProdutoEdicao(
            final ProdutoEdicaoDTO dto, final String codigoProduto, final String contentType, 
            final InputStream imgInputStream, final boolean istrac29, final ModoTela modoTela) {
    
        ProdutoEdicao produtoEdicao = null;
                
        final boolean indNovoProdutoEdicao = (dto.getId() == null);
        
        boolean semRestricao = false;
        
        if (indNovoProdutoEdicao) {
            produtoEdicao = new ProdutoEdicao();
            produtoEdicao.setProduto(produtoRepository.obterProdutoPorCodigoProdin(codigoProduto));
            produtoEdicao.setOrigem(Origem.MANUAL);
        } else {
            produtoEdicao = produtoEdicaoRepository.buscarPorId(dto.getId());
            semRestricao = validarAtualizacaoCampoSemRestricao(dto, produtoEdicao);
        }
        
        boolean indDataLancamentoAlterada = true;
        boolean indDataLancamentoPrevistoAlterada = true;
        boolean indDataRecolhimentoAlterada = true;
        
    	if(dto.getId() != null) {
    		
    		Lancamento lancamento = lService.obterPrimeiroLancamentoDaEdicao(dto.getId());
        	
    		if(lancamento != null) {
    			
        		indDataLancamentoAlterada = isDataLancamentoAlterada(dto, lancamento,null);
        		indDataRecolhimentoAlterada = isDataRecolhimentoAlterada(dto, lancamento,null);
        		indDataLancamentoPrevistoAlterada = isDataLancamentoPrevistoAlterada(dto, lancamento,null);
        	}
    	}
        
    	if(dto.isParcial()) {
        	if(!semRestricao) {
        		if(indDataLancamentoAlterada || indDataRecolhimentoAlterada || indDataLancamentoPrevistoAlterada) {
        			throw new ValidacaoException(TipoMensagem.WARNING, "Não é possível a alteração de datas dos Lancamentos de Parciais. Utilize a tela de Parciais.");
        		}
        	}
        }
    	
        if (!ModoTela.EDICAO.equals(modoTela) && produtoEdicaoRepository.isNumeroEdicaoCadastrada(produtoEdicao.getProduto().getId(),
                dto.getNumeroEdicao(), produtoEdicao.getId())) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Número da edição ja cadastra. Escolha outro número.");
        }
        
        this.validarAlteracaoDePrecoDeCapaDoProdutoEdicao(produtoEdicao, dto);
        
        // 01 ) Salvar/Atualizar o ProdutoEdicao:
        produtoEdicao = this.salvarProdutoEdicao(dto, produtoEdicao);
        
        // 02) Salvar imagem:
        if (imgInputStream != null) {
            
            // Verifica se o tipo do arquivo é imagem JPEG, PNG ou GIF:
            if(!FileType.JPEG.getContentType().equalsIgnoreCase(contentType) &&
                    !FileType.GIF.getContentType().equalsIgnoreCase(contentType)  &&
                    !FileType.PNG.getContentType().equalsIgnoreCase(contentType)) {
            	
                throw new ValidacaoException(TipoMensagem.WARNING, "O formato da imagem da capa não é válido!");
            }
            
            capaService.saveCapa(produtoEdicao.getId(), contentType, imgInputStream);
        }
            
        final Usuario usuario = usuarioService.getUsuarioLogado();
        
        List<Lancamento> lancamentos = this.obterLancamentos(dto, produtoEdicao);
        this.validarRegimeRecolhimento(dto, lancamentos, produtoEdicao, indDataRecolhimentoAlterada);

        Lancamento lancamento = null;
      
        if(indDataLancamentoAlterada || indDataRecolhimentoAlterada) {
        	if(indDataLancamentoAlterada) {
        		
        		lancamento = this.obterLancamento(dto, produtoEdicao, false);
        	} else {
        		
        		lancamento = this.obterLancamento(dto, produtoEdicao, true);
        	}
        	
        	if(lancamento != null && lancamento.getNumeroLancamento() != null && lancamento.getNumeroLancamento() > 0) {
        		
        		dto.setNumeroLancamento(lancamento.getNumeroLancamento());
        	}
        
        	lancamento = this.salvarLancamento(lancamento, dto, produtoEdicao, usuario);
        	
        } else { // atualizar reparte_previsto, 
        	// obter ultima lancamento
        	lancamento = this.obterLancamento(dto, produtoEdicao, true);
        	lancamento = this.salvarLancamentoReparte(lancamento, dto, produtoEdicao, usuario);
        	
        }
        
        if(dto.isParcial()) {
        	
        	if(lancamento == null) {
        		lancamento = this.obterLancamento(dto, produtoEdicao, true);
        	}
        	
        	this.salvarLancamentoParcial(dto, produtoEdicao, usuario, indNovoProdutoEdicao, lancamento);
        }
    	
    	if(indDataRecolhimentoAlterada) {
    		
    		for (Lancamento lanc : lancamentos) {
    			
    			if(Arrays.asList(StatusLancamento.PLANEJADO, StatusLancamento.CONFIRMADO, StatusLancamento.EM_BALANCEAMENTO
    					, StatusLancamento.BALANCEADO, StatusLancamento.EXPEDIDO).contains(lanc.getStatus())) {
    				
    				lanc.setDataRecolhimentoDistribuidor(dto.getDataRecolhimentoDistribuidor());
    			}
    		}
    	}
        
        this.inserirDescontoProdutoEdicao(produtoEdicao, indNovoProdutoEdicao);
        
    }

	private boolean validarAtualizacaoCampoSemRestricao(ProdutoEdicaoDTO dto, ProdutoEdicao produtoEdicao) {
		
		if(produtoEdicao.getPacotePadrao() != dto.getPacotePadrao()){
			return true;
		}
		
		if(produtoEdicao.getPeb() != dto.getPeb()){
			return true;
		}
		
		if(produtoEdicao.getCodigoDeBarras() == null ) {
			if ( dto.getCodigoDeBarras() == null )
			   return false;
			else
			   return true;
		}
		
		if(!produtoEdicao.getCodigoDeBarras().equals(dto.getCodigoDeBarras())) {
			return true;
		}
		
		return false;
	}

	private void validarAlteracaoDePrecoDeCapaDoProdutoEdicao(final ProdutoEdicao produtoEdicao, final ProdutoEdicaoDTO produtoEdicaoDTO){
    	
    	if(produtoEdicao == null || produtoEdicao.getId() == null) {
    		return;
    	}
    	
    	final StatusLancamento statusLancamento = lService.obterStatusDoPrimeiroLancamentoDaEdicao(produtoEdicao.getId());
    	
    	final boolean permiteAlteracaoDePrecoSemValidacao =
    					Arrays.asList(StatusLancamento.PLANEJADO,
				    	    		StatusLancamento.CONFIRMADO,
				    	    		StatusLancamento.FURO,
				    	    		StatusLancamento.EM_BALANCEAMENTO,
				    	    		StatusLancamento.BALANCEADO,
				    	    		StatusLancamento.ESTUDO_FECHADO).contains(statusLancamento);
    	
    	if(!permiteAlteracaoDePrecoSemValidacao){
 
    		final boolean precoPrevistoDiferente = (produtoEdicao.getPrecoPrevisto().compareTo(produtoEdicaoDTO.getPrecoPrevisto())!=0);
    		
    		final boolean precoCustoDiferente = (produtoEdicao.getPrecoVenda().compareTo(produtoEdicaoDTO.getPrecoVenda())!=0);
    		
    		if(precoPrevistoDiferente || precoCustoDiferente ){
    			
    			throw new ValidacaoException(
    					new ValidacaoVO(TipoMensagem.WARNING,
    							"Não é permitido alterar os valores de Preço de Capa para produtos com status de [" + statusLancamento.getDescricao()+"]"));
    		}
    	}
    }
    
    private void validarRegimeRecolhimento(
    		final ProdutoEdicaoDTO dto, 
    		final List<Lancamento> lancamentos, 
    		final ProdutoEdicao produtoEdicao,
    		final boolean indDataRecolhimentoAlterada ) {
    	
    	for (Lancamento lancamento : lancamentos) {
    		
    		if (indDataRecolhimentoAlterada && this.isLancamentoBalanceadoRecolhimento(lancamento)) {
    			
    			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não é possível alterar o regime de recolhimento para lançamentos em recolhimento."));
    		}
    		
    		if (ModoTela.REDISTRIBUICAO.equals(dto.getModoTela()) && dto.isParcial()) {
    			
    			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Para redistribuição não é possível escolher o regime de recolhimento parcial."));
    		}
		}
    }
    
    private List<Lancamento> obterLancamentos(final ProdutoEdicaoDTO dto, final ProdutoEdicao produtoEdicao) {
        
        if (produtoEdicao.getLancamentos().isEmpty() || ModoTela.REDISTRIBUICAO.equals(dto.getModoTela())) {
            return new ArrayList<>();
        } else {
            return lService.obterLancamentosEdicao(produtoEdicao.getId());
        }
    }
    
    private Lancamento obterLancamento(final ProdutoEdicaoDTO dto, final ProdutoEdicao produtoEdicao, boolean obterUltimoLancamento) {
        
        if (produtoEdicao.getLancamentos().isEmpty() || ModoTela.REDISTRIBUICAO.equals(dto.getModoTela())) {
            return new Lancamento();
        } else {
        	if(obterUltimoLancamento) {
        		
        		return lService.obterUltimoLancamentoDaEdicao(produtoEdicao.getId(), distribuidorService.obterDataOperacaoDistribuidor());
        	} else {
        		
        		return lService.obterPrimeiroLancamentoDaEdicao(produtoEdicao.getId());
        	}
        }
    }
    
    private boolean isLancamentoBalanceadoRecolhimento(final Lancamento lancamento) {
    	
        return StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.equals(lancamento.getStatus()) 
        		|| StatusLancamento.BALANCEADO_RECOLHIMENTO.equals(lancamento.getStatus())
                || StatusLancamento.EM_RECOLHIMENTO.equals(lancamento.getStatus())
    			|| StatusLancamento.CANCELADO.equals(lancamento.getStatus())
                || StatusLancamento.RECOLHIDO.equals(lancamento.getStatus())
    			|| StatusLancamento.FECHADO.equals(lancamento.getStatus());

    }
    
    private void salvarLancamentoParcial(final ProdutoEdicaoDTO dto,final ProdutoEdicao produtoEdicao, final Usuario usuario,
            final boolean indNovoProdutoEdicao, final Lancamento lancamento) {
        
        if(indNovoProdutoEdicao){
        	
        	if (!TipoLancamento.LANCAMENTO.equals(dto.getTipoLancamento())) {
        		throw new ValidacaoException(TipoMensagem.WARNING, "O tipo de distribuição deve ser \"Lançamento\"");
        	}
            
            final LancamentoParcial lancamentoParcial =
                    this.criarNovoLancamentoParcial(dto, produtoEdicao);
            
            PeriodoLancamentoParcial periodo =
                    this.criarNovoPeriodoLancamentoParcial(lancamentoParcial);
            
            periodo = periodoLancamentoParcialRepository.merge(periodo);
            
            lancamento.setPeriodoLancamentoParcial(periodo);
            
            lancamentoRepository.merge(lancamento);
            
        } else {
            
            LancamentoParcial lancamentoParcial  = produtoEdicao.getLancamentoParcial();
            
            if (lancamentoParcial == null) {

            	if (!TipoLancamento.LANCAMENTO.equals(dto.getTipoLancamento())) {
            		throw new ValidacaoException(TipoMensagem.WARNING, "O tipo de distribuição deve ser \"Lançamento\"");
            	}
            	
                lancamentoParcial = this.criarNovoLancamentoParcial(dto, produtoEdicao);
                
                final PeriodoLancamentoParcial periodo = this.criarNovoPeriodoLancamentoParcial(lancamentoParcial);
                
                lancamento.setPeriodoLancamentoParcial(periodo);
                
                lancamentoRepository.merge(lancamento);
                
            } else {
                
                lancamentoParcial.setLancamentoInicial(dto.getDataLancamentoPrevisto());
                lancamentoParcial.setRecolhimentoFinal(dto.getDataRecolhimentoPrevisto());
               
                lancamentoParcialRepository.merge(lancamentoParcial);
            }
            
        }
    }
    
    private PeriodoLancamentoParcial criarNovoPeriodoLancamentoParcial(
            final LancamentoParcial lancamentoParcial) {
        PeriodoLancamentoParcial periodo = new PeriodoLancamentoParcial();
        periodo.setLancamentoParcial(lancamentoParcial);
        periodo.setTipo(TipoLancamentoParcial.FINAL);
        periodo.setStatus(StatusLancamentoParcial.PROJETADO);
        periodo.setNumeroPeriodo(1);
        
        periodo = periodoLancamentoParcialRepository.merge(periodo);
        return periodo;
    }
    
    private LancamentoParcial criarNovoLancamentoParcial(final ProdutoEdicaoDTO dto,
            final ProdutoEdicao produtoEdicao) {
        LancamentoParcial lancamentoParcial = new LancamentoParcial();
        lancamentoParcial.setProdutoEdicao(produtoEdicao);
        lancamentoParcial.setStatus(StatusLancamentoParcial.PROJETADO);
        lancamentoParcial.setLancamentoInicial(dto.getDataLancamento());
        lancamentoParcial.setRecolhimentoFinal(dto.getDataRecolhimentoReal());
        
        lancamentoParcial = lancamentoParcialRepository.merge(lancamentoParcial);
        return lancamentoParcial;
    }
	                                        /**
     * Aplica todas as regras de validação para o cadastro de uma Edição.
     * 
     * @param dto
     * @param produtoEdicao
     */
    private void validarProdutoEdicao(final ProdutoEdicaoDTO dto, final ProdutoEdicao produtoEdicao) {
        
		                                                                                /*
         * Regra: Os campos abaixos só podem ser alterados caso a Edição ainda
         * não tenha sido publicado pelo distribuidor: - Código da Edição; -
         * Número da Edição;
         * 
         * Alteração: "Data de Lançamento do Distribuidor" > "Data 'de hoje'"
         */
        
        if(produtoEdicao.getId()!= null){
            
            if (produtoEdicaoRepository.isProdutoEdicaoJaPublicada(produtoEdicao.getId())) {
                
                
                if (produtoEdicao.getProduto().getCodigo()!=null && !produtoEdicao.getProduto().getCodigo().equals(dto.getCodigoProduto())) {
                    throw new ValidacaoException(TipoMensagem.ERROR,
                            "Não é permitido alterar o código de uma Edição já publicada!");
                }
                
                // Campo: Número do ProdutoEdicao:
                if (!produtoEdicao.getNumeroEdicao().equals(dto.getNumeroEdicao())) {
                    throw new ValidacaoException(TipoMensagem.ERROR,
                            "Não é permitido alterar o número de uma Edição já publicada!");
                }
            }
            
        }
        
        /*
         * Regra: Se não existir nenhuma edição associada ao produto, salvar n.
         * 1
         */
        if (!produtoEdicaoRepository.hasProdutoEdicao(produtoEdicao.getProduto())) {
            produtoEdicao.setNumeroEdicao(Long.valueOf(1));
        }
        
        /*
         * Regra: Não deve existir dois número de edição para o mesmo grupo de
         * Edições:
         */
        /* VALIDAÇÃO FEITA POR EDICAO + LCTO */
        //if (this.produtoEdicaoRepository.isNumeroEdicaoCadastrada(
        //dto.getCodigoProduto(), dto.getNumeroEdicao(), produtoEdicao.getId())) {
        // throw new ValidacaoException(TipoMensagem.ERROR,
        // "Este número de edição já esta cadastrada para outra Edição!");
        //}
        
        
        
        /* Regra: Não deve existir duas Edições com o mesmo código de barra. */
        // Nota: Conforme conversado com o Cesar e Paulo Bacherini em
        // 05/11/2012, dois produtos diferentes podem sim ter o mesmo código de
        // barras
		                                                                                /*
         * List<ProdutoEdicao> lstPeCodBarra =
         * this.produtoEdicaoRepository.obterProdutoEdicaoPorCodigoDeBarra(
         * dto.getCodigoDeBarras(), produtoEdicao.getId()); if (lstPeCodBarra !=
         * null && !lstPeCodBarra.isEmpty()) { // Nota: Caso exista, mas não se
         * trate do MESMO produto edição for (ProdutoEdicao
         * produtoEdicaoPorCodigoBarra : lstPeCodBarra) {
         * 
         * if (produtoEdicaoPorCodigoBarra.getId() != produtoEdicao.getId()) {
         * 
         * ProdutoEdicao peCodBarra = lstPeCodBarra.get(0); StringBuilder msg =
         * new StringBuilder(); msg.append("O Produto '");
         * msg.append(peCodBarra.getProduto().getNome());
         * msg.append("' - Edição º"); msg.append(peCodBarra.getNumeroEdicao());
         * msg.append(" já esta cadastrado com este código de barra!");
         * 
         * throw new ValidacaoException(TipoMensagem.ERROR, msg.toString());
         * 
         * } } }
         */
    }
    
	                                        /**
     * Salva ou atualiza um ProdutoEdicao.<br>
     * . Os campos permitidos no cenário de gravação ou alteração de um
     * ProdutoEdição criado por um Distribuidor:
     * <ul>
     * <li>Imagem da Capa;</li>
     * <li>Código do ProdutoEdição;</li>
     * <li>Nome Comercial do ProdutoEdição;</li>
     * <li>Número da Edição;</li>
     * <li>Pacote Padrão;</li>
     * <li>Tipo de Lançamento;</li>
     * <li>Preço da Capa (Previsto);</li>
     * <li>Data de Lançamento (Previsto);</li>
     * <li>Reparte Previsto;</li>
     * <li>Reparte Promocional;</li>
     * <li>Categoria;</li>
     * <li>Código de Barras;</li>
     * <li>Código de Barras Corporativo;</li>
     * <li>Desconto;</li>
     * <li>Chamada da Capa;</li>
     * <li>Regime de Recolhimento (Parcial);</li>
     * <li>Brinde;</li>
     * <li>Boletim Informativo;</li>
     * </ul>
     * <br>
     * Os campos permitidos no cenário de alteração de um ProdutoEdição vindo da
     * Interface:
     * <ul>
     * <li>Imagem da Capa;</li>
     * <li>Preço da Capa (Real);</li>
     * <li>Código de Barras;</li>
     * <li>Chamada da Capa;</li>
     * <li>Brinde;</li>
     * <li>Peso;</li>
     * </ul>
     * 
     * @param dto
     * @param produtoEdicao
     */
    
    private ProdutoEdicao salvarProdutoEdicao(final ProdutoEdicaoDTO dto, final ProdutoEdicao produtoEdicao) {
       
        // 01) Validações:
        this.validarProdutoEdicao(dto, produtoEdicao);
        
        // 02) Campos a serem persistidos e/ou alterados:
        
        final BigInteger repartePrevisto = (dto.getRepartePrevisto() == null) ? BigInteger.ZERO : dto.getRepartePrevisto();
        
        final BigInteger repartePromocional = (dto.getRepartePromocional() == null) ? BigInteger.ZERO : dto.getRepartePromocional();
        produtoEdicao.setPacotePadrao(dto.getPacotePadrao());
        
        // Reparte:
        produtoEdicao.setReparteDistribuido(repartePrevisto.add(repartePromocional));
        
        // Texto boletim informativo:
        produtoEdicao.setBoletimInformativo(dto.getBoletimInformativo());
              
        if ((produtoEdicao.getOrigem().equals(br.com.abril.nds.model.Origem.MANUAL))) {
            // Campos exclusivos para o Distribuidor::
            
            // Identificação:
            produtoEdicao.setNumeroEdicao(dto.getNumeroEdicao());
            produtoEdicao.setCaracteristicaProduto(dto.getCaracteristicaProduto());
            produtoEdicao.setPrecoPrevisto(dto.getPrecoPrevisto());
            
            produtoEdicao.setGrupoProduto(dto.getGrupoProduto());
            
            // Características do lançamento:
            // TODO: !!!colocar o select da categoria aqui!!!
            produtoEdicao.setCodigoDeBarraCorporativo(dto.getCodigoDeBarrasCorporativo());
            
            // Outros:
            
            // Característica Física:
            produtoEdicao.setPeso(dto.getPeso());
            final Dimensao dimEdicao = new Dimensao();
            dimEdicao.setLargura(dto.getLargura());
            dimEdicao.setComprimento(dto.getComprimento());
            dimEdicao.setEspessura(dto.getEspessura());
            produtoEdicao.setDimensao(dimEdicao);
            
            //Desconto Fornecedor x Distribuidor
            produtoEdicao.setDescricaoDesconto(dto.getDescricaoDesconto());
            produtoEdicao.setDesconto(dto.getDesconto());
            
            // Segmentação
            final SegmentacaoProduto segm = produtoEdicao.getSegmentacao()!=null?produtoEdicao.getSegmentacao():new SegmentacaoProduto();
            segm.setClasseSocial(dto.getClasseSocial());
            segm.setSexo(dto.getSexo());
            segm.setFaixaEtaria(dto.getFaixaEtaria());
            segm.setTemaPrincipal(dto.getTemaPrincipal());
            segm.setTemaSecundario(dto.getTemaSecundario());
            
            produtoEdicao.setSegmentacao(segm);
        }
        
        produtoEdicao.setPeb(dto.getPeb());
        produtoEdicao.setNomeComercial(dto.getNomeComercialProduto());
        
        // Regime de Recolhimento;
        if(produtoEdicao.isParcial() != dto.isParcial()) {
        	
        	Lancamento l = lancamentoRepository.obterPrimeiroLancamentoDaEdicao(dto.getId());
        	
        	if(Arrays.asList(StatusLancamento.BALANCEADO_RECOLHIMENTO, StatusLancamento.EM_RECOLHIMENTO, StatusLancamento.RECOLHIDO, StatusLancamento.FECHADO).contains(l.getStatus())) {
        		
        		throw new ValidacaoException(TipoMensagem.WARNING, "Não é possível a alteração do Regime de Recolhimento para esta edição.");
        	}
        	
        	if(!dto.isParcial()) {
        		
        		throw new ValidacaoException(TipoMensagem.WARNING, "Não é possível alterar um lançamento parcial para normal.");
        	}
        	
        	produtoEdicao.setParcial(dto.isParcial());	
        }
        
        // Campos editáveis, independente da Origem
        produtoEdicao.setTipoClassificacaoProduto(dto.getTipoClassificacaoProduto() == null || dto.getTipoClassificacaoProduto().getId() == null ? null : dto.getTipoClassificacaoProduto());
        produtoEdicao.setPrecoVenda(dto.getPrecoVenda() == null ? dto.getPrecoPrevisto() : dto.getPrecoVenda()); // View:
        // Preço
        // Capa
        // -
        // Real;
        produtoEdicao.setCodigoDeBarras(dto.getCodigoDeBarras());
        produtoEdicao.setChamadaCapa(dto.getChamadaCapa());
        produtoEdicao.setPeso(dto.getPeso()!=null?dto.getPeso():Long.valueOf("0"));
        produtoEdicao.setPossuiBrinde(false);
        produtoEdicao.setBrinde(null);
        
        if(dto.getIdBrinde()!=null && dto.isPossuiBrinde()){
            final Brinde brinde = brindeRepository.buscarPorId(dto.getIdBrinde());
            if (brinde!=null){
                produtoEdicao.setPossuiBrinde(true);
                produtoEdicao.setBrinde(brinde);
            }
        }
        
        if (produtoEdicao.getId() == null) {
            // Salvar:
            
            final Produto produto = produtoRepository.obterProdutoPorCodigoProdin(dto.getCodigoProduto());
            
            produtoEdicao.setProduto(produto);
            
            produtoEdicaoRepository.adicionar(produtoEdicao);
        } else {
            // Atualizar:
            produtoEdicaoRepository.alterar(produtoEdicao);
        }
        
        return produtoEdicao;
    }
    
    /**
* Salva o lançamento, quando nao houve mudanca de datas
* salvar box Reparte: reparte_previsto,exp venda e promocional
* @param lancamento
* @param dto
* @param produtoEdicao
* @param usuario
*/
private Lancamento salvarLancamentoReparte(Lancamento lancamento, final ProdutoEdicaoDTO dto, final ProdutoEdicao produtoEdicao, final Usuario usuario) {

final BigInteger repartePromocional = dto.getRepartePromocional() == null ? BigInteger.ZERO : dto.getRepartePromocional();

if ( dto.getRepartePrevisto() != null) {
	lancamento.setReparte(dto.getRepartePrevisto());
	}

	lancamento.setRepartePromocional(repartePromocional);
	lancamento.setUsuario(usuario);
	
	lancamento = lancamentoRepository.merge(lancamento);

	produtoEdicaoRepository.alterar(produtoEdicao);
	
	return lancamento;
}
    
	                                        /**
     * Salva o lançamento.
     * 
     * @param lancamento
     * @param dto
     * @param produtoEdicao
     * @param usuario
     */
    private Lancamento salvarLancamento(Lancamento lancamento, final ProdutoEdicaoDTO dto, final ProdutoEdicao produtoEdicao, final Usuario usuario) {
       
        lancamento.setNumeroLancamento(dto.getNumeroLancamento());

        if(dto.getDataLancamento()!=null){
            lancamento.setDataLancamentoDistribuidor(dto.getDataLancamento());
        }
        lancamento.setDataLancamentoPrevista(dto.getDataLancamentoPrevisto());
        lancamento.setDataRecolhimentoPrevista(dto.getDataRecolhimentoPrevisto());
        
        if(dto.getDataRecolhimentoDistribuidor()!=null){
            lancamento.setDataRecolhimentoDistribuidor(dto.getDataRecolhimentoDistribuidor());
        }
       ;
        final BigInteger repartePromocional = dto.getRepartePromocional() == null ? BigInteger.ZERO : dto.getRepartePromocional();
        
        if (lancamento.getId() == null || dto.getRepartePrevisto() != null) {
            lancamento.setReparte(dto.getRepartePrevisto());
        }
        
        final boolean tipoDeLancamentoRedistribuicao = TipoLancamento.REDISTRIBUICAO.equals(dto.getTipoLancamento());
        
        final boolean produtoEdicaoSemLancamento = (produtoEdicao.getLancamentos() == null || produtoEdicao.getLancamentos().isEmpty());  
        
        if(tipoDeLancamentoRedistribuicao && produtoEdicaoSemLancamento){
        	
        	throw new ValidacaoException(TipoMensagem.WARNING, "O tipo de distribuição deve ser \"Lançamento\"");
        }
        
        lancamento.setTipoLancamento(dto.getTipoLancamento());
        lancamento.setRepartePromocional(repartePromocional);
        lancamento.setUsuario(usuario);
        
        if (lancamento.getId() == null) {
            
            if (lancamento.getDataLancamentoDistribuidor() == null) {
                lancamento.setDataLancamentoDistribuidor(dto.getDataLancamentoPrevisto());
            }
            
            if (lancamento.getDataRecolhimentoDistribuidor() == null) {
                lancamento.setDataRecolhimentoDistribuidor(dto.getDataRecolhimentoPrevisto());
            }
            
            lancamento.setStatus(StatusLancamento.PLANEJADO);
            
            final Date dtSysdate = new Date();
            lancamento.setDataCriacao(dtSysdate);
            lancamento.setDataStatus(dtSysdate);
            
            lancamento.setProdutoEdicao(produtoEdicao);
            
            produtoEdicao.getLancamentos().add(lancamento);
        }
        
        lancamento = lancamentoRepository.merge(lancamento);
        
        produtoEdicaoRepository.alterar(produtoEdicao);
        
        return lancamento;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, String> isProdutoEdicaoValidoParaRemocao(final Long idProdutoEdicao) throws Exception {
        
        final Map<String, String> validacaoMap = new HashMap<String, String>();
        
        final ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
        
        if (produtoEdicao == null) {
            
            validacaoMap.put("edicaoInexistente", "Por favor, selecione uma Edição existente!");
        } else {
        
            if (Origem.INTERFACE.equals(produtoEdicao.getOrigem())) {
            
            	validacaoMap.put("origemInterface", "Não é possível excluir edição de origem interface.");
            }

            final Set<Lancamento> lancamentos = produtoEdicao.getLancamentos();
            
            for (final Lancamento lancamento : lancamentos) {
                
                if (!(lancamento.getStatus().equals(StatusLancamento.PLANEJADO)
                        || lancamento.getStatus().equals(StatusLancamento.CONFIRMADO)
                        || lancamento.getStatus().equals(StatusLancamento.EM_BALANCEAMENTO)
                        || lancamento.getStatus().equals(StatusLancamento.FURO)) ) {
                    
                    validacaoMap.put("edicaoExpedida",
                            "Produto balanceado, é necessário realizar o furo da publicação para realizar a exclusão!");
                    
                    return validacaoMap;
                }
                
                List<ItemNotaFiscalEntrada> itens = this.itemNotaFiscalEntradaRepository.obterItensPorProdutoEdicao(produtoEdicao.getId());
                
                boolean produtoComNota = itens != null && !itens.isEmpty(); 
                
                if(produtoComNota) {
                    
                    validacaoMap.put("edicaoComNota", "Já existe uma nota fiscal recebida para esta edição e não pode ser excluida!");
                    
                    return validacaoMap;
                }
                
                if(lancamento.getStatus().equals(StatusLancamento.BALANCEADO)) {
                    validacaoMap.put("edicaoEmBalanceamentoBalanceada",
                            "Esta Edição possui lancamento já balanceado, é necessário realizar o Furo da Edição!");
                }
                
                if(lancamento.getEstudo() != null) {
                    validacaoMap.put("edicaoPossuiEstudo", "Esta edição já possui estudo!");
                }
                
                if(produtoEdicao.getEstoqueProduto() != null) {
                    
                    validacaoMap.put("edicaoPossuiEstoque",
                            "Esta edição possui produtos em estoque e não pode ser excluida!");
                    
                    return validacaoMap;
                }
            }
        }
        
        return validacaoMap;
        
    }
    
    @Override
    @Transactional(readOnly = false)
    public void excluirProdutoEdicao(final Long idProdutoEdicao) throws UniqueConstraintViolationException {
        
        final ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
        if (produtoEdicao == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Por favor, selecione uma Edição existente!");
        }
        
        final Set<Lancamento> lancamentos = produtoEdicao.getLancamentos();
        
        for (final Lancamento lancamento : lancamentos) {
            
            if(lancamento.getRecebimentos() != null && !lancamento.getRecebimentos().isEmpty() ) {
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Esta edição possui nota emitida e não pode ser excluida!");
            }
            
            
            if (!(lancamento.getStatus().equals(StatusLancamento.PLANEJADO)
                    || lancamento.getStatus().equals(StatusLancamento.CONFIRMADO)
                    || lancamento.getStatus().equals(StatusLancamento.EM_BALANCEAMENTO)
                    || lancamento.getStatus().equals(StatusLancamento.FURO)) ) {
                
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Produto balanceado, é necessário realizar o furo da publicação para realizar a exclusão!");
                
            }
            
            if(lancamento.getStatus().equals(StatusLancamento.BALANCEADO)) {
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Esta edição possui lancamento já balanceado, é necessário realizar o Furo da Edição!");
            }
            
            if(lancamento.getEstudo() != null) {
                throw new ValidacaoException(TipoMensagem.WARNING, "Esta edição já possui estudo!");
            }
            
            if(produtoEdicao.getEstoqueProduto() != null) {
                
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Esta edição possui produtos em estoque e não pode ser excluida!");
            }
        }
        
        try {
            
            for (final Lancamento lancamento : lancamentos){
                if (Origem.MANUAL.equals(produtoEdicao.getOrigem())) {
                    
                    lancamentoRepository.remover(lancamento);
                } else {
                    
                    lancamento.setStatus(StatusLancamento.CANCELADO);
                    
                    final Usuario usuario = usuarioService.getUsuarioLogado();
                    
                    lancamento.setUsuario(usuario);
                    
                    if (lancamento.getPeriodoLancamentoParcial() != null) {
                        
                        lancamento.getPeriodoLancamentoParcial().setStatus(
                                StatusLancamentoParcial.CANCELADO);
                        periodoLancamentoParcialRepository.alterar(lancamento
                                .getPeriodoLancamentoParcial());
                        
                        lancamento.getPeriodoLancamentoParcial()
                        .getLancamentoParcial()
                        .setStatus(StatusLancamentoParcial.CANCELADO);
                        lancamentoParcialRepository.alterar(lancamento
                                .getPeriodoLancamentoParcial()
                                .getLancamentoParcial());
                    }
                    
                    lancamentoRepository.alterar(lancamento);
                }
            }
            
            if (Origem.MANUAL.equals(produtoEdicao.getOrigem())) {
                if (produtoEdicao.getLancamentoParcial() != null) {
                    lancamentoParcialRepository.remover(produtoEdicao
                            .getLancamentoParcial());
                }
                produtoEdicaoRepository.remover(produtoEdicao);
                
            } else {
                
                produtoEdicao.setAtivo(false);
                produtoEdicaoRepository.alterar(produtoEdicao);
            }
            
        } catch (final DataIntegrityViolationException e) {
            LOGGER.debug(e.getMessage(), e);
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "Esta edição não pode ser excluida por estar associada em outras partes do sistema!");
            
        } catch (final Exception e) {
            LOGGER.error("Ocorreu um erro ao tentar excluir a edição!", e);
            throw new ValidacaoException(TipoMensagem.ERROR, "Ocorreu um erro ao tentar excluir a edição!");
        }
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<ProdutoEdicao> buscarProdutoPorCodigoBarras(final String codigoBarras){
        
        return produtoEdicaoRepository.obterProdutoEdicaoPorCodigoBarra(codigoBarras);
    }
    
    private void validarStatusProdutoEdicaoRedistribuicao(final String status) {
        
        if( StatusLancamento.PLANEJADO.name().equals(status) 		||
                StatusLancamento.CONFIRMADO.name().equals(status) 		||
                StatusLancamento.EM_BALANCEAMENTO.name().equals(status) ||
                StatusLancamento.BALANCEADO.name().equals(status) 		||
                StatusLancamento.FURO.name().equals(status) 			||
                StatusLancamento.EXPEDIDO.name().equals(status) ) {
            return;
        }
        
        throw new ValidacaoException(TipoMensagem.WARNING,
                "Situação do lançamento não permite cadastrar nova redistribuição!");
        
    }
    
    @Transactional(readOnly = true)
    @Override
    public ProdutoEdicaoDTO obterProdutoEdicaoDTO(final String codigoProduto, final Long idProdutoEdicao, final boolean redistribuicao,
            final String situacaoProdutoEdicao) {
        
        
        final Produto produto = pService.obterProdutoPorCodigo(codigoProduto);
        
        final ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
        
        dto.setNomeProduto(produto.getNome());
        dto.setCodigoProduto(produto.getCodigo());
        dto.setFase(produto.getFase());
        dto.setPacotePadrao(produto.getPacotePadrao());
        dto.setPeso(produto.getPeso());
        dto.setFormaComercializacao(produto.getFormaComercializacao());
        
        String nomeFornecedor = "";
        if (produto.getFornecedor() != null
                && produto.getFornecedor().getJuridica() != null) {
            nomeFornecedor = produto.getFornecedor().getJuridica().getNomeFantasia();
            dto.setIdFornecedor(produto.getFornecedor().getId());
        }
        
        dto.setNomeFornecedor(nomeFornecedor);
        
        if (idProdutoEdicao != null) {
            
            final ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(idProdutoEdicao, false);
            
            dto.setId(idProdutoEdicao);
            dto.setNomeComercialProduto(produtoEdicao.getNomeComercial());
            dto.setCaracteristicaProduto(produtoEdicao.getCaracteristicaProduto());
            dto.setGrupoProduto(produtoEdicao.getGrupoProduto()!=null?produtoEdicao.getGrupoProduto():produto.getTipoProduto()!=null?produto.getTipoProduto().getGrupoProduto():null);
            dto.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
            dto.setPacotePadrao(produtoEdicao.getPacotePadrao());
            dto.setPrecoPrevisto(produtoEdicao.getPrecoPrevisto());
            dto.setTipoSegmentoProdutoId(produtoEdicao.getProduto().getTipoSegmentoProduto() == null ? null : produtoEdicao.getProduto().getTipoSegmentoProduto().getId());
            
            dto.setClassificacao(produtoEdicao.getTipoClassificacaoProduto() == null ? null : produtoEdicao.getTipoClassificacaoProduto().getId().toString());
            
            final BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
            dto.setPrecoVenda(precoVenda);
            dto.setExpectativaVenda(produtoEdicao.getExpectativaVenda());
            dto.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
            dto.setCodigoDeBarrasCorporativo(produtoEdicao.getCodigoDeBarraCorporativo());
            dto.setChamadaCapa(produtoEdicao.getChamadaCapa());
            dto.setParcial(produtoEdicao.isParcial());
            dto.setPossuiBrinde(produtoEdicao.isPossuiBrinde());
            
            dto.setPeso(produtoEdicao.getPeso());
            dto.setBoletimInformativo(produtoEdicao.getBoletimInformativo());
            dto.setOrigemInterface(produtoEdicao.getOrigem().equals(br.com.abril.nds.model.Origem.INTERFACE));
            dto.setPeb(produtoEdicao.getPeb());
            dto.setEditor(produtoEdicao.getProduto().getEditor() != null ? produtoEdicao.getProduto().getEditor().getPessoaJuridica().getNome() : "");
            if (produtoEdicao.getBrinde() !=null) {
                dto.setDescricaoBrinde(produtoEdicao.getBrinde().getDescricao());
                dto.setIdBrinde(produtoEdicao.getBrinde().getId());
            }
            carregarInformacaoDimensaoProduto(dto, produtoEdicao);
            
            carregarInformacaoDesconto(produto, dto, produtoEdicao);
            
            carregarInformacaoLancamentos(dto, produtoEdicao);
            
            carregarSegmentacaoProdutoEdicao(dto, produtoEdicao);
            
            /*
             * A situacao da edicao vem da query principal devido a regra de furo
             */
            dto.setStatusSituacao(situacaoProdutoEdicao);
            
            dto.setLancamentoExcluido(dto.getDataLancamento() == null);
            
        } else {
            
            obterProdutoEdicaoDTOManual(codigoProduto, produto, dto);
        }
        
        if (redistribuicao) {
            
            if (dto.isParcial()) {
                
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Regime de recolhimento parcial não permite cadastro de redistribuição!");
            }
            
            
            validarStatusProdutoEdicaoRedistribuicao(dto.getStatusSituacao());
            
            dto.setTipoLancamento(TipoLancamento.REDISTRIBUICAO);
            dto.setRepartePrevisto(null);
            dto.setRepartePromocional(null);
            dto.setDataLancamento(null);
            dto.setDataLancamentoPrevisto(null);
            dto.setNumeroLancamento(this.obterNumeroLancamento(idProdutoEdicao, null));
            dto.setModoTela(ModoTela.REDISTRIBUICAO);
            
        } else if (idProdutoEdicao != null) {
            
            dto.setModoTela(ModoTela.EDICAO);
            
        } else {
            
            dto.setTipoLancamento(TipoLancamento.LANCAMENTO);
            dto.setNumeroLancamento(this.obterNumeroLancamento(idProdutoEdicao, null));
            dto.setModoTela(ModoTela.NOVO);
        }
        
        if (dto.getTipoSegmentoProdutoId() == null && produto.getTipoSegmentoProduto() != null){
            dto.setTipoSegmentoProdutoId(produto.getTipoSegmentoProduto().getId());
        }
        
        return dto;
    }
    
    @Override
    @Transactional
    public Integer obterNumeroLancamento(final Long idProdutoEdicao, final Long idPeriodo) {
        
        Integer ultimoNumeroLancamento = null;
        
        if (idProdutoEdicao != null) {
            
            ultimoNumeroLancamento =
                    lancamentoRepository.obterUltimoNumeroLancamento(
                            idProdutoEdicao, idPeriodo);
        }
        
        return (ultimoNumeroLancamento != null ? ultimoNumeroLancamento + 1 : 1);
    }
    
    private void carregarInformacaoDimensaoProduto(final ProdutoEdicaoDTO dto, final ProdutoEdicao produtoEdicao) {
        
        final Dimensao dimEdicao = produtoEdicao.getDimensao();
        
        if (dimEdicao == null) {
            dto.setComprimento(0);
            dto.setEspessura(0);
            dto.setLargura(0);
        } else {
            dto.setComprimento(dimEdicao.getComprimento());
            dto.setEspessura(dimEdicao.getEspessura());
            dto.setLargura(dimEdicao.getLargura());
        }
    }
    
    private void carregarInformacaoDesconto(final Produto produto,final ProdutoEdicaoDTO dto, final ProdutoEdicao produtoEdicao) {
        
        dto.setDesconto(BigDecimal.ZERO);
        
        if(!produtoEdicao.getOrigem().equals(Origem.INTERFACE)){
            
            if(produtoEdicao.getDesconto() != null ){
                
                dto.setDesconto(produtoEdicao.getDesconto());
                
                if(produtoEdicao.getDescricaoDesconto() != null && !"".equals(produtoEdicao.getDescricaoDesconto())){
                    dto.setDescricaoDesconto(produtoEdicao.getDescricaoDesconto());
                }
            }
            
        }else{
        	
      //  	DescontoLogistica des = produtoEdicao.getDescontoLogistica();
        	
        	if(produtoEdicao.getDescontoLogistica()!=null ){
                dto.setDesconto( produtoEdicao.getDescontoLogistica().getPercentualDesconto() );
                dto.setDescricaoDesconto(produtoEdicao.getDescontoLogistica().getDescricao());
            }
        	else 
            if(produto.getDescontoLogistica()!=null ){
                dto.setDesconto( produto.getDescontoLogistica().getPercentualDesconto() );
                dto.setDescricaoDesconto(produto.getDescontoLogistica().getDescricao());
            }
        }
    }
    
    private void carregarInformacaoLancamentos(final ProdutoEdicaoDTO dto,final ProdutoEdicao produtoEdicao) {
        
        final Lancamento uLancamento = lService.obterPrimeiroLancamentoDaEdicao(produtoEdicao.getId());//TODO
        
        TipoLancamento tipoLancamento = lService.isRedistribuicao(dto.getCodigoProduto(), produtoEdicao.getNumeroEdicao()) == true ? TipoLancamento.REDISTRIBUICAO : TipoLancamento.LANCAMENTO;
        
        if (uLancamento != null) {
            
            dto.setNumeroLancamento(uLancamento.getNumeroLancamento());
            
            dto.setTipoLancamento(tipoLancamento);
            
            Date dataLancamento = null;
            
            if(uLancamento.getPeriodoLancamentoParcial()!= null){
                
                final LancamentoParcial lancamentoParcial  = lancamentoParcialRepository.obterLancamentoPorProdutoEdicao(produtoEdicao.getId());
                
                if(lancamentoParcial!= null){
                    
                    dto.setDataLancamentoPrevisto(lancamentoParcial.getLancamentoInicial());
                    dto.setDataRecolhimentoPrevisto(lancamentoParcial.getRecolhimentoFinal());
                }else{
                    
                    dto.setDataLancamentoPrevisto(uLancamento.getDataLancamentoPrevista());
                    dto.setDataRecolhimentoPrevisto(uLancamento.getDataRecolhimentoPrevista());
                }
                
                final PeriodoLancamentoParcial primeiroPeriodo = periodoLancamentoParcialRepository.obterPrimeiroLancamentoParcial(produtoEdicao.getId());

                dataLancamento = uLancamento.getDataLancamentoDistribuidor();
                
                if(primeiroPeriodo != null) {

                	Lancamento lancamento = primeiroPeriodo.getLancamentoPeriodoParcial();
                    
                    dataLancamento = lancamento != null ? lancamento.getDataLancamentoDistribuidor() : dataLancamento;
                }
                
                final PeriodoLancamentoParcial ultimoPeriodo = periodoLancamentoParcialRepository.obterUltimoLancamentoParcial(produtoEdicao.getId());
                
                if(ultimoPeriodo == null) {
                	
                	LOGGER.error(String.format("Nenhum Lançamento do tipo \"LANCAMENTO\" foi encontrado. %s", produtoEdicao.toString()));
                	throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum Lançamento do tipo \"LANCAMENTO\" foi encontrado para a edição.");
                }
                
                Lancamento lancamento = ultimoPeriodo.getLancamentoPeriodoParcial();
                if(ultimoPeriodo != null && lancamento != null) {
                    dto.setDataRecolhimentoReal(lancamento.getDataRecolhimentoDistribuidor());
                } else {
                    dto.setDataRecolhimentoReal(uLancamento.getDataRecolhimentoDistribuidor());
                }
                
                
            }else{
                
                dataLancamento = uLancamento.getDataLancamentoDistribuidor();
                dto.setDataLancamentoPrevisto(uLancamento.getDataLancamentoPrevista());
                
                dto.setDataRecolhimentoPrevisto(uLancamento.getDataRecolhimentoPrevista());
                dto.setDataRecolhimentoReal(uLancamento.getDataRecolhimentoDistribuidor());
            }
            
            dto.setDataLancamento(DateUtil.parseDataPTBR(dataCabalistica).compareTo(dataLancamento) == 0 ? null : dataLancamento);
            dto.setRepartePrevisto(uLancamento.getReparte());
            dto.setRepartePromocional(uLancamento.getRepartePromocional());
            
            final int semanaRecolhimento = SemanaUtil.obterNumeroSemana(uLancamento.getDataRecolhimentoDistribuidor(), distribuidorService.inicioSemanaRecolhimento().getCodigoDiaSemana());
            
            dto.setSemanaRecolhimento(semanaRecolhimento);
        }
    }
    
    private void carregarSegmentacaoProdutoEdicao(final ProdutoEdicaoDTO dto,final ProdutoEdicao produtoEdicao) {
        
        SegmentacaoProduto segm = produtoEdicao.getSegmentacao();
        
        if(segm!=null){
            
            dto.setClasseSocial(segm.getClasseSocial());
            dto.setSexo(segm.getSexo());
            dto.setFaixaEtaria(segm.getFaixaEtaria());
            dto.setTemaPrincipal(segm.getTemaPrincipal());
            dto.setTemaSecundario(segm.getTemaSecundario());
        }
        else{
            
            segm = produtoEdicao.getProduto().getSegmentacao();
            
            if(segm!=null){
                dto.setClasseSocial(segm.getClasseSocial());
                dto.setSexo(segm.getSexo());
                dto.setFaixaEtaria(segm.getFaixaEtaria());
                dto.setTemaPrincipal(segm.getTemaPrincipal());
                dto.setTemaSecundario(segm.getTemaSecundario());
            }
        }
    }
    
    private void obterProdutoEdicaoDTOManual(final String codigoProduto,final Produto produto, final ProdutoEdicaoDTO dto) {
        
        // Edição criada pelo Distribuidor:
        dto.setOrigemInterface(false);
        
        dto.setPeb(produto.getPeb());
        
        if (produto.getOrigem().equals(Origem.INTERFACE)
                && produto.getDescontoLogistica()!= null){
            
            dto.setDesconto(produto.getDescontoLogistica().getPercentualDesconto());
            dto.setDescricaoDesconto(produto.getDescontoLogistica().getDescricao());
        }
        else{
            
            dto.setDesconto(produto.getDesconto());
            dto.setDescricaoDesconto(produto.getDescricaoDesconto());
        }
        
        final Long ultimaEdicao = produtoEdicaoRepository.obterUltimoNumeroEdicao(produto.getId());
        
        if (ultimaEdicao == null) {
            dto.setNumeroEdicao(1L);
        } else {
            dto.setNumeroEdicao(ultimaEdicao + 1);
        }
        
        dto.setGrupoProduto(produto.getTipoProduto()!=null?produto.getTipoProduto().getGrupoProduto():null);
        
    }
    
    @Override
    @Transactional
    public ProdutoEdicao buscarPorID(final Long idProdutoEdicao) {
        return produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<EdicoesProdutosDTO> obterHistoricoEdicoes(final FiltroHistogramaVendas filtro) {
        
    	List<EdicoesProdutosDTO> historicoEdicoes = produtoEdicaoRepository.obterHistoricoEdicoes(filtro); 
    	
    	for (EdicoesProdutosDTO edicoesProdutosDTO : historicoEdicoes) {
			if(edicoesProdutosDTO.getStatus().equalsIgnoreCase("FECHADO") 
					&& (edicoesProdutosDTO.getVenda() == null || edicoesProdutosDTO.getVenda().compareTo(BigInteger.ZERO)<=0)){
				edicoesProdutosDTO.setVenda(estoqueProdutoService.obterVendaBaseadoNoEstoque(edicoesProdutosDTO.getIdProdutoEdicao().longValue()));
			}
		}
    	
        return historicoEdicoes;
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<AnaliseHistogramaDTO> obterBaseEstudoHistogramaPorFaixaVenda(final FiltroHistogramaVendas filtro,
            String codigoProduto,final String[] faixasVenda, final String[] edicoes){
        
        if (codigoProduto != null){
            
            codigoProduto = Util.padLeft(codigoProduto, "0", 8);
        }
        
        final List<AnaliseHistogramaDTO> list = new ArrayList<AnaliseHistogramaDTO>();
        
        final String[] newFaixasVenda = new String[faixasVenda.length];
        
        
        System.arraycopy(faixasVenda, 0, newFaixasVenda, 0, faixasVenda.length);

        final List<ProdutoEdicaoDTO> lstPrDTO = new ArrayList<ProdutoEdicaoDTO>();
        for (final String strEd : edicoes){
            
            final ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
            dto.setCodigoProduto(codigoProduto);
            dto.setNumeroEdicao(Long.valueOf(strEd));
            lstPrDTO.add(dto);
        }
        
        BigDecimal reparteTotal = BigDecimal.ZERO;
        BigDecimal vendaTotal = BigDecimal.ZERO;
        BigDecimal partReparte = BigDecimal.ZERO;
        BigDecimal partVenda = BigDecimal.ZERO;
        BigInteger qtdeCotas = BigInteger.ZERO;
        BigInteger cotasEsmagadas = BigInteger.ZERO;
        BigDecimal vendaEsmagadas = BigDecimal.ZERO;
        BigDecimal qtdeCotasSemVenda = BigDecimal.ZERO;
        
        final StringBuilder strCotas = new StringBuilder();
        
        for (int i = 0; i < newFaixasVenda.length; i++) {
            final String[] faixa = newFaixasVenda[i].split("-");
            final AnaliseHistogramaDTO obj =
                    produtoEdicaoRepository.obterBaseEstudoHistogramaPorFaixaVenda(
                            filtro, codigoProduto, Integer.parseInt(faixa[0]), Integer.parseInt(faixa[1]), edicoes);
            
            final List<Integer> cotas = new ArrayList<Integer>();
            if (!obj.getIdCotaStr().isEmpty()){
                
                for (final String strCota : obj.getIdCotaStr().split(",")){
                    cotas.add(Integer.valueOf(strCota));
                }
                
                strCotas.append(strCotas.length() == 0 ? obj.getIdCotaStr() : "," + obj.getIdCotaStr());
            }
            
            if (!cotas.isEmpty()){
                
            	Collections.sort(lstPrDTO);
            	
                final List<AnaliseHistoricoDTO> historico =
                        cotaService.buscarHistoricoCotas(lstPrDTO, cotas, null, null);
                
                BigDecimal vendaMedia = BigDecimal.ZERO, reparteMedio = BigDecimal.ZERO;
                
                for (final AnaliseHistoricoDTO anaDTO : historico){
                    
                    if (anaDTO.getVendaMedia() != null){
                        
                        vendaMedia = vendaMedia.add(new BigDecimal(anaDTO.getVendaMedia()).setScale(2, RoundingMode.HALF_EVEN));
                    }
                    
                    if (anaDTO.getReparteMedio() != null){
                        
                        reparteMedio = reparteMedio.add(new BigDecimal(anaDTO.getReparteMedio()).setScale(2, RoundingMode.HALF_EVEN));
                    }
                }
                
                obj.setVdaTotal(vendaMedia.setScale(2, RoundingMode.HALF_EVEN));
                obj.setRepTotal(reparteMedio.setScale(2, RoundingMode.HALF_EVEN));
            }
            
            if (obj.getQtdeCotas().compareTo(BigInteger.ZERO) != 0){
            	obj.setRepMedio(obj.getRepTotal().divide(new BigDecimal(obj.getQtdeCotas()), RoundingMode.HALF_EVEN));
            	obj.setVdaMedio(obj.getVdaTotal().divide(new BigDecimal(obj.getQtdeCotas()), RoundingMode.HALF_EVEN));
            }
            
            if (obj.getRepTotal().compareTo(BigDecimal.ZERO) != 0){
            	obj.setPercVenda(obj.getVdaTotal().multiply(CEM).divide(obj.getRepTotal(), 2, RoundingMode.HALF_EVEN));
            }
            
            obj.executeScaleValues(edicoes.length);
            
            if(obj!=null){
                list.add(obj);
            }
            
            reparteTotal = reparteTotal.add(obj.getRepTotal());
            vendaTotal = vendaTotal.add(obj.getVdaTotal());
            qtdeCotas = qtdeCotas.add(obj.getQtdeCotas());
            cotasEsmagadas = cotasEsmagadas.add(obj.getCotasEsmagadas());
            vendaEsmagadas = vendaEsmagadas.add(obj.getVendaEsmagadas());
            
            if (obj.getQtdeCotasSemVenda() != null){
                qtdeCotasSemVenda = qtdeCotasSemVenda.add(obj.getQtdeCotasSemVenda());
            }
        }
        
        final AnaliseHistogramaDTO totalizar = new AnaliseHistogramaDTO();
        totalizar.setFaixaDe(BigInteger.ZERO);
        totalizar.setFaixaAte(new BigInteger("9999999"));
        totalizar.setFaixaVenda("Total");
        totalizar.setRepTotal(reparteTotal);
        totalizar.setVdaTotal(vendaTotal);
        totalizar.setQtdeCotas(qtdeCotas);
        totalizar.setCotasEsmagadas(cotasEsmagadas);
        totalizar.setVendaEsmagadas(vendaEsmagadas);
        totalizar.setQtdeCotasSemVenda(qtdeCotasSemVenda);
        totalizar.setRepMedio(reparteTotal.divide(new BigDecimal(qtdeCotas), 2, RoundingMode.HALF_EVEN));
        totalizar.setVdaMedio(vendaTotal.divide(new BigDecimal(qtdeCotas), 2, RoundingMode.HALF_EVEN));
        totalizar.setPercVenda(vendaTotal.multiply(CEM).divide(reparteTotal, 2, RoundingMode.HALF_EVEN));
        totalizar.setEncalheMedio(reparteTotal.subtract(vendaTotal).divide(new BigDecimal(qtdeCotas), 2, RoundingMode.HALF_EVEN));
        totalizar.setQtdeTotalCotasAtivas(cotaRepository.obterQuantidadeCotas(SituacaoCadastro.ATIVO));
        totalizar.setReparteDistribuido(movimentoEstoqueService.obterReparteDistribuidoProduto(codigoProduto));
        totalizar.setIdCotaStr(strCotas.toString());
        
        for (final AnaliseHistogramaDTO aDto : list){
        	aDto.setPartReparte(aDto.getRepTotal().multiply(CEM).divide(totalizar.getRepTotal(), 2, RoundingMode.HALF_EVEN));
        	if(totalizar.getVdaTotal().compareTo(BigDecimal.ZERO) > 0) {
        		aDto.setPartVenda(aDto.getVdaTotal().multiply(CEM).divide(totalizar.getVdaTotal(), 2, RoundingMode.HALF_EVEN));
        	} else {
        		aDto.setPartVenda(aDto.getVdaTotal().multiply(CEM).divide(totalizar.getRepTotal(), 2, RoundingMode.HALF_EVEN));
        	}
            
            partReparte = partReparte.add(aDto.getPartReparte());
            partVenda = partVenda.add(aDto.getPartVenda());
        }
        
        totalizar.setPartReparte(partReparte.setScale(0, RoundingMode.HALF_EVEN));
        totalizar.setPartVenda(partVenda.setScale(0, RoundingMode.HALF_EVEN));
        
        list.add(totalizar);
        
        return list;
    }
    
    @Override
    @Transactional
    public BigDecimal obterPorcentualDesconto(final ProdutoEdicao produtoEdicao) {
        
        BigDecimal porcentagemDesconto = null;
        
        switch (produtoEdicao.getOrigem()) {
        
        case INTERFACE:
            
            final DescontoLogistica descontoLogistica =
            (produtoEdicao.getDescontoLogistica() != null)
            ? produtoEdicao.getDescontoLogistica()
                    : produtoEdicao.getProduto().getDescontoLogistica();
            
            if (descontoLogistica != null) {
                
                porcentagemDesconto = descontoLogistica.getPercentualDesconto();
            }
            
            break;
            
        default:
            
            porcentagemDesconto =
            (produtoEdicao.getDesconto() != null)
            ? produtoEdicao.getDesconto()
                    : produtoEdicao.getProduto().getDesconto();
            
            if  ((porcentagemDesconto == null
                    || BigInteger.ZERO.equals(porcentagemDesconto.unscaledValue()))
                    && (Origem.INTERFACE.equals(produtoEdicao.getProduto().getOrigem()))) {
                
                final DescontoLogistica descontoLog =
                        produtoEdicao.getProduto().getDescontoLogistica();
                
                if (descontoLog != null) {
                    
                    porcentagemDesconto = descontoLog.getPercentualDesconto();
                }
            }
            
            break;
        }
        
        if	(porcentagemDesconto == null
                || BigInteger.ZERO.equals(porcentagemDesconto.unscaledValue())) {
            
            throw new ValidacaoException(new ValidacaoVO(
                    TipoMensagem.WARNING,
                    "O produto " + produtoEdicao.getProduto().getNome()
                + " não possui desconto! É necessario cadastrar um desconto para ele na tela de cadastro de produtos"));
        }
        
        return porcentagemDesconto;
    }
    
    @Override
    @Transactional
    public List<ProdutoEdicaoDTO> obterEdicoesProduto(final FiltroHistoricoVendaDTO filtro) {
    	
    	if(filtro.getNumeroEdicao() != null){
    		
    		ProdutoEdicao pe = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(filtro.getProdutoDto().getCodigoProduto(), filtro.getNumeroEdicao());
    		
    		if(pe==null){
    			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Edição não encontrada!"));
    		}
    		
    		boolean isParcial = produtoEdicaoRepository.isEdicaoParcial(pe.getId());
    		
    		if(isParcial){
    			filtro.setBuscarPeriodosParciais(true);
    		}
    	}
    	
    	List<ProdutoEdicaoDTO> listEdicoesProdutoDto = produtoEdicaoRepository.obterEdicoesProduto(filtro);

    	for (ProdutoEdicaoDTO peDTO : listEdicoesProdutoDto) {
    		if((peDTO.getDescricaoSituacaoLancamento().equalsIgnoreCase("FECHADO")) 
    				&& (peDTO.getQtdeVendas() == null || peDTO.getQtdeVendas().compareTo(BigInteger.ZERO) <= 0)){
    			peDTO.setQtdeVendas(estoqueProdutoService.obterVendaBaseadoNoEstoque(peDTO.getId()).toBigInteger());
    		}
    		
    		if((filtro.getNumeroEdicao() == null) && (peDTO.getPeriodo() != null)){
    			
    			String statusLancamentoAtualizado = produtoEdicaoRepository.obterStatusLancamentoPeriodoParcial(peDTO.getId(), peDTO.getPeriodo(), peDTO.getNumeroEdicao());
    			
    			if(!statusLancamentoAtualizado.isEmpty()){
    				peDTO.setDescricaoSituacaoLancamento(statusLancamentoAtualizado);
    			}
    			
    			peDTO.setParcialConsolidado(true);
    			
    		}
    	}
    	
    	return listEdicoesProdutoDto; 
    }
    
    /**
     * @param codigoBarra
     * @return
     * @see br.com.abril.nds.repository.ProdutoEdicaoRepository#obterPorCodigoBarraILike(java.lang.String)
     */
    @Override
    @Transactional(readOnly=true)
    public List<ItemAutoComplete> obterPorCodigoBarraILike(final String codigoBarra) {
        return produtoEdicaoRepository.obterPorCodigoBarraILike(codigoBarra);
    }
    
    @Override
    @Transactional
    public void insereVendaRandomica(final String codigoProduto, final Integer numeroEdicao) {
        produtoEdicaoRepository.insereVendaRandomica(produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, Long.valueOf(numeroEdicao)));
    }
    
    @Override
    @Transactional
    public void tratarInformacoesAdicionaisProdutoEdicaoArquivo(final ProdutoEdicaoDTO dto) {
        
        if(dto.getCodigoProduto()==null) {
            return;
        }
        
        final Produto produto = produtoRepository.obterProdutoPorCodigoProdin(dto.getCodigoProduto());
        
        if(produto==null) {
            return;
        }
        
        if (Origem.INTERFACE.equals(produto.getOrigem()) && produto.getDescontoLogistica()==null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "O produto inserido não possui desconto cadastrado.");
        } else if (!Origem.INTERFACE.equals(produto.getOrigem()) && produto.getDesconto() == null) {
        	throw new ValidacaoException(TipoMensagem.WARNING, "O produto inserido não possui desconto cadastrado.");
        }
        
        dto.setDesconto(Origem.INTERFACE.equals(produto.getOrigem()) ? 
        		produto.getDescontoLogistica().getPercentualDesconto() : 
        		produto.getDesconto());

        dto.setDescricaoDesconto(Origem.INTERFACE.equals(produto.getOrigem()) ? 
        		produto.getDescontoLogistica().getDescricao() : 
        		produto.getDescricaoDesconto());

        if(dto.getPacotePadrao()==null) {
            dto.setPacotePadrao(produto.getPacotePadrao());
        }
        
        if(dto.getPeb()==null) {
            dto.setPeb(produto.getPeb());
        }
        
        if(dto.getPeso()==null) {
            dto.setPeso(produto.getPeso());
        }
        
        final String parcial = dto.getRecolhimentoParcial();
        
        if("SIM".equalsIgnoreCase(parcial) || "TRUE".equalsIgnoreCase(parcial)  || "PARCIAL".equalsIgnoreCase(parcial)) {
            dto.setParcial(true);
        } else if("NÃO".equalsIgnoreCase(parcial) || "NAO".equalsIgnoreCase(parcial) || "FALSE".equalsIgnoreCase(parcial)) {
            dto.setParcial(false);
        } else {
        	throw new ValidacaoException(TipoMensagem.WARNING, String.format("Informação \"%s\" na coluna parcial está inválida", parcial));
        }
        
        final Calendar calendar = Calendar.getInstance();
        calendar.set(100, 1, 1);
        
        final Date ano0100 = calendar.getTime();
        
        if(DateUtil.isDataInicialMaiorDataFinal(ano0100, dto.getDataLancamentoPrevisto())
                || DateUtil.isDataInicialMaiorDataFinal(ano0100, dto.getDataRecolhimentoPrevisto())) {
            throw new  ValidacaoException(TipoMensagem.WARNING, "Data em formato incorreto. Ano deve ser escrito por extenso. Ex: '10/10/2010'");
        }
        
        dto.setPossuiBrinde(false);
        dto.setPermiteValeDesconto(false);
        dto.setOrigemInterface(false);
        dto.setNumeroLancamento(1);
        
    }
    
    @Override
	public List<ProdutoEdicao> obterProdutosEdicaoPorId(final Set<Long> idsProdutoEdicao) {
    	
    	return this.produtoEdicaoRepository.obterProdutosEdicaoPorId(idsProdutoEdicao);
    }

    @Override
    @Transactional(readOnly=true)
    public List<ItemDTO<String, String>> obterProdutosBalanceados(Date dataLancamento) {
        
        return this.produtoEdicaoRepository.obterProdutosBalanceados(dataLancamento);
    }
    
    @Override
    @Transactional(readOnly=true)
    public BigDecimal obterPrecoEdicaoParaAlteracao(final String codigoProduto, final Long numeroEdicao){
    	
    	ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto,numeroEdicao);
    	
    	if(produtoEdicao == null){
    		return null;
    	}
    	
    	this.validarLancamentoParaAlteracaoPreco(produtoEdicao.getId());
    	
    	return produtoEdicao.getPrecoVenda();
    }
    
    @Override
    @Transactional
    public void executarAlteracaoPrecoCapa(final String codigo, final Long numeroEdicao, final BigDecimal precoProduto) {
    	
    	if((codigo == null || codigo.isEmpty()) || numeroEdicao == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Informe uma publicação para alteração."));
		}
		
		if(precoProduto == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Informe um valor para o campo [Novo Preço]."));
		}
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigo, numeroEdicao);
	
		if(produtoEdicao == null ) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Informe uma publicação existente para alteração."));
		}
		
		this.validarLancamentoParaAlteracaoPreco(produtoEdicao.getId());
		
		BigDecimal valorAntigo = produtoEdicao.getPrecoVenda();
		produtoEdicao.setPrecoVenda(precoProduto);
		produtoEdicaoRepository.merge(produtoEdicao);
		
		movimentoEstoqueCotaService.atualizarPrecoProdutoExpedido(produtoEdicao.getId(), precoProduto);
		
		HistoricoAlteracaoPrecoVenda hapc = new HistoricoAlteracaoPrecoVenda();
		hapc.setDataOperacao(distribuidorService.obterDataOperacaoDistribuidor());
		hapc.setUsuario(usuarioService.getUsuarioLogado());
		hapc.setProdutoEdicao(produtoEdicao);
		hapc.setValorAntigo(valorAntigo);
		hapc.setValorAtual(precoProduto);
		historicoAlteracaoPrecoVendaRepository.adicionar(hapc);
    }

	private void validarLancamentoParaAlteracaoPreco(final Long idProdutoEdicao) {
		
		final boolean publicacaoEditavel = 
				lancamentoRepository.existeLancamentoParaOsStatus(idProdutoEdicao,
						StatusLancamento.EXPEDIDO,StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO);
		
		if(!publicacaoEditavel){
			
			final StatusLancamento statusLancamento = lService.obterStatusDoPrimeiroLancamentoDaEdicao(idProdutoEdicao);
			
			throw new  ValidacaoException(TipoMensagem.WARNING, 
					"O status do lançamento [" +statusLancamento.getDescricao() +"] não permite alteração de preço. ");
		}
	}
}

