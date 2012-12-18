package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.DividaComissaoDTO;
import br.com.abril.nds.dto.DividaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO.ColunaOrdenacao;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/inadimplencia")
public class InadimplenciaController {


	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroInadimplencia";
	
	private static final String ERRO_PESQUISAR_INADIMPLENCIAS = "Erro inesperado ao pesquisar inadimplencias.";	
	private static final String WARNING_PESQUISA_SEM_RESULTADO = "Não há resultados para a pesquisa realizada.";

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private DividaService dividaService;
	
	private static final String FORMATO_DATA = "dd/MM/yyyy";
	
	@Autowired
	private static final Logger LOG = LoggerFactory
			.getLogger(InadimplenciaController.class);
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private CobrancaService cobrancaService;
	
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
	@Rules(Permissao.ROLE_FINANCEIRO_HISTORICO_INADIMPLENCIA)
	public void index() {
		gerarListaStatus();
		result.forwardTo(InadimplenciaController.class).inadimplencia();
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
			boolean situacaoEmAberto, boolean situacaoNegociada, boolean situacaoPaga) {
		
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();

		TableModel<CellModelKeyValue<StatusDividaDTO>> grid = null;
		
		String total = "0,00";
		String count = "0";
		
		try {
			
			FiltroCotaInadimplenteDTO filtroAtual = new FiltroCotaInadimplenteDTO();
			filtroAtual.setPaginacao(new PaginacaoVO(page,rp,sortorder,sortname));
			filtroAtual.setColunaOrdenacao(Util.getEnumByStringValue(ColunaOrdenacao.values(), sortname));
			filtroAtual.setNumCota(numCota);
			filtroAtual.setNomeCota(nomeCota.replace(" (PF)", "").replace(" (PJ)", ""));
			filtroAtual.setPeriodoDe(periodoDe);
			filtroAtual.setPeriodoAte(periodoAte);
			filtroAtual.setSituacaoEmAberto(situacaoEmAberto);
			filtroAtual.setSituacaoPaga(situacaoPaga);
			filtroAtual.setSituacaoNegociada(situacaoNegociada);
			
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
			LOG.error(ERRO_PESQUISAR_INADIMPLENCIAS, e);
		}
	
		if(grid==null) {
			grid = new TableModel<CellModelKeyValue<StatusDividaDTO>>();
		}
		
		Object[] retorno = new Object[5];
		retorno[0] = grid;
		retorno[1] = mensagens;
		retorno[2] = status.name();
		retorno[3] = total;
		retorno[4] = count;
		
	
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
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtro
	 */
	private void tratarFiltro(FiltroCotaInadimplenteDTO filtroAtual) {

		FiltroCotaInadimplenteDTO filtroSession = (FiltroCotaInadimplenteDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
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
		
		List<Divida> dividas = dividaService.getDividasAcumulo(idDivida);
		
		List<DividaDTO> dividasDTO = new ArrayList<DividaDTO>();
		
		for(Divida divida : dividas) {
			dividasDTO.add(new DividaDTO(
					DateUtil.formatarData(divida.getCobranca().getDataVencimento(), FORMATO_DATA), 
					CurrencyUtil.formatarValor(divida.getCobranca().getValor())));
		}
		result.use(Results.json()).from(dividasDTO, "result").serialize();
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
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroCotaInadimplenteDTO filtro = (FiltroCotaInadimplenteDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		List<StatusDividaDTO> listaInadimplencias = dividaService.obterInadimplenciasCota(filtro);
		
		Double total = dividaService.obterSomaDividas(filtro);
		Integer count = dividaService.obterTotalCotasInadimplencias(filtro).intValue();
		
		RodapeDTO rodape = new RodapeDTO(count, CurrencyUtil.formatarValor(total));
		
		FileExporter.to("inadimplencia_cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, rodape, 
				listaInadimplencias, StatusDividaDTO.class, this.httpResponse);
		
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
	
}
