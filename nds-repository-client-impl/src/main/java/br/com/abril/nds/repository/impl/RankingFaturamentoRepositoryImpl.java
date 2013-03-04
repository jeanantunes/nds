package br.com.abril.nds.repository.impl;

import java.math.BigInteger;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.repository.RankingFaturamentoRepository;

@Repository
public class RankingFaturamentoRepositoryImpl extends RankingAbstract implements
		RankingFaturamentoRepository {

	@Override
	public void executeJobGerarRankingFaturamento() {
		StringBuilder hql = new StringBuilder();
		
		BigInteger novoId = criarNovoIDRanking();
		
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
		
	}

	@Override
	String getTipoRanking() {
		
		return "faturamento";
	}

}
