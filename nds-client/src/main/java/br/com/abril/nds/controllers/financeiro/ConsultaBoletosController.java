package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO.OrdenacaoColunaLancamento;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pela tela de consulta de Boletos. 
 * 
 * @author Discover Technology
 *
 */

@Resource
@Path("/financeiro/boletos")
public class ConsultaBoletosController {

    private Result result;

    private static final List<ItemDTO<StatusCobranca,String>> listaStatusCombo =  new ArrayList<ItemDTO<StatusCobranca,String>>();

	@Autowired
	private BoletoService boletoService;
	
	
	
	private static final String FILTRO_PESQUISA_BOLETOS_SESSION_ATTRIBUTE = "filtroPesquisaLancamento";
	private HttpSession httpSession;
	
	

	public ConsultaBoletosController(Result result) {
		this.result = result;
	}
	
	@Get
    public void consulta(){
		listaStatusCombo.clear();
		listaStatusCombo.add(new ItemDTO<StatusCobranca,String>(null,"Todos"));
		listaStatusCombo.add(new ItemDTO<StatusCobranca,String>(StatusCobranca.NAO_PAGO,"Não Pago"));
		listaStatusCombo.add(new ItemDTO<StatusCobranca,String>(StatusCobranca.PAGO,"Pago"));
		
		result.include("listaStatusCombo",listaStatusCombo);
		result.include("dataDe", DateUtil.formatarData(Calendar.getInstance().getTime(), "MM/dd/yyyy"));
		result.include("dataAte",DateUtil.formatarData(Calendar.getInstance().getTime(), "MM/dd/yyyy"));
	}
	
	@Post
	@Path("/consultaBoletos")
	public void consultaBoletos(String numCota,
								String dataDe,
								String dataAte, 
								StatusCobranca status,
								
								String sortorder, 
								String sortname, 
								int page, 
								int rp
								
								){
		
		//VALIDACOES
		if (numCota==null || numCota.isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "Digite o número da cota.");
		}
		if (dataDe==null || dataDe.isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "Digite a data inicial.");
		}
		if (dataAte==null || dataAte.isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "Digite a data final.");
		}
		if (DateUtil.isDataInicialMaiorDataFinal(DateUtil.parseData(dataDe,"dd/MM/yyyy"),DateUtil.parseData(dataAte,"dd/MM/yyyy"))){
			throw new ValidacaoException(TipoMensagem.ERROR, "A data inicial deve ser menor do que a data final.");
		}
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		FiltroConsultaBoletosCotaDTO filtroAtual = new FiltroConsultaBoletosCotaDTO(Integer.parseInt(numCota),
                																	DateUtil.parseData(dataDe,"dd/MM/yyyy"),
                																	DateUtil.parseData(dataAte,"dd/MM/yyyy"),
                																	status);
		
		
        if (filtroAtual != null) {
			
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
	
			filtroAtual.setPaginacao(paginacao);

			filtroAtual.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaLancamento.values(), sortname));
		}
		
		
		
		FiltroConsultaBoletosCotaDTO filtroSessao = (FiltroConsultaBoletosCotaDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_BOLETOS_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
		
			filtroAtual.getPaginacao().setPaginaAtual(1);
			
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_BOLETOS_SESSION_ATTRIBUTE, filtroAtual);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//BUSCA BOLETOS
		List<Boleto> boletos = this.boletoService.obterBoletosPorCota(Integer.parseInt(numCota),
				                                                      DateUtil.parseData(dataDe,"dd/MM/yyyy"),
				                                                      DateUtil.parseData(dataAte,"dd/MM/yyyy"),
				                                                      status);
		
		//CARREGA DIRETO DA ENTIDADE PARA A TABELA
		List<CellModel> listaModelo = new LinkedList<CellModel>();
		
		if (boletos.size()==0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		
		for (Boleto boleto : boletos){	
			listaModelo.add(new CellModel(1,
										  Integer.toString(boleto.getNossoNumero()),
										  DateUtil.formatarData(boleto.getDataEmissao(),"dd/MM/yyyy"),
										  DateUtil.formatarData(boleto.getDataVencimento(),"dd/MM/yyyy"),
										  DateUtil.formatarData(boleto.getDataPagamento(),"dd/MM/yyyy"),
										  boleto.getEncargos(),
										  Double.toString(boleto.getValor()),
										  boleto.getTipoBaixa(),
										  boleto.getStatusCobranca().name(),
										  boleto.getAcao()
                      					)
              );
		}	
		
		TableModel<CellModel> tm = new TableModel<CellModel>();
		tm.setTotal(listaModelo.size());
		tm.setRows(listaModelo);
		
		//PREPARA RESULTADO PARA A VIEW (HASHMAP)
		Map<String, TableModel<CellModel>> resultado = new HashMap<String, TableModel<CellModel>>();
		resultado.put("TblModelBoletos", tm);
		
		//RETORNA HASHMAP EM FORMATO JASON PARA A VIEW
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();

	}
	
}
