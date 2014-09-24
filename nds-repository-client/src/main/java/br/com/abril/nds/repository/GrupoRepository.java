package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.GrupoCota;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.GrupoCota }  
 * 
 * @author Discover Technology
 */
public interface GrupoRepository extends Repository<GrupoCota,Long> {
	
	/**
	 * Obtém os numeros das cotas de operação diferenciada e que estão 
	 * dentro do range de data vigencia e numero cota informados.
	 * 
	 * @param filtro
	 * 
	 * @return List - Integer
	 */
	public List<Integer> obterCotasComOperacaoDiferenciada(FiltroEmissaoCE filtro);

	/**
	 * Obtém os numeros e nomes das cotas que não possuem operação diferenciada 
	 * e que estão dentro do range de data vigencia e numero cota 
	 * informados.
	 * 
	 * @param filtro
	 * 
	 * @return List - CotaEmissaoDTO
	 */
	public List<CotaEmissaoDTO> obterCotasSemOperacaoDiferenciada(FiltroEmissaoCE filtro);

	/**
	 * Retorna os dias de recolhimento de operação diferenciada
	 * da cota em questão, com vigência dentro do range
	 * 
	 * @param numeroCota
	 * @param dataInicio
	 * @param dataFim
	 * 
	 * @return List - DiaSemana
	 */
	public List<DiaSemana> obterDiasRecolhimentoOperacaoDiferenciada(
			final Integer numeroCota, 
			final Date dataInicio, 
			final Date dataFim);
	
	List<DiaSemana> obterDiasOperacaoDiferenciadaCota(final Integer numeroCota,final Date dataOperacao);
	
	List<GrupoCota> obterGruposCota(Date data) ;

	Boolean existeGrupoCota(String nome, Long idGrupo, Date dataOperacao);
	
	Integer countTodosGrupos(Date dataOperacao);

	List<GrupoCota> obterGruposAtivos(String sortname, String sortorder, boolean includeHistory);

	String obterNomeGrupoPorCota(Long id, Long idGrupoIgnorar, Date dataOperacao);

	String obterNomeGrupoPorMunicipio(String municipio, Long idGrupoIgnorar, Date dataOperacao);

	Set<Long> obterIdsCotasGrupo(Long idGrupo);

	Set<String> obterMunicipiosCotasGrupo(Long idGrupo);

	/**
	 * Obtém lista de GrupoCota(Operação diferenciada)
	 * @param idCota
	 * @param dataOperacao
	 * @return List<GrupoCota>
	 */ 
	List<GrupoCota> obterListaGrupoCotaPorCotaId(Long idCota, Date dataOperacao);

	/**
	 * Obtém lista de GrupoCota(Operação diferenciada)
	 * @param numeroCota
	 * @param dataOperacao
	 * @return List<GrupoCota>
	 */ 
	List<GrupoCota> obterListaGrupoCotaPorNumeroCota(Integer numeroCota, Date dataOperacao);
}
