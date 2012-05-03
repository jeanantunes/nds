package br.com.abril.nds.repository;

import java.util.Set;

import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneTransportador;

public interface TelefoneTransportadorRepositoty extends Repository<TelefoneTransportador, Long>{

	Telefone pesquisarTelefonePrincipalTransportador(Long idTransportador);

	void removerTelefones(Set<Long> listaTelefoneRemover);

	TelefoneTransportador buscarTelefonePorTelefoneTransportador(Long idTelefone, Long idTransportador);
}