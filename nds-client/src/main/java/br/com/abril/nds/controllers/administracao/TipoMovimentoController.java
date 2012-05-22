package br.com.abril.nds.controllers.administracao;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TipoMovimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoMovimento;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/tipoMovimento")
public class TipoMovimentoController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroTipoMovimento";
	
	@Autowired
	private HttpSession session;
		
	@Autowired
	private Result result;
	
	public void tipoMovimento() {
		
	}
	
	
	@Post
	public void pesquisarTipoMovimento(FiltroTipoMovimento filtro, Integer page, Integer rp, String sortname, String sortorder) {
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<TipoMovimentoDTO>> tableModel = efetuarConsulta(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	/**
	 * Efetua a consulta e monta a estrutura do grid de tipos de movimento.
	 * @param filtro
	 * @return 
	 */	
	private TableModel<CellModelKeyValue<TipoMovimentoDTO>> efetuarConsulta(FiltroTipoMovimento filtro) {
		
		//TODO getRealTipos
		List<TipoMovimentoDTO> listaTipoMovimento = new ArrayList<TipoMovimentoDTO>();//lancamentoParcialService.buscarLancamentosParciais(filtro);
		listaTipoMovimento.add(new TipoMovimentoDTO(1L,"Tipo1","ESTOQUE", "DEBITO","SIM","-"));
		listaTipoMovimento.add(new TipoMovimentoDTO(1L,"Tipo2","FINANCEIRO", "CREDITO","NAO","-"));
		listaTipoMovimento.add(new TipoMovimentoDTO(1L,"Tipo3","ESTOQUE", "DEBITO","SIM","-"));
		listaTipoMovimento.add(new TipoMovimentoDTO(1L,"Tipo4","FINANCEIRO", "CREDITO","NAO","-"));
		listaTipoMovimento.add(new TipoMovimentoDTO(1L,"Tipo5","ESTOQUE", "DEBITO","SIM","-"));
		
		//TODO getRealQtde
		Integer totalRegistros = 5;//lancamentoParcialService.totalBuscaLancamentosParciais(filtro);
		
		TableModel<CellModelKeyValue<TipoMovimentoDTO>> tableModel = new TableModel<CellModelKeyValue<TipoMovimentoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaTipoMovimento));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	/**
	 * Inicializa dados da tela
	 */
	public void index() {
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);
		
		result.forwardTo(TipoMovimentoController.class).tipoMovimento();
	}
		


	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroTipoMovimento filtroAtual) {

		FiltroTipoMovimento filtroSession = (FiltroTipoMovimento) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}
}
