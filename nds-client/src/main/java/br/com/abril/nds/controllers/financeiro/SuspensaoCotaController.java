package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.DividaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/suspensaoCota")
@Rules(Permissao.ROLE_FINANCEIRO_SUSPENSAO_COTA)
public class SuspensaoCotaController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SuspensaoCotaController.class);

    protected static final String WARNING_SUSPENSAO_COTA_SUSPENSA = "Não foi possível realizar a suspensão pois a cota já estava suspensa.";
    protected static final String MSG_PESQUISA_SEM_RESULTADO = "Não há sugestões para suspensão de cota.";
	protected static final String NENHUM_REGISTRO_SELECIONADO = "Nenhum registro foi selecionado!";
    protected static final String SEM_BAIXA_NA_DATA = "Não foi feita baixa bancária na data de operação atual.";
	protected static final int RESULTADOS_POR_PAGINA_INCIAL = 15;
    protected static final String ERRO_CARREGAR_SUGESTAO_SUSPENSAO = "Erro inesperado ao carregar sugestão de suspensão de cotas.";
	protected static final String ERRO_SUSPENDER_COTAS = "Erro inesperado ao suspender cotas.";
	private static final String FORMATO_DATA = "dd/MM/yyyy";
	private static final String PAGINACAO_SESSION_ATTRIBUTE = "paginacaoSuspensaoCotaSession";
	

		
	@Autowired
	private HttpServletResponse httpResponse;
	private final Result result;
	private final HttpSession session;
	
	private CotaService cotaService;
	
	
	public SuspensaoCotaController(Result result,HttpSession session,
			CotaService cotaService) {
		
		this.result = result;
		this.session = session;
		this.cotaService = cotaService;
	}
	
	/**
	 * Inicializa dados da tela
	 */
	@Path("/")
	public void index() {
		
		session.setAttribute("selecionados",null);
	}
	
	public void obterCotasSuspensaoJSON(Integer page, Integer rp, String sortname, 
			String sortorder) {
		
		this.session.setAttribute(PAGINACAO_SESSION_ATTRIBUTE, new PaginacaoVO(sortname, sortorder));
		
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();
		
		TableModel<CellModelKeyValue<CotaSuspensaoDTO>> grid = null;
		
		try {
			
			grid = obterCotasSuspensao(sortname,sortorder,page,rp);			
				
		} catch(ValidacaoException e) {
			mensagens = e.getValidacao().getListaMensagens();
			status = e.getValidacao().getTipoMensagem();
		} catch(Exception e) {			
			mensagens.add(ERRO_CARREGAR_SUGESTAO_SUSPENSAO);
			LOGGER.error(ERRO_CARREGAR_SUGESTAO_SUSPENSAO, e);
			status = TipoMensagem.ERROR;
		}
		
		if(grid == null) {
			grid = new TableModel<CellModelKeyValue<CotaSuspensaoDTO>>();
		}
		
		Object[] retorno = new Object[5];
		retorno[0] = grid;
		retorno[1] = mensagens;
		retorno[2] = status.name();
		retorno[3] = grid.getTotal();
		retorno[4] = CurrencyUtil.formatarValor(this.cotaService.obterTotalDividaCotasSujeitasSuspensao());
		
		result.use(Results.json()).withoutRoot().from(retorno).recursive().serialize();	
	}
	
	public void getInadinplenciasDaCota(Long idCota) {
		
		List<Cobranca> cobrancas = cotaService.obterCobrancasDaCotaEmAberto(idCota);
		
		List<DividaDTO> dividas = new ArrayList<DividaDTO>();
		
		for(Cobranca cobranca : cobrancas) {
			dividas.add(new DividaDTO(
					DateUtil.formatarData(cobranca.getDataVencimento(), FORMATO_DATA),
					DateUtil.formatarData(cobranca.getDataEmissao(), FORMATO_DATA),
					CurrencyUtil.formatarValorQuatroCasas(MathUtil.round(cobranca.getValor(), 4))));
		}
		result.use(Results.json()).from(dividas, "result").serialize();
	}
	
	private TableModel<CellModelKeyValue<CotaSuspensaoDTO>> obterCotasSuspensao(String sortname, 
			String sortorder, Integer page, Integer rp) {
		
		Long total = cotaService.obterTotalCotasSujeitasSuspensao();
		Integer inicio = (page-1)*rp;
        if (total == 0L) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,MSG_PESQUISA_SEM_RESULTADO));
		}	
		
		List<CotaSuspensaoDTO> listaCotaSuspensao = cotaService.obterDTOCotasSujeitasSuspensao(sortorder,sortname,inicio,rp);
				
		List<CellModelKeyValue<CotaSuspensaoDTO>> listaCelula = new LinkedList<CellModelKeyValue<CotaSuspensaoDTO>>();
		
		@SuppressWarnings("unchecked")
		List<Long> selecionados = (List<Long>) session.getAttribute("selecionados");
		
		for(CotaSuspensaoDTO cota : listaCotaSuspensao) {						
		
			if( selecionados!=null && selecionados.contains(cota.getIdCota()) ) {
				cota.setSelecionado(true);
			}
			
			listaCelula.add(new CellModelKeyValue<CotaSuspensaoDTO>(cota.getIdCota().intValue(),cota));			
		}
		
		if(listaCotaSuspensao.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,MSG_PESQUISA_SEM_RESULTADO));
		}		

		TableModel<CellModelKeyValue<CotaSuspensaoDTO>> grid = new TableModel<CellModelKeyValue<CotaSuspensaoDTO>>();
		
		grid.setPage(page);
		grid.setTotal(total.intValue());
		grid.setRows(listaCelula);
		
		return grid;
	}
	
	/**
	 * Adiciona ou remove um item da lista de item adicionado
	 * 
	 * @param idLancamento - id do lancamento a ser adicionado
	 * @param selecionado - true(adiciona a lista) false(remove da lista)
	 */
	@Post
	public void selecionarItem(Long idCota, Boolean selecionado) {
		
		@SuppressWarnings("unchecked")
		List<Long> selecionados = (List<Long>) session.getAttribute("selecionados");
		
		if(selecionados == null) {
			
			selecionados = new ArrayList<Long>();			
		}

		int index = selecionados.indexOf(idCota); 
		
		if(index==-1) {
			selecionados.add(idCota);
		} else {
			selecionados.remove(index);
		}
		
		session.setAttribute("selecionados", selecionados);
		
		result.use(Results.json()).withoutRoot().from(selecionado).recursive().serialize();
	}
	
	            /**
     * Adiciona ou remove todos os itens da pesquisa a lista de itens
     * selecionados da sessão.
     * 
     * @param selecionado - true(adiciona todos) false (remove todos)
     */	
	@Post
	public void selecionarTodos(Boolean selecionado){
		
		if(selecionado==false) {
			session.setAttribute("selecionados", null);
		} else {
			
			List<CotaSuspensaoDTO> lista= cotaService.obterDTOCotasSujeitasSuspensao("","",null, null);
			
			@SuppressWarnings("unchecked")
			List<Long> selecionados = (List<Long>) session.getAttribute("selecionados");
			
			if(selecionados==null) {
				selecionados = new ArrayList<Long>();
			}
			
			for ( CotaSuspensaoDTO cota : lista ) {
								
				selecionados.add(cota.getIdCota());
			}
			
			session.setAttribute("selecionados", selecionados);
		}
		
		result.use(Results.json()).withoutRoot().from(selecionado).recursive().serialize();
	}
	
	@SuppressWarnings("unchecked")
	@Post
	@Rules(Permissao.ROLE_FINANCEIRO_SUSPENSAO_COTA_ALTERACAO)
	public void suspenderCotas() {
		
		TipoMensagem status =  TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();
		
		List<CotaSuspensaoDTO> cotasExigemContrato = null;
		
		List<Long> idCotas = (List<Long>) session.getAttribute("selecionados");
		
		if(idCotas == null || idCotas.size()==0) {
			mensagens.add(NENHUM_REGISTRO_SELECIONADO);
			status =  TipoMensagem.WARNING;
		} else {
			
			try {
				 cotasExigemContrato = cotaService.suspenderCotasGetDTO(idCotas, getUsuarioLogado().getId());
				 
				 session.setAttribute("selecionados", null);
			} catch (InvalidParameterException e) {
				
				mensagens.add(WARNING_SUSPENSAO_COTA_SUSPENSA);
				LOGGER.error(WARNING_SUSPENSAO_COTA_SUSPENSA, e);
				status = TipoMensagem.WARNING;
				
			} catch(Exception e ) {
				mensagens.add(ERRO_SUSPENDER_COTAS);
				LOGGER.error(ERRO_SUSPENDER_COTAS, e);
				status = TipoMensagem.ERROR;
			}
		}
		
		if(cotasExigemContrato==null) {
			cotasExigemContrato = new ArrayList<CotaSuspensaoDTO>();
		}
		
		Object[] retorno = new Object[3];
		retorno[0] = cotasExigemContrato;
		retorno[1] = mensagens;
		retorno[2] = status.name();
		
		result.use(Results.json()).from(retorno, "result").serialize();
	}
		
	@Exportable
	public class RodapeDTO {
		@Export(label="Total de Cotas Sugeridas:", alignWithHeader="Nome")
		private Integer qtde;
		@Export(label="Total R$:", alignWithHeader="Divida Acumulada R$")
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
     * Exporta os dados da pesquisa.
     * 
     * @param fileType - tipo de arquivo
     * 
     * @throws IOException Exceção de E/S
     */
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		PaginacaoVO paginacao = (PaginacaoVO) this.session.getAttribute(PAGINACAO_SESSION_ATTRIBUTE);
		
		String sortOrder = null, sortColumn = null;

		if (paginacao != null) {
			
			sortColumn = paginacao.getSortColumn();
			sortOrder = paginacao.getSortOrder();
		}
				
		List<CotaSuspensaoDTO> listaDividasGeradas = cotaService.obterDTOCotasSujeitasSuspensao(sortOrder, sortColumn, null, null);
		
		FileExporter.to("suspensao_cota", fileType).inHTTPResponse(this.getNDSFileHeader(), null,  
				listaDividasGeradas, CotaSuspensaoDTO.class, this.httpResponse);
		
		result.nothing();
	}

}
