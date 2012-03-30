package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.DividaDTO;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.BaixaBancariaSerivice;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class SuspensaoCotaController {

	protected static final String MSG_PESQUISA_SEM_RESULTADO = "Não há sugestões para suspensão de cota.";
	protected static final String NENHUM_REGISTRO_SELECIONADO = "Nenhum registro foi selecionado!";
	protected static final String SEM_BAIXA_NA_DATA = "Não foi feita baixa bancária na data de operação atual.";
	protected static final int RESULTADOS_POR_PAGINA_INCIAL = 15;
	protected static final String ERRO_CARREGAR_SUGESTAO_SUSPENSAO = "Erro inesperado ao carregar sugestão de suspensão de cotas.";
	protected static final String ERRO_SUSPENDER_COTAS = "Erro inesperado ao suspender cotas.";
	private static final String FORMATO_DATA = "dd/MM/yyyy";
	
	private static final Logger LOG = LoggerFactory
			.getLogger(SuspensaoCotaController.class);
	
	private final Result result;
	private final HttpSession session;
	private BaixaBancariaSerivice baixaBancariaSerivice;
	private CotaService cotaService;
	
	public SuspensaoCotaController(Result result,HttpSession session,
			CotaService cotaService, BaixaBancariaSerivice baixaBancariaSerivice) {
		
		this.result = result;
		this.session = session;
		this.cotaService = cotaService;
		this.baixaBancariaSerivice = baixaBancariaSerivice;
	}
	
	public void suspensaoCota() {
		
	}
	
	/**
	 * Inicializa dados da tela
	 */
	public void index() {
		
		session.setAttribute("selecionados",null);
		
		result.forwardTo(SuspensaoCotaController.class).suspensaoCota();
	}

	private void verificarBaixaBancariaNaData() {
				
		boolean existeBaixa = baixaBancariaSerivice.verificarBaixaBancariaNaData(new Date());
		
		if ( !existeBaixa ) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, SEM_BAIXA_NA_DATA));
		}
	}
	
	public void obterCotasSuspensaoJSON(Integer page, Integer rp, String sortname, 
			String sortorder) {
		
		TipoMensagem status = TipoMensagem.SUCCESS;
		List<String> mensagens = new ArrayList<String>();
	
		TableModel<CellModelKeyValue<CotaSuspensaoDTO>> grid = null;
		
		try {
			verificarBaixaBancariaNaData();
			
			grid = obterCotasSuspensao(sortname,sortorder,page,rp);			
				
		} catch(ValidacaoException e) {
			mensagens = e.getValidacao().getListaMensagens();
			status = e.getValidacao().getTipoMensagem();
		} catch(Exception e) {			
			mensagens.add(ERRO_CARREGAR_SUGESTAO_SUSPENSAO);
			LOG.error(ERRO_CARREGAR_SUGESTAO_SUSPENSAO, e);
			status = TipoMensagem.ERROR;
		}
		
		if(grid == null) {
			grid = new TableModel<CellModelKeyValue<CotaSuspensaoDTO>>();
		}
		
		Object[] retorno = new Object[3];
		retorno[0] = grid;
		retorno[1] = mensagens;
		retorno[2] = status.name();
		
		result.use(Results.json()).withoutRoot().from(retorno).serialize();	
	}
	
	public void getInadinplenciasDaCota(Long idCota) {
		
		List<Cobranca> cobrancas = cotaService.obterCobrancasDaCotaEmAberto(idCota);
		
		List<DividaDTO> dividas = new ArrayList<DividaDTO>();
		
		for(Cobranca cobranca : cobrancas) {
			dividas.add(new DividaDTO(
					DateUtil.formatarData(cobranca.getDataVencimento(), FORMATO_DATA), 
					CurrencyUtil.formatarValor(cobranca.getValor())));
		}
		result.use(Results.json()).from(dividas, "result").serialize();
	}
	
	private TableModel<CellModelKeyValue<CotaSuspensaoDTO>> obterCotasSuspensao(String sortname, 
			String sortorder, Integer page, Integer rp) {
		
		Long total = cotaService.obterTotalCotasSujeitasSuspensao();
		Integer inicio = (page-1)*rp;
		if(total.equals(0)) {
			
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
	 * Adiciona ou remove todos os itens da pesquisa a lista de itens selecionados da sessão.
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
				 cotasExigemContrato = cotaService.suspenderCotasGetDTO(idCotas, getUsuario().getId());
				 
			} catch(Exception e ) {
				mensagens.add(ERRO_SUSPENDER_COTAS);
				LOG.error(ERRO_SUSPENDER_COTAS, e);
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
	
	//TODO getRealUsuario
	public Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setLogin("fakeUsuario");
		usuario.setNome("Fake Usuario");
		return usuario;
	}
}
