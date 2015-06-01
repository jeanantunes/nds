package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaRotaRoteiroDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.service.exception.RelationshipRestrictionException;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Interface que define as regras de acesso a serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Box }  
 * 
 * @author Discover Technology
 */
public interface BoxService {

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
	 * @return Quantidade de registros
	 */
	public abstract Long quantidade(Integer codigoBox, TipoBox tipoBox);
	
	
	/**
	 * Busca os Box respeitando as restricoes parametrizadas.
 	 * @param codigoBox Código do Box
	 * @param tipoBox Tipo do Box {@link TipoBox}
	 * @param orderBy nome do campo para compor a ordenação
	 * @param ordenacao tipo da ordenação
	 * @param initialResult resultado inicial
	 * @param maxResults numero maximo de resultados
	 * @return
	 */
	public abstract List<Box> busca(Integer codigoBox, TipoBox tipoBox, String orderBy,
			Ordenacao ordenacao, Integer initialResult, Integer maxResults);
	/**
	 * Atualiza a entidade box do repositorio de dados
	 * @param entity
	 * @return o Box
	 * @throws UniqueConstraintViolationException Lançada caso o codigo do box ja esteja em uso.
	 */
	public abstract Box merge(Box entity) throws UniqueConstraintViolationException,RelationshipRestrictionException;
	
	/**
	 * Busca o Box por id;
	 * @param id
	 * @return o Box
	 */
	public abstract Box buscarPorId(Long id);
	/**
	 * Remove o box do repositorio de dados
	 * @param id
	 * @throws RelationshipRestrictionException Lança quando o box em questão está relacionado há Cota ou Roteiro.
	 */
	public abstract void remover(Long id)throws RelationshipRestrictionException;
	
	
	/**
	 * @see BoxRepository#obtemCotaRotaRoteiro(long)
	 */
	public abstract List<CotaRotaRoteiroDTO> obtemCotaRotaRoteiro(long id, String sortname, String sortorder);
	
	/**
	 * Busca lista de Box por tipo;
	 * @param list
	 * @return List<Box> 
	 */
	public List<Box> buscarPorTipo(List<TipoBox> list);
	
	/**
	 * Retorna todos os boxes, se tipo for informado retorna todos do tipo.
	 * @param tipoBox
	 * @return
	 */
	List<Box> buscarTodos(TipoBox tipoBox);
	
	/**
	 * Verifica se o {@link Box} está associado a {@link Cota} ou {@link Roteiro} 
	 * @param id id do {@link Box}
	 * @return
	 */
	public abstract boolean hasAssociacao(long id); 

	/**
	 * Obtém box ligado a cota
	 * @param idCota
	 * @return
	 */
	public Box obterBoxPorCota(Integer numeroCota);

	/**
	 * Obtem lista de Box por intervalo de Código
	 * @param codigoBoxDe
	 * @param codigoBoxAte
	 * @return List<Box>
	 */
	List<Box> obterBoxPorIntervaloCodigo(Integer codigoBoxDe, Integer codigoBoxAte);
	
	/**
	 * Busca lista de Box por Rota
	 * @param rotaId
	 * @return List<Box>
	 */
	List<Box> buscarBoxPorRota(Long rotaId);
	
	/**
	 * Busca lista de Box por Roteiro
	 * @param roteiroId
	 * @return List<Box>
	 */
	List<Box> buscarBoxPorRoteiro(Long roteiroId);

	String obterDescricaoBoxPorCota(Integer numeroCota);
}