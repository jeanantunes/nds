package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.mapping.Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.controllers.expedicao.ConfirmacaoExpedicaoController;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.DividaDTO;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class SuspensaoCotaController {

	protected static final String MSG_PESQUISA_SEM_RESULTADO = "Não há resultados para a apesquisa realizada.";
	protected static final String NENHUM_REGISTRO_SELECIONADO = "Nenhum registro foi selecionado!";
	protected static final String SEM_BAIXA_NA_DATA = "Não foi feita baixa bancária na data de operação atual.";
	protected static final int RESULTADOS_POR_PAGINA_INCIAL = 15;
	protected static final String ERRO_CARREGAR_SUGESTAO_SUSPENSAO = "Erro inesperado ao carregar sugestão de suspensão de cotas.";
	
	private static final Logger LOG = LoggerFactory
			.getLogger(SuspensaoCotaController.class);
	
	private final Result result;
	private final HttpSession session;
	
	public SuspensaoCotaController(Result result,HttpSession session) {
		
		this.result = result;
		this.session = session;
	}
	
	public void suspensaoCota() {
		
	}
	
	/**
	 * Inicializa dados da tela
	 */
	public void index() {
	
		// TODO - REMOVER!
		session.setAttribute("baixa", true);
		
		result.forwardTo(SuspensaoCotaController.class).suspensaoCota();
	}

	//TODO REMOVER ENTRADA
	public void semBaixa() {
		
		session.setAttribute("baixa", false);
		
		result.forwardTo(SuspensaoCotaController.class).suspensaoCota();
	}
	
	private void verificarBaixaBancariaNaData() {
				
		if ( (Boolean)session.getAttribute("baixa") == false ) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, SEM_BAIXA_NA_DATA));
		} 
		
	}
	
	public void obterCotasSuspensaoJSON(Integer page, Integer rp, String sortname, 
			String sortorder) {
		
		TipoMensagem status = TipoMensagem.SUCCESS;
		List<String> mensagens = new ArrayList<String>();
		Long totalSugerida = 0L;		
		Double total = 0.0;
		
		TableModel<CellModelKeyValue<CotaSuspensaoDTO>> grid = null;
		
		try {
			verificarBaixaBancariaNaData();
			//TODO getRealSize()
			totalSugerida = 50L;
			//TODO getRealConsignado
			total = 50000.00;
			grid = obterCotasSuspensao(page,rp,sortname,sortorder, totalSugerida);			
				
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
		
		Object[] retorno = new Object[5];
		retorno[0] = grid;
		retorno[1] = mensagens;
		retorno[2] = status.name();
		retorno[3] = totalSugerida;
		retorno[4] = total;
		
		result.use(Results.json()).withoutRoot().from(retorno).serialize();	
	}
	
	
	//TODO obter dados reais
	public List<CotaSuspensaoDTO> getListaCotaSuspensao(Integer page, Integer rp, Long total) {

		List<CotaSuspensaoDTO> listaCotaSuspensao = new ArrayList<CotaSuspensaoDTO>();
	
		for(Integer i = page*rp ; i < (page*rp) + rp ; i++) {
								
			CotaSuspensaoDTO cotaS = new CotaSuspensaoDTO(
					i.longValue(), 
					"Pessoa" + i, 
					i*100.0, 
					i* 10.0, 
					false);
					//i%2==0);
			
			listaCotaSuspensao.add(cotaS);	
		}
		
		return listaCotaSuspensao;
	}


	//TODO obter dados reais
	public void getInadinplenciasDaCota(Long idCota) {
		
		List<DividaDTO> dividas = new ArrayList<DividaDTO>();
		dividas.add(new DividaDTO("10/10/2010", 100.0));
		dividas.add(new DividaDTO("10/10/2011", 200.0));
		
		result.use(Results.json()).from(dividas, "result").serialize();
	}
	
	private TableModel<CellModelKeyValue<CotaSuspensaoDTO>> obterCotasSuspensao(Integer page, Integer rp, String sortname, 
			String sortorder, Long total) {
		
		//TODO getRealList()
		List<CotaSuspensaoDTO> listaCotaSuspensao = getListaCotaSuspensao(page,rp,total);
		
		
		List<CellModelKeyValue<CotaSuspensaoDTO>> listaCelula = new LinkedList<CellModelKeyValue<CotaSuspensaoDTO>>();
		
		@SuppressWarnings("unchecked")
		List<Long> selecionados = (List<Long>) session.getAttribute("selecionados");
		
		for(CotaSuspensaoDTO cota : listaCotaSuspensao) {						
		
			if( selecionados!=null && selecionados.contains(cota.getCota()) ) {
				cota.setSelecionado(true);
			}
			
			listaCelula.add(new CellModelKeyValue<CotaSuspensaoDTO>(cota.getCota().intValue(),cota));			
		}
		
		if(listaCotaSuspensao.isEmpty()) {
			
			//TODO verificar mensagem
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
		
			Date date = (Date) session.getAttribute("date");
			Long idFornecedor = (Long) session.getAttribute("idFornecedor");
			Boolean estudo = (Boolean) session.getAttribute("estudo");
			
			//TODO getRealList();
			List<CotaSuspensaoDTO> lista= getListaCotaSuspensao(1,50,50L);
			
			@SuppressWarnings("unchecked")
			List<Long> selecionados = (List<Long>) session.getAttribute("selecionados");
			
			if(selecionados==null) {
				selecionados = new ArrayList<Long>();
			}
			
			for ( CotaSuspensaoDTO cota : lista ) {
								
				selecionados.add(cota.getCota());
			}
			
			session.setAttribute("selecionados", selecionados);
		}
		
		result.use(Results.json()).withoutRoot().from(selecionado).recursive().serialize();
	}
	
	@Post
	public void suspenderCototas() {
		System.out.println("GUI");
	}
	
}
