package br.com.abril.nds.controllers.distribuicao;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaQueRecebeClassificacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.ClassificacaoNaoRecebidaService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/classificacaoNaoRecebida")
public class ClassificacaoNaoRecebidaController extends BaseController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroClassificacaoNaoRecebidaDTO";
	
	private static final ValidacaoVO VALIDACAO_VO_SUCESSO = new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso.");
	
	@Autowired
	private Result result;
	
	@Autowired
	private TipoClassificacaoProdutoService tipoClassificacaoProdutoService;
	
	@Autowired
	private ClassificacaoNaoRecebidaService classificacaoNaoRecebidaService; 
	
	@Autowired
	private HttpSession session;
	
	@Rules(Permissao.ROLE_DISTRIBUICAO_CLASSIFICACAO_NAO_RECEBIDA)
	public void index(){
		this.carregarComboClassificacao();
	}
	
	@Post
	public void pesquisarClassificacaoNaoRecebida(FiltroClassificacaoNaoRecebidaDTO filtro, String sortorder, String sortname, int page, int rp ){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		validarEntradaFiltroClassificacao(filtro);
		
		List<CotaQueRecebeClassificacaoDTO> listaCotaQueRecebeClassificacaoDTO = this.classificacaoNaoRecebidaService.obterCotasQueRecebemClassificacao(filtro);

		guardarFiltroNaSession(filtro);
		
		TableModel<CellModelKeyValue<CotaQueRecebeClassificacaoDTO>> tableModel = new TableModel<CellModelKeyValue<CotaQueRecebeClassificacaoDTO>>();
		
		configurarTableModelComPaginacao(listaCotaQueRecebeClassificacaoDTO, tableModel, filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Post
	public void excluirClassificacaoNaoRecebida(Long id){
		this.classificacaoNaoRecebidaService.excluirClassificacaoNaoRecebida(id);
		
		result.use(Results.json()).from(VALIDACAO_VO_SUCESSO,"result").recursive().serialize();
	}
	
	
	private void validarEntradaFiltroCota(FiltroClassificacaoNaoRecebidaDTO filtro) {
		if((filtro.getCotaDto().getNumeroCota() == null || filtro.getCotaDto().getNumeroCota() == 0) && 
				(filtro.getCotaDto().getNomePessoa() == null || filtro.getCotaDto().getNomePessoa().trim().isEmpty()))
			throw new ValidacaoException(TipoMensagem.WARNING, "Código ou nome da cota é obrigatório.");		
	}
	
	private void validarEntradaFiltroClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro) {
		if(filtro.getIdTipoClassificacaoProduto() == null || filtro.getIdTipoClassificacaoProduto() == 0)
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma Classificação foi selecionada.");		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	
	private void guardarFiltroNaSession(FiltroClassificacaoNaoRecebidaDTO filtro) {
		
		FiltroClassificacaoNaoRecebidaDTO filtroSession = (FiltroClassificacaoNaoRecebidaDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)){
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	private void carregarComboClassificacao() {

		List<TipoClassificacaoProduto> listaTipoClassificacaoProduto = tipoClassificacaoProdutoService.obterTodos();

		List<ItemDTO<Long, String>> listaTipoClassificacaoProdutoCombo = new ArrayList<ItemDTO<Long, String>>();

		for (TipoClassificacaoProduto tipoClassificacaoProduto : listaTipoClassificacaoProduto) {

			// Preenchendo a lista que irá representar o combobox de área de
			// influência na view
			listaTipoClassificacaoProdutoCombo.add(new ItemDTO<Long, String>(tipoClassificacaoProduto.getId(), tipoClassificacaoProduto.getDescricao()));
		}

		result.include("listaTipoClassificacao", listaTipoClassificacaoProdutoCombo);
	}
	
}
