package br.com.abril.nds.controllers.estoque;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ConsultaNotaFiscalVO;
import br.com.abril.nds.client.vo.ResultadoConsultaDetallheNFVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.DetalheNotaFiscalDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NotaFiscalEntradaFornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO.ColunaOrdenacao;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.NaturezaOperacaoService;
import br.com.abril.nds.service.NotaFiscalEntradaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pela tela de consulta de Notas Fiscais. 
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/estoque/consultaNotas")
@Rules(Permissao.ROLE_ESTOQUE_CONSULTA_NOTAS)
public class ConsultaNotasController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private NaturezaOperacaoService tipoNotaFiscalService;

	@Autowired
	private NotaFiscalEntradaService notaFiscalService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroConsultaNotaFiscal";
	
	@Path("/")
	public void index() {
		
		preencherCombos();
		
		inserirDataAtual();
	}
	
	@Get
	public void exportar(final FileType fileType) throws IOException {
		
		if (fileType == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de arquivo não encontrado!");
		}
		
		FiltroConsultaNotaFiscalDTO filtro = this.obterFiltroExportacao();
		
		List<NotaFiscalEntradaFornecedorDTO> listaNotasFiscais =
			notaFiscalService.obterNotasFiscaisCadastradasDTO(filtro);
		
		List<ConsultaNotaFiscalVO> listaConsultaNF = this.obterListaConsultaNotasFiscais(listaNotasFiscais);
		 
		FileExporter.to("consulta-nota-fiscal", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					listaConsultaNF, ConsultaNotaFiscalVO.class, this.httpServletResponse);
	}
	
	public void pesquisarNotas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal,
							   String dataInicial, String dataFinal, String sortorder, String sortname, int page, int rp) {

		filtroConsultaNotaFiscal =
				prepararFiltro(filtroConsultaNotaFiscal, dataInicial, dataFinal, sortorder, sortname, page, rp);

		this.session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroConsultaNotaFiscal);
		
		Integer quantidadeRegistros = this.notaFiscalService.obterQuantidadeNotasFicaisCadastradas(filtroConsultaNotaFiscal);
		
		if (quantidadeRegistros <= 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<NotaFiscalEntradaFornecedorDTO> listaNotasFiscais = notaFiscalService.obterNotasFiscaisCadastradasDTO(filtroConsultaNotaFiscal);
					
		this.result.use(FlexiGridJson.class).noReference().from(listaNotasFiscais).total(quantidadeRegistros.intValue()).page(page).serialize();
		
	}

	public void pesquisarDetalhesNotaFiscal(final Long idNota, final int page, final int rp, final String sortname, final String sortorder) {

		final PaginacaoVO paginacao = new PaginacaoVO(sortname, sortorder);
		
		final DetalheNotaFiscalDTO detalheNotaFiscal = this.notaFiscalService.obterDetalhesNotaFical(idNota, paginacao);

		if (detalheNotaFiscal == null || detalheNotaFiscal.getItensDetalhados() == null
									  || detalheNotaFiscal.getItensDetalhados().isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		TableModel<CellModel> tableModelDetalhesNota = getTableModelDetalhesNotaFiscal(detalheNotaFiscal.getItensDetalhados());
		tableModelDetalhesNota.setPage(1);
		tableModelDetalhesNota.setTotal(detalheNotaFiscal.getItensDetalhados().size());

		ResultadoConsultaDetallheNFVO resultadoConsultaDetallheNF = 
			new ResultadoConsultaDetallheNFVO(
				tableModelDetalhesNota, String.valueOf(detalheNotaFiscal.getTotalExemplares().intValue()), 
					CurrencyUtil.formatarValor(detalheNotaFiscal.getValorTotalSumarizado()), 
					CurrencyUtil.formatarValorQuatroCasas(detalheNotaFiscal.getValorTotalSumarizadoComDesconto()));

		this.result.use(Results.json()).withoutRoot().from(resultadoConsultaDetallheNF).recursive().serialize();
	}
	
	/**
	 * Obtém o filtro de pesquisa para exportação.
	 * 
	 * @return FiltroConsultaNotaFiscalDTO
	 */
	private FiltroConsultaNotaFiscalDTO obterFiltroExportacao() {
		
		FiltroConsultaNotaFiscalDTO filtroSessao =
			(FiltroConsultaNotaFiscalDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSessao == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Sessão expirada. Por favor, logue novamente.");
		}
		
		if (filtroSessao.getPaginacao() != null) {
			
			filtroSessao.getPaginacao().setPaginaAtual(null);
			filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
		}
		
		if (filtroSessao.getIdFornecedor() != null) {
			
			Fornecedor fornecedor = 
				this.fornecedorService.obterFornecedorPorId(filtroSessao.getIdFornecedor());
		
			if (fornecedor != null) {
				
				filtroSessao.setNomeFornecedor(fornecedor.getJuridica().getRazaoSocial());
			}
		}
		
		if (filtroSessao.getIdTipoNotaFiscal() != null) {
			
			NaturezaOperacao tipoNotaFiscal = 
				this.tipoNotaFiscalService.obterPorId(filtroSessao.getIdTipoNotaFiscal());
		
			if (tipoNotaFiscal != null) {
				
				filtroSessao.setTipoNotaFiscal(tipoNotaFiscal.getDescricao());
			}
		}
		
		return filtroSessao;
	}
	
	private void preencherCombos() {

		List<Fornecedor> fornecedores = fornecedorService.obterFornecedoresAtivos();
		
		List<Fornecedor> fornecedoresDistribuicao = new ArrayList<Fornecedor>();
		
		for (Fornecedor f : fornecedores){
			
			
			if (f.getFornecedorUnificador()==null){
				
				fornecedoresDistribuicao.add(f);
			}
		}

		List<NaturezaOperacao> tiposNotaFiscal = 
				this.tipoNotaFiscalService.obterNaturezasOperacoesPorTipoAtividadeDistribuidor();

		this.result.include("fornecedores", fornecedoresDistribuicao);
		this.result.include("tiposNotaFiscal", tiposNotaFiscal);
	}

	private void inserirDataAtual() {
		
		result.include("dataAtual", DateUtil.formatarData(new Date(), "dd/MM/yyyy"));
	}
	
	private List<ConsultaNotaFiscalVO> obterListaConsultaNotasFiscais(List<NotaFiscalEntradaFornecedorDTO> listaNotasFiscais) {

		Map<Long, String> mapaFornecedorNotaFiscal = obterMapaFornecedorNotaFiscal(listaNotasFiscais);
		
		List<ConsultaNotaFiscalVO> listaConsultasNF = new ArrayList<ConsultaNotaFiscalVO>();

		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		for (NotaFiscalEntradaFornecedorDTO notaFiscal : listaNotasFiscais) {
			
			ConsultaNotaFiscalVO consultaNotaFiscalVO = new ConsultaNotaFiscalVO();
			try {
				consultaNotaFiscalVO.setDataEmissao( sdf.parse(notaFiscal.getDataEmissao()) );
				consultaNotaFiscalVO.setDataExpedicao( sdf.parse(notaFiscal.getDataExpedicao()) );
			} catch (ParseException e) {
				throw new ValidacaoException(TipoMensagem.ERROR, e.getMessage());
			}
			
			consultaNotaFiscalVO.setChaveAcesso(notaFiscal.getChaveAcesso());
			consultaNotaFiscalVO.setNomeFornecedor(mapaFornecedorNotaFiscal.get(notaFiscal.getId()));
			consultaNotaFiscalVO.setNotaRecebida(notaFiscal.getNotaRecebida());
			if(!StringUtil.isEmpty(notaFiscal.getNumero())) {
				try {
					consultaNotaFiscalVO.setNumeroNota( Long.parseLong(notaFiscal.getNumero()) );
				} catch(Exception e) {
					
				}
			}
//			consultaNotaFiscalVO.setTipoNotaFiscal(notaFiscal.getDescricao());
			consultaNotaFiscalVO.setValor(notaFiscal.getValorTotalNota());
			consultaNotaFiscalVO.setValorComDesconto(notaFiscal.getValorTotalNotaComDesconto());
			consultaNotaFiscalVO.setSerie(notaFiscal.getSerie());
			consultaNotaFiscalVO.setNotaEnvio(notaFiscal.getNumeroNotaEnvio());
			
			listaConsultasNF.add(consultaNotaFiscalVO);
		}

		return listaConsultasNF;
	}

	/**
	 * Obtém mapa de nomes de fornecedores de uma nota fiscal entrada.
	 * 
	 * @param filtroConsultaNotaFiscal
	 * 
	 * @return Map<Long, String>
	 */
	private Map<Long, String> obterMapaFornecedorNotaFiscal(List<NotaFiscalEntradaFornecedorDTO> listaNotaFiscalEntradaFornecedor) {
		
		Map<Long, String> mapaFornecedorNotaFiscal = new LinkedHashMap<Long, String>();
		
		if(listaNotaFiscalEntradaFornecedor == null) {
			return mapaFornecedorNotaFiscal;
		}
		
		List<Long> listaIdNotaFiscal = new ArrayList<Long>();
		
		for(NotaFiscalEntradaFornecedorDTO notaFiscalEntradaFornecedor : listaNotaFiscalEntradaFornecedor) {
			listaIdNotaFiscal.add(notaFiscalEntradaFornecedor.getId());
		}
		
		List<ItemDTO<Long, String>> listaFonecedorNotaFiscal = notaFiscalService.obterFornecedorNotaFiscal(listaIdNotaFiscal);
		
		if(listaFonecedorNotaFiscal == null || listaFonecedorNotaFiscal.isEmpty()) {
			return mapaFornecedorNotaFiscal;
		}
		
		for(ItemDTO<Long, String> item : listaFonecedorNotaFiscal) {
			if(mapaFornecedorNotaFiscal.containsKey(item.getKey())) {
				mapaFornecedorNotaFiscal.put(item.getKey(), "Diversos");
			} else {
				mapaFornecedorNotaFiscal.put(item.getKey(), item.getValue());
			}
		}
		
		return mapaFornecedorNotaFiscal;
		
	}
	
	private TableModel<CellModel> getTableModelDetalhesNotaFiscal(List<DetalheItemNotaFiscalDTO> listaDetalhesNotaFiscal) {
		
		List<CellModel> listaCellModels = new LinkedList<CellModel>();

		for (DetalheItemNotaFiscalDTO detalheNotaFiscalVO : listaDetalhesNotaFiscal) {

			boolean isFalta = 
					TipoDiferenca.FALTA_DE.equals(detalheNotaFiscalVO.getTipoDiferenca()) || 
					  TipoDiferenca.FALTA_EM.equals(detalheNotaFiscalVO.getTipoDiferenca());

			String sobrasFaltas = isFalta ? "-" : "";
			sobrasFaltas += itemExibicaoToString(detalheNotaFiscalVO.getSobrasFaltas());
		    
		    BigDecimal valorTotal = 
		    	(detalheNotaFiscalVO.getValorTotal() == null) 
		    		? BigDecimal.ZERO : detalheNotaFiscalVO.getValorTotal();
		    
		    BigDecimal valorTotalComDesconto = 
			    	(detalheNotaFiscalVO.getValorTotalComDesconto() == null) 
			    		? BigDecimal.ZERO : detalheNotaFiscalVO.getValorTotalComDesconto();
		    
		    BigInteger qtdeExemplares = 
		    	(detalheNotaFiscalVO.getQuantidadeExemplares() == null) 
		    		? BigInteger.ZERO : detalheNotaFiscalVO.getQuantidadeExemplares();
		    
			CellModel cellModel = 
					new CellModel(
							detalheNotaFiscalVO.getCodigoItem().intValue(),
							itemExibicaoToString(detalheNotaFiscalVO.getCodigoProduto()),
							itemExibicaoToString(detalheNotaFiscalVO.getNomeProduto()),
							itemExibicaoToString(detalheNotaFiscalVO.getNumeroEdicao()),
							itemExibicaoToString(CurrencyUtil.formatarValor(detalheNotaFiscalVO.getPrecoVenda())),
							itemExibicaoToString(CurrencyUtil.formatarValorQuatroCasas(detalheNotaFiscalVO.getPrecoComDesconto())),
							itemExibicaoToString(qtdeExemplares.intValue()),
							sobrasFaltas, 
							itemExibicaoToString(CurrencyUtil.formatarValor(valorTotal)),
							itemExibicaoToString(CurrencyUtil.formatarValorQuatroCasas(valorTotalComDesconto)),
							detalheNotaFiscalVO.isProdutoSemCadastro());

			listaCellModels.add(cellModel);
		}
		
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		tableModel.setRows(listaCellModels);

		return tableModel;
	}
	
	private String itemExibicaoToString(Object itemExibicao) {
		
		return String.valueOf(itemExibicao == null ? "-" : itemExibicao);
	}
	
	private FiltroConsultaNotaFiscalDTO prepararFiltro(
			FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal,
			String dataInicial, String dataFinal, String sortorder, String sortname, int page, int rp) {

		if (filtroConsultaNotaFiscal.getIdFornecedor() == -1L) {
			
			filtroConsultaNotaFiscal.setIdFornecedor(null);
		}
		
		if (filtroConsultaNotaFiscal.getIdTipoNotaFiscal()!=null && filtroConsultaNotaFiscal.getIdTipoNotaFiscal() == -1) {
			
			filtroConsultaNotaFiscal.setIdTipoNotaFiscal(null);
		}

		PeriodoVO periodo = obterPeriodoValidado(dataInicial, dataFinal);
		
		filtroConsultaNotaFiscal.setPeriodo(periodo);
		
		Long idDistribuidor = this.distribuidorService.obterId();
		
		filtroConsultaNotaFiscal.setIdDistribuidor(idDistribuidor);
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder,sortname);

		filtroConsultaNotaFiscal.setPaginacao(paginacao);

		String[] sortNames = sortname.split(",");
		
		List<ColunaOrdenacao> listaColunaOrdenacao = new ArrayList<ColunaOrdenacao>();
		
		for (String sort : sortNames) {

			listaColunaOrdenacao.add(Util.getEnumByStringValue(ColunaOrdenacao.values(), sort.trim()));
		}
		
		filtroConsultaNotaFiscal.setListaColunaOrdenacao(listaColunaOrdenacao);

		return filtroConsultaNotaFiscal;
	}
	
	private PeriodoVO obterPeriodoValidado(String dataInicial, String dataFinal) {
		
		ValidacaoVO validacao = new ValidacaoVO();
		
		validacao.setTipoMensagem(TipoMensagem.WARNING);
		
		List<String> mensagens = new ArrayList<String>();

		if (dataInicial != null && !dataInicial.isEmpty() && !DateUtil.isValidDate(dataInicial, "dd/MM/yyyy")) {
			
			mensagens.add("Data inicial inválida");
		} 
		
		if (dataFinal != null && !dataFinal.isEmpty() && !DateUtil.isValidDate(dataFinal, "dd/MM/yyyy")) {
			
			mensagens.add("Data final inválida");
		}

		if ((dataInicial == null || dataInicial.isEmpty()) && dataFinal != null && !dataFinal.isEmpty()) {
			
			mensagens.add("Data inicial inválida");
		}

		if (!mensagens.isEmpty()) {

			validacao.setListaMensagens(mensagens);
			
			throw new ValidacaoException(validacao);
		}
		
		if ((dataInicial == null || dataInicial.isEmpty()) && (dataFinal == null || dataFinal.isEmpty())) {
			
			return new PeriodoVO();
		
		} else if (dataInicial != null && !dataInicial.isEmpty() && (dataFinal == null || dataFinal.isEmpty())) {
			
			return new PeriodoVO(DateUtil.parseData(dataInicial, "dd/MM/yyyy"), DateUtil.removerTimestamp(new Date()));
		} 

		return new PeriodoVO(DateUtil.parseData(dataInicial, "dd/MM/yyyy"), DateUtil.parseData(dataFinal, "dd/MM/yyyy"));
	}

}
