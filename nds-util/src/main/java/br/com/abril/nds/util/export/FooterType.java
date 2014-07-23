package br.com.abril.nds.util.export;

/**
 * Enumera o tipo de operação que será realizada em cima do campo anotado com {@link Footer}
 * 
 * @author Discover Technology
 *
 */
public enum FooterType {
	
	/**
	 * Referencia a contagem de dados.
	 */
	COUNT,
	
	/**
	 * Referencia a sumarização de dados.
	 */
	SUM,
	
	/**
	 * Referencia a média de dados.
	 */
	AVG

}
