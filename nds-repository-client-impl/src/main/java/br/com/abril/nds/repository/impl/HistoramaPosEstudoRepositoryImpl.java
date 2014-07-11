package br.com.abril.nds.repository.impl;

import java.util.List;

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
	public HistogramaPosEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo(int faixaDe, int faixaAte, Integer estudoId, List<Long> listaIdEdicaoBase) {
		
        String parametroEdicoesBase = " ";
		
        if (listaIdEdicaoBase !=null && !listaIdEdicaoBase.isEmpty()){
		    
        	parametroEdicoesBase = " AND EPE.PRODUTO_EDICAO_ID IN (:EDICOES_BASES) ";
		}
        
		StringBuilder sql = new StringBuilder();
		
		sql.append("  SELECT '").append(faixaDe).append(" a ").append(faixaAte).append("' faixaReparte, ");
		
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
		
		sql.append("               SUM(REP.RECEBIDO) AS RECEBIDO, ");
		
		sql.append("               SUM(REP.VENDA_MEDIA) AS VENDA_MEDIA, ");
		
		sql.append("               REP.QTDE_EDICOES, ");
		
		sql.append("               (CASE WHEN SUM(REP.REPARTE) < SUM(VENDA_MEDIA) THEN REP.NUMERO_COTA ELSE NULL END) IS_REPARTE_MENOR_VENDA ");
		
		sql.append("          FROM (SELECT C.ID, ");
		
		sql.append("                       C.NUMERO_COTA, ");
		
		sql.append("                       EC.REPARTE, ");
		
		
		sql.append("                       (SELECT SUM(EPE.QTDE_RECEBIDA) "); 
		
		sql.append("                        FROM estoque_produto_cota EPE "); 
		
		sql.append("                        WHERE EPE.COTA_ID = C.ID ");
		
        sql.append(                         parametroEdicoesBase);
		
		sql.append("                        ) RECEBIDO,  ");
		
		
        sql.append("                       (SELECT SUM(EPE.QTDE_RECEBIDA - EPE.QTDE_DEVOLVIDA) "); 
		
		sql.append("                        FROM estoque_produto_cota EPE "); 
		
		sql.append("                        WHERE EPE.COTA_ID = C.ID ");
		
        sql.append(                         parametroEdicoesBase);
		
		sql.append("                        ) VENDA,  ");
		
		
        sql.append("                       (SELECT AVG(EPE.QTDE_RECEBIDA - EPE.QTDE_DEVOLVIDA) "); 
		
		sql.append("                        FROM estoque_produto_cota EPE "); 
		
		sql.append("                        WHERE EPE.COTA_ID = C.ID ");
		
        sql.append(                         parametroEdicoesBase);
		
		sql.append("                        ) VENDA_MEDIA,  ");
		
		
        sql.append("                       (SELECT COUNT(*) "); 
		
		sql.append("                        FROM estoque_produto_cota EPE "); 
		
		sql.append("                        WHERE EPE.COTA_ID = C.ID ");
		
        sql.append(                         parametroEdicoesBase);
		
		sql.append("                        ) QTDE_EDICOES  ");

		
		sql.append("                  FROM estudo_cota_gerado EC ");
		
		sql.append("                  JOIN COTA C ON C.ID = EC.COTA_ID ");
		
		sql.append("                  WHERE EC.ESTUDO_ID = :ESTUDO_ID) REP ");

		
		sql.append("         WHERE REP.REPARTE BETWEEN :DE AND :ATE GROUP BY NUMERO_COTA) TES ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("ESTUDO_ID", estudoId);
		
		query.setParameter("DE", faixaDe);
		
		query.setParameter("ATE", faixaAte);
		
		if (listaIdEdicaoBase !=null && !listaIdEdicaoBase.isEmpty()){
			
		    query.setParameterList("EDICOES_BASES", listaIdEdicaoBase);
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(HistogramaPosEstudoAnaliseFaixaReparteDTO.class));

		HistogramaPosEstudoAnaliseFaixaReparteDTO resultado = (HistogramaPosEstudoAnaliseFaixaReparteDTO) query.uniqueResult();
		
		return resultado;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> obterListaIdProdEdicoesBaseEstudo(Long idEstudo) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT PRODUTO_EDICAO_ID FROM ESTUDO_PRODUTO_EDICAO_BASE WHERE ESTUDO_ID = :ESTUDO_ID ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("ESTUDO_ID", idEstudo);
		
		return (List<Long>) query.list();
	}
}
