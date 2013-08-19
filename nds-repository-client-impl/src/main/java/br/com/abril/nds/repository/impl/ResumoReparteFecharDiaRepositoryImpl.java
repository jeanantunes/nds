package br.com.abril.nds.repository.impl;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
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
	public SumarizacaoReparteDTO obterSumarizacaoReparte(Date data) {
		
	    Objects.requireNonNull(data, "Data para contagem dos lançamentos expedidos não deve ser nula!");
        
        Date dataInicio = DateUtil.removerTimestamp(data);
        Date dataFim = DateUtil.adicionarDias(dataInicio, 1);
        
        String templateHqlProdutoEdicaoExpedido = new StringBuilder("(select distinct(produtoEdicaoExpedido.id) from Expedicao expedicao ")
           .append("join expedicao.lancamentos lancamento join lancamento.produtoEdicao produtoEdicaoExpedido where " )
           .append("expedicao.dataExpedicao >= :dataInicio and expedicao.dataExpedicao < :dataFim and lancamento.status <> :statusFuro)").toString();

        String templateHqlDiferenca =  new StringBuilder("(select sum(case when diferenca.tipoDiferenca = :%s then (diferenca.qtde * diferenca.produtoEdicao.pacotePadrao * diferenca.produtoEdicao.precoVenda) ")
        	.append("else (diferenca.qtde * diferenca.produtoEdicao.precoVenda) end) from Diferenca diferenca join diferenca.lancamentoDiferenca lancamentoDiferenca ")
	        .append("left join lancamentoDiferenca.movimentosEstoqueCota movimentosEstoqueCota left join lancamentoDiferenca.movimentoEstoque movimentoEstoque ")
	        .append("where (movimentosEstoqueCota.status = :movimentoAprovado OR movimentoEstoque.status = :movimentoAprovado) ")
	        .append("and diferenca.dataMovimento = :data and diferenca.tipoDiferenca in (:%s, :%s) ")
	        .append("and diferenca.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido).append(") as %s ").toString();

        StringBuilder hql = new StringBuilder(" select sum(me.qtde * produtoEdicao.precoVenda) as totalReparte, ");
        
	    hql.append(String.format(templateHqlDiferenca, "tipoDiferencaSobraDe", "tipoDiferencaSobraDe", "tipoDiferencaSobraEm", "totalSobras")).append(",")
	       .append(String.format(templateHqlDiferenca, "tipoDiferencaFaltaDe", "tipoDiferencaFaltaDe", "tipoDiferencaFaltaEm", "totalFaltas")).append(",");
	    
	    hql.append("(select sum(case when movimentoEstoque.tipoMovimento.grupoMovimentoEstoque = :grupoTransferenciaLancamentoEntrada ")
           .append("then (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda) else (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda * -1) end) ")
           .append("from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data and movimentoEstoque.status = :statusAprovado ")
           .append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:grupoTransferenciaLancamentoEntrada, :grupoTransferenciaLancamentoSaida) ")
           .append("and movimentoEstoque.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido).append(") as totalTransferencias, ");
	    
        hql.append("(select sum(case when movimentoEstoque.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEnvioJornaleiro ")
           .append("then (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda) else (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda * -1) end) ")
           .append("from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data and movimentoEstoque.status = :statusAprovado ")
           .append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:grupoMovimentoEnvioJornaleiro, :grupoMovimentoEstornoEnvioJornaleiro) ")
           .append("and movimentoEstoque.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido).append(") as totalDistribuido ");
        
        hql.append(" from MovimentoEstoque me ")
           .append(" join me.produtoEdicao produtoEdicao ") 
           .append(" where me.data = :data ")
           .append(" and me.status = :statusAprovado ")
           .append(" and me.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEnvioJornaleiro ")
           .append(" and produtoEdicao in ").append(templateHqlProdutoEdicaoExpedido);
 
        Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("movimentoAprovado", StatusAprovacao.APROVADO);
        query.setParameter("data", data);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        query.setParameter("statusFuro", StatusLancamento.FURO);
        query.setParameter("tipoDiferencaSobraDe", TipoDiferenca.SOBRA_DE);
        query.setParameter("tipoDiferencaSobraEm", TipoDiferenca.SOBRA_EM);
        query.setParameter("tipoDiferencaFaltaDe", TipoDiferenca.FALTA_DE);
        query.setParameter("tipoDiferencaFaltaEm", TipoDiferenca.FALTA_EM);
        query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
        query.setParameter("grupoTransferenciaLancamentoEntrada", GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
        query.setParameter("grupoTransferenciaLancamentoSaida", GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO);
        query.setParameter("grupoMovimentoEnvioJornaleiro", GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
        query.setParameter("grupoMovimentoEstornoEnvioJornaleiro", GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO);
        
        try {
        	
            Constructor<SumarizacaoReparteDTO> constructor = SumarizacaoReparteDTO.class.getConstructor(BigDecimal.class, BigDecimal.class, BigDecimal.class, BigDecimal.class, BigDecimal.class);
            
            query.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
        
        } catch (NoSuchMethodException | SecurityException e) {
            
        	String msg = "Erro definindo result transformer para classe: " + SumarizacaoReparteDTO.class.getName();
            
        	LOG.error(msg, e);
            
            throw new RuntimeException(msg, e);
        } 
        
	    return (SumarizacaoReparteDTO) query.uniqueResult();
	}
		
    @Override
	public List<ReparteFecharDiaDTO> obterResumoReparte(Date data){
	   return obterLancamentosExpedidos(data, null);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReparteFecharDiaDTO> obterResumoReparte(Date data, PaginacaoVO paginacao) {
        return obterLancamentosExpedidos(data, paginacao);
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
    private List<ReparteFecharDiaDTO> obterLancamentosExpedidos(Date data, PaginacaoVO paginacao) {
        Objects.requireNonNull(data, "Data para consulta ao resumo do reparte não deve ser nula!");

        Date dataInicio = DateUtil.removerTimestamp(data);
        Date dataFim = DateUtil.adicionarDias(dataInicio, 1);
        
        String exemplaresDiferencaDe = "diferenca.qtde * produtoEdicao.pacotePadrao";
        String exemplaresDiferencaEm = "diferenca.qtde";
        
        String templateHqlProdutoEdicaoExpedido = new StringBuilder("(select distinct(produtoEdicaoExpedido.id) from Expedicao expedicao ")
           .append("join expedicao.lancamentos lancamento join lancamento.produtoEdicao produtoEdicaoExpedido where " )
           .append("expedicao.dataExpedicao >= :dataInicio and expedicao.dataExpedicao < :dataFim and lancamento.status <> :statusFuro)").toString();
        
        
        String templateHqlDiferenca = new StringBuilder("(select sum(%s) from Diferenca diferenca join diferenca.lancamentoDiferenca lancamentoDiferenca left join lancamentoDiferenca.movimentosEstoqueCota movimentosEstoqueCota left join lancamentoDiferenca.movimentoEstoque movimentoEstoque ") 
        	.append("where (movimentosEstoqueCota.status = :movimentoAprovado OR movimentoEstoque.status = :movimentoAprovado) and diferenca.dataMovimento = :data and diferenca.produtoEdicao.id = produtoEdicao.id and diferenca.tipoDiferenca = :%s) as %s ").toString();
        
        StringBuilder hql = new StringBuilder("select produtoEdicao.id as idProdutoEdicao, produto.codigo as codigo, ");
				      hql.append("produto.nome as nomeProduto, ");
				      hql.append("produtoEdicao.numeroEdicao as numeroEdicao, ");
				      hql.append("produtoEdicao.precoVenda as precoVenda, ");
				      hql.append("me.qtde as qtdeReparte, ");
        
        //Diferenças, convertendo as qtde sempre para exemplares
        hql.append(String.format(templateHqlDiferenca, exemplaresDiferencaDe, "tipoDiferencaSobraDe", "qtdeSobraDe")).append(",");
        hql.append(String.format(templateHqlDiferenca, exemplaresDiferencaEm, "tipoDiferencaSobraEm", "qtdeSobraEm")).append(",");
        hql.append(String.format(templateHqlDiferenca, exemplaresDiferencaDe, "tipoDiferencaFaltaDe", "qtdeFaltaDe")).append(",");
        hql.append(String.format(templateHqlDiferenca, exemplaresDiferencaEm, "tipoDiferencaFaltaEm", "qtdeFaltaEm")).append(",");
        
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
        
        query.setParameter("movimentoAprovado", StatusAprovacao.APROVADO);
        query.setParameter("data", dataInicio);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        query.setParameter("tipoDiferencaSobraDe", TipoDiferenca.SOBRA_DE);
        query.setParameter("tipoDiferencaSobraEm", TipoDiferenca.SOBRA_EM);
        query.setParameter("tipoDiferencaFaltaDe", TipoDiferenca.FALTA_DE);
        query.setParameter("tipoDiferencaFaltaEm", TipoDiferenca.FALTA_EM);
        query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
        query.setParameter("grupoMovimentoEnvioJornaleiro", GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
        query.setParameter("grupoMovimentoEstornoEnvioJornaleiro", GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO);
        query.setParameter("grupoTransferenciaLancamentoEntrada", GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
        query.setParameter("grupoTransferenciaLancamentoSaida", GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO);
        query.setParameter("statusFuro", StatusLancamento.FURO);
        
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
