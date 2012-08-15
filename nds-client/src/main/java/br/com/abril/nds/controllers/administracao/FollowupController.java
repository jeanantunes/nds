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
import br.com.abril.nds.dto.ConsultaFollowupCadastroDTO;
import br.com.abril.nds.dto.ConsultaFollowupChamadaoDTO;
import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.ConsultaFollowupPendenciaNFeDTO;
import br.com.abril.nds.dto.ConsultaFollowupStatusCotaDTO;
import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupPendenciaNFeDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.FollowupCadastroService;
import br.com.abril.nds.service.FollowupChamadaoService;
import br.com.abril.nds.service.FollowupNegociacaoService;
import br.com.abril.nds.service.FollowupPendenciaNFeService;
import br.com.abril.nds.service.FollowupStatusCotaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
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
public class FollowupController {

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
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	private static final String FILTRO_FOLLOWUP_CONSIGNADOS_SESSION_ATTRIBUTE = "filtroFollowupConsignados";
	private static final String FILTRO_FOLLOWUP_PENDENCIA_NFE_SESSION_ATTRIBUTE = "filtroFollowupPendenciaNFE";
	private static final String FILTRO_FOLLOWUP_CADASTRO_SESSION_ATTRIBUTE = "filtroFollowupCadastro";
	private static final String FILTRO_FOLLOWUP_STATUS_COTA_SESSION_ATTRIBUTE = "filtroFollowupStatusCota";
	//private static final String QTD_REGISTROS_FOLLOWUP_CONSIGNADOS_SESSION_ATTRIBUTE = "qtdRegistrosFollowupConsignados";

	private BigDecimal valorConsignadoSuspensaoCotas;
	private int quantidadeDiasSuspensaoCotas;

	@Path("/")
	@Rules(Permissao.ROLE_ADMINISTRACAO_FOLLOW_UP_SISTEMA)
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
	
	private TableModel<CellModelKeyValue<ConsultaFollowupChamadaoDTO>> efetuarConsultaChamadao(
			FiltroFollowupChamadaoDTO filtro) {
		
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
		/**
		 * EMS 179 = SEM DEFINICAO em 2012-06-18.
		 * 
		FiltroFollowupNegociacaoDTO filtroNegociacao = 
    		new FiltroFollowupNegociacaoDTO(Calendar.getInstance().getTime());
		
		TableModel<CellModelKeyValue<ConsultaFollowupNegociacaoDTO>> tableModel = efetuarConsultaDadosNegociacao(filtroNegociacao);
		 * 
		 */
		throw new ValidacaoException(TipoMensagem.WARNING, "EMS 179 = SEM DEFINICAO em 2012-06-18.");
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
			String periodo = dto.getDataInicioPeriodo() + " até " + dto.getDataFimPeriodo();
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
		
		tableModel.setTotal(15);
		
		return tableModel;
	}

	@Path("/pesquisaDadosPendenciaNFEEncalhe")
	public void pesquisaDadosPendenciaNFEEncalhe( String sortorder, String sortname, int page, int rp ) {
		
		FiltroFollowupPendenciaNFeDTO filtroPendenciaNFEEncalhe = new FiltroFollowupPendenciaNFeDTO(Calendar.getInstance().getTime());
		
		filtroPendenciaNFEEncalhe.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.tratarFiltroPendenciaNFE(filtroPendenciaNFEEncalhe);
		
		TableModel<CellModelKeyValue<ConsultaFollowupPendenciaNFeDTO>> tableModel = efetuarConsultaDadosPendenciaNFEEncalhe(filtroPendenciaNFEEncalhe);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();		
	}
	
	private TableModel<CellModelKeyValue<ConsultaFollowupPendenciaNFeDTO>> efetuarConsultaDadosPendenciaNFEEncalhe(FiltroFollowupPendenciaNFeDTO filtro) {
		
		List<ConsultaFollowupPendenciaNFeDTO> listasdependencias = this.followuppendencianfeService.obterPendencias(filtro);
		
		TableModel<CellModelKeyValue<ConsultaFollowupPendenciaNFeDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaFollowupPendenciaNFeDTO>>();
		
		Integer totalRegistros = listasdependencias.size();
		
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Pendencias NF-e Encalhe: Não foram encontrados resultados para Follow Up.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listasdependencias));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(15);
		
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
		
	@Get
	public void exportar(FileType fileType, String tipoExportacao) throws IOException {
		
		if(tipoExportacao.equals("negociacao")){
			
		}else if(tipoExportacao.equals("chamadao")){
			FiltroFollowupChamadaoDTO filtro = (FiltroFollowupChamadaoDTO) session.getAttribute(FILTRO_FOLLOWUP_CONSIGNADOS_SESSION_ATTRIBUTE);
			List<ConsultaFollowupChamadaoDTO> listadechamadao = this.followupchamadaoService.obterConsignados(filtro);
			
			if(listadechamadao.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("FollowUp_Chamadao", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					listadechamadao, ConsultaFollowupChamadaoDTO.class, this.httpResponse);
		}else if(tipoExportacao.equals("pendenciaNFE")){
			
			FiltroFollowupPendenciaNFeDTO filtro = (FiltroFollowupPendenciaNFeDTO) session.getAttribute(FILTRO_FOLLOWUP_CONSIGNADOS_SESSION_ATTRIBUTE);
			
			List<ConsultaFollowupPendenciaNFeDTO> listasdependencias = this.followuppendencianfeService.obterPendencias(filtro);
			
			if(listasdependencias.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("FollowUp_pendencias_nfe", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					listasdependencias, ConsultaFollowupPendenciaNFeDTO.class, this.httpResponse);
		}else if(tipoExportacao.equals("alteracao")){
			
			FiltroFollowupStatusCotaDTO filtro = (FiltroFollowupStatusCotaDTO) session.getAttribute(FILTRO_FOLLOWUP_STATUS_COTA_SESSION_ATTRIBUTE);
			
			
			List<ConsultaFollowupStatusCotaDTO> listacadastral = this.followupstatuscotaService.obterStatusCota(filtro);
			
			for(ConsultaFollowupStatusCotaDTO dto: listacadastral){
				String periodo = dto.getDataInicioPeriodo() + " até " + dto.getDataFimPeriodo();
				dto.setPeriodoStatus(periodo);
			}
			
			
			if(listacadastral.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("FollowUp_Status_Cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					listacadastral, ConsultaFollowupStatusCotaDTO.class, this.httpResponse);
			
		}else if(tipoExportacao.equals("atualizacao")){
			
			FiltroFollowupCadastroDTO filtro = (FiltroFollowupCadastroDTO) session.getAttribute(FILTRO_FOLLOWUP_CADASTRO_SESSION_ATTRIBUTE);
			
			
			List<ConsultaFollowupCadastroDTO> listasdependencias = this.followupcadastroService.obterCadastros(filtro);
			
			if(listasdependencias.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("FollowUp_dados_cadastrais", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					listasdependencias, ConsultaFollowupCadastroDTO.class, this.httpResponse);
		}
		
		result.nothing();
	}
	
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
	}
	
	public Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Lazaro Jornaleiro");
		return usuario;
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

