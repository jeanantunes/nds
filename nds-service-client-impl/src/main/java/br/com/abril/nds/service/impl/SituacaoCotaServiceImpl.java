package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.HistoricoSituacaoCotaVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.repository.CotaBaseRepository;
import br.com.abril.nds.repository.CotaRepository;
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
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.QuartzUtil;

/**
 * Classe de implementação de serviços referentes a situação de cota.
 * 
 * @author Discover Technology
 *
 */
@Service
public class SituacaoCotaServiceImpl implements SituacaoCotaService, ApplicationContextAware {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(SituacaoCotaServiceImpl.class);
	  
	  
	  
	@Autowired
	private HistoricoSituacaoCotaRepository historicoSituacaoCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	private ApplicationContext applicationContext;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private TelefoneService telefoneService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CotaGarantiaService cotaGarantiaService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private CotaBaseRepository cotaBaseRepository;
	
	@Autowired
	private MixCotaProdutoRepository mixCotaProdutoRepository;
	
	@Autowired
	private DividaService dividaService;
	
	@Autowired
	private CotaService cotaService;
	
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired 
	private ConsultaConsignadoCotaService consultaConsignadoCotaService;

	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		
		this.applicationContext = applicationContext;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.SituacaoCotaService#obterHistoricoStatusCota(br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO)
	 */
	@Transactional(readOnly = true)
	public List<HistoricoSituacaoCotaVO> obterHistoricoStatusCota(FiltroStatusCotaDTO filtro) {
		
		if(filtro.getNumeroCota() != null) {
			
			return this.historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
		}
		
		return this.historicoSituacaoCotaRepository.obterUltimoHistoricoStatusCota(filtro);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.SituacaoCotaService#obterTotalHistoricoStatusCota(br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO)
	 */
	@Transactional(readOnly = true)
	public Long obterTotalHistoricoStatusCota(FiltroStatusCotaDTO filtro) {
		
		if(filtro.getNumeroCota() != null) {
			
			return this.historicoSituacaoCotaRepository.obterTotalHistoricoStatusCota(filtro);
		}
		
		return this.historicoSituacaoCotaRepository.obterTotalUltimoHistoricoStatusCota(filtro);
	
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.SituacaoCotaService#atualizarSituacaoCota(br.com.abril.nds.model.cadastro.HistoricoSituacaoCota)
	 */
	@Transactional
	public void atualizarSituacaoCota(HistoricoSituacaoCota historicoSituacaoCota, Date dataDeOperacao) {	
		
	    this.validarAlteracaoStatusCota(historicoSituacaoCota, dataDeOperacao);
		
		Cota cota = cotaService.obterPorId(historicoSituacaoCota.getCota().getId());
		
		if (DateUtil.obterDiferencaDias(
				dataDeOperacao, historicoSituacaoCota.getDataInicioValidade()) == 0) {
		
			historicoSituacaoCota.setProcessado(true);
			
			cota.setSituacaoCadastro(historicoSituacaoCota.getNovaSituacao());
			
			this.cotaRepository.alterar(cota);
			
		} else {
			
			historicoSituacaoCota.setProcessado(false);
		}
		
		historicoSituacaoCota.setDataEdicao(new Date());

		historicoSituacaoCota.setRestaurado(false);
		
		this.historicoSituacaoCotaRepository.adicionar(historicoSituacaoCota);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.SituacaoCotaService#removerAgendamentosAlteracaoSituacaoCota(java.lang.Long)
	 */
	public void removerAgendamentosAlteracaoSituacaoCota(Long idCota) {
		
		if (idCota == null) {
			
			throw new IllegalArgumentException("ID da Cota nulo!");
		}
		
		StdScheduler scheduler = (StdScheduler) applicationContext.getBean("schedulerFactoryBean");

		QuartzUtil.doAgendador(scheduler).removeJobsFromGroup(idCota.toString());
	}
	
	@Override
	@Transactional(readOnly=true)
	public SituacaoCadastro obterSituacaoCadastroCota(Integer numeroCota){
		
		if (numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Número de cota é obrigatório.");
		}
		
		return this.cotaRepository.obterSituacaoCadastroCota(numeroCota);
	}
	
	private void validarAlteracaoStatusCota(HistoricoSituacaoCota novoHistoricoSituacaoCota,
	        Date dataOperacao){
	    
	    if (novoHistoricoSituacaoCota == null || dataOperacao == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Preencha as informações do Status da Cota");
        }
        
        Cota cota = novoHistoricoSituacaoCota.getCota();
            
        if (cota == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota inexistente!");
        }
        
        if(SituacaoCadastro.INATIVO.equals(cota.getSituacaoCadastro())){
            
        	// permitir que o usuario supervisor ou admin altere status
        if ( ! usuarioService.isSupervisor() && !"SAD".equalsIgnoreCase(usuarioService.getUsuarioLogado().getLogin())) {
        	
            throw new ValidacaoException(TipoMensagem.WARNING, "Não é possível ativar uma cota com status Inativa!(Somente o Supervisor ou SAD podem ativar)");
            
        }
        else {
        	LOGGER.error("ATENCAO.. USUARIO "+usuarioService.getNomeUsuarioLogado()+ " alterando status de cota inativa=" +
            cota.getNumeroCota());
        }
        
        }
        
        List<String> listaMensagens = new ArrayList<String>();
        
        if (novoHistoricoSituacaoCota.getNovaSituacao() == null) {
            
            listaMensagens.add("O preenchimento do campo [Status] é obrigatório!");
        }
        
        if (novoHistoricoSituacaoCota.getNovaSituacao()==SituacaoCadastro.ATIVO){
            
            String defaultMessage = "Para alterar o status da cota para [Ativo] é necessário que a mesma possua ao menos: ";
            
            boolean indLeastOneNecessaryMsg = false;
            
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
                
                if (ultimoHistorico!=null){
                
	                long diasSuspensao = DateUtil.obterDiferencaDias(ultimoHistorico.getDataInicioValidade(), 
	                        Calendar.getInstance().getTime());
	                
	                if (diasSuspensao >= 90 && !this.cotaBaseRepository.cotaTemCotaBase(cota.getId())){
	                    
	                    msgs.add(
	                        "Cota suspensa por mais de 90 dias, cadastro de Cota Base obrigatório para mudança de status para Ativo");
	                }
                }
            }
            
            if (!msgs.isEmpty()){
                
                throw new ValidacaoException(TipoMensagem.WARNING,msgs);
            }
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
                
                if (novoHistoricoSituacaoCota.getDataInicioValidade().compareTo(dataOperacao) < 0) {
                    
                    listaMensagens.add("A data inicial do período deve ser igual ou maior que a data de operação!");
                }
            }
        }
        
        if (!listaMensagens.isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, listaMensagens);
        }
        
        if (!SituacaoCadastro.INATIVO.equals(novoHistoricoSituacaoCota.getNovaSituacao())) {
            
            return;
        }
        
        BigDecimal totalDividas = this.dividaService.obterTotalDividasAbertoCota(cota.getId());
        
        if (totalDividas != null && totalDividas.compareTo(BigDecimal.ZERO) > 0){
            throw new ValidacaoException(
                    TipoMensagem.WARNING, 
                    "AVISO: A cota ["+cota.getPessoa().getNome()+"] possui um total de "+ 
                    CurrencyUtil.formatarValorComSimbolo(totalDividas.floatValue()) +" de dívidas em aberto !");
        }
        
        FiltroConsultaConsignadoCotaDTO filtro = new FiltroConsultaConsignadoCotaDTO();
        filtro.setIdCota(cota.getId());
        
        BigDecimal totalConsignado = this.consultaConsignadoCotaService.buscarTotalGeralDaCota(filtro);
        
        if (totalConsignado != null && totalConsignado.compareTo(BigDecimal.ZERO) > 0) {
            
            throw new ValidacaoException(
                    TipoMensagem.WARNING, 
                    "A cota ["+cota.getPessoa().getNome()+"] possui um total de "+ 
                    CurrencyUtil.formatarValorComSimbolo(totalConsignado.floatValue()) +
                    " em consignado e não pode ser inativada!");
        }
	}
}