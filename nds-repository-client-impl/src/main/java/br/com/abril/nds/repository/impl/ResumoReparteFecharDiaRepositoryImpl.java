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
import br.com.abril.nds.vo.PaginacaoVO;


@Repository
public class ResumoReparteFecharDiaRepositoryImpl  extends AbstractRepository implements ResumoReparteFecharDiaRepository {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ResumoReparteFecharDiaRepositoryImpl.class);
	

	@Override
	public SumarizacaoReparteDTO obterSumarizacaoReparte(Date data,Date dataReparteHistoico) {
		
        Objects.requireNonNull(data, "Data para contagem dos lançamentos expedidos não deve ser nula!");
        
	    //Sql que obtem o os produtos que foram expedidos
        String templateHqlProdutoEdicaoExpedido = new StringBuilder("(select distinct(produtoEdicaoExpedido.id) from Expedicao expedicao ")
           .append(" join expedicao.lancamentos lancamento join lancamento.produtoEdicao produtoEdicaoExpedido where " )
           .append(" lancamento.status <> :statusFuro and lancamento.dataLancamentoDistribuidor =:data )").toString();
        
        // Sql que obtem as diferenças apontadas para o distribuidor
        String templateHqlDiferenca =  new StringBuilder("(select  ")
	        .append(" COALESCE( sum(CASE  ")
		    .append(" 		       WHEN diferenca.tipoDiferenca='SOBRA_DE' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda)")
		    .append(" 		       WHEN diferenca.tipoDiferenca='SOBRA_EM' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda)")
		    .append(" 		       WHEN diferenca.tipoDiferenca='GANHO_DE' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda)")
		    .append(" 		       WHEN diferenca.tipoDiferenca='GANHO_EM' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda)")
		    .append(" 		       WHEN diferenca.tipoDiferenca='SOBRA_EM_DIRECIONADA_COTA' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda)")
		    .append(" 		       WHEN diferenca.tipoDiferenca='FALTA_DE' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda *-1)")
		    .append(" 		       WHEN diferenca.tipoDiferenca='FALTA_EM' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda *-1)")
		    .append(" 		       WHEN diferenca.tipoDiferenca='FALTA_EM_DIRECIONADA_COTA' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda *-1)")
		    .append(" 		       WHEN diferenca.tipoDiferenca='PERDA_DE' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda *-1)")
		    .append(" 		       WHEN diferenca.tipoDiferenca='PERDA_EM' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda *-1)")
		    .append(" 		       ELSE 0 END),0) ")
        	.append(" from Diferenca diferenca join diferenca.lancamentoDiferenca lancamentoDiferenca ")
	        .append(" where diferenca.dataMovimento = :data and diferenca.tipoDiferenca in (:tipoDiferenca) ")
	        .append(" and diferenca.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido).append(")").toString();
       
        // Sql que obtem as diferencas apontadas para distribuidor que estão
        // pendentes de aprovação do GFS
        String templateHqlDiferencaGFSPendenteDeAprovacao =  new StringBuilder("(select sum(diferenca.qtde * diferenca.produtoEdicao.precoVenda) ")
	    	.append(" from Diferenca diferenca join diferenca.lancamentoDiferenca lancamentoDiferenca ")
	        .append(" where lancamentoDiferenca.movimentoEstoque.statusIntegracao in (:statusIntegracaoGFS) and  diferenca.dataMovimento = :data and diferenca.tipoDiferenca in (:%s) ")
	        .append(" and diferenca.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido).append(") as %s ").toString();
        
        // Sql que obtem as diferenças direcionadas apenas para as cotas
        String templateHqlDiferencaRateioCota =  new StringBuilder("(select ")
	        .append(" COALESCE( sum(CASE  ")
		    .append(" 		       WHEN diferenca.tipoDiferenca='FALTA_EM' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda  * -1) ")
		    .append(" 		       WHEN diferenca.tipoDiferenca='FALTA_EM_DIRECIONADA_COTA' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda  * -1) ")
		    .append(" 		       WHEN diferenca.tipoDiferenca='PERDA_EM' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda * -1 )")
		    .append(" 		       WHEN diferenca.tipoDiferenca='SOBRA_EM' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda)  ")
		    .append(" 		       WHEN diferenca.tipoDiferenca='GANHO_EM' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda)  ")
		    .append(" 		       WHEN diferenca.tipoDiferenca='SOBRA_EM_DIRECIONADA_COTA' THEN ( diferenca.qtde * diferenca.produtoEdicao.precoVenda )  ")
		    .append(" 		       ELSE 0 END),0) ")
        	.append(" from Diferenca diferenca join diferenca.lancamentoDiferenca lancamentoDiferenca ")
	        .append(" where  diferenca.dataMovimento = :data and diferenca.tipoDiferenca in (:%s) ")
	        .append(" and diferenca.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido)
	        .append(" and diferenca.id in ( select distinct rateio.diferenca.id from RateioDiferenca rateio where rateio.dataMovimento =:data ) ").append(")").toString();
        
        //Sql que obtem produtos com recebimento fisico
        String templateHqlRecebimentoEstoqueFisico = new StringBuilder()
        	.append(" (select COALESCE(sum(me.qtde*produtoEdicaoME.precoVenda),0) from MovimentoEstoque me join me.produtoEdicao produtoEdicaoME ")
	        .append(" where me.dataAprovacao = :data ")
	        .append(" and me.status = :statusAprovado ")
	        .append(" and me.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoRecebimentoFisico ")
	        .append(" and produtoEdicaoME.id in ").append(templateHqlProdutoEdicaoExpedido).append(")").toString();
        
        String templateHqlRecebimentoEstoqueFisicoPromocional = new StringBuilder()
	        .append(" (select COALESCE(sum(me.qtde*me.produtoEdicao.precoVenda),0) from MovimentoEstoque me join me.produtoEdicao produtoEdicaoME ")
		       .append(" where me.dataAprovacao = :data ")
		       .append(" and me.status = :statusAprovado ")
		       .append(" and me.tipoMovimento.grupoMovimentoEstoque IN( :grupoMovimentoRecebimentoFisicoPromocional )")
		       .append(" and produtoEdicaoME.id = produtoEdicao.id )").toString();
        
        StringBuilder hql = new StringBuilder(" select COALESCE(sum(hstEstoque.qtde * produtoEdicao.precoVenda),0)")
        	.append(" + ").append(templateHqlRecebimentoEstoqueFisico).append(" + ").append(templateHqlDiferenca).append(" as totalReparte, ");
                
	    hql.append(String.format(templateHqlDiferencaGFSPendenteDeAprovacao,"tipoDiferencaSobras", "totalSobras")).append(",")
	       .append(String.format(templateHqlDiferencaGFSPendenteDeAprovacao,"tipoDiferencaFaltas", "totalFaltas")).append(",");
	    
	    hql.append("(select sum(case when movimentoEstoque.tipoMovimento.grupoMovimentoEstoque = :grupoTransferenciaLancamentoEntrada ")
           .append("then (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda) else (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda * -1) end) ")
           .append("from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data and movimentoEstoque.status = :statusAprovado ")
           .append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:grupoTransferenciaLancamentoEntrada, :grupoTransferenciaLancamentoSaida) ")
           .append("and movimentoEstoque.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido).append(") as totalTransferencias, ");
	    
        hql.append("(select sum(case when movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in ( :grupoMovimentoEnvioJornaleiro ) ")
           .append("then (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda) else (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda * -1) end) ")
           .append("from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data and movimentoEstoque.status = :statusAprovado ")
           .append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:grupoMovimentoEnvioJornaleiro, :grupoMovimentoEstornoEnvioJornaleiro) ")
           .append("and movimentoEstoque.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido).append(") + ")
           .append(String.format(templateHqlDiferencaRateioCota,  "tipoDiferencaRateioCota")).append(" as totalDistribuido ,");
        
        hql.append(templateHqlRecebimentoEstoqueFisicoPromocional).append(" as totalDiferenca ");
        
        hql.append(" from Expedicao expedicao ")
	        .append(" join expedicao.lancamentos lancamento " )
	        .append(" join lancamento.produtoEdicao produtoEdicao ")
	        .append(" join produtoEdicao.produto produto ")
	        .append(" left join produtoEdicao.historicoEstoqueProduto hstEstoque ")
	        .append(" where  ")
	        .append(" (hstEstoque.data is null or hstEstoque.data =:dataConsultaHistorico ) ")
	        .append("  and lancamento.status <> :statusFuro ")
	        .append(" and lancamento.dataLancamentoDistribuidor =:data ) ");
        
        Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("dataConsultaHistorico", dataReparteHistoico);
        query.setParameter("data", data);
        query.setParameter("statusFuro", StatusLancamento.FURO);
        query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
        query.setParameter("grupoTransferenciaLancamentoEntrada", GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
        query.setParameter("grupoTransferenciaLancamentoSaida", GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO);
        query.setParameter("grupoMovimentoRecebimentoFisico", GrupoMovimentoEstoque.RECEBIMENTO_FISICO);  
        query.setParameterList("grupoMovimentoRecebimentoFisicoPromocional",Arrays.asList(
        		GrupoMovimentoEstoque.ESTORNO_REPARTE_PROMOCIONAL,
        		GrupoMovimentoEstoque.GRUPO_MATERIAL_PROMOCIONAL));
        this.atribuirGruposMovimentoEnvioJornaleiro(query, "grupoMovimentoEnvioJornaleiro");
        this.atribuirGruposMovimentoEstornoEnvioJornaleiro(query, "grupoMovimentoEstornoEnvioJornaleiro");
        this.atribuirStatusIntegracaoGFS(query, "statusIntegracaoGFS");
        this.atribuirTipoDiferencaSobras(query, "tipoDiferencaSobras");
        this.atribuirTipoDiferencaFalta(query,"tipoDiferencaFaltas");
        this.atribuirTipoDiferenca(query,"tipoDiferenca");
        this.atribuirTipoDiferencaRateioCota(query, "tipoDiferencaRateioCota");
        
        try {
        	
            Constructor<SumarizacaoReparteDTO> constructor = 
            		SumarizacaoReparteDTO.class.getConstructor(BigDecimal.class, BigDecimal.class, BigDecimal.class, 
            				BigDecimal.class,BigDecimal.class,BigDecimal.class);
            
            query.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
        
        } catch (NoSuchMethodException | SecurityException e) {
            
        	String msg = "Erro definindo result transformer para classe: " + SumarizacaoReparteDTO.class.getName();
            
        	LOGGER.error(msg, e);
            
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
    private List<ReparteFecharDiaDTO> obterLancamentosExpedidos(Date data, PaginacaoVO paginacao,Date dataReparteHistoico) {
        Objects.requireNonNull(data, "Data para consulta ao resumo do reparte não deve ser nula!");

        Date dataInicio = DateUtil.removerTimestamp(data);
        
        String templateHqlDiferenca = new StringBuilder("(select sum(diferenca.qtde) from Diferenca diferenca join diferenca.lancamentoDiferenca lancamentoDiferenca  ") 
        	.append("where diferenca.dataMovimento = :data and diferenca.produtoEdicao.id = produtoEdicao.id and diferenca.tipoDirecionamento =:tipoDirecionamentoDiferenca and diferenca.tipoDiferenca in (:%s) ) as %s ").toString();
        
        String templateHqlRecebimentoEstoqueFisico = new StringBuilder()
           .append(" (select COALESCE(sum(me.qtde),0) from MovimentoEstoque me join me.produtoEdicao produtoEdicaoME ")
	       .append(" where me.dataAprovacao = :data ")
	       .append(" and me.status = :statusAprovado ")
	       .append(" and me.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoRecebimentoFisico ")
	       .append(" and produtoEdicaoME.id = produtoEdicao.id )").toString();
        
        String templateHqlRecebimentoEstoqueFisicoPromocional = new StringBuilder()
	        .append(" (select COALESCE(sum(me.qtde),0) from MovimentoEstoque me join me.produtoEdicao produtoEdicaoME ")
	        .append(" where me.dataAprovacao = :data ")
	        .append(" and me.tipoMovimento.grupoMovimentoEstoque IN( :grupoMovimentoRecebimentoFisicoPromocional) ")
	        .append(" and produtoEdicaoME.id = produtoEdicao.id )").toString();
        
        StringBuilder hql = new StringBuilder("select distinct produtoEdicao.id as idProdutoEdicao, produto.codigo as codigo, ");
				      hql.append("produto.nome as nomeProduto, ");
				      hql.append("produtoEdicao.numeroEdicao as numeroEdicao, ");
				      hql.append("produtoEdicao.precoVenda as precoVenda, ");
				      hql.append("COALESCE( hstEstoque.qtde,0 ) + ").append(templateHqlRecebimentoEstoqueFisico).append(" as qtdeReparte, ");
        
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
	         .append(" (hstEstoque.data is null or hstEstoque.data =:dataConsultaHistorico ) ")
	         .append(" and lancamento.status <> :statusFuro ")
	         .append(" and lancamento.dataLancamentoDistribuidor =:data ) ");
	    	
        hql.append("order by produto.codigo asc");
    
        Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("dataConsultaHistorico", dataReparteHistoico);
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
    
    private void atribuirTipoDiferencaRateioCota(final Query query, final String paramName) {
		
    	query.setParameterList(paramName, Arrays.asList(
    			TipoDiferenca.SOBRA_EM,
				TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA, 
				TipoDiferenca.GANHO_EM,
				TipoDiferenca.FALTA_EM,
				TipoDiferenca.FALTA_EM_DIRECIONADA_COTA,
				TipoDiferenca.PERDA_EM));
	}

	private void atribuirTipoDiferencaFalta(final Query query, final String paramName) {
		
		query.setParameterList(paramName, Arrays.asList(
				TipoDiferenca.FALTA_DE,
				TipoDiferenca.FALTA_EM,
				TipoDiferenca.PERDA_DE,
				TipoDiferenca.PERDA_EM));
	}

	private void atribuirTipoDiferencaSobras(final Query query, final String paramName) {
		
		query.setParameterList(paramName, Arrays.asList(
				TipoDiferenca.SOBRA_DE,
				TipoDiferenca.SOBRA_EM,
				TipoDiferenca.GANHO_DE,
				TipoDiferenca.GANHO_EM));
	}

	private void atribuirStatusIntegracaoGFS(final Query query, final String paramName) {
		
		query.setParameterList(paramName, Arrays.asList(
				StatusIntegracao.EM_PROCESSAMENTO,
        		StatusIntegracao.EM_PROCESSO,
        		StatusIntegracao.SOLICITADO,
        		StatusIntegracao.NAO_INTEGRADO,
        		StatusIntegracao.RE_INTEGRADO,
        		StatusIntegracao.INTEGRADO,
        		StatusIntegracao.AGUARDANDO_GFS));
	}
	
	private void atribuirTipoDiferenca(final Query query, final String paramName) {
			
		query.setParameterList(paramName, Arrays.asList(
				TipoDiferenca.FALTA_DE,
				TipoDiferenca.FALTA_EM,
				TipoDiferenca.PERDA_DE,
				TipoDiferenca.PERDA_EM,
				TipoDiferenca.SOBRA_DE,
				TipoDiferenca.SOBRA_EM,
				TipoDiferenca.GANHO_DE,
				TipoDiferenca.GANHO_EM,
				TipoDiferenca.FALTA_EM_DIRECIONADA_COTA,
				TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA));
	 }

    
}
