package br.com.abril.nds.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.dne.Bairro;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.Logradouro;
import br.com.abril.nds.vo.EnderecoVO;

public interface EnderecoService {

	void removerEndereco(Endereco endereco);

	Endereco salvarEndereco(Endereco endereco);
	
	Endereco obterEnderecoPorId(Long idEndereco);

	void cadastrarEnderecos(List<EnderecoAssociacaoDTO> listaEnderecos, Pessoa pessoa);
	
	void removerEnderecos(Collection<Long> idsEndereco);

	Endereco buscarEnderecoPorId(Long idEndereco);

	List<EnderecoAssociacaoDTO> buscarEnderecosPorIdPessoa(Long idPessoa, Set<Long> idsIgnorar);

	List<String> obterMunicipiosCotas();
	
	List<String> obterUnidadeFederacaoBrasil();
	
	List<Localidade> obterLocalidadesPorUFNome(String nome, String siglaUF);
	
	List<Bairro> obterBairrosPorCodigoIBGENome(String nome, Long codigoIBGE);
	
	List<Logradouro> obterLogradourosPorCodigoBairroNome(Long codigoBairro, String nomeLogradouro);
	
	EnderecoVO obterEnderecoPorCep(String cep);

	List<Logradouro> pesquisarLogradouros(String nomeLogradouro);

	List<Bairro> pesquisarBairros(String nomeBairro);

	List<Localidade> pesquisarLocalidades(String nomeLocalidade);
}
