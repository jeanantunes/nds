package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFornecedorDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;

/**
 * Interface que define as regras de implementação referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}  
 * 
 * @author william.machado
 *
 */
public interface FornecedorRepository extends Repository<Fornecedor, Long> {

	List<Fornecedor> obterFornecedoresAtivos();
	
	List<Fornecedor> obterFornecedores(String cnpj);
	
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

	
	/**
	 * Obtém a lista de fornecedores relativos a um produto.
	 * 
	 * @param codigoProduto
	 * @param grupoFornecedor
	 * 
	 * @return List - Fornecedor.
	 */
	public List<Fornecedor> obterFornecedoresDeProduto(String codigoProduto,
													   GrupoFornecedor grupoFornecedor);
	
	
	List<Fornecedor> obterFornecedoresCota(Long idCota);
	
	List<Fornecedor> obterFornecedoresNaoReferenciadosComCota(Long idCota);

	/**
	 * Método que retorna uma lista de fornecedores com base no filtro recebido. 
	 * 
	 * @param filtroConsultaFornecedor - Filtro de Fornecedores.
	 * 
	 * @return List<FornecedorDTO>
	 */
	List<FornecedorDTO> obterFornecedoresPorFiltro(FiltroConsultaFornecedorDTO filtroConsultaFornecedor);
	

	/**
	 * Método que retorna a contagem de fornecedores com base no filtro recebido. 
	 * 
	 * @param filtroConsultaFornecedor - Filtro de Fornecedores.
	 * 
	 * @return Long
	 */
	Long obterContagemFornecedoresPorFiltro(FiltroConsultaFornecedorDTO filtroConsultaFornecedor);
}
