package br.com.abril.nds.service;

import java.util.Collection;
import java.util.List;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;

/**
 * Serviço responsável pela lógica de negócios que envolve a entidade
 * {@link br.com.abril.nds.model.cadastro.Entregador} 
 * 
 * @author Discover Technology
 *
 */
public interface EntregadorService {

	/**
	 * Método que retorna todos os Entregadores cadastrados.
	 * 
	 * @return List<Entregador> - lista de entregadores. 
	 */
	List<Entregador> obterEntregadoresPorFiltro(FiltroEntregadorDTO filtroEntregador);
	
	/**
	 * Método que persiste um entregador na base de dados.
	 * 
	 * @param entregador - Entregador a ser persistido.
	 */
	void salvarEntregador(Entregador entregador);
	
	/**
	 * Método que remove um entregador a partir de seu ID cadastrado na base de dados.
	 * 
	 * @param idEntregador - Id do Entregador a ser removido.
	 */
	void removerEntregadorPorId(Long idEntregador);

	/**
	 * Método que retorna a contagem geral da consulta 
	 * {@link br.com.abril.nds.service.EntregadorService#obterEntregadoresPorFiltro(FiltroEntregadorDTO)}
	 * 
	 * @param filtroEntregador - Filtro utilizado na pesquisa.
	 * 
	 * @return Long - resultado da contagem.
	 */
	Long obterContagemEntregadoresPorFiltro(FiltroEntregadorDTO filtroEntregador);
	
	/**
	 * Método que retorna um {@link br.com.abril.nds.model.cadastro.Entregador} através de seu Id.
	 * 
	 * @param idEntregador - Id do entregador a ser pesquisado.
	 * 
	 * @return Entregador - entregador encontrado.
	 */
	Entregador obterEntregadorPorId(Long idEntregador);
	
	/**
	 * Método que retorna uma lista de Endereços associados, através do ID do entregador.
	 * 
	 * @param idEntregador - Id do entregador em questão.
	 * 
	 * @return List<EnderecoAssociacaoDTO> - lista obtida na consulta.
	 */
	List<EnderecoAssociacaoDTO> obterEnderecosPorIdEntregador(Long idEntregador);
	
	/**
	 * Método responsável por processar os endereços relacionados a um determinado Entregador.
	 * 
	 * @param idEntregador - Id do entregador em questão.
	 * 
	 * @param listaEnderecoAssociacaoSalvar - Lista dos endereços a serem salvos.
	 * 
	 * @param listaEnderecoAssociacaoRemover - Lista dos endereços a serem removidos.
	 */
	void processarEnderecos(Long idEntregador,
						    List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar,
						    List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover);
	
	/**
	 * Método responsável por processar os telefones relacionados ao entregador.
	 * 
	 * @param idEntregador - Id do entregador
	 * 
	 * @param listaTelefonesAdicionar - Telefones a serem adicionados.
	 *  
	 * @param listaTelefonesRemover - Telefones a serem removidos.
	 */
	void processarTelefones(Long idEntregador, 
							List<TelefoneEntregador> listaTelefonesAdicionar, 
							Collection<Long> listaTelefonesRemover);
}
