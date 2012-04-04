package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.HistoricoSituacaoCotaVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO.OrdenacaoColunasStatusCota;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.service.SituacaoCotaService;
import br.com.abril.nds.util.CellModelKeyValue;
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
 * Controller responsável pela Manutenção do Status da Cota.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/financeiro/manutencaoStatusCota")
public class ManutencaoStatusCotaController {

	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private SituacaoCotaService situacaoCotaService;
	
	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaManutencaoStatusCota";
	
	@Get
	@Path("/")
	public void index() {

		this.carregarCombos();
	}
	
	@Post
	@Path("/pesquisar")
	public void pesquisar(FiltroStatusCotaDTO filtro, String sortorder, String sortname, int page, int rp) {
		
		this.configurarFiltroPesquisa(filtro, sortorder, sortname, page, rp);
		
		List<HistoricoSituacaoCota> listaHistoricoStatusCota =
			this.situacaoCotaService.obterHistoricoStatusCota(filtro);
		
		if (listaHistoricoStatusCota == null || listaHistoricoStatusCota.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			
		} else {

			this.processarHistoricoStatusCota(listaHistoricoStatusCota, filtro);
		}
	}
	
	@Post
	@Path("/novo")
	public void novo() {
		
		
	}
	
	@Post
	@Path("/novo/confirmar")
	public void confirmarNovo() {
		
		
	}
	
	/*
	 * Processa o histórico da situação da cota.
	 *  
	 * @param listaHistoricoStatusCota - lista de histórico
	 * @param filtro - filtro de pesquisa
	 */
	private void processarHistoricoStatusCota(List<HistoricoSituacaoCota> listaHistoricoStatusCota,
											  FiltroStatusCotaDTO filtro) {
		
		List<HistoricoSituacaoCotaVO> listaHistoricoSituacaoCotaVO = new ArrayList<HistoricoSituacaoCotaVO>();
		
		for (HistoricoSituacaoCota historicoSituacaoCota : listaHistoricoStatusCota) {
			
			HistoricoSituacaoCotaVO historicoSituacaoCotaVO = new HistoricoSituacaoCotaVO();
			
			historicoSituacaoCotaVO.setData(
				DateUtil.formatarDataPTBR(historicoSituacaoCota.getDataInicioValidade()));
			
			historicoSituacaoCotaVO.setDescricao(historicoSituacaoCota.getDescricao());
			
			historicoSituacaoCotaVO.setMotivo(historicoSituacaoCota.getMotivo().toString());
			
			historicoSituacaoCotaVO.setUsuario(historicoSituacaoCota.getResponsavel().getNome());
			
			historicoSituacaoCotaVO.setStatusAnterior(historicoSituacaoCota.getSituacaoAnterior().toString());
			
			historicoSituacaoCotaVO.setStatusAtualizado(historicoSituacaoCota.getNovaSituacao().toString());
		
			listaHistoricoSituacaoCotaVO.add(historicoSituacaoCotaVO);
		}
		
		TableModel<CellModelKeyValue<HistoricoSituacaoCotaVO>> tableModel =
			new TableModel<CellModelKeyValue<HistoricoSituacaoCotaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaHistoricoSituacaoCotaVO));

		Long qtdeTotalRegistros = this.situacaoCotaService.obterTotalHistoricoStatusCota(filtro);
		
		if (qtdeTotalRegistros != null) {
		
			tableModel.setTotal(qtdeTotalRegistros.intValue());
		}
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
	
		result.use(Results.json()).from(tableModel, "result").recursive().serialize();
	}
	
	/*
	 * Configura o filtro da pesquisa.
	 * 
	 * @param filtroAtual - filtro de pesquisa atual
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 * 
	 * @return Filtro
	 */
	private void configurarFiltroPesquisa(FiltroStatusCotaDTO filtroAtual, 
										  String sortorder, 
										  String sortname, 
										  int page, 
										  int rp) {

		this.configurarPaginacaoPesquisa(filtroAtual, sortorder, sortname, page, rp);
		
		FiltroStatusCotaDTO filtroSessao =
			(FiltroStatusCotaDTO) 
				this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
		
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtroAtual);
	}
	
	/*
	 * Configura a paginação do filtro de pesquisa.
	 * 
	 * @param filtro - filtro da pesquisa
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 */
	private void configurarPaginacaoPesquisa(FiltroStatusCotaDTO filtro, 
											 String sortorder,
											 String sortname, 
											 int page, 
											 int rp) {
		
		if (filtro != null) {
			
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
	
			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunasStatusCota.values(), sortname));
		}
	}
	
	/*
	 * Carrega os combos utilizados na tela.
	 */
	private void carregarCombos() {
		
		this.carregarComboStatusCota();
		this.carregarComboMotivoStatusCota();
	}
	
	/*
	 * Carrega o combo de status da cota. 
	 */
	private void carregarComboStatusCota() {
		
		List<ItemDTO<SituacaoCadastro, String>> listaSituacoesStatusCota =
			new ArrayList<ItemDTO<SituacaoCadastro, String>>();
		
		for (SituacaoCadastro situacaoCadastro : SituacaoCadastro.values()) {
			
			listaSituacoesStatusCota.add(
				new ItemDTO<SituacaoCadastro, String>(situacaoCadastro, situacaoCadastro.toString())
			);
		}
		
		result.include("listaSituacoesStatusCota", listaSituacoesStatusCota);
	}
	
	/*
	 * Carrega o combo de status da cota. 
	 */
	private void carregarComboMotivoStatusCota() {
		
		List<ItemDTO<MotivoAlteracaoSituacao, String>> listaMotivosStatusCota =
			new ArrayList<ItemDTO<MotivoAlteracaoSituacao, String>>();
		
		for (MotivoAlteracaoSituacao motivoAlteracaoSituacao : MotivoAlteracaoSituacao.values()) {
			
			listaMotivosStatusCota.add(
				new ItemDTO<MotivoAlteracaoSituacao, String>(
					motivoAlteracaoSituacao, motivoAlteracaoSituacao.toString())
			);
		}
		
		result.include("listaMotivosStatusCota", listaMotivosStatusCota);
	}
	
}
