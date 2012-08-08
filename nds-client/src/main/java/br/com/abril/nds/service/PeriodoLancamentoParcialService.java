package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.dto.PeriodoParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;

public interface PeriodoLancamentoParcialService {

	/**
	 * Retorna DTOs de periodos parciais de acordo com o filtro
	 * 
	 * @param filtro
	 */
	List<PeriodoParcialDTO> obterPeriodosParciais(FiltroParciaisDTO filtro);

	/**
	 * Retorna COUNT total da pesquisa "obterPeriodosParciais"
	 * 
	 * @param filtro
	 * @return
	 */
	Integer totalObterPeriodosParciais(FiltroParciaisDTO filtro);

	/**
	 * Obtem detalhes das vendas do produtoEdição nas datas de Lancamento e Recolhimento
	 * @param dataLancamento
	 * @param dataRecolhimento
	 * @param idProdutoEdicao
	 * @return List<ParcialVendaDTO>
	 */
	List<ParcialVendaDTO> obterDetalhesVenda(Date dataLancamento, Date dataRecolhimento, Long idProdutoEdicao);

}
