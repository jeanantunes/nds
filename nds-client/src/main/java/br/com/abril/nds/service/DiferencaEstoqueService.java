package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.abril.nds.client.vo.RateioCotaVO;
import br.com.abril.nds.dto.DetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.estoque.Diferenca}  
 * 
 * @author Discover Technology
 *
 */
public interface DiferencaEstoqueService {
	
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
	 * Obtém as diferenças de estoque (faltas/sobras) para consulta
	 * de acordo com o filtro.
	 * 
	 * @param filtro - filtro de pesquisa
	 * 
	 * @return {@link List<Diferenca>}
	 */
	List<Diferenca> obterDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro);
	
	/**
	 * Obtém a quantidade total de registros de diferenças para lançamento.
	 * 
	 * @param filtro - filtro de pesquisa
	 * 
	 * @return Quantidade total de registros de diferenças para lançamento
	 */
	Long obterTotalDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro);
	
	/**
	 * Obtém a quantidade total de registros de diferenças para consulta.
	 * 
	 * @param filtro - filtro de pesquisa
	 * 
	 * @return Quantidade total de registros de diferenças para consulta
	 */
	Long obterTotalDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro);
	
	boolean verificarPossibilidadeExclusao(Long idDiferenca);

	void efetuarAlteracoes(Set<Diferenca> listaNovasDiferencas,
			 			   Map<Long, List<RateioCotaVO>> mapaRateioCotas,
						   FiltroLancamentoDiferencaEstoqueDTO filtroPesquisa,
						   Long idUsuario,Boolean isDiferencaNova);

	boolean validarDataLancamentoDiferenca(Date dataLancamentoDiferenca, Long idProdutoEdicao, TipoDiferenca tipoDiferenca);
	
	Diferenca obterDiferenca(Long id);
	
	Diferenca lancarDiferencaAutomatica(Diferenca diferenca);

	/**
	 * Método que retorna os detalhes de diferença por cota.
	 * 
	 * @param filtro
	 * 
	 * @return DetalheDiferencaCotaDTO
	 */
	DetalheDiferencaCotaDTO obterDetalhesDiferencaCota(FiltroDetalheDiferencaCotaDTO filtro);
	
	List<RateioCotaVO> obterRateiosCotaPorIdDiferenca(Long idDiferenca);
	
	void salvarLancamentosDiferenca(Set<Diferenca> listaNovasDiferencas,
			   Map<Long, List<RateioCotaVO>> mapaRateioCotas,
			   Long idUsuario,Boolean isDiferencaNova);
	
	/**
	 * Cancela as diferenças obtidas de acordo com o filtro informado.
	 * 
	 * @param filtro - filtro de pesquisa
	 * @param idUsuario - identificador do usuário
	 */
	void cancelarDiferencas(FiltroLancamentoDiferencaEstoqueDTO filtroPesquisa, Long idUsuario);

	void excluirLancamentoDiferenca(Long idDiferenca);
	
	BigInteger obterQuantidadeTotalDiferencas(String codigoProduto, Long numeroEdicao,
				 							  TipoEstoque tipoEstoque, Date dataMovimento);
	
	boolean existeDiferencaPorNota(Long idProdutoEdicao, Date dataNotaEnvio, Integer numeroCota);
	
	byte[] imprimirRelatorioFaltasSobras(Date dataMovimento) throws Exception;

	/**
	 * Gera Movimentos e atualiza estoque da diferença passada como parametro
	 * 
	 * @param diferenca
	 * @param idUsuario
	 */
	void gerarMovimentoEstoqueDiferenca(Diferenca diferenca, Long idUsuario);
	
}
