package br.com.abril.nds.controllers.devolucao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.FornecedorService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável por controlar as ações da pagina de Chamada de Encalhe Antecipada.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path(value="/devolucao/chamadaEncalheAntecipada")
public class ChamadaEncalheAntecipadaController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private BoxService boxService;
	
	@Path("/")
	public void index(){
		
		result.include("listaFornecedores",obterFornecedores(null) );
		result.include("listaBoxes",obterBoxs(null));
	}
	
	@Post
	@Path("/pesquisarFornecedor")
	public void pesquisarFornecedorPorProduto(String codigoProduto){
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = obterFornecedores(codigoProduto);
		
		result.use(Results.json()).from(listaFornecedoresCombo, "result").recursive().serialize();
	}
	
	@Post
	@Path("/pesquisarBox")
	public void pesquisarBoxPoProduto(String codigoProduto,Long numeroEdicao ){
			
		List<ItemDTO<Long, String>> listaBoxCombo = obterBoxs(codigoProduto);
		
		result.use(Results.json()).from(listaBoxCombo, "result").recursive().serialize();
	}
	
	@Post
	@Path("/pesquisarDataProgramada")
	public void pesquisarpesquisarDataProgramadaEdicao(String codigoProduto, String numeroEdicao){
		
		result.use(Results.json()).from(new Date(), "result").recursive().serialize();
	}
	
	
	
	private List<ItemDTO<Long, String>> obterFornecedores(String codigoProduto){
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresPorProduto(codigoProduto, GrupoFornecedor.PUBLICACAO);
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(
				new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		return listaFornecedoresCombo;
	}
	
	private List<ItemDTO<Long, String>> obterBoxs(String codigoProduto){
		
		List<Box> listaBox = boxService.obterBoxPorProduto(codigoProduto);
		
		List<ItemDTO<Long, String>> listaBoxCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Box box : listaBox) {
			listaBoxCombo.add(
				new ItemDTO<Long, String>(box.getId(), box.getCodigo()));
		}
		
		return listaBoxCombo;
	}
}
