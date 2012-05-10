package br.com.abril.nds.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;

public interface FornecedorService {

	Fornecedor obterFornecedorUnico(String codigoProduto);
	
	List<Fornecedor> obterFornecedores();
	
	List<Fornecedor> obterFornecedoresAtivos();
	
	List<Fornecedor> obterFornecedores(String cnpj);
	
	/**
	 * Busca os fornecedores de acordo com a permissão
	 * de balanceamento de matriz de lançamento e a situação do fornecedor
	 * @param permiteBalanceamento se o fornecedor permite ou não balanceamento
	 * @param situacoes filtra os fornecedores de acordo com a situação
	 * @return lista de fornecedores que atendem os parâmtros de pesquisa
	 */
	List<Fornecedor> obterFornecedores(boolean permiteBalanceamento,
			SituacaoCadastro... situacoes);
	
	/**
	 * Obtém os fornecedores de acordo com o código produto.
	 * 
	 * @param codigoProduto - código do produto
	 * @param grupoFornecedor - grupo do fornecedor
	 * 
	 * @return lista de fornecedores
	 */
	List<Fornecedor> obterFornecedoresPorProduto(String codigoProduto,
												 GrupoFornecedor grupoFornecedor);
	
	Fornecedor obterFornecedorPorId(Long id);

	List<TelefoneAssociacaoDTO> buscarTelefonesFornecedor(Long idFornecedor, Set<Long> idsIgnorar);
	
	List<Fornecedor> obterFornecedoresCota(Long idCota);
	
	List<Fornecedor> obterFornecedores(Long idCota);
	
	void salvarFornecedorCota(List<Long> fornecedores, Long idCota);
	
}