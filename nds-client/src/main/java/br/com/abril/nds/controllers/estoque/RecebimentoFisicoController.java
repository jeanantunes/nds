package br.com.abril.nds.controllers.estoque;

import java.util.List;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.repository.impl.FornecedorRepositoryImpl;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;


@Resource
public class RecebimentoFisicoController {
	
	private Result result;
	FornecedorRepositoryImpl fornecedorRepository;
	
	@Path("estoque/recebimentoFisico")
	public void recebimentoFisico(Result result, FornecedorRepositoryImpl fornecedorRepository) {
		this.result = result;
		this.fornecedorRepository = fornecedorRepository;
	}
	
	@Post 
	public List<Fornecedor> obterFornecedoresAtivos(){		
		List<Fornecedor> listaFornecedores = fornecedorRepository.obterFornecedoresAtivos();
		return listaFornecedores;
	}
	
	@Get
	public List<RecebimentoFisico> consulta(){
		return null;
	}
	
	@Get
	public List<RecebimentoFisico> inserir(){
		return null;
	}
	
	private void carregarComboTiposDiferenca() {

		/*List<ItemDTO<RecebimentoFisico, String>> listaRecebimentoFisico =
			new ArrayList<ItemDTO<RecebimentoFisico, String>>();
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.FALTA_DE, TipoDiferenca.FALTA_DE.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.FALTA_EM, TipoDiferenca.FALTA_EM.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.SOBRA_DE, TipoDiferenca.SOBRA_DE.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.SOBRA_EM, TipoDiferenca.SOBRA_EM.getDescricao())
		);
		
		result.include("listaTiposDiferenca", listaTiposDiferenca);*/
	}
}
