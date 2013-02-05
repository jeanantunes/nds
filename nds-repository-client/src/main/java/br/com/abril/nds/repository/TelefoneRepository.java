package br.com.abril.nds.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Telefone;

public interface TelefoneRepository extends Repository<Telefone, Long> {

	public void removerTelefones(Collection<Long> idsTelefones);
	
	public List<Telefone> buscarTelefonesPessoa(Long idPessoa, Set<Long> idsIgnorar);
}
