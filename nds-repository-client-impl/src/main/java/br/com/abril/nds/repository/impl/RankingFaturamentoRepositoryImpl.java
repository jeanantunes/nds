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
	public void gerarRankingFaturamento() {
		StringBuilder sql = new StringBuilder();

		sql.append(" INSERT INTO ranking_faturamento (COTA_ID, FATURAMENTO, DATA_GERACAO_RANK) ");
		sql.append(" select ");
		sql.append("     innerQuery.cota_id, ");
		sql.append("     sum(innerQuery.faturamento), ");
		sql.append("     SYSDATE() ");
		sql.append(" from ");
		sql.append("     (select "); 
		sql.append("         epc.cota_id as cota_id, ");
		sql.append("             (epc.qtde_recebida - epc.qtde_devolvida) * pe.preco_venda as faturamento ");
		sql.append("     from ");
		sql.append("         estoque_produto_cota epc ");
		sql.append("     join lancamento l ON l.produto_edicao_id = epc.produto_edicao_id ");
		sql.append("     join produto_edicao PE ON pe.id = epc.produto_edicao_id ");
		sql.append("     join produto ON produto.ID = PE.PRODUTO_ID ");
		sql.append("     join tipo_produto tp ON TP.id = produto.tipo_produto_id ");
		sql.append("     JOIN cota c ON epc.COTA_ID = c.ID ");
		sql.append("     where ");
		sql.append("         tp.grupo_produto <> 'CROMO' ");
		sql.append("             and tp.grupo_produto <> 'TALÃO' ");
		sql.append("             and l.data_rec_distrib between DATE_SUB(NOW(), INTERVAL + 13 week) and NOW() ");
		sql.append("             AND c.SITUACAO_CADASTRO in ('ATIVO' , 'SUSPENSO') ");
		sql.append("     group by epc.cota_id , pe.id) as innerQuery ");
		sql.append(" group by innerQuery.cota_id ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.executeUpdate();
	}
	
	public void gerarRankingFaturamentoParaCotasSemRanking() {
		StringBuilder sql = new StringBuilder();

		sql.append(" INSERT INTO ranking_faturamento (COTA_ID, FATURAMENTO, DATA_GERACAO_RANK) ");
		sql.append(" select ");
		sql.append("     c.ID, ");
		sql.append("     0, SYSDATE() ");
		sql.append(" from ");
		sql.append("     cota c, ");
		sql.append("     lancamento l ");
		sql.append(" where ");
		sql.append("     not exists(select "); 
		sql.append("             rs.ID ");
		sql.append("         from ");
		sql.append("             ranking_faturamento rs ");
		sql.append("         where ");
		sql.append("             rs.COTA_ID = c.id) ");
		sql.append("         AND C.SITUACAO_CADASTRO IN ('ATIVO' , 'SUSPENSO') ");
		sql.append("         and l.data_rec_distrib between DATE_SUB(NOW(), INTERVAL + 13 week) and NOW() ");
		sql.append(" group by c.ID ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.executeUpdate();
	}
	
	public void deletarRankingFaturamento() {
		StringBuilder sql = new StringBuilder();

		sql.append(" delete from ranking_faturamento; ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.executeUpdate();
	}
	
	@Override
	public void atualizarClassificacaoCota() {
	    StringBuilder sql = new StringBuilder();
	    sql.append("update cota c ");
	    sql.append("  join ranking_faturamento r on r.cota_id = c.id ");
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
		hql.append(" rf.cota = :cota");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("cota", cota);
		
		List<RankingFaturamento> list = query.list();
		return list;
		
	}

}
