package br.com.abril.nds.util;

import java.util.Properties;

import org.hibernate.Query;
import org.hibernate.internal.TypeLocatorImpl;
import org.hibernate.type.EnumType;
import org.hibernate.type.Type;
import org.hibernate.type.TypeResolver;

import br.com.abril.nds.vo.PaginacaoVO;

public class QueryUtil {



    public static void addPaginacao(Query query, PaginacaoVO paginacaoVO) {
    	
    	if( paginacaoVO != null && paginacaoVO.getQtdResultadosPorPagina()!=null && paginacaoVO.getPosicaoInicial()!=null) {
			query.setFirstResult(paginacaoVO.getPosicaoInicial());
			query.setMaxResults(paginacaoVO.getQtdResultadosPorPagina());
		}
    }
    
    public static void addOrderBy(StringBuilder query, PaginacaoVO paginacaoVO, String colunaDefault, String... colunasValidas) {
    	
    	if(paginacaoVO==null || paginacaoVO.getOrdenacao()==null || paginacaoVO.getSortColumn()==null)
    		return;
    
    	String order = paginacaoVO.getOrdenacao().name();
    	String column = paginacaoVO.getSortColumn();
    	
    	String colunaValida = null;
    	
    	for(String coluna : colunasValidas) {
    		
    		if(coluna.equals(column)) {
    			colunaValida = column;
    			break;
    		}
    	}
    	
    	if(colunaValida == null && colunaDefault == null)
    		return;
    	
    	if (colunaValida == null) {
    		colunaValida = colunaDefault;
    	}
    	
    	query.append(" order by ");
    	query.append(colunaValida); 
    	query.append(" ");
    	query.append(order);
    	
    }
    
    public static Type obterTypeEnum(final Class<?> clazz){
    	
   	 final Properties params = new Properties();
        
        params.put("enumClass", clazz.getCanonicalName());
        params.put("type", "12");
        
        final Type enumType = new TypeLocatorImpl(new TypeResolver()).custom(EnumType.class, params);
        
        return enumType;
   }
    
}
