package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroControleAprovacaoDTO;
import br.com.abril.nds.model.aprovacao.Aprovacao;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.ProdutoEdicao}
 * 
 * @author Discover Technology
 */
public interface ControleAprovacaoService {

	/**
	 * Obtém as aprovações de acordo com os parâmetros informados.
	 * 
	 * @param filtro - filtro para a pesquisa
	 * 
	 * @return {@link List<Aprovacao>}
	 */
	List<Aprovacao> obterAprovacoes(FiltroControleAprovacaoDTO filtro);
	
	/**
	 * Obtém o total de aprovações de acordo com os parâmetros informados.
	 * 
	 * @param filtro - filtro para a pesquisa
	 * 
	 * @return {@link List<Aprovacao>}
	 */
	Long obterTotalAprovacoes(FiltroControleAprovacaoDTO filtro);
	
}
