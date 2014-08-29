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
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ResumoReparteFecharDiaRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;


@Repository
public class ResumoReparteFecharDiaRepositoryImpl  extends AbstractRepository implements ResumoReparteFecharDiaRepository {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ResumoReparteFecharDiaRepositoryImpl.class);
	
	@Override
	public SumarizacaoReparteDTO obterSumarizacaoReparte(Date data) {
		
        Objects.requireNonNull(data, "Data para contagem dos lançamentos expedidos não deve ser nula!");
        
        BigDecimal valorReparte = this.obterValorSumarizadoDoReparte(data);
        
        BigDecimal valorSobras = this.obterValorSumarizadoDeSobras(data);
        
        BigDecimal valorFaltas =  this.obterValorSumarizadoDeFaltas(data);
        
        BigDecimal valorTransferencia =  this.obterValorSumarizadoDeTransferencia(data);
        
        BigDecimal valorDistribuido = this.obterValorSumarizadoReparteDistribuido(data).add(this.obterValorSumarizadoRateioCota(data));
        
        BigDecimal valorPromocional =  this.obterValorSumarizadoRepartePromocional(data);
        
        SumarizacaoReparteDTO retorno = 
        		new SumarizacaoReparteDTO(valorReparte,valorSobras,valorFaltas,valorTransferencia,valorDistribuido,valorPromocional);
        
	    return retorno;
	}
	
	@Override
	public List<ReparteFecharDiaDTO> obterResumoReparte(Date data, Date dataReparteHistoico){
	   return obterLancamentosExpedidos(data, null);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReparteFecharDiaDTO> obterResumoReparte(Date data, PaginacaoVO paginacao,Date dataReparteHistoico) {
        return obterLancamentosExpedidos(data, paginacao);
    }

    @Override
    public Long contarLancamentosExpedidos(Date data) {
        Objects.requireNonNull(data, "Data para contagem dos lançamentos expedidos não deve ser nula!");
        
        Date dataInicio = DateUtil.removerTimestamp(data);
     
        StringBuilder hql = new StringBuilder("select count(distinct lancamento.id) ");
        
        hql.append(" from Expedicao expedicao ")
        .append(" join expedicao.lancamentos lancamento " )
        .append(" join lancamento.produtoEdicao produtoEdicao ")
        .append(" join produtoEdicao.produto produto ")
        .append(" where  ")
        .append(" lancamento.status <> :statusFuro ")
        .append(" and lancamento.dataLancamentoDistribuidor =:data ) ");
 
        Query query = getSession().createQuery(hql.toString());
        query.setParameter("data", dataInicio);
        query.setParameter("statusFuro", StatusLancamento.FURO);
        
        return (Long) query.uniqueResult();
    }

    
    @SuppressWarnings("unchecked")
    private List<ReparteFecharDiaDTO> obterLancamentosExpedidos(Date data, PaginacaoVO paginacao) {
        Objects.requireNonNull(data, "Data para consulta ao resumo do reparte não deve ser nula!");

        Date dataInicio = DateUtil.removerTimestamp(data);
        
        String templateHqlDiferenca = new StringBuilder("(select sum(diferenca.qtde) from Diferenca diferenca join diferenca.lancamentoDiferenca lancamentoDiferenca  ") 
        	.append("where diferenca.dataMovimento = :data and diferenca.produtoEdicao.id = produtoEdicao.id and diferenca.tipoDirecionamento =:tipoDirecionamentoDiferenca and diferenca.tipoDiferenca in (:%s) ) as %s ").toString();
        
        String templateHqlRecebimentoEstoqueFisico = new StringBuilder()
           .append(" (select COALESCE(sum(me.qtde),0) from MovimentoEstoque me join me.produtoEdicao produtoEdicaoME ")
	       .append(" where me.data <= :data ")
	       .append(" and me.status = :statusAprovado ")
	       .append(" and me.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoRecebimentoFisico ")
	       .append(" and produtoEdicaoME.id = produtoEdicao.id )").toString();
        
        String templateHqlRecebimentoEstoqueFisicoPromocional = new StringBuilder()
	        .append(" (select COALESCE(sum(me.qtde),0) from MovimentoEstoque me join me.produtoEdicao produtoEdicaoME ")
	        .append(" where me.data <= :data ")
	        .append(" and me.tipoMovimento.grupoMovimentoEstoque IN( :grupoMovimentoRecebimentoFisicoPromocional) ")
	        .append(" and produtoEdicaoME.id = produtoEdicao.id )").toString();
        
        StringBuilder hql = new StringBuilder("select distinct produtoEdicao.id as idProdutoEdicao, produto.codigo as codigo, ");
				      hql.append("produto.nome as nomeProduto, ");
				      hql.append("produtoEdicao.numeroEdicao as numeroEdicao, ");
				      hql.append("produtoEdicao.precoVenda as precoVenda, ");
				      hql.append(templateHqlRecebimentoEstoqueFisico).append(" as qtdeReparte, ");
        
        // Diferenças, convertendo as qtde sempre para exemplares
        hql.append(String.format(templateHqlDiferenca,  "tipoDiferencaSobraDe", "qtdeSobraDe")).append(",");
        hql.append(String.format(templateHqlDiferenca,  "tipoDiferencaSobraEm", "qtdeSobraEm")).append(",");
        hql.append(String.format(templateHqlDiferenca,  "tipoDiferencaFaltaDe", "qtdeFaltaDe")).append(",");
        hql.append(String.format(templateHqlDiferenca,  "tipoDiferencaFaltaEm", "qtdeFaltaEm")).append(",");
        
        // Quantidade efetiva distribuída, considerando movimento de envio de
        // reparte, como também o estorno, em caso de furo
        hql.append("(select sum(case when movimentoEstoque.tipoMovimento.grupoMovimentoEstoque IN( :grupoMovimentoEnvioJornaleiro )");
        hql.append("then movimentoEstoque.qtde else (movimentoEstoque.qtde * -1) end) from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data "); 
        hql.append("and movimentoEstoque.produtoEdicao.id = produtoEdicao.id and movimentoEstoque.status = :statusAprovado ");
        hql.append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:grupoMovimentoEnvioJornaleiro, :grupoMovimentoEstornoEnvioJornaleiro)) as qtdeDistribuido, ");
        
        // Transferências de estoque de lançamento, entrada e saída
        hql.append("(select sum(case when movimentoEstoque.tipoMovimento.grupoMovimentoEstoque = :grupoTransferenciaLancamentoEntrada ");
        hql.append("then movimentoEstoque.qtde else (movimentoEstoque.qtde * -1) end) from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data "); 
        hql.append("and movimentoEstoque.produtoEdicao.id = produtoEdicao.id and movimentoEstoque.status = :statusAprovado ");
        hql.append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:grupoTransferenciaLancamentoEntrada, :grupoTransferenciaLancamentoSaida)) as qtdeTransferencia, ");
        
        hql.append( templateHqlRecebimentoEstoqueFisicoPromocional).append(" as qtdeDiferenca ");
        
    	 hql.append(" from Expedicao expedicao ")
	         .append(" join expedicao.lancamentos lancamento " )
	         .append(" join lancamento.produtoEdicao produtoEdicao ")
	         .append(" join produtoEdicao.produto produto ")
	         .append(" left join produtoEdicao.historicoEstoqueProduto hstEstoque ")
	         .append(" where  ")
	         .append(" lancamento.status <> :statusFuro ")
	         .append(" and lancamento.dataLancamentoDistribuidor =:data ) ");
	    	
        hql.append("order by produto.codigo asc");
    
        Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("data", dataInicio);
        query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
        query.setParameter("grupoMovimentoRecebimentoFisico", GrupoMovimentoEstoque.RECEBIMENTO_FISICO);
        query.setParameterList("grupoMovimentoRecebimentoFisicoPromocional",Arrays.asList(
        		GrupoMovimentoEstoque.ESTORNO_REPARTE_PROMOCIONAL,
        		GrupoMovimentoEstoque.GRUPO_MATERIAL_PROMOCIONAL));
        query.setParameter("grupoTransferenciaLancamentoEntrada", GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
        query.setParameter("grupoTransferenciaLancamentoSaida", GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO);
        query.setParameter("statusFuro", StatusLancamento.FURO);
        query.setParameterList("tipoDiferencaSobraDe", Arrays.asList(TipoDiferenca.SOBRA_DE,TipoDiferenca.GANHO_DE));
        query.setParameterList("tipoDiferencaSobraEm", Arrays.asList(TipoDiferenca.SOBRA_EM,TipoDiferenca.GANHO_EM));
        query.setParameterList("tipoDiferencaFaltaDe", Arrays.asList(TipoDiferenca.FALTA_DE,TipoDiferenca.PERDA_DE));
        query.setParameterList("tipoDiferencaFaltaEm", Arrays.asList(TipoDiferenca.FALTA_EM,TipoDiferenca.PERDA_EM));
        query.setParameter("tipoDirecionamentoDiferenca", TipoDirecionamentoDiferenca.ESTOQUE);
        this.atribuirGruposMovimentoEnvioJornaleiro(query, "grupoMovimentoEnvioJornaleiro");
        this.atribuirGruposMovimentoEstornoEnvioJornaleiro(query, "grupoMovimentoEstornoEnvioJornaleiro");
        
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
                            BigInteger.class, BigInteger.class,BigInteger.class);
            query.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
        } catch (NoSuchMethodException | SecurityException e) {
            String msg = "Erro definindo result transformer para classe: " + ReparteFecharDiaDTO.class.getName();
            LOGGER.error(msg, e);
            throw new RuntimeException(msg, e);
        } 
        return query.list();
    }
    
    private void atribuirGruposMovimentoEstornoEnvioJornaleiro(final Query query, final String paramName){
    	
    	query.setParameterList(paramName, Arrays.asList(
        		GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO, 
        		GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE, 
        		GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA_PARA_LANCAMENTO,
        		GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA_PARA_PRODUTOS_DANIFICADOS,
        		GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA_PARA_RECOLHIMENTO,
        		GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA_PARA_SUPLEMENTAR));
    }
    
    private void atribuirGruposMovimentoEnvioJornaleiro(final Query query, final String paramName){
    	
    	query.setParameterList(paramName, Arrays.asList(
        		GrupoMovimentoEstoque.ENVIO_JORNALEIRO, 
        		GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE, 
        		GrupoMovimentoEstoque.VENDA_ENCALHE, 
        		GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR));
    }
    
private BigDecimal obterValorSumarizadoDoReparte(Date dataFechamento){
    	
    	Query query = getSession().createSQLQuery(this.obterSqlDaSumarizacaoDoReparte());
    	
    	query.setParameter("dataFechamento", dataFechamento);
        
    	query.setParameter("statusFuro", StatusLancamento.FURO.name());
        
        query.setParameter("statusAprovado", StatusAprovacao.APROVADO.name());
        
        query.setParameter("grupoReparte", GrupoMovimentoEstoque.RECEBIMENTO_FISICO.name());  
        
    	return (BigDecimal) Util.nvl(query.uniqueResult(),BigDecimal.ZERO);
    }
    
    private BigDecimal obterValorSumarizadoDeFaltas(Date dataFechamento){
    	
    	Query query = getSession().createSQLQuery(this.obterSqlDaSumarizacaoDasDiferencas());
    	
    	query.setParameter("dataFechamento", dataFechamento);
        
    	query.setParameter("statusFuro", StatusLancamento.FURO.name());
        
        query.setParameterList("tipoDiferenca", Arrays.asList(
								TipoDiferenca.FALTA_DE.name(),
								TipoDiferenca.FALTA_EM.name(),
								TipoDiferenca.PERDA_DE.name(),
								TipoDiferenca.PERDA_EM.name()));
        
        query.setParameterList("tipoDiferencasGFS", Arrays.asList(
								StatusIntegracao.EM_PROCESSAMENTO.name(),
				        		StatusIntegracao.EM_PROCESSO.name(),
				        		StatusIntegracao.SOLICITADO.name(),
				        		StatusIntegracao.NAO_INTEGRADO.name(),
				        		StatusIntegracao.RE_INTEGRADO.name(),
				        		StatusIntegracao.INTEGRADO.name(),
				        		StatusIntegracao.AGUARDANDO_GFS.name()));
        
    	return (BigDecimal) Util.nvl(query.uniqueResult(),BigDecimal.ZERO);
    }
    
    private BigDecimal obterValorSumarizadoDeSobras(Date dataFechamento){
    	
    	Query query = getSession().createSQLQuery(this.obterSqlDaSumarizacaoDasDiferencas());
    	
    	query.setParameter("dataFechamento", dataFechamento);
        
    	query.setParameter("statusFuro", StatusLancamento.FURO.name());
        
        query.setParameterList("tipoDiferenca", Arrays.asList(
								TipoDiferenca.SOBRA_DE.name(),
								TipoDiferenca.SOBRA_EM.name(),
								TipoDiferenca.GANHO_DE.name(),
								TipoDiferenca.GANHO_EM.name()));
       
        query.setParameterList("tipoDiferencasGFS", Arrays.asList(
								StatusIntegracao.EM_PROCESSAMENTO.name(),
				        		StatusIntegracao.EM_PROCESSO.name(),
				        		StatusIntegracao.SOLICITADO.name(),
				        		StatusIntegracao.NAO_INTEGRADO.name(),
				        		StatusIntegracao.RE_INTEGRADO.name(),
				        		StatusIntegracao.INTEGRADO.name(),
				        		StatusIntegracao.AGUARDANDO_GFS.name()));
        
    	return (BigDecimal) Util.nvl(query.uniqueResult(),BigDecimal.ZERO);
    }
    
    private BigDecimal obterValorSumarizadoDeTransferencia(Date dataFechamento){
    	
    	StringBuilder hql = new StringBuilder();
    	
    	hql.append(" SELECT COALESCE(if(tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE= :grupoTransferenciaLancamentoEntrada,"); 
    	hql.append("		movimentoEstoque.QTDE*produtoEdicao.PRECO_VENDA, ");
    	hql.append("		movimentoEstoque.QTDE*produtoEdicao.PRECO_VENDA*-1 ),0) ");
    	hql.append(" FROM MOVIMENTO_ESTOQUE movimentoEstoque ");
    	hql.append(" INNER JOIN TIPO_MOVIMENTO tipoMovimento ON movimentoEstoque.TIPO_MOVIMENTO_ID=tipoMovimento.ID ");
    	hql.append(" INNER JOIN PRODUTO_EDICAO produtoEdicao ON movimentoEstoque.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
    	hql.append(" WHERE movimentoEstoque.DATA=:dataFechamento");
    	hql.append(" AND movimentoEstoque.STATUS= :statusAprovado ");
    	hql.append(" AND tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE IN (:grupoTransferenciaLancamentoEntrada,:grupoTransferenciaLancamentoSaida) "); 
    	hql.append(" AND movimentoEstoque.PRODUTO_EDICAO_ID IN ( ");
    	hql.append("	SELECT DISTINCT produtoEdicaoExpedicao.ID ");
    	hql.append("	FROM EXPEDICAO expedicaoProduto ");
    	hql.append("	INNER JOIN LANCAMENTO lancamentoExpedicao ON expedicaoProduto.ID=lancamentoExpedicao.EXPEDICAO_ID ");
    	hql.append("	INNER JOIN PRODUTO_EDICAO produtoEdicaoExpedicao ON lancamentoExpedicao.PRODUTO_EDICAO_ID=produtoEdicaoExpedicao.ID ");
    	hql.append("	WHERE lancamentoExpedicao.STATUS<>:statusFuro "); 
    	hql.append("	AND lancamentoExpedicao.DATA_LCTO_DISTRIBUIDOR=:dataFechamento) ");
    	
    	Query query = getSession().createSQLQuery(hql.toString());
    	
    	query.setParameter("dataFechamento", dataFechamento);
        
    	query.setParameter("statusFuro", StatusLancamento.FURO.name());
        
    	query.setParameter("statusAprovado", StatusAprovacao.APROVADO.name());
        
    	query.setParameter("grupoTransferenciaLancamentoEntrada", GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO.name());
        
    	query.setParameter("grupoTransferenciaLancamentoSaida", GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO.name());
    	
    	return (BigDecimal) Util.nvl(query.uniqueResult(),BigDecimal.ZERO);
    }
    
    private BigDecimal obterValorSumarizadoReparteDistribuido(Date dataFechamento){

    	StringBuilder hql = new StringBuilder();
    	
    	hql.append(" SELECT SUM(if(tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE IN (:grupoMovimentoEnvioJornaleiro), ");
    	hql.append("		movimentoEstoque.QTDE*produtoEdicao.PRECO_VENDA, ");  
    	hql.append("		movimentoEstoque.QTDE*produtoEdicao.PRECO_VENDA*-1)) ");
    	hql.append(" FROM MOVIMENTO_ESTOQUE movimentoEstoque ");
    	hql.append(" INNER JOIN TIPO_MOVIMENTO tipoMovimento ON movimentoEstoque.TIPO_MOVIMENTO_ID=tipoMovimento.ID "); 
    	hql.append(" INNER JOIN PRODUTO_EDICAO produtoEdicao ON movimentoEstoque.PRODUTO_EDICAO_ID=produtoEdicao.ID "); 
    	hql.append(" WHERE movimentoEstoque.DATA=:dataFechamento ");
    	hql.append(" AND movimentoEstoque.STATUS=:statusAprovado "); 
    	hql.append(" AND tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE IN (:grupoMovimentoEnvioJornaleiro, :grupoMovimentoEstornoEnvioJornaleiro) "); 
    	hql.append(" AND movimentoEstoque.PRODUTO_EDICAO_ID IN ( ");
    	hql.append("	SELECT DISTINCT produtoEdicaoExpedicao.ID ");
    	hql.append("	FROM EXPEDICAO expedicaoProduto ");
    	hql.append("	INNER JOIN LANCAMENTO lancamentoExpedicao ON expedicaoProduto.ID=lancamentoExpedicao.EXPEDICAO_ID ");
    	hql.append("	INNER JOIN PRODUTO_EDICAO produtoEdicaoExpedicao ON lancamentoExpedicao.PRODUTO_EDICAO_ID=produtoEdicaoExpedicao.ID ");
    	hql.append("	WHERE lancamentoExpedicao.STATUS<>:statusFuro ");
    	hql.append("	AND lancamentoExpedicao.DATA_LCTO_DISTRIBUIDOR=:dataFechamento ) ");
    	
    	Query query = getSession().createSQLQuery(hql.toString());
    	
    	query.setParameter("dataFechamento", dataFechamento);
        
    	query.setParameter("statusFuro", StatusLancamento.FURO.name());
        
    	query.setParameter("statusAprovado", StatusAprovacao.APROVADO.name());
        
    	query.setParameterList("grupoMovimentoEnvioJornaleiro", Arrays.asList(
				        		GrupoMovimentoEstoque.ENVIO_JORNALEIRO.name(), 
				        		GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE.name(), 
				        		GrupoMovimentoEstoque.VENDA_ENCALHE.name(), 
				        		GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR.name()));
    	
    	
    	query.setParameterList("grupoMovimentoEstornoEnvioJornaleiro", Arrays.asList(
				        		GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO.name(), 
				        		GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE.name(), 
				        		GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA_PARA_LANCAMENTO.name(),
				        		GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA_PARA_PRODUTOS_DANIFICADOS.name(),
				        		GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA_PARA_RECOLHIMENTO.name(),
				        		GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA_PARA_SUPLEMENTAR.name()));
    	
    	return (BigDecimal) Util.nvl(query.uniqueResult(),BigDecimal.ZERO);
    }
    
    private BigDecimal obterValorSumarizadoRateioCota(Date dataFechamento){
    	
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append(" SELECT COALESCE(SUM( ");
    	sql.append("		if( diferencaProduto.TIPO_DIFERENCA IN (:tipoSobraDirecionadaCota) ");
    	sql.append("			 ,diferencaProduto.QTDE*produtoEdicao.PRECO_VENDA ");
    	sql.append("			 ,diferencaProduto.QTDE*produtoEdicao.PRECO_VENDA*-1 )), 0) ");
    	sql.append(" FROM DIFERENCA diferencaProduto ");
    	sql.append(" INNER JOIN LANCAMENTO_DIFERENCA lancamentoDiferenca ON diferencaProduto.LANCAMENTO_DIFERENCA_ID=lancamentoDiferenca.ID ");
    	sql.append(" INNER JOIN PRODUTO_EDICAO produtoEdicao ON diferencaProduto.PRODUTO_EDICAO_ID=produtoEdicao.ID  ");
    	sql.append(" WHERE diferencaProduto.DATA_MOVIMENTACAO=:dataFechamento  ");
    	sql.append(" AND diferencaProduto.TIPO_DIFERENCA IN (:tipoSobraDirecionadaCota,:tipoFaltaDirecionadaCota) ");
    	sql.append(" AND diferencaProduto.PRODUTO_EDICAO_ID IN ( ");
    	sql.append("	SELECT DISTINCT produtoExpedicao.ID ");
    	sql.append("	FROM EXPEDICAO expedicaoProduto ");
    	sql.append("	INNER JOIN LANCAMENTO lancamentoExpedicao ON expedicaoProduto.ID=lancamentoExpedicao.EXPEDICAO_ID ");
    	sql.append("	INNER JOIN PRODUTO_EDICAO produtoExpedicao ON lancamentoExpedicao.PRODUTO_EDICAO_ID=produtoExpedicao.ID ");
    	sql.append("	WHERE lancamentoExpedicao.STATUS<>:statusFuro ");
    	sql.append("	AND lancamentoExpedicao.DATA_LCTO_DISTRIBUIDOR=:dataFechamento) "); 
    	sql.append(" AND diferencaProduto.id IN ( ");
    	sql.append("	SELECT DISTINCT rateioDiferencaCota.DIFERENCA_ID FROM RATEIO_DIFERENCA rateioDiferencaCota ");
    	sql.append("	WHERE rateioDiferencaCota.DATA_MOVIMENTO=:dataFechamento)");
	
    	Query query = getSession().createSQLQuery(sql.toString());
    	
    	query.setParameter("dataFechamento", dataFechamento);
        
    	query.setParameter("statusFuro", StatusLancamento.FURO.name());
    	
    	query.setParameterList("tipoSobraDirecionadaCota", Arrays.asList(
				    			TipoDiferenca.SOBRA_EM.name(),
								TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA.name(), 
								TipoDiferenca.GANHO_EM.name()));
    	
    	query.setParameterList("tipoFaltaDirecionadaCota", Arrays.asList(
								TipoDiferenca.FALTA_EM.name(),
								TipoDiferenca.FALTA_EM_DIRECIONADA_COTA.name(),
								TipoDiferenca.PERDA_EM.name()));
    
    	return (BigDecimal) Util.nvl(query.uniqueResult(),BigDecimal.ZERO);
    }
    
    private BigDecimal obterValorSumarizadoRepartePromocional(Date dataFechamento){
    	
    	Query query = getSession().createSQLQuery(this.obterSqlDaSumarizacaoDoReparte());
    	
    	query.setParameter("dataFechamento", dataFechamento);
        
    	query.setParameter("statusFuro", StatusLancamento.FURO.name());
        
        query.setParameter("statusAprovado", StatusAprovacao.APROVADO.name());
        
        query.setParameterList("grupoReparte", Arrays.asList(
        		GrupoMovimentoEstoque.ESTORNO_REPARTE_PROMOCIONAL.name(),
        		GrupoMovimentoEstoque.GRUPO_MATERIAL_PROMOCIONAL.name()));  
        
    	return (BigDecimal) Util.nvl(query.uniqueResult(),BigDecimal.ZERO);
    }
    
    private String obterSqlDaSumarizacaoDasDiferencas(){
    	
    	StringBuilder hql = new StringBuilder();
    	
    	hql.append(" SELECT COALESCE(SUM(diferencaProduto.QTDE*produtoEdicaoDiferenca.PRECO_VENDA),0)");
    	hql.append(" FROM DIFERENCA diferencaProduto ");
    	hql.append(" INNER JOIN LANCAMENTO_DIFERENCA lancamentoDiferenca ON diferencaProduto.LANCAMENTO_DIFERENCA_ID=lancamentoDiferenca.ID "); 
    	hql.append(" INNER JOIN MOVIMENTO_ESTOQUE movimentoEstoqueLancamento ON lancamentoDiferenca.MOVIMENTO_ESTOQUE_ID=movimentoEstoqueLancamento.ID ");
    	hql.append(" INNER JOIN PRODUTO_EDICAO produtoEdicaoDiferenca ON diferencaProduto.PRODUTO_EDICAO_ID=produtoEdicaoDiferenca.ID ");
    	hql.append(" WHERE diferencaProduto.DATA_MOVIMENTACAO=:dataFechamento ");
    	hql.append(" AND movimentoEstoqueLancamento.STATUS_INTEGRACAO IN (:tipoDiferencasGFS)");
    	hql.append(" AND diferencaProduto.TIPO_DIFERENCA IN (:tipoDiferenca) ");
    	hql.append(" AND diferencaProduto.PRODUTO_EDICAO_ID IN ( ");
    	hql.append("	SELECT DISTINCT produtoEdicaoExpedicao.ID ");
    	hql.append("	FROM EXPEDICAO expedicaoProduto ");
    	hql.append("	INNER JOIN LANCAMENTO lancamentoExpedicao ON expedicaoProduto.ID=lancamentoExpedicao.EXPEDICAO_ID ");
    	hql.append("	INNER JOIN PRODUTO_EDICAO produtoEdicaoExpedicao ON lancamentoExpedicao.PRODUTO_EDICAO_ID=produtoEdicaoExpedicao.ID ");
    	hql.append("	WHERE lancamentoExpedicao.STATUS<>:statusFuro ");
    	hql.append("	AND lancamentoExpedicao.DATA_LCTO_DISTRIBUIDOR=:dataFechamento)");
    	
    	return hql.toString();
    }
    
    private String obterSqlDaSumarizacaoDoReparte(){
    	
    	StringBuilder hql = new StringBuilder();
    	
    	hql.append(" SELECT COALESCE(SUM(movimentoEstoque.QTDE*produtoEdicao.PRECO_VENDA), 0) ");
    	hql.append(" FROM MOVIMENTO_ESTOQUE movimentoEstoque ");
    	hql.append(" INNER JOIN PRODUTO_EDICAO produtoEdicao ON movimentoEstoque.PRODUTO_EDICAO_ID=produtoEdicao.ID ");
    	hql.append(" INNER JOIN TIPO_MOVIMENTO tipoMovimento on movimentoEstoque.TIPO_MOVIMENTO_ID=tipoMovimento.ID ");
    	hql.append(" WHERE movimentoEstoque.DATA<=:dataFechamento ");
    	hql.append(" AND movimentoEstoque.STATUS=:statusAprovado ");
    	hql.append(" AND tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE IN (:grupoReparte) ");
    	hql.append(" AND (produtoEdicao.ID IN ( ");
    	hql.append(" SELECT DISTINCT produtoEdicaoExpedicao.ID ");
    	hql.append(" 	FROM EXPEDICAO expedicao ");
    	hql.append(" 	INNER JOIN LANCAMENTO lancamentoProduto ON expedicao.ID=lancamentoProduto.EXPEDICAO_ID ");
    	hql.append(" 	INNER JOIN PRODUTO_EDICAO produtoEdicaoExpedicao ON lancamentoProduto.PRODUTO_EDICAO_ID=produtoEdicaoExpedicao.ID ");
    	hql.append(" 	WHERE lancamentoProduto.STATUS<>:statusFuro ");
    	hql.append(" 	AND lancamentoProduto.DATA_LCTO_DISTRIBUIDOR=:dataFechamento)) ");

    	return hql.toString();
    }
}
