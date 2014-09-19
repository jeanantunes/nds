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
	
	private String obterRestricaoCaseFaixaReparte(Integer[][] faixas) {
		
		StringBuilder retorno = new StringBuilder(" CASE ");
		
		for (Integer[] faixa : faixas) {

			retorno.append(" WHEN ");
			retorno.append(String.format(" EC.REPARTE BETWEEN %s AND %s ", faixa[0], faixa[1]));
			retorno.append(" THEN ");
			retorno.append("'");
			retorno.append(faixa[0]);
			retorno.append(" a ");
			retorno.append(faixa[1]);
			retorno.append("'");
		}
		
		retorno.append(" END ");

		return retorno.toString();
	}
	
	private String obterRestricaoWhereFaixaReparte(Integer[][] faixas) {
		
		StringBuilder retorno = new StringBuilder(" AND ( ");
		
		int i = 0;
		
		for (Integer[] faixa : faixas) {

			if ( i != 0) {
			
				retorno.append(" OR ");
			}
			
			retorno.append(String.format(" EC.REPARTE BETWEEN %s AND %s ", faixa[0], faixa[1]));
			
			i++;
		}
		
		retorno.append(" ) ");

		return retorno.toString();
	}
	
	@Override
	public HistogramaPosEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo(Integer estudoId, List<Long> listaIdEdicaoBase, Integer[] faixa) {
		
		SQLQuery query = this.obterQueryHistogramaPosEstudo(estudoId, listaIdEdicaoBase, true, faixa);
		
		query.setMaxResults(1);
		
		return (HistogramaPosEstudoAnaliseFaixaReparteDTO) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<HistogramaPosEstudoAnaliseFaixaReparteDTO> obterHistogramaPosEstudo(Integer estudoId, List<Long> listaIdEdicaoBase, Integer[]... faixas) {

		SQLQuery query = this.obterQueryHistogramaPosEstudo(estudoId, listaIdEdicaoBase, false, faixas);
		
		return query.list();
	}

	
	private SQLQuery obterQueryHistogramaPosEstudo(Integer estudoId, List<Long> listaIdEdicaoBase, boolean filtrarFaixaEspecifica, Integer[]... faixas) {
		
		boolean isEdicoesBaseEspecificas = (listaIdEdicaoBase !=null && !listaIdEdicaoBase.isEmpty());
		
        String tuplaSqlEdicoesBase = " ";
        
        if (isEdicoesBaseEspecificas){
		    
        	tuplaSqlEdicoesBase = " AND EPE.PRODUTO_EDICAO_ID IN (:EDICOES_BASES) ";
		}
        
		StringBuilder sql = new StringBuilder();
		
		sql.append("  SELECT ");
		
		sql.append("  faixaReparte, ");
		
		sql.append("  SUM(REPARTE) reparteTotal, ");
		
		sql.append("  AVG(REPARTE) reparteMedio, ");
		
		if (isEdicoesBaseEspecificas){
		
		    sql.append("   SUM(VENDA_MEDIA) vendaNominal, ");
		
		    sql.append("   SUM(VENDA_MEDIA) / COUNT(DISTINCT NUMERO_COTA) vendaMedia, ");
		}
		
		sql.append("  COUNT(DISTINCT NUMERO_COTA) qtdCotas, ");
		
		sql.append("  SUM(RECEBIDO) qtdRecebida, ");
		
		if (isEdicoesBaseEspecificas){
		
		    sql.append("   (SUM(VENDA_MEDIA) / SUM(REPARTE)) * 100 vendaPercent, ");
		
		    sql.append("   (SUM(REPARTE) - SUM(VENDA_MEDIA)) / COUNT(*) encalheMedio, ");
		
		    sql.append("   COUNT(IS_REPARTE_MENOR_VENDA) qtdCotaPossuemReparteMenorVenda,  ");
		}
		
		sql.append("  (SUM(REPARTE) / (SELECT SUM(REPARTE) FROM estudo_cota_gerado WHERE ESTUDO_ID = :ESTUDO_ID) * 100) participacaoReparte, ");
		
		sql.append("  group_concat(IS_REPARTE_MENOR_VENDA) numeroCotasStr ");
		
		sql.append("  FROM (SELECT ");
		
		sql.append("        REP.faixaReparte, ");
		
		sql.append("        REP.ID, ");
		
		sql.append("        REP.NUMERO_COTA, ");
		
		sql.append("        SUM(REP.REPARTE) REPARTE, ");
		
		sql.append("        SUM(REP.RECEBIDO) AS RECEBIDO, ");
		
		if (isEdicoesBaseEspecificas){
		    
		    sql.append("    SUM(REP.VENDA_MEDIA) AS VENDA_MEDIA, ");
		}

		sql.append("        (CASE WHEN SUM(REP.REPARTE) < SUM(VENDA_MEDIA) THEN REP.NUMERO_COTA ELSE NULL END) IS_REPARTE_MENOR_VENDA ");
		
		sql.append("        FROM (SELECT  						" );

		sql.append(               this.obterRestricaoCaseFaixaReparte(faixas));
		
		sql.append("              AS faixaReparte, ");

		sql.append("		 	  C.ID, 			");
		
		sql.append("              C.NUMERO_COTA, ");
		
		sql.append("              EC.REPARTE, ");
		
		
		sql.append("              (SELECT SUM(EPE.QTDE_RECEBIDA) "); 
		
		sql.append("               FROM estoque_produto_cota EPE "); 
		
		sql.append("               WHERE EPE.COTA_ID = C.ID ");
		
        sql.append(                tuplaSqlEdicoesBase);
		
		sql.append("              ) RECEBIDO,  ");
		
			
	    sql.append("              (SELECT AVG(EPE.QTDE_RECEBIDA - EPE.QTDE_DEVOLVIDA) "); 
			
		sql.append("               FROM estoque_produto_cota EPE "); 
			
		sql.append("               WHERE EPE.COTA_ID = C.ID ");
			
	    sql.append(                tuplaSqlEdicoesBase);
			
		sql.append("              ) VENDA_MEDIA  ");
		
		
		sql.append("              FROM estudo_cota_gerado EC ");
		
		sql.append("              JOIN COTA C ON C.ID = EC.COTA_ID ");
		
		sql.append("              WHERE EC.ESTUDO_ID = :ESTUDO_ID ");
		
		sql.append("              AND EC.REPARTE IS NOT NULL 	  ");
		
		if (filtrarFaixaEspecifica) {
		
			sql.append(this.obterRestricaoWhereFaixaReparte(faixas));
		}
		
		sql.append("	          ) REP ");

		sql.append("        GROUP BY NUMERO_COTA) TES ");
		
		sql.append(" GROUP BY faixaReparte");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("ESTUDO_ID", estudoId);
		
		if (isEdicoesBaseEspecificas){
			
		    query.setParameterList("EDICOES_BASES", listaIdEdicaoBase);
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(HistogramaPosEstudoAnaliseFaixaReparteDTO.class));

		return query;
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
