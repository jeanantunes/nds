package br.com.abril.nds.repository.impl;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoReparteDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ResumoReparteFecharDiaRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;


@Repository
public class ResumoReparteFecharDiaRepositoryImpl  extends AbstractRepository implements ResumoReparteFecharDiaRepository {
    
    private static final Logger LOG = LoggerFactory.getLogger(ResumoReparteFecharDiaRepositoryImpl.class);
	

	@Override
	public SumarizacaoReparteDTO obterSumarizacaoReparte(Date data,Date dataReparteHistoico) {
		
	    Objects.requireNonNull(data, "Data para contagem dos lançamentos expedidos não deve ser nula!");
        
        Date dataInicio = DateUtil.removerTimestamp(data);
        Date dataFim = DateUtil.adicionarDias(dataInicio, 1);
       
        String templateHqlProdutoEdicaoExpedido = new StringBuilder("(select distinct(produtoEdicaoExpedido.id) from Expedicao expedicao ")
           .append("join expedicao.lancamentos lancamento join lancamento.produtoEdicao produtoEdicaoExpedido where " )
           .append("expedicao.dataExpedicao >= :dataInicio and expedicao.dataExpedicao < :dataFim and lancamento.status <> :statusFuro)").toString();
        
        String templateHqlDiferenca =  new StringBuilder("(select sum(diferenca.qtde * diferenca.produtoEdicao.precoVenda) ")
	    	.append(" from Diferenca diferenca join diferenca.lancamentoDiferenca lancamentoDiferenca ")
	        .append(" where diferenca.dataMovimento = :data and diferenca.tipoDiferenca in (:%s) ")
	        .append(" and diferenca.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido).append(") as %s ").toString();
        
        String templateHqlDiferencaRateioCota =  new StringBuilder("(select sum(diferenca.qtde * diferenca.produtoEdicao.precoVenda) ")
	    	.append(" from Diferenca diferenca join diferenca.lancamentoDiferenca lancamentoDiferenca ")
	        .append(" where  diferenca.dataMovimento = :data and diferenca.tipoDiferenca in (:%s) ")
	        .append(" and diferenca.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido)
	        .append(" and diferenca.id in ( select distinct rateio.diferenca.id from RateioDiferenca rateio where rateio.dataMovimento =:data ) ").append(")").toString();
        
        String templateHqlHistoricoEstoque = new StringBuilder(" COALESCE( (select sum(hstEstoque.qtde * hstEstoque.produtoEdicao.precoVenda) ")
        	.append(" from HistoricoEstoqueProduto hstEstoque  ") 
        	.append(" where hstEstoque.data =:dataConsultaHistorico ")
        	.append(" and hstEstoque.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido).append(" ),0) ").toString();
        
        StringBuilder hql = new StringBuilder(" select sum(me.qtde * produtoEdicao.precoVenda) + ").append(templateHqlHistoricoEstoque).append(" as totalReparte, ");
        
	    hql.append(String.format(templateHqlDiferenca,  "tipoDiferencaSobras", "totalSobras")).append(",")
	       .append(String.format(templateHqlDiferenca,  "tipoDiferencaFaltas", "totalFaltas")).append(",");
	    
	    hql.append("(select sum(case when movimentoEstoque.tipoMovimento.grupoMovimentoEstoque = :grupoTransferenciaLancamentoEntrada ")
           .append("then (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda) else (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda * -1) end) ")
           .append("from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data and movimentoEstoque.status = :statusAprovado ")
           .append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:grupoTransferenciaLancamentoEntrada, :grupoTransferenciaLancamentoSaida) ")
           .append("and movimentoEstoque.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido).append(") as totalTransferencias, ");
	    
        hql.append("(select sum(case when movimentoEstoque.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEnvioJornaleiro ")
           .append("then (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda) else (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda * -1) end) ")
           .append("from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data and movimentoEstoque.status = :statusAprovado ")
           .append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:grupoMovimentoEnvioJornaleiro, :grupoMovimentoEstornoEnvioJornaleiro) ")
           .append("and movimentoEstoque.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido).append(") - ")
           .append(String.format(templateHqlDiferencaRateioCota,  "tipoDiferencaFaltas")).append(" as totalDistribuido ");
        
        hql.append(" from MovimentoEstoque me ")
           .append(" join me.produtoEdicao produtoEdicao ") 
           .append(" where me.data = :data ")
           .append(" and me.status = :statusAprovado ")
           .append(" and me.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEnvioJornaleiro ")
           .append(" and produtoEdicao in ").append(templateHqlProdutoEdicaoExpedido);
 
        Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("dataConsultaHistorico", dataReparteHistoico);
        query.setParameter("data", data);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        query.setParameter("statusFuro", StatusLancamento.FURO);
        query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
        query.setParameter("grupoTransferenciaLancamentoEntrada", GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
        query.setParameter("grupoTransferenciaLancamentoSaida", GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO);
        query.setParameter("grupoMovimentoEnvioJornaleiro", GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
        query.setParameter("grupoMovimentoEstornoEnvioJornaleiro", GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO);

        query.setParameterList("tipoDiferencaSobras", Arrays.asList(TipoDiferenca.SOBRA_DE,
        															TipoDiferenca.SOBRA_DE_DIRECIONADA_COTA, 
        															TipoDiferenca.GANHO_DE,
        															TipoDiferenca.SOBRA_EM,
        															TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA, 
        															TipoDiferenca.GANHO_EM));
        
        query.setParameterList("tipoDiferencaFaltas", Arrays.asList(TipoDiferenca.FALTA_DE,
        															TipoDiferenca.PERDA_DE,
        															TipoDiferenca.FALTA_EM,
        															TipoDiferenca.PERDA_EM));
        
        try {
        	
            Constructor<SumarizacaoReparteDTO> constructor = 
            		SumarizacaoReparteDTO.class.getConstructor(BigDecimal.class, BigDecimal.class, BigDecimal.class, BigDecimal.class,BigDecimal.class);
            
            query.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
        
        } catch (NoSuchMethodException | SecurityException e) {
            
        	String msg = "Erro definindo result transformer para classe: " + SumarizacaoReparteDTO.class.getName();
            
        	LOG.error(msg, e);
            
            throw new RuntimeException(msg, e);
        } 
        
	    return (SumarizacaoReparteDTO) query.uniqueResult();
	}
		
    @Override
	public List<ReparteFecharDiaDTO> obterResumoReparte(Date data, Date dataReparteHistoico){
	   return obterLancamentosExpedidos(data, null,dataReparteHistoico);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReparteFecharDiaDTO> obterResumoReparte(Date data, PaginacaoVO paginacao,Date dataReparteHistoico) {
        return obterLancamentosExpedidos(data, paginacao,dataReparteHistoico);
    }

    @Override
    public Long contarLancamentosExpedidos(Date data) {
        Objects.requireNonNull(data, "Data para contagem dos lançamentos expedidos não deve ser nula!");
        
        Date dataInicio = DateUtil.removerTimestamp(data);
        Date dataFim = DateUtil.adicionarDias(dataInicio, 1);
        
        String templateHqlProdutoEdicaoExpedido = new StringBuilder("(select distinct(produtoEdicaoExpedido.id) from Expedicao expedicao ")
           .append("join expedicao.lancamentos lancamento join lancamento.produtoEdicao produtoEdicaoExpedido where " )
           .append("expedicao.dataExpedicao >= :dataInicio and expedicao.dataExpedicao < :dataFim and lancamento.status <> :statusFuro)").toString();
        
        StringBuilder hql = new StringBuilder("select count(me) ");
        
        hql.append(" from MovimentoEstoque me ")
	       .append(" join me.produtoEdicao produtoEdicao ") 
	       .append(" join produtoEdicao.produto produto ") 
	       .append(" where me.data = :data ")
	       .append(" and me.status = :statusAprovado ")
	       .append(" and me.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEnvioJornaleiro ")
	       .append(" and produtoEdicao in ").append(templateHqlProdutoEdicaoExpedido);
 
        Query query = getSession().createQuery(hql.toString());
        query.setParameter("data", dataInicio);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
        query.setParameter("statusFuro", StatusLancamento.FURO);
        query.setParameter("grupoMovimentoEnvioJornaleiro", GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
        
        return (Long) query.uniqueResult();
    }

    
    @SuppressWarnings("unchecked")
    private List<ReparteFecharDiaDTO> obterLancamentosExpedidos(Date data, PaginacaoVO paginacao,Date dataReparteHistoico) {
        Objects.requireNonNull(data, "Data para consulta ao resumo do reparte não deve ser nula!");

        Date dataInicio = DateUtil.removerTimestamp(data);
        Date dataFim = DateUtil.adicionarDias(dataInicio, 1);
        
        String templateHqlProdutoEdicaoExpedido = new StringBuilder("(select distinct(produtoEdicaoExpedido.id) from Expedicao expedicao ")
           .append("join expedicao.lancamentos lancamento join lancamento.produtoEdicao produtoEdicaoExpedido where " )
           .append("expedicao.dataExpedicao >= :dataInicio and expedicao.dataExpedicao < :dataFim and lancamento.status <> :statusFuro )").toString();
        
        
        String templateHqlDiferenca = new StringBuilder("(select sum(diferenca.qtde) from Diferenca diferenca join diferenca.lancamentoDiferenca lancamentoDiferenca  ") 
        	.append("where diferenca.dataMovimento = :data and diferenca.produtoEdicao.id = produtoEdicao.id and diferenca.tipoDiferenca in (:%s) ) as %s ").toString();
        
        String templateHqlHistoricoEstoque = new StringBuilder(" COALESCE( (select hstEstoque.qtde ")
	    	.append(" from HistoricoEstoqueProduto hstEstoque  ") 
	    	.append(" where hstEstoque.data =:dataConsultaHistorico ")
	    	.append(" and hstEstoque.produtoEdicao.id = produtoEdicao.id ")
	    	.append(" and hstEstoque.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido).append(" ),0) ").toString();
        
        StringBuilder hql = new StringBuilder("select produtoEdicao.id as idProdutoEdicao, produto.codigo as codigo, ");
				      hql.append("produto.nome as nomeProduto, ");
				      hql.append("produtoEdicao.numeroEdicao as numeroEdicao, ");
				      hql.append("produtoEdicao.precoVenda as precoVenda, ");
				      hql.append("me.qtde + ").append(templateHqlHistoricoEstoque).append(" as qtdeReparte, ");
        
        //Diferenças, convertendo as qtde sempre para exemplares
        hql.append(String.format(templateHqlDiferenca,  "tipoDiferencaSobraDe", "qtdeSobraDe")).append(",");
        hql.append(String.format(templateHqlDiferenca,  "tipoDiferencaSobraEm", "qtdeSobraEm")).append(",");
        hql.append(String.format(templateHqlDiferenca,  "tipoDiferencaFaltaDe", "qtdeFaltaDe")).append(",");
        hql.append(String.format(templateHqlDiferenca,  "tipoDiferencaFaltaEm", "qtdeFaltaEm")).append(",");
        
        //Quantidade efetiva distribuída, considerando movimento de envio de reparte, como também o estorno, em caso de furo
        hql.append("(select sum(case when movimentoEstoque.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEnvioJornaleiro ");
        hql.append("then movimentoEstoque.qtde else (movimentoEstoque.qtde * -1) end) from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data "); 
        hql.append("and movimentoEstoque.produtoEdicao.id = produtoEdicao.id and movimentoEstoque.status = :statusAprovado ");
        hql.append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:grupoMovimentoEnvioJornaleiro, :grupoMovimentoEstornoEnvioJornaleiro)) as qtdeDistribuido, ");
        
        //Transferências de estoque de lançamento, entrada e saída
        hql.append("(select sum(case when movimentoEstoque.tipoMovimento.grupoMovimentoEstoque = :grupoTransferenciaLancamentoEntrada ");
        hql.append("then movimentoEstoque.qtde else (movimentoEstoque.qtde * -1) end) from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data "); 
        hql.append("and movimentoEstoque.produtoEdicao.id = produtoEdicao.id and movimentoEstoque.status = :statusAprovado ");
        hql.append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:grupoTransferenciaLancamentoEntrada, :grupoTransferenciaLancamentoSaida)) as qtdeTransferencia ");

        hql.append(" from MovimentoEstoque me ")
	       .append(" join me.produtoEdicao produtoEdicao ") 
	       .append(" join produtoEdicao.produto produto ") 
	       .append(" where me.data = :data ")
	       .append(" and me.status = :statusAprovado ")
	       .append(" and me.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEnvioJornaleiro ")
	       .append(" and produtoEdicao in ").append(templateHqlProdutoEdicaoExpedido);
        
        hql.append("order by produto.codigo asc");
    
        Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("dataConsultaHistorico", dataReparteHistoico);
        query.setParameter("data", dataInicio);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
        query.setParameter("grupoMovimentoEnvioJornaleiro", GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
        query.setParameter("grupoMovimentoEstornoEnvioJornaleiro", GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO);
        query.setParameter("grupoTransferenciaLancamentoEntrada", GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
        query.setParameter("grupoTransferenciaLancamentoSaida", GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO);
        query.setParameter("statusFuro", StatusLancamento.FURO);
        query.setParameterList("tipoDiferencaSobraDe", Arrays.asList(TipoDiferenca.SOBRA_DE,TipoDiferenca.SOBRA_DE_DIRECIONADA_COTA, TipoDiferenca.GANHO_DE));
        query.setParameterList("tipoDiferencaSobraEm", Arrays.asList(TipoDiferenca.SOBRA_EM,TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA, TipoDiferenca.GANHO_EM));
        query.setParameterList("tipoDiferencaFaltaDe", Arrays.asList(TipoDiferenca.FALTA_DE,TipoDiferenca.PERDA_DE));
        query.setParameterList("tipoDiferencaFaltaEm", Arrays.asList(TipoDiferenca.FALTA_EM,TipoDiferenca.PERDA_EM));
        
        if (paginacao != null) {
            query.setFirstResult(paginacao.getPosicaoInicial());
            query.setMaxResults(paginacao.getQtdResultadosPorPagina());
        }
        
        try {
            Constructor<ReparteFecharDiaDTO> constructor = ReparteFecharDiaDTO.class
                    .getConstructor(Long.class,String.class, String.class, Long.class,
                            BigDecimal.class, BigInteger.class,
                            BigInteger.class, BigInteger.class,
                            BigInteger.class, BigInteger.class,
                            BigInteger.class, BigInteger.class);
            query.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
        } catch (NoSuchMethodException | SecurityException e) {
            String msg = "Erro definindo result transformer para classe: " + ReparteFecharDiaDTO.class.getName();
            LOG.error(msg, e);
            throw new RuntimeException(msg, e);
        } 
        return query.list();
    }


}
