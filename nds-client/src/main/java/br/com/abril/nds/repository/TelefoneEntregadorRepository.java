package br.com.abril.nds.repository;

import java.util.Collection;

import br.com.abril.nds.model.cadastro.TelefoneEntregador;

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
	
}
