package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.GrupoCota;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.GrupoCota }  
 * 
 * @author Discover Technology
 */
public interface GrupoRepository extends Repository<GrupoCota,Long> {
	
	/**
	 * Obtém todos os Grupos
	 * @return List<GrupoCota> grupos
	 */
	List<GrupoCota> obterTodosGrupos(Date data) ;

	Boolean existeGrupoCota(String nome, Long idGrupo);
	
	Integer countTodosGrupos();

	List<GrupoCota> obterGrupos(String sortname, String sortorder);

	GrupoCota obterGrupoPorCota(Long id);

}
