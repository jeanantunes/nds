package br.com.abril.nds.service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.abril.nds.dto.CotaDescontoProdutoDTO;
import br.com.abril.nds.dto.DescontoEditorDTO;
import br.com.abril.nds.dto.DescontoProdutoDTO;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.TipoDescontoEditorDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;


/**
 * Interface que define serviços referentes aos desconto do sistema
 * 
 */
public interface DescontoService {

	List<TipoDescontoDTO> buscarTipoDesconto(FiltroTipoDescontoDTO filtro);
	
	Integer buscarQntTipoDesconto(FiltroTipoDescontoDTO filtro);
	
	List<TipoDescontoCotaDTO> buscarTipoDescontoCota(FiltroTipoDescontoCotaDTO filtro);
	
	Integer buscarQuantidadeTipoDescontoCota(FiltroTipoDescontoCotaDTO filtro);
	
	List<TipoDescontoProdutoDTO> buscarTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro);
	
	Integer buscarQuantidadeTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro);
	
	/**
	 * Exclui um tipo de desconto, respeitando a data vigente desse desconto.
	 * 
	 * @param idDesconto - identificador do desconto
	 * @param tipoDesconto - tipo de desconto selecionado
	 */
	void excluirDesconto(Long idDesconto, br.com.abril.nds.model.cadastro.desconto.TipoDesconto tipoDesconto);
	
	/**
	 * Inclui um desconto geral para diversos fornecedores.
	 * 
	 * @param desconto - valor do desconto
	 * @param fornecedores - fornecedores associados
	 * @param usuario - usuario
	 */
	void incluirDescontoDistribuidor(BigDecimal desconto, List<Long> fornecedores,Usuario usuario);
	
	/**
	 * Inclui um desconto especifico para uma determindad cota.
	 * 
	 * @param valorDesconto - valor do desconto
	 * @param fornecedores - fornecedores associados
	 * @param numeroCota - número da cota
	 * @param usuario - usuario
	 */
	void incluirDescontoCota(BigDecimal valorDesconto, List<Long> fornecedores,Integer numeroCota,Usuario usuario);
	
	/**
	 * Retorna os fornecedores associados a um desconto.
	 * 
	 * @param idDesconto - identificador do desconto
	 * @param tipoDesconto - tipo de desconto
	 * @return List<Fornecedor>
	 */
	List<Fornecedor> buscarFornecedoresAssociadosADesconto(Long idDesconto, br.com.abril.nds.model.cadastro.desconto.TipoDesconto tipoDesconto);

	/**
	 * Método que realiza a inclusão de um tipo de desconto para produto.
	 * 
	 * @param desconto - Desconto Produto
	 * 
	 * @param usuario - Usuário.
	 */
	void incluirDescontoProduto(DescontoProdutoDTO desconto, Usuario usuario);	

	/**
	 * Método que retorna uma coleção com as cotas relacionadas ao tipo de desconto especificado.
	 * 
	 * @param idDescontoProduto - ID do Tipo de desconto.
	 * 
	 * @return - List<CotaDescontoProdutoDTO> - As cotas relacionadas.
	 */
	List<CotaDescontoProdutoDTO> obterCotasDoTipoDescontoProduto(Long idDescontoProduto, Ordenacao ordenacao);
	
	void processarDescontoDistribuidor(Set<Fornecedor> fornecedores, BigDecimal valorDesconto);
	
	void processarDescontoDistribuidor(BigDecimal valorDesconto);
	
	void processarDescontoCota(Cota cota,Set<Fornecedor> fornecedores, BigDecimal valorDesconto);
	
	void processarDescontoCota(Cota cota,BigDecimal valorDesconto);
	
	void processarDescontoProduto(Set<ProdutoEdicao> produtos,Set<Cota> cotas, BigDecimal valorDesconto, Boolean descontoPredominante);
	
	void processarDescontoProduto(ProdutoEdicao produto,BigDecimal valorDesconto, Boolean descontoPredominante);
	
	/**
	 * Método que retorna uma coleção de dados referentes aos tipos de desconto por produto cadastrados
	 * para determinada cota.
	 * 
	 * @param idCota - ID da Cota.
	 * 
	 * @return - List<TipoDescontoProdutoDTO>.
	 */
	List<TipoDescontoProdutoDTO> obterTiposDescontoProdutoPorCota(Long idCota, String sortorder, String sortname);
	
	/**
	 * Recupera o percentual de desconto a ser aplicado para o produto edição de acordo com o 
	 * tipo de produto
	 * 
	 * @param lancamento TODO
	 * @param cota Cota para recuperação do percentual de desconto
	 * @param produtoEdicao produto edição para recuperação do percentual de desconto
	 * 
	 * @return percentual de desconto a ser utilizado
	 */
	BigDecimal obterValorDescontoPorCotaProdutoEdicao(Lancamento lancamento, Long idCota, ProdutoEdicao produtoEdicao);
	
	/**
	 * Recupera o Desconto a ser aplicado para o produto edição de acordo com o tipo de produto
	 * 
	 * @param lancamento
	 * @param cota
	 * @param produtoEdicao
	 * @return
	 */
	Desconto obterDescontoPorCotaProdutoEdicao(Lancamento lancamento, Long idCota, ProdutoEdicao produtoEdicao);

	BigDecimal obterComissaoCota(Integer numeroCota);

	/**
	 * Recupera descontos Gerais e Específicos da cota mesclados respeitando a hierarquia. (Especifico>Geral)
	 * @param cota
	 * @param sortorder
	 * @param sortname
	 * @return
	 */
	List<TipoDescontoDTO> obterMergeDescontosEspecificosEGerais(Cota cota,
			String sortorder, String sortname);
		

	BigDecimal obterComissaoParametroDistribuidor();
	
	/**
	 * Recupera o Map de Descontos a serem aplicado para o produto edição
	 * @return
	 */
	Map<String, DescontoDTO> obterDescontosMapPorLancamentoProdutoEdicao(Date data);
	
	/**
	 * @param descontos
	 * @param cotaId
	 * @param fornecedorId
	 * @param editorId TODO
	 * @param produtoId
	 * @param produtoEdicaoId
	 * @return
	 * @throws Exception
	 */
	DescontoDTO obterDescontoPor(Map<String, DescontoDTO> descontos, Long cotaId, Long fornecedorId, Long editorId, Long produtoId, Long produtoEdicaoId) throws Exception;

	DescontoDTO obterDescontoProximosLancamentosPor(Map<String, DescontoDTO> descontos, Long cotaId, Long fornecedorId, Long produtoEdicaoId, Long produtoId);

    DescontoDTO obterDescontoPor(Integer numeroCota, String codigoProduto, Long numeroEdicao) throws Exception;

    DescontoDTO obterDescontoProximosLancamentosPorDeTodasCotas(Map<String, DescontoDTO> descontos, Long fornecedorId,
            Long produtoEdicaoId, Long produtoId);

    Map<String, DescontoDTO> obterDescontosMapPorLancamentoProdutoEdicao();

	List<TipoDescontoEditorDTO> buscarTipoDescontoEditor(FiltroTipoDescontoEditorDTO filtro);

	Integer buscarQuantidadeTipoDescontoEditor(FiltroTipoDescontoEditorDTO filtro);

	void incluirDescontoEditor(DescontoEditorDTO descontoDTO, List<Long> cotas, Usuario usuarioLogado);

	List<Cota> buscarCotasAssociadasAoDescontoEditor(Long idDesconto, TipoDesconto tipoDesconto);

	List<CotaDescontoProdutoDTO> carregarCotasPorRegiao(Long idRegiao, String sortorder);

}