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
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoReparteDTO;
import br.com.abril.nds.dto.filtro.FiltroExtratoEdicaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;

@Repository
public class MovimentoEstoqueRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoque, Long> 
implements MovimentoEstoqueRepository {
	
	/**
	 * Construtor padrão.
	 */
	public MovimentoEstoqueRepositoryImpl() {
		super(MovimentoEstoque.class);
	}
	
		
	/**
	 * Obtém uma lista de extratoEdicao de acordo com statusAprovacao.
	 */
	//@Override
	@SuppressWarnings("unchecked")
	public List<ExtratoEdicaoDTO> obterListaExtratoEdicao(FiltroExtratoEdicaoDTO filtro, StatusAprovacao statusAprovacao) {

		String codigoProduto = (filtro != null && filtro.getCodigoProduto() != null) ? filtro.getCodigoProduto() : null;
		Long numeroEdicao = filtro.getNumeroEdicao();
		
		StringBuilder hql = new StringBuilder("");
		
		hql.append(" select m.id as idMovimento, " +
				   " tipoMovimento.id as idTipoMovimento, " +
				   " m.data as dataMovimento, " +
				   " tipoMovimento.descricao as descMovimento, " +
				   " sum(case when tipoMovimento.operacaoEstoque  = :tipoOperacaoEntrada then m.qtde else 0 end) as qtdEdicaoEntrada, " +
				   " sum(case when tipoMovimento.operacaoEstoque  = :tipoOperacaoSaida then m.qtde else 0 end) as qtdEdicaoSaida, " +
				   " ld.id as idLancamentoDiferenca ");		
				
		hql.append(" from MovimentoEstoque m ");
		
		hql.append(" join m.tipoMovimento tipoMovimento ");
		
		hql.append(" join m.produtoEdicao produtoEdicao ");
		
		hql.append(" join produtoEdicao.produto produto ");
		
		hql.append(" left join m.lancamentoDiferenca ld ");
		
		hql.append(" left join ld.diferenca dif ");
			
		hql.append(" where produtoEdicao.numeroEdicao = :numeroEdicao ");		

		hql.append(" and produto.codigo = :codigoProduto ");
		
		if (filtro != null && filtro.getGruposExcluidos() != null) {
			hql.append(" and tipoMovimento.grupoMovimentoEstoque not in (:gruposExcluidos) ");
		}

		if(statusAprovacao != null) {
			hql.append(" and m.status = :statusAprovacao  ");
		}
		
		hql.append(" group by produtoEdicao.id, m.data, tipoMovimento.id, dif.tipoDiferenca ");		
		
		hql.append(" order by ");
		
		hql.append(" case when m.origem = 'CARGA_INICIAL' then m.data else m.dataAprovacao end asc, "); // Diferencial para registros inseridos via carga todos tem a mesma data de criação
		
		hql.append(" case when m.origem = 'CARGA_INICIAL' then tipoMovimento.operacaoEstoque end asc, " ); // Diferencial para registros inseridos via carga todos tem a mesma data de criação
		
		hql.append(" m.id ");
		
		Query query = getSession().createQuery(hql.toString());
		
		if(statusAprovacao != null) {
			query.setParameter("statusAprovacao", statusAprovacao);
		}
		
		query.setParameter("tipoOperacaoEntrada", OperacaoEstoque.ENTRADA);
		
		query.setParameter("tipoOperacaoSaida", OperacaoEstoque.SAIDA);

		query.setParameter("codigoProduto", codigoProduto);
		 
		query.setParameter("numeroEdicao", numeroEdicao);
		
		if (filtro != null && filtro.getGruposExcluidos() != null) {

			query.setParameterList("gruposExcluidos", filtro.getGruposExcluidos());
		}
		
		if (filtro != null && filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ExtratoEdicaoDTO.class));
		
		return query.list();
	}

	@Override
	public BigInteger obterReparteDistribuidoProduto(String codigoProduto){
//		 select coalesce(sum(QTDE),0) from movimento_estoque where TIPO_MOVIMENTO_ID=13 and PRODUTO_EDICAO_ID=2350
		
		Query query = getSession().createQuery("select coalesce(sum(me.qtde),0) from MovimentoEstoque me " +
				" join me.tipoMovimento tm " +
				" join me.produtoEdicao " +
				" join me.produtoEdicao.produto produto " +
				"where produto.codigo = :produtoId " +
				"and tm.id = :tipoMovimentoId");
		
		query.setParameter("produtoId", codigoProduto);
		
		final long ENVIO_AO_JORNALEIRO = 13l; //13:Envio ao jornaleiro tabela tipo_movimento 
		query.setParameter("tipoMovimentoId", ENVIO_AO_JORNALEIRO);
		
		Object uniqueResult2 = query.uniqueResult();
		BigInteger uniqueResult = (BigInteger)uniqueResult2;
		return uniqueResult;
	}
	
	/**
	 * Obtem os Grupos de Movimento de Estoque de Consignado
	 * @return List<String>
	 */
	public List<String> obterListaGrupoMovimentoConsignado(){
		
		List<String> listaGrupoMovimentoEstoque = Arrays.asList(GrupoMovimentoEstoque.ENVIO_JORNALEIRO.name(),
																GrupoMovimentoEstoque.ENVIO_JORNALEIRO_JURAMENTADO.name(),
																GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE.name(),
																GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE_JURAMENTADO.name(),
																GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE.name(),
																GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO.name(),
																GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE.name(),
																GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR.name(),
																GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE.name(),
																GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR.name());
		
		return listaGrupoMovimentoEstoque;
	}

	/**
     * Obtem valor total de Consignado ou AVista da data
     * @param data
     * @param operacaoEstoque
     * @param formaComercializacao
     * @return BigDecimal
     */
	@Override
    public BigDecimal obterSaldoDistribuidor(Date data, OperacaoEstoque operacaoEstoque, FormaComercializacao formaComercializacao) {

	    
        Objects.requireNonNull(data, "Data para contagem dos lançamentos expedidos não deve ser nula!");
        
        //Sql que obtem o os produtos que foram expedidos
        String templateHqlProdutoEdicaoExpedido = new StringBuilder("(select distinct(produtoEdicaoExpedido.id) from Expedicao expedicao ")
           .append(" join expedicao.lancamentos lancamento join lancamento.produtoEdicao produtoEdicaoExpedido join produtoEdicaoExpedido.produto prodExpedido where " )
           .append(" lancamento.status <> :statusFuro and lancamento.dataLancamentoDistribuidor =:data and prodExpedido.formaComercializacao=:formaComercializacao)").toString();
       
        // Sql que obtem as diferenças direcionadas apenas para as cotas
        String templateHqlDiferencaRateioCota =  new StringBuilder("(select ")
            .append(" COALESCE( sum(CASE  ")
            .append("              WHEN diferenca.tipoDiferenca='FALTA_EM' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda  * -1) ")
            .append("              WHEN diferenca.tipoDiferenca='PERDA_EM' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda * -1 )")
            .append("              WHEN diferenca.tipoDiferenca='SOBRA_EM' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda)  ")
            .append("              WHEN diferenca.tipoDiferenca='GANHO_EM' THEN (diferenca.qtde * diferenca.produtoEdicao.precoVenda)  ")
            .append("              WHEN diferenca.tipoDiferenca='SOBRA_EM_DIRECIONADA_COTA' THEN ( diferenca.qtde * diferenca.produtoEdicao.precoVenda )  ")
            .append("              ELSE 0 END),0) ")
            .append(" from Diferenca diferenca join diferenca.lancamentoDiferenca lancamentoDiferenca ")
            .append(" where  diferenca.dataMovimento = :data and diferenca.tipoDiferenca in (:%s) ")
            .append(" and diferenca.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido)
            .append(" and diferenca.id in ( select distinct rateio.diferenca.id from RateioDiferenca rateio where rateio.dataMovimento =:data ) ").append(")").toString();
       
        StringBuilder hql = new StringBuilder(" select ");
        
        hql.append("((select sum(case when movimentoEstoque.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEnvioJornaleiro ")
           .append("then (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda) else (movimentoEstoque.qtde * movimentoEstoque.produtoEdicao.precoVenda * -1) end) ")
           .append("from MovimentoEstoque movimentoEstoque where movimentoEstoque.data = :data and movimentoEstoque.status = :statusAprovado ")
           .append("and movimentoEstoque.tipoMovimento.grupoMovimentoEstoque in (:grupoMovimentoEnvioJornaleiro, :grupoMovimentoEstornoEnvioJornaleiro) ")
           .append("and movimentoEstoque.produtoEdicao.id in ").append(templateHqlProdutoEdicaoExpedido).append(") + ")
           .append(String.format(templateHqlDiferencaRateioCota,  "tipoDiferencaRateioCota")).append(") as totalDistribuido ");
        
        hql.append(" from Distribuidor ");
        
        Query query = getSession().createQuery(hql.toString());
        
        query.setMaxResults(1);
        
        query.setParameter("formaComercializacao", formaComercializacao);
        query.setParameter("data", data);
        query.setParameter("statusFuro", StatusLancamento.FURO);
        query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
        query.setParameter("grupoMovimentoEnvioJornaleiro", GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
        query.setParameter("grupoMovimentoEstornoEnvioJornaleiro", GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO);
                      
        query.setParameterList("tipoDiferencaRateioCota", Arrays.asList(TipoDiferenca.SOBRA_EM,
                                                                        TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA, 
                                                                        TipoDiferenca.GANHO_EM,
                                                                        TipoDiferenca.FALTA_EM,
                                                                        TipoDiferenca.PERDA_EM));
        
        return (BigDecimal) query.uniqueResult();
	}
	

	/**
	 * Obtem valor total de Consignado ou AVista da data
	 * @param data
	 * @param formaComercializacao
	 * @return BigDecimal
	 */
	@Override
	public BigDecimal obterSaldoDistribuidorEntrada(Date data,
			                                        FormaComercializacao formaComercializacao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("   SELECT SUM(COALESCE(CEC.QTDE_PREVISTA, 0) * PE.PRECO_VENDA) ");
		
		sql.append("   FROM CHAMADA_ENCALHE_COTA CEC ");
		
		sql.append("   INNER JOIN CHAMADA_ENCALHE CE ON CE.ID = CEC.CHAMADA_ENCALHE_ID ");
		     
		sql.append("   INNER JOIN PRODUTO_EDICAO PE ON CE.PRODUTO_EDICAO_ID = PE.ID ");
		     
		sql.append("   INNER JOIN PRODUTO P ON PE.PRODUTO_ID = P.ID ");
		
		
		sql.append("   LEFT JOIN ( ");
		
		sql.append("		select produto_edicao_furo.id as idProdutoEdicaoFuro ");
		
		sql.append("		from FURO_PRODUTO fp 								");
		
		sql.append("		inner join produto_edicao produto_edicao_furo 		");
		
		sql.append("		on produto_edicao_furo.id = fp.produto_edicao_id 	");
		
		sql.append("		where fp.data_lcto_distribuidor = :data	");
		
		sql.append("   ) as produtosFuradosNaData     			");
		
		sql.append("   ON produtosFuradosNaData.idProdutoEdicaoFuro = PE.ID	");
		
		
		sql.append("   WHERE	");
		     
		sql.append("   CE.DATA_RECOLHIMENTO = :data ");
		
		sql.append("   AND P.FORMA_COMERCIALIZACAO = :formaComercializacao ");
		
		sql.append("   AND produtosFuradosNaData.idProdutoEdicaoFuro is null       ");
		
		Query query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("data", data);
		
		query.setParameter("formaComercializacao", formaComercializacao.name());
		
		Object result = query.uniqueResult();
		
		return (result == null) ? BigDecimal.ZERO : (BigDecimal) result;
	}
    	
    private StringBuilder getSQLDescontoLogistica(){
            
        StringBuilder sql = new StringBuilder();
        
        sql.append(" ( ");
        
        sql.append("    CASE WHEN PE.DESCONTO_LOGISTICA_ID IS NOT NULL THEN ");
        
        sql.append("    ( ");
        
        sql.append("       SELECT DL.PERCENTUAL_DESCONTO ");
        
        sql.append("       FROM DESCONTO_LOGISTICA DL");
        
        sql.append("       WHERE DL.ID = PE.DESCONTO_LOGISTICA_ID ");
        
        sql.append("    ) ");
        
        sql.append("    ELSE ");
        
        sql.append("    ( ");
        
        sql.append("       CASE WHEN PR.DESCONTO_LOGISTICA_ID IS NOT NULL THEN ");
        
        sql.append("       ( ");
        
        sql.append("          SELECT DL.PERCENTUAL_DESCONTO ");
        
        sql.append("          FROM DESCONTO_LOGISTICA DL");
        
        sql.append("          WHERE DL.ID = PR.DESCONTO_LOGISTICA_ID ");
        
        sql.append("       ) ");
        
        sql.append("       ELSE 0 END");
        
        sql.append("    ) ");
        
        sql.append("    END ");
        
        sql.append(" ) ");
        
        return sql;
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentoEstoque> obterMovimentoEstoquePorIdProdutoEdicao(ProdutoEdicao produtoEdicao) {
		
		
		StringBuilder hql = new StringBuilder("select "); 
		hql.append("movimentoEstoque.id as id, ");
		hql.append("movimentoEstoque.tipoMovimento as tipoMovimento, ");
		hql.append("movimentoEstoque.qtde as qtde ");
		hql.append("from ");
		hql.append("MovimentoEstoque as movimentoEstoque ");
		hql.append("where movimentoEstoque.produtoEdicao = :produtoEdicao");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("produtoEdicao", produtoEdicao);
		
		query.setResultTransformer(Transformers.aliasToBean(MovimentoEstoque.class));
		return query.list();
	}
	

	@Override
	public MovimentoEstoque obterMovimentoEstoqueDoItemNotaFiscal(
			Long idItemNotaFiscal, 
			TipoMovimentoEstoque tipoMovimento) {
		
		StringBuilder hql = new StringBuilder(" select movimentoEstoque from MovimentoEstoque movimentoEstoque ")
			.append(" join movimentoEstoque.itemRecebimentoFisico itemRecebimentoFisico  	")
			.append(" join itemRecebimentoFisico.itemNotaFiscal itemNotaFiscal				")
			.append(" join movimentoEstoque.tipoMovimento tipoMovimento 					")
			.append(" where ")
			.append(" tipoMovimento.id = :idTipoMovimento and 	")
			.append(" itemNotaFiscal.id = :idItemNotaFiscal		");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setMaxResults(1);
		
		query.setParameter("idItemNotaFiscal", idItemNotaFiscal);
		query.setParameter("idTipoMovimento", tipoMovimento.getId());
		
		return (MovimentoEstoque) query.uniqueResult();
	}

	@Override
	public List<Long> obterMovimentosRepartePromocionalSemEstornoRecebimentoFisico(
			Long idProdutoEdicao,
			GrupoMovimentoEstoque grupoMovimentoEstoqueRepartePromocional,
			GrupoMovimentoEstoque grupoMovimentoEstoqueEstornoRecebimentoFisico) {
		
		StringBuilder sql = new StringBuilder(" SELECT ME.ID FROM MOVIMENTO_ESTOQUE ME ")
		.append(" INNER JOIN ITEM_RECEB_FISICO IRF ON ( IRF.ID = ME.ITEM_REC_FISICO_ID ) ")
		.append(" INNER JOIN PRODUTO_EDICAO PE ON (PE.ID = ME.PRODUTO_EDICAO_ID) ")
		.append(" INNER JOIN TIPO_MOVIMENTO TM_PROMOCIONAL ON (ME.TIPO_MOVIMENTO_ID = TM_PROMOCIONAL.ID) ")
		.append(" WHERE PE.ID = :idProdutoEdicao and  TM_PROMOCIONAL.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEstoqueRepartePromocional ")
		.append(" AND IRF.ID NOT IN ( " )
		
		.append(" SELECT IRF.ID FROM MOVIMENTO_ESTOQUE ME ")
		.append(" INNER JOIN ITEM_RECEB_FISICO IRF ON ( IRF.ID = ME.ITEM_REC_FISICO_ID ) ")
		.append(" INNER JOIN PRODUTO_EDICAO PE ON (PE.ID = ME.PRODUTO_EDICAO_ID) ")
		.append(" INNER JOIN TIPO_MOVIMENTO TM_ESTORNO ON (ME.TIPO_MOVIMENTO_ID = TM_ESTORNO.ID) ")
		.append(" WHERE PE.ID = :idProdutoEdicao and TM_ESTORNO.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEstoqueEstornoRecebimentoFisico ")
		
		.append(" ) " );
		
		Query query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("grupoMovimentoEstoqueRepartePromocional", grupoMovimentoEstoqueRepartePromocional.name());
		query.setParameter("grupoMovimentoEstoqueEstornoRecebimentoFisico", grupoMovimentoEstoqueEstornoRecebimentoFisico.name());
		
		return query.list();
	}
	
}
