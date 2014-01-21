package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.GrupoCota;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.GrupoCota }  
 * 
 * @author Discover Technology
 */
public interface GrupoRepository extends Repository<GrupoCota,Long> {
	
	List<DiaSemana> obterDiasOperacaoDiferenciadaCota(Integer numeroCota);
	
	List<GrupoCota> obterGruposCota(Date data) ;

	Boolean existeGrupoCota(String nome, Long idGrupo);
	
	Integer countTodosGrupos();

	List<GrupoCota> obterGrupos(String sortname, String sortorder);

	String obterNomeGrupoPorCota(Long id, Long idGrupoIgnorar);

	String obterNomeGrupoPorMunicipio(String municipio);

	Set<Long> obterIdsCotasGrupo(Long idGrupo);

	Set<String> obterMunicipiosCotasGrupo(Long idGrupo);
}
