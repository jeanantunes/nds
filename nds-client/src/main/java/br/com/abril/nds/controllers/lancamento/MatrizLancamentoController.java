package br.com.abril.nds.controllers.lancamento;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ProdutoLancamentoVO;
import br.com.abril.nds.client.vo.ResultadoResumoBalanceamentoVO;
import br.com.abril.nds.client.vo.ResumoPeriodoBalanceamentoVO;
import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.ConfirmacaoVO;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
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
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CalendarioService calendarioService;

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroMatrizBalanceamento";
	
	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO = "balanceamentoLancamento";
	
	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO = "balanceamentoAlterado";
	
	@Path("/matrizLancamento")
	@Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ)
	public void index() {
		
		removerAtributoAlteracaoSessao();
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);
		
		List<Fornecedor> fornecedores = fornecedorService.obterFornecedores(
				true, SituacaoCadastro.ATIVO);
		String data = DateUtil.formatarDataPTBR(new Date());
		result.include("data", data);
		result.include("fornecedores", fornecedores);
	}
	
	@Post
	public void obterMatrizLancamento(Date dataLancamento, List<Long> idsFornecedores) {
				
		validarDadosPesquisa(dataLancamento, idsFornecedores);
		
		removerAtributoAlteracaoSessao();
		
		FiltroLancamentoDTO filtro = configurarFiltropesquisa(dataLancamento, idsFornecedores);
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
			this.obterBalanceamentoLancamento(filtro, false);
				
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoLancamento(balanceamentoLancamento);
						
		this.result.use(CustomJson.class).from(resultadoResumoBalanceamento, "resultado").recursive().serialize();
		
	}
	
	
	@Post
	public void obterGridMatrizLancamento(String sortorder, String sortname, int page, int rp) {
		
		List<ProdutoLancamentoDTO> listaProdutoBalanceamento = this.getProdutoLancamentoDTOFromMatrizSessao();
		
		FiltroLancamentoDTO filtro = obterFiltroSessao();
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		filtro.setTotalRegistrosEncontrados(listaProdutoBalanceamento.size());
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
		
		if (listaProdutoBalanceamento != null && !listaProdutoBalanceamento.isEmpty()) {	
			
			processarBalanceamento(listaProdutoBalanceamento, filtro);
			
		} else {
			
			this.result.use(Results.json()).from(Results.nothing()).serialize();
		}
	}
	
	private List<ProdutoLancamentoDTO> getProdutoLancamentoDTOFromMatrizSessao() {
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
				(BalanceamentoLancamentoDTO) session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}		

		List<ProdutoLancamentoDTO> listaProdutoBalanceamento =
			new ArrayList<ProdutoLancamentoDTO>();
		
		for (Map.Entry<Date,List<ProdutoLancamentoDTO>> entry
				: balanceamentoLancamento.getMatrizLancamento().entrySet()) {
		
			for (ProdutoLancamentoDTO produtoLancamento : entry.getValue()) {
				
				if (!produtoLancamento.isLancamentoAgrupado()) {
					
					listaProdutoBalanceamento.add(produtoLancamento);
				}
			}	
		}
		
		return listaProdutoBalanceamento;
	}
	
	@Post
	public void confirmarMatrizLancamento(List<Date> datasConfirmadas) {
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
			(BalanceamentoLancamentoDTO) session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}

		if (datasConfirmadas == null || datasConfirmadas.size() <= 0){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Selecione ao menos uma data!");
		}
	
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoSessao =
			balanceamentoLancamento.getMatrizLancamento();
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento =
			this.clonarMapaLancamento(matrizLancamentoSessao);
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoConfirmada =
			matrizLancamentoService.confirmarMatrizLancamento(matrizLancamento,
															  datasConfirmadas, getUsuario());
		
		matrizLancamento =
			this.atualizarMatizComProdutosConfirmados(matrizLancamento, matrizLancamentoConfirmada);
		
		balanceamentoLancamento.setMatrizLancamento(matrizLancamento);
		
		this.session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO, balanceamentoLancamento);
		
		this.verificarLancamentosConfirmados();
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,
			"Balanceamento da matriz de lançamento confirmado com sucesso!"), "result").serialize();
	}

	@Post
	public void voltarConfiguracaoOriginal() {
		
		FiltroLancamentoDTO filtro = obterFiltroSessao();
		
		BalanceamentoLancamentoDTO balanceamentoLancamento =
			this.obterBalanceamentoLancamento(filtro, true);
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoLancamento(balanceamentoLancamento);
		
		removerAtributoAlteracaoSessao();
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	public void reprogramarLancamentosSelecionados(List<ProdutoLancamentoVO> produtosLancamento,
												   String novaDataFormatada) {
		
		this.validarDadosReprogramar(novaDataFormatada);
		
		adicionarAtributoAlteracaoSessao();
		
		Date novaData = DateUtil.parseDataPTBR(novaDataFormatada);

		this.validarListaParaReprogramacao(produtosLancamento);
		
		this.validarDataReprogramacao(produtosLancamento, novaData);
		
		this.atualizarMapaLancamento(produtosLancamento, novaData);
		
		this.result.use(Results.json()).from(Results.nothing()).serialize();
	}
	
	@Post
	public void reprogramarLancamentoUnico(ProdutoLancamentoVO produtoLancamento) {
		
		String novaDataFormatada = produtoLancamento.getNovaData();
		
		this.validarDadosReprogramar(novaDataFormatada);

		adicionarAtributoAlteracaoSessao();
		
		Date novaData = DateUtil.parseDataPTBR(novaDataFormatada);
		
		List<ProdutoLancamentoVO> produtosLancamento = new ArrayList<ProdutoLancamentoVO>();
		
		if (produtoLancamento != null){
			
			produtosLancamento.add(produtoLancamento);
		}
		
		this.validarListaParaReprogramacao(produtosLancamento);
		
		this.validarDataReprogramacao(produtosLancamento, novaData);
		
		this.atualizarMapaLancamento(produtosLancamento, novaData);
		
		this.result.use(Results.json()).from(Results.nothing()).serialize();
	}
	
	/**
	 * Método que atualiza a matriz de lançamento de acordo com os produtos confirmados
	 * 
	 * @param matrizLancamento - matriz de lançamento
	 * @param matrizLancamentoConfirmada - matriz de lançamento confirmada
	 * 
	 * @return matriz atualizada
	 */
	private TreeMap<Date, List<ProdutoLancamentoDTO>> atualizarMatizComProdutosConfirmados(
								TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
								TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoConfirmada) {
		
		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry
				: matrizLancamentoConfirmada.entrySet()) {
			
			Date novaData = entry.getKey();
			
			List<ProdutoLancamentoDTO> produtosLancamentoConfirmados =
					matrizLancamentoConfirmada.get(novaData);
			
			matrizLancamento.put(novaData, produtosLancamentoConfirmados);
		}
		
		return matrizLancamento;
	}
	
	/**
	 * Método que verifica se todos os lançamentos estão confirmados
	 * para remover a flag de alteração de dados da sessão.
	 */
	private void verificarLancamentosConfirmados() {
		
		List<ConfirmacaoVO> listaConfirmacao = montarListaDatasConfirmacao();
		
		boolean lancamentosConfirmados = true;
		
		for (ConfirmacaoVO confirmacao : listaConfirmacao) {
			
			if (!confirmacao.isConfirmado()) {
				
				lancamentosConfirmados = false;
				
				break;
			}
		}
		
		if (lancamentosConfirmados) {
			
			removerAtributoAlteracaoSessao();
		}
	}
	
	/**
	 * Valida os dados para reprogramação.
	 * 
	 * @param data - data para reprogramação
	 */
	private void validarDadosReprogramar(String data) {
		
		if (data == null || data.trim().isEmpty()) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING, "O preenchimento da data é obrigatório!"));
		}
		
		if (!DateUtil.isValidDatePTBR(data)) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING, "Data inválida!"));
		}
	}
	
	/**
	 * Valida a data de reprogramação de lançamento.
	 * 
	 * @param produtosLancamento - lista de produtos de lançamento
	 * @param novaData - nova data de recolhimento
	 */
	private void validarDataReprogramacao(List<ProdutoLancamentoVO> produtosLancamento, Date novaData) {
		
		List<ConfirmacaoVO> confirmacoes = this.montarListaDatasConfirmacao();

		for (ConfirmacaoVO confirmacao : confirmacoes) {

			if (DateUtil.parseDataPTBR(confirmacao.getMensagem()).equals(novaData)) {

				if (confirmacao.isConfirmado()) {

					throw new ValidacaoException(TipoMensagem.WARNING,
						"Lançamentos não podem ser reprogramados para uma data já confirmada!");
				}
			}
		}		
		
		BalanceamentoLancamentoDTO balanceamentoLancamento =
			(BalanceamentoLancamentoDTO) this.session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}
		
		int numeroSemana = balanceamentoLancamento.getNumeroSemana();
		Date dataLancamento = balanceamentoLancamento.getDataLancamento();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor == null) {
			
			throw new RuntimeException("Dados do distribuidor inexistentes!");
		}
		
		Date dataInicioSemana = DateUtil.obterDataDaSemanaNoAno(
			numeroSemana, distribuidor.getInicioSemana().getCodigoDiaSemana(), dataLancamento);
		
		boolean dataInicioSemanaMaior =
			DateUtil.isDataInicialMaiorDataFinal(dataInicioSemana, novaData);
		
		if (dataInicioSemanaMaior) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A nova data de lançamento deve ser maior ou igual à data de início da semana ["
				+ DateUtil.formatarDataPTBR(dataInicioSemana) + "]");
		}
		
		boolean diaUtil = calendarioService.isDiaUtil(novaData);
		
		if (!diaUtil) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A nova data de lançamento deve ser um dia útil!");
		}
		
		List<String> listaMensagens = new ArrayList<String>();
		
		String produtos = "";
		
		Integer qtdDiasLimiteParaReprogLancamento =
				distribuidor.getQtdDiasLimiteParaReprogLancamento();
		
		for (ProdutoLancamentoVO produtoLancamento : produtosLancamento) {
		
			String dataRecolhimentoPrevistaFormatada = produtoLancamento.getDataRecolhimentoPrevista();
			
			if (dataRecolhimentoPrevistaFormatada == null
					|| dataRecolhimentoPrevistaFormatada.trim().isEmpty()) {
				
				continue;
			}
			
			Date dataRecolhimentoPrevista =
				DateUtil.parseDataPTBR(produtoLancamento.getDataRecolhimentoPrevista());
			
			Date dataLimiteReprogramacao =
				DateUtil.subtrairDias(dataRecolhimentoPrevista,
									  distribuidor.getQtdDiasLimiteParaReprogLancamento());
			
			if (novaData.compareTo(dataLimiteReprogramacao) == 1) {
				
				if (produtos.isEmpty()) {
					produtos += "<table>";
				}
				
				produtos +=
					"<tr>"
					+ "<td><u>Produto:</u> " + produtoLancamento.getNomeProduto() + "</td>"
					+ "<td><u>Edição:</u> " + produtoLancamento.getNumeroEdicao() + "</td>"
					+ "<td><u>Data recolhimento:</u> " + dataRecolhimentoPrevistaFormatada + "</td>"
					+ "</tr>";
			}
		}
		
		if (!produtos.isEmpty()) {
		
			listaMensagens.add(
				"A nova data de lançamento não deve ultrapassar "
				+ "a data de recolhimento prevista - a quantidade de dias limite [" 
				+ qtdDiasLimiteParaReprogLancamento + "] para o(s) produto(s):"
			);
			
			listaMensagens.add(produtos + "</table>");
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaMensagens);
			
			throw new ValidacaoException(validacao);
		}
	}
	
	/**
	 * Valida a lista de produtos informados na tela para reprogramação.
	 * 
	 * @param produtosLancamento - lista de produtos de lançamento
	 */
	private void validarListaParaReprogramacao(List<ProdutoLancamentoVO> produtosLancamento) {
		
		if (produtosLancamento == null || produtosLancamento.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"É necessário selecionar ao menos um produto para realizar a reprogramação!");
		}
	}
	
	/**
	 * Método que atualiza o mapa de lançamento de acordo com as escolhas do usuário
	 * 
	 * @param produtosLancamento - lista de produtos a serem alterados
	 * @param novaData - nova data de lançamento
	 */
	private void atualizarMapaLancamento(List<ProdutoLancamentoVO> produtosLancamento,
										 Date novaData) {
		
		BalanceamentoLancamentoDTO balanceamentoLancamentoSessao =
			(BalanceamentoLancamentoDTO)
				this.session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamentoSessao == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoSessao =
			balanceamentoLancamentoSessao.getMatrizLancamento();
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento =
			this.clonarMapaLancamento(matrizLancamentoSessao);
		
		List<ProdutoLancamentoDTO> listaProdutoLancamentoRemover =
			new ArrayList<ProdutoLancamentoDTO>();
		
		List<ProdutoLancamentoDTO> listaProdutoLancamentoAdicionar =
			new ArrayList<ProdutoLancamentoDTO>();
		
		this.montarListasParaAlteracaoMapa(produtosLancamento,
									  	   matrizLancamento,
									  	   listaProdutoLancamentoAdicionar,
									  	   listaProdutoLancamentoRemover);
		
		this.removerEAdicionarMapa(matrizLancamento,
							  	   listaProdutoLancamentoAdicionar,
							  	   listaProdutoLancamentoRemover,
							  	   novaData);
		
		balanceamentoLancamentoSessao.setMatrizLancamento(matrizLancamento);
		
		this.session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO,
								  balanceamentoLancamentoSessao);
	}
	
	/**
	 * Cria uma cópia do mapa da matriz de lançamento.
	 * Isso é necessário pois se houver alterações na cópia,
	 * não altera os valores do mapa original por referência.
	 * 
	 * @param matrizLancamentoSessao - matriz de lançamento da sesão
	 * 
	 * @return cópia do mapa da matriz de lançamento
	 */
	@SuppressWarnings("unchecked")
	private TreeMap<Date, List<ProdutoLancamentoDTO>> clonarMapaLancamento(
								Map<Date, List<ProdutoLancamentoDTO>> matrizLancamentoSessao) {
		
		byte[] mapSerialized =
			SerializationUtils.serialize(matrizLancamentoSessao);

		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento =
			(TreeMap<Date, List<ProdutoLancamentoDTO>>) SerializationUtils.deserialize(mapSerialized);
		
		return matrizLancamento;
	}
	
	/**
	 * Monta as listas para alteração do mapa da matriz de lançamento
	 * 
	 * @param produtosLancamento - lista de produtos de lançamento
	 * @param matrizLancamento - matriz de lançamento
	 * @param listaProdutoLancamentoAdicionar - lista de produtos que serão adicionados
	 * @param listaProdutoLancamentoRemover - lista de produtos que serão removidos
	 */
	private void montarListasParaAlteracaoMapa(List<ProdutoLancamentoVO> produtosLancamento,
											   Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,   									 
											   List<ProdutoLancamentoDTO> listaProdutoLancamentoAdicionar,
											   List<ProdutoLancamentoDTO> listaProdutoLancamentoRemover) {
		
		List<ProdutoLancamentoDTO> listaProdutoLancamentoSessao =
			new ArrayList<ProdutoLancamentoDTO>();
		
		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : matrizLancamento.entrySet()) {
			
			listaProdutoLancamentoSessao.addAll(entry.getValue());
		}
		
		for (ProdutoLancamentoVO produtoLancamento : produtosLancamento) {
			
			for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamentoSessao) {
				
				if (produtoLancamentoDTO.getIdLancamento().equals(
						Long.valueOf(produtoLancamento.getId()))) {
					
					listaProdutoLancamentoRemover.add(produtoLancamentoDTO);
					
					listaProdutoLancamentoAdicionar.add(produtoLancamentoDTO);
					
					List<ProdutoLancamentoDTO> produtosLancamentoAgrupados =
							produtoLancamentoDTO.getProdutosLancamentoAgrupados();
					
					if (!produtosLancamentoAgrupados.isEmpty()) {
						
						listaProdutoLancamentoRemover.addAll(produtosLancamentoAgrupados);
						
						listaProdutoLancamentoAdicionar.addAll(produtosLancamentoAgrupados);
					}
					
					break;
				}
			}
		}
	}
	
	/**
	 * Remove e adiona os produtos no mapa da matriz de lançamento.
	 * 
	 * @param matrizLancamento - mapa da matriz de lançamento
	 * @param listaProdutoLancamentoAdicionar - lista de produtos que serão adicionados
	 * @param listaProdutoLancamentoRemover - lista de produtos que serão removidos
	 * @param novaData - nova data de lançamento
	 */
	private void removerEAdicionarMapa(Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,   									 
		     						   List<ProdutoLancamentoDTO> listaProdutoLancamentoAdicionar,
		     						   List<ProdutoLancamentoDTO> listaProdutoLancamentoRemover,
		     						   Date novaData) {
		
		//Remover do mapa
		for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamentoRemover) {
		
			List<ProdutoLancamentoDTO> produtosLancamentoDTO =
				matrizLancamento.get(produtoLancamentoDTO.getNovaDataLancamento());
			
			produtosLancamentoDTO.remove(produtoLancamentoDTO);
			
			if (produtosLancamentoDTO.isEmpty()) {
				
				matrizLancamento.remove(produtoLancamentoDTO.getNovaDataLancamento());
				
			} else {
				
				matrizLancamento.put(produtoLancamentoDTO.getNovaDataLancamento(),
									 produtosLancamentoDTO);
			}
		}
		
		//Adicionar no mapa		
		for (ProdutoLancamentoDTO produtoLancamentoAdicionar : listaProdutoLancamentoAdicionar) {
			
			if (produtoLancamentoAdicionar.permiteReprogramacao()) {
			
				List<ProdutoLancamentoDTO> produtosLancamento = matrizLancamento.get(novaData);
				
				if (produtosLancamento == null) {
					
					produtosLancamento = new ArrayList<ProdutoLancamentoDTO>();
				}
				
				produtoLancamentoAdicionar.setNovaDataLancamento(novaData);
				
				matrizLancamentoService.tratarAgrupamentoPorProdutoDataLcto(
					produtoLancamentoAdicionar, produtosLancamento);
				
				produtosLancamento.add(produtoLancamentoAdicionar);
				
				matrizLancamento.put(novaData, produtosLancamento);
				
			} else {
				
				Date dataAntiga = produtoLancamentoAdicionar.getNovaDataLancamento();
				
				List<ProdutoLancamentoDTO> listaProdutoLancamentoDTO =
					matrizLancamento.get(dataAntiga);
				
				if (listaProdutoLancamentoDTO == null) {
					
					listaProdutoLancamentoDTO = new ArrayList<ProdutoLancamentoDTO>();
				}
				
				listaProdutoLancamentoDTO.add(produtoLancamentoAdicionar);
				
				matrizLancamento.put(dataAntiga, listaProdutoLancamentoDTO);
			}
		}
	}
	
	private void processarBalanceamento(List<ProdutoLancamentoDTO> listaProdutoLancamento,
										FiltroLancamentoDTO filtro) {

		PaginacaoVO paginacao = filtro.getPaginacao();
		
		listaProdutoLancamento =
			PaginacaoUtil.paginarEOrdenarEmMemoria(listaProdutoLancamento, paginacao, paginacao.getSortColumn());
		
		List<ProdutoLancamentoVO> listaProdutoBalanceamentoVO =
				new LinkedList<ProdutoLancamentoVO>();
		
		Double valorTotal = getValorTotal(listaProdutoLancamento);
		
		listaProdutoBalanceamentoVO = getProdutosLancamentoVO(listaProdutoLancamento);
						
		TableModel<CellModelKeyValue<ProdutoLancamentoVO>> tm = new TableModel<CellModelKeyValue<ProdutoLancamentoVO>>();
		List<CellModelKeyValue<ProdutoLancamentoVO>> cells = CellModelKeyValue
				.toCellModelKeyValue(listaProdutoBalanceamentoVO);
		
		List<Object> resultado = new ArrayList<Object>();
		
		tm.setRows(cells);
		tm.setPage(paginacao.getPaginaAtual());
		tm.setTotal(filtro.getTotalRegistrosEncontrados());

		resultado.add(tm);
		resultado.add(CurrencyUtil.formatarValor(valorTotal));
		
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
		
	}
	
	private Double getValorTotal(List<ProdutoLancamentoDTO> listaProdutoLancamento) {

		Double valorTotal = 0.0;
		
		for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamento) {

			valorTotal += produtoLancamentoDTO.getValorTotal().doubleValue();
		}
		
		return valorTotal;
	}

	private List<ProdutoLancamentoVO> getProdutosLancamentoVO(List<ProdutoLancamentoDTO> listaProdutoLancamento) {

		List<ProdutoLancamentoVO> listaProdutoBalanceamentoVO = new LinkedList<ProdutoLancamentoVO>();
		
		for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamento) {
			
			listaProdutoBalanceamentoVO.add(getVoProdutoBalanceamento(produtoLancamentoDTO));
		}

		return listaProdutoBalanceamentoVO;
	}

	private ProdutoLancamentoVO getVoProdutoBalanceamento(
			ProdutoLancamentoDTO produtoLancamentoDTO) {

		
		ProdutoLancamentoVO produtoBalanceamentoVO = new ProdutoLancamentoVO();
		
		produtoBalanceamentoVO.setCodigoProduto(produtoLancamentoDTO.getCodigoProduto());
		
		produtoBalanceamentoVO.setNovaData(
			DateUtil.formatarDataPTBR(produtoLancamentoDTO.getNovaDataLancamento()));
		
		produtoBalanceamentoVO.setDataLancamentoPrevista(
			DateUtil.formatarDataPTBR(produtoLancamentoDTO.getDataLancamentoPrevista()));
		
		produtoBalanceamentoVO.setDataRecolhimentoPrevista(
			DateUtil.formatarDataPTBR(produtoLancamentoDTO.getDataRecolhimentoPrevista()));
		
		produtoBalanceamentoVO.setId(produtoLancamentoDTO.getIdLancamento());
		
		produtoBalanceamentoVO.setDescricaoLancamento(produtoLancamentoDTO.getDescricaoLancamento());
		
		produtoBalanceamentoVO.setNomeProduto(produtoLancamentoDTO.getNomeProduto());
		produtoBalanceamentoVO.setNumeroEdicao(produtoLancamentoDTO.getNumeroEdicao());
		
		produtoBalanceamentoVO.setPrecoVenda(CurrencyUtil.formatarValor(produtoLancamentoDTO.getPrecoVenda()));
		
		produtoBalanceamentoVO.setRepartePrevisto(
			MathUtil.round(produtoLancamentoDTO.getRepartePrevisto(), 2).toString());
		
		produtoBalanceamentoVO.setValorTotal(CurrencyUtil.formatarValor(produtoLancamentoDTO.getValorTotal()));
		
		if(produtoLancamentoDTO.getReparteFisico()==null)
			produtoBalanceamentoVO.setReparteFisico("0");
		else
			produtoBalanceamentoVO.setReparteFisico(
				MathUtil.round(produtoLancamentoDTO.getReparteFisico(), 2).toString());
		
		// TODO: Este campos será informado em um nova EMS de Ajuste
		if(produtoLancamentoDTO.getDistribuicao() == null) {
			produtoBalanceamentoVO.setDistribuicao("");
		} else {
			produtoBalanceamentoVO.setDistribuicao(produtoLancamentoDTO.getDistribuicao());
		}
		
		produtoBalanceamentoVO.setBloquearData(!produtoLancamentoDTO.permiteReprogramacao());
		
		produtoBalanceamentoVO.setBloquearExclusao(produtoLancamentoDTO.isBalanceamentoConfirmado());
		
		produtoBalanceamentoVO.setIdProdutoEdicao(produtoLancamentoDTO.getIdProdutoEdicao());
		
		produtoBalanceamentoVO.setPossuiFuro(produtoLancamentoDTO.isPossuiFuro());
		
		produtoBalanceamentoVO.setDestacarLinha(
			!produtoLancamentoDTO.isPossuiRecebimentoFisico()
				|| produtoLancamentoDTO.isAlteradoInteface());
				
		return produtoBalanceamentoVO;
	}	

	/**
	 * Obtém os dados do cabeçalho de exportação.
	 * 
	 * @return NDSFileHeader
	 */
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
	}
	
	@Exportable
	public class RodapeDTO {
		@Export(label="Valor Total R$:")
		private String total;
		
		public RodapeDTO(String total) {
			this.total = total;
		}
		
		public String getTotal() {
			return total;
		}
	}
	
	/**
	 * Exporta os dados da pesquisa.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		List<ProdutoLancamentoDTO> listaProdutoBalanceamento =
			getProdutoLancamentoDTOFromMatrizSessao();
		
		FiltroLancamentoDTO filtro = obterFiltroSessao();
		
		if (listaProdutoBalanceamento != null && !listaProdutoBalanceamento.isEmpty()) {	
			
			Double valorTotal = getValorTotal(listaProdutoBalanceamento);
			
			List<ProdutoLancamentoVO> listaProdutoBalanceamentoVO =
				getProdutosLancamentoVO(listaProdutoBalanceamento);
			
			RodapeDTO rodape = new RodapeDTO(CurrencyUtil.formatarValor(valorTotal));
			
			FileExporter.to("matriz_balanceamento", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, rodape, 
					listaProdutoBalanceamentoVO, ProdutoLancamentoVO.class, this.httpResponse);
		}
		
		result.nothing();
	}
	
	private String montarNomeFornecedores(List<Long> idsFornecedores) {
		
		String nomeFornecedores = "";
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresPorId(idsFornecedores);
		
		if (listaFornecedor != null && !listaFornecedor.isEmpty()) {
			
			for (Fornecedor fornecedor : listaFornecedor) {
				
				if (!nomeFornecedores.isEmpty()) {
					
					nomeFornecedores += " / ";
				}
				
				nomeFornecedores += fornecedor.getJuridica().getRazaoSocial();
			}
		}
		
		return nomeFornecedores;
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
	 * Obtém a matriz de balanceamento de balanceamento.
	 * 
	 * @param dataBalanceamento - data de balanceamento
	 * @param listaIdsFornecedores - lista de identificadores dos fornecedores
	 * @param configuracaoInicial - indicada se a matriz de lançamento deve ser sugerida de acordo com configuração inicial
	 * 
	 * @return - objeto contendo as informações do balanceamento
	 */
	private BalanceamentoLancamentoDTO obterBalanceamentoLancamento(FiltroLancamentoDTO filtro,
																	boolean configuracaoInicial) {
		
		BalanceamentoLancamentoDTO balanceamento =
			this.matrizLancamentoService.obterMatrizLancamento(filtro, configuracaoInicial);
					
		this.session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO, balanceamento);
		
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
		
		filtro.setNomesFornecedor(this.montarNomeFornecedores(listaIdsFornecedores));
		
		this.session.setAttribute(FILTRO_SESSION_ATTRIBUTE,filtro);
		
		return filtro;
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
	private ResultadoResumoBalanceamentoVO obterResultadoResumoLancamento(
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
				
				Long qtdeTitulos = 0L;
				Long qtdeTitulosParciais = 0L;
				
				Long pesoTotal = 0L;
				BigDecimal qtdeExemplares = BigDecimal.ZERO;
				BigDecimal valorTotal = BigDecimal.ZERO;
				
				for (ProdutoLancamentoDTO produtoBalanceamento : listaProdutosRecolhimento) {
					
					if (produtoBalanceamento.isLancamentoAgrupado()) {
						
						continue;
					}
					
					if (produtoBalanceamento.getParcial() != null) {
						
						qtdeTitulosParciais++;
					}
					
					if (produtoBalanceamento.getPeso() != null) {
						
						pesoTotal += produtoBalanceamento.getPeso();
					}
					
					if (produtoBalanceamento.getValorTotal() != null) {
						
						valorTotal = valorTotal.add(produtoBalanceamento.getValorTotal());
					}
					
					if (produtoBalanceamento.getRepartePrevisto() != null) {
						
						qtdeExemplares = qtdeExemplares.add(produtoBalanceamento.getRepartePrevisto());
					}
					
					qtdeTitulos++;
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
		
		resultadoResumoBalanceamento.setListaProdutosLancamentosCancelados(balanceamentoBalanceamento.getProdutosLancamentosCancelados());
		
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
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}
		
		return filtro;
	}

	/**
	 * Obtem agrupamento diário para confirmação de Balanceamento
	 */
	@Post
	public void obterAgrupamentoDiarioBalanceamento() {

		List<ConfirmacaoVO> confirmacoesVO = this.montarListaDatasConfirmacao();

		if (confirmacoesVO != null) {
		
			result.use(Results.json()).from(confirmacoesVO, "result").serialize();
		}
	}

	/**
	 * Obtem a concentração ordenada e agrupada por data para a Matriz de Lançamento
	 * 
	 * @return List<ConfirmacaoVO>: confirmacoesVO
	 */
	private List<ConfirmacaoVO> montarListaDatasConfirmacao() {

		List<ConfirmacaoVO> confirmacoesVO =
			matrizLancamentoService.obterDatasConfirmacao(this.getProdutoLancamentoDTOFromMatrizSessao());

		return confirmacoesVO;
	}
	
	@Post
	public void verificarBalanceamentosAlterados() {
		
		Boolean balanceamentoAlterado =
			(Boolean) this.session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO);
		
		if (balanceamentoAlterado == null) {
			
			balanceamentoAlterado = false;
		}
		
		this.result.use(Results.json()).from(balanceamentoAlterado.toString(), "result").serialize();
	}
	
	/**
	 * Adiciona um indicador, que informa se houve reprogramação de produtos, na sessão.
	 */
	private void adicionarAtributoAlteracaoSessao() {
		
		this.session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO, true);
	}
	
	/**
	 * Remove um indicador, que informa se houve reprogramação de produtos, da sessão.
	 */
	private void removerAtributoAlteracaoSessao() {
		
		this.session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO, null);
	}

	@Post
	public void atualizarResumoBalanceamento() {
				
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
			(BalanceamentoLancamentoDTO)
				this.session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoLancamento(balanceamentoLancamento);
		
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	public void atualizarGridMatrizLancamento() {
				
		List<ProdutoLancamentoDTO> listaProdutoBalanceamento = getProdutoLancamentoDTOFromMatrizSessao();
		
		FiltroLancamentoDTO filtro = obterFiltroSessao();
		
		filtro.setTotalRegistrosEncontrados(listaProdutoBalanceamento.size());
		
		if (listaProdutoBalanceamento != null && !listaProdutoBalanceamento.isEmpty()) {	

			processarBalanceamento(listaProdutoBalanceamento, filtro);
			
		} else {
			
			this.result.use(Results.json()).from(Results.nothing()).serialize();
		}
		
	}
	
	@Post
	public void excluirLancamento(Long idLancamento){
		matrizLancamentoService.excluiLancamento(idLancamento);		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Lançamento excluído com sucesso!")).serialize();
	}
}
