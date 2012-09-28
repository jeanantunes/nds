package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.client.vo.EntregadorCotaProcuracaoPaginacaoVO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.ProcuracaoEntregador;

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
	
	/**
	 * Método que retorna a Procuração de um entragador, através do seu ID.
	 * 
	 * @param idEntregador - Id do entregador a ser pesquisado.
	 * 
	 * @return ProcuracaoEntregador - Procuração encontrada. 
	 */
	ProcuracaoEntregador obterProcuracaoEntregadorPorIdEntregador(Long idEntregador);
	
	/**
	 * Método que verifica se uma pessoa já foi associada à um entregador, 
	 * através da quantidade de registros retornada.
	 * 
	 * @param idPessoa - Id da pessoa em questão
	 * 
	 * @param idEntregador - Caso seja uma edição, este entregador será ignorado na pesquisa da pessoa.
	 * 
	 * @return Integer - quantidade de registros encontrada.
	 */
	Integer obterQuantidadeEntregadoresPorIdPessoa(Long idPessoa, Long idEntregador);

	EntregadorCotaProcuracaoPaginacaoVO buscarCotasAtendidas(Long idEntregador,
			int pagina, int resultadosPorPagina, String sortname,
			String sortorder);
	
	/**
	 * Verifica a existencia de {@link Entregador} pelo codigo diferente do id;
	 * @param codigo Código do Entregador
	 * @param id (Opional) ID do entragador
	 * @return
	 */
	public abstract boolean hasEntregador(Long codigo, Long id);
	
	Entregador obterEntregadorPorRota(Long idRota);

	boolean verificarEntregador(Long idCota);

	/**
	 * Obtém lista de Entregadores por nome
	 * @param nome
	 * @return
	 */
	List<Entregador> obterEntregadoresPorNome(String nome);

	/**
	 * Obtém entregador por nome
	 * @param nome
	 * @return
	 */
	Entregador obterPorNome(String nome);
}
