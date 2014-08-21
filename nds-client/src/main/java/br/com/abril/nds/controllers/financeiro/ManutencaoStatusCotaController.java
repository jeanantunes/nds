package br.com.abril.nds.controllers.financeiro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.client.vo.HistoricoSituacaoCotaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO.OrdenacaoColunasStatusCota;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.SituacaoCotaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;
import br.com.abril.nds.vo.ValidacaoVO;
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
@SuppressWarnings("deprecation")
@Resource
@Path("/financeiro/manutencaoStatusCota")
@Rules(Permissao.ROLE_FINANCEIRO_MANUTENCAO_STATUS_COTA)
public class ManutencaoStatusCotaController extends BaseController {

	@Autowired
	private Result result;
	
	@Autowired
	private SituacaoCotaService situacaoCotaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private DividaService dividaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Get
	@Path("/")
	public void index() {

		this.carregarCombos();
	}
	
	@Post
	@Path("/pesquisar")
	public void pesquisar(FiltroStatusCotaDTO filtro, String sortorder, String sortname, int page, int rp) {
		
		if (filtro.getPeriodo() != null){
			this.validarPeriodoHistoricoStatusCota(filtro.getPeriodo());
		}
		
		this.configurarPaginacaoPesquisa(filtro, sortorder, sortname, page, rp);
		
		Long qtdeTotalRegistros = this.situacaoCotaService.obterTotalHistoricoStatusCota(filtro);
		
		if (qtdeTotalRegistros == null || qtdeTotalRegistros == 0L){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {
			
			List<HistoricoSituacaoCotaVO> listaHistoricoStatusCota =
					this.situacaoCotaService.obterHistoricoStatusCota(filtro);
			
			TableModel<CellModelKeyValue<HistoricoSituacaoCotaVO>> tableModel =
				new TableModel<CellModelKeyValue<HistoricoSituacaoCotaVO>>();
			
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaHistoricoStatusCota));

			tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
			
			tableModel.setTotal(qtdeTotalRegistros.intValue());
		
			result.use(Results.json()).from(tableModel, "result").recursive().serialize();
		}
	}
	
	@Post
	@Path("/novo")
	@Rules(Permissao.ROLE_FINANCEIRO_MANUTENCAO_STATUS_COTA_ALTERACAO)
	public void novo(FiltroStatusCotaDTO filtro) {
		
		 Cota cota = this.validarDadosCota(filtro);
		 
		 CotaVO cotaVO = 
			new CotaVO(cota.getNumeroCota(), PessoaUtil.obterNomeExibicaoPeloTipo(cota.getPessoa()));
		
		 cotaVO.setCodigoBox(cota.getBox()!=null?cota.getBox().getCodigo() + " - "+cota.getBox().getNome():"Não cadastrado");
		 
		 result.use(Results.json()).from(cotaVO, "result").serialize();
	}
	
	@Post
	@Path("/novo/confirmar")
	@Rules(Permissao.ROLE_FINANCEIRO_MANUTENCAO_STATUS_COTA_ALTERACAO)
	public void confirmarNovo(HistoricoSituacaoCota novoHistoricoSituacaoCota) throws SchedulerException {
		
	    if (novoHistoricoSituacaoCota == null ||
	            novoHistoricoSituacaoCota.getCota().getNumeroCota() == null){
	        
	        throw new ValidacaoException(TipoMensagem.ERROR, "Parâmetros inválidos.");
	    }
	    
	    novoHistoricoSituacaoCota.setCota(
	            this.cotaService.obterPorNumeroDaCota(novoHistoricoSituacaoCota.getCota().getNumeroCota()));
	    
		Date dataOperacaoDistribuidor = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		novoHistoricoSituacaoCota.setTipoEdicao(TipoEdicao.INCLUSAO);
		
		novoHistoricoSituacaoCota.setResponsavel(this.getUsuarioLogado());
		
		novoHistoricoSituacaoCota.setSituacaoAnterior(
			novoHistoricoSituacaoCota.getCota().getSituacaoCadastro());
		
		if (novoHistoricoSituacaoCota.getDataInicioValidade() == null) {
			
			novoHistoricoSituacaoCota.setDataInicioValidade(dataOperacaoDistribuidor);
		}

		//novoHistoricoSituacaoCota.setCota(
		        //this.cotaService.obterPorId(novoHistoricoSituacaoCota.getCota().getId()));
		
		this.situacaoCotaService.atualizarSituacaoCota(
			novoHistoricoSituacaoCota, dataOperacaoDistribuidor);

		ValidacaoVO validacao = 
			new ValidacaoVO(TipoMensagem.SUCCESS, "A alteração do Status da Cota foi agendada com sucesso!");
		
		result.use(Results.json()).from(validacao, "result").recursive().serialize();
	}
	
	@Get
	@Path("/popularGridFollowUp")
	public void popularGridFollowUp(Integer numeroCota) {			
		result.include("numeroCotaFollowUp", numeroCota);		
		result.forwardTo(ManutencaoStatusCotaController.class).index();
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
			
			if (filtro.getPeriodo() != null){
				
				if (filtro.getPeriodo().getDataInicial() == null ||
					filtro.getPeriodo().getDataFinal() == null){
					
					filtro.setPeriodo(null);
				}
			}
			
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
		
		List<ItemDTO<SituacaoCadastro, String>> listaSituacoesNovoStatusCota =
			new ArrayList<ItemDTO<SituacaoCadastro, String>>();
		
		for (SituacaoCadastro situacaoCadastro : SituacaoCadastro.values()) {
			
			listaSituacoesStatusCota.add(
				new ItemDTO<SituacaoCadastro, String>(situacaoCadastro, situacaoCadastro.toString())
			);

			if (!SituacaoCadastro.PENDENTE.equals(situacaoCadastro)) {
				
				listaSituacoesNovoStatusCota.add(
					new ItemDTO<SituacaoCadastro, String>(situacaoCadastro, situacaoCadastro.toString())
				);
			}
		}
		
		result.include("listaSituacoesStatusCota", listaSituacoesStatusCota);

		result.include("listaSituacoesNovoStatusCota", listaSituacoesNovoStatusCota);
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
	
	/*
	 * Valida os dados da cota vindos do filtro de pesquisa.
	 *  
	 * @param filtro - filtro de pesquisa
	 * 
	 * @return Cota
	 */
	private Cota validarDadosCota(FiltroStatusCotaDTO filtro) {
		
		if (filtro == null
				|| filtro.getNumeroCota() == null) {
			 
			 throw new ValidacaoException(TipoMensagem.WARNING, "Preencha as informações da cota!");
		 }
		 
		 Cota cota = this.cotaService.obterPorNumeroDaCota(filtro.getNumeroCota());
		 
		 if (cota == null) {
			 
			 throw new ValidacaoException(TipoMensagem.WARNING, "Cota inexistente!");
		 }
		 
		 return cota;
	}
	
	/*
	 * Valida o período do histórico do status da cota.
	 *  
	 * @param periodo - período
	 */
	private void validarPeriodoHistoricoStatusCota(PeriodoVO periodo) {
		
		if (DateUtil.isDataInicialMaiorDataFinal(periodo.getDataInicial(), periodo.getDataFinal())) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe um período válido!");
		}
	}

	/**
	 * Verifica se a cota possui dividas em aberto
	 * @param numeroCota
	 */
	@Post
	@Path("/dividasAbertoCota")
	public void dividasAbertoCota(Integer numeroCota){
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		BigDecimal totalDividas = this.dividaService.obterTotalDividasVencidasEmAberto(cota.getId());
		
		ValidacaoVO validacao = null;
	
		if (totalDividas!=null){
			
			if (totalDividas.floatValue() > 0f){
				
				validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "AVISO: A cota ["+cota.getPessoa().getNome()+"] possui um total de "+ CurrencyUtil.formatarValorComSimbolo(totalDividas.floatValue()) +" de dívidas em aberto !");
			}
		}	

		result.use(Results.json()).from(validacao!=null?validacao:"", "result").recursive().serialize();
	}

}
