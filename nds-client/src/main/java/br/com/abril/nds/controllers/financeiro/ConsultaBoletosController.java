package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.BoletoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.BoletoCotaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO.OrdenacaoColunaBoletos;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.BoletoAntecipado;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
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
@SuppressWarnings("deprecation")
@Resource
@Path("/financeiro/boletos")
@Rules(Permissao.ROLE_FINANCEIRO_CONSULTA_BOLETOS_COTA)
public class ConsultaBoletosController extends BaseController {

	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private CotaService cotaService;
	
    @Autowired
	private Validator validator;
	
	private Result result;
    
    private HttpSession httpSession;
    
    private HttpServletResponse httpResponse;

    private static final List<ItemDTO<StatusCobranca,String>> listaStatusCombo =  new ArrayList<ItemDTO<StatusCobranca,String>>();

    @Autowired
    private DistribuidorService distribuidorService;
    
    private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaConsultaBoletos";
   
	/**
	 * Construtor da classe
	 * @param result
	 * @param httpSession
	 * @param httpResponse
	 */
	public ConsultaBoletosController(Result result, HttpSession httpSession, HttpServletResponse httpResponse) {
		this.result = result;
		this.httpSession = httpSession;
		this.httpResponse = httpResponse;
	}
	
	/**
	 * Método de chamada da página
	 * Pré-carrega itens da pagina com informações default.
	 */
	@Get
	@Path("/")
	public void consulta(){ 
		
		result.include("data", DateUtil.formatarDataPTBR(distribuidorService.obterDataOperacaoDistribuidor()));
		
		listaStatusCombo.clear();
		listaStatusCombo.add(new ItemDTO<StatusCobranca,String>(null,"Todos"));
		listaStatusCombo.add(new ItemDTO<StatusCobranca,String>(StatusCobranca.PAGO,"Pagos"));
		listaStatusCombo.add(new ItemDTO<StatusCobranca,String>(StatusCobranca.NAO_PAGO,"Não Pagos"));
		
		result.include("listaStatusCombo",listaStatusCombo);
	}

    /**
     * Método de Consulta de boletos
     * @param numCota
     * @param dataDe
     * @param dataAte
     * @param status
     * @param sortorder
     * @param sortname
     * @param page
     * @param rp
     * @throws ValidacaoException(): caso não encontre boletos para os parâmetros
     */
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
		validar(numCota, dataDe, dataAte, status);
		
		//CONFIGURAR PAGINA DE PESQUISA
		FiltroConsultaBoletosCotaDTO filtroAtual = new FiltroConsultaBoletosCotaDTO(numCota,
                															        dataDe,
                															        dataAte,
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
		List<BoletoCotaDTO> boletosDTO = this.boletoService.obterBoletosPorCota(filtroAtual);
		
		//VERIFICA SE CAMPO PARA ENVIO SERA ABERTO NAO
		boletosDTO = this.boletoService.verificaEnvioDeEmail(boletosDTO);
		
		
		//CARREGA DIRETO DA ENTIDADE PARA A TABELA
		List<CellModel> listaModelo = new LinkedList<CellModel>();
		
		if (boletosDTO.size()==0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		
		DecimalFormat formatoMoeda = new DecimalFormat("#,###,##0.00");
		
		for (BoletoCotaDTO boletoDTO : boletosDTO){
			
			String statusBoleto = "";
			
			if (boletoDTO.isBoletoAntecipado()){
				
				statusBoleto = "Boleto em branco" + (boletoDTO.getStatusDivida().equals(StatusDivida.BOLETO_ANTECIPADO_EM_ABERTO)?" - Não pago":" - Pago");
			}
			else{
				
			    if (boletoDTO.getStatusDivida() != null && 
			            (boletoDTO.getStatusDivida().equals(StatusDivida.PENDENTE_INADIMPLENCIA) || 
			                    boletoDTO.getStatusDivida().equals(StatusDivida.POSTERGADA) ||
			                    boletoDTO.getStatusDivida().equals(StatusDivida.NEGOCIADA))){
			        
			        statusBoleto = boletoDTO.getStatusDivida().getDescricao();
			    } else if (boletoDTO.getStatusCobranca() != null){
			        
			        statusBoleto = boletoDTO.getStatusCobranca().toString();
			    } else {
			        
			        statusBoleto = "";
			    }
			    
//				statusBoleto = ((boletoDTO.getStatusDivida() != null && StatusDivida.PENDENTE_INADIMPLENCIA.equals(boletoDTO.getStatusDivida())) ?  
//		                        StatusDivida.PENDENTE_INADIMPLENCIA.getDescricao() : 
//                                (boletoDTO.getStatusCobranca()!=null?boletoDTO.getStatusCobranca().toString():""));
			}    
			
			listaModelo.add(new CellModel(1,
										  (boletoDTO.getNossoNumero()!=null?boletoDTO.getNossoNumero():""),
										  (boletoDTO.getDataEmissao()!=null?DateUtil.formatarData(boletoDTO.getDataEmissao(),"dd/MM/yyyy"):""),
										  (boletoDTO.getDataVencimento()!=null?DateUtil.formatarData(boletoDTO.getDataVencimento(),"dd/MM/yyyy"):""),
										  (boletoDTO.getDataPagamento()!=null?DateUtil.formatarData(boletoDTO.getDataPagamento(),"dd/MM/yyyy"):""),
										  (boletoDTO.getEncargos()!=null? formatoMoeda.format(boletoDTO.getEncargos()) : ""),
										  (boletoDTO.getValor()!=null? formatoMoeda.format(boletoDTO.getValor()) : ""),
										  (boletoDTO.getTipoBaixa()!=null?boletoDTO.getTipoBaixa().getDescricao():""),
										   statusBoleto,
			                              (boletoDTO.isRecebeCobrancaEmail())
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
		
		//RETORNA HASHMAP EM FORMATO JSON PARA A VIEW
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();

	}
	
	private byte[] obterImpressaoBoleto(String nossoNumero){
		
		byte[] b = null;
		
		try{
			
			b = this.boletoService.gerarImpressaoBoleto(nossoNumero);
			
			if (b == null){
				
			    b = this.boletoService.gerarImpressaoBoletoEmBranco(nossoNumero);
			}
		}	
		catch(Exception e){
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar impressão do Boleto. "+e.getMessage());
		}
		
		return b;
	}
	
	/**
	 * Exibe o boleto em formato PDF.
	 * @param nossoNumero
	 * @throws Exception
	 */
	@Get
	@Path("/imprimeBoleto")
	@Rules(Permissao.ROLE_FINANCEIRO_CONSULTA_BOLETOS_COTA_ALTERACAO)
	public void imprimeBoleto(String nossoNumero) throws Exception{

		byte[] b = this.obterImpressaoBoleto(nossoNumero);
		
		if (b == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "O boleto "+nossoNumero+" não foi encontrado.");
		}

		this.httpResponse.setContentType("application/pdf");
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename=boleto" + nossoNumero + ".pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		output.write(b);

		
		httpResponse.flushBuffer();
	}
	
	/**
	 * Retorna a informação de boleto pago ou não pago para a tela.
	 * @param nossoNumero
	 * @throws Exception
	 */
	@Post
	@Path("/verificaBoleto")
	public void verificaBoleto(String nossoNumero) throws Exception{
		
		if(validarBoletoPago(nossoNumero)){
			byte[] b = this.obterImpressaoBoleto(nossoNumero);
			result.use(Results.json()).from(nossoNumero,"result").recursive().serialize();
			
	    }else{
	    	
	    	throw new ValidacaoException(TipoMensagem.WARNING, "O boleto "+nossoNumero+" já está pago ou postergado.");
	    }
	}
	
	/**
	 * Envio de boleto por email no formato PDF.
	 * @param nossoNumero
	 * @throws Exception
	 */
	@Post
	@Path("/enviaBoleto")
	@Rules(Permissao.ROLE_FINANCEIRO_CONSULTA_BOLETOS_COTA_ALTERACAO)
	public void enviaBoleto(String nossoNumero) throws Exception{

		if (!validarBoletoPago(nossoNumero)){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "O boleto "+nossoNumero+" já está pago.");
		}
		
		boletoService.enviarBoletoEmail(nossoNumero);
		
		//CONTROLE DE VIAS IMPRESSAS
		boletoService.incrementarVia(nossoNumero);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Boleto "+nossoNumero+" enviado com sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	/**
	 * Valida os parâmetros de entrada para a consulta de boletos.
	 * @param numCota
	 * @param dataDe
	 * @param dataAte
	 * @param status
	 * @throws ValidacaoException() no caso de parâmetros invalidos
	 */
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
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, mensagens);
			throw new ValidacaoException(validacao);
		}
		
		if (numCota==null || numCota<=0) {
			this.httpSession.removeAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
			throw new ValidacaoException(TipoMensagem.WARNING, "É obrigatório informar a [Cota].");
		}
		
		if ( (dataDe!=null) && (dataAte!=null) ) {
			this.httpSession.removeAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		    if ( DateUtil.isDataInicialMaiorDataFinal( DateUtil.parseDataPTBR(DateUtil.formatarData(dataDe,"dd/MM/yyyy")) ,DateUtil.parseDataPTBR(DateUtil.formatarData(dataAte,"dd/MM/yyyy")) ) ) {
			    throw new ValidacaoException(TipoMensagem.WARNING, "A data inicial deve ser menor do que a data final.");
		    }
		}
	}
	
	/**
	 * Método responsável por verificar se o boleto está pago ou não pago.
	 * @param nossoNumero
	 */
	public boolean validarBoletoPago(String nossoNumero){
		
		Boleto boleto = boletoService.obterBoletoPorNossoNumero(nossoNumero,false);
		
		if (boleto == null){
			
			BoletoAntecipado boletoantecipado = this.boletoService.obterBoletoEmBrancoPorNossoNumero(nossoNumero);
			
			if (boletoantecipado == null){
			    
			    return false;
			}
			
			return ( boletoantecipado.getStatus()!=StatusDivida.QUITADA);
		}
		
		return (boleto.getStatusCobranca()!=StatusCobranca.PAGO);
	}
	
	/**
	 * Exporta os dados da pesquisa.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	public void exportar(FileType fileType) throws IOException {
		
		FiltroConsultaBoletosCotaDTO filtro = this.obterFiltroExportacao();
		
		if(filtro == null) {
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Filtro inválido."),Constantes.PARAM_MSGS).recursive().serialize();
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro inválido.");
		}
		
		List<BoletoCotaDTO> boletosDTO = this.boletoService.obterBoletosPorCota(filtro);
		
		List<BoletoVO> listaBoletos = new ArrayList<BoletoVO>();
		
		for (BoletoCotaDTO boletoDTO : boletosDTO) {	
			
			BoletoVO boletoVO = new BoletoVO();
			
			boletoVO.setNossoNumero(StringUtils.defaultString(boletoDTO.getNossoNumero()));
			boletoVO.setDataEmissao(boletoDTO.getDataEmissao());
			boletoVO.setDataVencimento(boletoDTO.getDataVencimento());
			boletoVO.setDataPagamento(boletoDTO.getDataPagamento());
			boletoVO.setEncargos(MathUtil.defaultValue(boletoDTO.getEncargos()));
			boletoVO.setValor(MathUtil.defaultValue(boletoDTO.getValor()));
			boletoVO.setTipoBaixa(boletoDTO.getTipoBaixa()!=null?boletoDTO.getTipoBaixa().getDescricao():"");
			boletoVO.setStatus(boletoDTO.getStatusCobranca());
			
			listaBoletos.add(boletoVO);
		}
		
		FileExporter.to("boleto-cota", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				listaBoletos, BoletoVO.class, this.httpResponse);
		
		result.nothing();
		
	}
	
	/*
	 * Obtém o filtro de pesquisa para exportação.
	 */
	private FiltroConsultaBoletosCotaDTO obterFiltroExportacao() {
		
		FiltroConsultaBoletosCotaDTO filtro = (FiltroConsultaBoletosCotaDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {
				
				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
			if (filtro.getNumeroCota() != null) {
				
				Cota cota = this.cotaService.obterPorNumeroDaCota(filtro.getNumeroCota());
				
				if (cota != null) {
					
					Pessoa pessoa = cota.getPessoa();
					
					if (pessoa instanceof PessoaFisica) {
						
						filtro.setNomeCota(((PessoaFisica) pessoa).getNome());
												
					} else if (pessoa instanceof PessoaJuridica) {
						
						filtro.setNomeCota(((PessoaJuridica) pessoa).getRazaoSocial());
					}
				}
			}
		}
		
		return filtro;
	}
}
