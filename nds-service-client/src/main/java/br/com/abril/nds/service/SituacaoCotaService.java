package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.HistoricoSituacaoCotaVO;
import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;

/**
 * Interface que define serviços referentes a situação de cota.
 * 
 * @author Discover Technology
 *
 */
public interface SituacaoCotaService {
	
	/**
	 * Obtém o histórico dos status da cota de acordo com o filtro de pesquisa.
	 * 
	 * @param filtro - filtro de pesquisa
	 * 
	 * @return Histórico dos status da cota
	 */
	List<HistoricoSituacaoCotaVO> obterHistoricoStatusCota(FiltroStatusCotaDTO filtro);
	
	/**
	 * Obtém o total do histórico de status da cota de acordo
	 * com o filtro de pesquisa.
	 * 
	 * @param filtro - filtro de pesquisa
	 * 
	 * @return Total de registros
	 */
	Long obterTotalHistoricoStatusCota(FiltroStatusCotaDTO filtro);
	
	/**
	 * Atualiza a situação da cota baseando-se no histórico passado como parâmetro.
	 * 
	 * @param historicoSituacaoCota - histórico situação da cota 
	 */
	void atualizarSituacaoCota(HistoricoSituacaoCota historicoSituacaoCota, Date dataDeOperacao); 
	
	/**
	 * Remove todos os agendamentos de alteração de status para esta cota.
	 * 
	 * @param idCota - id da cota
	 */
	void removerAgendamentosAlteracaoSituacaoCota(Long idCota);

	SituacaoCadastro obterSituacaoCadastroCota(Integer numeroCota);
}