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
import br.com.abril.nds.client.vo.ValidacaoVO;
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
	
	private static final String ATRIBUTO_SESSAO_MAPA_RECOLHIMENTO = "mapaRecolhimento";
	
	private static final String ATRIBUTO_SESSAO_MAPA_RECOLHIMENTO_INICIAL = "mapaRecolhimentoInicial";
	
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
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizBalanceamento = 
			this.obterMatrizBalanceamento(dataPesquisa, listaIdsFornecedores);

		this.httpSession.setAttribute(ATRIBUTO_SESSAO_MAPA_RECOLHIMENTO_INICIAL, matrizBalanceamento);
		
		List<ResumoPeriodoBalanceamentoDTO> resumoPeriodoBalanceamento = 
			this.obterResumoPeriodoBalanceamento(matrizBalanceamento);
		
		this.result.use(Results.json()).from(resumoPeriodoBalanceamento, "result").serialize();
	}
	
	@Post
	@Path("/configurarNovaDataRecolhimento")
	public void configurarNovaDataRecolhimento() {
		
		
	}
	
	@Post
	@Path("/reprogramar")
	public void reprogramar() {
		
		
	}
	
	@Post
	@Path("/exibirMatrizBalanceamento")
	public void exibirMatrizBalanceamento() {
		
	}
	
	@Post
	@Path("/exibirMatrizBalanceamentoPorDia")
	@SuppressWarnings("unchecked")
	public void exibirMatrizBalanceamentoDoDia(String dataFormatada, String sortorder,
											   String sortname, Integer page, Integer rp) {

		if (dataFormatada == null || dataFormatada.trim().isEmpty()) {
		
			return;
		}
		
		Date data = DateUtil.parseDataPTBR(dataFormatada);

		Map<Date, List<ProdutoRecolhimentoDTO>> matrizBalanceamento =
			(Map<Date, List<ProdutoRecolhimentoDTO>>)
				httpSession.getAttribute(ATRIBUTO_SESSAO_MAPA_RECOLHIMENTO);
		
		List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDia = matrizBalanceamento.get(data);
		
		if (listaProdutoRecolhimentoDia != null && !listaProdutoRecolhimentoDia.isEmpty()) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			processarBalanceamento(listaProdutoRecolhimentoDia, paginacao, sortname);
		}
	}
	
	private void processarBalanceamento(List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDia,
										PaginacaoVO paginacao, String sortname) {
		
		List<ProdutoRecolhimentoVO> listaProdutoRecolhimentoVO =
			new LinkedList<ProdutoRecolhimentoVO>();
		
		ProdutoRecolhimentoVO produtoRecolhimentoVO = null;
		
		for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimentoDia) {
			
			produtoRecolhimentoVO = new ProdutoRecolhimentoVO();
			
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
			
//			produtoRecolhimentoVO.setPrecoCapa(
//				CurrencyUtil.formatarValor(produtoRecolhimentoDTO.get));

			produtoRecolhimentoVO.setFornecedor(produtoRecolhimentoDTO.getNomeFornecedor());
			
			produtoRecolhimentoVO.setEditor(produtoRecolhimentoDTO.getNomeEditor());
			
			if (produtoRecolhimentoDTO.getParcial() != null) {
				produtoRecolhimentoVO.setParcial(produtoRecolhimentoDTO.getParcial().toString());
			} else {
				produtoRecolhimentoVO.setParcial("");
			}
				
//			produtoRecolhimentoVO.setBrinde(produtoRecolhimentoDTO.get);

			produtoRecolhimentoVO.setDataLancamento(
				DateUtil.formatarDataPTBR(produtoRecolhimentoDTO.getDataLancamento()));

			produtoRecolhimentoVO.setDataRecolhimento(
				DateUtil.formatarDataPTBR(produtoRecolhimentoDTO.getDataRecolhimentoDistribuidor()));
			
			if (produtoRecolhimentoDTO.getExpectativaEncalheSede() != null) {
				produtoRecolhimentoVO.setSede(
					CurrencyUtil.formatarValor(produtoRecolhimentoDTO.getExpectativaEncalheSede()));
			} else {
				produtoRecolhimentoVO.setSede("");
			}
			
			if (produtoRecolhimentoDTO.getExpectativaEncalheAtendida() != null) {
				produtoRecolhimentoVO.setAtendida(
					CurrencyUtil.formatarValor(produtoRecolhimentoDTO.getExpectativaEncalheAtendida()));
			} else {
				produtoRecolhimentoVO.setAtendida("");
			}
			
			if (produtoRecolhimentoDTO.getExpectativaEncalhe() != null) {
				produtoRecolhimentoVO.setQtdeExemplares(
					CurrencyUtil.formatarValor(produtoRecolhimentoDTO.getExpectativaEncalhe()));
			} else {
				produtoRecolhimentoVO.setQtdeExemplares("");
			}
			
			produtoRecolhimentoVO.setValorTotal(
				CurrencyUtil.formatarValor(produtoRecolhimentoDTO.getValorTotal()));
			
			produtoRecolhimentoVO.setNovaData("");
			
			listaProdutoRecolhimentoVO.add(produtoRecolhimentoVO);
		}
		
		int totalRegistros = listaProdutoRecolhimentoVO.size();
		
		listaProdutoRecolhimentoVO =
			PaginacaoUtil.paginarEOrdenarEmMemoria(listaProdutoRecolhimentoVO, paginacao, sortname);
		
		TableModel<CellModelKeyValue<ProdutoRecolhimentoVO>> tableModel =
			new TableModel<CellModelKeyValue<ProdutoRecolhimentoVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaProdutoRecolhimentoVO));
		tableModel.setPage(paginacao.getPaginaAtual());
		tableModel.setTotal(totalRegistros);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	@Path("/balancearPorEditor")
	@SuppressWarnings("unchecked")
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
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizBalanceamentoEditor =
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		this.httpSession.setAttribute(ATRIBUTO_SESSAO_MAPA_RECOLHIMENTO_INICIAL, matrizBalanceamentoEditor);
		
		List<ResumoPeriodoBalanceamentoDTO> resumoPeriodoBalanceamento = 
			this.obterResumoPeriodoBalanceamento(matrizBalanceamentoEditor);
		
		this.result.use(Results.json()).from(resumoPeriodoBalanceamento, "result").serialize();
	}
	
	@Post
	@Path("/balancearPorValor")
	public void balancearPorValor(Integer numeroSemana, List<Long> listaIdsFornecedores) {
		
		this.recolhimentoService.obterMatrizBalanceamento(numeroSemana,
														  listaIdsFornecedores,
														  TipoBalanceamentoRecolhimento.VALOR);
	}
	
	@Post
	@Path("/voltarConfiguracaoOriginal")
	public void voltarConfiguracaoOriginal() {
		
		
	}
	
	@Post
	@Path("/confirmar")
	public void confirmar() {
		
		
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
	@SuppressWarnings("unchecked")
	private Map<Date, List<ProdutoRecolhimentoDTO>> obterMatrizBalanceamento(Date dataBalanceamento, 
																	  		 List<Long> listaIdsFornecedores) {

		Map<Date, List<ProdutoRecolhimentoDTO>> matrizBalanceamento =  
			(Map<Date, List<ProdutoRecolhimentoDTO>>)
				this.httpSession.getAttribute(ATRIBUTO_SESSAO_MAPA_RECOLHIMENTO);
		
		if (matrizBalanceamento == null 
				&& dataBalanceamento != null 
				&& listaIdsFornecedores != null) {

			matrizBalanceamento = this.obterMatrizBalanceamentoMock(dataBalanceamento, listaIdsFornecedores);
			
			this.httpSession.setAttribute(ATRIBUTO_SESSAO_MAPA_RECOLHIMENTO, matrizBalanceamento);
		}
		
		if (matrizBalanceamento == null || matrizBalanceamento.isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		return matrizBalanceamento;
	}
	
	/*
	 * Verifica se já existe a matriz na sessão.
	 */
	private Map<Date, List<ProdutoRecolhimentoDTO>> obterMatrizBalanceamento() {
		
		return this.obterMatrizBalanceamento(null, null); 
	}
	
	/*
	 * MOCK:
	 * Verifica se já existe a matriz na sessão, caso contrário irá criá-la a partir do dia parametrizado.
	 */
	private Map<Date, List<ProdutoRecolhimentoDTO>> obterMatrizBalanceamentoMock(Date dataBalanceamento, 
																	  	  		 List<Long> listaIdsFornecedores) {

		Map<Date, List<ProdutoRecolhimentoDTO>> matrizBalanceamento = 
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		Date dataLancamento = DateUtil.parseDataPTBR("11/04/2012");

		for (int diaRecolhimento = 18; diaRecolhimento <= 24; diaRecolhimento++) {
		
			Date dataRecolhimento = DateUtil.parseDataPTBR(diaRecolhimento + "/04/2012");
			
			List<ProdutoRecolhimentoDTO> listaProdutosRecolhimento = new ArrayList<ProdutoRecolhimentoDTO>();
			
			for (int i = 1; i <= 100; i++) {
				
				ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
				
				Produto produto = new Produto();
				
				produto.setCodigo("" + i);
				produto.setDescricao("Produto " + i);
				
				ProdutoEdicao produtoEdicao = new ProdutoEdicao();
				
				produtoEdicao.setNumeroEdicao(1L);
				produtoEdicao.setPeso(new BigDecimal(i));
				produtoEdicao.setPossuiBrinde(false);
				produtoEdicao.setPrecoVenda(new BigDecimal(i));
				
				produtoEdicao.setProduto(produto);
				
				produtoRecolhimento.setSequencia((long) i);
				produtoRecolhimento.setExpectativaEncalheAtendida(BigDecimal.ZERO);
				produtoRecolhimento.setDataLancamento(dataLancamento);
				produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
				produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
				produtoRecolhimento.setNomeEditor("Zé Editor " + i);
				produtoRecolhimento.setNomeFornecedor("Zé Fornecedor " + i);
				produtoRecolhimento.setExpectativaEncalhe(new BigDecimal(i));
				produtoRecolhimento.setValorTotal(new BigDecimal(i));
				produtoRecolhimento.setProdutoEdicao(produtoEdicao);
				
				listaProdutosRecolhimento.add(produtoRecolhimento);
			}
			
			matrizBalanceamento.put(dataRecolhimento, listaProdutosRecolhimento);
		}
		
		return matrizBalanceamento; 
	}

	/*
	 * Retorna os dados do recolhimento referente a um dia especifico.
	 */
	private List<ProdutoRecolhimentoDTO> obterBalanceamentoDia(Date dataRecolhimento, 
															   List<Long> listaIdsFornecedores) {
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizBalanceamento = 
			this.obterMatrizBalanceamento(dataRecolhimento, listaIdsFornecedores);

		if (matrizBalanceamento != null) {
			
			return matrizBalanceamento.get(dataRecolhimento);
		}
		
		return null;
	}
	
	/*
	 * Obtém o resumo do período de balanceamento de acordo com a data da pesquisa
	 * e a lista de id's dos fornecedores.
	 */
	private List<ResumoPeriodoBalanceamentoDTO> obterResumoPeriodoBalanceamento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizBalanceamento) {

		if (matrizBalanceamento == null || matrizBalanceamento.isEmpty()) {
			
			return null;
		}
		
		List<ResumoPeriodoBalanceamentoDTO> resumoPeriodoBalanceamento =
			new ArrayList<ResumoPeriodoBalanceamentoDTO>();
		
		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizBalanceamento.entrySet()) {
			
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
		
		return resumoPeriodoBalanceamento;
	}
	
}