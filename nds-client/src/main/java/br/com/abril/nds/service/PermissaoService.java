package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Interface relacionada aos serviços de permissões do sistema
 * @author InfoA2
 */
public interface PermissaoService {

	/**
	 * Retorna a lista de permissões do sistema
	 * @return List<Permissao>
	 */
	public List<Permissao> busca(String nome, String descricao, String orderBy, Ordenacao ordenacao, int initialResult, int maxResults);

	/**
	 * Retorna o valor total da quantidade de registros retornados na busca
	 * @param nome
	 * @param descricao
	 * @return
	 */
	public Long quantidade(String nome, String descricao);
	
}
