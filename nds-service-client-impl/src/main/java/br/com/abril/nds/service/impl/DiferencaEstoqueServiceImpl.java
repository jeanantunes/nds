package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ContasAPagarConsignadoVO;
import br.com.abril.nds.client.vo.DiferencaVO;
import br.com.abril.nds.client.vo.RateioCotaVO;
import br.com.abril.nds.client.vo.RelatorioLancamentoFaltasSobrasVO;
import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.DetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.ImpressaoDiferencaEstoqueDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.RateioDiferencaCotaDTO;
import br.com.abril.nds.dto.RateioDiferencaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.LancamentoDiferenca;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.LancamentoDiferencaRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.RateioDiferencaRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.TipoMovimentoFinanceiroService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.JasperUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.estoque.Diferenca}
 * 
 * @author Discover Technology
 * 
 */
@Service
public class DiferencaEstoqueServiceImpl implements DiferencaEstoqueService {
    
    @Autowired
    private DiferencaEstoqueRepository diferencaEstoqueRepository;
    
    @Autowired
    private LancamentoDiferencaRepository lancamentoDiferencaRepository;
    
    @Autowired
    private RateioDiferencaRepository rateioDiferencaRepository;
    
    @Autowired
    private TipoMovimentoEstoqueRepository tipoMovimentoRepository;
    
    @Autowired
    private EstudoCotaRepository estudoCotaRepository;
    
    @Autowired
    private ParametroSistemaRepository parametroSistemaRepository;
    
    @Autowired
    private RecebimentoFisicoRepository recebimentoFisicoRepository;
    
    @Autowired
    private CalendarioService calendarioService;
    
    @Autowired
    private MovimentoEstoqueService movimentoEstoqueService;
    
    @Autowired
    private MovimentoEstoqueCotaService movimentoEstoqueCotaService;
    
    @Autowired
    private CotaRepository cotaRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private DistribuidorService distribuidorService;

    
    @Autowired
    private MovimentoEstoqueRepository movimentoEstoqueRepository;
    
    @Autowired
    private ParametrosDistribuidorService parametrosDistribuidorService;
    
    @Autowired
    private LancamentoService lancamentoService;
    
    @Autowired
    private DebitoCreditoCotaService debitoCreditoCotaService;
    
    @Autowired
    private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
    
    @Autowired
    private TipoMovimentoFinanceiroService tipoMovimentoFinanceiroService;
    
    
    @Autowired
    private LancamentoRepository lancamentoRepository;
  
    
    @Override
    @Transactional(readOnly = true)
    public List<Diferenca> obterDiferencasLancamento(final FiltroLancamentoDiferencaEstoqueDTO filtro) {
        
        return diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long obterTotalDiferencasLancamento(final FiltroLancamentoDiferencaEstoqueDTO filtro) {
        
        return diferencaEstoqueRepository.obterTotalDiferencasLancamento(filtro);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Diferenca> obterDiferencas(final FiltroConsultaDiferencaEstoqueDTO filtro) {
        
        if(filtro.getNumeroCota() != null) {
            final Cota cota = cotaRepository.obterPorNumeroDaCota(filtro.getNumeroCota());
            filtro.setIdCota(cota.getId());
        }
        
        return diferencaEstoqueRepository.obterDiferencas(filtro);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long obterTotalDiferencas(final FiltroConsultaDiferencaEstoqueDTO filtro) {
        
        final Date dataInicialLancamento = calendarioService.subtrairDiasUteis(new Date(), 7);
        
        return diferencaEstoqueRepository.obterTotalDiferencas(filtro, dataInicialLancamento);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean verificarPossibilidadeExclusao(final Long idDiferenca){
        if (idDiferenca == null){
            throw new ValidacaoException(TipoMensagem.ERROR, "Id da Diferença é obrigatório.");
        }
        
        final Boolean diferenca = diferencaEstoqueRepository.buscarStatusDiferencaLancadaAutomaticamente(idDiferenca);
        
        return diferenca == null ? false : !diferenca;
    }
    
    @Override
    @Transactional
    public void lancarDiferencaAutomaticaContagemDevolucao(final Diferenca diferenca) {
        
        processarDiferenca(diferenca, TipoEstoque.DEVOLUCAO_ENCALHE, StatusConfirmacao.CONFIRMADO, true);
        
        final Usuario usuario = usuarioService.getUsuarioLogado();
        
        final Lancamento ultimoLancamento = this.obterUltimoLancamentoProduto(diferenca);
        
        this.gerarMovimentoEstoque(diferenca, usuario.getId(),
                        true, true, ultimoLancamento.getDataLancamentoDistribuidor(), null);
        
    }
    
    @Override
    @Transactional
    public Diferenca lancarDiferenca(Diferenca diferenca, final TipoEstoque tipoEstoque) {
        
        final boolean automatica = false;
        
        diferenca = processarDiferenca(diferenca, tipoEstoque,StatusConfirmacao.PENDENTE, automatica);
        
        return diferenca;
    }
    
    @Override
    @Transactional
    public Diferenca lancarDiferencaAutomatica(Diferenca diferenca,
            final TipoEstoque tipoEstoque) {
        
        final boolean automatica = true;
        
        diferenca = processarDiferenca(diferenca, tipoEstoque,StatusConfirmacao.PENDENTE, automatica);
        
        return diferenca;
    }
    
    @Override
    @Transactional
    public Diferenca lancarDiferencaAutomatica(Diferenca diferenca,
            final TipoEstoque tipoEstoque,
            final StatusAprovacao statusAprovacao,
            final Origem origem) {
        
        final boolean automatica = true;
        
        diferenca = processarDiferenca(diferenca, tipoEstoque, StatusConfirmacao.CONFIRMADO, automatica);
        
        this.confirmarLancamentosDiferenca(Arrays.asList(diferenca), statusAprovacao, true, origem);
        
        return diferenca;
    }
    
    private Diferenca processarDiferenca(final Diferenca diferenca, final TipoEstoque tipoEstoque, final StatusConfirmacao statusConfirmacao, final boolean automatica) {
        
        final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
        
        diferenca.setStatusConfirmacao(statusConfirmacao);
        diferenca.setTipoDirecionamento(TipoDirecionamentoDiferenca.ESTOQUE);
        diferenca.setAutomatica(automatica);
        diferenca.setDataMovimento(dataOperacao);
        diferenca.setTipoEstoque(tipoEstoque);
        
        return diferencaEstoqueRepository.merge(diferenca);
    }
    
    @Override
    @Transactional
    public void efetuarAlteracoes(Set<Diferenca> listaNovasDiferencas,
            final Map<Long, List<RateioCotaVO>> mapaRateioCotas,
            final FiltroLancamentoDiferencaEstoqueDTO filtroPesquisa,
            final Long idUsuario,final Boolean isDiferencaNova) {
        
        listaNovasDiferencas = salvarDiferenca(listaNovasDiferencas, mapaRateioCotas, idUsuario, isDiferencaNova, StatusConfirmacao.CONFIRMADO);
        
        final boolean isAprovacaoMovimentoDiferencaAutomatico = distribuidorService.utilizaControleAprovacaoFaltaSobra();
        
        final Origem origem = (mapaRateioCotas != null && !mapaRateioCotas.isEmpty()) ? Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_COTA : null;
        
        this.confirmarLancamentosDiferenca(new ArrayList<>(listaNovasDiferencas), null, !isAprovacaoMovimentoDiferencaAutomatico, origem);
    }
    
    @Override
    @Transactional
    public void salvarLancamentosDiferenca(final Set<Diferenca> listaNovasDiferencas,
            final Map<Long, List<RateioCotaVO>> mapaRateioCotas,
            final Long idUsuario,final Boolean isDiferencaNova){
        
        
        salvarDiferenca(listaNovasDiferencas, mapaRateioCotas, idUsuario,isDiferencaNova, StatusConfirmacao.PENDENTE);
    }
    
    @Override
    @Transactional
    public Diferenca atualizar(Diferenca diferenca) {
    	return this.diferencaEstoqueRepository.merge(diferenca);
    }
    
    private Set<Diferenca> salvarDiferenca(final Set<Diferenca> listaNovasDiferencas,
            final Map<Long, List<RateioCotaVO>> mapaRateioCotas,
            final Long idUsuario,
            final Boolean isDiferencaNova,
            final StatusConfirmacao statusConfirmacao) {
        
        final Usuario usuario = usuarioService.buscar(idUsuario);
        
        final Set<Diferenca> diferencasAtualizadas = new HashSet<>();
        
        for (final Diferenca diferenca : listaNovasDiferencas) {
            
            Diferenca diferencaASalvar = (Diferenca) SerializationUtils.clone(diferenca);
            
            final Long idDiferencaTemporario = diferencaASalvar.getId();
            
            if (isDiferencaNova != null && isDiferencaNova) {
                
                diferencaASalvar.setId(null);
                
                diferencaASalvar = diferencaEstoqueRepository.merge(diferencaASalvar);
            }
            
            List<RateioCotaVO> listaRateioCotas = null;
            
            if (mapaRateioCotas != null && !mapaRateioCotas.isEmpty()) {
                
                listaRateioCotas = mapaRateioCotas.remove(idDiferencaTemporario);
                
                mapaRateioCotas.put(diferencaASalvar.getId(), listaRateioCotas);
            }
            
            if (!TipoDirecionamentoDiferenca.ESTOQUE.equals(diferencaASalvar.getTipoDirecionamento())) {
                
                final List<RateioDiferenca> rateiosCota = this.processarRateioCotas(diferencaASalvar, mapaRateioCotas, idUsuario);
                
                if(rateiosCota!= null){
                    diferencaASalvar.setRateios(rateiosCota);
                }
                else{
                    diferencaASalvar.setRateios(rateioDiferencaRepository.obterRateiosPorDiferenca(diferencaASalvar.getId()));
                }
                diferencaASalvar.setExistemRateios(true);
                
            } else {
                
                rateioDiferencaRepository.removerRateioDiferencaPorDiferenca(diferencaASalvar.getId());
                diferencaASalvar.setExistemRateios(false);
            }
            
            diferencaASalvar.setStatusConfirmacao(statusConfirmacao);
            
            diferencaASalvar.setResponsavel(usuario);
            
            diferencaASalvar = diferencaEstoqueRepository.merge(diferencaASalvar);
            
            diferencasAtualizadas.add(diferencaASalvar);
        }
        
        return diferencasAtualizadas;
    }
    
    private void direcionarItensEstoque(final Diferenca diferenca,
            final BigInteger qntDiferenca,
            final boolean validarTransfEstoqueDiferenca,
            final Date dataLancamento) {
        
        Diferenca diferencaRedirecionada = this.redirecionarDiferencaEstoque(qntDiferenca, diferenca);
        
        final StatusAprovacao statusAprovacao = obterStatusLancamento(diferencaRedirecionada);
        
        final MovimentoEstoque movimentoEstoque =
                this.gerarMovimentoEstoque(
                        diferencaRedirecionada, diferencaRedirecionada.getResponsavel().getId(),
                        diferencaRedirecionada.isAutomatica(),
                        validarTransfEstoqueDiferenca, dataLancamento, null);
        
        final LancamentoDiferenca lancamentoDiferenca =
                this.gerarLancamentoDiferenca(statusAprovacao, movimentoEstoque, null);
        
        diferencaRedirecionada.setLancamentoDiferenca(lancamentoDiferenca);
        
        diferencaRedirecionada = diferencaEstoqueRepository.merge(diferencaRedirecionada);
        
        this.processarTransferenciaEstoque(diferencaRedirecionada,diferencaRedirecionada.getResponsavel().getId(), null);
    }
    
    private Diferenca redirecionarDiferencaEstoque(final BigInteger qntDiferenca,final Diferenca diferenca){
        
        final Diferenca diferencaEstoque = new Diferenca();
        
        diferencaEstoque.setId(null);
        diferencaEstoque.setAutomatica(diferenca.isAutomatica());
        diferencaEstoque.setDataMovimento(distribuidorService.obterDataOperacaoDistribuidor());
        diferencaEstoque.setProdutoEdicao(diferenca.getProdutoEdicao());
        diferencaEstoque.setResponsavel(diferenca.getResponsavel());
        diferencaEstoque.setTipoDiferenca(diferenca.getTipoDiferenca());
        diferencaEstoque.setTipoEstoque(diferenca.getTipoEstoque());
        diferencaEstoque.setItemRecebimentoFisico(diferenca.getItemRecebimentoFisico());
        diferencaEstoque.setStatusConfirmacao(diferenca.getStatusConfirmacao());
        diferencaEstoque.setTipoDirecionamento(TipoDirecionamentoDiferenca.ESTOQUE);
        diferencaEstoque.setQtde(qntDiferenca);
        
        return diferencaEstoque;
    }
    
    @Override
    @Transactional
    public void cancelarDiferencas(final FiltroLancamentoDiferencaEstoqueDTO filtroPesquisa,
            final List<Long> idsDiferencasSelecionadas,
            final Long idUsuario) {
        
        final Usuario usuario = usuarioService.buscar(idUsuario);
        
        filtroPesquisa.setPaginacao(null);
        filtroPesquisa.setOrdenacaoColuna(null);
        
        final List<Diferenca> listaDiferencas =
                diferencaEstoqueRepository.obterDiferencasLancamento(filtroPesquisa);
        
        for (final Diferenca diferenca : listaDiferencas) {
            
            if (idsDiferencasSelecionadas != null
                    && !idsDiferencasSelecionadas.isEmpty()) {
                
                if (!idsDiferencasSelecionadas.contains(diferenca.getId())) {
                    
                    continue;
                }
            }
            
            diferenca.setStatusConfirmacao(StatusConfirmacao.CANCELADO);
            diferenca.setResponsavel(usuario);
            
            diferencaEstoqueRepository.merge(diferenca);
        }
    }
    
    private Cota obterCotaDaDiferenca(final Diferenca diferenca){
        
        if (diferenca.getRateios() == null) {
            
            return null;
        }
        
        for (final RateioDiferenca rd : diferenca.getRateios()){
            
            if (rd.getCota() != null) {
                
                return rd.getCota();
            }
        }
        
        return null;
    }
    
    private void confirmarLancamentosDiferenca(final List<Diferenca> listaDiferencas, StatusAprovacao statusAprovacao, final boolean isMovimentoDiferencaAutomatico, final Origem origem) {
        
        final Usuario usuario = usuarioService.getUsuarioLogado();
        
        for (Diferenca diferenca : listaDiferencas) {
            
            final Lancamento ultimoLancamento = this.obterUltimoLancamentoProduto(diferenca);
            
            final boolean produtoRecolhido = this.verificarRecolhimentoProdutoEdicao(ultimoLancamento, diferenca.getDataMovimento());
            
            diferenca.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
            
            List<MovimentoEstoqueCota> listaMovimentosEstoqueCota = null;
            MovimentoEstoque movimentoEstoque = null;
            
            final boolean validarTransfEstoqueDiferenca = TipoEstoque.LANCAMENTO.equals(diferenca.getTipoEstoque());
            
            if (diferenca.getRateios() != null && !diferenca.getRateios().isEmpty()) {
                
                listaMovimentosEstoqueCota = new ArrayList<MovimentoEstoqueCota>();
                
                BigInteger qntTotalRateio = BigInteger.ZERO;
                
                for (final RateioDiferenca rateioDiferenca : diferenca.getRateios()) {
                    
                    qntTotalRateio = qntTotalRateio.add(rateioDiferenca.getQtde());
                    
                    final MovimentoEstoqueCota movimentoEstoqueCota =
                            this.gerarMovimentoEstoqueCota(diferenca, rateioDiferenca,
                                    usuario.getId(), isMovimentoDiferencaAutomatico,
                                    ultimoLancamento.getDataLancamentoDistribuidor());
                    
                    if (produtoRecolhido) {
                        
                        this.lancarDebitoCreditoCota(diferenca, rateioDiferenca, movimentoEstoqueCota, usuario);
                    }
                    
                    listaMovimentosEstoqueCota.add(movimentoEstoqueCota);
                }
                
                if (diferenca.getTipoDiferenca().isSobra()  
                		|| diferenca.getTipoDiferenca().isFaltaParaCota()
                        || diferenca.getTipoDiferenca().isAlteracaoReparte()) {
                    
                    movimentoEstoque = this.gerarMovimentoEstoque(
                            diferenca, diferenca.getResponsavel().getId(), diferenca.isAutomatica(),
                            validarTransfEstoqueDiferenca,
                            ultimoLancamento.getDataLancamentoDistribuidor(), origem);
                
                }

                //Verifica se ha direcionamento de produtos para o estoque do distribuidor
                if (diferenca.getQtde().compareTo(qntTotalRateio) > 0) {
                    
                    this.direcionarItensEstoque(
                            diferenca, diferenca.getQtde().subtract(qntTotalRateio),
                            validarTransfEstoqueDiferenca, ultimoLancamento.getDataLancamentoDistribuidor());
                    
                    diferenca.setQtde(qntTotalRateio);
                }
            } else {
                
                movimentoEstoque =
                        this.gerarMovimentoEstoque(diferenca, usuario.getId(),
                                isMovimentoDiferencaAutomatico, validarTransfEstoqueDiferenca,
                                ultimoLancamento.getDataLancamentoDistribuidor(), origem);
            }
            
            if(!diferenca.getProdutoEdicao().getProduto().getOrigem().equals(Origem.MANUAL)) {
	            if (statusAprovacao == null) {
	                
	                statusAprovacao = obterStatusLancamento(diferenca);
	            }
            } else {
            	
            	
            	if (diferenca.getTipoDiferenca().isAlteracaoReparte()) {
                    
                    statusAprovacao = StatusAprovacao.APROVADO;
                    
            	} else if (diferenca.getTipoDiferenca().isFalta() || diferenca.getTipoDiferenca().isFaltaParaCota()) {
            	
            		statusAprovacao = StatusAprovacao.PERDA;
            				
            	} else {
                    
                    statusAprovacao = StatusAprovacao.GANHO;
                }
            	
            }
            
            if(listaMovimentosEstoqueCota != null && !listaMovimentosEstoqueCota.isEmpty()) {
	            for(MovimentoEstoqueCota mec : listaMovimentosEstoqueCota) {
	            	
	            	if(!mec.getProdutoEdicao().getProduto().getOrigem().equals(Origem.MANUAL)) {
	            	
		        		if (!diferenca.getTipoDiferenca().isAlteracaoReparte() && this.foraDoPrazoDoGFS(diferenca)) {
		                    
		        			if(origem != null && origem.equals(Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_FECHAMENTO_ENCALHE)) {
		        				mec.setStatusIntegracao(StatusIntegracao.ENCALHE);
		                    } else {
		                    	mec.setStatusIntegracao(StatusIntegracao.FORA_DO_PRAZO);
		                    }
		                    
		                }
	            	} else {
	            		
	            		mec.setStatusIntegracao(StatusIntegracao.NAO_INTEGRAR);
	            		
	            		final boolean utilizaControleAprovacao = parametrosDistribuidorService.getParametrosDistribuidor().getUtilizaControleAprovacao();
	            		
	            		if(utilizaControleAprovacao) {
	            			
	            			statusAprovacao = StatusAprovacao.PENDENTE;
	            			
	                    } else {
	                    	
	                    	if (diferenca.getTipoDiferenca().isAlteracaoReparte()) {
	                            
	                            statusAprovacao = StatusAprovacao.APROVADO;
	                            
	                    	} else if (diferenca.getTipoDiferenca().isFalta() || diferenca.getTipoDiferenca().isFaltaParaCota()) {
	                    	
	                    		statusAprovacao = StatusAprovacao.PERDA;
	                    				
	                    	} else {
	                            
	                            statusAprovacao = StatusAprovacao.GANHO;
	                        }
	                    	
	                    }
	            		
	            	}
	            }
            }
            
            final LancamentoDiferenca lancamentoDiferenca = this.gerarLancamentoDiferenca(statusAprovacao, movimentoEstoque, listaMovimentosEstoqueCota);
            
            diferenca.setLancamentoDiferenca(lancamentoDiferenca);
            
            //TODO: Verificar com negocio a obrigatoriedade de ter Recebimento Fisico para lancar Diferenca
            ItemRecebimentoFisico itemRecebFisico = null;
            final List<ItemRecebimentoFisico> itensRecebFisico = recebimentoFisicoRepository.obterItensRecebimentoFisicoDoProduto(diferenca.getProdutoEdicao().getId());
            if(itensRecebFisico != null && !itensRecebFisico.isEmpty()) {
                itemRecebFisico = itensRecebFisico.get(0);
                diferenca.setItemRecebimentoFisico(itemRecebFisico);
            }
            
            diferenca = diferencaEstoqueRepository.merge(diferenca);
            
            this.processarTransferenciaEstoque(diferenca, usuario.getId(), origem);
            
            diferencaEstoqueRepository.flush();
        }
    }
    
    private Boolean foraDoPrazoDoGFS(final Diferenca diferenca) {
        
        return !this.validarDataLancamentoDiferenca(
                diferenca.getDataMovimento(), diferenca.getProdutoEdicao().getId(),
                diferenca.getTipoDiferenca());
    }
    
    private Lancamento obterUltimoLancamentoProduto(final Diferenca diferenca) {
        
        Lancamento ultimoLancamento = null;
        
        final Cota cota = this.obterCotaDaDiferenca(diferenca);
        
        if (cota != null) {
            
            ultimoLancamento = lancamentoService.obterUltimoLancamentoDaEdicaoParaCota(diferenca.getProdutoEdicao().getId(), cota.getId());
            
            if(ultimoLancamento == null) {
            	ultimoLancamento = lancamentoService.obterUltimoLancamentoDaEdicao(diferenca.getProdutoEdicao().getId());
            }
            
        } else {
            
            ultimoLancamento =
                    lancamentoService.obterUltimoLancamentoDaEdicao(
                            diferenca.getProdutoEdicao().getId());
        }
        
        return ultimoLancamento;
    }
    
    private boolean verificarRecolhimentoProdutoEdicao(final Lancamento lancamento, final Date data) {
        
        if (lancamento == null || data == null) {
            
            return false;
        }
        
        return (lancamento.getDataRecolhimentoDistribuidor().compareTo(data) < 0);
    }
    
    private void lancarDebitoCreditoCota(final Diferenca diferenca,
            final RateioDiferenca rateioDiferenca,
            final MovimentoEstoqueCota movimentoEstoqueCota,
            final Usuario usuario) {
        
        final DebitoCreditoDTO debitoCredito = new DebitoCreditoDTO();
        
        debitoCredito.setDataVencimento(
                DateUtil.formatarDataPTBR(
                        calendarioService.adicionarDiasUteis(diferenca.getDataMovimento(), 1)));
        
        final Cota cota = rateioDiferenca.getCota();
        
        debitoCredito.setNomeCota(cota.getPessoa().getNome());
        debitoCredito.setNumeroCota(cota.getNumeroCota());
        
        debitoCredito.setTipoMovimentoFinanceiro(
                this.obterTipoMovimentoFinanceiro(diferenca.getTipoDiferenca()));
        
        //null
        final BigDecimal valor =
                movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto()
                .multiply(new BigDecimal(rateioDiferenca.getQtde()));
        
        debitoCredito.setValor(valor.toString());
        
        debitoCredito.setIdUsuario(usuario.getId());
        
        final MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO =
                debitoCreditoCotaService.gerarMovimentoFinanceiroCotaDTO(debitoCredito);
        
        movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
        
        movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(
                movimentoFinanceiroCotaDTO);
    }
    
    private TipoMovimentoFinanceiro obterTipoMovimentoFinanceiro(final TipoDiferenca tipoDiferenca) {
        
        GrupoMovimentoFinaceiro grupoMovimentoFinaceiro = null;
        OperacaoFinaceira operacaoFinaceira = null;
        
        if (tipoDiferenca.isFalta() || tipoDiferenca.isAlteracaoReparte()) {
            
            grupoMovimentoFinaceiro = GrupoMovimentoFinaceiro.CREDITO;
            operacaoFinaceira = OperacaoFinaceira.CREDITO;
            
        } else {
            
            grupoMovimentoFinaceiro = GrupoMovimentoFinaceiro.DEBITO;
            operacaoFinaceira = OperacaoFinaceira.DEBITO;
        }
        
        return
                tipoMovimentoFinanceiroService
                .obterTipoMovimentoFincanceiroPorGrupoFinanceiroEOperacaoFinanceira(
                        grupoMovimentoFinaceiro, operacaoFinaceira);
    }
    
    private void processarTransferenciaEstoque(final Diferenca diferenca, final Long idUsuario, final Origem origem) {
        
        if (TipoEstoque.LANCAMENTO.equals(diferenca.getTipoEstoque())) {
            
            return;
        }
        
        if (diferenca.getRateios() != null && !diferenca.getRateios().isEmpty()) {
            
            return;
        }
        
        if (TipoEstoque.RECOLHIMENTO.equals(diferenca.getTipoEstoque())) {
            
            return;
        }
        
        
        if (TipoEstoque.GANHO.equals(diferenca.getTipoEstoque())) {
            
            return;
        }
        
        if (TipoEstoque.PERDA.equals(diferenca.getTipoEstoque())) {
            
            return;
        }
        
        TipoMovimentoEstoque tipoMovimentoEstoqueLancamento = null;
        
        TipoMovimentoEstoque tipoMovimentoEstoqueAlvo = null;
        
        if (OperacaoEstoque.SAIDA.equals(
                diferenca.getTipoDiferenca().getTipoMovimentoEstoque().getOperacaoEstoque())) {
            
            tipoMovimentoEstoqueLancamento =
                    tipoMovimentoRepository.buscarTipoMovimentoEstoque(
                            obterTipoMovimentoEstoqueTransferencia(
                                    TipoEstoque.LANCAMENTO, OperacaoEstoque.ENTRADA));
            
            tratarTipoMovimentoEstoque(tipoMovimentoEstoqueLancamento, "Tipo de movimento de entrada não encontrado!");
            
            tipoMovimentoEstoqueAlvo =
                    tipoMovimentoRepository.buscarTipoMovimentoEstoque(
                            obterTipoMovimentoEstoqueTransferencia(
                                    diferenca.getTipoEstoque(), OperacaoEstoque.SAIDA));
            
            tratarTipoMovimentoEstoque(tipoMovimentoEstoqueAlvo, "Tipo de movimento de saída não encontrado!");
            
        } else
            if (OperacaoEstoque.ENTRADA.equals(
                    diferenca.getTipoDiferenca().getTipoMovimentoEstoque().getOperacaoEstoque())) {
                
                tipoMovimentoEstoqueLancamento =
                        tipoMovimentoRepository.buscarTipoMovimentoEstoque(
                                obterTipoMovimentoEstoqueTransferencia(
                                        TipoEstoque.LANCAMENTO, OperacaoEstoque.SAIDA));
                
                tratarTipoMovimentoEstoque(tipoMovimentoEstoqueLancamento, "Tipo de movimento de saída não encontrado!");
                
                tipoMovimentoEstoqueAlvo =
                        tipoMovimentoRepository.buscarTipoMovimentoEstoque(
                                obterTipoMovimentoEstoqueTransferencia(
                                        diferenca.getTipoEstoque(), OperacaoEstoque.ENTRADA));
                
                tratarTipoMovimentoEstoque(tipoMovimentoEstoqueAlvo, "Tipo de movimento de entrada não encontrado!");
            }
        
        movimentoEstoqueService.gerarMovimentoEstoque(
                diferenca.getProdutoEdicao().getId(), idUsuario, diferenca.getQtde(),
                tipoMovimentoEstoqueLancamento, origem);
        
        movimentoEstoqueService.gerarMovimentoEstoque(
                diferenca.getProdutoEdicao().getId(), idUsuario, diferenca.getQtde(),
                tipoMovimentoEstoqueAlvo, origem);
    }
    
    private void tratarTipoMovimentoEstoque(final TipoMovimentoEstoque tipoMovimento,final String mensagem){
        
        if (tipoMovimento == null) {
            throw new ValidacaoException(TipoMensagem.WARNING,mensagem);
        }
    }
    
    private GrupoMovimentoEstoque obterTipoMovimentoEstoqueTransferencia(final TipoEstoque tipoEstoque, final OperacaoEstoque operacaoEstoque){
        
        if (tipoEstoque == null) {
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "Erro para obter tipo de movimento estoque para lançamento de faltas e sobras!");
        }
        switch(tipoEstoque) {
        
        case LANCAMENTO:
            return isOperacaoEntrada(operacaoEstoque)
                    ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO
                            :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO;
        case SUPLEMENTAR:
            return isOperacaoEntrada(operacaoEstoque)
                    ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR
                            :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR;
        case RECOLHIMENTO:
            return isOperacaoEntrada(operacaoEstoque)
                    ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO
                            :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_RECOLHIMENTO;
        case PRODUTOS_DANIFICADOS:
            return isOperacaoEntrada(operacaoEstoque)
                    ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS
                            :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS;
            
        case DEVOLUCAO_FORNECEDOR:
            return isOperacaoEntrada(operacaoEstoque)
                    ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_FORNECEDOR
                            :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_FORNECEDOR;
            
        case DEVOLUCAO_ENCALHE:
            return isOperacaoEntrada(operacaoEstoque)
                    ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_ENCALHE
                            :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_ENCALHE;
            
        case GANHO:
            return GrupoMovimentoEstoque.GANHO_EM;
            
        case PERDA:
            return GrupoMovimentoEstoque.PERDA_EM;
		default:

	        return null;            
        }        
    }
    
    private boolean isOperacaoEntrada(final OperacaoEstoque operacaoEstoque){
        return (OperacaoEstoque.ENTRADA.equals(operacaoEstoque));
    }
    
    private StatusAprovacao obterStatusLancamento(final Diferenca diferenca) {
        
        final boolean utilizaControleAprovacao = parametrosDistribuidorService.getParametrosDistribuidor().getUtilizaControleAprovacao();
        
        StatusAprovacao statusAprovacao = null;
        if(!utilizaControleAprovacao) {
            statusAprovacao = StatusAprovacao.APROVADO;
        } else {
            statusAprovacao = StatusAprovacao.PENDENTE;
        }
        
        if (this.foraDoPrazoDoGFS(diferenca)) {
            
            if (diferenca.getTipoDiferenca().isFalta()) {
                
                statusAprovacao = StatusAprovacao.PERDA;
                
            } else {
                
                statusAprovacao = StatusAprovacao.GANHO;
            }
        }
        
        return statusAprovacao;
    }
    
    private List<RateioDiferenca> processarRateioCotas(final Diferenca diferenca,
            final Map<Long, List<RateioCotaVO>> mapaRateioCotas,
            final Long idUsuario) {
        
        if (mapaRateioCotas == null || mapaRateioCotas.isEmpty()) {
            
            return null;
        }
        
        final List<RateioCotaVO> listaRateioCotaVO = mapaRateioCotas.get(diferenca.getId());
        
        if (listaRateioCotaVO == null || listaRateioCotaVO.isEmpty()) {
            
            return null;
        }
        
        final List<Long> rateiosAssociadosDiferenca = new ArrayList<Long>();
        
        final List<RateioDiferenca> rateiosProcessados = new ArrayList<>();
        
        for (final RateioCotaVO rateioCotaVO : listaRateioCotaVO) {
            
            rateioCotaVO.setDataMovimento(diferenca.getDataMovimento());
            
            RateioDiferenca rateioDiferenca = null;
            
            if(rateioCotaVO.getIdRateio()!= null){
                
                rateioDiferenca = rateioDiferencaRepository.buscarPorId(rateioCotaVO.getIdRateio());
            }
            
            if(rateioDiferenca == null){
                
                rateioDiferenca = new RateioDiferenca();
                
                final EstudoCota estudoCota =
                        estudoCotaRepository.obterEstudoCotaDeLancamentoComEstudoFechado(
                                distribuidorService.obterDataOperacaoDistribuidor(),
                                diferenca.getProdutoEdicao().getId(),
                                rateioCotaVO.getNumeroCota());
                
                rateioDiferenca.setEstudoCota(estudoCota);
                
                rateioDiferenca.setDiferenca(diferenca);
            }
            
            final Cota cota = cotaRepository.obterPorNumeroDaCota(rateioCotaVO.getNumeroCota());
            
            rateioDiferenca.setCota(cota);
            
            rateioDiferenca.setQtde(rateioCotaVO.getQuantidade());
            
            rateioDiferenca.setDataNotaEnvio(rateioCotaVO.getDataEnvioNota());
            
            final Lancamento ultimoLancamento =
                    lancamentoService.obterUltimoLancamentoDaEdicao(
                            diferenca.getProdutoEdicao().getId());
            
            rateioDiferenca.setDataMovimento(ultimoLancamento.getDataLancamentoDistribuidor());
            
            rateioDiferenca = rateioDiferencaRepository.merge(rateioDiferenca);
            
            rateiosAssociadosDiferenca.add(rateioDiferenca.getId());
            
            rateiosProcessados.add(rateioDiferenca);
        }
        
        if(!rateiosAssociadosDiferenca.isEmpty()){
            
            rateioDiferencaRepository.removerRateiosNaoAssociadosDiferenca(diferenca.getId(), rateiosAssociadosDiferenca);
        }
        
        return rateiosProcessados;
    }
    
    /**
     * Método que obtém a data de recebimento de uma nota fiscal de entrada
     * cujo recebimento físico tenha sído confirmado.
     * 
     * @param recebimentoFisico
     * 
     * @return Date
     */
    private Date obterDataRecebimentoNota(RecebimentoFisico recebimentoFisico) {
    	
    	if(recebimentoFisico != null && recebimentoFisico.getDataConfirmacao()!=null) {
    		
    		NotaFiscalEntrada notaFiscalEntrada = recebimentoFisico.getNotaFiscal();
    		
    		if(notaFiscalEntrada!=null && notaFiscalEntrada.getDataRecebimento()!=null) {
    			
    			return DateUtil.removerTimestamp(notaFiscalEntrada.getDataRecebimento());
    			
    		}
    		
    	}
    	
    	return DateUtil.removerTimestamp(Calendar.getInstance().getTime());
    	
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validarDataLancamentoDiferenca(final Date dataLancamentoDiferenca,
            final Long idProdutoEdicao,
            final TipoDiferenca tipoDiferenca) {
        
        final List<ItemRecebimentoFisico> listaItensRecebimentoFisico =
                recebimentoFisicoRepository.obterItensRecebimentoFisicoDoProduto(idProdutoEdicao);
        
        final ParametroSistema parametroNumeroDiasPermitidoLancamento =
                this.obterParametroNumeroDiasPermissaoLancamentoDiferenca(tipoDiferenca);
        
        Integer numeroDiasPermitidoLancamento = 0;
        
        if (parametroNumeroDiasPermitidoLancamento != null) {
            
            numeroDiasPermitidoLancamento =
                    Integer.parseInt(parametroNumeroDiasPermitidoLancamento.getValor());
        }
        
        for (final ItemRecebimentoFisico itemRecebimentoFisico : listaItensRecebimentoFisico) {
            
            if (itemRecebimentoFisico.getRecebimentoFisico().getDataConfirmacao() == null) {
                
                itemRecebimentoFisico.getRecebimentoFisico().setDataConfirmacao(Calendar.getInstance().getTime());
            }
            

          final Date dataRecebimentoNota = obterDataRecebimentoNota(itemRecebimentoFisico.getRecebimentoFisico());
          
          final long diferencaDias = DateUtil.obterDiferencaDias(
        		  dataRecebimentoNota, dataLancamentoDiferenca);
            
            if (diferencaDias == numeroDiasPermitidoLancamento
                    || diferencaDias < numeroDiasPermitidoLancamento) {
                
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Diferenca obterDiferenca(final Long id) {
        
        return diferencaEstoqueRepository.buscarPorId(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RateioCotaVO> obterRateiosCotaPorIdDiferenca(final Long idDiferenca){
        
        final List<RateioCotaVO> listaRetorno = new ArrayList<RateioCotaVO>();
        
        final Diferenca diferenca = obterDiferenca(idDiferenca);
        
        if(diferenca!= null && diferenca.getRateios()!= null){
            
            for(final RateioDiferenca rateio : diferenca.getRateios()){
                
                final RateioCotaVO rateioVO = new RateioCotaVO();
                
                rateioVO.setIdRateio(rateio.getId());
                rateioVO.setIdDiferenca(rateio.getDiferenca().getId());
                rateioVO.setNomeCota(rateio.getCota().getPessoa().getNome());
                rateioVO.setNumeroCota(rateio.getCota().getNumeroCota());
                rateioVO.setQuantidade(rateio.getQtde());
                rateioVO.setDataEnvioNota(rateio.getDataNotaEnvio());
                
                final Long reparteCota =
                        movimentoEstoqueCotaService.obterQuantidadeReparteProdutoCota
                        (rateio.getDiferenca().getProdutoEdicao().getId(), rateio.getCota().getNumeroCota());
                
                rateioVO.setReparteCota(new BigInteger(reparteCota.toString()));
                
                listaRetorno.add(rateioVO);
            }
        }
        
        return listaRetorno;
    }
    
	                                    /*
     * Obtém o parâmetro de número de dias de permissão para lançamento de uma
     * diferença.
     * 
     * @param tipoDiferenca - tipo de diferença
     * 
     * @return {@link ParametroSistema}
     */
    private ParametroSistema obterParametroNumeroDiasPermissaoLancamentoDiferenca(final TipoDiferenca tipoDiferenca) {
        
        ParametroSistema parametroNumeroDiasLancamento;
        
        switch (tipoDiferenca)  {
        
        case FALTA_DE:
            
            parametroNumeroDiasLancamento =
            parametroSistemaRepository.buscarParametroPorTipoParametro(
                    TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_FALTA_DE);
            
            break;
            
        case FALTA_EM:
            
            parametroNumeroDiasLancamento =
            parametroSistemaRepository.buscarParametroPorTipoParametro(
                    TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_FALTA_EM);
            
            break;
        
        case FALTA_EM_DIRECIONADA_COTA:
            
            parametroNumeroDiasLancamento =
            parametroSistemaRepository.buscarParametroPorTipoParametro(
                    TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_FALTA_EM);
            
            break;
            
        case SOBRA_DE:
            
            parametroNumeroDiasLancamento =
            parametroSistemaRepository.buscarParametroPorTipoParametro(
                    TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_SOBRA_DE);
            
            break;
            
        case SOBRA_EM:
            
            parametroNumeroDiasLancamento =
            parametroSistemaRepository.buscarParametroPorTipoParametro(
                    TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_SOBRA_EM);
            
            break;
            
        case SOBRA_DE_DIRECIONADA_COTA:
            
            parametroNumeroDiasLancamento =
            parametroSistemaRepository.buscarParametroPorTipoParametro(
                    TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_SOBRA_DE);
            
            break;
            
        case SOBRA_EM_DIRECIONADA_COTA:
            
            parametroNumeroDiasLancamento =
            parametroSistemaRepository.buscarParametroPorTipoParametro(
                    TipoParametroSistema.NUMERO_DIAS_PERMITIDO_LANCAMENTO_SOBRA_EM);
            
            break;
            
        default:
            
            parametroNumeroDiasLancamento = null;
        }
        
        return parametroNumeroDiasLancamento;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public DetalheDiferencaCotaDTO obterDetalhesDiferencaCota(final FiltroDetalheDiferencaCotaDTO filtro) {
        
        final List<RateioDiferencaCotaDTO> detalhesDiferenca = rateioDiferencaRepository.obterRateioDiferencaCota(filtro);
        
        DetalheDiferencaCotaDTO detalheDiferencaCota = rateioDiferencaRepository.obterDetalhesDiferencaCota(filtro);
        
        if (detalheDiferencaCota == null) {
            
            detalheDiferencaCota = new DetalheDiferencaCotaDTO();
        }
        
        detalheDiferencaCota.setDetalhesDiferenca(detalhesDiferenca);
        
        return detalheDiferencaCota;
    }
    
    @Override
    @Transactional
    public void excluirLancamentoDiferenca(final Long idDiferenca) {
        
        rateioDiferencaRepository.removerRateioDiferencaPorDiferenca(idDiferenca);
        
        final Diferenca diferenca = diferencaEstoqueRepository.buscarPorId(idDiferenca);
        
        if (diferenca.getItemRecebimentoFisico() != null) {
            
            diferenca.getItemRecebimentoFisico().setDiferenca(null);
        }
        
        diferencaEstoqueRepository.remover(diferenca);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigInteger obterQuantidadeTotalDiferencas(final String codigoProduto, final Long numeroEdicao,
            final TipoEstoque tipoEstoque, final Date dataMovimento) {
        
        return diferencaEstoqueRepository.obterQuantidadeTotalDiferencas(
                codigoProduto, numeroEdicao, tipoEstoque, dataMovimento);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeDiferencaPorNota(final Long idProdutoEdicao,
            final Date dataNotaEnvio,
            final Integer numeroCota) {
        
        return diferencaEstoqueRepository.existeDiferencaPorNota(
                idProdutoEdicao, dataNotaEnvio, numeroCota);
    }
    
	                                    /*
     * Efetua a geração da movimentação de estoque para diferença.
     */
    private MovimentoEstoque gerarMovimentoEstoque(final Diferenca diferenca, final Long idUsuario,
            final boolean isAprovacaoAutomatica,
            final boolean validarTransfEstoqueDiferenca,
            final Date dataLancamento, final Origem origem) {
        
        GrupoMovimentoEstoque grupoMovimentoEstoque = null;
        
        final TipoDiferenca tipoDiferenca = diferenca.getTipoDiferenca();
        
        this.tratarDiferencasDirecionadasParaCota(
            diferenca, tipoDiferenca, idUsuario, isAprovacaoAutomatica,
            validarTransfEstoqueDiferenca, dataLancamento, origem
        );

        StatusIntegracao statusIntegracao = null;
        
        Distribuidor distribuidor = distribuidorService.obter();
        Fornecedor fornecedorProduto = diferenca.getProdutoEdicao().getProduto().getFornecedor();
        boolean origemProdutoManual = false;
        
        if(fornecedorProduto != null && fornecedorProduto.getCodigoInterface() != null
        		&& (fornecedorProduto.getCodigoInterface().toString().equals(distribuidor.getCodigoDistribuidorDinap())
        			|| fornecedorProduto.getCodigoInterface().toString().equals(distribuidor.getCodigoDistribuidorFC()))) {
        	
        	origemProdutoManual = false;
        } else if(fornecedorProduto != null && !fornecedorProduto.getFornecedoresUnificados().isEmpty()) {
        	for(Fornecedor f : fornecedorProduto.getFornecedoresUnificados()) {
        		if(f.getCodigoInterface().equals(distribuidor.getCodigoDistribuidorDinap()) 
        				|| f.getCodigoInterface().equals(distribuidor.getCodigoDistribuidorFC())) {
        			origemProdutoManual = false;
        		} else {
        			origemProdutoManual = true;
        		}
        	}
        } else {
        	
        	origemProdutoManual = true;
        }
        
        if(!origemProdutoManual) {
        	
        	if (tipoDiferenca.isAlteracaoReparte()) {
                
                statusIntegracao = StatusIntegracao.NAO_INTEGRAR;
            }
            
            if (!tipoDiferenca.isAlteracaoReparte() && this.foraDoPrazoDoGFS(diferenca)) {
                
                if(origem != null && origem.equals(Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_FECHAMENTO_ENCALHE)) {
                    statusIntegracao = StatusIntegracao.ENCALHE;
                } else {
                    statusIntegracao = StatusIntegracao.FORA_DO_PRAZO;
                }
                
                grupoMovimentoEstoque = obterGrupoMovimentoEstoqueForaDoPrazo(tipoDiferenca);
                
            } else {
                
                grupoMovimentoEstoque = tipoDiferenca.getTipoMovimentoEstoque();
                
            }
            
    	} else {
    		
    		statusIntegracao = StatusIntegracao.NAO_INTEGRAR;
    		
    		grupoMovimentoEstoque = tipoDiferenca.getTipoMovimentoEstoque();
    		
    	}
        
        final TipoMovimentoEstoque tipoMovimentoEstoque = tipoMovimentoRepository.buscarTipoMovimentoEstoque(grupoMovimentoEstoque);
        
        if (tipoMovimentoEstoque == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de Movimento de Estoque não encontrado.");
        }

        return movimentoEstoqueService.gerarMovimentoEstoqueDiferenca(
                diferenca.getProdutoEdicao().getId(), idUsuario,
                diferenca.getQtde(), tipoMovimentoEstoque,
                isAprovacaoAutomatica, validarTransfEstoqueDiferenca, dataLancamento, 
                statusIntegracao, origem, TipoDiferenca.FALTA_EM_DIRECIONADA_COTA.equals(tipoDiferenca));
    }
    
    private void tratarDiferencasDirecionadasParaCota(final Diferenca diferenca,
            final TipoDiferenca tipoDiferenca,
            final Long idUsuario,
            final boolean isAprovacaoAutomatica,
            final boolean validarTransfEstoqueDiferenca,
            final Date dataLancamento, final Origem origem) {

        final TipoDiferenca novoTipoDiferenca = 
        		tipoDiferenca.isSobra() ? TipoDiferenca.SOBRA_ENVIO_PARA_COTA :
        			tipoDiferenca.isFalta() ? TipoDiferenca.FALTA_EM_DIRECIONADA_COTA : 
        				tipoDiferenca.isFaltaParaCota() ? TipoDiferenca.AJUSTE_REPARTE_FALTA_COTA :
        					null;

        if (novoTipoDiferenca != null && !TipoDirecionamentoDiferenca.ESTOQUE.equals(diferenca.getTipoDirecionamento())) {

            try {

                final Diferenca diferencaSaidaDistribuidor = (Diferenca) BeanUtils.cloneBean(diferenca);

                diferencaSaidaDistribuidor.setTipoDiferenca(novoTipoDiferenca);

                this.gerarMovimentoEstoque(
                    diferencaSaidaDistribuidor, idUsuario, isAprovacaoAutomatica,
                    validarTransfEstoqueDiferenca, dataLancamento, origem
                );

            } catch (final Exception e) {
                
                throw new IllegalArgumentException(e);
            }
        }

    }
    
    private GrupoMovimentoEstoque obterGrupoMovimentoEstoqueForaDoPrazo(final TipoDiferenca tipoDiferenca) {
        
        GrupoMovimentoEstoque grupoMovimentoEstoque;
        
        if (tipoDiferenca.isAjusteDistribuidor()) {

        	return tipoDiferenca.getTipoMovimentoEstoque();
        	
        } else if (tipoDiferenca.isDiferencaDe()) {
            
            if (tipoDiferenca.isFalta()) {
                
                grupoMovimentoEstoque = GrupoMovimentoEstoque.PERDA_DE;
                
            } else {
                
                grupoMovimentoEstoque = GrupoMovimentoEstoque.GANHO_DE;
            }
            
        } else {
            
            if (tipoDiferenca.isFalta() || tipoDiferenca.isPerda()) {
                
                grupoMovimentoEstoque = GrupoMovimentoEstoque.PERDA_EM;
                
            } else {
                
                grupoMovimentoEstoque = GrupoMovimentoEstoque.GANHO_EM;
            }
        }
        
        return grupoMovimentoEstoque;
    }
    
	                                    /*
     * Efetua a geração do movimento de estoque do rateio da diferença para
     * cota.
     */
    private MovimentoEstoqueCota gerarMovimentoEstoqueCota(final Diferenca diferenca,
            final RateioDiferenca rateioDiferenca,
            final Long idUsuario,
            final boolean isAprovacaoAutomatica,
            final Date dataLancamento) {
        
        final TipoMovimentoEstoque tipoMovimentoEstoqueCota =
                tipoMovimentoRepository.buscarTipoMovimentoEstoque(
                        diferenca.getTipoDiferenca().getGrupoMovimentoEstoqueCota());
        
        final Long estudoCotaId = (rateioDiferenca.getEstudoCota() != null)
                ? rateioDiferenca.getEstudoCota().getId() : null;
                
        return movimentoEstoqueService.gerarMovimentoCotaDiferenca(
                dataLancamento, diferenca.getProdutoEdicao().getId(), rateioDiferenca.getCota().getId(),
                idUsuario, rateioDiferenca.getQtde(), tipoMovimentoEstoqueCota, estudoCotaId, isAprovacaoAutomatica);
    }
    
	                                    /*
     * Efetua a geração de lançamento de diferença.
     */
    private LancamentoDiferenca gerarLancamentoDiferenca(final StatusAprovacao statusAprovacao,
            final MovimentoEstoque movimentoEstoque,
            final List<MovimentoEstoqueCota> listaMovimentosEstoqueCota) {
        
        final LancamentoDiferenca lancamentoDiferenca = new LancamentoDiferenca();
        
        lancamentoDiferenca.setDataProcessamento(new Date());
        lancamentoDiferenca.setMovimentoEstoque(movimentoEstoque);
        lancamentoDiferenca.setMovimentosEstoqueCota(listaMovimentosEstoqueCota);
        lancamentoDiferenca.setStatus(statusAprovacao);
        
        return lancamentoDiferencaRepository.merge(lancamentoDiferenca);
    }
    
    @Override
    @Transactional(readOnly = true)
    public byte[] imprimirRelatorioFaltasSobras(final Date dataMovimento) throws Exception {
        
        final List<ImpressaoDiferencaEstoqueDTO> dadosImpressao =
                diferencaEstoqueRepository.obterDadosParaImpressaoNaData(dataMovimento);
        
        if (dadosImpressao == null
                || dadosImpressao.isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Não há dados para impressão nesta data");
        }
        
        final List<RelatorioLancamentoFaltasSobrasVO> listaRelatorio =
                new ArrayList<RelatorioLancamentoFaltasSobrasVO>();
        
        final int qtdeRateiosPorLinha = 5;
        
        for (final ImpressaoDiferencaEstoqueDTO dadoImpressao : dadosImpressao) {
            
            final List<RateioDiferencaDTO> rateios =
                    rateioDiferencaRepository.obterRateiosParaImpressaoPorDiferenca(
                            dadoImpressao.getProdutoEdicao().getId(), dataMovimento);
            
            if (rateios != null && !rateios.isEmpty()) {
                
                if (rateios.size() <= qtdeRateiosPorLinha) {
                    
                    dadoImpressao.setRateios(rateios);
                    
                    listaRelatorio.add(
                            new RelatorioLancamentoFaltasSobrasVO(dadoImpressao));
                    
                } else {
                    
                    final int qtdeLinhas =
                            (int) Math.ceil((double) rateios.size() / qtdeRateiosPorLinha);
                    
                    int indice = 0;
                    
                    for (int linha = 0; linha < qtdeLinhas; linha++) {
                        
                        final ImpressaoDiferencaEstoqueDTO dadoImpressaoComRateio =
                                new ImpressaoDiferencaEstoqueDTO();
                        
                        if (linha == 0){
                            dadoImpressaoComRateio.setIdDiferenca(dadoImpressao.getIdDiferenca());
                            dadoImpressaoComRateio.setProdutoEdicao(dadoImpressao.getProdutoEdicao());
                        }
                        
                        dadoImpressaoComRateio.setQtdeFaltas(dadoImpressao.getQtdeFaltas());
                        dadoImpressaoComRateio.setQtdeSobras(dadoImpressao.getQtdeSobras());
                        
                        dadoImpressaoComRateio.setRateios(
                                rateios.subList(indice, (indice += qtdeRateiosPorLinha) > rateios.size() ? rateios.size() : indice));
                        
                        listaRelatorio.add(
                                new RelatorioLancamentoFaltasSobrasVO(dadoImpressaoComRateio));
                    }
                }
            }
            // Dados impressão sem rateio
            else{
                
                dadoImpressao.setQtdeFaltas(null);
                dadoImpressao.setQtdeSobras(null);
                
                listaRelatorio.add(
                        new RelatorioLancamentoFaltasSobrasVO(dadoImpressao));
            }
        }
        
        final Map<String, Object> parametrosRelatorio = new HashMap<String, Object>();
        
        parametrosRelatorio.put("DISTRIBUIDOR", distribuidorService.obterRazaoSocialDistribuidor());
        parametrosRelatorio.put("DATA_MOVIMENTO", DateUtil.formatarDataPTBR(dataMovimento));
        parametrosRelatorio.put("LOGO_DISTRIBUIDOR", JasperUtil.getImagemRelatorio(parametrosDistribuidorService.getLogotipoDistribuidor()));
        
        final JRBeanCollectionDataSource datasourceRelatorio = new JRBeanCollectionDataSource(listaRelatorio);
        
        final URL urlRelatorio =
                Thread.currentThread().getContextClassLoader().getResource("/reports/faltas_sobras.jasper");
        
        final String caminhoRelatorio = urlRelatorio.toURI().getPath();
        
        return JasperRunManager.runReportToPdf(caminhoRelatorio, parametrosRelatorio, datasourceRelatorio);
    }
    
    @Override
    @Transactional(readOnly = true)
    public void validarDadosParaImpressaoNaData(final String dataMovimentoFormatada) {
        
        if (dataMovimentoFormatada == null
                || dataMovimentoFormatada.trim().equals("")) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Informe uma data de movimento");
        }
        
        final Date dataMovimento = DateUtil.parseDataPTBR(dataMovimentoFormatada);
        
        final Long quantidadeDadosImpressao =
                diferencaEstoqueRepository.obterQuantidadeDadosParaImpressaoNaData(dataMovimento);
        
        if (quantidadeDadosImpressao == null
                || quantidadeDadosImpressao == 0L) {
            
            throw new ValidacaoException(
TipoMensagem.WARNING, "Não há dados para impressão nesta data");
        }
    }
    
    @Override
    @Transactional
    public Set<Diferenca> verificarDiferencasIguais(final Set<Diferenca> listaDiferencas, final Diferenca diferenca) {
        
    	if(listaDiferencas == null){
    		
    		return listaDiferencas;
    	}
    	 
    	if (!listaDiferencas.isEmpty()) {
    		
    		if(!this.atualizarItem(listaDiferencas, diferenca)){
        		
        		listaDiferencas.add(diferenca);
        	}
    	} else {
    		
    		listaDiferencas.add(diferenca);
    	}
  
        return listaDiferencas;
    }

	private boolean atualizarItem(final Set<Diferenca> listaDiferencas,
            final Diferenca diferenca){
		
		boolean itemAtualizado = false;
		
		for(Diferenca item : listaDiferencas){
			
			if(item.getProdutoEdicao().equals(diferenca.getProdutoEdicao())
	                && item.getTipoDiferenca().equals(diferenca.getTipoDiferenca())
	                && item.getTipoDirecionamento().equals(diferenca.getTipoDirecionamento())
	                && item.getTipoEstoque().equals(diferenca.getTipoEstoque())) {
				
	            final BigInteger diferencaInicial = item.getQtde();
	            final BigDecimal valorTotalDiferenca = item.getValorTotalDiferenca();
	            
	            item.setQtde(diferencaInicial.add(diferenca.getQtde()));
	            item.setValorTotalDiferenca(valorTotalDiferenca.add(diferenca.getValorTotalDiferenca()));
	            
	            itemAtualizado = true;
	        }
		}
	
		return itemAtualizado;
	}
    
    @Override
    @Transactional
    public Set<DiferencaVO> verificarDiferencasVOIguais(final Set<DiferencaVO> listaNovaDiferencaVO, DiferencaVO diferencaVO) {
        
		 if (listaNovaDiferencaVO == null) {
			 
			 return listaNovaDiferencaVO;
		 }
    	   
    	if(!listaNovaDiferencaVO.isEmpty()) {
    		 
    		if(!this.atualizarItem(listaNovaDiferencaVO, diferencaVO)) {

    			Long id = this.gerarIdentificadorDiferenca(new ArrayList<DiferencaVO>(listaNovaDiferencaVO));
        		diferencaVO.setId(id);
    			listaNovaDiferencaVO.add(diferencaVO);
            }
    	} else {
    		
    		Long id = this.gerarIdentificadorDiferenca(new ArrayList<DiferencaVO>(listaNovaDiferencaVO));
    		diferencaVO.setId(id);
    		
    		listaNovaDiferencaVO.add(diferencaVO);
    	}
     
        return listaNovaDiferencaVO;
    }
    
    private boolean atualizarItem(final Set<DiferencaVO> listaDiferencas, DiferencaVO novaDiferenca) {
    	
    	boolean itemAtualizado = false;
    	
    	for(DiferencaVO diferenca : listaDiferencas) {
    		
    		if (diferenca.getCodigoProduto().equals(novaDiferenca.getCodigoProduto())
                    && diferenca.getNumeroEdicao().equals(novaDiferenca.getNumeroEdicao())
                    && diferenca.getTipoDiferenca().equals(novaDiferenca.getTipoDiferenca())
                    && diferenca.getTipoDirecionamento().equals(novaDiferenca.getTipoDirecionamento())
                    && diferenca.getTipoEstoque().equals(novaDiferenca.getTipoEstoque())) {
                
    			if(novaDiferenca.getId() == null) {
    				novaDiferenca.setId(diferenca.getId());
    			}
                diferenca.setQuantidade(diferenca.getQuantidade().add(novaDiferenca.getQuantidade()));
                
                itemAtualizado = true;
            }
    	}
    	
    	return itemAtualizado;
    }
    
    @Override
    @Transactional
    public Map<Long, List<RateioCotaVO>> insercaoDeNovosValoresNaMesmaCota(
            final Map<Long, List<RateioCotaVO>> mapaRateiosCadastrados,
            final List<RateioCotaVO> listaNovosRateios) {
        
        final Set<Entry<Long, List<RateioCotaVO>>> entrySet = mapaRateiosCadastrados.entrySet();
        final Iterator<Entry<Long, List<RateioCotaVO>>> iterator = entrySet.iterator();
        List<RateioCotaVO> listaRateiosCadastrados;
        
        while(iterator.hasNext()) {
            final Entry<Long, List<RateioCotaVO>> entry = iterator.next();
            
            listaRateiosCadastrados = entry.getValue();
            for(final RateioCotaVO rateiosCadastrados : listaRateiosCadastrados ) {
                for(final RateioCotaVO rateioCotaVO : listaNovosRateios) {
                    if( rateiosCadastrados.getNumeroCota().equals(rateioCotaVO.getNumeroCota()) ) {
                        
                        final BigInteger quantidade = rateiosCadastrados.getQuantidade();
                        final BigInteger reparteAtual = rateiosCadastrados.getReparteAtualCota();
                        
                        rateiosCadastrados.setQuantidade(quantidade.add(rateioCotaVO.getQuantidade()));
                        rateiosCadastrados.setReparteAtualCota(reparteAtual.subtract(rateioCotaVO.getQuantidade()));
                    }
                }
            }
        }
        return mapaRateiosCadastrados;
    }
    
    @Override
    @Transactional
    public Map<Long, List<RateioCotaVO>> verificarSeExisteListaNoMapa(
            final Map<Long, List<RateioCotaVO>> mapaRateiosCadastrados, final Long id,
            final RateioCotaVO rateioCotaVO) {
        
        final Set<Entry<Long, List<RateioCotaVO>>> entrySet = mapaRateiosCadastrados.entrySet();
        final Iterator<Entry<Long, List<RateioCotaVO>>> iterator = entrySet.iterator();
        
        if(iterator.hasNext()) {
            while(iterator.hasNext()) {
                final Entry<Long, List<RateioCotaVO>> keyValue = iterator.next();
                final List<RateioCotaVO> listaValue = keyValue.getValue();
                
                for(final RateioCotaVO value : listaValue) {
                    if(rateioCotaVO.getNumeroCota().equals(value.getNumeroCota()) &&
                            rateioCotaVO.getIdDiferenca().equals(value.getIdDiferenca()) ) {
                        
                        final BigInteger quantidade = value.getQuantidade();
                        final BigInteger reparteAtual = value.getReparteAtualCota();
                        
                        value.setQuantidade(quantidade.add(rateioCotaVO.getQuantidade()));
                        value.setReparteAtualCota(reparteAtual.subtract(rateioCotaVO.getQuantidade()));
                    }
                }
            }
        } else {
            
            final List<RateioCotaVO> listaRateiosCadastrados = new ArrayList<RateioCotaVO>();
            listaRateiosCadastrados.add(rateioCotaVO);
            mapaRateiosCadastrados.put(id, listaRateiosCadastrados);
            
        }
        
        return mapaRateiosCadastrados;
    }
    
    @Override
    @Transactional
    public DiferencaVO verificarDiferencaComListaSessao(
            final Set<DiferencaVO> listaNovasDiferencasVO, DiferencaVO diferencaVO,
            final Long idDiferenca) {
        
        
        final Iterator<DiferencaVO> iterator = listaNovasDiferencasVO.iterator();
        
        while(iterator.hasNext()) {
            final DiferencaVO diferencaVoComparacao = iterator.next();
            if(diferencaVoComparacao.getId().equals(idDiferenca) && !diferencaVoComparacao.equals(diferencaVO)) {
                diferencaVO  = diferencaVoComparacao;
            }
        }
        return diferencaVO;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validarProdutoEmRecolhimento(final ProdutoEdicao produtoEdicao){
        
        if(produtoEdicao == null){
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Produto não encontrada."));
        }
        
        Lancamento lancamento = null;
        
        if(produtoEdicao.isParcial()){
            
            lancamento = lancamentoRepository.obterLancamentoParcialFinal(produtoEdicao.getId());
            
        }else{
            
            lancamento = lancamentoRepository.obterUltimoLancamentoDaEdicao(produtoEdicao.getId());
        }
        
        if(lancamento!= null
                && Arrays.asList(StatusLancamento.BALANCEADO_RECOLHIMENTO,
                        StatusLancamento.EM_RECOLHIMENTO,
                        StatusLancamento.RECOLHIDO,
                        StatusLancamento.FECHADO).contains(lancamento.getStatus())){
            
            return false;
        }
        
        return true;
        
    }

    @Override
    @Transactional(readOnly=true)
    public void validarRateioParaCotasInativas(List<RateioCotaVO> rateioCotas) {
        
        if(rateioCotas == null)
            return;
        
        for(RateioCotaVO rateio : rateioCotas) {
            Cota cota = cotaRepository.obterPorNumerDaCota(rateio.getNumeroCota(), SituacaoCadastro.INATIVO);
            if(cota != null)
                throw new ValidacaoException(TipoMensagem.WARNING, "Cota " + cota.getNumeroCota() + " está inativa.");
        }
        
    }
    
    @Override
    @Transactional
    public Diferenca lancarDiferencaFechamentoCEIntegracao(Diferenca diferenca, 
    													   MovimentoEstoque movimentoEstoque,
    													   StatusAprovacao statusAprovacao) {
       
       diferenca = processarDiferenca(diferenca, diferenca.getTipoEstoque(),StatusConfirmacao.CONFIRMADO,Boolean.TRUE);
        
       LancamentoDiferenca lancamentoDiferenca =
    		   this.gerarLancamentoDiferenca(statusAprovacao, movimentoEstoque, null);
       
       diferenca.setLancamentoDiferenca(lancamentoDiferenca);
       
       diferenca = diferencaEstoqueRepository.merge(diferenca);
       
       return diferenca;
    }
    
    @Override
    public List<ContasAPagarConsignadoVO> pesquisarDiferncas(
            final String codigoProduto, final Long numeroEdicao, final Date data) {
        
        final List<String> msgs = new ArrayList<String>();
        
        if (codigoProduto == null || codigoProduto.isEmpty()){
            msgs.add("Código de Produto inválido");
        }
        
        if (numeroEdicao == null){
            msgs.add("Número edição inválido");
        }
        
        if (data == null){
            msgs.add("Data inválida");
        }
        
        if (!msgs.isEmpty()){
            throw new ValidacaoException(TipoMensagem.WARNING, msgs);
        }
        
        return this.diferencaEstoqueRepository.pesquisarDiferncas(codigoProduto, numeroEdicao, data);
    }
    
    private <E> Long gerarIdentificadorDiferenca(final List<E> listaParaOperacao) {
        
        Long identificador = 0L;
        
        if(listaParaOperacao == null || listaParaOperacao.isEmpty()){
            return identificador;
        }
        
        Collections.sort(listaParaOperacao, new Comparator<E>() {
            @Override
            public int compare(final E o1, final E o2) {
                
                if(o1 instanceof DiferencaVO && o2 instanceof DiferencaVO) {
                	
                    return ((DiferencaVO) o1).getId().compareTo(((DiferencaVO) o2).getId());
                } else if ( o1 instanceof Diferenca && o2 instanceof Diferenca ) {
                    
                    return ((Diferenca) o1).getId().compareTo(((Diferenca) o2).getId());
                } else {
                	
                    return 0;
                }
            }
        });
        
        final E diferenca = listaParaOperacao.get(listaParaOperacao.size()-1);
        
        if(diferenca instanceof DiferencaVO) {
            
            final DiferencaVO diferebcaVO = (DiferencaVO) diferenca;
            
            identificador = (Long) Util.nvl(diferebcaVO.getId(), 0) + 1;
        } else if ( diferenca instanceof Diferenca ) {
            
            final Diferenca difer = (Diferenca)diferenca;
            
            final Long valor = difer.getId();
            
            identificador = valor == null ? 0L : valor + 1;
        }
        
        return identificador;
    }
}
