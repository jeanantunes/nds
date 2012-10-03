package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheRodapeDTO;
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.util.Intervalo;


public interface MovimentoEstoqueCotaRepository extends Repository<MovimentoEstoqueCota, Long> {
	
	/**
	 * Obtém lista de MovimentoEstoqueCota relativa a uma operação de
	 * conferência de encalhe.
	 * 
	 * @param idControleConferenciaEncalheCota
	 * 
	 * @return List - MovimentoEstoqueCota
	 */
	public List<MovimentoEstoqueCota> obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(Long idControleConferenciaEncalheCota);
	
	/**
	 * Obtém a quantidade de tipos de produtoEdicao da consulta de encalhe.
	 * Caso o parâmetro "indQtdEncalheAposPrimeiroDia" = false a pesquisa ira retornar 
	 * a quantidade de tipos de produtoEdicao do encalhe sumarizada do primeiro dia.
	 * 
	 * 
	 * @param filtro
	 * @param indQtdEncalheAposPrimeiroDia
	 * 
	 * @return Qtde - Integer
	 */
	public Integer obterQtdProdutoEdicaoEncalhe(FiltroConsultaEncalheDTO filtro, boolean indQtdEncalheAposPrimeiroDia);
	
	/**
	 * Obtém a quantidade de itens da consulta de encalhe.
	 * Caso o parâmetro "indQtdEncalheAposPrimeiroDia" = false a pesquisa ira retornar 
	 * a quantidade de itens do encalhe sumarizada do primeiro dia.
	 * 
	 * @param filtro
	 * @param indQtdEncalheAposPrimeiroDia
	 * 
	 * @return Qtde -  BigDecimal
	 */
	public BigDecimal obterQtdItemProdutoEdicaoEncalhe(FiltroConsultaEncalheDTO filtro, boolean indQtdEncalheAposPrimeiroDia);
	
	/**
	 * Pesquisa uma lista de ContagemDevolucao.
	 * 
	 * @param filtro
	 * @param tipoMovimentoEstoque
	 * @param indBuscaTotalParcial
	 * 
	 * @return List - ContagemDevolucao
	 */
	public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(
			FiltroDigitacaoContagemDevolucaoDTO filtro, 
			TipoMovimentoEstoque tipoMovimentoEstoque, 
			boolean indBuscaTotalParcial);
	
	/**
	 * Obtém a quantidade de registros da pesquisa de ContagemDevolucao.
	 * 
	 * @param filtro
	 * @param tipoMovimentoEstoque
	 * 
	 * @return Qtde - Integer
	 */
	public Integer obterQuantidadeContagemDevolucao(
			FiltroDigitacaoContagemDevolucaoDTO filtro, 
			TipoMovimentoEstoque tipoMovimentoEstoque);
	
	
	/**
	 * Obtém o valorTotalGeral da pesquisa de contagemDevolucao 
	 * que corresponde a somatória da seguinte expressão 
	 * (qtdMovimentoEncalhe * precoVendoProdutoEdicao).
	 * 
	 * @param filtro
	 * @param tipoMovimentoEstoque
	 * 
	 * @return valorTotalGeral - BigDecimal
	 */
	public BigDecimal obterValorTotalGeralContagemDevolucao(
			FiltroDigitacaoContagemDevolucaoDTO filtro, 
			TipoMovimentoEstoque tipoMovimentoEstoque);
	
	/**
	 * Obtém o Movimento de Estoque da cota pelo Tipo de Movimento.
	 * 
	 * @param data
	 * @param idCota
	 * @param grupoMovimentoEstoque
	 * 
	 * @return List - MovimentoEstoqueCota
	 */
	List<MovimentoEstoqueCota> obterMovimentoCotaPorTipoMovimento(
			Date data, Long idCota, GrupoMovimentoEstoque grupoMovimentoEstoque);
	
	/**
	 * Obtém a qtde registros da pesquisa de ConsultaEncalhe.
	 * 
	 * @param filtro
	 * 
	 * @return Qtde - Integer
	 */
	public Integer obterQtdConsultaEncalhe(FiltroConsultaEncalheDTO filtro);
	
	/**
	 * Pesquisa lista de ConsultaEncalhe.
	 * 
	 * @param filtro
	 * 
	 * @return List - ConsultaEncalhe
	 */
	public  List<ConsultaEncalheDTO> obterListaConsultaEncalhe(FiltroConsultaEncalheDTO filtro);
	
	
	/**
	 * 
	 * Obtém a quantidade de movimento para determinados produtoEdicao e cota de
	 * acordo com o range de data informado.
	 * 
	 * @param idCota
	 * @param idProdutoEdicao
	 * @param dataInicial
	 * @param dataFinal
	 * @param operacaoEstoque
	 * 
	 * @return BigInteger
	 */
	public BigInteger obterQtdeMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(
			Long idCota,
			Long idProdutoEdicao,
			Date dataInicial, 
			Date dataFinal,
			OperacaoEstoque operacaoEstoque);
	
	/**
	 * Obtem dados referentes ao Mapa de Abastecimento
	 * 
	 * @param filtro
	 * @return List - AbastecimentoDTO
	 */
	public List<AbastecimentoDTO> obterDadosAbastecimento(FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtem quantidade de registros retornados pelo filtro de "obterDadosAbastecimento"
	 * 
	 * @param filtro
	 * @return Long - Quantidade
	 */
	public Long countObterDadosAbastecimento(FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtém detalhes - Mapa de Abastecimento
	 * @param idBox 
	 * 
	 * @param filtro
	 * @return
	 */
	public List<ProdutoAbastecimentoDTO> obterDetlhesDadosAbastecimento(Long idBox, FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtém dados do Mapa de Abastecimento Por Box
	 * 
	 * @param filtro
	 * @return
	 */
	List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorBox(
			FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtém dados do Mapa de Abastecimento por Rota
	 * 
	 * @param filtro
	 * @return
	 */
	List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorBoxRota(
			FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtem quantidade de registros retornados pelo filtro de "obterMapaAbastecimentoPorBoxRota"
	 * 
	 * @param filtro
	 * @return
	 */
	Long countObterMapaAbastecimentoPorBoxRota(FiltroMapaAbastecimentoDTO filtro);
	
	/**
	 * Obtém dados do Mapa de Abastecimento por Produto Edição
	 * 
	 * @param filtro
	 * @return
	 */
	public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorProdutoEdicao(
			FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtem quantidade de registros retornados pelo filtro de "obterMapaAbastecimentoPorProdutoEdicao"
	 * 
	 * @param filtro
	 * @return Long - Quantidade
	 */
	Long countObterMapaAbastecimentoPorProdutoEdicao(FiltroMapaAbastecimentoDTO filtro);
	
	/**
	 * Obtém dados do Mapa de Abastecimento por Cota
	 * 
	 * @param filtro
	 * @return
	 */
	public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorCota(
			FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtem quantidade de registros retornados pelo filtro de "obterMapaAbastecimentoPorCota"
	 * 
	 * @param filtro
	 * @return
	 */
	Long countObterMapaAbastecimentoPorCota(FiltroMapaAbastecimentoDTO filtro);
	
	/**
	 * Obtém dados do Mapa de Abastecimento por com quebra por Cota
	 * 
	 * @param filtro
	 * @return
	 */
	public List<ProdutoAbastecimentoDTO> obterMapaDeImpressaoPorProdutoQuebrandoPorCota(
			FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtem quantidade de registros retornados pelo filtro de "obterMapaDeImpressaoPorProdutoQuebrandoPorCota"
	 * 
	 * @param filtro
	 * @return
	 */
	Long countObterMapaDeImpressaoPorProdutoQuebrandoPorCota(FiltroMapaAbastecimentoDTO filtro);
	
	/**
	 * Obtém Movimento Estoque Cota por parametros.
	 * 
	 * @param idCota id da cota
	 * @param listaGrupoMovimentoEstoques 
	 * @param periodo
	 * @param listaFornecedores
	 * @param listaProduto
	 * @return lista movimento estoque cota
	 */
	public List<MovimentoEstoqueCota> obterMovimentoEstoqueCotaPor(Distribuidor distribuidor, Long idCota, GrupoNotaFiscal grupoNotaFiscal, List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques, Intervalo<Date> periodo, List<Long> listaFornecedores, List<Long> listaProduto);

	/**
	 * Obtém quantidade de produto transferido pela/para cota de acordo com o tipo de movimento
	 * @param idCota - Id da cota
	 * @param idProdutoEdicao - Id do ProdutoEdicao
	 * @return 
	 */
	public Long obterQuantidadeProdutoEdicaoMovimentadoPorCota(Long idCota, Long idProdutoEdicao, Long idTipoMovimento);

	public abstract List<MovimentoEstoqueCota> obterMovimentoEstoqueCotaPor(Distribuidor distribuidor,
			Long idCota, List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques, Intervalo<Date> periodo, List<Long> listaFornecedores);
	/**
	 * Pesquisa lista de ConsultaEncalheDetalhe.
	 * 
	 * @param filtro
	 * @return
	 */
	public List<ConsultaEncalheDetalheDTO> obterListaConsultaEncalheDetalhe(FiltroConsultaEncalheDetalheDTO filtro);
	
	/**
	 * Obtém a qtde registros da pesquisa de ConsultaEncalheDetalhe.
	 * 
	 * @param filtro
	 * @return
	 */
	public Integer obterQtdeConsultaEncalheDetalhe(FiltroConsultaEncalheDetalheDTO filtro);

	/**
	 * Obtém o valores totais.
	 * 
	 * @param filtro
	 * @return
	 */
	public ConsultaEncalheRodapeDTO obterValoresTotais(FiltroConsultaEncalheDTO filtro);

	/**
	 * Obtém Dados para Grid de Mapa por Entregador
	 * 
	 * @param filtro
	 * @return
	 */
	public List<ProdutoAbastecimentoDTO> obterMapaDeAbastecimentoPorEntregador(
			FiltroMapaAbastecimentoDTO filtro);
	/**
	 * Count  de mapa por entregador
	 * 
	 * @param filtro
	 * @return
	 */
	public Long countObterMapaDeAbastecimentoPorEntregador(
			FiltroMapaAbastecimentoDTO filtro);
	

	/**
	 * Obtém dados do Mapa de Abastecimento por Entregador
	 * 
	 * @param filtro
	 * @return
	 */
	public List<ProdutoAbastecimentoDTO> obterMapaDeImpressaoPorEntregador(FiltroMapaAbastecimentoDTO filtro);
}
