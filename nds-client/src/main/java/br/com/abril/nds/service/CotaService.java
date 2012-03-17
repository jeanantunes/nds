package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Cota;

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
}
