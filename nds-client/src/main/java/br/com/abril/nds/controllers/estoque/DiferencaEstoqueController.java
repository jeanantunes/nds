package br.com.abril.nds.controllers.estoque;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.DiferencaVO;
import br.com.abril.nds.client.vo.RateioCotaVO;
import br.com.abril.nds.client.vo.ResultadoDiferencaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.EstoqueDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.RateioDiferencaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO.OrdenacaoColunaConsulta;
import br.com.abril.nds.dto.filtro.FiltroDetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO.OrdenacaoColunaLancamento;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ItemNotaEnvioService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ConfirmacaoVO;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações referentes às
 * telas de consulta de diferenças e lançamento de diferenças.
 * 
 * @author Discover Technology
 */
@SuppressWarnings("deprecation")
@Resource
@Path("/estoque/diferenca")
public class DiferencaEstoqueController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private MovimentoEstoqueCotaService movimentoEstoqueCotaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private EstoqueProdutoService estoqueProdutoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ItemNotaEnvioService itemNotaEnvioService;
	
	private static final String FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE = "filtroPesquisaLancamento";
	
	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaConsultaFaltasSobras";
	
	private static final String LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE = "listaNovasDiferencas";
	
	private static final String LISTA_DIFERENCAS_SESSION_ATTRIBUTE = "listaDiferencas";
	
	private static final String LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE = "listaNovasDiferencasVO";
	
	private static final String LISTA_DIFERENCAS_PESQUISADAS_SESSION_ATTRIBUTE = "listaDiferencasPesquisadas";
	
	private static final String MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE = "mapaRateiosCadastrados";
	
	private static final String MODO_NOVA_DIFERENCA_SESSION_ATTRIBUTE = "modoNovaDiferenca";
	
	private static final String FILTRO_DETALHE_DIFERENCA_COTA = "filtroDetalheDiferencaCota";

	public DiferencaEstoqueController() {}

	@Get
	@Rules(Permissao.ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS)
	public void lancamento() {
		
		this.carregarCombosLancamento();
		
		this.limparSessao();

		result.include(
			"dataAtual", 
				DateUtil.formatarDataPTBR(
					this.distribuidorService.obterDataOperacaoDistribuidor()));
		
		result.include("permissaoBotaoConfirmacao", usuarioPossuiRule(Permissao.ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_BOTAO_CONFIRMACAO));
	}
	
	@Get
	@Rules(Permissao.ROLE_ESTOQUE_CONSULTA_FALTAS_SOBRAS)
	public void consulta() {
		this.carregarCombosConsulta();
		
		result.include("dataAtual", new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).format(new Date()));
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
	
		for (Diferenca diferenca : listaDiferencas) {

			Date dataLancamento = null;

			String descricaoStatusAprovacao = null;
			
			String motivo = null;
			
			if ((diferenca.getLancamentoDiferenca() != null) &&
					(diferenca.getLancamentoDiferenca().getMovimentoEstoque() != null)) {
				
				dataLancamento = diferenca.getLancamentoDiferenca().getMovimentoEstoque().getData();
				
				motivo = diferenca.getLancamentoDiferenca().getMovimentoEstoque().getMotivo();
			} else {
				
				if(diferenca.getLancamentoDiferenca()!= null 
						&& diferenca.getLancamentoDiferenca().getMovimentosEstoqueCota()!= null 
						&& !diferenca.getLancamentoDiferenca().getMovimentosEstoqueCota().isEmpty()){
					
					dataLancamento = diferenca.getLancamentoDiferenca().getMovimentosEstoqueCota().get(0).getData();
					
					motivo = diferenca.getLancamentoDiferenca().getMovimentosEstoqueCota().get(0).getMotivo();
				}
			}
			
			descricaoStatusAprovacao = 
					diferenca.getLancamentoDiferenca() != null 
						&& diferenca.getLancamentoDiferenca().getStatus()!= null ?
								diferenca.getLancamentoDiferenca().getStatus().getDescricaoAbreviada() : "";
			
			DiferencaVO consultaDiferencaVO = new DiferencaVO();
			
			consultaDiferencaVO.setId(diferenca.getId());
			
			consultaDiferencaVO.setDataLancamento(DateUtil.formatarData(dataLancamento, Constantes.DATE_PATTERN_PT_BR));
			
			consultaDiferencaVO.setCodigoProduto(diferenca.getProdutoEdicao().getProduto().getCodigo());
			
			consultaDiferencaVO.setDescricaoProduto(diferenca.getProdutoEdicao().getProduto().getNome());
			
			consultaDiferencaVO.setDescricaoProduto(diferenca.getProdutoEdicao().getProduto().getNome());
			consultaDiferencaVO.setNumeroEdicao(diferenca.getProdutoEdicao().getNumeroEdicao().toString());
			
			consultaDiferencaVO.setPrecoVenda(CurrencyUtil.formatarValor(diferenca.getProdutoEdicao().getPrecoVenda()));
			
			consultaDiferencaVO.setTipoDiferenca(diferenca.getTipoDiferenca());
			consultaDiferencaVO.setDescricaoTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());
			
			if (diferenca.getItemRecebimentoFisico() != null
						&& diferenca.getItemRecebimentoFisico().getItemNotaFiscal() != null
						&& diferenca.getItemRecebimentoFisico().getItemNotaFiscal().getNotaFiscal() != null
						&& diferenca.getItemRecebimentoFisico().getItemNotaFiscal().getNotaFiscal().getNumero() != null) {
				consultaDiferencaVO.setNumeroNotaFiscal(diferenca.getItemRecebimentoFisico().getItemNotaFiscal().getNotaFiscal().getNumero().toString());
			} else {
				consultaDiferencaVO.setNumeroNotaFiscal(" - ");
			}
			
			if(diferenca.getLancamentoDiferenca() != null
					&& diferenca.getLancamentoDiferenca().getMovimentoEstoque() != null
					&& diferenca.getLancamentoDiferenca().getMovimentoEstoque().getStatusIntegracao() != null) {
				
				consultaDiferencaVO.setStatusIntegracao(diferenca.getLancamentoDiferenca().getMovimentoEstoque().getStatusIntegracao().getDescricao());
				
			}
			
			consultaDiferencaVO.setQuantidade(diferenca.getQtde());
			
			consultaDiferencaVO.setStatusAprovacao(descricaoStatusAprovacao);
			
			consultaDiferencaVO.setMotivoAprovacao(motivo);
			
			consultaDiferencaVO.setValorTotalDiferenca(CurrencyUtil.formatarValor(diferenca.getValorTotalDiferenca()));
			
			listaConsultaDiferenca.add(consultaDiferencaVO);
			
			qtdeTotalDiferencas = qtdeTotalDiferencas.add(diferenca.getQtde());
			
			valorTotalDiferencas = valorTotalDiferencas.add(diferenca.getValorTotalDiferenca());
		}
		
		String valorTotalDiferencasFormatado = CurrencyUtil.formatarValorComSimbolo(valorTotalDiferencas);
		
		ResultadoDiferencaVO resultadoDiferencaVO = new ResultadoDiferencaVO(null, qtdeTotalDiferencas, valorTotalDiferencasFormatado);
		
		FileExporter.to("consulta-faltas-sobras", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtroSessao, resultadoDiferencaVO, 
				listaConsultaDiferenca, DiferencaVO.class, this.httpServletResponse);
		
		this.result.nothing();
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
			
			this.httpSession.setAttribute(LISTA_DIFERENCAS_SESSION_ATTRIBUTE,listaLancamentoDiferencas);
		}
	}
	
	
	
	@Post
	@Path("/lancamento/pesquisa/novos")
	@SuppressWarnings("unchecked")
	public void pesquisarLancamentosNovos(Date dataMovimento, TipoDiferenca tipoDiferenca,
										  String sortorder, String sortname, Integer page, Integer rp) {
				
		Boolean modoEdicaoNovaDiferenca = (Boolean) this.httpSession.getAttribute(MODO_NOVA_DIFERENCA_SESSION_ATTRIBUTE);
		
		if(modoEdicaoNovaDiferenca!= null && modoEdicaoNovaDiferenca){
			
			Set<Diferenca> listaNovasDiferencas = (Set<Diferenca>)
					this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
			
			if (listaNovasDiferencas != null 
					&& !listaNovasDiferencas.isEmpty()) {

				FiltroLancamentoDiferencaEstoqueDTO filtro = 
					this.carregarFiltroPesquisaLancamentos(
						dataMovimento, null, sortorder, sortname, page, rp);
				
				processarDiferencasLancamentoNovos(listaNovasDiferencas, filtro);
				
			}else{
				
				renderizarGridVazio();
			}
			
		}else{
			
			List<Diferenca> listaDiferencas = (List<Diferenca>)
					this.httpSession.getAttribute(LISTA_DIFERENCAS_SESSION_ATTRIBUTE);
			
			if (listaDiferencas != null 
					&& !listaDiferencas.isEmpty()) {

				FiltroLancamentoDiferencaEstoqueDTO filtro = 
					this.carregarFiltroPesquisaLancamentos(
						dataMovimento, tipoDiferenca, sortorder, sortname, page, rp);
				
				Long qtdeTotalRegistros = this.diferencaEstoqueService.obterTotalDiferencasLancamento(filtro);
				
				processarDiferencasLancamento(listaDiferencas, filtro, qtdeTotalRegistros.intValue());
			}
			else{
				
				renderizarGridVazio();
			}
		}
		
		this.httpSession.setAttribute("diferencasSelecionadas", new ArrayList<Long>());
	}

	private void renderizarGridVazio() {
		
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
	
	@Post
	@Path("/lancamento/cadastrarNovasDiferencasNotaEnvio")
	@Rules(Permissao.ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_ALTERACAO)
	public void cadastrarNovasDiferencasNotaEnvio(TipoDiferenca tipoDiferenca, 
												  Date dataNotaEnvio,
										 		  Integer numeroCota,
										 		  String nomeCota,
										 		  List<DiferencaVO>diferencasProdutos,
										 		  Long idDiferenca) {
		
		if (tipoDiferenca == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O preenchimento do campo [Tipo de Diferença] é obrigatório!");
		}
		
		if(idDiferenca == null){
			
			incluirLancamentoDiferencaNotaEnvio(tipoDiferenca,dataNotaEnvio,numeroCota,nomeCota,diferencasProdutos);
		}
		else{
			
			editarDiferencaNotaEnvio(idDiferenca,diferencasProdutos);
		}
		
		result.use(Results.json()).from("").serialize();
	}
	
	private void editarDiferencaNotaEnvio(Long idDiferenca, List<DiferencaVO>diferencasProdutos){
		
		BigInteger quantidadeDiferenca = (diferencasProdutos!= null && !diferencasProdutos.isEmpty())
											?diferencasProdutos.get(0).getQuantidade()
													:BigInteger.ZERO;
			
		DiferencaVO diferencaEditavel = this.obterDiferencaPorId(idDiferenca);
		
		if(diferencaEditavel!= null){ 
			
			diferencaEditavel.setQuantidade(quantidadeDiferenca);
			
			ProdutoEdicao produtoEdicao =
					this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
						diferencaEditavel.getCodigoProduto(), diferencaEditavel.getNumeroEdicao());
			
			BigDecimal valorTotalDiferenca = calcularValorTotalDiferenca(quantidadeDiferenca, produtoEdicao);
			
			diferencaEditavel.setVlTotalDiferenca(valorTotalDiferenca);
			
			diferencaEditavel.setQuantidade(quantidadeDiferenca);
		    
			if (diferencaEditavel.isCadastrado()){
				
				this.httpSession.setAttribute(MODO_NOVA_DIFERENCA_SESSION_ATTRIBUTE,true);
				
				atualizarDiferencaNova(idDiferenca, quantidadeDiferenca,diferencaEditavel, valorTotalDiferenca);
				
			}else{
				
				this.httpSession.setAttribute(MODO_NOVA_DIFERENCA_SESSION_ATTRIBUTE,false);
				
				atualizarDiferenca(idDiferenca, quantidadeDiferenca,diferencaEditavel, valorTotalDiferenca);
			}
		}
		
	}
	
	private void incluirLancamentoDiferencaNotaEnvio(TipoDiferenca tipoDiferenca, Date dataNotaEnvio,
													  Integer numeroCota,String nomeCota, List<DiferencaVO> diferencasProdutos) {
		
		this.validarLancamentoPorCota(numeroCota, dataNotaEnvio, diferencasProdutos);
		
		for(DiferencaVO diferenca : diferencasProdutos){
			
			incluirDiferencaNotaEnvio(diferenca, tipoDiferenca);
			
			RateioCotaVO rateio = new RateioCotaVO();
			
			rateio.setNumeroCota(numeroCota);
			rateio.setNomeCota(nomeCota);
			rateio.setIdDiferenca(diferenca.getId());
			rateio.setDataEnvioNota(dataNotaEnvio);
			rateio.setQuantidade(diferenca.getQuantidade());
			
			List<RateioCotaVO> rateiosNovos = new ArrayList<RateioCotaVO>();
			
			rateiosNovos.add(rateio);
			
			incluirRateioNotaEnvio(diferenca, rateiosNovos);
		}
		
		this.httpSession.setAttribute(MODO_NOVA_DIFERENCA_SESSION_ATTRIBUTE,true);
	}

	@SuppressWarnings("unchecked")
	private Long incluirDiferencaNotaEnvio(DiferencaVO diferencaVO, TipoDiferenca tipoDiferenca){
		
		diferencaVO.setCadastrado(true);
		diferencaVO.setTipoEstoque(TipoEstoque.LANCAMENTO);
		diferencaVO.setTipoDirecionamento(TipoDirecionamentoDiferenca.NOTA);
		diferencaVO.setTipoDiferenca(tipoDiferenca);
		
		Set<DiferencaVO> listaNovasDiferencasVO = 
				(HashSet<DiferencaVO>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
			
		if (listaNovasDiferencasVO == null) {
			
			listaNovasDiferencasVO = new HashSet<DiferencaVO>();
		}
		
		Long id  = this.gerarIdentificadorDiferenca(new ArrayList<DiferencaVO>(listaNovasDiferencasVO));
		
		diferencaVO.setId(id);
		
		listaNovasDiferencasVO.add(diferencaVO);
		
		Set<Diferenca> listaDiferencas = (Set<Diferenca>)
				this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
			
		if (listaDiferencas == null) {
			
			listaDiferencas = new HashSet<Diferenca>();
		}
		
		id = this.gerarIdentificadorDiferenca(new ArrayList<Diferenca>(listaDiferencas));
		
		Date dataMovimentacao = this.dataMovimentacaoDiferenca();
		
		Diferenca diferenca = this.obterDiferenca(diferencaVO, id, dataMovimentacao); 
		
		listaDiferencas.add(diferenca);
		
		this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE, listaNovasDiferencasVO);
		
		this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE, listaDiferencas);
		
		return diferenca.getId();
	}
	
	@SuppressWarnings("unchecked")
	private void incluirRateioNotaEnvio(DiferencaVO diferenca, List<RateioCotaVO> listaNovosRateios) {
		
		Map<Long, List<RateioCotaVO>> mapaRateiosCadastrados =
				(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
			
		List<RateioCotaVO> listaRateiosCadastrados = null;
		
		if (mapaRateiosCadastrados == null) {

			mapaRateiosCadastrados = new HashMap<Long, List<RateioCotaVO>>();

		} else {
			
			listaRateiosCadastrados = mapaRateiosCadastrados.get(diferenca.getId());
		}
		
		if (listaRateiosCadastrados == null || listaRateiosCadastrados.isEmpty()) {
			
			listaRateiosCadastrados = new ArrayList<RateioCotaVO>();
		}
		
		for (RateioCotaVO rateioCotaVO : listaNovosRateios) {
			
			rateioCotaVO.setIdDiferenca(diferenca.getId());

			if (!listaRateiosCadastrados.contains(rateioCotaVO)) {
				
				listaRateiosCadastrados.add(rateioCotaVO);
			}
		}
		
		mapaRateiosCadastrados.put(diferenca.getId(), listaRateiosCadastrados);
		
		this.httpSession.setAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE, mapaRateiosCadastrados);
	}

	@Post
	@Path("/lancamento/cadastrarNovasDiferencas")
	@Rules(Permissao.ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_ALTERACAO)
	public void cadastrarNovasDiferencas(TipoDiferenca tipoDiferenca, 
										 boolean direcionadoParaEstoque,
										 String codigoProduto,
										 Integer edicaoProduto, 
										 BigInteger diferenca,
										 BigInteger reparteAtual,
										 BigInteger qntReparteRateio,
										 List<RateioCotaVO> rateioCotas,
										 Long idDiferenca,
										 TipoEstoque tipoEstoque,
										 String pacotePadrao) {
		
		if (tipoDiferenca == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O preenchimento do campo [Tipo de Diferença] não foi informado!");
		}
		
		if(edicaoProduto==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Campo [Edição] não foi informado!");
		}
		
		if(diferenca==null) { 
			throw new ValidacaoException(TipoMensagem.WARNING, "Campo [Diferença] é obrigatório!");
		}
		
		this.validarProdutoEmRecolhimento(direcionadoParaEstoque, codigoProduto,edicaoProduto,null);
		
		if(idDiferenca == null) {
			
			incluirLancamentoDiferencaEstoqueRateio(tipoDiferenca, direcionadoParaEstoque,
													codigoProduto, edicaoProduto, 
													diferenca, reparteAtual,
													qntReparteRateio, rateioCotas,
													tipoEstoque, pacotePadrao);
		} else {
			
			TipoDirecionamentoDiferenca tipoDirecionamento = null;
			
			if (!direcionadoParaEstoque ) {
				
				tipoDirecionamento  = TipoDirecionamentoDiferenca.COTA;
			} else {
				
				tipoDirecionamento  = TipoDirecionamentoDiferenca.ESTOQUE;
			}
			
			editarDiferenca(idDiferenca, diferenca,qntReparteRateio, rateioCotas, tipoDirecionamento,reparteAtual, tipoEstoque);
		}
		
		result.use(Results.json()).from("").serialize();
	}
	
	/*
	 * Verifica se o produto informado esta em processo de recolhimento caso a diferença seja redirecionada para cota.
	 */
	private void validarProdutoEmRecolhimento(boolean direcionadoParaEstoque,String codigoProduto, Integer edicaoProduto, ProdutoEdicao produtoEdicao) {
		
		if (!direcionadoParaEstoque ) {
			
			if (produtoEdicao == null){
				
				produtoEdicao = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, edicaoProduto.toString());
			}
			
			if (!diferencaEstoqueService.validarProdutoEmRecolhimento(produtoEdicao)){
				
				StringBuilder mensagem = new StringBuilder();
				
				mensagem.append(" Produto [").append(produtoEdicao.getProduto().getCodigo()).append(" - " )
						.append(produtoEdicao.getProduto().getNomeComercial()).append( " - " )
						.append(produtoEdicao.getNumeroEdicao()) 
						.append("] encontra-se em recolhimento.");
				
				throw new ValidacaoException(TipoMensagem.WARNING,mensagem.toString());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void incluirLancamentoDiferencaEstoqueRateio(TipoDiferenca tipoDiferenca,
													boolean direcionadoParaEstoque,
													String codigoProduto,
													Integer edicaoProduto, 
													BigInteger diferenca,
													BigInteger reparteAtual, 
													BigInteger qntReparteRateio,
													List<RateioCotaVO> rateioCotas,
													TipoEstoque tipoEstoque,
													String pacotePadrao) {
		if(!direcionadoParaEstoque) {
			
			validarTipoDiferenca(tipoDiferenca, diferenca, qntReparteRateio, TipoDirecionamentoDiferenca.COTA);
				
			DiferencaVO diferencaVO = obterDiferencaVO(tipoDiferenca, codigoProduto, edicaoProduto, diferenca, reparteAtual, tipoEstoque, pacotePadrao);
			diferencaVO.setTipoDirecionamento(TipoDirecionamentoDiferenca.COTA);
			
			Long idDiferenca = incluirDiferencaEstoque(diferencaVO, tipoDiferenca);
			
			Set<DiferencaVO> listaNovasDiferencasVO = (HashSet<DiferencaVO>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);

			diferencaVO = this.diferencaEstoqueService.verificarDiferencaComListaSessao(listaNovasDiferencasVO, diferencaVO, idDiferenca);
			try {
			
				cadastrarRateioCotas(rateioCotas, diferencaVO);
				
			} catch(ValidacaoException e) {
				
				excluirDiferenca(diferencaVO.getId());
				
				throw e;
			}	
		} else {
			
			DiferencaVO diferencaVO = obterDiferencaVO(tipoDiferenca, codigoProduto, edicaoProduto, diferenca, reparteAtual, tipoEstoque, pacotePadrao);
			diferencaVO.setTipoDirecionamento(TipoDirecionamentoDiferenca.ESTOQUE);
				
			incluirDiferencaEstoque(diferencaVO, tipoDiferenca);		
			
		}
		
		this.httpSession.setAttribute(MODO_NOVA_DIFERENCA_SESSION_ATTRIBUTE,true);
	}

	private void validarTipoDiferenca(TipoDiferenca tipoDiferenca,
									  BigInteger diferenca, 
									  BigInteger qntReparteRateio,
									  TipoDirecionamentoDiferenca direcionamento) {
		
		if (TipoDiferenca.FALTA_EM.equals(tipoDiferenca)
				&& TipoDirecionamentoDiferenca.COTA.equals(direcionamento)){
			
			if(qntReparteRateio.compareTo(diferenca) < 0){
				throw new ValidacaoException(TipoMensagem.WARNING,"A quantidade total de diferença de produto deve ser direcionada a(s) cota(s)!");
			}
		}
	}
	
	private void editarDiferenca(Long idDiferenca,
								BigInteger qntDiferenca ,
								BigInteger qntReparteRateio,
								List<RateioCotaVO> rateiosCota,
								TipoDirecionamentoDiferenca direcionamento,
								BigInteger reparteAtual, TipoEstoque tipoEstoque){
			
		DiferencaVO diferencaEditavel = this.obterDiferencaPorId(idDiferenca);
		
		if(diferencaEditavel!= null){
					
			TipoDiferenca tipoDiferencas = diferencaEditavel.getTipoDiferenca(); 
			 		
			validarTipoDiferenca(tipoDiferencas, qntDiferenca, qntReparteRateio, direcionamento);
			
			String mensagemErro = null;
			
			if (!TipoDirecionamentoDiferenca.COTA.equals(diferencaEditavel.getTipoDirecionamento())) {
			
				mensagemErro =  
					validarEstoqueDiferenca(
						diferencaEditavel.getQtdeEstoqueAtual(), qntDiferenca, 
							tipoDiferencas, false);
			}
			
			if (mensagemErro != null) {
				
				throw new ValidacaoException(TipoMensagem.WARNING, mensagemErro);
			}
			
			diferencaEditavel.setQuantidade(qntDiferenca);
			
			if(TipoDirecionamentoDiferenca.COTA.equals(direcionamento)){
				
				removerRateiosCota(idDiferenca);
				
				cadastrarRateioCotas(rateiosCota, diferencaEditavel);
				
				diferencaEditavel.setTipoDirecionamento(TipoDirecionamentoDiferenca.COTA);
				
			}
			else if (TipoDirecionamentoDiferenca.ESTOQUE.equals(direcionamento)){
				
				removerRateiosCota(idDiferenca);
				
				diferencaEditavel.setTipoDirecionamento(TipoDirecionamentoDiferenca.ESTOQUE);
			}
			
			ProdutoEdicao produtoEdicao =
					this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
						diferencaEditavel.getCodigoProduto(), diferencaEditavel.getNumeroEdicao());
			
			BigDecimal valorTotalDiferenca = calcularValorTotalDiferenca(qntDiferenca, produtoEdicao);
			
			diferencaEditavel.setVlTotalDiferenca(valorTotalDiferenca);
			
			diferencaEditavel.setQuantidade(qntDiferenca);
			
			diferencaEditavel.setTipoEstoque(tipoEstoque);
		    
			if (diferencaEditavel.isCadastrado()){
				
				this.httpSession.setAttribute(MODO_NOVA_DIFERENCA_SESSION_ATTRIBUTE,true);
				
				atualizarDiferencaNova(idDiferenca, qntDiferenca,diferencaEditavel, valorTotalDiferenca);
				
			}else{
				
				this.httpSession.setAttribute(MODO_NOVA_DIFERENCA_SESSION_ATTRIBUTE,false);
				
				atualizarDiferenca(idDiferenca, qntDiferenca,diferencaEditavel, valorTotalDiferenca);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void atualizarDiferenca(Long idDiferenca, BigInteger qntDiferenca,
									DiferencaVO diferencaEditavel, BigDecimal valorTotalDiferenca) {
		
		List<Diferenca> listaDiferencas = (List<Diferenca>)
				this.httpSession.getAttribute(LISTA_DIFERENCAS_SESSION_ATTRIBUTE);

		if(listaDiferencas!= null){
		
			for(Diferenca df : listaDiferencas){
				
				if(df.getId().equals(idDiferenca)){
					df.setQtde(qntDiferenca);
					df.setTipoEstoque(diferencaEditavel.getTipoEstoque());
					df.setTipoDirecionamento(diferencaEditavel.getTipoDirecionamento());
					df.setValorTotalDiferenca(valorTotalDiferenca);
					break;
				}
			}
			
			this.httpSession.setAttribute(LISTA_DIFERENCAS_SESSION_ATTRIBUTE, listaDiferencas);
		}
	}

	@SuppressWarnings("unchecked")
	private void atualizarDiferencaNova(Long idDiferenca,
										BigInteger qntDiferenca, DiferencaVO diferencaEditavel,
										BigDecimal valorTotalDiferenca) {
		
		Set<Diferenca> listaDiferencas = (Set<Diferenca>)
				this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);

		if(listaDiferencas!= null){
		
			for(Diferenca diferenca : listaDiferencas){
				
				if(diferenca.getId().equals(idDiferenca)){
					diferenca.setQtde(qntDiferenca);
					diferenca.setTipoDirecionamento(diferencaEditavel.getTipoDirecionamento());
					diferenca.setValorTotalDiferenca(valorTotalDiferenca);
					diferenca.setTipoEstoque(diferencaEditavel.getTipoEstoque());
					break;
				}
			}
			
			this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE, listaDiferencas);
		}
		
		Set<DiferencaVO> listaNovasDiferencasVO = 
				(HashSet<DiferencaVO>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
		
		if(listaNovasDiferencasVO!= null){
		
			for(DiferencaVO diferenca : listaNovasDiferencasVO){
				
				if(diferenca.getId().equals(idDiferenca)){
					diferenca.setQuantidade(qntDiferenca);
					diferenca.setTipoEstoque(diferencaEditavel.getTipoEstoque());
					diferenca.setTipoDirecionamento(diferencaEditavel.getTipoDirecionamento());
					diferenca.setValorTotalDiferenca(CurrencyUtil.formatarValor(valorTotalDiferenca));
					break;
				}
			}
			
			this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE, listaNovasDiferencasVO);
		}
	}

	@SuppressWarnings("unchecked")
	private void removerRateiosCota(Long idDiferenca) {
		
		Map<Long, List<RateioCotaVO>> mapaRateiosCadastrados =
				(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		if(mapaRateiosCadastrados!= null 
				&& mapaRateiosCadastrados.containsKey(idDiferenca)){
			
			mapaRateiosCadastrados.remove(idDiferenca);
		}
	}
	
	private DiferencaVO obterDiferencaVO(TipoDiferenca tipoDiferenca, 
										 String codigoProduto, 
										 Integer edicaoProduto, 
										 BigInteger diferenca, 
										 BigInteger reparteAtual, 
										 TipoEstoque tipoEstoque, 
										 String pacotePadrao) {
		
		DiferencaVO diferencaVO =  new DiferencaVO();
		
		diferencaVO.setCodigoProduto(codigoProduto);
		diferencaVO.setNumeroEdicao(Util.nvl(edicaoProduto,"").toString());
		diferencaVO.setQuantidade(diferenca);
		diferencaVO.setQtdeEstoqueAtual(reparteAtual);

		diferencaVO.setTipoDiferenca(
			this.obterTipoDiferenca(
				tipoDiferenca, tipoEstoque));

		diferencaVO.setTipoEstoque(tipoEstoque);
		diferencaVO.setCadastrado(true);
		diferencaVO.setPacotePadrao(pacotePadrao);
		
		diferencaVO.setDataLancamento(
			DateUtil.formatarDataPTBR(
				this.distribuidorService.obterDataOperacaoDistribuidor()));
		
		return diferencaVO;
	}

	private TipoDiferenca obterTipoDiferenca(TipoDiferenca tipoDiferenca, 
											 TipoEstoque tipoEstoque) {
		
		if (!tipoDiferenca.isAlteracaoReparte()) {
			
			return tipoDiferenca;
		}
		
		switch (tipoEstoque) {

			case LANCAMENTO:
				
				return TipoDiferenca.ALTERACAO_REPARTE_PARA_LANCAMENTO;
	
			case RECOLHIMENTO:
				
				return TipoDiferenca.ALTERACAO_REPARTE_PARA_RECOLHIMENTO;
				
			case SUPLEMENTAR:
				
				return TipoDiferenca.ALTERACAO_REPARTE_PARA_SUPLEMENTAR;
				
			case PRODUTOS_DANIFICADOS:
				
				return TipoDiferenca.ALTERACAO_REPARTE_PARA_PRODUTOS_DANIFICADOS;
				
			default:
				break;
		}
		
		throw new ValidacaoException(
			TipoMensagem.ERROR, "Tipo de estoque inválido para Alteração de Reparte");
	}

	@SuppressWarnings("unchecked")
	private Long incluirDiferencaEstoque(DiferencaVO diferencaVO, TipoDiferenca tipoDiferenca) {
		
		Set<DiferencaVO> diferencasNovas = new HashSet<DiferencaVO>();
		
		diferencasNovas.add(diferencaVO);
		
		this.validarProdutoDuplicadoLancamento(diferencasNovas);
		
		Set<DiferencaVO> listaNovasDiferencasVO = (HashSet<DiferencaVO>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
		
		
		/*
		 * Lista VO
		 */
		
		if (listaNovasDiferencasVO == null) {
			
			listaNovasDiferencasVO = new HashSet<DiferencaVO>();
		}
		
		Long id  = this.gerarIdentificadorDiferenca(new ArrayList<DiferencaVO>(listaNovasDiferencasVO));
		
		diferencaVO.setId(id);
		
		
		///
		listaNovasDiferencasVO = this.diferencaEstoqueService.verificarDiferencasVOIguais(listaNovasDiferencasVO, diferencaVO);
//		listaNovasDiferencasVO.add(diferencaVO);
		
		/*
		 * lista diferenca
		 */
		
		Set<Diferenca> listaDiferencas = (Set<Diferenca>)
				this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
			
		if (listaDiferencas == null) {
			
			listaDiferencas = new HashSet<Diferenca>();
		}
		
		//
		id = this.gerarIdentificadorDiferenca(new ArrayList<Diferenca>(listaDiferencas));
		//
		
		Date dataMovimentacao = this.dataMovimentacaoDiferenca();
		
		this.validarNovaDiferenca(diferencaVO, dataMovimentacao, tipoDiferenca);
		
		Diferenca diferenca = this.obterDiferenca(diferencaVO, id, dataMovimentacao); 
		
		listaDiferencas.removeAll(Collections.singleton(null));
		HashMap<Long, Set<Diferenca>> mapaLongListaDiferencas = this.diferencaEstoqueService.verificarDiferencasIguais(listaDiferencas, diferenca);
		Set<Entry<Long, Set<Diferenca>>> entrySet = mapaLongListaDiferencas.entrySet();
		Entry<Long, Set<Diferenca>> iterator = entrySet.iterator().next();
		
		listaDiferencas = iterator.getValue();
		Long idListaUpdate = iterator.getKey();
		
		
		this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE, listaNovasDiferencasVO);
		
		this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE, listaDiferencas);
		
		if(idListaUpdate != null) {
			return idListaUpdate;
		} else {
			return diferenca.getId();
		}
	}
	
	private <E> Long gerarIdentificadorDiferenca(List<E> listaParaOperacao){
		
		Long identificador = 0L;
		
		if(listaParaOperacao == null || listaParaOperacao.isEmpty()){
			return identificador;
		}
		
		Collections.sort(listaParaOperacao, new Comparator<E>() {
			public int compare(E o1, E o2) {
				
				if(o1 instanceof DiferencaVO && o2 instanceof DiferencaVO){
					return ((DiferencaVO)o1).getId().compareTo(((DiferencaVO)o2).getId());
				}
				else if ( o1 instanceof Diferenca && o2 instanceof Diferenca ){
					
					return ((Diferenca)o1).getId().compareTo(((Diferenca)o2).getId());
				}
				else{
					return 0;
				}
			};
		});
		
		E diferenca = listaParaOperacao.get(listaParaOperacao.size()-1);
		
		if(diferenca instanceof DiferencaVO){
			
			DiferencaVO diferebcaVO = ((DiferencaVO) diferenca); 
			
			identificador = (Long) Util.nvl(diferebcaVO.getId(), 0) + 1;
		}
		else if ( diferenca instanceof Diferenca ){
			
			Diferenca difer = ((Diferenca)diferenca);
			
			Long valor = difer.getId();
			
			identificador = (valor == null)?0L:valor + 1;
			
		}
		
		return identificador;
	}
	
	private Diferenca obterDiferenca(DiferencaVO diferencaVO,
									Long idDiferenca,Date dataMovimentacao){
		
		Diferenca diferenca = new Diferenca();
		
		diferenca.setId(idDiferenca);
		
		ProdutoEdicao produtoEdicao =
			this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
				diferencaVO.getCodigoProduto(), diferencaVO.getNumeroEdicao());
		
		diferenca.setProdutoEdicao(produtoEdicao);
		diferenca.setQtde(diferencaVO.getQuantidade());
		diferenca.setStatusConfirmacao(StatusConfirmacao.PENDENTE);
		diferenca.setTipoDiferenca(diferencaVO.getTipoDiferenca());
		diferenca.setAutomatica(false);
        diferenca.setTipoDirecionamento(diferencaVO.getTipoDirecionamento());
        diferenca.setTipoEstoque(diferencaVO.getTipoEstoque());
		diferenca.setDataMovimento(dataMovimentacao);
		
		BigDecimal valorTotalDiferenca = calcularValorTotalDiferenca(diferenca.getQtde(), produtoEdicao);
		
		diferenca.setValorTotalDiferenca(valorTotalDiferenca);
		
		return diferenca;
	}

	private BigDecimal calcularValorTotalDiferenca(BigInteger diferenca, ProdutoEdicao produtoEdicao) {

		return produtoEdicao.getPrecoVenda().multiply(new BigDecimal(diferenca));
	}

	private void validarLancamentoPorCota(Integer numeroCota,Date dataNotaEnvio, List<DiferencaVO> diferencasProdutos) {
		
		if (dataNotaEnvio == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Nota de Envio] é obrigatório!");
		}
		
		if(numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Cota] é obrigatório!");
		}
		
		validarDiferencaProduto(diferencasProdutos);
	}
	
	private Date dataMovimentacaoDiferenca(){
		
		return this.distribuidorService.obterDataOperacaoDistribuidor();
	}

	private void validarDiferencaProduto(List<DiferencaVO> diferencasProdutos) {
				
		if(diferencasProdutos == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Não foi informado nenhuma diferença para o(s) produto(s)!");
		}
			
		List<Long> linhasComErro = new ArrayList<Long>();

		for (DiferencaVO diferencaVO : diferencasProdutos) {
			
			if (diferencaVO.getQuantidade() == null
					|| BigInteger.ZERO.equals(diferencaVO.getQuantidade())) {
				
				linhasComErro.add(diferencaVO.getId());
			}
		}
		
		if (!linhasComErro.isEmpty()) {
			
			ValidacaoVO validacao = 
				new ValidacaoVO(TipoMensagem.WARNING, "Existe(m) diferença(s) preenchida(s) para o(s) produto(s) incorretamente!");
			
			validacao.setDados(linhasComErro);
			
			throw new ValidacaoException(validacao);
		}
		
	}
	
	@Post
	@Path("/pesquisarDiferencas")
	public void pesquisarDiferencas(String codigoProduto, 
									Long idFornecedor, String dataInicial,
									String dataFinal, TipoDiferenca tipoDiferenca, 
									Integer numeroCota, String nomeCota,
									String sortorder, String sortname,
									int page, int rp) {
		
		this.validarEntradaDadosPesquisa(codigoProduto, idFornecedor,
										 dataInicial, dataFinal, tipoDiferenca);
		
		FiltroConsultaDiferencaEstoqueDTO filtro =
			this.carregarFiltroPesquisa(codigoProduto, idFornecedor,
										dataInicial, dataFinal, tipoDiferenca, numeroCota, nomeCota,
										sortorder, sortname, page, rp);
		
		List<Diferenca> listaDiferencas = diferencaEstoqueService.obterDiferencas(filtro);
		
		if (listaDiferencas == null || listaDiferencas.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {
			
			this.processarDiferencas(listaDiferencas, filtro);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cadastrarRateioCotas(List<RateioCotaVO> listaNovosRateios, DiferencaVO diferencaVO) {
		
		validarNovosRateios(listaNovosRateios, diferencaVO);
		
		Map<Long, List<RateioCotaVO>> mapaRateiosCadastrados =
			(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		if (mapaRateiosCadastrados == null) {

			mapaRateiosCadastrados = new HashMap<Long, List<RateioCotaVO>>();

		}
		
		ProdutoEdicao produtoEdicao =
			this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
					diferencaVO.getCodigoProduto(), diferencaVO.getNumeroEdicao());
		
		for (RateioCotaVO rateioCotaVO : listaNovosRateios) {
			
			rateioCotaVO.setIdDiferenca(diferencaVO.getId());
			
			Date dataMovimentacao = 
				this.movimentoEstoqueCotaService.obterDataUltimaMovimentacaoReparteExpedida(
						rateioCotaVO.getNumeroCota(), produtoEdicao.getId());
			
			rateioCotaVO.setDataMovimento(
				dataMovimentacao == null ? 
					this.distribuidorService.obterDataOperacaoDistribuidor() : dataMovimentacao);
			
			this.validarNovoRateio(rateioCotaVO,diferencaVO);
			
			mapaRateiosCadastrados = 
				this.diferencaEstoqueService.incluirSeNaoExisteNoMapa(
					mapaRateiosCadastrados, diferencaVO.getId(), rateioCotaVO);
		}

		this.httpSession.setAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE, mapaRateiosCadastrados);
	}

	private void validarNovosRateios(List<RateioCotaVO> listaNovosRateios,DiferencaVO diferencaVO) {
		
		if (listaNovosRateios == null || listaNovosRateios.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha os dados para o rateio!");
		}
		
		this.validarPreenchimentoNovosRateios(listaNovosRateios);
		
		this.validarCotasDuplicadasRateio(listaNovosRateios);
		
		this.validarSomaQuantidadeRateio(listaNovosRateios, diferencaVO);
	}
	
	@Post
	@Path("/lancamento/limparSessao")
	@SuppressWarnings("unchecked")
	public void limparDadosSessao(boolean confirmado) {
	
		Set<Diferenca> listaNovasDiferencas = (Set<Diferenca>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
		
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
	@Rules(Permissao.ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_ALTERACAO)
	public void excluirFaltaSobra(Long idDiferenca){
		
		Boolean modoNovaDiferenca = (Boolean) this.httpSession.getAttribute(MODO_NOVA_DIFERENCA_SESSION_ATTRIBUTE); 
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = 
				(FiltroLancamentoDiferencaEstoqueDTO) 
					this.httpSession.getAttribute(FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE);
		
		if(modoNovaDiferenca!= null && modoNovaDiferenca){
		
			excluirDiferenca(idDiferenca);
			
			this.pesquisarLancamentosNovos(
				filtro.getDataMovimento(), filtro.getTipoDiferenca(), filtro.getPaginacao().getSortOrder(), 
					filtro.getOrdenacaoColuna().toString(), filtro.getPaginacao().getPaginaAtual(), 
						filtro.getPaginacao().getQtdResultadosPorPagina());
		}
		else{
			
			diferencaEstoqueService.excluirLancamentoDiferenca(idDiferenca);
			
			this.pesquisarLancamentos(DateUtil.formatarDataPTBR(filtro.getDataMovimento()) 
					, filtro.getTipoDiferenca(), filtro.getPaginacao().getSortOrder(), 
						filtro.getOrdenacaoColuna().toString(), filtro.getPaginacao().getPaginaAtual(), 
							filtro.getPaginacao().getQtdResultadosPorPagina());
			
		}
	
	}

	@SuppressWarnings("unchecked")
	private void excluirDiferenca(Long idDiferenca) {
		
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
		
		 Map<Long, List<RateioCotaVO>> mapaRateioCotas =
					(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		 if(mapaRateioCotas!= null && mapaRateioCotas.containsKey(idDiferenca)){
			 mapaRateioCotas.remove(idDiferenca);
		 } 
		 
		this.httpSession.setAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE, listaNovasDiferencasVO);
	}

	@Post
	@SuppressWarnings("unchecked")
	@Rules(Permissao.ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_ALTERACAO)
	public void confirmarLancamentos() {
		
		Boolean modoNovaDiferenca = (Boolean) this.httpSession.getAttribute(MODO_NOVA_DIFERENCA_SESSION_ATTRIBUTE);
		
		Set<Diferenca> listaNovasDiferencas = null;

		Map<Long, List<RateioCotaVO>> mapaRateioCotas =
				(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
			
			FiltroLancamentoDiferencaEstoqueDTO filtroPesquisa =
				(FiltroLancamentoDiferencaEstoqueDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE);
		
		if(modoNovaDiferenca != null && modoNovaDiferenca ){
			listaNovasDiferencas = (Set<Diferenca>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
		} else {
			
			listaNovasDiferencas = new HashSet<Diferenca>();

			// Para não limitar os resultados que irão ser persistidos no sistema
			filtroPesquisa.getPaginacao().setPaginaAtual(1);
			filtroPesquisa.getPaginacao().setQtdResultadosPorPagina(null);
			listaNovasDiferencas.addAll(this.diferencaEstoqueService.obterDiferencasLancamento(filtroPesquisa));

			// Esta forma limitava os registros a serem persistidos apenas para a página onde o usuário se encontrava
			/*listaNovasDiferencas.addAll(
					(List<Diferenca>) this.httpSession.getAttribute(LISTA_DIFERENCAS_SESSION_ATTRIBUTE));*/
		}
		
		this.validarDiferencasSelecionadas(listaNovasDiferencas);

		this.diferencaEstoqueService.efetuarAlteracoes(
			listaNovasDiferencas, mapaRateioCotas, filtroPesquisa, this.getUsuarioLogado().getId(), modoNovaDiferenca);
		
		result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
		
		this.limparSessao();
	}

	@SuppressWarnings("unchecked")
	private void validarDiferencasSelecionadas(Set<Diferenca> listaNovasDiferencas) {
		
		List<Long> idsDiferencasSelecionadas = null;
		
		if (this.httpSession.getAttribute("diferencasSelecionadas") != null) {
			
			idsDiferencasSelecionadas = 
				(List<Long>) this.httpSession.getAttribute("diferencasSelecionadas");
		}
		
		if (idsDiferencasSelecionadas == null || idsDiferencasSelecionadas.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma diferença selecionada.");
		}
		
		Iterator<Diferenca> iterator = listaNovasDiferencas.iterator();

		while (iterator.hasNext()) {
			
			Diferenca diferenca = iterator.next();
			
			if (!idsDiferencasSelecionadas.contains(diferenca.getId())) {
				
				iterator.remove();
			}
		}
	}

	@Post
	@SuppressWarnings("unchecked")
	@Rules(Permissao.ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_ALTERACAO)
	public void salvarLancamentos() {
		
		Boolean modoNovaDiferenca = (Boolean) this.httpSession.getAttribute(MODO_NOVA_DIFERENCA_SESSION_ATTRIBUTE);
		
		Set<Diferenca> listaNovasDiferencas = null;
		
		if(modoNovaDiferenca != null && modoNovaDiferenca ) {
			
			listaNovasDiferencas = (Set<Diferenca>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_SESSION_ATTRIBUTE);
			
		} else {
			
			listaNovasDiferencas = new HashSet<Diferenca>();
			
			listaNovasDiferencas.addAll((List<Diferenca>) this.httpSession.getAttribute(LISTA_DIFERENCAS_SESSION_ATTRIBUTE));
			
		}
		
		Map<Long, List<RateioCotaVO>> mapaRateioCotas =
			(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		if(listaNovasDiferencas != null){
						
			this.diferencaEstoqueService.salvarLancamentosDiferenca(listaNovasDiferencas, mapaRateioCotas, this.getUsuarioLogado().getId(), modoNovaDiferenca);
		}
		
		result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
		
		this.limparSessao();
	}
	
	@SuppressWarnings("unchecked")
	@Post
	@Rules(Permissao.ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_ALTERACAO)
	public void cancelarLancamentos() {
		
		Boolean modoNovaDiferenca =
			(Boolean) this.httpSession.getAttribute(MODO_NOVA_DIFERENCA_SESSION_ATTRIBUTE);
		
		if(modoNovaDiferenca == null || !modoNovaDiferenca) {
			
			FiltroLancamentoDiferencaEstoqueDTO filtroPesquisa =
				(FiltroLancamentoDiferencaEstoqueDTO) this.httpSession.getAttribute(
					FILTRO_PESQUISA_LANCAMENTO_SESSION_ATTRIBUTE);
			
			List<Long> idsDiferencasSelecionadas = null;
			
			if (this.httpSession.getAttribute("diferencasSelecionadas") != null) {
				
				idsDiferencasSelecionadas = 
					(List<Long>) this.httpSession.getAttribute("diferencasSelecionadas");
			}
			
			if (idsDiferencasSelecionadas == null) {
				
				throw new ValidacaoException(TipoMensagem.ERROR, "Nenhuma diferença selecionada.");
			}
			
			this.diferencaEstoqueService.cancelarDiferencas(
				filtroPesquisa, idsDiferencasSelecionadas, this.getUsuarioLogado().getId());
			
		} else {
			
			this.limparSessao();
		}
		
		result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
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
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.PERDA_DE, TipoDiferenca.PERDA_DE.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.PERDA_EM, TipoDiferenca.PERDA_EM.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.GANHO_DE, TipoDiferenca.GANHO_DE.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.GANHO_EM, TipoDiferenca.GANHO_EM.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(
				TipoDiferenca.ALTERACAO_REPARTE_PARA_LANCAMENTO, 
				TipoDiferenca.ALTERACAO_REPARTE_PARA_LANCAMENTO.getDescricao())
		);
		
		result.include("listaTiposDiferenca", listaTiposDiferenca);
	}
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private List<ItemDTO<Long, String>> carregarComboFornecedores(String codigoProduto) {
		
		List<Fornecedor> listaFornecedor = this.fornecedorService.obterFornecedoresAtivos();
		
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
			
			if (filtroSessao.getCodigoProduto() != null && !filtroSessao.getCodigoProduto().isEmpty() ) {
		
				Produto produto = this.produtoService.obterProdutoPorCodigo(filtroSessao.getCodigoProduto());
				
				if (produto != null) {
					
					filtroSessao.setNomeProduto(produto.getNome());
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
		
		for (Diferenca diferenca : listaDiferencas) {
			
			ProdutoEdicao produtoEdicao = diferenca.getProdutoEdicao();
			
			Produto produto = produtoEdicao.getProduto();
			
			DiferencaVO lancamentoDiferenca = new DiferencaVO();
			
			if (diferenca.getId() != null) {
				
				lancamentoDiferenca.setId(diferenca.getId());
			}
			
			lancamentoDiferenca.setCodigoProduto(produto.getCodigo());
			lancamentoDiferenca.setDescricaoProduto(produto.getNome());
			lancamentoDiferenca.setNumeroEdicao(produtoEdicao.getNumeroEdicao().toString());
			lancamentoDiferenca.setTipoEstoque(diferenca.getTipoEstoque());
			lancamentoDiferenca.setTipoDirecionamento(diferenca.getTipoDirecionamento());
			lancamentoDiferenca.setPrecoVenda(CurrencyUtil.formatarValor(produtoEdicao.getPrecoVenda()));
			lancamentoDiferenca.setPacotePadrao(String.valueOf(produtoEdicao.getPacotePadrao()));
			lancamentoDiferenca.setQuantidade(diferenca.getQtde());
			lancamentoDiferenca.setTipoDiferenca(diferenca.getTipoDiferenca());
			lancamentoDiferenca.setDescricaoTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());
			lancamentoDiferenca.setAutomatica(diferenca.isAutomatica());
			lancamentoDiferenca.setDataLancamento(DateUtil.formatarDataPTBR(diferenca.getDataMovimento()));
			
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

		List<DiferencaVO> listaLancamentosDiferenca = processarListaDiferencas(listaDiferencas); 
		
		BigInteger qtdeTotalDiferencas = BigInteger.ZERO;
		
		BigDecimal valorTotalDiferencas = BigDecimal.ZERO;
		
		for(DiferencaVO diferenca : listaLancamentosDiferenca){
			
			qtdeTotalDiferencas = qtdeTotalDiferencas.add(diferenca.getQuantidade());
				
			valorTotalDiferencas = valorTotalDiferencas.add( diferenca.getVlTotalDiferenca());
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
	
	private List<DiferencaVO> processarListaDiferencas(Set<Diferenca> listaDiferencas){
		
		List<DiferencaVO> listaLancamentosDiferenca = new LinkedList<DiferencaVO>();
		
		for (Diferenca diferenca : listaDiferencas) {
			
			ProdutoEdicao produtoEdicao = diferenca.getProdutoEdicao();
			
			Produto produto = produtoEdicao.getProduto();
			
			DiferencaVO lancamentoDiferenca = new DiferencaVO();
			
			if (diferenca.getId() != null) {
				
				lancamentoDiferenca.setId(diferenca.getId());
			}
			
			DiferencaVO diferencaItemNovo = this.obterDiferencaVOInclusaoNovoItem(diferenca.getId());
			
			if(diferencaItemNovo!= null){
				
				lancamentoDiferenca.setQtdeEstoque(diferencaItemNovo.getQtdeEstoque());
				lancamentoDiferenca.setQtdeEstoqueAtual(diferencaItemNovo.getQtdeEstoqueAtual());
			}
			
			lancamentoDiferenca.setCodigoProduto(produto.getCodigo());
			lancamentoDiferenca.setDescricaoProduto(produto.getNome());
			lancamentoDiferenca.setNumeroEdicao(produtoEdicao.getNumeroEdicao().toString());
			lancamentoDiferenca.setPrecoVenda(CurrencyUtil.formatarValor(produtoEdicao.getPrecoVenda()));
			lancamentoDiferenca.setPacotePadrao(String.valueOf(produtoEdicao.getPacotePadrao()));
			lancamentoDiferenca.setQuantidade(diferenca.getQtde());
			lancamentoDiferenca.setTipoDiferenca(diferenca.getTipoDiferenca());
			lancamentoDiferenca.setDescricaoTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());
			lancamentoDiferenca.setTipoDirecionamento(diferenca.getTipoDirecionamento());
			lancamentoDiferenca.setDataLancamento(DateUtil.formatarDataPTBR(diferenca.getDataMovimento()));
			lancamentoDiferenca.setValorTotalDiferenca(CurrencyUtil.formatarValor(diferenca.getValorTotalDiferenca()));
			lancamentoDiferenca.setVlTotalDiferenca(diferenca.getValorTotalDiferenca());
			lancamentoDiferenca.setCadastrado(true);
			lancamentoDiferenca.setTipoEstoque(diferenca.getTipoEstoque());
			
			listaLancamentosDiferenca.add(lancamentoDiferenca);
		}
		
		return listaLancamentosDiferenca;
	}
	
	@SuppressWarnings("unchecked")
	private DiferencaVO obterDiferencaVOInclusaoNovoItem(Long idDiferenca){
		
		Set<DiferencaVO> listaNovasDiferencasVO = (Set<DiferencaVO>)
				this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
		
		if(listaNovasDiferencasVO!= null && !listaNovasDiferencasVO.isEmpty()){
			
			for(DiferencaVO diferenca : listaNovasDiferencasVO){
				
				if(diferenca.getId().equals(idDiferenca)){
					return diferenca;
				}
			}
		}
		
		return null;
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
		
		for (Diferenca diferenca : listaDiferencas) {
			
			Date dataLancamento = null;
			
			if (diferenca.getLancamentoDiferenca() != null && 
					diferenca.getLancamentoDiferenca().getMovimentoEstoque() != null) {
				
				dataLancamento = diferenca.getLancamentoDiferenca().getMovimentoEstoque().getData();

			} else {
				
				if(diferenca.getLancamentoDiferenca()!= null 
						&& diferenca.getLancamentoDiferenca().getMovimentosEstoqueCota()!= null 
						&& !diferenca.getLancamentoDiferenca().getMovimentosEstoqueCota().isEmpty()){
					
					dataLancamento = diferenca.getLancamentoDiferenca().getMovimentosEstoqueCota().get(0).getData();
				}
			}
			
			DiferencaVO consultaDiferencaVO = new DiferencaVO();
			
			consultaDiferencaVO.setId(diferenca.getId());
			
			consultaDiferencaVO.setDataLancamento(DateUtil.formatarData(dataLancamento,Constantes.DATE_PATTERN_PT_BR));
			
			consultaDiferencaVO.setCodigoProduto(diferenca.getProdutoEdicao().getProduto().getCodigo());
			consultaDiferencaVO.setDescricaoProduto(diferenca.getProdutoEdicao().getProduto().getNome());
			consultaDiferencaVO.setNumeroEdicao(diferenca.getProdutoEdicao().getNumeroEdicao().toString());
			
			consultaDiferencaVO.setPrecoVenda(CurrencyUtil.formatarValor(diferenca.getProdutoEdicao().getPrecoVenda()));
			
			consultaDiferencaVO.setTipoDiferenca(diferenca.getTipoDiferenca());
			consultaDiferencaVO.setDescricaoTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());
			
			if (diferenca.getItemRecebimentoFisico() != null
					&& diferenca.getItemRecebimentoFisico().getItemNotaFiscal() != null
					&& diferenca.getItemRecebimentoFisico().getItemNotaFiscal().getNotaFiscal() != null
					&& diferenca.getItemRecebimentoFisico().getItemNotaFiscal().getNotaFiscal().getNumero() != null) {
				
				consultaDiferencaVO.setNumeroNotaFiscal(
					diferenca.getItemRecebimentoFisico().getItemNotaFiscal().getNotaFiscal().getNumero().toString());
			} else {
				consultaDiferencaVO.setNumeroNotaFiscal(" - ");
			}
			
			consultaDiferencaVO.setQuantidade(diferenca.getQtde());
			
			
			consultaDiferencaVO.setStatusAprovacao(
					(diferenca.getLancamentoDiferenca() != null 
								&& diferenca.getLancamentoDiferenca().getStatus()!= null) ?
										diferenca.getLancamentoDiferenca().getStatus().getDescricaoAbreviada() : "");
			
			if(diferenca.getLancamentoDiferenca() != null
					&& diferenca.getLancamentoDiferenca().getMovimentoEstoque() != null
					&& diferenca.getLancamentoDiferenca().getMovimentoEstoque().getStatusIntegracao() != null) {
				
				consultaDiferencaVO.setStatusIntegracao(diferenca.getLancamentoDiferenca().getMovimentoEstoque().getStatusIntegracao().getDescricao());
				
			} else if(diferenca.getLancamentoDiferenca() != null
					&& diferenca.getLancamentoDiferenca().getMovimentosEstoqueCota() != null
					&& diferenca.getLancamentoDiferenca().getMovimentosEstoqueCota().get(0) != null
					&& diferenca.getLancamentoDiferenca().getMovimentosEstoqueCota().get(0).getStatusIntegracao() != null) {
				
				consultaDiferencaVO.setStatusIntegracao(
					diferenca.getLancamentoDiferenca().getMovimentosEstoqueCota().get(0).getStatusIntegracao().getDescricao()
				);				
			}
			
			consultaDiferencaVO.setMotivoAprovacao(diferenca.getLancamentoDiferenca().getStatus().toString());
			
			consultaDiferencaVO.setValorTotalDiferenca(CurrencyUtil.formatarValor(diferenca.getValorTotalDiferenca()));
			
			consultaDiferencaVO.setTipoEstoque(diferenca.getTipoEstoque());
			
			consultaDiferencaVO.setExistemRateios(diferenca.isExistemRateios());
			
			listaConsultaDiferenca.add(consultaDiferencaVO);

			Fornecedor fornecedor = fornecedorService.obterFornecedorUnico(diferenca.getProdutoEdicao().getProduto().getCodigo());
			
			if(fornecedor != null) {
				consultaDiferencaVO.setFornecedor(fornecedor.getJuridica().getNomeFantasia());
			}

			qtdeTotalDiferencas = qtdeTotalDiferencas.add(diferenca.getQtde());
			
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
	 * @param idFornecedor - identificador do fornecedor
	 * @param dataInicial - data de movimento inicial
	 * @param dataFinal - data de movimento final
	 * @param tipoDiferenca - tipo de diferença
	 * @param numeroCota - numero da cota
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 * 
	 * @return Filtro
	 */
	private FiltroConsultaDiferencaEstoqueDTO carregarFiltroPesquisa(String codigoProduto, 
																	 Long idFornecedor, String dataInicial,
																	 String dataFinal, TipoDiferenca tipoDiferenca, 
																	 Integer numeroCota, String nomeCota,
																	 String sortorder, String sortname,
																	 int page, int rp) {
		
		FiltroConsultaDiferencaEstoqueDTO filtroAtual =  new FiltroConsultaDiferencaEstoqueDTO();
		
		filtroAtual.setCodigoProduto(codigoProduto);
		filtroAtual.setIdFornecedor(idFornecedor);
		filtroAtual.setNumeroCota(numeroCota);
		filtroAtual.setNomeCota(nomeCota);
	
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
	private void validarEntradaDadosPesquisa(String codigoProduto, 
											 Long idFornecedor, String dataInicial,
											 String dataFinal, TipoDiferenca tipoDiferenca) {
			
		if ((dataInicial != null && !dataInicial.trim().isEmpty()
				&& !DateUtil.isValidDatePTBR(dataInicial))) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Data Lançamento Inicial inválida");
		}
		
		if ((dataFinal != null && !dataFinal.trim().isEmpty()
				&& !DateUtil.isValidDatePTBR(dataFinal))) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Data Lançamento Final inválida");
		}
		
		if (dataInicial!= null && dataFinal!= null 
				&& DateUtil.isDataInicialMaiorDataFinal(DateUtil.parseDataPTBR(dataInicial),
												 DateUtil.parseDataPTBR(dataFinal))) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O campo [Data Lançamento] não deve ser maior que o campo [Até]!");
		}
		
		if ((dataInicial == null || dataInicial.trim().isEmpty())
				|| (dataFinal == null || dataFinal.trim().isEmpty())) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING,
					"Para realizar a pesquisa é necessário informar um Período de Data de Lançamento!");
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
	@Rules(Permissao.ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_ALTERACAO)
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
	private void validarSomaQuantidadeRateio(List<RateioCotaVO> listaNovosRateios, DiferencaVO diferencaVO ) {
		
		BigInteger somaQtdeRateio = BigInteger.ZERO;
		
		for (RateioCotaVO rateioCotaVO : listaNovosRateios) {
			
			somaQtdeRateio = somaQtdeRateio.add(rateioCotaVO.getQuantidade());
		}
		
		if (somaQtdeRateio.compareTo(diferencaVO.getQuantidade()) > 0) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "A somatória da quantidade do(s) campo(s) [Diferença] (" 
					+ somaQtdeRateio + ") direcionada a(s) Cota(s) é maior que a quantidade do campo [Diferença] referente ao produto informado (" 
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
	private void validarNovoRateio(RateioCotaVO novoRateioCota, DiferencaVO diferencaVO) {
		
		List<Long> linhasComErro = new ArrayList<Long>();
		
		List<String> listaMensagensErro = new ArrayList<String>();
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(novoRateioCota.getNumeroCota());
		
		if (cota == null) {
			
			linhasComErro.add(novoRateioCota.getId());
			
			listaMensagensErro.add("Cota inválida: Número [" + novoRateioCota.getNumeroCota() + "] - Nome [" + novoRateioCota.getNomeCota() + " ]");
		}
		
		TipoDiferenca tipoDiferenca = diferencaVO.getTipoDiferenca();
		
		String mensagemErro = 
			this.validarEstoqueDiferenca(
				novoRateioCota.getReparteCota(), novoRateioCota.getQuantidade(), 
					tipoDiferenca, true); 
		
		if(mensagemErro != null){
			
			listaMensagensErro.add(mensagemErro);
			
			linhasComErro.add(novoRateioCota.getId());
		}
		
		if (!linhasComErro.isEmpty() && !listaMensagensErro.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaMensagensErro);
		
			validacao.setDados(linhasComErro);
		
			throw new ValidacaoException(validacao);
		}
	}
	
	private void validarNovaDiferenca(DiferencaVO diferenca,
									  Date dataMovimento,	
			  						  TipoDiferenca tipoDiferenca){
		
		List<String> listaMensagensErro = new ArrayList<String>();
		
		ProdutoEdicao produtoEdicao =
			this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
				diferenca.getCodigoProduto(), diferenca.getNumeroEdicao());
		
		if (produtoEdicao == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Produto inválido: Código [" + diferenca.getCodigoProduto() + "] - Edição [" + diferenca.getNumeroEdicao() + " ]");
		}
		
		String mensagemErro = null;
		
		if (!TipoDirecionamentoDiferenca.COTA.equals(diferenca.getTipoDirecionamento())) {
			
			mensagemErro = 
				validarEstoqueDiferenca(
					diferenca.getQtdeEstoqueAtual(),diferenca.getQuantidade() ,
						tipoDiferenca, false); 
		}

		if (mensagemErro != null) {
			
			listaMensagensErro.add(mensagemErro);
		}
		
		if (!listaMensagensErro.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaMensagensErro);
		
			throw new ValidacaoException(validacao);
		}
	}

	private String validarEstoqueDiferenca(BigInteger qtdeEstoqueAtual, BigInteger quantidade, 
											TipoDiferenca tipoDiferenca,boolean isRateioCota) {
		
		if (quantidade.compareTo(BigInteger.ZERO) == 0) {
			
			return (!isRateioCota) ?
							"Quantidade de Diferença para o tipo de diferença '" + tipoDiferenca.getDescricao() 
								+ "' não pode ser igual a zero!"
								  
								:"A somatória do(s) campo(s) [Diferença] do Rateio da(s) Cota(s) para o tipo de diferença '" +
								  		tipoDiferenca.getDescricao() + "' não pode ser igual a zero!";
		}
		
		if (TipoDiferenca.FALTA_DE.equals(tipoDiferenca)
				|| TipoDiferenca.FALTA_EM.equals(tipoDiferenca)) {
		
			if(TipoDiferenca.FALTA_DE.equals(tipoDiferenca)){

				if(qtdeEstoqueAtual.compareTo(quantidade) < 0){
					
					return (!isRateioCota)?
								"Quantidade de Diferença para o tipo de diferença '" + tipoDiferenca.getDescricao() 
									+ "' não pode ser maior que a quantidade em estoque do produto!"
										
									:"Quantidade do campo [Diferença] do Rateio da Cota para o tipo de diferença '" +
										tipoDiferenca.getDescricao() + "' não pode ser maior que a quantidade do campo [Reparte] da Cota!";
				}
				
			}else{
				
				if (qtdeEstoqueAtual == null 
						|| (quantidade.compareTo(qtdeEstoqueAtual) > 0)) {
					
					return (!isRateioCota) ?
								"Quantidade de Diferença para o tipo de diferença '" + tipoDiferenca.getDescricao() 
									+ "' não pode ser maior que a quantidade em estoque do produto!"
										
									:"Quantidade do campo [Diferença] do Rateio da Cota para o tipo de diferença '" +
										tipoDiferenca.getDescricao() + "' não pode ser maior que a quantidade do campo [Reparte] da Cota!";
				}
			}
		}
		
		return null;
	}
		
	@SuppressWarnings("unchecked")
	private void validarProdutoDuplicadoLancamento(Set<DiferencaVO> listaNovasDiferencas){
		
		List<DiferencaVO> listaDiferencas = new ArrayList<DiferencaVO>();
		
		if (listaNovasDiferencas == null) {
			
			return;
		}
	
		Set<DiferencaVO> listaDiferencasCadastradas =
			(Set<DiferencaVO>) this.httpSession.getAttribute(
				LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
		
		if (listaDiferencasCadastradas != null) {
			listaDiferencas.addAll(listaNovasDiferencas);
			listaDiferencas.addAll(listaDiferencasCadastradas);
		}

		ComparatorChain comparatorChain = new ComparatorChain();
		
		comparatorChain.addComparator(new BeanComparator("codigoProduto"));
		comparatorChain.addComparator(new BeanComparator("numeroEdicao"));
		comparatorChain.addComparator(new BeanComparator("tipoDirecionamento"));
				
		Collections.sort(listaDiferencas, comparatorChain);
		
		List<Long> linhasComErro = new ArrayList<Long>();
		
		DiferencaVO ultimaDiferencaVO = null;
		
		for (DiferencaVO diferencaVO : listaDiferencas) {
			
			if (ultimaDiferencaVO != null) {
				
				if (diferencaVO.getCodigoProduto().trim().equalsIgnoreCase(ultimaDiferencaVO.getCodigoProduto())
						&& diferencaVO.getNumeroEdicao().trim().equalsIgnoreCase(ultimaDiferencaVO.getNumeroEdicao())
						&& diferencaVO.getTipoDirecionamento().equals(ultimaDiferencaVO.getTipoDirecionamento())
						&& diferencaVO.getTipoEstoque().equals(ultimaDiferencaVO.getTipoEstoque())) {
					
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

			throw new ValidacaoException(TipoMensagem.WARNING, "Existem produtos duplicados para o lançamento!");
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
		
		this.httpSession.removeAttribute(LISTA_DIFERENCAS_SESSION_ATTRIBUTE);
		
		this.httpSession.removeAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
		
		this.httpSession.removeAttribute(MODO_NOVA_DIFERENCA_SESSION_ATTRIBUTE);
	}
	
	
	@Post
	@Path("/lancamento/rateio/buscarDiferenca")
	public void buscarDiferenca(Long idDiferenca){
		
		DiferencaVO diferencaVO = this.obterDiferencaPorId(idDiferenca);
		
		ProdutoEdicao pe = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(diferencaVO.getCodigoProduto(), diferencaVO.getNumeroEdicao());
		
		BigInteger qtdeEstoqueAtual = 
			atualizarQuantidadeEstoqueComNovasDiferencas(
				obterReparteAtualProdutoEdicao(diferencaVO,pe.getId()), diferencaVO.getTipoEstoque());
		
		diferencaVO.setQtdeEstoqueAtual(qtdeEstoqueAtual);		
		
		if(!TipoDirecionamentoDiferenca.ESTOQUE.equals(diferencaVO.getTipoDirecionamento())){
			
			List<RateioCotaVO> rateiosDiferenca = this.obterRateiosEdicaoDiferenca(idDiferenca);
			
			if(rateiosDiferenca!= null && !rateiosDiferenca.isEmpty()){
			
				if (TipoDirecionamentoDiferenca.NOTA.equals(diferencaVO.getTipoDirecionamento())) {
					
					diferencaVO.setQtdeEstoque(this.obterQuantidadeReparteNota(pe, rateiosDiferenca));
				}
				
				Map<String, Object> mapa = new TreeMap<String, Object>();
				mapa.put("diferenca", diferencaVO);
				mapa.put("idProdutoEdicao", pe.getId());
				mapa.put("rateios",rateiosDiferenca);
				
				result.use(CustomJson.class).from(mapa).serialize();
			}
			else{

				Map<String, Object> mapa = new TreeMap<String, Object>();
				mapa.put("diferenca", diferencaVO);
				mapa.put("idProdutoEdicao", pe.getId());

				result.use(CustomJson.class).from(mapa).serialize();
			}
		}
		else{
			
			Map<String, Object> mapa = new TreeMap<String, Object>();
			mapa.put("diferenca", diferencaVO);
			mapa.put("idProdutoEdicao", pe.getId());

			result.use(CustomJson.class).from(mapa).serialize();
		}
	}

	private BigInteger obterQuantidadeReparteNota(ProdutoEdicao produtoEdicao,
												  List<RateioCotaVO> rateiosDiferenca) {
		
		BigInteger quantidadeReparteNota = BigInteger.ZERO;
			
		Date dataEnvioNota = null;
		Integer numeroCota = null;
		
		for (RateioCotaVO rateioCotaVO : rateiosDiferenca) {
			
			dataEnvioNota = rateioCotaVO.getDataEnvioNota();
			numeroCota = rateioCotaVO.getNumeroCota();
			
			break;
		}
		
		DetalheItemNotaFiscalDTO detalheItemNota = 
			this.itemNotaEnvioService.obterItemNotaEnvioLancamentoProduto(dataEnvioNota, numeroCota, produtoEdicao.getId());
		
		quantidadeReparteNota = detalheItemNota.getQuantidadeExemplares();
			
		return quantidadeReparteNota;
	}
	
	
	
	@SuppressWarnings({ "unchecked"})
	private List<RateioCotaVO> obterRateiosEdicaoDiferenca(Long idDiferenca){
			
		 Map<Long, List<RateioCotaVO>> mapaRateioCotas =
					(Map<Long, List<RateioCotaVO>>) this.httpSession.getAttribute(MAPA_RATEIOS_CADASTRADOS_SESSION_ATTRIBUTE);
			
		
		if(mapaRateioCotas!= null && mapaRateioCotas.containsKey(idDiferenca)){
			 
			 return mapaRateioCotas.get(idDiferenca);
		}
		
		return diferencaEstoqueService.obterRateiosCotaPorIdDiferenca(idDiferenca);
	}

	@SuppressWarnings("incomplete-switch")
	private BigInteger obterReparteAtualProdutoEdicao(DiferencaVO diferencaVO, Long idProdutoEdicao) {
		
		if(diferencaVO.getTipoEstoque() == null){
			return BigInteger.ZERO;
		}
		
		EstoqueProduto estoque = estoqueProdutoService.buscarEstoquePorProduto(idProdutoEdicao);
		
		if (estoque == null) {
			return BigInteger.ZERO;
		}
		
		BigInteger quantidadeEstoque = BigInteger.ZERO;
		
		switch (diferencaVO.getTipoEstoque()) {
			case DEVOLUCAO_ENCALHE:
				quantidadeEstoque =  estoque.getQtdeDevolucaoEncalhe();
				break;
			case DEVOLUCAO_FORNECEDOR:
				quantidadeEstoque =  estoque.getQtdeDevolucaoFornecedor();
				break;
			case LANCAMENTO:
				quantidadeEstoque =  estoque.getQtde();
				break;
			case SUPLEMENTAR:
				quantidadeEstoque =  estoque.getQtdeSuplementar();
				break;
			case DANIFICADO:
				quantidadeEstoque =  estoque.getQtdeDanificado();
				break;
		}
		
		return (quantidadeEstoque == null) ? BigInteger.ZERO : quantidadeEstoque;
	}
	
	@Post
	@Path("/lancamento/rateio/buscarReparteCotaPreco")
	public void buscarReparteCotaPreco(Long idProdutoEdicao, Integer numeroCota){
		
		
		if(numeroCota == null)
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Cota deve ser informada."));
		
		Long qtde = movimentoEstoqueCotaService.obterQuantidadeReparteProdutoCota(idProdutoEdicao, numeroCota);
				
		ProdutoEdicao pe = produtoEdicaoService.obterProdutoEdicao(idProdutoEdicao, false);
		
		Object[] dados = new Object[2];
		dados[0] = qtde;
		dados[1] = CurrencyUtil.formatarValor(pe.getPrecoVenda());
		
		result.use(Results.json()).from(dados, "result").serialize();
	}
	
	@Post
	@Path("/lancamento/buscarReparteCotaProduto")
	public void buscarReparteCotaProduto(String codigoProduto, String numeroEdicao, Integer numeroCota) {

		if(numeroCota == null)
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Cota deve ser informada."));
		
		if(codigoProduto == null || numeroEdicao == null)
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Produto deve ser informado."));
		
		ProdutoEdicao produtoEdicao = 
			this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
				codigoProduto, numeroEdicao);
		
		Long qtde = movimentoEstoqueCotaService.obterQuantidadeReparteProdutoCota(
			produtoEdicao.getId(), numeroCota);
		
		result.use(Results.json()).withoutRoot().from(qtde).serialize();
	}
	
	@Post
	@Path("/lancamento/rateio/buscarPrecoProdutoEdicao")
	public void buscarPrecoProdutoEdicao(String codigoProduto, Long numeroEdicao, boolean direcionadoParaEstoque){
		
		codigoProduto = StringUtils.leftPad(codigoProduto, 8, '0');
		
		if(numeroEdicao == null)
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Número da Edição não informado."));
		
		
		ProdutoEdicao pe = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao.toString());
		
		if(pe == null)
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Edição não encontrada."));
		
		EstoqueProduto estoque = estoqueProdutoService.buscarEstoquePorProduto(pe.getId());
		
		if(estoque == null) 
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Produto sem estoque cadastrado."));
		
		this.validarProdutoEmRecolhimento(direcionadoParaEstoque, null, null, pe);
						
		Object[] dados = new Object[4];
		dados[0] = CurrencyUtil.formatarValor(pe.getPrecoVenda());
		dados[1] = pe.getPacotePadrao();
		dados[2] = pe.getId();
		
		List<EstoqueDTO> estoques = gerarEstoques(estoque,codigoProduto,numeroEdicao);
				
		dados[3] = estoques;  	
		
		result.use(Results.json()).from(dados, "result").serialize();
	}
	
	@Post
	@Path("/lancamento/buscarEstoquesAlteracaoReparte")
	public void buscarEstoquesAlteracaoReparte() {
		
		List<EstoqueDTO> estoques = new ArrayList<EstoqueDTO>();
		
		estoques.add(
			new EstoqueDTO(
				TipoEstoque.LANCAMENTO.name(), 
				TipoEstoque.LANCAMENTO.getDescricao(),
				null
			) 
		);
		
		estoques.add(
			new EstoqueDTO(
				TipoEstoque.RECOLHIMENTO.name(), 
				TipoEstoque.RECOLHIMENTO.getDescricao(),
				null
			) 
		); 
		
		estoques.add(
			new EstoqueDTO(
				TipoEstoque.SUPLEMENTAR.name(), 
				TipoEstoque.SUPLEMENTAR.getDescricao(),
				null
			) 
		);
		
		estoques.add(
			new EstoqueDTO(
				TipoEstoque.PRODUTOS_DANIFICADOS.name(), 
				TipoEstoque.PRODUTOS_DANIFICADOS.getDescricao(),
				null
			) 
		);
		
		this.result.use(Results.json()).from(estoques, "result").serialize();
	}
		
	private List<EstoqueDTO> gerarEstoques(EstoqueProduto estoque,String codigoPrduto,Long numeroEdicao) {
		
		List<EstoqueDTO> estoques = new ArrayList<EstoqueDTO>();
		
		BigInteger qtde =
			(estoque.getQtde() != null)
				? estoque.getQtde() : BigInteger.ZERO;
		
		if (!BigInteger.ZERO.equals(qtde)) {
			estoques.add(
				new EstoqueDTO(
					TipoEstoque.LANCAMENTO.name(), 
					TipoEstoque.LANCAMENTO.getDescricao(),
					atualizarQuantidadeEstoqueComNovasDiferencas(qtde, TipoEstoque.LANCAMENTO)
					) 
				); 
		}
		
		BigInteger qtdeSuplementar =
			(estoque.getQtdeSuplementar() != null)
				? estoque.getQtdeSuplementar() : BigInteger.ZERO;
		
		if (!BigInteger.ZERO.equals(qtdeSuplementar)) {
			estoques.add(
				new EstoqueDTO(
					TipoEstoque.SUPLEMENTAR.name(), 
					TipoEstoque.SUPLEMENTAR.getDescricao(),
					atualizarQuantidadeEstoqueComNovasDiferencas(qtdeSuplementar, TipoEstoque.SUPLEMENTAR)
					) 
				);
		}
		
		BigInteger qtdeDevolucaoEncalhe =
			(estoque.getQtdeDevolucaoEncalhe() != null)
				? estoque.getQtdeDevolucaoEncalhe() : BigInteger.ZERO;
		
		if (!BigInteger.ZERO.equals(qtdeDevolucaoEncalhe)) {
			estoques.add(
				new EstoqueDTO(
					TipoEstoque.DEVOLUCAO_ENCALHE.name(), 
					TipoEstoque.DEVOLUCAO_ENCALHE.getDescricao(),
					atualizarQuantidadeEstoqueComNovasDiferencas(qtdeDevolucaoEncalhe, TipoEstoque.DEVOLUCAO_ENCALHE)
					) 
				);
		}
		
		return estoques;
	}

	@SuppressWarnings("unchecked")
	private BigInteger atualizarQuantidadeEstoqueComNovasDiferencas(BigInteger quantidadeEstoqueAtual,
														  			TipoEstoque tipoEstoque) {
		
		Set<DiferencaVO> listaNovasDiferencas =
			(Set<DiferencaVO>) this.httpSession.getAttribute(
				LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
		
		if (listaNovasDiferencas != null) {
			
			for (DiferencaVO diferencaVO : listaNovasDiferencas) {
				
				if (tipoEstoque != null
						&& diferencaVO.getTipoEstoque() != null
						&& diferencaVO.getTipoEstoque().equals(tipoEstoque)) {
				
					if (diferencaVO.getTipoDiferenca().isFalta()) {
						
						quantidadeEstoqueAtual =
							quantidadeEstoqueAtual.subtract(diferencaVO.getQuantidade());
						
					} else {
						
						quantidadeEstoqueAtual =
							quantidadeEstoqueAtual.add(diferencaVO.getQuantidade());
					}
				}
			}
		}
		
		return quantidadeEstoqueAtual;
	}
	
	@SuppressWarnings("unchecked")
	public BigInteger obterQuantidadeDiferencaEstoque(String codigoProduto, Long numeroEdicao, TipoEstoque tipoEstoque,
												      BigInteger quantidadeEstoque, Date dataOperacao) {
		
		Set<DiferencaVO> listaNovasDiferencas =
				(Set<DiferencaVO>) this.httpSession.getAttribute(LISTA_NOVAS_DIFERENCAS_VO_SESSION_ATTRIBUTE);
		
		BigInteger quatidadeReparteCadastrada =
			this.diferencaEstoqueService.obterQuantidadeTotalDiferencas(
				codigoProduto, numeroEdicao, tipoEstoque, dataOperacao);
		
		if (quatidadeReparteCadastrada != null) {
			
			quantidadeEstoque = quantidadeEstoque.add(quatidadeReparteCadastrada);
		}
		
		if(listaNovasDiferencas!= null && !listaNovasDiferencas.isEmpty()) {
			
			for(DiferencaVO diferenca : listaNovasDiferencas) {
				
				if(diferenca.getCodigoProduto().equals(codigoProduto)
						&& diferenca.getNumeroEdicao().equals(numeroEdicao.toString())
						&& diferenca.getTipoEstoque().equals(tipoEstoque)){
					
					quantidadeEstoque =
						this.calcularQuantidadesFaltasSobras(quantidadeEstoque, diferenca);
				}
			}
			
			return quantidadeEstoque;
		}
		
		return quantidadeEstoque;
	}

	private BigInteger calcularQuantidadesFaltasSobras(BigInteger quantidadeEstoque, DiferencaVO diferenca) {
		
		if (diferenca.getTipoDiferenca().equals(TipoDiferenca.FALTA_EM) 
				|| diferenca.getTipoDiferenca().equals(TipoDiferenca.FALTA_DE)) {
			
			quantidadeEstoque =
					quantidadeEstoque.subtract(diferenca.getQuantidade());
		
		} else if (diferenca.getTipoDiferenca().equals(TipoDiferenca.SOBRA_EM) 
				||diferenca.getTipoDiferenca().equals(TipoDiferenca.SOBRA_DE)
				||diferenca.getTipoDiferenca().equals(TipoDiferenca.SOBRA_DE_DIRECIONADA_COTA)
				||diferenca.getTipoDiferenca().equals(TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA)) {
			
			quantidadeEstoque =
					quantidadeEstoque.add(diferenca.getQuantidade());
		}
		
		return quantidadeEstoque;
	}

	@Post
	@Path("/lancamento/rateio/buscarProdutosCotaNota")
	public void	buscarProdutosCotaNota(Date dateNotaEnvio, Integer numeroCota) {
		
		this.validarDadosBuscaProdutosNota(dateNotaEnvio, numeroCota);
		
		List<DetalheItemNotaFiscalDTO> itensNotaEnvio = 
			this.itemNotaEnvioService.obterItensNotaEnvioLancamentoProduto(dateNotaEnvio, numeroCota);
		
		List<DiferencaVO> prods = new ArrayList<DiferencaVO>();
		
		DiferencaVO diferencaVO = null;

		for (DetalheItemNotaFiscalDTO detalheItemNota : itensNotaEnvio) {
			
			ProdutoEdicao produtoEdicao = produtoEdicaoService.obterProdutoEdicao(detalheItemNota.getIdProdutoEdicao(), false);
			
			if (!diferencaEstoqueService.validarProdutoEmRecolhimento(produtoEdicao)){
				continue;
			}
			
			diferencaVO = new DiferencaVO();

			diferencaVO.setCodigoProduto(detalheItemNota.getCodigoProduto());
			diferencaVO.setDescricaoProduto(detalheItemNota.getNomeProduto());
			diferencaVO.setNumeroEdicao(detalheItemNota.getNumeroEdicao().toString());
			diferencaVO.setPrecoVenda(detalheItemNota.getPrecoVenda().toString());
			diferencaVO.setQtdeEstoque(detalheItemNota.getQuantidadeExemplares().add(detalheItemNota.getSobrasFaltas()));
			diferencaVO.setPacotePadrao(detalheItemNota.getPacotePadrao().toString());
		
			prods.add(diferencaVO);
		}
		
		if (prods.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado!");
		}
		
		result.use(FlexiGridJson.class).from(prods).total(prods.size()).page(1).serialize();
	}

	private void validarDadosBuscaProdutosNota(Date dateNotaEnvio, Integer numeroCota) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (dateNotaEnvio == null) {
		
			listaMensagens.add("A data da nota de envio é obrigatória!");
		}
		
		if (numeroCota == null) {
			
			listaMensagens.add("O número da cota é obrigatório!");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, listaMensagens);
		}
	}

	@Post
	@Path("/obterDetalhesDiferencaCota")
	public void obterDetalhesDiferencaCota(FiltroDetalheDiferencaCotaDTO filtro, String sortorder, String sortname, int page, int rp) {

		filtro = prepararFiltroDetalheDiferencaCota(filtro, sortorder, sortname, page, rp);

		DetalheDiferencaCotaDTO detalheDiferencaCota = obterDetalheDiferencaCotaDTO(filtro);

		result.use(Results.json()).withoutRoot().from(detalheDiferencaCota).recursive().serialize();
	}
	
	@Get
	public void exportarDetalhesEstoqueCota(FileType fileType) throws IOException {
		
		if (fileType == null) {
		
			throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de arquivo não encontrado!");
		}

		FiltroDetalheDiferencaCotaDTO filtro = (FiltroDetalheDiferencaCotaDTO) this.httpSession.getAttribute(FILTRO_DETALHE_DIFERENCA_COTA);

		DetalheDiferencaCotaDTO detalhes = obterDetalheDiferencaCotaDTO(filtro);

		FileExporter.to("diferenca-detalhe-estoque-cota", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtro, detalhes, 
					detalhes.getDetalhesDiferenca(), RateioDiferencaCotaDTO.class, this.httpServletResponse);
		
		this.result.nothing();
	}

	private FiltroDetalheDiferencaCotaDTO prepararFiltroDetalheDiferencaCota(FiltroDetalheDiferencaCotaDTO filtro, 
																			 String sortorder, String sortname, int page, int rp) {

		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

		filtro.setPaginacao(paginacao);

		filtro.setColunaOrdenacao(
			Util.getEnumByStringValue(FiltroDetalheDiferencaCotaDTO.ColunaOrdenacao.values(), sortname)
		);
		
		this.httpSession.setAttribute(FILTRO_DETALHE_DIFERENCA_COTA, filtro);
 
		return filtro;
	}

	private DetalheDiferencaCotaDTO obterDetalheDiferencaCotaDTO(FiltroDetalheDiferencaCotaDTO filtro) {

		DetalheDiferencaCotaDTO detalheDiferencaCota = this.diferencaEstoqueService.obterDetalhesDiferencaCota(filtro);

		List<CellModelKeyValue<RateioDiferencaCotaDTO>> lista = new ArrayList<CellModelKeyValue<RateioDiferencaCotaDTO>>();

		int index = 0;

		for (RateioDiferencaCotaDTO detalheDiferencaDTO : detalheDiferencaCota.getDetalhesDiferenca()) {

			CellModelKeyValue<RateioDiferencaCotaDTO> cellModel = 
					new CellModelKeyValue<RateioDiferencaCotaDTO>(++index, detalheDiferencaDTO);

			lista.add(cellModel);
		}

		TableModel<CellModelKeyValue<RateioDiferencaCotaDTO>> tableModel = 
				new TableModel<CellModelKeyValue<RateioDiferencaCotaDTO>>();

		tableModel.setRows(lista);
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		tableModel.setTotal( detalheDiferencaCota!= null && detalheDiferencaCota.getQuantidadeTotalRegistrosDiferencaCota() != null 
								? detalheDiferencaCota.getQuantidadeTotalRegistrosDiferencaCota().intValue():0);

		detalheDiferencaCota.setTableModel(tableModel);

		return detalheDiferencaCota;
	}
	
	@Post
	@Path("/validarDadosParaImpressao")
	public void validarDadosParaImpressao(String dataMovimentoFormatada) throws Exception {
		
		this.diferencaEstoqueService.validarDadosParaImpressaoNaData(dataMovimentoFormatada);
	
		this.result.use(Results.json()).from("", "result").recursive().serialize();
	}
	
	@Get
	@Path("/imprimirRelatorioFaltasSobras")
	public Download imprimirRelatorioFaltasSobras(String dataMovimentoFormatada) throws Exception {
		
		Date dataMovimento = DateUtil.parseDataPTBR(dataMovimentoFormatada);
		
		byte[] relatorio = this.diferencaEstoqueService.imprimirRelatorioFaltasSobras(dataMovimento);
		
		String nomeArquivoRelatorio = "relatorioFaltasSobras" + FileType.PDF.getExtension();
		
		return new ByteArrayDownload(
			relatorio, FileType.PDF.getContentType(), nomeArquivoRelatorio, true);
	}
	
	/**
	 * Adiciona ou remove um item da lista de item adicionado
	 * 
	 * @param idDiferenca - id da diferença selecionada
	 * @param selecionado - true(adiciona a lista) false(remove da lista)
	 */
	@Post
	@Path("/selecionar")
	@SuppressWarnings("unchecked")
	public void selecionar(Long idDiferenca, Boolean selecionado) {
		
		List<Long> selecionados = (List<Long>) this.httpSession.getAttribute("diferencasSelecionadas");
		
		if (selecionados == null) {
			
			selecionados = new ArrayList<Long>();			
		}

		int index = selecionados.indexOf(idDiferenca); 
		
		if (index == -1) {
			
			selecionados.add(idDiferenca);
		}
		else{
			
			if (!selecionado){
				
				selecionados.remove(idDiferenca);
			}
		}
		
		this.httpSession.setAttribute("diferencasSelecionadas", selecionados);
		
		this.result.nothing();
	}
	
	/**
	 * Adiciona ou remove todos os itens da pesquisa a lista de itens selecionados da sessão.
	 * 
	 * @param selecionado - true(adiciona todos) false (remove todos)
	 */
	@Post
	public void selecionarTodos(Boolean selecionado, List<Long> listaIdsDiferencas){
		
		if (selecionado == false) {
			
			this.httpSession.setAttribute("diferencasSelecionadas", null);
			
		} else {
							
			this.httpSession.setAttribute("diferencasSelecionadas", listaIdsDiferencas);
		}

		this.result.nothing();
	}
	
}
