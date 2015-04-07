package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ProdutoLancamentoVO;
import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.DadosBalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoCanceladoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MatrizLancamentoNovaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.BigIntegerUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.vo.ConfirmacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class MatrizLancamentoNovaServiceImpl implements MatrizLancamentoNovaService {
    
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MatrizLancamentoNovaServiceImpl.class);
    
    private DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento;
    
    @Autowired
    protected LancamentoRepository lancamentoRepository;
    
    @Autowired
    protected CalendarioService calendarioService;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    protected DistribuidorRepository distribuidorRepository;
    
    @Autowired
    private FornecedorRepository fornecedorRepository;
    
    @Autowired
    private LancamentoService lancamentoService;
    
    
    @Override
    @Transactional(readOnly = true)
    public BalanceamentoLancamentoDTO obterMatrizLancamento(
            final FiltroLancamentoDTO filtro) {
        
        final BalanceamentoLancamentoDTO matrizLancamento = this.balancear(filtro);
        
        final List<ProdutoLancamentoCanceladoDTO> produtosLancamentosCancelados = this
                .obterProdutosLancamentosCancelados(filtro);
        
        if (produtosLancamentosCancelados != null
                && !produtosLancamentosCancelados.isEmpty()) {
            matrizLancamento
            .setProdutosLancamentosCancelados(produtosLancamentosCancelados);
        }
        
        return matrizLancamento;
    }
    
    private List<ProdutoLancamentoCanceladoDTO> obterProdutosLancamentosCancelados(
            final FiltroLancamentoDTO filtro) {
        
        final Date dataLancamento = filtro.getData();
        
        final Intervalo<Date> periodoDistribuicao = this
                .getPeriodoDistribuicao(dataLancamento);
        
        final List<ProdutoLancamentoCanceladoDTO> produtosLancamentosCancelados = lancamentoRepository
                .obterLancamentosCanceladosPor(periodoDistribuicao,
                        filtro.getIdsFornecedores());
        
        return produtosLancamentosCancelados;
        
    }
    
    @Override
    @Transactional
    public Map<Date, List<ProdutoLancamentoDTO>> confirmarMatrizLancamento(
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final List<Date> datasConfirmadas, final Usuario usuario) {
        
        final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamentoRetorno = new TreeMap<Date, List<ProdutoLancamentoDTO>>();
        
        this.validarDadosConfirmacao(matrizLancamento);
        
        final Map<Long, ProdutoLancamentoDTO> mapaLancamento = new TreeMap<Long, ProdutoLancamentoDTO>();
        
        for (final Date dataConfirmada : datasConfirmadas) {
            
            final List<ProdutoLancamentoDTO> listaProdutoLancamentoDTO = matrizLancamento
                    .get(dataConfirmada);
            
            if (listaProdutoLancamentoDTO == null || listaProdutoLancamentoDTO.isEmpty()) {
                
                continue;
            }
            
            Integer sequenciaMatriz = lancamentoRepository.obterProximaSequenciaMatrizPorData(dataConfirmada);
            
            this.ordenarProdutos(listaProdutoLancamentoDTO);
            
            for (final ProdutoLancamentoDTO produtoLancamento : listaProdutoLancamentoDTO) {
                
                if (!this.isProdutoConfirmado(produtoLancamento)) {
                    
                    produtoLancamento.setSequenciaMatriz(sequenciaMatriz++);
                    
                    final Long idLancamento = produtoLancamento.getIdLancamento();
                    
                    // Monta Map para controlar a atualização dos lançamentos
                    
                    mapaLancamento.put(idLancamento, produtoLancamento);
                    
                } else {
                    
                    this.montarMatrizLancamentosConfirmadosRetorno(
                            matrizLancamentoRetorno, produtoLancamento,
                            dataConfirmada);
                }
            }
        }
        
        if (!mapaLancamento.isEmpty()) {
            
            this.atualizarLancamentos(matrizLancamentoRetorno, usuario, mapaLancamento, OperacaoMatrizLancamento.CONFIRMAR);
        }
        
        return matrizLancamentoRetorno;
    }
    
	                                                                        /**
     * Efetua a ordenação dos produtos por nome do produto e tipo de lançamento,
     * se produto for parcial respeita a ordem de PARCIAL, FINAL
     * 
     * @param produtos
     */
    private void ordenarProdutos(final List<ProdutoLancamentoDTO> produtos) {
        
        Collections.sort(produtos, new Comparator<ProdutoLancamentoDTO>() {
            
            @Override
            public int compare(final ProdutoLancamentoDTO p1, final ProdutoLancamentoDTO p2) {
                
                int tipoLancamentoP1 = 0;
                int tipoLancamentoP2 = 0;
                
                if ("Parcial".equalsIgnoreCase(p1.getDescricaoLancamento())) {
                    tipoLancamentoP1 = 1;
                }
                
                if ("Parcial".equalsIgnoreCase(p2.getDescricaoLancamento())) {
                    tipoLancamentoP2 = 1;
                }
                
                if ("Final".equalsIgnoreCase(p1.getDescricaoLancamento())) {
                    tipoLancamentoP1 = 2;
                }
                
                if ("Final".equalsIgnoreCase(p2.getDescricaoLancamento())) {
                    tipoLancamentoP2 = 2;
                }
                
                return (tipoLancamentoP1 + p1.getNomeProduto())
                        .compareTo(tipoLancamentoP2 + p2.getNomeProduto());
                
            }
        });
        
    }
    
    @Override
    @Transactional
    public Map<Date, List<ProdutoLancamentoDTO>> salvarMatrizLancamento(
            final Date dataSalvar, final List<Long> idsFornecedores,
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final Usuario usuario) {
        
        final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamentoRetorno = new TreeMap<Date, List<ProdutoLancamentoDTO>>();
        
        this.validarDadosConfirmacao(matrizLancamento);
        
        final Map<Long, ProdutoLancamentoDTO> mapaLancamento = new TreeMap<Long, ProdutoLancamentoDTO>();
        
        for (final Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : matrizLancamento
                .entrySet()) {
            
            final Date dataLancamento = entry.getKey();
            
            final List<ProdutoLancamentoDTO> listaProdutoLancamentoDTO = entry
                    .getValue();
            
            if (listaProdutoLancamentoDTO == null
                    || listaProdutoLancamentoDTO.isEmpty()) {
                
                continue;
            }
            
            for (final ProdutoLancamentoDTO produtoLancamento : listaProdutoLancamentoDTO) {
                
                if (produtoLancamento.isAlterado()) {
                    
                    
                    if(dataLancamento.compareTo(dataSalvar)==0){
                        produtoLancamento.setStatus(StatusLancamento.EM_BALANCEAMENTO);
                        produtoLancamento.setStatusLancamento(StatusLancamento.EM_BALANCEAMENTO.name());
                    }else{
                        produtoLancamento.setStatus(StatusLancamento.CONFIRMADO);
                        produtoLancamento.setStatusLancamento(StatusLancamento.CONFIRMADO.name());
                    }
                    
                    final Long idLancamento = produtoLancamento.getIdLancamento();
                    
                    // Monta Map para controlar a atualização dos lançamentos
                    mapaLancamento.put(idLancamento, produtoLancamento);
                    
                } else if (!this.isProdutoConfirmado(produtoLancamento) && dataLancamento.compareTo(dataSalvar) == 0) {
                    
                    final Long idLancamento = produtoLancamento.getIdLancamento();
                    
                    // Monta Map para controlar a atualização dos lançamentos
                    mapaLancamento.put(idLancamento, produtoLancamento);
                    
                } else {
                    
                    this.montarMatrizLancamentosConfirmadosRetorno(
                            matrizLancamentoRetorno, produtoLancamento,
                            dataLancamento);
                }
            }
        }
        
        if (!mapaLancamento.isEmpty()) {
            
            this.atualizarLancamentos(matrizLancamentoRetorno, usuario,
                    mapaLancamento, OperacaoMatrizLancamento.SALVAR);
        }
        
        return matrizLancamentoRetorno;
    }
    
    @Override
    @Transactional
    public Map<Date, List<ProdutoLancamentoDTO>> salvarMatrizLancamentoTodosDias(
            final Date dataSalvar, final List<Long> idsFornecedores,
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final Usuario usuario) {
        
        final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamentoRetorno = new TreeMap<Date, List<ProdutoLancamentoDTO>>();
        
        this.validarDadosConfirmacao(matrizLancamento);
        
        final Map<Long, ProdutoLancamentoDTO> mapaLancamento = new TreeMap<Long, ProdutoLancamentoDTO>();
        
        for (final Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : matrizLancamento
                .entrySet()) {
            
            final Date dataLancamento = entry.getKey();
            
            final List<ProdutoLancamentoDTO> listaProdutoLancamentoDTO = entry
                    .getValue();
            
            if (listaProdutoLancamentoDTO == null
                    || listaProdutoLancamentoDTO.isEmpty()) {
                
                continue;
            }
            
            for (final ProdutoLancamentoDTO produtoLancamento : listaProdutoLancamentoDTO) {
                
                if (produtoLancamento.isAlterado()) {
                	
                    
                    /*
                    if(dataLancamento.compareTo(dataSalvar)==0){
                        produtoLancamento.setStatus(StatusLancamento.EM_BALANCEAMENTO);
                        produtoLancamento.setStatusLancamento(StatusLancamento.EM_BALANCEAMENTO.name());
                    }else{
                        produtoLancamento.setStatus(StatusLancamento.CONFIRMADO);
                        produtoLancamento.setStatusLancamento(StatusLancamento.CONFIRMADO.name());
                    }
                    */
                    
                    final Long idLancamento = produtoLancamento.getIdLancamento();
                    
                    // Monta Map para controlar a atualização dos lançamentos
                    mapaLancamento.put(idLancamento, produtoLancamento);
                    
                } else if (!this.isProdutoConfirmado(produtoLancamento) && dataLancamento.compareTo(dataSalvar) == 0) {
                    
                	produtoLancamento.setAlterado(true);
                	
                    final Long idLancamento = produtoLancamento.getIdLancamento();
                    
                    // Monta Map para controlar a atualização dos lançamentos
                    mapaLancamento.put(idLancamento, produtoLancamento);
                    
                } else {
                    
                    this.montarMatrizLancamentosConfirmadosRetorno(
                            matrizLancamentoRetorno, produtoLancamento,
                            dataLancamento);
                }
            }
        }
        
        if (!mapaLancamento.isEmpty()) {
            
            this.atualizarLancamentosMatriz(matrizLancamentoRetorno, usuario,
                    mapaLancamento, OperacaoMatrizLancamento.SALVAR);
        }
        
        return matrizLancamentoRetorno;
    }
    
    private void validarDadosConfirmacao(
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento) {
        
        if (matrizLancamento == null || matrizLancamento.isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Matriz de lançamento não informada!");
        }
    }
    
	                                                                        /**
     * Método que atualiza as informações dos lançamentos.
     * 
     * @param matrizLancamentoRetorno
     * @param idsLancamento - identificadores de lançamentos
     * @param usuario - usuário
     * @param mapaLancamento - mapa de lancamentos e produtos de recolhimento
     * 
     * @return {@link Map<Date, List<ProdutoLancamentoDTO>>}
     */
    private void atualizarLancamentos(
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamentoRetorno,
            final Usuario usuario, final Map<Long, ProdutoLancamentoDTO> mapaLancamento,
            final OperacaoMatrizLancamento operacaoMatrizLancamento) {
        
        final StatusLancamento proximoStatusLancamento = this.getProximoStatusLancamentoPorOperacao(operacaoMatrizLancamento);
        
        final Set<Long> idsLancamento = mapaLancamento.keySet();
        
        final List<Lancamento> listaLancamentos = lancamentoRepository
                .obterLancamentosPorIdOrdenados(idsLancamento);
        
        this.validarDadosAtualizacaoLancamento(idsLancamento, listaLancamentos);
        
        for (final Lancamento lancamento : listaLancamentos) {
            
            final ProdutoLancamentoDTO produtoLancamento = mapaLancamento.get(lancamento.getId());
            
            final Date novaData = produtoLancamento.getNovaDataLancamento();
            
            if (produtoLancamento.isLancamentoAgrupado()) {
                
                continue;
            }
            
            this.tratarLancamentosAgrupadosParaConfirmacao(lancamento,
                    listaLancamentos, mapaLancamento);
            
            final boolean gerarHistoricoLancamento = !lancamento.getStatus()
                    .equals(proximoStatusLancamento);
            
            if (gerarHistoricoLancamento) {
                
                this.gerarHistoricoLancamento(usuario, lancamento);
            }
            
            if(produtoLancamento.isAlterado()){
                produtoLancamento.setAlterado(false);
                this.alterarLancamento(produtoLancamento, lancamento, novaData,
                        produtoLancamento.getStatus(), usuario);
            }else {
                
                this.alterarLancamento(produtoLancamento, lancamento, novaData, proximoStatusLancamento, usuario);
            }
            
            this.montarMatrizLancamentosRetorno(matrizLancamentoRetorno,
                    produtoLancamento, novaData, proximoStatusLancamento);
            
            this.ajustarDataDeRecolhimentoDoProduto(lancamento,novaData);
            
            lancamentoRepository.merge(lancamento);
        }
    }
    /*
    * Método que atualiza as informações dos lançamentos.
    * 
    * @param matrizLancamentoRetorno
    * @param idsLancamento - identificadores de lançamentos
    * @param usuario - usuário
    * @param mapaLancamento - mapa de lancamentos e produtos de recolhimento
    * 
    * @return {@link Map<Date, List<ProdutoLancamentoDTO>>}
    */
   private void atualizarLancamentosMatriz(
           final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamentoRetorno,
           final Usuario usuario, final Map<Long, ProdutoLancamentoDTO> mapaLancamento,
           final OperacaoMatrizLancamento operacaoMatrizLancamento) {
       
       final StatusLancamento proximoStatusLancamento = this
               .getProximoStatusLancamentoPorOperacao(operacaoMatrizLancamento);
       
       final Set<Long> idsLancamento = mapaLancamento.keySet();
       
       final List<Lancamento> listaLancamentos = lancamentoRepository
               .obterLancamentosPorIdOrdenados(idsLancamento);
       
       this.validarDadosAtualizacaoLancamento(idsLancamento, listaLancamentos);
       
       for (final Lancamento lancamento : listaLancamentos) {
           
           final ProdutoLancamentoDTO produtoLancamento = mapaLancamento
                   .get(lancamento.getId());
           
           final Date novaData = produtoLancamento.getNovaDataLancamento();
           
           if (produtoLancamento.isLancamentoAgrupado()) {
               
               continue;
           }
           
           this.tratarLancamentosAgrupadosParaConfirmacao(lancamento,
                   listaLancamentos, mapaLancamento);
           
           final boolean gerarHistoricoLancamento = !lancamento.getStatus()
                   .equals(proximoStatusLancamento);
           
           if (gerarHistoricoLancamento) {
               
               this.gerarHistoricoLancamento(usuario, lancamento);
           }
           
           if(produtoLancamento.isAlterado()){
               produtoLancamento.setAlterado(false);
               this.alterarLancamento(produtoLancamento, lancamento, novaData,
                       produtoLancamento.getStatus(), usuario);
           }else {
               
               this.alterarLancamento(produtoLancamento, lancamento, novaData,
            		   produtoLancamento.getStatus(), usuario);
           }
           
           this.montarMatrizLancamentosRetorno(matrizLancamentoRetorno,
                   produtoLancamento, novaData, produtoLancamento.getStatus());
           
           this.ajustarDataDeRecolhimentoDoProduto(lancamento,novaData);
           
           lancamentoRepository.merge(lancamento);
       }
   }
    
    /**
     * Ajusta a data de recolhimento do produto caso a data de lançamento informada seja maior que a mesma
     * e o produto não seja PARCIAL.
     * 
     * @param lancamento lancamento do produto
     * @param novaData nova data de lancamento informada
     */
    private void ajustarDataDeRecolhimentoDoProduto(
    		final Lancamento lancamento,final Date novaData) {

    	ProdutoEdicao produtoEdicao = lancamento.getProdutoEdicao();
    	
    	if(produtoEdicao!= null){
    		
    		if(!produtoEdicao.isParcial() 
    				&& novaData.compareTo(lancamento.getDataRecolhimentoDistribuidor())>=0){
        		
        		Date novaDataRecolhimento = DateUtil.adicionarDias(novaData, produtoEdicao.getPeb());
        		
        		lancamento.setDataRecolhimentoDistribuidor(novaDataRecolhimento);
        	}
    	}
	}

	private StatusLancamento getProximoStatusLancamentoPorOperacao(
            final OperacaoMatrizLancamento operacaoMatrizLancamento) {
        
        StatusLancamento statusLancamento = null;
        
        if (operacaoMatrizLancamento.equals(OperacaoMatrizLancamento.SALVAR)) {
            
            statusLancamento = StatusLancamento.EM_BALANCEAMENTO;
        }
        
        if (operacaoMatrizLancamento.equals(OperacaoMatrizLancamento.CONFIRMAR)) {
            
            statusLancamento = StatusLancamento.BALANCEADO;
        }
        
        return statusLancamento;
    }
    
    private void validarDadosAtualizacaoLancamento(final Set<Long> idsLancamento,
            final List<Lancamento> listaLancamentos) {
        
        if (listaLancamentos == null || listaLancamentos.isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Lançamento não encontrado!");
        }
        
        if (idsLancamento.size() != listaLancamentos.size()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Lançamento não encontrado!");
        }
    }
    
    private void efetivarAgrupamentoLancamento(final Lancamento lancamentoAtualizar,
            final Lancamento lancamentoAgrupar) {
        
        lancamentoAtualizar.setEstudo(this.agruparEstudo(lancamentoAtualizar,
                lancamentoAgrupar));
        
        lancamentoAtualizar.setRecebimentos(this.agruparRecebimentos(
                lancamentoAtualizar, lancamentoAgrupar));
        
        this.agruparLancamento(lancamentoAtualizar, lancamentoAgrupar);
    }
    
    private void agruparLancamento(final Lancamento lancamentoAtualizar,
            final Lancamento lancamentoAgrupar) {
        
        final BigInteger somaReparte = BigIntegerUtil.soma(
                lancamentoAtualizar.getReparte(),
                lancamentoAgrupar.getReparte());
        
        lancamentoAtualizar.setReparte(somaReparte);
        
        final BigInteger somaRepartePromocional = BigIntegerUtil.soma(
                lancamentoAtualizar.getRepartePromocional(),
                lancamentoAgrupar.getRepartePromocional());
        
        lancamentoAtualizar.setRepartePromocional(somaRepartePromocional);
    }
    
    private Estudo agruparEstudo(final Lancamento lancamentoAtualizar,
            final Lancamento lancamentoAgrupar) {
        
        Estudo estudoAtualizar = lancamentoAtualizar.getEstudo();
        
        final Estudo estudoAgrupar = lancamentoAgrupar.getEstudo();
        
        if (estudoAgrupar == null) {
            
            return estudoAtualizar;
        }
        
        if (estudoAtualizar != null) {
            
            final BigInteger somaQtdeReparte = BigIntegerUtil.soma(
                    estudoAtualizar.getQtdeReparte(),
                    estudoAgrupar.getQtdeReparte());
            
            estudoAtualizar.setQtdeReparte(somaQtdeReparte);
            
        } else {
            
            estudoAtualizar = new Estudo();
            
            estudoAtualizar
            .setDataLancamento(estudoAgrupar.getDataLancamento());
            estudoAtualizar.setProdutoEdicao(estudoAgrupar.getProdutoEdicao());
            estudoAtualizar.setQtdeReparte(estudoAgrupar.getQtdeReparte());
            estudoAtualizar.setStatus(StatusLancamento.ESTUDO_FECHADO);
            estudoAtualizar.setDataCadastro(new Date());
        }
        
        final Set<EstudoCota> estudoCotasAgrupar = estudoAgrupar.getEstudoCotas();
        
        if (estudoCotasAgrupar == null || estudoCotasAgrupar.isEmpty()) {
            
            return estudoAtualizar;
        }
        
        final Set<EstudoCota> estudoCotasAtualizar = estudoAtualizar.getEstudoCotas();
        
        estudoAtualizar.setEstudoCotas(this.agruparEstudoCota(estudoAtualizar,
                estudoCotasAtualizar, estudoCotasAgrupar));
        
        return estudoAtualizar;
    }
    
    private Set<EstudoCota> agruparEstudoCota(final Estudo estudoAtualizar,
            Set<EstudoCota> estudoCotasAtualizar,
            final Set<EstudoCota> estudoCotasAgrupar) {
        
        if (estudoCotasAtualizar == null) {
            estudoCotasAtualizar = new TreeSet<EstudoCota>();
        }
        
        for (final EstudoCota estudoCotaAgrupar : estudoCotasAgrupar) {
            
            boolean existeEstudoCota = false;
            
            for (final EstudoCota estudoCotaAtualizar : estudoCotasAtualizar) {
                
                if (estudoCotaAtualizar.getCota().getId()
                        .equals(estudoCotaAgrupar.getCota().getId())) {
                    
                    existeEstudoCota = true;
                    
                    estudoCotaAtualizar.setQtdeEfetiva(estudoCotaAtualizar
                            .getQtdeEfetiva().add(
                                    estudoCotaAgrupar.getQtdeEfetiva()));
                    
                    estudoCotaAtualizar.setQtdePrevista(estudoCotaAtualizar
                            .getQtdePrevista().add(
                                    estudoCotaAgrupar.getQtdePrevista()));
                    
                    break;
                }
            }
            
            if (!existeEstudoCota) {
                
                final EstudoCota estudoCotaAtualizar = new EstudoCota();
                
                estudoCotaAtualizar.setCota(estudoCotaAgrupar.getCota());
                estudoCotaAtualizar.setQtdeEfetiva(estudoCotaAgrupar
                        .getQtdeEfetiva());
                estudoCotaAtualizar.setQtdePrevista(estudoCotaAgrupar
                        .getQtdePrevista());
                estudoCotaAtualizar.setEstudo(estudoAtualizar);
                
                estudoCotasAtualizar.add(estudoCotaAtualizar);
            }
        }
        
        return estudoCotasAtualizar;
    }
    
    private Set<ItemRecebimentoFisico> agruparRecebimentos(
            final Lancamento lancamentoAtualizar, final Lancamento lancamentoAgrupar) {
        
        Set<ItemRecebimentoFisico> recebimentosAtualizar = lancamentoAtualizar
                .getRecebimentos();
        
        final Set<ItemRecebimentoFisico> recebimentosAgrupar = lancamentoAgrupar
                .getRecebimentos();
        
        if (recebimentosAgrupar == null || recebimentosAgrupar.isEmpty()) {
            
            return recebimentosAtualizar;
        }
        
        if (recebimentosAtualizar == null) {
            
            recebimentosAtualizar = new TreeSet<ItemRecebimentoFisico>();
        }
        
        recebimentosAtualizar.addAll(recebimentosAgrupar);
        
        return recebimentosAtualizar;
    }
    
    
    private void tratarLancamentosAgrupadosParaConfirmacao(
            final Lancamento lancamentoAtualizar, final List<Lancamento> lancamentos,
            final Map<Long, ProdutoLancamentoDTO> mapaLancamento) {
        
        final ProdutoLancamentoDTO produtoLancamento = mapaLancamento
                .get(lancamentoAtualizar.getId());
        
        for (final ProdutoLancamentoDTO produtoLancamentoAgrupado : produtoLancamento
                .getProdutosLancamentoAgrupados()) {
            
            final Lancamento lancamentoAgrupar = this.obterLancamentoDaLista(
                    produtoLancamentoAgrupado.getIdLancamento(), lancamentos);
            
            this.efetivarAgrupamentoLancamento(lancamentoAtualizar,
                    lancamentoAgrupar);
            
            try {
                
                lancamentoRepository.remover(lancamentoAgrupar);
                
            } catch (final Exception e) {
                final String msg = "Erro ao excluir o lançamento do Produto: "
                        + produtoLancamento.getNomeProduto()
                    + " - Edição: " + produtoLancamento.getNumeroEdicao();
                LOGGER.error(msg, e);
                throw new ValidacaoException(TipoMensagem.WARNING, msg);
            }
        }
        
        produtoLancamento.getProdutosLancamentoAgrupados().clear();
    }
    
    private Lancamento obterLancamentoDaLista(final Long idLancamento,
            final List<Lancamento> lancamentos) {
        
        for (final Lancamento lancamento : lancamentos) {
            
            if (lancamento.getId().equals(idLancamento)) {
                
                return lancamento;
            }
        }
        
        return null;
    }
    
    private void alterarLancamento(final ProdutoLancamentoDTO produtoLancamento,
            final Lancamento lancamento, final Date novaData,
            final StatusLancamento statusLancamento, final Usuario usuario) {
        
        lancamento.setDataLancamentoDistribuidor(novaData);
        lancamento.setStatus(statusLancamento);
        lancamento.setDataStatus(new Date());
        lancamento.setSequenciaMatriz(produtoLancamento.getSequenciaMatriz());
        lancamento.setUsuario(usuario);
    }
    
    private void gerarHistoricoLancamento(final Usuario usuario, final Lancamento lancamento) {
        
        final HistoricoLancamento historicoLancamento = new HistoricoLancamento();
        
        historicoLancamento.setLancamento(lancamento);
        historicoLancamento.setTipoEdicao(TipoEdicao.ALTERACAO);
        historicoLancamento.setStatusNovo(lancamento.getStatus());
        historicoLancamento.setDataEdicao(new Date());
        historicoLancamento.setResponsavel(usuario);
        
    }
    
    private void montarMatrizLancamentosConfirmadosRetorno(
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final ProdutoLancamentoDTO produtoLancamento, final Date dataLancamento) {
        
        List<ProdutoLancamentoDTO> produtosLancamento = matrizLancamento
                .get(dataLancamento);
        
        if (produtosLancamento == null) {
            
            produtosLancamento = new ArrayList<ProdutoLancamentoDTO>();
        }
        
        produtosLancamento.add(produtoLancamento);
        
        matrizLancamento.put(dataLancamento, produtosLancamento);
    }
    
    
    private void montarMatrizLancamentosRetorno(
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final ProdutoLancamentoDTO produtoLancamento, final Date novaData,
            final StatusLancamento statusLancamento) {
        
        if (produtoLancamento.isLancamentoAgrupado()) {
            
            return;
        }
        
        produtoLancamento.setDataLancamentoDistribuidor(novaData);
        produtoLancamento.setStatusLancamento(statusLancamento.toString());
        
        List<ProdutoLancamentoDTO> produtosLancamento = matrizLancamento
                .get(novaData);
        
        if (produtosLancamento == null) {
            
            produtosLancamento = new ArrayList<ProdutoLancamentoDTO>();
        }
        
        produtosLancamento.add(produtoLancamento);
        
        matrizLancamento.put(novaData, produtosLancamento);
    }
    
    /**
     * Valida o filtro informado para realizar o balanceamento.
     */
    private void validarFiltro(final FiltroLancamentoDTO filtro) {
        
        if (filtro == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Os dados do filtro devem ser informados!");
            
        } else {
            
            final List<String> mensagens = new ArrayList<String>();
            
            if (filtro.getData() == null) {
                
                mensagens
                .add("Os dados do filtro da tela devem ser informados!");
            }
            
            if (filtro.getIdsFornecedores() == null
                    || filtro.getIdsFornecedores().isEmpty()) {
                
                mensagens
                .add("Os dados do filtro da tela devem ser informados!");
            }
            
            if (!mensagens.isEmpty()) {
                
                final ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.WARNING,
                        mensagens);
                
                throw new ValidacaoException(validacaoVO);
            }
        }
    }
    
	                                                                        /**
     * Efetua todas as etapas para a realização do balanceamento da matriz de
     * lançamento.
     */
    private BalanceamentoLancamentoDTO balancear(
            final FiltroLancamentoDTO filtro) {
        
        final BalanceamentoLancamentoDTO balanceamentoLancamento = new BalanceamentoLancamentoDTO();
        
        Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento = null;
        
        DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento;
        
        final List<Long> idFornenedores = filtro.getIdsFornecedores();
        
        List<Long> porFornenedor;
        
        final Set<Date> datasFornecedor = new TreeSet<Date>();
        
        
        for (final Long idFornecedor : idFornenedores) {
            
            porFornenedor = new ArrayList<Long>();
            porFornenedor.add(idFornecedor);
            filtro.setIdsFornecedores(porFornenedor);
            
            this.validarFiltro(filtro);
            
            dadosBalanceamentoLancamento = this.obterDadosLancamento(filtro);
            
            if(datasFornecedor!=null && dadosBalanceamentoLancamento.getDatasDistribuicaoPorFornecedor()!=null && dadosBalanceamentoLancamento.getDatasDistribuicaoPorFornecedor().get(Long.valueOf(1))!=null){
                datasFornecedor.addAll(dadosBalanceamentoLancamento.getDatasDistribuicaoPorFornecedor().get(Long.valueOf(1)));
            }
            
            datasFornecedor.addAll(dadosBalanceamentoLancamento.getDatasExpectativaReparte());
            
            dadosBalanceamentoLancamento.setDatasBalanceaveis(datasFornecedor);
            
            this.validarDadosEntradaBalanceamento(dadosBalanceamentoLancamento);
            
            balanceamentoLancamento
            .setCapacidadeDistribuicao(dadosBalanceamentoLancamento
                    .getCapacidadeDistribuicao());
            balanceamentoLancamento
            .setMediaLancamentoDistribuidor(dadosBalanceamentoLancamento
                    .getMediaDistribuicao().longValue());
            balanceamentoLancamento
            .setDataLancamento(dadosBalanceamentoLancamento
                    .getDataLancamento());
            
            balanceamentoLancamento
            .setDatasExpedicaoConfirmada(dadosBalanceamentoLancamento
                    .getDatasExpedicaoConfirmada());
            
            balanceamentoLancamento
            .setMediaLancamentoDistribuidor(dadosBalanceamentoLancamento
                    .getMediaDistribuicao().longValue());
            
            matrizLancamento = this.gerarMatrizBalanceamentoLancamento(dadosBalanceamentoLancamento);
            
            if (balanceamentoLancamento.getMatrizLancamento() == null) {
                
                balanceamentoLancamento.setMatrizLancamento(matrizLancamento);
                
            } else {
                
                balanceamentoLancamento.addMatrizLancamento(matrizLancamento);
                
            }
        }
        
        filtro.setIdsFornecedores(idFornenedores);
        

        return balanceamentoLancamento;
    }
    
	                                                                        /**
     * Valida os dados de entrada para realização do balanceamento.
     */
    private void validarDadosEntradaBalanceamento(
            final DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
        
        if (dadosBalanceamentoLancamento == null
                || dadosBalanceamentoLancamento.getCapacidadeDistribuicao() == null
                || dadosBalanceamentoLancamento
                .getDatasDistribuicaoPorFornecedor() == null
                || dadosBalanceamentoLancamento.getDatasExpectativaReparte() == null
                || dadosBalanceamentoLancamento.getProdutosLancamento() == null
                || dadosBalanceamentoLancamento
                .getQtdDiasLimiteParaReprogLancamento() == null) {
            
            throw new RuntimeException("Dados para efetuar balanceamento inválidos!");
        }
    }
    
	                                                                        /**
     * Gera o mapa contendo a matriz de balanceamento de lançamento.
     */
    private Map<Date, List<ProdutoLancamentoDTO>> gerarMatrizBalanceamentoLancamento(
            final DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
        
        final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento = new TreeMap<Date, List<ProdutoLancamentoDTO>>();
        
        List<ProdutoLancamentoDTO> produtosLancamentoNaoBalanceadosTotal = new ArrayList<ProdutoLancamentoDTO>();
        
        final Set<Date> datasConfirmadas = dadosBalanceamentoLancamento.getDatasExpedicaoConfirmada();
        
        final Map<Long, TreeSet<Date>> datasDistribuicaoPorFornecedor = this
                .obterDatasDistribuicao(dadosBalanceamentoLancamento,
                        datasConfirmadas);
        
        final List<ProdutoLancamentoDTO> produtosLancamentoBalancear = this
                .processarProdutosLancamentoNaoBalanceaveis(matrizLancamento,
                        dadosBalanceamentoLancamento);
        
        
        
        final Set<Date> datasExpectativaReparte = dadosBalanceamentoLancamento.getDatasExpectativaReparte();
        
        for (final Map.Entry<Long, TreeSet<Date>> entry : datasDistribuicaoPorFornecedor.entrySet()) {
            
            final Long idFornecedor = entry.getKey();
            
            final TreeSet<Date> datasDistribuicao = entry.getValue();
            
            final Set<Date> datasExpectativaReparteOrdenadas = ordenarMapaExpectativaRepartePorDatasDistribuicao(
                    datasExpectativaReparte, entry.getValue());
            
            datasExpectativaReparteOrdenadas.addAll(dadosBalanceamentoLancamento.getDatasBalanceaveis());
            
            for (final Date dataLancamentoPrevista : datasExpectativaReparteOrdenadas) {
                
                final List<ProdutoLancamentoDTO> produtosLancamentoBalanceaveisDataPrevista = this
                        .obterProdutosLancamentoBalanceaveisPorData(produtosLancamentoBalancear,dataLancamentoPrevista);
                        
                     
                        final List<ProdutoLancamentoDTO> produtosLancamentoNaoBalanceados = this
                                .processarProdutosLancamentoBalanceaveis(
                                        matrizLancamento, datasDistribuicao,
                                        dataLancamentoPrevista,
                                        dadosBalanceamentoLancamento,
                                        produtosLancamentoBalanceaveisDataPrevista,
                                        idFornecedor);
                        
                        if (produtosLancamentoNaoBalanceados != null
                                && !produtosLancamentoNaoBalanceados.isEmpty()) {
                            
                            produtosLancamentoNaoBalanceadosTotal
                            .addAll(produtosLancamentoNaoBalanceados);
                        }
                    
              
            }
        }
       return matrizLancamento;
    }
    
    private Set<Date> obterDatasConfirmadas(
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final Set<Date> datasExpedicaoConfirmada) {
        
        final Set<Date> datasConfirmadas = new TreeSet<>();
        
        
        for (final Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : matrizLancamento
                .entrySet()) {
            
            for (final ProdutoLancamentoDTO produtoLancamento : entry.getValue()) {
                
                datasConfirmadas.add(entry.getKey());
                
                if (!this.isProdutoConfirmado(produtoLancamento)) {
                    
                    datasConfirmadas.remove(entry.getKey());
                    
                    break;
                }
            }
        }
        
        return datasConfirmadas;
    }
    
	                                                                        /**
     * Obtém as datas de distribuição, desconsiderando as datas em que o
     * balanceamento já foi confirmado.
     */
    private Map<Long, TreeSet<Date>> obterDatasDistribuicao(
            final DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento,
            final Set<Date> datasConfirmadas) {
        
        final Map<Long, TreeSet<Date>> datasDistribuicaoPorFornecedor = dadosBalanceamentoLancamento
                .getDatasDistribuicaoPorFornecedor();
            
        return datasDistribuicaoPorFornecedor;
    }
    
    private Set<Date> ordenarMapaExpectativaRepartePorDatasDistribuicao(
            final Set<Date> datasExpectativaReparte, final TreeSet<Date> datasDistribuicao) {
        
        final Set<Date> datasExpectativaReparteOrdenado = new LinkedHashSet<Date>();
        
        for (final Date dataDistribuicao : datasDistribuicao) {
            
            if (datasExpectativaReparte.contains(dataDistribuicao)) {
                
                datasExpectativaReparteOrdenado.add(dataDistribuicao);
                
                datasExpectativaReparte.remove(dataDistribuicao);
            }
        }
        
        datasExpectativaReparteOrdenado.addAll(datasExpectativaReparte);
            
        return datasExpectativaReparteOrdenado;
    }
    
	                                                                        /**
     * Processa os produtos para lançamento que não devem ser balanceados e
     * adiciona os mesmos no mapa da matriz de balanceamento.
     */
    private List<ProdutoLancamentoDTO> processarProdutosLancamentoNaoBalanceaveis(
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final DadosBalanceamentoLancamentoDTO dadosLancamentoBalanceamento) {
        
        final List<ProdutoLancamentoDTO> produtosLancamentoNaoProcessados = this
                .processarProdutosLancamentoConfirmadosEExpedidos(
                        matrizLancamento, dadosLancamentoBalanceamento);
        
        return produtosLancamentoNaoProcessados;
    }
    
    
    private List<ProdutoLancamentoDTO> processarProdutosLancamentoConfirmadosEExpedidos(
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final DadosBalanceamentoLancamentoDTO dadosLancamentoBalanceamento) {
        
        final List<ProdutoLancamentoDTO> produtosLancamentoNaoProcessados = new ArrayList<>();
        
        final List<ProdutoLancamentoDTO> produtosLancamento = dadosLancamentoBalanceamento
                .getProdutosLancamento();
        
        
        BigInteger totalBalanceavel = BigInteger.ZERO;
        BigInteger totalNaoBalanceavel = BigInteger.ZERO;
        
        final Set<Date> datasValidasBalanceaveis = dadosLancamentoBalanceamento.getDatasBalanceaveis();
        
        for (final ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
            
            final Date dataLancamentoDistribuidor = produtoLancamento
                    .getDataLancamentoDistribuidor();
            
                
                this.adicionarProdutoLancamentoNaMatriz(matrizLancamento,
                        produtoLancamento, dataLancamentoDistribuidor);
                
                totalNaoBalanceavel = totalNaoBalanceavel.add(produtoLancamento.getRepartePrevisto());
        }
        
        
        
        
        
        long tb = 0;
        
        if (!datasValidasBalanceaveis.isEmpty()) {
            dadosLancamentoBalanceamento.setDatasBalanceaveis(datasValidasBalanceaveis);
            tb = totalBalanceavel.longValue() / datasValidasBalanceaveis.size();
            dadosLancamentoBalanceamento.setMediaDistribuicao(new BigInteger(""+tb));
            
        }
        
        return produtosLancamentoNaoProcessados;
    }
    
    
	                                                                        /**
     * Processa os produtos para lançamento que devem ser balanceados e adiciona
     * os mesmos no mapa da matriz de balanceamento.
     */
    private List<ProdutoLancamentoDTO> processarProdutosLancamentoBalanceaveis(
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final TreeSet<Date> datasDistribuicao, final Date dataLancamentoPrevista,
            final DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento,
            final List<ProdutoLancamentoDTO> produtosLancamentoBalanceaveis,
            final Long idFornecedor) {
        
        final Date dataLancamentoEscolhida = this.obterDataDistribuicaoEscolhida(
                matrizLancamento, datasDistribuicao, dataLancamentoPrevista);
        
        if (dataLancamentoEscolhida == null) {
            
            return null;
        }
        
        final List<ProdutoLancamentoDTO> produtosLancamentoDataEscolhida = matrizLancamento
                .get(dataLancamentoEscolhida);
        
        final BigInteger expectativaReparteDataEscolhida = this
                .obterExpectativaReparteTotal(produtosLancamentoDataEscolhida);
        
        final List<ProdutoLancamentoDTO> produtosLancamentoNaoBalanceados = this
                .balancearProdutosLancamento(matrizLancamento,
                        produtosLancamentoBalanceaveis,
                        dadosBalanceamentoLancamento,
                        expectativaReparteDataEscolhida,
                        dataLancamentoEscolhida, false,
                        dadosBalanceamentoLancamento.getCapacidadeDistribuicao(),
                        false,
                        dadosBalanceamentoLancamento.getMediaDistribuicao(),
                        false,
                        idFornecedor);
        
        return produtosLancamentoNaoBalanceados;
    }
    
	                                                                        /**
     * Obtém uma data de distribuição de acordo as datas de distribuição
     * permitidas. Ordem de tentativa de escolha da data: 1º Data igual a data
     * prevista 2º Menor data que ainda não possui nenhum produto balanceado 3º
     * Data que possui menor quantidade de expectativa de reparte balanceado
     */
    private Date obterDataDistribuicaoEscolhida(
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final TreeSet<Date> datasDistribuicao, final Date dataLancamentoPrevista) {
        
        Date dataLancamentoEscolhida = null;
        
        if (dataLancamentoPrevista != null
                && datasDistribuicao.contains(dataLancamentoPrevista)) {
            
            dataLancamentoEscolhida = dataLancamentoPrevista;
        }
        
        if (dataLancamentoEscolhida == null) {
            
            for (final Date dataDistribuicao : datasDistribuicao) {
                
                final List<ProdutoLancamentoDTO> produtosLancamento = matrizLancamento
                        .get(dataDistribuicao);
                
                if (produtosLancamento == null) {
                    
                    dataLancamentoEscolhida = dataDistribuicao;
                    
                    break;
                }
            }
        }
        
        if (dataLancamentoEscolhida == null) {
            
            BigInteger menorExpectativaReparte = null;
            
            for (final Date dataDistribuicao : datasDistribuicao) {
                
                final List<ProdutoLancamentoDTO> produtosLancamento = matrizLancamento
                        .get(dataDistribuicao);
                
                final BigInteger expectativaReparteData = this
                        .obterExpectativaReparteTotal(produtosLancamento);
                
                if (menorExpectativaReparte == null
                        || expectativaReparteData
                        .compareTo(menorExpectativaReparte) == -1) {
                    
                    menorExpectativaReparte = expectativaReparteData;
                    
                    dataLancamentoEscolhida = dataDistribuicao;
                }
            }
        }
        
        return dataLancamentoEscolhida;
    }
    
    
    
    /**
     * Obtém os produtos balanceáveis de uma determinada data.
     */
    private List<ProdutoLancamentoDTO> obterProdutosLancamentoBalanceaveisPorData(
            final List<ProdutoLancamentoDTO> produtosLancamento, final Date dataLancamento) {
        
        final List<ProdutoLancamentoDTO> produtosLancamentoFiltrados = new ArrayList<ProdutoLancamentoDTO>();
        
        if (produtosLancamento == null || produtosLancamento.isEmpty()
                || dataLancamento == null) {
            
            return produtosLancamentoFiltrados;
        }
        
        
        
        for (final ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
            
            if (produtoLancamento.getDataLancamentoDistribuidor().equals(dataLancamento)) {
                
                produtosLancamentoFiltrados.add(produtoLancamento);
                
            }
        }
        
        produtosLancamento.removeAll(produtosLancamentoFiltrados);
        
        this.ordenarProdutosLancamentoPorPeriodicidadeExpectativaReparte(produtosLancamentoFiltrados);
        
        return produtosLancamentoFiltrados;
    }
    
	                                                                        /**
     * Obtém a expectativa de reparte total dos produtos para lançamento.
     */
    private BigInteger obterExpectativaReparteTotal(
            final List<ProdutoLancamentoDTO> produtosLancamento) {
        
        BigInteger expectativaReparteTotal = BigInteger.ZERO;
        
        if (produtosLancamento != null) {
            
            for (final ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
                
                if (produtoLancamento.isLancamentoAgrupado()) {
                    
                    continue;
                }
                
                if (produtoLancamento.getRepartePrevisto() != null) {
                    
                    expectativaReparteTotal = expectativaReparteTotal
                            .add(produtoLancamento.getRepartePrevisto());
                }
            }
        }
        
        return expectativaReparteTotal;
    }
    
	                                                                        /**
     * Método que efetua o balanceamento de acordo com os parâmetros informados.
     */
    private List<ProdutoLancamentoDTO> balancearProdutosLancamento(
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final List<ProdutoLancamentoDTO> produtosLancamentoBalanceaveis,
            final DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento,
            BigInteger expectativaReparteDataAtual, final Date dataLancamento,
            final boolean permiteForaDaData, final BigInteger capacidadeDistribuicao,
            final boolean permiteExcederCapacidadeDistribuicao,final BigInteger capacidadeMedia,
            final boolean permiteExcederCapacidadeMedia, final Long idFornecedor) {
        
        final Integer qtdDiasLimiteParaReprogLancamento = dadosBalanceamentoLancamento
                .getQtdDiasLimiteParaReprogLancamento();
        
        final List<ProdutoLancamentoDTO> produtosLancamentoBalanceados = new ArrayList<ProdutoLancamentoDTO>();
        
        final List<ProdutoLancamentoDTO> produtosLancamentoNaoBalanceados = new ArrayList<ProdutoLancamentoDTO>();
        
        // Utilizado para não possibilitar adicionar mais produtos que a media
        // do distribuidor no dia.
        long repartePrevisto = 0;
        
        if(matrizLancamento.get(dataLancamento)!=null){
            
            for (final ProdutoLancamentoDTO produtoLancamento : matrizLancamento.get(dataLancamento)) {
                
                if(produtoLancamento.getRepartePrevisto()!=null){
                    repartePrevisto = repartePrevisto + produtoLancamento.getRepartePrevisto().longValue();
                }
            }
        }
        
        
        for (final ProdutoLancamentoDTO produtoLancamento : produtosLancamentoBalanceaveis) {
            
            final boolean fornecedorCompativelParaDistribuicao = idFornecedor
                    .equals(produtoLancamento.getIdFornecedor());
            
            boolean excedeLimiteDataReprogramacao = this
                    .excedeLimiteDataReprogramacao(produtoLancamento,
                            qtdDiasLimiteParaReprogLancamento, dataLancamento);
            
            final boolean excedeCapacidadeDistribuidor = this
                    .excedeCapacidadeDistribuidor(expectativaReparteDataAtual,
                            produtoLancamento, capacidadeDistribuicao);
            
            this.existeLancamentoNaData(
                    matrizLancamento, produtoLancamento, dataLancamento);
            
            if (permiteForaDaData) {
                excedeLimiteDataReprogramacao = false;
            }
            
            if (fornecedorCompativelParaDistribuicao
                    && (permiteExcederCapacidadeDistribuicao || !excedeLimiteDataReprogramacao && !excedeCapacidadeDistribuidor)
                    && (permiteExcederCapacidadeMedia || repartePrevisto < capacidadeMedia.longValue())) {
                
                expectativaReparteDataAtual = expectativaReparteDataAtual
                        .add(produtoLancamento.getRepartePrevisto());
                
                this.adicionarProdutoLancamentoNaMatriz(matrizLancamento,
                        produtoLancamento, dataLancamento);
                
                produtosLancamentoBalanceados.add(produtoLancamento);
                
                
                repartePrevisto = repartePrevisto+produtoLancamento.getRepartePrevisto().longValue();
                
            } else {
                
                produtosLancamentoNaoBalanceados.add(produtoLancamento);
            }
        }
        
        produtosLancamentoBalanceaveis.removeAll(produtosLancamentoBalanceados);
        
        return produtosLancamentoNaoBalanceados;
    }
    
    private boolean existeLancamentoNaData(
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final ProdutoLancamentoDTO produtoLancamentoAdicionar, final Date dataLancamento) {
        
        final List<ProdutoLancamentoDTO> produtosLancamento = matrizLancamento
                .get(dataLancamento);
        
        if (produtosLancamento != null && !produtosLancamento.isEmpty()) {
            
            for (final ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
                
                if (!produtoLancamentoAdicionar.getIdLancamento().equals(
                        produtoLancamento.getIdLancamento())
                        && produtoLancamentoAdicionar.getIdProdutoEdicao()
                        .equals(produtoLancamento.getIdProdutoEdicao())
                        && dataLancamento.compareTo(produtoLancamento
                                .getNovaDataLancamento()) == 0) {
                    
                    return true;
                }
            }
        }
        
        return false;
    }
    
	                                                                        /**
     * Valida se a data de lançamento excede a data limite para reprogramação do
     * produto.
     */
    private boolean excedeLimiteDataReprogramacao(
            final ProdutoLancamentoDTO produtoLancamento,
            final Integer qtdDiasLimiteParaReprogLancamento, final Date dataLancamento) {
        
        final Date dataLimiteReprogramacao = calendarioService
                .subtrairDiasUteisComOperacao(
                        produtoLancamento.getDataRecolhimentoPrevista(),
                        qtdDiasLimiteParaReprogLancamento);
        
        return dataLancamento.compareTo(dataLimiteReprogramacao) == 1;
    }
    
	                                                                        /**
     * Valida se o produto informado excede a capacidade de distribuição no dia.
     */
    private boolean excedeCapacidadeDistribuidor(
            BigInteger expectativaReparteDataAtual,
            final ProdutoLancamentoDTO produtoLancamento,
            final BigInteger capacidadeDistribuicao) {
        
        expectativaReparteDataAtual = expectativaReparteDataAtual
                .add(produtoLancamento.getRepartePrevisto());
        
        return expectativaReparteDataAtual.compareTo(capacidadeDistribuicao) == 1;
    }
    
	                                                                        /**
    
	                                                                        /**
     * Processa os produtos que não puderam ser balanceados , pois estes
     * excederam a capacidade de distribuição.
     */
    
    /**
     * Ordena os produtos informados por periodicidade do produto e pela
     * expectativa de reparte.
     */
    @SuppressWarnings("unchecked")
    private void ordenarProdutosLancamentoPorPeriodicidadeExpectativaReparte(
            final List<ProdutoLancamentoDTO> produtosLancamento) {
        
        final ComparatorChain comparatorChain = new ComparatorChain();
        
        comparatorChain.addComparator(new BeanComparator(
                "ordemPeriodicidadeProduto"));
        comparatorChain.addComparator(new BeanComparator("repartePrevisto"),
                true);
        
        Collections.sort(produtosLancamento, comparatorChain);
    }
    
    /**
     * Ordena os produtos informados por periodicidade do produto e pela
     * expectativa de reparte.
     */
    @SuppressWarnings("unchecked")
    private void ordenarProdutosLancamentoPorNome(
            final List<ProdutoLancamentoDTO> produtosLancamento) {
        
        final ComparatorChain comparatorChain = new ComparatorChain();
        
        comparatorChain.addComparator(new BeanComparator("nomeProduto"));
        
        Collections.sort(produtosLancamento, comparatorChain);
    }
    

    /**
     * Adiciona o produto informado na matriz de balanceamento na data
     * informada.
     */
    private void adicionarProdutoLancamentoNaMatriz(
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final ProdutoLancamentoDTO produtoLancamento, final Date dataLancamento) {
        
        List<ProdutoLancamentoDTO> produtosLancamentoMatriz = matrizLancamento
                .get(dataLancamento);
        
        if (produtosLancamentoMatriz == null) {
            
            produtosLancamentoMatriz = new ArrayList<ProdutoLancamentoDTO>();
        }
        
        produtoLancamento.setNovaDataLancamento(dataLancamento);
        produtoLancamento.setDataLancamentoDistribuidor(dataLancamento);
        
        this.tratarAgrupamentoPorProdutoDataLcto(produtoLancamento,
                produtosLancamentoMatriz);
        
        produtosLancamentoMatriz.add(produtoLancamento);
        
        matrizLancamento.put(dataLancamento, produtosLancamentoMatriz);
    }
    
    @Override
    public void tratarAgrupamentoPorProdutoDataLcto(
            final ProdutoLancamentoDTO produtoLancamentoAdicionar,
            final List<ProdutoLancamentoDTO> produtosLancamento) {
        
        for (final ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
            
            if (produtoLancamentoAdicionar.isLancamentoAgrupado()) {
                
                continue;
            }
            
            if (!produtoLancamento.isLancamentoAgrupado()
                    && !produtoLancamentoAdicionar.getIdLancamento().equals(produtoLancamento.getIdLancamento())
                    && produtoLancamentoAdicionar.getIdProdutoEdicao().equals(produtoLancamento.getIdProdutoEdicao())
                    && produtoLancamentoAdicionar.getNovaDataLancamento().equals(produtoLancamento.getNovaDataLancamento())) {
                
                if (this.agruparPrimeiroLancamento(produtoLancamentoAdicionar, produtoLancamento)) {
                    
                    this.agruparProdutos(produtoLancamentoAdicionar, produtoLancamento);
                    
                } else {
                    
                    this.agruparProdutos(produtoLancamento, produtoLancamentoAdicionar);
                }
            }
        }
    }
    
	                                                                        /**
     * Verifica se o primeiro produto passado como parâmetro deve ser agrupado.
     * Primeiramente verifica pelo maior status de lançamento. Em seguida
     * verifica pelo id de lançamento maior.
     */
    private boolean agruparPrimeiroLancamento(
            final ProdutoLancamentoDTO produtoLancamentoAdicionar,
            final ProdutoLancamentoDTO produtoLancamento) {
        
        if (produtoLancamento.getStatus().ordinal() > produtoLancamentoAdicionar
                .getStatus().ordinal()) {
            
            return true;
            
        } else if (produtoLancamento.getStatus().ordinal() < produtoLancamentoAdicionar
                .getStatus().ordinal()) {
            
            return false;
            
        } else {
            
            return produtoLancamento.getIdLancamento().compareTo(produtoLancamentoAdicionar.getIdLancamento()) == 1;
            
        }
    }
    
    private void agruparProdutos(final ProdutoLancamentoDTO produtoLancamentoAgrupar,
            final ProdutoLancamentoDTO produtoLancamento) {
        
        final BigInteger repartePreviso = produtoLancamento.getRepartePrevisto().add(
                produtoLancamentoAgrupar.getRepartePrevisto());
        
        BigInteger reparteFisico = BigInteger.ZERO;
        BigInteger reparteFisicoAgrupar = BigInteger.ZERO;
        
        reparteFisico = produtoLancamento.getReparteFisico() != null ? produtoLancamento
                .getReparteFisico() : BigInteger.ZERO;
                
                reparteFisicoAgrupar = produtoLancamentoAgrupar.getReparteFisico() != null ? produtoLancamentoAgrupar
                        .getReparteFisico() : BigInteger.ZERO;
                        
                        final BigDecimal valorTotal = produtoLancamento.getValorTotal().add(
                                produtoLancamentoAgrupar.getValorTotal());
                        
                        produtoLancamento.setRepartePrevisto(repartePreviso);
                        produtoLancamento.setReparteFisico(reparteFisico
                                .add(reparteFisicoAgrupar));
                        produtoLancamento.setValorTotal(valorTotal);
                        
                        if (!produtoLancamentoAgrupar.getProdutosLancamentoAgrupados()
                                .isEmpty()) {
                            
                            produtoLancamento.getProdutosLancamentoAgrupados().addAll(
                                    produtoLancamentoAgrupar.getProdutosLancamentoAgrupados());
                        }
                        
                        produtoLancamento.getProdutosLancamentoAgrupados().add(
                                produtoLancamentoAgrupar);
                        
                        produtoLancamentoAgrupar.setLancamentoAgrupado(true);
    }
    
	                                                                        /**
     * Verifica se o produto é balanceável.
     */
    public boolean isProdutoBalanceavel(final ProdutoLancamentoDTO produtoLancamento,
            final Intervalo<Date> periodoDistribuicao,Date dataOperacaoDistribuidor) {
        
        final Date dataLancamentoDistribuidor = produtoLancamento.getDataLancamentoDistribuidor();
        final Date dataInicial = periodoDistribuicao.getDe();
        final Date dataFinal = periodoDistribuicao.getAte();
        
        final boolean isDataNoPeriodo = DateUtil.validarDataEntrePeriodo(dataLancamentoDistribuidor, dataInicial, dataFinal);
        
        if(produtoLancamento.getStatusLancamento().equals(StatusLancamento.EXPEDIDO.name())){
        	
        	return false;
        	
        }else if(produtoLancamento.getStatusLancamento().equals(StatusLancamento.FURO.name())
         	   && !dataLancamentoDistribuidor.before(dataOperacaoDistribuidor) && isDataNoPeriodo){
         	
         	return false;
        }else if(produtoLancamento.getStatusLancamento().equals(StatusLancamento.CONFIRMADO.name())
          	   && !dataLancamentoDistribuidor.before(dataOperacaoDistribuidor) && isDataNoPeriodo){
          	
          	return true;
  
        }else if(!produtoLancamento.getStatusLancamento().equals(StatusLancamento.EXPEDIDO.name())
        	   && !dataLancamentoDistribuidor.before(dataOperacaoDistribuidor) && isDataNoPeriodo){
        	
        	return false;
        }else {
        
        	return true;
        //return !produtoLancamento.getStatusLancamento().equals(StatusLancamento.EXPEDIDO.name())
            //&& !this.isProdutoConfirmado(produtoLancamento) && !produtoLancamento.isStatusLancamentoEmBalanceamento()
            //&& (!produtoLancamento.isStatusLancamentoFuro() || !isDataNoPeriodo);
        }
        
    }
    
    /**
     * Verifica se o produto foi confirmado.
     */
    @Override
    public boolean isProdutoConfirmado(final ProdutoLancamentoDTO produtoLancamento) {
        
        if (produtoLancamento.isStatusLancamentoBalanceado()
                || produtoLancamento.isStatusLancamentoExpedido()) {
            
            return true;
        }
        
        return false;
    }
    
    @Override
    @Transactional
    public boolean isDataConfirmada(final Date dataLancamento) {
        
        final List<ProdutoLancamentoDTO> listaverificadaConfirmada = lancamentoRepository
                .verificarDataConfirmada(dataLancamento);
        
        if (listaverificadaConfirmada.isEmpty()) {
            
            return false;
            
        } else {
            
            for (final ProdutoLancamentoDTO verificarConfirmado : listaverificadaConfirmada) {
                if (this.isProdutoConfirmado(verificarConfirmado)) {
                    
                    return true;
                    
                }
            }
            
            return false;
        }
    }
    
	                                                                        /**
     * Monta o DTO com as informações para realização do balanceamento.
     */
    private DadosBalanceamentoLancamentoDTO obterDadosLancamento(
            final FiltroLancamentoDTO filtro) {
        
        final Date dataLancamento = filtro.getData();
        
        final Intervalo<Date> periodoDistribuicao = this
                .getPeriodoDistribuicao(dataLancamento);
        
        final Map<Long, TreeSet<Date>> datasDistribuicaoPorFornecedor = this
                .obterDatasDistribuicaoFornecedor(periodoDistribuicao,
                        filtro.getIdsFornecedores());
        
        final List<ProdutoLancamentoDTO> produtosLancamento = lancamentoRepository
                .obterBalanceamentoLancamento(dataLancamento,periodoDistribuicao,
                        filtro.getIdsFornecedores());
        
        BigInteger media = BigInteger.ZERO;
        BigInteger diasRecolhimentoFornecedor = BigInteger.ZERO;
        
        for (final Map.Entry<Long, TreeSet<Date>> entry : datasDistribuicaoPorFornecedor
                .entrySet()) {
            diasRecolhimentoFornecedor = diasRecolhimentoFornecedor
                    .add(new BigInteger("" + entry.getValue().size()));
            break;
        }
        
        final Set<Date> datasExpectativaReparte = new LinkedHashSet<Date>();
        
        for (final ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
            media = media.add(produtoLancamento.getRepartePrevisto());
            datasExpectativaReparte.add(produtoLancamento.getDataLancamentoDistribuidor());
        }


        
        if (dadosBalanceamentoLancamento == null) {
            dadosBalanceamentoLancamento = new DadosBalanceamentoLancamentoDTO();
        }
        
        dadosBalanceamentoLancamento.setPeriodoDistribuicao(periodoDistribuicao);
        
       
        dadosBalanceamentoLancamento.setDatasDistribuicaoPorFornecedor(datasDistribuicaoPorFornecedor);
        
        dadosBalanceamentoLancamento.setCapacidadeDistribuicao(distribuidorRepository
                .capacidadeDistribuicao());
        
        if (media.compareTo(BigInteger.ZERO) != 0) {
            dadosBalanceamentoLancamento.setMediaDistribuicao(new BigInteger(""
                    + media.longValue()
                    / diasRecolhimentoFornecedor.longValue()));
        }
        
        dadosBalanceamentoLancamento.setProdutosLancamento(produtosLancamento);
        dadosBalanceamentoLancamento.setDatasExpectativaReparte(datasExpectativaReparte);
        dadosBalanceamentoLancamento.setQtdDiasLimiteParaReprogLancamento(distribuidorRepository.qtdDiasLimiteParaReprogLancamento());
        dadosBalanceamentoLancamento.setDataLancamento(dataLancamento);
        
        return dadosBalanceamentoLancamento;
    }
    
    
	                                                                        /**
     * Monta o perídodo da semana de distribuição referente à data informada.
     */
    private Intervalo<Date> getPeriodoDistribuicao(final Date dataLancamento) {
        
        final int codigoDiaSemana = distribuidorRepository.buscarInicioSemanaLancamento()
                .getCodigoDiaSemana();
        
        final Date dataInicialSemana = SemanaUtil.obterDataInicioSemana(
                codigoDiaSemana, dataLancamento);
        
        final Date dataFinalSemana = DateUtil.adicionarDias(dataInicialSemana, 6);
        
        final Intervalo<Date> periodo = new Intervalo<Date>(dataInicialSemana,
                dataFinalSemana);
        
        return periodo;
    }
    
	                                                                        /**
     * Obtém as datas de distribuição dos fornecedores informados.
     */
    private Map<Long, TreeSet<Date>> obterDatasDistribuicaoFornecedor(
            final Intervalo<Date> periodoDistribuicao, final List<Long> listaIdsFornecedores) {
        
        final List<DistribuicaoFornecedor> listaDistribuicaoFornecedor = distribuidorRepository
                .buscarDiasDistribuicaoFornecedor(listaIdsFornecedores,
                        OperacaoDistribuidor.DISTRIBUICAO);
        
        if (listaDistribuicaoFornecedor == null
                || listaDistribuicaoFornecedor.isEmpty()) {
            
            final List<String> mensagens = new ArrayList<String>();
            
            mensagens
.add("Dias de distribuição para os fornecedores não encontrados!");
            mensagens
.add("Cadastre dias de distruibuição para os Fornecedores:");
            for (final Long idFornecedor : listaIdsFornecedores) {
                mensagens.add(fornecedorRepository.obterNome(idFornecedor)
                        .toFormattedString());
            }
            mensagens
.add("Para continuar desmarque os fornecedores da lista e refaça sua pesquisa.");
            
            throw new ValidacaoException(TipoMensagem.WARNING, mensagens);
        }
        
        final Map<Long, Set<Integer>> codigosDiaSemanaPorFornecedor = new HashMap<Long, Set<Integer>>();
        
        for (final DistribuicaoFornecedor distribuicaoFornecedor : listaDistribuicaoFornecedor) {
            
            final Long idFornecedor = distribuicaoFornecedor.getFornecedor().getId();
            
            Set<Integer> codigosDiaSemana = codigosDiaSemanaPorFornecedor
                    .get(idFornecedor);
            
            if (codigosDiaSemana == null) {
                
                codigosDiaSemana = new TreeSet<>();
            }
            
            codigosDiaSemana.add(distribuicaoFornecedor.getDiaSemana()
                    .getCodigoDiaSemana());
            
            codigosDiaSemanaPorFornecedor.put(idFornecedor, codigosDiaSemana);
        }
        
        final Map<Long, TreeSet<Date>> datasDistribuicaoPorFornecedor = this
                .obterDatasDistribuicao(periodoDistribuicao,
                        codigosDiaSemanaPorFornecedor);
        
        return datasDistribuicaoPorFornecedor;
    }
    
	                                                                        /**
     * Obtém as datas para distribuição no período informado, de acordo com os
     * códigos dos dias da semana informados.
     */
    private Map<Long, TreeSet<Date>> obterDatasDistribuicao(
            final Intervalo<Date> periodoRecolhimento,
            final Map<Long, Set<Integer>> codigosDiaSemanaPorFornecedor) {
        
        final Map<Long, TreeSet<Date>> datasDistribuicaoComOperacao = new HashMap<Long, TreeSet<Date>>();
        
        for (final Map.Entry<Long, Set<Integer>> entry : codigosDiaSemanaPorFornecedor
                .entrySet()) {
            
            final Long idFornecedor = entry.getKey();
            
            final TreeSet<Date> datasDistribuicao = SemanaUtil
                    .obterPeriodoDeAcordoComDiasDaSemana(
                            periodoRecolhimento.getDe(),
                            periodoRecolhimento.getAte(), entry.getValue());
            
            TreeSet<Date> datasDistribuicaoDoFornecedor = datasDistribuicaoComOperacao
                    .get(idFornecedor);
            
            if (datasDistribuicaoDoFornecedor == null) {
                
                datasDistribuicaoDoFornecedor = new TreeSet<>();
            }
            
            for (final Date data : datasDistribuicao) {
                
                try {
                    
                    this.verificaDataOperacao(data,null,null,null);
                    
                    datasDistribuicaoDoFornecedor.add(data);
                    
                } catch (final ValidacaoException e) {
                    LOGGER.error(e.getMessage(), e);
                    continue;
                }
            }
            
            datasDistribuicaoComOperacao.put(idFornecedor,
                    datasDistribuicaoDoFornecedor);
        }
        
        return datasDistribuicaoComOperacao;
    }
    
    @Transactional
    public String verificaDataOperacao( Date data,Long idFornecedor, OperacaoDistribuidor operacaoDistribuidor,ProdutoLancamentoVO produtoLancamento) {
        
    	String msg ="";
        final Calendar cal = Calendar.getInstance();
        
        cal.setTime(data);
        
        //Comentado por solicitação do Genesio. trac185
        /*
        if (DateUtil.isSabadoDomingo(cal)) {
            
            throw new ValidacaoException(
                    TipoMensagem.WARNING,
                    "A data de lançamento deve ser uma data em que o distribuidor realiza operação!");
        }
        */
        
        if (calendarioService.isFeriadoSemOperacao(data)) {
            
            throw new ValidacaoException(
                    TipoMensagem.WARNING,
                    "A data de lançamento deve ser uma data em que o distribuidor realiza operação! (Feriado sem operação)");
        }
        
        if (calendarioService.isFeriadoMunicipalSemOperacao(data)) {
            
            throw new ValidacaoException(
                    TipoMensagem.WARNING,
                    "A data de lançamento deve ser uma data em que o distribuidor realiza operação! (Feriado Municipal sem oeperação)");
        }
        
        if(idFornecedor!=null && operacaoDistribuidor!=null && produtoLancamento!=null){
          if (!calendarioService.isDiaOperante(data, idFornecedor, operacaoDistribuidor)) {
            
        	msg= "Produto: "+produtoLancamento.getNomeProduto()+" da edição "+produtoLancamento.getNumeroEdicao()+ " no dia "+DateUtil.formatarDataPTBR(data)+" ("+operacaoDistribuidor+")"+"\n";
        			
            //throw new ValidacaoException(
                    //TipoMensagem.WARNING,
                    //"A data de lançamento ("+data+") deve ser uma data em que o distribuidor realiza operação!"+"("+operacaoDistribuidor+")");
          }
        }
        return msg;
    }
    
    @Override
    @Transactional
    public void validarDiaSemanaDistribuicaoFornecedores(final Date dataDistribuicao) {
        
        final List<DistribuicaoFornecedor> listaDistribuicaoFornecedores = distribuidorRepository
                .buscarDiasDistribuicaoFornecedor(OperacaoDistribuidor.DISTRIBUICAO);
        
        for (final DistribuicaoFornecedor distribuicaoFornecedor : listaDistribuicaoFornecedores) {
            
            final Integer codigosDiaSemanaFornecedor = distribuicaoFornecedor
                    .getDiaSemana().getCodigoDiaSemana();
            
            final Integer codigoDiaSemanaDistribuicao = SemanaUtil
                    .obterDiaDaSemana(dataDistribuicao);
            
            if (codigoDiaSemanaDistribuicao.equals(codigosDiaSemanaFornecedor)) {
                
                return;
            }
        }
        
        throw new ValidacaoException(TipoMensagem.WARNING,
                "A data de lançamento deve estar em um dia da semana "
                        + "que haja distribuição de algum fornecedor!");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ConfirmacaoVO> obterDatasConfirmacao(
            final BalanceamentoLancamentoDTO balanceamentoLancamento) {
        
        final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento = balanceamentoLancamento
                .getMatrizLancamento();
        
        final Set<Date> datasConfirmacao = balanceamentoLancamento
                .getMatrizLancamento().keySet();
        
        final Set<Date> datasExpedicaoConfirmada = balanceamentoLancamento
                .getDatasExpedicaoConfirmada();
        
        final Set<Date> datasConfirmadas = this.obterDatasConfirmadas(
                matrizLancamento, datasExpedicaoConfirmada);
        
        final Map<Date, Boolean> mapaDatasConfirmacaoOrdenada = new TreeMap<Date, Boolean>();
        
        for (final Date dataConfirmacao : datasConfirmacao) {
            mapaDatasConfirmacaoOrdenada.put(dataConfirmacao, false);
        }
        
        for (final Date dataConfirmada : datasConfirmadas) {
            mapaDatasConfirmacaoOrdenada.put(dataConfirmada, true);
        }
        
        final List<ConfirmacaoVO> confirmacoesVO = new ArrayList<ConfirmacaoVO>();
        
        final Set<Entry<Date, Boolean>> entrySet = mapaDatasConfirmacaoOrdenada
                .entrySet();
        
        for (final Entry<Date, Boolean> item : entrySet) {
            
            final boolean dataConfirmada = item.getValue();
            
            confirmacoesVO.add(new ConfirmacaoVO(DateUtil.formatarDataPTBR(item
                    .getKey()), dataConfirmada));
        }
        
        return confirmacoesVO;
    }
    
    @Override
    @Transactional
    public void voltarConfiguracaoInicial(final Date dataLancamento,
            final BalanceamentoLancamentoDTO balanceamentoLancamento, final Usuario usuario) {
        
        if (dataLancamento == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Data de lançamento não informada!");
        }
        
        final Intervalo<Date> periodoDistribuicao = this
                .getPeriodoDistribuicao(dataLancamento);
        
        this.voltarConfiguracaoInicialLancamentosPrevistos(periodoDistribuicao,
                usuario);
        
        this.voltarConfiguracaoInicialLancamentosDistribuidor(
                periodoDistribuicao, usuario);
    }
    
    private void voltarConfiguracaoInicialLancamentosPrevistos(
            final Intervalo<Date> periodoDistribuicao, final Usuario usuario) {
        
        final List<Lancamento> lancamentos = lancamentoRepository
                .obterLancamentosPrevistosPorPeriodo(periodoDistribuicao);
        
        for (final Lancamento lancamento : lancamentos) {
            
            lancamento.setDataLancamentoDistribuidor(lancamento
                    .getDataLancamentoPrevista());
            
            lancamento.setStatus(StatusLancamento.CONFIRMADO);
            lancamento.setSequenciaMatriz(null);
            lancamento.setUsuario(usuario);
            
            lancamentoRepository.merge(lancamento);
        }
    }
    
    private void voltarConfiguracaoInicialLancamentosDistribuidor(
            final Intervalo<Date> periodoDistribuicao, final Usuario usuario) {
        
        final List<Lancamento> lancamentos = lancamentoRepository
                .obterLancamentosDistribuidorPorPeriodo(periodoDistribuicao);
        
        for (final Lancamento lancamento : lancamentos) {
            
            lancamento.setStatus(StatusLancamento.CONFIRMADO);
            lancamento.setSequenciaMatriz(null);
            lancamento.setUsuario(usuario);
            
            lancamentoRepository.merge(lancamento);
        }
    }
    
	                                                                        /**
     * Enum para identificação da operação da Matriz de Lançamento.
     */
    private enum OperacaoMatrizLancamento {
        
        SALVAR, CONFIRMAR;
    }
    
    @Override
    @Transactional
    public void reabrirMatriz(final List<Date> datasConfirmadas, final Usuario usuario) {
        
        this.validarReaberturaMatriz(datasConfirmadas,
                distribuidorService.obterDataOperacaoDistribuidor());
        
        
        if (lancamentoRepository.existeMatrizLancamentosExpedidos(datasConfirmadas)) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Existem lançamentos que já foram expedidos. Não será possivel reabir a matriz nesse dia!");
            
        }
        
        final List<Lancamento> lancamentos = lancamentoRepository
                .obterMatrizLancamentosConfirmados(datasConfirmadas);
        
        for (final Lancamento lancamento : lancamentos) {
            
            this.validarLancamentoParaReabertura(lancamento);
            
            lancamento.setStatus(StatusLancamento.EM_BALANCEAMENTO);
            
            lancamento.setSequenciaMatriz(null);
            
            lancamento.setUsuario(usuario);
            
            lancamentoRepository.alterar(lancamento);
            
        }
    }
    
    private void validarLancamentoParaReabertura(final Lancamento lancamento) {
        
        if (!lancamento.getStatus().equals(StatusLancamento.BALANCEADO)) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Existem lançamentos que já se econtram em processo de lançamento!");
        }
        
    }
    
    private void validarReaberturaMatriz(final List<Date> datasConfirmadas,
            final Date dataOperacao) {
        
        final List<String> mensagens = new ArrayList<>();
        
        if (datasConfirmadas.isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Nenhuma data foi informada!");
        }
        
        for (final Date dataConfirmada : datasConfirmadas) {
            
            if (dataConfirmada.compareTo(dataOperacao) < 0) {
                
                final String dataFormatada = DateUtil
                        .formatarDataPTBR(dataConfirmada);
                
                mensagens.add("Para reabrir a matriz, a data (" + dataFormatada
                        + ") deve ser maior que a data de operação!");
            }
        }
        
        if (!mensagens.isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, mensagens);
        }
    }
    
}
