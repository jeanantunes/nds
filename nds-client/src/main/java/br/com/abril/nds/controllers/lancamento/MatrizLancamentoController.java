package br.com.abril.nds.controllers.lancamento;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ConfirmacaoVO;
import br.com.abril.nds.client.vo.ResultadoResumoBalanceamentoVO;
import br.com.abril.nds.client.vo.ResumoPeriodoBalanceamentoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
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
import br.com.abril.nds.vo.LancamentoVO;
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
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	private static final String FORMATO_DATA = "dd/MM/yyyy";
	
	private static final String CAMPO_REQUERIDO_KEY = "required_field";
	private static final String CAMPO_MAIOR_IGUAL_KEY = "validator.must.be.greaterEquals";

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroMatrizBalanceamento";
	
	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO = "balanceamentoLancamento";
	
	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO = "balanceamentoAlterado";
	
	@Path("/matrizLancamento")
	public void index() {
		
		removerAtributoAlteracaoSessao();
		
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
		
		removerAtributoAlteracaoSessao();
		
		FiltroLancamentoDTO filtro = configurarFiltropesquisa(dataLancamento, idsFornecedores);
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
			this.obterBalanceamentoLancamento(filtro, false);
				
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoLancamento(balanceamentoLancamento);
						
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	
	
	@Post
	public void obterGridMatrizLancamento(String sortorder, String sortname, int page, int rp) {
		
		List<ProdutoLancamentoDTO> listaProdutoBalanceamento = getProdutoLancamentoDTOFromMatrizSessao();
		
		FiltroLancamentoDTO filtro = obterFiltroSessao();
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		filtro.setTotalRegistrosEncontrados(listaProdutoBalanceamento.size());
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
		
		if (listaProdutoBalanceamento != null && !listaProdutoBalanceamento.isEmpty()) {	
			
			List<LancamentoVO> listaProdutoBalanceamentoVO = processarBalanceamento(listaProdutoBalanceamento,filtro.getPaginacao());
			
			Double valorTotal = 0.0;
			
			for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoBalanceamento) {
				
				valorTotal += produtoLancamentoDTO.getValorTotal().doubleValue();
				
			}
			
			TableModel<CellModelKeyValue<LancamentoVO>> tm = new TableModel<CellModelKeyValue<LancamentoVO>>();
			List<CellModelKeyValue<LancamentoVO>> cells = CellModelKeyValue
					.toCellModelKeyValue(listaProdutoBalanceamentoVO);
			
			tm.setRows(cells);
			tm.setPage(page);
			tm.setTotal(filtro.getTotalRegistrosEncontrados());
			
			Object[] resultado = {tm, CurrencyUtil.formatarValor(valorTotal)};
			result.use(Results.json()).withoutRoot().from(resultado).serialize();
			
		} else {
			
			this.result.use(Results.json()).from(Results.nothing()).serialize();
		}
		
	}
	
	public List<ProdutoLancamentoDTO> getProdutoLancamentoDTOFromMatrizSessao() {
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
				(BalanceamentoLancamentoDTO) session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamento == null
				|| balanceamentoLancamento.getMatrizLancamento() == null
				|| balanceamentoLancamento.getMatrizLancamento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}		

		List<ProdutoLancamentoDTO> listaProdutoBalanceamento =
			new ArrayList<ProdutoLancamentoDTO>();
		
		
		
		for (Map.Entry<Date,List<ProdutoLancamentoDTO>> entry :
			balanceamentoLancamento.getMatrizLancamento().entrySet()) {
		
			listaProdutoBalanceamento.addAll(entry.getValue());
		}
		
		return listaProdutoBalanceamento;
	}
	
	@Post
	public void confirmarMatrizLancamento(List<Date> datasConfirmadas) {
		
		// TODO: obter a matriz da sessão
		
		// TODO: chamar a service de confirmação

		if (datasConfirmadas==null || datasConfirmadas.size()<=0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Selecione ao menos uma data!");
		}	
	
		// TODO: validar matriz de lançamento conforme parâmetro: datasConfirmadas
			
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Confirmado com sucesso !"), "result").recursive().serialize();
	}
	
	@Post
	public void voltarConfiguracaoOriginal() {
		
		FiltroLancamentoDTO filtro = obterFiltroSessao();
		
		BalanceamentoLancamentoDTO balanceamentoLancamento =
			this.obterBalanceamentoLancamento(filtro, true);
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoLancamento(balanceamentoLancamento);
							
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	public void reprogramarLancamentosSelecionados(List<LancamentoVO> produtosLancamento,
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
	public void reprogramarLancamentoUnico(LancamentoVO produtoLancamento) {
		
		String novaDataFormatada = produtoLancamento.getNovaData();
		
		this.validarDadosReprogramar(novaDataFormatada);

		adicionarAtributoAlteracaoSessao();
		
		Date novaData = DateUtil.parseDataPTBR(novaDataFormatada);
		
		List<LancamentoVO> produtosLancamento = new ArrayList<LancamentoVO>();
		
		if (produtoLancamento != null){
			
			produtosLancamento.add(produtoLancamento);
		}
		
		this.validarListaParaReprogramacao(produtosLancamento);
		
		this.validarDataReprogramacao(produtosLancamento, novaData);
		
		this.atualizarMapaLancamento(produtosLancamento, novaData);
		
		this.result.use(Results.json()).from(Results.nothing()).serialize();
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
	private void validarDataReprogramacao(List<LancamentoVO> produtosLancamento, Date novaData) {
		
		BalanceamentoLancamentoDTO balanceamentoLancamento =
			(BalanceamentoLancamentoDTO) this.session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		if (balanceamentoLancamento == null) {
			
			return;
		}
		
		int numeroSemana = balanceamentoLancamento.getNumeroSemana();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor == null) {
			
			throw new RuntimeException("Dados do distribuidor inexistentes!");
		}
		
		Date dataInicioSemana = DateUtil.obterDataDaSemanaNoAno(
			numeroSemana, distribuidor.getInicioSemana().getCodigoDiaSemana());
		
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
		
		for (LancamentoVO produtoLancamento : produtosLancamento) {
		
			String dataRecolhimentoPrevistaFormatada = produtoLancamento.getDataRecolhimento();
			
			if (dataRecolhimentoPrevistaFormatada == null
					|| dataRecolhimentoPrevistaFormatada.trim().isEmpty()) {
				
				continue;
			}
			
			Date dataRecolhimentoPrevista =
				DateUtil.parseDataPTBR(produtoLancamento.getDataRecolhimento());
			
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
					+ "<td><u>Edição:</u> " + produtoLancamento.getNumEdicao() + "</td>"
					+ "<td><u>Data recolhimento:</u> " + dataRecolhimentoPrevistaFormatada + "</td>"
					+ "</tr>";
			}
		}
		
		if (!produtos.isEmpty()) {
		
			listaMensagens.add(
				"A nova data de lançamento não deve ultrapassar "
				+ "a data de recolhimento prevista para o(s) produto(s):"
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
	private void validarListaParaReprogramacao(List<LancamentoVO> produtosLancamento) {
		
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
	private void atualizarMapaLancamento(List<LancamentoVO> produtosLancamento,
										 Date novaData) {
		
		BalanceamentoLancamentoDTO balanceamentoLancamentoSessao =
			(BalanceamentoLancamentoDTO)
				this.session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoSessao =
				balanceamentoLancamentoSessao.getMatrizLancamento();
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento =
			clonarMapaLancamento(matrizLancamentoSessao);
		
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
	private void montarListasParaAlteracaoMapa(List<LancamentoVO> produtosLancamento,
											   Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,   									 
											   List<ProdutoLancamentoDTO> listaProdutoLancamentoAdicionar,
											   List<ProdutoLancamentoDTO> listaProdutoLancamentoRemover) {
		
		List<ProdutoLancamentoDTO> listaProdutoLancamentoSessao =
			new ArrayList<ProdutoLancamentoDTO>();
		
		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : matrizLancamento.entrySet()) {
			
			listaProdutoLancamentoSessao.addAll(entry.getValue());
		}
		
		for (LancamentoVO produtoLancamento : produtosLancamento) {
			
			for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamentoSessao) {
				
				if (produtoLancamentoDTO.getIdLancamento().equals(
						Long.valueOf(produtoLancamento.getId()))) {
					
					listaProdutoLancamentoRemover.add(produtoLancamentoDTO);
					
					listaProdutoLancamentoAdicionar.add(produtoLancamentoDTO);
					
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
		for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamentoAdicionar) {
			
			if (produtoLancamentoDTO.isPermiteReprogramacao()) {
			
				List<ProdutoLancamentoDTO> listaProdutoLancamentoDTO =
					matrizLancamento.get(novaData);
				
				if (listaProdutoLancamentoDTO == null) {
					
					listaProdutoLancamentoDTO = new ArrayList<ProdutoLancamentoDTO>();
				}
				
				listaProdutoLancamentoDTO.add(produtoLancamentoDTO);
				
				produtoLancamentoDTO.setNovaDataLancamento(novaData);
				
				matrizLancamento.put(novaData, listaProdutoLancamentoDTO);
				
			} else {
				
				Date dataAntiga = produtoLancamentoDTO.getNovaDataLancamento();
				
				List<ProdutoLancamentoDTO> listaProdutoLancamentoDTO =
					matrizLancamento.get(dataAntiga);
				
				if (listaProdutoLancamentoDTO == null) {
					
					listaProdutoLancamentoDTO = new ArrayList<ProdutoLancamentoDTO>();
				}
				
				listaProdutoLancamentoDTO.add(produtoLancamentoDTO);
				
				matrizLancamento.put(dataAntiga, listaProdutoLancamentoDTO);
			}
		}
	}
	
	private List<LancamentoVO> processarBalanceamento(List<ProdutoLancamentoDTO> listaProdutoLancamento,
										PaginacaoVO paginacao) {
		
		List<LancamentoVO> listaProdutoBalanceamentoVO =
				new LinkedList<LancamentoVO>();
			
		for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamento) {

			listaProdutoBalanceamentoVO.add(getVoProdutoBalanceamento(produtoLancamentoDTO));
		}
		
		listaProdutoBalanceamentoVO =
			PaginacaoUtil.paginarEOrdenarEmMemoria(listaProdutoBalanceamentoVO, paginacao, paginacao.getSortColumn());
					
		return listaProdutoBalanceamentoVO;
	}
	
	
	private LancamentoVO getVoProdutoBalanceamento(
			ProdutoLancamentoDTO produtoLancamentoDTO) {

		
		LancamentoVO produtoBalanceamentoVO = new LancamentoVO();
		
		produtoBalanceamentoVO.setCodigoProduto(produtoLancamentoDTO.getCodigoProduto());
		
		produtoBalanceamentoVO.setNovaData(
				DateUtil.formatarDataPTBR(produtoLancamentoDTO.getNovaDataLancamento()));
		
		produtoBalanceamentoVO.setDataPrevisto(
				DateUtil.formatarDataPTBR(produtoLancamentoDTO.getDataLancamentoPrevista()));
		
		produtoBalanceamentoVO.setDataLancamentoDistribuidor(
				DateUtil.formatarDataPTBR(produtoLancamentoDTO.getDataLancamentoDistribuidor()));
		
		produtoBalanceamentoVO.setDataRecolhimento(
				DateUtil.formatarDataPTBR(produtoLancamentoDTO.getDataRecolhimentoPrevista()));
		
		produtoBalanceamentoVO.setId(produtoLancamentoDTO.getIdLancamento());
		
		if(produtoLancamentoDTO.getParcial() == null)
			produtoBalanceamentoVO.setLancamento("Lancamento");
		else
			produtoBalanceamentoVO.setLancamento(produtoLancamentoDTO.getParcial().getDescricao());
		
		produtoBalanceamentoVO.setNomeProduto(produtoLancamentoDTO.getNomeProduto());
		produtoBalanceamentoVO.setNumEdicao(produtoLancamentoDTO.getNumeroEdicao());
		
		produtoBalanceamentoVO.setPreco(CurrencyUtil.formatarValor(produtoLancamentoDTO.getPrecoVenda()));
		
		produtoBalanceamentoVO.setReparte(produtoLancamentoDTO.getRepartePrevisto().toString());
		
		produtoBalanceamentoVO.setTotal(CurrencyUtil.formatarValor(produtoLancamentoDTO.getValorTotal()));
		
		if(produtoLancamentoDTO.getReparteFisico()==null)
			produtoBalanceamentoVO.setFisico(0);
		else
			produtoBalanceamentoVO.setFisico(produtoLancamentoDTO.getReparteFisico().intValue());
		
		produtoBalanceamentoVO.setCancelamentoGD(produtoLancamentoDTO.getStatusLancamento().equals(StatusLancamento.CANCELADO_GD));
		
		if(produtoLancamentoDTO.getNumeroReprogramacoes() == null)
			produtoBalanceamentoVO.setReprogramacoesExcedidas(false);
		else
			produtoBalanceamentoVO.setReprogramacoesExcedidas(produtoLancamentoDTO.getNumeroReprogramacoes() >= Constantes.NUMERO_REPROGRAMACOES_LIMITE);
				
		produtoBalanceamentoVO.setEstudoFechado(produtoLancamentoDTO.isPossuiEstudo());
		
		produtoBalanceamentoVO.setPossuiRecebimentoFisico(produtoLancamentoDTO.isPossuiRecebimentoFisico());
		
		//TODO - Pendente
		produtoBalanceamentoVO.setDistribuicao("");
		
		produtoBalanceamentoVO.setBloquearData(!produtoLancamentoDTO.isPermiteReprogramacao());
		
		produtoBalanceamentoVO.setIdProdutoEdicao(produtoLancamentoDTO.getIdProdutoEdicao());
		
		return produtoBalanceamentoVO;
	}

	//----------------------------------------------------------------------------
	

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
		
		FiltroLancamentoDTO filtro = obterFiltroSessao();
		
		List<ProdutoLancamentoDTO> listaProdutoBalanceamento = getProdutoLancamentoDTOFromMatrizSessao();
		
		if (listaProdutoBalanceamento != null && !listaProdutoBalanceamento.isEmpty()) {	
			
			List<LancamentoVO> listaProdutoBalanceamentoVO = processarBalanceamento(listaProdutoBalanceamento,filtro.getPaginacao());
			
			Double valorTotal = 0.0;
			
			for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoBalanceamento) {
				
				valorTotal += produtoLancamentoDTO.getValorTotal().doubleValue();
			}
						
			RodapeDTO rodape = new RodapeDTO(CurrencyUtil.formatarValor(valorTotal));
			
			FileExporter.to("matriz_balanceamento", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, rodape, 
					listaProdutoBalanceamentoVO, LancamentoVO.class, this.httpResponse);
		}
	
		
		result.nothing();
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

	/**
	 * Obtem agrupamento diário para confirmação de Balanceamento
	 */
	@Post
	public void obterAgrupamentoDiarioBalanceamento() {

		List<ConfirmacaoVO> confirmacoesVO = new ArrayList<ConfirmacaoVO>();

		BalanceamentoLancamentoDTO balanceamentoLancamento =
			(BalanceamentoLancamentoDTO) this.session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);

		if (balanceamentoLancamento == null
				|| balanceamentoLancamento.getMatrizLancamento() == null
				|| balanceamentoLancamento.getMatrizLancamento().isEmpty()) {
			
			result.nothing();
			
			return;
		}
	
		confirmacoesVO = this.agruparBalanceamento(balanceamentoLancamento);

		TableModel<CellModelKeyValue<ConfirmacaoVO>> tableModel =
			new TableModel<CellModelKeyValue<ConfirmacaoVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(confirmacoesVO));

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	/**
	 * Obtem a concentração ordenada e agrupada por data para a Matriz de Lançamento
	 * @param BalanceamentoLancamentoDTO: balanceamentoDTO
	 * @return List<ConfirmacaoVO>: confirmacoesVO
	 */
    private List<ConfirmacaoVO> agruparBalanceamento(BalanceamentoLancamentoDTO balanceamentoDTO){
		
		List<ConfirmacaoVO> confirmacoesVO = new ArrayList<ConfirmacaoVO>();

		Map<Date, Boolean> mapaDatasConfirmacaoOrdenada = new LinkedHashMap<Date, Boolean>();

		boolean confirmado = false;

		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry
				: balanceamentoDTO.getMatrizLancamento().entrySet()) {
			
            List<ProdutoLancamentoDTO> listaProdutosRecolhimento = entry.getValue();
			
			if (listaProdutosRecolhimento != null && !listaProdutosRecolhimento.isEmpty()) {
				
				for (ProdutoLancamentoDTO produtoBalanceamento : listaProdutosRecolhimento) {

					confirmado =
						(produtoBalanceamento.getStatusLancamento().equals(StatusLancamento.BALANCEADO)
							&& (produtoBalanceamento.getDataLancamentoDistribuidor().compareTo(
									produtoBalanceamento.getNovaDataLancamento()) == 0));
					
					if (mapaDatasConfirmacaoOrdenada.get(produtoBalanceamento.getNovaDataLancamento()) == null
							|| (!confirmado && mapaDatasConfirmacaoOrdenada.get(
									produtoBalanceamento.getNovaDataLancamento()))) {
						
						mapaDatasConfirmacaoOrdenada.put(produtoBalanceamento.getNovaDataLancamento(),
														 confirmado);
					}
				}
			}
		}
		
		Set<Entry<Date, Boolean>> entrySet = mapaDatasConfirmacaoOrdenada.entrySet();
		
		for (Entry<Date, Boolean> item : entrySet) {
			
			confirmacoesVO.add(
				new ConfirmacaoVO(DateUtil.formatarDataPTBR(item.getKey()), item.getValue()));
		}

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
		
		if (balanceamentoLancamento == null
				|| balanceamentoLancamento.getMatrizLancamento() == null
				|| balanceamentoLancamento.getMatrizLancamento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
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
			
			List<LancamentoVO> listaProdutoBalanceamentoVO = processarBalanceamento(listaProdutoBalanceamento,filtro.getPaginacao());
			
			Double valorTotal = 0.0;
			
			for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoBalanceamento) {
				
				valorTotal += produtoLancamentoDTO.getValorTotal().doubleValue();
				
			}
			
			TableModel<CellModelKeyValue<LancamentoVO>> tm = new TableModel<CellModelKeyValue<LancamentoVO>>();
			List<CellModelKeyValue<LancamentoVO>> cells = CellModelKeyValue
					.toCellModelKeyValue(listaProdutoBalanceamentoVO);
			
			tm.setRows(cells);
			tm.setPage(filtro.getPaginacao().getPaginaAtual());
			tm.setTotal(filtro.getTotalRegistrosEncontrados());
			
			Object[] resultado = {tm, CurrencyUtil.formatarValor(valorTotal)};
			result.use(Results.json()).withoutRoot().from(resultado).serialize();
			
		} else {
			
			this.result.use(Results.json()).from(Results.nothing()).serialize();
		}
		
	}
}
