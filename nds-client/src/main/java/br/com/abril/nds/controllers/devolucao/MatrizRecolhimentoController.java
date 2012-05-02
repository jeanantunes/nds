package br.com.abril.nds.controllers.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.RecolhimentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
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
		
		Map<Date, List<RecolhimentoDTO>> matrizBalanceamento = 
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
	public void exibirMatrizBalanceamentoDoDia() {

	}
	
	@Post
	@Path("/balancearPorEditor")
	@SuppressWarnings("unchecked")
	public void balancearPorEditor() {
		
		Map<Date, List<RecolhimentoDTO>> matrizBalanceamentoAtual = this.obterMatrizBalanceamento();
		
		Map<Date, Long> mapaRecolhimentoEditor = new HashMap<Date, Long>();
		
		for (Map.Entry<Date, List<RecolhimentoDTO>> entry : matrizBalanceamentoAtual.entrySet()) {
			
			Date dataRecolhimento = entry.getKey();
			
			List<RecolhimentoDTO> listaDadosRecolhimento = entry.getValue();
			
			if (listaDadosRecolhimento == null || listaDadosRecolhimento.isEmpty()) {
				
				continue;
			}
			
			for (RecolhimentoDTO dadosRecolhimento : listaDadosRecolhimento) {
				
				//mapaRecolhimentoEditor.put(entry.getKey(), k);
			}
			
			
		}
		
		Map<Date, List<RecolhimentoDTO>> matrizBalanceamentoEditor = new TreeMap<Date, List<RecolhimentoDTO>>();
		
		this.httpSession.setAttribute(ATRIBUTO_SESSAO_MAPA_RECOLHIMENTO_INICIAL, matrizBalanceamentoEditor);
		
		List<ResumoPeriodoBalanceamentoDTO> resumoPeriodoBalanceamento = 
			this.obterResumoPeriodoBalanceamento(matrizBalanceamentoEditor);
		
		this.result.use(Results.json()).from(resumoPeriodoBalanceamento, "result").serialize();
	}
	
	@Post
	@Path("/balancearPorValor")
	public void balancearPorValor() {
		
		
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
	private Map<Date, List<RecolhimentoDTO>> obterMatrizBalanceamento(Date dataBalanceamento, 
																	  List<Long> listaIdsFornecedores) {

		Map<Date, List<RecolhimentoDTO>> matrizBalanceamento =  
			(Map<Date, List<RecolhimentoDTO>>) this.httpSession.getAttribute(ATRIBUTO_SESSAO_MAPA_RECOLHIMENTO);
		
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
	private Map<Date, List<RecolhimentoDTO>> obterMatrizBalanceamento() {
		
		return this.obterMatrizBalanceamento(null, null); 
	}
	
	/*
	 * MOCK:
	 * Verifica se já existe a matriz na sessão, caso contrário irá criá-la a partir do dia parametrizado.
	 */
	private Map<Date, List<RecolhimentoDTO>> obterMatrizBalanceamentoMock(Date dataBalanceamento, 
																	  	  List<Long> listaIdsFornecedores) {

		Map<Date, List<RecolhimentoDTO>> matrizBalanceamento = new TreeMap<Date, List<RecolhimentoDTO>>();
		
		Date dataLancamento = DateUtil.parseDataPTBR("11/04/2012");

		for (int diaRecolhimento = 18; diaRecolhimento <= 24; diaRecolhimento++) {
		
			Date dataRecolhimento = DateUtil.parseDataPTBR(diaRecolhimento + "/04/2012");
			
			List<RecolhimentoDTO> listaDadosRecolhimento = new ArrayList<RecolhimentoDTO>();
			
			for (int i = 1; i <= 100; i++) {
				
				RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
				
				Produto produto = new Produto();
				
				produto.setCodigo("" + i);
				produto.setDescricao("Produto " + i);
				
				ProdutoEdicao produtoEdicao = new ProdutoEdicao();
				
				produtoEdicao.setNumeroEdicao(1L);
				produtoEdicao.setPeso(new BigDecimal(i));
				produtoEdicao.setPossuiBrinde(false);
				produtoEdicao.setPrecoVenda(new BigDecimal(i));
				
				produtoEdicao.setProduto(produto);
				
				dadosRecolhimento.setAtendida(BigDecimal.ZERO);
				dadosRecolhimento.setDataLancamento(dataLancamento);
				dadosRecolhimento.setDataRecolhimento(dataRecolhimento);
				dadosRecolhimento.setNomeEditor("Zé Editor " + i);
				dadosRecolhimento.setNomeFornecedor("Zé Fornecedor " + i);
				dadosRecolhimento.setQtdeExemplares(new BigDecimal(i));
				dadosRecolhimento.setSede(new BigDecimal(i));
				dadosRecolhimento.setValorTotal(new BigDecimal(i));
				dadosRecolhimento.setProdutoEdicao(produtoEdicao);
				
				listaDadosRecolhimento.add(dadosRecolhimento);
			}
			
			matrizBalanceamento.put(dataRecolhimento, listaDadosRecolhimento);
		}
		
		return matrizBalanceamento; 
	}

	/*
	 * Retorna os dados do recolhimento referente a um dia especifico.
	 */
	private List<RecolhimentoDTO> obterBalanceamentoDia(Date dataRecolhimento, 
														List<Long> listaIdsFornecedores) {
		
		Map<Date, List<RecolhimentoDTO>> matrizBalanceamento = 
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
	private List<ResumoPeriodoBalanceamentoDTO> obterResumoPeriodoBalanceamento(Map<Date, List<RecolhimentoDTO>> matrizBalanceamento) {

		if (matrizBalanceamento == null || matrizBalanceamento.isEmpty()) {
			
			return null;
		}
		
		List<ResumoPeriodoBalanceamentoDTO> resumoPeriodoBalanceamento =
			new ArrayList<ResumoPeriodoBalanceamentoDTO>();
		
		for (Map.Entry<Date, List<RecolhimentoDTO>> entry : matrizBalanceamento.entrySet()) {
			
			Date dataRecolhimento = entry.getKey();
			
			ResumoPeriodoBalanceamentoDTO itemResumoPeriodoBalanceamento = new ResumoPeriodoBalanceamentoDTO();
			
			itemResumoPeriodoBalanceamento.setData(dataRecolhimento);
			
			List<RecolhimentoDTO> listaDadosRecolhimento = entry.getValue();
			
			if (listaDadosRecolhimento != null && !listaDadosRecolhimento.isEmpty()) {
				
				boolean exibeDestaque = false;
				
				Long qtdeTitulos = Long.valueOf(listaDadosRecolhimento.size());
				Long qtdeTitulosParciais = 0L;
				
				BigDecimal pesoTotal = BigDecimal.ZERO;
				BigDecimal qtdeExemplares = BigDecimal.ZERO;
				BigDecimal valorTotal = BigDecimal.ZERO;
				
				for (RecolhimentoDTO dadosRecolhimento : listaDadosRecolhimento) {
					
					if (dadosRecolhimento.getAtendida() != null
							&& dadosRecolhimento.getAtendida().doubleValue() > 0) {
						
						exibeDestaque = true;
					}
					
					if (dadosRecolhimento.getParcial() != null) {
						
						qtdeTitulosParciais++;
					}
					
					if (dadosRecolhimento.getProdutoEdicao().getPeso() != null) {
						
						pesoTotal = pesoTotal.add(dadosRecolhimento.getProdutoEdicao().getPeso());
					}
					
					if (dadosRecolhimento.getQtdeExemplares() != null) {
					
						qtdeExemplares = qtdeExemplares.add(dadosRecolhimento.getQtdeExemplares());
					}
					
					if (dadosRecolhimento.getValorTotal() != null) {
						
						valorTotal = valorTotal.add(dadosRecolhimento.getValorTotal());
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