package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ConsultaFollowupCadastroDTO;
import br.com.abril.nds.dto.ConsultaFollowupCadastroParcialDTO;
import br.com.abril.nds.dto.ConsultaFollowupChamadaoDTO;
import br.com.abril.nds.dto.ConsultaFollowupDistribuicaoDTO;
import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.ConsultaFollowupPendenciaNFeDTO;
import br.com.abril.nds.dto.ConsultaFollowupStatusCotaDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupPendenciaNFeDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupStatusCotaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.FollowupCadastroParcialService;
import br.com.abril.nds.service.FollowupCadastroService;
import br.com.abril.nds.service.FollowupChamadaoService;
import br.com.abril.nds.service.FollowupDistribuicaoService;
import br.com.abril.nds.service.FollowupNegociacaoService;
import br.com.abril.nds.service.FollowupPendenciaNFeService;
import br.com.abril.nds.service.FollowupStatusCotaService;
import br.com.abril.nds.service.NotaFiscalEntradaService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações referentes à tela de Follow Up do
 * Sistema.
 * 
 * @author InfoA2 - Alex
 */
@Resource
@Path("/followup")
@Rules(Permissao.ROLE_ADMINISTRACAO_FOLLOW_UP_SISTEMA)
public class FollowupController extends BaseController {

	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;

	@Autowired
	private FollowupChamadaoService followupchamadaoService;
	
	@Autowired
	private FollowupNegociacaoService followupnegociacaoService;

	@Autowired
	private FollowupCadastroService followupcadastroService;	

	@Autowired
	private FollowupStatusCotaService followupstatuscotaService;

	@Autowired
	private FollowupPendenciaNFeService followuppendencianfeService;
	
	@Autowired
	private FollowupCadastroParcialService followupCadastroParcialService;
	
	@Autowired
	private FollowupDistribuicaoService followupDistribuicaoService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private NotaFiscalEntradaService notaFiscalEntradaService;
	
	@Autowired
    private NotaFiscalService notaFiscalService;
    
	
	private static final String FILTRO_FOLLOWUP_CONSIGNADOS_SESSION_ATTRIBUTE = "filtroFollowupConsignados";
	private static final String FILTRO_FOLLOWUP_PENDENCIA_NFE_SESSION_ATTRIBUTE = "filtroFollowupPendenciaNFE";
	private static final String FILTRO_FOLLOWUP_CADASTRO_SESSION_ATTRIBUTE = "filtroFollowupCadastro";
	private static final String FILTRO_FOLLOWUP_CADASTRO_PARCIAL_SESSION_ATTRIBUTE = "filtroFollowupCadastroParcial";
	private static final String FILTRO_FOLLOWUP_STATUS_COTA_SESSION_ATTRIBUTE = "filtroFollowupStatusCota";
	private static final String FILTRO_FOLLOWUP_NEGOCIACAO_SESSION_ATTRIBUTE = "filtroFollowupNegociacao";
	
	//private static final String QTD_REGISTROS_FOLLOWUP_CONSIGNADOS_SESSION_ATTRIBUTE = "qtdRegistrosFollowupConsignados";

	private BigDecimal valorConsignadoSuspensaoCotas;
	private int quantidadeDiasSuspensaoCotas;

	@Path("/")
	public void index() {
		session.setAttribute(FILTRO_FOLLOWUP_CONSIGNADOS_SESSION_ATTRIBUTE, null);
	}
	
	@Post
	@Path("/pesquisaDadosChamadao")
	public void pesquisaDadosChamadao( String sortorder, String sortname, int page, int rp ) {
    	FiltroFollowupChamadaoDTO filtroChamadao = new FiltroFollowupChamadaoDTO(Calendar.getInstance().getTime(), 0, getValorConsignadoSuspensaoCotas());
		
    	this.recuperarParametros(filtroChamadao);		
		
    	filtroChamadao.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.tratarFiltroChamadao(filtroChamadao);
		TableModel<CellModelKeyValue<ConsultaFollowupChamadaoDTO>> tableModel = efetuarConsultaChamadao(filtroChamadao);
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<ConsultaFollowupChamadaoDTO>> efetuarConsultaChamadao(FiltroFollowupChamadaoDTO filtro) {
		
		List<ConsultaFollowupChamadaoDTO> listadechamadao = this.followupchamadaoService.obterConsignados(filtro);
		
		TableModel<CellModelKeyValue<ConsultaFollowupChamadaoDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaFollowupChamadaoDTO>>();
		
		Integer totalRegistros = listadechamadao.size();
		
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Chamadao: Não foram encontrados resultados para Follow Up.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listadechamadao));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}

	@Post
	@Path("/pesquisaDadosNegociacao")
	public void pesquisaDadosNegociacao( String sortorder, String sortname, int page, int rp ) {
				
		FiltroFollowupNegociacaoDTO filtroNegociacao = new FiltroFollowupNegociacaoDTO(new Date());
		
		filtroNegociacao.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		filtroNegociacao.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroFollowupNegociacaoDTO.OrdenacaoColuna.values(),sortname));
		
		tratarFiltroNegociacao(filtroNegociacao);
		
		TableModel<CellModelKeyValue<ConsultaFollowupNegociacaoDTO>>  negociacoes = efetuarConsultaDadosNegociacao(filtroNegociacao);
		
		result.use(Results.json()).withoutRoot().from(negociacoes).recursive().serialize();
	}
	
	private void tratarFiltroNegociacao(FiltroFollowupNegociacaoDTO filtroNegociacao) {
		
		FiltroFollowupNegociacaoDTO filtroSession = (FiltroFollowupNegociacaoDTO) session.getAttribute(FILTRO_FOLLOWUP_NEGOCIACAO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroNegociacao)) {
			
			filtroNegociacao.getPaginacao().setPaginaAtual(1);
		}
		session.setAttribute(FILTRO_FOLLOWUP_NEGOCIACAO_SESSION_ATTRIBUTE, filtroNegociacao);
	}

	private TableModel<CellModelKeyValue<ConsultaFollowupNegociacaoDTO>> efetuarConsultaDadosNegociacao(FiltroFollowupNegociacaoDTO filtroNegociacao) {
		
		Long totalRegistros = this.followupnegociacaoService.obterQuantidadeNegociacaoFollowup(filtroNegociacao);
		
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Negociação: Não foram encontrados resultados para Follow Up.");
		}

		List<ConsultaFollowupNegociacaoDTO> listacadastral = this.followupnegociacaoService.obterNegociacoes(filtroNegociacao);
		
		this.formatarCamposNegociacao(listacadastral);
		
		TableModel<CellModelKeyValue<ConsultaFollowupNegociacaoDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaFollowupNegociacaoDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listacadastral));
		
		tableModel.setPage((filtroNegociacao.getPaginacao()== null)?1:filtroNegociacao.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros.intValue());
		
		return tableModel;
	}
	
	private void formatarCamposNegociacao(List<ConsultaFollowupNegociacaoDTO> lista){
		
		if(lista == null || lista.isEmpty()){
			return;
		}
		
		for(ConsultaFollowupNegociacaoDTO dto : lista ){
			dto.setDescricaoFormaPagamento( (dto.getTipoCobranca() == null)? "" : dto.getTipoCobranca().getDescricao());
			dto.setDescricaoParcelamento(dto.getNumeroParcelaAtual() + " / " + dto.getQuantidadeParcelas());
			dto.setValorParcelaFormatado(CurrencyUtil.formatarValor(dto.getValorParcela()));
			dto.setDataVencimentoFormatada(DateUtil.formatarDataPTBR(dto.getDataVencimento()));
		}
	}
	
	public void cancelarNegociacao(Long idNegociacao) {
		
		followupnegociacaoService.cancelarBaixaNegociacao(idNegociacao);
		
		result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Reversão de pagamento da negociação efetuada com sucesso!"),
								"result").recursive().serialize();
	}	
	
	@Path("/pesquisaDadosStatusCota")
	public void pesquisaDadosStatusCota( String sortorder, String sortname, int page, int rp ) {
		FiltroFollowupStatusCotaDTO filtroStatusCota = new FiltroFollowupStatusCotaDTO();
		
		filtroStatusCota.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.tratarFiltroStatusCota(filtroStatusCota);
		
		TableModel<CellModelKeyValue<ConsultaFollowupStatusCotaDTO>> tableModel = efetuarConsultaDadosStatusCota(filtroStatusCota);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<ConsultaFollowupStatusCotaDTO>> efetuarConsultaDadosStatusCota(
			FiltroFollowupStatusCotaDTO filtroStatusCota) {
		
		List<ConsultaFollowupStatusCotaDTO> listacadastral = this.followupstatuscotaService.obterStatusCota(filtroStatusCota);
		
		for(ConsultaFollowupStatusCotaDTO dto: listacadastral){
			String periodo = dto.getDataInicioPeriodo() + (dto.getDataFimPeriodo()!=null && !dto.getDataFimPeriodo().isEmpty()?" até " + dto.getDataFimPeriodo():"");
			dto.setPeriodoStatus(periodo);
		}
		
		TableModel<CellModelKeyValue<ConsultaFollowupStatusCotaDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaFollowupStatusCotaDTO>>();
		
		Integer totalRegistros = listacadastral.size();
		
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Cadastro: Não foram encontrados resultados para Follow Up.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listacadastral));
		
		tableModel.setPage(filtroStatusCota.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}

	@Path("/pesquisaDadosCadastrais")
	public void pesquisaDadosCadastrais( String sortorder, String sortname, int page, int rp ) {	
		
		FiltroFollowupCadastroDTO filtro = new FiltroFollowupCadastroDTO();
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.tratarFiltroCadastro(filtro);
		
		TableModel<CellModelKeyValue<ConsultaFollowupCadastroDTO>> tableModel = efetuarConsultaDadosCadastral(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<ConsultaFollowupCadastroDTO>> efetuarConsultaDadosCadastral(FiltroFollowupCadastroDTO filtro) {
		
		List<ConsultaFollowupCadastroDTO> listasdependencias = this.followupcadastroService.obterCadastros(filtro);
		
		TableModel<CellModelKeyValue<ConsultaFollowupCadastroDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaFollowupCadastroDTO>>();
		
		Integer totalRegistros = listasdependencias.size();
		
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Pendencias NF-e Encalhe: Não foram encontrados resultados para Follow Up.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listasdependencias));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}

	@Path("/pesquisaDistribuicaoCotasAjustes")
	public void pesquisaDistribuicaoCotasAjustes(String sortorder, String sortname, int page, int rp){
		
		ConsultaFollowupDistribuicaoDTO dto = new ConsultaFollowupDistribuicaoDTO();
		
		dto.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		TableModel<CellModelKeyValue<ConsultaFollowupDistribuicaoDTO>> tableModel = efetuarConsultaDistribuicao(dto);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<ConsultaFollowupDistribuicaoDTO>> efetuarConsultaDistribuicao (ConsultaFollowupDistribuicaoDTO dto) {
		
		List<ConsultaFollowupDistribuicaoDTO> consultaDistribuicao = this.followupDistribuicaoService.obterCotas(dto);
		
		TableModel<CellModelKeyValue<ConsultaFollowupDistribuicaoDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaFollowupDistribuicaoDTO>>();
		
		Integer totalRegistros = consultaDistribuicao.size();
		
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foram encontrados resultados para Follow Up.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(consultaDistribuicao));
		
		tableModel.setPage(dto.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(15);
		
		return tableModel;
	}
	
	@Path("/pesquisaDadosPendenciaNFEEncalhe")
	public void pesquisaDadosPendenciaNFEEncalhe( String sortorder, String sortname, int page, int rp ) {
		
		FiltroFollowupPendenciaNFeDTO filtroPendenciaNFEEncalhe = new FiltroFollowupPendenciaNFeDTO(Calendar.getInstance().getTime());
		
		filtroPendenciaNFEEncalhe.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.tratarFiltroPendenciaNFE(filtroPendenciaNFEEncalhe);
		
		TableModel<CellModelKeyValue<ConsultaFollowupPendenciaNFeDTO>> tableModel = consultaPendenciaNFEEncalhe(filtroPendenciaNFEEncalhe);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();		
	}
	
	
	
	@Post
	@Path("/pesquisaDadosCadastroParcial")
	public void pesquisaDadosCadastroParcial( String sortorder, String sortname, int page, int rp ) {
		FiltroFollowupCadastroParcialDTO filtro = new FiltroFollowupCadastroParcialDTO();
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		session.setAttribute(FILTRO_FOLLOWUP_CADASTRO_PARCIAL_SESSION_ATTRIBUTE, filtro);
		
		TableModel<CellModelKeyValue<ConsultaFollowupCadastroParcialDTO>> tableModel = efetuarConsultaDadosCadastralParcial(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	

	
    private TableModel<CellModelKeyValue<ConsultaFollowupCadastroParcialDTO>> efetuarConsultaDadosCadastralParcial(FiltroFollowupCadastroParcialDTO filtro) {
		
		List<ConsultaFollowupCadastroParcialDTO> lista = this.followupCadastroParcialService.obterCadastrosParcial(filtro);
		
		TableModel<CellModelKeyValue<ConsultaFollowupCadastroParcialDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaFollowupCadastroParcialDTO>>();
		
		Integer totalRegistros = filtro.getPaginacao().getQtdResultadosTotal();
		
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Cadastro Parcial: Não foram encontrados resultados para Follow Up.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(lista));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	
	
	private TableModel<CellModelKeyValue<ConsultaFollowupPendenciaNFeDTO>> consultaPendenciaNFEEncalhe(FiltroFollowupPendenciaNFeDTO filtro) {

	    TableModel<CellModelKeyValue<ConsultaFollowupPendenciaNFeDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaFollowupPendenciaNFeDTO>>();
		
		Long qtdeRegistrosPendencias = followuppendencianfeService.qtdeRegistrosPendencias(filtro);
		
		if(qtdeRegistrosPendencias == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Pendencias NF-e Encalhe: Não foram encontrados resultados para Follow Up.");
		}
		
		List<ConsultaFollowupPendenciaNFeDTO> listasdependencias = this.followuppendencianfeService.consultaPendenciaNFEEncalhe(filtro);

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listasdependencias));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(qtdeRegistrosPendencias.intValue());
		
		return tableModel;
		
	}

	private void recuperarParametros(FiltroFollowupChamadaoDTO filtro) {
		// TODO: colocar a recuperacao da tabela parametros distribuidor.
		this.setValorConsignadoSuspensaoCotas(new BigDecimal(0));
		this.setQuantidadeDiasSuspensaoCotas(0);
		filtro.setValorConsignadoLimite(this.getValorConsignadoSuspensaoCotas());
		filtro.setQuantidadeDiasSuspenso(this.getQuantidadeDiasSuspensaoCotas());
	}

	private void tratarFiltroChamadao(FiltroFollowupChamadaoDTO filtroParam) {

		FiltroFollowupChamadaoDTO filtroSession = (FiltroFollowupChamadaoDTO) session.getAttribute(FILTRO_FOLLOWUP_CONSIGNADOS_SESSION_ATTRIBUTE);
			
		if (filtroSession != null && filtroSession.equals(filtroParam)) {
			
			filtroParam.getPaginacao().setPaginaAtual(1);
		}
		session.setAttribute(FILTRO_FOLLOWUP_CONSIGNADOS_SESSION_ATTRIBUTE, filtroParam);
		
	}
	
	private void tratarFiltroStatusCota(FiltroFollowupStatusCotaDTO filtroParam){

		FiltroFollowupStatusCotaDTO filtroSession = (FiltroFollowupStatusCotaDTO) session.getAttribute(FILTRO_FOLLOWUP_STATUS_COTA_SESSION_ATTRIBUTE);
			
		if (filtroSession != null && filtroSession.equals(filtroParam)) {
			
			filtroParam.getPaginacao().setPaginaAtual(1);
		}
		session.setAttribute(FILTRO_FOLLOWUP_STATUS_COTA_SESSION_ATTRIBUTE, filtroParam);
		
	}
	
	private void tratarFiltroPendenciaNFE(FiltroFollowupPendenciaNFeDTO filtroParam) {

		FiltroFollowupPendenciaNFeDTO filtroSession = (FiltroFollowupPendenciaNFeDTO) session.getAttribute(FILTRO_FOLLOWUP_PENDENCIA_NFE_SESSION_ATTRIBUTE);
			
		if (filtroSession != null && filtroSession.equals(filtroParam)) {
			
			filtroParam.getPaginacao().setPaginaAtual(1);
		}
		session.setAttribute(FILTRO_FOLLOWUP_PENDENCIA_NFE_SESSION_ATTRIBUTE, filtroParam);
		
	}
	
	private void tratarFiltroCadastro(FiltroFollowupCadastroDTO filtroParam) {

		FiltroFollowupCadastroDTO filtroSession = (FiltroFollowupCadastroDTO) session.getAttribute(FILTRO_FOLLOWUP_CADASTRO_SESSION_ATTRIBUTE);
			
		if (filtroSession != null && filtroSession.equals(filtroParam)) {
			
			filtroParam.getPaginacao().setPaginaAtual(1);
		}
		session.setAttribute(FILTRO_FOLLOWUP_CADASTRO_SESSION_ATTRIBUTE, filtroParam);
		
	}
		
	/**
	 * Imprime Negociacao
	 * @param fileType
	 * @throws IOException
	 */
	@Get
	public void imprimirNegociacao(FileType fileType) throws IOException {
		FiltroFollowupNegociacaoDTO filtroNegociacao = (FiltroFollowupNegociacaoDTO) session.getAttribute(FILTRO_FOLLOWUP_NEGOCIACAO_SESSION_ATTRIBUTE);

		if (filtroNegociacao != null) {
			removePaginacao(filtroNegociacao.getPaginacao());
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao imprimir arquivo");
		}
		
		List<ConsultaFollowupNegociacaoDTO> lista = this.followupnegociacaoService.obterNegociacoes(filtroNegociacao);
		
		this.formatarCamposNegociacao(lista);
		
		if(lista.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
		FileExporter.to("FollowUp_dados_negociacao", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroNegociacao, lista, ConsultaFollowupNegociacaoDTO.class, this.httpResponse);
		
		result.nothing();
	}

	/**
	 * Imprime Chamadao
	 * 
	 * @param fileType
	 * @throws IOException
	 */
	@Get
	public void imprimirChamadao(FileType fileType) throws IOException {
		FiltroFollowupChamadaoDTO filtro = (FiltroFollowupChamadaoDTO) session.getAttribute(FILTRO_FOLLOWUP_CONSIGNADOS_SESSION_ATTRIBUTE);
		if (filtro != null) {
			removePaginacao(filtro.getPaginacao());
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao imprimir arquivo");
		}
		
		List<ConsultaFollowupChamadaoDTO> listadechamadao = this.followupchamadaoService.obterConsignados(filtro);
		
		if(listadechamadao.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
		FileExporter.to("FollowUp_Chamadao", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, 
				listadechamadao, ConsultaFollowupChamadaoDTO.class, this.httpResponse);
		
		result.nothing();
	}

	/**
	 * Imprime Pendencias NFe
	 * 
	 * @param fileType
	 * @throws IOException
	 */
	@Get
	public void imprimirPendenciasNFe(FileType fileType) throws IOException {
		
	    FiltroFollowupPendenciaNFeDTO filtro = (FiltroFollowupPendenciaNFeDTO) session.getAttribute(FILTRO_FOLLOWUP_CONSIGNADOS_SESSION_ATTRIBUTE);
		
	    if (filtro != null) {
			removePaginacao(filtro.getPaginacao());
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao imprimir arquivo");
		}
		
		List<ConsultaFollowupPendenciaNFeDTO> listasdependencias = this.followuppendencianfeService.consultaPendenciaNFEEncalhe(filtro);
		
		if(listasdependencias.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
		FileExporter.to("FollowUp_pendencias_nfe", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, listasdependencias, ConsultaFollowupPendenciaNFeDTO.class, this.httpResponse);
		
		result.nothing();
	}

	/**
	 * Imprime Alteracao Status Cota
	 * 
	 * @param fileType
	 * @throws IOException
	 */
	@Get
	public void imprimirAlteracaoStatusCota(FileType fileType) throws IOException {
	    
		FiltroFollowupStatusCotaDTO filtro = (FiltroFollowupStatusCotaDTO) session.getAttribute(FILTRO_FOLLOWUP_STATUS_COTA_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			removePaginacao(filtro.getPaginacao());
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao imprimir arquivo");
		}
		
		List<ConsultaFollowupStatusCotaDTO> listacadastral = this.followupstatuscotaService.obterStatusCota(filtro);
		
		for(ConsultaFollowupStatusCotaDTO dto: listacadastral){
			String periodo = dto.getDataInicioPeriodo() + " até " + dto.getDataFimPeriodo();
			dto.setPeriodoStatus(periodo);
		}
		
		if(listacadastral.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
		FileExporter.to("FollowUp_Status_Cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, 
				listacadastral, ConsultaFollowupStatusCotaDTO.class, this.httpResponse);
		
		result.nothing();
	}

	/**
	 * Imprime Atualizacao Cadastral
	 * 
	 * @param fileType
	 * @throws IOException
	 */
	@Get
	public void imprimirAtualizacaoCadastral(FileType fileType)
			throws IOException {
		FiltroFollowupCadastroDTO filtro = (FiltroFollowupCadastroDTO) session.getAttribute(FILTRO_FOLLOWUP_CADASTRO_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			removePaginacao(filtro.getPaginacao());
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao imprimir arquivo");
		}
		
		List<ConsultaFollowupCadastroDTO> listasdependencias = this.followupcadastroService.obterCadastros(filtro);
		
		if(listasdependencias.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
		FileExporter.to("FollowUp_dados_cadastrais", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, 
				listasdependencias, ConsultaFollowupCadastroDTO.class, this.httpResponse);
		
		result.nothing();
	}

	/**
	 * Imprime o Cadastro Parcial
	 * 
	 * @param fileType
	 * @throws IOException
	 */
	@Get
	public void imprimirCadastroParcial(FileType fileType) throws IOException {
		FiltroFollowupCadastroParcialDTO filtro = (FiltroFollowupCadastroParcialDTO) session.getAttribute(FILTRO_FOLLOWUP_CADASTRO_PARCIAL_SESSION_ATTRIBUTE);
		if (filtro != null) {
			removePaginacao(filtro.getPaginacao());
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao imprimir arquivo");
		}
		
		List<ConsultaFollowupCadastroParcialDTO> lista = this.followupCadastroParcialService.obterCadastrosParcial(filtro);
		
		if(lista.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
		FileExporter.to("FollowUp_dados_cadastrais", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro,  
				lista, ConsultaFollowupCadastroParcialDTO.class, this.httpResponse);
		
		result.nothing();
	}

	@Post
    @Path("/cadastrarNota")
    @Rules(Permissao.ROLE_NFE_ENTRADA_NFE_FOLLWOUP_ALTERACAO)  
    public void cadastrarNota(final NotaFiscalEntradaCota nota, final Integer numeroCota, final Long idControleConferenciaEncalheCota){
        this.notaFiscalEntradaService.inserirNotaFiscal(nota, numeroCota, idControleConferenciaEncalheCota);

        final ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Cadastro efetuado com sucesso.");
        
        result.use(Results.json()).from(validacao, "result").recursive().serialize();
        
    }
	
	@Path("/pesquisarItensPorNota")
    public void pesquisarItensPorNota(final long idControleConferencia, final String sortorder, final String sortname, final int page, final int rp){
        
        final Integer total = this.notaFiscalService.qtdeNota(idControleConferencia);
        
        if (total <= 0) {       

            throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
            
        }
        
        final List<ItemNotaFiscalPendenteDTO> listItemNota = this.notaFiscalService.buscarItensPorNota(idControleConferencia, sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page * rp - rp, rp);
        
        result.use(FlexiGridJson.class).from(listItemNota).page(page).total(total).serialize();
        
    }
	
	/**
	 * Remove os valores da paginação
	 * 
	 * @param paginacaoVO
	 */
	private void removePaginacao(PaginacaoVO paginacaoVO) {
		if (paginacaoVO != null) {
			
			paginacaoVO.setPaginaAtual(null);
			paginacaoVO.setQtdResultadosPorPagina(null);
		}
	}
	
	public BigDecimal getValorConsignadoSuspensaoCotas() {
		return valorConsignadoSuspensaoCotas;
	}

	public void setValorConsignadoSuspensaoCotas(
			BigDecimal valorConsignadoSuspensaoCotas) {
		this.valorConsignadoSuspensaoCotas = valorConsignadoSuspensaoCotas;
	}

	public int getQuantidadeDiasSuspensaoCotas() {
		return quantidadeDiasSuspensaoCotas;
	}

	public void setQuantidadeDiasSuspensaoCotas(int quantidadeDiasSuspensaoCotas) {
		this.quantidadeDiasSuspensaoCotas = quantidadeDiasSuspensaoCotas;
	}
}

