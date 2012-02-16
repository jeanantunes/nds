package br.com.abril.nds.controllers.estoque;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/estoque/diferenca")
public class DiferencaEstoqueController {

	private Result result;
	
	public DiferencaEstoqueController(Result result) {
		
		this.result = result;
	}
	
	@Get
	public void lancamento() {
		
		this.carregarCombos();
	}
	
	@Get
	@Path("/lancamento/pesquisa")
	public void pesquisarLancamentos(Date dataMovimento, String tipoDiferenca) {

		result.forwardTo(DiferencaEstoqueController.class).lancamento();
	}
	
	@Get
	public void consulta() {
		
	}
	
	/**
	 * Método responsável por carregar todos os combos.
	 */
	private void carregarCombos() {
		
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
	
}
