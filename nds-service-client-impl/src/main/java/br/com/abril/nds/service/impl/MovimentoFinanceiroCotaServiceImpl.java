package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ProcessamentoFinanceiroCotaVO;
import br.com.abril.nds.dto.CotaFaturamentoDTO;
import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.MovimentosEstoqueEncalheDTO;
import br.com.abril.nds.dto.ProcessamentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.HistoricoMovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.HistoricoMovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.NegociacaoDividaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.strategy.importacao.input.HistoricoFinanceiroInput;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

@Service
public class MovimentoFinanceiroCotaServiceImpl implements MovimentoFinanceiroCotaService {
    
    @Autowired
    private CotaService cotaService;
    
    @Autowired
    private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
    
    @Autowired
    private HistoricoMovimentoFinanceiroCotaRepository historicoMovimentoFinanceiroCotaRepository;
    
    @Autowired
    private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;

    @Autowired
    private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
    
    @Autowired
    private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
    
    @Autowired
    private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
    
    @Autowired
    private ControleConferenciaEncalheCotaRepository controleConferenciaEncalheCotaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ProdutoEdicaoRepository produtoEdicaoRepository;
    
    @Autowired
    private FornecedorRepository fornecedorRepository;
    
    @Autowired
    private FormaCobrancaService formaCobrancaService;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private CalendarioService calendarioService;
    
    @Autowired
    private NegociacaoDividaRepository negociacaoDividaRepository;
    
    @Autowired
    private PoliticaCobrancaService politicaCobrancaService;
    
    /*
     * Gera um mapa de movimentos de estoque da cota por fornecedor.
     */
    private Map<Long, List<MovimentoEstoqueCota>> gerarMapaMovimentoEstoqueCotaPorFornecedor(
            final List<MovimentoEstoqueCota> movimentosEstoqueCota) {
        
        final Map<Long, List<MovimentoEstoqueCota>> mapaMovimentoEstoqueCotaPorFornecedor = new HashMap<Long, List<MovimentoEstoqueCota>>();
        
        if (movimentosEstoqueCota == null || movimentosEstoqueCota.isEmpty()) {
            
            return mapaMovimentoEstoqueCotaPorFornecedor;
        }
        
        for (final MovimentoEstoqueCota movimentoEstoqueCota : movimentosEstoqueCota) {
            
            Fornecedor fornecedor = null;
            
            if (movimentoEstoqueCota != null && movimentoEstoqueCota.getProdutoEdicao() != null
                    && movimentoEstoqueCota.getProdutoEdicao().getProduto() != null) {
                
                fornecedor = movimentoEstoqueCota.getProdutoEdicao().getProduto().getFornecedor();
            }
            
            if (fornecedor == null && movimentoEstoqueCota.getEstoqueProdutoCota() != null
                    && movimentoEstoqueCota.getEstoqueProdutoCota().getProdutoEdicao() != null
                    && movimentoEstoqueCota.getEstoqueProdutoCota().getProdutoEdicao().getProduto() != null) {
                
                fornecedor = movimentoEstoqueCota.getEstoqueProdutoCota().getProdutoEdicao().getProduto()
                        .getFornecedor();
            }
            
            if (fornecedor != null) {
                
                List<MovimentoEstoqueCota> movimentosEstoqueCotaFornecedor = mapaMovimentoEstoqueCotaPorFornecedor
                        .get(fornecedor.getId());
                
                if (movimentosEstoqueCotaFornecedor == null) {
                    
                    movimentosEstoqueCotaFornecedor = new ArrayList<MovimentoEstoqueCota>();
                }
                
                movimentosEstoqueCotaFornecedor.add(movimentoEstoqueCota);
                
                mapaMovimentoEstoqueCotaPorFornecedor.put(fornecedor.getId(), movimentosEstoqueCotaFornecedor);
            }
        }
        
        return mapaMovimentoEstoqueCotaPorFornecedor;
    }
    
    /**
     * Gera Movimentos Financeiro para a Cota
     * 
     * @param movimentoFinanceiroCotaDTO
     * @param movimentosEstoqueCota
     * @return MovimentoFinanceiroCota
     */
    @Override
    @Transactional
    public List<MovimentoFinanceiroCota> gerarMovimentosFinanceirosDebitoCredito(
            final MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO) {
        
        final Map<Long, List<MovimentoEstoqueCota>> mapaMovimentoEstoqueCotaPorFornecedor = this
                .gerarMapaMovimentoEstoqueCotaPorFornecedor(movimentoFinanceiroCotaDTO.getMovimentos());
        
        final List<MovimentoFinanceiroCota> movimentosFinanceirosCota = new ArrayList<MovimentoFinanceiroCota>();
        
        MovimentoFinanceiroCota movimentoFinanceiroCota;
        
        if (mapaMovimentoEstoqueCotaPorFornecedor.isEmpty()) {
            
            movimentoFinanceiroCota = gerarMovimentoFinanceiroCota(movimentoFinanceiroCotaDTO, null);
            
            movimentosFinanceirosCota.add(movimentoFinanceiroCota);
            
        } else {
            
            for (final Map.Entry<Long, List<MovimentoEstoqueCota>> entry : mapaMovimentoEstoqueCotaPorFornecedor
                    .entrySet()) {
                
                movimentoFinanceiroCota = gerarMovimentoFinanceiroCota(movimentoFinanceiroCotaDTO, entry.getValue());
                
                movimentosFinanceirosCota.add(movimentoFinanceiroCota);
                
            }
        }
        
        return movimentosFinanceirosCota;
    }
    
    private List<MovimentoFinanceiroCota> gerarMovimentosFinanceirosDebitoCredito(
            final MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO, final List<MovimentosEstoqueEncalheDTO> movimentosEstoqueCota ) {
        
        final Map<Long, List<MovimentosEstoqueEncalheDTO>> mapaMovimentoEstoqueCotaPorFornecedor = this
                .agrupaMovimentosEstoqueEncalheEncPorFornecedor(movimentosEstoqueCota);
        
        final List<MovimentoFinanceiroCota> movimentosFinanceirosCota = new ArrayList<MovimentoFinanceiroCota>();
        
        MovimentoFinanceiroCota movimentoFinanceiroCota;
        
        if (mapaMovimentoEstoqueCotaPorFornecedor.isEmpty()) {
            
            movimentoFinanceiroCota = gerarMovimentoFinanceiroCota(movimentoFinanceiroCotaDTO, null);
            
            movimentosFinanceirosCota.add(movimentoFinanceiroCota);
            
        } else {
            
            for (final Map.Entry<Long, List<MovimentosEstoqueEncalheDTO>> entry : mapaMovimentoEstoqueCotaPorFornecedor.entrySet()) {
                
                movimentoFinanceiroCota = gerarMovimentoFinanceiroCotaDTO(movimentoFinanceiroCotaDTO, entry.getValue());
                
                movimentosFinanceirosCota.add(movimentoFinanceiroCota);
                
            }
        }
        
        return movimentosFinanceirosCota;
    }
    
    /**
     * Gera Movimento Financeiro para a Cota
     * 
     * @param movimentoFinanceiroCotaDTO
     * @param movimentosEstoqueCota
     * @return MovimentoFinanceiroCota
     */
    @Override
    @Transactional
    public MovimentoFinanceiroCota gerarMovimentoFinanceiroCota(
            final MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO,
            final List<MovimentoEstoqueCota> movimentosEstoqueCota) {
        
        this.validarFornecedor(movimentoFinanceiroCotaDTO);
        
        MovimentoFinanceiroCota movimentoFinanceiroCota = null;
        MovimentoFinanceiroCota movimentoFinanceiroCotaMerged = null;
        
        if (movimentoFinanceiroCotaDTO.getIdMovimentoFinanceiroCota() != null) {
            
            movimentoFinanceiroCota = movimentoFinanceiroCotaRepository.buscarPorId(movimentoFinanceiroCotaDTO.getIdMovimentoFinanceiroCota());
            
        } else {
            
            movimentoFinanceiroCota = new MovimentoFinanceiroCota();
        }
        
        final TipoMovimentoFinanceiro tipoMovimentoFinanceiro = movimentoFinanceiroCotaDTO.getTipoMovimentoFinanceiro();
        
        if (tipoMovimentoFinanceiro != null) {
            
            if (tipoMovimentoFinanceiro.isAprovacaoAutomatica()) {
                
                movimentoFinanceiroCota.setAprovadoAutomaticamente(Boolean.TRUE);
                movimentoFinanceiroCota.setAprovador(movimentoFinanceiroCotaDTO.getUsuario());
                movimentoFinanceiroCota.setDataAprovacao(movimentoFinanceiroCotaDTO.getDataAprovacao());
                movimentoFinanceiroCota.setStatus(StatusAprovacao.APROVADO);
            } else {
                
                movimentoFinanceiroCota.setStatus(StatusAprovacao.PENDENTE);
            }
            
            movimentoFinanceiroCota.setCota(movimentoFinanceiroCotaDTO.getCota());
            movimentoFinanceiroCota.setTipoMovimento(tipoMovimentoFinanceiro);
            movimentoFinanceiroCota.setData(movimentoFinanceiroCotaDTO.getDataVencimento());
            movimentoFinanceiroCota.setDataCriacao(movimentoFinanceiroCotaDTO.getDataCriacao());
            movimentoFinanceiroCota.setUsuario(movimentoFinanceiroCotaDTO.getUsuario());
            movimentoFinanceiroCota.setValor(movimentoFinanceiroCotaDTO.getValor());
            movimentoFinanceiroCota.setMotivo(movimentoFinanceiroCotaDTO.getMotivo());
            movimentoFinanceiroCota.setLancamentoManual(movimentoFinanceiroCotaDTO.isLancamentoManual());
            movimentoFinanceiroCota.setBaixaCobranca(movimentoFinanceiroCotaDTO.getBaixaCobranca());
            movimentoFinanceiroCota.setObservacao(movimentoFinanceiroCotaDTO.getObservacao());
            movimentoFinanceiroCota.setMovimentos(movimentosEstoqueCota);
            movimentoFinanceiroCota.setFornecedor(movimentoFinanceiroCotaDTO.getFornecedor());
            
            movimentoFinanceiroCotaMerged = movimentoFinanceiroCotaRepository.merge(movimentoFinanceiroCota);
            
            gerarHistoricoMovimentoFinanceiroCota(movimentoFinanceiroCotaMerged, movimentoFinanceiroCotaDTO.getTipoEdicao());
            
            if (movimentosEstoqueCota != null) {
                
                for (final MovimentoEstoqueCota est : movimentosEstoqueCota) {
                    
                    est.setMovimentoFinanceiroCota(movimentoFinanceiroCotaMerged);
                    
                    movimentoEstoqueCotaRepository.merge(est);
                }
            }
        }
        
        return movimentoFinanceiroCotaMerged;
    }
    
   
 
    private MovimentoFinanceiroCota gerarMovimentoFinanceiroCotaDTO(
            final MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO,
            final List<MovimentosEstoqueEncalheDTO> movimentosEstoqueCota) {
        
        this.validarFornecedor(movimentoFinanceiroCotaDTO);
        
        MovimentoFinanceiroCota movimentoFinanceiroCota = null;
        MovimentoFinanceiroCota movimentoFinanceiroCotaMerged = null;
        
        if (movimentoFinanceiroCotaDTO.getIdMovimentoFinanceiroCota() != null) {
            
            movimentoFinanceiroCota = movimentoFinanceiroCotaRepository.buscarPorId(movimentoFinanceiroCotaDTO.getIdMovimentoFinanceiroCota());
            
        } else {
            
            movimentoFinanceiroCota = new MovimentoFinanceiroCota();
        }
        
        final TipoMovimentoFinanceiro tipoMovimentoFinanceiro = movimentoFinanceiroCotaDTO.getTipoMovimentoFinanceiro();
        
        if (tipoMovimentoFinanceiro != null) {
            
            if (tipoMovimentoFinanceiro.isAprovacaoAutomatica()) {
                
                movimentoFinanceiroCota.setAprovadoAutomaticamente(Boolean.TRUE);
                movimentoFinanceiroCota.setAprovador(movimentoFinanceiroCotaDTO.getUsuario());
                movimentoFinanceiroCota.setDataAprovacao(movimentoFinanceiroCotaDTO.getDataAprovacao());
                movimentoFinanceiroCota.setStatus(StatusAprovacao.APROVADO);
            } else {
                
                movimentoFinanceiroCota.setStatus(StatusAprovacao.PENDENTE);
            }
            
            movimentoFinanceiroCota.setCota(movimentoFinanceiroCotaDTO.getCota());
            movimentoFinanceiroCota.setTipoMovimento(tipoMovimentoFinanceiro);
            movimentoFinanceiroCota.setData(movimentoFinanceiroCotaDTO.getDataVencimento());
            movimentoFinanceiroCota.setDataCriacao(movimentoFinanceiroCotaDTO.getDataCriacao());
            movimentoFinanceiroCota.setUsuario(movimentoFinanceiroCotaDTO.getUsuario());
            movimentoFinanceiroCota.setValor(movimentoFinanceiroCotaDTO.getValor());
            movimentoFinanceiroCota.setLancamentoManual(movimentoFinanceiroCotaDTO.isLancamentoManual());
            movimentoFinanceiroCota.setBaixaCobranca(movimentoFinanceiroCotaDTO.getBaixaCobranca());
            movimentoFinanceiroCota.setObservacao(movimentoFinanceiroCotaDTO.getObservacao());
            movimentoFinanceiroCota.setFornecedor(movimentoFinanceiroCotaDTO.getFornecedor());
            
            movimentoFinanceiroCotaMerged = movimentoFinanceiroCotaRepository.merge(movimentoFinanceiroCota);
            
            gerarHistoricoMovimentoFinanceiroCota(movimentoFinanceiroCotaMerged, movimentoFinanceiroCotaDTO.getTipoEdicao());
            
            if (movimentosEstoqueCota != null) {
                
                for (final MovimentosEstoqueEncalheDTO est : movimentosEstoqueCota) {
                    
                    movimentoEstoqueCotaRepository.updateById(est.getIdMovimentoEstoqueCota(), movimentoFinanceiroCotaMerged);
                }
            }
        }
        
        return movimentoFinanceiroCotaMerged;
    }
    
    private void validarFornecedor(final MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO) {
        
        Fornecedor fornecedor = movimentoFinanceiroCotaDTO.getFornecedor();
        
        final Cota cota = cotaService.obterPorId(movimentoFinanceiroCotaDTO.getCota().getId());
        
        if (fornecedor == null) {
            
            if (cota.getParametroCobranca() != null && cota.getParametroCobranca().getFornecedorPadrao() != null) {
                
                fornecedor = cota.getParametroCobranca().getFornecedorPadrao();
                
            } else {
                
                fornecedor = formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor().getPoliticaCobranca().getFornecedorPadrao();
            }
        }
        
        if (fornecedor == null) {
            
            throw new ValidacaoException(
                    TipoMensagem.WARNING,
                    "A [Cota "
                            + cota.getNumeroCota()
                            + "] necessita de um fornecedor padrão em parâmetros financeiros para a geração de movimentos financeiros de débito ou crédito !");
        }
        
        movimentoFinanceiroCotaDTO.setFornecedor(fornecedor);
    }
    
    /**
     * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#obterMovimentosFinanceiroCota()
     */
    @Override
    @Transactional
    public List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
            final FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {
        
    	List<GrupoMovimentoFinaceiro> 
    		gruposMovimentoFinanceiro = this.getGrupoMovimentosFinanceirosDebitosCreditos();
    	
    	gruposMovimentoFinanceiro.add(GrupoMovimentoFinaceiro.DEBITO_COTA_TAXA_DE_ENTREGA_ENTREGADOR);
    	gruposMovimentoFinanceiro.add(GrupoMovimentoFinaceiro.DEBITO_COTA_TAXA_DE_ENTREGA_TRANSPORTADOR);
    	// incluso para nova opcao no dropdown na tela de debito credito cota -- tipo de lancamento
    	gruposMovimentoFinanceiro.add(GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO);
    	
        filtroDebitoCreditoDTO.setGrupoMovimentosFinanceirosDebitosCreditos(gruposMovimentoFinanceiro); 
        
        this.aplicarParametrosDebitoTaxaDeEntrega(filtroDebitoCreditoDTO);
        
        return movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
    }

	private void aplicarParametrosDebitoTaxaDeEntrega(
			final FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {
		
		if(filtroDebitoCreditoDTO.getIdTipoMovimento()!= null){
        	
        	final TipoMovimentoFinanceiro tipoMovimento =  
        			tipoMovimentoFinanceiroRepository.buscarPorId(filtroDebitoCreditoDTO.getIdTipoMovimento());
        	
        	if(tipoMovimento!= null
        			&& GrupoMovimentoFinaceiro.DEBITO.equals(tipoMovimento.getGrupoMovimentoFinaceiro())){
        		
        		final List<Long> idsMovimentosDebitoTaxaEntrega = 
        				tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiro(
        						Arrays.asList(GrupoMovimentoFinaceiro.DEBITO_COTA_TAXA_DE_ENTREGA_ENTREGADOR,
        									  GrupoMovimentoFinaceiro.DEBITO_COTA_TAXA_DE_ENTREGA_TRANSPORTADOR));
        		
        		filtroDebitoCreditoDTO.setIdsTipoMovimentoTaxaEntrega(idsMovimentosDebitoTaxaEntrega);
        	}
        	
        }
	}
    
    @Override
    @Transactional
    public List<GrupoMovimentoFinaceiro> getGrupoMovimentosFinanceirosDebitosCreditos() {
        final List<GrupoMovimentoFinaceiro> gruposMovimentosFinanceiros = new ArrayList<GrupoMovimentoFinaceiro>();
        
        gruposMovimentosFinanceiros.add(GrupoMovimentoFinaceiro.CREDITO);
        gruposMovimentosFinanceiros.add(GrupoMovimentoFinaceiro.DEBITO);
        gruposMovimentosFinanceiros.add(GrupoMovimentoFinaceiro.DEBITO_SOBRE_FATURAMENTO);
        gruposMovimentosFinanceiros.add(GrupoMovimentoFinaceiro.CREDITO_SOBRE_FATURAMENTO);
        gruposMovimentosFinanceiros.add(GrupoMovimentoFinaceiro.COMPRA_NUMEROS_ATRAZADOS);
        gruposMovimentosFinanceiros.add(GrupoMovimentoFinaceiro.TAXA_EXTRA);
       
            
       
        return gruposMovimentosFinanceiros;
    }
    
    
    
  
    
    /**
     * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#obterContagemMovimentosFinanceiroCota(br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO)
     */
    @Override
    @Transactional
    public Integer obterContagemMovimentosFinanceiroCota(final FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {
        
        return movimentoFinanceiroCotaRepository.obterContagemMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
    }
    
    /**
     * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#removerMovimentoFinanceiroCota(java.lang.Long)
     */
    @Override
    @Transactional
    public void removerMovimentoFinanceiroCota(final Long idMovimento) {
        
    	final MovimentoFinanceiroCota m = movimentoFinanceiroCotaRepository.buscarPorId(idMovimento);
    	
        this.movimentoFinanceiroCotaRepository.remover(m);
    }
    
    /**
     * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#obterMovimentoFinanceiroCotaPorId(java.lang.Long)
     */
    @Override
    @Transactional
    public MovimentoFinanceiroCota obterMovimentoFinanceiroCotaPorId(final Long idMovimento) {
        
        final MovimentoFinanceiroCota m = movimentoFinanceiroCotaRepository.buscarPorId(idMovimento);
        
        Hibernate.initialize(m.getConsolidadoFinanceiroCota());
        
        return m;
    }
    
    /**
     * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#obterSomatorioValorMovimentosFinanceiroCota(br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO)
     */
    @Override
    @Transactional
    public BigDecimal obterSomatorioValorMovimentosFinanceiroCota(final FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {
        
        return movimentoFinanceiroCotaRepository.obterSomatorioValorMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
    }
    
    private void gerarHistoricoMovimentoFinanceiroCota(final MovimentoFinanceiroCota movimentoFinanceiroCota,
            final TipoEdicao tipoEdicao) {
        
        final HistoricoMovimentoFinanceiroCota historicoMovimentoFinanceiroCota = new HistoricoMovimentoFinanceiroCota();
        
        historicoMovimentoFinanceiroCota.setCota(movimentoFinanceiroCota.getCota());
        historicoMovimentoFinanceiroCota.setResponsavel(movimentoFinanceiroCota.getUsuario());
        historicoMovimentoFinanceiroCota.setTipoEdicao(tipoEdicao);
        historicoMovimentoFinanceiroCota.setTipoMovimento((TipoMovimentoFinanceiro) movimentoFinanceiroCota
                .getTipoMovimento());
        historicoMovimentoFinanceiroCota.setValor(movimentoFinanceiroCota.getValor());
        historicoMovimentoFinanceiroCota.setMovimentoFinanceiroCota(movimentoFinanceiroCota);
        historicoMovimentoFinanceiroCota.setDataEdicao(new Date());
        historicoMovimentoFinanceiroCota.setData(movimentoFinanceiroCota.getData());
        
        historicoMovimentoFinanceiroCotaRepository.adicionar(historicoMovimentoFinanceiroCota);
    }
    
    /**
     * Obtém valores dos faturamentos bruto ou liquido das cotas no período
     * 
     * @param cotas
     * @param baseCalculo
     * @param dataInicial
     * @param dataFinal
     * @return Map<Long,BigDecimal>: Faturamentos das cotas
     */
    @Override
    @Transactional(readOnly = true)
    public Map<Long, BigDecimal> obterFaturamentoCotasPeriodo(final List<Cota> cotas, final BaseCalculo baseCalculo,
            final Date dataInicial, final Date dataFinal) {
        
        Map<Long, BigDecimal> res = null;
        
        final List<CotaFaturamentoDTO> cotasFaturamento = movimentoFinanceiroCotaRepository
                .obterFaturamentoCotasPorPeriodo(cotas, dataInicial, dataFinal);
        
        if (cotasFaturamento != null && !cotasFaturamento.isEmpty()) {
            res = new HashMap<Long, BigDecimal>();
            for (final CotaFaturamentoDTO item : cotasFaturamento) {
                if (baseCalculo == BaseCalculo.FATURAMENTO_BRUTO) {
                    res.put(item.getIdCota(), item.getFaturamentoBruto());
                } else {
                    res.put(item.getIdCota(), item.getFaturamentoLiquido());
                }
            }
            
        }
        
        return res;
    }
    
    @Transactional(readOnly = true)
    public BigDecimal obterFaturamentoDaCotaNoPeriodo(final Long idCota,final BaseCalculo baseCalculo,
            final Date dataInicial, final Date dataFinal){
    	
    	Cota cota = new Cota();
    	cota.setId(idCota);
    	
    	Map<Long, BigDecimal> res = this.obterFaturamentoCotasPeriodo(Arrays.asList(cota),baseCalculo,dataInicial,dataFinal);
    	
    	return res.containsKey(idCota)? res.get(idCota) : BigDecimal.ZERO;  
    }
    
    
    @Override
    @Transactional
    public void processarRegistrohistoricoFinanceiro(final HistoricoFinanceiroInput valorInput, final Date dataOperacao) {
        
        final Cota cota = validarHistoricoFinanceiroInput(valorInput);
        
        final MovimentoFinanceiroCota movimento = new MovimentoFinanceiroCota();
        movimento.setCota(cota);
        movimento.setData(valorInput.getData());
        movimento.setDataAprovacao(new Date());
        movimento.setDataCriacao(new Date());
        movimento.setStatus(StatusAprovacao.APROVADO);
        movimento.setAprovadoAutomaticamente(true);
        movimento.setMotivo("VIRADA NDS");
        movimento.setAprovador(usuarioRepository.getUsuarioImportacao());
        movimento.setUsuario(usuarioRepository.getUsuarioImportacao());
        movimento.setFornecedor(cota.getParametroCobranca().getFornecedorPadrao());
        
        ConsolidadoFinanceiroCota cfc = consolidadoFinanceiroRepository.buscarPorCotaEData(cota.getId(), valorInput
                .getData());
        
        if (cfc == null) {
            cfc = new ConsolidadoFinanceiroCota();
            cfc.setCota(cota);
            cfc.setDataConsolidado(valorInput.getData());
        }
        
        if (!valorInput.getValorPendente().equals(BigDecimal.ZERO)) {
            
            movimento.setValor(valorInput.getValorPendente());
            movimento.setTipoMovimento(tipoMovimentoFinanceiroRepository.buscarPorDescricao("Pendente"));
            
            cfc.setPendente(valorInput.getValorPendente());
            
        }
        
        if (!valorInput.getValorPostergado().equals(BigDecimal.ZERO)) {
            
            movimento.setValor(valorInput.getValorPostergado());
            movimento.setTipoMovimento(tipoMovimentoFinanceiroRepository.buscarPorDescricao("Postergado"));
            
            cfc.setValorPostergado(valorInput.getValorPostergado());
            
        }
        
        if (!valorInput.getValorFuturo().equals(BigDecimal.ZERO)) {
            
            movimento.setValor(valorInput.getValorFuturo());
            movimento.setTipoMovimento(tipoMovimentoFinanceiroRepository.buscarPorDescricao((valorInput
                    .getValorFuturo().compareTo(BigDecimal.ZERO) > 0 ? "Crédito" : "Débito")));
            
            cfc.setDebitoCredito(valorInput.getValorFuturo());
            
        }
        
        if (cfc.getId() == null) {
            cfc.setTotal(movimento.getValor());
            cfc.getMovimentos().add(movimento);
            consolidadoFinanceiroRepository.adicionar(cfc);
        } else {
            cfc.getTotal().add(movimento.getValor());
            cfc.getMovimentos().add(movimento);
            consolidadoFinanceiroRepository.alterar(cfc);
        }
    }
    
    private Cota validarHistoricoFinanceiroInput(final HistoricoFinanceiroInput valorInput) throws ImportacaoException {
        if (valorInput.getNumeroCota() == null) {
            throw new ImportacaoException("Cota não Informada.");
        }
        
        if (valorInput.getValorFuturo() == null || valorInput.getValorPendente() == null
                || valorInput.getValorPostergado() == null) {
            throw new ImportacaoException("Valor (futuro, pendente ou postergado) nulo.");
        }
        
        if (!(valorInput.getValorFuturo().equals(BigDecimal.ZERO)
                ^ valorInput.getValorPendente().equals(BigDecimal.ZERO) ^ valorInput.getValorPostergado().equals(
                        BigDecimal.ZERO))) {
            throw new ImportacaoException("Mais de um valor (futuro, pendente ou postergado) com valor.");
        }
        
        if (valorInput.getValorFuturo().equals(BigDecimal.ZERO)
                && valorInput.getValorPendente().equals(BigDecimal.ZERO)
                && valorInput.getValorPostergado().equals(BigDecimal.ZERO)) {
            throw new ImportacaoException("Todos os Valores (futuro, pendente e postergado) Zerados.");
        }
        
        final Cota cota = cotaService.obterCotaPDVPorNumeroDaCota(valorInput.getNumeroCota());
        if (cota == null) {
            throw new ImportacaoException("Cota " + valorInput.getNumeroCota() + " inexistente.");
        }
        return cota;
    }
    
    @Transactional(readOnly = true)
    @Override
    public boolean existeOutrosMovimentosFinanceiroCota(final FiltroDebitoCreditoDTO filtroDebitoCredito,
            final Long idMovimentoFinanceiroAtual) {
        final List<Long> ids = movimentoFinanceiroCotaRepository.obterIdsMovimentosFinanceiroCota(filtroDebitoCredito);
        // Além do registro atual, existem outros, logo deve ser true
        if (ids.size() > 1) {
            return true;
        }
        
        // Se existe apenas um objeto e o id não é o mesmo
        if (ids.size() == 1 && !ids.get(0).equals(idMovimentoFinanceiroAtual)) {
            return true;
        }
        
        return false;
    }
    
    @Transactional
    @Override
    public void removerPostergadosDia(final Long idCota, final List<TipoMovimentoFinanceiro> tiposMovimentoPostergado,
            final Date dataOperacao) {
        
        final List<MovimentoFinanceiroCota> movs = movimentoFinanceiroCotaRepository
                .obterMovimentosFinanceirosCotaPorTipoMovimento(idCota, null, tiposMovimentoPostergado, dataOperacao);
        
        for (final MovimentoFinanceiroCota mfc : movs) {
            
            if (mfc.getMovimentos() != null) {
                
                for (final MovimentoEstoqueCota mec : mfc.getMovimentos()) {
                    
                    mec.setMovimentoFinanceiroCota(null);
                    movimentoEstoqueCotaRepository.merge(mec);
                }
            }
            
            movimentoFinanceiroCotaRepository.remover(mfc);
        }
    }
    
    /**
     * Obtem Quantidade de Informações para o processamento financeiro (Geração
     * de MovimentoFinanceiroCota, Divida e Cobrança) das Cotas
     * 
     * @param numeroCota
     * @param data
     * @return int
     */
    @Transactional
    @Override
    public int obterQuantidadeProcessamentoFinanceiroCota(final Integer numeroCota, final Date data) {
        
        return movimentoFinanceiroCotaRepository.obterQuantidadeProcessamentoFinanceiroCota(numeroCota).intValue();
    }
    
    /**
     * Obtem Informações para o processamento financeiro (Geração de
     * MovimentoFinanceiroCota, Divida e Cobrança) das Cotas
     * 
     * @param numeroCota
     * @param data
     * @param sortorder
     * @param sortname
     * @param initialResult
     * @param maxResults
     * @return List<ProcessamentoFinanceiroCotaVO>
     */
    @Transactional
    @Override
    public List<ProcessamentoFinanceiroCotaVO> obterProcessamentoFinanceiroCota(final Integer numeroCota,
            final Date data, final String sortorder, final String sortname, final int initialResult,
            final int maxResults) {
        
        final List<ProcessamentoFinanceiroCotaVO> processamentoFinanceiroVO = new ArrayList<ProcessamentoFinanceiroCotaVO>();
        
        final List<ProcessamentoFinanceiroCotaDTO> informacoesProcessamentoFinanceiroCota = movimentoFinanceiroCotaRepository
                .obterProcessamentoFinanceiroCota(numeroCota, data, sortorder, sortname, initialResult, maxResults);
        
        for (final ProcessamentoFinanceiroCotaDTO item : informacoesProcessamentoFinanceiroCota) {
            
        	if(item.getValorConsignado().intValue() == 0 && item.getValorAVista().intValue() == 0
        			&& item.getDebitos().intValue() == 0 && item.getCreditos().intValue() == 0 && item.getSaldo().intValue() == 0) {
        		continue;
        	}
        		
        	processamentoFinanceiroVO.add(new ProcessamentoFinanceiroCotaVO(item.getNumeroCota().toString(), 
            		                                                        item.getNomeCota(), 
            		                                                        CurrencyUtil.formatarValorQuatroCasas(item.getValorConsignado()), 
            		                                                        CurrencyUtil.formatarValorQuatroCasas(item.getValorAVista()), 
            		                                                        CurrencyUtil.formatarValorQuatroCasas(item.getDebitos()), 
            		                                                        CurrencyUtil.formatarValorQuatroCasas(item.getCreditos()), 
            		                                                        CurrencyUtil.formatarValor(item.getSaldo())));
        	
        }
        
        return processamentoFinanceiroVO;
    }
    
    /**
     * Remove Movimentos Financeiros da Cota referentes a Conferencia na Data
     * nao Consolidados
     * 
     * @param numeroCota
     * @param dataOperacao
     */
    @Transactional
    @Override
    public void removerMovimentosFinanceirosCotaConferenciaNaoConsolidados(final Integer numeroCota,
            final Date dataOperacao) {
        
        final List<MovimentoFinanceiroCota> mfcs = movimentoFinanceiroCotaRepository
                .obterMovimentosFinanceirosCotaConferenciaNaoConsolidados(numeroCota, dataOperacao);
        
        this.removerMovimentosFinanceirosCota(mfcs);
    }
    
	@Override
	@Transactional
	public void removerMovimentosFinanceirosCota(final Long idConsolidado) {
		final String motivo = "Financeiro Reprocessado "
				+ DateUtil.formatarDataPTBR((distribuidorService
						.obterDataOperacaoDistribuidor()));
		
		final List<String> grupoMovimentoFinaceiros = Arrays.asList(
				GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE.name(),
				GrupoMovimentoFinaceiro.ENVIO_ENCALHE.name(),
				GrupoMovimentoFinaceiro.TAXA_EXTRA.name());
		
		
		this.movimentoEstoqueCotaRepository.updateByIdConsolidadoAndGrupos(
				idConsolidado,grupoMovimentoFinaceiros , motivo,
				null, StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
		
		this.historicoMovimentoFinanceiroCotaRepository.removeByIdConsolidadoAndGrupos(idConsolidado, grupoMovimentoFinaceiros);
		
		/*TODO: GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO*/
		final List<GrupoMovimentoFinaceiro> grupoMovimentoFinaceirosNegocioacao = Arrays.asList(GrupoMovimentoFinaceiro.NEGOCIACAO_COMISSAO);
		
		this.negociacaoDividaRepository.updateValorDividaValorMovimento(idConsolidado,grupoMovimentoFinaceirosNegocioacao);
		
		this.negociacaoDividaRepository.removeNegociacaoMovimentoFinanceiroByIdConsolidadoAndGrupos(idConsolidado, grupoMovimentoFinaceirosNegocioacao);
		
		this.movimentoFinanceiroCotaRepository.removeByIdConsolidadoAndGrupos(idConsolidado, grupoMovimentoFinaceiros);
	}
	
	@Override
    @Transactional
    public void removerMovimentosFinanceirosCotaPorDataCota(final Date dataOperacao, final Long idCota) {
        final String motivo = "Financeiro Reprocessado "
                + DateUtil.formatarDataPTBR((distribuidorService
                        .obterDataOperacaoDistribuidor()));
        
        final List<String> grupoMovimentoFinaceiros = Arrays.asList(
                GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE.name(),
                GrupoMovimentoFinaceiro.ENVIO_ENCALHE.name());
        
        this.movimentoEstoqueCotaRepository.updateByCotaAndDataOpAndGrupos(
                idCota, dataOperacao, grupoMovimentoFinaceiros , motivo,
                StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
        
        this.historicoMovimentoFinanceiroCotaRepository.removeByCotaAndDataOpAndGrupos(
                idCota, dataOperacao, grupoMovimentoFinaceiros);
        
        this.movimentoFinanceiroCotaRepository.removeByCotaAndDataOpAndGrupos(
                idCota, dataOperacao, grupoMovimentoFinaceiros);
    }
    
    /**
     * Remove movimentos financeiros do consolidado ou postergado Referentes à
     * encalhe ou reparte da cota
     * 
     * @param mfcs
     */
    @Transactional
    @Override
    public void removerMovimentosFinanceirosCota(final List<MovimentoFinanceiroCota> mfcs) {
        
        final List<Long> idsMfc = new ArrayList<Long>();
        
        final List<Long> idsHmfc = new ArrayList<Long>();
        
        for (final MovimentoFinanceiroCota mfc : mfcs) {
            
            final GrupoMovimentoFinaceiro tmf = ((TipoMovimentoFinanceiro) mfc.getTipoMovimento())
                    .getGrupoMovimentoFinaceiro();
            
            if (tmf.equals(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE)
                    || tmf.equals(GrupoMovimentoFinaceiro.ENVIO_ENCALHE)
                    || tmf.equals(GrupoMovimentoFinaceiro.NEGOCIACAO_COMISSAO)) {
                
                final List<MovimentoEstoqueCota> mecs = mfc.getMovimentos();
                
             //   if (mecs != null && !mecs.isEmpty()) {
                    
                    for (final MovimentoEstoqueCota mec : mecs) {
                        
                        mec.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
                        
                        mec.setMovimentoFinanceiroCota(null);
                        
                        mec.setMotivo("Financeiro Reprocessado "
                				+ DateUtil.formatarDataPTBR((distribuidorService
                						.obterDataOperacaoDistribuidor())));
                        
                        movimentoEstoqueCotaRepository.alterar(mec);
                    }
            //    }
                
                final List<HistoricoMovimentoFinanceiroCota> hmfcs = mfc.getHistoricos();
                
            //    if (hmfcs != null && !hmfcs.isEmpty()) {
                    
                    for (final HistoricoMovimentoFinanceiroCota hmfc : hmfcs) {
                        
                        idsHmfc.add(hmfc.getId());
                    }
             //   }
                
                mfc.setHistoricos(null);
                
                movimentoFinanceiroCotaRepository.alterar(mfc);
                
                for (final Long idHmfc : idsHmfc) {
                    
                    historicoMovimentoFinanceiroCotaRepository.removerPorId(idHmfc);
                }
                
                if (tmf.equals(GrupoMovimentoFinaceiro.NEGOCIACAO_COMISSAO)
                        || tmf.equals(GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO)) {
                    
                    final Negociacao negociacao = negociacaoDividaRepository.obterNegociacaoPorMovFinanceiroId(mfc.getId());
                    
                    if (negociacao != null && (negociacao.getParcelas() == null || negociacao.getParcelas().isEmpty())) {
                        
                        negociacao.setValorDividaPagaComissao(negociacao.getValorDividaPagaComissao().add(mfc.getValor()));
                        
                        final List<MovimentoFinanceiroCota> mfcsNegociacao = negociacao.getMovimentosFinanceiroCota();
                        
                        mfcsNegociacao.remove(mfc);
                        
                        negociacao.setMovimentosFinanceiroCota(mfcsNegociacao);
                        
                        negociacaoDividaRepository.alterar(negociacao);
                    }
                }
                
                idsMfc.add(mfc.getId());
            }
        }
        
        for (final Long idMfc : idsMfc) {
            
            movimentoFinanceiroCotaRepository.removerPorId(idMfc);
        }
    }
    
    /**
     * Gera Movimento Financeiro para a cota (Chamada em Reparte ou Encalhe)
     * 
     * @param cota
     * @param fornecedor
     * @param movimentosEstoqueCota
     * @param movimentosEstorno
     * @param tipoMovimentoFinanceiro
     * @param valor
     * @param dataOperacao
     * @param usuario
     */
    private void gerarMovimentoFinanceiro(final Cota cota, final Fornecedor fornecedor,
            final List<MovimentosEstoqueEncalheDTO> movimentosEstoqueCota, final List<MovimentoEstoqueCota> movimentosEstorno,
            final TipoMovimentoFinanceiro tipoMovimentoFinanceiro, final BigDecimal valor, final Date dataOperacao,
            final Usuario usuario) {
        
        final MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
        
        movimentoFinanceiroCotaDTO.setCota(cota);
        movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
        movimentoFinanceiroCotaDTO.setUsuario(usuario);
        movimentoFinanceiroCotaDTO.setValor(valor);
        movimentoFinanceiroCotaDTO.setDataOperacao(dataOperacao);
        movimentoFinanceiroCotaDTO.setBaixaCobranca(null);
        movimentoFinanceiroCotaDTO.setDataVencimento(dataOperacao);
        movimentoFinanceiroCotaDTO.setDataAprovacao(dataOperacao);
        movimentoFinanceiroCotaDTO.setDataCriacao(dataOperacao);
        movimentoFinanceiroCotaDTO.setObservacao(null);
        movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
        movimentoFinanceiroCotaDTO.setAprovacaoAutomatica(true);
        movimentoFinanceiroCotaDTO.setLancamentoManual(false);
        movimentoFinanceiroCotaDTO.setFornecedor(fornecedor);
       
        
        if (movimentosEstoqueCota != null) {
            
            for (final MovimentosEstoqueEncalheDTO item : movimentosEstoqueCota) {
                
                movimentoEstoqueCotaRepository.updateById(item.getIdMovimentoEstoqueCota(), StatusEstoqueFinanceiro.FINANCEIRO_PROCESSADO);
            }
        }
        
        if (movimentosEstorno != null) {
            
            for (final MovimentoEstoqueCota item : movimentosEstorno) {
                
                item.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_PROCESSADO);
                
                movimentoEstoqueCotaRepository.merge(item);
            }
        }
        
        this.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO, movimentosEstoqueCota);
    }
    
    private void gerarMovimentoFinanceiroCota(final Cota cota, final Fornecedor fornecedor,
            final List<MovimentoEstoqueCota> movimentosEstoqueCota, final List<MovimentoEstoqueCota> movimentosEstorno,
            final TipoMovimentoFinanceiro tipoMovimentoFinanceiro, final BigDecimal valor, final Date dataOperacao,
            final Usuario usuario) {
        
        final MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
        
        movimentoFinanceiroCotaDTO.setCota(cota);
        movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
        movimentoFinanceiroCotaDTO.setUsuario(usuario);
        movimentoFinanceiroCotaDTO.setValor(valor);
        movimentoFinanceiroCotaDTO.setDataOperacao(dataOperacao);
        movimentoFinanceiroCotaDTO.setBaixaCobranca(null);
        movimentoFinanceiroCotaDTO.setDataVencimento(dataOperacao);
        movimentoFinanceiroCotaDTO.setDataAprovacao(dataOperacao);
        movimentoFinanceiroCotaDTO.setDataCriacao(dataOperacao);
        movimentoFinanceiroCotaDTO.setObservacao(null);
        movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
        movimentoFinanceiroCotaDTO.setAprovacaoAutomatica(true);
        movimentoFinanceiroCotaDTO.setLancamentoManual(false);
        movimentoFinanceiroCotaDTO.setFornecedor(fornecedor);
        movimentoFinanceiroCotaDTO.setMovimentos(movimentosEstoqueCota);
        
        if (movimentosEstoqueCota != null) {
            
            for (final MovimentoEstoqueCota item : movimentosEstoqueCota) {
                
            	 item.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_PROCESSADO);
                 
                 movimentoEstoqueCotaRepository.merge(item);
            }
        }
        
        if (movimentosEstorno != null) {
            
            for (final MovimentoEstoqueCota item : movimentosEstorno) {
                
                item.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_PROCESSADO);
                
                movimentoEstoqueCotaRepository.merge(item);
            }
        }
        
        this.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
    }
    
    
    /**
     * Distingue Movimentos de Estoque da Cota por Fornecedor; Separa a lista de
     * Movimentos de Estoque em outras listas; Cada lista separada possui
     * Movimentos de Estoque de um único Fornecedor.
     * 
     * @param movimentosEstoqueCota
     * @return Map<Long,List<MovimentoEstoqueCota>>
     */
    private Map<Long, List<MovimentosEstoqueEncalheDTO>> agrupaMovimentosEstoqueEncalheEncPorFornecedor(
            final List<MovimentosEstoqueEncalheDTO> movimentosEstoqueCota) {
        
        final Map<Long, List<MovimentosEstoqueEncalheDTO>> movEstAgrup = new HashMap<Long, List<MovimentosEstoqueEncalheDTO>>();
        
        List<MovimentosEstoqueEncalheDTO> mecs;
        
        if (movimentosEstoqueCota != null){
            for (final MovimentosEstoqueEncalheDTO mec : movimentosEstoqueCota) {
                
                final Long fornecedor = mec.getIdFornecedor();
                
                if (fornecedor == null) {
                    
                    throw new ValidacaoException(TipoMensagem.WARNING,
                            "Fornecedor não encontrado na geração de Movimento Financeiro para o Movimentos de Estoque ["
                                    + mec.getIdMovimentoEstoqueCota() + "]!");
                }
                
                mecs = movEstAgrup.get(fornecedor);
                
                mecs = mecs == null ? new ArrayList<MovimentosEstoqueEncalheDTO>() : mecs;
                
                mecs.add(mec);
                
                movEstAgrup.put(fornecedor, mecs);
            }
        }
        
        return movEstAgrup;
    }
    
    /**
     * Distingue Movimentos de Estoque da Cota por Fornecedor; Separa a lista de
     * Movimentos de Estoque em outras listas; Cada lista separada possui
     * Movimentos de Estoque de um único Fornecedor.
     * 
     * @param movimentosEstoqueCota
     * @return Map<Long,List<MovimentoEstoqueCota>>
     */
    private Map<Long, List<MovimentoEstoqueCota>> agrupaMovimentosEstoqueCotaPorFornecedor(
            final List<MovimentoEstoqueCota> movimentosEstoqueCota) {
        
        final Map<Long, List<MovimentoEstoqueCota>> movEstAgrup = new HashMap<Long, List<MovimentoEstoqueCota>>();
        
        List<MovimentoEstoqueCota> mecs;
        
        for (final MovimentoEstoqueCota mec : movimentosEstoqueCota) {
            
            final Fornecedor fornecedor = mec.getProdutoEdicao().getProduto().getFornecedor();
            
            if (fornecedor == null) {
                
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Fornecedor não encontrado na geração de Movimento Financeiro para o Movimentos de Estoque ["
                                + mec.getId() + "] [Cota " + mec.getCota().getNumeroCota() + "] !");
            }
            
            mecs = movEstAgrup.get(fornecedor.getId());
            
            mecs = mecs == null ? new ArrayList<MovimentoEstoqueCota>() : mecs;
            
            mecs.add(mec);
            
            movEstAgrup.put(fornecedor.getId(), mecs);
        }
        
        return movEstAgrup;
    }
    
    /**
     * Obtém movimentos de envio de reparte à cota a vista que ainda não geraram
     * financeiro Agrupados por fornecedor
     * 
     * @param idCota
     * @param dataOperacao
     * @return Map<Long,List<MovimentoEstoqueCota>>
     */
    private Map<Long, List<MovimentoEstoqueCota>> obterMovimentosEstoqueReparteAVista(final Long idCota,
            final Date dataOperacao) {
        
        final List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoEnvioReparte = movimentoEstoqueCotaRepository
                .obterMovimentosAVistaPendentesGerarFinanceiro(idCota, dataOperacao);
        
        final Map<Long, List<MovimentoEstoqueCota>> movimentosReparteAgrupadosPorFornecedor = this
                .agrupaMovimentosEstoqueCotaPorFornecedor(movimentosEstoqueCotaOperacaoEnvioReparte);
        
        return movimentosReparteAgrupadosPorFornecedor;
    }
    
    /**
     * Obtém movimentos de envio de reparte à cota a vista ou com conferencia
     * prevista para o dia que ainda não geraram financeiro Agrupados por
     * fornecedor
     * 
     * @param idCota
     * @param dataOperacao
     * @return Map<Long,List<MovimentoEstoqueCota>>
     */
    private Map<Long, List<MovimentoEstoqueCota>> obterMovimentosConsignadosPrevistoDiaEAVistaCotaAVista(
            final Long idCota, final Date dataOperacao) {
        
        final List<MovimentoEstoqueCota> movimentosEstoqueCotaConsignadosCotaAVista = movimentoEstoqueCotaRepository
                .obterMovimentosConsignadosCotaAVistaPrevistoDia(idCota, dataOperacao);
        
        final List<MovimentoEstoqueCota> movimentosEstoqueCotaAVistaCotaAVista = movimentoEstoqueCotaRepository
                .obterMovimentosAVistaPendentesGerarFinanceiro(idCota, dataOperacao);
        
        final List<MovimentoEstoqueCota> movimentosEstoqueCotaConsignadosEAVistaCotaAVista = new ArrayList<MovimentoEstoqueCota>();
        
        movimentosEstoqueCotaConsignadosEAVistaCotaAVista.addAll(movimentosEstoqueCotaConsignadosCotaAVista);
        
        movimentosEstoqueCotaConsignadosEAVistaCotaAVista.addAll(movimentosEstoqueCotaAVistaCotaAVista);
        
        final Map<Long, List<MovimentoEstoqueCota>> movimentosReparteAgrupadosPorFornecedor = this
                .agrupaMovimentosEstoqueCotaPorFornecedor(movimentosEstoqueCotaConsignadosEAVistaCotaAVista);
        
        return movimentosReparteAgrupadosPorFornecedor;
    }
    
    /**
     * Obtém movimentos de envio de reparte à cota que ainda não geraram
     * financeiro com Chamada Encalhe Agrupados por fornecedor
     * 
     * @param idCota
     * @param datas
     * @return Map<Long,List<MovimentoEstoqueCota>>
     */
    private Map<Long, List<MovimentosEstoqueEncalheDTO>> obterMovimentosEstoqueReparteComChamadaEncalheOuProdutoContaFirme(
            final Long idCota, final List<Date> datas) {
        
    	
    	List<Long> idTiposMovimentoEstoque = tipoMovimentoEstoqueRepository.buscarIdTiposMovimentoEstoque(Arrays.asList(
        		GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR,
                GrupoMovimentoEstoque.COMPRA_ENCALHE,
                GrupoMovimentoEstoque.RECEBIMENTO_REPARTE,
                GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE,
                GrupoMovimentoEstoque.SOBRA_EM_COTA));
    	
        final List<MovimentosEstoqueEncalheDTO> movimentosEstoqueCotaOperacaoEnvioReparte = movimentoEstoqueCotaRepository
                .obterMovimentosPendentesGerarFinanceiroComChamadaEncalheOuProdutoContaFirme(idCota, datas, idTiposMovimentoEstoque);
  
        
        final Map<Long, List<MovimentosEstoqueEncalheDTO>> movimentosReparteAgrupadosPorFornecedor = this
                .agrupaMovimentosEstoqueEncalheEncPorFornecedor(movimentosEstoqueCotaOperacaoEnvioReparte);
        
        return movimentosReparteAgrupadosPorFornecedor;
    }
    
    /**
     * Obtém movimentos da conferência de encalhe Agrupados por fornecedor
     * 
     * @param idCota
     * @param idControleConferenciaEncalheCota
     * @return Map<Long,List<MovimentoEstoqueCota>>
     */
    private Map<Long, List<MovimentosEstoqueEncalheDTO>> obterMovimentosEstoqueEncalhe(final Long idCota,
            final Long idControleConferenciaEncalheCota) {
        
        final List<MovimentosEstoqueEncalheDTO> movimentosEstoqueCotaOperacaoConferenciaEncalhe = movimentoEstoqueCotaRepository
                .obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota);
        
        final Map<Long, List<MovimentosEstoqueEncalheDTO>> movimentosEncalheAgrupadosPorFornecedor = this
                .agrupaMovimentosEstoqueEncalheEncPorFornecedor(movimentosEstoqueCotaOperacaoConferenciaEncalhe);
        
        return movimentosEncalheAgrupadosPorFornecedor;
    }
    
    /**
     * Obtém movimentos estornados que não geram financeiro Agrupados por
     * fornecedor
     * 
     * @param idCota
     * @param dataOperacao TODO
     * @param datas
     * @return Map<Long,List<MovimentoEstoqueCota>>
     */
    private Map<Long, List<MovimentoEstoqueCota>> obterMovimentosEstoqueEstorno(final Long idCota, List<Date> datas) {
        
    	final List<Long> idTiposMovimentoEstorno = tipoMovimentoEstoqueRepository.buscarIdTiposMovimentoEstoque(
    			Arrays.asList(GrupoMovimentoEstoque.ESTORNO_COMPRA_ENCALHE, 
	    					  GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR, 
	    					  GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE,
	    					  GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA,
	    					  GrupoMovimentoEstoque.FALTA_EM_COTA));
    	
        final List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoEstorno = movimentoEstoqueCotaRepository
                .obterMovimentosEstornadosPorChamadaEncalhe(idCota, idTiposMovimentoEstorno, datas);
        
        final Map<Long, List<MovimentoEstoqueCota>> movimentosEstornoAgrupadosPorFornecedor = this
                .agrupaMovimentosEstoqueCotaPorFornecedor(movimentosEstoqueCotaOperacaoEstorno);
        
        return movimentosEstornoAgrupadosPorFornecedor;
    }
    
    /**
     * Retorna o somatório dos valores dos Movimentos de Estoque
     * 
     * @param movimentos
     * @return BigDecimal
     */
    private BigDecimal obterValorMovimentosEstoqueEncalhe(final List<MovimentosEstoqueEncalheDTO> movimentos) {
        
        BigDecimal total = BigDecimal.ZERO;
        
        if (movimentos == null) {
            
            return total;
        }
        
        for (final MovimentosEstoqueEncalheDTO m : movimentos) {
            
            final BigInteger qtd = m.getQtde();
            
            final BigDecimal valor =  m.getPrecoComDesconto() != null ? m.getPrecoComDesconto() : m.getPrecoVenda();
                    
                    total = total.add(valor.multiply(new BigDecimal(qtd)));
        }
        
        return total;
    }
    
    

    
    
    /**
     * Retorna o somatório dos valores dos Movimentos de Estoque
     * 
     * @param movimentos
     * @return BigDecimal
     */
    private BigDecimal obterValorMovimentosEstoqueCota(final List<MovimentoEstoqueCota> movimentos) {
        
        BigDecimal total = BigDecimal.ZERO;
        
        if (movimentos == null) {
            
            return total;
        }
        
        for (final MovimentoEstoqueCota m : movimentos) {
            
            final BigInteger qtd = m.getQtde();
            
            final BigDecimal valor = m.getValoresAplicados() != null ? m.getValoresAplicados().getPrecoComDesconto() != null ? m
                    .getValoresAplicados().getPrecoComDesconto()
                    : m.getProdutoEdicao().getPrecoVenda()
                    : m.getProdutoEdicao().getPrecoVenda();

                    
                    total = total.add(valor.multiply(new BigDecimal(qtd)));
        }
        
        return total;
    }
    
    /**
     * Gera Financeiro para Movimentos de Estoque da Cota referentes à Envio de
     * Reparte.
     * 
     * @param cota
     * @param fornecedor
     * @param movimentosEstoqueCota - movimentos
     * @param movimentosEstoqueCota - movimentos de estorno
     * @param dataOperacao
     * @param usuario
     */
    private void gerarMovimentoFinanceiroCotaReparte(final Cota cota, final Fornecedor fornecedor,
            final List<MovimentoEstoqueCota> movimentosEstoqueCota, final List<MovimentoEstoqueCota> movimentosEstorno,
            final Date dataOperacao, final Usuario usuario) {
        
        BigDecimal precoVendaItem = BigDecimal.ZERO;
        BigInteger quantidadeItem = BigInteger.ZERO;
        BigDecimal totalItem = BigDecimal.ZERO;
        BigDecimal totalGeral = BigDecimal.ZERO;
        BigDecimal totalEstorno = BigDecimal.ZERO;
        
        totalEstorno = this.obterValorMovimentosEstoqueCota(movimentosEstorno);
        
        if (movimentosEstoqueCota != null) {
            
            for (final MovimentoEstoqueCota item : movimentosEstoqueCota) {
                
                final ProdutoEdicao produtoEdicao = item.getProdutoEdicao();
                
                final ValoresAplicados va = item.getValoresAplicados();
                
                if (va != null && va.getPrecoComDesconto() != null) {
                    
                    precoVendaItem = va.getPrecoComDesconto();
                } else {
                    
                    precoVendaItem = (produtoEdicao != null && produtoEdicao.getPrecoVenda() != null) ? produtoEdicao
                            .getPrecoVenda() : BigDecimal.ZERO;
                }
                
                quantidadeItem = (item.getQtde() != null) ? item.getQtde() : BigInteger.ZERO;
                
                totalItem = precoVendaItem.multiply(new BigDecimal(quantidadeItem.longValue()));
                
                totalGeral = totalGeral.add(totalItem);
            }
        }
        
        final TipoMovimentoFinanceiro tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository
                .buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);
        
        totalGeral = totalGeral.subtract(totalEstorno != null ? totalEstorno : BigDecimal.ZERO);
        
        this.gerarMovimentoFinanceiroCota(cota, fornecedor, movimentosEstoqueCota, movimentosEstorno,
                tipoMovimentoFinanceiro, totalGeral, dataOperacao, usuario);
    }
   
    
    //TODO LUPE
    private void gerarMovimentoFinanceiroCotaReparte(final Cota cota, final Fornecedor fornecedor,
             final List<MovimentoEstoqueCota> movimentosEstorno,
            final Date dataOperacao, final Usuario usuario, final List<MovimentosEstoqueEncalheDTO> movimentosEstoqueCota) {
        
        BigDecimal precoVendaItem = BigDecimal.ZERO;
        BigInteger quantidadeItem = BigInteger.ZERO;
        BigDecimal totalItem = BigDecimal.ZERO;
        BigDecimal totalGeral = BigDecimal.ZERO;
        BigDecimal totalEstorno = BigDecimal.ZERO;
        
        totalEstorno = this.obterValorMovimentosEstoqueCota(movimentosEstorno);
        
        if (movimentosEstoqueCota != null) {
            
            for (final MovimentosEstoqueEncalheDTO item : movimentosEstoqueCota) {
            	
                if (item.getPrecoComDesconto() != null) {
                    
                    precoVendaItem = item.getPrecoComDesconto();
                } else {
                    
                    precoVendaItem = (BigDecimal) ObjectUtils.defaultIfNull(item.getPrecoVenda(), BigDecimal.ZERO);
                }
                
                quantidadeItem = (item.getQtde() != null) ? item.getQtde() : BigInteger.ZERO;
                
                totalItem = precoVendaItem.multiply(new BigDecimal(quantidadeItem.longValue()));
                
                totalGeral = totalGeral.add(totalItem);
            }
        }
        
        final TipoMovimentoFinanceiro tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository
                .buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);
        
        totalGeral = totalGeral.subtract(totalEstorno != null ? totalEstorno : BigDecimal.ZERO);
        
        this.gerarMovimentoFinanceiro(cota, fornecedor, movimentosEstoqueCota, movimentosEstorno,
                tipoMovimentoFinanceiro, totalGeral, dataOperacao, usuario);
    }
    
    /**
     * Gera Financeiro para Movimentos de Estoque da Cota à Vista referentes à
     * Envio de Reparte.
     * 
     * @param cota
     * @param fornecedor
     * @param movimentosEstoqueCotaOperacaoEnvioReparte
     * @param movimentosEstoqueCotaOperacaoEstorno
     * @param dataOperacao
     * @param usuario
     */
    @Transactional
    @Override
    public void gerarMovimentoFinanceiroCotaAVista(final Cota cota, final Fornecedor fornecedor,
            final List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoEnvioReparte,
            final List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoEstorno, final Date dataOperacao,
            final Usuario usuario) {
        
        // GERA MOVIMENTO FINANCEIRO DOS MOVIMENTOS DE ESTOQUE DE REPARTE PARA
        // COTA DO TIPO A_VISTA
        
        if (movimentosEstoqueCotaOperacaoEnvioReparte != null && !movimentosEstoqueCotaOperacaoEnvioReparte.isEmpty()) {
            
            this.gerarMovimentoFinanceiroCotaReparte(cota, fornecedor, movimentosEstoqueCotaOperacaoEnvioReparte,
                    movimentosEstoqueCotaOperacaoEstorno, dataOperacao, usuario);
        }
    }
    
    /**
     * Gera movimentos financeiros Cota x Fornecedor
     * 
     * @param cota
     * @param fornecedor
     * @param idControleConferenciaEncalheCota
     * @param dataOperacao
     * @param usuario
     * @param movimentosEstoqueCotaOperacaoEnvioReparte
     * @param movimentosEstoqueCotaOperacaoConferenciaEncalhe
     * @param movimentosEstoqueCotaOperacaoEstorno
     */
    private void gerarMovimentoFinanceiroCotaRecolhimentoPorFornecedor(final Cota cota, 
    		                                                           final Fornecedor fornecedor,
														               final Long idControleConferenciaEncalheCota, 
														               final Date dataOperacao, 
														               final Usuario usuario,
														               final List<MovimentosEstoqueEncalheDTO> movimentosEstoqueCotaOperacaoEnvioReparte,
														               final List<MovimentosEstoqueEncalheDTO> movimentosEstoqueCotaOperacaoConferenciaEncalhe,
														               final List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoEstorno) {
        
        final TipoCota tipoCota = cota != null ? cota.getTipoCota() : null;
        
        BigDecimal valorTotalEncalheOperacaoConferenciaEncalhe;
        BigDecimal valorTotalEncalheOperacaoEnvioReparte;
        
        TipoMovimentoFinanceiro tipoMovimentoFinanceiro;
        
        // GERA MOVIMENTO FINANCEIRO DOS MOVIMENTOS DE ESTOQUE DE
        // REPARTE(DEBITO) PARA COTA DO TIPO CONSIGNADO E A_VISTA
        
        this.gerarMovimentoFinanceiroCotaReparte(cota, 
        										 fornecedor, 
        										 movimentosEstoqueCotaOperacaoEstorno, 
        										 dataOperacao, 
        										 usuario,
        										 movimentosEstoqueCotaOperacaoEnvioReparte);
        
        if (TipoCota.CONSIGNADO.equals(tipoCota)) {
            
            // GERA MOVIMENTO FINANCEIRO DOS MOVIMENTOS DE ESTOQUE DE
            // ENCALHE(CREDITO) PARA COTA DO TIPO CONSIGNADO
            
            valorTotalEncalheOperacaoConferenciaEncalhe = this
                    .obterValorMovimentosEstoqueEncalhe(movimentosEstoqueCotaOperacaoConferenciaEncalhe);
            valorTotalEncalheOperacaoEnvioReparte = this
                    .obterValorMovimentosEstoqueEncalhe(movimentosEstoqueCotaOperacaoEnvioReparte);
            
            if (movimentosEstoqueCotaOperacaoConferenciaEncalhe != null) {
                
                tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
                
                this.gerarMovimentoFinanceiro(cota, 
                							  fornecedor, 
                							  movimentosEstoqueCotaOperacaoConferenciaEncalhe,
                							  movimentosEstoqueCotaOperacaoEstorno, 
                							  tipoMovimentoFinanceiro,
                							  valorTotalEncalheOperacaoConferenciaEncalhe, 
                							  dataOperacao, 
                							  usuario);
                
                BigDecimal percentualTaxaExtra = distribuidorService.obter().getPercentualTaxaExtra();
                if(percentualTaxaExtra != null) {
                	
                	gerarMovimentoFincaneiroCotaTaxaExtra(cota, fornecedor,
							dataOperacao, usuario,
							valorTotalEncalheOperacaoEnvioReparte.subtract(valorTotalEncalheOperacaoConferenciaEncalhe),
							percentualTaxaExtra);
                }
            } else {  // gerar taxa extra para cotas ausentes 
            	if ( valorTotalEncalheOperacaoEnvioReparte != null && valorTotalEncalheOperacaoEnvioReparte.floatValue() > 0 )
	            	{
	            	 BigDecimal percentualTaxaExtra = distribuidorService.obter().getPercentualTaxaExtra();
	                 if(percentualTaxaExtra != null) {
	                 	
	                 	gerarMovimentoFincaneiroCotaTaxaExtra(cota, fornecedor,
	 							dataOperacao, usuario,
	 							valorTotalEncalheOperacaoEnvioReparte.subtract(valorTotalEncalheOperacaoEnvioReparte),
	 							percentualTaxaExtra);
	                 }
	            	}
            }
            
        } else if (tipoCota.equals(TipoCota.A_VISTA)) {
            
            // GERA MOVIMENTO FINANCEIRO DOS MOVIMENTOS DE ESTOQUE DE
            // ENCALHE(CREDITO) PARA COTA DO TIPO A_VISTA
            
            valorTotalEncalheOperacaoConferenciaEncalhe = this.obterValorMovimentosEstoqueEncalhe(movimentosEstoqueCotaOperacaoConferenciaEncalhe);
            
            if ((valorTotalEncalheOperacaoConferenciaEncalhe == null) || (valorTotalEncalheOperacaoConferenciaEncalhe.floatValue() <= 0)) {
            	// gerar taxa extra para cotas ausentes 
            	 valorTotalEncalheOperacaoEnvioReparte = this
                         .obterValorMovimentosEstoqueEncalhe(movimentosEstoqueCotaOperacaoEnvioReparte);
            
            	if ( valorTotalEncalheOperacaoEnvioReparte != null && valorTotalEncalheOperacaoEnvioReparte.floatValue() > 0 )
	            	{
	            	 BigDecimal percentualTaxaExtra = distribuidorService.obter().getPercentualTaxaExtra();
	                 if(percentualTaxaExtra != null) {
	                 	
	                 	gerarMovimentoFincaneiroCotaTaxaExtra(cota, fornecedor,
	 							dataOperacao, usuario,
	 							valorTotalEncalheOperacaoEnvioReparte.subtract(valorTotalEncalheOperacaoEnvioReparte),
	 							percentualTaxaExtra);
	                 }
	            	}
                return;
            }
            
            tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
            
            this.gerarMovimentoFinanceiro(cota, 
            		                      fornecedor, 
            		                      movimentosEstoqueCotaOperacaoConferenciaEncalhe, 
            		                      null,
            		                      tipoMovimentoFinanceiro, 
            		                      valorTotalEncalheOperacaoConferenciaEncalhe, 
            		                      dataOperacao, 
            		                      usuario);
            
            BigDecimal percentualTaxaExtra = distribuidorService.obter().getPercentualTaxaExtra();
            if(percentualTaxaExtra != null) {
            	
            	gerarMovimentoFincaneiroCotaTaxaExtra(cota, fornecedor,
						dataOperacao, usuario,
						valorTotalEncalheOperacaoConferenciaEncalhe,
						percentualTaxaExtra);
            }
        }
    }

	private void gerarMovimentoFincaneiroCotaTaxaExtra(final Cota cota,
			final Fornecedor fornecedor, final Date dataOperacao,
			final Usuario usuario,
			BigDecimal valorTotalEncalheOperacaoConferenciaEncalhe,
			BigDecimal percentualTaxaExtra) {
		
		String motivo = dataOperacao +" - "+ distribuidorService.obter().getDescricaoTaxaExtra();
		BigDecimal valor = BigDecimal.ZERO ; // valorTotalEncalheOperacaoConferenciaEncalhe; 
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroTaxaExtra = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.TAXA_EXTRA);
		if(tipoMovimentoFinanceiroTaxaExtra == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, String.format("Tipo de Movimento '%s' não encontrado.", GrupoMovimentoFinaceiro.TAXA_EXTRA.getDescricao()));
		}
		
    	List<MovimentoFinanceiroCota> movimentosFinanceiros = movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCota(cota.getId(), dataOperacao, Arrays.asList(cota.getTipoCota()));
    	MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = null;
    	if(movimentosFinanceiros != null && !movimentosFinanceiros.isEmpty()) {
    		
    		for(MovimentoFinanceiroCota mfc : movimentosFinanceiros) {
    			
    			if(mfc.getTipoMovimento().equals(tipoMovimentoFinanceiroTaxaExtra) && mfc.getFornecedor().getId().equals(fornecedor.getId())) {
    				
    				movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
    				movimentoFinanceiroCotaDTO.setIdMovimentoFinanceiroCota(mfc.getId());
    	    		movimentoFinanceiroCotaDTO.setCota(mfc.getCota());
    	    		movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro((TipoMovimentoFinanceiro) mfc.getTipoMovimento());
    	    		movimentoFinanceiroCotaDTO.setUsuario(mfc.getUsuario());
    	    		movimentoFinanceiroCotaDTO.setMotivo(motivo);
    	    		movimentoFinanceiroCotaDTO.setDataOperacao(dataOperacao);
    	    		movimentoFinanceiroCotaDTO.setBaixaCobranca(mfc.getBaixaCobranca());
    	    		movimentoFinanceiroCotaDTO.setDataVencimento(dataOperacao);
    	    		movimentoFinanceiroCotaDTO.setDataAprovacao(dataOperacao);
    	    		movimentoFinanceiroCotaDTO.setDataCriacao(dataOperacao);
    	    		movimentoFinanceiroCotaDTO.setObservacao(mfc.getObservacao());
    	    		movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.ALTERACAO);
    	    		movimentoFinanceiroCotaDTO.setAprovacaoAutomatica(mfc.getTipoMovimento().isAprovacaoAutomatica());
    	    		movimentoFinanceiroCotaDTO.setLancamentoManual(false);
    	    		movimentoFinanceiroCotaDTO.setFornecedor(fornecedor);
    	    		movimentoFinanceiroCotaDTO.setMovimentos(null);

    	    		//break; // break or not break ? odemir
    			} else {
    				GrupoMovimentoFinaceiro grupoMovimentoFinanceiro = ((TipoMovimentoFinanceiro) mfc.getTipoMovimento()).getGrupoMovimentoFinaceiro();
    				
    			
    				if(mfc.getFornecedor().getId().equals(fornecedor.getId())) {
    					
    					if(grupoMovimentoFinanceiro.equals(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE)) {
    						valor = valor.add(mfc.getValor());
    					} else if(grupoMovimentoFinanceiro.equals(GrupoMovimentoFinaceiro.ENVIO_ENCALHE)) {
    						valor = valor.subtract(mfc.getValor());
    					}
    				}
    			}
    		} 
    		
    		if(movimentoFinanceiroCotaDTO == null) {
    			
    			movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
        		movimentoFinanceiroCotaDTO.setCota(cota);
        		movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiroTaxaExtra);
        		movimentoFinanceiroCotaDTO.setUsuario(usuario);
        		movimentoFinanceiroCotaDTO.setMotivo(motivo);
        		movimentoFinanceiroCotaDTO.setDataOperacao(dataOperacao);
        		movimentoFinanceiroCotaDTO.setBaixaCobranca(null);
        		movimentoFinanceiroCotaDTO.setDataVencimento(dataOperacao);
        		movimentoFinanceiroCotaDTO.setDataAprovacao(dataOperacao);
        		movimentoFinanceiroCotaDTO.setDataCriacao(dataOperacao);
        		movimentoFinanceiroCotaDTO.setObservacao(null);
        		movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
        		movimentoFinanceiroCotaDTO.setAprovacaoAutomatica(tipoMovimentoFinanceiroTaxaExtra.isAprovacaoAutomatica());
        		movimentoFinanceiroCotaDTO.setLancamentoManual(false);
        		movimentoFinanceiroCotaDTO.setFornecedor(fornecedor);
        		movimentoFinanceiroCotaDTO.setMovimentos(null);
    		}
    		
			valor = valor.multiply(percentualTaxaExtra).divide(BigDecimal.valueOf(100));
			movimentoFinanceiroCotaDTO.setValor(CurrencyUtil.truncateDecimal(valor, 2));

    	} else { // cota ausente
    	
    		movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
    		movimentoFinanceiroCotaDTO.setCota(cota);
    		movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiroTaxaExtra);
    		movimentoFinanceiroCotaDTO.setUsuario(usuario);
    		valor = valorTotalEncalheOperacaoConferenciaEncalhe.multiply(percentualTaxaExtra).divide(BigDecimal.valueOf(100));
    		movimentoFinanceiroCotaDTO.setValor(valor);
    		movimentoFinanceiroCotaDTO.setMotivo(motivo);
    		movimentoFinanceiroCotaDTO.setDataOperacao(dataOperacao);
    		movimentoFinanceiroCotaDTO.setBaixaCobranca(null);
    		movimentoFinanceiroCotaDTO.setDataVencimento(dataOperacao);
    		movimentoFinanceiroCotaDTO.setDataAprovacao(dataOperacao);
    		movimentoFinanceiroCotaDTO.setDataCriacao(dataOperacao);
    		movimentoFinanceiroCotaDTO.setObservacao(null);
    		movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
    		movimentoFinanceiroCotaDTO.setAprovacaoAutomatica(tipoMovimentoFinanceiroTaxaExtra.isAprovacaoAutomatica());
    		movimentoFinanceiroCotaDTO.setLancamentoManual(false);
    		movimentoFinanceiroCotaDTO.setFornecedor(fornecedor);
    		movimentoFinanceiroCotaDTO.setMovimentos(null);
    		
    	}
		
    	if(movimentoFinanceiroCotaDTO != null && movimentoFinanceiroCotaDTO.getValor().compareTo(BigDecimal.ZERO) > 0) {
    		
    		this.gerarMovimentoFinanceiroCota(movimentoFinanceiroCotaDTO, null);
    	}
	}
    
    /**
     * Gera Movimentos Financeiros das Cotas
     * 
     * @param cotas
     * @param dataOperacao
     * @param usuario
     */
    @Transactional
    @Override
    public void gerarMovimentoFinanceiroCota(final List<Cota> cotas, final Date dataOperacao, final Usuario usuario) {
        
        for (final Cota cota : cotas) {
            
            this.gerarMovimentoFinanceiroCota(cota, dataOperacao, usuario);
        }
    }
    
    /**
     * Gera Movimentos Financeiros da Cota no Processamento Financeiro de Cotas
     * à vista
     * 
     * @param cota
     * @param dataOperacao
     * @param usuario
     */
    @Transactional
    @Override
    public void gerarMovimentoFinanceiroCota(final Cota cota, final Date dataOperacao, final Usuario usuario) {
        
        // MOVIMENTOS DE ENVIO DE REPARTE À COTA QUE AINDA NÃO GERARAM
        // FINANCEIRO AGRUPADOS POR FORNECEDOR
        Map<Long, List<MovimentoEstoqueCota>> movimentosReparteAgrupadosPorFornecedor;
        final boolean isConferenciaRealizada = controleConferenciaEncalheCotaRepository.isConferenciaEncalheCotaFinalizada(cota.getId(), dataOperacao);
        
        if (isConferenciaRealizada) {
            
            movimentosReparteAgrupadosPorFornecedor = this.obterMovimentosEstoqueReparteAVista(cota.getId(), dataOperacao);
            
        } else {
            
            movimentosReparteAgrupadosPorFornecedor = this.obterMovimentosConsignadosPrevistoDiaEAVistaCotaAVista(cota.getId(), dataOperacao);
            
        }
        
        // MOVIMENTOS ESTORNADOS QUE ENTRAM COMO CREDITO À COTA AGUPADOS POR
        // FORNECEDOR
        final Map<Long, List<MovimentoEstoqueCota>> movimentosEstornoAgrupadosPorFornecedor = this.obterMovimentosEstoqueEstorno(cota.getId(), Arrays.asList(dataOperacao));
        
        // TODOS OS FORNECEDORES ENVOLVIDOS
        final Set<Long> fornecedoresId = new HashSet<Long>();
        fornecedoresId.addAll(movimentosReparteAgrupadosPorFornecedor.keySet());
        fornecedoresId.addAll(movimentosEstornoAgrupadosPorFornecedor.keySet());
        
        for (final Long fornecedorId : fornecedoresId) {
            
            final Fornecedor fornecedor = fornecedorRepository.buscarPorId(fornecedorId);
            
            // GERA MOVIMENTOS FINANCEIROS PARA A COTA A VISTA E PRODUTOS CONTA
            // FIRME
            this.gerarMovimentoFinanceiroCotaAVista(cota, fornecedor, movimentosReparteAgrupadosPorFornecedor
                    .get(fornecedorId), movimentosEstornoAgrupadosPorFornecedor.get(fornecedorId), dataOperacao, usuario);
        }
    }
    
    /**
     * Gera movimento financeiro para cota na Conferencia/Fechamento de Encalhe
     * 
     * @param idControleConferenciaEncalheCota
     */
    @Transactional
    @Override
    public void gerarMovimentoFinanceiroCota(final Cota cota, final List<Date> datas, final Usuario usuario,
            final Long idControleConferenciaEncalheCota, Integer diasPostergacao) {
    	
        Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
        
        if (diasPostergacao!=null){
        	
        	dataOperacao = this.calendarioService.adicionarDiasUteis(dataOperacao, diasPostergacao);
        }
        
        // MOVIMENTOS DA CONFERENCIA DE ENCALHE AGRUPADOS POR FORNECEDOR
        Map<Long, List<MovimentosEstoqueEncalheDTO>> movimentosEncalheAgrupadosPorFornecedor = null;
        
        if (idControleConferenciaEncalheCota != null) {
            
            movimentosEncalheAgrupadosPorFornecedor = this.obterMovimentosEstoqueEncalhe(cota.getId(), idControleConferenciaEncalheCota);
        } else {
            
            movimentosEncalheAgrupadosPorFornecedor = new HashMap<Long, List<MovimentosEstoqueEncalheDTO>>();
        }
        
        // MOVIMENTOS DE ENVIO DE REPARTE À COTA QUE AINDA NÃO GERARAM
        // FINANCEIRO AGUPADOS POR FORNECEDOR
        final Map<Long, List<MovimentosEstoqueEncalheDTO>> movimentosReparteAgrupadosPorFornecedor = this
                .obterMovimentosEstoqueReparteComChamadaEncalheOuProdutoContaFirme(cota.getId(), datas);
        
        // MOVIMENTOS ESTORNADOS QUE ENTRAM COMO CREDITO À COTA AGUPADOS POR
        // FORNECEDOR
        final Map<Long, List<MovimentoEstoqueCota>> movimentosEstornoAgrupadosPorFornecedor = this.obterMovimentosEstoqueEstorno(cota.getId(), datas);
        
        // TODOS OS FORNECEDORES ENVOLVIDOS
        final Set<Long> fornecedoresId = new HashSet<Long>();
        fornecedoresId.addAll(movimentosReparteAgrupadosPorFornecedor.keySet());
        fornecedoresId.addAll(movimentosEncalheAgrupadosPorFornecedor.keySet());
        fornecedoresId.addAll(movimentosEstornoAgrupadosPorFornecedor.keySet());
        
        for (final Long fornecedorId : fornecedoresId) {
            
            final Fornecedor fornecedor = fornecedorRepository.buscarPorId(fornecedorId);
            
            this.gerarMovimentoFinanceiroCotaRecolhimentoPorFornecedor(cota, 
            		                                                   fornecedor,
                                                                       idControleConferenciaEncalheCota, 
                                                                       dataOperacao, 
                                                                       usuario, 
                                                                       movimentosReparteAgrupadosPorFornecedor.get(fornecedorId), 
                                                                       movimentosEncalheAgrupadosPorFornecedor.get(fornecedorId),
                                                                       movimentosEstornoAgrupadosPorFornecedor.get(fornecedorId));
        }
    }
    
    @Override
    @Transactional
    public void gerarMovimentoFinanceiroDebitoCota(final TipoMovimentoFinanceiro tipoMovimento,
									    		  final Usuario usuario,
									    		  final Cota cota,
									    		  final Date dataVencimento,
									    		  final Date dataOperacao,
									    		  final BigDecimal valorDebito, 
									    		  final String observacaoMovimento){
														    	
    	MovimentoFinanceiroCota movimentoFinanceiro = new MovimentoFinanceiroCota();
		
		movimentoFinanceiro.setAprovadoAutomaticamente(true);
		movimentoFinanceiro.setAprovador(usuario);
		movimentoFinanceiro.setCota(cota);
		movimentoFinanceiro.setData(dataVencimento);
		movimentoFinanceiro.setDataAprovacao(dataOperacao);
		movimentoFinanceiro.setDataCriacao(dataOperacao);
		movimentoFinanceiro.setLancamentoManual(false);
		movimentoFinanceiro.setStatus(StatusAprovacao.APROVADO);
		movimentoFinanceiro.setStatusIntegracao(StatusIntegracao.INTEGRADO);
		movimentoFinanceiro.setUsuario(usuario);
		movimentoFinanceiro.setValor(valorDebito);	
		movimentoFinanceiro.setTipoMovimento(tipoMovimento);
		movimentoFinanceiro.setFornecedor(cota.getParametroCobranca().getFornecedorPadrao());
		movimentoFinanceiro.setObservacao(observacaoMovimento);
		
		movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiro);
    	
    }
    
    @Transactional
    @Override
    public void processarCreditosParaCotasNoProcessoDeFuroDeProdutoContaFirme(
    		final Long idLancamento, 
			final Long idUsuario) {

		final List<MovimentoEstoqueCota> movimentosEstoqueCota = 
				movimentoEstoqueCotaRepository.obterMovimentosComProdutoContaFirme(idLancamento);
		
		if( movimentosEstoqueCota== null || movimentosEstoqueCota.isEmpty()){
			return;
		}
		
		final Fornecedor fornecedorPadraoDistribuidor = politicaCobrancaService.obterFornecedorPadrao();
		
		final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		final Date dataCredito = calendarioService.obterProximaDataDiaUtil(dataOperacao);
		
		final TipoMovimentoFinanceiro tipoMovimentoCredito =
				tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.CREDITO);
		
		final Usuario usuario  = new Usuario(idUsuario);
		
		for(MovimentoEstoqueCota movimento : movimentosEstoqueCota){
		
			final ValoresAplicados valores = movimento.getValoresAplicados();
			
			final BigDecimal valorCredito = valores.getPrecoComDesconto().multiply(new BigDecimal(movimento.getQtde()));
			
			Fornecedor fornecedor = movimento.getCota().getParametroCobranca().getFornecedorPadrao();
			
			if(fornecedor == null){
				fornecedor = fornecedorPadraoDistribuidor;
			}
			
			
			final MovimentoFinanceiroCota movimentoFinanceiro = new MovimentoFinanceiroCota();
			
			movimentoFinanceiro.setAprovadoAutomaticamente(true);
			movimentoFinanceiro.setAprovador(usuario);
			movimentoFinanceiro.setCota(movimento.getCota());
			movimentoFinanceiro.setData(dataCredito);
			movimentoFinanceiro.setDataAprovacao(dataOperacao);
			movimentoFinanceiro.setDataCriacao(dataOperacao);
			movimentoFinanceiro.setLancamentoManual(false);
			movimentoFinanceiro.setStatus(StatusAprovacao.APROVADO);
			movimentoFinanceiro.setStatusIntegracao(StatusIntegracao.INTEGRADO);
			movimentoFinanceiro.setUsuario(usuario);
			movimentoFinanceiro.setValor(valorCredito);	
			movimentoFinanceiro.setTipoMovimento(tipoMovimentoCredito);
			movimentoFinanceiro.setFornecedor(fornecedor);
			movimentoFinanceiro.setObservacao("Credito para Produto Conta Firme devido a furo de produto");
			
			movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiro);
		}
	}
    
    @Transactional
    @Override
    public void processarDebitosParaCotasNoProcessoDeExpedicaoDeProdutoContaFirme(final ExpedicaoDTO expedicaoDTO, 
    																			  final List<MovimentoEstoqueCotaDTO>movimentosEstoqueCota){
    	
    	final List<MovimentoFinanceiroCotaDTO> movimentosFinanceiros = new ArrayList<>();
		
		for(MovimentoEstoqueCotaDTO movimento : movimentosEstoqueCota){

			final BigDecimal valorDebito = movimento.getPrecoComDesconto().multiply(new BigDecimal(movimento.getQtde()));
			
			final MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
			
			if(movimento.getIdFornecedor() == null){
				movimento.setIdFornecedor(expedicaoDTO.getIdFornecedorPadraoDistribuidor());
			}
			
			movimentoFinanceiroCotaDTO.setAprovacaoAutomatica(true);
			movimentoFinanceiroCotaDTO.setDataCriacao(expedicaoDTO.getDataOperacao());		
			movimentoFinanceiroCotaDTO.setStatus(StatusAprovacao.APROVADO.name());
			movimentoFinanceiroCotaDTO.setDataVencimento(expedicaoDTO.getDataVencimentoDebito());
			movimentoFinanceiroCotaDTO.setDataAprovacao(expedicaoDTO.getDataOperacao());		
			movimentoFinanceiroCotaDTO.setIdTipoMovimento(expedicaoDTO.getTipoMovimentoDebito().getId());		
			movimentoFinanceiroCotaDTO.setIdUsuario(expedicaoDTO.getIdUsuario());		
			movimentoFinanceiroCotaDTO.setUsuarioAprovadorId(expedicaoDTO.getIdUsuario());
			movimentoFinanceiroCotaDTO.setValor(valorDebito);
			movimentoFinanceiroCotaDTO.setLancamentoManual(true);
			movimentoFinanceiroCotaDTO.setObservacao("Débito para Produto Conta Firme");		
			movimentoFinanceiroCotaDTO.setIdCota(movimento.getIdCota());
			movimentoFinanceiroCotaDTO.setStatusIntegracao(StatusIntegracao.INTEGRADO.name());
			movimentoFinanceiroCotaDTO.setIdFornecedor(movimento.getIdFornecedor());

			movimentosFinanceiros.add(movimentoFinanceiroCotaDTO);
		}
		
		movimentoFinanceiroCotaRepository.adicionarEmLoteDTO(movimentosFinanceiros);
	}
}
