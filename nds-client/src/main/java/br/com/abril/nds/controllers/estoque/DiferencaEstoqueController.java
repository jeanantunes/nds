package br.com.abril.nds.controllers.estoque;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.service.FornecedorService;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/estoque/diferenca")
public class DiferencaEstoqueController {

	private Result result;
	
	@Autowired
	private FornecedorService fornecedorService;

	public DiferencaEstoqueController(Result result) {
		
		this.result = result;
	}
	
	@Get
	public void lancamento() {
		
		this.carregarCombosLancamento();
	}
	
	@Post
	@Path("/lancamento/pesquisa")
	public void pesquisarLancamentos(String dataMovimento, String tipoDiferenca) {
		
		System.out.println(dataMovimento);
		
		System.out.println(tipoDiferenca);
		
		result.use(Results.json()).from("teste", "result").serialize();
	}
	
	@Get
	public void consulta() {
		this.carregarCombosConsulta();
	}
	
	@Post
	@Path("/consultarFaltasSobras")
	public void consultarFaltasSobras() {
		result.use(Results.json()).from("Retorno", "result").serialize();
	}

	/**
	 * Método responsável por carregar todos os combos da tela de consulta.
	 */
	public void carregarCombosConsulta() {
		this.carregarComboTiposDiferenca();
		this.carregarComboFornecedores();
	}
	
	/**
	 * Método responsável por carregar todos os combos da tela de lançamento.
	 */
	private void carregarCombosLancamento() {
		
		this.carregarComboTiposDiferenca();
	}
	
	/**
	 * Método responsável por carregar o combo de tipos de diferença.
	 */
	private void carregarComboTiposDiferenca() {

		List<ItemDTO<TipoDiferenca, String>> listaTiposDiferenca =
			new ArrayList<ItemDTO<TipoDiferenca, String>>();
		
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
		
		result.include("listaTiposDiferenca", listaTiposDiferenca);
	}
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private void carregarComboFornecedores() {

		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedores();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo =
			new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(
				new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getNomeFantasia())
			);
		}
		
		result.include("listaFornecedores", listaFornecedoresCombo);
	}
	
}
