package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.estoque.Diferenca}.
 * 
 * @author Discover Technology
 *
 */
public interface DiferencaEstoqueRepository extends Repository<Diferenca, Long> {

	/**
	 * Obtém as diferenças de estoque (faltas/sobras) para lançamento
	 * de acordo com o filtro.
	 * 
	 * @param filtro - filtro de pesquisa
	 * 
	 * @return {@link List<Diferenca>}
	 */
	List<Diferenca> obterDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro);
	
	/**
	 * Obtém a quantidade total de registros de diferenças para lançamento.
	 * 
	 * @param filtro - filtro de pesquisa
	 * 
	 * @return Quantidade total de registros de diferenças para lançamento
	 */
	Long obterTotalDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro);
	
	/**
	 * Obtém as diferenças de estoque (faltas/sobras) para consulta
	 * de acordo com o filtro.
	 * 
	 * @param filtro - filtro de pesquisa
	 * 
	 * @return {@link List<Diferenca>}
	 */
	List<Diferenca> obterDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro);
	
	/**
	 * Obtém a quantidade total de registros de diferenças para consulta.
	 * 
	 * @param filtro - filtro de pesquisa
	 * @param dataLimiteLancamentoPesquisa - Data limite de lançamento para realizar a pesquisa
	 * 
	 * @return Quantidade total de registros de diferenças para consulta
	 */
	Long obterTotalDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro,
							  Date dataLimiteLancamentoPesquisa);
	
	/**
	 * Verifica se a diferença foi lançada automaticamente (pelo recebimento físico)
	 * 
	 * @param idDiferenca
	 * @return flag que indica se diferença foi lançada automaticamente
	 */
	boolean buscarStatusDiferencaLancadaAutomaticamente(Long idDiferenca);

	BigDecimal obterValorFinanceiroPorTipoDiferenca(TipoDiferenca tipoDiferenca);
	
	BigInteger obterQuantidadeTotalDiferencas(String codigoProduto, Long numeroEdicao,
											  TipoEstoque tipoEstoque, Date dataMovimento);
	
	boolean existeDiferencaPorNota(Long idProdutoEdicao, Date dataNotaEnvio,
			  					   Integer numeroCota);
	
	 /**
     * Obtém as diferenças lançadas da na data
     * 
     * @param data
     *            data para recuperação das diferenças
     * @return lista de diferenças lançadas na data
     */
	List<Diferenca> obterDiferencas(Date data);
	
}