package br.com.abril.nds.controllers;

import java.util.LinkedList;
import java.util.List;

import br.com.abril.nds.controllers.testgrid.CellModel;
import br.com.abril.nds.controllers.testgrid.ExtratoEdicaoDTO;
import br.com.abril.nds.controllers.testgrid.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/extratoEdicao")
public class ExtratoEdicaoController {
	
	private Result result;
	
	public ExtratoEdicaoController(Result result) {
		this.result = result;
	}
	
	public void index(){
		
	}
	
		
	public void toJSon() throws Exception{
		
		int contador = 0;
		
		List<CellModel<ExtratoEdicaoDTO>> listaCelula = new LinkedList<CellModel<ExtratoEdicaoDTO>>();
		
		ExtratoEdicaoDTO extratoEdicao = null;
		
		while(contador++<10) {
			
			extratoEdicao = new ExtratoEdicaoDTO();
			
			extratoEdicao.setIdExtratoEdicaoDTO(contador);
			extratoEdicao.setNome("nome_"+contador);
			extratoEdicao.setEmail("email_"+contador);
			
			listaCelula.add(new CellModel<ExtratoEdicaoDTO>(extratoEdicao));
			
		}
		
		TableModel tm = new TableModel();
		
		tm.setPage(1);
		tm.setTotal(listaCelula.size());
		tm.setRows(listaCelula.toArray(new CellModel[listaCelula.size()]));
		
		result.use(Results.json()).withoutRoot().from(tm).recursive().serialize();
		
	}
	
	
}
