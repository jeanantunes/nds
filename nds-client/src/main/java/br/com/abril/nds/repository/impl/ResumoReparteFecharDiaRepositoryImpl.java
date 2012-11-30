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

        String templateHqlDiferenca =  new StringBuilder("(select sum(case when diferenca.tipoDiferenca = :%s then (diferenca.qtde * diferenca.produtoEdicao.pacotePadrao * diferenca.produtoEdicao.precoVenda) ")
        .append("else (diferenca.qtde * diferenca.produtoEdicao.precoVenda) end) from Diferenca diferenca where diferenca.dataMovimento = :data and diferenca.tipoDiferenca in (:%s, :%s)) as %s ").toString();
       
	    
	    StringBuilder hql = new StringBuilder("select sum(lancamento.reparte * produtoEdicao.precoVenda) as totalReparte, ");
	    hql.append(String.format(templateHqlDiferenca, "tipoDiferencaSobraDe", "tipoDiferencaSobraDe", "tipoDiferencaSobraEm", "totalSobras")).append(",");
	    hql.append(String.format(templateHqlDiferenca, "tipoDiferencaFaltaDe", "tipoDiferencaFaltaDe", "tipoDiferencaFaltaEm", "totalFaltas")).append(",");
	    
	    hql.append("(select sum(case when movimentoEstoque.tipoMovimento.grupoMovimentoEstoque = :grupoTransferenciaLancamentoEntrada ");
        hql.append("then (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda) else (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda * -1) end) ");
        hql.append("from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data and movimentoEstoque.status = :statusAprovado "); 
        hql.append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:grupoTransferenciaLancamentoEntrada, :grupoTransferenciaLancamentoSaida)) as totalTransferencias, ");
	    
        hql.append("(select sum(case when movimentoEstoque.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEnvioJornaleiro ");
        hql.append("then (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda) else (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda * -1) end) ");
        hql.append("from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data and movimentoEstoque.status = :statusAprovado "); 
        hql.append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:grupoMovimentoEnvioJornaleiro, :grupoMovimentoEstornoEnvioJornaleiro)) as totalDistribuido ");
        
        hql.append("from Expedicao expedicao ");
        hql.append("join expedicao.lancamentos lancamento ");
        hql.append("join lancamento.produtoEdicao produtoEdicao ");
        hql.append("where expedicao.dataExpedicao >= :dataInicio and expedicao.dataExpedicao < :dataFim ");
        //É permitido o furo após a expedição, mas pela implementação atual não está desvinculando o lançamento furado da expedição
        //então para calculo do reparte edxpedido do dia são excluídos os lançamentos com furo após expedição na query
        hql.append("and lancamento.status <> :statusFuro ");
 
        Query query = getSession().createQuery(hql.toString());
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
            Constructor<SumarizacaoReparteDTO> constructor = SumarizacaoReparteDTO.class
                    .getConstructor(BigDecimal.class, BigDecimal.class, BigDecimal.class,
                            BigDecimal.class, BigDecimal.class);
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
        
        StringBuilder hql = new StringBuilder("select count(lancamento) ");
        hql.append("from Expedicao expedicao ");
        hql.append("join expedicao.lancamentos lancamento ");
        hql.append("where expedicao.dataExpedicao >= :dataInicio and expedicao.dataExpedicao < :dataFim ");
        //É permitido o furo após a expedição, mas pela implementação atual não está desvinculando o lançamento furado da expedição
        //então para calculo do reparte edxpedido do dia são excluídos os lançamentos com furo após expedição na query
        hql.append("and lancamento.status <> :statusFuro ");
 
        Query query = getSession().createQuery(hql.toString());
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        query.setParameter("statusFuro", StatusLancamento.FURO);
        
        return (Long) query.uniqueResult();
    }

    
    @SuppressWarnings("unchecked")
    private List<ReparteFecharDiaDTO> obterLancamentosExpedidos(Date data, PaginacaoVO paginacao) {
        Objects.requireNonNull(data, "Data para consulta ao resumo do reparte não deve ser nula!");

        Date dataInicio = DateUtil.removerTimestamp(data);
        Date dataFim = DateUtil.adicionarDias(dataInicio, 1);
        
        String exemplaresDiferencaDe = "qtde * produtoEdicao.pacotePadrao";
        String exemplaresDiferencaEm = "qtde";
        
        String templateHqlDiferenca = new StringBuilder("(select sum(%s) from Diferenca diferenca ") 
        .append("where diferenca.dataMovimento = :data and diferenca.produtoEdicao.id = produtoEdicao.id and diferenca.tipoDiferenca = :%s) as %s ").toString();
        
        StringBuilder hql = new StringBuilder("select produto.codigo as codigo, ");
        hql.append("produto.nome as nomeProduto, ");
        hql.append("produtoEdicao.numeroEdicao as numeroEdicao, ");
        hql.append("produtoEdicao.precoVenda as precoVenda, ");
        hql.append("lancamento.reparte as qtdeReparte, ");
        
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
     
        hql.append("from Expedicao expedicao ");
        hql.append("join expedicao.lancamentos lancamento ");
        hql.append("join lancamento.produtoEdicao produtoEdicao ");
        hql.append("join produtoEdicao.produto produto ");
        hql.append("where expedicao.dataExpedicao >= :dataInicio and expedicao.dataExpedicao < :dataFim ");
        //É permitido o furo após a expedição, mas pela implementação atual não está desvinculando o lançamento furado da expedição
        //então para calculo do reparte edxpedido do dia são excluídos os lançamentos com furo após expedição na query
        hql.append("and lancamento.status <> :statusFuro ");
        hql.append("order by codigo asc");
    
        Query query = getSession().createQuery(hql.toString());
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
                    .getConstructor(String.class, String.class, Long.class,
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
