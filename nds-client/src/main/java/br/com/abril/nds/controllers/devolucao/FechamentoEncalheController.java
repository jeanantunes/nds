package br.com.abril.nds.controllers.devolucao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.service.FornecedorService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

/**
 * Classe responsável pelo controle das ações referentes à
 * tela de chamadão de publicações.
 * 
 * @author Discover Technology
 */
@Resource
@Path("devolucao/fechamentoEncalhe")
public class FechamentoEncalheController {

	@Autowired
	private Result result;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Path("/")
	public void index() {
		
		/*
		List<ItemDTO<Long, String>> listaFornecedoresCombo =
			this.carregarComboFornecedores(null);
			
		result.include("listaFornecedores", listaFornecedoresCombo);
		*/
	}
	
	
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private List<ItemDTO<Long, String>> carregarComboFornecedores(String codigoProduto) {
		
		List<Fornecedor> listaFornecedor =
			fornecedorService.obterFornecedoresPorProduto(codigoProduto, null);
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo =
			new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(
				new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial())
			);
		}
			
		return listaFornecedoresCombo;
	}
}
