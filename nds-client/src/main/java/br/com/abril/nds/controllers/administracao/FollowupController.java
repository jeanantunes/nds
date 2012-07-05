package br.com.abril.nds.controllers.administracao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaFollowupCadastroDTO;
import br.com.abril.nds.dto.ConsultaFollowupChamadaoDTO;
import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.ConsultaFollowupPendenciaNFeDTO;
import br.com.abril.nds.dto.ConsultaFollowupStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupPendenciaNFeDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupStatusCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.FollowupCadastroService;
import br.com.abril.nds.service.FollowupChamadaoService;
import br.com.abril.nds.service.FollowupNegociacaoService;
import br.com.abril.nds.service.FollowupPendenciaNFeService;
import br.com.abril.nds.service.FollowupStatusCotaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
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
	
	private static final String FILTRO_FOLLOWUP_CONSIGNADOS_SESSION_ATTRIBUTE = "filtroFollowupConsignados";
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
		this.tratarFiltro(filtroChamadao);		
		TableModel<CellModelKeyValue<ConsultaFollowupChamadaoDTO>> tableModel = efetuarConsultaChamadao(filtroChamadao);
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
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
		FiltroFollowupStatusCotaDTO filtroStatusCota = 
    		new FiltroFollowupStatusCotaDTO(Calendar.getInstance().getTime());
		
		TableModel<CellModelKeyValue<ConsultaFollowupStatusCotaDTO>> tableModel = efetuarConsultaDadosStatusCota(filtroStatusCota);
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	@Path("/pesquisaDadosCadastrais")
	public void pesquisaDadosCadastrais( String sortorder, String sortname, int page, int rp ) {		
	}

	@Path("/pesquisaDadosPendenciaNFEEncalhe")
	public void pesquisaDadosPendenciaNFEEncalhe( String sortorder, String sortname, int page, int rp ) {
		
		FiltroFollowupPendenciaNFeDTO filtroPendenciaNFEEncalhe = new FiltroFollowupPendenciaNFeDTO(Calendar.getInstance().getTime());
		
		filtroPendenciaNFEEncalhe.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		TableModel<CellModelKeyValue<ConsultaFollowupPendenciaNFeDTO>> tableModel = efetuarConsultaDadosPendenciaNFEEncalhe(filtroPendenciaNFEEncalhe);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();		
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

	private void recuperarParametros(FiltroFollowupChamadaoDTO filtro) {
		// TODO: colocar a recuperacao da tabela parametros distribuidor.		
		this.setValorConsignadoSuspensaoCotas(new BigDecimal(0));
		this.setQuantidadeDiasSuspensaoCotas(0);
		filtro.setValorConsignadoLimite(this.getValorConsignadoSuspensaoCotas());
		filtro.setQuantidadeDiasSuspenso(this.getQuantidadeDiasSuspensaoCotas());
	}

	private void tratarFiltro(FiltroFollowupChamadaoDTO filtroParam) {
		FiltroFollowupChamadaoDTO filtroSession = (FiltroFollowupChamadaoDTO) session
				.getAttribute(FILTRO_FOLLOWUP_CONSIGNADOS_SESSION_ATTRIBUTE);

		if (filtroSession != null && filtroSession.equals(filtroParam)) {

			filtroParam.getPaginacao().setPaginaAtual(1);
		}

		session.setAttribute(FILTRO_FOLLOWUP_CONSIGNADOS_SESSION_ATTRIBUTE, filtroParam);
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

	private TableModel<CellModelKeyValue<ConsultaFollowupNegociacaoDTO>> efetuarConsultaDadosNegociacao(
			FiltroFollowupNegociacaoDTO filtro) {
		
		List<ConsultaFollowupNegociacaoDTO> listadenegociacao = this.followupnegociacaoService.obterNegociacoes(filtro) ;
		
		TableModel<CellModelKeyValue<ConsultaFollowupNegociacaoDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaFollowupNegociacaoDTO>>();
		
		Integer totalRegistros = listadenegociacao.size();
		
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Negociacao: Não foram encontrados resultados para Follow Up.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listadenegociacao));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	
	private TableModel<CellModelKeyValue<ConsultaFollowupPendenciaNFeDTO>> efetuarConsultaDadosPendenciaNFEEncalhe(
			FiltroFollowupPendenciaNFeDTO filtro) {
		
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

	
	private TableModel<CellModelKeyValue<ConsultaFollowupCadastroDTO>> efetuarConsultaDadosCadastrais(
			FiltroFollowupCadastroDTO filtro) {
		
		List<ConsultaFollowupCadastroDTO> listacadastral = this.followupcadastroService.obterCadastros(filtro);
		
		TableModel<CellModelKeyValue<ConsultaFollowupCadastroDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaFollowupCadastroDTO>>();
		
		Integer totalRegistros = listacadastral.size();
		
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Cadastro: Não foram encontrados resultados para Follow Up.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listacadastral));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	private TableModel<CellModelKeyValue<ConsultaFollowupStatusCotaDTO>> efetuarConsultaDadosStatusCota(
			FiltroFollowupStatusCotaDTO filtroStatusCota) {
		
		List<ConsultaFollowupStatusCotaDTO> listacadastral = this.followupstatuscotaService.obterStatusCota(filtroStatusCota);
		
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
}

