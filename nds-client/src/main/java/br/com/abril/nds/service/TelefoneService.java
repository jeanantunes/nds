package br.com.abril.nds.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;

public interface TelefoneService {
	
	List<TelefoneAssociacaoDTO> buscarTelefonesCota(Long idCota, Set<Long> idsIgnorar);
	
	void cadastrarTelefonesCota(List<TelefoneCota> listaTelefonesAdicionar, Collection<Long> listaTelefonesRemover);
	
	void salvarTelefonesCota(List<TelefoneCota> listaTelefonesCota);
	
	void removerTelefonesCota(Collection<Long> listaTelefonesCota);
	
	List<TelefoneAssociacaoDTO> buscarTelefonesFornecedor(Long idFornecedor, Set<Long> idsIgnorar);
	
	void cadastrarTelefonesFornecedor(List<TelefoneFornecedor> listaTelefonesAdicionar, Collection<Long> listaTelefonesRemover);
	
	void salvarTelefonesFornecedor(List<TelefoneFornecedor> listaTelefonesFornecedor);
	
	void removerTelefonesFornecedor(Collection<Long> listaTelefonesFornecedor);
	
	/**
	 * Método responsável por salvar uma lista de 
	 * {@link br.com.abril.nds.model.cadastro.Telefone} 
	 * para um determinado 
	 * {@link br.com.abril.nds.model.cadastro.Entregador}.
	 * 
	 * @param listaTelefonesEntregador - Lista da associação:
	 * {@link br.com.abril.nds.model.cadastro.Telefone} / {@link br.com.abril.nds.model.cadastro.Entregador} 
	 * 
	 */
	void salvarTelefonesEntregador(List<TelefoneEntregador> listaTelefonesEntregador);
	
	/**
	 * Método responsável por remover uma lista de 
	 * {@link br.com.abril.nds.model.cadastro.Telefone} 
	 * de um determinado 
	 * {@link br.com.abril.nds.model.cadastro.Entregador}.
	 * 
	 * @param listaTelefonesEntregador - Lista com os Ids dos telefones
	 */
	void removerTelefonesEntregador(Collection<Long> listaTelefonesEntregador);
}