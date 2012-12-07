package br.com.abril.nds.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.dne.Bairro;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.Logradouro;
import br.com.abril.nds.vo.EnderecoVO;

public interface EnderecoService {

	void removerEndereco(Endereco endereco);

	Endereco salvarEndereco(Endereco endereco);
	
	Endereco obterEnderecoPorId(Long idEndereco);
	
	void removerEnderecos(Collection<Long> idsEndereco);

	Endereco buscarEnderecoPorId(Long idEndereco);

	List<EnderecoAssociacaoDTO> buscarEnderecosPorIdPessoa(Long idPessoa, Set<Long> idsIgnorar);

	List<String> obterMunicipiosCotas();
	
	List<String> obterUnidadeFederacaoBrasil();
	
	List<String> obterLocalidadesPorUFNome(String nome, String siglaUF);
	
	List<String> obterBairrosPorCodigoIBGENome(String nome, Long codigoIBGE);
	
	List<String> obterLogradourosPorCodigoBairroNome(Long codigoBairro, String nomeLogradouro);
	
	EnderecoVO obterEnderecoPorCep(String cep);

	List<String> pesquisarLogradouros(String nomeLogradouro);

	List<String> pesquisarBairros(String nomeBairro);
	
	List<String> pesquisarTodosBairros(); 

	List<String> pesquisarLocalidades(String nomeLocalidade);
	
	void validarEndereco(EnderecoDTO endereco, TipoEndereco tipoEndereco);
	
	List<String> obterBairrosPorCidade(String cidade);

	String buscarLocalidadePorIbge(String codigoIbge);

	List<String> obterListaLocalidadeCotas();

	List<String> obterLocalidadesPorUF(String uf);

	public abstract List<String> obterBairrosCotas();
	
}
