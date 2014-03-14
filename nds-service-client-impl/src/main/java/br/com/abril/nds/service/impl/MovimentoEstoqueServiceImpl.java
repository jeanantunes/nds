package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.MovimentosEstoqueCotaSaldoDTO;
import br.com.abril.nds.enums.CodigoErro;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.EstoqueProdutoCotaJuramentado;
import br.com.abril.nds.model.estoque.EstoqueProdutoFila;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque.Dominio;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.financeiro.DescontoProximosLancamentos;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.movimentacao.FuroProduto;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoEstudoCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DescontoProximosLancamentosRepository;
import br.com.abril.nds.repository.DescontoRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaJuramentadoRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoFilaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.exception.TipoMovimentoEstoqueInexistenteException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.strategy.importacao.input.HistoricoVendaInput;
import br.com.abril.nds.util.BigIntegerUtil;
import br.com.abril.nds.util.MathUtil;



@Service
public class MovimentoEstoqueServiceImpl implements MovimentoEstoqueService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MovimentoEstoqueServiceImpl.class);
    
    @Autowired
    private EstoqueProdutoRespository estoqueProdutoRespository;
    
    @Autowired
    private ItemRecebimentoFisicoRepository itemRecebimentoFisicoRepository;
    
    @Autowired
    private MovimentoEstoqueRepository movimentoEstoqueRepository;
    
    @Autowired
    private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
    
    @Autowired
    private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
    
    @Autowired
    private EstudoCotaRepository estudoCotaRepository;

    @Autowired
    private EstoqueProdutoFilaRepository estoqueProdutoFilaRepository;
    
    @Autowired
    private CotaRepository cotaRepository;
    
    @Autowired
    private DescontoService descontoService;
   
    @Autowired
    private DescontoRepository descontoRepository;
    
    @Autowired
    private DescontoProximosLancamentosRepository descontoProximosLancamentosRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
    
    @Autowired
    private ProdutoEdicaoRepository produtoEdicaoRepository;
    
    @Autowired
    private LancamentoRepository lancamentoRepository;
    
    @Autowired
    private EstoqueProdutoCotaJuramentadoRepository estoqueProdutoCotaJuramentadoRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Override
    @Transactional
    public void gerarMovimentoEstoqueFuroPublicacao(final Lancamento lancamento, final FuroProduto furoProduto, final Long idUsuario) {
        
        final Long idProdutoEdicao = lancamento.getProdutoEdicao().getId();
        
        final TipoMovimentoEstoque tipoMovimento =
                tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
                        GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO);
        
        final TipoMovimentoEstoque tipoMovimentoCota =
                tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
                        GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO);
        
        final TipoMovimentoEstoque tipoMovimentoEstCotaAusente =
                tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
                        GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
        
        BigInteger total = BigInteger.ZERO;
        
        final List<MovimentoEstoqueCota> listaMovimentoEstoqueCotas =
                lancamentoRepository.buscarMovimentosEstoqueCotaParaFuro(
                        lancamento, tipoMovimentoCota);
        
        MovimentoEstoqueCota movimento = null;
        
        for (final MovimentoEstoqueCota movimentoEstoqueCota : listaMovimentoEstoqueCotas) {
            
            movimento = (MovimentoEstoqueCota) movimentoEstoqueCota.clone();
            
            movimento =
                    gerarMovimentoCota(
                            null, idProdutoEdicao, movimento.getCota().getId(), idUsuario,
                            movimento.getQtde(), tipoMovimentoCota,lancamento.getDataLancamentoDistribuidor(), null, lancamento.getId(), null);
            
            if (movimentoEstoqueCota.getTipoMovimento() != tipoMovimentoEstCotaAusente){
                
                total = total.add(movimento.getQtde());
            } else {
                
                total = total.subtract(movimento.getQtde());
            }
            
            movimentoEstoqueCota.setMovimentoEstoqueCotaFuro(movimento);
            
            movimentoEstoqueCotaRepository.alterar(movimentoEstoqueCota);
            
        }
        
        final MovimentoEstoque movimentoEstoque = gerarMovimentoEstoque(null, idProdutoEdicao, idUsuario, total, tipoMovimento);
        movimentoEstoque.setFuroProduto(furoProduto);
        movimentoEstoqueRepository.merge(movimentoEstoque);
        
    }
    
    @Override
    @Transactional
    public void gerarMovimentoEstoqueDeExpedicao(final Date dataPrevista, final Date dataDistribuidor, final Long idProduto, final Long idProdutoEdicao,
            final Long idLancamento, final Long idUsuario, final Date dataOperacao, final TipoMovimentoEstoque tipoMovimento, final TipoMovimentoEstoque tipoMovimentoCota,final TipoMovimentoEstoque tipoMovimentoJuramentado) {
        
        final List<EstudoCotaDTO> listaEstudoCota = estudoCotaRepository.obterEstudoCotaPorDataProdutoEdicao(idLancamento, idProdutoEdicao);
        
        BigInteger total = BigInteger.ZERO;
        
        BigInteger totalParcialJuramentado = BigInteger.ZERO;
        
        final Map<String, DescontoDTO> descontos = descontoService.obterDescontosMapPorLancamentoProdutoEdicao(dataDistribuidor);
        
        final DescontoProximosLancamentos descontoProximosLancamentos = descontoProximosLancamentosRepository.obterDescontoProximosLancamentosPor(idProduto, dataDistribuidor);
        
        final List<MovimentoEstoqueCotaDTO> movimentosEstoqueCota = new ArrayList<MovimentoEstoqueCotaDTO>();
        
        ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
        
        tratarIncrementoProximoLancamento(descontos,descontoProximosLancamentos, null, 
                produtoEdicao.getProduto().getFornecedor().getId(), idProdutoEdicao, idProduto);
        
        for (final EstudoCotaDTO estudoCota : listaEstudoCota) {
            
            if (estudoCota.getQtdeEfetiva() == null || BigInteger.ZERO.equals(estudoCota.getQtdeEfetiva())) {
                
                continue;
            }
            
            tratarIncrementoProximoLancamento(descontos,descontoProximosLancamentos, estudoCota.getIdCota(), 
                    produtoEdicao.getProduto().getFornecedor().getId(), idProdutoEdicao, idProduto);
            
            final MovimentoEstoqueCotaDTO mec = criarMovimentoExpedicaoCota(
                    dataPrevista, idProdutoEdicao, estudoCota.getIdCota(),
                    idUsuario, estudoCota.getQtdeEfetiva(), tipoMovimentoCota,
                    dataDistribuidor, dataOperacao, idLancamento, estudoCota.getId(), descontos, false);
            
            if(TipoEstudoCota.NORMAL.equals(estudoCota.getTipoEstudo())){
                
                total = total.add(estudoCota.getQtdeEfetiva());
            }
            else{
                
                totalParcialJuramentado = totalParcialJuramentado.add(estudoCota.getQtdeEfetiva());
            }
            
            movimentosEstoqueCota.add(mec);
        }
        
        if(total.compareTo(BigInteger.ZERO) > 0){
            gerarMovimentoEstoque(idProdutoEdicao, idUsuario, total, tipoMovimento, dataDistribuidor, false);
        }
        
        if(totalParcialJuramentado.compareTo(BigInteger.ZERO) > 0){
            gerarMovimentoEstoque(idProdutoEdicao, idUsuario, totalParcialJuramentado, tipoMovimentoJuramentado, dataDistribuidor, false);
        }
        
        movimentoEstoqueCotaRepository.adicionarEmLoteDTO(movimentosEstoqueCota);
        
    }
    
	private void tratarIncrementoProximoLancamento(Map<String, DescontoDTO>  descontos, 
	        DescontoProximosLancamentos descontoProximosLancamentos, Long idCota, Long idFornecedor, Long idProduto, Long idEdicao) {
	    
        if (descontoProximosLancamentos != null) {
            
            DescontoDTO descontoDTO =  null;
            
            if(idCota!=null)
                descontoDTO = descontoService.obterDescontoProximosLancamentosPor(
                    descontos, idCota, idFornecedor, idProduto, idEdicao);
            else
                descontoDTO = descontoService.obterDescontoProximosLancamentosPorDeTodasCotas(
                        descontos, idFornecedor, idProduto, idEdicao);
                
            if (descontoDTO != null) {
                
                Integer quantidadeProximosLancamaentos = 
                    descontoProximosLancamentos.getQuantidadeProximosLancamaentos();
                
                descontoProximosLancamentos.setQuantidadeProximosLancamaentos(quantidadeProximosLancamaentos - 1);
                
                Desconto desconto = descontoProximosLancamentos.getDesconto();
                
                desconto.setUsado(true);
                
                descontoRepository.merge(desconto);
                
                descontoProximosLancamentosRepository.merge(descontoProximosLancamentos);
            }
        }
    }

                /**
     * Obtem Objeto com Lista de movimentos de estoque referentes à reparte e
     * Map de edicoes com saidas e entradas diversas
     * 
     * @param listaMovimentoCota
     * @return MovimentosEstoqueCotaSaldoDTO
     */
    @Override
    @Transactional
    public MovimentosEstoqueCotaSaldoDTO getMovimentosEstoqueCotaSaldo(final List<MovimentoEstoqueCota> listaMovimentoCota){
        
        final List<MovimentoEstoqueCota> listaMovimentosEstoqueCotaReparte = new ArrayList<MovimentoEstoqueCota>();
        
        final Map<Long,BigInteger> produtoEdicaoQtdSaida = new HashMap<Long, BigInteger>();
        
        final Map<Long,BigInteger> produtoEdicaoQtdEntrada = new HashMap<Long, BigInteger>();
        
        for (final MovimentoEstoqueCota movimentoCota : listaMovimentoCota) {
            
            if (((TipoMovimentoEstoque) movimentoCota.getTipoMovimento())
                    .getGrupoMovimentoEstoque().equals(
                            GrupoMovimentoEstoque.RECEBIMENTO_REPARTE)) {
                
                listaMovimentosEstoqueCotaReparte.add(movimentoCota);
            }
            else{
                
                BigInteger qtdProduto = BigInteger.ZERO;
                
                if (((TipoMovimentoEstoque) movimentoCota.getTipoMovimento())
                        .getGrupoMovimentoEstoque().getOperacaoEstoque().equals(OperacaoEstoque.SAIDA)) {
                    
                    qtdProduto = produtoEdicaoQtdSaida.get(movimentoCota.getProdutoEdicao().getId())!=null?
                            produtoEdicaoQtdSaida.get(movimentoCota.getProdutoEdicao().getId()):
                                BigInteger.ZERO;
                            
                            qtdProduto = movimentoCota.getQtde().add(qtdProduto);
                            
                            produtoEdicaoQtdSaida.put(movimentoCota.getProdutoEdicao().getId(), qtdProduto);
                }
                
                if (((TipoMovimentoEstoque) movimentoCota.getTipoMovimento())
                        .getGrupoMovimentoEstoque().getOperacaoEstoque().equals(OperacaoEstoque.ENTRADA)) {
                    
                    qtdProduto = produtoEdicaoQtdEntrada.get(movimentoCota.getProdutoEdicao().getId())!=null?
                            produtoEdicaoQtdEntrada.get(movimentoCota.getProdutoEdicao().getId()):
                                BigInteger.ZERO;
                            
                            qtdProduto = movimentoCota.getQtde().add(qtdProduto);
                            
                            produtoEdicaoQtdEntrada.put(movimentoCota.getProdutoEdicao().getId(), qtdProduto);
                }
            }
        }
        
        return new MovimentosEstoqueCotaSaldoDTO(listaMovimentosEstoqueCotaReparte, produtoEdicaoQtdSaida, produtoEdicaoQtdEntrada);
    }
    
    @Override
    @Transactional
    public List<MovimentoEstoqueCota> enviarSuplementarCotaAusente(final Date data,
            final Long idCota,
            final List<MovimentoEstoqueCota> listaMovimentoCota)
                    throws TipoMovimentoEstoqueInexistenteException {
        
        final Cota cota = cotaRepository.buscarPorId(idCota);
        
        if(listaMovimentoCota==null || listaMovimentoCota.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota '" + cota.getNumeroCota()
                + "' não possui reparte na data.");
        }
        
        final TipoMovimentoEstoque tipoMovimento =
                tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);
        
        final TipoMovimentoEstoque tipoMovimentoCota =
                tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
        
        if ( tipoMovimento == null ) {
            throw new TipoMovimentoEstoqueInexistenteException(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);
        }
        
        if ( tipoMovimentoCota == null ) {
            throw new TipoMovimentoEstoqueInexistenteException(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
        }
        
        final MovimentosEstoqueCotaSaldoDTO movimentosSaldo = this.getMovimentosEstoqueCotaSaldo(listaMovimentoCota);
        
        final List<MovimentoEstoqueCota> listaMovimentoCotaEnvio = this.estornarMovimentosDaCotaAusente(movimentosSaldo.getMovimentosEstoqueCota(),
                data,
                tipoMovimento,
                tipoMovimentoCota,
                movimentosSaldo.getProdutoEdicaoQtdSaida(),
                movimentosSaldo.getProdutoEdicaoQtdEntrada());
        
        return listaMovimentoCotaEnvio;
    }
    
    private List<MovimentoEstoqueCota> estornarMovimentosDaCotaAusente(final List<MovimentoEstoqueCota> listaMovimentosEstoqueCotaReparte,
            final Date data,
            final TipoMovimentoEstoque tipoMovimento,
            final TipoMovimentoEstoque tipoMovimentoCota,
            final Map<Long,BigInteger> produtoEdicaoQtdSaida,
            final Map<Long,BigInteger> produtoEdicaoQtdEntrada){
        
        final List<MovimentoEstoqueCota> listaMovimentoCotaEnvio = new ArrayList<MovimentoEstoqueCota>();
        
        for (final MovimentoEstoqueCota movimentoCota : listaMovimentosEstoqueCotaReparte) {
            
            if (movimentoCota.getData() != null
                    && movimentoCota.getProdutoEdicao() != null
                    && movimentoCota.getUsuario() != null
                    && movimentoCota.getQtde() != null ) {
                
                
                final BigInteger quantidadeSaidas = produtoEdicaoQtdSaida.get(movimentoCota.getProdutoEdicao().getId())!=null?
                        produtoEdicaoQtdSaida.get(movimentoCota.getProdutoEdicao().getId()):
                            BigInteger.ZERO;
                        
                        final BigInteger quantidadeEntradas = produtoEdicaoQtdEntrada.get(movimentoCota.getProdutoEdicao().getId())!=null?
                                produtoEdicaoQtdEntrada.get(movimentoCota.getProdutoEdicao().getId()):
                                    BigInteger.ZERO;
                                
                                final BigInteger saldoProduto = quantidadeEntradas.subtract(quantidadeSaidas);
                                
                                final BigInteger quantidade = movimentoCota.getQtde().add(saldoProduto);
                                
                                gerarMovimentoEstoque(null,
                                        movimentoCota.getProdutoEdicao().getId(),
                                        movimentoCota.getUsuario().getId(),
                                        quantidade,
                                        tipoMovimento);
                                
                                listaMovimentoCotaEnvio.add(gerarMovimentoCota(data,
                                        movimentoCota.getProdutoEdicao().getId(),
                                        movimentoCota.getCota().getId(),
                                        movimentoCota.getUsuario().getId(),
                                        quantidade,
                                        tipoMovimentoCota,
                                        data,
                                        null,
                                        movimentoCota.getLancamento()!=null?movimentoCota.getLancamento().getId():null,
                                                null));
            }
        }
        
        return listaMovimentoCotaEnvio;
    }
    
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoqueJuramentado(final Long idProdutoEdicao, final Long idUsuario, final BigInteger quantidade,final TipoMovimentoEstoque tipoMovimentoEstoque) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque,null, null, false,false, true, null, false, null);
        
        return movimentoEstoque;
    }
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoque(final Long idItemRecebimentoFisico, final Long idProdutoEdicao, final Long idUsuario, final BigInteger quantidade,final TipoMovimentoEstoque tipoMovimentoEstoque) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(idItemRecebimentoFisico, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque,null, null, false,false, true, null, false, null);
        
        return movimentoEstoque;
    }
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoque(final Long idProdutoEdicao, final Long idUsuario, final BigInteger quantidade,final TipoMovimentoEstoque tipoMovimentoEstoque, final Origem origem) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque, origem, null, false,false, true, null, false, null);
        
        return movimentoEstoque;
    }
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoque(final Long idProdutoEdicao, final Long idUsuario, final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque,null, null, false, false, true, null, false, null);
        
        return movimentoEstoque;
    }
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoque(final Long idProdutoEdicao, final Long idUsuario, final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque, boolean enfileiraAlteracaoEstoqueProduto, Cota cota) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque,null, null, false, false, true, null, enfileiraAlteracaoEstoqueProduto, cota);
        
        return movimentoEstoque;
    }
    
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoque(final Long idProdutoEdicao, final Long idUsuario, final BigInteger quantidade,final TipoMovimentoEstoque tipoMovimentoEstoque, final Date dataOperacao, final boolean isImportacao) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque, null, dataOperacao, isImportacao, false, true, null, false, null);
        
        return movimentoEstoque;
    }
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoqueDiferenca(final Long idProdutoEdicao, final Long idUsuario,
            final BigInteger quantidade,final TipoMovimentoEstoque tipoMovimentoEstoque,
            final boolean isMovimentoDiferencaAutomatica,
            final boolean validarTransfEstoqueDiferenca,
            final Date dataLancamento, final StatusIntegracao statusIntegracao, final Origem origem) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade,
                tipoMovimentoEstoque, origem, null,
                false, isMovimentoDiferencaAutomatica, validarTransfEstoqueDiferenca, statusIntegracao, false, null);
        return movimentoEstoque;
    }
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoqueDiferenca(final Long idProdutoEdicao, final Long idUsuario,
            final BigInteger quantidade,final TipoMovimentoEstoque tipoMovimentoEstoque,
            final boolean isMovimentoDiferencaAutomatica,
            final boolean validarTransfEstoqueDiferenca,
            final Date dataLancamento, final StatusIntegracao statusIntegracao) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade,
                tipoMovimentoEstoque, null, null,
                false, isMovimentoDiferencaAutomatica, validarTransfEstoqueDiferenca, statusIntegracao, false, null);
        return movimentoEstoque;
    }
    
    private MovimentoEstoque criarMovimentoEstoque(final Long idItemRecebimentoFisico, final Long idProdutoEdicao, final Long idUsuario,
            final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque,
            final Origem origem, Date dataOperacao, final boolean isImportacao,
            final boolean isMovimentoDiferencaAutomatica, final boolean validarTransfEstoqueDiferenca,
            final StatusIntegracao statusIntegracao,
            boolean enfileiraAlteracaoEstoqueProduto, 
            Cota cota) {
        
        this.validarDominioGrupoMovimentoEstoque(tipoMovimentoEstoque, Dominio.DISTRIBUIDOR);
        
        MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
        
        if (dataOperacao == null) {
            
            dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
        }
        
        if (idItemRecebimentoFisico != null) {
            
            final ItemRecebimentoFisico itemRecebimentoFisico = itemRecebimentoFisicoRepository.buscarPorId(idItemRecebimentoFisico);
            
            if (itemRecebimentoFisico != null) {
                movimentoEstoque.setItemRecebimentoFisico(itemRecebimentoFisico);
            }
            
        }
        
        movimentoEstoque.setProdutoEdicao(new ProdutoEdicao(idProdutoEdicao));
        
        movimentoEstoque.setData(dataOperacao);
        
        if (tipoMovimentoEstoque != null) {
            if (tipoMovimentoEstoque.getGrupoMovimentoEstoque() != null
                    && tipoMovimentoEstoque.getGrupoMovimentoEstoque().equals(GrupoMovimentoEstoque.RECEBIMENTO_FISICO)) {
                movimentoEstoque.setDataCriacao(new Date());
            } else {
                movimentoEstoque.setDataCriacao(dataOperacao);
            }
            movimentoEstoque.setAprovadoAutomaticamente(tipoMovimentoEstoque.isAprovacaoAutomatica());

            movimentoEstoque.setUsuario(new Usuario(idUsuario));
            movimentoEstoque.setTipoMovimento(tipoMovimentoEstoque);
            movimentoEstoque.setQtde(quantidade);
            movimentoEstoque.setOrigem(origem);
            
            
            if (tipoMovimentoEstoque.isAprovacaoAutomatica() || isMovimentoDiferencaAutomatica) {
                
                movimentoEstoque.setStatus(StatusAprovacao.APROVADO);
                movimentoEstoque.setAprovador(new Usuario(idUsuario));
                movimentoEstoque.setDataAprovacao(distribuidorService.obterDataOperacaoDistribuidor());
                
                
                if(enfileiraAlteracaoEstoqueProduto) {
                	
                	enfileirarAlteracaoEncalheEstoqueProduto(
                			cota, 
                			movimentoEstoque.getProdutoEdicao(), 
                			tipoMovimentoEstoque.getGrupoMovimentoEstoque(), 
                			OperacaoEstoque.ENTRADA, 
                			quantidade);
                	
                } else {
                    
                	final Long idEstoque = atualizarEstoqueProduto(tipoMovimentoEstoque, movimentoEstoque, isImportacao, validarTransfEstoqueDiferenca);
                    
                	movimentoEstoque.setEstoqueProduto(new EstoqueProduto(idEstoque));
                    
                }
                
                
                
            }
        }
        if (statusIntegracao != null) {
            
            movimentoEstoque.setStatusIntegracao(statusIntegracao);
        }
        
        movimentoEstoque = movimentoEstoqueRepository.merge(movimentoEstoque);
        
        return movimentoEstoque;
    }
    
    @Override
    @Transactional
    public Long atualizarEstoqueProduto(final TipoMovimentoEstoque tipoMovimentoEstoque,
            final MovimentoEstoque movimentoEstoque) {
        
    	return atualizarEstoqueProduto(tipoMovimentoEstoque, movimentoEstoque, false, true);
        
    }
    
    @Transactional
    public void atualizarEstoqueProdutoDaFilaCota(Integer numeroCota) {
    
		List<EstoqueProdutoFila> listaEstoqueProdutoFila = 
			estoqueProdutoFilaRepository.buscarEstoqueProdutoFilaNumeroCota(numeroCota);
		
		if (listaEstoqueProdutoFila == null 
				|| listaEstoqueProdutoFila.isEmpty()) {
			
			return;
		}
		
		for (EstoqueProdutoFila eFila : listaEstoqueProdutoFila) {

			atualizarEstoqueProdutoDaFila(eFila);
		}
    	
		cleanUpEstoqueProdutoFila(listaEstoqueProdutoFila);
    }
    
    private void atualizarEstoqueProdutoDaFila(EstoqueProdutoFila eFila) {
    	
		Long idProdutoEdicao = eFila.getProdutoEdicao().getId();
		
		TipoEstoque tipoEstoque = eFila.getTipoEstoque();
		
		EstoqueProduto estoqueProduto = estoqueProdutoRespository.obterEstoqueProdutoParaAtualizar(idProdutoEdicao);
		
		BigInteger novaQuantidade = BigInteger.ZERO;
		
		final boolean isOperacaoEntrada = OperacaoEstoque.ENTRADA.equals(eFila.getOperacaoEstoque());
		
		switch (tipoEstoque) {
        
	        case DEVOLUCAO_ENCALHE:
	            
	        	 final BigInteger qtdeEncalhe = estoqueProduto.getQtdeDevolucaoEncalhe() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
	             
	             novaQuantidade = isOperacaoEntrada ? qtdeEncalhe.add(eFila.getQtde()) :
	                 qtdeEncalhe.subtract(eFila.getQtde());
	             
	             estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
	
	            
	            break;
	            
	        case SUPLEMENTAR:
	            
	        	final BigInteger qtdeSuplementar = estoqueProduto.getQtdeSuplementar() == null ? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
	            
	            novaQuantidade = isOperacaoEntrada ? qtdeSuplementar.add(eFila.getQtde()) :
	                qtdeSuplementar.subtract(eFila.getQtde());
	            
	            estoqueProduto.setQtdeSuplementar(novaQuantidade);
	            
	            break;
	            
	        default :
	        	
	        	throw new ValidacaoException(TipoMensagem.WARNING, "Estoque inválido para a operação.");
        	
		};
		
        this.validarAlteracaoEstoqueProdutoDistribuidor(
        	novaQuantidade, tipoEstoque, estoqueProduto.getProdutoEdicao(), true);

    	estoqueProdutoRespository.merge(estoqueProduto);
    }
    
    private void cleanUpEstoqueProdutoFila(List<EstoqueProdutoFila> listaEstoqueProdutoFila) {
    	
    	for (EstoqueProdutoFila e : listaEstoqueProdutoFila) {
    	
    		estoqueProdutoFilaRepository.remover(e);
    	}
    }
    
    /**
	 * Exclui um registro de MovimentoEstoque enfileirando 
	 * a alteraçao do EstoqueProduto relativo (na tbl EstoqueProdutoFila).
	 * 
	 * @param cota
	 * @param movimentoEstoque
	 */
    @Override
    @Transactional
	public void excluirRegistroMovimentoEstoqueDeEncalhe(Cota cota, MovimentoEstoque movimentoEstoque) {
		
		if(movimentoEstoque.getQtde() != null && (movimentoEstoque.getQtde().compareTo(BigInteger.ZERO) != 0)) {
			
			enfileirarAlteracaoEncalheEstoqueProduto(
					cota,
					movimentoEstoque.getProdutoEdicao(), 
					((TipoMovimentoEstoque) movimentoEstoque.getTipoMovimento()).getGrupoMovimentoEstoque(),
					OperacaoEstoque.SAIDA,
					movimentoEstoque.getQtde());
			
		}
		
		movimentoEstoqueRepository.remover(movimentoEstoque);
		
	}

    private void enfileirarAlteracaoEncalheEstoqueProduto(
    		Cota cota,
    		ProdutoEdicao produtoEdicao, 
    		GrupoMovimentoEstoque grupoMovimentoEstoque,
    		OperacaoEstoque operacaoEstoque,
    		BigInteger qtde) {
    	
    	EstoqueProdutoFila epf = new EstoqueProdutoFila();
    	
    	epf.setProdutoEdicao(produtoEdicao);
    	epf.setCota(cota);
    	epf.setQtde(qtde);
    	epf.setOperacaoEstoque(operacaoEstoque);
    	epf.setTipoEstoque(grupoMovimentoEstoque.getTipoEstoque());
		
    	estoqueProdutoFilaRepository.adicionar(epf);
    	
    }
    
    
    /**
	 * Atualiza registro de MovimentoEstoque de encalhe bem como o registro de EstoqueProduto relacionado.
	 * 
	 * @param movimentoEstoque
	 * @param conferenciaEncalheDTO
	 */
    @Override
	@Transactional
    public void atualizarMovimentoEstoqueDeEncalhe(
    		Cota cota, 
    		MovimentoEstoque movimentoEstoque, 
			BigInteger newQtdeMovEstoque) {
		
		BigInteger oldQtdeMovEstoque = movimentoEstoque.getQtde() != null ? movimentoEstoque.getQtde() : BigInteger.ZERO;
		newQtdeMovEstoque = newQtdeMovEstoque != null ? newQtdeMovEstoque : BigInteger.ZERO;
		
		movimentoEstoque.setQtde(newQtdeMovEstoque);
		
		GrupoMovimentoEstoque grupoMovimentoEstoque = ((TipoMovimentoEstoque) movimentoEstoque.getTipoMovimento()).getGrupoMovimentoEstoque();
		
		this.movimentoEstoqueRepository.alterar(movimentoEstoque);
		
		BigInteger diferencaQtdItens = oldQtdeMovEstoque.subtract(newQtdeMovEstoque);
		
		if(diferencaQtdItens.compareTo(BigInteger.ZERO) == 0) {
			return;
		}
		
		OperacaoEstoque operacaoEstoque = null;
		
		if(BigIntegerUtil.isMaiorQueZero(diferencaQtdItens)) {
			operacaoEstoque = OperacaoEstoque.SAIDA;
		} else {
			operacaoEstoque = OperacaoEstoque.ENTRADA;
		}
		
		diferencaQtdItens = diferencaQtdItens.abs();
		
		enfileirarAlteracaoEncalheEstoqueProduto(cota, movimentoEstoque.getProdutoEdicao(), grupoMovimentoEstoque, operacaoEstoque, diferencaQtdItens);
		
	}
    

    private Long atualizarEstoqueProduto(final TipoMovimentoEstoque tipoMovimentoEstoque,
            final MovimentoEstoque movimentoEstoque, final boolean isImportacao,
            final boolean validarTransfEstoqueDiferenca) {
        
        if (StatusAprovacao.APROVADO.equals(movimentoEstoque.getStatus())) {
            
            final Long idProdutoEdicao = movimentoEstoque.getProdutoEdicao().getId();
            
            EstoqueProduto estoqueProduto =
                    estoqueProdutoRespository.buscarEstoqueProdutoPorProdutoEdicao(idProdutoEdicao);
            
            if (estoqueProduto == null) {
                
                estoqueProduto = new EstoqueProduto();
                
                final ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
                
                estoqueProduto.setProdutoEdicao(produtoEdicao);
                
                estoqueProduto.setQtde(BigInteger.ZERO);
            }
            
            estoqueProduto.setQtde(estoqueProduto.getQtde() != null ? estoqueProduto.getQtde() : BigInteger.ZERO);
            
            final TipoEstoque tipoEstoque = tipoMovimentoEstoque.getGrupoMovimentoEstoque().getTipoEstoque();
            
            if (TipoEstoque.COTA.equals(tipoEstoque)) {
                
                return estoqueProduto.getId();
            }
            
            BigInteger novaQuantidade = BigInteger.ZERO;
            
            BigInteger novaQuantidadeSomatorioEstoque = BigInteger.ZERO;
            
            final boolean isOperacaoEntrada = OperacaoEstoque.ENTRADA.equals(tipoMovimentoEstoque.getOperacaoEstoque());
            
            switch (tipoEstoque) {
            
            case LANCAMENTO:
                
                novaQuantidade = isOperacaoEntrada ? estoqueProduto.getQtde().add(movimentoEstoque.getQtde()) :
                    estoqueProduto.getQtde().subtract(movimentoEstoque.getQtde());
                
                estoqueProduto.setQtde(novaQuantidade);
                
                break;
                
            case PRODUTOS_DANIFICADOS:
                
                final BigInteger qtdeDanificado = estoqueProduto.getQtdeDanificado() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDanificado();
                
                novaQuantidade = isOperacaoEntrada ? qtdeDanificado.add(movimentoEstoque.getQtde()) :
                    qtdeDanificado.subtract(movimentoEstoque.getQtde());
                
                estoqueProduto.setQtdeDanificado(novaQuantidade);
                
                break;
                
            case DEVOLUCAO_ENCALHE:
                
                final BigInteger qtdeEncalhe = estoqueProduto.getQtdeDevolucaoEncalhe() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
                
                novaQuantidade = isOperacaoEntrada ? qtdeEncalhe.add(movimentoEstoque.getQtde()) :
                    qtdeEncalhe.subtract(movimentoEstoque.getQtde());
                
                estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
                
                break;
                
            case DEVOLUCAO_FORNECEDOR:
                
                final BigInteger qtdeDevolucaoFornecedor = estoqueProduto.getQtdeDevolucaoFornecedor() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoFornecedor();
                
                novaQuantidade = qtdeDevolucaoFornecedor.add(movimentoEstoque.getQtde());
                
                BigInteger _qtdeLancamento = estoqueProduto.getQtde() == null ? BigInteger.ZERO : estoqueProduto.getQtde();
                BigInteger _qtdeSuplementar = estoqueProduto.getQtdeSuplementar() == null ? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
                BigInteger _qtdeDevolucaoEncalhe = estoqueProduto.getQtdeDevolucaoEncalhe() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
                
                novaQuantidadeSomatorioEstoque = _qtdeLancamento.add(_qtdeSuplementar).add(_qtdeDevolucaoEncalhe);
                novaQuantidadeSomatorioEstoque = novaQuantidadeSomatorioEstoque.subtract(movimentoEstoque.getQtde());
                
                BigInteger totalValorSubtrairDoEstoque = movimentoEstoque.getQtde();
                BigInteger valorSubtrairDoEstoque = BigInteger.ZERO;
                
                valorSubtrairDoEstoque = subtrairDoEstoque(_qtdeLancamento, totalValorSubtrairDoEstoque);
                _qtdeLancamento = _qtdeLancamento.subtract(valorSubtrairDoEstoque);
                totalValorSubtrairDoEstoque = totalValorSubtrairDoEstoque.subtract(valorSubtrairDoEstoque);
                
                valorSubtrairDoEstoque = subtrairDoEstoque(_qtdeSuplementar, totalValorSubtrairDoEstoque);
                _qtdeSuplementar = _qtdeSuplementar.subtract(valorSubtrairDoEstoque);
                totalValorSubtrairDoEstoque = totalValorSubtrairDoEstoque.subtract(valorSubtrairDoEstoque);
                
                valorSubtrairDoEstoque = subtrairDoEstoque(_qtdeDevolucaoEncalhe, totalValorSubtrairDoEstoque);
                _qtdeDevolucaoEncalhe = _qtdeDevolucaoEncalhe.subtract(valorSubtrairDoEstoque);
                
                estoqueProduto.setQtde(_qtdeLancamento);
                estoqueProduto.setQtdeSuplementar(_qtdeSuplementar);
                estoqueProduto.setQtdeDevolucaoEncalhe(_qtdeDevolucaoEncalhe);
                estoqueProduto.setQtdeDevolucaoFornecedor(novaQuantidade);
                
                break;
                
            case SUPLEMENTAR:
                
                final BigInteger qtdeSuplementar = estoqueProduto.getQtdeSuplementar() == null ? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
                
                novaQuantidade = isOperacaoEntrada ? qtdeSuplementar.add(movimentoEstoque.getQtde()) :
                    qtdeSuplementar.subtract(movimentoEstoque.getQtde());
                
                estoqueProduto.setQtdeSuplementar(novaQuantidade);
                
                break;
                
            case RECOLHIMENTO:
                
                final BigInteger qtdeRecolhimento = estoqueProduto.getQtdeDevolucaoEncalhe() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
                
                novaQuantidade = isOperacaoEntrada ? qtdeRecolhimento.add(movimentoEstoque.getQtde()) :
                    qtdeRecolhimento.subtract(movimentoEstoque.getQtde());
                
                estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
                
                break;
                
            case JURAMENTADO:
                
                final BigInteger qtde = estoqueProduto.getQtde() == null ? BigInteger.ZERO : estoqueProduto.getQtde();
                
                novaQuantidade = isOperacaoEntrada ? 	qtde.add(movimentoEstoque.getQtde()) :
                    qtde.subtract(movimentoEstoque.getQtde());
                
                estoqueProduto.setQtde(novaQuantidade);
                
                break;
                
            case PERDA:
                
                BigInteger qtdePerda = estoqueProduto.getQtdePerda() == null ? BigInteger.ZERO : estoqueProduto.getQtdePerda();
                
                if (movimentoEstoque.getOrigem()!=null && movimentoEstoque.getOrigem().equals(Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_FECHAMENTO_ENCALHE)){
                    
                    if (estoqueProduto.getQtdeDevolucaoEncalhe()==null){
                        
                        estoqueProduto.setQtdeDevolucaoEncalhe(BigInteger.ZERO);
                    }
                    
                    novaQuantidade = estoqueProduto.getQtdeDevolucaoEncalhe().subtract(movimentoEstoque.getQtde());
                    
                    estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
                }
                // Se a origem para lançamento de PERDA for direcionada para
                // Cota não deve movimentar estoque de lançamento
                else if (movimentoEstoque.getOrigem() == null
                        || !movimentoEstoque.getOrigem().equals(Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_COTA)){
                    
                    if (estoqueProduto.getQtde()==null){
                        
                        estoqueProduto.setQtde(BigInteger.ZERO);
                    }
                    
                    novaQuantidade = estoqueProduto.getQtde().subtract(movimentoEstoque.getQtde());
                    
                    estoqueProduto.setQtde(novaQuantidade);
                }
                
                qtdePerda = qtdePerda.add( movimentoEstoque.getQtde());
                
                estoqueProduto.setQtdePerda(qtdePerda);
                
                break;
                
            case GANHO:
                
                BigInteger qtdeGanho = estoqueProduto.getQtdeGanho() == null ? BigInteger.ZERO : estoqueProduto.getQtdeGanho();
                
                if (movimentoEstoque.getOrigem()!=null && movimentoEstoque.getOrigem().equals(Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_FECHAMENTO_ENCALHE)){
                    
                    if (estoqueProduto.getQtdeDevolucaoEncalhe()==null){
                        
                        estoqueProduto.setQtdeDevolucaoEncalhe(BigInteger.ZERO);
                    }
                    
                    novaQuantidade = estoqueProduto.getQtdeDevolucaoEncalhe().add(movimentoEstoque.getQtde());
                    
                    estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
                }
                // Se a origem para lançamento de GANHO for direcionada para
                // Cota não deve movimentar estoque de lançamento
                else if (movimentoEstoque.getOrigem() == null
                        || !movimentoEstoque.getOrigem().equals(Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_COTA)){
                    
                    if (estoqueProduto.getQtde()==null){
                        
                        estoqueProduto.setQtde(BigInteger.ZERO);
                    }
                    
                    novaQuantidade = estoqueProduto.getQtde().add(movimentoEstoque.getQtde());
                    
                    estoqueProduto.setQtde(novaQuantidade);
                }
                
                qtdeGanho = qtdeGanho.add(movimentoEstoque.getQtde());
                
                estoqueProduto.setQtdeGanho(qtdeGanho);
                
                break;
                
            default:
                
                throw new ValidacaoException(TipoMensagem.WARNING, "Estoque inválido para a operação.");
            }
            
            // Caso seja importação, deve inserir mesmo se o estoque ficar
            // negativo - Definido em conjunto com Cesar Pop Punk
            if (!isImportacao && !TipoEstoque.DEVOLUCAO_FORNECEDOR.equals(tipoEstoque)) {
                this.validarAlteracaoEstoqueProdutoDistribuidor(
                        novaQuantidade, tipoEstoque, estoqueProduto.getProdutoEdicao(),
                        validarTransfEstoqueDiferenca);
            }
            
            if(!isImportacao && TipoEstoque.DEVOLUCAO_FORNECEDOR.equals(tipoEstoque)) {
                this.validarAlteracaoEstoqueProdutoDistribuidorParaDevolucaoFornecedor(
                        novaQuantidadeSomatorioEstoque, estoqueProduto.getProdutoEdicao(),
                        validarTransfEstoqueDiferenca);
            }
			
            if (estoqueProduto.getId() == null) {
                return estoqueProdutoRespository.adicionar(estoqueProduto);
            } else {
                estoqueProdutoRespository.merge(estoqueProduto);
                return estoqueProduto.getId();
            }
            
        }
        
        return null;
    }
    
    private BigInteger subtrairDoEstoque(final BigInteger qtdeEstoque, final BigInteger qtdeSubtrairDoEstoque) {
        
        if( BigInteger.ZERO.compareTo(qtdeEstoque) >= 0 ) {
            
            return BigInteger.ZERO;
            
        }
        
        if(qtdeEstoque.compareTo(qtdeSubtrairDoEstoque) > 0) {
            
            return qtdeSubtrairDoEstoque;
            
        } else {
            
            return qtdeEstoque;
            
        }
        
        
    }
    
    private void validarAlteracaoEstoqueProdutoDistribuidorParaDevolucaoFornecedor(
            final BigInteger saldoEstoque, final ProdutoEdicao produtoEdicao, final boolean validarTransfEstoqueDiferenca) {
        
        if (validarTransfEstoqueDiferenca
                && !this.validarSaldoEstoque(saldoEstoque)) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Saldo do produto ["
                            + produtoEdicao.getProduto().getCodigo() + " - "
                            + produtoEdicao.getProduto().getNomeComercial()
                            + " - " + produtoEdicao.getNumeroEdicao()
                + "] nos estoques \"Lançamento, Devolução Encalhe e Suplementar\", "
                + "insuficientes para movimentação.");
        }
    }
    
    
    private void validarAlteracaoEstoqueProdutoDistribuidor(final BigInteger saldoEstoque,
            final TipoEstoque tipoEstoque,
            final ProdutoEdicao produtoEdicao,
            final boolean validarTransfEstoqueDiferenca) {
        
        if (validarTransfEstoqueDiferenca && !this.validarSaldoEstoque(saldoEstoque)) {
            
            throw new ValidacaoException(
                    TipoMensagem.WARNING,
                    "Saldo do produto [" + produtoEdicao.getProduto().getCodigo()
                    + " - " + produtoEdicao.getProduto().getNomeComercial() + " - "
                    + produtoEdicao.getNumeroEdicao()
                    + "] no estoque \"" + tipoEstoque.getDescricao()
                + "\", insuficiente para movimentação.", CodigoErro.SALDO_ESTOQUE_DISTRIBUIDOR_INSUFICIENTE);
        }
    }
    
    private void validarAlteracaoEstoqueProdutoCota(final BigInteger saldoEstoque,
            final ProdutoEdicao produtoEdicao) {
        
        if (!this.validarSaldoEstoque(saldoEstoque)) {
            
            throw new ValidacaoException(
                    TipoMensagem.WARNING,
                    "Saldo do produto [" + produtoEdicao.getProduto().getCodigo()
                    + " - " + produtoEdicao.getProduto().getNomeComercial() + " - "
                    + produtoEdicao.getNumeroEdicao()
 + "] no estoque da cota, insuficiente para movimentação.");
        }
    }
    
    private boolean validarSaldoEstoque(final BigInteger saldoEstoque) {
        
        return (saldoEstoque != null && saldoEstoque.compareTo(BigInteger.ZERO) >= 0);
    }
    
    @Override
    @Transactional
    public MovimentoEstoqueCota gerarMovimentoCota(final Date dataLancamento,
            final Long idProdutoEdicao, final Long idCota, final Long idUsuario,
            final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque, final Date dataOperacao) {
        
        return criarMovimentoCota(dataLancamento, idProdutoEdicao, idCota, idUsuario, quantidade, tipoMovimentoEstoque, null, dataOperacao, null, null, false);
        
    }
    
    @Override
    @Transactional
    public MovimentoEstoqueCota gerarMovimentoCotaDiferenca(final Date dataLancamento,final Long idProdutoEdicao,
            final Long idCota, final Long idUsuario,
            final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque,
            final Long idEstudoCota,
            final boolean isMovimentoDiferencaAutomatico) {
        
        return criarMovimentoCota(dataLancamento, idProdutoEdicao, idCota,
                idUsuario, quantidade, tipoMovimentoEstoque, dataLancamento, null, null, idEstudoCota, isMovimentoDiferencaAutomatico);
    }
    
    
    @Override
    @Transactional
    public MovimentoEstoqueCota gerarMovimentoCota(final Date dataLancamento, final Long idProdutoEdicao, final Long idCota,
            final Long idUsuario, final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque,
            final Date dataMovimento, final Date dataOperacao, final Long idLancamento, final Long idEstudoCota){
        
        return criarMovimentoCota(dataLancamento, idProdutoEdicao, idCota, idUsuario, quantidade,
                tipoMovimentoEstoque, dataMovimento, dataOperacao, idLancamento, idEstudoCota, false);
    }
    
    private MovimentoEstoqueCota criarMovimentoCota(final Date dataLancamento, final Long idProdutoEdicao, final Long idCota,
            final Long idUsuario, final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque,
            final Date dataMovimento, Date dataOperacao, Long idLancamento, final Long idEstudoCota,final boolean isMovimentoDiferencaAutomatico) {
        
        this.validarDominioGrupoMovimentoEstoque(tipoMovimentoEstoque, Dominio.COTA);
        
        if (dataOperacao == null) {
            
            dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
        }
        
        MovimentoEstoqueCota movimentoEstoqueCota = new MovimentoEstoqueCota();
        
        movimentoEstoqueCota.setTipoMovimento(tipoMovimentoEstoque);
        movimentoEstoqueCota.setCota(new Cota(idCota));
        
        movimentoEstoqueCota.setData(dataMovimento==null? dataOperacao : dataMovimento);
        
        movimentoEstoqueCota.setDataLancamentoOriginal(dataMovimento);
        
        movimentoEstoqueCota.setDataCriacao(dataOperacao);
        movimentoEstoqueCota.setProdutoEdicao(new ProdutoEdicao(idProdutoEdicao));
        movimentoEstoqueCota.setQtde(quantidade);
        movimentoEstoqueCota.setUsuario(new Usuario(idUsuario));
        movimentoEstoqueCota.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
        
        if (idEstudoCota != null) {
            
            movimentoEstoqueCota.setEstudoCota(new EstudoCota(idEstudoCota));
        }
        
        if (dataLancamento != null && idProdutoEdicao != null) {
            
            if (idLancamento==null) {
                
                idLancamento =
                        lancamentoRepository.obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidor(
                                new ProdutoEdicao(idProdutoEdicao), null, dataLancamento);
            }
            
            
            if (idLancamento != null) {
                
                final Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
                
                movimentoEstoqueCota.setLancamento(lancamento);
                
                final ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
                
                final Desconto desconto = descontoService.obterDescontoPorCotaProdutoEdicao(lancamento, idCota, produtoEdicao);
                
                final BigDecimal precoComDesconto =
                        produtoEdicao.getPrecoVenda().subtract(
                                MathUtil.calculatePercentageValue(produtoEdicao.getPrecoVenda(), desconto.getValor()));
                
                final ValoresAplicados valoresAplicados = new ValoresAplicados();
                valoresAplicados.setPrecoVenda(produtoEdicao.getPrecoVenda());
                valoresAplicados.setValorDesconto(desconto.getValor());
                valoresAplicados.setPrecoComDesconto(precoComDesconto);
                
                movimentoEstoqueCota.setValoresAplicados(valoresAplicados);
                
            }
        }
        
        if (tipoMovimentoEstoque.isAprovacaoAutomatica() || isMovimentoDiferencaAutomatico) {
            
            movimentoEstoqueCota.setStatus(StatusAprovacao.APROVADO);
            movimentoEstoqueCota.setAprovador(new Usuario(idUsuario));
            movimentoEstoqueCota.setDataAprovacao(dataOperacao);
            
            movimentoEstoqueCota = movimentoEstoqueCotaRepository.merge(movimentoEstoqueCota);
            
            final Long idEstoqueCota = this.atualizarEstoqueProdutoCota(tipoMovimentoEstoque, movimentoEstoqueCota);
            
            movimentoEstoqueCota.setEstoqueProdutoCota(new EstoqueProdutoCota(idEstoqueCota));
            
        } else {
            
            movimentoEstoqueCota = movimentoEstoqueCotaRepository.merge(movimentoEstoqueCota);
        }
        
        return movimentoEstoqueCota;
    }
    
    @Override
    @Transactional
    public Long atualizarEstoqueProdutoCota(final TipoMovimentoEstoque tipoMovimentoEstoque,
            final MovimentoEstoqueCota movimentoEstoqueCota) {
        
        if (StatusAprovacao.APROVADO.equals(movimentoEstoqueCota.getStatus())) {
            
            final Long idCota = movimentoEstoqueCota.getCota().getId();
            final Long idProdutoEd = movimentoEstoqueCota.getProdutoEdicao().getId();
            
            EstoqueProdutoCota estoqueProdutoCota =
                    estoqueProdutoCotaRepository.buscarEstoquePorProdutoECota(
                            idProdutoEd, idCota);
            
            if (estoqueProdutoCota == null) {
                
                estoqueProdutoCota = new EstoqueProdutoCota();
                
                final ProdutoEdicao produtoEdicao =
                        produtoEdicaoRepository.buscarPorId(idProdutoEd);
                
                estoqueProdutoCota.setProdutoEdicao(produtoEdicao);
                estoqueProdutoCota.setQtdeDevolvida(BigInteger.ZERO);
                estoqueProdutoCota.setQtdeRecebida(BigInteger.ZERO);
                estoqueProdutoCota.setCota(new Cota(idCota));
            }
            
            BigInteger novaQuantidade;
            
            BigInteger quantidadeRecebida;
            
            BigInteger quantidadeDevolvida;
            
            if (OperacaoEstoque.ENTRADA.equals(tipoMovimentoEstoque.getOperacaoEstoque())) {
                
                quantidadeRecebida = (estoqueProdutoCota.getQtdeRecebida() != null)
                        ? estoqueProdutoCota.getQtdeRecebida() : BigInteger.ZERO;
                        
                        novaQuantidade = quantidadeRecebida.add(movimentoEstoqueCota.getQtde());
                        
                        estoqueProdutoCota.setQtdeRecebida(novaQuantidade);
                        
            } else {
                
                quantidadeDevolvida = (estoqueProdutoCota.getQtdeDevolvida() != null)
                        ? estoqueProdutoCota.getQtdeDevolvida() : BigInteger.ZERO;
                        
                        novaQuantidade = quantidadeDevolvida.add(movimentoEstoqueCota.getQtde());
                        
                        estoqueProdutoCota.setQtdeDevolvida(novaQuantidade);
            }
            
            this.validarAlteracaoEstoqueProdutoCota(
                    novaQuantidade, estoqueProdutoCota.getProdutoEdicao());
            
            if (estoqueProdutoCota.getId() == null) {
                
                return estoqueProdutoCotaRepository.adicionar(estoqueProdutoCota);
                
            } else {
                
                estoqueProdutoCotaRepository.alterar(estoqueProdutoCota);
                
                return estoqueProdutoCota.getId();
            }
        }
        
        return null;
    }
    
    /*
     * Atualiza o estoque do produto da cota juramentado.
     */
    @Override
    @Transactional
    public EstoqueProdutoCotaJuramentado atualizarEstoqueProdutoCotaJuramentado(final MovimentoEstoqueCota movimentoEstoqueCota,
            final TipoMovimentoEstoque tipoMovimentoEstoque) {
        
        final Long idProdutoEdicao = movimentoEstoqueCota.getProdutoEdicao().getId();
        final Long idCota = movimentoEstoqueCota.getCota().getId();
        
        EstoqueProdutoCotaJuramentado estoqueProdutoCotaJuramentado =
                estoqueProdutoCotaJuramentadoRepository.buscarEstoquePorProdutoECotaNaData(
                        idProdutoEdicao, idCota, new Date());
        
        if (estoqueProdutoCotaJuramentado == null) {
            
            final ProdutoEdicao produtoEdicao =
                    produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
            
            final Cota cota = cotaRepository.buscarPorId(idCota);
            
            estoqueProdutoCotaJuramentado = new EstoqueProdutoCotaJuramentado();
            
            estoqueProdutoCotaJuramentado.setProdutoEdicao(produtoEdicao);
            estoqueProdutoCotaJuramentado.setCota(cota);
            estoqueProdutoCotaJuramentado.setData(new Date());
        }
        
        final BigInteger qtdeAtual =
                (estoqueProdutoCotaJuramentado.getQtde() == null)
                ? BigInteger.ZERO : estoqueProdutoCotaJuramentado.getQtde();
        
        final BigInteger qtdeMovimento =
                (movimentoEstoqueCota.getQtde() == null) ? BigInteger.ZERO : movimentoEstoqueCota.getQtde();
        
        if (OperacaoEstoque.ENTRADA.equals(tipoMovimentoEstoque.getOperacaoEstoque())) {
            
            estoqueProdutoCotaJuramentado.setQtde(qtdeAtual.add(qtdeMovimento));
            
        } else {
            
            estoqueProdutoCotaJuramentado.setQtde(qtdeAtual.subtract(qtdeMovimento));
        }
        
        this.validarAlteracaoEstoqueProdutoCota(
                estoqueProdutoCotaJuramentado.getQtde(),
                estoqueProdutoCotaJuramentado.getProdutoEdicao());
        
        estoqueProdutoCotaJuramentado.getMovimentos().add(movimentoEstoqueCota);
        
        estoqueProdutoCotaJuramentado = estoqueProdutoCotaJuramentadoRepository.merge(estoqueProdutoCotaJuramentado);
        
        return estoqueProdutoCotaJuramentado ;
    }
    
    @Override
    @Transactional
    public void processarRegistroHistoricoVenda(final HistoricoVendaInput vendaInput, final Date dataOperacao) {
        
        final Integer reparte = vendaInput.getQtdReparte();
        final Integer encalhe = vendaInput.getQtdEncalhe();
        
        final ProdutoEdicao edicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
                vendaInput.getCodigoProduto().toString(), vendaInput.getNumeroEdicao().longValue());
        
        if(edicao == null) {
            throw new ImportacaoException("Edição " + vendaInput.getNumeroEdicao()
                    + " inexistente para produto : " + vendaInput.getCodigoProduto().toString());
        }
        
        final Cota cota = cotaRepository.obterPorNumeroDaCota(vendaInput.getNumeroCota());
        
        if(cota == null) {
            throw new ImportacaoException("Cota " + vendaInput.getNumeroCota() + " inexistente.");
        }
        
        final Long idUsuario = usuarioRepository.getUsuarioImportacao().getId();
        
        persistirRegistroVendaHistoricoReparte(idUsuario, reparte, edicao, cota, dataOperacao);
        
        persistirRegistroVendaHistoricoEncalhe(idUsuario, encalhe, edicao, cota, dataOperacao);
        
    }
    
	            /**
     * Persistem os dados de reparte de histórico de vendas
     * 
     * @param idUsuario
     * @param reparte
     * @param edicao
     * @param cota
     * @param dataOperacao
     */
    private void persistirRegistroVendaHistoricoReparte(final Long idUsuario, final Integer reparte, final ProdutoEdicao edicao, final Cota cota, final Date dataOperacao){
        
        if(reparte != null && reparte>0) {
            
            TipoMovimentoEstoque tipoMovimentoEnvioReparte =
                    tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
            
            if(tipoMovimentoEnvioReparte == null){
                
                tipoMovimentoEnvioReparte = new TipoMovimentoEstoque();
                tipoMovimentoEnvioReparte.setAprovacaoAutomatica(true);
                tipoMovimentoEnvioReparte.setDescricao("Envio a Jornaleiro");
                tipoMovimentoEnvioReparte.setIncideDivida(true);
                tipoMovimentoEnvioReparte.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
                
                tipoMovimentoEstoqueRepository.adicionar(tipoMovimentoEnvioReparte);
            }
            
            TipoMovimentoEstoque tipoMovimentoRecebimentoReparte =
                    tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
            
            if(tipoMovimentoRecebimentoReparte == null){
                
                tipoMovimentoRecebimentoReparte  = new TipoMovimentoEstoque();
                tipoMovimentoRecebimentoReparte.setAprovacaoAutomatica(true);
                tipoMovimentoRecebimentoReparte.setDescricao("Recebimento Reparte");
                tipoMovimentoRecebimentoReparte.setIncideDivida(true);
                tipoMovimentoRecebimentoReparte.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
                
                tipoMovimentoEstoqueRepository.adicionar(tipoMovimentoRecebimentoReparte);
            }
            
            gerarMovimentoEstoque(edicao.getId(), idUsuario, BigInteger.valueOf(reparte), tipoMovimentoEnvioReparte, dataOperacao, true);
            
            gerarMovimentoCota(null, edicao.getId(), cota.getId(), idUsuario, BigInteger.valueOf(reparte), tipoMovimentoRecebimentoReparte, dataOperacao);
        }
    }
    
	            /**
     * Persistem os dados de encalhe de histórico de vendas
     * 
     * @param idUsuario
     * @param encalhe
     * @param edicao
     * @param cota
     */
    private void persistirRegistroVendaHistoricoEncalhe(final Long idUsuario, final Integer encalhe, final ProdutoEdicao edicao, final Cota cota,
            final Date dataOperacao){
        
        if(encalhe != null && encalhe>0) {
            
            TipoMovimentoEstoque tipoMovimentoEnvioEncalhe =
                    tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_ENCALHE);
            
            if(tipoMovimentoEnvioEncalhe == null){
                
                tipoMovimentoEnvioEncalhe = new TipoMovimentoEstoque();
                tipoMovimentoEnvioEncalhe.setAprovacaoAutomatica(true);
                tipoMovimentoEnvioEncalhe.setDescricao("Envio Encalhe - Estoque");
                tipoMovimentoEnvioEncalhe.setIncideDivida(true);
                tipoMovimentoEnvioEncalhe.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_ENCALHE);
                
                tipoMovimentoEstoqueRepository.adicionar(tipoMovimentoEnvioEncalhe);
            }
            
            TipoMovimentoEstoque tipoMovimentoRecebimentoEncalhe =
                    tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE);
            
            if(tipoMovimentoRecebimentoEncalhe  == null){
                
                tipoMovimentoRecebimentoEncalhe = new TipoMovimentoEstoque();
                tipoMovimentoRecebimentoEncalhe.setAprovacaoAutomatica(true);
                tipoMovimentoRecebimentoEncalhe.setDescricao("Recebimento Encalhe");
                tipoMovimentoRecebimentoEncalhe.setIncideDivida(true);
                tipoMovimentoRecebimentoEncalhe.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE);
                
                tipoMovimentoEstoqueRepository.adicionar(tipoMovimentoRecebimentoEncalhe);
            }
            
            gerarMovimentoEstoque(edicao.getId(), idUsuario, BigInteger.valueOf(encalhe), tipoMovimentoRecebimentoEncalhe, dataOperacao, true);
            
            gerarMovimentoCota(null, edicao.getId(), cota.getId(), idUsuario, BigInteger.valueOf(encalhe), tipoMovimentoEnvioEncalhe, dataOperacao);
        }
    }
    
    /* (non-Javadoc)
     * @see br.com.abril.nds.service.MovimentoEstoqueService#devolverConsignadoNotaCanceladaParaDistribuidor(br.com.abril.nds.model.fiscal.nota.NotaFiscal)
     */
    @Override
    @Transactional
    public void devolucaoConsignadoNotaCancelada(final NotaFiscal notaFiscalCancelada) {
        
        final TipoMovimentoEstoque tipoMovimento = tipoMovimentoEstoqueRepository.
                buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.CANCELAMENTO_NOTA_FISCAL_DEVOLUCAO_CONSIGNADO);
        
        gerarMovimentoCancelamentoNotaFiscal(notaFiscalCancelada, tipoMovimento);
    }
    
    /* (non-Javadoc)
     * @see br.com.abril.nds.service.MovimentoEstoqueService#devolucaoRecolhimentoNotaCancelada(br.com.abril.nds.model.fiscal.nota.NotaFiscal)
     */
    @Override
    @Transactional
    public void devolucaoRecolhimentoNotaCancelada(final NotaFiscal notaFiscalCancelada) {
        
        final TipoMovimentoEstoque tipoMovimento = tipoMovimentoEstoqueRepository.
                buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.CANCELAMENTO_NOTA_FISCAL_DEVOLUCAO_ENCALHE);
        
        gerarMovimentoCancelamentoNotaFiscal(notaFiscalCancelada, tipoMovimento);
    }
    
    private void gerarMovimentoCancelamentoNotaFiscal(final NotaFiscal notaFiscalCancelada,
            final TipoMovimentoEstoque tipoMovimento) {
        final List<DetalheNotaFiscal> listaProdutosServicosNotaCancelada = notaFiscalCancelada.getNotaFiscalInformacoes().getDetalhesNotaFiscal();
        
        final Long idUsuario = usuarioService.getUsuarioLogado().getId();
        
        for (final DetalheNotaFiscal detalheNotaFiscal : listaProdutosServicosNotaCancelada) {
            
            this.criarMovimentoEstoque(null,
					detalheNotaFiscal.getProdutoServico().getProdutoEdicao().getId(), idUsuario, detalheNotaFiscal.getProdutoServico().getQuantidade(), tipoMovimento,null, null, false,false, true, null, false, null);
        }
    }
    
    @Override
    @Transactional
    public BigInteger obterReparteDistribuidoProduto(final String produtoEdicaoId){
        return movimentoEstoqueRepository.obterReparteDistribuidoProduto(produtoEdicaoId);
    }
    
    private void validarDominioGrupoMovimentoEstoque(final TipoMovimentoEstoque tipoMovimentoEstoque,
            final Dominio dominio) {
        
        if (tipoMovimentoEstoque != null
                && dominio != null
                && !dominio.equals(tipoMovimentoEstoque.getGrupoMovimentoEstoque().getDominio())) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Domínio do grupo de movimento de estoque inválido");
        }
    }
    
    @Override
    @Transactional
    public MovimentoEstoqueCotaDTO criarMovimentoExpedicaoCota(final Date dataLancamento, final Long idProdutoEdicao, final Long idCota,
            final Long idUsuario, final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque,
            final Date dataMovimento, Date dataOperacao, Long idLancamento, final Long idEstudoCota, final Map<String, DescontoDTO> descontos, final boolean isMovimentoDiferencaAutomatico) {
        
        this.validarDominioGrupoMovimentoEstoque(tipoMovimentoEstoque, Dominio.COTA);
        
        if (dataOperacao == null) {
            
            dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
        }
        
        final MovimentoEstoqueCotaDTO movimentoEstoqueCota = new MovimentoEstoqueCotaDTO();
        
        movimentoEstoqueCota.setTipoMovimentoId(tipoMovimentoEstoque.getId());
        movimentoEstoqueCota.setIdCota(idCota);
        
        movimentoEstoqueCota.setData(dataMovimento==null? dataOperacao : dataMovimento);
        
        movimentoEstoqueCota.setDataLancamentoOriginal(dataMovimento);
        
        movimentoEstoqueCota.setDataCriacao(dataOperacao);
        movimentoEstoqueCota.setIdProdEd(idProdutoEdicao);
        movimentoEstoqueCota.setQtde(quantidade);
        movimentoEstoqueCota.setUsuarioId(idUsuario);
        movimentoEstoqueCota.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
        
        if (idEstudoCota != null) {
            
            movimentoEstoqueCota.setEstudoCotaId(idEstudoCota);
        }
        
        if (dataLancamento != null && idProdutoEdicao != null) {
            
            if (idLancamento==null) {
                
                idLancamento = lancamentoRepository.obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidor(
                        new ProdutoEdicao(idProdutoEdicao), null, dataLancamento);
            }
            
            
            if (idLancamento != null) {
                
                final Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
                
                movimentoEstoqueCota.setLancamentoId(lancamento.getId());
                
                final ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
                
				                                                /**
                 * A busca dos descontos é feita diretamente no Map, por chave,
                 * agilizando o retorno do resultado
                 */
                DescontoDTO descontoDTO = null;
                try {
                    descontoDTO = descontoService.obterDescontoPor(descontos, idCota, produtoEdicao.getProduto().getFornecedor().getId(), produtoEdicao.getProduto().getId(), produtoEdicao.getId());
                } catch (final Exception e) {
                    final String msg = "Produto sem desconto: " + produtoEdicao.getProduto().getCodigo() + " / "
                            + produtoEdicao.getNumeroEdicao();
                    LOGGER.error(msg, e);
                    throw new ValidacaoException(TipoMensagem.ERROR, msg);
                }
                
                final BigDecimal desconto = descontoDTO != null ? descontoDTO.getValor() : BigDecimal.ZERO;
                
                final BigDecimal precoComDesconto =
                        produtoEdicao.getPrecoVenda().subtract(MathUtil.calculatePercentageValue(produtoEdicao.getPrecoVenda(), desconto));
                
                movimentoEstoqueCota.setPrecoVenda(produtoEdicao.getPrecoVenda());
                movimentoEstoqueCota.setPrecoComDesconto(precoComDesconto);
                movimentoEstoqueCota.setValorDesconto(desconto);
                
            }
        }
        
        if (tipoMovimentoEstoque.isAprovacaoAutomatica() || isMovimentoDiferencaAutomatico) {
            
            movimentoEstoqueCota.setStatus(StatusAprovacao.APROVADO.name());
            movimentoEstoqueCota.setUsuarioAprovadorId(idUsuario);
            movimentoEstoqueCota.setDataAprovacao(dataOperacao);
            
            //movimentoEstoqueCota = movimentoEstoqueCotaRepository.merge(movimentoEstoqueCota);
            
            // Necessario para gerar o id do estoque da cota utilizado na transacao
            final MovimentoEstoqueCota mec = new MovimentoEstoqueCota();
            mec.setCota(new Cota(idCota));
            mec.setProdutoEdicao(new ProdutoEdicao(idProdutoEdicao));
            mec.setStatus(StatusAprovacao.APROVADO);
            mec.setQtde(quantidade);
            final Long idEstoqueCota = this.atualizarEstoqueProdutoCota(tipoMovimentoEstoque, mec);
            
            movimentoEstoqueCota.setEstoqueProdutoEdicaoCotaId(idEstoqueCota);
            
        } else {
            
            //movimentoEstoqueCota = movimentoEstoqueCotaRepository.merge(movimentoEstoqueCota);
        }
        
        return movimentoEstoqueCota;
    }
    
    @Override
    @Transactional
    public MovimentoEstoque obterMovimentoEstoqueDoItemNotaFiscal(final Long idItemNotaFiscal, final TipoMovimentoEstoque tipoMovimento) {
        
        return movimentoEstoqueRepository.obterMovimentoEstoqueDoItemNotaFiscal(
                idItemNotaFiscal, tipoMovimento);
        
    }
    
    @Override
    @Transactional
    public List<Long> obterMovimentosRepartePromocionalSemEstornoRecebimentoFisico(
            final Long idProdutoEdicao,
            final GrupoMovimentoEstoque grupoMovimentoEstoqueRepartePromocional,
            final GrupoMovimentoEstoque grupoMovimentoEstoqueEstornoRecebimentoFisico){
        
        return movimentoEstoqueRepository.obterMovimentosRepartePromocionalSemEstornoRecebimentoFisico(idProdutoEdicao, grupoMovimentoEstoqueRepartePromocional,
                grupoMovimentoEstoqueEstornoRecebimentoFisico);
        
        
    }
    
}
