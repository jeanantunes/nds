package br.com.abril.nds.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;

public interface TelefoneService {
	
	void cadastrarTelefone(List<TelefoneAssociacaoDTO> listaTelefones);
	
	void cadastrarTelefone(TelefoneAssociacaoDTO associacaoTelefone);
	
	void removerTelefones(Collection<Long> listaTelefones);
	
	/**
	 * M�todo respons�vel por salvar uma lista de 
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
	 * M�todo respons�vel por remover uma lista de 
	 * {@link br.com.abril.nds.model.cadastro.Telefone} 
	 * de um determinado 
	 * {@link br.com.abril.nds.model.cadastro.Entregador}.
	 * 
	 * @param listaTelefonesEntregador - Lista com os Ids dos telefones
	 */
	void removerTelefonesEntregador(Collection<Long> listaTelefonesEntregador);
}