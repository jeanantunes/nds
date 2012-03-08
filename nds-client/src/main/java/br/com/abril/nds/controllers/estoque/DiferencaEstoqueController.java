package br.com.abril.nds.controllers.estoque;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.DiferencaVO;
import br.com.abril.nds.client.vo.ResultadoDiferencaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO.OrdenacaoColunaConsulta;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO.OrdenacaoColunaLancamento;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/estoque/diferenca")
public class DiferencaEstoqueController {

	private Result result;
	
	private Localization localization;
	
	private HttpSession httpSession;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;
	
	private static final String FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE = "filtroPesquisaLancamento";
	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisa";

	public DiferencaEstoqueController(Result result, 
								 	  Localization localization,
								 	  HttpSession httpSession) {
		
		this.result = result;
		this.localization = localization;
		this.httpSession = httpSession;
	}
	
	@Get
	public void lancamento() {
		
		this.carregarCombosLancamento();
		
		result.include("dataAtual", DateUtil.formatarDataPTBR(new Date()));
	}
	
	@Post
	@Path("/lancamento/pesquisa")
	public void pesquisarLancamentos(String dataMovimentoFormatada, TipoDiferenca tipoDiferenca, 
									 String sortorder, String sortname, int page, int rp) {
		
		
		this.validarEntradaDadosPesquisaLancamentos(dataMovimentoFormatada);
		
		Date dataMovimento = DateUtil.parseDataPTBR(dataMovimentoFormatada);
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = 
			this.carregarFiltroPesquisaLancamentos(
				dataMovimento, tipoDiferenca, sortorder, sortname, page, rp);
		
		List<Diferenca> listaLancamentoDiferencas = 
			this.diferencaEstoqueService.obterDiferencasLancamento(filtro);
		
		if (listaLancamentoDiferencas == null || listaLancamentoDiferencas.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			
		} else {
			
			this.processarDiferencasLancamento(listaLancamentoDiferencas, filtro);
		}
	}
	
	@Post
	@Path("/lancamento/novo")
	public void carregarNovasDiferencas(String dataMovimentoFormatada, TipoDiferenca tipoDiferenca) {
		
		this.validarEntradaDadosNovoLancamento(dataMovimentoFormatada, tipoDiferenca);
		
		int qtdeInicialPadrao = 50;
		
		List<DiferencaVO> listaNovasDiferencas = new ArrayList<DiferencaVO>(qtdeInicialPadrao);
		
		for (int indice = 0; indice < qtdeInicialPadrao; indice++) {
			
			DiferencaVO diferenca = new DiferencaVO();
			
			diferenca.setDataLancamento(dataMovimentoFormatada);
			
			diferenca.setTipoDiferenca(tipoDiferenca.getDescricao());
			
			listaNovasDiferencas.add(diferenca);
		}
		
		TableModel<CellModelKeyValue<DiferencaVO>> tableModel =
			new TableModel<CellModelKeyValue<DiferencaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNovasDiferencas));
		
		tableModel.setTotal(qtdeInicialPadrao);
		
		tableModel.setPage(1);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	@Path("/lancamento/cadastrarNovasDiferencas")
	public void cadastrarNovasDiferencas(List<DiferencaVO> listaNovasDiferencas) {
		
		if (listaNovasDiferencas == null 
				|| listaNovasDiferencas.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Preencha os dados para lançamento!");
		}
		
		for (DiferencaVO diferenca : listaNovasDiferencas) {
			
			this.validarNovaDiferenca(diferenca);
			
			
		}
		
		result.use(Results.json()).withoutRoot().from(listaNovasDiferencas).recursive().serialize();
	}

	@Get
	public void consulta() {
		this.carregarCombosConsulta();
		
		result.include("dataAtual", new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).format(new Date()));
	}
	
	@Post
	@Path("/pesquisarDiferencas")
	public void pesquisarDiferencas(String codigoProduto, Long numeroEdicao,
									Long idFornecedor, String dataInicial,
									String dataFinal, TipoDiferenca tipoDiferenca,
									String sortorder, String sortname,
									int page, int rp) {
		
		this.validarEntradaDadosPesquisa(numeroEdicao, idFornecedor,
										 dataInicial, dataFinal, tipoDiferenca);
		
		FiltroConsultaDiferencaEstoqueDTO filtro =
			this.carregarFiltroPesquisa(codigoProduto, numeroEdicao, idFornecedor,
										dataInicial, dataFinal, tipoDiferenca,
										sortorder, sortname, page, rp);
		
		List<Diferenca> listaDiferencas =
			diferencaEstoqueService.obterDiferencas(filtro);
		
		if (listaDiferencas == null || listaDiferencas.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {
			this.processarDiferencas(listaDiferencas, filtro);
		}
	}

	/**
	 * Método responsável por carregar todos os combos da tela de consulta.
	 */
	public void carregarCombosConsulta() {
		this.carregarComboTiposDiferenca();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo =
			this.carregarComboFornecedores(null);
		
		result.include("listaFornecedores", listaFornecedoresCombo);
	}
	
	@Post
	public void excluirFaltaSobra(Long idDiferenca){
		
		if (this.diferencaEstoqueService.verificarPossibilidadeExclusao(idDiferenca)){
			//setar id de diferenca na sessão
		} else {
			result.use(Results.json()).from(
					new ValidacaoVO(TipoMensagem.ERROR, 
							"Diferença com tipo aprovação automática não pode ser excluida."), 
							Constantes.PARAM_MSGS).recursive().serialize();
		}
		
		//setar id de diferenca na sessão
		
		/*result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), 
				Constantes.PARAM_MSGS).recursive().serialize();*/
		
		
		
		//result.forwardTo(DiferencaEstoqueController.class).lancamento();
	}
	
	@Post
	public void confirmarLancamentos(){
		//TODO: efetuar demais operações pertinentes a esta rotina, morô?
	}
	
	/**
	 * Método responsável por carregar todos os combos da tela de lançamento.
	 */
	private void carregarCombosLancamento() {
		
		this.carregarComboTiposDiferenca();
	}
	
	/**
	 * Método responsável por carregar o combo de tipos de diferença.
	 */
	private void carregarComboTiposDiferenca() {

		List<ItemDTO<TipoDiferenca, String>> listaTiposDiferenca =
			new ArrayList<ItemDTO<TipoDiferenca, String>>();
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.FALTA_DE, TipoDiferenca.FALTA_DE.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.FALTA_EM, TipoDiferenca.FALTA_EM.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.SOBRA_DE, TipoDiferenca.SOBRA_DE.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.SOBRA_EM, TipoDiferenca.SOBRA_EM.getDescricao())
		);
		
		result.include("listaTiposDiferenca", listaTiposDiferenca);
	}
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private List<ItemDTO<Long, String>> carregarComboFornecedores(String codigoProduto) {
		
		//TODO: obter forncedores do tipo revista
		
		List<Fornecedor> listaFornecedor =
			fornecedorService.obterFornecedoresPorProduto(codigoProduto);
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo =
			new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(
				new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getNomeFantasia())
			);
		}
			
		return listaFornecedoresCombo;
	}
	
	@Post
	@Path("/pesquisarFonecedores")
	public void pesquisarFonecedores(String codigoProduto) {
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = 
			carregarComboFornecedores(codigoProduto);
		
		result.use(Results.json()).from(listaFornecedoresCombo, "result").recursive().serialize();
	}
	
	/*
	 * Processa o resulta das diferenças para lançamento.
	 *  
	 * @param listaDiferencas - lista de diferenças
	 * @param filtro - filtro da pesquisa
	 */
	private void processarDiferencasLancamento(List<Diferenca> listaDiferencas, 
											   FiltroLancamentoDiferencaEstoqueDTO filtro) {

		List<DiferencaVO> listaLancamentosDiferenca = new LinkedList<DiferencaVO>();
		
		BigDecimal qtdeTotalDiferencas = BigDecimal.ZERO;
		
		BigDecimal valorTotalDiferencas = BigDecimal.ZERO;
		
		for (Diferenca diferenca : listaDiferencas) {
			
			ProdutoEdicao produtoEdicao = diferenca.getProdutoEdicao();
			
			Produto produto = produtoEdicao.getProduto();
			
			DiferencaVO lancamentoDiferenca = new DiferencaVO();
			
			lancamentoDiferenca.setId(diferenca.getId().intValue());
			lancamentoDiferenca.setCodigoProduto(produto.getCodigo());
			lancamentoDiferenca.setDescricaoProduto(produto.getDescricao());
			lancamentoDiferenca.setNumeroEdicao(produtoEdicao.getNumeroEdicao().toString());
			
			lancamentoDiferenca.setPrecoVenda(CurrencyUtil.formatarValor(produtoEdicao.getPrecoVenda()));
			
			lancamentoDiferenca.setPacotePadrao(String.valueOf(produtoEdicao.getPacotePadrao()));
			lancamentoDiferenca.setQuantidade(diferenca.getQtde().toString());
			lancamentoDiferenca.setTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());

			lancamentoDiferenca.setValorTotalDiferenca(
				CurrencyUtil.formatarValor(diferenca.getValorTotalDiferenca()));
			
			listaLancamentosDiferenca.add(lancamentoDiferenca);
			
			qtdeTotalDiferencas = 
				qtdeTotalDiferencas.add(diferenca.getQtde());
			
			valorTotalDiferencas = valorTotalDiferencas.add(diferenca.getValorTotalDiferenca());
		}
		
		TableModel<CellModelKeyValue<DiferencaVO>> tableModel =
			new TableModel<CellModelKeyValue<DiferencaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentosDiferenca));
		
		Long qtdeTotalRegistros = 
			this.diferencaEstoqueService.obterTotalDiferencasLancamento(filtro);
		
		tableModel.setTotal(qtdeTotalRegistros.intValue());
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		String valorTotalDiferencasFormatado = 
			CurrencyUtil.formatarValor(valorTotalDiferencas, getLocale());
		
		ResultadoDiferencaVO resultadoLancamentoDiferenca = 
			new ResultadoDiferencaVO(tableModel, qtdeTotalDiferencas, valorTotalDiferencasFormatado);
	
		result.use(Results.json()).withoutRoot().from(resultadoLancamentoDiferenca).recursive().serialize();
	}
	
	/*
	 * Processa o resultado das diferenças para lançamento.
	 *  
	 * @param listaDiferencas - lista de diferenças
	 */
	private void processarDiferencasLancamentoMock(int page) {

		List<DiferencaVO> listaLancamentosDiferenca = new LinkedList<DiferencaVO>();
		
		BigDecimal qtdeTotalDiferencas = BigDecimal.ZERO;
		BigDecimal valorTotalDiferencas = BigDecimal.ZERO;
		
		int quantidadeRegistros = 30;
		
		for (int i = 0; i < quantidadeRegistros; i++) {
			
			DiferencaVO lancamentoDiferenca = new DiferencaVO();
			
			lancamentoDiferenca.setId(i);
			lancamentoDiferenca.setCodigoProduto(i + "");
			lancamentoDiferenca.setDescricaoProduto("Descrição Produto " + i);
			lancamentoDiferenca.setNumeroEdicao(i + "");
			
			lancamentoDiferenca.setPrecoVenda(CurrencyUtil.formatarValor(i + 100));
			
			lancamentoDiferenca.setPacotePadrao(i + "");
			lancamentoDiferenca.setQuantidade(i + "");
			lancamentoDiferenca.setTipoDiferenca(TipoDiferenca.FALTA_DE.getDescricao());
			
			BigDecimal valorDiferencas = new BigDecimal(i + 100).multiply(new BigDecimal(i));
			
			lancamentoDiferenca.setValorTotalDiferenca(CurrencyUtil.formatarValor(valorDiferencas));
			
			listaLancamentosDiferenca.add(lancamentoDiferenca);
			
			qtdeTotalDiferencas = 
				qtdeTotalDiferencas.add(new BigDecimal(i));
			
			valorTotalDiferencas = valorTotalDiferencas.add(valorDiferencas);
		}
		
		TableModel<CellModelKeyValue<DiferencaVO>> tableModel =
			new TableModel<CellModelKeyValue<DiferencaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentosDiferenca));
		
		tableModel.setTotal(quantidadeRegistros);
		
		tableModel.setPage(page);
		
		String valorTotalDiferencasFormatado = 
			CurrencyUtil.formatarValor(valorTotalDiferencas, getLocale());
		
		ResultadoDiferencaVO resultadoLancamentoDiferenca = 
			new ResultadoDiferencaVO(tableModel, qtdeTotalDiferencas, valorTotalDiferencasFormatado);
	
		result.use(Results.json()).withoutRoot().from(resultadoLancamentoDiferenca).recursive().serialize();
	}
	
	/*
	 * Processa o resultado das diferenças.
	 *  
	 * @param listaDiferencas - lista de diferenças
	 * @param filtro - filtro da pesquisa
	 */
	private void processarDiferencas(List<Diferenca> listaDiferencas,
									 FiltroConsultaDiferencaEstoqueDTO filtro) {

		List<DiferencaVO> listaConsultaDiferenca = new LinkedList<DiferencaVO>();
		
		BigDecimal qtdeTotalDiferencas = BigDecimal.ZERO;
		BigDecimal valorTotalDiferencas = BigDecimal.ZERO;
		
		int quantidadeRegistros = diferencaEstoqueService.obterTotalDiferencas(filtro).intValue();
		
		for (Diferenca diferenca : listaDiferencas) {
			
			DiferencaVO consultaDiferencaVO = new DiferencaVO();
			
			consultaDiferencaVO.setId(diferenca.getId().intValue());
			
			consultaDiferencaVO.setDataLancamento(
				DateUtil.formatarData(diferenca.getMovimentoEstoque().getDataInclusao(),
									  Constantes.DATE_PATTERN_PT_BR));
			
			consultaDiferencaVO.setCodigoProduto(diferenca.getProdutoEdicao().getProduto().getCodigo());
			consultaDiferencaVO.setDescricaoProduto(diferenca.getProdutoEdicao().getProduto().getNome());
			consultaDiferencaVO.setNumeroEdicao(diferenca.getProdutoEdicao().getNumeroEdicao().toString());
			
			consultaDiferencaVO.setPrecoVenda(
				CurrencyUtil.formatarValor(diferenca.getProdutoEdicao().getPrecoVenda()));

			consultaDiferencaVO.setTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());
			
			if (diferenca.getItemRecebimentoFisico() != null) {
				consultaDiferencaVO.setNumeroNotaFiscal(
					diferenca.getItemRecebimentoFisico().getItemNotaFiscal().getNotaFiscal().getNumero());
			} else {
				consultaDiferencaVO.setNumeroNotaFiscal(" - ");
			}
			
			consultaDiferencaVO.setQuantidade(diferenca.getQtde().toString());
			
			consultaDiferencaVO.setStatusAprovacao(
				diferenca.getMovimentoEstoque().getStatus().toString());
			
			consultaDiferencaVO.setMotivoAprovacao(diferenca.getMovimentoEstoque().getMotivo());
			
			consultaDiferencaVO.setValorTotalDiferenca(
				CurrencyUtil.formatarValor(diferenca.getValorTotalDiferenca()));
			
			listaConsultaDiferenca.add(consultaDiferencaVO);
			
			qtdeTotalDiferencas = 
				qtdeTotalDiferencas.add(diferenca.getQtde());
			
			valorTotalDiferencas = valorTotalDiferencas.add(diferenca.getValorTotalDiferenca());
		}
		
		TableModel<CellModelKeyValue<DiferencaVO>> tableModel =
			new TableModel<CellModelKeyValue<DiferencaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaConsultaDiferenca));
		
		tableModel.setTotal(quantidadeRegistros);
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		String valorTotalDiferencasFormatado = 
			CurrencyUtil.formatarValor(valorTotalDiferencas, getLocale());
		
		ResultadoDiferencaVO resultadoDiferencaVO = 
			new ResultadoDiferencaVO(tableModel, qtdeTotalDiferencas, valorTotalDiferencasFormatado);
	
		result.use(Results.json()).withoutRoot().from(resultadoDiferencaVO).recursive().serialize();
	}
	
	/*
	 * Carrega o filtro da pesquisa de lançamento de diferenças.
	 * 
	 * @param dataMovimento - data do movimento
	 * @param tipoDiferenca - tipo de diferença
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 * 
	 * @return Filtro
	 */
	private FiltroLancamentoDiferencaEstoqueDTO carregarFiltroPesquisaLancamentos(Date dataMovimento, 
																				  TipoDiferenca tipoDiferenca,
																				  String sortorder, 
																				  String sortname, 
																				  int page, 
																				  int rp) {
		
		FiltroLancamentoDiferencaEstoqueDTO filtroAtual = 
			new FiltroLancamentoDiferencaEstoqueDTO(dataMovimento, tipoDiferenca);
		
		this.configurarPaginacaoPesquisaLancamentos(filtroAtual, sortorder, sortname, page, rp);
		
		FiltroLancamentoDiferencaEstoqueDTO filtroSessao =
			(FiltroLancamentoDiferencaEstoqueDTO) 
				this.httpSession.getAttribute(FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
		
			filtroAtual.getPaginacao().setPaginaAtual(1);
			
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE, filtroAtual);
		
		return filtroAtual;
	}
	
	/*
	 * Carrega o filtro da pesquisa de consulta de diferenças.
	 * 
	 * @param codigoProduto - código do produto
	 * @param numeroEdicao - número da edição
	 * @param idFornecedor - identificador do fornecedor
	 * @param dataInicial - data de movimento inicial
	 * @param dataFinal - data de movimento final
	 * @param tipoDiferenca - tipo de diferença
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 * 
	 * @return Filtro
	 */
	private FiltroConsultaDiferencaEstoqueDTO carregarFiltroPesquisa(String codigoProduto, Long numeroEdicao,
																	 Long idFornecedor, String dataInicial,
																	 String dataFinal, TipoDiferenca tipoDiferenca,
																	 String sortorder, String sortname,
																	 int page, int rp) {
		
		FiltroConsultaDiferencaEstoqueDTO filtroAtual =  new FiltroConsultaDiferencaEstoqueDTO();
		
		filtroAtual.setCodigoProduto(codigoProduto);
		filtroAtual.setNumeroEdicao(numeroEdicao);
		filtroAtual.setIdFornecedor(idFornecedor);
		
		filtroAtual.setPeriodoVO(
				new PeriodoVO(DateUtil.parseData(dataInicial, Constantes.DATE_PATTERN_PT_BR),
							  DateUtil.parseData(dataFinal, Constantes.DATE_PATTERN_PT_BR)));
		
		filtroAtual.setTipoDiferenca(tipoDiferenca);
		
		this.configurarPaginacaoPesquisa(filtroAtual, sortorder, sortname, page, rp);
		
		FiltroConsultaDiferencaEstoqueDTO filtroSessao =
			(FiltroConsultaDiferencaEstoqueDTO) 
				this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
		
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtroAtual);
		
		return filtroAtual;
	}
	
	/*
	 * Configura a paginação do filtro de pesquisa de lançamentos.
	 * 
	 * @param filtro - filtro da pesquisa
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 */
	private void configurarPaginacaoPesquisaLancamentos(FiltroLancamentoDiferencaEstoqueDTO filtro, 
														String sortorder,
														String sortname, 
														int page, 
														int rp) {
		
		if (filtro != null) {
			
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
	
			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaLancamento.values(), sortname));
		}
	}
	
	/*
	 * Configura a paginação do filtro de pesquisa.
	 * 
	 * @param filtro - filtro da pesquisa
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 */
	private void configurarPaginacaoPesquisa(FiltroConsultaDiferencaEstoqueDTO filtro, 
											 String sortorder,
											 String sortname, 
											 int page, 
											 int rp) {
		
		if (filtro != null) {
			
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
	
			filtro.setPaginacao(paginacao);
			
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaConsulta.values(), sortname));
		}
	}
	
	/*
	 * Valida a entrada de dados para pesquisa de lançamentos de diferença de estoque.
	 * 
	 * @param dataMovimentoFormatada - data de movimento formatado
	 */
	private void validarEntradaDadosPesquisaLancamentos(String dataMovimentoFormatada) {
		
		if (dataMovimentoFormatada == null 
				|| dataMovimentoFormatada.trim().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.ERROR, "O preenchimento do campo [Data de Movimento] é obrigatório!");
		}
		
		if (!DateUtil.isValidDatePTBR(dataMovimentoFormatada)) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Data de Movimento inválida");
		}
	}
	
	/*
	 * Valida a entrada de dados para pesquisa de diferença de estoque.
	 * 
	 * @param dataMovimentoFormatada - data de movimento formatado
	 */
	private void validarEntradaDadosPesquisa(Long numeroEdicao, Long idFornecedor,
											 String dataInicial, String dataFinal,
											 TipoDiferenca tipoDiferenca) {
		
		if (dataInicial == null 
				|| dataInicial.trim().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.ERROR, "O preenchimento do campo [Data Inicial] é obrigatório!");
		}
		
		if (dataFinal == null 
				|| dataFinal.trim().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.ERROR, "O preenchimento do campo [Data Final] é obrigatório!");
		}
			
		if (!DateUtil.isValidDatePTBR(dataInicial)) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Data Inicial inválida");
		}
		
		if (!DateUtil.isValidDatePTBR(dataFinal)) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Data Final inválida");
		}
		
		if (DateUtil.isDataInicialMaiorDataFinal(DateUtil.parseDataPTBR(dataInicial),
												 DateUtil.parseDataPTBR(dataFinal))) {
			
			throw new ValidacaoException(
				TipoMensagem.ERROR, "O campo [Data Incial] não pode ser maior que o campo [Data Final]!");
		}
		
		if (numeroEdicao == null && idFornecedor == null && tipoDiferenca == null) {
			throw new ValidacaoException(
				TipoMensagem.ERROR, "Para realizar a pesquisa é necessário informar a edição, o fornecedor ou o tipo de diferença!");
		}
		
	}
	
	/*
	 * Valida a entrada de dados para pesquisa de lançamentos de diferença de estoque.
	 * 
	 * @param dataMovimentoFormatada - data de movimento formatado
	 * @param tipoDiferenca - tipo de diferença
	 */
	private void validarEntradaDadosNovoLancamento(String dataMovimentoFormatada, TipoDiferenca tipoDiferenca) {
		
		if (dataMovimentoFormatada == null 
				|| dataMovimentoFormatada.trim().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.ERROR, "O preenchimento do campo [Data de Movimento] é obrigatório!");
		}
		
		if (!DateUtil.isValidDatePTBR(dataMovimentoFormatada)) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Data de Movimento inválida");
		}
		
		if (tipoDiferenca == null) {
			
			throw new ValidacaoException(
				TipoMensagem.ERROR, "O preenchimento do campo [Tipo de Diferença] é obrigatório!");
		}
	}
	
	/*
	 * Valida a entrada de uma nova diferença.
	 * 
	 * @param diferenca - diferença
	 */
	private void validarNovaDiferenca(DiferencaVO diferenca) {
		
		boolean diferencaInvalida = false;
		
		if (diferenca.getCodigoProduto() == null 
				|| diferenca.getCodigoProduto().trim().isEmpty()) {
			
			diferencaInvalida = true;
		}
		
		if (diferenca.getDescricaoProduto() == null 
				|| diferenca.getDescricaoProduto().trim().isEmpty()) {
			
			diferencaInvalida = true;
		}
		
		if (diferenca.getNumeroEdicao() == null 
				|| diferenca.getNumeroEdicao().trim().isEmpty()) {
			
			diferencaInvalida = true;
		}
		
		if (diferenca.getQuantidade() == null 
				|| diferenca.getQuantidade().trim().isEmpty()) {
			
			diferencaInvalida = true;
		}
		
		if (diferencaInvalida) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Existe(m) lançamento(s) preenchido(s) incorretamente!");
		}
	}
	
	private Locale getLocale() {
		if (localization != null) {
			return localization.getLocale();
		}
		return null;
	}
	
}
