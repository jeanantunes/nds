package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.seguranca.Permissao}
 * 
 * @author Discover Technology
 *
 */
public interface PermissaoRepository extends Repository<Permissao,Long> {

	/**
	 * Retorna a lista de permissões organizada
	 * @param nome
	 * @param descricao
	 * @param orderBy
	 * @param ordenacao
	 * @param initialResult
	 * @param maxResults
	 * @return List<Permissao>
	 */
	public List<Permissao> busca(String nome, String descricao, String orderBy, Ordenacao ordenacao, int initialResult, int maxResults);

	/**
	 * Retorna a quantidade de registros da busca realizada
	 * @param nome
	 * @param descricao
	 * @return Long
	 */
	public Long quantidade(String nome, String descricao);

}
