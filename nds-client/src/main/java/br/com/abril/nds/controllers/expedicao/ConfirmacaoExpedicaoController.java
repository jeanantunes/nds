package br.com.abril.nds.controllers.expedicao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MovimentoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
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
		
		private static final Logger LOG = LoggerFactory.getLogger(ConfirmacaoExpedicaoController.class);
		
		/**
		 * Construtor
		 * 
		 * @param result
		 */
		public ConfirmacaoExpedicaoController(Result result,HttpSession session, 
				FornecedorService fornecedorService, LancamentoService lancamentoService,
				MovimentoService movimentoService) {
			
			this.result = result;
			this.fornecedorService = fornecedorService;
			this.lancamentoService = lancamentoService;
			this.session = session;
			this.inicializarTela();
		}
		
		public void inicializarTela() {
			gerarListaFornecedores();
			gerarDataLancamento();
			session.setAttribute("selecionados", null);
		}
				
		
		
		@SuppressWarnings("unchecked")
		@Post
		public void selecionarLancamento(Long idLancamento, Boolean selecionado) throws Exception {
			
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
		
		@SuppressWarnings("unchecked")
		@Post
		public void selecionarTodos(Boolean selecionado) throws Exception {
			
			if(selecionado==false) {
				session.setAttribute("selecionados", null);
			} else {
			
				Date date = (Date) session.getAttribute("date");
				Long idFornecedor = (Long) session.getAttribute("idFornecedor");
				Boolean estudo = (Boolean) session.getAttribute("estudo");
				
				List<LancamentoNaoExpedidoDTO> listaExpedicoes = 
						lancamentoService.obterLancamentosNaoExpedidos(null, date, idFornecedor, estudo);
				
				List<Long> selecionados = (List<Long>) session.getAttribute("selecionados");
				
				if(selecionados==null) {
					selecionados = new ArrayList<Long>();
				}
				
				for ( LancamentoNaoExpedidoDTO lancamento : listaExpedicoes ) {
									
					selecionados.add(lancamento.getIdLancamento());
				}
				
				session.setAttribute("selecionados", selecionados);
			}
			
			result.use(Results.json()).withoutRoot().from(selecionado).recursive().serialize();
		}
		
		/**
		 * Método com finalidade de redirecionamento para a p�gina
		 */
		public void confirmacaoExpedicao() {
		}
		
		@SuppressWarnings("unchecked")
		public void confirmarExpedicao(){
			
			List<String> mensagens = new ArrayList<String>();
			
			List<Long> selecionados = (List<Long>) session.getAttribute("selecionados");
			
			if(selecionados==null  || selecionados.isEmpty()) {
				mensagens.add(NENHUM_REGISTRO_SELECIONADO);
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, mensagens));
			} 
			
			try {
				
				for( Long idLancamento:selecionados ) {		
					lancamentoService.confirmarExpedicao(idLancamento, getUsuario().getId());
				}
				
			} catch(Exception e) {
				
			}
			
			
			mensagens.add(CONFIRMACAO_EXPEDICAO_SUCESSO);
			
			
			ValidacaoVO voValidacao = new ValidacaoVO(TipoMensagem.SUCCESS,mensagens);			
			
			result.use(Results.json()).withoutRoot().from(voValidacao).recursive().serialize();
		}
		
		public Usuario getUsuario() {
			return null;
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
		 * @param dataLancamento - Data de lan�amento da expedição
		 * @param idFornecedor - código do fornecedor
		 * @throws Exception 
		 */
		@SuppressWarnings("unchecked")
		public void pesquisarExpedicoes(Integer page, Integer rp, String sortname, 
						String sortorder, Long idFornecedor, 
						String dtLancamento, Boolean estudo){
			
			PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder, sortname);
			
			String status = SUCESSO;
			
			List<String> mensagens = new ArrayList<String>();
			
			TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>> grid = null;
			
			Date date = DateUtil.parseData(dtLancamento, Constantes.DATE_PATTERN_PT_BR);
			
			session.setAttribute("paginacaoVO",paginacaoVO);
			session.setAttribute("date",date);
			session.setAttribute("idFornecedor",idFornecedor);
			session.setAttribute("estudo",estudo);
			
			if(date == null && !dtLancamento.trim().isEmpty()) {
				mensagens.add(DATA_INVALIDA);
				status = FALHA;
			} else {
			
				List<LancamentoNaoExpedidoDTO> listaExpedicoes = 
						lancamentoService.obterLancamentosNaoExpedidos(paginacaoVO, date, idFornecedor, estudo);
				
				Long total = lancamentoService.obterTotalLancamentosNaoExpedidos(date, idFornecedor, estudo);
				
				List<CellModelKeyValue<LancamentoNaoExpedidoDTO>> listaCelula = new LinkedList<CellModelKeyValue<LancamentoNaoExpedidoDTO>>();
				
				List<Long> selecionados = (List<Long>) session.getAttribute("selecionados");
				
				for(LancamentoNaoExpedidoDTO expedicao : listaExpedicoes) {						
				
					if( selecionados!=null && selecionados.contains(expedicao.getIdLancamento()) ) {
						expedicao.setSelecionado(true);
					}
					
					listaCelula.add(new CellModelKeyValue<LancamentoNaoExpedidoDTO>(expedicao.getCodigo().intValue(),expedicao));			
				}
				
				if(listaExpedicoes.isEmpty()) {
					mensagens.add(MSG_PESQUISA_SEM_RESULTADO);
				}
							
				grid = new TableModel<CellModelKeyValue<LancamentoNaoExpedidoDTO>>();
				
				grid.setPage(page);
				grid.setTotal(total.intValue());
				grid.setRows(listaCelula);
				
			}
				
			Object[] retorno = new Object[3];
			retorno[0] = status;
			retorno[1] = mensagens;
			retorno[2] = grid;
			
			result.use(Results.json()).withoutRoot().from(retorno).recursive().serialize();
			
		}	
		
	}