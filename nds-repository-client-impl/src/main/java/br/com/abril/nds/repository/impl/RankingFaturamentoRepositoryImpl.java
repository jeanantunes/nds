package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.distribuicao.RankingFaturamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RankingFaturamentoIdRepository;
import br.com.abril.nds.repository.RankingFaturamentoRepository;

@Repository
public class RankingFaturamentoRepositoryImpl extends AbstractRepositoryModel<RankingFaturamento, Long> implements
		RankingFaturamentoRepository {
	
	@Autowired
	private RankingFaturamentoIdRepository faturamentoIdRepository;
	
	public RankingFaturamentoRepositoryImpl(){
		super(RankingFaturamento.class);
	}

	@Override
	public void executeJobGerarRankingFaturamento() {
		StringBuilder hql = new StringBuilder();
		
		BigInteger novoId = faturamentoIdRepository.criarNovoIDRanking();
		
		hql = new StringBuilder();
		hql.append(" INSERT INTO ranking_faturamento ( COTA_ID, FATURAMENTO,RANKING_FATURAMENTO_GERADOS_ID) ")
		.append(" (select cota_id, sum(fat) as faturamento,").append(novoId).append(" from ")
		.append(" (select mec.data, mec.cota_id, ")
		.append(" case when mec.tipo_movimento_id=21 then (mec.qtde*pe.preco_venda) ")
		.append(" when mec.tipo_movimento_id=26 then (mec.qtde*pe.preco_venda*-1)  ")
		.append(" else 0 end as fat	  ")
		.append(" from movimento_estoque_cota mec   ")
		.append(" join estoque_produto_cota EPC ON EPC.ID = mec.ESTOQUE_PROD_COTA_ID   ")
		.append(" join produto_edicao PE ON pe.ID = EPC.PRODUTO_EDICAO_ID   ")
		.append(" join produto ON produto.ID = PE.PRODUTO_ID   ")
		.append(" join TIPO_PRODUTO TP on TP.id=produto.tipo_produto_id   ")
		.append(" where   ")
		.append(" (mec.tipo_movimento_id=21 or mec.tipo_movimento_id=26) and ")
		.append(" (TP.GRUPO_PRODUTO<>'CROMO' and  TP.GRUPO_PRODUTO<>'TALÃO') and ")
		.append(" mec.DATA between DATE_SUB(NOW(),INTERVAL + 13 week) and NOW() ")
		.append(" group by mec.COTA_ID,pe.ID,mec.tipo_movimento_id ) as target ")
		.append(" where target.DATA between DATE_SUB(NOW(),INTERVAL + 13 week) and NOW() ")
		.append(" group by cota_id ")
		.append(" order by faturamento desc) ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		query.executeUpdate();
		
		atualizarClassificacaoCota();
	}
	
	@Override
	public void atualizarClassificacaoCota() {
	    StringBuilder sql = new StringBuilder();
	    sql.append("update cota c ");
	    sql.append("  join ranking_faturamento r on r.cota_id = c.id ");
	    sql.append("   and r.ranking_faturamento_gerados_id = (select max(id) from ranking_faturamento_gerados) ");
	    sql.append("   set c.classificacao_espectativa_faturamento = ");
	    sql.append("   (case when r.faturamento < (select coalesce(valor_ate, 1000) ");
	    sql.append("                                 from distribuidor_classificacao_cota ");
	    sql.append("                                where cod_classificacao = 'D') then 'D' ");
	    sql.append("         when r.faturamento < (select coalesce(valor_ate, 2000) ");
	    sql.append("                                 from distribuidor_classificacao_cota ");
	    sql.append("                                where cod_classificacao = 'C') then 'C' ");
	    sql.append("         when r.faturamento < (select coalesce(valor_ate, 4000) ");
	    sql.append("                                 from distribuidor_classificacao_cota ");
	    sql.append("                                where cod_classificacao = 'B') then 'B' ");
	    sql.append("         else (case when (select count(p.id) ");
	    sql.append("                            from pdv p ");
	    sql.append("                           where p.cota_id = c.id) < 2 then 'A' else 'AA' end) ");
	    sql.append("   end) ");
	    
	    SQLQuery query = this.getSession().createSQLQuery(sql.toString());
	    query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<RankingFaturamento>  buscarPorCota(Cota cota){
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select rf from RankingFaturamento rf where "); 
		hql.append(" rf.rankingFaturamentoGerado.id = (select max(rfg.id) from RankingFaturamentoGerado rfg)");
		hql.append(" and rf.cota = :cota");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("cota", cota);
		
		
		List<RankingFaturamento> list = query.list();
		return list;
		
	}

}
