package br.com.abril.nds.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.job.AtualizaEstoqueJob;
import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDtoOrdenaPorCodigo;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDtoOrdenaPorEdicao;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDtoOrdenaPorPrecoCapa;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDtoOrdenaPorPrecoDesconto;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDtoOrdenaPorProduto;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDtoOrdenaPorSequencia;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDtoOrdenaPorTotalDevolucao;
import br.com.abril.nds.dto.MovimentoEstoqueCotaGenericoDTO;
import br.com.abril.nds.dto.fechamentoencalhe.GridFechamentoEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.GrupoCota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalheBox;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalheBoxPK;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.CotaUnificacaoRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EstoqueProdutoFilaRepository;
import br.com.abril.nds.repository.EstudoGeradoRepository;
import br.com.abril.nds.repository.FechamentoEncalheBoxRepository;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoServicoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.BoletoEmailService;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.EstudoCotaService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.GrupoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class FechamentoEncalheServiceImpl implements FechamentoEncalheService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FechamentoEncalheServiceImpl.class);
    
    @Autowired
    private FechamentoEncalheRepository fechamentoEncalheRepository;
    
    @Autowired
    private CotaRepository cotaRepository;
    
    @Autowired
    private GerarCobrancaService gerarCobrancaService;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private GrupoService grupoService;
    
    @Autowired
    private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
    
    @Autowired
    private ChamadaEncalheRepository chamadaEncalheRepository;
    
    @Autowired
    private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
    
    @Autowired
    private FechamentoEncalheBoxRepository fechamentoEncalheBoxRepository;
    
    @Autowired
    private TipoNotaFiscalRepository tipoNotaFiscalRepository;
    
    @Autowired
    private NotaFiscalService notaFiscalService;
    
    @Autowired
    private NotaFiscalRepository notaFiscalRepository;
    
    @Autowired
    private ProdutoServicoRepository produtoServicoRepository;
    
    @Autowired
    private MovimentoEstoqueService movimentoEstoqueService;
    
    @Autowired
    private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
    
    @Autowired
    private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
    
    @Autowired
    private DistribuidorRepository distribuidorRepository;
    
    @Autowired
    private DiferencaEstoqueService diferencaEstoqueService;
    
    @Autowired
    private ProdutoEdicaoRepository edicaoRepository;
    
    @Autowired
    private ConferenciaEncalheRepository conferenciaEncalheRepository;
    
    @Autowired
    private CotaService cotaService;
    
    @Autowired
    private BoletoService boletoService;
    
    @Autowired
    private LancamentoRepository lancamentoRepository;
    
    @Autowired
    private EstudoService estudoService;
    
    @Autowired
    GrupoRepository grupoRepository;
    
    @Autowired
    private EstudoCotaService estudoCotaService;
    
    @Autowired
    private ParciaisService parciaisService;
    
    @Autowired
    protected BoletoEmailService boletoEmailService;
    
    @Autowired
    private NegociacaoDividaService negociacaoDividaService;
    
    @Autowired
    private CotaUnificacaoRepository cotaUnificacaoRepository;
    
    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private CalendarioService calendarioService;
    
    @Autowired
    private PeriodoLancamentoParcialRepository periodoLancamentoParcialRepository;
    
    @Autowired
    private EstudoGeradoRepository estudoGeradoRepository;
    
    @Autowired
    private EstoqueProdutoFilaRepository estoqueProdutoFilaRepository;
    
    @Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
    
    @Autowired
    private FormaCobrancaService formaCobrancaService;
    
    @Override
    @Transactional
    public List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(final FiltroFechamentoEncalheDTO filtro,
            final String sortorder, final String sortname, final Integer page, final Integer rp) {
        
        Integer startSearch = null;
        
        if (page != null && rp != null) {
            startSearch = page * rp - rp;
        }
        
        String sort = sortname;
        
        if ("total".equals(sortname)) {
            sort = null;
        }
        
        final Boolean fechado = fechamentoEncalheRepository.buscaControleFechamentoEncalhe(filtro.getDataEncalhe());
        
        List<FechamentoFisicoLogicoDTO> listaEncalhe = fechamentoEncalheRepository.buscarConferenciaEncalheNovo(
                filtro, sortorder, sort, startSearch, rp);
        
        if (listaEncalhe.isEmpty()) {
        	return listaEncalhe;
        }
        	
        final ArrayList<Long> listaDeIdsProdutoEdicao = new ArrayList<Long>();
        
        for (final FechamentoFisicoLogicoDTO encalhe : listaEncalhe) {
        	encalhe.setDataRecolhimento(filtro.getDataEncalhe());
            listaDeIdsProdutoEdicao.add(encalhe.getProdutoEdicao());
        }
        
        final List<FechamentoFisicoLogicoDTO> listaMovimentoEstoqueCota = fechamentoEncalheRepository
                .buscarMovimentoEstoqueCota(filtro, listaDeIdsProdutoEdicao);
        
        carregarQtdLogicoNaListaEncalheFisicoLogico(filtro, 
        		listaEncalhe,
				listaMovimentoEstoqueCota,
				listaDeIdsProdutoEdicao);
        
        carregarQtdFisicoNaListaEncalheFisicoLogico(filtro, fechado, listaEncalhe);
        
        if (sort == null) {
        	listaEncalhe = this.retornarListaOrdenada(listaEncalhe, "sequencia", sortorder);
        } else {
        	listaEncalhe = this.retornarListaOrdenada(listaEncalhe, sort, sortorder);
        }
        
        return listaEncalhe;
        
    }

	/**
	 * Carrega na listaEncalhe as quantidades digitadas
	 * na funcionalidade de fechamento de encalhe (Físico).
	 * 
	 * @param filtro
	 * @param fechado
	 * @param listaEncalhe
	 */
	private void carregarQtdFisicoNaListaEncalheFisicoLogico(
			final FiltroFechamentoEncalheDTO filtro, final Boolean fechado,
			List<FechamentoFisicoLogicoDTO> listaEncalhe
			) {
		
		if (filtro.getBoxId() == null) {
        	
            final List<FechamentoEncalhe> listaFechamento = fechamentoEncalheRepository
                    .buscarFechamentoEncalhe(filtro.getDataEncalhe());
            
            for (final FechamentoFisicoLogicoDTO encalhe : listaEncalhe) {
                
                this.setarInfoComumFechamentoFisicoLogicoDTO(encalhe, fechado);
                
                for (final FechamentoEncalhe fechamento : listaFechamento) {
                    if (encalhe.getProdutoEdicao().equals(
                            fechamento.getFechamentoEncalhePK().getProdutoEdicao().getId())) {
                        encalhe.setFisico(fechamento.getQuantidade());
                        encalhe.setDiferenca(calcularDiferenca(encalhe));
                        break;
                    }
                }
            }
            
        } else {
        	
            final List<FechamentoEncalheBox> listaFechamentoBox = fechamentoEncalheBoxRepository
                    .buscarFechamentoEncalheBox(filtro);
            
            for (final FechamentoFisicoLogicoDTO encalhe : listaEncalhe) {
                
                this.setarInfoComumFechamentoFisicoLogicoDTO(encalhe, fechado);
                
                for (final FechamentoEncalheBox fechamento : listaFechamentoBox) {
                    
                    if (encalhe.getProdutoEdicao().equals(
                            fechamento.getFechamentoEncalheBoxPK().getFechamentoEncalhe().getFechamentoEncalhePK()
                            .getProdutoEdicao().getId())) {
                        
                    	encalhe.setFisico(fechamento.getQuantidade());
                        
                    	encalhe.setDiferenca(calcularDiferenca(encalhe));
                        
                        break;
                    }
                    
                }
            }
        }
	}

	/**
	 * Carrega na listaEncalhe as quantidades vindas da 
	 * funcionalidade de conferência de encalhe (Lógico).
	 * 
	 * Caso a pesquisa não especifique o box, as quantidades
	 * de venda serão abatidas no encalhe.
	 * 
	 * @param filtro
	 * @param listaEncalhe
	 * @param listaMovimentoEstoqueCota
	 * @param listaIdProdutoEdicao
	 */
	private void carregarQtdLogicoNaListaEncalheFisicoLogico(
			final FiltroFechamentoEncalheDTO filtro,
			List<FechamentoFisicoLogicoDTO> listaEncalhe,
			final List<FechamentoFisicoLogicoDTO> listaMovimentoEstoqueCota,
			final ArrayList<Long> listaDeIdsProdutoEdicao) {
		
		List<FechamentoFisicoLogicoDTO> listaMovimentoEstoqueCotaVendaProduto = null;
	        
	    if (filtro.getBoxId() == null) {
	        	listaMovimentoEstoqueCotaVendaProduto = fechamentoEncalheRepository
	                    .buscarMovimentoEstoqueCotaVendaProduto(filtro, listaDeIdsProdutoEdicao);
	        
	    }
		
		for (final FechamentoFisicoLogicoDTO encalhe : listaEncalhe) {
            
            // Soma as quantidades para os exemplares de devolucao
            for (final FechamentoFisicoLogicoDTO movimentoEstoqueCota : listaMovimentoEstoqueCota) {
                
                if (encalhe.getProdutoEdicao().equals(movimentoEstoqueCota.getProdutoEdicao())
                        && movimentoEstoqueCota.getExemplaresDevolucao() != null) {
                    
                    if (encalhe.getExemplaresDevolucao() == null) {
                    	encalhe.setExemplaresDevolucao(BigInteger.valueOf(0));
                    }
                    
                    if (encalhe.getExemplaresDevolucaoJuramentado() == null) {
                        encalhe.setExemplaresDevolucaoJuramentado(BigInteger.valueOf(0));
                    }
                    
                    if (encalhe.getExemplaresVendaEncalhe() == null) {
                        encalhe.setExemplaresVendaEncalhe(BigInteger.valueOf(0));
                    }
                    
                    encalhe.setExemplaresDevolucao(encalhe.getExemplaresDevolucao().add(
                            movimentoEstoqueCota.getExemplaresDevolucao()));
                    
                    if (movimentoEstoqueCota.isJuramentada()) {
                        encalhe.setExemplaresDevolucaoJuramentado(encalhe.getExemplaresDevolucaoJuramentado().add(
                                movimentoEstoqueCota.getExemplaresDevolucao()));
                    }
                }
            }
            
            if (filtro.getBoxId() == null) {
                
                // Subtrai as quantidades para os exemplares de devolucao
                for (final FechamentoFisicoLogicoDTO movimentoEstoqueCotaVendaProduto : listaMovimentoEstoqueCotaVendaProduto) {
                    if (encalhe.getProdutoEdicao().equals(movimentoEstoqueCotaVendaProduto.getProdutoEdicao())
                            && movimentoEstoqueCotaVendaProduto.getExemplaresDevolucao() != null) {
                        
                        final BigInteger exemplaresDevolucaoConferencia = Util.nvl(encalhe.getExemplaresDevolucao(), BigInteger.ZERO);
                        
                        encalhe.setExemplaresDevolucao(exemplaresDevolucaoConferencia);
                        
                        encalhe.setExemplaresVendaEncalhe(movimentoEstoqueCotaVendaProduto.getExemplaresDevolucao());
                    }
                }
            }
        }
	}
    
    private List<FechamentoFisicoLogicoDTO> retornarListaOrdenada(
            final List<FechamentoFisicoLogicoDTO> listaConferencia, final String sort, final String sortorder) {
        
        if (sortorder == null) {
            return listaConferencia;
        }
        
        if (sort.equals("sequencia")) {
            final FechamentoFisicoLogicoDtoOrdenaPorSequencia ordenacao = new FechamentoFisicoLogicoDtoOrdenaPorSequencia(
                    sortorder);
            Collections.sort(listaConferencia, ordenacao);
            return listaConferencia;
        }
        if (sort.equals("codigo")) {
            final FechamentoFisicoLogicoDtoOrdenaPorCodigo ordenacao = new FechamentoFisicoLogicoDtoOrdenaPorCodigo(
                    sortorder);
            Collections.sort(listaConferencia, ordenacao);
            return listaConferencia;
        } else if (sort.equals("produto")) {
            final FechamentoFisicoLogicoDtoOrdenaPorProduto ordenacao = new FechamentoFisicoLogicoDtoOrdenaPorProduto(
                    sortorder);
            Collections.sort(listaConferencia, ordenacao);
            return listaConferencia;
        } else if (sort.equals("edicao")) {
            final FechamentoFisicoLogicoDtoOrdenaPorEdicao ordenacao = new FechamentoFisicoLogicoDtoOrdenaPorEdicao(
                    sortorder);
            Collections.sort(listaConferencia, ordenacao);
            return listaConferencia;
        } else if (sort.equals("precoCapa")) {
            final FechamentoFisicoLogicoDtoOrdenaPorPrecoCapa ordenacao = new FechamentoFisicoLogicoDtoOrdenaPorPrecoCapa(
                    sortorder);
            Collections.sort(listaConferencia, ordenacao);
            return listaConferencia;
        } else if (sort.equals("precoCapaDesc")) {
            final FechamentoFisicoLogicoDtoOrdenaPorPrecoDesconto ordenacao = new FechamentoFisicoLogicoDtoOrdenaPorPrecoDesconto(
                    sortorder);
            Collections.sort(listaConferencia, ordenacao);
            return listaConferencia;
        } else if (sort.equals("exemplaresDevolucao")) {
            final FechamentoFisicoLogicoDtoOrdenaPorTotalDevolucao ordenacao = new FechamentoFisicoLogicoDtoOrdenaPorTotalDevolucao(
                    sortorder);
            Collections.sort(listaConferencia, ordenacao);
            return listaConferencia;
        }
        return null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public int buscarQuantidadeConferenciaEncalhe(final FiltroFechamentoEncalheDTO filtro) {
        
        return fechamentoEncalheRepository.buscarQuantidadeConferenciaEncalheNovo(filtro);
    }
    
    private BigInteger calcularDiferenca(final FechamentoFisicoLogicoDTO conferencia) {

        if (conferencia.getFisico() == null) {
        	
    	    conferencia.setFisico(BigInteger.ZERO);
    	}

    	if (conferencia.getExemplaresDevolucao() == null) {
    	    
    		conferencia.setExemplaresDevolucao(BigInteger.ZERO);
    	}

    	BigInteger exemplaresDevolucao = (conferencia.getExemplaresDevolucao() == null) ? BigInteger.ZERO : conferencia.getExemplaresDevolucao();
    	BigInteger exemplaresDevolucaoJuramentado = (conferencia.getExemplaresDevolucaoJuramentado() == null) ? BigInteger.ZERO : conferencia.getExemplaresDevolucaoJuramentado();
    	BigInteger exemplaresVendasEncalhe = (conferencia.getExemplaresVendaEncalhe() == null) ? BigInteger.ZERO : conferencia.getExemplaresVendaEncalhe();

    	BigInteger qtdeDevolucaoFisico = exemplaresDevolucao.subtract(exemplaresDevolucaoJuramentado).subtract(exemplaresVendasEncalhe);

    	BigInteger qtdeFisico = (conferencia.getFisico() == null) ? BigInteger.ZERO : conferencia.getFisico();

    	return qtdeFisico.subtract(qtdeDevolucaoFisico);  
    }
    
    private void setarInfoComumFechamentoFisicoLogicoDTO(
    		final FechamentoFisicoLogicoDTO encalhe,
            final boolean fechado) {
        
        if (encalhe.getExemplaresDevolucao() == null) {
            encalhe.setExemplaresDevolucao(BigInteger.valueOf(0));
        }
        encalhe.setTotal(new BigDecimal(encalhe.getExemplaresDevolucao()).multiply(encalhe
                .getPrecoCapaDesconto()));
        encalhe.setFechado(fechado);
        
        carregarDescricaoEstoque(encalhe);
       
    }

    private void carregarDescricaoEstoque(FechamentoFisicoLogicoDTO encalhe) {
    	
    	if(encalhe.getFechado()) {

    		if(isEstoqueLancamento(encalhe)) {

                encalhe.setEstoque(TipoEstoque.LANCAMENTO.getDescricao());

    		} else if(encalhe.isMatrizRecolhimento()) {

    			encalhe.setEstoque(TipoEstoque.DEVOLUCAO_ENCALHE.getDescricaoAbreviada());
    			
    		} else {
    			
    			encalhe.setEstoque(TipoEstoque.SUPLEMENTAR.getDescricao());
    		}

    	} else {

            if (encalhe.isChamadao() && !encalhe.isMatrizRecolhimento()) {

            	encalhe.setEstoque(TipoEstoque.SUPLEMENTAR.getDescricao());

            } else if (isEstoqueLancamento(encalhe)) {

            	encalhe.setEstoque(TipoEstoque.LANCAMENTO.getDescricao());

            } else {

            	encalhe.setEstoque(TipoEstoque.DEVOLUCAO_ENCALHE.getDescricaoAbreviada());            
            }    		
    	}    	
    }
    
    /**
     * Retorna true se o produto edição for parcial e seu 
     * tipo de lancamento for PARCIAL, ja nesse cenário este
     * se encontra no estoque de LANCAMENTO.
     * 
     * @param encalhe
     * @return boolean
     * 
     */
    private boolean isEstoqueLancamento(FechamentoFisicoLogicoDTO encalhe) {
    	
    	return "P".equals(encalhe.getTipo()) && 
    			encalhe.getRecolhimento() != null && 
    			TipoLancamentoParcial.PARCIAL.name().equals(encalhe.getRecolhimento());
    	
    }
    
    
    @Override
    @Transactional
    public void salvarFechamentoEncalhe(final FiltroFechamentoEncalheDTO filtro,
            final List<FechamentoFisicoLogicoDTO> listaFechamento) {
        
        FechamentoFisicoLogicoDTO fechamento;
        BigInteger qtd = null;
        
        for (int i = 0; i < listaFechamento.size(); i++) {
            
            fechamento = listaFechamento.get(i);
            
            final BigInteger exemplaresDevolucao = fechamento.getFisico() != null ?
            		fechamento.getFisico() : fechamento.getExemplaresDevolucao() == null ? BigInteger.ZERO : fechamento.getExemplaresDevolucao();
            
            final BigInteger exemplaresDevolucaoJuramentado = fechamento.getExemplaresDevolucaoJuramentado() == null ? 
            		BigInteger.ZERO : fechamento.getExemplaresDevolucaoJuramentado();

            final BigInteger exemplaresVendaEncalhe = fechamento.getExemplaresVendaEncalhe() == null ? 
            		BigInteger.ZERO : fechamento.getExemplaresVendaEncalhe();
            
            if (exemplaresDevolucao == null) {
            	
            	throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, indique valor de físico para todos os produtos.");            	
            }
            
            if(!exemplaresVendaEncalhe.equals(BigInteger.ZERO)) {
            	qtd =  fechamento.getFisico();
            } else {
            	
            	qtd = exemplaresDevolucao.subtract(exemplaresDevolucaoJuramentado).subtract(exemplaresVendaEncalhe);
            }

            final FechamentoEncalhePK id = new FechamentoEncalhePK();
            id.setDataEncalhe(filtro.getDataEncalhe());
            final ProdutoEdicao pe = new ProdutoEdicao();
            pe.setId(fechamento.getProdutoEdicao());
            id.setProdutoEdicao(pe);
            
            FechamentoEncalhe fechamentoEncalhe = fechamentoEncalheRepository.buscarPorId(id);
            
            if (fechamentoEncalhe == null) {
                
                fechamentoEncalhe = new FechamentoEncalhe();
                fechamentoEncalhe.setFechamentoEncalhePK(id);
                fechamentoEncalhe.setQuantidade(qtd);
                
                fechamentoEncalheRepository.adicionar(fechamentoEncalhe);
                
            } else {
               
                fechamentoEncalhe.setQuantidade(qtd);
                fechamentoEncalheRepository.alterar(fechamentoEncalhe);
            }
            
            fechamento.setFisico(qtd); // retorna valor pra tela
        }
        
        fechamentoEncalheRepository.flush();
    }
    
    private Integer obterDiaRecolhimento(final Date dataRecolhimento) {
        
        if (dataRecolhimento == null) {
            return null;
        }
        
        return DateUtil.obterDiaDaSemana(dataRecolhimento);
        
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CotaAusenteEncalheDTO> buscarCotasAusentes(final Date dataEncalhe, final boolean isSomenteCotasSemAcao,
            final String sortorder, final String sortname, final int page, final int rp) {
        
        final int startSearch = page * rp - rp;
        
        final Integer diaRecolhimento = obterDiaRecolhimento(dataEncalhe);
        
        DiaSemana diaSemanaRecolhimento = DiaSemana.getByCodigoDiaSemana(diaRecolhimento);
        
        final List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe = fechamentoEncalheRepository.obterCotasAusentes(
                dataEncalhe, diaSemanaRecolhimento, isSomenteCotasSemAcao, sortorder, sortname, startSearch, rp);
        
        if (!isSomenteCotasSemAcao) {
            
            for (final CotaAusenteEncalheDTO cotaAusenteEncalheDTO : listaCotaAusenteEncalhe) {
                
                carregarDescricaoCotaAusente(cotaAusenteEncalheDTO);
            }
        }
        
        return listaCotaAusenteEncalhe;
    }
    
    private void carregarDescricaoCotaAusente(final CotaAusenteEncalheDTO cotaAusenteEncalheDTO) {
        
        if (!cotaAusenteEncalheDTO.isIndPossuiChamadaEncalheCota()) {
            
            if (cotaAusenteEncalheDTO.isIndMFCNaoConsolidado()) {
                
                cotaAusenteEncalheDTO.setAcao(" Sem C.E.-Cota com Divida ");
            } else {
                
                cotaAusenteEncalheDTO.setAcao(" Sem C.E.-Cobrado ");
            }
        } else if (cotaAusenteEncalheDTO.isUnificacao()) {
            
            cotaAusenteEncalheDTO.setAcao(" Cota com unificação ");
        } else {
            
            if (cotaAusenteEncalheDTO.isFechado()) {
                
                cotaAusenteEncalheDTO.setAcao("Cobrado");
                
            } else if (cotaAusenteEncalheDTO.isPostergado()) {
                
                final Date dataPostergacao = fechamentoEncalheRepository.obterChamdasEncalhePostergadas(
                        cotaAusenteEncalheDTO.getIdCota(), cotaAusenteEncalheDTO.getDataEncalhe());
                
                cotaAusenteEncalheDTO.setAcao("Postergado, " + DateUtil.formatarData(dataPostergacao, "dd/MM/yyyy"));
            }
            
        }
        
        Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
        
        List<GrupoCota> gps = this.grupoRepository.obterListaGrupoCotaPorCotaId(cotaAusenteEncalheDTO.getIdCota(), dataOperacao);
		
		if (gps != null && !gps.isEmpty()){
			
            cotaAusenteEncalheDTO.setAcao((cotaAusenteEncalheDTO.getAcao() == null || cotaAusenteEncalheDTO.getAcao().trim().isEmpty()) ? 
            		                       "Operação Diferenciada" : cotaAusenteEncalheDTO.getAcao() + " / Operação Diferenciada");
        }
        
    }
    
    @Override
    @Transactional(readOnly = true)
    public Integer buscarTotalCotasAusentes(final Date dataEncalhe, final boolean isSomenteCotasSemAcao, 
            Integer numeroCota) {
        
        final Integer diaRecolhimento = obterDiaRecolhimento(dataEncalhe);
        
        DiaSemana diaSemanaRecolhimento = DiaSemana.getByCodigoDiaSemana(diaRecolhimento);
        
        return fechamentoEncalheRepository.obterTotalCotasAusentes(dataEncalhe, diaSemanaRecolhimento, isSomenteCotasSemAcao,
                null, null, 0, 0, numeroCota);
    }
    
    @Override
    @Transactional
    public void postergarCotas(final Date dataEncalhe, final Date dataPostergacao, final List<Long> idsCotas) {
        
        if (idsCotas == null || idsCotas.isEmpty()) {
            throw new IllegalArgumentException("Lista de ids das cotas não pode ser nula e nem vazia.");
        }
        
        if (dataEncalhe == null) {
            throw new IllegalArgumentException("Data de encalhe não pode ser nula.");
        }
        
        final List<CotaAusenteEncalheDTO> listaCotasAusentes = this.buscarCotasAusentes(dataEncalhe, true, null, null,
                0, 0);
        
        final Map<String, ChamadaEncalhe> chamadasEncalheAPostergar = obterChamadasEncalheAPostergar(dataEncalhe,
                dataPostergacao, listaCotasAusentes);
        
        for (final Long idCota : idsCotas) {
            
            this.postergar(dataEncalhe, dataPostergacao, idCota, chamadasEncalheAPostergar);
        }
    }
    
    @Override
    @Transactional
    public void postergarTodasCotas(final Date dataEncalhe, final Date dataPostergacao,
            final List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe) {
        
        if (dataEncalhe == null) {
            throw new IllegalArgumentException("Data de encalhe não pode ser nula.");
        }
        
        final Map<String, ChamadaEncalhe> chamadasEncalheAPostergar = obterChamadasEncalheAPostergar(dataEncalhe,
                dataPostergacao, listaCotaAusenteEncalhe);
        
        for (final CotaAusenteEncalheDTO cotaAusente : listaCotaAusenteEncalhe) {
            
            this.postergar(dataEncalhe, dataPostergacao, cotaAusente.getIdCota(), chamadasEncalheAPostergar);
        }
    }
    
    private Map<String, ChamadaEncalhe> obterChamadasEncalheAPostergar(final Date dataEncalhe,
            final Date dataPostergacao, final List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe) {
        
        Integer sequencia = chamadaEncalheRepository.obterMaiorSequenciaPorDia(dataPostergacao);
        
        final Set<ChamadaEncalhe> chamadasEncalheAPostergarSet = new HashSet<>();
        for (final CotaAusenteEncalheDTO cotaAusente : listaCotaAusenteEncalhe) {
            chamadasEncalheAPostergarSet.addAll(chamadaEncalheRepository.obterChamadasEncalhePor(dataEncalhe,
                    cotaAusente.getIdCota()));
        }
        
        final List<ChamadaEncalhe> chamadasEncalheAPostergarList = new ArrayList<>(chamadasEncalheAPostergarSet);
        Collections.sort(chamadasEncalheAPostergarList, new Comparator<ChamadaEncalhe>() {
            
            @Override
            public int compare(final ChamadaEncalhe o1, final ChamadaEncalhe o2) {
                return (o1.getSequencia().equals(o2.getSequencia()) ? 0 : (o1.getSequencia() < o2.getSequencia() ? -1
                        : 1));
            }
        });
        
        final Map<String, ChamadaEncalhe> chamadasEncalheAPostergar = new HashMap<>();
        for (final ChamadaEncalhe ce : chamadasEncalheAPostergarList) {
            ce.setSequencia(++sequencia);
            chamadasEncalheAPostergar.put(ce.getProdutoEdicao().getId().toString(), ce);
        }
        
        return chamadasEncalheAPostergar;
        
    }
    
    @Override
    @Transactional(noRollbackFor = GerarCobrancaValidacaoException.class)
    public void cobrarCota(final Date dataOperacao, final Usuario usuario, final Long idCota)
            throws GerarCobrancaValidacaoException {
        
        final Cota cota = cotaRepository.buscarCotaPorID(idCota);
        
        this.realizarCobrancaCotas(dataOperacao, usuario, null, cota);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = { GerarCobrancaValidacaoException.class,
            AutenticacaoEmailException.class })
    public void realizarCobrancaCotas(final Date dataOperacao, final Usuario usuario,
            List<CotaAusenteEncalheDTO> listaCotasAusentes, final Cota cotaAusente)
                    throws GerarCobrancaValidacaoException {
        
        final ValidacaoVO validacaoVO = new ValidacaoVO();
        
        if (cotaAusente != null) {
            
            listaCotasAusentes = new ArrayList<>();
            final CotaAusenteEncalheDTO cotaAusenteEncalheDTO = new CotaAusenteEncalheDTO();
            cotaAusenteEncalheDTO.setIdCota(cotaAusente.getId());
            listaCotasAusentes.add(cotaAusenteEncalheDTO);
        }
        
        for (final CotaAusenteEncalheDTO cotaAusenteEncalheDTO : listaCotasAusentes) {
            
            this.realizarCobrancaCota(dataOperacao, usuario, cotaAusenteEncalheDTO.getIdCota(), validacaoVO);
        }
        
        // Se um dia precisar tratar as mensagens de erro de e-mail, elas estão
        // nesta lista
        /*
         * if (validacaoEmails.getListaMensagens() != null &&
         * !validacaoEmails.getListaMensagens().isEmpty()) { }
         */
        
        if (validacaoVO.getListaMensagens() != null && !validacaoVO.getListaMensagens().isEmpty()) {
            
            throw new GerarCobrancaValidacaoException(validacaoVO);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = { GerarCobrancaValidacaoException.class, AutenticacaoEmailException.class })
    public void realizarCobrancaCota(final Date dataOperacao, final Usuario usuario, final Long idCota, final ValidacaoVO validacaoVO) {
        
        final Set<String> nossoNumeroEnvioEmail = new HashSet<String>();
        
        final Cota cota = cotaRepository.buscarCotaPorID(idCota);
        
        if (cota == null) {
            throw new ValidacaoException(TipoMensagem.ERROR, "Cota inexistente.");
        } 
        
        BigDecimal reparte = chamadaEncalheCotaRepository.obterReparteDaChamaEncalheCota(cota.getNumeroCota(), Arrays.asList(dataOperacao), false, false);
        
        reparte = reparte != null ? reparte : BigDecimal.ZERO;
        
        negociacaoDividaService.abaterNegociacaoPorComissao(idCota, reparte, BigDecimal.ZERO, usuario);
        
        this.gerarMovimentosFinanceiros(cota, dataOperacao, usuario);
        
        if (cota.getTipoCota().equals(TipoCota.CONSIGNADO)) {
           
        	// se a cota for unificadora ou unificada não pode gerar cobrança nesse ponto
			final boolean cotaUnificada = this.cotaUnificacaoRepository.verificarCotaUnificada(cota.getNumeroCota()),

			cotaUnificadora = this.cotaUnificacaoRepository.verificarCotaUnificadora(cota.getNumeroCota());
			
			if (!cotaUnificadora && !cotaUnificada) {

	            try {
	                
	                final boolean existeBoletoAntecipado = boletoService.existeBoletoAntecipadoCotaDataRecolhimento(cota.getId(), dataOperacao);
	                
	                FormaCobranca fc = formaCobrancaService.obterFormaCobrancaCota(cota.getId(), null, dataOperacao);
	                
	                if (existeBoletoAntecipado 
	                		|| (fc != null && fc.getTipoCobranca() != null && TipoCobranca.BOLETO_EM_BRANCO.equals(fc.getTipoCobranca()))) {
	                    
	                    gerarCobrancaService.gerarDividaPostergada(cota.getId(), usuario.getId());
	                } else {
	                    
	                    gerarCobrancaService.gerarCobranca(cota.getId(), usuario.getId(), nossoNumeroEnvioEmail, new HashSet<String>());
	                }
	            } catch (final GerarCobrancaValidacaoException e) {
	                LOGGER.error(e.getMessage(), e);
	                if (validacaoVO.getListaMensagens() == null) {
	                    
	                    validacaoVO.setListaMensagens(new ArrayList<String>());
	                }
	                
	                validacaoVO.getListaMensagens().addAll(e.getValidacaoVO().getListaMensagens());
	            }
			}
        }
        
        final List<ChamadaEncalheCota> listaChamadaEncalheCota = chamadaEncalheCotaRepository.obterListChamadaEncalheCota(cota.getId(), dataOperacao);
        
        for (final ChamadaEncalheCota chamadaEncalheCota : listaChamadaEncalheCota) {
            
            chamadaEncalheCota.setFechado(true);
            
            chamadaEncalheCotaRepository.merge(chamadaEncalheCota);
        }
    }
    
    private void gerarMovimentosFinanceiros(final Cota cota, 
    		                                final Date dataOperacao,
    		                                final Usuario usuario) {
        
        if (cota.getTipoCota().equals(TipoCota.CONSIGNADO)) {
            
            // CANCELA DIVIDA EXCLUI CONSOLIDADO E MOVIMENTOS FINANCEIROS DE
            // REPARTE X ENCALHE (RECEBIMENTO_REPARTE E ENVIO_ENCALHE) PARA QUE
            // SEJAM RECRIADOS
            gerarCobrancaService.cancelarDividaCobranca(null, cota.getId(), dataOperacao, true);
        } else if (cota.getTipoCota().equals(TipoCota.A_VISTA)) {
            
            // EXLUI MOVIMENTOS FINANCEIROS COTA PARA CRIÁ-LOS NOVAMENTE
            movimentoFinanceiroCotaService.removerMovimentosFinanceirosCotaConferenciaNaoConsolidados(cota.getNumeroCota(), 
            		                                                                                  dataOperacao);
        }
        
        final List<Date> datasRecolhimento = this.grupoService.obterDatasRecolhimentoOperacaoDiferenciada(cota.getNumeroCota(), 
                																						  dataOperacao);
        
        if(datasRecolhimento != null && !datasRecolhimento.isEmpty()) {

            // CRIA MOVIMENTOS FINANCEIROS DE REPARTE X ENCALHE (RECEBIMENTO_REPARTE
            // E ENVIO_ENCALHE)
            movimentoFinanceiroCotaService.gerarMovimentoFinanceiroCota(cota, datasRecolhimento, usuario, null, null); 
        	
        }
        
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal buscarValorTotalEncalhe(final Date dataEncalhe, final Long idCota) {
        
        final List<FechamentoFisicoLogicoDTO> list = fechamentoEncalheRepository.buscarValorTotalEncalhe(dataEncalhe,
                idCota);
        BigDecimal soma = BigDecimal.ZERO;
        
        for (final FechamentoFisicoLogicoDTO dto : list) {
            soma = soma.add(new BigDecimal(dto.getExemplaresDevolucao()).multiply(dto.getPrecoCapa()));
        }
        
        return soma;
    }
    
    /**
     * Ao realizar o encerramento do encalhe serão gerados registros em
     * MovimentoEstoqueCota e MovimentoEstoque que correspondem ao produtos que
     * as cotas devolveram ao distribuidor de forma juramentada.
     * 
     */
    private void processarMovimentosProdutosJuramentados(final Date dataEncalhe, final Usuario usuario,
            final Date dataOperacao) {
        
        final List<MovimentoEstoqueCotaGenericoDTO> listaMovimentoEstoqueCota = movimentoEstoqueCotaRepository
                .obterListaMovimentoEstoqueCotaDevolucaoJuramentada(dataEncalhe);
        
        final TipoMovimentoEstoque tipoMovEstoqueEnvioJornaleiroJuramentado = tipoMovimentoEstoqueRepository
                .buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO_JURAMENTADO);
        
        for (final MovimentoEstoqueCotaGenericoDTO item : listaMovimentoEstoqueCota) {
            
            Date dataProximoLancamento = 
                this.processarEstudoCotaLancamentoParcial(item, usuario.getId(), dataOperacao);
            
            movimentoEstoqueService.gerarMovimentoEstoque(item.getIdProdutoEdicao(), usuario.getId(), item
                    .getQtde(), tipoMovEstoqueEnvioJornaleiroJuramentado, dataProximoLancamento, false);
        }
        
        for (final MovimentoEstoqueCotaGenericoDTO item : listaMovimentoEstoqueCota) {
        
            final Lancamento lancamentoParcial =
                lancamentoRepository.obterLancamentoParcialChamadaEncalhe(item.getIdChamadaEncalhe());
            
            final Lancamento proximoLancamentoParcial = parciaisService.getProximoLancamentoPeriodo(lancamentoParcial);
            
            this.lancamentoService.reajustarNumerosLancamento(proximoLancamentoParcial.getPeriodoLancamentoParcial());
        }
    }
    
    /*
     * Cria estudo e estudo cota para os proximos lançamentos parciais
     * juramentado
     */
    private Date processarEstudoCotaLancamentoParcial(final MovimentoEstoqueCotaGenericoDTO item, final Long usuarioId,
            final Date dataOperacao) {
        
        final Lancamento lancamentoParcial = lancamentoRepository.obterLancamentoParcialChamadaEncalhe(item
                .getIdChamadaEncalhe());
        
        if (lancamentoParcial == null) {
            return null;
        }

        final Lancamento proximoLancamentoPeriodo = this.getNovoLancamentoJuramentado(lancamentoParcial);
        
        if (proximoLancamentoPeriodo == null) {
            return null;
        }
        
        Estudo estudo = null;
        
        Date dataProximoLancamento = proximoLancamentoPeriodo.getDataLancamentoDistribuidor();
        
        EstudoGerado estudoGerado = null;
        
        if (proximoLancamentoPeriodo.getEstudo() == null) {
            
            estudoGerado = estudoService.criarEstudo(proximoLancamentoPeriodo.getProdutoEdicao(),
                    item.getQtde(), dataProximoLancamento, proximoLancamentoPeriodo.getId());
            
            estudo = estudoService.liberar(estudoGerado.getId());
            
        } else {
            
            estudoGerado = this.estudoGeradoRepository.buscarPorId(proximoLancamentoPeriodo.getEstudo().getId());
            
            estudo = this.estudoService.atualizarEstudo(proximoLancamentoPeriodo.getEstudo().getId(), item.getQtde());
        }

        EstudoCotaGerado estudoCotaGerado =
            estudoCotaService.criarEstudoCotaJuramentado(
                proximoLancamentoPeriodo.getProdutoEdicao(), estudoGerado, item.getQtde(), new Cota(item.getIdCota()));
        
        EstudoCota estudoCota = estudoCotaService.liberar(estudoCotaGerado.getId(), estudo);
        
        TipoMovimentoEstoque tipoMovimentoEstoque = 
                this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
                        GrupoMovimentoEstoque.RECEBIMENTO_JORNALEIRO_JURAMENTADO);
        
        MovimentoEstoqueCota movimentoEstoqueCotaJuramentado =
                this.movimentoEstoqueCotaRepository.buscarPorId(item.getMovimentoEstoqueCotaId());
        
        MovimentoEstoqueCota movimentoEstoqueCota = 
            this.movimentoEstoqueService.gerarMovimentoCota(proximoLancamentoPeriodo.getDataCriacao(), 
                item.getIdProdutoEdicao(), item.getIdCota(), usuarioId, item.getQtde(), tipoMovimentoEstoque, 
                dataProximoLancamento, dataOperacao, proximoLancamentoPeriodo.getId(), estudoCota.getId());
        
        movimentoEstoqueCotaJuramentado.setMovimentoEstoqueCotaJuramentado(movimentoEstoqueCota);
        
        this.movimentoEstoqueCotaRepository.merge(movimentoEstoqueCotaJuramentado);
        
        return dataProximoLancamento;
    }
    
    private Lancamento getNovoLancamentoJuramentado(final Lancamento lancamentoParcial) {
    	
    	final Lancamento proximoLancamentoParcial = parciaisService.getProximoLancamentoPeriodo(lancamentoParcial);
    	
    	if (proximoLancamentoParcial == null) {
            return null;
        }
    	
    	Date dataNovoLancamento = this.getDataNovoLancamentoJuramentado(lancamentoParcial, proximoLancamentoParcial);
    	
    	if (proximoLancamentoParcial.getJuramentado() == null
    	        || !proximoLancamentoParcial.getJuramentado()) {
    	    
    	    this.ajustarDataProximoLancamentoParcial(dataNovoLancamento, proximoLancamentoParcial);
    	    
    	    return this.criarNovoLancamentoJuramentado(proximoLancamentoParcial, dataNovoLancamento);
    	}
    	
    	return proximoLancamentoParcial;
    }
    
    private Lancamento criarNovoLancamentoJuramentado(Lancamento proximoLancamentoParcial, Date dataNovoLancamento) {

        proximoLancamentoParcial.setTipoLancamento(TipoLancamento.REDISTRIBUICAO);

        this.lancamentoRepository.merge(proximoLancamentoParcial);

        Lancamento novoLancamento = null;
        
        try {

            novoLancamento = (Lancamento) BeanUtils.cloneBean(proximoLancamentoParcial);
            
        } catch (Exception e) {

            throw new IllegalArgumentException(e);
        }

        novoLancamento.setReparte(BigInteger.ZERO);
        novoLancamento.setRepartePromocional(BigInteger.ZERO);
        novoLancamento.setDataLancamentoPrevista(dataNovoLancamento);
        novoLancamento.setDataLancamentoDistribuidor(dataNovoLancamento);
        novoLancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
        novoLancamento.setStatus(StatusLancamento.EXPEDIDO);
        novoLancamento.setNumeroLancamento(null);
        novoLancamento.setEstudo(null);
        novoLancamento.setChamadaEncalhe(null);
        novoLancamento.setHistoricos(null);
        novoLancamento.setMovimentoEstoqueCotas(null);
        novoLancamento.setRecebimentos(null);
        novoLancamento.setJuramentado(true);

        Long id = this.lancamentoRepository.adicionar(novoLancamento);
        
        novoLancamento = this.lancamentoRepository.buscarPorId(id);
        
        PeriodoLancamentoParcial periodo = proximoLancamentoParcial.getPeriodoLancamentoParcial();
        periodo.getLancamentos().add(novoLancamento);
        
        this.periodoLancamentoParcialRepository.merge(periodo);
        
        return novoLancamento;
    }

    private void ajustarDataProximoLancamentoParcial(Date dataNovoLancamento, Lancamento proximoLancamentoParcial) {

        Date dataLancamento = proximoLancamentoParcial.getDataLancamentoDistribuidor();
        
        if (dataNovoLancamento.compareTo(proximoLancamentoParcial.getDataLancamentoDistribuidor()) == 0) {
            
            Date dataLancamentoAjustada = this.calendarioService.adicionarDiasUteis(dataLancamento, 1);
            
            proximoLancamentoParcial.setDataLancamentoDistribuidor(dataLancamentoAjustada);
            proximoLancamentoParcial.setDataLancamentoPrevista(dataLancamentoAjustada);
            
            this.lancamentoRepository.merge(proximoLancamentoParcial);
        }
    }

    private Date getDataNovoLancamentoJuramentado(final Lancamento lancamentoParcial, final Lancamento proximoLancamentoParcial) {
    	
        final Date dataNovoLancamento =
            this.calendarioService.adicionarDiasUteis(
                lancamentoParcial.getDataRecolhimentoDistribuidor(), 1);

        return dataNovoLancamento;
    }
    
    @Override
    @Transactional
    public List<CotaDTO> obterListaCotaConferenciaNaoFinalizada(final Date dataOperacao) {
        return conferenciaEncalheRepository.obterListaCotaConferenciaNaoFinalizada(dataOperacao);
    }
    
    
    private void agendarAgoraAtualizacaoEstoqueProdutoConf() {

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
	    JobDetail job = newJob(AtualizaEstoqueJob.class).build();
	    
	    Trigger trigger = newTrigger().startNow().build();
        
		try {
			
			scheduler.scheduleJob(job, trigger);
		
		} catch (SchedulerException e) {
        
			throw new ValidacaoException(TipoMensagem.WARNING, "Falha na atualização de estoque de produtos");
		
		}
		
    }
    
    /**
     * Método que faz consulta na tbl estoque_produto_fila. 
     * Caso existam registros nessa tbl sera disparado o job 
     * de atualizacao de estoque de produto informado o usuário 
     * para tentar o encerramento o de encalhe dentro de alguns 
     * minutos.
     */
    @Transactional(readOnly=true)
    public void verificarEstoqueProdutoNaoAtualizado() {
    	
    	boolean indExisteEstoqueProdutoFila = estoqueProdutoFilaRepository.verificarExitenciaEstoqueProdutoFila();
    	
    	if(indExisteEstoqueProdutoFila) {
    		
    		agendarAgoraAtualizacaoEstoqueProdutoConf();

        	throw new ValidacaoException(TipoMensagem.WARNING, "Existem registros de estoque de produto desatualizados, tente novamente dentro de alguns minutos.");
    		
    	}
    	
    }
    
    
    @Override
    @Transactional
    public Set<String> encerrarOperacaoEncalhe(final Date dataEncalhe, final Usuario usuario,
            final FiltroFechamentoEncalheDTO filtroSessao, final List<FechamentoFisicoLogicoDTO> listaEncalheSessao,
            final boolean cobrarCotas) {
    	
        final Integer totalCotasAusentes = this.buscarTotalCotasAusentesSemPostergado(dataEncalhe, true, true);
        
        if (totalCotasAusentes > 0) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Cotas ausentes existentes!");
        }
        
        final ControleFechamentoEncalhe controleFechamentoEncalhe = new ControleFechamentoEncalhe();
        controleFechamentoEncalhe.setDataEncalhe(dataEncalhe);
        controleFechamentoEncalhe.setUsuario(usuario);
        
        fechamentoEncalheRepository.salvarControleFechamentoEncalhe(controleFechamentoEncalhe);
        
        // TODO: Refatorar a parte de fechamento de encalhe para melhor
        // desempenho
        final List<FechamentoFisicoLogicoDTO> listaEncalhe = this.buscarFechamentoEncalhe(filtroSessao, null, null,null, null);

        this.processarMovimentosProdutosJuramentados(dataEncalhe, usuario, distribuidorRepository
                .obterDataOperacaoDistribuidor());
        
        if (!listaEncalhe.isEmpty()) {
            
            for (final FechamentoFisicoLogicoDTO item : listaEncalhe) {
                
                gerarMovimentoFaltasSobras(item, usuario);
                
                if (item.getRecolhimento() != null && TipoLancamentoParcial.PARCIAL.name().equals(item.getRecolhimento())) {
                	
                	this.tratarEncalheProdutoEdicaoParcial(item, usuario, item.getFisico());

                } else if (item.isChamadao() && item.isMatrizRecolhimento()) { 

                	movimentoEstoqueService.transferirEstoqueProdutoChamadaoParaRecolhimento(item.getProdutoEdicao(), usuario);          	                	
                }                
            }
        }
        
        // Cobra cotas as demais cotas, no caso, as não ausentes e com centralização
        // Não gera cobrança para cotas do tipo À Vista
        final Set<String> nossoNumeroCentralizacao = new HashSet<String>();
        
        if (cobrarCotas) {
        	
            try {
                
                gerarCobrancaService.gerarCobranca(null, 
                		                           usuario.getId(), 
                		                           new HashSet<String>(), 
                		                           nossoNumeroCentralizacao, 
                		                           Arrays.asList(TipoCota.CONSIGNADO));
                
            } catch (final GerarCobrancaValidacaoException e) {
            	
                LOGGER.error(e.getMessage(), e);
                
                throw new ValidacaoException(e.getValidacaoVO());
            }
        }
        
        return nossoNumeroCentralizacao;
    }

    private void tratarEncalheProdutoEdicaoParcial(final FechamentoFisicoLogicoDTO item, final Usuario usuario, final BigInteger encalheFisico) {
        
    	if(item.isChamadao() && !item.isMatrizRecolhimento()) {
    		return;
    	}
    	movimentoEstoqueService.transferirEstoqueProdutoEdicaoParcialParaLancamento(item.getProdutoEdicao(), usuario);
        
        final Lancamento lancamentoParcial = lancamentoRepository.obterLancamentoParcialChamadaEncalhe(item
                .getChamadaEncalheId());
        
        if (lancamentoParcial != null) {
            
            parciaisService.atualizarReparteDoProximoLancamentoPeriodo(
                lancamentoParcial, usuario, encalheFisico);
        }
    }

   

    private void gerarMovimentoFaltasSobras(final FechamentoFisicoLogicoDTO item, final Usuario usuarioLogado) {
        
        BigInteger qntDiferenca = BigInteger.ZERO;
        if (item != null && item.getDiferenca() != null) {
            qntDiferenca = new BigInteger(item.getDiferenca().toString());
        }
        
        if (qntDiferenca.compareTo(BigInteger.ZERO) == 0) {
            return;
        }
        
        final ProdutoEdicao produtoEdicao = edicaoRepository.buscarPorId(item.getProdutoEdicao());
        
        final Diferenca diferenca = new Diferenca();
        
        diferenca.setQtde(qntDiferenca.abs());
        diferenca.setResponsavel(usuarioLogado);
        diferenca.setProdutoEdicao(produtoEdicao);
        
        if (qntDiferenca.compareTo(BigInteger.ZERO) < 0) {
            
            diferenca.setTipoDiferenca(TipoDiferenca.PERDA_EM);
            
            diferencaEstoqueService.lancarDiferencaAutomatica(diferenca, TipoEstoque.RECOLHIMENTO,
                    StatusAprovacao.PERDA, Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_FECHAMENTO_ENCALHE);
            
        } else if (qntDiferenca.compareTo(BigInteger.ZERO) > 0) {
            
            diferenca.setTipoDiferenca(TipoDiferenca.GANHO_EM);
            
            diferencaEstoqueService.lancarDiferencaAutomatica(diferenca, TipoEstoque.RECOLHIMENTO,
                    StatusAprovacao.GANHO, Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_FECHAMENTO_ENCALHE);
            
        }
    }
    
    @Transactional
    private boolean validarEncerramentoOperacao(final Date dataEncalhe) {
        
        int countFechamento = 0;
        int countConferencia = 0;
        boolean porBox;
        
        final List<FechamentoEncalhe> listFechamentoEncalhe = fechamentoEncalheRepository
                .buscarFechamentoEncalhe(dataEncalhe);
        
        if (listFechamentoEncalhe.isEmpty()) {
            // Não tem nada salvo
            return false;
        }
        
        if (listFechamentoEncalhe.get(0).getListFechamentoEncalheBox() == null
                || listFechamentoEncalhe.get(0).getListFechamentoEncalheBox().isEmpty()) {
            // consolidado
            porBox = false;
            countFechamento = listFechamentoEncalhe.size();
        } else {
            // por box
            porBox = true;
            for (final FechamentoEncalhe fechamentoEncalhe : listFechamentoEncalhe) {
                countFechamento += fechamentoEncalhe.getListFechamentoEncalheBox().size();
            }
        }
        
        countConferencia = fechamentoEncalheRepository.buscaQuantidadeConferencia(dataEncalhe, porBox);
        
        return countFechamento == countConferencia;
    }
    
    @Transactional
    private void postergar(final Date dataEncalhe, final Date dataPostergacao, final Long idCota,
            final Map<String, ChamadaEncalhe> chamadasEncalheAPostergar) {
        
        if (chamadasEncalheAPostergar == null) {
            throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao obter sequência.");
        }
        
        final List<ChamadaEncalheCota> listChamadaEncalheCota = fechamentoEncalheRepository.buscarChamadaEncalheCota(
                dataEncalhe, idCota);
        
        for (final ChamadaEncalheCota chamadaEncalheCota : listChamadaEncalheCota) {
            
            // Atualizando para postergado
            chamadaEncalheCota.setPostergado(true);
            chamadaEncalheCotaRepository.merge(chamadaEncalheCota);
            
            // Criando chamada de encalhe
            ChamadaEncalhe chamadaEncalhe = chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(
                    chamadaEncalheCota.getChamadaEncalhe().getProdutoEdicao(), dataPostergacao, chamadaEncalheCota
                    .getChamadaEncalhe().getTipoChamadaEncalhe());
            
            final ChamadaEncalhe chamadaEncalheOriginal = chamadaEncalheCota.getChamadaEncalhe();
            
            if (chamadaEncalhe == null) {
                
                chamadaEncalhe = new ChamadaEncalhe();
                chamadaEncalhe.setDataRecolhimento(dataPostergacao);
                chamadaEncalhe.setProdutoEdicao(chamadaEncalheCota.getChamadaEncalhe().getProdutoEdicao());
                chamadaEncalhe.setTipoChamadaEncalhe(chamadaEncalheCota.getChamadaEncalhe().getTipoChamadaEncalhe());
                
                final Set<Lancamento> lancamentos = chamadaEncalheRepository.obterLancamentos(chamadaEncalheOriginal
                        .getId());
                
                chamadaEncalhe.setLancamentos(lancamentos);
                if (chamadaEncalheCota.getChamadaEncalhe() != null
                        && chamadaEncalheCota.getChamadaEncalhe().getProdutoEdicao() != null
                        && chamadaEncalheCota.getChamadaEncalhe().getProdutoEdicao().getId() != null) {
                    
                    chamadaEncalhe.setSequencia(chamadasEncalheAPostergar.get(
                            chamadaEncalheCota.getChamadaEncalhe().getProdutoEdicao().getId().toString())
                            .getSequencia());
                }
                
                chamadaEncalheRepository.adicionar(chamadaEncalhe);
            }
            
            if (BigInteger.ZERO.compareTo(chamadaEncalheCota.getQtdePrevista()) < 0) {
                
                // Criando novo chamadaEncalheCota
                final ChamadaEncalheCota cce = new ChamadaEncalheCota();
                cce.setChamadaEncalhe(chamadaEncalhe);
                cce.setCota(chamadaEncalheCota.getCota());
                cce.setQtdePrevista(chamadaEncalheCota.getQtdePrevista());
                chamadaEncalheCotaRepository.adicionar(cce);
                
            }
            
        }
    }

    @Override
    @Transactional
    public void salvarFechamentoEncalheBox(final FiltroFechamentoEncalheDTO filtro,
            final List<FechamentoFisicoLogicoDTO> listaFechamento) {
        
        FechamentoFisicoLogicoDTO fechamento;
        BigInteger qtd = null;
        
        for (int i = 0; i < listaFechamento.size(); i++) {
            
            fechamento = listaFechamento.get(i);

            if (filtro.isCheckAll()) {
                
                qtd = fechamento.getExemplaresDevolucao();
                
            } else {
                
                qtd = fechamento.getFisico() == null ? BigInteger.ZERO : fechamento.getFisico();
            }
            
            final FechamentoEncalhePK id = new FechamentoEncalhePK();
            id.setDataEncalhe(filtro.getDataEncalhe());
            final ProdutoEdicao pe = new ProdutoEdicao();
            pe.setId(fechamento.getProdutoEdicao());
            id.setProdutoEdicao(pe);
            FechamentoEncalheBox fechamentoEncalheBox = new FechamentoEncalheBox();
            
            final FechamentoEncalheBoxPK fechamentoEncalheBoxPK = new FechamentoEncalheBoxPK();
            final Box box = new Box();
            box.setId(filtro.getBoxId());
            fechamentoEncalheBoxPK.setBox(box);
            
            FechamentoEncalhe fechamentoEncalhe = fechamentoEncalheRepository.buscarPorId(id);
            if (fechamentoEncalhe == null) {
                fechamentoEncalhe = new FechamentoEncalhe();
                fechamentoEncalhe.setFechamentoEncalhePK(id);
                fechamentoEncalheRepository.adicionar(fechamentoEncalhe);
                fechamentoEncalheBox.setQuantidade(qtd);
                fechamentoEncalheBoxPK.setFechamentoEncalhe(fechamentoEncalhe);
                fechamentoEncalheBox.setFechamentoEncalheBoxPK(fechamentoEncalheBoxPK);
                fechamentoEncalheBoxRepository.adicionar(fechamentoEncalheBox);
                
            } else {
                
                fechamentoEncalheBox = fechamentoEncalheBoxRepository.buscarPorId(fechamentoEncalheBoxPK);
                if (fechamentoEncalheBox == null) {
                    fechamentoEncalheBoxPK.setFechamentoEncalhe(fechamentoEncalhe);
                    fechamentoEncalheBox = new FechamentoEncalheBox();
                    fechamentoEncalheBox.setFechamentoEncalheBoxPK(fechamentoEncalheBoxPK);
                    fechamentoEncalheBox.setQuantidade(qtd);
                    
                } else {
                    fechamentoEncalheBox.setQuantidade(qtd);
                }
                fechamentoEncalheBoxRepository.merge(fechamentoEncalheBox);
                
            }
            
            fechamento.setFisico(qtd); // retorna valor pra tela
        }
        
    }
    
    /**
     * (non-Javadoc)
     * 
     * @see br.com.abril.nds.service.FechamentoEncalheService#existeFechamentoEncalheDetalhado(br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO)
     */
    @Override
    @Transactional(readOnly = true)
    public Boolean existeFechamentoEncalheDetalhado(final FiltroFechamentoEncalheDTO filtro) {
        
        return fechamentoEncalheBoxRepository.verificarExistenciaFechamentoEncalheDetalhado(filtro.getDataEncalhe());
        
    }
    
    /**
     * (non-Javadoc)
     * 
     * @see br.com.abril.nds.service.FechamentoEncalheService#existeFechamentoEncalheConsolidado(br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO)
     */
    @Override
    @Transactional(readOnly = true)
    public Boolean existeFechamentoEncalheConsolidado(final FiltroFechamentoEncalheDTO filtro) {
        
        return fechamentoEncalheRepository.verificarExistenciaFechamentoEncalheConsolidado(filtro.getDataEncalhe());
        
    }
    
    private FechamentoEncalhePK obterFechamentoEncalhePK(Date dataEncalhe, Long idProdutoEdicao) {
    	
    	 FechamentoEncalhePK id = new FechamentoEncalhePK();
    	 
    	 id.setDataEncalhe(dataEncalhe);
    	 
         ProdutoEdicao pe = new ProdutoEdicao();
         pe.setId(idProdutoEdicao);
         id.setProdutoEdicao(pe);
         
         return id;
    	 
    }
    
    /**
     * Remove o registro de fechamentoEncalheBox e 
     * sumariza sua qtde ao registro de fechamentoEncalhe relacionado.
     * 
     * @param fechamentoEncalhe
     */
    private void removerFechamentoEncalheBox(FechamentoEncalhe fechamentoEncalhe) {
    	
    	for (final FechamentoEncalheBox encalheBox : fechamentoEncalhe.getListFechamentoEncalheBox()) {
    		
            if (fechamentoEncalhe.getQuantidade() == null) {
            	
                fechamentoEncalhe.setQuantidade(encalheBox.getQuantidade());
                
            } else {
            	
                fechamentoEncalhe.setQuantidade(
                	(fechamentoEncalhe.getQuantidade() == null ? BigInteger.ZERO : fechamentoEncalhe.getQuantidade()).add( 
                	(encalheBox.getQuantidade() == null ? BigInteger.ZERO : encalheBox.getQuantidade()))
                );
                
            }
            
            fechamentoEncalheBoxRepository.remover(encalheBox);
            
        }
    	
    }
    
    @Override
    @Transactional
    public void converteFechamentoDetalhadoEmConsolidado(final FiltroFechamentoEncalheDTO filtro) {
        final List<FechamentoFisicoLogicoDTO> listaConferencia = this.buscarFechamentoEncalhe(filtro, null, "codigo",
                null, null);
        FechamentoFisicoLogicoDTO fechamento;
        
        for (int i = 0; i < listaConferencia.size(); i++) {
        	
            fechamento = listaConferencia.get(i);
            
            FechamentoEncalhePK id = obterFechamentoEncalhePK(filtro.getDataEncalhe(), fechamento.getProdutoEdicao());
            
            FechamentoEncalhe fechamentoEncalhe = fechamentoEncalheRepository.buscarPorId(id);
            
            if (fechamentoEncalhe == null) {
                continue;
            }
            
            removerFechamentoEncalheBox(fechamentoEncalhe);
            
            fechamentoEncalhe.setListFechamentoEncalheBox(null);
            
            fechamentoEncalheRepository.alterar(fechamentoEncalhe);
            
        }
        
    }
    
    @Override
    @Transactional
    public ControleFechamentoEncalhe buscaControleFechamentoEncalhePorData(final Date dataFechamentoEncalhe) {
        return fechamentoEncalheRepository.buscaControleFechamentoEncalhePorData(dataFechamentoEncalhe);
    }
    
    @Override
    @Transactional
    public Date buscaDataUltimoControleFechamentoEncalhe() {
        return fechamentoEncalheRepository.buscaDataUltimoControleFechamentoEncalhe();
    }
    
    @Override
    @Transactional
    public Date buscarUltimoFechamentoEncalheDia(final Date dataFechamentoEncalhe) {
        return fechamentoEncalheRepository.buscarUltimoFechamentoEncalheDia(dataFechamentoEncalhe);
    }
    
    @Override
    @Transactional
    public List<AnaliticoEncalheDTO> buscarAnaliticoEncalhe(final FiltroFechamentoEncalheDTO filtro,
            final String sortorder, final String sortname, final Integer page, final Integer rp) {
        
        Integer startSearch = null;
        if (page != null && rp != null) {
            startSearch = page * rp - rp;
        }
        
        return fechamentoEncalheRepository.buscarAnaliticoEncalhe(filtro, sortorder, sortname, startSearch, rp);
    }
    
    @Override
    @Transactional
    public BigDecimal obterValorTotalAnaliticoEncalhe(final FiltroFechamentoEncalheDTO filtro) {
        
        return fechamentoEncalheRepository.obterValorTotalAnaliticoEncalhe(filtro);
        
    }
    
    @Override
    @Transactional
    public Integer buscarTotalAnaliticoEncalhe(final FiltroFechamentoEncalheDTO filtro) {
        
        return fechamentoEncalheRepository.buscarTotalAnaliticoEncalhe(filtro);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Date buscarUtimoDiaDaSemanaRecolhimento() {
        
        final int codigoInicioSemana = distribuidorService.inicioSemanaRecolhimento().getCodigoDiaSemana();
        
        final Date dataInicioSemana = SemanaUtil.obterDataInicioSemana(codigoInicioSemana, new Date());
        
        final Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
        
        return dataFimSemana;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Boolean validarEncerramentoOperacaoEncalhe(final Date data) {
        
        return fechamentoEncalheRepository.validarEncerramentoOperacaoEncalhe(data);
    }
    
    @Override
    public List<GridFechamentoEncalheDTO> listaEncalheTotalParaGrid(
            final List<FechamentoFisicoLogicoDTO> listaEncalheSessao) {
        
        final List<GridFechamentoEncalheDTO> listaGrid = new ArrayList<GridFechamentoEncalheDTO>();
        for (final FechamentoFisicoLogicoDTO encalhe : listaEncalheSessao) {
            final GridFechamentoEncalheDTO gridFechamento = new GridFechamentoEncalheDTO();
            gridFechamento.setCodigo(Long.parseLong(encalhe.getCodigo()));
            gridFechamento.setFisico(encalhe.getFisico());
            listaGrid.add(gridFechamento);
        }
        return listaGrid;
    }
    
    @Override
    @Transactional
    public Integer buscarTotalCotasAusentesSemPostergado(final Date dataEncalhe, final boolean isSomenteCotasSemAcao,
            final boolean ignorarUnificacao) {
        
        final Integer diaRecolhimento = obterDiaRecolhimento(dataEncalhe);
        
        DiaSemana diaSemanaRecolhimento = DiaSemana.getByCodigoDiaSemana(diaRecolhimento);
        
        return fechamentoEncalheRepository.obterTotalCotasAusentesSemPostergado(dataEncalhe, diaSemanaRecolhimento,
                isSomenteCotasSemAcao, null, null, 0, 0, ignorarUnificacao);
    }
}
