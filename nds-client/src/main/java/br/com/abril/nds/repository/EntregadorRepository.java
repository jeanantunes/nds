package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO;
import br.com.abril.nds.model.cadastro.Entregador;

/**
 * Interface que define as regras para o repositório de
 * {@link br.com.abril.nds.model.cadastro.Entregador} 
 * 
 * @author Discover Technology
 *
 */
public interface EntregadorRepository extends Repository<Entregador, Long> {

	/**
	 * Método que retorna uma lista de Entregadores cadastrados a partir de um
	 * {@link br.com.abril.nds.dto.filtro.FiltroEntregadorDTO}
	 * 
	 * @param filtroEntregador - Filtro utilizado na pesquisa.
	 * 
	 * @return List<Entregador> - Resultado da consulta.  
	 */
	List<Entregador> obterEntregadoresPorFiltro(FiltroEntregadorDTO filtroEntregador);
	
	/**
	 * Método que retorna a contagem geral da consulta 
	 * {@link br.com.abril.nds.repository.EntregadorRepository#obterEntregadoresPorFiltro(FiltroEntregadorDTO)}
	 * 
	 * @param filtroEntregador - Filtro utilizado na pesquisa.
	 * 
	 * @return Long - resultado da contagem.
	 */
	Long obterContagemEntregadoresPorFiltro(FiltroEntregadorDTO filtroEntregador);

	/**
	 * Método que retorna uma lista de Endereços associados, através do ID do entregador.
	 * 
	 * @param idEntregador - Id do entregador em questão.
	 * 
	 * @return List<EnderecoAssociacaoDTO> - lista obtida na consulta.
	 */
	List<EnderecoAssociacaoDTO> obterEnderecosPorIdEntregador(Long idEntregador);
}
