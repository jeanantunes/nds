package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.service.exception.TipoMovimentoEstoqueInexistente;

public interface CotaAusenteService {
	
	/**
	 * Gera nova cota ausente e envia seu reparte da data para Suplementar
	 * 
	 * @param numCota - Número da cota
	 * @param data - Data em que a cota será declarada ausente
	 * @param idUsuario - Códifo do usuário
	 * @throws TipoMovimentoEstoqueInexistente - Exceção para TipoMovimentoInexistente não registrado no banco
	 */
	void declararCotaAusenteEnviarSuplementar(Integer numCota, Date data, Long idUsuario) throws TipoMovimentoEstoqueInexistente;	
	
	/**
	 * Gera nova cota ausente e envia seu reparte da data para Suplementar e realiza o rateio deste reparte para
	 * outras cotas
	 * 
	 * @param movimentosRateio - Movimentos com rateios definidos
	 * @param numCota - Número da cota
	 * @param data - Data em que a cota será declarada ausente
	 * @param idUsuario - Códifo do usuário
	 * @throws TipoMovimentoEstoqueInexistente - Exceção para TipoMovimentoInexistente não registrado no banco
	 */
	void declararCotaAusenteRatearReparte(Integer numCota, Date data, Long idUsuario, List<MovimentoEstoqueCotaDTO> movimentosRateio) throws TipoMovimentoEstoqueInexistente;
	
	/**
	 * Obtém lista de Cotas Ausentes de acordo com o filtro
	 * 
	 * @param filtroCotaAusenteDTO - Filtro utilizado na pesquisa
	 * @return Lista de Cotas Ausentes
	 */
	List<CotaAusenteDTO> obterCotasAusentes(FiltroCotaAusenteDTO filtroCotaAusenteDTO);
	
	Long obterCountCotasAusentes(FiltroCotaAusenteDTO filtro);
	
	void cancelarCotaAusente(Long idCotaAusente, Long idUsuario) throws TipoMovimentoEstoqueInexistente;

}
