package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.MovimentoAprovacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroControleAprovacaoDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.movimentacao.Movimento;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.movimentacao.Movimento}
 * 
 * @author Discover Technology
 */
public interface MovimentoRepository extends Repository<Movimento, Long> {

	/**
	 * Obtém movimentos para aprovação de acordo com o filtro.
	 * 
	 * @param filtro - filtro para pesquisa
	 * 
	 * @return {@link List<MovimentoAprovacaoDTO>}
	 */
	List<MovimentoAprovacaoDTO> obterMovimentosAprovacao(FiltroControleAprovacaoDTO filtro);
	
	/**
	 * Obtém o total de movimentos para aprovação de acordo com o filtro.
	 * 
	 * @param filtro - filtro para pesquisa
	 * 
	 * @return total de movimentos para aprovação
	 */
	Long obterTotalMovimentosAprovacao(FiltroControleAprovacaoDTO filtro);
	
	boolean existeMovimentoEstoquePendente(List<GrupoMovimentoEstoque> gruposMovimento);
	
	boolean existeMovimentoFinanceiroPendente(List<GrupoMovimentoFinaceiro> gruposMovimento);
	
}
