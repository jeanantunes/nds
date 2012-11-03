package br.com.abril.nds.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneDTO;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.model.cadastro.TipoTelefone;

public interface TelefoneService {
	
	void removerTelefones(Collection<Long> listaTelefones);
	
	/**
	 * Método responsável por salvar uma lista de 
	 * {@link br.com.abril.nds.model.cadastro.Telefone} 
	 * para um determinado 
	 * {@link br.com.abril.nds.model.cadastro.Entregador}.
	 * 
	 * @param listaTelefonesEntregador - Lista da associa��o:
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

	List<TelefoneAssociacaoDTO> buscarTelefonesPorIdPessoa(Long idPessoa, Set<Long> idsIgnorar);

	Telefone buscarTelefonePorId(Long longValue);
	
	void validarTelefone(TelefoneDTO telefone, TipoTelefone tipoTelefone);
	
}