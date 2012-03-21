package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ProdutoValorDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.Cobranca;

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
	 * Obtém uma lista de cotas pelo nome.
	 * 
	 * @param nome - nome da cota (pessoa)
	 * 
	 * @return Lista de {@link Cota}
	 */
	List<Cota> obterPorNome(String nome);
	
	/**
	 * Obtém uma lista dos endereços cadastrados para uma determinada cota.
	 * 
	 * @param idCota - Id da cota.
	 * 
	 * @return List<Endereco> 
	 */
	List<EnderecoAssociacaoDTO> obterEnderecosPorIdCota(Long idCota);
	
	/**
	 * Obtém sugestão de cotas a serem suspensas com base 
	 * @param paginacaoVO 
	 * 
	 * @param limiteInadimplencia - Quantidade de inadimplencias que define a cota como sujeita a suspensão
	 * @param paginacaoVO - Dados referentes a paginação e ordenação
	 * 
	 * @return Cotas
	 */
	List<Cota> obterCotasSujeitasSuspensao(String sortOrder, String sortColumn, Integer limiteInadimplencia);
	
	/**
	 * Obtém cobranças não pagas em nome da cota
	 * 
	 * @param idCota -  Código da cota
	 * @return Lista de Cobranças
	 */
	List<Cobranca> obterCobrancasDaCotaEmAberto(Long idCota);
	
	/**
	 * Obtém data em que houve a primeira inadimplencia com cobrança ainda em aberto
	 * 
	 * @param idCota -  Código da Cota
	 * @return dia
	 */
	Date obterDataAberturaDividas(Long idCota);
	
	/**
	 * Obtém valor dos repartes Consignados a cota em determinado dia
	 * 
	 * @param idCota - código da cota
	 * @param date - data
	 * @return
	 */
	List<ProdutoValorDTO> obterReparteDaCotaNoDia(Long idCota, Date date);
	
	/**
	 * Obtém valor total de consignados da cota
	 * 
	 * @param idCota
	 * @return
	 */	
	List<ProdutoValorDTO> obterValorConsignadoDaCota(Long idCota);
	
	/**
	 * Obtém total de cobranças não pagas pela cota
	 * 
	 * @param idCota - Código da Cota
	 * @return valor
	 */
	Double obterDividaAcumuladaCota(Long idCota);
}
