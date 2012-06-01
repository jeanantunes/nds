package br.com.abril.nds.controllers.expedicao;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.TipoMovimentoDTO;
import br.com.abril.nds.dto.TipoMovimentoDTO.IncideDivida;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO.ColunaOrdenacao;
import br.com.abril.nds.dto.filtro.FiltroTipoMovimento;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/mapaAbastecimento")
public class MapaAbastecimentoController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroMapaAbastecimento";
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private Result result;
	
	public void mapaAbastecimento() {
		
	}
	
	@Post
	public void pesquisar(FiltroMapaAbastecimentoDTO filtro, Integer page, Integer rp, String sortname, String sortorder) {
		
		if(filtro.getDataLancamento() == null || filtro.getDataLancamento().isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "'Data de Lançamento' é obrigatória.");
				
		if(DateUtil.isValidDatePTBR(filtro.getDataLancamento()))
			throw new ValidacaoException(TipoMensagem.WARNING, "'Data de Lançamento' não é válida.");
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder));
		filtro.setColunaOrdenacao(ColunaOrdenacao.getPorDescricao(sortname));
		tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<AbastecimentoDTO>> tableModel = efetuarConsulta(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
		
	@Post
	public void pesquisarDetalhes(String sortname, String sortorder) {
		
		//FiltroMapaAbastecimentoDTO filtro = (FiltroMapaAbastecimentoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		

		//TODO getRealList
		List<ProdutoAbastecimentoDTO> lista = new ArrayList<ProdutoAbastecimentoDTO>();
		lista.add(new ProdutoAbastecimentoDTO("1","1",1L,1,"1","1"));
		lista.add(new ProdutoAbastecimentoDTO("1","1",1L,1,"1","1"));
		lista.add(new ProdutoAbastecimentoDTO("1","1",1L,1,"1","1"));
		lista.add(new ProdutoAbastecimentoDTO("1","1",1L,1,"1","1"));
		lista.add(new ProdutoAbastecimentoDTO("1","1",1L,1,"1","1"));
		
		
		//TODO getRealCount
		Integer totalRegistros = lista.size();
		
		result.use(FlexiGridJson.class).from(lista).page(1).total(totalRegistros).serialize();
	}
	
	/**
	 * Efetua a consulta e monta a estrutura do grid de tipos de movimento.
	 * @param filtro
	 * @return 
	 */	
	private TableModel<CellModelKeyValue<AbastecimentoDTO>> efetuarConsulta(FiltroMapaAbastecimentoDTO filtro) {
		
		//TODO getRealList
		List<AbastecimentoDTO> lista = new ArrayList<AbastecimentoDTO>();
		lista.add(new AbastecimentoDTO("", 1L, "1", 1, 1, "1"));
		lista.add(new AbastecimentoDTO("", 1L, "1", 1, 1, "1"));
		lista.add(new AbastecimentoDTO("", 1L, "1", 1, 1, "1"));
		lista.add(new AbastecimentoDTO("", 1L, "1", 1, 1, "1"));
		
		//TODO getRealCount
		Integer totalRegistros = 10;
		
		TableModel<CellModelKeyValue<AbastecimentoDTO>> tableModel = new TableModel<CellModelKeyValue<AbastecimentoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(lista));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	/**
	 * Efetua a consulta e monta a estrutura do grid de tipos de movimento.
	 * @param filtro
	 * @return 
	 */	
	private TableModel<CellModelKeyValue<ProdutoAbastecimentoDTO>> efetuarConsultaDetalhes(Long idBox) {
		
		//TODO getRealList
		List<ProdutoAbastecimentoDTO> lista = new ArrayList<ProdutoAbastecimentoDTO>();
		lista.add(new ProdutoAbastecimentoDTO("1","1",1L,1,"1","1"));
		lista.add(new ProdutoAbastecimentoDTO("1","1",1L,1,"1","1"));
		lista.add(new ProdutoAbastecimentoDTO("1","1",1L,1,"1","1"));
		lista.add(new ProdutoAbastecimentoDTO("1","1",1L,1,"1","1"));
		lista.add(new ProdutoAbastecimentoDTO("1","1",1L,1,"1","1"));
		
		//TODO getRealCount
		Integer totalRegistros = 10;
		
		TableModel<CellModelKeyValue<ProdutoAbastecimentoDTO>> tableModel = new TableModel<CellModelKeyValue<ProdutoAbastecimentoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(lista));
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroMapaAbastecimentoDTO filtroAtual) {

		FiltroMapaAbastecimentoDTO filtroSession = (FiltroMapaAbastecimentoDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}
	
	/**
	 * Inicializa dados da tela
	 */
	public void index() {
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);
		
		result.forwardTo(MapaAbastecimentoController.class).mapaAbastecimento();
	}
}
