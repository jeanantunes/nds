package br.com.abril.icd.axis.util;

import java.util.Locale;

/**
 * Classe utilitária para constantes comuns no sistema.
 * 
 * @author Discover Technology
 *
 */
public abstract class Constantes {
	
	public static final Locale LOCALE_EN_US = new Locale("en", "US");
	
	public static final Locale LOCALE_PT_BR = new Locale("pr", "BR");
	
	public static final String DATE_PATTERN_PT_BR = "dd/MM/yyyy";
	
	public static final String DAY_MONTH_PT_BR = "dd/MM";
	
	public static final String FORMATO_DATA_ARQUIVO_CNAB = "ddMMyy";
	
	public static final String DATE_TIME_PATTERN_PT_BR = "dd/MM/yyyy HH:mm:ss";
	
	public static final String DATE_PATTERN_PT_BR_FOR_FILE = "dd-MM-yyyy";
	
	public static final String[] EXTENSOES_IMAGENS = new String[]{".jpg", ".jpeg", ".png", ".bmp"};
	
	public static final String PARAM_MSGS = "mensagens";
	
	public static final String TIPO_MSG_SUCCESS = "success";
	
	public static final String TIPO_MSG_WARNING = "warning";
	
	public static final String TIPO_MSG_ERROR = "error";
	
	public static final String UPLOAD_AJAX_REQUEST_ATTRIBUTE = "formUploadAjax";
	
	public static final String CPF = "cpf";
	
	public static final String CNPJ = "cnpj";
	
	public static final String DATA_FMT_PESQUISA_MYSQL = "YYYY-MM-DD";
	
	public static final Integer MAX_CODIGO_INTERFACE_FORNCECEDOR_MANUAL = 9999;
	
	public static final Integer QTD_MAX_REGISTROS_AUTO_COMPLETE = 50;
	
	public static final String NOME_PROPERTIES_NDS_CLIENT = "nds-client.properties";
	
	public static final String COMPOSICAO_COBRANCA_CREDITO = "C";
	
	public static final String COMPOSICAO_COBRANCA_DEBITO = "D";

	public static final int TAMANHO_CAMPO_PRODUTO_CONSULTA = 8;
	
	public static final String[] MAILS_RECEBIMENTO_INTERFACE = new String[]{"novodistrib@dgb.com.br"};
	
}


