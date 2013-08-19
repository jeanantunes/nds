package br.com.abril.nds.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;

public interface TelefoneFornecedorRepository extends Repository<TelefoneFornecedor, Long> {

	List<TelefoneAssociacaoDTO> buscarTelefonesFornecedor(Long idFornecedor, Set<Long> idsIgnorar);
	
	void removerTelefonesFornecedor(Collection<Long> listaTelefonesFornecedor);
	
	List<Telefone> buscarTelefonesPessoaPorFornecedor(Long idFornecedor);
	
	TelefoneFornecedor obterTelefoneFornecedor(Long idTelefone, Long idFornecedor);

	public abstract TelefoneFornecedor obterTelefonePrincipal(long idFornecedor);
}