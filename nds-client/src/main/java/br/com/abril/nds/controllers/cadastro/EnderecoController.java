package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pela lógica de visualização 
 * referentes a funcionalidade de cadastro de endereços. 
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/cadastro/endereco")
public class EnderecoController {

	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;

	@Path("/")
	public void index() { }

	/**
	 * Constante que representa o nome do atributo com a lista de endereços 
	 * armazenado na sessão para serem persistidos na base. 
	 */
	private static final String LISTA_ENDERECOS_SALVAR_SESSAO = "listaEnderecosSalvarSessao";
	
	/**
	 * Constante que representa o nome do atributo com a lista de endereços 
	 * armazenado na sessão para serem removidos da base. 
	 */
	private static final String LISTA_ENDERECOS_REMOVER_SESSAO = "listaEnderecosRemoverSessao";

	/**
	 * Método que realiza a pesquisa dos endereços cadastrados para uma determinada pessoa.
	 */
	@Post
	public void pesquisarEnderecos() {

		TableModel<CellModel> tableModelEndereco = getTableModelListaEndereco(getListaEndereco());

		session.setAttribute(LISTA_ENDERECOS_SALVAR_SESSAO, tableModelEndereco);

		result.use(Results.json()).withoutRoot().from(tableModelEndereco).recursive().serialize();
	}

	/**
	 * Método responsável por incluir um novo endereço para a pessoa em questão.
	 * 
	 * @param endereco
	 * @param isEnderecoPrincipal
	 */
	@Post
	@SuppressWarnings("unchecked")
	public void incluirNovoEndereco(Endereco endereco, boolean isEnderecoPrincipal) {
		
		TableModel<CellModel> tableModelEndereco = 
				(TableModel<CellModel>) session.getAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);

		CellModel cellModel = getCellModelEndereco(endereco);

		tableModelEndereco.getRows().add(cellModel);

		session.setAttribute(LISTA_ENDERECOS_SALVAR_SESSAO, tableModelEndereco);

		result.use(Results.json()).withoutRoot().from(tableModelEndereco).recursive().serialize();
	}

	@Post
	public void removerEndereco(Long idEndereco) {
		
	}
	
	/**
	 * Método que retorna um Table Model de acordo com a lista de Endereços desejada.
	 * 
	 * @param listaEndereco
	 * @return TableModel<CellModel>
	 */
	private TableModel<CellModel> getTableModelListaEndereco(List<Endereco> listaEndereco) {

		TableModel<CellModel> tableModel = new TableModel<CellModel>();

		List<CellModel> listaCellModel = new ArrayList<CellModel>();

		for (Endereco endereco : listaEndereco) {
			
			CellModel cellModel = getCellModelEndereco(endereco);

			listaCellModel.add(cellModel);
		}

		tableModel.setPage(1);
		tableModel.setRows(listaCellModel);
		tableModel.setTotal(listaCellModel.size()); 

		return tableModel;
	}

	/**
	 * Método que retorna um Cell Model de acordo com o Endereço desejado.
	 * 
	 * @param endereco
	 * @return CellModel
	 */
	private CellModel getCellModelEndereco(Endereco endereco) {

		return new CellModel(
					  endereco.getId().intValue(),
					  //endereco.getTipoEndereco().toString(),
					  endereco.getLogradouro(), 
					  endereco.getBairro(),
					  endereco.getCep(), 
					  endereco.getCidade(), 
					  "true"
				   );
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	private List<Endereco> getListaEndereco() {

		List<Endereco> listaEndereco = new ArrayList<Endereco>();

		Endereco endereco = new Endereco();

		endereco.setBairro("Vila Carvalho");
		endereco.setCep("13735-430");
		endereco.setCidade("Mococa");
		endereco.setId(1L);
		endereco.setLogradouro("Capitão José Cristovam de Lima");
		endereco.setNumero(5);
		//endereco.setTipoEndereco(TipoEndereco.RESIDENCIAL);
		endereco.setUf("SP");

		listaEndereco.add(endereco);

		endereco = new Endereco();

		endereco.setBairro("Vila Carvalho");
		endereco.setCep("13735-430");
		endereco.setCidade("Mococa");
		endereco.setId(2L);
		endereco.setLogradouro("Capitão José Cristovam de Lima");
		endereco.setNumero(5);
		//endereco.setTipoEndereco(TipoEndereco.RESIDENCIAL);
		endereco.setUf("SP");

		listaEndereco.add(endereco);

		endereco = new Endereco();

		endereco.setBairro("Vila Carvalho");
		endereco.setCep("13735-430");
		endereco.setCidade("Mococa");
		endereco.setId(3L);
		endereco.setLogradouro("Capitão José Cristovam de Lima");
		endereco.setNumero(5);
		//endereco.setTipoEndereco(TipoEndereco.RESIDENCIAL);
		endereco.setUf("SP");

		listaEndereco.add(endereco);

		return listaEndereco;
	}
}
