package br.com.abril.nds.controllers.expedicao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/confirmacaoExpedicao")
public class ConfirmacaoExpedicaoController {

		private final Result result;
		private final HttpSession session;
		
		private FornecedorService fornecedorService;
		private LancamentoService lancamentoService;

		protected static final String SUCESSO = "SUCCESS";
		protected static final String FALHA = "ERROR";
		protected static final Long COMBO_VAZIO = -1L;
		
		protected static final String MSG_PESQUISA_SEM_RESULTADO = "Não há resultados para a apesquisa realizada.";
		protected static final String DATA_INVALIDA = "A data informada é inválida";
		protected static final String CONFIRMACAO_EXPEDICAO_SUCESSO = "Expedições confirmadas com sucesso!";
		protected static final String NENHUM_REGISTRO_SELECIONADO="Nenhum registro foi selecionado!";
		protected static final String ERRO_CONFIRMAR_EXPEDICOES="Erro não esperado ao confirmar expedições.";
		protected static final String ERRO_PESQUISAR_LANCAMENTOS_NAO_EXPEDIDOS = "Erro não esperado ao pesquisar lançamentos não expedidos.";
		protected static final String MSG_EXISTE_MATRIZ_BALANCEAMENTO_CONFIRMADO = "Há matriz de lançamento confirmada.";
		
		private static final Logger LOG = LoggerFactory
				.getLogger(ConfirmacaoExpedicaoController.class);
		
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
		@Rules(Permissao.ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO)
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
		 * Adiciona ou remove todos os itens da pesquisa a lista de itens selecionados da sessão.
		 * 
		 * @param selecionado - true(adiciona todos) false (remove todos)
		 */
		@Post
		public void selecionarTodos(Boolean selecionado){
			
			if(selecionado==false) {
				session.setAttribute("selecionados", null);
			} else {
			
				Date date = (Date) session.getAttribute("date");
				Long idFornecedor = (Long) session.getAttribute("idFornecedor");
				Boolean estudo = (Boolean) session.getAttribute("estudo");
				
				List<LancamentoNaoExpedidoDTO> listaExpedicoes = 
						lancamentoService.obterLancamentosNaoExpedidos(null, date, idFornecedor, estudo);
				
				@SuppressWarnings("unchecked")
				List<Long> selecionados = (List<Long>) session.getAttribute("selecionados");
				
				if(selecionados==null) {
					selecionados = new ArrayList<Long>();
				}
				
				for ( LancamentoNaoExpedidoDTO lancamento : listaExpedicoes ) {
					if(lancamento.getEstudo() != null) {
					selecionados.add(lancamento.getIdLancamento());
					}
				}
				
				session.setAttribute("selecionados", selecionados);
			}
			
			result.use(Results.json()).withoutRoot().from(selecionado).recursive().serialize();
		}
		
		/**
		 * Método com finalidade de redirecionamento para a página
		 */
		public void confirmacaoExpedicao() {
		}
		
		
		/**
		 * Confirma expedição, gera movimentos do distribuidor e cotas e retorna dados da 
		 * pesquisa atualizados
		 * 
		 * @param page - nº da página a pesquisar
		 * @param rp - nº de registros por página
		 * @param sortname - nome da coluna de ordenação
		 * @param sortorder - ordenação (asc - desc)
		 * @param idFornecedor - código do fornecedor
		 * @param dtLancamento - data de lançamento
		 * @param estudo - boolean - possui ou não estudo
		 */
		public void confirmarExpedicao( Integer page, Integer rp, String sortname, 
				String sortorder, Long idFornecedor, 
				String dtLancamento, Boolean estudo){
			
			String status = SUCESSO;
			
			List<String> mensagens = new ArrayList<String>();
			
			@SuppressWarnings("unchecked")
			List<Long> selecionados = (List<Long>) session.getAttribute("selecionados");
			
			TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>> grid = null;
		
			try {
				
				if(selecionados==null  || selecionados.isEmpty()) {
					throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, NENHUM_REGISTRO_SELECIONADO));
				} 
				
				lancamentoService.confirmarExpedicoes(selecionados,getUsuario().getId());
			
				mensagens.add(CONFIRMACAO_EXPEDICAO_SUCESSO);
				
				grid = gerarGrid(
						page, rp, sortname, sortorder, idFornecedor, dtLancamento, estudo);
							
				
			} catch(ValidacaoException e) {
				
				if(e.getUrl() == null) {
				
					mensagens.clear();
					
					mensagens.addAll(e.getValidacao().getListaMensagens());
					status=TipoMensagem.WARNING.name();
				}
				
			}catch(Exception e) {
				mensagens.clear();
				mensagens.add(ERRO_CONFIRMAR_EXPEDICOES);
				status=TipoMensagem.ERROR.name();
				LOG.error(ERRO_CONFIRMAR_EXPEDICOES, e);
			}
			
			if(grid==null) {
				grid = new TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>>();
			}
				
			
			Object[] retorno = new Object[3];
			retorno[0] = grid;
			retorno[1] = mensagens;
			retorno[2] = status;
			
			
			result.use(Results.json()).withoutRoot().from(retorno).recursive().serialize();
		}
		
		/**
		 * Método que obtém o usuário logado
		 * 
		 * @return usuário logado
		 */
		public Usuario getUsuario() {
			//TODO getUsuario
			Usuario usuario = new Usuario();
			usuario.setId(1L);
			return usuario;
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
		 * @param ultimaPesquisa - data/hora gerada ao clique do botão pesquisar utilizada para identificar
		 * 	uma nova pesquisa(data diferente da guardada em sessão), pódendo assim limpar seleções e lançar
		 * 	mensagens de validação de filtro de pesquisa, que não correrão ao paginar os dados
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
			}			
			
			List<String> mensagens = new ArrayList<String>();
			
			TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>> grid = null;
			
			try {
				validarExistenciaMatriz(DateUtil.parseData(dtLancamento, Constantes.DATE_PATTERN_PT_BR));
				
				grid = gerarGrid(
						page, rp, sortname, sortorder, idFornecedor, dtLancamento, estudo);
			
			}catch(ValidacaoException e) {
				mensagens = e.getValidacao().getListaMensagens();
				status=e.getValidacao().getTipoMensagem().name();
				grid = new TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>>();
			
			} catch (Exception e) {
				mensagens.clear();
				mensagens.add(ERRO_PESQUISAR_LANCAMENTOS_NAO_EXPEDIDOS);
				status=TipoMensagem.ERROR.name();
				LOG.error(ERRO_PESQUISAR_LANCAMENTOS_NAO_EXPEDIDOS, e);
				grid = new TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>>();
			}
			
			if (!isNewSearch) {
				mensagens = new ArrayList<String>();
			}
			
			Object[] retorno = new Object[3];
			retorno[0] = grid;
			retorno[1] = mensagens;
			retorno[2] = status;
			
			result.use(Results.json()).withoutRoot().from(retorno).serialize();						
		}	
			
		/**
		 * Gera a tabela utilizada pelo FlexGrid de acordo com os filtros e dados de paginação
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
				throw new ValidacaoException("/pesquisarExpedicoes",new ValidacaoVO(TipoMensagem.WARNING,DATA_INVALIDA));
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
					
					listaCelula.add(new CellModelKeyValue<LancamentoNaoExpedidoDTO>(expedicao.getCodigo().intValue(),expedicao));			
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
			if(lancamentoService.existeMatrizBalanceamentoConfirmado(dataLancamento)){
				throw new ValidacaoException("/pesquisarExpedicoes",new ValidacaoVO(TipoMensagem.WARNING,MSG_EXISTE_MATRIZ_BALANCEAMENTO_CONFIRMADO));
			}
		}
	}