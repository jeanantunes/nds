package br.com.abril.nds.client.util;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe utilitária que define padrões de paginação.
 * 
 * @author Discover Technology
 *
 */
public class PaginacaoUtil {

	/**
	 * Efetua a paginação e ordenação da lista em memória.
	 * 
	 * @param lista - lista com os dados a paginar e ordenar
	 * @param paginacao - dados da paginação
	 * @param nomeAtributoOrdenacao - nome do atributo para ordenação
	 * 
	 * @return Lista paginada e ordenada
	 */
	public static <T extends Object> List<T> paginarEOrdenarEmMemoria(List<T> lista,
																	  PaginacaoVO paginacao,
																	  String nomeAtributoOrdenacao) {
		
		if (paginacao == null) {
			
			throw new IllegalArgumentException("Dados da paginação nulos!");
		}
		
		ordenarEmMemoria(lista, paginacao.getOrdenacao(), nomeAtributoOrdenacao);
		
		return paginarEmMemoria(lista, paginacao);
	}
	
	/**
	 * Efetua a paginação de uma lista em memória.
	 * 
	 * @param listaAPaginar - lista a ser paginada
	 * @param paginacao - dados da paginação
	 * 
	 * @return Lista paginada
	 */
	public static <T extends Object> List<T> paginarEmMemoria(List<T> listaAPaginar,
															  PaginacaoVO paginacao) {

		if (listaAPaginar == null || listaAPaginar.isEmpty()) {
			
			return listaAPaginar;
		}
		
		if (paginacao == null
				|| paginacao.getQtdResultadosPorPagina() == null
				|| paginacao.getPosicaoInicial() == null) {
			
			throw new IllegalArgumentException("Dados da paginação nulos!");
		}
		
		Integer posicaoInicial = paginacao.getPosicaoInicial();

		Integer qtdeTotalRegistros = listaAPaginar.size();

		Integer posicaoFinal = qtdeTotalRegistros;

		if (qtdeTotalRegistros > paginacao.getQtdResultadosPorPagina()) {

			posicaoFinal = posicaoInicial
					+ paginacao.getQtdResultadosPorPagina();

			if (posicaoFinal > qtdeTotalRegistros) {

				posicaoFinal = qtdeTotalRegistros;
			}
		}

		return listaAPaginar.subList(posicaoInicial, posicaoFinal);
	}
	
	/**
	 * Efetua a ordenação de uma lista em memória.
	 * 
	 * @param listaAOrdenar - Lista que será ordenada.
	 * @param ordenacao - Define se a ordenação será ascendente ou descendente.
	 * @param nomeAtributoOrdenacao - nome do atributo que será usada para a ordenação.
	 * 
	 * @return Lista ordenada
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> List<T> ordenarEmMemoria(List<T> listaAOrdenar,
															  Ordenacao ordenacao,
															  String nomeAtributoOrdenacao) {
		
		if (listaAOrdenar == null || listaAOrdenar.isEmpty()) {
			
			return listaAOrdenar;
		}
		
		if (ordenacao == null) {
			
			throw new IllegalArgumentException("Tipo de ordenação nulo!");
		}
		
		if (nomeAtributoOrdenacao == null) {
			
			throw new IllegalArgumentException("Nome do atributo para ordenação nulo!");
		}
		
		Collections.sort(
			listaAOrdenar, new BeanComparator(nomeAtributoOrdenacao));

		if (Ordenacao.DESC.equals(ordenacao)) {

			Collections.reverse(listaAOrdenar);
		}
		
		return listaAOrdenar;
	}
	
}
