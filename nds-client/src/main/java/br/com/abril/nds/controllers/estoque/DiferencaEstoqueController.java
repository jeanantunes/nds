package br.com.abril.nds.controllers.estoque;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ConfirmacaoVO;
import br.com.abril.nds.client.vo.DiferencaVO;
import br.com.abril.nds.client.vo.RateioCotaVO;
import br.com.abril.nds.client.vo.ResultadoDiferencaVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO.OrdenacaoColunaConsulta;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO.OrdenacaoColunaLancamento;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.EstudoCotaService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações referentes às
 * telas de consulta de diferenças e lançamento de diferenças.
 * 
 * @author Discover Technology
 */
@Resource
@Path("/estoque/diferenca")
public class DiferencaEstoqueController {

	private Result result;
	
	private HttpSession httpSession;
	
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private EstudoCotaService estudoCotaService; 
	
	@Autowired
	private EstudoService estudoService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private static final String FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE = "filtroPesquisaLancamento";
	
	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaConsultaFaltasSobras";
	
	private static final String LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE = "listaNovasDiferencas";
	
	private static final String LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE = "listaNovasDiferencasVO";
	
	private static final String LISTA_DIFERENCAS_PESQUISADAS_SESSION_ATTRIBUTE = "listaDiferencasPesquisadas";
	
	private static final String MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE = "mapaRateiosCadastrados";
	
	private static final String MODO_INCLUSAO_SESSION_ATTRIBUTE = "modoInclusaoDiferenca";

	public DiferencaEstoqueController(Result result, 
								 	  HttpSession httpSession,
								 	  HttpServletResponse httpServletResponse) {
		
		this.result = result;
		this.httpSession = httpSession;
		this.httpServletResponse = httpServletResponse;
	}

	@Get
	public void exportar(FileType fileType) throws IOException {
				
		if (fileType == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de arquivo não encontrado!");
		}
		
		FiltroConsultaDiferencaEstoqueDTO filtroSessao = this.obterFiltroParaExportacao();
		
		List<Diferenca> listaDiferencas = diferencaEstoqueService.obterDiferencas(filtroSessao);
		
		List<DiferencaVO> listaConsultaDiferenca = new LinkedList<DiferencaVO>();
		
		BigInteger qtdeTotalDiferencas = BigInteger.ZERO;
		BigDecimal valorTotalDiferencas = BigDecimal.ZERO;
		
		BigDecimal valorDesconto = null;
		
		for (Diferenca diferenca : listaDiferencas) {
			
			DiferencaVO consultaDiferencaVO = new DiferencaVO();
			
			consultaDiferencaVO.setId(diferenca.getId());
			
			consultaDiferencaVO.setDataLancamento(
				DateUtil.formatarData(
					diferenca.getMovimentoEstoque().getData(), Constantes.DATE_PATTERN_PT_BR));
			
			consultaDiferencaVO.setCodigoProduto(diferenca.getProdutoEdicao().getProduto().getCodigo());
			
			consultaDiferencaVO.setDescricaoProduto(diferenca.getProdutoEdicao().getProduto().getNome());
			
			consultaDiferencaVO.setDescricaoProduto(diferenca.getProdutoEdicao().getProduto().getNome());
			consultaDiferencaVO.setNumeroEdicao(diferenca.getProdutoEdicao().getNumeroEdicao().toString());
			
			consultaDiferencaVO.setPrecoVenda(
				CurrencyUtil.formatarValor(diferenca.getProdutoEdicao().getPrecoVenda()));
			
			valorDesconto = BigDecimal.ZERO;
			
			if (diferenca.getProdutoEdicao().getDesconto() != null) {
				
				valorDesconto = diferenca.getProdutoEdicao().getDesconto();
			}
			
			consultaDiferencaVO.setPrecoDesconto(
				CurrencyUtil.formatarValor(diferenca.getProdutoEdicao().getPrecoVenda()
					.subtract(valorDesconto)));

			if (diferenca.getProdutoEdicao().getPrecoVenda() != null
					&& diferenca.getProdutoEdicao().getDesconto() != null) {
				
				consultaDiferencaVO.setPrecoDesconto(
					CurrencyUtil.formatarValor(diferenca.getProdutoEdicao().getPrecoVenda()
						.subtract(diferenca.getProdutoEdicao().getDesconto())));
			
			} else {
				
				consultaDiferencaVO.setPrecoDesconto("");
			}
			
			consultaDiferencaVO.setTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());
			
			if (diferenca.getItemRecebimentoFisico() != null) {
				consultaDiferencaVO.setNumeroNotaFiscal(
					diferenca.getItemRecebimentoFisico().getItemNotaFiscal().getNotaFiscal().getNumero().toString());
			} else {
				consultaDiferencaVO.setNumeroNotaFiscal(" - ");
			}
			
			consultaDiferencaVO.setQuantidade(diferenca.getQtde());
			
			consultaDiferencaVO.setStatusAprovacao(
				diferenca.getMovimentoEstoque().getStatus().getDescricaoAbreviada());
			
			consultaDiferencaVO.setMotivoAprovacao(diferenca.getMovimentoEstoque().getMotivo());
			
			consultaDiferencaVO.setValorTotalDiferenca(
				CurrencyUtil.formatarValor(diferenca.getValorTotalDiferenca()));
			
			listaConsultaDiferenca.add(consultaDiferencaVO);
			
			qtdeTotalDiferencas = 
				qtdeTotalDiferencas.add(diferenca.getQtde());
			
			valorTotalDiferencas = valorTotalDiferencas.add(diferenca.getValorTotalDiferenca());
		}
		
		String valorTotalDiferencasFormatado = 
			CurrencyUtil.formatarValorComSimbolo(valorTotalDiferencas);
		
		ResultadoDiferencaVO resultadoDiferencaVO = 
			new ResultadoDiferencaVO(null, qtdeTotalDiferencas, valorTotalDiferencasFormatado);
		
		FileExporter.to("consulta-faltas-sobras", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtroSessao, resultadoDiferencaVO, 
				listaConsultaDiferenca, DiferencaVO.class, this.httpServletResponse);
	}
	
	@Get
	@Rules(Permissao.ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS)
	public void lancamento() {
		
		this.carregarCombosLancamento();
		
		this.limparSessao();
		
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
			
			Long qtdeTotalRegistros = this.diferencaEstoqueService.obterTotalDiferencasLancamento(filtro);
			
			this.processarDiferencasLancamento(listaLancamentoDiferencas, filtro, qtdeTotalRegistros.intValue());
		}
	}
	
	@Post
	@Path("/lancamento/pesquisa/novos")
	@SuppressWarnings("unchecked")
	public void pesquisarLancamentosNovos(Date dataMovimento, TipoDiferenca tipoDiferenca,
										  String sortorder, String sortname, Integer page, Integer rp) {
		
		Set<Diferenca> listaNovasDiferencas = (Set<Diferenca>)
			this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
		
		if (listaNovasDiferencas != null 
				&& !listaNovasDiferencas.isEmpty()) {

			FiltroLancamentoDiferencaEstoqueDTO filtro = 
				this.carregarFiltroPesquisaLancamentos(
					dataMovimento, tipoDiferenca, sortorder, sortname, page, rp);
			
			processarDiferencasLancamentoNovos(listaNovasDiferencas, filtro);
			
		} else {
			
			ResultadoDiferencaVO resultadoDiferencaVO = new ResultadoDiferencaVO();
			
			TableModel<CellModelKeyValue<DiferencaVO>> tableModel =
					new TableModel<CellModelKeyValue<DiferencaVO>>();
				
			tableModel.setRows(new ArrayList<CellModelKeyValue<DiferencaVO>>());

			tableModel.setTotal(0);
			
			tableModel.setPage(1);
			
			resultadoDiferencaVO.setTableModel(tableModel);
			
			resultadoDiferencaVO.setQtdeTotalDiferencas(BigInteger.ZERO);
			
			resultadoDiferencaVO.setValorTotalDiferencas("");
			
			result.use(Results.json()).from(resultadoDiferencaVO, "result").recursive().serialize();
		}
	}
	
	@Post
	@Path("/lancamento/novo")
	@SuppressWarnings("unchecked")
	public void carregarNovasDiferencas(String dataMovimentoFormatada, TipoDiferenca tipoDiferenca) {
		
		List<DiferencaVO> listaNovasDiferencas = new ArrayList<DiferencaVO>();
		
		List<DiferencaVO> listaDiferencasCadastradas =
			(List<DiferencaVO>) this.httpSession.getAttribute(LISTA_DIFERENCAS_PESQUISADAS_SESSION_ATTRIBUTE);
		
		int qtdeDiferencasCadastradas = 0;
		
		if (listaDiferencasCadastradas != null) {
			
			qtdeDiferencasCadastradas = listaDiferencasCadastradas.size();
		}
		
		int qtdeInicialPadrao = qtdeDiferencasCadastradas + 50;
		
		for (int indice = listaNovasDiferencas.size(); indice < qtdeInicialPadrao; indice++) {
			
			DiferencaVO diferenca = new DiferencaVO();
			
			diferenca.setId(Long.valueOf(indice));
			
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
	public void cadastrarNovasDiferencas(TipoDiferenca tipoDiferenca, boolean lancamentoPorCota, String codigoProduto,
										 Integer edicaoProduto, Long diferenca, boolean direcionadoParaEstoque,
										 List<Integer> listaNumeroCota, List<Long> diferencas, Date dataNotaEnvio,
										 Integer numeroCota, List<String> listaCodigoProduto, List<Long> valorDiferencasProdNota) {
		//TODO
		/*if (listaNovasDiferencas == null 
				|| listaNovasDiferencas.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha os dados para lançamento!");
		}
		
		this.validarPreenchimentoNovasDiferencas(listaNovasDiferencas);
		
		this.validarProdutosDuplicadosLancamento(listaNovasDiferencas);
		
		Set<Diferenca> listaDiferencas = (Set<Diferenca>)
			this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
		
		if (listaDiferencas == null) {
			
			listaDiferencas = new HashSet<Diferenca>();
		}
		
		BigDecimal valorDesconto = null;
		
		Long id = 0L;
		
		for (Diferenca diferenca : listaDiferencas) {
			diferenca.setId(id);
			id++;
		}
		
		for (DiferencaVO diferencaVO : listaNovasDiferencas) {
			
			this.validarNovaDiferenca(diferencaVO, dataMovimento, tipoDiferenca);
			
			Diferenca diferenca = new Diferenca();
			
			diferenca.setId(id);
			
			ProdutoEdicao produtoEdicao =
				this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
					diferencaVO.getCodigoProduto(), diferencaVO.getNumeroEdicao());
			
			diferenca.setProdutoEdicao(produtoEdicao);
			diferenca.setQtde(diferencaVO.getQuantidade());
			diferenca.setStatusConfirmacao(StatusConfirmacao.PENDENTE);
			diferenca.setTipoDiferenca(tipoDiferenca);
			diferenca.setAutomatica(false);
			
			BigDecimal valorTotalDiferenca = BigDecimal.ZERO;
			
			valorDesconto = BigDecimal.ZERO;
			
			if (produtoEdicao.getDesconto() != null) {
				
				valorDesconto = produtoEdicao.getDesconto();
			}
			
			if (TipoDiferenca.FALTA_DE.equals(tipoDiferenca)
					|| TipoDiferenca.SOBRA_DE.equals(tipoDiferenca)) {
				
				valorTotalDiferenca =
					produtoEdicao.getPrecoVenda().subtract(valorDesconto).multiply(
						new BigDecimal(produtoEdicao.getPacotePadrao())).multiply( new BigDecimal( diferenca.getQtde() ) );
				
			} else if (TipoDiferenca.FALTA_EM.equals(tipoDiferenca)
							|| TipoDiferenca.SOBRA_EM.equals(tipoDiferenca)) {
				
				valorTotalDiferenca =
					produtoEdicao.getPrecoVenda().subtract(
						valorDesconto).multiply(new BigDecimal( diferenca.getQtde() ) );
			}
			
			
			
			diferenca.setValorTotalDiferenca(valorTotalDiferenca);
			
			MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
			
			movimentoEstoque.setData(dataMovimento);
			movimentoEstoque.setQtde(diferencaVO.getQuantidade());
			movimentoEstoque.setProdutoEdicao(produtoEdicao);
			movimentoEstoque.setStatus(StatusAprovacao.PENDENTE);
			
			diferenca.setMovimentoEstoque(movimentoEstoque);

			listaDiferencas.add(diferenca);
			
			id ++;
		}
		
		Set<DiferencaVO> listaNovasDiferencasVO = 
			(HashSet<DiferencaVO>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
		
		if (listaNovasDiferencasVO == null) {
			
			listaNovasDiferencasVO = new HashSet<DiferencaVO>();
		}
		
		id = 0L;
		
		for (DiferencaVO diferencaVO : listaNovasDiferencasVO) {
			diferencaVO.setId(id);
			id++;
		}
		
		listaNovasDiferencasVO.addAll(listaNovasDiferencas);
		
		this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE, listaNovasDiferencasVO);
		
		this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE, listaDiferencas);
		
		this.httpSession.setAttribute(MODO_INCLUSAO_SESSION_ATTRIBUTE, true);*/
		
		result.use(Results.json()).from("").serialize();
	}

	@Get
	@Rules(Permissao.ROLE_ESTOQUE_CONSULTA_FALTAS_SOBRAS)
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
		
		this.validarEntradaDadosPesquisa(codigoProduto, numeroEdicao, idFornecedor,
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
	
	@Post
	@Path("/lancamento/rateio")
	@SuppressWarnings("unchecked")
	public void carregarRateio(Long idDiferenca) {

		List<RateioCotaVO> listaNovosRateiosCota = new ArrayList<RateioCotaVO>();
		
		Map<Long, List<RateioCotaVO>> mapaRateiosCadastrados =
			(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		List<RateioCotaVO> listaRateiosCadastrados = new ArrayList<RateioCotaVO>();
		
		if (mapaRateiosCadastrados == null) {
			
			mapaRateiosCadastrados = new HashMap<Long, List<RateioCotaVO>>();
			
		} else {
			
			listaRateiosCadastrados = mapaRateiosCadastrados.get(idDiferenca);
		}
		
		if (listaRateiosCadastrados != null && !listaRateiosCadastrados.isEmpty()) {
			
			listaNovosRateiosCota.addAll(listaRateiosCadastrados);
		}
		
		int qtdeInicialPadrao = listaNovosRateiosCota.size() + 50;
		
		for (int indice = listaNovosRateiosCota.size(); indice < qtdeInicialPadrao; indice++) {
			
			RateioCotaVO rateioCota = new RateioCotaVO();
			
			rateioCota.setId(Long.valueOf(indice));
			
			rateioCota.setIdDiferenca(idDiferenca);
			
			listaNovosRateiosCota.add(rateioCota);
		}
		
		TableModel<CellModelKeyValue<RateioCotaVO>> tableModel =
			new TableModel<CellModelKeyValue<RateioCotaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNovosRateiosCota));
		
		tableModel.setTotal(qtdeInicialPadrao);
		
		tableModel.setPage(1);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	@Post
	@Path("/lancamento/rateio/obterQuantidadeReparte")
	public void obterQuantidadeReparteCota(Long idDiferenca, Integer numeroCota) {
		
		DiferencaVO diferenca = this.obterDiferencaPorId(idDiferenca);
		
		Date dataMovimento = DateUtil.parseDataPTBR(diferenca.getDataLancamento());
		
		ProdutoEdicao produtoEdicao =
			this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
				diferenca.getCodigoProduto(), diferenca.getNumeroEdicao());
		
		EstudoCota estudoCota =
			this.estudoCotaService.obterEstudoCotaDeLancamentoComEstudoFechado(
				dataMovimento, produtoEdicao.getId(), numeroCota);
		
		if (estudoCota != null 
				&& estudoCota.getQtdeEfetiva() != null) {
		
			result.use(Results.json()).from(estudoCota.getQtdeEfetiva(), "result").serialize();
		
		} else {
			
			result.use(Results.json()).from("", "result").serialize();
		}
	}

	@Post
	@Path("/lancamento/rateio/validarEstudo")
	public void verificarExistenciaEstudo(Long idDiferenca) {
		
		DiferencaVO diferencaVO = this.obterDiferencaPorId(idDiferenca);
		
		Date dataMovimento = DateUtil.parseDataPTBR(diferencaVO.getDataLancamento());
		
		ProdutoEdicao produtoEdicao =
			this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
				diferencaVO.getCodigoProduto(), diferencaVO.getNumeroEdicao());
		
		Estudo estudo =
			this.estudoService.obterEstudoDoLancamentoPorDataProdutoEdicao(dataMovimento, produtoEdicao.getId());
		
		if (estudo == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe Estudo na data deste lançamento!");
		}

		result.use(Results.json()).from("", "result").serialize();
	}
	
	@Post
	@Path("/lancamento/cadastrarRateioCotas")
	@SuppressWarnings("unchecked")
	public void cadastrarRateioCotas(List<RateioCotaVO> listaNovosRateios, Long idDiferenca) {
		
		if (listaNovosRateios == null 
				|| listaNovosRateios.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha os dados para o rateio!");
		}
		
		this.validarPreenchimentoNovosRateios(listaNovosRateios);
		
		this.validarCotasDuplicadasRateio(listaNovosRateios);
		
		this.validarSomaQuantidadeRateio(listaNovosRateios, idDiferenca);
		
		Map<Long, List<RateioCotaVO>> mapaRateiosCadastrados =
			(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		List<RateioCotaVO> listaRateiosCadastrados = null;
		
		if (mapaRateiosCadastrados == null) {

			mapaRateiosCadastrados = new HashMap<Long, List<RateioCotaVO>>();

		} else {
			
			listaRateiosCadastrados = mapaRateiosCadastrados.get(idDiferenca);
		}
		
		if (listaRateiosCadastrados == null || listaRateiosCadastrados.isEmpty()) {
			
			listaRateiosCadastrados = new ArrayList<RateioCotaVO>();
		}
		
		for (RateioCotaVO rateioCotaVO : listaNovosRateios) {
		
			this.validarNovoRateio(rateioCotaVO);

			if (!listaRateiosCadastrados.contains(rateioCotaVO)) {
				
				listaRateiosCadastrados.add(rateioCotaVO);
			}
		}
		
		mapaRateiosCadastrados.put(idDiferenca, listaRateiosCadastrados);
		
		this.httpSession.setAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE, mapaRateiosCadastrados);
		
		result.use(Results.json()).from("", "result").serialize();
	}
	
	@Post
	@Path("/lancamento/limparSessao")
	@SuppressWarnings("unchecked")
	public void limparDadosSessao(boolean confirmado) {
	
		Set<Diferenca> listaNovasDiferencas =
			(Set<Diferenca>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
		
		Map<Long, List<RateioCotaVO>> mapaRateiosCadastrados =
			(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		if (!confirmado) {
		
			if ((listaNovasDiferencas != null && !listaNovasDiferencas.isEmpty())
					|| (mapaRateiosCadastrados != null && !mapaRateiosCadastrados.isEmpty())) {
	
				result.use(Results.json()).from(new ConfirmacaoVO(false), "result").serialize();
				
				return;
			}
		}
		
		this.limparSessao();
			
		result.use(Results.json()).from(new ConfirmacaoVO(true), "result").serialize();
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
	@Path("/lancamento/excluir")
	@SuppressWarnings("unchecked")
	public void excluirFaltaSobra(Long idDiferenca){
		
		Set<Diferenca> listaNovasDiferencas = (Set<Diferenca>)
			this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);

		if (listaNovasDiferencas != null) {
			
			Diferenca diferencaARemover = null;
			
			for (Diferenca diferenca : listaNovasDiferencas) {
				
				if (diferenca.getId().equals(idDiferenca)) {
					
					diferencaARemover = diferenca;
					
					break;
				}
			}
			
			if (diferencaARemover != null) {
				
				listaNovasDiferencas.remove(diferencaARemover);
			}
		}
		
		this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE, listaNovasDiferencas);
		
		Set<DiferencaVO> listaNovasDiferencasVO = (Set<DiferencaVO>)
			this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
		
		if (listaNovasDiferencasVO != null) {
			
			DiferencaVO diferencaVOARemover = null;
			
			for (DiferencaVO diferencaVO : listaNovasDiferencasVO) {
				
				if (diferencaVO.getId().equals(idDiferenca)) {
					
					diferencaVOARemover = diferencaVO;
					break;
				}
			}
			
			if (diferencaVOARemover != null) {
				
				listaNovasDiferencasVO.remove(diferencaVOARemover);
			}
		}
		
		this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE, listaNovasDiferencasVO);
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = 
			(FiltroLancamentoDiferencaEstoqueDTO) 
				this.httpSession.getAttribute(FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE);
		
		this.pesquisarLancamentosNovos(
			filtro.getDataMovimento(), filtro.getTipoDiferenca(), filtro.getPaginacao().getSortOrder(), 
				filtro.getOrdenacaoColuna().toString(), filtro.getPaginacao().getPaginaAtual(), 
					filtro.getPaginacao().getQtdResultadosPorPagina());
	}

	@Post
	@SuppressWarnings("unchecked")
	public void confirmarLancamentos() {
		
		Boolean modoInclusao = (Boolean) this.httpSession.getAttribute(MODO_INCLUSAO_SESSION_ATTRIBUTE);
		
		Set<Diferenca> listaNovasDiferencas =
			(Set<Diferenca>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
		
		if (modoInclusao != null 
				&& modoInclusao
				&& (listaNovasDiferencas == null
						|| listaNovasDiferencas.isEmpty())) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há lançamentos a confirmar!");
		}
		
		 Map<Long, List<RateioCotaVO>> mapaRateioCotas =
			(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		FiltroLancamentoDiferencaEstoqueDTO filtroPesquisa =
			(FiltroLancamentoDiferencaEstoqueDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE);
		
		this.diferencaEstoqueService.efetuarAlteracoes(
			listaNovasDiferencas, mapaRateioCotas, filtroPesquisa, this.getUsuario().getId());
		
		result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
		
		this.limparSessao();
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
		
		List<Fornecedor> listaFornecedor =
			fornecedorService.obterFornecedoresPorProduto(codigoProduto, GrupoFornecedor.PUBLICACAO);
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo =
			new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(
				new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial())
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
	 * Obtém o filtro de pesquisa para exportação.
	 */
	private FiltroConsultaDiferencaEstoqueDTO obterFiltroParaExportacao() {
		
		FiltroConsultaDiferencaEstoqueDTO filtroSessao =
			(FiltroConsultaDiferencaEstoqueDTO) 
				this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null) {
			
			if (filtroSessao.getPaginacao() != null) {
				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
			if (filtroSessao.getCodigoProduto() != null
				&& filtroSessao.getNumeroEdicao() != null) {
		
				ProdutoEdicao produtoEdicao =
					this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
						filtroSessao.getCodigoProduto(), filtroSessao.getNumeroEdicao().toString());
				
				if (produtoEdicao != null) {
					
					filtroSessao.setNomeProduto(produtoEdicao.getProduto().getNome());
				}
			}
			
			if (filtroSessao.getIdFornecedor() != null) {
				
				Fornecedor fornecedor =
					this.fornecedorService.obterFornecedorPorId(filtroSessao.getIdFornecedor());
				
				if (fornecedor != null) {
					
					filtroSessao.setNomeFornecedor(fornecedor.getJuridica().getRazaoSocial());
				}
			}
		}
		
		return filtroSessao;
	}
	
	/*
	 * Processa o resultado das diferenças para lançamento.
	 *  
	 * @param listaDiferencas - lista de diferenças
	 * @param filtro - filtro da pesquisa
	 */
	private void processarDiferencasLancamento(List<Diferenca> listaDiferencas, 
											   FiltroLancamentoDiferencaEstoqueDTO filtro,
											   Integer qtdeTotalRegistros) {

		List<DiferencaVO> listaLancamentosDiferenca = new LinkedList<DiferencaVO>();
		
		BigInteger qtdeTotalDiferencas = BigInteger.ZERO;
		
		BigDecimal valorTotalDiferencas = BigDecimal.ZERO;
		
		BigDecimal valorDesconto = null;
		
		for (Diferenca diferenca : listaDiferencas) {
			
			ProdutoEdicao produtoEdicao = diferenca.getProdutoEdicao();
			
			Produto produto = produtoEdicao.getProduto();
			
			DiferencaVO lancamentoDiferenca = new DiferencaVO();
			
			if (diferenca.getId() != null) {
				
				lancamentoDiferenca.setId(diferenca.getId());
			}
			
			lancamentoDiferenca.setCodigoProduto(produto.getCodigo());
			lancamentoDiferenca.setDescricaoProduto(produto.getDescricao());
			lancamentoDiferenca.setNumeroEdicao(produtoEdicao.getNumeroEdicao().toString());
			
			lancamentoDiferenca.setPrecoVenda(CurrencyUtil.formatarValor(produtoEdicao.getPrecoVenda()));
			
			valorDesconto = BigDecimal.ZERO;
			
			if (diferenca.getProdutoEdicao().getDesconto() != null) {
				
				valorDesconto = diferenca.getProdutoEdicao().getDesconto();
			}
			
			lancamentoDiferenca.setPrecoDesconto(
				CurrencyUtil.formatarValor(diferenca.getProdutoEdicao().getPrecoVenda()
					.subtract(valorDesconto)));
			
			lancamentoDiferenca.setPacotePadrao(String.valueOf(produtoEdicao.getPacotePadrao()));
			lancamentoDiferenca.setQuantidade(diferenca.getQtde());
			lancamentoDiferenca.setTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());
			lancamentoDiferenca.setAutomatica(diferenca.isAutomatica());
			
			if (diferenca.getMovimentoEstoque() != null) {
			
				Date dataLancamento = diferenca.getMovimentoEstoque().getData();
				
				if (dataLancamento != null) {
					
					lancamentoDiferenca.setDataLancamento(DateUtil.formatarDataPTBR(dataLancamento));
				}
			}

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

		tableModel.setTotal(qtdeTotalRegistros);
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		String valorTotalDiferencasFormatado = 
			CurrencyUtil.formatarValorComSimbolo(valorTotalDiferencas);
		
		ResultadoDiferencaVO resultadoLancamentoDiferenca = 
			new ResultadoDiferencaVO(tableModel, qtdeTotalDiferencas, valorTotalDiferencasFormatado);
		
		this.httpSession.setAttribute(
			LISTA_DIFERENCAS_PESQUISADAS_SESSION_ATTRIBUTE, listaLancamentosDiferenca);
	
		result.use(Results.json()).from(resultadoLancamentoDiferenca, "result").recursive().serialize();
	}
	
	/*
	 * Processa o resultado das novas diferenças para lançamento.
	 *  
	 * @param listaDiferencas - lista de diferenças
	 * @param filtro - filtro da pesquisa
	 */
	private void processarDiferencasLancamentoNovos(Set<Diferenca> listaDiferencas, 
												   	FiltroLancamentoDiferencaEstoqueDTO filtro) {

		List<DiferencaVO> listaLancamentosDiferenca = new LinkedList<DiferencaVO>();
		
		BigInteger qtdeTotalDiferencas = BigInteger.ZERO;
		
		BigDecimal valorTotalDiferencas = BigDecimal.ZERO;
		
		BigDecimal valorDesconto = null;
		
		for (Diferenca diferenca : listaDiferencas) {
			
			ProdutoEdicao produtoEdicao = diferenca.getProdutoEdicao();
			
			Produto produto = produtoEdicao.getProduto();
			
			DiferencaVO lancamentoDiferenca = new DiferencaVO();
			
			if (diferenca.getId() != null) {
				
				lancamentoDiferenca.setId(diferenca.getId());
			}
			
			lancamentoDiferenca.setCodigoProduto(produto.getCodigo());
			lancamentoDiferenca.setDescricaoProduto(produto.getDescricao());
			lancamentoDiferenca.setNumeroEdicao(produtoEdicao.getNumeroEdicao().toString());
			
			lancamentoDiferenca.setPrecoVenda(CurrencyUtil.formatarValor(produtoEdicao.getPrecoVenda()));
			
			valorDesconto = BigDecimal.ZERO;
			
			if (diferenca.getProdutoEdicao().getDesconto() != null) {
				
				valorDesconto = diferenca.getProdutoEdicao().getDesconto();
			}
			
			lancamentoDiferenca.setPrecoDesconto(
				CurrencyUtil.formatarValor(diferenca.getProdutoEdicao().getPrecoVenda()
					.subtract(valorDesconto)));
			
			lancamentoDiferenca.setPacotePadrao(String.valueOf(produtoEdicao.getPacotePadrao()));
			lancamentoDiferenca.setQuantidade(diferenca.getQtde());
			lancamentoDiferenca.setTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());
			
			if (diferenca.getMovimentoEstoque() != null) {
				
				Date dataLancamento = diferenca.getMovimentoEstoque().getData();
				
				if (dataLancamento != null) {
					
					lancamentoDiferenca.setDataLancamento(DateUtil.formatarDataPTBR(dataLancamento));
				}
			}
			
			lancamentoDiferenca.setValorTotalDiferenca(
				CurrencyUtil.formatarValor(diferenca.getValorTotalDiferenca()));
			
			lancamentoDiferenca.setCadastrado(true);
			
			listaLancamentosDiferenca.add(lancamentoDiferenca);
			
			qtdeTotalDiferencas = 
				qtdeTotalDiferencas.add(diferenca.getQtde());
			
			valorTotalDiferencas = valorTotalDiferencas.add(diferenca.getValorTotalDiferenca());
		}
		
		List<DiferencaVO> listaLancamentosDiferencaPaginada =
			PaginacaoUtil.paginarEOrdenarEmMemoria(
				listaLancamentosDiferenca, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());
		
		TableModel<CellModelKeyValue<DiferencaVO>> tableModel =
			new TableModel<CellModelKeyValue<DiferencaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentosDiferencaPaginada));

		tableModel.setTotal(listaLancamentosDiferenca.size());
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		String valorTotalDiferencasFormatado = 
			CurrencyUtil.formatarValorComSimbolo(valorTotalDiferencas);
		
		ResultadoDiferencaVO resultadoLancamentoDiferenca = 
			new ResultadoDiferencaVO(tableModel, qtdeTotalDiferencas, valorTotalDiferencasFormatado);
		
		this.httpSession.setAttribute(
			LISTA_DIFERENCAS_PESQUISADAS_SESSION_ATTRIBUTE, listaLancamentosDiferencaPaginada);
	
		result.use(Results.json()).from(resultadoLancamentoDiferenca, "result").recursive().serialize();
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
		
		BigInteger qtdeTotalDiferencas = BigInteger.ZERO;
		BigDecimal valorTotalDiferencas = BigDecimal.ZERO;
		
		int quantidadeRegistros = diferencaEstoqueService.obterTotalDiferencas(filtro).intValue();
		
		BigDecimal valorDesconto = null;
		
		for (Diferenca diferenca : listaDiferencas) {
			
			DiferencaVO consultaDiferencaVO = new DiferencaVO();
			
			consultaDiferencaVO.setId(diferenca.getId());
			
			consultaDiferencaVO.setDataLancamento(
				DateUtil.formatarData(diferenca.getMovimentoEstoque().getData(),
									  Constantes.DATE_PATTERN_PT_BR));
			
			consultaDiferencaVO.setCodigoProduto(diferenca.getProdutoEdicao().getProduto().getCodigo());
			consultaDiferencaVO.setDescricaoProduto(diferenca.getProdutoEdicao().getProduto().getNome());
			consultaDiferencaVO.setNumeroEdicao(diferenca.getProdutoEdicao().getNumeroEdicao().toString());
			
			consultaDiferencaVO.setPrecoVenda(
				CurrencyUtil.formatarValor(diferenca.getProdutoEdicao().getPrecoVenda()));
			
			valorDesconto = BigDecimal.ZERO;
			
			if (diferenca.getProdutoEdicao().getDesconto() != null) {
				
				valorDesconto = diferenca.getProdutoEdicao().getDesconto();
			}
			
			consultaDiferencaVO.setPrecoDesconto(
				CurrencyUtil.formatarValor(diferenca.getProdutoEdicao().getPrecoVenda()
					.subtract(valorDesconto)));
			
			consultaDiferencaVO.setTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());
			
			if (diferenca.getItemRecebimentoFisico() != null) {
				consultaDiferencaVO.setNumeroNotaFiscal(
					diferenca.getItemRecebimentoFisico().getItemNotaFiscal().getNotaFiscal().getNumero().toString());
			} else {
				consultaDiferencaVO.setNumeroNotaFiscal(" - ");
			}
			
			consultaDiferencaVO.setQuantidade(diferenca.getQtde());
			
			consultaDiferencaVO.setStatusAprovacao(
				diferenca.getMovimentoEstoque().getStatus().getDescricaoAbreviada());
			
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
			CurrencyUtil.formatarValorComSimbolo(valorTotalDiferencas);
		
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
		
		if (!dataInicial.trim().isEmpty() && dataFinal.isEmpty()) {
			
			dataFinal = DateUtil.formatarDataPTBR(new Date());
		}
		
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
				TipoMensagem.WARNING, "O preenchimento do campo [Data de Movimento] é obrigatório!");
		}
		
		if (!DateUtil.isValidDatePTBR(dataMovimentoFormatada)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Data de Movimento inválida");
		}
	}
	
	/*
	 * Valida a entrada de dados para pesquisa de diferença de estoque.
	 * 
	 * @param codigoProduto
	 * @param numeroEdicao
	 * @param idFornecedor
	 * @param dataInicial
	 * @param dataFinal
	 * @param tipoDiferenca
	 * 
	 */
	private void validarEntradaDadosPesquisa(String codigoProduto, Long numeroEdicao,
											 Long idFornecedor, String dataInicial,
											 String dataFinal, TipoDiferenca tipoDiferenca) {
			
		if (dataInicial != null && !dataInicial.trim().isEmpty()
				&& !DateUtil.isValidDatePTBR(dataInicial)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Data Inicial inválida");
		}
		
		if (dataFinal != null && !dataFinal.trim().isEmpty()
				&& !DateUtil.isValidDatePTBR(dataFinal)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Data Final inválida");
		}
		
		if (!dataFinal.trim().isEmpty() && dataInicial.isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O preenchimento do campo [Data Inicial] é obrigatório!");
		}
		
		if (!dataInicial.trim().isEmpty() && dataFinal.isEmpty()) {
			
			if (DateUtil.parseDataPTBR(dataInicial).compareTo(new Date()) == 1) {
				
				throw new ValidacaoException(
					TipoMensagem.WARNING, "O campo [Data Inicial] não deve ser maior que a data do dia!");
			}
		}
		
		if (DateUtil.isDataInicialMaiorDataFinal(DateUtil.parseDataPTBR(dataInicial),
												 DateUtil.parseDataPTBR(dataFinal))) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O campo [Data Incial] não deve ser maior que o campo [Data Final]!");
		}
		
		if ((codigoProduto == null || codigoProduto.trim().isEmpty()) && numeroEdicao == null 
				&& (dataInicial == null || dataInicial.trim().isEmpty())
				&& (dataFinal == null || dataFinal.trim().isEmpty())
				&& idFornecedor == null && tipoDiferenca == null) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING,
					"Para realizar a pesquisa é necessário informar um ou mais filtro(s) da pesquisa!");
		}
		
	}
	
	/**
	 * Valida a entrada de dados para pesquisa de lançamentos de diferença de estoque.
	 * 
	 * @param dataMovimentoFormatada - data de movimento formatado
	 * @param tipoDiferenca - tipo de diferença
	 */
	@Post
	@Path("/lancamento/novo/validar")
	public void validarEntradaDadosNovoLancamento(String dataMovimentoFormatada, TipoDiferenca tipoDiferenca) {
		
		if (dataMovimentoFormatada == null 
				|| dataMovimentoFormatada.trim().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O preenchimento do campo [Data de Movimento] é obrigatório!");
		}
		
		if (tipoDiferenca == null) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O preenchimento do campo [Tipo de Diferença] é obrigatório!");
		}
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	/*
	 * Valida o preenchimento das novas diferenças.
	 * 
	 * @param listaNovasDiferencas - lista das novas diferenças
	 */
	private void validarPreenchimentoNovasDiferencas(List<DiferencaVO> listaNovasDiferencas) {

		List<Long> linhasComErro = new ArrayList<Long>();


		for (DiferencaVO diferenca : listaNovasDiferencas) {
			
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
					|| BigDecimal.ZERO.equals(diferenca.getQuantidade())) {
				
				diferencaInvalida = true;
			}
			
			if (diferencaInvalida) {

				linhasComErro.add(diferenca.getId());
			}
		}
		
		if (!linhasComErro.isEmpty()) {
			
			ValidacaoVO validacao = 
				new ValidacaoVO(TipoMensagem.WARNING, "Existe(m) lançamento(s) preenchido(s) incorretamente!");
			
			validacao.setDados(linhasComErro);
			
			throw new ValidacaoException(validacao);
		}
	}
	
	/*
	 * Valida o preenchimentos dos novos rateios.
	 * 
	 * @param listaNovosRateios - lista dos novos rateios
	 */
	private void validarPreenchimentoNovosRateios(List<RateioCotaVO> listaNovosRateios) {

		List<Long> linhasComErro = new ArrayList<Long>();

		for (RateioCotaVO rateioCotaVO : listaNovosRateios) {
			
			boolean rateioInvalido = false;
			
			if (rateioCotaVO.getNumeroCota() == null) {
				
				rateioInvalido = true;
			}
			
			if (rateioCotaVO.getNomeCota() == null
					|| rateioCotaVO.getNomeCota().trim().isEmpty()) {
				
				rateioInvalido = true;
			}
			
			if (rateioCotaVO.getReparteCota() == null) {
				
				rateioInvalido = true;
			}
			
			if (rateioCotaVO.getQuantidade() == null
					|| BigDecimal.ZERO.equals(rateioCotaVO.getQuantidade())) {
				
				rateioInvalido = true;
			}
			
			if (rateioInvalido) {
				
				linhasComErro.add(rateioCotaVO.getId());
			}
		}
		
		if (!linhasComErro.isEmpty()) {
			
			ValidacaoVO validacao = 
				new ValidacaoVO(TipoMensagem.WARNING, "Existe(m) rateio(s) preenchido(s) incorretamente!");
			
			validacao.setDados(linhasComErro);
			
			throw new ValidacaoException(validacao);
		}
	}
	
	/*
	 * Efetua a validação da somatória das quantidades 
	 * do rateio x quantidade da diferença.
	 * 
	 * @param listaNovosRateios - lista do nos rateios
	 * @param idDiferenca - id da diferença
	 */
	private void validarSomaQuantidadeRateio(List<RateioCotaVO> listaNovosRateios, Long idDiferenca) {
		
		DiferencaVO diferencaVO = this.obterDiferencaPorId(idDiferenca);

		BigInteger somaQtdeRateio = BigInteger.ZERO;
		
		for (RateioCotaVO rateioCotaVO : listaNovosRateios) {
			
			somaQtdeRateio = somaQtdeRateio.add(rateioCotaVO.getQuantidade());
		}
		
		if (somaQtdeRateio.compareTo(diferencaVO.getQuantidade()) > 0) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "A somatória das quantidades de rateio (" 
					+ somaQtdeRateio + ") é maior que a quantidade da diferença (" 
					+ diferencaVO.getQuantidade() + ")!");
		}
	}
	
	/*
	 * Valida se há cotas duplicadas no rateio.
	 * 
	 * @param listaNovosRateios - lista dos novos rateios
	 */
	@SuppressWarnings("unchecked")
	private void validarCotasDuplicadasRateio(List<RateioCotaVO> listaNovosRateios) {
		
		if (listaNovosRateios == null) {
			
			return;
		}
		
		Collections.sort(listaNovosRateios, new BeanComparator("numeroCota"));
		
		List<Long> linhasComErro = new ArrayList<Long>();
		
		RateioCotaVO ultimoRateioCotaVO = null;
		
		for (RateioCotaVO rateioCotaVO : listaNovosRateios) {
			
			Integer numeroCota = rateioCotaVO.getNumeroCota();
			
			if (numeroCota == null) {
				
				continue;
			}
			
			if (ultimoRateioCotaVO != null) {
				
				if (numeroCota.equals(ultimoRateioCotaVO.getNumeroCota())) {
					
					linhasComErro.add(ultimoRateioCotaVO.getId());
					linhasComErro.add(rateioCotaVO.getId());
				}
			}
			
			ultimoRateioCotaVO = rateioCotaVO;
		}
		
		if (!linhasComErro.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, "Existem cotas duplicadas para o rateio!");
			
			validacao.setDados(linhasComErro);
		
			throw new ValidacaoException(validacao);
		}
	}
	
	/*
	 * Valida o cadastro de um novo rateio.
	 * 
	 * @param novoRateioCota - novo rateio
	 */
	private void validarNovoRateio(RateioCotaVO novoRateioCota) {
		
		List<Long> linhasComErro = new ArrayList<Long>();
		
		List<String> listaMensagensErro = new ArrayList<String>();
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(novoRateioCota.getNumeroCota());
		
		if (cota == null) {
			
			linhasComErro.add(novoRateioCota.getId());
			
			listaMensagensErro.add("Cota inválida: Número [" + novoRateioCota.getNumeroCota() + "] - Nome [" + novoRateioCota.getNomeCota() + " ]");
		}
		
		DiferencaVO diferencaVO = this.obterDiferencaPorId(novoRateioCota.getIdDiferenca());
		
		TipoDiferenca tipoDiferenca =
			Util.getEnumByStringValue(TipoDiferenca.values(), diferencaVO.getTipoDiferenca());
		
		if (TipoDiferenca.FALTA_DE.equals(tipoDiferenca)
				|| TipoDiferenca.FALTA_EM.equals(tipoDiferenca)) {
			
			if (novoRateioCota.getReparteCota() == null
					|| novoRateioCota.getQuantidade().compareTo(novoRateioCota.getReparteCota()) > 0) {
				
				linhasComErro.add(novoRateioCota.getId());
				
				listaMensagensErro.add("Quantidade do rateio para o tipo de diferença '" +
					tipoDiferenca.getDescricao() + "' não pode ser maior que a quantidade do reparte da cota!");
			}
		}
		
		if (!linhasComErro.isEmpty() && !listaMensagensErro.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaMensagensErro);
		
			validacao.setDados(linhasComErro);
		
			throw new ValidacaoException(validacao);
		}
	}
	
	/*
	 * Valida o cadastro de uma nova diferença.
	 * 
	 * @param diferenca - nova diferença
	 */
	private void validarNovaDiferenca(DiferencaVO diferenca, 
									  Date dataMovimento,
									  TipoDiferenca tipoDiferenca) {
		
		List<Long> linhasComErro = new ArrayList<Long>();
		
		List<String> listaMensagensErro = new ArrayList<String>();
		
		ProdutoEdicao produtoEdicao =
			this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
				diferenca.getCodigoProduto(), diferenca.getNumeroEdicao());
		
		if (produtoEdicao == null) {
			
			linhasComErro.add(diferenca.getId());
			
			listaMensagensErro.add("Produto inválido: Código [" + diferenca.getCodigoProduto() + "] - Edição [" + diferenca.getNumeroEdicao() + " ]");
		}

		if (TipoDiferenca.FALTA_DE.equals(tipoDiferenca)
				|| TipoDiferenca.FALTA_EM.equals(tipoDiferenca)) {
		
			if (diferenca.getQtdeEstoqueAtual() == null 
					|| (diferenca.getQuantidade().compareTo(diferenca.getQtdeEstoqueAtual()) > 0)) {
				
				linhasComErro.add(diferenca.getId());
				
				listaMensagensErro.add(
					"Quantidade de Exemplares para o tipo de diferença '" + tipoDiferenca.getDescricao() 
						+ "' não pode ser maior que a Quantidade em Estoque do produto!");
			}
		}
		
		if (!this.diferencaEstoqueService.validarDataLancamentoDiferenca(
				dataMovimento, produtoEdicao.getId(), tipoDiferenca)) {
			
			linhasComErro.add(diferenca.getId());
			
			listaMensagensErro.add("Prazo para lançamento de diferença esgotado para o Produto: Código [" + diferenca.getCodigoProduto() + "] - Edição [" + diferenca.getNumeroEdicao() + " ]");
		}
		
		if (!linhasComErro.isEmpty() && !listaMensagensErro.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaMensagensErro);
		
			validacao.setDados(linhasComErro);
		
			throw new ValidacaoException(validacao);
		}
	}
	
	/*
	 * Valida se há produtos duplicados no lançamento da diferença.
	 * 
	 * @param listaNovasDiferencas - lista das novas diferenças
	 */
	@SuppressWarnings("unchecked")
	private void validarProdutosDuplicadosLancamento(List<DiferencaVO> listaNovasDiferencas) {
		
		List<DiferencaVO> listaDiferencas = new ArrayList<DiferencaVO>();
		
		if (listaNovasDiferencas == null) {
			
			return;
		}
		
		Set<DiferencaVO> listaDiferencasCadastradas =
			(Set<DiferencaVO>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
		
		if (listaDiferencasCadastradas != null) {
			listaDiferencas.addAll(listaNovasDiferencas);
			listaDiferencas.addAll(listaDiferencasCadastradas);
		}
		
		ComparatorChain comparatorChain = new ComparatorChain();
		
		comparatorChain.addComparator(new BeanComparator("codigoProduto"));
		comparatorChain.addComparator(new BeanComparator("numeroEdicao"));
		
		Collections.sort(listaDiferencas, comparatorChain);
		
		List<Long> linhasComErro = new ArrayList<Long>();
		
		DiferencaVO ultimaDiferencaVO = null;
		
		for (DiferencaVO diferencaVO : listaDiferencas) {
			
			if (ultimaDiferencaVO != null) {
				
				if (diferencaVO.getCodigoProduto().trim().equalsIgnoreCase(ultimaDiferencaVO.getCodigoProduto())
						&& diferencaVO.getNumeroEdicao().trim().equalsIgnoreCase(ultimaDiferencaVO.getNumeroEdicao())) {
					
					if (!ultimaDiferencaVO.isCadastrado()) {
						
						linhasComErro.add(ultimaDiferencaVO.getId());
					}
					
					if (!diferencaVO.isCadastrado()) {
						
						linhasComErro.add(diferencaVO.getId());
					}
				}
			}
			
			ultimaDiferencaVO = diferencaVO;
		}
		
		if (!linhasComErro.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, "Existem produtos duplicados para o lançamento!");
			
			validacao.setDados(linhasComErro);
		
			throw new ValidacaoException(validacao);
		}
	}
	
	/*
	 * Obtém uma diferença pesquisada pelo seu id.
	 * 
	 * @param idDiferenca - id da diferença
	 * 
	 * @return DiferencaVO
	 */
	@SuppressWarnings("unchecked")
	private DiferencaVO obterDiferencaPorId(Long idDiferenca) {
		
		List<DiferencaVO> listaDiferencas =
			(List<DiferencaVO>) this.httpSession.getAttribute(LISTA_DIFERENCAS_PESQUISADAS_SESSION_ATTRIBUTE);
		
		if (listaDiferencas == null || listaDiferencas.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existem lançamentos de diferença");
		}
		
		for (DiferencaVO diferenca : listaDiferencas) {
			
			if (diferenca.getId().equals(idDiferenca)) {
				
				return diferenca;
			}
		}
		
		return null;
	}
	
	/*
	 * Limpa os dados da sessão.
	 */
	private void limparSessao() {
	
		this.httpSession.removeAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
		
		this.httpSession.removeAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
		
		this.httpSession.removeAttribute(LISTA_DIFERENCAS_PESQUISADAS_SESSION_ATTRIBUTE);
		
		this.httpSession.removeAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		this.httpSession.removeAttribute(MODO_INCLUSAO_SESSION_ATTRIBUTE);
	}
	
	/*
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
	
	//TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		
		usuario.setNome("Jornaleiro da Silva");
		
		return usuario;
	}
	
	@Post
	@Path("/lancamento/rateio/buscarDiferenca")
	public void buscarDiferenca(Long idDiferenca){
		
		DiferencaVO diferencaVO = null;
		
		if (idDiferenca == null){
			
			diferencaVO = new DiferencaVO();
//			diferencaVO.setNumeroEdicao("2");
//			diferencaVO.setPrecoVenda("20.34");
//			diferencaVO.setDescricaoProduto("tchans");
//			diferencaVO.setQuantidade(BigDecimal.TEN);
//			diferencaVO.setCodigoProduto("códigueta");
		} else {
			//TODO
			diferencaVO = new DiferencaVO();
					//this.diferencaEstoqueService.obterDiferenca(idDiferenca);
		}
		
		result.use(Results.json()).from(diferencaVO, "result").recursive().serialize();
	}
	
	@Post
	@Path("/lancamento/rateio/buscarReparteCotaPreco")
	public void buscarReparteCotaPreco(Long idProdutoEdicao, Integer numeroCota){
		
		//TODO
		System.out.println("idProdutoEdicao: " + idProdutoEdicao + " - " + "numeroCota:" + numeroCota);
		Object[] dados = new Object[2];
		dados[0] = 25;
		dados[1] = 3.98;
		
		result.use(Results.json()).from(dados, "result").serialize();
	}
	
	@Post
	@Path("/lancamento/rateio/buscarPrecoProdutoEdicao")
	public void buscarPrecoProdutoEdicao(String codigoProduto, Integer numeroEdicao){
		
		System.out.println("codigoProduto: " + codigoProduto + " - " + "numeroEdicao:" + numeroEdicao);
		Object[] dados = new Object[2];
		dados[0] = 25;
		dados[1] = 3.98;
		
		result.use(Results.json()).from(dados, "result").serialize();
	}
	
	@Post
	@Path("/lancamento/rateio/buscarProdutosCotaNota")
	public void	buscarProdutosCotaNota(Date dateNotaEnvio, Integer numeroCota){
		
		//TODO
		List<DiferencaVO> prods = new ArrayList<DiferencaVO>();
		
		DiferencaVO diferencaVO = new DiferencaVO();
		diferencaVO.setCodigoProduto("123");
		diferencaVO.setDescricaoProduto("nome produto");
		diferencaVO.setNumeroEdicao("2");
		diferencaVO.setPrecoVenda("12,98");
		diferencaVO.setQuantidade(BigInteger.TEN);
		
		prods.add(diferencaVO);
		
		diferencaVO = new DiferencaVO();
		diferencaVO.setCodigoProduto("123");
		diferencaVO.setDescricaoProduto("nome produto");
		diferencaVO.setNumeroEdicao("2");
		diferencaVO.setPrecoVenda("12.98");
		diferencaVO.setQuantidade(BigInteger.TEN);
		
		prods.add(diferencaVO);
		
		result.use(FlexiGridJson.class).from(prods).total(prods.size()).page(1).serialize();
	}
}
