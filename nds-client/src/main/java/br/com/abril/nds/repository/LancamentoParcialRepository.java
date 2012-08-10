package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;

public interface LancamentoParcialRepository extends Repository<LancamentoParcial, Long>{

	LancamentoParcial obterLancamentoPorProdutoEdicao(Long idProdutoEdicao);

	/**
	 * Obter Lancamentos parciais filtrados
	 * 
	 * @param filtro - Dados do filtro
	 * @return Lista de ParcialDTO
	 */
	List<ParcialDTO> buscarLancamentosParciais(FiltroParciaisDTO filtro);

	/**
	 * Retorna a quantidade de registros que a busca com filtro trará.
	 * 
	 * @param filtro 
	 * @return Quantidade de registros
	 */
	Integer totalbuscaLancamentosParciais(FiltroParciaisDTO filtro);

	/**
	 * Obter lancamentoParcial a partir de idProdutoEdicao e dataRecolhimentoFinal.
	 * 
	 * @param idProdutoEdicao
	 * @param dataRecolhimentoFinal
	 * 
	 * @return LancamentoParcial
	 */
	LancamentoParcial obterLancamentoParcial(Long idProdutoEdicao, Date dataRecolhimentoFinal);

	
	
}
