package br.com.abril.nds.controllers.distribuicao;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.sun.accessibility.internal.resources.accessibility;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ProdutoNaoRecebidoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.ExcecaoSegmentoParciaisService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Path("/distribuicao/excecaoSegmentoParciais")
@Resource()
public class ExcecaoSegmentoParciaisController extends BaseController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroExcecaoSegmentoParciaisDTO";
	
	@Autowired
	private ExcecaoSegmentoParciaisService excecaoSegmentoParciaisService;
	
	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;
	
	
	@Rules(Permissao.ROLE_DISTRIBUICAO_EXCECAO_SEGMENTO_PARCIAIS)
	public void index(){
		
	}
	
	@Post("/pesquisarProdutosNaoRecebidosPelaCota")
	public void pesquisarProdutosNaoRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro, String sortorder, String sortname, int page, int rp ){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		validarEntradaFiltroCota(filtro);
		
		List<ProdutoNaoRecebidoDTO> listaProdutoNaoRecebidoDto = this.excecaoSegmentoParciaisService.obterProdutosNaoRecebidosPelaCota(filtro);

		validarLista(listaProdutoNaoRecebidoDto);
		
		guardarFiltroNaSession(filtro);
		
		TableModel<CellModelKeyValue<ProdutoNaoRecebidoDTO>> tableModel = new TableModel<CellModelKeyValue<ProdutoNaoRecebidoDTO>>();
		
		configurarTableModelSemPaginacao(listaProdutoNaoRecebidoDto, tableModel);
		//configurarTableModelComPaginacao(listaProdutoNaoRecebidoDto, tableModel, filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post("autoCompletarPorNomeProdutoNaoRecebidoPelaCota")
	public void autoCompletarPorNomeProdutoNaoRecebidoPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro){
		List<ProdutoNaoRecebidoDTO> listaProdutosNaoRecebidosPelaCota = this.excecaoSegmentoParciaisService.obterProdutosNaoRecebidosPelaCota(filtro);
		
		List<ItemAutoComplete> listaProdutosNaoRecebidosPelaCotaAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (listaProdutosNaoRecebidosPelaCota != null && !listaProdutosNaoRecebidosPelaCota.isEmpty()) {
			
			for (ProdutoNaoRecebidoDTO produtoNaoRecebidoDTO : listaProdutosNaoRecebidosPelaCota) {
				
				listaProdutosNaoRecebidosPelaCotaAutoComplete.add(new ItemAutoComplete(produtoNaoRecebidoDTO.getNomeProduto(), null, produtoNaoRecebidoDTO));
			}
		}
		
		this.result.use(Results.json()).from(listaProdutosNaoRecebidosPelaCotaAutoComplete, "result").include("value", "chave").serialize();
	}
	
	private void validarEntradaFiltroCota(FiltroExcecaoSegmentoParciaisDTO filtro) {
		if((filtro.getCotaDto().getNumeroCota() == null || filtro.getCotaDto().getNumeroCota() == 0) && 
				(filtro.getCotaDto().getNomePessoa() == null || filtro.getCotaDto().getNomePessoa().trim().isEmpty()))
			throw new ValidacaoException(TipoMensagem.WARNING, "Código ou nome da cota é obrigatório.");		
	}
	
	@SuppressWarnings("rawtypes")
	private void validarLista(List list){
		if (list == null || list.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private TableModel configurarTableModelSemPaginacao( List listaDto, TableModel tableModel){
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDto));

		tableModel.setPage(1);

		tableModel.setTotal(listaDto.size());
		
		return tableModel;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private TableModel configurarTableModelComPaginacao( List listaDto, TableModel tableModel, FiltroDTO filtro){
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDto));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	
	
	private void guardarFiltroNaSession(FiltroExcecaoSegmentoParciaisDTO filtro) {
		
		FiltroExcecaoSegmentoParciaisDTO filtroSession = (FiltroExcecaoSegmentoParciaisDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)){
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
}
