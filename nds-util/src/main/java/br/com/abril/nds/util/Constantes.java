package br.com.abril.nds.util;

/**
 * Classe utilitária para constantes comuns no sistema.
 * 
 * @author Discover Technology
 *
 */
public abstract class Constantes {
	
	public static final String DATE_PATTERN_PT_BR = "dd/MM/yyyy";
	
	public static final String[] EXTENSOES_IMAGENS = new String[]{".jpg", ".jpeg", ".png", ".bmp"};
	
	public static final String PARAM_MSGS = "mensagens";
	
	public static final String TIPO_MSG_SUCCESS = "success";
	
	public static final String TIPO_MSG_WARNING = "warning";
	
	public static final String TIPO_MSG_ERROR = "error";
	
	/**
	 * Constante que representa o nome do atributo com a lista de endereços 
	 * armazenado na sessão para serem persistidos na base. 
	 */
	public static final String ATRIBUTO_SESSAO_LISTA_ENDERECOS_SALVAR = "listaEnderecosSalvarSessao";

	/**
	 * Constante que representa o nome do atributo com a lista de endereços 
	 * armazenado na sessão para serem persistidos na base. 
	 */
	public static final String ATRIBUTO_SESSAO_LISTA_ENDERECOS_REMOVER = "listaEnderecosRemoverSessao";
	
}


