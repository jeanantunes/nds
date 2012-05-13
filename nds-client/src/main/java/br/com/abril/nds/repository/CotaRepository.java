package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ProdutoValorDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.EnderecoCota;

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
	 * @param numeroCota - nÃºmero da cota
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
	 * @param rp 
	 * @param page 
	 * @param paginacaoVO 
	 * 
	 * @param limiteInadimplencia - Quantidade de inadimplencias que define a cota como sujeita a suspensão
	 * @param paginacaoVO - Dados referentes a paginação e ordenação
	 * 
	 * @return Cotas
	 */
	List<CotaSuspensaoDTO> obterCotasSujeitasSuspensao(String sortOrder, String sortColumn, Integer page, Integer rp);
	
	
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
	
	List<Integer> obterDiasConcentracaoPagamentoCota(Long idCota);

	Long obterTotalCotasSujeitasSuspensao();
	
	List<Cota> obterCotaAssociadaFiador(Long idFiador);
	
	/**
	 * 
	 * Obtem cotas sujeitas a antecipação de recolhimento de encalhe.
	 * 
	 * @param filtro - filtro coma as opções de consulta 
	 * 
	 * @return List<ChamdaAntecipadaEncalheDTO>
	 */
	List<ChamadaAntecipadaEncalheDTO> obterCotasSujeitasAntecipacoEncalhe(FiltroChamadaAntecipadaEncalheDTO filtro);
	
	/**
	 * Obtem a quantidade de cotas sujeitas a antecipação de recolhimento de encalhe.
	 * 
	 * @param filtro - filtro coma as opções de consulta 
	 * 
	 * @return Long
	 */
	Long obterQntCotasSujeitasAntecipacoEncalhe(FiltroChamadaAntecipadaEncalheDTO filtro);
	
	/**
	 * Obtem a quantidade de exemplares de uma cota sujeita a antecipação de recolhimento de encalhe.
	 * 
	 * @param filtro - filtro coma as opções de consulta 
	 * 
	 * @return BigDecimal
	 */
	BigDecimal obterQntExemplaresCotasSujeitasAntecipacoEncalhe(FiltroChamadaAntecipadaEncalheDTO filtro);

	
	Cota obterCotaPDVPorNumeroDaCota(Integer numeroCota);
	
	/**
	 * Obtem o endereço principal da cota.
	 * @param idCota Id da Cota
	 * @return endereço principal da cota.
	 */
	public abstract EnderecoCota obterEnderecoPrincipal(long idCota);
	
	/**
	 * Retorna uma lista de Cotas cadastradas para manutenção de edição e exclusão
	 * 
	 * @param filtro - opções de filtro para consulta
	 * 
	 * @return List<CotaDTO>
	 */
	List<CotaDTO> obterCotas(FiltroCotaDTO filtro);
	
	/**
	 * Retorna a quantidade de Cotas cadastradas para manutenção de edição e exclusão
	 * 
	 * @param filtro - opções de filtro para consulta
	 * 
	 * @return Long
	 */
	Long obterQuantidadeCotasPesquisadas(FiltroCotaDTO filtro);
	
	Integer gerarSugestaoNumeroCota();
}
