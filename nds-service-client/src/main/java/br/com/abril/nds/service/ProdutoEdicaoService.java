package br.com.abril.nds.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.abril.nds.dto.AnaliseHistogramaDTO;
import br.com.abril.nds.dto.ConsultaProdutoEdicaoDTO;
import br.com.abril.nds.dto.DataCEConferivelDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO.ModoTela;
import br.com.abril.nds.dto.filtro.FiltroHistogramaVendas;
import br.com.abril.nds.dto.filtro.FiltroHistoricoVendaDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.ItemAutoComplete;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.ProdutoEdicao}
 * 
 * @author Discover Technology
 */
public interface ProdutoEdicaoService {

	/**
	 * Obtém o ProdutoEdição.
	 * 
	 * @param idProdutoEdicao
	 * @param indCarregaFornecedores
	 * 
	 * @return
	 */
	public ProdutoEdicao obterProdutoEdicao(Long idProdutoEdicao, boolean indCarregaFornecedores);
	
	/**
	 * Valida informações básicas do produto edição e devolve
	 * uma lista de {@code String} caso incosistências sejam 
	 * encontradas.
	 * 
	 * @param dto
	 * @param codigoProduto
	 * 
	 * @return List - String
	 */
	public List<String> validarDadosBasicosEdicao(ProdutoEdicaoDTO dto, String codigoProduto,ProdutoEdicaoDTO dtoAnterior);

	
	/**
	 * Obtém os produtos edição de acordo com o nome do produto.
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
	 * Obtém um produtos edição de acordo
	 * com o código do produto e o número da edição.
	 * 
	 * @param codigoProduto - código do produto
	 * @param numeroEdicao - número da edição
	 * 
	 * @return {@link ProdutoEdicao}
	 */
	ProdutoEdicao obterProdutoEdicaoPorCodProdutoNumEdicao(String codigoProduto, String numeroEdicao);

	/**
	 * Obtém edições do produto 
	 * 
	 * @param codigoProduto - código do produto
	 * @return Lista de edições do produto
	 */
	List<ProdutoEdicao> obterProdutosEdicaoPorCodigoProduto(String codigoProduto);

	List<ProdutoEdicao> obterProdutoPorCodigoNomeParaRecolhimento(
			String codigoNomeProduto, 
			Integer numeroCota, 
			Integer quantidadeRegistros,
			Map<Long, DataCEConferivelDTO> mapaDataCEConferivel);
	
	
	/**
	 * Altera um produtoEdicao
	 * 
	 * @param ProdutoEdicao produtoEdicao
	 * @return void
	 */
	void alterarProdutoEdicao(ProdutoEdicao produtoEdicao);
	
	/**
	 * Pesquisa as Edições já cadastradas.<br>
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
	 * @param page
	 * @param maxResults
	 * 
	 * @return
	 */
	public List<ProdutoEdicaoDTO> pesquisarEdicoes(String codigoProduto, String nomeProduto,
			Intervalo<Date> dataLancamento, Intervalo<Double> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde,
			String sortorder, String sortname, int page, int maxResults);
	
	public List<ConsultaProdutoEdicaoDTO> pesquisarConsultaEdicoes(String codigoProduto, String nomeProduto,
			Intervalo<Date> dataLancamento, Intervalo<Double> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde,
			String sortorder, String sortname, int page, int maxResults);
	
	/**
	 * Obtém a quantidade de edições cadastradas filtradas pelos critérios 
	 * escolhidos pelo usuário.
	 * 
	 * @param dto
	 * 
	 * @return
	 */
	public Long countPesquisarEdicoes(String codigoProduto, String nomeProduto,
			Intervalo<Date> dataLancamento, Intervalo<Double> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde);
	
	/**
	 * Salva ou Atualiza um novo ProdutoEdição.
	 * 
	 * @param dto
	 * @param codigoProduto
	 * @param contentType
	 * @param imgInputStream
	 * @param istrac29
	 * @param modoTela
	 */
	public void salvarProdutoEdicao(
	        ProdutoEdicaoDTO dto, String codigoProduto, String contentType, 
	        InputStream imgInputStream,boolean istrac29, ModoTela modoTela);
	
	/**
	 * Exclui uma Edição da base de dados.<br>
	 * Os critérios para exclusão são:
	 * <ul>
	 * <li>A Edição não pode ser cadastrado via INTERFACE;</li>
	 * <li>A Edição não pode estar sendo utilizada em outras partes dos sistema;</li>
	 * </ul>
	 * 
	 * @param idProdutoEdicao
	 * 
	 * @exception 
	 */
	public void excluirProdutoEdicao(Long idProdutoEdicao) throws UniqueConstraintViolationException;
	
	/**
	 * @return TODO
	 * Exclui uma Edição da base de dados.<br>
	 * Os critérios para exclusão são:
	 * <ul>
	 * <li>A Edição não pode ser cadastrado via INTERFACE;</li>
	 * <li>A Edição não pode estar sendo utilizada em outras partes dos sistema;</li>
	 * </ul>
	 * 
	 * @param idProdutoEdicao
	 * 
	 * @exception 
	 */
	public Map<String, String> isProdutoEdicaoValidoParaRemocao(Long idProdutoEdicao) throws Exception;
	
	/**
	 * Retorna um produto edição dado seu código de barras
	 * @param codigoBarras - código de barras
	 * @return  ProdutoEdicao
	 */
	List<ProdutoEdicao> buscarProdutoPorCodigoBarras(String codigoBarras);
	
	/**
	 * Retorna o DTO produtoEdicao
	 * @param codigoProduto
	 * @param idProdutoEdicao
	 * @param redistribuicao
	 * @param situacaoProdutoEdicao 
	 * 
	 * @return
	 */
	public ProdutoEdicaoDTO obterProdutoEdicaoDTO(String codigoProduto, Long idProdutoEdicao, boolean redistribuicao, String situacaoProdutoEdicao);

	public ProdutoEdicao buscarPorID(Long idProdutoEdicao);
	
	/**
	 * Obtém a porcentagem de desconto de um produto edição, 
	 * respeitando a regra de prioridade
	 * 
	 * @param produtoEdicao
	 * @return
	 */
	public BigDecimal obterPorcentualDesconto(ProdutoEdicao produtoEdicao);

	/**
	 * Obtém a lista de ProdutoEdicaoDTO de acordo com o FiltroHistoricoVendaDTO
	 * @param filtro
	 * @return List ProdutoEdicaoDTO
	 * 
	 */
	public List<ProdutoEdicaoDTO> obterEdicoesProduto(FiltroHistoricoVendaDTO filtro);
	
	public List<EdicoesProdutosDTO> obterHistoricoEdicoes(FiltroHistogramaVendas filtro);
	
	public List<AnaliseHistogramaDTO> obterBaseEstudoHistogramaPorFaixaVenda(FiltroHistogramaVendas filtro,String codigoProduto,String[] faixasVenda, String[] edicoes);

	public abstract List<ItemAutoComplete> obterPorCodigoBarraILike(String codigoBarra);

	void insereVendaRandomica(String codigoProduto, Integer numeroEdicao);

    Integer obterNumeroLancamento(Long idProdutoEdicao, Long idPeriodo);
 
    public void tratarInformacoesAdicionaisProdutoEdicaoArquivo(ProdutoEdicaoDTO prodEdicao);
    
    List<ProdutoEdicao> obterProdutosEdicaoPorId(Set<Long> idsProdutoEdicao);

    List<ItemDTO<String, String>> obterProdutosBalanceados(Date dataLancamento);
	
    BigDecimal obterPrecoEdicaoParaAlteracao(final String codigoProduto,final Long numeroEdicao);

	void executarAlteracaoPrecoCapa(final String codigo,final Long numeroEdicao,final BigDecimal precoProduto);
}
