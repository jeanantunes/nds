package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.Transportador;

public interface TelefoneTransportadorRepositoty extends Repository<Transportador, Long>{

	Telefone pesquisarTelefonePrincipalTransportador(Long idTransportador);
}