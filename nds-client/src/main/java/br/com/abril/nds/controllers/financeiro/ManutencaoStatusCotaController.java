package br.com.abril.nds.controllers.financeiro;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.job.StatusCotaJob;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.client.vo.HistoricoSituacaoCotaVO;
import br.com.abril.nds.dto.CotaGarantiaDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO.OrdenacaoColunasStatusCota;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.CotaGarantiaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.SituacaoCotaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
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
public class ManutencaoStatusCotaController {

	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private SituacaoCotaService situacaoCotaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private CotaGarantiaService cotaGarantiaService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private DividaService dividaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaManutencaoStatusCota";
	
	@Get
	@Path("/")
	@Rules(Permissao.ROLE_FINANCEIRO_MANUTENCAO_STATUS_COTA)
	public void index() {

		this.carregarCombos();
	}
	
	@Post
	@Path("/pesquisar")
	public void pesquisar(FiltroStatusCotaDTO filtro, String sortorder, String sortname, int page, int rp) {
		
		this.validarPeriodoHistoricoStatusCota(filtro.getPeriodo());
		
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
	public void novo(FiltroStatusCotaDTO filtro) {
		
		 Cota cota = this.validarDadosCota(filtro);
		 
		 CotaVO cotaVO = 
			new CotaVO(cota.getNumeroCota(), PessoaUtil.obterNomeExibicaoPeloTipo(cota.getPessoa()));
		
		 cotaVO.setCodigoBox(cota.getBox()!=null?cota.getBox().getCodigo() + " - "+cota.getBox().getNome():"Não cadastrado");
		 
		 result.use(Results.json()).from(cotaVO, "result").serialize();
	}
	
	@Post
	@Path("/novo/confirmar")
	public void confirmarNovo(HistoricoSituacaoCota novoHistoricoSituacaoCota) throws SchedulerException {
		
		this.validarAlteracaoStatus(novoHistoricoSituacaoCota);
		
		novoHistoricoSituacaoCota.setTipoEdicao(TipoEdicao.INCLUSAO);
		
		novoHistoricoSituacaoCota.setResponsavel(this.getUsuario());
		
		novoHistoricoSituacaoCota.setSituacaoAnterior(
			novoHistoricoSituacaoCota.getCota().getSituacaoCadastro());
		
		if (novoHistoricoSituacaoCota.getDataInicioValidade() == null) {
			
			novoHistoricoSituacaoCota.setDataInicioValidade(new Date());
		}
		
		this.criarJobAtualizacaoNovaSituacaoCota(novoHistoricoSituacaoCota);
		
		this.criarJobAtualizacaoSituacaoAnteriorCota(novoHistoricoSituacaoCota);

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
	 * Cria o job para atualização da nova situação da cota.
	 *  
	 * @param novoHistoricoSituacaoCota - novo histórico de situação da cota
	 * 
	 * @throws SchedulerException Exceção de agendamento
	 */
	private void criarJobAtualizacaoNovaSituacaoCota(HistoricoSituacaoCota novoHistoricoSituacaoCota) throws SchedulerException {
		
		JobDataMap jobDataMap = new JobDataMap();

		jobDataMap.put(StatusCotaJob.HISTORICO_SITUACAO_COTA_DATA_KEY, novoHistoricoSituacaoCota);
		
		jobDataMap.put(StatusCotaJob.FIM_PERIODO_VALIDADE_SITUACAO_COTA_DATA_KEY, false);
		
	    this.criarJobQuartz(
	    	novoHistoricoSituacaoCota, novoHistoricoSituacaoCota.getDataInicioValidade(), jobDataMap);
	}
	
	/*
	 * Cria o job para atualização da situação anterior da cota.
	 *  
	 * @param novoHistoricoSituacaoCota - novo histórico de situação da cota
	 * 
	 * @throws SchedulerException Exceção de agendamento
	 */
	private void criarJobAtualizacaoSituacaoAnteriorCota(HistoricoSituacaoCota novoHistoricoSituacaoCota) throws SchedulerException {
		
		if (novoHistoricoSituacaoCota.getDataFimValidade() == null) {
			
			return;
		}
		
		JobDataMap jobDataMap = new JobDataMap();

		jobDataMap.put(StatusCotaJob.HISTORICO_SITUACAO_COTA_DATA_KEY, novoHistoricoSituacaoCota);
		
		jobDataMap.put(StatusCotaJob.FIM_PERIODO_VALIDADE_SITUACAO_COTA_DATA_KEY, true);
		
	    this.criarJobQuartz(
	    	novoHistoricoSituacaoCota, novoHistoricoSituacaoCota.getDataFimValidade(), jobDataMap);
	}
	
	/*
	 * Cria um job do quartz.
	 * 
	 * @param novoHistoricoSituacaoCota - novo histórico de situação da cota
	 * @param dataInicio - data de início do agendamento
	 * @param jobDataMap - dados para o agendamento
	 * 
	 * @throws SchedulerException Exceção de agendamento
	 */
	private void criarJobQuartz(HistoricoSituacaoCota novoHistoricoSituacaoCota, 
								Date dataInicio,
								JobDataMap jobDataMap) throws SchedulerException {
	
		String jobKey = String.valueOf(new Date().getTime());
	    
	    String jobGroup = novoHistoricoSituacaoCota.getCota().getId().toString();
		
	    JobDetail job = 
	    	newJob(StatusCotaJob.class).withIdentity(jobKey, jobGroup).usingJobData(jobDataMap).build();

	    Trigger trigger =
	    	newTrigger()
	    		.startAt(dataInicio)
	    		.endAt(dataInicio)
	    		.withSchedule(
	    			simpleSchedule()
		    			.withMisfireHandlingInstructionFireNow()
		    	).build();

	    Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
	    
	    scheduler.scheduleJob(job, trigger);
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
			
			String descricao = 
				historicoSituacaoCota.getDescricao() == null ? "" : historicoSituacaoCota.getDescricao();
			
			historicoSituacaoCotaVO.setDescricao(descricao);
			
			String motivo = 
				historicoSituacaoCota.getMotivo() == null ? "" : historicoSituacaoCota.getMotivo().toString();

			historicoSituacaoCotaVO.setMotivo(motivo);
			
			historicoSituacaoCotaVO.setUsuario(historicoSituacaoCota.getResponsavel().getNome());
			
			historicoSituacaoCotaVO.setStatusAnterior(historicoSituacaoCota.getSituacaoAnterior().toString());
			
			historicoSituacaoCotaVO.setStatusAtualizado(historicoSituacaoCota.getNovaSituacao().toString());
			
			historicoSituacaoCotaVO.setNomeCota(historicoSituacaoCota.getCota().getPessoa().getNome());
			
			historicoSituacaoCotaVO.setNumeroCota(historicoSituacaoCota.getCota().getNumeroCota());
			
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
	
	/*
	 * Valida a alteração no status da cota ser inserida no histórico.
	 * 
	 * @param novoHistoricoSituacaoCota - nova histórico da situação da cota
	 */
	private void validarAlteracaoStatus(HistoricoSituacaoCota novoHistoricoSituacaoCota) {
		
		if (novoHistoricoSituacaoCota == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha as informações do Status da Cota");
		}
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (novoHistoricoSituacaoCota.getNovaSituacao() == null) {
			
			listaMensagens.add("O preenchimento do campo [Status] é obrigatório!");
		}
		
		if (novoHistoricoSituacaoCota.getCota() == null
				|| novoHistoricoSituacaoCota.getCota().getNumeroCota() == null) {
			
			listaMensagens.add("Informações da cota inválidas!");
			
		} else {
			
			Cota cota = 
				this.cotaService.obterPorNumeroDaCota(novoHistoricoSituacaoCota.getCota().getNumeroCota());
			
			if (cota == null) {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Cota inexistente!");
			}
			
			if (novoHistoricoSituacaoCota.getNovaSituacao()==SituacaoCadastro.ATIVO){
				
				List<EnderecoAssociacaoDTO> enderecosCota = this.cotaService.obterEnderecosPorIdCota(cota.getId());
			    if (enderecosCota==null || enderecosCota.size()<=0){
			    	throw new ValidacaoException(TipoMensagem.WARNING, "Para alterar o status da cota para [Ativo] é necessário que a mesma possua ao menos um [Endereço] cadatrado!");
			    }
			    
			    List<TelefoneAssociacaoDTO> telefonesCota = this.cotaService.buscarTelefonesCota(cota.getId(), null);
			    if (telefonesCota==null || telefonesCota.size()<=0){
			    	throw new ValidacaoException(TipoMensagem.WARNING, "Para alterar o status da cota para [Ativo] é necessário que a mesma possua ao menos um [Telefone] cadatrado!");
			    }
			    
			    Distribuidor distribuidor = this.distribuidorService.obter();
				if (distribuidor.isUtilizaGarantiaPdv()){
					CotaGarantiaDTO<CotaGarantia> cotaGarantiaDTO = this.cotaGarantiaService.getByCota(cota.getId());
					if (cotaGarantiaDTO.getCotaGarantia()==null){
						throw new ValidacaoException(TipoMensagem.WARNING, "Para alterar o status da cota para [Ativo] é necessário que a mesma possua [Garantia] cadatrada!");
					}
				}
				
				List<Rota> rotasCota = this.roteirizacaoService.obterRotasPorCota(cota.getNumeroCota());
				if (rotasCota==null || rotasCota.size()<=0){
					throw new ValidacaoException(TipoMensagem.WARNING, "Para alterar o status da cota para [Ativo] é necessário que a mesma possua [Roteirização] cadatrada!");
				}
			}
	
			novoHistoricoSituacaoCota.setCota(cota);
		}
		
		if (novoHistoricoSituacaoCota.getDataInicioValidade() != null
				|| novoHistoricoSituacaoCota.getDataFimValidade() != null) {
			
			if (novoHistoricoSituacaoCota.getDataInicioValidade() == null) {
				
				listaMensagens.add("Informe a data inicial do período!");
				
			} else {
			
				if (DateUtil.isDataInicialMaiorDataFinal(
						novoHistoricoSituacaoCota.getDataInicioValidade(), novoHistoricoSituacaoCota.getDataFimValidade())) {
					
					listaMensagens.add("Informe um período válido!");
				}
				
				Date dataAtual = DateUtil.removerTimestamp(new Date());
				
				if (novoHistoricoSituacaoCota.getDataInicioValidade().compareTo(dataAtual) < 0) {
					
					listaMensagens.add("A data inicial do período deve ser igual ou maior que a data atual!");
				}
			}
		}
		
		if (!listaMensagens.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaMensagens);
			
			throw new ValidacaoException(validacao);
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
		BigDecimal totalDividas = this.dividaService.obterTotalDividasAbertoCota(cota.getId());
		ValidacaoVO validacao = null;
	
		if (totalDividas!=null){
			if (totalDividas.floatValue() > 0f){
				validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "AVISO: A cota ["+cota.getPessoa().getNome()+"] possui um total de "+ CurrencyUtil.formatarValorComSimbolo(totalDividas.floatValue()) +" de dívidas em aberto !");
			}
		}	

		result.use(Results.json()).from(validacao!=null?validacao:"", "result").recursive().serialize();
	}	
	
	//TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		
		usuario.setNome("Jornaleiro da Silva");
		
		return usuario;
	}
	
}
