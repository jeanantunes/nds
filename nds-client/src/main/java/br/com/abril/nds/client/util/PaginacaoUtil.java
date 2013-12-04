package br.com.abril.nds.client.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.NullComparator;

import br.com.abril.nds.dto.filtro.FiltroDTO;
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

		while (posicaoInicial > posicaoFinal) {
			
			posicaoInicial = posicaoInicial - paginacao.getQtdResultadosPorPagina();
			
			paginacao.setPaginaAtual(paginacao.getPaginaAtual() - 1);
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
	public static <T extends Object> Collection<T> ordenarEmMemoria(List<T> listaAOrdenar,
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
		
		Collections.sort(listaAOrdenar, new BeanComparator(nomeAtributoOrdenacao, new NullComparator()));

		if (Ordenacao.DESC.equals(ordenacao)) {

			Collections.reverse(listaAOrdenar);
		}
		
		return listaAOrdenar;
	}
	
	/**
	 * Armazena na sessão a quantidade de registros retornados
	 * na pesquisa para controle de paginação.
	 * 
	 * @param session - sessão
	 * @param atributoSessao - nome do atributo na sessão
	 * @param qtdRegistros - quantidade de registros
	 */
	public static void armazenarQtdRegistrosPesquisa(HttpSession session,
			  						  		 		 String atributoSessao, int qtdRegistros) {

		session.setAttribute(atributoSessao, qtdRegistros);
	}
	
	/**
	 * Atualiza a quantidade de registros retornados na 
	 * pesquisa para controle de paginação.
	 * 
	 * @param session - sessão
	 * @param atributoSessao - nome do atributo na sessão
	 */
	public static void atualizarQtdRegistrosPesquisa(HttpSession session,
			   								   		 String atributoSessao) {
		
		Integer qtdRegistrosPesquisaPagina =
			(Integer) session.getAttribute(atributoSessao);
		
		if (qtdRegistrosPesquisaPagina != null) {
			
			qtdRegistrosPesquisaPagina -=  1;
			
			session.setAttribute(atributoSessao, qtdRegistrosPesquisaPagina);
		}
	}
	
	/**
	 * Calcula a página atual e armazena no VO de paginação.
	 * 
	 * @param session - sessão
	 * @param atributoSessao - nome do atributo na sessão
	 * @param filtroAtual
	 * @param filtroSessao
	 */
	public static void calcularPaginaAtual(HttpSession session,
										   String atributoQtdRegistrosSessao,
										   String atributoFiltroSessao,
										   FiltroDTO filtroAtual,
										   FiltroDTO filtroSessao) {
	
		Integer qtdRegistrosPesquisaPagina =
			(Integer) session.getAttribute(atributoQtdRegistrosSessao);
		
		Integer paginaAtual = filtroAtual.getPaginacao().getPaginaAtual();
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
		
			filtroAtual.getPaginacao().setPaginaAtual(1);
			
			qtdRegistrosPesquisaPagina = null;
		}		
		
		if (!paginaAtual.equals(1) && qtdRegistrosPesquisaPagina != null
				&& qtdRegistrosPesquisaPagina.equals(0)) {
			
			paginaAtual -= 1;
			
			filtroAtual.getPaginacao().setPaginaAtual(paginaAtual);
		}
		
		session.setAttribute(atributoFiltroSessao, filtroAtual);
	}		

}
