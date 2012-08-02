package br.com.abril.nds.util;

import org.apache.commons.lang.StringUtils;

/**
 * Classe utilitária para CouchDB.
 * 
 * @author Discover Technology
 *
 */
public abstract class CouchDBUtil {
	
	/**
	 * Prefixo do nome dos bancos de dados
	 */
	private static final String DB_NAME_PREFIX = "db_";
	
	/**
	 * Obtém o nome do banco de dados de integração do distribuidor.
	 * 
	 * @param codigoDistribuidor - código do distribuidor
	 */
	public static String obterNomeBancoDeDadosIntegracaoDistribuidor(String codigoDistribuidor) {
		
		if (codigoDistribuidor == null || codigoDistribuidor.trim().isEmpty()) {
			
			return codigoDistribuidor;
		}
		
		return DB_NAME_PREFIX + StringUtils.leftPad(codigoDistribuidor, 8, "0");
	}

}
