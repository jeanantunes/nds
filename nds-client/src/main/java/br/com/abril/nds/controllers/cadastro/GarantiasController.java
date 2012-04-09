package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import br.com.abril.nds.model.cadastro.Garantia;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/fiador")
public class GarantiasController {

	private static final String LISTA_GARANTIAS_SALVAR_SESSAO = "garantiasSalvarSessao";
	
	private static final String LISTA_GARANTIAS_REMOVER_SESSAO = "garanitasRemoverSessao";
	
	private Result result;
	
	private HttpSession httpSession;
	
	public GarantiasController(Result result, HttpSession httpSession){
		this.result = result;
		this.httpSession = httpSession;
	}
	
	@Post
	public void obterGarantiasFiador(){
		List<Garantia> listaGarantiasSessao = this.obterListaGarantiasSessao();
		
		Long idFiador = (Long) this.httpSession.getAttribute(FiadorController.ID_FIADOR_EDICAO);
		
		if (idFiador != null){
			//buscar no server
		}
		
		this.result.use(Results.json()).from(this.getTableModelListaGarantias(listaGarantiasSessao), "result").recursive().serialize();
	}
	
	@Post
	public void adicionarGarantia(Garantia garantia){
		List<Garantia> listaGarantiasSessao = this.obterListaGarantiasSessao();
		
		if (garantia.getId() == null){
			garantia.setId((new Date()).getTime() * -1);
			listaGarantiasSessao.add(garantia);
		} else {
			for (int i = 0 ; i < listaGarantiasSessao.size() ; i++){
				if (garantia.getId().equals(listaGarantiasSessao.get(i).getId())){
					listaGarantiasSessao.set(i, garantia);
					break;
				}
			}
		}
		
		this.httpSession.setAttribute(LISTA_GARANTIAS_SALVAR_SESSAO, listaGarantiasSessao);
		
		this.result.use(Results.json()).from(this.getTableModelListaGarantias(listaGarantiasSessao), "result").recursive().serialize();
	}
	
	@SuppressWarnings("unchecked")
	private List<Garantia> obterListaGarantiasSessao(){
		List<Garantia> listaGarantiasSessao = 
				(List<Garantia>) this.httpSession.getAttribute(LISTA_GARANTIAS_SALVAR_SESSAO);
		
		if (listaGarantiasSessao == null){
			listaGarantiasSessao = new ArrayList<Garantia>();
		}
		
		return listaGarantiasSessao;
	}
	
	private TableModel<CellModel> getTableModelListaGarantias(List<Garantia> listaGarantias) {

		TableModel<CellModel> tableModel = new TableModel<CellModel>();

		List<CellModel> listaCellModel = new ArrayList<CellModel>();

		for (Garantia garantia : listaGarantias) {
			
			CellModel cellModel = new CellModel(
				garantia.getId().intValue(), 
				garantia.getDescricao(),
				garantia.getValor() != null ? garantia.getValor().toString() : ""
			);

			listaCellModel.add(cellModel);
		}

		tableModel.setPage(1);
		tableModel.setRows(listaCellModel);
		tableModel.setTotal(listaCellModel.size()); 

		return tableModel;
	}
}
