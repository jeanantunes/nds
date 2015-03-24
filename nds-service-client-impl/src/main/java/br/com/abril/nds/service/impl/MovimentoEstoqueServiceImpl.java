package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.FormaCobrancaFornecedorDTO;
import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.MovimentoEstoqueDTO;
import br.com.abril.nds.dto.MovimentosEstoqueCotaSaldoDTO;
import br.com.abril.nds.enums.CodigoErro;
import br.com.abril.nds.enums.Dominio;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.EstoqueProdutoFila;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
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
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoFilaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.exception.TipoMovimentoEstoqueInexistenteException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.service.validation.CobrancaFornecedorValidator;
import br.com.abril.nds.strategy.importacao.input.HistoricoVendaInput;
import br.com.abril.nds.util.BigIntegerUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.Util;



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
    private UsuarioService usuarioService;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private EstoqueProdutoService estoqueProdutoService; 
    
    @Autowired
    private CobrancaFornecedorValidator cobrancaFornecedorValidator;
    
    @Autowired
    private FormaCobrancaRepository formaCobrancaRepository;

    @Override
    @Transactional
    public void gerarMovimentoEstoqueFuroPublicacao(final Lancamento lancamento, final FuroProduto furoProduto, final Long idUsuario) {
        
        final Long idProdutoEdicao = lancamento.getProdutoEdicao().getId();
        
        final TipoMovimentoEstoque tipoMovimento = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO);
        
        final TipoMovimentoEstoque tipoMovimentoCota = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO);
        
        final TipoMovimentoEstoque tipoMovimentoEstCotaAusente = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
        
        BigInteger total = BigInteger.ZERO;
        
        final List<MovimentoEstoqueCota> listaMovimentoEstoqueCotas = lancamentoRepository.buscarMovimentosEstoqueCotaParaFuro(lancamento, tipoMovimentoCota);
        
        MovimentoEstoqueCota movimento = null;
        
        for (final MovimentoEstoqueCota movimentoEstoqueCota : listaMovimentoEstoqueCotas) {
            
            movimento = (MovimentoEstoqueCota) movimentoEstoqueCota.clone();
            
            movimento = gerarMovimentoCota(
            		null, 
            		idProdutoEdicao, 
            		movimento.getCota().getId(), 
            		idUsuario,
                    movimento.getQtde(), 
                    tipoMovimentoCota, 
                    lancamento.getDataLancamentoDistribuidor(), 
                    null, 
                    lancamento.getId(), 
                    null, FormaComercializacao.CONSIGNADO);
            
            if (movimentoEstoqueCota.getTipoMovimento() != tipoMovimentoEstCotaAusente) {
                
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
    public List<MovimentoEstoqueCotaDTO> gerarMovimentoEstoqueDeExpedicao(final LancamentoDTO lancamento, final ExpedicaoDTO expedicao) {
        
        final List<EstudoCotaDTO> listaEstudoCota = estudoCotaRepository.obterEstudoCotaPorDataProdutoEdicao(lancamento.getId(), lancamento.getIdProdutoEdicao());
        
        Distribuidor distribuidor = distribuidorService.obter();
        
        BigInteger total = BigInteger.ZERO;
        
        BigInteger totalParcialJuramentado = BigInteger.ZERO;
        
        final Map<String, DescontoDTO> descontos = descontoService.obterDescontosMapPorLancamentoProdutoEdicao(lancamento.getDataDistribuidor());
        
        final DescontoProximosLancamentos descontoProximosLancamentos = 
        		descontoProximosLancamentosRepository.obterDescontoProximosLancamentosPor(lancamento.getIdProduto(), lancamento.getDataDistribuidor());
        
        final List<MovimentoEstoqueCotaDTO> movimentosEstoqueCota = new ArrayList<MovimentoEstoqueCotaDTO>();
        
        ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.buscarPorId(lancamento.getIdProdutoEdicao());
        
        tratarIncrementoProximoLancamento(descontos, descontoProximosLancamentos, null, 
                produtoEdicao.getProduto().getFornecedor().getId(), lancamento.getIdProdutoEdicao(), lancamento.getIdProduto());
        
        final List<MovimentoEstoqueCotaDTO> movimentosEstoqueCotaComProdutoContaFirme = new ArrayList<>();
        
        final boolean produtoContaFirme = (FormaComercializacao.CONTA_FIRME.equals(produtoEdicao.getProduto().getFormaComercializacao()));
        
        List<FormaCobrancaFornecedorDTO> formasCobrancaCotaFornecedor = formaCobrancaRepository.obterFormasCobrancaCotaFornecedor();
        formasCobrancaCotaFornecedor.addAll(formaCobrancaRepository.obterFormasCobrancaDistribuidorFornecedor());
        ordenarFormasCobrancaCotaFornecedor(formasCobrancaCotaFornecedor);
        
        Map<Long, List<Long>> mapaFornecedorCotas = new HashMap<>();
        for(FormaCobrancaFornecedorDTO fcf : formasCobrancaCotaFornecedor) {
        	if(mapaFornecedorCotas.get(fcf.getIdFornecedor()) == null) {
        		mapaFornecedorCotas.put(fcf.getIdFornecedor(), new ArrayList<Long>());
        	}
        	
        	mapaFornecedorCotas.get(fcf.getIdFornecedor()).add(fcf.getIdCota());
        }
        
        Set<Long> cotasSemFormaCobranca = new HashSet<>();
        montarListaCotasComProblemasFormaCobranca(listaEstudoCota, produtoEdicao, formasCobrancaCotaFornecedor, mapaFornecedorCotas, cotasSemFormaCobranca);
        
        validarListaCotasCompProblemasFormaCobranca(cotasSemFormaCobranca);
        
        for (final EstudoCotaDTO estudoCota : listaEstudoCota) {
            
            if (estudoCota.getQtdeEfetiva() == null || BigInteger.ZERO.equals(estudoCota.getQtdeEfetiva())) {
                
                continue;
            }
            
            boolean formaCobrancaValida = false;
            for(FormaCobrancaFornecedorDTO fcf : formasCobrancaCotaFornecedor) {
            	
            	if(fcf.getIdCota() != null && fcf.getIdCota().equals(estudoCota.getIdCota()) 
            			&& fcf.getIdFornecedor() != null && fcf.getIdFornecedor().equals(produtoEdicao.getProduto().getFornecedor().getId())) {
            		formaCobrancaValida = true;
            		break;
            	}
            	
            	if(!formaCobrancaValida) {
            		if(fcf.getIdCota() == null && fcf.getIdFornecedor().equals(produtoEdicao.getProduto().getFornecedor().getId())) {
            			formaCobrancaValida = true;
            			break;
            		}
            	}
            }
            
            if(!formaCobrancaValida) {
            	Cota cota = this.cotaRepository.buscarPorId(estudoCota.getIdCota());
            	LOGGER.error("Erro ao obter Forma de Cobrança para Cota/Fornecedor.");
            	throw new ValidacaoException(TipoMensagem.ERROR, String.format("Erro ao obter Forma de Cobrança para Cota/Fornecedor: %s", cota.getNumeroCota()));
            }
            
            tratarIncrementoProximoLancamento(descontos, descontoProximosLancamentos, estudoCota.getIdCota(), 
                    produtoEdicao.getProduto().getFornecedor().getId(), lancamento.getIdProdutoEdicao(), lancamento.getIdProduto());
            
            final MovimentoEstoqueCotaDTO mec = criarMovimentoExpedicaoCota(
                    distribuidor, lancamento.getDataPrevista(), produtoEdicao,
                    expedicao.getIdUsuario(), expedicao.getTipoMovimentoEstoqueCota(), expedicao.getDataOperacao(),
                    expedicao.getDataOperacao(), lancamento.getId(), descontos, false, estudoCota);

            
            mec.setIdFornecedor(estudoCota.getIdFornecedorPadraoCota());
            
            if(produtoContaFirme) {
            	
            	mec.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_PROCESSADO.name());
            	
            	movimentosEstoqueCotaComProdutoContaFirme.add(mec);
            }
            
            if(TipoEstudoCota.NORMAL.equals(estudoCota.getTipoEstudo())) {
                
                total = total.add(estudoCota.getQtdeEfetiva());
            }
            else{
                
                totalParcialJuramentado = totalParcialJuramentado.add(estudoCota.getQtdeEfetiva());
            }
            
            movimentosEstoqueCota.add(mec);
        }
        
        if(total.compareTo(BigInteger.ZERO) > 0){
            gerarMovimentoEstoque(
            		lancamento.getIdProdutoEdicao(), expedicao.getIdUsuario(), total, 
            		expedicao.getTipoMovimentoEstoque(),lancamento.getDataDistribuidor(), false);
        }
        
        if(totalParcialJuramentado.compareTo(BigInteger.ZERO) > 0){
            gerarMovimentoEstoque(
            		lancamento.getIdProdutoEdicao(), expedicao.getIdUsuario(), totalParcialJuramentado,
            		expedicao.getTipoMovimentoEstoqueJuramentado(), lancamento.getDataDistribuidor(), false);
        }
        
        movimentoEstoqueCotaRepository.adicionarEmLoteDTO(movimentosEstoqueCota);
        
        return movimentosEstoqueCotaComProdutoContaFirme;
        
    }

	private void ordenarFormasCobrancaCotaFornecedor(List<FormaCobrancaFornecedorDTO> formasCobrancaCotaFornecedor) {
		Collections.sort(formasCobrancaCotaFornecedor, new Comparator<FormaCobrancaFornecedorDTO>() {

			@Override
			public int compare(FormaCobrancaFornecedorDTO o1, FormaCobrancaFornecedorDTO o2) {
				
				if(o1 == null && o2 == null) {
					throw new ValidacaoException(TipoMensagem.ERROR, "Parâmetro Forma de Cobrança nulo ou incorreto.");
				}
				
				if (o1.getIdFornecedor() == null || o2.getIdFornecedor() == null) {
					throw new ValidacaoException(TipoMensagem.ERROR, "Fornecedor nulo. Parâmetro Forma de Cobrança incorreto.");
				}
				
				if(o1.getIdCota() == null) return -1;
				if(o2.getIdCota() == null) return 1;
				
				return o1.getIdCota().compareTo(o2.getIdCota());
			}
		});
	}

	private void validarListaCotasCompProblemasFormaCobranca(Set<Long> cotasSemFormaCobranca) {
		if(cotasSemFormaCobranca != null && !cotasSemFormaCobranca.isEmpty()) {
        	
        	LOGGER.error("Erro ao obter Forma de Cobrança para Cota/Fornecedor.");
        	StringBuilder cotasComProblemas = new StringBuilder();
        	List<Long> cotas = new ArrayList<>(cotasSemFormaCobranca);
        	cotas.remove(null);
        	Collections.sort(cotas);
        	for(Long numCota : cotas) {
        		
        		cotasComProblemas.append(numCota).append(" / ");
        	}
        	cotasComProblemas.replace(cotasComProblemas.length()-3, cotasComProblemas.length(), "");
        	throw new ValidacaoException(TipoMensagem.ERROR, String.format("Erro ao obter Forma de Cobrança para Cota/Fornecedor: %s", cotasComProblemas.toString()));
        }
	}

	private void montarListaCotasComProblemasFormaCobranca(final List<EstudoCotaDTO> listaEstudoCota,
			ProdutoEdicao produtoEdicao, List<FormaCobrancaFornecedorDTO> formasCobrancaCotaFornecedor,
			Map<Long, List<Long>> mapaFornecedorCotas, Set<Long> cotasSemFormaCobranca) {
		
		Map<Long, Long> cotaIdsNumeroCota = new HashMap<>();
		for(FormaCobrancaFornecedorDTO fcf : formasCobrancaCotaFornecedor) {
			cotaIdsNumeroCota.put(fcf.getIdCota(), fcf.getNumeroCota());
		}
		
		for(FormaCobrancaFornecedorDTO fcf : formasCobrancaCotaFornecedor) {
        	
			if(!fcf.getIdFornecedor().equals(produtoEdicao.getProduto().getFornecedor().getId())) {
				continue;
			}
			
        	List<Long> fornecedorCotas = mapaFornecedorCotas.get(fcf.getIdFornecedor());
        	for (final EstudoCotaDTO estudoCota : listaEstudoCota) {
        		
        		if(fornecedorCotas.contains(estudoCota.getIdCota())) {
        			continue;
        		}
        		
        		if(!fornecedorCotas.contains(estudoCota.getIdCota())) {
        			for(Entry<Long, List<Long>> fornecedorId : mapaFornecedorCotas.entrySet()) {
        				if(mapaFornecedorCotas.get(fornecedorId.getKey()) != null 
        						&& mapaFornecedorCotas.get(fornecedorId.getKey()).contains(estudoCota.getIdCota())) {
        					cotasSemFormaCobranca.add(cotaIdsNumeroCota.get(estudoCota.getIdCota()));
        					continue;
        				}
        			}
        		}
        		
    			if(!fornecedorCotas.contains(estudoCota.getIdCota()) && fornecedorCotas.contains(null)) {
    				continue;
    			}
        		
    			if(fcf.getNumeroCota() == null) continue;
    			cotasSemFormaCobranca.add(fcf.getNumeroCota());
        	}
        	
        }
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
                                
                                listaMovimentoCotaEnvio.add(criarMovimentoCota(data
                                		, movimentoCota.getProdutoEdicao().getId()
                                		, movimentoCota.getCota().getId()
                                		, movimentoCota.getUsuario().getId()
                                		, quantidade
                                		, tipoMovimentoCota
                                		, data
                                		, null
                                		, movimentoCota.getLancamento() != null ? movimentoCota.getLancamento().getId() : null
                                		, null
                                		, false
                                		, movimentoCota.getValoresAplicados(), FormaComercializacao.CONSIGNADO, false, false));
                                
            }
        }
        
        return listaMovimentoCotaEnvio;
    }
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoqueJuramentado(final Long idItemRecebimentoFisico, final Long idProdutoEdicao, final Long idUsuario, final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque, MovimentoEstoqueCota movimentoEstoqueCota) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(idItemRecebimentoFisico, null, idProdutoEdicao, idUsuario, quantidade,tipoMovimentoEstoque, null, null, false, false, true, null, false, null);
        
        return movimentoEstoque;
    }
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoque(final Long idItemRecebimentoFisico, final Long idProdutoEdicao, final Long idUsuario, final BigInteger quantidade,final TipoMovimentoEstoque tipoMovimentoEstoque) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(idItemRecebimentoFisico, null, idProdutoEdicao, idUsuario, quantidade,tipoMovimentoEstoque, null, null, false, false, true, null, false, null);
        
        return movimentoEstoque;
    }
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoque(final Long idProdutoEdicao, final Long idUsuario, final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque, final Origem origem) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque, origem, null, false, false, true, null, false, null);
        
        return movimentoEstoque;
    }
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoque(final Long idProdutoEdicao, final Long idUsuario, final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque, null, null, false, false, true, null, false, null);
        
        return movimentoEstoque;
    }
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoque(final Long idProdutoEdicao, final Long idUsuario, final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque, boolean enfileiraAlteracaoEstoqueProduto, Cota cota) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, null, idProdutoEdicao, idUsuario, quantidade,tipoMovimentoEstoque, null, null, false, false, true, null, enfileiraAlteracaoEstoqueProduto, cota);
        
        return movimentoEstoque;
    }
    
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoque(final Long idProdutoEdicao, final Long idUsuario, final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque, final Date dataOperacao, final boolean isImportacao) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque, null, dataOperacao, isImportacao, false, true, null, false, null);
        
        return movimentoEstoque;
    }
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoqueDiferenca(final Diferenca diferenca, final Long idUsuario,
            final TipoMovimentoEstoque tipoMovimentoEstoque,final boolean isMovimentoDiferencaAutomatica,
            final boolean validarTransfEstoqueDiferenca,
            final Date dataLancamento,
            final StatusIntegracao statusIntegracao, final Origem origem, final boolean isFaltaDirecionadaCota) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, diferenca.getTipoEstoque(), diferenca.getProdutoEdicao().getId(), idUsuario,
                diferenca.getQtde(), tipoMovimentoEstoque, origem,
                null, isFaltaDirecionadaCota, isMovimentoDiferencaAutomatica, validarTransfEstoqueDiferenca, statusIntegracao, false, null);
        
        return movimentoEstoque;
    }
    
    @Override
    @Transactional
    public MovimentoEstoque gerarMovimentoEstoqueDiferenca(final Long idProdutoEdicao, final Long idUsuario,
            final BigInteger quantidade,final TipoMovimentoEstoque tipoMovimentoEstoque,
            final boolean isMovimentoDiferencaAutomatica,
            final boolean validarTransfEstoqueDiferenca,
            final Date dataLancamento, final StatusIntegracao statusIntegracao) {
        
        final MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, null, idProdutoEdicao, idUsuario,
                quantidade, tipoMovimentoEstoque, null,
                null, false, isMovimentoDiferencaAutomatica, validarTransfEstoqueDiferenca, statusIntegracao, false, null);
        return movimentoEstoque;
    }
    
    //FIXME: Refatorar nome para ser relacionado a transferencia de suplementar para recolhimento
    @Transactional
    public void transferirEstoqueProdutoChamadaoParaRecolhimento(Long idProdutoEdicao,
            Usuario usuario) {
    	
    	EstoqueProduto estoqueProduto = estoqueProdutoRespository.buscarEstoquePorProduto(idProdutoEdicao);
    	
    	BigInteger qtdeRecolhimento 	= (estoqueProduto.getQtdeDevolucaoEncalhe() == null) ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
    	BigInteger qtdeSuplementar 		= (estoqueProduto.getQtdeSuplementar() == null) ? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
    	
    	TipoMovimentoEstoque tipoMovimentoSaidaSuplementar = 
    			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR);
    	
    	TipoMovimentoEstoque tipoMovimentoEntradaRecolhimento = 
    			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO);
    	
    	
    	BigInteger novaQtdeRecolhimento = qtdeRecolhimento.add(qtdeSuplementar);
    	estoqueProduto.setQtdeSuplementar(BigInteger.ZERO);
    	estoqueProduto.setQtdeDevolucaoEncalhe(novaQtdeRecolhimento);
    	estoqueProdutoRespository.alterar(estoqueProduto);
        
        ParametroMovimentoEstoque parametroMovimentoEstoque = new ParametroMovimentoEstoque();
		parametroMovimentoEstoque.dataOperacao 				= distribuidorService.obterDataOperacaoDistribuidor();
		parametroMovimentoEstoque.idProdutoEdicao 			= idProdutoEdicao;
		parametroMovimentoEstoque.idUsuario 				= usuario.getId(); 			
		
		parametroMovimentoEstoque.tipoMovimentoEstoque 		= tipoMovimentoSaidaSuplementar;
		parametroMovimentoEstoque.quantidade 				= qtdeSuplementar; 	
        MovimentoEstoque movEstoqSaidaSuplementar 	= criarNovoObjetoMovimentoEstoque(parametroMovimentoEstoque);
        movimentoEstoqueRepository.adicionar(movEstoqSaidaSuplementar);

		parametroMovimentoEstoque.tipoMovimentoEstoque 		= tipoMovimentoEntradaRecolhimento;
		parametroMovimentoEstoque.quantidade 				= qtdeSuplementar; 
        MovimentoEstoque movEstoqEntradaRecolhimento 	= criarNovoObjetoMovimentoEstoque(parametroMovimentoEstoque);
        movimentoEstoqueRepository.adicionar(movEstoqEntradaRecolhimento);
    	
    }
    
    @Transactional
    public void transferirEstoqueProdutoEdicaoParcial(Long idProdutoEdicao, Usuario usuario) {
    	
    	EstoqueProduto estoqueProduto = estoqueProdutoRespository.buscarEstoquePorProduto(idProdutoEdicao);
    	
    	BigInteger qtdeRecolhimento 	= (estoqueProduto.getQtdeDevolucaoEncalhe() == null) ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
    	BigInteger qtdeSuplementar 		= (estoqueProduto.getQtdeSuplementar() == null) ? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
    	BigInteger qtdeLancamento		= (estoqueProduto.getQtde() == null) ? BigInteger.ZERO : estoqueProduto.getQtde();
    	
    	qtdeLancamento = qtdeLancamento.add(qtdeRecolhimento).add(qtdeSuplementar);
    	
    	estoqueProduto.setQtdeDevolucaoEncalhe(BigInteger.ZERO);
    	estoqueProduto.setQtdeSuplementar(BigInteger.ZERO);
    	estoqueProduto.setQtde(qtdeLancamento);
    	estoqueProdutoRespository.alterar(estoqueProduto);
    	
    	TipoMovimentoEstoque tipoMovimentoSaidaRecolhimento = 
    			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_RECOLHIMENTO);

    	TipoMovimentoEstoque tipoMovimentoSaidaSuplementar = 
    			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR);
    	
        TipoMovimentoEstoque tipoMovimentoEntradaLancamento =
        		tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
        
        
        ParametroMovimentoEstoque parametroMovimentoEstoque = new ParametroMovimentoEstoque();
		parametroMovimentoEstoque.dataOperacao 				= distribuidorService.obterDataOperacaoDistribuidor();
		parametroMovimentoEstoque.idProdutoEdicao 			= idProdutoEdicao;
		parametroMovimentoEstoque.idUsuario 				= usuario.getId(); 			
		
		
		parametroMovimentoEstoque.tipoMovimentoEstoque 		= tipoMovimentoSaidaRecolhimento;
		parametroMovimentoEstoque.quantidade 				= qtdeRecolhimento; 	
        MovimentoEstoque movEstoqSaidaRecolhimento 	= criarNovoObjetoMovimentoEstoque(parametroMovimentoEstoque);
        movimentoEstoqueRepository.adicionar(movEstoqSaidaRecolhimento);

		parametroMovimentoEstoque.tipoMovimentoEstoque 		= tipoMovimentoSaidaSuplementar;
		parametroMovimentoEstoque.quantidade 				= qtdeSuplementar; 	
        MovimentoEstoque movEstoqSaidaSuplementar 	= criarNovoObjetoMovimentoEstoque(parametroMovimentoEstoque);
        movimentoEstoqueRepository.adicionar(movEstoqSaidaSuplementar);

		parametroMovimentoEstoque.tipoMovimentoEstoque 		= tipoMovimentoEntradaLancamento;
		parametroMovimentoEstoque.quantidade 				= qtdeSuplementar.add(qtdeRecolhimento); 	
        MovimentoEstoque movEstoqEntradaLancamento 	= criarNovoObjetoMovimentoEstoque(parametroMovimentoEstoque);
        movimentoEstoqueRepository.adicionar(movEstoqEntradaLancamento);
        
    }
    
    
    class ParametroMovimentoEstoque {
    	
    	public Date dataOperacao; 
		public Long idItemRecebimentoFisico;
		public Long idProdutoEdicao;
		public TipoMovimentoEstoque tipoMovimentoEstoque;
		public Long idUsuario;
		public BigInteger quantidade;
		public Origem origem;
		public boolean isMovimentoDiferencaAutomatica;
		public boolean enfileiraAlteracaoEstoqueProduto;
		public StatusIntegracao statusIntegracao;
    }
    
    /**
     * Retorna um novo objeto do tipo {@code MovimentoEstoque}
     * populado com os atributos informados.
     * 
     * @param parametroMovimentoEstoque
     * 
     * @return MovimentoEstoque
     */
    private MovimentoEstoque criarNovoObjetoMovimentoEstoque(ParametroMovimentoEstoque parametroMovimentoEstoque) {
        
		Date dataOperacao 				            = parametroMovimentoEstoque.dataOperacao;
		Long idItemRecebimentoFisico 	            = parametroMovimentoEstoque.idItemRecebimentoFisico;
		Long idProdutoEdicao 			            = parametroMovimentoEstoque.idProdutoEdicao;
		TipoMovimentoEstoque tipoMovimentoEstoque   = parametroMovimentoEstoque.tipoMovimentoEstoque;
		Long idUsuario 			                    = parametroMovimentoEstoque.idUsuario;
		BigInteger quantidade 	                    = parametroMovimentoEstoque.quantidade;
		Origem origem 			                    = parametroMovimentoEstoque.origem;
		boolean isMovimentoDiferencaAutomatica 		= parametroMovimentoEstoque.isMovimentoDiferencaAutomatica;
		StatusIntegracao statusIntegracao 			= parametroMovimentoEstoque.statusIntegracao;
    	
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
                  
                  
              }
          }
          
          if (statusIntegracao != null) {
              movimentoEstoque.setStatusIntegracao(statusIntegracao);
          }
          
          return movimentoEstoque;
    	
    }
    
    
    
    private MovimentoEstoque criarMovimentoEstoque(final Long idItemRecebimentoFisico, TipoEstoque tipoEstoque, final Long idProdutoEdicao,
            final Long idUsuario, final BigInteger quantidade,
            final TipoMovimentoEstoque tipoMovimentoEstoque, final Origem origem, Date dataOperacao,
            final boolean isImportacao, final boolean isMovimentoDiferencaAutomatica,
            final boolean validarTransfEstoqueDiferenca,
            final StatusIntegracao statusIntegracao, 
            boolean enfileiraAlteracaoEstoqueProduto, Cota cota) {
        
        this.validarDominioGrupoMovimentoEstoque(tipoMovimentoEstoque, Dominio.DISTRIBUIDOR);
        
        ParametroMovimentoEstoque parametroMovimentoEstoque = new ParametroMovimentoEstoque();
        
		parametroMovimentoEstoque.dataOperacao 				        = dataOperacao;
		parametroMovimentoEstoque.idItemRecebimentoFisico 	        = idItemRecebimentoFisico;
		parametroMovimentoEstoque.idProdutoEdicao 			        = idProdutoEdicao;
		parametroMovimentoEstoque.tipoMovimentoEstoque 		        = tipoMovimentoEstoque;
		parametroMovimentoEstoque.idUsuario 				        = idUsuario; 			
		parametroMovimentoEstoque.quantidade 				        = quantidade; 	
		parametroMovimentoEstoque.origem 					        = origem;		
		parametroMovimentoEstoque.isMovimentoDiferencaAutomatica 	= isMovimentoDiferencaAutomatica;
		parametroMovimentoEstoque.statusIntegracao 					= statusIntegracao;
        
        MovimentoEstoque movimentoEstoque = criarNovoObjetoMovimentoEstoque(parametroMovimentoEstoque);
        
        if (tipoMovimentoEstoque != null && (tipoMovimentoEstoque.isAprovacaoAutomatica() || isMovimentoDiferencaAutomatica)) {
                
            if(enfileiraAlteracaoEstoqueProduto) {
            	
            	enfileirarAlteracaoEncalheEstoqueProduto(
            			cota, 
            			movimentoEstoque.getProdutoEdicao(), 
            			tipoMovimentoEstoque.getGrupoMovimentoEstoque(), 
            			OperacaoEstoque.ENTRADA, 
            			quantidade);
            	
            } else {
                
            	final Long idEstoque = atualizarEstoqueProduto(tipoMovimentoEstoque, movimentoEstoque, isImportacao, validarTransfEstoqueDiferenca, tipoEstoque);
                
            	movimentoEstoque.setEstoqueProduto(new EstoqueProduto(idEstoque));
                
            }
                
        }
        
        movimentoEstoque = movimentoEstoqueRepository.merge(movimentoEstoque);
        
        return movimentoEstoque;
    }
    
    @Override
    @Transactional
    public Long atualizarEstoqueProduto(final TipoMovimentoEstoque tipoMovimentoEstoque,
            final MovimentoEstoque movimentoEstoque) {
        
    	return atualizarEstoqueProduto(tipoMovimentoEstoque, movimentoEstoque, false, true, null);
        
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
    	
    	final EstoqueProdutoFila epf = new EstoqueProdutoFila();
    	
    	epf.setProdutoEdicao(produtoEdicao);
    	epf.setCota(cota);
    	epf.setQtde(qtde);
    	epf.setOperacaoEstoque(operacaoEstoque);
    	epf.setTipoEstoque(grupoMovimentoEstoque.getTipoEstoque());
		
    	estoqueProdutoFilaRepository.adicionar(epf);
    }


	
    
    
    
	/**
	 * Atualiza registro de MovimentoEstoque de encalhe bem como o registro de EstoqueProduto relacionado.
     * @param movimentoEstoqueDTO
     * @param conferenciaEncalheDTO
	 */
    @Override
	@Transactional
    public void atualizarMovimentoEstoqueDeEncalhe(
    		Cota cota, 
    		MovimentoEstoqueDTO movimentoEstoqueDTO, 
			BigInteger newQtdeMovEstoque, Long idProdutoEdicao) {
		
		BigInteger oldQtdeMovEstoque = (BigInteger) ObjectUtils.defaultIfNull(movimentoEstoqueDTO.getQtde(),  BigInteger.ZERO);
		newQtdeMovEstoque =(BigInteger) ObjectUtils.defaultIfNull( newQtdeMovEstoque, BigInteger.ZERO);
		
		this.movimentoEstoqueRepository.updateById(movimentoEstoqueDTO.getId(), newQtdeMovEstoque);
		
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
		
		estoqueProdutoFilaRepository.insert(cota.getId(), idProdutoEdicao, movimentoEstoqueDTO.getGrupoMovimentoEstoque().getTipoEstoque(), operacaoEstoque, diferencaQtdItens);
	}
    
	private Long atualizarEstoqueProduto(final TipoMovimentoEstoque tipoMovimentoEstoque,
            final MovimentoEstoque movimentoEstoque, final boolean isImportacao,
            final boolean validarTransfEstoqueDiferenca, TipoEstoque tipoEstoqueDirecionamento) {
        
        if (StatusAprovacao.APROVADO.equals(movimentoEstoque.getStatus())) {
            
            final Long idProdutoEdicao = movimentoEstoque.getProdutoEdicao().getId();
            
            EstoqueProduto estoqueProduto = estoqueProdutoRespository.buscarEstoqueProdutoPorProdutoEdicao(idProdutoEdicao);
            
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
            
            final boolean isOperacaoEntrada = OperacaoEstoque.ENTRADA.equals(tipoMovimentoEstoque.getOperacaoEstoque());
            
            BigInteger qntMovimento = movimentoEstoque.getQtde() == null ? BigInteger.ZERO : movimentoEstoque.getQtde();
            
            switch (tipoEstoque) {
            
            case LANCAMENTO:
                
                novaQuantidade = isOperacaoEntrada ? estoqueProduto.getQtde().add(qntMovimento) :
                    estoqueProduto.getQtde().subtract(qntMovimento);
                
                estoqueProduto.setQtde(novaQuantidade);
                
                break;
                
            case PRODUTOS_DANIFICADOS:
                
                final BigInteger qtdeDanificado = estoqueProduto.getQtdeDanificado() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDanificado();
                
                novaQuantidade = isOperacaoEntrada ? qtdeDanificado.add(qntMovimento) :
                    qtdeDanificado.subtract(qntMovimento);
                
                estoqueProduto.setQtdeDanificado(novaQuantidade);
                
                break;
                
            case DEVOLUCAO_ENCALHE:
            	
                BigInteger qtdeEstoqueProdutoSuplementar = estoqueProduto.getQtdeSuplementar() == null ? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
                
                BigInteger qtdeEstoqueProdutoEncalhe = estoqueProduto.getQtdeDevolucaoEncalhe() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
                
                final BigInteger qtdeEstoqueProduto = estoqueProduto.getQtde() == null ? BigInteger.ZERO : estoqueProduto.getQtde();
                
                final BigInteger qtdeEstoqueProdutoTotal = qtdeEstoqueProdutoEncalhe.add(qtdeEstoqueProdutoSuplementar).add(qtdeEstoqueProduto);

                if(!isOperacaoEntrada && qtdeEstoqueProdutoEncalhe.subtract(qntMovimento).longValue() >= 0) {
                	
                	novaQuantidade = qtdeEstoqueProdutoEncalhe.subtract(qntMovimento);
                } else {
                	
                	if(movimentoEstoque.getOrigem() != null || qtdeEstoqueProdutoSuplementar.longValue() > 0) {
                		
                		if(qtdeEstoqueProdutoSuplementar.longValue() > 0) {
                			
                			//FIXME: Refatorar nome para ser relacionado a transferencia de suplementar para recolhimento
                			transferirEstoqueProdutoChamadaoParaRecolhimento(estoqueProduto.getProdutoEdicao().getId(), movimentoEstoque.getUsuario());
                			
                			qtdeEstoqueProdutoEncalhe = estoqueProduto.getQtdeDevolucaoEncalhe() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
                			
                		} else {
                			
                			qntMovimento = this.efetuarOperacaoDeTransferenciaEstoque(movimentoEstoque, estoqueProduto, qntMovimento);		
                		}
                	}
                
                	if(!isOperacaoEntrada && qtdeEstoqueProdutoEncalhe.subtract(qntMovimento).longValue() >= 0) {
                		
                		novaQuantidade = qtdeEstoqueProdutoEncalhe.subtract(qntMovimento);
                	} else {
                		
                		novaQuantidade = isOperacaoEntrada ? qtdeEstoqueProdutoTotal.add(qntMovimento) : qtdeEstoqueProdutoTotal.subtract(qntMovimento);
                	}
                }
                
                if(BigIntegerUtil.isMenorQueZero(novaQuantidade)) {
                	
                	TipoMovimentoEstoque tipoMovimentoPara = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.GANHO_DE);
                	
                	this.gerarMovimentoEstoque(idProdutoEdicao, movimentoEstoque.getUsuario().getId(), novaQuantidade.negate(), tipoMovimentoPara);
                	
                	estoqueProdutoService.processarTransferenciaEntreEstoques(idProdutoEdicao, TipoEstoque.LANCAMENTO, TipoEstoque.DEVOLUCAO_ENCALHE, movimentoEstoque.getUsuario().getId());
                	
                	novaQuantidade = BigInteger.ZERO;
                }                 	
                	
                estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
                
                break;
                
            case DEVOLUCAO_FORNECEDOR:
                
                final BigInteger qtdeDevolucaoFornecedor = Util.nvl(estoqueProduto.getQtdeDevolucaoFornecedor(),BigInteger.ZERO);
                
                novaQuantidade = isOperacaoEntrada 
                		? qtdeDevolucaoFornecedor.add(qntMovimento)
                				:qtdeDevolucaoFornecedor.subtract(qntMovimento);
                
                estoqueProduto.setQtdeDevolucaoFornecedor(novaQuantidade);
                
                break;
                
            case SUPLEMENTAR:
                
                final BigInteger qtdeSuplementar = estoqueProduto.getQtdeSuplementar() == null ? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
                
                novaQuantidade = isOperacaoEntrada ? qtdeSuplementar.add(qntMovimento) :
                    qtdeSuplementar.subtract(qntMovimento);
                
                estoqueProduto.setQtdeSuplementar(novaQuantidade);
                
                break;
                
            case RECOLHIMENTO:
                
                final BigInteger qtdeRecolhimento = estoqueProduto.getQtdeDevolucaoEncalhe() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
                
                novaQuantidade = isOperacaoEntrada ? qtdeRecolhimento.add(qntMovimento) :
                    qtdeRecolhimento.subtract(qntMovimento);
                
                estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
                
                break;
                
            case JURAMENTADO:
                
                final BigInteger qtde = estoqueProduto.getQtdeJuramentado() == null
                                            ? BigInteger.ZERO : estoqueProduto.getQtdeJuramentado();
                
                novaQuantidade = isOperacaoEntrada ? qtde.add(qntMovimento) :
                    qtde.subtract(qntMovimento);
                
                estoqueProduto.setQtdeJuramentado(novaQuantidade);
                
                break;
                
            case PERDA:
                
            	BigInteger qtdePerda = Util.nvl(estoqueProduto.getQtdePerda(), BigInteger.ZERO);
            		
        		if (movimentoEstoque.getOrigem()!=null && (movimentoEstoque.getOrigem().equals(Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_FECHAMENTO_ENCALHE))){
                    
                    if (estoqueProduto.getQtdeDevolucaoEncalhe() == null) {
                        
                        estoqueProduto.setQtdeDevolucaoEncalhe(BigInteger.ZERO);
                    }
                    
                    novaQuantidade = estoqueProduto.getQtdeDevolucaoEncalhe().subtract(qntMovimento);
                    
                    estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
                }
                // Se a origem para lançamento de PERDA for direcionada para
                // Cota não deve movimentar estoque de lançamento
                else if (movimentoEstoque.getOrigem() == null
                        || !movimentoEstoque.getOrigem().equals(Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_COTA)){
                    
                    if (estoqueProduto.getQtde() == null) {
                        
                        estoqueProduto.setQtde(BigInteger.ZERO);
                    }
                    
                    if(!tipoEstoqueDirecionamento.equals(TipoEstoque.RECOLHIMENTO) 
                    		&& !tipoEstoqueDirecionamento.equals(TipoEstoque.SUPLEMENTAR)) {
                    	
                    	novaQuantidade = estoqueProduto.getQtde().subtract(qntMovimento);
                    	estoqueProduto.setQtde(novaQuantidade);
                    }
                    
                    if(tipoEstoqueDirecionamento.equals(TipoEstoque.RECOLHIMENTO)) {
                    	
                    	estoqueProduto.setQtdeDevolucaoEncalhe(estoqueProduto.getQtdeDevolucaoEncalhe().subtract(qntMovimento));
                    } else if(tipoEstoqueDirecionamento.equals(TipoEstoque.SUPLEMENTAR)) {
                    	
                    	estoqueProduto.setQtdeSuplementar(estoqueProduto.getQtdeSuplementar().subtract(qntMovimento));
                    }
                }
                
                qtdePerda = qtdePerda.add(qntMovimento);
            	
                estoqueProduto.setQtdePerda(qtdePerda);
            	               	
                break;
                
            case GANHO:
                
                BigInteger qtdeGanho = estoqueProduto.getQtdeGanho() == null ? BigInteger.ZERO : estoqueProduto.getQtdeGanho();
                
                if (movimentoEstoque.getOrigem()!=null && movimentoEstoque.getOrigem().equals(Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_FECHAMENTO_ENCALHE)){
                    
                    if (estoqueProduto.getQtdeDevolucaoEncalhe()==null){
                        
                        estoqueProduto.setQtdeDevolucaoEncalhe(BigInteger.ZERO);
                    }
                    
                    novaQuantidade = estoqueProduto.getQtdeDevolucaoEncalhe().add(qntMovimento);
                    
                    estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
                }
                // Se a origem para lançamento de GANHO for direcionada para
                // Cota não deve movimentar estoque de lançamento
                else if (movimentoEstoque.getOrigem() == null
                        || !movimentoEstoque.getOrigem().equals(Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_COTA)){
                    
                    if (estoqueProduto.getQtde() == null) {
                        
                        estoqueProduto.setQtde(BigInteger.ZERO);
                    }
                    
                    if(!tipoEstoqueDirecionamento.equals(TipoEstoque.RECOLHIMENTO) 
                    		&& !tipoEstoqueDirecionamento.equals(TipoEstoque.SUPLEMENTAR)) {
                    	
                    	novaQuantidade = estoqueProduto.getQtde().add(qntMovimento);
                    	
                    	estoqueProduto.setQtde(novaQuantidade);
                    }
                    
                    if(tipoEstoqueDirecionamento.equals(TipoEstoque.RECOLHIMENTO)) {
                    	
                    	estoqueProduto.setQtdeDevolucaoEncalhe(estoqueProduto.getQtdeDevolucaoEncalhe().add(qntMovimento));
                    } else if(tipoEstoqueDirecionamento.equals(TipoEstoque.SUPLEMENTAR)) {
                    	
                    	estoqueProduto.setQtdeSuplementar(estoqueProduto.getQtdeSuplementar().add(qntMovimento));
                    }
                    	
                }
                
                qtdeGanho = qtdeGanho.add(qntMovimento);
                
                estoqueProduto.setQtdeGanho(qtdeGanho);
                
                break;
                
            default:
                
                throw new ValidacaoException(TipoMensagem.WARNING, "Estoque inválido para a operação.");
            }
            
            // Caso seja importação, deve inserir mesmo se o estoque ficar
            // negativo - Definido em conjunto com Cesar Pop Punk
            if (!isImportacao && !TipoEstoque.DEVOLUCAO_FORNECEDOR.equals(tipoEstoque) && !TipoEstoque.DEVOLUCAO_ENCALHE.equals(tipoEstoque)) {
                this.validarAlteracaoEstoqueProdutoDistribuidor(
                        novaQuantidade, tipoEstoque, estoqueProduto.getProdutoEdicao(),
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

	@SuppressWarnings("incomplete-switch")
	private BigInteger efetuarOperacaoDeTransferenciaEstoque(
			final MovimentoEstoque movimentoEstoque,
			EstoqueProduto estoqueProduto, BigInteger qntMovimento) {
		
		BigInteger qntDevolucaoFornecedor = null;
		BigInteger qtdePerda = null;
		
		 switch (movimentoEstoque.getOrigem()) {
		
		 	case TRANSFERENCIA_DEVOLUCAO_FORNECEDOR:
				
				qntDevolucaoFornecedor = Util.nvl(estoqueProduto.getQtdeDevolucaoFornecedor(),BigInteger.ZERO);
		    	
		    	qntDevolucaoFornecedor  = qntDevolucaoFornecedor.add(qntMovimento);
		    	
		    	estoqueProduto.setQtdeDevolucaoFornecedor(qntDevolucaoFornecedor);
				
		    	break;
			
			case TRANSFERENCIA_ESTORNO_DEVOLUCAO_FORNECEDOR:
				
				qntDevolucaoFornecedor = Util.nvl(estoqueProduto.getQtdeDevolucaoFornecedor(),BigInteger.ZERO);
		    	
		    	qntDevolucaoFornecedor  = qntDevolucaoFornecedor.subtract(movimentoEstoque.getQtde());
		    	
		    	estoqueProduto.setQtdeDevolucaoFornecedor(qntDevolucaoFornecedor);				
				
		    	break;
			
			case TRANSFERENCIA_ESTORNO_PERDA_EM_DEVOLUCAO_ENCALHE_FORNECEDOR:
				
				qtdePerda  = Util.nvl(estoqueProduto.getQtdePerda(), BigInteger.ZERO);
				
				qtdePerda = qtdePerda.subtract(movimentoEstoque.getQtde());
				
				estoqueProduto.setQtdePerda(qtdePerda);
			
				break;
			
			case TRANSFERENCIA_ESTORNO_SOBRA_DEVOLUCAO_FORNECEDOR:
				
				qntMovimento = qntMovimento.negate();
				
				break;
				
			case TRANSFERENCIA_PERDA_EM_DEVOLUCAO_ENCALHE_FORNECEDOR:
				
				qtdePerda  = Util.nvl(estoqueProduto.getQtdePerda(), BigInteger.ZERO);
		    	
		    	qtdePerda = qtdePerda.add( movimentoEstoque.getQtde());
		     	
		        estoqueProduto.setQtdePerda(qtdePerda);
		        
		        break;
		}
		return qntMovimento;
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
            final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque, final Date dataOperacao,
            final ValoresAplicados valoresAplicados, FormaComercializacao formaComercializacao, boolean isContribuinte, boolean isExigeNotaFiscal) {
        
        return criarMovimentoCota(
                dataLancamento, idProdutoEdicao, idCota, idUsuario, 
                quantidade, tipoMovimentoEstoque, null, dataOperacao, null, null, false, 
                valoresAplicados, formaComercializacao, isContribuinte, isExigeNotaFiscal);
        
    }
    
    @Override
    @Transactional
    public MovimentoEstoqueCota gerarMovimentoCotaDiferenca(final Date dataLancamento, 
    		final Long idProdutoEdicao, 
            final Long idCota, final Long idUsuario,
            final BigInteger quantidade, 
            final TipoMovimentoEstoque tipoMovimentoEstoque,
            final Long idEstudoCota,
            final boolean isMovimentoDiferencaAutomatico) {
        
        return criarMovimentoCota(dataLancamento, idProdutoEdicao, idCota,
                idUsuario, quantidade, tipoMovimentoEstoque, dataLancamento, null, null, idEstudoCota, isMovimentoDiferencaAutomatico, null, FormaComercializacao.CONSIGNADO, false, false);
    }
    
    @Override
    @Transactional
    public MovimentoEstoqueCota gerarMovimentoCota(final Date dataLancamento, final Long idProdutoEdicao, final Long idCota,
            final Long idUsuario, final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque,
            final Date dataMovimento, final Date dataOperacao, final Long idLancamento, final Long idEstudoCota, FormaComercializacao formaComercializacao) {
        
        return criarMovimentoCota(dataLancamento, idProdutoEdicao, idCota, idUsuario, quantidade,
                tipoMovimentoEstoque, dataMovimento, dataOperacao, idLancamento, idEstudoCota, false, null, formaComercializacao, false, false);
    }
    
    private MovimentoEstoqueCota criarMovimentoCota(final Date dataLancamento, final Long idProdutoEdicao, final Long idCota,
            final Long idUsuario, final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque,
            final Date dataMovimento, Date dataOperacao, Long idLancamento, final Long idEstudoCota,final boolean isMovimentoDiferencaAutomatico,
            final ValoresAplicados valoresAplicadosParam, FormaComercializacao formaComercializacao, boolean isContribuinte, boolean isExigeNota) {
        
        this.validarDominioGrupoMovimentoEstoque(tipoMovimentoEstoque, Dominio.COTA);
        
        if (dataOperacao == null) {
            
            dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
        }
        
        MovimentoEstoqueCota movimentoEstoqueCota = new MovimentoEstoqueCota();
        
        List<FormaCobrancaFornecedorDTO> formasCobrancaCotaFornecedor = formaCobrancaRepository.obterFormasCobrancaCotaFornecedor();
        formasCobrancaCotaFornecedor.addAll(formaCobrancaRepository.obterFormasCobrancaDistribuidorFornecedor());
        ordenarFormasCobrancaCotaFornecedor(formasCobrancaCotaFornecedor);
        
        Map<Long, List<Long>> mapaFornecedorCotas = new HashMap<>();
        for(FormaCobrancaFornecedorDTO fcf : formasCobrancaCotaFornecedor) {
        	if(mapaFornecedorCotas.get(fcf.getIdFornecedor()) == null) {
        		mapaFornecedorCotas.put(fcf.getIdFornecedor(), new ArrayList<Long>());
        	}
        	
        	mapaFornecedorCotas.get(fcf.getIdFornecedor()).add(fcf.getIdCota());
        }
        
        movimentoEstoqueCota.setTipoMovimento(tipoMovimentoEstoque);
        movimentoEstoqueCota.setCota(new Cota(idCota));
        
        movimentoEstoqueCota.setData(dataOperacao);
        
        movimentoEstoqueCota.setDataLancamentoOriginal(dataMovimento);
        movimentoEstoqueCota.setFormaComercializacao(formaComercializacao);
        movimentoEstoqueCota.setDataCriacao(dataOperacao);
        movimentoEstoqueCota.setProdutoEdicao(new ProdutoEdicao(idProdutoEdicao));
        movimentoEstoqueCota.setQtde(quantidade);
        movimentoEstoqueCota.setUsuario(new Usuario(idUsuario));
        movimentoEstoqueCota.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
        
        if (idEstudoCota != null) {
            
            movimentoEstoqueCota.setEstudoCota(new EstudoCota(idEstudoCota));
        }
        
        if (valoresAplicadosParam != null) {
            
            movimentoEstoqueCota.setValoresAplicados(valoresAplicadosParam);
        } else {
        	
        	if(idCota != null && idProdutoEdicao != null) {
        		Cota c = cotaRepository.buscarCotaPorID(idCota);
        		ValoresAplicados valoresAplicados = movimentoEstoqueCotaRepository.obterValoresAplicadosProdutoEdicao(c.getNumeroCota(), idProdutoEdicao, dataOperacao);
        		movimentoEstoqueCota.setValoresAplicados(valoresAplicados);
        	}
        }        
        
        movimentoEstoqueCota.setCotaContribuinteExigeNF(isExigeNota == true ? isExigeNota : isContribuinte);
          
        if (dataLancamento != null && idProdutoEdicao != null) {
            
            if (idLancamento == null) {
                
                idLancamento = lancamentoRepository.obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidor(new ProdutoEdicao(idProdutoEdicao), null, dataLancamento);

            }
            
            if (idLancamento != null) {
                
                final Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
                
                movimentoEstoqueCota.setLancamento(lancamento);
                
                if(movimentoEstoqueCota.getValoresAplicados() == null) {
                	
                	final ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
                	DescontoDTO desconto = null;
                	try {
                		
                		desconto = descontoService.obterDescontoPor(cotaRepository.buscarPorId(idCota).getNumeroCota(), produtoEdicao.getProduto().getCodigo(), produtoEdicao.getNumeroEdicao());
                	} catch (Exception e) {
                		
                		LOGGER.error("Impossível obter o Desconto.", e);
                		throw new ValidacaoException(TipoMensagem.WARNING, "Impossível obter o Desconto.");
                	}
                	
                	final BigDecimal precoComDesconto =
                			produtoEdicao.getPrecoVenda().subtract(
                					MathUtil.calculatePercentageValue(produtoEdicao.getPrecoVenda(), (desconto != null ? desconto.getValor() : BigDecimal.ZERO)));
                	
                	final ValoresAplicados valoresAplicados = new ValoresAplicados();
                	valoresAplicados.setPrecoVenda(produtoEdicao.getPrecoVenda());
                	valoresAplicados.setValorDesconto(desconto.getValor());
                	valoresAplicados.setPrecoComDesconto(precoComDesconto);
                	
                	movimentoEstoqueCota.setValoresAplicados(valoresAplicados);
                }
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
    public Long atualizarEstoqueProdutoCota(final TipoMovimentoEstoque tipoMovimentoEstoque, final MovimentoEstoqueCota movimentoEstoqueCota) {
        
        if (StatusAprovacao.APROVADO.equals(movimentoEstoqueCota.getStatus())) {
            
            final Long idCota = movimentoEstoqueCota.getCota().getId();
            final Long idProdutoEd = movimentoEstoqueCota.getProdutoEdicao().getId();
            
            EstoqueProdutoCota estoqueProdutoCota = estoqueProdutoCotaRepository.buscarEstoquePorProdutoECota(idProdutoEd, idCota);
            
            if (estoqueProdutoCota == null) {
                
                estoqueProdutoCota = new EstoqueProdutoCota();
                
                final ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEd);
                
                estoqueProdutoCota.setProdutoEdicao(produtoEdicao);
                estoqueProdutoCota.setQtdeDevolvida(BigInteger.ZERO);
                estoqueProdutoCota.setQtdeRecebida(BigInteger.ZERO);
                estoqueProdutoCota.setCota(new Cota(idCota));
            }
            
            BigInteger novaQuantidade;
            
            BigInteger quantidadeRecebida;
            
            BigInteger quantidadeDevolvida;
            
            if (OperacaoEstoque.ENTRADA.equals(tipoMovimentoEstoque.getOperacaoEstoque())) {
                
                quantidadeRecebida = (estoqueProdutoCota.getQtdeRecebida() != null) ? estoqueProdutoCota.getQtdeRecebida() : BigInteger.ZERO;
                        
                novaQuantidade = quantidadeRecebida.add(movimentoEstoqueCota.getQtde());
                
                estoqueProdutoCota.setQtdeRecebida(novaQuantidade);
                        
            } else {
                
                quantidadeDevolvida = (estoqueProdutoCota.getQtdeDevolvida() != null) ? estoqueProdutoCota.getQtdeDevolvida() : BigInteger.ZERO;
                        
                novaQuantidade = quantidadeDevolvida.add(movimentoEstoqueCota.getQtde());
                
                estoqueProdutoCota.setQtdeDevolvida(novaQuantidade);
            }
            
            this.validarAlteracaoEstoqueProdutoCota(novaQuantidade, estoqueProdutoCota.getProdutoEdicao());
            
            if (estoqueProdutoCota.getId() == null) {
                
                return estoqueProdutoCotaRepository.adicionar(estoqueProdutoCota);
                
            } else {
                
                estoqueProdutoCotaRepository.alterar(estoqueProdutoCota);
                
                return estoqueProdutoCota.getId();
            }
        }
        
        return null;
    }
    
    @Override
    @Transactional
    public void processarRegistroHistoricoVenda(final HistoricoVendaInput vendaInput, final Date dataOperacao) {
        
        final Integer reparte = vendaInput.getQtdReparte();
        final Integer encalhe = vendaInput.getQtdEncalhe();
        
        final ProdutoEdicao edicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
                vendaInput.getCodigoProduto().toString(), vendaInput.getNumeroEdicao().longValue());
        
        if(edicao == null) {
            throw new ImportacaoException(String.format("Edição % inexistente para o Produto: % ", vendaInput.getNumeroEdicao(), vendaInput.getCodigoProduto().toString()));
        }
        
        final Cota cota = cotaRepository.obterPorNumeroDaCota(vendaInput.getNumeroCota());
        
        if(cota == null) {
            throw new ImportacaoException(String.format("Cota % inexistente.", vendaInput.getNumeroCota()));
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
            
            gerarMovimentoCota(null, edicao.getId(), cota.getId(), idUsuario, BigInteger.valueOf(reparte), tipoMovimentoRecebimentoReparte, dataOperacao, null, FormaComercializacao.CONSIGNADO, false, false);
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
            
            gerarMovimentoCota(null, edicao.getId(), cota.getId(), idUsuario, BigInteger.valueOf(encalhe), tipoMovimentoEnvioEncalhe, dataOperacao, null, FormaComercializacao.CONSIGNADO, false, false);
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
            this.criarMovimentoEstoque(null, null, detalheNotaFiscal.getProdutoServico().getProdutoEdicao().getId(), idUsuario, detalheNotaFiscal.getProdutoServico().getQuantidade(), tipoMovimento,null, null, false,false, true, null, false, null);
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
    public MovimentoEstoqueCotaDTO criarMovimentoExpedicaoCota(Distribuidor distribuidor, final Date dataLancamento, final ProdutoEdicao produtoEdicao,
            final Long idUsuario, final TipoMovimentoEstoque tipoMovimentoEstoque, final Date dataMovimento,
            Date dataOperacao, Long idLancamento, final Map<String, DescontoDTO> descontos, final boolean isMovimentoDiferencaAutomatico, EstudoCotaDTO estudoCotaDTO) {
        
        this.validarDominioGrupoMovimentoEstoque(tipoMovimentoEstoque, Dominio.COTA);
        
        if(estudoCotaDTO == null) {
        	
        	throw new IllegalArgumentException(String.format("%s não pode ser nulo.", EstudoCotaDTO.class.getName()));
        }
        
        if(distribuidor == null) {
        	
        	throw new IllegalArgumentException(String.format("%s não pode ser nulo.", Distribuidor.class.getName()));
        }
        
        if (dataOperacao == null) {
            
            dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
        }
        
        final MovimentoEstoqueCotaDTO movimentoEstoqueCota = new MovimentoEstoqueCotaDTO();
        
        movimentoEstoqueCota.setTipoMovimentoId(tipoMovimentoEstoque.getId());
        
        movimentoEstoqueCota.setIdCota(estudoCotaDTO.getIdCota());
        
        movimentoEstoqueCota.setData(dataMovimento==null ? dataOperacao : dataMovimento);
        
        movimentoEstoqueCota.setDataLancamentoOriginal(dataMovimento);
        
        movimentoEstoqueCota.setDataCriacao(dataOperacao);
        movimentoEstoqueCota.setIdProdEd(produtoEdicao.getId());
        movimentoEstoqueCota.setQtde(estudoCotaDTO.getQtdeEfetiva());
        movimentoEstoqueCota.setUsuarioId(idUsuario);
        movimentoEstoqueCota.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
        
        if(produtoEdicao.getProduto().getFormaComercializacao().equals(FormaComercializacao.CONTA_FIRME) 
        		|| (estudoCotaDTO.getTipoCota().equals(TipoCota.A_VISTA) && !estudoCotaDTO.isDevolveEncalhe())){        	
        	movimentoEstoqueCota.setFormaComercializacao(FormaComercializacao.CONTA_FIRME.name());
        } else {
    		movimentoEstoqueCota.setFormaComercializacao(FormaComercializacao.CONSIGNADO.name());
    	}
        
        if (estudoCotaDTO.getId() != null) {
            movimentoEstoqueCota.setEstudoCotaId(estudoCotaDTO.getId());
        }
        
        movimentoEstoqueCota.setCotaContribuinteExigeNF(!distribuidor.isPossuiRegimeEspecialDispensaInterna() || estudoCotaDTO.isCotaContribuinteExigeNotaFiscal());
        
        if (dataLancamento != null && produtoEdicao.getId() != null) {
            
            if (idLancamento == null) {
                
                idLancamento = lancamentoRepository.obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidor(
                        new ProdutoEdicao(produtoEdicao.getId()), null, dataLancamento);
            }
            
            if (idLancamento != null) {
                
                final Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
                
                movimentoEstoqueCota.setLancamentoId(lancamento.getId());
                
                // FIXME Provisorio ate validação do Evaldo !
                // final ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
                

                if(produtoEdicao.getPrecoVenda().compareTo(BigDecimal.ZERO) <= 0) {
                	throw new ValidacaoException(TipoMensagem.ERROR, "Produto com Preço de Venda inválido.");
                }
                
				/**
                 * A busca dos descontos é feita diretamente no Map, por chave,
                 * agilizando o retorno do resultado
                 */
                DescontoDTO descontoDTO = null;
                try {
                	
                	if( produtoEdicao.getProduto().getFornecedor() == null) {
                		throw new Exception("Produto sem Fornecedor cadastrado!");
                	}
                	
                	if( produtoEdicao.getProduto().getEditor() == null) {
                		throw new Exception("Produto sem Editor cadastrado!");
                	}
                	
                	descontoDTO = descontoService.obterDescontoPor(descontos, estudoCotaDTO.getIdCota()
                    		, produtoEdicao.getProduto().getFornecedor().getId()
                    		, produtoEdicao.getProduto().getEditor().getId()
                    		, produtoEdicao.getProduto().getId()
                    		, produtoEdicao.getId());

                    if(descontoDTO == null) {
                    	LOGGER.error("Produto sem desconto: " + produtoEdicao.getProduto().getCodigo() + " / " + produtoEdicao.getNumeroEdicao());
                    	throw new ValidacaoException();
                    }
                    
                } catch (final ValidacaoException e) {
                    final String msg = "Produto sem desconto: " + produtoEdicao.getProduto().getCodigo() + " / " + produtoEdicao.getNumeroEdicao();
                    LOGGER.error(msg, e);
                    throw new ValidacaoException(TipoMensagem.ERROR, msg);
                } catch (final Exception e) {
                    final String msg = e.getMessage();
                    LOGGER.error(msg, e);
                    throw new ValidacaoException(TipoMensagem.ERROR, msg);
                }
                
                final BigDecimal desconto = descontoDTO != null ? descontoDTO.getValor() : BigDecimal.ZERO;
                
                final BigDecimal precoComDesconto = produtoEdicao.getPrecoVenda().subtract(MathUtil.calculatePercentageValue(produtoEdicao.getPrecoVenda(), desconto));
                
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
            mec.setCota(new Cota(estudoCotaDTO.getIdCota()));
            mec.setProdutoEdicao(new ProdutoEdicao(produtoEdicao.getId()));
            mec.setStatus(StatusAprovacao.APROVADO);
            mec.setQtde(estudoCotaDTO.getQtdeEfetiva());
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
    
    @Transactional(readOnly=true)
    public BigDecimal obterValorConsignadoDeVendaSuplementar(final Date dataMovimentacao){
    	
    	return movimentoEstoqueRepository.obterValorConsignadoDeVendaEncalheSuplementar(dataMovimentacao);
    }
    
}
