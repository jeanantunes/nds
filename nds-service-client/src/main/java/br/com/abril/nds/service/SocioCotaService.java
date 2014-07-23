package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.SocioCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaSocio;

public interface SocioCotaService {

	/**
	 * Retorna o sócio através de seu ID.
	 * 
	 * @param idSocioCota - ID do sócio.
	 * 
	 * @return Sócio da cota.
	 */ 
	SocioCota obterSocioPorId(Long idSocioCota);
	
	/**
	 * Remove um determinado sócio relacionado com a cota.
	 * 
	 * @param idSocioCota - ID do sócio a ser excluído.
	 */
	void removerSocioCota(Long idSocioCota);
	
	/**
	 * Retorna uma lista de sócios referente o código da cota informada
	 * @param idCota - identificador da cota
	 * @return  List<SocioCota>
	 */
	List<SocioCota> obterSociosCota(Long idCota);
	
	/**
	 * 
	 * @param idCota
	 */
	void confirmarCadastroSociosCota(Long idCota);
	
	/**
	 * Persiste os dados de sócio referente o identificador da cota informado
	 * @param socioCota
	 * @param idCota
	 */
	void salvarSocioCota(SocioCota socioCota, Long idCota );

    /**
     * Recupera os sócios do histórico de titularidade da cota
     * 
     * @param identificador da cota 
     * @param idHistorico
     *            identificador do histórico de titularidade
     * @return lista de sócios do histórico de titularidade da cota
     */
	List<HistoricoTitularidadeCotaSocio> obterSociosHistoricoTitularidadeCota(Long idCota, Long idHistorico);

    /**
     * Recupera o sócio do histórico de titularidade da cota
     * 
     * @param idSocioCota
     *            identificador do sócio do histórico de titularidade da cota
     * @return sócio do histórico de titularidade da cota
     */
	HistoricoTitularidadeCotaSocio obterSocioHistoricoTitularidadeCota(Long idSocioCota);

}
