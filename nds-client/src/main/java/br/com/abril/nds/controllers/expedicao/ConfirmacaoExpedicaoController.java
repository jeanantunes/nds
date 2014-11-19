package br.com.abril.nds.controllers.expedicao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.TipoMovimentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

import com.google.common.primitives.Longs;

@Resource
@Path("/confirmacaoExpedicao")
@Rules(Permissao.ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO)
public class ConfirmacaoExpedicaoController extends BaseController{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmacaoExpedicaoController.class);

	@Autowired
	private final Result result;
	
	@Autowired
	private final HttpSession session;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private TipoMovimentoService tipoMovimentoService;
	
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private PoliticaCobrancaService politicaCobrancaService;
		
	protected static final String SUCESSO = "SUCCESS";
	protected static final String ALERTA = "WARNING";
	protected static final String FALHA = "ERROR";
	protected static final Long COMBO_VAZIO = -1L;
	
    protected static final String MSG_PESQUISA_SEM_RESULTADO = "Não há resultados para a pesquisa realizada.";
    protected static final String DATA_INVALIDA = "A data informada é inválida";
    protected static final String CONFIRMACAO_EXPEDICAO_SUCESSO = "Expedições confirmadas com sucesso!";
    protected static final String CONFIRMACAO_EXPEDICAO_ESTOQUE_INDISPONIVEL = "Expedições não confirmadas. Estoque indisponível para todos os lançamentos selecionados !";
	protected static final String NENHUM_REGISTRO_SELECIONADO="Nenhum registro foi selecionado!";
    protected static final String ERRO_CONFIRMAR_EXPEDICOES = "Erro não esperado ao confirmar expedições.";
    protected static final String ERRO_PESQUISAR_LANCAMENTOS_NAO_EXPEDIDOS = "Erro não esperado ao pesquisar lançamentos não expedidos.";
    protected static final String MSG_NAO_EXISTE_MATRIZ_BALANCEAMENTO_CONFIRMADO = "Não há produtos a serem expedidos na data informada.";
    // protected static final String MSG_EXISTE_MATRIZ_BALANCEAMENTO_CONFIRMADO
    // = "Há matriz de lançamento confirmada.";
		
	protected static final String STATUS_EXPEDICAO = "statusExpedicao";
		/**
		 * Construtor
		 * 
		 * @param result
		 */
		public ConfirmacaoExpedicaoController(Result result,HttpSession session, 
				FornecedorService fornecedorService, LancamentoService lancamentoService,
				MovimentoEstoqueService movimentoEstoqueService) {
			
			this.result = result;
			this.fornecedorService = fornecedorService;
			this.lancamentoService = lancamentoService;
			this.session = session;
		}
		
		/**
		 * Inicializa dados da tela
		 */
		@Path("/")
		public void index() {
			gerarListaFornecedores();
			gerarDataLancamento();
			session.setAttribute("selecionados", null);
			result.forwardTo(ConfirmacaoExpedicaoController.class).confirmacaoExpedicao();
		}
				
		
		/**
		 * Adiciona ou remove um item da lista de item adicionado
		 * 
		 * @param idLancamento - id do lancamento a ser adicionado
		 * @param selecionado - true(adiciona a lista) false(remove da lista)
		 */
		@Post
		@Rules(Permissao.ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO_ALTERACAO)
		public void selecionarLancamento(Long idLancamento, Boolean selecionado) {
			
			@SuppressWarnings("unchecked")
			List<Long> selecionados = (List<Long>) session.getAttribute("selecionados");
			
			if(selecionados == null) {
				
				selecionados = new ArrayList<Long>();			
			}

			int index = selecionados.indexOf(idLancamento); 
			
			if(index==-1) {
				selecionados.add(idLancamento);
			} else {
				selecionados.remove(index);
			}
			
			session.setAttribute("selecionados", selecionados);
			
			result.use(Results.json()).withoutRoot().from(selecionado).recursive().serialize();
		}
		
		        /**
     * Adiciona ou remove todos os itens da pesquisa a lista de itens
     * selecionados da sessão.
     * 
     * @param selecionado - true(adiciona todos) false (remove todos)
     */
		@Post
		@Rules(Permissao.ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO_ALTERACAO)
		public void selecionarTodos(Boolean selecionado){
			
			if(selecionado == false) {
				
				session.setAttribute("selecionados", null);
				
			} else {
			
				Date date = (Date) session.getAttribute("date");
				Long idFornecedor = (Long) session.getAttribute("idFornecedor");
				
				List<Long> listaIdsExpedicoes = lancamentoService.obterIdsLancamentosNaoExpedidos(null, date, idFornecedor, true);
								
				session.setAttribute("selecionados", listaIdsExpedicoes);
			}
			
			result.use(Results.json()).withoutRoot().from(selecionado).recursive().serialize();
		}
		
		 /**
         * Método com finalidade de redirecionamento para a página
         */
    	public void confirmacaoExpedicao() {
    	}
	
		/**
         * Confirma expedição, gera movimentos do distribuidor e cotas e retorna
         * dados da pesquisa atualizados
         * 
         * @param page - nº da página a pesquisar
         * @param rp - nº de registros por página
         * @param sortname - nome da coluna de ordenação
         * @param sortorder - ordenação (asc - desc)
         * @param idFornecedor - código do fornecedor
         * @param dtLancamento - data de lançamento
         * @param estudo - boolean - possui ou não estudo
         */
		@Rules(Permissao.ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO_ALTERACAO)
		public void confirmarExpedicao( Integer page, Integer rp, String sortname, 
				String sortorder, Long idFornecedor, 
				String dtLancamento, Boolean estudo){
			
			String status = SUCESSO;
			
			List<String> mensagens = new ArrayList<String>();
			
			@SuppressWarnings("unchecked")
			List<Long> selecionados = (List<Long>) session.getAttribute("selecionados");
			
			TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>> grid = null;

			try {

				verificarExecucaoInterfaces();
				
				if(selecionados==null  || selecionados.isEmpty()) {
					throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, NENHUM_REGISTRO_SELECIONADO));
				} 
				
				session.setAttribute(STATUS_EXPEDICAO, getMsgProcessamento(1, selecionados.size()));
				
				final TipoMovimentoEstoque tipoMovimento =
					tipoMovimentoService.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
				
				final TipoMovimentoEstoque tipoMovimentoJuramentado =
						tipoMovimentoService.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO_JURAMENTADO);
				
				final TipoMovimentoEstoque tipoMovimentoCota =
						tipoMovimentoService.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
				
				final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
				
				final Date dataVencimentoDebito = calendarioService.obterProximaDataDiaUtil(dataOperacao);
				
				final Fornecedor fornecedorPadraoDistribuidor = politicaCobrancaService.obterFornecedorPadrao();
				
				//Ordena IDS
				long[] lista = Longs.toArray(selecionados);
		        Arrays.sort(lista); 
		        selecionados = Longs.asList(lista);
				
		        String retorno = null;
		        List<String> listaRetorno = new ArrayList<>();
		        
				for(int i=0; i<selecionados.size(); i++) {
					
					final ExpedicaoDTO expedicaoDTO = new ExpedicaoDTO(
							selecionados.get(i), getUsuarioLogado().getId(), dataOperacao, 
							tipoMovimento, tipoMovimentoCota, tipoMovimentoJuramentado, 
							dataVencimentoDebito,fornecedorPadraoDistribuidor.getId());
					
					retorno = lancamentoService.confirmarExpedicao(expedicaoDTO);
					
					session.setAttribute(STATUS_EXPEDICAO, getMsgProcessamento((i+1), selecionados.size()));
					
					this.tratarMensagemRetorno(retorno, listaRetorno);
				}
				
				if (!listaRetorno.isEmpty()) {
				    
				    throw new ValidacaoException(TipoMensagem.WARNING, listaRetorno);
				}
				
				mensagens.add(CONFIRMACAO_EXPEDICAO_SUCESSO);
				
				grid = gerarGrid(page, rp, sortname, sortorder, idFornecedor, dtLancamento, estudo);
							
				session.setAttribute("selecionados",null);
				
			} catch(ValidacaoException e) {
				
				if(e.getUrl() == null) {
				
					mensagens.clear();
					
					mensagens.addAll(e.getValidacao().getListaMensagens());
					status=TipoMensagem.WARNING.name();
				}
				
			} catch(Exception e) {
				mensagens.clear();
				mensagens.add(ERRO_CONFIRMAR_EXPEDICOES);
				status=TipoMensagem.ERROR.name();
				LOGGER.error(ERRO_CONFIRMAR_EXPEDICOES, e);
			}
			
			session.setAttribute(STATUS_EXPEDICAO, "FINALIZADO");
			
			if(grid==null) {
				grid = new TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>>();
			}
				
			
			Object[] retorno = new Object[3];
			retorno[0] = grid;
			retorno[1] = mensagens;
			retorno[2] = status;

			
			result.use(Results.json()).withoutRoot().from(retorno).recursive().serialize();
		}

        private void tratarMensagemRetorno(String retorno, List<String> listaRetorno) {
            
            if (retorno != null) {
                
                if (listaRetorno.isEmpty()) {
                    
                    listaRetorno.add("Redistribuição não pode ser expedida, "
                            + "pois o lançamento já se encontra em processo de recolhimento. "
                            + "Caso necessário reabra a matriz de recolhimento!");
                }
                
                listaRetorno.add(retorno);
            }
        }
		
		private Object getMsgProcessamento(Integer atual, Integer total) {
			
        return "Processando Expedições... " + " (" + atual + "/" + total + ")";
		}

		@Post
		public void verificarExpedicao(){
			
			String status = (String) session.getAttribute(STATUS_EXPEDICAO);
			
        result.use(Results.json()).withoutRoot().from(status == null ? "Processando Expedições..." : status)
                .recursive().serialize();
		}
		
	
		/**
		 * Gera a data de lancamento como data atual
		 */
		private void gerarDataLancamento() {
			
			String dataLancamento = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			
			result.include("dataLancamento",dataLancamento);			
		}
		
		/**
		 * Gera a lista de Fornecedores para utilizar no combo
		 */
		public void gerarListaFornecedores() {
			
			List<Fornecedor> fornecedores =  fornecedorService.obterFornecedores();
			
			result.include("fornecedores",fornecedores);
		}
		
		        /**
     * Realiza pesquisa de expedições por filtro, e retorna JSON com a mesma.
     * 
     * @param page - nº da página a pesquisar
     * @param rp - nº de registros por página
     * @param sortname - nome da coluna de ordenação
     * @param sortorder - ordenação (asc - desc)
     * @param idFornecedor - código do fornecedor
     * @param dtLancamento - data de lançamento
     * @param estudo - boolean - possui ou não estudo
     * @param ultimaPesquisa - data/hora gerada ao clique do botão pesquisar
     *            utilizada para identificar uma nova pesquisa(data diferente da
     *            guardada em sessão), pódendo assim limpar seleções e lançar
     *            mensagens de validação de filtro de pesquisa, que não correrão
     *            ao paginar os dados
     * 
     */
		public void pesquisarExpedicoes(Integer page, Integer rp, String sortname, 
						String sortorder, Long idFornecedor, 
						String dtLancamento, Boolean estudo, String ultimaPesquisa){
						
			String status= SUCESSO;
			
			boolean isNewSearch = !ultimaPesquisa.equals((String)session.getAttribute("ultimaPesquisa"));
			
			if(isNewSearch) {
				session.setAttribute("selecionados", null);
				session.setAttribute("ultimaPesquisa", ultimaPesquisa);
				page = 1;
			}			
			
			List<String> mensagens = new ArrayList<String>();
			
			TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>> grid = null;
			
			try {
				validarExistenciaMatriz(DateUtil.parseData(dtLancamento, Constantes.DATE_PATTERN_PT_BR));
				
				grid = gerarGrid(page, rp, sortname, sortorder, idFornecedor, dtLancamento, estudo);
			
			} catch(ValidacaoException e) {
				mensagens = e.getValidacao().getListaMensagens();
				status = e.getValidacao().getTipoMensagem().name();
				grid = new TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>>();
			
			} catch (Exception e) {
				mensagens.clear();
				mensagens.add(ERRO_PESQUISAR_LANCAMENTOS_NAO_EXPEDIDOS);
				status=TipoMensagem.ERROR.name();
				LOGGER.error(ERRO_PESQUISAR_LANCAMENTOS_NAO_EXPEDIDOS, e);
				grid = new TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>>();
			}
			
			if (!isNewSearch) {
				mensagens = new ArrayList<String>();
			}
			
			Object[] retorno = new Object[3];
			retorno[0] = grid;
			retorno[1] = mensagens;
			retorno[2] = status;
			
			result.use(Results.json()).withoutRoot().from(retorno).recursive().serialize();						
		}	
			
		        /**
     * Gera a tabela utilizada pelo FlexGrid de acordo com os filtros e dados de
     * paginação
     * 
     * @param page - nº da página a pesquisar
     * @param rp - nº de registros por página
     * @param sortname - nome da coluna de ordenação
     * @param sortorder - ordenação (asc - desc)
     * @param idFornecedor - código do fornecedor
     * @param dtLancamento - data de lançamento
     * @param estudo - boolean - possui ou não estudo
     * @return
     */
		public TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>> gerarGrid( Integer page, Integer rp, String sortname, 
				String sortorder, Long idFornecedor, 
				String dtLancamento, Boolean estudo){
			
			PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder, sortname);
						
			TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>> grid = null;
			
			Date date = DateUtil.parseData(dtLancamento, Constantes.DATE_PATTERN_PT_BR);
			
			session.setAttribute("paginacaoVO",paginacaoVO);
			session.setAttribute("date",date);
			session.setAttribute("idFornecedor",idFornecedor);
			session.setAttribute("estudo",estudo);
			
			if(date == null && !dtLancamento.trim().isEmpty()) {
				throw new ValidacaoException("/pesquisarExpedicoes", new ValidacaoVO(TipoMensagem.WARNING, DATA_INVALIDA));
			} else {
			
				List<LancamentoNaoExpedidoDTO> listaExpedicoes = 
						lancamentoService.obterLancamentosNaoExpedidos(paginacaoVO, date, idFornecedor, estudo);

				Long total = lancamentoService.obterTotalLancamentosNaoExpedidos(date, idFornecedor, estudo);
				
				List<CellModelKeyValue<LancamentoNaoExpedidoDTO>> listaCelula = new LinkedList<CellModelKeyValue<LancamentoNaoExpedidoDTO>>();
				
				@SuppressWarnings("unchecked")
				List<Long> selecionados = (List<Long>) session.getAttribute("selecionados");
				
				for(LancamentoNaoExpedidoDTO expedicao : listaExpedicoes) {						
				
					if( selecionados!=null && selecionados.contains(expedicao.getIdLancamento()) ) {
						expedicao.setSelecionado(true);
					}
					
					listaCelula.add(new CellModelKeyValue<LancamentoNaoExpedidoDTO>(Integer.valueOf(expedicao.getCodigo()),expedicao));			
				}
				
				if(listaExpedicoes.isEmpty()) {
					throw new ValidacaoException("/pesquisarExpedicoes",new ValidacaoVO(TipoMensagem.WARNING,MSG_PESQUISA_SEM_RESULTADO));
				}
							
				grid = new TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>>();
				
				grid.setPage(page);
				grid.setTotal(total.intValue());
				grid.setRows(listaCelula);
				
			}
			return grid;
		}
		
		        /**
     * Válida
     * 
     * @param dataLancamento
     */
		private void validarExistenciaMatriz(Date dataLancamento) {
			if(!lancamentoService.existeMatrizBalanceamentoConfirmado(dataLancamento)){
				throw new ValidacaoException("/pesquisarExpedicoes",new ValidacaoVO(TipoMensagem.WARNING,MSG_NAO_EXISTE_MATRIZ_BALANCEAMENTO_CONFIRMADO));
			}
		}
		
		private void verificarExecucaoInterfaces() {
			if (distribuidorService.verificaDesbloqueioProcessosLancamentosEstudos()) {
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "As interfaces encontram-se em processamento. Aguarde o termino da execução para continuar!");
			}
		}
		
		@Get
		@Path("obterValorDesconto/{numeroCota}/{codigoProduto}/{numeroEdicao}")
		public void obterValorDesconto(Integer numeroCota, String codigoProduto, Long numeroEdicao) throws Exception {
		    
		    
		    if(numeroCota.equals(0))
		        numeroCota = null;
		    
		    if(codigoProduto.equals("0"))
                codigoProduto = null;
            
		    
		    if(numeroEdicao.equals(0L))
                numeroEdicao = null;
		    
		    DescontoDTO dto = descontoService.obterDescontoPor(numeroCota, codigoProduto, numeroEdicao);
		    
		    if(dto == null)
		        dto = new DescontoDTO();
		    
		    result.use(Results.json()).from(dto, "result").serialize();
		    
		}
		
	}