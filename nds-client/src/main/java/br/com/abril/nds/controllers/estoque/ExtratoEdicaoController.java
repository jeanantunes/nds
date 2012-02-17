package br.com.abril.nds.controllers.estoque;

import java.util.Date;
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
@Path("/estoque/extratoEdicao")
public class ExtratoEdicaoController {
	
	private Result result;
	
	public ExtratoEdicaoController(Result result) {
		this.result = result;
	}
	
	public void index(){
		
	}
	
	private List<ExtratoEdicaoDTO> getListaFromBD() {
		
		List<ExtratoEdicaoDTO> listaExtrato = new LinkedList<ExtratoEdicaoDTO>();
		
		int contador = 0;
		
		ExtratoEdicaoDTO extratoEdicao = null;
		
		while(contador++<10) {
			
			extratoEdicao = new ExtratoEdicaoDTO();
			
			extratoEdicao.setIdExtratoEdicaoDTO(contador);
			extratoEdicao.setData(new Date());
			extratoEdicao.setMovimento("movimento_"+contador);
			extratoEdicao.setEntrada(5L);
			extratoEdicao.setSaida("saida_"+contador);
			extratoEdicao.setParcial("parcial_"+contador);
			
			listaExtrato.add(extratoEdicao);
			
		}
		
		return listaExtrato;
		
	}
		
	public void toJSon() throws Exception{
		

		List<ExtratoEdicaoDTO> listaDoBD = getListaFromBD();
		
		List<CellModel<ExtratoEdicaoDTO>> listaCelula = new LinkedList<CellModel<ExtratoEdicaoDTO>>();
		
		
		//ITERANDO A LISTA QUE VEM DO BANCO.
		for(ExtratoEdicaoDTO extrato : listaDoBD) {
			
			CellModel<ExtratoEdicaoDTO> modeloGenerico = new CellModel<ExtratoEdicaoDTO>( extrato, 0, "_data", "movimento", "entrada", "saida", "parcial");
			
			modeloGenerico.getCell()[0] = "01-01-2012";//getDataFromMyDateObj(extrat.getData)		
			

			//CellModel<ExtratoEdicaoDTO> modeloGenerico = new CellModel<ExtratoEdicaoDTO>(extrato);

			modeloGenerico.setId(0);
			
			listaCelula.add(modeloGenerico);
			
		}
		
		TableModel<ExtratoEdicaoDTO> tm = new TableModel<ExtratoEdicaoDTO>();
		
		tm.setPage(1);
		tm.setTotal(listaCelula.size());
		tm.setRows(listaCelula.toArray(new CellModel[listaCelula.size()]));
		
		result.use(Results.json()).withoutRoot().from(tm).recursive().serialize();
		
		
	}
	
	
}
