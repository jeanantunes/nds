package br.com.abril.nds.controllers.financeiro;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO.OrdenacaoColunaBoletos;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.Constantes;
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
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Message;
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

    
	@Autowired
	private EmailService email;
	
	@Autowired
	private BoletoService boletoService;
	
    @Autowired
	private Validator validator;
	
	private Result result;
    
    private HttpSession httpSession;
    
    private HttpServletResponse httpResponse;

    private static final List<ItemDTO<StatusCobranca,String>> listaStatusCombo =  new ArrayList<ItemDTO<StatusCobranca,String>>();

    private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisa";
   
	
	public ConsultaBoletosController(Result result, HttpSession httpSession, HttpServletResponse httpResponse) {
		this.result = result;
		this.httpSession = httpSession;
		this.httpResponse = httpResponse;
	}
	
	@Get
    public void consulta(){
		listaStatusCombo.clear();
		listaStatusCombo.add(new ItemDTO<StatusCobranca,String>(null,"Todos"));
		listaStatusCombo.add(new ItemDTO<StatusCobranca,String>(StatusCobranca.NAO_PAGO,"Não Pagos"));
		listaStatusCombo.add(new ItemDTO<StatusCobranca,String>(StatusCobranca.PAGO,"Pagos"));
		
		result.include("listaStatusCombo",listaStatusCombo);
		result.include("dataDe", DateUtil.formatarData(Calendar.getInstance().getTime(), "dd/MM/yyyy"));
		result.include("dataAte",DateUtil.formatarData(Calendar.getInstance().getTime(), "dd/MM/yyyy"));
	}

	@Post
	@Path("/consultaBoletos")
	public void consultaBoletos(Integer numCota,
								Date dataDe,
								Date dataAte, 
								StatusCobranca status,
								String sortorder, 
								String sortname, 
								int page, 
								int rp
								){
		
		//VALIDACOES
		validar(numCota,
                dataDe,
                dataAte,
                status);
		
		//CONFIGURAR PAGINA DE PESQUISA
		FiltroConsultaBoletosCotaDTO filtroAtual = new FiltroConsultaBoletosCotaDTO(numCota,
                															        /*DateUtil.parseDataPTBR(DateUtil.formatarData(*/dataDe/*,"dd/MM/yyyy"))*/,
                															        /*DateUtil.parseDataPTBR(DateUtil.formatarData(*/dataAte/*,"dd/MM/yyyy"))*/,
                															        status);
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		filtroAtual.setPaginacao(paginacao);
		filtroAtual.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaBoletos.values(), sortname));
	
		FiltroConsultaBoletosCotaDTO filtroSessao = (FiltroConsultaBoletosCotaDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
		
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtroAtual);
		
		
		
		//BUSCA BOLETOS
		List<Boleto> boletos = this.boletoService.obterBoletosPorCota(filtroAtual);
		
		//CARREGA DIRETO DA ENTIDADE PARA A TABELA
		List<CellModel> listaModelo = new LinkedList<CellModel>();
		
		if (boletos.size()==0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		
		String dtPagto;
		for (Boleto boleto : boletos){	
			
			dtPagto="";
			if (boleto.getDataPagamento()!=null){
				dtPagto=DateUtil.formatarData(boleto.getDataPagamento(),"dd/MM/yyyy");
			}
			
			listaModelo.add(new CellModel(1,
										  boleto.getNossoNumero(),
										  DateUtil.formatarData(boleto.getDataEmissao(),"dd/MM/yyyy"),
										  DateUtil.formatarData(boleto.getDataVencimento(),"dd/MM/yyyy"),
										  dtPagto,
										  boleto.getEncargos(),
										  boleto.getValor().toString(),
										  boleto.getTipoBaixa(),
										  boleto.getStatusCobranca().name(),
										  boleto.getAcao()
                      					)
              );
		}	
		
		TableModel<CellModel> tm = new TableModel<CellModel>();

		//DEFINE TOTAL DE REGISTROS NO TABLEMODEL
		tm.setTotal( (int) this.boletoService.obterQuantidadeBoletosPorCota(filtroAtual));
		
		//DEFINE CONTEUDO NO TABLEMODEL
		tm.setRows(listaModelo);
		
		//DEFINE PAGINA ATUAL NO TABLEMODEL
		tm.setPage(filtroAtual.getPaginacao().getPaginaAtual());
		
		
		//PREPARA RESULTADO PARA A VIEW (HASHMAP)
		Map<String, TableModel<CellModel>> resultado = new HashMap<String, TableModel<CellModel>>();
		resultado.put("TblModelBoletos", tm);
		
		//RETORNA HASHMAP EM FORMATO JASON PARA A VIEW
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();

	}
	
	@Get
	@Path("/imprimeBoleto")
	public void imprimeBoleto(String nossoNumero) throws Exception{
		
		byte[] b = boletoService.gerarImpressaoBoleto(nossoNumero);
        
		this.httpResponse.setContentType("application/pdf");
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename=boleto.pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		output.write(b);

		httpResponse.flushBuffer();
	}
	
	@Post
	@Path("/enviaBoleto")
	public void enviaBoleto(String nossoNumero) throws Exception{

		File[] anexo = new File[]{boletoService.gerarAnexoBoleto(nossoNumero)};
		String[] destinatarios = new String[]{boletoService.obterEmailCota(nossoNumero)};
		
		email.enviar("Assunto", "Mensagem", destinatarios, anexo);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	public void validar(Integer numCota,
					    Date dataDe,
					    Date dataAte, 
					    StatusCobranca status){
		//VALIDACOES
		if (validator.hasErrors()) {
			List<String> mensagens = new ArrayList<String>();
			for (Message message : validator.getErrors()) {
				mensagens.add(message.getMessage());
			}
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.ERROR, mensagens);
			throw new ValidacaoException(validacao);
		}
		
		if (numCota==null || numCota<=0){
			throw new ValidacaoException(TipoMensagem.ERROR, "Digite o número da cota.");
		}
		
		if ( (dataDe!=null) && (dataAte!=null) ){
		    if ( DateUtil.isDataInicialMaiorDataFinal( DateUtil.parseDataPTBR(DateUtil.formatarData(dataDe,"dd/MM/yyyy")) ,DateUtil.parseDataPTBR(DateUtil.formatarData(dataAte,"dd/MM/yyyy")) ) ) {
			    throw new ValidacaoException(TipoMensagem.ERROR, "A data inicial deve ser menor do que a data final.");
		    }
		}
	}
	
}
