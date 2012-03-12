package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Cota;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}  
 * 
 * @author Discover Technology
 *
 */
public interface CotaRepository extends Repository<Cota, Long> {

	/**
	 * Obtém uma Cota pelo seu número.
	 * 
	 * @param numeroCota - número da cota
	 * 
	 * @return {@link Cota}
	 */
	Cota obterPorNumerDaCota(Integer numeroCota);
	
	/**
	 * Obtém uma lista de cotas através
	 * da comparação por nome.
	 * 
	 * @param nome - nome da cota
	 * 
	 * @return Lista de {@link Cota}
	 */
	List<Cota> obterCotasPorNomePessoa(String nome);
	
	/**
	 * Obtém uma cota pelo seu nome.
	 * 
	 * @param nome - nome da cota (pessoa)
	 * 
	 * @return {@link Cota}
	 */
	Cota obterPorNome(String nome);
	
}
