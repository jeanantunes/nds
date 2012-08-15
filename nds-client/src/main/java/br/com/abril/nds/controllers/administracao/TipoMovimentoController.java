package br.com.abril.nds.controllers.administracao;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.TipoMovimentoDTO;
import br.com.abril.nds.dto.TipoMovimentoDTO.IncideDivida;
import br.com.abril.nds.dto.filtro.FiltroTipoMovimento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.TipoMovimentoService;
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
	private TipoMovimentoService tipoMovimentoService;
		
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
		
		List<TipoMovimentoDTO> listaTipoMovimento = tipoMovimentoService.obterTiposMovimento(filtro);
		
		for(TipoMovimentoDTO tipo:listaTipoMovimento) {
			if(IncideDivida.SIM.equals(tipo.getIncideDividaValue()) && tipo.getPermiteAlteracao())
				tipo.setPermiteAlteracao(this.isUsuarioNivelGerencial());
		}
		
		Integer totalRegistros = tipoMovimentoService.countObterTiposMovimento(filtro);
		
		TableModel<CellModelKeyValue<TipoMovimentoDTO>> tableModel = new TableModel<CellModelKeyValue<TipoMovimentoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaTipoMovimento));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	/**
	 * Grava o Tipo de Movimento no banco de dados
	 * 
	 * @param tipoMovimentoDTO
	 */
	@Post
	public void salvarTipoMovimento(TipoMovimentoDTO tipoMovimentoDTO) {
		
		tipoMovimentoService.salvarTipoMovimento(tipoMovimentoDTO);
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();
	}	
	
	/**
	 * Altera o Tipo de Movimento no banco de dados
	 * 
	 * @param tipoMovimentoDTO
	 */
	@Post
	public void alterarTipoMovimento(TipoMovimentoDTO tipoMovimentoDTO) {
						
		tipoMovimentoService.editarTipoMovimento(tipoMovimentoDTO, this.getUsuario());
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();
	}
	
	/**
	 * Exclui Tipo de Movimento do banco de dados
	 * 
	 * @param codigo
	 */
	@Post
	public void excluirTipoMovimento(Long codigo) {		
		
		tipoMovimentoService.excluirTipoMovimento(codigo, this.getUsuario());
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();
	}
	
	/**
	 * Inicializa dados da tela
	 */
	@Rules(Permissao.ROLE_ADMINISTRACAO_TIPO_MOVIMENTO)
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

	//TODO Verificar se usuário possui nível gerencial
	private boolean isUsuarioNivelGerencial() {
		return false;
	}
	
	//TODO Obter Usuário logado
	private Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		return usuario;
	}
}
