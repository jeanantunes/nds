package br.com.abril.nds.controllers.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ProdutoRecolhimentoVO;
import br.com.abril.nds.client.vo.ResultadoResumoBalanceamentoVO;
import br.com.abril.nds.client.vo.ResumoPeriodoBalanceamentoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
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
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO = "balanceamentoRecolhimento";
	
	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO_INICIAL = "balanceamentoRecolhimentoInicial";

	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO = "balanceamentoAlterado";
	
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
			this.obterBalanceamentoRecolhimentoInicial(null, dataPesquisa, numeroSemana, listaIdsFornecedores);
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
		
		removerAtributoAlteracaoSessao();
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	@Path("/confirmar")
	public void confirmar(Integer numeroSemana) {
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			(BalanceamentoRecolhimentoDTO)
				this.httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
		
		validarBloqueioMatrizFechada(balanceamentoRecolhimento);
		
		recolhimentoService.confirmarBalanceamentoRecolhimento(
			balanceamentoRecolhimento.getMatrizRecolhimento(), numeroSemana);
		
		removerAtributoAlteracaoSessao();
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,
			"Balanceamento da matriz de recolhimento confirmado com sucesso!"), Constantes.PARAM_MSGS)
				.recursive().serialize();
	}
	
	@Post
	@Path("/balancearPorEditor")
	public void balancearPorEditor(Integer numeroSemana, Date dataPesquisa,
								   List<Long> listaIdsFornecedores) {
		
		this.validarBloqueioMatrizFechada(null);
		
		this.validarDadosPesquisa(dataPesquisa, listaIdsFornecedores);
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			this.obterBalanceamentoRecolhimentoEditor(dataPesquisa, numeroSemana, listaIdsFornecedores);
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
		
		removerAtributoAlteracaoSessao();
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	@Path("/balancearPorValor")
	public void balancearPorValor(Integer numeroSemana, Date dataPesquisa,
			   					  List<Long> listaIdsFornecedores) {
		
		this.validarBloqueioMatrizFechada(null);
		
		this.validarDadosPesquisa(dataPesquisa, listaIdsFornecedores);
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			this.obterBalanceamentoRecolhimentoValor(dataPesquisa, numeroSemana, listaIdsFornecedores);
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
		
		removerAtributoAlteracaoSessao();
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	@Path("/salvar")
	public void salvar() {
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			(BalanceamentoRecolhimentoDTO)
				this.httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
		
		this.validarBloqueioMatrizFechada(balanceamentoRecolhimento);
		
		recolhimentoService.salvarBalanceamentoRecolhimento(
			balanceamentoRecolhimento.getMatrizRecolhimento());
		
		removerAtributoAlteracaoSessao();
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,
			"Balanceamento da matriz de recolhimento salvo com sucesso!"), Constantes.PARAM_MSGS)
				.recursive().serialize();
	}
	
	@Post
	@Path("/exibirMatrizFornecedor")
	public void exibirMatrizFornecedor(String dataFormatada, String sortorder,
									   String sortname, Integer page, Integer rp) {
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento =
			(BalanceamentoRecolhimentoDTO)
				httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
	
		if (balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento() == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		List<ProdutoRecolhimentoDTO> listaProdutoRecolhimento =
			new ArrayList<ProdutoRecolhimentoDTO>();
		
		Date data = null;
		
		if (dataFormatada != null && !dataFormatada.trim().isEmpty()) {
		
			data = DateUtil.parseDataPTBR(dataFormatada);
		}
		
		if (data != null) {
			
			listaProdutoRecolhimento =
				balanceamentoRecolhimento.getMatrizRecolhimento().get(data);
			
		} else {
			
			for (Map.Entry<Date,List<ProdutoRecolhimentoDTO>> entry :
					balanceamentoRecolhimento.getMatrizRecolhimento().entrySet()) {
			
				listaProdutoRecolhimento.addAll(entry.getValue());
			}
		}
		
		if (listaProdutoRecolhimento != null && !listaProdutoRecolhimento.isEmpty()) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			processarBalanceamento(listaProdutoRecolhimento, balanceamentoRecolhimento.isMatrizFechada(),
								   paginacao, sortname);
		} else {
			
			this.result.use(Results.json()).from(Results.nothing()).serialize();
		}
	}
	
	@Post
	@Path("/voltarConfiguracaoOriginal")
	public void voltarConfiguracaoOriginal() {
		
		this.validarBloqueioMatrizFechada(null);
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			(BalanceamentoRecolhimentoDTO)
				this.httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO_INICIAL);
		
		balanceamentoRecolhimento = 
			this.obterBalanceamentoRecolhimentoInicial(balanceamentoRecolhimento, null, null, null);
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
		
		removerAtributoAlteracaoSessao();
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	@Path("/reprogramarSelecionados")
	public void reprogramarSelecionados(List<ProdutoRecolhimentoVO> listaProdutoRecolhimento,
										String novaDataFormatada, String dataAntigaFormatada,
										Integer numeroSemana) {
		
		this.validarBloqueioMatrizFechada(null);
		
		this.validarDadosReprogramar(novaDataFormatada, numeroSemana);
		
		Date novaData = DateUtil.parseDataPTBR(novaDataFormatada);
		
		this.validarPeriodoReprogramacao(numeroSemana, novaData);
		
		this.validarListaParaReprogramacao(listaProdutoRecolhimento);
		
		Date dataAntiga = DateUtil.parseDataPTBR(dataAntigaFormatada);
		
		this.atualizarMapaRecolhimento(listaProdutoRecolhimento, novaData, dataAntiga);
		
		this.adicionarAtributoAlteracaoSessao();
		
		this.result.use(Results.json()).from(Results.nothing()).serialize();
	}
	
	@Post
	@Path("/reprogramarRecolhimentoUnico")
	public void reprogramarRecolhimentoUnico(ProdutoRecolhimentoVO produtoRecolhimento,
										     String dataAntigaFormatada, Integer numeroSemana) {
		
		this.validarBloqueioMatrizFechada(null);
		
		String novaDataFormatada = produtoRecolhimento.getNovaData();
		
		Date novaData = DateUtil.parseDataPTBR(novaDataFormatada);
		
		this.validarDadosReprogramar(novaDataFormatada, numeroSemana);
		
		this.validarPeriodoReprogramacao(numeroSemana, novaData);
		
		List<ProdutoRecolhimentoVO> listaProdutoRecolhimento = new ArrayList<ProdutoRecolhimentoVO>();
		
		if (produtoRecolhimento != null){
			
			listaProdutoRecolhimento.add(produtoRecolhimento);
		}
		
		this.validarListaParaReprogramacao(listaProdutoRecolhimento);
		
		Date dataAntiga = DateUtil.parseDataPTBR(dataAntigaFormatada);
		
		this.atualizarMapaRecolhimento(listaProdutoRecolhimento, novaData, dataAntiga);
		
		this.adicionarAtributoAlteracaoSessao();
		
		this.result.use(Results.json()).from(Results.nothing()).serialize();
	}
	
	@Post
	@Path("/atualizarResumoBalanceamento")
	public void atualizarResumoBalanceamento() {
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			(BalanceamentoRecolhimentoDTO)
				this.httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
		
		if (balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento() == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}

	@Post
	@Path("/verificarBalanceamentosAlterados")
	public void verificarBalanceamentosAlterados() {
		
		Boolean balanceamentoAlterado =
			(Boolean) this.httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO);
		
		if (balanceamentoAlterado == null) {
			
			balanceamentoAlterado = false;
		}
		
		this.result.use(Results.json()).from(balanceamentoAlterado.toString(), "result").serialize();
	}
	
	private void validarBloqueioMatrizFechada(BalanceamentoRecolhimentoDTO balanceamentoRecolhimento) {
		
		if (balanceamentoRecolhimento == null) {
			
			balanceamentoRecolhimento = 
					(BalanceamentoRecolhimentoDTO)
						this.httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
		}
		
		if (balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.isMatrizFechada()) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING,
								"Ação não permitida! A matriz já se encontra fechada!"));
		}
	}
	
	private void adicionarAtributoAlteracaoSessao() {
		
		this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO, true);
	}
	
	private void removerAtributoAlteracaoSessao() {
		
		this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO, null);
	}
	
	private void atualizarMapaRecolhimento(List<ProdutoRecolhimentoVO> listaProdutoRecolhimento,
										   Date novaData, Date dataAntiga) {
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimentoSessao =
			(BalanceamentoRecolhimentoDTO)
				httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoSessao =
			balanceamentoRecolhimentoSessao.getMatrizRecolhimento();
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento =
			clonarMapaRecolhimento(matrizRecolhimentoSessao);
		
		//Monta listas para adicionar e remover do mapa
		List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoRemover =
			new ArrayList<ProdutoRecolhimentoDTO>();
		
		List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoAdicionar =
			new ArrayList<ProdutoRecolhimentoDTO>();
		
		montarListasParaManipulacaoMapa(listaProdutoRecolhimento,
										matrizRecolhimento,
										listaProdutoRecolhimentoAdicionar,
										listaProdutoRecolhimentoRemover,
										dataAntiga);
		
		removerEAdicionarMapa(matrizRecolhimento,
							  listaProdutoRecolhimentoAdicionar,
							  listaProdutoRecolhimentoRemover,
							  novaData);
		
		this.validarSequencia(matrizRecolhimento);
		
		balanceamentoRecolhimentoSessao.setMatrizRecolhimento(matrizRecolhimento);
		
		this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO,
									  balanceamentoRecolhimentoSessao);
	}
	
	@SuppressWarnings("unchecked")
	private Map<Date, List<ProdutoRecolhimentoDTO>> clonarMapaRecolhimento(
								Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoSessao) {
		
		byte[] mapSerialized =
			SerializationUtils.serialize(matrizRecolhimentoSessao);

		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento =
			(Map<Date, List<ProdutoRecolhimentoDTO>>) SerializationUtils.deserialize(mapSerialized);
		
		return matrizRecolhimento;
	}

	private void montarListasParaManipulacaoMapa(List<ProdutoRecolhimentoVO> listaProdutoRecolhimento,
											     Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoSessao,   									 
											     List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoAdicionar,
			   									 List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoRemover,
			   									 Date dataAntiga) {
		
		List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoSessao = null;
		
		if (dataAntiga != null) {
			
			listaProdutoRecolhimentoSessao = matrizRecolhimentoSessao.get(dataAntiga);
			
		} else {
		
			listaProdutoRecolhimentoSessao = new ArrayList<ProdutoRecolhimentoDTO>();
			
			for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimentoSessao.entrySet()) {
				
				listaProdutoRecolhimentoSessao.addAll(entry.getValue());
			}
		}
		
		for (ProdutoRecolhimentoVO produtoRecolhimento : listaProdutoRecolhimento) {
			
			for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimentoSessao) {
				
				if (produtoRecolhimentoDTO.getIdLancamento().equals(
						Long.valueOf(produtoRecolhimento.getIdLancamento()))) {
					
					listaProdutoRecolhimentoRemover.add(produtoRecolhimentoDTO);
					
					produtoRecolhimentoDTO.setSequencia(
						Long.valueOf(produtoRecolhimento.getSequencia()));
					
					listaProdutoRecolhimentoAdicionar.add(produtoRecolhimentoDTO);
					
					break;
				}
			}
		}
	}
	
	private void removerEAdicionarMapa(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoSessao,   									 
		     						   List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoAdicionar,
		     						   List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoRemover,
		     						   Date novaData) {
		
		//Remover do mapa
		for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimentoRemover) {
		
			List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO =
				matrizRecolhimentoSessao.get(produtoRecolhimentoDTO.getNovaData());
			
			listaProdutoRecolhimentoDTO.remove(produtoRecolhimentoDTO);
			
			if (listaProdutoRecolhimentoDTO.isEmpty()) {
				
				matrizRecolhimentoSessao.remove(produtoRecolhimentoDTO.getNovaData());
				
			} else {
				
				matrizRecolhimentoSessao.put(produtoRecolhimentoDTO.getNovaData(),
											 listaProdutoRecolhimentoDTO);
			}
		}
		
		//Adicionar no mapa
		for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimentoAdicionar) {
			
			List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO =
				matrizRecolhimentoSessao.get(novaData);
			
			if (listaProdutoRecolhimentoDTO == null) {
				
				listaProdutoRecolhimentoDTO = new ArrayList<ProdutoRecolhimentoDTO>();
			}
			
			produtoRecolhimentoDTO.setNovaData(novaData);
			
			listaProdutoRecolhimentoDTO.add(produtoRecolhimentoDTO);
			
			matrizRecolhimentoSessao.put(novaData, listaProdutoRecolhimentoDTO);
		}
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
				produtoRecolhimentoDTO.getCodigoProduto());
			
			produtoRecolhimentoVO.setNomeProduto(
				produtoRecolhimentoDTO.getNomeProduto());
			
			produtoRecolhimentoVO.setNumeroEdicao(
				produtoRecolhimentoDTO.getNumeroEdicao().toString());
			
			produtoRecolhimentoVO.setPrecoVenda(
				CurrencyUtil.formatarValor(produtoRecolhimentoDTO.getPrecoVenda()));

			produtoRecolhimentoVO.setNomeFornecedor(produtoRecolhimentoDTO.getNomeFornecedor());
			
			produtoRecolhimentoVO.setNomeEditor(produtoRecolhimentoDTO.getNomeEditor());
			
			if (produtoRecolhimentoDTO.getParcial() != null) {
				produtoRecolhimentoVO.setParcial(produtoRecolhimentoDTO.getParcial().toString());
			} else {
				produtoRecolhimentoVO.setParcial(
					(produtoRecolhimentoDTO.getParcial() != null) ? "Sim" : "Não");
			}
				
			produtoRecolhimentoVO.setBrinde(
				(produtoRecolhimentoDTO.isPossuiBrinde()) ? "Sim" : "Não");

			produtoRecolhimentoVO.setDataLancamento(
				DateUtil.formatarDataPTBR(produtoRecolhimentoDTO.getDataLancamento()));

			produtoRecolhimentoVO.setDataRecolhimento(
				DateUtil.formatarDataPTBR(produtoRecolhimentoDTO.getDataRecolhimentoDistribuidor()));
			
			if (produtoRecolhimentoDTO.getExpectativaEncalheSede() != null) {
				produtoRecolhimentoVO.setEncalheSede(
					produtoRecolhimentoDTO.getExpectativaEncalheSede());
			} else {
//				produtoRecolhimentoVO.setEncalheSede("");
			}
			
			if (produtoRecolhimentoDTO.getExpectativaEncalheAtendida() != null) {
				produtoRecolhimentoVO.setEncalheAtendida(
					produtoRecolhimentoDTO.getExpectativaEncalheAtendida());
			} else {
//				produtoRecolhimentoVO.setEncalheAtendida("");
			}
			
			if (produtoRecolhimentoDTO.getExpectativaEncalhe() != null) {
				produtoRecolhimentoVO.setEncalhe(produtoRecolhimentoDTO.getExpectativaEncalhe());
			} else {
//				produtoRecolhimentoVO.setEncalhe("");
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
	
	private void validarDadosReprogramar(String data, Integer numeroSemana) {
		
		if (data == null || data.trim().isEmpty()) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING, "O preenchimento da data é obrigatório!"));
		}
		
		if (!DateUtil.isValidDatePTBR(data)) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING, "Data inválida!"));
		}
		
		if (numeroSemana == null) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING, "Semana inválida!"));
		}
	}
	
	private void validarSequencia(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
		
		Set<Long> sequenciasValidas = new TreeSet<Long>();
		
		Set<Long> sequenciasInvalidas = new TreeSet<Long>();
		
		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()) {
			
			List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO = entry.getValue();
		
			for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimentoDTO) {
				
				boolean adicionada = sequenciasValidas.add(produtoRecolhimentoDTO.getSequencia());
				
				if (!adicionada) {
					
					sequenciasInvalidas.add(produtoRecolhimentoDTO.getSequencia());
				}
			}
		}
		
		if (!sequenciasInvalidas.isEmpty()) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING,
					"O campo [SM] não pode ser duplicado! A(s) SM(s) duplicadas são: " + sequenciasInvalidas));
		}
		
	}
	
	private void validarPeriodoReprogramacao(Integer numeroSemana, Date novaData) {
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor == null) {
			
			throw new RuntimeException("Dados do distribuidor inexistentes!");
		}
		
		Date dataInicioSemana = DateUtil.obterDataDaSemanaNoAno(
			numeroSemana, distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
		
		boolean dataValidaSemana =
			DateUtil.validarDataEntrePeriodo(novaData, dataInicioSemana, dataFimSemana);
		
		if (!dataValidaSemana) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A data deve estar entre " + DateUtil.formatarDataPTBR(dataInicioSemana) + " e " 
				+ DateUtil.formatarDataPTBR(dataFimSemana) + ", referente à semana " + numeroSemana);
		}
	}
	
	private void validarListaParaReprogramacao(List<ProdutoRecolhimentoVO> listaProdutoRecolhimento) {
		
		if (listaProdutoRecolhimento == null || listaProdutoRecolhimento.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"É necessário selecionar ao menos um produto para realizar a reprogramação!");
		}
	}
	
	private BalanceamentoRecolhimentoDTO obterBalanceamentoRecolhimentoInicial(
												BalanceamentoRecolhimentoDTO balanceamentoRecolhimento,
												Date dataBalanceamento,
												Integer numeroSemana,
												List<Long> listaIdsFornecedores) {
		
		if ((balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento() == null)
					&& numeroSemana != null 
					&& listaIdsFornecedores != null) {

			//TODO: chamar o método para balanceamento automático do service
			
//			this.recolhimentoService.obterMatrizBalanceamento(numeroSemana,
//															  listaIdsFornecedores,
//															  TipoBalanceamentoRecolhimento.AUTOMATICO);
			
			balanceamentoRecolhimento = 
				this.obterBalanceamentoRecolhimentoMock(dataBalanceamento, listaIdsFornecedores);
			
			this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO_INICIAL,
										  balanceamentoRecolhimento);
			
			this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO,
										  balanceamentoRecolhimento);
		}
		
		if (balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento() == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		return balanceamentoRecolhimento;
	}
	
	private BalanceamentoRecolhimentoDTO obterBalanceamentoRecolhimentoEditor(
																	Date dataBalanceamento,
																	Integer numeroSemana,
																	List<Long> listaIdsFornecedores) {
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento =
			new BalanceamentoRecolhimentoDTO();
		
		if (numeroSemana != null && listaIdsFornecedores != null) {

			//TODO: chamar o método para balanceamento por editor do service
			
//			this.recolhimentoService.obterMatrizBalanceamento(numeroSemana,
//															  listaIdsFornecedores,
//															  TipoBalanceamentoRecolhimento.EDITOR);
			
			balanceamentoRecolhimento = 
				this.obterBalanceamentoRecolhimentoMock(dataBalanceamento, listaIdsFornecedores);
			
			this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO,
										  balanceamentoRecolhimento);
		}
		
		if (balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento() == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		return balanceamentoRecolhimento;
	}
	
	private BalanceamentoRecolhimentoDTO obterBalanceamentoRecolhimentoValor(
																	Date dataBalanceamento,
																	Integer numeroSemana,
																	List<Long> listaIdsFornecedores) {
	
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento =
			new BalanceamentoRecolhimentoDTO();
		
		if (numeroSemana != null && listaIdsFornecedores != null) {

			//TODO: chamar o método para balanceamento por valor do service
			
//			this.recolhimentoService.obterMatrizBalanceamento(numeroSemana,
//															  listaIdsFornecedores,
//															  TipoBalanceamentoRecolhimento.VALOR);
			
			balanceamentoRecolhimento = 
				this.obterBalanceamentoRecolhimentoMock(dataBalanceamento, listaIdsFornecedores);
			
			this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO,
										  balanceamentoRecolhimento);
		}
		
		if (balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento() == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		return balanceamentoRecolhimento;
	}
	
	/*
	 * MOCK:
	 */
	private BalanceamentoRecolhimentoDTO obterBalanceamentoRecolhimentoMock(Date dataBalanceamento, 
																	 		List<Long> listaIdsFornecedores) {

		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = 
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		Date dataLancamento = DateUtil.parseDataPTBR("11/04/2012");

		Long idLancamento = 1L;
		
		Long idProdutoEdicao = 1L;
		
		Long sequencia = 1L;
		
		for (int diaRecolhimento = 9; diaRecolhimento <= 15; diaRecolhimento++) {
		
			Date dataRecolhimento = DateUtil.parseDataPTBR(diaRecolhimento + "/05/2012");
			
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
				
				produtoRecolhimento.setIdLancamento(idLancamento++);
				produtoRecolhimento.setSequencia(sequencia++);
				produtoRecolhimento.setExpectativaEncalheAtendida(BigDecimal.ZERO);
				produtoRecolhimento.setExpectativaEncalheSede(BigDecimal.ZERO);
				produtoRecolhimento.setDataLancamento(dataLancamento);
				produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
				produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
				produtoRecolhimento.setNomeEditor("Zé Editor " + i);
				produtoRecolhimento.setNomeFornecedor("Zé Fornecedor " + i);
				produtoRecolhimento.setExpectativaEncalhe(new BigDecimal(i * 100));
				produtoRecolhimento.setValorTotal(new BigDecimal(i));
				produtoRecolhimento.setPossuiChamada(i % 2 == 0);
				produtoRecolhimento.setNovaData(dataRecolhimento);
				
				produtoRecolhimento.setIdProdutoEdicao(idProdutoEdicao++);
				produtoRecolhimento.setCodigoProduto("" + i);
				produtoRecolhimento.setNomeProduto("Produto " + i);
				produtoRecolhimento.setNumeroEdicao(1L);
				produtoRecolhimento.setPeso(new BigDecimal(i));
				produtoRecolhimento.setPossuiBrinde(false);
				produtoRecolhimento.setPrecoVenda(new BigDecimal(i));
				
				listaProdutosRecolhimento.add(produtoRecolhimento);
			}
			
			matrizRecolhimento.put(dataRecolhimento, listaProdutosRecolhimento);
		}
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = new BalanceamentoRecolhimentoDTO();
		
		balanceamentoRecolhimento.setMatrizRecolhimento(matrizRecolhimento);
		
		balanceamentoRecolhimento.setCapacidadeRecolhimentoDistribuidor(new BigDecimal(5000));
		
		balanceamentoRecolhimento.setMatrizFechada(false);
		
		return balanceamentoRecolhimento;
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
		
		List<ResumoPeriodoBalanceamentoVO> resumoPeriodoBalanceamento =
			new ArrayList<ResumoPeriodoBalanceamentoVO>();
		
		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : balanceamentoRecolhimento.getMatrizRecolhimento().entrySet()) {
			
			Date dataRecolhimento = entry.getKey();
			
			ResumoPeriodoBalanceamentoVO itemResumoPeriodoBalanceamento = new ResumoPeriodoBalanceamentoVO();
			
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
					
					if (produtoRecolhimento.getPeso() != null) {
						
						pesoTotal = pesoTotal.add(produtoRecolhimento.getPeso());
					}
					
					if (produtoRecolhimento.getValorTotal() != null) {
						
						valorTotal = valorTotal.add(produtoRecolhimento.getValorTotal());
					}
					
					if (produtoRecolhimento.getExpectativaEncalhe() != null) {
						
						qtdeExemplares = qtdeExemplares.add(produtoRecolhimento.getExpectativaEncalhe());
					}
					
					if (produtoRecolhimento.getExpectativaEncalheAtendida() != null) {
											
						qtdeExemplares = qtdeExemplares.add(produtoRecolhimento.getExpectativaEncalheAtendida());
					}
					
					if (produtoRecolhimento.getExpectativaEncalheSede() != null) {
						
						qtdeExemplares = qtdeExemplares.add(produtoRecolhimento.getExpectativaEncalheSede());
					}
				}
				
				boolean excedeCapacidadeDistribuidor = false;
				
				if (balanceamentoRecolhimento.getCapacidadeRecolhimentoDistribuidor() != null) {
				
					excedeCapacidadeDistribuidor =
						(balanceamentoRecolhimento.getCapacidadeRecolhimentoDistribuidor()
							.compareTo(qtdeExemplares) == -1);
				}
				
				itemResumoPeriodoBalanceamento.setExcedeCapacidadeDistribuidor(excedeCapacidadeDistribuidor);
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
		
		resultadoResumoBalanceamento.setCapacidadeRecolhimentoDistribuidor(
			balanceamentoRecolhimento.getCapacidadeRecolhimentoDistribuidor());
		
		return resultadoResumoBalanceamento;
	}
	
}