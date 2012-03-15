package br.com.abril.nds.repository;

import java.util.Collection;

import br.com.abril.nds.model.cadastro.Telefone;

public interface TelefoneRepository extends Repository<Telefone, Long> {

	public void removerTelefones(Collection<Long> idsTelefones);
}
