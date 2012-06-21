package br.com.abril.nds.controllers.financeiro;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.ConsultaConsignadoCota;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/consultaConsignadoCota")
public class ConsultaConsignadoCotaController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE_CONSIGNADO_COTA = "filtroConsultaConsignadoCotaController";
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private ConsultaConsignadoCota consultaConsignadoCota;
	
	@Path("/")
	public void index(){
		
	}
	
	
	@Post
	@Path("pesquisarConsignadoCota")
	public void pesquisarConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro, String sortorder, String sortname, int page, int rp){

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<ConsultaConsignadoCotaDTO>> tableModel = efetuarConsultaConsignadoCota(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<ConsultaConsignadoCotaDTO>> efetuarConsultaConsignadoCota(
			FiltroConsultaConsignadoCotaDTO filtro) {
		
		List<ConsultaConsignadoCotaDTO> listaConsignadoCota = this.consultaConsignadoCota.buscarConsignadoCota(filtro, "limitar");
		
		TableModel<CellModelKeyValue<ConsultaConsignadoCotaDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaConsignadoCotaDTO>>();
		
//		Integer totalRegistros = this.romaneioService.buscarTotalDeRomaneios(filtro);
//		if(totalRegistros == 0){
//			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
//		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaConsignadoCota));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(15);
		
		return tableModel;
	}
	
	private void validarEntrada(FiltroConsultaConsignadoCotaDTO filtro) {		
		
		if(filtro.getIdCota() == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Pelo menos um filtro deve ser preenchido!");			
		}
		
	}
	
	private void tratarFiltro(FiltroConsultaConsignadoCotaDTO filtroAtual) {

		FiltroConsultaConsignadoCotaDTO filtroSession = (FiltroConsultaConsignadoCotaDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSIGNADO_COTA);
		
		if (filtroSession != null && filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_CONSIGNADO_COTA, filtroAtual);
	}

}
