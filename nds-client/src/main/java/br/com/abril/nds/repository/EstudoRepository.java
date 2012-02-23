package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.planejamento.Estudo;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.Estudo}  
 * 
 * @author Discover Technology
 *
 */
public interface EstudoRepository extends Repository<Estudo, Long> {
	
	/**
	 * Obtém o estudo relacionado ao lançamento mais próximo
	 * da data de referência e produto passados como parâmetro.
	 * 
	 * @param data - data de referencia
	 * @param codigoProduto - código do produto
	 * @param numeroEdicao - número da edição
	 * 
	 * @return {@link Estudo}
	 */
	Estudo obterEstudoDoLancamentoMaisProximo(Date dataReferencia, String codigoProduto, Long numeroEdicao);

}
