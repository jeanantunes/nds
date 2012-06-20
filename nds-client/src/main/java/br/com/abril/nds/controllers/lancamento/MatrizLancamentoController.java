package br.com.abril.nds.controllers.lancamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ResultadoResumoBalanceamentoVO;
import br.com.abril.nds.client.vo.ResumoPeriodoBalanceamentoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.view.Results;

@Resource
public class MatrizLancamentoController {

	@Autowired
	private Result result;

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private MatrizLancamentoService matrizLancamentoService;
		
	@Autowired 
	private Localization localization;
	
	@Autowired
	private HttpSession session;
	
	private static final String FORMATO_DATA = "dd/MM/yyyy";
	
	private static final String CAMPO_REQUERIDO_KEY = "required_field";
	private static final String CAMPO_MAIOR_IGUAL_KEY = "validator.must.be.greaterEquals";

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroMatrizBalanceamento";
	
	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO = "balanceamento";
	
	@Path("/matrizLancamento")
	public void index() {
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);
		
		List<Fornecedor> fornecedores = fornecedorService.obterFornecedores(
				true, SituacaoCadastro.ATIVO);
		String data = DateUtil.formatarData(new Date(), FORMATO_DATA);
		result.include("data", data);
		result.include("fornecedores", fornecedores);
	}
	
	@Post
	public void obterMatrizLancamento(Date dataLancamento, List<Long> idsFornecedores) {
		
		validarDadosPesquisa(dataLancamento, idsFornecedores);
		
		FiltroLancamentoDTO filtro = configurarFiltropesquisa(dataLancamento, idsFornecedores);
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
				this.obterBalanceamentoRecolhimento(filtro);
				
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoLancamento);
						
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	
	
	@Post
	public void obterGridMatrizLancamento(String sortorder, String sortname, int page, int rp) {
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = null;
		
		// TODO: obter a matriz da sessão
		
		if (balanceamentoLancamento == null
				|| balanceamentoLancamento.getMatrizLancamento() == null
				|| balanceamentoLancamento.getMatrizLancamento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		List<ProdutoLancamentoDTO> listaProdutoLancamento = null;
		
		// TODO: montar a lista de produtoLancamentoDTO através do map
		
		if (listaProdutoLancamento != null && !listaProdutoLancamento.isEmpty()) {
			
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			processarBalanceamento(listaProdutoLancamento, paginacao);
			
		} else {
			
			this.result.use(Results.json()).from(Results.nothing()).serialize();
		}
	}
	
	@Post
	public void confirmarMatrizLancamento() {
		
		// TODO: obter a matriz da sessão
		
		// TODO: chamar a service de confirmação
	}
	
	@Post
	public void voltarConfiguracaoOriginal() {
		
		// TODO: montar a matriz inicial e setar na sessão
	}
	
	@Post
	public void reprogramarLancamentosSelecionados(List<ProdutoLancamentoDTO> listaProdutoLancamento,
												   String novaDataFormatada, String dataAntigaFormatada) {
		
		// TODO: reprogramar os lançamentos selecionados
		
		// TODO: atualizar a matriz q estava na sessão
		
		// TODO: setar a matriz na sessão
	}
	
	@Post
	public void reprogramarLancamentoUnico(ProdutoLancamentoDTO produtoLancamento,
										   String dataAntigaFormatada) {
		
		// TODO: reprogramar o lançamento informado
		
		// TODO: atualizar a matriz q estava na sessão
		
		// TODO: setar a matriz na sessão
	}	
	
	/**
	 * Obtém o resumo do período de balanceamento de lançamento.
	 */
	private ResultadoResumoBalanceamentoVO obterResumoBalanceamentoLancamento(
											BalanceamentoLancamentoDTO balanceamentoLancamento) {
		
		// TODO: montar o resumo de balanceamento de lançamento
		
		return null;
	}
	
	private void processarBalanceamento(List<ProdutoLancamentoDTO> listaProdutoLancamento,
										PaginacaoVO paginacao) {
		
		TableModel<CellModelKeyValue<ProdutoLancamentoDTO>> tableModel =
			new TableModel<CellModelKeyValue<ProdutoLancamentoDTO>>();
		
		tableModel.setPage(paginacao.getPaginaAtual());
		//tableModel.setTotal(totalRegistros);
		
		List<CellModelKeyValue<ProdutoLancamentoDTO>> listaCellModel =
			new ArrayList<CellModelKeyValue<ProdutoLancamentoDTO>>();
		
		CellModelKeyValue<ProdutoLancamentoDTO> cellModel = null;
		
		for (ProdutoLancamentoDTO dto : listaProdutoLancamento) {
			
			cellModel =
				new CellModelKeyValue<ProdutoLancamentoDTO>(dto.getIdLancamento().intValue(),
															dto);
			
			listaCellModel.add(cellModel);
		}
		
		tableModel.setRows(listaCellModel);
		
		// TODO: montar o DTO ou VO (se necessário) para exibir no grid
		
		// TODO: paginação e ordenação em memória
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	
	//----------------------------------------------------------------------------
	

	/**
	 * Obtém a matriz de balanceamento de balanceamento.
	 * 
	 * @param dataBalanceamento - data de balanceamento
	 * @param listaIdsFornecedores - lista de identificadores dos fornecedores
	 * @return - objeto contendo as informações do balanceamento
	 */
	private BalanceamentoLancamentoDTO obterBalanceamentoRecolhimento(FiltroLancamentoDTO filtro) {
		
		BalanceamentoLancamentoDTO balanceamento = this.matrizLancamentoService.obterMatrizLancamento(filtro);
					
		this.session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO, balanceamento);
		
		
		if (balanceamento == null
				|| balanceamento.getMatrizLancamento() == null
				|| balanceamento.getMatrizLancamento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		return balanceamento;
	}
	
	
	/**
	 * Configura o filtro informado na tela e o armazena na sessão.
	 * 
	 * @param dataPesquisa - data da pesquisa
	 * @param listaIdsFornecedores - lista de identificadores de fornecedores
	 */
	private FiltroLancamentoDTO configurarFiltropesquisa(Date dataPesquisa, List<Long> listaIdsFornecedores) {
		
		FiltroLancamentoDTO filtro =
			new FiltroLancamentoDTO(dataPesquisa, listaIdsFornecedores);
		
		this.session.setAttribute(FILTRO_SESSION_ATTRIBUTE,filtro);
		
		return filtro;
	}
	
	/**
	 * Remove um indicador, que informa se houve reprogramação de produtos, da sessão.
	 */
	private void removerAtributoAlteracaoSessao() {
		
		this.session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);
	}	
	
	/**
	 * Valida os dados da pesquisa.
	 *  
	 * @param numeroSemana - número da semana
	 * @param dataPesquisa - data da pesquisa
	 * @param listaIdsFornecedores - lista de id's dos fornecedores
	 */
	private void validarDadosPesquisa(Date dataPesquisa, List<Long> listaIdsFornecedores) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (dataPesquisa == null) {
			
			listaMensagens.add("O preenchimento do campo [Data] é obrigatório!");
			
		}
		
		if (listaIdsFornecedores == null || listaIdsFornecedores.isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Fornecedor] é obrigatório!");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
		}
	}
	
	
	/**
	 * Obtém o resumo do período de balanceamento de acordo com a data da pesquisa
	 * e a lista de id's dos fornecedores.
	 */
	private ResultadoResumoBalanceamentoVO obterResultadoResumoBalanceamento(
											BalanceamentoLancamentoDTO balanceamentoBalanceamento) {
		
		if (balanceamentoBalanceamento == null
				|| balanceamentoBalanceamento.getMatrizLancamento() == null
				|| balanceamentoBalanceamento.getMatrizLancamento().isEmpty()) {
			
			return null;
		}
		
		List<ResumoPeriodoBalanceamentoVO> resumoPeriodoBalanceamento =
			new ArrayList<ResumoPeriodoBalanceamentoVO>();
		
		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : balanceamentoBalanceamento.getMatrizLancamento().entrySet()) {
			
			Date dataRecolhimento = entry.getKey();
			
			ResumoPeriodoBalanceamentoVO itemResumoPeriodoBalanceamento = new ResumoPeriodoBalanceamentoVO();
			
			itemResumoPeriodoBalanceamento.setData(dataRecolhimento);
			
			List<ProdutoLancamentoDTO> listaProdutosRecolhimento = entry.getValue();
			
			if (listaProdutosRecolhimento != null && !listaProdutosRecolhimento.isEmpty()) {
				
				boolean exibeDestaque = false;
				
				Long qtdeTitulos = Long.valueOf(listaProdutosRecolhimento.size());
				Long qtdeTitulosParciais = 0L;
				
				BigDecimal pesoTotal = BigDecimal.ZERO;
				BigDecimal qtdeExemplares = BigDecimal.ZERO;
				BigDecimal valorTotal = BigDecimal.ZERO;
				
				for (ProdutoLancamentoDTO produtoBalanceamento : listaProdutosRecolhimento) {
					
					if (produtoBalanceamento.getParcial() != null) {
						
						qtdeTitulosParciais++;
					}
					
					if (produtoBalanceamento.getPeso() != null) {
						
						pesoTotal = pesoTotal.add(produtoBalanceamento.getPeso());
					}
					
					if (produtoBalanceamento.getValorTotal() != null) {
						
						valorTotal = valorTotal.add(produtoBalanceamento.getValorTotal());
					}
					
					if (produtoBalanceamento.getRepartePrevisto() != null) {
						
						qtdeExemplares = qtdeExemplares.add(produtoBalanceamento.getRepartePrevisto());
					}
				}
				
				boolean excedeCapacidadeDistribuidor = false;
				
				if (balanceamentoBalanceamento.getCapacidadeDistribuicao() != null) {
				
					excedeCapacidadeDistribuidor =
						(balanceamentoBalanceamento.getCapacidadeDistribuicao()
							.compareTo(qtdeExemplares) == -1);
				}
				
				itemResumoPeriodoBalanceamento.setExcedeCapacidadeDistribuidor(
					excedeCapacidadeDistribuidor);
				
				itemResumoPeriodoBalanceamento.setExibeDestaque(exibeDestaque);
				itemResumoPeriodoBalanceamento.setPesoTotal(pesoTotal);
				itemResumoPeriodoBalanceamento.setQtdeExemplares(MathUtil.round(qtdeExemplares, 2));
				itemResumoPeriodoBalanceamento.setQtdeTitulos(qtdeTitulos);
				
				itemResumoPeriodoBalanceamento.setQtdeTitulosParciais(qtdeTitulosParciais);
				
				itemResumoPeriodoBalanceamento.setValorTotal(valorTotal);
			}
			
			resumoPeriodoBalanceamento.add(itemResumoPeriodoBalanceamento);
		}
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = new ResultadoResumoBalanceamentoVO();
		
		resultadoResumoBalanceamento.setListaResumoPeriodoBalanceamento(resumoPeriodoBalanceamento);
		
		resultadoResumoBalanceamento.setCapacidadeRecolhimentoDistribuidor(
			balanceamentoBalanceamento.getCapacidadeDistribuicao());
		
		return resultadoResumoBalanceamento;
	}
	
	/**
	 * Obtém o filtro para pesquisa da sessão.
	 * 
	 * @return filtro
	 */
	private FiltroLancamentoDTO obterFiltroSessao() {
		
		FiltroLancamentoDTO filtro = (FiltroLancamentoDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro == null) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Filtro para a pesquisa não encontrado!");
		}
		
		return filtro;
	}
	
	@Post
	public void pesquisarMatrizLancamento(String sortorder, String sortname, int page, int rp) {
				
		PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder);
		
		FiltroLancamentoDTO filtro = obterFiltroSessao();
		filtro.setPaginacao(paginacaoVO);
		
		List<LancamentoDTO> dtos = matrizLancamentoService
				.buscarLancamentosBalanceamento(filtro);
		SumarioLancamentosDTO sumario = matrizLancamentoService
				.sumarioBalanceamentoMatrizLancamentos(filtro.getData(), filtro.getIdsFornecedores());

		TableModel<CellModelKeyValue<LancamentoDTO>> tm = new TableModel<CellModelKeyValue<LancamentoDTO>>();
		List<CellModelKeyValue<LancamentoDTO>> cells = CellModelKeyValue
				.toCellModelKeyValue(dtos);

		tm.setRows(cells);
		tm.setPage(page);
		tm.setTotal(sumario.getTotalLancamentos().intValue());
		Object[] resultado = {tm, sumario.getValorTotalFormatado()};
		result.use(Results.json()).withoutRoot().from(resultado).serialize();
	}

	@Get
	public void resumoPeriodo(Date dataInicial, List<Long> idsFornecedores) {
		
		verificarCamposObrigatorios(dataInicial, idsFornecedores);
		List<ResumoPeriodoBalanceamentoDTO> dtos = matrizLancamentoService
				.obterResumoPeriodo(dataInicial, idsFornecedores);
		result.use(Results.json()).withoutRoot().from(dtos).serialize();
	}

	private void verificarCamposObrigatorios(Date data,
			List<Long> idsFornecedores) {
		Date atual = DateUtil.removerTimestamp(new Date());
		List<String> mensagens = new ArrayList<String>();
		if (idsFornecedores == null || idsFornecedores.isEmpty()) {
			mensagens.add(localization.getMessage(CAMPO_REQUERIDO_KEY,
					"Fornecedor"));
		}
		if (data == null) {
			mensagens.add(localization.getMessage(CAMPO_REQUERIDO_KEY,
					"Data de Lançamento Matriz/Distribuidor"));
		} else if (data.before(atual)) {
			mensagens.add(localization.getMessage(CAMPO_MAIOR_IGUAL_KEY,
					"Data de Lançamento Matriz/Distribuidor", DateUtil.formatarDataPTBR(atual)));
		}
		if (!mensagens.isEmpty()) {
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.ERROR, mensagens);
			throw new ValidacaoException(validacao);
		}
	}

}
