package br.com.abril.nds.controllers.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.RecolhimentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.RecolhimentoService;
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
	private HttpSession session;
	
	@Autowired
	private Result result;

	@Autowired
	private RecolhimentoService recolhimentoService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	private static final String ATRIBUTO_SESSAO_MAPA_RECOLHIMENTO = "mapaRecolhimento";
	
	@Get
	@Path("/")
	public void index() {
		
		List<Fornecedor> fornecedores = this.fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO);

		result.include("fornecedores", fornecedores);
	}
	
	@Post
	@Path("/pesquisar")
	public void pesquisar(Integer numeroSemana, Date dataPesquisa, List<Long> listaIdsFornecedores) {
		
		this.validarDadosPesquisa(numeroSemana, dataPesquisa, listaIdsFornecedores);
		
		List<ResumoPeriodoBalanceamentoDTO> resumoPeriodoBalanceamento = 
			this.obterResumoPeriodoBalanceamento(dataPesquisa, listaIdsFornecedores);
		
		if (resumoPeriodoBalanceamento == null
				|| resumoPeriodoBalanceamento.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado!");
		}
		
		result.use(Results.json()).from(resumoPeriodoBalanceamento, "result").serialize();
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
	public void balancearPorEditor() {
		
		
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
	private void validarDadosPesquisa(Integer numeroSemana, Date dataPesquisa, List<Long> listaIdsFornecedores) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (numeroSemana == null && dataPesquisa == null) {
			
			listaMensagens.add("O preenchimento do campo [Semana] ou [Data] é obrigatório!");
			
		}
		
		if (listaIdsFornecedores == null || listaIdsFornecedores.isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Fornecedor] é obrigatório!");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
		}
	}
	
	/*
	 * Verifica se já existe a matriz na sessão, caso contrário irá criá-la a partir do dia parametrizado.
	 */
	@SuppressWarnings("unchecked")
	private Map<Date, List<RecolhimentoDTO>> obterMatrizBalanceamento(Date diaBalanceamento) {

		Map<Date, List<RecolhimentoDTO>> matrizBalanceamento =  
				(Map<Date, List<RecolhimentoDTO>>) this.session.getAttribute(ATRIBUTO_SESSAO_MAPA_RECOLHIMENTO);
		
		if (matrizBalanceamento == null) {

			matrizBalanceamento = new HashMap<Date, List<RecolhimentoDTO>>();

			matrizBalanceamento.put(diaBalanceamento, null);
		}
		
		return matrizBalanceamento; 
	}

	/*
	 * Retorna os dados do recolhimento referente a um dia especifico.
	 */
	private List<RecolhimentoDTO> obterRecolhimentoDia(Date diaRecolhimento) {
		
		Map<Date, List<RecolhimentoDTO>> matrizBalanceamento = obterMatrizBalanceamento(null);

		if (matrizBalanceamento != null) {
			
			return matrizBalanceamento.get(diaRecolhimento);
		}
		
		return null;
	}
	
	/*
	 * Obtém o resumo do período de balanceamento de acordo com a data da pesquisa
	 * e a lista de id's dos fornecedores.
	 */
	private List<ResumoPeriodoBalanceamentoDTO> obterResumoPeriodoBalanceamento(Date dataPesquisa, 
																				List<Long> listaIdsFornecedores) {
		
		Map<Date, List<RecolhimentoDTO>> matrizBalanceamento = this.obterMatrizBalanceamento(dataPesquisa);
		
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
					
					if (dadosRecolhimento.getPeso() != null) {
						
						pesoTotal = pesoTotal.add(dadosRecolhimento.getPeso());
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