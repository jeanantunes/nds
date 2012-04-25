package br.com.abril.nds.controllers.devolucao;

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
			this.recolhimentoService.obterResumoPeriodoBalanceamento(dataPesquisa, listaIdsFornecedores);
		
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
	private Map<Date, List<RecolhimentoDTO>> obterMatrizBalanceamento(Date dataBalanceamento) {

		Map<Date, List<RecolhimentoDTO>> matrizBalanceamento =  
				(Map<Date, List<RecolhimentoDTO>>) this.session.getAttribute(ATRIBUTO_SESSAO_MAPA_RECOLHIMENTO);
		
		if (matrizBalanceamento == null) {

			matrizBalanceamento = new HashMap<Date, List<RecolhimentoDTO>>();

			matrizBalanceamento.put(dataBalanceamento, null);
		}
		
		return matrizBalanceamento; 
	}

	/*
	 * Retorna os dados do recolhimento referente a um dia especifico.
	 */
	private List<RecolhimentoDTO> obterBalanceamentoDia(Date dataRecolhimento) {
		
		Map<Date, List<RecolhimentoDTO>> matrizBalanceamento = obterMatrizBalanceamento(null);

		if (matrizBalanceamento != null) {
			
			return matrizBalanceamento.get(dataRecolhimento);
		}
		
		return null;
	}
	
}