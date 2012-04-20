package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.PdvVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/pdv")
public class PdvController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private PdvService pdvService;
	
	private static final String PARAM_LISTA_PDV_SESSION = "pramListaPdvs";
	
	
	
	@Path("/")
	public void index(){
		
	}
	
	@Post
	@Path("/consultar")
	public void consultarPDVs(Long idCota){
		
		List<PdvVO> listaPdvs = null;
		
		if(getPdvsSession().isEmpty()){
			listaPdvs = getMock();
			session.setAttribute(PARAM_LISTA_PDV_SESSION, listaPdvs);
		}
		else{
			listaPdvs = getPdvsSession();
		}
		
		TableModel<CellModelKeyValue<PdvVO>> tableModel =
				new TableModel<CellModelKeyValue<PdvVO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaPdvs));
		
		tableModel.setTotal(listaPdvs.size());
		
		tableModel.setPage(1);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	@Path("/excluir")
	public void excluirPDV(Long idPdv, Long idCota, int id){
		
		if(!pdvService.isExcluirPdv(idPdv)){
			throw new ValidacaoException(TipoMensagem.WARNING, "Pdv não pode ser excluido!");
		}
		
		List<PdvVO> list = getPdvsSession();
		
		if(!list.isEmpty()){
			list.remove(id);
		}
		
		result.use(Results.json()).from("","result").recursive().serialize();
	}
		
	@Post
	@Path("/editar")
	public void editarPDV(Long idPdv, Long idCota){
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	private List<PdvVO> getPdvsSession(){
		
		if(session.getAttribute(PARAM_LISTA_PDV_SESSION)!= null){

			return (List<PdvVO>) session.getAttribute(PARAM_LISTA_PDV_SESSION);
		}
		return new ArrayList<PdvVO>();
	}
	
	public List<PdvVO> getMock(){
		
		List<PdvVO> listaPdvs = new ArrayList<PdvVO>();
		
		for(int x=0; x<6 ; x++){
			listaPdvs.add(new PdvVO(x,Long.valueOf(x),Long.valueOf(x),"Nome PDV" + x,"Tipo PDV","CONTATo","3689-25444","Rua XPTO","Bairro","Cidade"));
		}
		
		return listaPdvs;
		
		
	}

}
