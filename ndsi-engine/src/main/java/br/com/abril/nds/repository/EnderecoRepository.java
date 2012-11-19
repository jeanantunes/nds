package br.com.abril.nds.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.dne.Bairro;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.Logradouro;
import br.com.abril.nds.vo.EnderecoVO;

/**
 * Interface que define as regras para repositório referente a entidade
 * {@link br.com.abril.nds.model.cadastro.Endereco}
 * 
 * @author Discover Technology
 *
 */
public interface EnderecoRepository extends Repository<Endereco, Long> {

	/**
	 * Método responsável por remover os endereços de acordos com seus ids.
	 * 
	 * @param idsEndereco - Coleção com os ids dos endereços
	 */
	void removerEnderecos(Collection<Long> idsEndereco);

	List<Endereco> buscarEnderecosPessoa(Long idPessoa, Set<Long> idsIgnorar);

	List<String> obterMunicipiosCotas();
	
	List<Localidade> obterLocalidadesPorUFNome(String nome, String siglaUF);
	
	List<Bairro> obterBairrosPorCodigoIBGENome(String nome, Long codigoIBGE); 
	
	List<Logradouro> obterLogradourosPorCodigoBairroNome(Long codigoBairro, String nomeLogradouro);
	
	EnderecoVO obterEnderecoPorCep(String cep);
	
	List<Bairro> obterBairrosPorCidade(String cidade);
	
}
