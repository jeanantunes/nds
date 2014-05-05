package br.com.abril.nds.integracao.ems0135.processor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.data.helper.LancamentoDataHelper;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0135Input;
import br.com.abril.nds.integracao.model.canonic.EMS0135InputItem;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.TipoProdutoRepository;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;

@Component
public class EMS0135MessageProcessor extends AbstractRepository implements MessageProcessor {

    
    @Autowired
    private NdsiLoggerFactory ndsiLoggerFactory;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private TipoProdutoRepository tipoProdutoRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
	private FornecedorRepository fornecedorRepository;
    
    @Autowired
	private LancamentoService lancamentoService;
       
    @Override
    public void preProcess(AtomicReference<Object> tempVar) {
        tempVar.set(new ArrayList<NotaFiscalEntradaFornecedor>());
    }
    
    @Override
    public void processMessage(Message message) {
        
        EMS0135Input input = (EMS0135Input) message.getBody();
        
        NotaFiscalEntradaFornecedor notafiscalEntrada = null;
        
        if (input.getChaveAcessoNF() != null && !input.getChaveAcessoNF().isEmpty()) {
            
            if (input.getNumeroNotaEnvio() == null || input.getNumeroNotaEnvio().isEmpty()) {
                this.ndsiLoggerFactory.getLogger().logInfo(
                        message,
                        EventoExecucaoEnum.RELACIONAMENTO,
                        String.format("Numero da nota de envio se encontra vazio para chave de acesso "
                                + input.getChaveAcessoNF() + ". Nenhum registro será atualizado ou inserido!"));
                return;
            }
            
            notafiscalEntrada = obterNotaFiscalPorNumeroNotaEnvio(input.getNumeroNotaEnvio());
            
            // Caso encontre a nota fiscal de entrada, atualiza com a nova chave
            // de acesso
            if (notafiscalEntrada != null) {
                
                String chaveAcessoAntiga = notafiscalEntrada.getChaveAcesso();
                
                notafiscalEntrada.setChaveAcesso(input.getChaveAcessoNF());
                notafiscalEntrada.setNumero(input.getNotaFiscal());
                notafiscalEntrada.setSerie(input.getSerieNotaFiscal());
                
                this.getSession().merge(notafiscalEntrada);
                this.ndsiLoggerFactory.getLogger().logInfo(
                        message,
                        EventoExecucaoEnum.INF_DADO_ALTERADO,
                        String.format("Nota Fiscal de Entrada " + input.getNumeroNotaEnvio()
                                + " atualizada com chave de acesso NFE de " + chaveAcessoAntiga.trim() + " para "
                                + input.getChaveAcessoNF() + " com sucesso!"));
                return;
            }
        }
        
        notafiscalEntrada = obterNotaFiscal(input.getNotaFiscal(), input.getSerieNotaFiscal(), input.getCnpjEmissor(),
                input.getNumeroNotaEnvio());
        
        if (notafiscalEntrada == null) {
            
            notafiscalEntrada = new NotaFiscalEntradaFornecedor();
            
            notafiscalEntrada = populaNotaFiscalEntrada(notafiscalEntrada, input);
            
            notafiscalEntrada = populaItemNotaFiscalEntrada(notafiscalEntrada, input, message);
            
            if (null != notafiscalEntrada) {
                
                notafiscalEntrada = calcularValores(notafiscalEntrada);
                
                this.getSession().persist(notafiscalEntrada);
                
                this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.SEM_DOMINIO,
                        String.format("Nota Fiscal de Entrada: %1$s"
                        		    + " inserida com chave de acesso NFE de " + input.getChaveAcessoNF().trim(), input.getNotaFiscal()));
                
            } else {
                
                String msg = "Nota fiscal com produtos não encontrados no sistema";
                
                if (input.getNotaFiscal() != null && input.getNotaFiscal() > 0) {
                    msg = String.format("Nota fiscal com produtos não encontrados no sistema, número nota: %1$s",
                            input.getNotaFiscal());
                }
                
                this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, msg);
                return;
            }
            
        } else {
            
            this.ndsiLoggerFactory.getLogger().logWarning(
                    message,
                    EventoExecucaoEnum.REGISTRO_JA_EXISTENTE,
                    String.format("Nota Fiscal %1$s já cadastrada com serie %2$s e nota envio %3$s",
                            notafiscalEntrada.getNumero(), notafiscalEntrada.getSerie(),
                            notafiscalEntrada.getNumeroNotaEnvio()));
            return;
        }
    }
    
    private NotaFiscalEntradaFornecedor populaNotaFiscalEntrada(NotaFiscalEntradaFornecedor notafiscalEntrada,
            EMS0135Input input) {
        
        notafiscalEntrada.setDataEmissao(input.getDataEmissao());
        
        notafiscalEntrada.setNumero(input.getNotaFiscal() != null ? input.getNotaFiscal().longValue() : null);
        
        notafiscalEntrada.setSerie(input.getSerieNotaFiscal() != null && !input.getSerieNotaFiscal().isEmpty()
                && !"0".equals(input.getSerieNotaFiscal()) ? input.getSerieNotaFiscal() : null);
        
        notafiscalEntrada.setDataExpedicao(input.getDataEmissao());
        
        notafiscalEntrada.setChaveAcesso(input.getChaveAcessoNF() != null && !input.getChaveAcessoNF().isEmpty()
                && !"0".equals(input.getChaveAcessoNF()) ? input.getChaveAcessoNF() : null);
        notafiscalEntrada.setCfop(obterCFOP());
        
        notafiscalEntrada.setOrigem(Origem.INTERFACE);
        
        notafiscalEntrada.setStatusNotaFiscal(StatusNotaFiscalEntrada.NAO_RECEBIDA);
        
        notafiscalEntrada.setNumeroNotaEnvio(Long.parseLong(input.getNumeroNotaEnvio()));
        
        notafiscalEntrada.setValorBruto(BigDecimal.ZERO);
        
        notafiscalEntrada.setValorLiquido(BigDecimal.ZERO);
        
        notafiscalEntrada.setValorDesconto(BigDecimal.ZERO);
        
        PessoaJuridica emitente = this.obterPessoaJuridica(input.getCnpjEmissor());
        
        notafiscalEntrada.setEmitente(emitente);
        
        notafiscalEntrada.setTipoNotaFiscal(obterTipoNotaFiscal(GrupoNotaFiscal.NF_REMESSA_MERCADORIA_CONSIGNACAO,
                TipoUsuarioNotaFiscal.TREELOG, TipoUsuarioNotaFiscal.DISTRIBUIDOR));
        
        notafiscalEntrada.setEmitida(true);
        
        return notafiscalEntrada;
    }
    
    private NotaFiscalEntradaFornecedor populaItemNotaFiscalEntrada(NotaFiscalEntradaFornecedor nfEntrada,
            EMS0135Input input, Message message) {
        
        List<EMS0135InputItem> items = input.getItems();
        
        for (EMS0135InputItem inputItem : items) {
            
            final String codigoProduto = inputItem.getCodigoProduto();
            final Long edicao = inputItem.getEdicao();
            
            ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto, edicao);
            
            if (produtoEdicao == null) {
                
                Produto produto = this.produtoRepository.obterProdutoPorCodigoProdinLike(codigoProduto);
                
                if (produto == null) {
                    
                    TipoProduto tipoProduto = this.tipoProdutoRepository.buscarPorId(1L);
                    
                    Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
                    Fornecedor fornecedor = obterFornecedor(message);
                    if(fornecedor != null) {
                    	fornecedores.add(fornecedor);
                    }
                    
                    produto = new Produto();
                    
                    produto.setCodigo(inputItem.getCodigoProduto());
                    produto.setCodigoICD(inputItem.getCodigoProduto());
                    produto.setPeriodicidade(PeriodicidadeProduto.MENSAL);
                    produto.setNome(inputItem.getNomeProduto());
                    //produto.setNomeComercial(inputItem.getNomeProduto());
                    produto.setOrigem(Origem.MANUAL);
                    produto.setTipoProduto(tipoProduto);
                    produto.setPacotePadrao(10);
                    produto.setPeb(35);
                    produto.setPeso(100L);
                    produto.setIsGeracaoAutomatica(false);
                    produto.setFornecedores(fornecedores);
                    if(inputItem.getDesconto() != null) {
                        produto.setDesconto(BigDecimal.valueOf(inputItem.getDesconto()));
                    }
                    produto.setFormaComercializacao(FormaComercializacao.CONSIGNADO);

                    this.getSession().persist(produto);
                }
                
                produtoEdicao = new ProdutoEdicao();
                produtoEdicao.setProduto(produto);
                produtoEdicao.setNumeroEdicao(inputItem.getEdicao());
                
                if(inputItem.getDesconto() != null) {
                    produtoEdicao.setDesconto(BigDecimal.valueOf(inputItem.getDesconto()));
                }
                
                produtoEdicao.setPacotePadrao(10);
                produtoEdicao.setPeb(35);
                produtoEdicao.setPeso(100L);
                produtoEdicao.setPossuiBrinde(false);
                produtoEdicao.setPermiteValeDesconto(false);
                produtoEdicao.setParcial(false);
                produtoEdicao.setAtivo(true);
                produtoEdicao.setOrigem(Origem.PRODUTO_SEM_CADASTRO);
                produtoEdicao.setPrecoPrevisto(new BigDecimal(inputItem.getPreco()));
                produtoEdicao.setPrecoVenda(produtoEdicao.getPrecoPrevisto());
                this.getSession().persist(produtoEdicao);
                
                Date dataAtual = new Date();
                Date dataLancamento = inputItem.getDataLancamento();
                int numeroLancamentoNovo = 1;
                
                dataLancamento = dataLancamento == null ? dataAtual : dataLancamento;
                
                try {
    				//lancamento.setDataLancamentoDistribuidor(getDiaMatrizAberta(input.getDataLancamento(),dataRecolhimento,message,codigoProduto,edicao));
                	dataLancamento =lancamentoService.obterDataLancamentoValido(dataLancamento, produtoEdicao.getProduto().getFornecedor().getId());
    			} catch (Exception e) {
    			}
                Date dataRecolhimento = DateUtil.adicionarDias(dataLancamento, produto.getPeb());
                Lancamento lancamento = new Lancamento();
                lancamento.setDataCriacao(dataAtual);
                lancamento.setNumeroLancamento(numeroLancamentoNovo);
                lancamento.setDataLancamentoPrevista(dataLancamento);
                lancamento.setDataLancamentoDistribuidor(dataLancamento);
                lancamento.setDataRecolhimentoPrevista(dataRecolhimento);
                lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);
                
                lancamento.setProdutoEdicao(produtoEdicao);
                lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
                lancamento.setDataStatus(dataAtual);
                lancamento.setStatus(StatusLancamento.CONFIRMADO);
                lancamento.setReparte(new BigInteger(inputItem.getQtdExemplar().toString()));
                this.getSession().persist(lancamento);
            }
            
            ItemNotaFiscalEntrada item = new ItemNotaFiscalEntrada();
            
            item.setQtde(new BigInteger(inputItem.getQtdExemplar().toString()));
            item.setNotaFiscal(nfEntrada);
            item.setProdutoEdicao(produtoEdicao);
            item.setPreco(BigDecimal.valueOf(inputItem.getPreco()));
            item.setDesconto(BigDecimal.valueOf(inputItem.getDesconto()));
            
            Lancamento lancamento = obterLancamentoProdutoEdicao(produtoEdicao.getId());
            if (null == lancamento) {
                Calendar cal = Calendar.getInstance();
                
                cal.add(Calendar.DAY_OF_MONTH, 2);
                item.setDataLancamento(cal.getTime());
                
                cal.add(Calendar.DAY_OF_MONTH, produtoEdicao.getPeb());
                item.setDataRecolhimento(cal.getTime());
                
                item.setTipoLancamento(TipoLancamento.LANCAMENTO);
                
            } else {
                item.setDataLancamento(lancamento.getDataLancamentoPrevista());
                item.setDataRecolhimento(lancamento.getDataRecolhimentoPrevista());
                item.setTipoLancamento(lancamento.getTipoLancamento());
            }
            nfEntrada.getItens().add(item);
        }
        
        return nfEntrada;
    }
    
    /**
     * Realiza os cálculos de valores da Nota Fiscal. Após os cálculos, salva os
     * novos valores.
     * 
     * @param nfEntrada
     * @param input
     */
    private NotaFiscalEntradaFornecedor calcularValores(NotaFiscalEntradaFornecedor nfEntrada) {
        
        BigDecimal valorBruto = this.calcularValorBruto(nfEntrada);
        nfEntrada.setValorBruto(valorBruto);
        
        BigDecimal valorLiquido = this.calcularValorLiquido(nfEntrada);
        nfEntrada.setValorLiquido(valorLiquido);
        
        BigDecimal valorDesconto = valorBruto.subtract(valorLiquido);
        nfEntrada.setValorDesconto(valorDesconto);
        
        return nfEntrada;
    }
    
    /**
     * Método que contém as regras para o cálculo do "Valor Bruto" de uma NF.
     * 
     * @param nfEntrada
     * @param input
     * @return
     */
    private BigDecimal calcularValorBruto(NotaFiscalEntradaFornecedor nfEntrada) {
        
        BigDecimal valorBrutoTotal = nfEntrada.getValorBruto();
        
        for (ItemNotaFiscalEntrada item : nfEntrada.getItens()) {
            
            BigDecimal valorBrutoItem = item.getPreco().multiply(BigDecimal.valueOf(item.getQtde().doubleValue()));
            
            valorBrutoTotal = valorBrutoTotal.add(valorBrutoItem);
            
        }
        
        return valorBrutoTotal;
    }
    
    /**
     * Método que contém as regras para o cálculo do "Valor Líquido" de uma NF.
     * 
     * @param nfEntrada
     * @param input
     * 
     * @return
     */
    private BigDecimal calcularValorLiquido(NotaFiscalEntradaFornecedor nfEntrada) {
        
        BigDecimal valorLiquidoTotal = nfEntrada.getValorLiquido();
        
        for (ItemNotaFiscalEntrada item : nfEntrada.getItens()) {
            
            BigDecimal valorDesconto = item.getPreco().multiply(item.getDesconto().divide(new BigDecimal(100)));
            
            BigDecimal valorLiquidoItem = item.getPreco().subtract(valorDesconto);
            
            BigDecimal valorTotalItem = valorLiquidoItem.multiply(BigDecimal.valueOf(item.getQtde().doubleValue()));
            
            valorLiquidoTotal = valorLiquidoTotal.add(valorTotalItem);
            
        }
        
        return valorLiquidoTotal;
    }
    
    private Lancamento obterLancamentoProdutoEdicao(Long idProdutoEdicao) {
        
        StringBuilder hql = new StringBuilder();
        
        hql.append(" select lancamento ")
                .append(" from Lancamento lancamento ")
                .append(" where lancamento.dataLancamentoDistribuidor = ")
                .append(" (select max(lancamentoMaxDate.dataLancamentoDistribuidor) ")
                .append(" from Lancamento lancamentoMaxDate where lancamentoMaxDate.produtoEdicao.id=:idProdutoEdicao ) ")
                .append(" and lancamento.produtoEdicao.id=:idProdutoEdicao ");
        
        Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("idProdutoEdicao", idProdutoEdicao);
        
        Object lancamento = query.uniqueResult();
        
        return (lancamento != null) ? (Lancamento) lancamento : null;
        
    }
    
    private NotaFiscalEntradaFornecedor obterNotaFiscal(Long numero, String serie, String cnpjEmissor,
            String numeroNotaEnvio) {
        StringBuilder hql = new StringBuilder();
        
        boolean existeNotaFiscal = (numero != null && !numero.equals(0L) && serie != null && !serie.isEmpty() && !serie
                .equals("0"));
        
        PessoaJuridica emitente = this.obterPessoaJuridica(cnpjEmissor);
        
        hql.append("from NotaFiscalEntradaFornecedor nf ");
        hql.append("where nf.emitente = :emitente ");
        
        if (existeNotaFiscal) {
            hql.append("and nf.numero = :numero and nf.serie = :serie");
        } else {
            hql.append("and nf.numeroNotaEnvio = :numeroNotaEnvio ");
        }
        
        Query query = super.getSession().createQuery(hql.toString());
        
        query.setParameter("emitente", emitente);
        
        if (existeNotaFiscal) {
            if (numero != null && !numero.equals(0L)) {
                query.setParameter("numero", numero);
            }
            
            if (serie != null && !serie.isEmpty() && !serie.equals("0")) {
                query.setParameter("serie", serie);
            }
        } else {
            query.setParameter("numeroNotaEnvio", Long.parseLong(numeroNotaEnvio));
        }
        
        return (NotaFiscalEntradaFornecedor) query.uniqueResult();
        
    }
    
    /**
     * Obtém a nota fiscal de entrada do fornecedor através do campo
     * numeroNotaEnvio caso exista uma chaveAcesso
     * 
     * @param numeroNotaEnvio
     * @return
     */
    private NotaFiscalEntradaFornecedor obterNotaFiscalPorNumeroNotaEnvio(String numeroNotaEnvio) {
        StringBuilder hql = new StringBuilder();
        
        hql.append("from NotaFiscalEntradaFornecedor nf ").append("where nf.numeroNotaEnvio = :numeroNotaEnvio ");
        
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("numeroNotaEnvio", Long.parseLong(numeroNotaEnvio));
        return (NotaFiscalEntradaFornecedor) query.uniqueResult();
        
    }
    
    /**
     * Obtém o Produto Edição cadastrado previamente.
     * 
     * @param codigoPublicacao Código da Publicação.
     * @param edicao Número da Edição.
     * 
     * @return
     */
    private ProdutoEdicao obterProdutoEdicao(String codigoPublicacao, Long edicao) {
        
        Criteria criteria = this.getSession().createCriteria(ProdutoEdicao.class, "produtoEdicao");
        
        criteria.createAlias("produtoEdicao.produto", "produto");
        criteria.setFetchMode("produto", FetchMode.JOIN);
        
        criteria.add(Restrictions.eq("produto.codigo", codigoPublicacao));
        criteria.add(Restrictions.eq("produtoEdicao.numeroEdicao", edicao));
        
        return (ProdutoEdicao) criteria.uniqueResult();
    }
    
    private TipoNotaFiscal obterTipoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal, TipoUsuarioNotaFiscal emitente,
            TipoUsuarioNotaFiscal destinatario) {
        
        String hql = " from TipoNotaFiscal tipoNotaFiscal where tipoNotaFiscal.grupoNotaFiscal = :grupoNotaFiscal "
                + "and tipoNotaFiscal.emitente = :emitente and tipoNotaFiscal.destinatario = :destinatario "
                + " group by tipoNotaFiscal.id  ";
        
        Query query = getSession().createQuery(hql);
        
        query.setParameter("grupoNotaFiscal", grupoNotaFiscal);
        query.setParameter("emitente", emitente);
        query.setParameter("destinatario", destinatario);
        
        return (TipoNotaFiscal) query.uniqueResult();
    }
    
    private PessoaJuridica obterPessoaJuridica(String cnpj) {
        
        String hql = "from PessoaJuridica where cnpj = :cnpj";
        
        Query query = getSession().createQuery(hql);
        
        query.setParameter("cnpj", cnpj);
        
        return (PessoaJuridica) query.uniqueResult();
    }
    
    private CFOP obterCFOP() {
        
        Criteria criteria = getSession().createCriteria(CFOP.class);
        
        criteria.add(Restrictions.eq("codigo", "5102"));
        
        return (CFOP) criteria.uniqueResult();
    }
    
    private Fornecedor obterFornecedor(Message message) {
        String codigoDistribuidor = 
                message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue()).toString();
		
		Fornecedor fornecedor = this.fornecedorRepository.obterFornecedorPorCodigo(Integer.parseInt(codigoDistribuidor));
        return fornecedor;
    }
    
    @Override
    public void posProcess(Object tempVar) {
        // TODO Auto-generated method stub
    }
    
}
