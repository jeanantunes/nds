package br.com.abril.nds.integracao.ems0140.processor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0140Input;
import br.com.abril.nds.integracao.model.canonic.EMS0140InputItem;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.TipoProdutoRepository;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.RecolhimentoService;

import com.google.common.base.Strings;

@Component
public class EMS0140MessageProcessor extends AbstractRepository implements MessageProcessor {
    
    @Autowired
    private NdsiLoggerFactory ndsiLoggerFactory;
    
    @Autowired
    private TipoProdutoRepository tipoProdutoRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private LancamentoService lancamentoService;
    
    @Autowired
   	private RecolhimentoService recolhimentoService;
    
    @Override
    public void preProcess(AtomicReference<Object> tempVar) {
        tempVar.set(new ArrayList<EMS0140Input>());
    }
    
    @Override
    public void processMessage(Message message) {
        EMS0140Input input = (EMS0140Input) message.getBody();
        
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
            
            notafiscalEntrada = obterNotaFiscalPorNumeroNotaEnvio(input.getNumeroNotaEnvio(), input.getCnpjEmissor());
            
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
                                + " Atualizada com Chave de Acesso NFE"
                                +" de " + chaveAcessoAntiga 
                                +" para "+ input.getChaveAcessoNF() + " com sucesso!"));
                
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
                        String.format("Nota Fiscal Inserida no sistema %1$s", input.getNotaFiscal()));
            } else {
                
                String msg = "Nota Fiscal com produtos não encontrados no sistema";
                
                if (input.getNotaFiscal() != null && input.getNotaFiscal() > 0) {
                    msg = String.format("Nota Fiscal com produtos não encontrados no sistema, Número Nota %1$s",
                            input.getNotaFiscal());
                }
                
                this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, msg);
                return;
            }
            
        } else {
            
            this.ndsiLoggerFactory.getLogger().logWarning(
            
                    message,
                    
                    EventoExecucaoEnum.REGISTRO_JA_EXISTENTE,
                    
                    String.format("Nota Fiscal %1$s já cadastrada com Série %2$s Nota Envio %3$s",
                            notafiscalEntrada.getNumero(), notafiscalEntrada.getSerie(),
                            notafiscalEntrada.getNumeroNotaEnvio()));
            return;
        }
        
    }
    
    private NotaFiscalEntradaFornecedor populaNotaFiscalEntrada(NotaFiscalEntradaFornecedor notafiscalEntrada,
            EMS0140Input input) {
        
        notafiscalEntrada.setDataEmissao(input.getDataEmissao());
        
        notafiscalEntrada.setNumero(input.getNotaFiscal() != null ? input.getNotaFiscal().longValue() : 0L);
        
        notafiscalEntrada.setSerie(input.getSerieNotaFiscal() != null && !"0".equals(input.getSerieNotaFiscal()) ? input.getSerieNotaFiscal() : 0L);
        
        notafiscalEntrada.setDataExpedicao(input.getDataEmissao());
        
        notafiscalEntrada.setChaveAcesso(input.getChaveAcessoNF() != null && !input.getChaveAcessoNF().isEmpty()
                && !"0".equals(input.getChaveAcessoNF()) ? input.getChaveAcessoNF() : "0");
        
        notafiscalEntrada.setCfop(obterCFOP());
        
        notafiscalEntrada.setOrigem(Origem.INTERFACE);
        
        notafiscalEntrada.setStatusNotaFiscal(StatusNotaFiscalEntrada.NAO_RECEBIDA);
        
        notafiscalEntrada.setNumeroNotaEnvio(Long.parseLong(input.getNumeroNotaEnvio()));
        
        notafiscalEntrada.setValorBruto(BigDecimal.ZERO);
        
        notafiscalEntrada.setValorLiquido(BigDecimal.ZERO);
        
        notafiscalEntrada.setValorDesconto(BigDecimal.ZERO);
        
        PessoaJuridica emitente = this.obterPessoaJuridica(input.getCnpjEmissor());
        
        notafiscalEntrada.setEmitente(emitente);
        
        notafiscalEntrada.setNaturezaOperacao(
        		obterTipoNotaFiscal(GrupoNotaFiscal.NF_REMESSA_MERCADORIA_CONSIGNACAO,
                TipoUsuarioNotaFiscal.TREELOG, 
                TipoUsuarioNotaFiscal.DISTRIBUIDOR));
        
        notafiscalEntrada.setEmitida(true);
        
        return notafiscalEntrada;
    }
    
    private NaturezaOperacao obterTipoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal, TipoUsuarioNotaFiscal emitente,
            TipoUsuarioNotaFiscal destinatario) {
        
        String hql = " from NaturezaOperacao no where no.grupoNotaFiscal = :grupoNotaFiscal "
                + "and no.emitente = :emitente and no.destinatario = :destinatario "
                + " group by no.id  ";
        
        Query query = getSession().createQuery(hql);
        
        query.setParameter("grupoNotaFiscal", grupoNotaFiscal);
        query.setParameter("emitente", emitente);
        query.setParameter("destinatario", destinatario);
        
        return (NaturezaOperacao) query.uniqueResult();
    }
    
    private CFOP obterCFOP() {
        
        Criteria criteria = getSession().createCriteria(CFOP.class);
        
        criteria.add(Restrictions.eq("codigo", "5102"));
        
        return (CFOP) criteria.uniqueResult();
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

    private NotaFiscalEntradaFornecedor populaItemNotaFiscalEntrada(NotaFiscalEntradaFornecedor nfEntrada,
            EMS0140Input input, Message message) {
        
        List<EMS0140InputItem> items = input.getItems();
        
        for (EMS0140InputItem inputItem : items) {
            
            final String codigoProduto = inputItem.getCodigoProduto();
            
            final Long edicao = inputItem.getEdicao();
            
            ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto, edicao);
            
            if (produtoEdicao == null) {
                
                Produto produto = this.produtoRepository.obterProdutoPorCodigoProdinLike(codigoProduto);
                
                if (produto == null) {
                    
                    TipoProduto tipoProduto = obterTipoProduto(inputItem.getTipoProduto());
                    
                    produto = new Produto();
                    produto.setCodigo(inputItem.getCodigoProduto());
                    produto.setCodigoICD(inputItem.getCodigoProduto());
                    produto.setPeriodicidade((!Strings.isNullOrEmpty(inputItem.getPeriodicidade())) ? 
                            PeriodicidadeProduto.valueOf(inputItem.getPeriodicidade()) : PeriodicidadeProduto.MENSAL );
                    produto.setNome(inputItem.getNomeProduto());
                    produto.setOrigem(Origem.INTERFACE);
                    produto.setTipoProduto(tipoProduto);
                    produto.setPacotePadrao((inputItem.getPacotePadrao() != null) ? inputItem.getPacotePadrao() : 10);
                    produto.setPeb((inputItem.getPeb() != null) ? inputItem.getPeb() : 35);
                    produto.setPeso((inputItem.getPeso() != null) ? Math.round(inputItem.getPeso()) : 100L);
                    produto.setIsGeracaoAutomatica(true);
                    produto.setIsSemCeIntegracao(false);
                    
                    this.getSession().persist(produto);
                }
                
                produtoEdicao = new ProdutoEdicao();
                produtoEdicao.setProduto(produto);
                produtoEdicao.setNumeroEdicao(inputItem.getEdicao());
                produtoEdicao.setDesconto(BigDecimal.valueOf(inputItem.getDesconto()));
                produtoEdicao.setPacotePadrao(10);
                produtoEdicao.setPeb(35);
                produtoEdicao.setPeso(100L);
                produtoEdicao.setPossuiBrinde(false);
                produtoEdicao.setPermiteValeDesconto(false);
                produtoEdicao.setParcial(true);
                produtoEdicao.setAtivo(true);
                produtoEdicao.setOrigem(Origem.PRODUTO_SEM_CADASTRO);
                produtoEdicao.setPrecoPrevisto(inputItem.getPreco() == null ? BigDecimal.ZERO : new BigDecimal(inputItem.getPreco()));
                produtoEdicao.setPrecoVenda(tratarValorNulo(produtoEdicao.getPrecoPrevisto()));
                this.getSession().persist(produtoEdicao);
                
                Date dataAtual = new Date();
                Date dataLancamento = inputItem.getDataLancamento();
                dataLancamento = dataLancamento == null ? dataAtual : dataLancamento;
                Date dataRecolhimento = inputItem.getDataRecolhimento();
                
                int numeroLancamentoNovo = 1;
                
                Lancamento lancamento = new Lancamento();
                lancamento.setNumeroLancamento(numeroLancamentoNovo);
                lancamento.setDataCriacao(dataAtual);
                lancamento.setDataLancamentoPrevista(dataLancamento);
              
                Date dataRecolhimentoValido = recolhimentoService.obterDataRecolhimentoValido(dataRecolhimento, produtoEdicao.getProduto().getFornecedor().getId());
                
                lancamento.setDataRecolhimentoPrevista(dataRecolhimento);
                lancamento.setDataRecolhimentoDistribuidor(dataRecolhimentoValido);
                
                Fornecedor fornecedor = produtoEdicao.getProduto().getFornecedor();
                
				lancamento.setDataLancamentoDistribuidor(
			        lancamentoService.obterDataLancamentoValido(dataLancamento, (fornecedor != null) ? fornecedor.getId() : null));
    			
                lancamento.setProdutoEdicao(produtoEdicao);
                lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
                lancamento.setDataStatus(dataAtual);
                lancamento.setStatus(StatusLancamento.CONFIRMADO);
                lancamento.setReparte(new BigInteger(inputItem.getQtdExemplar().toString()));
                this.getSession().persist(lancamento);
            }
            
            ItemNotaFiscalEntrada itemNFE = new ItemNotaFiscalEntrada();
            
            itemNFE.setQtde(new BigInteger(inputItem.getQtdExemplar().toString()));
            itemNFE.setNotaFiscal(nfEntrada);
            itemNFE.setProdutoEdicao(produtoEdicao);
            itemNFE.setPreco(this.tratarValorNulo(produtoEdicao.getPrecoVenda()));
            itemNFE.setDesconto(BigDecimal.valueOf(inputItem.getDesconto()));
            
            Lancamento lancamento = obterLancamentoProdutoEdicao(produtoEdicao.getId());
            if (null == lancamento) {
                Calendar cal = Calendar.getInstance();
                
                cal.add(Calendar.DAY_OF_MONTH, 2);
                itemNFE.setDataLancamento(cal.getTime());
                
                cal.add(Calendar.DAY_OF_MONTH, produtoEdicao.getPeb());
                itemNFE.setDataRecolhimento(cal.getTime());
                
                itemNFE.setTipoLancamento(TipoLancamento.LANCAMENTO);
                
            } else {
                itemNFE.setDataLancamento(lancamento.getDataLancamentoPrevista());
                itemNFE.setDataRecolhimento(lancamento.getDataRecolhimentoPrevista());
                itemNFE.setTipoLancamento(lancamento.getTipoLancamento());
            }
            nfEntrada.getItens().add(itemNFE);
        }
        
        return nfEntrada;
    }
    
    private BigDecimal tratarValorNulo(BigDecimal valor) {
		return valor == null ? BigDecimal.ZERO : valor;
	}
    
    private TipoProduto obterTipoProduto(String nomeTipoProduto) {
        List<TipoProduto> listaTipoProduto = this.tipoProdutoRepository.busca(nomeTipoProduto, null, null, null, null, null, 0, 1);
        
        TipoProduto tipoProduto = null;
        
        if(listaTipoProduto.isEmpty()) {
            tipoProduto = this.tipoProdutoRepository.buscarPorId(1L);
        } else {
            tipoProduto = listaTipoProduto.get(0);
        }
        
        return tipoProduto; 
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
    
    private NotaFiscalEntradaFornecedor obterNotaFiscal(Long numero, Long serie, String cnpjEmissor,
            String numeroNotaEnvio) {
        StringBuilder hql = new StringBuilder();
        
        boolean existeNotaFiscal = (numero != null && !numero.equals(0L) && serie != null && !serie.equals(0L));
        
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
            
            if (serie != null && !serie.equals(0L)) {
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
    private NotaFiscalEntradaFornecedor obterNotaFiscalPorNumeroNotaEnvio(String numeroNotaEnvio, String cnpjEmissor) {
        StringBuilder hql = new StringBuilder();
        
        hql.append("select nf ");
        hql.append("from NotaFiscalEntradaFornecedor nf ");
        hql.append("join nf.emitente emitente ");
        hql.append("where nf.numeroNotaEnvio = :numeroNotaEnvio ");
        hql.append("and emitente.cnpj = :cnpj ");
        
        Query query = super.getSession().createQuery(hql.toString());
        
        query.setParameter("numeroNotaEnvio", Long.parseLong(numeroNotaEnvio));
        query.setParameter("cnpj", cnpjEmissor);
        
        return (NotaFiscalEntradaFornecedor) query.uniqueResult();
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
            
            BigDecimal valorDesconto = item.getPreco().multiply(item.getDesconto());
            
            BigDecimal valorLiquidoItem = item.getPreco().subtract(valorDesconto);
            
            BigDecimal valorTotalItem = valorLiquidoItem.multiply(BigDecimal.valueOf(item.getQtde().doubleValue()));
            
            valorLiquidoTotal = valorLiquidoTotal.add(valorTotalItem);
            
        }
        
        return valorLiquidoTotal;
    }
    
    private PessoaJuridica obterPessoaJuridica(String cnpj) {
        String hql = "from PessoaJuridica where cnpj = :cnpj";
        Query query = getSession().createQuery(hql);
        query.setParameter("cnpj", cnpj);
        return (PessoaJuridica) query.uniqueResult();
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
    
    @Override
    public void posProcess(Object tempVar) {
        
    }
    
}
