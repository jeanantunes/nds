package br.com.abril.nds.controllers.estoque;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.movimentacao.MovimentoEstoque;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/estoque/extratoEdicao")
public class ExtratoEdicaoController {
	
	private Result result;
	
	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	public ExtratoEdicaoController(Result result) {
		this.result = result;
	}
	
	public void index(){
		
	}
	
	
	private List<MovimentoEstoque> getFromBDTeste() {
	
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		List<MovimentoEstoque> listaModeloGenerico = new LinkedList<MovimentoEstoque>();
		long contador = 0;

		while(contador++<10) {
			
			Usuario usuario = new Usuario();
			usuario.setId(contador);
			usuario.setNome("nome_"+contador);
			
			Diferenca d = new Diferenca();
			d.setId(contador);
			d.setResponsavel(usuario);
			
			MovimentoEstoque m = new MovimentoEstoque();
			m.setId(contador);
			m.setDataInclusao(new Date());
			m.setDiferenca(d);
			listaModeloGenerico.add(m);
		}
		
		return listaModeloGenerico;
		
	}
	
	
	private void massaDadosParaTeste() {
		
	
		
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void pesquisaExtratoEdicao() throws Exception {
		
		List<CellModel> listaModeloGenerico = new LinkedList<CellModel>();
		
		for(MovimentoEstoque movimento :  getFromBDTeste()) {
			listaModeloGenerico.add(new CellModel(0, "teste", "ola", "sdfasfd", "sdfsa"));
		}
		
		TableModel<CellModel> tm = new TableModel<CellModel>();
		
		tm.setPage(1);
		
		tm.setTotal(listaModeloGenerico.size());
		
		tm.setRows(listaModeloGenerico);

		
		result.use(Results.json()).withoutRoot().from(tm).recursive().serialize();
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void pesquisaExtratoEdicaoModo2() throws Exception {
		
		List<CellModelKeyValue<MovimentoEstoque>> listaModeloGenerico = new LinkedList<CellModelKeyValue<MovimentoEstoque>>();
		
		
		for(MovimentoEstoque movimento :  getFromBDTeste()) {
			listaModeloGenerico.add(new CellModelKeyValue<MovimentoEstoque>(0, movimento));
		}
		
		TableModel<CellModelKeyValue<MovimentoEstoque>> tm = new TableModel<CellModelKeyValue<MovimentoEstoque>>();
		
		tm.setPage(1);
		
		tm.setTotal(listaModeloGenerico.size());
		
		tm.setRows(listaModeloGenerico);
		
		
		result.use(Results.json()).withoutRoot().from(tm).recursive().serialize();
		
	}
	
	
}
