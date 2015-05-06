package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.dto.MovimentoEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroExtratoEdicaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;

public interface MovimentoEstoqueRepository extends Repository<MovimentoEstoque, Long> {

	public List<ExtratoEdicaoDTO> obterListaExtratoEdicao(FiltroExtratoEdicaoDTO filtroExtratoEdicao, StatusAprovacao statusAprovacao);
	
	public BigInteger obterReparteDistribuidoProduto(String codigoProduto);
	
	/**
	 * Obtem valor total de Consignado ou AVista da data
	 * @param data
	 * @param operacaoEstoque
	 * @param formaComercializacao
	 * @return BigDecimal
	 */
	BigDecimal obterSaldoDistribuidor(Date data, OperacaoEstoque operacaoEstoque, FormaComercializacao formaComercializacao);

	List<MovimentoEstoque> obterMovimentoEstoquePorIdProdutoEdicao(ProdutoEdicao produtoEdicao);

	/**
	 * Obtem valor total de Consignado ou AVista da data
	 * @param data
	 * @param formaComercializacao
	 * @return BigDecimal
	 */
	BigDecimal obterSaldoDistribuidorEntrada(Date data, FormaComercializacao formaComercializacao);
	
	public MovimentoEstoque obterMovimentoEstoqueDoItemNotaFiscal(Long idItemNotaFiscal, TipoMovimentoEstoque tipoMovimento); 
	
	/**
	 * Obtém uma lista de movimentos de reparte promocional
	 * (inseridos durante o recebimento fisico), cujo os movimentos
	 * de recebimento fisico relativos não tenham sido extornados;
	 * 
	 * @param idProdutoEdicao
	 * @param grupoMovimentoEstoqueRepartePromocional
	 * @param grupoMovimentoEstoqueEstornoRecebimentoFisico
	 * 
	 * @return List - Long
	 */
	public List<Long> obterMovimentosRepartePromocionalSemEstornoRecebimentoFisico(
			Long idProdutoEdicao,
			GrupoMovimentoEstoque grupoMovimentoEstoqueRepartePromocional,
			GrupoMovimentoEstoque grupoMovimentoEstoqueEstornoRecebimentoFisico);
	
	BigInteger buscarSomaEstoqueJuramentadoPorProdutoData(Long idProdutoEdicao, Date data);

	public abstract MovimentoEstoqueDTO findByIdConferenciaEncalhe(Long idConferenciaEncalhe);

	public abstract void updateById(Long id, BigInteger qtde);
	
	BigDecimal obterSaldoDeReparteExpedido(final Date dataMovimento, boolean precoCapaHistoricoAlteracao);
	
	BigDecimal obterValorConsignadoDeVendaEncalheSuplementar(final Date dataMovimentacao );

	BigDecimal obterValorAlteracoesPrecosExpedicoesAnteriores(Date dataFechamento);

}