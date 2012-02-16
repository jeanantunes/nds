package br.com.abril.nds.controllers.estoque;

import java.util.LinkedList;
import java.util.List;

import br.com.abril.nds.controllers.testgrid.ExtratoEdicaoDTO;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
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
			extratoEdicao.setData("data_"+contador);
			extratoEdicao.setMovimento("movimento_"+contador);
			extratoEdicao.setEntrada("entrada_"+contador);
			extratoEdicao.setSaida("saida_"+contador);
			extratoEdicao.setParcial("parcial_"+contador);
			
			listaCelula.add(new CellModel<ExtratoEdicaoDTO>(extratoEdicao, "idExtratoEdicaoDTO", "movimento", "movimento"));
			
		}
		
		TableModel<ExtratoEdicaoDTO> tm = new TableModel<ExtratoEdicaoDTO>();
		
		tm.setPage(1);
		
		tm.setTotal(listaCelula.size());
		
		tm.setRows(listaCelula.toArray(new CellModel[listaCelula.size()]));
		
		result.use(Results.json()).withoutRoot().from(tm).recursive().serialize();
		
	}
	
	
}
