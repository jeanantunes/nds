package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.SocioCota;

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

}
