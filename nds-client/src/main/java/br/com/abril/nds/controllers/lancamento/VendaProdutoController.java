package br.com.abril.nds.controllers.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.VendaProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/lancamento/vendaProduto")
public class VendaProdutoController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroVendaProduto";
	
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private VendaProdutoService vendaProdutoService;
	
	public VendaProdutoController(Result result) {
		this.result = result;
	}
	
	
	@Path("/")
	public void index(){
		//this.carregarComboFornecedores();
	}
	
	
	@Post
	@Path("/pesquisarVendaProduto")
	public void pesquisarVendaProduto(FiltroVendaProdutoDTO filtro, String sortorder, String sortname, int page, int rp) {
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<VendaProdutoDTO>> tableModel = efetuarConsulta(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<VendaProdutoDTO>> efetuarConsulta(FiltroVendaProdutoDTO filtro) {
		 
		List<VendaProdutoDTO> listaVendaProdutoDTO =  this.vendaProdutoService.buscaVendaporProduto(filtro);				
		
		//Integer totalRegistros = lancamentoParcialService.totalBuscaLancamentosParciais(filtro);
		
		TableModel<CellModelKeyValue<VendaProdutoDTO>> tableModel = new TableModel<CellModelKeyValue<VendaProdutoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaVendaProdutoDTO));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(listaVendaProdutoDTO.size());
		
		return tableModel;
	}
	
	@Post
	@Path("/pesquisarLancamentoEdicao")
	public void pesquisarLancamentoEdicao(FiltroVendaProdutoDTO filtro,String sortorder, String sortname, int page, int rp){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<VendaProdutoDTO>> tableModel = efetuarConsulta(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}


	private void validarEntrada(FiltroVendaProdutoDTO filtro) {
		
		if(filtro.getCodigo()==null || filtro.getCodigo().trim().isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "Código é obrigatório.");		
		if(filtro.getNomeProduto()==null || filtro.getNomeProduto().trim().isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto é obrigatório.");
		
	}
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private void carregarComboFornecedores() {
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresAtivos();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		result.include("listaFornecedores",listaFornecedoresCombo );
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroVendaProdutoDTO filtroAtual) {

		FiltroVendaProdutoDTO filtroSession = (FiltroVendaProdutoDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}

}
