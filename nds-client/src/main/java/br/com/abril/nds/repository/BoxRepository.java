package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Box }  
 * 
 * @author Discover Technology
 */
public interface BoxRepository extends Repository<Box,Long> {
	
	/**
	 * Retorna uma lista de boxes referente um determinado produto.
	 * @param codigoProduto - código do propduto
	 * @return List<Box>
	 */
	List<Box> obterBoxPorProduto(String codigoProduto);
	
	/**
	 * Obtem a quantidade de registros respeitando as restricoes parametrizadas.
	 * @param codigoBox Código do Box
	 * @param tipoBox Tipo do Box {@link TipoBox}
	 * @param postoAvancado se restringe apenas a postos avançados
	 * @return Quantidade de registros
	 */
	public abstract Long quantidade(String codigoBox, TipoBox tipoBox, boolean postoAvancado);
	
	/**
	 * Busca os Box respeitando as restricoes parametrizadas.
 	 * @param codigoBox Código do Box
	 * @param tipoBox Tipo do Box {@link TipoBox}
	 * @param postoAvancado se restringe apenas a postos avançados
	 * @param orderBy nome do campo para compor a ordenação
	 * @param ordenacao tipo da ordenação
	 * @param initialResult resultado inicial
	 * @param maxResults numero maximo de resultados
	 * @return
	 */
	public abstract List<Box> busca(String codigoBox, TipoBox tipoBox, boolean postoAvancado,
			String  orderBy, Ordenacao ordenacao, int initialResult, int maxResults);
}
