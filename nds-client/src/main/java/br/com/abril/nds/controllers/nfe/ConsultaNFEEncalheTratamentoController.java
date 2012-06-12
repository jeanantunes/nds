package br.com.abril.nds.controllers.nfe;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaNFEEncalheTratamentoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNFEEncalheTratamento;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.service.ConsultaNFEEncalheTratamentoNotasRecebidasService;
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
@Path(value="/nfe/consultaNFEEncalheTratamento")
public class ConsultaNFEEncalheTratamentoController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE_CONSULTA = "filtroConsultaNFEEncalheTratamento";
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private ConsultaNFEEncalheTratamentoNotasRecebidasService consultaNFEEncalheTratamentoNotasRecebidasService;
	

	
	@Path("/")
	public void index(){
		carregarComboStatusNota();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void carregarComboStatusNota() {
		
	List<ItemDTO<String, String>> comboStatusNota = new ArrayList<ItemDTO<String, String>>();
		
		comboStatusNota.add(new ItemDTO(StatusNotaFiscalEntrada.RECEBIDA.name(), StatusNotaFiscalEntrada.RECEBIDA.getDescricao()));
		comboStatusNota.add(new ItemDTO(StatusNotaFiscalEntrada.PENDENTE_RECEBIMENTO.name(), StatusNotaFiscalEntrada.PENDENTE_RECEBIMENTO.getDescricao()));
		comboStatusNota.add(new ItemDTO(StatusNotaFiscalEntrada.PENDENTE_EMISAO.name(), StatusNotaFiscalEntrada.PENDENTE_EMISAO.getDescricao()));

		result.include("comboStatusNota", comboStatusNota);
		
	}
	
	@Post
	@Path("/pesquisarNotasRecebidas")
	public void pesquisarNotasRecebidas(FiltroConsultaNFEEncalheTratamento filtro, String sortorder, String sortname, int page, int rp){

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<ConsultaNFEEncalheTratamentoDTO>> tableModel = efetuarConsultaRomaneio(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<ConsultaNFEEncalheTratamentoDTO>> efetuarConsultaRomaneio(FiltroConsultaNFEEncalheTratamento filtro) {
		
		List<ConsultaNFEEncalheTratamentoDTO> listaNotasRecebidas = this.consultaNFEEncalheTratamentoNotasRecebidasService.buscarNFNotasRecebidas(filtro, "limitar");
		
		TableModel<CellModelKeyValue<ConsultaNFEEncalheTratamentoDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaNFEEncalheTratamentoDTO>>();
		
		Integer totalRegistros = this.consultaNFEEncalheTratamentoNotasRecebidasService.buscarTodasNFENotasRecebidas(filtro);
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNotasRecebidas));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}

	private void validarEntrada(FiltroConsultaNFEEncalheTratamento filtro) {
//		boolean validar = false;
//		
//		if(filtro.getIdBox()==null && filtro.getIdRoteiro()==null && filtro.getIdRota()==null){
//			validar = true;
//		}
//		
//		if(validar){
//			throw new ValidacaoException(TipoMensagem.WARNING, "Pelo menos um filtro deve ser preenchido!");
//		}
		
	}
	
	private void tratarFiltro(FiltroConsultaNFEEncalheTratamento filtroAtual) {

		FiltroConsultaNFEEncalheTratamento filtroSession = (FiltroConsultaNFEEncalheTratamento) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSULTA);
		
		if (filtroSession != null && filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_CONSULTA, filtroAtual);
	}

}
