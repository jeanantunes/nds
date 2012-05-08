package br.com.abril.nds.controllers.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ProdutoRecolhimentoVO;
import br.com.abril.nds.client.vo.ResultadoResumoBalanceamentoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoBalanceamentoRecolhimento;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pela Matriz de Recolhimento.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/devolucao/balanceamentoMatriz")
public class MatrizRecolhimentoController {

	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private Result result;

	@Autowired
	private RecolhimentoService recolhimentoService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO = "balanceamentoRecolhimento";
	
	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO_INICIAL = "balanceamentoRecolhimentoInicial";
	
	@Get
	@Path("/")
	public void index() {
		
		List<Fornecedor> fornecedores = this.fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO);

		this.result.include("fornecedores", fornecedores);
	}
	
	@Post
	@Path("/pesquisar")
	public void pesquisar(Integer numeroSemana, Date dataPesquisa, List<Long> listaIdsFornecedores) {
		
		this.validarDadosPesquisa(dataPesquisa, listaIdsFornecedores);
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			this.obterBalanceamentoRecolhimentoInicial(null, dataPesquisa, listaIdsFornecedores);
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	@Path("/exibirMatrizBalanceamentoPorDia")
	public void exibirMatrizBalanceamentoDoDia(String dataFormatada, String sortorder,
											   String sortname, Integer page, Integer rp) {

		if (dataFormatada == null || dataFormatada.trim().isEmpty()) {
		
			return;
		}
		
		Date data = DateUtil.parseDataPTBR(dataFormatada);

		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento =
			(BalanceamentoRecolhimentoDTO)
				httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
		
		if (balanceamentoRecolhimento != null
				&& balanceamentoRecolhimento.getMatrizRecolhimento() != null) {
			
			List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDia =
				balanceamentoRecolhimento.getMatrizRecolhimento().get(data);
			
			if (listaProdutoRecolhimentoDia != null && !listaProdutoRecolhimentoDia.isEmpty()) {
			
				PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
				
				processarBalanceamento(listaProdutoRecolhimentoDia, balanceamentoRecolhimento.isMatrizFechada(),
									   paginacao, sortname);
			}
		}
	}
	
	@Post
	@Path("/confirmar")
	public void confirmar() {
		
		
	}
	
	@Post
	@Path("/balancearPorEditor")
	public void balancearPorEditor() {
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizBalanceamentoAtual = this.obterMatrizBalanceamento();
		
		Map<Date, Long> mapaRecolhimentoEditor = new HashMap<Date, Long>();
		
		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizBalanceamentoAtual.entrySet()) {
			
			Date dataRecolhimento = entry.getKey();
			
			List<ProdutoRecolhimentoDTO> listaProdutosRecolhimento = entry.getValue();
			
			if (listaProdutosRecolhimento == null || listaProdutosRecolhimento.isEmpty()) {
				
				continue;
			}
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : listaProdutosRecolhimento) {
				
				//mapaRecolhimentoEditor.put(entry.getKey(), k);
			}
			
			
		}
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = new BalanceamentoRecolhimentoDTO();
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoEditor =
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		balanceamentoRecolhimento.setMatrizRecolhimento(matrizRecolhimentoEditor);
		
		this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO_INICIAL,
									  balanceamentoRecolhimento);
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").serialize();
	}
	
	@Post
	@Path("/balancearPorValor")
	public void balancearPorValor(Integer numeroSemana, List<Long> listaIdsFornecedores) {
		
		this.recolhimentoService.obterMatrizBalanceamento(numeroSemana,
														  listaIdsFornecedores,
														  TipoBalanceamentoRecolhimento.VALOR);
	}
	
	@Post
	@Path("/salvar")
	public void salvar() {
		
		
	}
	
	@Post
	@Path("/exibirMatrizFornecedor")
	public void exibirMatrizFornecedor() {
		
	}
	
	@Post
	@Path("/voltarConfiguracaoOriginal")
	public void voltarConfiguracaoOriginal() {
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			(BalanceamentoRecolhimentoDTO)
				this.httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO_INICIAL);
		
		balanceamentoRecolhimento = 
			this.obterBalanceamentoRecolhimentoInicial(balanceamentoRecolhimento, null, null);
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	@Path("/reprogramar")
	public void reprogramar(List<ProdutoRecolhimentoVO> listaProdutoRecolhimento) {
		
		System.out.println(listaProdutoRecolhimento.size());
		
		//TODO:
	}
	
	@Post
	@Path("/configurarNovaDataRecolhimento")
	public void configurarNovaDataRecolhimento() {
		
		
	}
	
	private void processarBalanceamento(List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDia,
										boolean matrizFechada, PaginacaoVO paginacao, String sortname) {
		
		List<ProdutoRecolhimentoVO> listaProdutoRecolhimentoVO =
			new LinkedList<ProdutoRecolhimentoVO>();
		
		ProdutoRecolhimentoVO produtoRecolhimentoVO = null;
		
		for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimentoDia) {
			
			produtoRecolhimentoVO = new ProdutoRecolhimentoVO();
			
			produtoRecolhimentoVO.setIdLancamento(produtoRecolhimentoDTO.getIdLancamento().toString());
			
			if (produtoRecolhimentoDTO.getSequencia() != null) {
				produtoRecolhimentoVO.setSequencia(
					produtoRecolhimentoDTO.getSequencia().toString());
			} else {
				produtoRecolhimentoVO.setSequencia(null);
			}
				
			produtoRecolhimentoVO.setCodigoProduto(
				produtoRecolhimentoDTO.getProdutoEdicao().getProduto().getCodigo());
			
			produtoRecolhimentoVO.setNomeProduto(
				produtoRecolhimentoDTO.getProdutoEdicao().getProduto().getNome());
			
			produtoRecolhimentoVO.setNumeroEdicao(
				produtoRecolhimentoDTO.getProdutoEdicao().getNumeroEdicao().toString());
			
			produtoRecolhimentoVO.setPrecoVenda(
				CurrencyUtil.formatarValor(produtoRecolhimentoDTO.getProdutoEdicao().getPrecoVenda()));

			produtoRecolhimentoVO.setNomeFornecedor(produtoRecolhimentoDTO.getNomeFornecedor());
			
			produtoRecolhimentoVO.setNomeEditor(produtoRecolhimentoDTO.getNomeEditor());
			
			if (produtoRecolhimentoDTO.getParcial() != null) {
				produtoRecolhimentoVO.setParcial(produtoRecolhimentoDTO.getParcial().toString());
			} else {
				produtoRecolhimentoVO.setParcial(
					(produtoRecolhimentoDTO.getParcial() != null) ? "Sim" : "Não");
			}
				
			produtoRecolhimentoVO.setBrinde(
				(produtoRecolhimentoDTO.getProdutoEdicao().isPossuiBrinde()) ? "Sim" : "Não");

			produtoRecolhimentoVO.setDataLancamento(
				DateUtil.formatarDataPTBR(produtoRecolhimentoDTO.getDataLancamento()));

			produtoRecolhimentoVO.setDataRecolhimento(
				DateUtil.formatarDataPTBR(produtoRecolhimentoDTO.getDataRecolhimentoDistribuidor()));
			
			if (produtoRecolhimentoDTO.getExpectativaEncalheSede() != null) {
				produtoRecolhimentoVO.setEncalheSede(
					CurrencyUtil.formatarValor(produtoRecolhimentoDTO.getExpectativaEncalheSede()));
			} else {
				produtoRecolhimentoVO.setEncalheSede("");
			}
			
			if (produtoRecolhimentoDTO.getExpectativaEncalheAtendida() != null) {
				produtoRecolhimentoVO.setEncalheAtendida(
					CurrencyUtil.formatarValor(produtoRecolhimentoDTO.getExpectativaEncalheAtendida()));
			} else {
				produtoRecolhimentoVO.setEncalheAtendida("");
			}
			
			if (produtoRecolhimentoDTO.getExpectativaEncalhe() != null) {
				produtoRecolhimentoVO.setEncalhe(
					CurrencyUtil.formatarValor(produtoRecolhimentoDTO.getExpectativaEncalhe()));
			} else {
				produtoRecolhimentoVO.setEncalhe("");
			}
			
			produtoRecolhimentoVO.setValorTotal(
				CurrencyUtil.formatarValor(produtoRecolhimentoDTO.getValorTotal()));
			
			if (produtoRecolhimentoDTO.getNovaData() != null) {
				produtoRecolhimentoVO.setNovaData(
					DateUtil.formatarDataPTBR(produtoRecolhimentoDTO.getNovaData()));
			} else {
				produtoRecolhimentoVO.setNovaData("");
			}
			
			produtoRecolhimentoVO.setBloqueioMatrizFechada(matrizFechada);
			
			produtoRecolhimentoVO.setBloqueioDataRecolhimento(
				produtoRecolhimentoDTO.isPossuiChamada());
			
			listaProdutoRecolhimentoVO.add(produtoRecolhimentoVO);
		}
		
		int totalRegistros = listaProdutoRecolhimentoVO.size();
		
		listaProdutoRecolhimentoVO =
			PaginacaoUtil.paginarEOrdenarEmMemoria(listaProdutoRecolhimentoVO, paginacao, sortname);
		
		TableModel<CellModelKeyValue<ProdutoRecolhimentoVO>> tableModel =
			new TableModel<CellModelKeyValue<ProdutoRecolhimentoVO>>();
		
		tableModel.setPage(paginacao.getPaginaAtual());
		tableModel.setTotal(totalRegistros);
		
		List<CellModelKeyValue<ProdutoRecolhimentoVO>> listaCellModel =
			new ArrayList<CellModelKeyValue<ProdutoRecolhimentoVO>>();
		
		CellModelKeyValue<ProdutoRecolhimentoVO> cellModel = null;
		
		for (ProdutoRecolhimentoVO vo : listaProdutoRecolhimentoVO) {
			
			cellModel =
				new CellModelKeyValue<ProdutoRecolhimentoVO>(Integer.valueOf(vo.getIdLancamento()), vo);
			
			listaCellModel.add(cellModel);
		}
		
		tableModel.setRows(listaCellModel);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	/*
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
	
	/*
	 * Verifica se já existe a matriz na sessão, caso contrário irá buscá-la a partir dos parâmetros.
	 */
	private BalanceamentoRecolhimentoDTO obterBalanceamentoRecolhimentoInicial(
												BalanceamentoRecolhimentoDTO balanceamentoRecolhimento,
												Date dataBalanceamento, 
												List<Long> listaIdsFornecedores) {

		if ((balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento() == null)
				&& dataBalanceamento != null 
				&& listaIdsFornecedores != null) {

			balanceamentoRecolhimento = 
				this.obterBalanceamentoRecolhimentoMock(dataBalanceamento, listaIdsFornecedores);
			
			this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO_INICIAL,
										  balanceamentoRecolhimento);
			
			this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO,
										  balanceamentoRecolhimento);
		}
		
		if (balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento() == null) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		return balanceamentoRecolhimento;
	}
	
	/*
	 * Verifica se já existe a matriz na sessão.
	 */
	private Map<Date, List<ProdutoRecolhimentoDTO>> obterMatrizBalanceamento() {
		
//		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento =
//			this.obterBalanceamentoRecolhimento(null, null);
//		
//		if (balanceamentoRecolhimento == null) {
//			
//			return null;
//		}
		
//		return balanceamentoRecolhimento.getMatrizRecolhimento();
		
		return null;
	}
	
	/*
	 * MOCK:
	 * Verifica se já existe a matriz na sessão, caso contrário irá criá-la a partir do dia parametrizado.
	 */
	private BalanceamentoRecolhimentoDTO obterBalanceamentoRecolhimentoMock(Date dataBalanceamento, 
																	 		List<Long> listaIdsFornecedores) {

		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = 
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		Date dataLancamento = DateUtil.parseDataPTBR("11/04/2012");

		for (int diaRecolhimento = 18; diaRecolhimento <= 24; diaRecolhimento++) {
		
			Date dataRecolhimento = DateUtil.parseDataPTBR(diaRecolhimento + "/04/2012");
			
			List<ProdutoRecolhimentoDTO> listaProdutosRecolhimento = new ArrayList<ProdutoRecolhimentoDTO>();
			
			for (int i = 1; i <= 100; i++) {
				
				ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
				
				Produto produto = new Produto();
				
				produto.setCodigo("" + i);
				produto.setNome("Produto " + i);
				
				ProdutoEdicao produtoEdicao = new ProdutoEdicao();
				
				produtoEdicao.setNumeroEdicao(1L);
				produtoEdicao.setPeso(new BigDecimal(i));
				produtoEdicao.setPossuiBrinde(false);
				produtoEdicao.setPrecoVenda(new BigDecimal(i));
				
				produtoEdicao.setProduto(produto);
				
				produtoRecolhimento.setIdLancamento((long) i);
				produtoRecolhimento.setSequencia((long) i);
				produtoRecolhimento.setExpectativaEncalheAtendida(BigDecimal.ZERO);
				produtoRecolhimento.setExpectativaEncalheSede(BigDecimal.ZERO);
				produtoRecolhimento.setDataLancamento(dataLancamento);
				produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
				produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
				produtoRecolhimento.setNomeEditor("Zé Editor " + i);
				produtoRecolhimento.setNomeFornecedor("Zé Fornecedor " + i);
				produtoRecolhimento.setExpectativaEncalhe(new BigDecimal(i));
				produtoRecolhimento.setValorTotal(new BigDecimal(i));
				produtoRecolhimento.setProdutoEdicao(produtoEdicao);
				produtoRecolhimento.setPossuiChamada(false);
				
				listaProdutosRecolhimento.add(produtoRecolhimento);
			}
			
			matrizRecolhimento.put(dataRecolhimento, listaProdutosRecolhimento);
		}
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = new BalanceamentoRecolhimentoDTO();
		
		balanceamentoRecolhimento.setMatrizRecolhimento(matrizRecolhimento);
		
		balanceamentoRecolhimento.setMatrizFechada(false);
		
		return balanceamentoRecolhimento;
	}

	/*
	 * Retorna os dados do recolhimento referente a um dia especifico.
	 */
	private List<ProdutoRecolhimentoDTO> obterBalanceamentoDia(Date dataRecolhimento, 
															   List<Long> listaIdsFornecedores) {
		
//		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
//			this.obterBalanceamentoRecolhimento(dataRecolhimento, listaIdsFornecedores);
//
//		if (balanceamentoRecolhimento != null
//				&& balanceamentoRecolhimento.getMatrizRecolhimento() != null) {
//			
//			return balanceamentoRecolhimento.getMatrizRecolhimento().get(dataRecolhimento);
//		}
		
		return null;
	}
	
	/*
	 * Obtém o resumo do período de balanceamento de acordo com a data da pesquisa
	 * e a lista de id's dos fornecedores.
	 */
	private ResultadoResumoBalanceamentoVO obterResultadoResumoBalanceamento(
											BalanceamentoRecolhimentoDTO balanceamentoRecolhimento) {
		
		if (balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento() == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
			
			return null;
		}
		
		List<ResumoPeriodoBalanceamentoDTO> resumoPeriodoBalanceamento =
			new ArrayList<ResumoPeriodoBalanceamentoDTO>();
		
		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : balanceamentoRecolhimento.getMatrizRecolhimento().entrySet()) {
			
			Date dataRecolhimento = entry.getKey();
			
			ResumoPeriodoBalanceamentoDTO itemResumoPeriodoBalanceamento = new ResumoPeriodoBalanceamentoDTO();
			
			itemResumoPeriodoBalanceamento.setData(dataRecolhimento);
			
			List<ProdutoRecolhimentoDTO> listaProdutosRecolhimento = entry.getValue();
			
			if (listaProdutosRecolhimento != null && !listaProdutosRecolhimento.isEmpty()) {
				
				boolean exibeDestaque = false;
				
				Long qtdeTitulos = Long.valueOf(listaProdutosRecolhimento.size());
				Long qtdeTitulosParciais = 0L;
				
				BigDecimal pesoTotal = BigDecimal.ZERO;
				BigDecimal qtdeExemplares = BigDecimal.ZERO;
				BigDecimal valorTotal = BigDecimal.ZERO;
				
				for (ProdutoRecolhimentoDTO produtoRecolhimento : listaProdutosRecolhimento) {
					
					if (produtoRecolhimento.getExpectativaEncalheAtendida() != null
							&& produtoRecolhimento.getExpectativaEncalheAtendida().doubleValue() > 0) {
						
						exibeDestaque = true;
					}
					
					if (produtoRecolhimento.getParcial() != null) {
						
						qtdeTitulosParciais++;
					}
					
					if (produtoRecolhimento.getProdutoEdicao().getPeso() != null) {
						
						pesoTotal = pesoTotal.add(produtoRecolhimento.getProdutoEdicao().getPeso());
					}
					
					if (produtoRecolhimento.getValorTotal() != null) {
						
						valorTotal = valorTotal.add(produtoRecolhimento.getValorTotal());
					}
				}
				
				itemResumoPeriodoBalanceamento.setExibeDestaque(exibeDestaque);
				itemResumoPeriodoBalanceamento.setPesoTotal(pesoTotal);
				itemResumoPeriodoBalanceamento.setQtdeExemplares(qtdeExemplares);
				itemResumoPeriodoBalanceamento.setQtdeTitulos(qtdeTitulos);
				itemResumoPeriodoBalanceamento.setQtdeTitulosParciais(qtdeTitulosParciais);
				itemResumoPeriodoBalanceamento.setValorTotal(valorTotal);
			}
			
			resumoPeriodoBalanceamento.add(itemResumoPeriodoBalanceamento);
		}
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = new ResultadoResumoBalanceamentoVO();
		
		resultadoResumoBalanceamento.setMatrizFechada(balanceamentoRecolhimento.isMatrizFechada());
		
		resultadoResumoBalanceamento.setListaResumoPeriodoBalanceamento(resumoPeriodoBalanceamento);
		
		return resultadoResumoBalanceamento;
	}
	
}