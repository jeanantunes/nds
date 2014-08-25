package br.com.abril.nds.client.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class LogFuncionalUtil {
	
	private static final Marker marker = MarkerFactory.getMarker("MARKER_LOG_FUNCIONAL");
	
	private LogFuncionalUtil(){}
	
	public enum StatusLog {
		
		INCIADO("Iniciado"),
		FINALIZADO("Finalizado");
		
		private String descricao;
		
		private StatusLog(String descricao) {
			
			this.descricao = descricao;
		}
		
		@Override
		public String toString() {
		
			return this.descricao;
		}
	}
	
	public static void logar(String nomeFuncionalidade, String metodoExecutado, StatusLog statusLog,String nomeUsuario, Class<?> clazz){
		
		final Logger logger = LoggerFactory.getLogger(clazz);
		
		logger.warn(marker,"[Usuario] {} - [Funcionalidade]{} - [Método]{} - [Status]{}",nomeUsuario,nomeFuncionalidade,metodoExecutado,statusLog.toString());
	}
	
	public static void logar(String nomeFuncionalidade, String metodoExecutado, StatusLog statusLog,String nomeUsuario, Class<?> clazz,Long tempoExecucao){
		
		final Logger logger = LoggerFactory.getLogger(clazz);
		
		logger.warn(marker,"[Usuario] {} - [Funcionalidade]{} - [Método]{} - [Status]{} - [Tempo Processamento] {}",nomeUsuario,nomeFuncionalidade,metodoExecutado,statusLog.toString(),tempoExecucao);
	}
}
