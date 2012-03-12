package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
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
	protected static final int RESULTADOS_POR_PAGINA_INCIAL = 15;
	
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
		
		TipoMensagem status = TipoMensagem.SUCCESS;
		List<String> mensagens = new ArrayList<String>();
		TableModel<CellModelKeyValue<CotaSuspensaoDTO>> grid = null;		
		
		try {
			
			verificarBaixaBancariaNaData();
			
			grid = obterCotasSuspensao(1,RESULTADOS_POR_PAGINA_INCIAL,"cota","asc");
			
		} catch(ValidacaoException e) {
			mensagens = e.getValidacao().getListaMensagens();
			status=e.getValidacao().getTipoMensagem();
		}
		
		result.include("grid",grid);
		result.include("msg",mensagens);
		result.include("status",status.name());		
		
		result.forwardTo(SuspensaoCotaController.class).suspensaoCota();
	}

	private void verificarBaixaBancariaNaData() {
		//throw new BaixaBancariaInexistenteException();		
	}
	
	public void obterCotasSuspensaoJSON(Integer page, Integer rp, String sortname, 
			String sortorder) {
		
		Object[] retorno = new Object[3];
		retorno[0] = obterCotasSuspensao(page,rp,sortname,sortorder);
		retorno[1] = new ArrayList<String>();
		retorno[2] = TipoMensagem.SUCCESS.name();
		
		result.use(Results.json()).withoutRoot().from(retorno).serialize();	
	}
	
	
	public List<CotaSuspensaoDTO> getListaCotaSuspensao() {

		List<CotaSuspensaoDTO> listaCotaSuspensao = new ArrayList<CotaSuspensaoDTO>();
		listaCotaSuspensao.add(new CotaSuspensaoDTO(
				1L, 
				"Guilherme de Morais Leandro", 
				50.0, 
				60.0, 
				true, 
				null));	
		
		return listaCotaSuspensao;
	}
	
	private TableModel<CellModelKeyValue<CotaSuspensaoDTO>> obterCotasSuspensao(Integer page, Integer rp, String sortname, 
			String sortorder) {
		
		//TODO getRealList()
		List<CotaSuspensaoDTO> listaCotaSuspensao = getListaCotaSuspensao();
		//TODO getRealSize()
		Integer total = listaCotaSuspensao.size();
		
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
		grid.setTotal(total);
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
	public void selecionarItem(Long idLancamento, Boolean selecionado) {
		
		@SuppressWarnings("unchecked")
		List<Long> selecionados = (List<Long>) session.getAttribute("selecionados");
		
		if(selecionados == null) {
			
			selecionados = new ArrayList<Long>();			
		}

		int index = selecionados.indexOf(idLancamento); 
		
		if(index==-1) {
			selecionados.add(idLancamento);
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
			List<CotaSuspensaoDTO> lista= getListaCotaSuspensao();
			
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
	
}
