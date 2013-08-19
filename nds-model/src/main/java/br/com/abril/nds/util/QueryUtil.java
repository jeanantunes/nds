package br.com.abril.nds.util;

import org.hibernate.Query;

import br.com.abril.nds.vo.PaginacaoVO;

public class QueryUtil {



    public static void addPaginacao(Query query, PaginacaoVO paginacaoVO) {
    	
    	if( paginacaoVO != null && paginacaoVO.getQtdResultadosPorPagina()!=null && paginacaoVO.getPosicaoInicial()!=null) {
			query.setFirstResult(paginacaoVO.getPosicaoInicial());
			query.setMaxResults(paginacaoVO.getQtdResultadosPorPagina());
		}
    }
    
    public static void addOrderBy(StringBuilder query, PaginacaoVO paginacaoVO, String... colunasValidas) {
    	
    	if(paginacaoVO==null || paginacaoVO.getOrdenacao()==null || paginacaoVO.getSortColumn()==null)
    		return;
    
    	String order = paginacaoVO.getOrdenacao().name();
    	String column = paginacaoVO.getSortColumn();
    	
    	String colunaValida = null;
    	
    	for(String coluna : colunasValidas) {
    		
    		if(coluna.equals(column))
    			colunaValida = column;
    	}
    	
    	if(colunaValida==null)
    		return;
    	
    	query.append(" order by " + colunaValida + " " + order);
    	
    }
    
}
