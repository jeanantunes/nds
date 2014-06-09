package br.com.abril.nds.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Endereco;
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
	
	List<String> obterBairrosCotas();
	
	List<String> obterUFsCotas();
	
	List<String> obterLocalidadesPorUFNome(String nome, String siglaUF);
	
	List<String> obterBairrosPorCodigoIBGENome(String nome, Long codigoIBGE); 
	
	List<String> obterLogradourosPorCodigoBairroNome(Long codigoBairro, String nomeLogradouro);
	
	EnderecoVO obterEnderecoPorCep(String cep);
	
	List<String> obterBairrosPorCidade(String cidade);
	
	List<String> pesquisarBairros(String nomeBairro);
	
	List<String> pesquisarLogradouros(String nomeLogradouro);

	List<String> obterListaLocalidadePdv();
	
	List<String> pesquisarLocalidades(String nomeLocalidade);

	List<String> obterLocalidadesPorUFPDVSemRoteirizacao(String uf);
	
	List<String> obterLocalidadesPorUFPDVBoxEspecial(String uf);

	List<String> obterUFsPDVSemRoteirizacao();
	
	Endereco getEnderecoSaneado(String cep);

	List<String> obterBairrosPDVSemRoteirizacao(String uf, String cidade);
	
	List<String> obterBairrosPDVBoxEspecial(String uf, String cidade);

    List<Endereco> buscarEnderecosPorPessoaCotaPDVs(Long idPessoa, Set<Long> idsIgnorar);
    
}