package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.model.financeiro.Divida;

public interface DividaRepository extends Repository<Divida, Long>{

	Divida obterUltimaDividaPorCota(Long idCota);
	
	/**
	 * Retorna as dividas geradas conforme parametros informados no filtro.
	 * @param filtro
	 * @return List<GeraDividaDTO> 
	 */
	List<GeraDividaDTO> obterDividasGeradas(FiltroDividaGeradaDTO filtro);
	
	/**
	 * Retorna a quantidade de registros da consulta de dividas geradas, conforme filtro informado.
	 * @param filtro
	 * @return Long
	 */
	Long obterQuantidadeRegistroDividasGeradas(FiltroDividaGeradaDTO filtro);
	
	/**
	 * Retorna a quantidade de registros da consulta de dividas geradas.
	 * @param dataMovimento
	 * @return Long
	 */
	Long obterQunatidadeDividaGeradas(Date dataMovimento);
}
