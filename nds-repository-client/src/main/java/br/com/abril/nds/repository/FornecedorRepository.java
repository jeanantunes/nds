package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFornecedorDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.EnderecoFornecedor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;

/**
 * Interface que define as regras de implementação referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}  
 * 
 * @author william.machado
 *
 */
public interface FornecedorRepository extends Repository<Fornecedor, Long> {

	/**
	 * Obtém o lista de id de todos os fornecedores
	 * @return List - Long
	 */
	List<Long> obterIdFornecedores();
	
	List<Fornecedor> obterFornecedoresAtivos();
	
	List<Fornecedor> obterFornecedores(String cnpj);
	
	List<Fornecedor> obterFornecedores();
	
	/**
	 * Busca os fornecedores de acordo com a permissão
	 * de balanceamento de matriz de lançamento e a situação do fornecedor
	 * @param situacoes filtra os fornecedores de acordo com a situação
	 * @return lista de fornecedores que atendem os parâmtros de pesquisa
	 */
	List<Fornecedor> obterFornecedores(SituacaoCadastro... situacoes);

	
	/**
	 * Obtém a lista de fornecedores relativos a um produto.
	 * 
	 * @param codigoProduto
	 * @param grupoFornecedor
	 * 
	 * @return List - Fornecedor.
	 */
	public List<Fornecedor> obterFornecedoresDeProduto(String codigoProduto,
													   GrupoFornecedor grupoFornecedor);
	
	
	List<Fornecedor> obterFornecedoresCota(Long idCota);
	
	List<Fornecedor> obterFornecedoresNaoReferenciadosComCota(Long idCota);

	/**
	 * Método que retorna uma lista de fornecedores com base no filtro recebido. 
	 * 
	 * @param filtroConsultaFornecedor - Filtro de Fornecedores.
	 * 
	 * @return List<FornecedorDTO>
	 */
	List<FornecedorDTO> obterFornecedoresPorFiltro(FiltroConsultaFornecedorDTO filtroConsultaFornecedor);
	
	/**
	 * Método que retorna uma lista de fornecedores com base no desconto recebido. 
	 * 
	 * @param idDesconto - id do Desconto
	 * 
	 * @return List<FornecedorDTO>
	 */
	List<Fornecedor> obterFornecedoresPorDesconto(Long idDesconto);

	/**
	 * Método que retorna a contagem de fornecedores com base no filtro recebido. 
	 * 
	 * @param filtroConsultaFornecedor - Filtro de Fornecedores.
	 * 
	 * @return Long
	 */
	Long obterContagemFornecedoresPorFiltro(FiltroConsultaFornecedorDTO filtroConsultaFornecedor);
	
	
	/**
	 * Obtem o ID e Nome dos Fornecedores.
	 * @param situacao (Opcional) Situação cadastral dos fornecedores.
	 * @param inferface (Opcional)<code>true</code> apenas fornecedores quem possiem <code>codigoInterface</code>
	 * @return Lista de {@link ItemDTO}} onde <code>key</code> id do {@link Fornecedor}} e <code>value</code> nome do {@link Fornecedor}.
	 */
	public abstract List<ItemDTO<Long, String>> obterFornecedoresIdNome(SituacaoCadastro situacao, Boolean inferface);

	/**
	 * Método que verifica a quantidade de pessoas cadastradas para outros fornecedores.
	 * 
	 * @param idPessoa
	 * @param idFornecedor
	 * @return qtde
	 */
	Integer obterQuantidadeFornecedoresPorIdPessoa(Long idPessoa, Long idFornecedor);
	
	List<Fornecedor> obterFornecedorLikeNomeFantasia(String nomeFantasia);
	
	List<Fornecedor> obterFornecedoresPorId(List<Long> idsFornecedores);
	
	/**
	 * Obtem Fornecedor por codigo
	 * @param codigo
	 * @return Fornecedor
	 */
	Fornecedor obterFornecedorPorCodigo(Integer codigo);
	
	/**
	 * Obtem o maior codigo de interface.
	 * @return
	 */
	public abstract Integer obterMaxCodigoInterface();
	
	
	
	/**
	 * Retorna o menor código interface disponível.
	 * 
	 * @return Integer
	 */
	public Integer obterMinCodigoInterfaceDisponivel();
	
	/**
	 * Retorna PessoaJuridica do Fornecedor pelo IdPessoa.
	 * 
	 * @param idPessoa
	 * @return
	 */
	public List<Fornecedor> obterFornecedoresPorIdPessoa(Long idPessoa);

	public abstract EnderecoFornecedor obterEnderecoPrincipal(long idFornecedor);

	List<Pessoa> obterFornecedorPorNome(String nomeFornecedor);

	List<Pessoa> obterFornecedorPorNomeFantasia(String nomeFantasia);
	
	/**
	 * Obtem Fornecedor Padrao, utilizado para em Movimentos Financeiros sem definição de Distribuidor
	 * @return Fornecedor
	 */
	public Fornecedor obterFornecedorPadrao();

	List<Pessoa> obterFornecedorPorNome(String nomeFornecedor,
			Integer qtdMaxResult);
	
	Fornecedor obterFornecedorPorMovimentoEstoqueCota(Long movimentoEstoqueCotaId);
	
	List<Fornecedor> obterFornecedoresPorSituacaoEOrigem(SituacaoCadastro situacaoCadastro, Origem origem);

	Origem obterOrigemCadastroFornecedor(Long idFornecedor);

	List<Fornecedor> obterFornecedoresDesc();

	public abstract ItemDTO<Long, String> obterNome(Long id);
	
	List<Fornecedor> obterFornecedoresUnificados();

    Fornecedor obterFornecedorPorCodigoJoinJuridica(Integer codigo);

	List<Fornecedor> obterFornecedoresFcDinap(String codigoDinap,
			String codigoFC);

	Integer obterCodigoInterface(Long idFornecedor);

}