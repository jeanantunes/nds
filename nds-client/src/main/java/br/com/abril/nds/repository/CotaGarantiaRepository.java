package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.RelatorioDetalheGarantiaDTO;
import br.com.abril.nds.dto.RelatorioGarantiasDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioGarantiasDTO;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;

/**
 * 
 * @author Diego Fernandes
 *
 */
public interface CotaGarantiaRepository extends Repository<CotaGarantia,Long>{
	
	
	/**
	 * Recupera a garantia da cota.
	 * @param idCota Id da cota.
	 * @return
	 */
	public CotaGarantia getByCota(Long idCota);
	
	/**
	 * Delete os imóveis da garantia.
	 * @param idGarantia id da garantia
	 */
	public void deleteListaImoveis(Long idGarantia);

	/**
	 * Deleta as outras garantias
	 * @param idGarantia id da garantia
	 */
	public void deleteListaOutros(Long idGarantia);
	
	
	/**
	 * Delete a garantia da cota.
	 * @param idCota Id da cota.
	 */
	public void deleteByCota(Long idCota);
	/**
	 * Recupera a garantia da cota.
	 * @param idCota idCota Id da cota.
	 * @param type tipo da garantia
	 * @return
	 */
	public abstract <T extends CotaGarantia> T getByCota(Long idCota, Class<T> type);
	
	/**
	 * Recupera o cheque
	 * @param idCheque
	 * @return 
	 */
	public abstract Cheque getCheque(long idCheque);

	CotaGarantiaFiador obterCotaGarantiaFiadorPorIdFiador(Long idFiador);

	/**
	 * Obtém garantias cadastradas
	 * @param filtro
	 * @return List<RelatorioGarantiasDTO>
	 */
	List<RelatorioGarantiasDTO> obterGarantiasCadastradas(FiltroRelatorioGarantiasDTO filtro);
	
	/**
	 * Obtém quantidade de garantias cadastradas
	 * @param filtro
	 * @return List<RelatorioGarantiasDTO>
	 */
	Long obterCountGarantiasCadastradas(FiltroRelatorioGarantiasDTO filtro);
	
	/**
	 * Obtem detalhe da garantia
	 * @return List<RelatorioDetalheGarantiaDTO>
	 */
	List<RelatorioDetalheGarantiaDTO> obterDetalheGarantiaCadastrada(FiltroRelatorioGarantiasDTO filtro);
	
	/**
	 * Obtem quantidade de detalhe da garantia
	 * @return Long
	 */
	Long obterCountDetalheGarantiaCadastrada(FiltroRelatorioGarantiasDTO filtro);

}
