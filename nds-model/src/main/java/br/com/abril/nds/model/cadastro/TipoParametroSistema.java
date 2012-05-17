package br.com.abril.nds.model.cadastro;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoParametroSistema {

	PATH_IMAGENS_CAPA, 
	
	PATH_IMAGENS_PDV, 
	
	/**
	 * Número de dias permitido para lançamento de uma
	 * diferença de estoque do tipo FALTA DE.
	 */
	NUMERO_DIAS_PERMITIDO_LANCAMENTO_FALTA_DE,
	
	/**
	 * Número de dias permitido para lançamento de uma
	 * diferença de estoque do tipo FALTA EM.
	 */
	NUMERO_DIAS_PERMITIDO_LANCAMENTO_FALTA_EM,
	
	/**
	 * Número de dias permitido para lançamento de uma
	 * diferença de estoque do tipo SOBRA DE.
	 */
	NUMERO_DIAS_PERMITIDO_LANCAMENTO_SOBRA_DE,
	
	/**
	 * Número de dias permitido para lançamento de uma
	 * diferença de estoque do tipo SOBRA EM.
	 */
	NUMERO_DIAS_PERMITIDO_LANCAMENTO_SOBRA_EM,
	
	/**
	 * SMTP de autenticação da conta de e-mail do sistema
	 */
	EMAIL_PROTOCOLO,
	
	/**
	 * Senha de autenticação da conta de e-mail do sistema
	 */
	EMAIL_SENHA,
	
	/**
	 * Número da Porta de autenticação da conta de e-mail do sistema
	 */
	EMAIL_PORTA,
	
	/**
	 * Nome do host de autenticação da conta de e-mail do sistema
	 */
	EMAIL_HOST,
	
	/**
	 * Usúario de de autenticação da conta de e-mail do sistema
	 */
	EMAIL_USUARIO,
	
	
	PATH_LOGO_SISTEMA,
	
	CNPJ,
	RAZAO_SOCIAL,
	//EMAIL,	// estou usando o EMAIL_USUARIO
	UF,
	
	CODIGO_DISTRIBUIDOR_DINAP,
	CODIGO_DISTRIBUIDOR_FC,
	
	LOGIN_DISTRIBUIDOR,
	SENHA_DISTRIBUIDOR,
	
	VERSAO_SISTEMA,
	
	PATH_INTERFACE_CE_EXPORTACAO,
	PATH_INTERFACE_PRODIN_IMPORTCAO,
	PATH_INTERFACE_PRODIN_EXPORTACAO,
	PATH_INTERFACE_MDC_IMPORTCAO,
	PATH_INTERFACE_MDC_EXPORTACAO,
	PATH_INTERFACE_BANCAS_EXPORTACAO,
	PATH_INTERFACE_GFS_IMPORTCAO,
	PATH_INTERFACE_GFS_EXPORTACAO,
	PATH_INTERFACE_NFE_IMPORTCAO,
	PATH_INTERFACE_NFE_EXPORTACAO,
	
	NFE_DPEC,	// valor TRUE ou FALSE;
	DATA_OPERACAO_CORRENTE;		// formato: dd/MM/yyyy
}