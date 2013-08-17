package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.distribuicao.RankingFaturamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RankingFaturamentoRepository;

@Repository
public class RankingFaturamentoRepositoryImpl extends AbstractRepositoryModel<RankingFaturamento, Long> implements
		RankingFaturamentoRepository {
	
	public RankingFaturamentoRepositoryImpl(){
		super(RankingFaturamento.class);
	}

	@Override
	public void executeJobGerarRankingFaturamento() {
		StringBuilder hql = new StringBuilder();
		
		hql = new StringBuilder();
		hql.append(" INSERT INTO ranking_faturamento ( COTA_ID, FATURAMENTO ) ")
		.append(" select cota_id, sum(fat) as faturamento ")
		.append(" from (select l.data_rec_distrib, epc.cota_id,  ")
		.append("  (epc.qtde_recebida - epc.qtde_devolvida) * pe.preco_venda as fat	   ")
		.append("  from estoque_produto_cota epc ")
		.append("  join lancamento l on l.produto_edicao_id = epc.produto_edicao_id ")
		.append("  join produto_edicao PE ON pe.id = epc.produto_edicao_id ")
		.append("  join produto ON produto.ID = PE.PRODUTO_ID ")
		.append("  join tipo_produto tp on TP.id=produto.tipo_produto_id    ")
		.append("  where tp.grupo_produto <> 'CROMO' ")
		.append("    and tp.grupo_produto <> 'TALÃO' ")
		.append("    and l.data_rec_distrib between DATE_SUB(NOW(),INTERVAL + 13 week) and NOW()  ")
		.append("  group by epc.cota_id, pe.id) as target  ")
		.append("  where target.data_rec_distrib between DATE_SUB(NOW(),INTERVAL + 13 week) and NOW()  ")
		.append("  group by cota_id  ")
		.append("  order by faturamento desc ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		query.executeUpdate();
		
		atualizarClassificacaoCota();
	}
	
	@Override
	public void atualizarClassificacaoCota() {
	    StringBuilder sql = new StringBuilder();
	    sql.append("update cota c ");
	    sql.append("  join ranking_faturamento r on r.cota_id = c.id ");
	    sql.append("   and r.data_geracao_rank = (select max(DATA_GERACAO_RANK) from ranking_faturamento) ");
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
		hql.append(" rf.dataGeracaoRank = (select max(rfg.dataGeracaoRank) from RankingFaturamento rfg)");
		hql.append(" and rf.cota = :cota");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("cota", cota);
		
		List<RankingFaturamento> list = query.list();
		return list;
		
	}

}
