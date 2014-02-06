package br.com.abril.nds.repository.impl;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.HistogramaPosEstudoAnaliseFaixaReparteDTO;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistogramaPosEstudoRepository;

@SuppressWarnings("rawtypes")
@Repository
public class HistoramaPosEstudoRepositoryImpl extends AbstractRepositoryModel implements
		HistogramaPosEstudoRepository  {

	@SuppressWarnings("unchecked")
	public HistoramaPosEstudoRepositoryImpl(){
		super(Object.class);
	}
	
	@Override
	public HistogramaPosEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo(int faixaDe, int faixaAte, Integer estudoId) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT '").append(faixaDe).append(" a ").append(faixaAte).append("' faixaReparte, ");
		sql.append("       SUM(REPARTE) reparteTotal, ");
		sql.append("	   AVG(REPARTE) reparteMedio, ");
		sql.append("       SUM(VENDA_MEDIA) vendaNominal, ");
		sql.append("	   SUM(VENDA_MEDIA) / COUNT(DISTINCT NUMERO_COTA) vendaMedia, ");
		sql.append("       COUNT(DISTINCT NUMERO_COTA) qtdCotas, ");
		sql.append("	   SUM(RECEBIDO) qtdRecebida, ");
		sql.append("	   (SUM(VENDA_MEDIA) / SUM(REPARTE)) * 100 vendaPercent, ");
		sql.append("	   (SUM(REPARTE) - SUM(VENDA_MEDIA)) / COUNT(*) encalheMedio, ");
		sql.append("       COUNT(IS_REPARTE_MENOR_VENDA) qtdCotaPossuemReparteMenorVenda,  ");
		sql.append("       (SUM(REPARTE) / (SELECT SUM(REPARTE) FROM estudo_cota_gerado WHERE ESTUDO_ID = :ESTUDO_ID) * 100) participacaoReparte, ");
		sql.append("  		group_concat(IS_REPARTE_MENOR_VENDA) numeroCotasStr ");
		sql.append("  FROM (SELECT REP.ID, ");
		sql.append("               REP.NUMERO_COTA, ");
		sql.append("               SUM(REP.REPARTE) REPARTE, ");
		sql.append("               SUM(EST.RECEBIDO) AS RECEBIDO, ");
		sql.append("               SUM(EST.VENDA_MEDIA) AS VENDA_MEDIA, ");
		sql.append("               EST.QTDE_EDICOES, ");
		sql.append("               (CASE WHEN SUM(REP.REPARTE) < SUM(VENDA_MEDIA) THEN REP.NUMERO_COTA ELSE NULL END) IS_REPARTE_MENOR_VENDA ");
		sql.append("          FROM (SELECT C.ID, ");
		sql.append("                       C.NUMERO_COTA, ");
		sql.append("                       EC.REPARTE ");
		sql.append("                  FROM estudo_cota_gerado EC ");
		sql.append("                  JOIN COTA C ON C.ID = EC.COTA_ID ");
		sql.append("                 WHERE EC.ESTUDO_ID = :ESTUDO_ID) REP ");
		sql.append("         LEFT JOIN (SELECT C.ID, ");
		sql.append("                       C.NUMERO_COTA, ");
		sql.append("                       SUM(EPE.QTDE_RECEBIDA) RECEBIDO, ");
		sql.append("                       SUM(EPE.QTDE_RECEBIDA - EPE.QTDE_DEVOLVIDA) VENDA, ");
		sql.append("                       AVG(EPE.QTDE_RECEBIDA - EPE.QTDE_DEVOLVIDA) VENDA_MEDIA, ");
		sql.append("                       COUNT(*) QTDE_EDICOES ");
		sql.append("                  FROM estoque_produto_cota EPE ");
		sql.append("                  JOIN COTA C ON C.ID = EPE.COTA_ID "); 
		sql.append("                  WHERE EPE.PRODUTO_EDICAO_ID IN (SELECT PRODUTO_EDICAO_ID FROM ESTUDO_PRODUTO_EDICAO_BASE WHERE ESTUDO_ID = :ESTUDO_ID) ");
		sql.append("                 GROUP BY C.ID, C.NUMERO_COTA) EST ON EST.ID = REP.ID ");
		sql.append("         WHERE REP.REPARTE BETWEEN :DE AND :ATE GROUP BY NUMERO_COTA) TES ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("ESTUDO_ID", estudoId);
		query.setParameter("DE", faixaDe);
		query.setParameter("ATE", faixaAte);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(HistogramaPosEstudoAnaliseFaixaReparteDTO.class));

		HistogramaPosEstudoAnaliseFaixaReparteDTO resultado = (HistogramaPosEstudoAnaliseFaixaReparteDTO) query.uniqueResult();
		
		return resultado;
	}
}
