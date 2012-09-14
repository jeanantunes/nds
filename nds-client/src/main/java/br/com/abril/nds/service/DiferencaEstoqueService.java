package br.com.abril.nds.service;

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
						   Long idUsuario);

	boolean validarDataLancamentoDiferenca(Date dataLancamentoDiferenca, Long idProdutoEdicao, TipoDiferenca tipoDiferenca);
	
	Diferenca obterDiferenca(Long id);
	
	Diferenca lancarDiferenca(Diferenca diferenca);

	/**
	 * Método que retorna os detalhes de diferença por cota.
	 * 
	 * @param filtro
	 * 
	 * @return DetalheDiferencaCotaDTO
	 */
	DetalheDiferencaCotaDTO obterDetalhesDiferencaCota(FiltroDetalheDiferencaCotaDTO filtro);
}
