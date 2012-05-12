package br.com.abril.nds.controllers.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/parciais")
public class ParciaisController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroParcial";
	
	@Autowired
	private HttpSession session;
		
	@Autowired
	private Result result;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private ProdutoService produtoService;
	
	public ParciaisController(Result result){
		this.result = result;
	}
	
	public void parciais() {
		
	}
	
	/**
	 * Inicializa dados da tela
	 */
	public void index() {
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);	
		
		carregarComboFornecedores();
		
		carregarComboStatus();
		
		result.forwardTo(ParciaisController.class).parciais();
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
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private void carregarComboStatus() {
				
		List<ItemDTO<String, String>> listaComboStatus = new ArrayList<ItemDTO<String,String>>();
		
		for (StatusLancamentoParcial status : StatusLancamentoParcial.values()) {
			listaComboStatus.add(new ItemDTO<String, String>( status.name(), status.toString() ));
		}
		
		result.include("listaStatus",listaComboStatus );
	}
	
	/**
	 * Pesquisa de parciais
	 */
	@Post
	public void pesquisarParciais(FiltroParciaisDTO filtro) {
			
		validarEntrada(filtro);
		
		TableModel<CellModelKeyValue<ParcialDTO>> grid = null;
		
		grid = efetuarConsulta(null);
		
		result.use(Results.json()).withoutRoot().from(grid).recursive().serialize();
		
	}
	
	private void validarEntrada(FiltroParciaisDTO filtro) {
		
		if(filtro.getDataInicial()!=null && !filtro.getDataInicial().trim().isEmpty() && !DateUtil.isValidDatePTBR(filtro.getDataInicial()))
			throw new ValidacaoException(TipoMensagem.WARNING, "Data inicial pesquisada é inválida.");
		
		if(filtro.getDataFinal()!=null && !filtro.getDataFinal().trim().isEmpty() && !DateUtil.isValidDatePTBR(filtro.getDataFinal()))
				throw new ValidacaoException(TipoMensagem.WARNING, "Data final pesquisada é inválida.");
		
		
	}

	/**
	 * Efetua a consulta e monta a estrutura do grid de parciais.
	 * @param filtro
	 */
	private TableModel<CellModelKeyValue<ParcialDTO>> efetuarConsulta(FiltroParciaisDTO filtro) {
		
		List<ParcialDTO> listaParciais = null;
		
		//TODO - listaParciais = obterListaReal();
		
		listaParciais = new ArrayList<ParcialDTO>();
		listaParciais.add(new ParcialDTO("a1", "a2", "a3", "a4", 5, "a6", "a7"));
		listaParciais.add(new ParcialDTO("b1", "b2", "b3", "b4", 5, "b6", "b7"));
		listaParciais.add(new ParcialDTO("c1", "c2", "c3", "c4", 5, "c6", "c7"));
		listaParciais.add(new ParcialDTO("d1", "d2", "d3", "d4", 5, "d6", "d7"));
		
		
		
		if (listaParciais == null || listaParciais.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não retornou resultados");
		}
		
		
		//TODO - Integer totalRegistros  = getRealTotal();
		Integer totalRegistros = 5;
		
		TableModel<CellModelKeyValue<ParcialDTO>> tableModel = new TableModel<CellModelKeyValue<ParcialDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaParciais));
		
		tableModel.setPage(1);
		
		tableModel.setTotal( (totalRegistros == null)?0:totalRegistros.intValue());
		
		return tableModel;
		
	}
	
	@Post
	public void pesquisarProdutoPorNumero(String codigoProduto) {
		
		if(codigoProduto == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Código do produto inválido!");
		}
		
		Produto produto = this.produtoService.obterProdutoPorCodigo(codigoProduto);

		if (produto == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Produto \"" + codigoProduto + "\" não encontrado!");
			
		} else {
			
			String nomeProduto = produto.getNome();
			
			result.use(Results.json()).withoutRoot().from(produto).recursive().serialize();
		}		
	}
}
