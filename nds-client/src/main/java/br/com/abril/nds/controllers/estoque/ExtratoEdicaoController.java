package br.com.abril.nds.controllers.estoque;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.movimentacao.MovimentoEstoque;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
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
	
	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	public ExtratoEdicaoController(Result result) {
		this.result = result;
	}
	
	public void index(){
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void pesquisaExtratoEdicao() throws Exception {
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		
		List<MovimentoEstoque> movimentos =  movimentoEstoqueRepository.obterListaMovimentoEstoque();
		
		List<CellModel> listaModeloGenerico = new LinkedList<CellModel>();
		
		for(MovimentoEstoque movimento : movimentos) {
			
			CellModel modeloGenerico = new CellModel( 
					movimento.getId().intValue(), 
					"", 
					"", 
					"", 
					"", 
					"");
			
			modeloGenerico.getCell()[0] = sdf.format(movimento.getDataInclusao());		
			modeloGenerico.getCell()[1] = sdf.format(movimento.getTipoMovimento().getDescricao());		
			modeloGenerico.getCell()[2] = "a";		
			modeloGenerico.getCell()[3] = "b";		
			modeloGenerico.getCell()[4] = "c";		
			
			modeloGenerico.setId(0);
			
			listaModeloGenerico.add(modeloGenerico);
			
		}
		
		TableModel<MovimentoEstoque> tm = new TableModel<MovimentoEstoque>();
		
		tm.setPage(1);
		tm.setTotal(listaModeloGenerico.size());
		tm.setRows(listaModeloGenerico.toArray(new CellModel[listaModeloGenerico.size()]));
		
		result.use(Results.json()).withoutRoot().from(tm).recursive().serialize();
		
	}
	
	
}
