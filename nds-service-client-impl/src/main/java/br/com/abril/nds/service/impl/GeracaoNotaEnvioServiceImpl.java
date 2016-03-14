package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaEnvioDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.RotaPDV;
import br.com.abril.nds.model.envio.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.envio.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvioPK;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.envio.nota.NotaEnvioEndereco;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoEstudoCota;
import br.com.abril.nds.repository.CotaAusenteRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoGeradoRepository;
import br.com.abril.nds.repository.FuroProdutoRepository;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.NotaEnvioRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.PessoaRepository;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.repository.RoteiroRepository;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.TelefoneRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.GeracaoNotaEnvioService;
import br.com.abril.nds.util.Intervalo;

@Service
public class GeracaoNotaEnvioServiceImpl implements GeracaoNotaEnvioService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GeracaoNotaEnvioServiceImpl.class);
    
    @Autowired
    private CotaRepository cotaRepository;
    
    @Autowired
    private DistribuidorRepository distribuidorRepository;
    
    @Autowired
    private PessoaRepository pessoaRepository;
    
    @Autowired
    private DescontoService descontoService;
    
    @Autowired
    private TelefoneRepository telefoneRepository;
    
    @Autowired
    private EnderecoRepository enderecoRepository;
    
    @Autowired
    private TelefoneCotaRepository telefoneCotaRepository;
    
    @Autowired
    private NotaEnvioRepository notaEnvioRepository;
    
    @Autowired
    private ItemNotaEnvioRepository itemNotaEnvioRepository;
    
    @Autowired
    private RotaRepository rotaRepository;
    
    @Autowired
    private EstudoCotaRepository estudoCotaRepository;
    
    @Autowired
    private PdvRepository pdvRepository;
    
    @Autowired
    private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
    
    @Autowired
    private RoteirizacaoRepository roteirizacaoRepository;
    
    @Autowired
    private CotaAusenteRepository cotaAusenteRepository;
    
    @Autowired
    private RoteiroRepository roteiroRepository;
    
    @Autowired
    private FuroProdutoRepository furoProdutoRepository;
    
    @Autowired
    private EstudoService estudoService;
    
    @Autowired
    private EstudoGeradoRepository estudoGeradoRepository;
    
    // Trava para evitar duplicidade ao gerar notas de envio por mais de um usuario simultaneamente
    // O HashMap suporta os mais detalhes e pode ser usado futuramente para restricoes mais finas
    public static final Map<String, Object> TRAVA_GERACAO_NE = new HashMap<>();
    
    @Override
    @Transactional
    public List<ConsultaNotaEnvioDTO> busca(final FiltroConsultaNotaEnvioDTO filtro) {
        
        this.validarRoteirizacaoCota(filtro,this.getIdsCotaIntervalo(filtro));
        
        if (filtro.getIdRoteiro() != null) {
            filtro.setFiltroRoteiroEspecial(TipoRoteiro.ESPECIAL.equals(roteiroRepository.buscarPorId(filtro.getIdRoteiro()).getTipoRoteiro()));
        }
        
        if("EMITIDAS".equals(filtro.getExibirNotasEnvio())) {
            return cotaRepository.obterDadosCotasComNotaEnvioEmitidas(filtro);
        } else if("AEMITIR".equals(filtro.getExibirNotasEnvio())) {
            return cotaRepository.obterDadosCotasComNotaEnvioAEmitir(filtro);
        } else {
            return cotaRepository.obterDadosCotasComNotaEnvioEmitidasEAEmitir(filtro);
        }
    }
    
    @Override
    @Transactional
    public Integer buscaCotasNotasDeEnvioQtd(final FiltroConsultaNotaEnvioDTO filtro) {
        
        if (filtro.getIdRoteiro() != null) {
            filtro.setFiltroRoteiroEspecial(TipoRoteiro.ESPECIAL.equals(roteiroRepository.buscarPorId(filtro.getIdRoteiro()).getTipoRoteiro()));
        }
        
        if("EMITIDAS".equals(filtro.getExibirNotasEnvio())) {
            return cotaRepository.obterDadosCotasComNotaEnvioEmitidasCount(filtro);
        } else if("AEMITIR".equals(filtro.getExibirNotasEnvio())) {
            return cotaRepository.obterDadosCotasComNotaEnvioAEmitirCount(filtro);
        } else {
            return cotaRepository.obterDadosCotasComNotaEnvioEmitidasEAEmitirCount(filtro);
        }
        
    }
    
    private List<ItemNotaEnvio> gerarItensNotaEnvio(final List<EstudoCota> listaEstudoCota, final Cota cota, final List<MovimentoEstoqueCota> listaMovimentoEstoqueCota, final Intervalo<Date> periodo, final Map<String, DescontoDTO> descontos) {
        
        final List<ItemNotaEnvio> listItemNotaEnvio = new ArrayList<ItemNotaEnvio>();
        
        gerarItensNEMovimento(listaMovimentoEstoqueCota, cota, listItemNotaEnvio, descontos);
        
        gerarItensNEEstudo(listaEstudoCota, cota, listItemNotaEnvio, periodo, descontos);
        
        sortItensByProdutoNome(listItemNotaEnvio);
        
        return listItemNotaEnvio;
    }
    
    /**
     * Obtém Quantidade de Movimentos de Saida x Entrada por Produto Considera
     * Movimentos de grupos especificos para apurar após Expedição
     * @param periodo
     * @param idCota
     * @return Map<Long, BigInteger>
     */
    @Transactional(readOnly=true)
    public Map<Long, BigInteger> obtemQuantidadeMovimentosPorProdutoAposExpedicao(final Intervalo<Date> periodo, final Long idCota){
        
        final String[] gruposMovimentoEstoque   = {
                GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO.name(),
                GrupoMovimentoEstoque.FALTA_DE_COTA.name(),
                GrupoMovimentoEstoque.FALTA_EM_COTA.name(),
                GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE.name(),
                GrupoMovimentoEstoque.SOBRA_DE_COTA.name(),
                GrupoMovimentoEstoque.SOBRA_EM_COTA.name(),
                GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE.name(),
                GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE.name(),
                GrupoMovimentoEstoque.FALTA_DE.name(),
                GrupoMovimentoEstoque.FALTA_EM.name(),
                GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE.name(),
                GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA.name(),
                GrupoMovimentoEstoque.SOBRA_DE.name(),
                GrupoMovimentoEstoque.SOBRA_EM.name(),
                GrupoMovimentoEstoque.SOBRA_ENVIO_PARA_COTA.name()
        };
        
        final Map<Long, BigInteger> mapProdutos =
                movimentoEstoqueCotaRepository.obterQtdMovimentoCotaPorTipoMovimento(periodo,
                        idCota,
                        gruposMovimentoEstoque);
        
        return mapProdutos;
    }
    
	                                                                /**
     * Gera itens de nota de envio a partir do movimentos de estoque que não
     * possuem estudo.
     * 
     * @param listaMovimentoEstoqueCota
     * @param cota
     * @param listItemNotaEnvio
     * @param descontos 
     */
    private void gerarItensNEMovimento(
            final List<MovimentoEstoqueCota> listaMovimentoEstoqueCota, final Cota cota,
            final List<ItemNotaEnvio> listItemNotaEnvio, final Map<String, DescontoDTO> descontos) {
        
        if (listaMovimentoEstoqueCota == null || listaMovimentoEstoqueCota.isEmpty()){
            
            return;
        }
        
        for(final MovimentoEstoqueCota mec : listaMovimentoEstoqueCota) {
            
            final Lancamento lancamento = mec.getLancamento();
            
            final ProdutoEdicao produtoEdicao = mec.getProdutoEdicao();
            
            if (lancamento.getEstudo() == null) {
                
                throw new ValidacaoException(TipoMensagem.ERROR, "Produto: " + produtoEdicao + " não possui estudo.");
            }
            
            final ItemNotaEnvio itemNotaEnvio = getItemNE(listItemNotaEnvio, mec.getProdutoEdicao());
            
            final BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
            
            itemNotaEnvio.setSequenciaMatrizLancamento(lancamento.getSequenciaMatriz());
            
            for(final EstudoCota ec : lancamento.getEstudo().getEstudoCotas()) {
                if(ec.getCota().getNumeroCota().equals(mec.getCota().getNumeroCota())
                        && ec.getEstudo().getProdutoEdicao().getId().equals(mec.getProdutoEdicao().getId())) {
                    itemNotaEnvio.setEstudoCota(ec);
                    break;
                }
            }

            itemNotaEnvio.setProdutoEdicao(produtoEdicao);
            itemNotaEnvio.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
            itemNotaEnvio.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
            itemNotaEnvio.setPublicacao(produtoEdicao.getProduto().getNomeComercial());
            
            DescontoDTO descontoDTO = null;
            try {
                descontoDTO = descontoService.obterDescontoPor(descontos, cota.getId()
                		, produtoEdicao.getProduto().getFornecedor().getId()
                		, produtoEdicao.getProduto().getEditor().getId()
                		, produtoEdicao.getProduto().getId(), produtoEdicao.getId());
            } catch (final Exception e) {
                final String msg = "Erro ao obter desconto: Cota: " + cota.getNumeroCota() + " / Produto: "
                        + produtoEdicao.getProduto().getCodigo() + " - " + produtoEdicao.getNumeroEdicao();
                LOGGER.error(msg, e);
                throw new ValidacaoException(TipoMensagem.ERROR, msg);
            }
            
            if(descontoDTO == null) {
                
                throw new ValidacaoException(TipoMensagem.ERROR, "Cota/Produto sem desconto: Cota: "+ cota.getNumeroCota() +" / Produto: "+ produtoEdicao.getProduto().getCodigo() +" - "+ produtoEdicao.getNumeroEdicao());
            }
            
            itemNotaEnvio.setDesconto(descontoDTO.getValor());
            
            BigInteger quantidade;
            
            quantidade = itemNotaEnvio.getReparte();
            
            if(quantidade == null) {
                quantidade = BigInteger.ZERO;
            }
            
            quantidade = quantidade.add(mec.getQtde());
            
            itemNotaEnvio.setReparte(quantidade);
            
            itemNotaEnvio.setPrecoCapa(precoVenda);
            
            List<MovimentoEstoqueCota> movimentosProdutoSemEstudo = itemNotaEnvio.getMovimentosProdutoSemEstudo();
            
            if(movimentosProdutoSemEstudo == null) {
                movimentosProdutoSemEstudo = new ArrayList<MovimentoEstoqueCota>();
            }
            
            movimentosProdutoSemEstudo.add(mec);
            
            itemNotaEnvio.setMovimentosProdutoSemEstudo(movimentosProdutoSemEstudo);
            
            listItemNotaEnvio.add(itemNotaEnvio);
        }
        
    }
    
	                                                                /**
     * Método que verifica se ja existe um itemNotaEnvio para um determinado
     * produto. caso não exista retorna uma nova instancia de ItemNotaEnvio.
     * 
     * @param listItemNotaEnvio
     * @param produtoEdicao
     * @return
     */
    private ItemNotaEnvio getItemNE(final List<ItemNotaEnvio> listItemNotaEnvio,
            final ProdutoEdicao produtoEdicao) {
        
        ItemNotaEnvio itemNE = new ItemNotaEnvio();
        
        for(final ItemNotaEnvio item : listItemNotaEnvio) {
            
            if(item.getProdutoEdicao().getId().equals(produtoEdicao.getId())) {
                itemNE = item;
                break;
            }
        }
        
        return itemNE;
    }
    
    /**
     * Gera itens de Nota de Envio a partir dos Estudos Cota
     * 
     * @param listaEstudoCota
     * @param cota
     * @param listItemNotaEnvio
     * @param descontos 
     */
    private void gerarItensNEEstudo(final List<EstudoCota> listaEstudoCota,
            final Cota cota, final List<ItemNotaEnvio> listItemNotaEnvio, final Intervalo<Date> periodo, final Map<String, DescontoDTO> descontos) {
        
        if (listaEstudoCota == null || listaEstudoCota.isEmpty()){
            
            return;
        }
        
        // Movimentos de Entrada e Saida para recalcular após Expedicão.
        final Map<Long, BigInteger> mapProdutos = this.obtemQuantidadeMovimentosPorProdutoAposExpedicao(periodo, cota.getId());
        
        
        for (final EstudoCota estudoCota : listaEstudoCota) {
            
            //Verifica se Estudo ja possui itens de Nota de Envio.
            if (estudoCota.getItemNotaEnvios()!=null && !estudoCota.getItemNotaEnvios().isEmpty()) {
                
            	listItemNotaEnvio.addAll(estudoCota.getItemNotaEnvios());
            		 
            	continue;
            }
            
            final ProdutoEdicao produtoEdicao = estudoCota.getEstudo().getProdutoEdicao();
            
            final BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
            
            BigInteger quantidadeResultante = BigInteger.ZERO;
            
            quantidadeResultante = mapProdutos.get(produtoEdicao.getId());
            
            DescontoDTO descontoDTO = null;
            try {
            	
            	if( produtoEdicao.getProduto().getFornecedor() == null) {
            		throw new Exception("Produto sem Fornecedor cadastrado!");
            	}
            	
            	if( produtoEdicao.getProduto().getEditor() == null) {
            		throw new Exception("Produto sem Editor cadastrado!");
            	}
            	
                descontoDTO = descontoService.obterDescontoPor(descontos, cota.getId()
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
                String msg = e.getMessage();
                LOGGER.error(msg, e);
                msg = "Erro ao obter desconto: Cota: " + cota.getNumeroCota() + " / Produto: "
                        + produtoEdicao.getProduto().getCodigo() + " - " + produtoEdicao.getNumeroEdicao()+"  "+msg;
                LOGGER.error(msg, e);
                throw new ValidacaoException(TipoMensagem.ERROR, msg);
            }
            
            if(quantidadeResultante == null) {
                
                quantidadeResultante = BigInteger.ZERO;
            }
            
            final BigInteger quantidade = quantidadeResultante.add(estudoCota.getQtdeEfetiva() == null ? BigInteger.ZERO : estudoCota.getQtdeEfetiva());
            
            ItemNotaEnvio itemNotaEnvio = null;
            
            for(final ItemNotaEnvio item : listItemNotaEnvio) {
                if(item.getProdutoEdicao().getId().equals(produtoEdicao.getId())) {
                    itemNotaEnvio = item;
                    break;
                }
                
                if(item.getEstudoCota() != null) {
                    final Integer sequenciaMatrizLancamento = item.getEstudoCota().getEstudo().getLancamento().getSequenciaMatriz();
                    item.setSequenciaMatrizLancamento(sequenciaMatrizLancamento);
                }
            }
            
            final boolean itemExistente = itemNotaEnvio != null;
            
            // Cria novo item nota caso o Estudo ainda não possua
            itemNotaEnvio = criarNovoItemNotaEnvio(itemNotaEnvio, estudoCota, produtoEdicao,
                    precoVenda, ((descontoDTO.getValor() != null) ? descontoDTO.getValor() : BigDecimal.ZERO), quantidade);
            
            if(estudoCota.getEstudo() != null && estudoCota.getEstudo().getLancamento() != null) {
                itemNotaEnvio.setSequenciaMatrizLancamento(estudoCota.getEstudo().getLancamento().getSequenciaMatriz());
            }
            
            if(!itemExistente) {
                listItemNotaEnvio.add(itemNotaEnvio);
            }
            
        }
    }
    
    private void sortItensByProdutoNome(final List<ItemNotaEnvio> listItemNotaEnvio) {
        Collections.sort(listItemNotaEnvio, new Comparator<ItemNotaEnvio>(){
            @Override
            public int compare(final ItemNotaEnvio o1, final ItemNotaEnvio o2) {
                
                return getNomeProdutoEdicao(o1).compareTo(getNomeProdutoEdicao(o2));
            }
            
        });
    }
    
    private String getNomeProdutoEdicao(final ItemNotaEnvio itemNotaEnvio) {
        
        String nomeProduto = "";
        
        if (itemNotaEnvio.getProdutoEdicao() != null) {
            if(itemNotaEnvio.getProdutoEdicao().getProduto() != null) {
                nomeProduto = itemNotaEnvio.getProdutoEdicao().getProduto().getNome();
            }
        }
        
        return nomeProduto;
    }
    
    private ItemNotaEnvio criarNovoItemNotaEnvio(ItemNotaEnvio itemNotaEnvio, final EstudoCota estudoCota,
            final ProdutoEdicao produtoEdicao, final BigDecimal precoVenda,
            final BigDecimal percentualDesconto, final BigInteger quantidade) {
        
        if(itemNotaEnvio == null) {
            itemNotaEnvio = new ItemNotaEnvio();
        }
        
        String nomePublicacao = 
        		TipoEstudoCota.JURAMENTADO.equals(estudoCota.getTipoEstudo()) ?
        				produtoEdicao.getProduto().getNomeComercial() + " Juramentado" : 
        				produtoEdicao.getProduto().getNomeComercial();
        
        itemNotaEnvio.setProdutoEdicao(produtoEdicao);
        itemNotaEnvio.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
        itemNotaEnvio.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
        itemNotaEnvio.setPublicacao(nomePublicacao);
        itemNotaEnvio.setDesconto(percentualDesconto);
        itemNotaEnvio.setReparte(quantidade);
        itemNotaEnvio.setPrecoCapa(precoVenda);
        itemNotaEnvio.setEstudoCota(estudoCota);
        
        return itemNotaEnvio;
    }
    
	                                                                /**
     * Obtem idRota do PDV principal da Cota e caso não encontre obtem do box da
     * cota
     * 
     * @param pdvPrincipal
     * @param cota
     * @return Long idRota
     */
    private Long getIdRotaCota(final PDV pdvPrincipal, final Cota cota){
        
        Long idRota = null;
        
        for (final RotaPDV r : pdvPrincipal.getRotas()){
            
            if (!r.getRota().getRoteiro().getTipoRoteiro().equals(TipoRoteiro.ESPECIAL)){
                
                idRota = r.getRota().getId();
                
                break;
            }
        }
        
        if (idRota == null) {
            
            if(cota.getBox() != null) {
                
                final List<Roteiro> roteiros = cota.getBox().getRoteirizacao().getRoteiros();
                
                Roteiro roteiro = null;
                
                for (final Roteiro r : roteiros) {
                    
                    if (!r.getTipoRoteiro().equals(TipoRoteiro.ESPECIAL)) {
                        
                        roteiro = r;
                        
                        break;
                    }
                }
                
                if (roteiro != null) {
                    idRota = roteiro.getRotas().get(0).getId();
                }
            }
        }
        
        return idRota;
    }
    
    @Override
    @Transactional(readOnly = false)
    public List<NotaEnvio> visualizar(final FiltroConsultaNotaEnvioDTO filtro) {
        
        final List<Long> listaIdCotas = this.getIdsCotaIntervalo(filtro);
        
        this.validarRoteirizacaoCota(filtro, listaIdCotas);
        
        final PessoaJuridica pessoaEmitente = distribuidorRepository.juridica();
        
        final List<EstudoCota> listaEstudosCotas =
                estudoCotaRepository.obterEstudosCotaParaNotaEnvio(listaIdCotas, filtro.getIntervaloMovimento(),filtro.getIdFornecedores(), filtro.getExibirNotasEnvio());
        
        final List<NotaEnvio> notasEnvio = new ArrayList<>();
        
        final Map<Long, List<EstudoCota>> mapEstudosCota = this.getMapEstudosCota(listaEstudosCotas);
        
        final Map<String, DescontoDTO> descontos = descontoService.obterDescontosMapPorLancamentoProdutoEdicao();
        
        final EnderecoDistribuidor enderecoDistribuidor = distribuidorRepository.obterEnderecoPrincipal();
        final TelefoneDistribuidor telefoneDistribuidor = distribuidorRepository.obterTelefonePrincipal();
        
        final Map<Long, List<MovimentoEstoqueCota>> mapMovimentoEstoqueCota = this.obterMapMovimentoEstoqueCota(listaIdCotas, filtro);
        
        for (final Long idCota : listaIdCotas) {
            
            this.gerarNotaEnvioParaVisualizacao(pessoaEmitente,
                    idCota,
                    filtro,
                    notasEnvio,
                    null, null, null,
                    mapEstudosCota.get(idCota),
                    descontos, enderecoDistribuidor, telefoneDistribuidor,mapMovimentoEstoqueCota);
        }
        
        return notasEnvio;
    }
    
    private void gerarNotaEnvioParaVisualizacao(final PessoaJuridica pessoaEmitente,
            final Long idCota,
            final FiltroConsultaNotaEnvioDTO filtro,
            final List<NotaEnvio> notasEnvio,
            final String chaveAcesso,
            final Integer codigoNaturezaOperacao,
            final String descricaoNaturezaOperacao,
            final List<EstudoCota> listaEstudosCota,
            final Map<String, DescontoDTO> descontos,
            final EnderecoDistribuidor enderecoDistribuidor,
            final TelefoneDistribuidor telefoneDistribuidor,
            final Map<Long, List<MovimentoEstoqueCota>> mapMovimentoEstoqueCota) {
        
        final Cota cota = cotaRepository.buscarPorId(idCota);
        
        if (cota == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Cota " + idCota + " não encontrada!");
        }
        
        final IdentificacaoDestinatario destinatarioAtualizado = this.obterDestinatarioAtualizado(cota, filtro.getIdRota(), filtro.getIntervaloMovimento());
        
        final List<ItemNotaEnvio> listaItemNotaEnvio =
                this.processarNotasDeEnvioGeradas(cota, filtro, notasEnvio, listaEstudosCota, destinatarioAtualizado, descontos,mapMovimentoEstoqueCota);
        
        NotaEnvio notaEnvio = null;
        
        if (listaItemNotaEnvio != null && !listaItemNotaEnvio.isEmpty()) {
            
            notaEnvio = criarNotaEnvio(destinatarioAtualizado,
                    chaveAcesso,
                    codigoNaturezaOperacao, descricaoNaturezaOperacao, filtro.getDataEmissao(),
                    pessoaEmitente, enderecoDistribuidor, telefoneDistribuidor);
            
            int sequencia = 0;
            
            for (final ItemNotaEnvio itemNotaEnvio : listaItemNotaEnvio) {
                
                if (itemNotaEnvio.getItemNotaEnvioPK() == null) {
                    
                    itemNotaEnvio.setItemNotaEnvioPK(new ItemNotaEnvioPK(notaEnvio, ++sequencia));
                }
            }
            
            notaEnvio.setListaItemNotaEnvio(listaItemNotaEnvio);
            
            notasEnvio.add(notaEnvio);
        }
        
        for(final NotaEnvio item : notasEnvio){
            item.getListaItemNotaEnvio().isEmpty();
            
            Collections.sort(item.getListaItemNotaEnvio(), new Comparator<ItemNotaEnvio>(){
                @Override
                public int compare(final ItemNotaEnvio o1, final ItemNotaEnvio o2) {
                    
                    if (o1 !=null && o2 != null) {
                        if (o1.getSequenciaMatrizLancamento() != null
                                && o2.getSequenciaMatrizLancamento() != null) {
                            return o1.getSequenciaMatrizLancamento().compareTo(o2.getSequenciaMatrizLancamento());
                        } else if ((o1.getProdutoEdicao() != null && o1.getProdutoEdicao().getProduto() != null)
                                && (o2.getProdutoEdicao() != null && o2.getProdutoEdicao().getProduto() != null)) {
                            o1.getProdutoEdicao().getProduto().getNome().compareTo(
                                    o2.getProdutoEdicao().getProduto().getNome());
                        }
                    }
                    return 0;
                }
                
            });
        }
        
    }
    
    /**
     * Atualiza os itens e o Dados do Destinatario das Notas de Envio
     * @param novasNotasEnvio
     * @param listaItemNotaEnvio
     * @param destinatarioAtualizado
     */
    private void atualizarNotasEnvio(final List<NotaEnvio> novasNotasEnvio,
            final List<ItemNotaEnvio> listaItemNotaEnvio,
            final IdentificacaoDestinatario destinatarioAtualizado){
        
        final List<ItemNotaEnvio> itensNotasEnvioExistentes = new ArrayList<ItemNotaEnvio>();
        
        for(final ItemNotaEnvio ine : listaItemNotaEnvio) {
            
            final ItemNotaEnvioPK itemNotaEnvioPK = ine.getItemNotaEnvioPK();
            
            final NotaEnvio notaEnvio = (itemNotaEnvioPK == null) ? null : itemNotaEnvioPK.getNotaEnvio();
            
            if (notaEnvio != null) {
                
                itensNotasEnvioExistentes.add(ine);
                
                if (!novasNotasEnvio.contains(notaEnvio)) {
                    
//                    notaEnvio.setDestinatario(destinatarioAtualizado);
                    
                    novasNotasEnvio.add(notaEnvio);
                }
            }
        }
        
        listaItemNotaEnvio.removeAll(itensNotasEnvioExistentes);
    }
    
    /**
     * Define sequencia para cada ItemNotaEnvio e atualiza MovimentoEstoqueCota sem estudo com cada ItemNotaEnvio
     * @param notaEnvio
     * @param listaItemNotaEnvio
     */
    private void atualizaMovimentosEstoqueItemNotaEnvio(final NotaEnvio notaEnvio, final List<ItemNotaEnvio> listaItemNotaEnvio){
        
        int sequencia = 0;
        
        for (final ItemNotaEnvio itemNotaEnvio : notaEnvio.getListaItemNotaEnvio()) {
            
            if(itemNotaEnvio.getItemNotaEnvioPK() == null) {
                
                itemNotaEnvio.setItemNotaEnvioPK(new ItemNotaEnvioPK(notaEnvio, ++sequencia));
                
                Integer seqMatrizlancamento = 0;
                
                if(itemNotaEnvio.getMovimentosProdutoSemEstudo() != null && !itemNotaEnvio.getMovimentosProdutoSemEstudo().isEmpty()) {
                    
                    for(final MovimentoEstoqueCota mec : itemNotaEnvio.getMovimentosProdutoSemEstudo()) {
                        
                        mec.setItemNotaEnvio(itemNotaEnvio);
                        
                        if (mec.getLancamento() != null){
                            
                            seqMatrizlancamento = mec.getLancamento().getSequenciaMatriz();
                        }
                        
                        this.movimentoEstoqueCotaRepository.merge(mec);
                    }
                    
                } else {
                    
                    seqMatrizlancamento = itemNotaEnvio.getEstudoCota().getEstudo().getLancamento().getSequenciaMatriz();
                }
                
                itemNotaEnvio.setSequenciaMatrizLancamento(seqMatrizlancamento);
            }
        }
    }
    
    /**
     * Gera Nota de envio da Cota
     * @param pessoaEmitente
     * @param idCota
     * @param notasEnvio
     * @param chaveAcesso
     * @param codigoNaturezaOperacao
     * @param descricaoNaturezaOperacao
     * @param listaEstudosCota
     * @param descontos TODO
     * @param enderecoDistribuidor TODO
     * @param telefoneDistribuidor TODO
     * @param mapMovimentoEstoqueCota TODO
     * @param idRota
     * @param dataEmissao
     * @param periodo
     * @param listaIdFornecedores
     */
    private void getNotaEnvioCota(final PessoaJuridica pessoaEmitente,
            final Long idCota,
            final FiltroConsultaNotaEnvioDTO filtro,
            final List<NotaEnvio> notasEnvio,
            final String chaveAcesso,
            final Integer codigoNaturezaOperacao,
            final String descricaoNaturezaOperacao,
            final List<EstudoCota> listaEstudosCota,
            final Map<String, DescontoDTO> descontos,
            final EnderecoDistribuidor enderecoDistribuidor,
            final TelefoneDistribuidor telefoneDistribuidor, 
            final Map<Long, List<MovimentoEstoqueCota>> mapMovimentoEstoqueCota) {
        
        final Cota cota = cotaRepository.buscarPorId(idCota);
        
        if (cota == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Cota " + idCota + " não encontrada!");
        }
        
        final IdentificacaoDestinatario destinatarioAtualizado = this.obterDestinatarioAtualizado(cota, filtro.getIdRota(), filtro.getIntervaloMovimento());
        
        if (destinatarioAtualizado == null) {
            return;// Caso retorne null pular próxima cota pois não encontrou
            // endereço PDV p uma cota sem movimentoEstudo
        }
        
        NotaEnvio notaEnvio = criarNotaEnvio(destinatarioAtualizado, chaveAcesso, codigoNaturezaOperacao,
        		descricaoNaturezaOperacao, filtro.getDataEmissao(), pessoaEmitente, enderecoDistribuidor, telefoneDistribuidor);
        
        notasEnvio.add(notaEnvio);
        
        final List<ItemNotaEnvio> listaItemNotaEnvio = 
        		this.processarNotasDeEnvioGeradas(cota, filtro, notasEnvio, listaEstudosCota, destinatarioAtualizado, descontos, mapMovimentoEstoqueCota);
        
        if (listaItemNotaEnvio != null && !listaItemNotaEnvio.isEmpty()) {
            
            notaEnvio.setListaItemNotaEnvio(listaItemNotaEnvio);
            this.atualizaMovimentosEstoqueItemNotaEnvio(notaEnvio, listaItemNotaEnvio);
            this.notaEnvioRepository.adicionar(notaEnvio);    
            
        } else {
        	notasEnvio.remove(notaEnvio);
        }
    }
        
                                                    /*
     * Efetua o processamento das notas de envio já geradas
     */
    private List<ItemNotaEnvio> processarNotasDeEnvioGeradas(final Cota cota,
            final FiltroConsultaNotaEnvioDTO filtro,
            final List<NotaEnvio> notasEnvio,
            final List<EstudoCota> listaEstudosCota,
            final IdentificacaoDestinatario destinatarioAtualizado,
            final Map<String, DescontoDTO> descontos,
            final Map<Long, List<MovimentoEstoqueCota>> mapMovimentoEstoqueCota) {
        
        final List<ItemNotaEnvio> listaItemNotaEnvio = 
        		this.gerarItensNotaEnvio(listaEstudosCota, cota, 
        								 mapMovimentoEstoqueCota.get(cota.getId()), 
        								 filtro.getIntervaloMovimento(), descontos);
        
        if (listaItemNotaEnvio==null || listaItemNotaEnvio.isEmpty()) {
            
            return null;
        }
        
        this.atualizarNotasEnvio(notasEnvio, listaItemNotaEnvio, destinatarioAtualizado);
        
        return listaItemNotaEnvio;
    }
    
    private IdentificacaoDestinatario obterDestinatarioAtualizado(final Cota cota, Long idRota, final Intervalo<Date> periodo){
    	
    	final PDV pdvPrincipalCota = cota.getPDVPrincipal();
    	
    	if (idRota == null) {
            
            idRota = this.getIdRotaCota(pdvPrincipalCota, cota);
        }
        
        return this.carregaDestinatario(cota, idRota, pdvPrincipalCota, periodo);
    }
    
    /**
     * Separa as Notas de Envio por Cota
     * @param estudosCotas
     * @return Map<Long,List<EstudoCota>>
     */
    private Map<Long,List<EstudoCota>> getMapEstudosCota(final List<EstudoCota> estudosCotas){
        
        final Map<Long,List<EstudoCota>> estudosCota = new HashMap<Long, List<EstudoCota>>();
        
        List<EstudoCota> estudos = null;
        
        for (final EstudoCota estudo : estudosCotas){
            
            estudos = estudosCota.get(estudo.getCota().getId());
            
            estudos = estudos == null ? new ArrayList<EstudoCota>() : estudos;
            
            estudos.add(estudo);
            
            estudosCota.put(estudo.getCota().getId(), estudos);
        }
        
        return estudosCota;
    }
    
	                                                                /**
     * Ordena a lista de Notas de Envio por Roteirização.
     * 
     * @param notasEnvio - lista de notas de envio
     * 
     * @return List<NotaEnvio>
     */
    private List<NotaEnvio> ordenarNotasEnvioPorRoteirizacao(final List<NotaEnvio> notasEnvio) {
        
        final Map<Integer, List<NotaEnvio>> mapaNotasEnvioPorCota =
                new HashMap<Integer, List<NotaEnvio>>();
        
        for (final NotaEnvio ne : notasEnvio) {
            
            final Integer numeroCota = ne.getDestinatario().getNumeroCota();
            
            List<NotaEnvio> nes =
                    mapaNotasEnvioPorCota.get(numeroCota);
            
            if (nes == null) {
                
                nes = new ArrayList<NotaEnvio>();
            }
            
            nes.add(ne);
            
            mapaNotasEnvioPorCota.put(numeroCota, nes);
        }
        
        final List<Integer> numerosCotaOrdenadosPelaRoteirizacao = roteirizacaoRepository.obterNumerosCotaOrdenadosRoteirizacao();
        
        final List<NotaEnvio> notasEnvioOrdenadas = new ArrayList<>();
        
        for (final Integer numeroCota : numerosCotaOrdenadosPelaRoteirizacao) {
            
            if (mapaNotasEnvioPorCota.containsKey(numeroCota)) {
                
                notasEnvioOrdenadas.addAll(mapaNotasEnvioPorCota.get(numeroCota));
            }
        }
        
        return notasEnvioOrdenadas;
    }
    
    /**
     * Gera Notas de Envio para as Cotas
     * @param idCotas
     * @param filtro
     * @param chaveAcesso
     * @param codigoNaturezaOperacao
     * @param descricaoNaturezaOperacao
     */
    private List<NotaEnvio> gerar(final List<Long> idCotas, 
    		final FiltroConsultaNotaEnvioDTO filtro, 
    		final String chaveAcesso,
            final Integer codigoNaturezaOperacao, 
            final String descricaoNaturezaOperacao) {
        
        if (idCotas == null || idCotas.isEmpty()) {
            return null;
        }
        
        final Distribuidor distribuidor = distribuidorRepository.obter();
        
        final TelefoneDistribuidor telefoneDistribuidor = distribuidorRepository.obterTelefonePrincipal();
        
        final List<EstudoCota> listaEstudosCotas = estudoCotaRepository.obterEstudosCotaParaNotaEnvio(idCotas, filtro.getIntervaloMovimento(), filtro.getIdFornecedores(), filtro.getExibirNotasEnvio());
        
        final List<NotaEnvio> notasEnvio = new ArrayList<>();
        
        final Map<Long, List<EstudoCota>> mapEstudosCota = this.getMapEstudosCota(listaEstudosCotas);
        
        final Map<String, DescontoDTO> descontos = descontoService.obterDescontosMapPorLancamentoProdutoEdicao();
        
        final Map<Long, List<MovimentoEstoqueCota>> mapMovimentoEstoqueCota = this.obterMapMovimentoEstoqueCota(idCotas, filtro);
        
        for (final Long idCota : idCotas) {
            
            this.getNotaEnvioCota(distribuidor.getJuridica(),
                    idCota,
                    filtro,
                    notasEnvio,
                    chaveAcesso,
                    codigoNaturezaOperacao,
                    descricaoNaturezaOperacao,
                    mapEstudosCota.get(idCota),
                    descontos,
                    distribuidor.getEnderecoDistribuidor(),
                    telefoneDistribuidor, mapMovimentoEstoqueCota);
        }
        
        return this.ordenarNotasEnvioPorRoteirizacao(notasEnvio);
    }
    
    private Map<Long, List<MovimentoEstoqueCota>> obterMapMovimentoEstoqueCota(final List<Long> idCotas, 
    		final FiltroConsultaNotaEnvioDTO filtro){
    	
    	List<MovimentoEstoqueCota> listaMovimentoEstoqueCotaSemEstudo = 
    			movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaSemEstudoPor(idCotas, 
																	                 filtro.getIntervaloMovimento(), 
																	                 filtro.getIdFornecedores(),
																	                 Arrays.asList(GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE));
    	
    	if(listaMovimentoEstoqueCotaSemEstudo == null || listaMovimentoEstoqueCotaSemEstudo.isEmpty()){
    		return new HashMap<>();
    	}
    	
    	HashMap<Long, List<MovimentoEstoqueCota>> map = new HashMap<>();
    	
    	for(MovimentoEstoqueCota item : listaMovimentoEstoqueCotaSemEstudo){
    		
    		Long idCota = item.getCota().getId();
    		
    		List<MovimentoEstoqueCota> itensCota =  map.get(idCota);
    		
    		if(itensCota == null){
    			
    			itensCota = new ArrayList<>();	
    		}
    		
    		itensCota.add(item);
    		
    		map.put(idCota, itensCota);
    	}
    	
    	return map;
    }
    
    /**
     * Cria nova nota de Envio
     * @param destinatarioAtualizado
     * @param chaveAcesso
     * @param codigoNaturezaOperacao
     * @param descricaoNaturezaOperacao
     * @param dataEmissao
     * @param pessoaEmitente
     * @param enderecoDistribuidor TODO
     * @param telefoneDistribuidor TODO
     * @return NotaEnvio
     * @throws ValidacaoException
     */
    private NotaEnvio criarNotaEnvio(final IdentificacaoDestinatario destinatarioAtualizado,
            final String chaveAcesso, final Integer codigoNaturezaOperacao,
            final String descricaoNaturezaOperacao, final Date dataEmissao,
            final PessoaJuridica pessoaEmitente, final EnderecoDistribuidor enderecoDistribuidor,
            final TelefoneDistribuidor telefoneDistribuidor) throws ValidacaoException {
        
        final NotaEnvio notaEnvio = new NotaEnvio();
        
        notaEnvio.setEmitente(carregarEmitente(pessoaEmitente, enderecoDistribuidor, telefoneDistribuidor));
        notaEnvio.setDestinatario(destinatarioAtualizado);
        notaEnvio.setChaveAcesso(chaveAcesso);
        notaEnvio.setCodigoNaturezaOperacao(codigoNaturezaOperacao);
        notaEnvio.setDescricaoNaturezaOperacao(descricaoNaturezaOperacao);
        notaEnvio.setDataEmissao(dataEmissao);
        
        return notaEnvio;
    }
    
    /**
     * @param enderecoDistribuidor TODO
     * @param telefoneDistribuidor TODO
     * @param distribuidor
     * @return
     * @throws ValidacaoException
     */
    private IdentificacaoEmitente carregarEmitente(final PessoaJuridica pessoaEmitente, final EnderecoDistribuidor enderecoDistribuidor, final TelefoneDistribuidor telefoneDistribuidor)
            throws ValidacaoException {
        
        if (enderecoDistribuidor == null) {
            throw new ValidacaoException(TipoMensagem.ERROR, "Endereço principal do distribuidor não encontrado!");
        }
        
        final IdentificacaoEmitente emitente = new IdentificacaoEmitente();
        
        // Corrigido devo ao fato da tabela pessoa gravar os documentos com
        // máscara, embora o campo documento da tabela nota_envio esperar apenas
        // 14 caracteres
        final String documento = pessoaEmitente.getDocumento().replaceAll("[-+.^:,/]", "");
        
        emitente.setDocumento(documento);
        emitente.setNome(pessoaEmitente.getNome());
        emitente.setPessoaEmitenteReferencia(pessoaEmitente);
        emitente.setInscricaoEstadual(pessoaEmitente.getInscricaoEstadual());
        
        try {
            emitente.setEndereco(cloneEndereco(enderecoDistribuidor.getEndereco()));
        } catch (final Exception exception) {
            final String msg = "Erro ao adicionar o endereço do distribuidor!";
            LOGGER.error(msg, exception);
            throw new ValidacaoException(TipoMensagem.ERROR, msg);
        }
        
        if (telefoneDistribuidor != null) {
            final Telefone telefone = telefoneDistribuidor.getTelefone();
            
            /*
            telefoneRepository.detach(telefone);
            telefone.setId(null);
            telefone.setPessoa(null);
            telefoneRepository.adicionar(telefone);
            */
            
            emitente.setTelefone(telefone);
        }
        return emitente;
    }
    
    private IdentificacaoDestinatario carregaDestinatario(final Cota cota, final Long idRota, final PDV pdvPrincipalCota, final Intervalo<Date> periodo) {
        
        final IdentificacaoDestinatario destinatario = new IdentificacaoDestinatario();
        
        destinatario.setNumeroCota(cota.getNumeroCota());
        
        destinatario.setDocumento(cota.getPessoa().getDocumento());
        
        EnderecoPDV enderecoPdv = pdvPrincipalCota!=null? pdvPrincipalCota.getEnderecoEntrega():null;
        
        if (enderecoPdv == null) {
            
            for (final EnderecoPDV ePdv : pdvPrincipalCota.getEnderecos()){
                
                if (ePdv.isPrincipal()){
                    
                    enderecoPdv = ePdv;
                    break;
                }
            }
        }
        
        if (enderecoPdv == null) {
            
			                                                                                                                                                                                                /*
             * Verifica se exite movimento e estudo para a cota na data
             * 
             * Caso exista o sistema deve apresentar erro por falta de endereço
             * PDV
             */
            if(existeMovimentoEstudoCotaData(cota, periodo)){
                
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Endereço do PDV principal da cota "
                                + cota.getNumeroCota() + " não encontrado!");
            }else{
                return null;
            }
        }
        
        NotaEnvioEndereco enderecoDestinatario = new NotaEnvioEndereco();
		BeanUtils.copyProperties(enderecoPdv.getEndereco(), enderecoDestinatario, "id");
		destinatario.setEndereco(enderecoDestinatario);
        
        if (cota.getPessoa() instanceof PessoaJuridica) {
            
            final PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
            
            destinatario.setInscricaoEstadual(pessoaJuridica.getInscricaoEstadual());
            
            destinatario.setDocumento(pessoaJuridica.getCnpj());
        } else if (cota.getPessoa() instanceof PessoaFisica) {
            
            final PessoaFisica pessoaFisica = (PessoaFisica) cota.getPessoa();
            
            destinatario.setInscricaoEstadual(pessoaFisica.getRg());
            
            destinatario.setDocumento(pessoaFisica.getCpf());
        }
        
        destinatario.setNome(cota.getPessoa().getNome());
        destinatario.setPessoaDestinatarioReferencia(cota.getPessoa());
        
        if (cota.getTefefonePrincipal() != null) {
            destinatario.setTelefone(cota.getTefefonePrincipal().getTelefone());
        }
        
        if(cota.getBox() != null) {
            destinatario.setCodigoBox(cota.getBox().getCodigo());
            destinatario.setNomeBox(cota.getBox().getNome());
        }
        
        if (idRota != null) {
            
            final Rota rota = rotaRepository.buscarPorId(idRota);
            
            if (rota == null) {
                
                throw new ValidacaoException(TipoMensagem.ERROR, "Rota não encontrada!");
            }
            
            destinatario.setCodigoRota(rota.getId().toString());
            destinatario.setDescricaoRota(rota.getDescricaoRota());
        }
        return destinatario;
    }
    
    private boolean existeMovimentoEstudoCotaData(final Cota cota, final Intervalo<Date> periodo) {
        
    	final List<EstudoCota> obterEstudoCota = estudoCotaRepository.obterEstudoCota(cota.getId(), periodo.getDe(), periodo.getAte());
        
        final String[] gruposMovimentoEstoque   = {
                GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO.name(),
                GrupoMovimentoEstoque.FALTA_DE_COTA.name(),
                GrupoMovimentoEstoque.FALTA_EM_COTA.name(),
                GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE.name(),
                GrupoMovimentoEstoque.SOBRA_DE_COTA.name(),
                GrupoMovimentoEstoque.SOBRA_EM_COTA.name(),
                GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE.name(),
                GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE.name()
        };
        
        final Map<Long, BigInteger> obterQtdMovimentoCotaPorTipoMovimento = movimentoEstoqueCotaRepository.obterQtdMovimentoCotaPorTipoMovimento(periodo, cota.getId(), gruposMovimentoEstoque);
        
        if ((obterEstudoCota != null && !obterEstudoCota.isEmpty())
                ||
                (obterQtdMovimentoCotaPorTipoMovimento != null && !obterQtdMovimentoCotaPorTipoMovimento.isEmpty())){
            return true;
        }
        
        return false;
    }
    
    private Endereco cloneEndereco(final Endereco endereco) throws CloneNotSupportedException {
        final Endereco novoEndereco = endereco.clone();
        
        if (novoEndereco.getCep() != null) {
            novoEndereco.setCep(novoEndereco.getCep().replace("-", ""));
        }
        if (novoEndereco.getCodigoUf() == null && novoEndereco.getCodigoCidadeIBGE() != null) {
            if (novoEndereco.getCodigoCidadeIBGE().toString().length() > 2) {
                novoEndereco.setCodigoUf(novoEndereco.getCodigoCidadeIBGE());
            } else {
                novoEndereco.setCodigoUf(novoEndereco.getCodigoCidadeIBGE());
            }
        }
        // enderecoRepository.adicionar(novoEndereco);
        return novoEndereco;
    }
    
    @Override
    @Transactional
    public List<NotaEnvio> gerarNotasEnvio(final FiltroConsultaNotaEnvioDTO filtro) {
        
        List<NotaEnvio> listaNotaEnvio = null;
        
        if (TRAVA_GERACAO_NE.get("neCotasSendoGeradas") != null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Notas de envio sendo geradas por outro usuário, tente novamente mais tarde.");
        }
        
        TRAVA_GERACAO_NE.put("neCotasSendoGeradas", true);
        
        try {
            
            final List<Long> listaIdCotas = this.getIdsCotaIntervalo(filtro);
            
            final List<Long> idCotasAusentes = cotaAusenteRepository.obterIdsCotasAusentesNoPeriodo(filtro.getIntervaloMovimento());
            
            if (idCotasAusentes != null) {
                listaIdCotas.removeAll(idCotasAusentes);
            }
            
            this.validarRoteirizacaoCota(filtro, listaIdCotas);
            
            listaNotaEnvio = this.gerar(listaIdCotas, filtro, null, null, null);
        } finally {
            TRAVA_GERACAO_NE.remove("neCotasSendoGeradas");
        }
        
        // if(true){        	
        	// throw new ValidacaoException(TipoMensagem.ERROR, "não gerar nota envio!!!!");
        // }
        
        return listaNotaEnvio;
    }
    
    private void validarRoteirizacaoCota(final FiltroConsultaNotaEnvioDTO filtro,final List<Long> listaIdCotas) {
        
    	final List<ItemDTO<String, String>> itens = cotaRepository.obterCotasSemRoterizacao(listaIdCotas);
    	
    	final List<String> cotasSemRoteirizacao = new ArrayList<String>();
        
        if(itens != null && !itens.isEmpty()){        	
            cotasSemRoteirizacao.add("Cota(s) com problemas de Roteirização:");
            
            for (ItemDTO<String, String> item : itens) {
            	final StringBuilder cotaSemRoteirizacao = new StringBuilder("Cota: "+ String.valueOf(item.getKey()) +" / "+ item.getValue());
            	cotasSemRoteirizacao.add(cotaSemRoteirizacao.toString());
            }
            
            if (!cotasSemRoteirizacao.isEmpty()) {
            	throw new ValidacaoException(TipoMensagem.WARNING, cotasSemRoteirizacao);
            }
        }
    	
    }
    
    private List<Long> getIdsCotaIntervalo(final FiltroConsultaNotaEnvioDTO filtro){
        
        final List<SituacaoCadastro> situacoesCadastro = Arrays.asList(SituacaoCadastro.ATIVO,SituacaoCadastro.SUSPENSO);
        
        final List<Long> listaIdCotas = cotaRepository.obterIdCotasEntre(filtro.getIntervaloCota(), filtro.getIntervaloBox(), situacoesCadastro, filtro.getIdRoteiro(), filtro.getIdRota(), null, null, null, null);
        
        return listaIdCotas;
    }

    @Transactional
    public void gerarNotaEnvioAtravesNotaFiscal(NotaFiscal notaFiscal) {
		
		popularNotaEnvioAtravesNotaFiscal(notaFiscal);
		
	}
	
	private NotaEnvio popularNotaEnvioAtravesNotaFiscal(NotaFiscal notaFiscal){
		
		NotaEnvio notaEnvio = new NotaEnvio();
		notaEnvio.setNotaFiscalID(notaFiscal.getId());
		notaEnvio.setChaveAcesso(notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica().getChaveAcesso());
		notaEnvio.setDataEmissao(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getDataEmissao());
		notaEnvio.setNotaImpressa(false);
		
		this.populateIdentificacaoEmitente(notaFiscal, notaEnvio);
		this.populateIdentificacaoDestinatario(notaFiscal, notaEnvio);
		notaEnvioRepository.adicionar(notaEnvio);
		notaEnvio.setListaItemNotaEnvio(this.criarItensNotaEnvio(notaFiscal, notaEnvio));
		return notaEnvio;
	}
	
	private void populateIdentificacaoEmitente(NotaFiscal notaFiscal, NotaEnvio notaEnvio){
		
		IdentificacaoEmitente identificacaoEmitente = new IdentificacaoEmitente();
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		Telefone telefone = null;
		for(TelefoneDistribuidor td : distribuidor.getTelefones()) {
			telefone = td.getTelefone();
		}

		identificacaoEmitente.setDocumento(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getDocumento().getDocumento());
		identificacaoEmitente.setEndereco(distribuidor.getEnderecoDistribuidor().getEndereco());
		identificacaoEmitente.setInscricaoEstadual(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getInscricaoEstadual());
		identificacaoEmitente.setNome(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getNome());
		identificacaoEmitente.setPessoaEmitenteReferencia(distribuidorRepository.obter().getJuridica());
		identificacaoEmitente.setTelefone(telefone);
		
		notaEnvio.setEmitente(identificacaoEmitente);
	}
	
	private void populateIdentificacaoDestinatario(NotaFiscal notaFiscal, NotaEnvio notaEnvio){
		
		IdentificacaoDestinatario identificacaoDestinatario = new IdentificacaoDestinatario();
		Cota cota = notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getCota();

		identificacaoDestinatario.setDocumento(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getDocumento().getDocumento());
		NotaEnvioEndereco enderecoDestinatario = new NotaEnvioEndereco();
		BeanUtils.copyProperties(cota.getEnderecoPrincipal().getEndereco(), enderecoDestinatario, "id");
		
		identificacaoDestinatario.setEndereco(enderecoDestinatario);
		identificacaoDestinatario.setInscricaoEstadual(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getInscricaoEstadual());
		identificacaoDestinatario.setNome(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getNome());
		identificacaoDestinatario.setPessoaDestinatarioReferencia(distribuidorRepository.obter().getJuridica());
		
		if(cota.getTefefonePrincipal() == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Telefone da cota ['" + cota.getNumeroCota() + "'] não pode ser nulo!");
		} else {			
			identificacaoDestinatario.setTelefone(cota.getTefefonePrincipal().getTelefone());
		}
		
		identificacaoDestinatario.setNumeroCota(cota.getNumeroCota());
		
		notaEnvio.setDestinatario(identificacaoDestinatario);
	}
	
	private List<ItemNotaEnvio> criarItensNotaEnvio(NotaFiscal notaFiscal, NotaEnvio notaEnvio) {
		
		List<ItemNotaEnvio> lisItemNotaEnvios = new ArrayList<>();
		
		ItemNotaEnvio itemNotaEnvio = null;
		
		Integer i = 0;
		for (DetalheNotaFiscal item : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
			itemNotaEnvio = new ItemNotaEnvio();
			ItemNotaEnvioPK itemPK = new ItemNotaEnvioPK(notaEnvio, ++i);
			
			itemNotaEnvio.setItemNotaEnvioPK(itemPK);
			itemNotaEnvio.setProdutoEdicao(item.getProdutoServico().getProdutoEdicao());
			itemNotaEnvio.setCodigoProduto(item.getProdutoServico().getProdutoEdicao().getProduto().getCodigo());
			itemNotaEnvio.setNumeroEdicao(item.getProdutoServico().getProdutoEdicao().getNumeroEdicao());
			itemNotaEnvio.setPublicacao(item.getProdutoServico().getProdutoEdicao().getProduto().getNomeComercial());
			itemNotaEnvio.setDesconto(item.getProdutoServico().getProdutoEdicao().getDesconto());
			itemNotaEnvio.setReparte(item.getProdutoServico().getQuantidade());
			itemNotaEnvio.setPrecoCapa(item.getProdutoServico().getProdutoEdicao().getPrecoVenda());
			lisItemNotaEnvios.add(itemNotaEnvio);
		}
		
		return lisItemNotaEnvios;
		
	}
    
}