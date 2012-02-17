package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;

public interface FornecedorService {

	List<Fornecedor> obterFornecedores();
	
	/**
	 * Busca os fornecedores de acordo com a permissão
	 * de balanceamento de matriz de lançamento e a situação do fornecedor
	 * @param permiteBalanceamento se o fornecedor permite ou não balanceamento
	 * @param situacoes filtra os fornecedores de acordo com a situação
	 * @return lista de fornecedores que atendem os parâmtros de pesquisa
	 */
	List<Fornecedor> obterFornecedores(boolean permiteBalanceamento,
			SituacaoCadastro... situacoes);
	
}
