package br.com.abril.nds.repository.impl;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.BaseEstudoAnaliseFaixaReparteDTO;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoramaPosEstudoRepository;

@Repository
public class HistoramaPosEstudoRepositoryImpl extends AbstractRepositoryModel implements
		HistoramaPosEstudoRepository  {

	public HistoramaPosEstudoRepositoryImpl(){
		super(Object.class);
	}
	
	@Override
	public BaseEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo(int faixaDe, int faixaAte, Integer estudoId) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" '" + faixaDe + " a " + faixaAte + "' as faixaReparte, ");
		hql.append("  sum(reparte) as reparteTotal, ");
		hql.append("  avg(reparte) as reparteMedio, ");
		hql.append("  sum(venda) as vendaNominal, ");
		hql.append("  avg(venda) as vendaMedia, ");
		hql.append("  (sum(venda) / sum(reparte) * 100) as vendaPercent, ");
		hql.append("  ((sum(reparte) - sum(venda)) / sum(qtdCota)) * 100 as encalheMedio, ");
		hql.append("  (sum(qtdRecebida)/sum(reparte) * 100) as participacaoReparte, ");
		hql.append("  sum(qtdCota) as qtdCotas, ");
		hql.append("  sum(qtdCotasQuePossuemReparteMenorQueVendaNominal) as qtdCotaPossuemReparteMenorVenda, ");
		hql.append("  sum(qtdRecebida) as qtdRecebida ");
		hql.append(" FROM ");
		hql.append("  ( ");
		hql.append("    SELECT ");
		hql.append("      COUNT(DISTINCT cota.id) as qtdCota,");
		hql.append("      movimento_estoque_cota.QTDE as reparte,");
		hql.append("      estoque_produto_cota.QTDE_RECEBIDA - estoque_produto_cota.QTDE_DEVOLVIDA as venda,");
		hql.append("      estoque_produto_cota.QTDE_RECEBIDA as qtdRecebida,");
		hql.append("      estoque_produto_cota.QTDE_DEVOLVIDA as qtdDevolvida,");
		hql.append("      CASE ");
		hql.append("        WHEN  movimento_estoque_cota.QTDE < ( estoque_produto_cota.QTDE_RECEBIDA - estoque_produto_cota.QTDE_DEVOLVIDA )");
		hql.append("          THEN 1");
		hql.append("        ELSE 0");
		hql.append("      END as qtdCotasQuePossuemReparteMenorQueVendaNominal");
		hql.append("    FROM movimento_estoque_cota");
		hql.append("      INNER JOIN estudo_cota ON estudo_cota.ID = movimento_estoque_cota.ESTUDO_COTA_ID");
		hql.append("      INNER JOIN cota ON cota.ID = estudo_cota.COTA_ID");
		hql.append("      INNER JOIN pessoa ON pessoa.ID = cota.PESSOA_ID");
		hql.append("      INNER JOIN estoque_produto_cota ON estoque_produto_cota.ID = movimento_estoque_cota.ESTOQUE_PROD_COTA_ID");
		hql.append("    WHERE ");
		hql.append("      TIPO_MOVIMENTO_ID = 21  AND estudo_cota.ESTUDO_ID = :estudoId ");
		hql.append("    GROUP BY cota.NUMERO_COTA");
		hql.append("    HAVING reparte between :faixaDe and :faixaAte ");
		hql.append("  ) as baseReparte");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		
		query.setParameter("estudoId", 80222); // estudoId
		query.setParameter("faixaDe", faixaDe);
		query.setParameter("faixaAte", faixaAte);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(BaseEstudoAnaliseFaixaReparteDTO.class));

		BaseEstudoAnaliseFaixaReparteDTO resultado = (BaseEstudoAnaliseFaixaReparteDTO) query.uniqueResult();
		
		return resultado;
	}

}
