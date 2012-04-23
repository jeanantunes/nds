package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO.ColunaOrdenacao;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class InadimplenciaController {


	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroInadimplencia";
	
	private static final String ERRO_PESQUISAR_INADIMPLENCIAS = "Erro inesperado ao pesquisar inadimplencias.";	
	private static final String WARNING_PESQUISA_SEM_RESULTADO = "Não há resultados para a pesquisa realizada.";

	
	@Autowired
	
	private static final Logger LOG = LoggerFactory
			.getLogger(InadimplenciaController.class);
	
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
	public void index() {
		gerarListaStatus();
		result.forwardTo(InadimplenciaController.class).inadimplencia();
	}

	private void gerarListaStatus() {
		
		List<ItemDTO<Integer, String>> status = new ArrayList<ItemDTO<Integer,String>>();
				
		for(SituacaoCadastro situacao : SituacaoCadastro.values()) {
			status.add(new ItemDTO<Integer, String>(situacao.ordinal(), situacao.toString()));
		}
		
		result.include("itensStatus", status);
		
	}
	
	public void pesquisar(String periodoDe, String periodoAte, String nomeCota, Integer numCota, String statusDivida, String situacao) {
		
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();

		TableModel<CellModelKeyValue<StatusDividaDTO>> grid = null;
		
		try {
			
			FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		//	filtro.setPaginacao(new PaginacaoVO(page,rp,sortorder));
		
			tratarFiltro(filtro);
			
			grid = obterInadimplencias(filtro);
		
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
		
		Object[] retorno = new Object[3];
		retorno[0] = grid;
		retorno[1] = mensagens;
		retorno[2] = status.name();
	
		result.use(Results.json()).withoutRoot().from(retorno).recursive().serialize();
	}
	

	private TableModel<CellModelKeyValue<StatusDividaDTO>> obterInadimplencias(
			FiltroCotaInadimplenteDTO filtro) {
		
		List<StatusDividaDTO> listaInadimplencias = new ArrayList<StatusDividaDTO>();
		
		try {
			
			for(Integer i = 0; i<50; i++) {
				listaInadimplencias.add(new StatusDividaDTO(
						i.longValue(),i,"Nome"+i,"Status"+i,i+",00","10/10/"+i,"10/10/"+i,"Situacao"+i, i+",00", i.longValue()));
			}
					
			//TODO - implementar
			//listaInadimplencias = cotaAusenteService.obterCotasAusentes(filtro) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (listaInadimplencias == null || listaInadimplencias.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, WARNING_PESQUISA_SEM_RESULTADO);
		}
		
		Long totalRegistros = 50L;//cotaAusenteService.obterCountCotasAusentes(filtro);
		
		TableModel<CellModelKeyValue<StatusDividaDTO>> tableModel = new TableModel<CellModelKeyValue<StatusDividaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaInadimplencias));
		
		tableModel.setPage(1);
		
		tableModel.setTotal( (totalRegistros == null)?0:totalRegistros.intValue());
		
		return tableModel;
	}

	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroCotaInadimplenteDTO filtro) {

		FiltroCotaInadimplenteDTO filtroSession = (FiltroCotaInadimplenteDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) {

			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
}
