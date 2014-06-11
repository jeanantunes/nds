package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.CotaTipoDTO;
import br.com.abril.nds.dto.GrupoCotaDTO;
import br.com.abril.nds.dto.MunicipioDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;


public interface GrupoService {
 
	/**
	 * Obtém todos os Grupos
	 * @param includeHistory 
	 * @return List<GrupoCota> grupos
	 */
	List<GrupoCotaDTO> obterTodosGrupos(String sortname, String sortorder, boolean includeHistory) ;

	void excluirGrupo(Long idGrupo);

	/**
	 * Obtém cotas por Tipo com paginação
	 * 
	 * @return
	 */
	List<CotaTipoDTO> obterCotaPorTipo(TipoDistribuicaoCota tipoCota, Integer page, Integer rp, String sortname, String sortorder);

	/**
	 * Obtém quantidade retornada pela consuta de cota por tipo
	 */
	int obterCountCotaPorTipo(TipoDistribuicaoCota tipoCota);

	/**
	 * Retorna Municipios e a quantidade de cotas para cada - resultado paginado
	 * @param page
	 * @param rp
	 * @param sortname
	 * @param sortorder
	 * @return
	 */
	List<MunicipioDTO> obterQtdeCotaMunicipio(Integer page, Integer rp, String sortname, String sortorder);
	
	/**
	 * Count da pesquisa "obterQtdeCotaMunicipio" 
	 * 
	 * @return
	 */
	int obterCountQtdeCotaMunicipio();

	/**
	 * Salvar grupo de cotas
	 * @param idGrupo 
	 * @param cotas
	 * @param nomeDiferenca
	 * @param diasSemana
	 * @param tipoCota 
	 */
	void salvarGrupoCotas(Long idGrupo, Set<Long> cotas, String nomeDiferenca,
			List<DiaSemana> diasSemana, TipoCaracteristicaSegmentacaoPDV tipoCota);

	/**
	 * Salvar grupo de municipios
	 * @param idGrupo 
	 * 
	 * @param municipios
	 * @param nomeDiferenca
	 * @param diasSemana
	 */
	void salvarGrupoMunicipios(Long idGrupo, Set<String> idMunicipios,
			String nome, List<DiaSemana> diasSemana);

	/**
	 * Obtém ids das Localidades do Grupo
	 * 
	 * @param idGrupo
	 * @return
	 */
	Set<String> obterMunicipiosDoGrupo(Long idGrupo);

	/**
	 * Obtém ids das Cotas do Grupo
	 * 
	 * @param idGrupo
	 * @return
	 */
	Set<Long> obterCotasDoGrupo(Long idGrupo);

	
	Integer countTodosGrupos(Date dataOperacao);

    public abstract Date getDataInicioProximaSemanaSemCE();
    
    /**
	 * Obtém datas de recolhimento da semana da data de operação diferenciada
	 * 
	 * @param numeroCota
	 * @param data
	 * @return List<Date>
	 */
	List<Date> obterDatasRecolhimentoOperacaoDiferenciada(Integer numeroCota,Date data);

}