package br.com.abril.nds.service;

import java.util.Collection;
import java.util.List;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ProcuracaoImpressaoDTO;
import br.com.abril.nds.dto.ProcuracaoImpressaoWrapper;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.ProcuracaoEntregador;
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
	 * 
	 * @param procuracaoEntregador - Procuração do entregador.
	 */
	Entregador salvarEntregadorProcuracao(Entregador entregador, ProcuracaoEntregador procuracaoEntregador);
	
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
	 * Método que retorna uma {@link br.com.abril.nds.model.cadastro.ProcuracaoEntregador} através do Id do entregador.
	 * 
	 * @param idEntregador - Id do entregador a ser pesquisado.
	 * 
	 * @return ProcuracaoEntregador - entregador encontrado.
	 */
	ProcuracaoEntregador obterProcuracaoEntregadorPorId(Long idEntregador);
	
	/**
	 * Método que retorna um {@link br.com.abril.nds.model.cadastro.Entregador} 
	 * 
	 * @param idEntregador - Id do entregador a ser pesquisado
	 * 
	 * @return Entregador
	 */
	Entregador buscarPorId(Long idEntregador);
	
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
	
	/**
	 * Método que retorna uma lista de telefones associados a um entregador.
	 * 
	 * @param idEntregador - Id do entregador.
	 * 
	 * @return List<TelefoneAssociacaoDTO> - lista retornada.
	 */
	List<TelefoneAssociacaoDTO> buscarTelefonesEntregador(Long idEntregador);
	
	/**
	 * Método que verifica se uma pessoa já foi associada à um entregador.
	 * 
	 * @param idPessoa - Id da pessoa em questão
	 * 
	 * @param idEntregador - Caso seja uma edição, este entregador será ignorado na pesquisa da pessoa. 
	 * 
	 * @return boolean - se já existe uma associação: true, caso não exista: false.
	 */
	boolean isPessoaJaCadastrada(Long idPessoa, Long idEntregador);
	
	/**
	 * Método que retorna o documento de procuração para impressão.
	 * 
	 * @param list
	 * 
	 * @return byte[]
	 * 
	 * @throws Exception
	 */
	byte[] getDocumentoProcuracao(List<ProcuracaoImpressaoWrapper> list) throws Exception;
}
