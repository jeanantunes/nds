package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPDV;

/**
 * 
 * Interface que define as regras de implementação referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.pdv.PDV}  
 * 
 * 
 * @author Discover Technology
 *
 */
public interface PdvRepository extends Repository<PDV, Long> {
	
	/**
	 * Retorna uma lista de PDVs de uma determinda cota.
	 * 
	 * @param filtro - filtro com os parâmetros de consulta de PDVs
	 * 
	 * @return List<PdvDTO>
	 */
	List<PdvDTO> obterPDVsPorCota(FiltroPdvDTO filtro);
	
	PDV obterPDV(Long idCota,Long idPDV);
	
	Long obterQntPDV(Long idCota, Long idPdvIgnorar);
	
	Boolean existePDVPrincipal(Long idCota, Long idPdvIgnorar);
	
	void setarPDVPrincipal(boolean principal, Long idCota);
	
	/**
	 * Obtem o PDV principal de um cota.
	 * 
	 * @param idCota
	 * @return
	 */
	public PDV obterPDVPrincipal(Long idCota);
	
	/**
	 * Obtém pdvs Disponíveis de acordo com os paramestros
	 * 
	 * @param numCota
	 * @param municipio
	 * @param uf
	 * @param bairro
	 * @param cep
	 * @param boxID 
	 * @param idBoxPrincipal TODO
	 * @param pesquisaPorCota
	 * @return
	 */
	public List<PDV> obterCotasPDVsDisponiveisPor(Integer numCota, String municipio, String uf, String bairro, 
			String cep, Long boxID, Long idBoxPrincipal);
	
    /**
     * Recupera os PDV's do histórico de titularidade da cota
     * 
     * @param filtro
     *            filtro com os parâmetros para recuperação dos PDV's do
     *            histórico de titularidade da cota
     * @return Lista de {@link HistoricoTitularidadeCotaPDV} com os PDV's associados ao histórico de
     *         titularidade da cota
     */
	List<HistoricoTitularidadeCotaPDV> obterPDVsHistoricoTitularidade(FiltroPdvDTO filtro);

    /**
     * Recupera o PDV associado ao histórico de titularidade da cota
     * 
     * @param idPdv
     *            identificador do PDV associado ao histórico de titularidade da cota
     * @return {@link HistoricoTitularidadeCotaPDV} associado ao histórico de
     *         titularidade da cota
     */
	HistoricoTitularidadeCotaPDV obterPDVHistoricoTitularidade(Long idPdv);
	
	/**
	 * Obtém PDV's por Rota
	 * @param idRota
	 * @return
	 */
	List<PDV> obterPDVPorRota(Long idRota);
	
	/**
     * Obtem PDV's por Cota e informações de Endereço
     * @param numCota
     * @param municipio
     * @param uf
     * @param bairro
     * @param cep
     * @return List<PDV>
     */
	List<PDV> obterPDVPorCotaEEndereco(Integer numCota, String municipio, String uf, String bairro, String cep);
	
	/**
	 * Remove Relacionamentos do PDV com Rotas
	 * @param idPdv
	 */
	void removeCotaPDVbyPDV(Long idPdv);
	
	public List<PdvDTO> obterPDVs(Integer numeroCota);

	public List<PdvDTO> obterPdvPorCotaComEndereco(Long idCota);
	
	PDV obterPDVPrincipal(Integer numeroCota);

    Long obterQtdPdvPorCota(Integer numeroCota);
}
