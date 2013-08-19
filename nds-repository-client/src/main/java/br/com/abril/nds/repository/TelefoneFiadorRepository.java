package br.com.abril.nds.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneFiador;

public interface TelefoneFiadorRepository extends Repository<TelefoneFiador, Long>{

	void removerTelefonesFiador(Collection<Long> listaTelefones);
	
	Telefone pesquisarTelefonePrincipalFiador(Long idFiador);
	
	List<TelefoneAssociacaoDTO> buscarTelefonesFiador(Long idFiador, Set<Long> idsIgnorar);

	List<Telefone> buscarTelefonesPessoaPorFiador(Long idFiador);

	TelefoneFiador obterTelefonePorTelefoneFiador(Long idTelefone, Long idFiador);

	void excluirTelefonesFiador(Long idFiador);

	boolean verificarTelefonePrincipalFiador(Long id, Set<Long> idsIgnorar);
}
