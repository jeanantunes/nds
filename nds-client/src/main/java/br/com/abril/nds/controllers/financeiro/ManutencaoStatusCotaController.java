package br.com.abril.nds.controllers.financeiro;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.metamodel.ValidationException;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.job.StatusCotaJob;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.client.vo.HistoricoSituacaoCotaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO.OrdenacaoColunasStatusCota;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.CotaBaseRepository;
import br.com.abril.nds.repository.HistoricoSituacaoCotaRepository;
import br.com.abril.nds.repository.MixCotaProdutoRepository;
import br.com.abril.nds.service.ConsultaConsignadoCotaService;
import br.com.abril.nds.service.CotaGarantiaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.SituacaoCotaService;
import br.com.abril.nds.service.TelefoneService;
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
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private TelefoneService telefoneService;
	
	@Autowired
	private ConsultaConsignadoCotaService consignadoCotaService;
	
	@Autowired
	private CotaBaseRepository cotaBaseRepository;
	
	@Autowired
	private MixCotaProdutoRepository mixCotaProdutoRepository;
	
	@Autowired
	private HistoricoSituacaoCotaRepository historicoSituacaoCotaRepository;
	
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
		
		this.validarAlteracaoStatus(novoHistoricoSituacaoCota);
		
		this.validarInativacaoCota(novoHistoricoSituacaoCota.getNovaSituacao(), novoHistoricoSituacaoCota.getCota().getNumeroCota());
		
		Date dataOperacaoDistribuidor = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		novoHistoricoSituacaoCota.setTipoEdicao(TipoEdicao.INCLUSAO);
		
		novoHistoricoSituacaoCota.setResponsavel(this.getUsuarioLogado());
		
		novoHistoricoSituacaoCota.setSituacaoAnterior(
			novoHistoricoSituacaoCota.getCota().getSituacaoCadastro());
		
		if (novoHistoricoSituacaoCota.getDataInicioValidade() == null) {
			
			novoHistoricoSituacaoCota.setDataInicioValidade(dataOperacaoDistribuidor);
		}

		Long idCota = novoHistoricoSituacaoCota.getCota().getId();
		
		novoHistoricoSituacaoCota.setCota(new Cota(idCota));
		
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

	    Scheduler scheduler = this.schedulerFactoryBean.getScheduler();
	    
	    scheduler.scheduleJob(job, trigger);
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
	
	/*
	 * Valida a alteração no status da cota ser inserida no histórico.
	 * 
	 * @param novoHistoricoSituacaoCota - nova histórico da situação da cota
	 */
	private void validarAlteracaoStatus(HistoricoSituacaoCota novoHistoricoSituacaoCota) {
		
		if (novoHistoricoSituacaoCota == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha as informações do Status da Cota");
		}
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(novoHistoricoSituacaoCota.getCota().getNumeroCota());
			
		if (cota == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota inexistente!");
		}
		
		if(SituacaoCadastro.INATIVO.equals(cota.getSituacaoCadastro())){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não é possível ativar uma cota com status Inativa!");
		
		}
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (novoHistoricoSituacaoCota.getNovaSituacao() == null) {
			
			listaMensagens.add("O preenchimento do campo [Status] é obrigatório!");
		}
		
		if (novoHistoricoSituacaoCota.getCota() == null
				|| novoHistoricoSituacaoCota.getCota().getNumeroCota() == null) {
			
			listaMensagens.add("Informações da cota inválidas!");
			
		} else {
			
			String defaultMessage = "Para alterar o status da cota para [Ativo] é necessário que a mesma possua ao menos: ";
			
			boolean indLeastOneNecessaryMsg = false;
			
			if (novoHistoricoSituacaoCota.getNovaSituacao()==SituacaoCadastro.ATIVO){
				
				List<String> msgs = new ArrayList<String>();
				
				Long qtde = this.enderecoService.obterQtdEnderecoAssociadoCota(cota.getId());
				
			    if (qtde == null || qtde == 0){
			    	msgs.add(indLeastOneNecessaryMsg ? "Um [Endereço] cadastrado!" : defaultMessage + " Um [Endereço] cadastrado!");
			    	indLeastOneNecessaryMsg = true;
			    }
			    
			    qtde = this.telefoneService.obterQtdTelefoneAssociadoCota(cota.getId());
			    if (qtde == null || qtde == 0){
			    	msgs.add(indLeastOneNecessaryMsg ? "Um [Telefone] cadastrado!" : defaultMessage + " Um [Telefone] cadastrado!");
			    	indLeastOneNecessaryMsg = true;
			    }
			    
				if (this.distribuidorService.utilizaGarantiaPdv()){
					
					qtde = this.cotaGarantiaService.getQtdCotaGarantiaByCota(cota.getId());
					
					if (qtde == null || qtde == 0){
						msgs.add(indLeastOneNecessaryMsg ? "Uma [Garantia] cadastrada!" : defaultMessage + " Uma [Garantia] cadastrada!");
						indLeastOneNecessaryMsg = true;
					}
				}
				
				qtde = this.roteirizacaoService.obterQtdRotasPorCota(cota.getNumeroCota());
				
				if (qtde == null || qtde == 0){
					msgs.add(indLeastOneNecessaryMsg ? "Uma [Roteirização] cadastrada!" : defaultMessage + " Uma [Roteirização] cadastrada!");
					indLeastOneNecessaryMsg = true;
				}
				
				//segundo César, situação PENDENTE == cota nova
				if (cota.getSituacaoCadastro() == SituacaoCadastro.PENDENTE){
					
					if (cota.getTipoDistribuicaoCota() == TipoDistribuicaoCota.CONVENCIONAL){
						
						if (!this.cotaBaseRepository.cotaTemCotaBase(cota.getId())){
							
							msgs.add(
								"É obrigatório o cadastro de Cota Base para mudança de status para Ativo de cotas novas.");
						}
						
					} else {
						
						if (!this.mixCotaProdutoRepository.existeMixCotaProdutoCadastrado(null, cota.getId())){
							
							msgs.add(
								"É obrigatório o cadastro de Mix para mudança de status para Ativo de cotas novas.");
						}
					}
				} else if (cota.getSituacaoCadastro() == SituacaoCadastro.SUSPENSO &&
					cota.getTipoDistribuicaoCota() == TipoDistribuicaoCota.CONVENCIONAL){
					
					HistoricoSituacaoCota ultimoHistorico = 
						this.historicoSituacaoCotaRepository.obterUltimoHistorico(
							cota.getNumeroCota(), SituacaoCadastro.SUSPENSO);
					
					long diasSuspensao = DateUtil.obterDiferencaDias(ultimoHistorico.getDataInicioValidade(), 
							Calendar.getInstance().getTime());
					
					if (diasSuspensao >= 90 && !this.cotaBaseRepository.cotaTemCotaBase(cota.getId())){
						
						msgs.add(
							"Cota suspensa por mais de 90 dias, cadastro de Cota Base obrigatório para mudança de status para Ativo");
					}
				}
				
				if (!msgs.isEmpty()){
					
					throw new ValidacaoException(TipoMensagem.WARNING,msgs);
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
				
				Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
				
				if (novoHistoricoSituacaoCota.getDataInicioValidade().compareTo(dataOperacao) < 0) {
					
					listaMensagens.add("A data inicial do período deve ser igual ou maior que a data de operação!");
				}
			}
		}
		
		if (!listaMensagens.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaMensagens);
			
			throw new ValidacaoException(validacao);
		}
	}

	private void validarInativacaoCota(SituacaoCadastro situacaoCadastro, Integer numeroCota) {
		
		if (!SituacaoCadastro.INATIVO.equals(situacaoCadastro)) {
			
			return;
		}
		
		validarDividasAbertoCota(numeroCota);
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);

		FiltroConsultaConsignadoCotaDTO filtro = new FiltroConsultaConsignadoCotaDTO();
		filtro.setIdCota(cota.getId());
		
		BigDecimal totalConsignado = this.consignadoCotaService.buscarTotalGeralDaCota(filtro);
		
		if (totalConsignado != null && totalConsignado.compareTo(BigDecimal.ZERO) > 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "A cota ["+cota.getPessoa().getNome()+"] possui um total de "+ CurrencyUtil.formatarValorComSimbolo(totalConsignado.floatValue()) +" em consignado e não pode ser inativada!");
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
	
	public void validarDividasAbertoCota(Integer numeroCota){
        Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
        BigDecimal totalDividas = this.dividaService.obterTotalDividasAbertoCota(cota.getId());
        
        if (totalDividas!=null){
            if (totalDividas.floatValue() > 0f){
                throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,"AVISO: A cota ["+cota.getPessoa().getNome()+"] possui um total de "+ CurrencyUtil.formatarValorComSimbolo(totalDividas.floatValue()) +" de dívidas em aberto !"));
            }
        }   
	}

}
