package br.com.abril.nds.repository;

import java.util.Collection;
import java.util.List;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;

/**
 * Repositório referente a entidade 
 * {@link br.com.abril.nds.model.cadastro.TelefoneEntregador}
 * 
 * @author Discover Technology
 *
 */
public interface TelefoneEntregadorRepository extends Repository<TelefoneEntregador, Long> {

	/**
	 * Método responsável por remover todos os telefones de um determinador entregador.
	 * 
	 * @param idEntregador - Id do entregador.
	 */
	void removerTelefoneEntregadorPorIdEntregador(Long idEntregador);
	
	/**
	 * Método responsável por remover determinados telefones de um entregador.
	 * 
	 * @param listaTelefonesFornecedor - Lista dos endereços a serem removidos.
	 */
	void removerTelefonesEntregador(Collection<Long> listaTelefonesFornecedor);
	
	/**
	 * Método responsável por retornar os telefones de um determinado entregador.
	 * 
	 * @param idEntregador - Id do entregador.
	 * 
	 * @return List<TelefoneEntregador> - telefones de um entregador. 
	 */
	List<TelefoneAssociacaoDTO> buscarTelefonesEntregador(Long idEntregador);
	
}
