package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}  
 * 
 * @author Discover Technology
 *
 */
public interface CotaService {

	Cota obterPorNumeroDaCota(Integer numeroCota);

	List<Cota> obterCotasPorNomePessoa(String nome);

	Cota obterPorNome(String nome);
	
	Cota obterPorId(Long idCota);
	
	/**
	 * Obtém uma lista dos endereços cadastrados para uma determinada cota.
	 * 
	 * @param idCota - Id da cota.
	 * 
	 * @return List<EnderecoAssociacaoDTO> 
	 */
	List<EnderecoAssociacaoDTO> obterEnderecosPorIdCota(Long idCota);
	/**
	 * Persiste um conjunto de endereços para uma determinada cota.
	 * 
	 * @param cota
	 * 
	 * @param listaEnderecoAssociacaoSalvar
	 * 
	 * @param listaEnderecoAssociacaoRemover
	 */
	void processarEnderecos(Cota cota, 
							List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar, 
							List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover);
	
	/**
	 * Obtém cobranças não pagas em nome da cota
	 * 
	 * @param idCota -  Código da cota
	 * @return Lista de Cobranças
	 */
	List<Cobranca> obterCobrancasDaCotaEmAberto(Long idCota);
	
	/**
	 * Suspende lista de cotas
	 * 
	 * @param idCotas - código das cotas a serem suspensas
	 * 
	 * @return Lista de Cotas suspensas
	 */List<Cota>suspenderCotas(List<Long> idCotas, Long idUsuario);
	
	/**
	 * Suspende cota
	 * 
	 * 
	 * @param idCota - Código da cota a ser suspensa
	 * 
	 * @return Cota - cota suspensa
	 */
	Cota suspenderCota(Long idCota, Usuario usuario);

	/**
	 * Suspende lista de cotas e retorna dados básicos das cota suspensas com 
	 * necessidade de devolução de contrato
	 * 
	 * @param idCotas - Códigos das cotas
	 * @param usuario - usuário
	 * @return
	 */
	List<CotaSuspensaoDTO> suspenderCotasGetDTO(List<Long> idCotas, Long idUsuario);
	
	/**
	 * Obtém sugestão de cotas a serem suspensas com base 
	 * @param rp 
	 * @param page 
	 * 
	 * @param limiteInadimplencia - Quantidade de inadimplencias que define a cota como sujeita a suspensão
	 * 
	 * @param paginacaoVO - Dados referentes a paginação e ordenação
	 * @return lista de CotaSuspensaoDTO
	 */
	List<CotaSuspensaoDTO> obterDTOCotasSujeitasSuspensao(String sortOrder, String sortColumn, Integer inicio, Integer rp);

	Long obterTotalCotasSujeitasSuspensao();
}
