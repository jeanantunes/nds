package br.com.abril.nds.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFornecedorDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.vo.ValidacaoVO;

public interface FornecedorService {

	Fornecedor obterPorId(Long idFornecedor);
	
	Fornecedor obterFornecedorUnico(String codigoProduto);
	
	List<Fornecedor> obterFornecedores();
	
	List<Fornecedor> obterFornecedoresAtivos();
	
	List<Fornecedor> obterFornecedores(String cnpj);
	
	/**
	 * Busca os fornecedores de acordo com a permissão
	 * de balanceamento de matriz de lançamento e a situação do fornecedor
	 * @param situacoes filtra os fornecedores de acordo com a situação
	 * @return lista de fornecedores que atendem os parâmtros de pesquisa
	 */
	List<Fornecedor> obterFornecedores(SituacaoCadastro... situacoes);
	
	/**
	 * Obtém os fornecedores de acordo com o código produto.
	 * 
	 * @param codigoProduto - código do produto
	 * @param grupoFornecedor - grupo do fornecedor
	 * 
	 * @return lista de fornecedores
	 */
	List<Fornecedor> obterFornecedoresPorProduto(String codigoProduto,
												 GrupoFornecedor grupoFornecedor);
	
	Fornecedor obterFornecedorPorId(Long id);

	List<TelefoneAssociacaoDTO> buscarTelefonesFornecedor(Long idFornecedor, Set<Long> idsIgnorar);
	
	List<Fornecedor> obterFornecedoresCota(Long idCota);
	
	List<Fornecedor> obterFornecedores(Long idCota);
	
	void salvarFornecedorCota(List<Long> fornecedores, Long idCota);
	
	ValidacaoVO validarFormaCobrancaFornecedoresCota(List<Long> fornecedores, Long idCota);
	
	/**
	 * @deprecated use {@link FornecedorService#obterFornecedoresIdNome(SituacaoCadastro, Boolean)}
	 * @return
	 */
	@Deprecated
	List<ItemDTO<Long,String>> buscarComboFornecedores();
	
	/**
	 * Método que retorna uma lista de fornecedores com base no filtro recebido. 
	 * 
	 * @param filtroConsultaFornecedor - Filtro de Fornecedores.
	 * 
	 * @return List<FornecedorDTO>
	 */
	List<FornecedorDTO> obterFornecedoresPorFiltro(FiltroConsultaFornecedorDTO filtroConsultaFornecedor);
	
	/**
	 * Método que retorna a contagem de fornecedores com base no filtro recebido. 
	 * 
	 * @param filtroConsultaFornecedor - Filtro de Fornecedores.
	 * 
	 * @return Long
	 */
	Long obterContagemFornecedoresPorFiltro(FiltroConsultaFornecedorDTO filtroConsultaFornecedor);
	/**
	 * @see br.com.abril.nds.repository.FornecedorRepository#obterFornecedoresIdNome(br.com.abril.nds.model.cadastro.SituacaoCadastro, java.lang.Boolean)
	 */
	public abstract List<ItemDTO<Long, String>> obterFornecedoresIdNome(SituacaoCadastro situacao, Boolean inferface);
	
	/**
	 * Método que remove um fornecedor de acordo com seu ID.
	 * 
	 * @param idFornecedor
	 */
	void removerFornecedor(Long idFornecedor);
	
	/**
	 * Método responsável por criar um novo fornecedor.
	 * 
	 * @param fornecedor - Fornecedor
	 */
	void salvarFornecedor(Fornecedor fornecedor);
	
	/**
	 * Método responsável pela atualização de fornecedor.
	 * 
	 * @param fornecedor - Fornecedor
	 * 
	 * @return - Fornecedor atualizado.
	 */
	Fornecedor merge(Fornecedor fornecedor);
	
	/**
	 * Método responsável por processar os endereços relacionados a um determinado Fornecedor.
	 * 
	 * @param idFornecedor - Id do fornecedor em questão.
	 * 
	 * @param listaEnderecoAssociacaoSalvar - Lista dos endereços a serem salvos.
	 * 
	 * @param listaEnderecoAssociacaoRemover - Lista dos endereços a serem removidos.
	 */
	void processarEnderecos(Long idFornecedor,
						    List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar,
						    List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover);
	
	/**
	 * Método responsável por processar os telefones relacionados ao fornecedor.
	 * 
	 * @param idFornecedor - Id do fornecedor
	 * 
	 * @param listaTelefonesAdicionar - Telefones a serem adicionados.
	 *  
	 * @param listaTelefonesRemover - Telefones a serem removidos.
	 */
	void processarTelefones(Long idEntregador, 
						    List<TelefoneAssociacaoDTO> listaTelefonesAdicionar, 
						    Collection<Long> listaTelefonesRemover);
	
	/**
	 * Método que retorna todos os endereços referentes ao fornecedor em questão
	 * 
	 * @param idFornecedor
	 * 
	 * @return List<EnderecoAssociacaoDTO>
	 */
	List<EnderecoAssociacaoDTO> obterEnderecosFornecedor(Long idFornecedor);

	/**
	 * Método que retorna todos os telefones referentes ao fornecedor em questão
	 * 
	 * @param idFornecedor
	 * 
	 * @return List<TelefoneAssociacaoDTO> 
	 */
	List<TelefoneAssociacaoDTO> obterTelefonesFornecedor(Long idFornecedor);

	/**
	 * Método que verifica se já existe pessoa cadastrada em outros fornecedores.
	 * 
	 * @param idPessoa
	 * 
	 * @param idFornecedor
	 * 
	 * @return true/false
	 */
	boolean isPessoaJaCadastrada(Long idPessoa, Long idFornecedor);

	List<Fornecedor> obterFornecedorLikeNomeFornecedor(String nomeFornecedor);
	
	List<Fornecedor> obterFornecedoresPorId(List<Long> idsFornecedores);

	public abstract Integer obterMaxCodigoInterface();
	
	public Integer obterMinCodigoInterfaceDisponivel();
	
	public Fornecedor obterFornecedorPorCodigoInterface(Integer codigoInterface);

	List<Pessoa> obterFornecedorPorNome(String nomeFornecedor);
	
	List<Pessoa> obterFornecedorPorNome(String nomeFornecedor, Integer qtdMaxResult);

	List<Pessoa> obterFornecedorPorNomeFantasia(String nomeFantasia);
	
	List<Fornecedor> obterFornecedoresPorSituacaoEOrigem(SituacaoCadastro situacaoCadastro, Origem origem);

	Origem obterOrigemCadastroFornecedor(Long idFornecedor);

	List<Fornecedor> obterFornecedoresDesc();
	
	List<ItemDTO<Long, String>> obterFornecedoresUnificados();
	
	List<Fornecedor> obterFornecedoresNaoUnificados();
	
	List<ItemDTO<Long, String>> obterFornecedoresFcDinap();

	Integer obterCodigoInterfacePorID(Long idFornecedor);
}