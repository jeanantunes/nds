package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.RelatorioTiposProdutosDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioTiposProdutos;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoProduto}
 * 
 * @author Discover Technology
 *
 */
public interface TipoProdutoService {

	/**
	 * Remove o Tipo Produto do repositório de dados.
	 * @param id
	 * @throws UniqueConstraintViolationException 
	 * @throws ValidacaoException Lança quando o Tipo de Produto em questão não pode ser removido.
	 */
	public abstract void remover(Long id) throws UniqueConstraintViolationException, ValidacaoException;
	
	/**
	 * Atualiza a entidade tipo produto no repositorio de dados.
	 * @param entity entidade a ser persistida.
	 * @return o TipoProduto que foi persistido.
	 * @throws ValidacaoException quando o Tipo de Produto em questão não pode ser editado.
	 */
	public abstract TipoProduto merge(TipoProduto entity) throws ValidacaoException;
	
	/**
	 * Busca os tipos de produtos respeitando as restricoes parametrizadas.
	 * @param nomeTipoProduto nome do tipo do produto
	 * @param codigo
	 * @param codigoNCM
	 * @param codigoNBM
	 * @param orderBy nome do campo para compor a ordenção
	 * @param ordenacao tipo da ordenção
	 * @param initialResult resultado inicial
	 * @param maxResults numero máximo de resultados
	 * @return lista de Tipos de Produtos.
	 */
	public abstract List<TipoProduto> busca(String nomeTipoProduto, Long codigo, String codigoNCM, String codigoNBM, String orderBy, 
			Ordenacao ordenacao, int initialResult, int maxResults);
	
	/**
	 * Obtem a quantidade de registros respeitando as restricoes parametrizadas.
	 * @param nomeTipoProduto
	 * @param codigo
	 * @param codigoNCM
	 * @param codigoNBM
	 * @return
	 */
	public abstract Long quantidade(String nomeTipoProduto, Long codigo, String codigoNCM, String NBM);
	
	/**
	 * Obtém tipo produto por id.
	 * @param id 
	 * @return tipoProduto
	 */
	public abstract TipoProduto buscaPorId(Long id);
	
	/**
	 * Obtem um codigo sugerido pelo sistema para ser cadastrado no tipo produto
	 * @return codigo
	 */
	public abstract String getCodigoSugerido();
	
	/**
	 * Obtem lista de NCM
	 * @return List<NCM>
	 */
	public abstract List<NCM> obterListaNCM();
	
	/**
	 * Obtem NCM por id
	 * @return NCM
	 */
	public abstract NCM obterNCMporId(Long idNcm);
	
	/**
	 * Obtem NCM por codigo
	 * @return NCM
	 */
	public abstract NCM obterNCMporCodigo(Long codigoNcm);
	
	List<RelatorioTiposProdutosDTO> gerarRelatorio(FiltroRelatorioTiposProdutos filtro);
	
	Long obterQunatidade(FiltroRelatorioTiposProdutos filtro);

	List<TipoProduto> obterTiposProdutoDesc();
}
