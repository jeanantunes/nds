package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DividaComissaoDTO;
import br.com.abril.nds.dto.DividaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO.ColunaOrdenacao;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/inadimplencia")
@Rules(Permissao.ROLE_FINANCEIRO_HISTORICO_INADIMPLENCIA)
public class InadimplenciaController extends BaseController {
    
    private static final Logger LOGGER = Logger.getLogger(InadimplenciaController.class);


	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroInadimplencia";
	
	private static final String ERRO_PESQUISAR_INADIMPLENCIAS = "Erro inesperado ao pesquisar inadimplencias.";	
    private static final String WARNING_PESQUISA_SEM_RESULTADO = "Não há resultados para a pesquisa realizada.";
    private static final String WARNING_PESQUISA_VALIDACAO_SITUACAO = "É necessário selecionar a situação da dívida.";

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private DividaService dividaService;
	
	

	@Autowired
	private HttpServletResponse httpResponse;
	
	private final Result result;
	private final HttpSession session;
	
	public InadimplenciaController(Result result, HttpSession session) {
		this.result=result;
		this.session = session;
	}
	
	public void inadimplencia() {
		
	}
	
	/**
	 * Inicializa dados da tela
	 */
	@Path("/")
	public void index() {
		gerarListaStatus();
		this.result.include("statusDivida", StatusDivida.values());
		this.result.forwardTo(InadimplenciaController.class).inadimplencia();
	}

	private void gerarListaStatus() {
		
		List<ItemDTO<String, String>> status = new ArrayList<ItemDTO<String,String>>();
				
		for(SituacaoCadastro situacao : SituacaoCadastro.values()) {
			status.add(new ItemDTO<String, String>(situacao.name(), situacao.toString()));
		}
		
		result.include("itensStatus", status);
		
	}
	
	public void pesquisar( Integer page, Integer rp, String sortname, String sortorder,
			String periodoDe, String periodoAte, String nomeCota, Integer numCota, String statusCota, 
			List<StatusDivida> statusDivida) {
		
		List<String> mensagens = new ArrayList<String>();
		TipoMensagem status = TipoMensagem.SUCCESS;
		TableModel<CellModelKeyValue<StatusDividaDTO>> grid = new TableModel<CellModelKeyValue<StatusDividaDTO>>();
		
		if (statusDivida == null) {
			
			mensagens.add(WARNING_PESQUISA_VALIDACAO_SITUACAO);
			status=TipoMensagem.WARNING;
			
			this.tratraRetornoPesquisa(mensagens, status, grid, null, null);
			
			return;
		}
		
		String total = "0,00";
		String count = "0";
		
		try {
			
			nomeCota = PessoaUtil.removerSufixoDeTipo(nomeCota);
			
			FiltroCotaInadimplenteDTO filtroAtual = new FiltroCotaInadimplenteDTO();
			filtroAtual.setPaginacao(new PaginacaoVO(page,rp,sortorder,sortname));
			filtroAtual.setColunaOrdenacao(Util.getEnumByStringValue(ColunaOrdenacao.values(), sortname));
			
			filtroAtual.setNumCota(numCota);
			filtroAtual.setNomeCota(nomeCota);
			filtroAtual.setPeriodoDe(periodoDe);
			filtroAtual.setPeriodoAte(periodoAte);
			
			filtroAtual.setStatusDivida(statusDivida);
			
			filtroAtual.setDataOperacaoDistribuidor(distribuidorService.obterDataOperacaoDistribuidor());
			
			if(statusCota!= null && !statusCota.equals("none")) {
				
				filtroAtual.setStatusCota(SituacaoCadastro.valueOf(statusCota).name());
			}
		
			tratarFiltro(filtroAtual);
			
			grid = obterInadimplencias(filtroAtual);
			
			total = CurrencyUtil.formatarValor(dividaService.obterSomaDividas(filtroAtual));
			count = dividaService.obterTotalCotasInadimplencias(filtroAtual).toString();
		
		} catch(ValidacaoException e) {
		
			mensagens.clear();
		
			mensagens.addAll(e.getValidacao().getListaMensagens());
			status=TipoMensagem.WARNING;	
		}catch(Exception e) {
			mensagens.clear();
			mensagens.add(ERRO_PESQUISAR_INADIMPLENCIAS);
			status=TipoMensagem.ERROR;
			LOGGER.error(ERRO_PESQUISAR_INADIMPLENCIAS, e);
		}
		
		this.tratraRetornoPesquisa(mensagens, status, grid, total, count);
	}

	private void tratraRetornoPesquisa(List<String> mensagens, TipoMensagem status,
									   TableModel<CellModelKeyValue<StatusDividaDTO>> grid,
									   String total, String count) {
		
		Object[] retorno = new Object[5];
		
		retorno[0] = grid != null ? grid : "";
		retorno[1] = mensagens != null ? mensagens : "";
		retorno[2] = status != null ? status.name() : "";
		retorno[3] = total != null ? total : "";
		retorno[4] = count != null ? count : "";
		
		result.use(Results.json()).withoutRoot().from(retorno).recursive().serialize();
	}

	private TableModel<CellModelKeyValue<StatusDividaDTO>> obterInadimplencias(
			FiltroCotaInadimplenteDTO filtro) {
	
		List<StatusDividaDTO> listaInadimplencias = dividaService.obterInadimplenciasCota(filtro);
				
		if (listaInadimplencias == null || listaInadimplencias.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, WARNING_PESQUISA_SEM_RESULTADO);
		}
			
		Long totalRegistros = dividaService.obterTotalInadimplenciasCota(filtro);
		
		TableModel<CellModelKeyValue<StatusDividaDTO>> tableModel = new TableModel<CellModelKeyValue<StatusDividaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaInadimplencias));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal( (totalRegistros == null)?0:totalRegistros.intValue());
		
		return tableModel;
	}

	        /**
     * Executa tratamento de paginação em função de alteração do filtro de
     * pesquisa.
     * 
     * @param filtro
     */
	private void tratarFiltro(FiltroCotaInadimplenteDTO filtroAtual) {

		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}
	
	@Exportable
	public class RodapeDTO {
		@Export(label="Qtde Cotas:", alignWithHeader="Nome")
		private Integer qtde;
		@Export(label="Total R$:", alignWithHeader="Divida Total R$:")
		private String total;
		
		public RodapeDTO(Integer qtde, String total) {
			this.qtde = qtde;
			this.total = total;
		}
		
		public Integer getQtde() {
			return qtde;
		}
		public String getTotal() {
			return total;
		}
	}
	
	        /**
     * Obtém detalhes de acumulo da divida
     * 
     * @param idDivida
     */
	public void getDetalhesDivida(Long idDivida) {
		
		List<DividaDTO> dividasDTO = null;
		
		Divida dividaAtual = dividaService.obterDividaPorId(idDivida);
		
		if (StatusDivida.NEGOCIADA.equals(dividaAtual.getStatus())) {
			
			dividasDTO = this.montarDividasDTONegociacao(idDivida);
			
		} else {
			
			dividasDTO = this.montarDividasDTO(idDivida);
		}
		
		result.use(Results.json()).from(dividasDTO, "result").serialize();
	}
	
	private List<DividaDTO> montarDividasDTONegociacao(Long idDivida) {
		
		List<MovimentoFinanceiroCota> movimentosFinanceiroCota =
			this.dividaService.obterDividasNegociacao(idDivida);
		
		List<DividaDTO> dividasDTO = new ArrayList<>();
		
		DividaDTO dividaDTO = null;
		
		for(MovimentoFinanceiroCota mec : movimentosFinanceiroCota) {
			
			dividaDTO = new DividaDTO(DateUtil.formatarDataPTBR(mec.getData()),
									  CurrencyUtil.formatarValor(mec.getValor()));
			
			dividasDTO.add(dividaDTO);
		}
		
		return dividasDTO;
	}
	
	private List<DividaDTO> montarDividasDTO(Long idDivida) {
		
		List<Divida> dividas = this.dividaService.obterDividasAcumulo(idDivida);
		
		List<DividaDTO> dividasDTO = new ArrayList<>();
		
		DividaDTO dividaDTO = null;
		
		for(Divida divida : dividas) {
			
			dividaDTO =
				new DividaDTO(DateUtil.formatarDataPTBR(divida.getCobranca().getDataVencimento()), 
							  CurrencyUtil.formatarValor(divida.getCobranca().getValor()));
			
			dividasDTO.add(dividaDTO);
		}
		
		return dividasDTO;
	}

	        /**
     * Obtém a divida
     * 
     * @param idDivida
     */
	public void getDividaComissao(Long idDivida) {
		DividaComissaoDTO resultado = dividaService.obterDadosDividaComissao(idDivida);
		
		result.use(Results.json()).from(resultado, "result").serialize();			
	}	
	
	        /**
     * Exporta os dados da pesquisa.
     * 
     * @param fileType - tipo de arquivo
     * 
     * @throws IOException Exceção de E/S
     */
	@SuppressWarnings("deprecation")
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroCotaInadimplenteDTO filtro = (FiltroCotaInadimplenteDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		filtro.getPaginacao().setQtdResultadosPorPagina(null);
		
		List<StatusDividaDTO> listaInadimplencias = dividaService.obterInadimplenciasCota(filtro);
		
		Double total = dividaService.obterSomaDividas(filtro);
		Integer count = dividaService.obterTotalCotasInadimplencias(filtro).intValue();
		
		RodapeDTO rodape = new RodapeDTO(count, CurrencyUtil.formatarValor(total));
		
		FileExporter.to("inadimplencia_cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, rodape, 
				listaInadimplencias, StatusDividaDTO.class, this.httpResponse);
		
		result.nothing();
	}
}
