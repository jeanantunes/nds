package br.com.abril.nds.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.pdv.TelefonePDV;

public interface TelefonePdvRepository extends Repository<TelefonePDV, Long> {

	void removerTelefonesPdv(Collection<Long> listaTelefones);
	
	Telefone pesquisarTelefonePrincipalPdv(Long idPdv);
	
	List<TelefoneAssociacaoDTO> buscarTelefonesPdv(Long idPdv, Set<Long> idsIgnorar);

	List<Telefone> buscarTelefonesPessoaPorPdv(Long idPdv);

	TelefonePDV obterTelefonePorTelefonePdv(Long idTelefone, Long idPdv);

	void excluirTelefonesPdv(Long idPdv);
}
