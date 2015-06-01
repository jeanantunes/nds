package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.abril.nds.dto.AnaliseHistogramaDTO;
import br.com.abril.nds.dto.DataCEConferivelDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroHistogramaVendas;
import br.com.abril.nds.dto.filtro.FiltroHistoricoVendaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.ItemAutoComplete;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.ProdutoEdicao}
 * 
 * @author Discover Technology
 */
public interface ProdutoEdicaoRepository extends Repository<ProdutoEdicao, Long> {
	
	/**
	 * Obtém o codigoSM a partir do idProdutoEdicao e 
	 * dataRecolhimento
	 * 
	 * @param idProdutoEdicao
	 * @param dataRecolhimento
	 * @param numeroCota
	 * 
	 * @return Integer
	 */
	public Integer obterCodigoMatrizPorProdutoEdicao(Long idProdutoEdicao, Date dataRecolhimento, Integer numeroCota);
	
	
	/**
	 * Obtém produtos edição de acordo com o nome do produto.
	 * 
	 * @param nomeProduto - nome do produto
	 * 
	 * @return {@link List<ProdutoEdicao>}
	 */
	List<ProdutoEdicao> obterProdutoEdicaoPorNomeProduto(String nomeProduto);
	
	/**
	 * Obtém um furo do produto de acordo com os parâmetros informados.
	 * 
	 * @param codigo - código do produto
	 * @param nomeProduto - nome do produto
	 * @param edicao - número da edição
	 * @param dataLancamento - data de lançamento
	 * 
	 * @return {@link FuroProdutoDTO}
	 */
	FuroProdutoDTO obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
			String codigo, String nomeProduto, Long edicao, Date dataLancamento, boolean furado);
	
	/**
	 * Obtém um produto de edição de acordo com o 
	 * código do produto e o número da edição.
	 * 
	 * @param codigoProduto - nome do produto
	 * @param numeroEdicao - número da edição
	 * 
	 * @return {@link ProdutoEdicao}
	 */
	ProdutoEdicao obterProdutoEdicaoPorCodProdutoNumEdicao(String codigoProduto,
														   Long numeroEdicao);
	
	
	ProdutoEdicao obterMaxProdutoEdicaoPorCodProdutoNumEdicao(String codigoProduto, Long numeroEdicao);
	
	/**
	 * Obtém produtos edição de acordo com o 
	 * produto e o produto edição.
	 * 
	 * @param produto - produto
	 * @param produtoEdicao - produto edição
	 * 
	 * @return {@link List<ProdutoEdicao>}
	 */
	List<ProdutoEdicao> obterListaProdutoEdicao(Produto produto, ProdutoEdicao produtoEdicao);

	/**
	 * Obtém edições do produto 
	 * 
	 * @param codigoProduto - código do produto
	 * @return Lista de edições do produto
	 */
	List<ProdutoEdicao> obterProdutosEdicaoPorCodigoProduto(String codigoProduto);

	/**
	 * Obtém edições com desconto do produto
	 * 
	 * @param codigoProduto - código do produto
	 * @return Lista de edições do produto
	 */
	List<TipoDescontoProdutoDTO> obterProdutosEdicoesPorCodigoProdutoComDesconto(String codigoProduto);
	
	List<ProdutoEdicao> obterProdutoEdicaoPorCodigoBarra(String codigoBarra);

	ProdutoEdicao obterProdutoEdicaoPorSM(Long sm);
	
	List<ProdutoEdicao> obterProdutoPorCodigoNomeCodigoSM(
			final Integer codigoSM,
			final String codigoNomeProduto, 
			final Integer numeroCota, 
			final Integer quantidadeRegisttros,
			final Map<Long, DataCEConferivelDTO> mapaDataCEConferivel,
			final Date dataOperacao,
			final boolean indAceitaRecolhimentoParcialAtraso);
	
	/**
	 * Obtém uma lista de produtos edição de acordo com o parâmetro iformado.
	 * 
	 * @param idsProdutoEdicao - identificadores de produto edição
	 * 
	 * @return {@link List<ProdutoEdicao>}
	 */
	List<ProdutoEdicao> obterProdutosEdicaoPorId(Set<Long> idsProdutoEdicao);
	
	/**
	 * Pesquisa as Edições já cadastradas e traz o resultado ordenado.<br>
	 * Possui como opções de filtro:<br>
	 * <ul>
	 * <li>Código do Produto;</li>
	 * <li>Nome do Produto;</li>
	 * <li>Data de Lançamento;</li>
	 * <li>Situação do Lançamento;</li>
	 * <li>Código de Barra da Edição;</li>
	 * <li>Contém brinde;</li>
	 * </ul>
	 * 
	 * @param dto
	 * @param sortorder
	 * @param sortname
	 * @param initialResult
	 * @param maxResults
	 * 
	 * @return
	 */
	public List<ProdutoEdicaoDTO> pesquisarEdicoes(String codigoProduto, String nomeProduto,
			Intervalo<Date> dataLancamento, Intervalo<Double> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde,
			String sortorder, String sortname, int initialResult, int maxResults);
	
	/**
	 * Obtém a quantidade de edições cadastradas filtradas pelas opções 
	 * escolhidos pelo usuário na tela de pesquisa.
	 * 
	 * @param dto
	 * @return
	 */
	public Integer countPesquisarEdicoes(String codigoProduto, String nomeProduto,
			Intervalo<Date> dataLancamento, Intervalo<Double> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde);
	
	/**
	 * Pesquisa as últimas edições cadastradas.<br>
	 * 
	 * @param codigoProduto
	 * @param qtdEdicoes Quantidade de Edições a trazer.
	 * 
	 * @return
	 */
	public List<ProdutoEdicaoDTO> pesquisarUltimasEdicoes(String codigoProduto,
			int qtdEdicoes);
	
	/**
	 * Verifica se existe alguma Edição já cadastrada para o produto.
	 * 
	 * @param produto
	 * 
	 * @return 
	 */
	public boolean hasProdutoEdicao(Produto produto);
	
	/**
	 * Verifica se o ProdutoEdicao já foi publicado pela distribuidora.<br>
	 * Irá verificar se a Data de Publicação da Distribuida é maior que a 
	 * "Data do Dia Corrente (hoje)" do Lançamento 
	 * ({@link br.com.abril.nds.model.planejamento.Lancamento})
	 * com a data de Distribuição mais antiga.
	 *  
	 * @param idProdutoEdicao ID da Edição. (Se for passado <i>null</i> será
	 * considerado 0 (zero).
	 * 
	 * @return
	 * <ul>
	 * <li>true: A Edição já foi publicada (DataPublicacaoEditora <= DataCorrente);</li>
	 * <li>false: A Edição ainda não foi publicada (DataPublicacaoEditora > DataCorrente);</li>
	 * </ul>
	 */
	public boolean isProdutoEdicaoJaPublicada(Long idProdutoEdicao);
	
	/**
	 * Verifica se o número da edição já foi cadastrado para outra edição 
	 * de um mesmo Produto. 
	 * 
	 * @param idProduto Código do Produto.
	 * @param numeroEdicao Número da Edição a ser verificada.
	 * @param idProdutoEdicao ID da Edição (caso exista).
	 * 
	 * @return
	 */
	public boolean isNumeroEdicaoCadastrada(Long idProduto, 
			Long numeroEdicao, Long idProdutoEdicao);
	
	/**
	 * Obtém todos os Produtos Edições que possuem exatamente o mesmo
	 * código de barra.
	 *  
	 * @param codigoDeBarras
	 * @param idProdutoEdicao
	 * 
	 * @return
	 */
	public List<ProdutoEdicao> obterProdutoEdicaoPorCodigoDeBarra(String codigoDeBarras,
			Long idProdutoEdicao);
	
	/**
	 * Obtém produtoEdicao por (produto e numeroEdicao) ou nome
	 * @param idProduto
	 * @param numeroEdicao
	 * @param nome
	 * @return ProdutoEdicao
	 */
	ProdutoEdicao obterProdutoEdicaoPorProdutoEEdicaoOuNome(Long idProduto,
														    Long numeroEdicao,
														    String nome);
	
	/**
	 * Obtém a quantidade de edições cadastradas para um determinado produto.
	 * 
	 * @param codigoProduto - Código do produto a ser pesquisado.
	 * 
	 * @return - Quantidade de produtos edições.
	 */
	Integer obterQuantidadeEdicoesPorCodigoProduto(String codigoProduto);
	
	/**
	 * 
	 * 
	 * @param codigoProduto
	 * @param limite
	 * 
	 * @return
	 */
	List<ProdutoEdicao> obterProdutosEdicoesPorCodigoProdutoLimitado(String codigoProduto, Integer limite);
	
	/**
	 * Obtém produtos relacionados a um fornecedor.
	 * 
	 * @param idFornecedor - id do fornecedor
	 * 
	 * @return {@link Set} de {@link ProdutoEdicao}
	 */
	Set<ProdutoEdicao> obterProdutosEdicaoPorFornecedor(Long idFornecedor);

	List<ProdutoEdicao> buscarProdutosLancadosData(Date data);

	public String buscarNome(Long long1);
	
	/**
	 * Obtém o último número da edição
	 * @param idProduto
	 * 
	 * @return numeroEdicao
	 */
	public Long obterUltimoNumeroEdicao(Long idProduto);

	/**
	 * Retorna os produtoEdicao de distribuidores que não estão sendo utilizados no sistema (e consequentemente podem ser alterados)
	 * @param fornecedores
	 * @return
	 */
	public Set<ProdutoEdicao> filtrarDescontoProdutoEdicaoPorDistribuidor(Set<Fornecedor> fornecedores);

	/**
	 * Retorna os produtoEdicao de cotas que não estão sendo utilizados no sistema (e consequentemente podem ser alterados)
	 * @param fornecedores
	 * @return
	 */
	public Set<ProdutoEdicao> filtrarDescontoProdutoEdicaoPorCota(Cota cota, Set<Fornecedor> fornecedores);

	public List<ProdutoEdicaoDTO> obterEdicoesProduto(FiltroHistoricoVendaDTO filtro);
	
	public ProdutoEdicaoDTO obterHistoricoProdutoEdicao(String codigoProduto, Long numeroEdicao, Integer numeroCota);

	/**
	 * Retorna os produtoEdicao de produtos que não estão sendo utilizados no sistema (e consequentemente podem ser alterados)
	 * @param fornecedores
	 * @return
	public Set<ProdutoEdicao> filtrarDescontoProdutoEdicaoPorProduto(Produto produto);*/

	public List<EdicoesProdutosDTO> obterHistoricoEdicoes(FiltroHistogramaVendas filtro);
	
	/**
	 * Retorna o produtoEdicao associado a um ID Lancamento
	 * @param idLancamento
	 * @return
	 */
	public ProdutoEdicao obterProdutoEdicaoPorIdLancamento(Long idLancamento);
	
	/**
	 * 
	 * @param filtro
	 * @param codigoProduto
	 * @param de
	 * @param ate
	 * @param edicoes
	 * @return
	 */
	public AnaliseHistogramaDTO obterBaseEstudoHistogramaPorFaixaVenda(FiltroHistogramaVendas filtro,String codigoProduto,Integer de,Integer ate, String[] edicoes);

	/**
	 * Retorna o percentual de desconto logistica de um produto edição
	 * 
	 * @param idPropdutoEdicao
	 * @return BigDecimal
	 */
	BigDecimal obterDescontoLogistica(Long idPropdutoEdicao);

	public abstract List<ItemAutoComplete> obterPorCodigoBarraILike(String codigoBarra);
	
	/**
	 * Verifica se uma edição é parcial
	 * 
	 * @param idProdutoEdicao
	 * 
	 * @return Boolean
	 */
	public Boolean isEdicaoParcial(Long idProdutoEdicao);
	
	public Boolean estudoPodeSerSomado(Long idEstudoBase, String codigoProduto);
	
	ProdutoEdicaoDTO findReparteEVenda(ProdutoEdicaoDTO dto);

	List<ProdutoEdicaoDTO> findReparteEVenda(
			List<ProdutoEdicaoDTO> produtosEdicao);

	void insereVendaRandomica(ProdutoEdicao produtoEdicao);

	List<ProdutoEdicao> listProdutoEdicaoPorCodProdutoNumEdicoes(
			String codigoProduto, Long numeroEdicaoInicial,
			Long numeroEdicaoFinal);

    boolean isEdicaoAberta(Long produtoEdicaoId);
    
    public List<Long> obterNumeroDas6UltimasEdicoesFechadas(Long idProduto);
    
    public List<Long> obterNumeroDas6UltimasEdicoesFechadasPorICD(String codigoICD);

	public BigDecimal obterVendaEsmagadaMedia(String codigoProduto, Integer numeroEdicao, Integer numeroCotaEsmagada);

	int obterQtdEdicoesPresentes(String codigoProduto, String[] edicoes,
			Integer numeroCotaEsmagada);

	public boolean cotaTemProduto(String codigoProduto, Integer numeroEdicao,
			Integer numeroCota);

    public List<ItemDTO<String, String>> obterProdutosBalanceados(Date dataLancamento);
    
    BigDecimal obterPrecoProdutoEdicao(String codigoProduto, Long numeroEdicao);


	public abstract ProdutoEdicaoDTO obterHistoricoProdutoEdicaoParcial(Long idProdutoEdicao,
			Integer numeroPeriodo, Integer numeroCota, Long numeroEdicao);


	public abstract String obterStatusLancamentoPeriodoParcial(Long idProdutoEdicao,
			Integer numeroPeriodo, Long numeroEdicao);
}