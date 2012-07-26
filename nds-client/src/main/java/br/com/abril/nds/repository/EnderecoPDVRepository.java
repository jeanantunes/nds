package br.com.abril.nds.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;

public interface EnderecoPDVRepository extends Repository<EnderecoPDV, Long> {

	List<EnderecoAssociacaoDTO> buscaEnderecosPDV(Long idPDV,	Set<Long> idsIgnorar);

	List<Endereco> buscarEnderecosPessoaPorPDV(Long idPDV);

	EnderecoPDV buscarEnderecoPorEnderecoPDV(Long idEndereco, Long idPDV);

	void excluirEnderecosPorIdPDV(Long idPDV);

	void excluirEnderecosPDV(Collection<Long> idsEnderecoPDV);
	
	List<Endereco> buscarMunicipioPdvPrincipal();
}
