package br.com.abril.nds.util;

import org.apache.commons.lang.StringUtils;

import com.google.gson.GsonBuilder;

/**
 * Classe utilitária para CouchDB.
 * 
 * @author Discover Technology
 *
 */
public abstract class CouchDBUtil {
	
	/**
	 * Nome do banco de dados de usuários
	 */
	public static final String DB_NAME_USERS = "_users";
	
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
	
	/**
	 * Obtém uma instância do Gson Builder com adaptadores de data registrados.
	 * 
	 * @return {@link GsonBuilder}
	 */
	public static GsonBuilder getGsonBuilderForDate() {
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		
		gsonBuilder.setDateFormat(Constantes.DATE_TIME_PATTERN_PT_BR);
		
		return gsonBuilder;
	}

}
