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
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaGenericoDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.util.Intervalo;


public interface MovimentoEstoqueCotaRepository extends Repository<MovimentoEstoqueCota, Long> {
	
	/**
	 * Obtém as qtdes de devolução de encalhe juramentado
	 * agrupados por cota relativos a uma data de operação.
	 * 
	 * @param dataOperacao
	 * 
	 * @return List<MovimentoEstoqueCotaGenericoDTO>
	 */
	public List<MovimentoEstoqueCotaGenericoDTO> obterListaMovimentoEstoqueCotaDevolucaoJuramentada(Date dataOperacao);
	
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
	 * Pesquisa uma lista de ContagemDevolucao.
	 * 
	 * @param filtro
	 * @param indBuscaTotalParcial
	 * 
	 * @return List - ContagemDevolucao
	 */
	public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(
			FiltroDigitacaoContagemDevolucaoDTO filtro, boolean indBuscaTotalParcial);
	
	/**
	 * Obtém a quantidade de registros da pesquisa de ContagemDevolucao.
	 * 
	 * @param filtro
	 * 
	 * @return Qtde - Integer
	 */
	public Integer obterQuantidadeContagemDevolucao(
			FiltroDigitacaoContagemDevolucaoDTO filtro);
	
	/**
	 * Obtém a qtde registros da pesquisa de ConsultaEncalhe.
	 * 
	 * @param filtro
	 * 
	 * @return Qtde - Integer
	 */
	public Integer obterQtdeConsultaEncalhe(FiltroConsultaEncalheDTO filtro);
	
	/**
	 * Obtém o valorTotalGeral da pesquisa de contagemDevolucao 
	 * que corresponde a somatória da seguinte expressão 
	 * (qtdMovimentoEncalhe * precoVendoProdutoEdicao).
	 * 
	 * @param filtro
	 * 
	 * @return valorTotalGeral - BigDecimal
	 */
	public BigDecimal obterValorTotalGeralContagemDevolucao(
			FiltroDigitacaoContagemDevolucaoDTO filtro);
	
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
	 * Obtém o valor total do encalhe para a cota (caso específicada)
	 * e período de recolhimento.
	 * 
	 * @param filtro
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal obterValorTotalEncalhe(FiltroConsultaEncalheDTO filtro);
	
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
	 * 
	 * Obtém a o valor total referente ao movimento para determinados produtoEdicao e cota de
	 * acordo com o range de data informado.
	 * 
	 * @param idCota
	 * @param idFornecedor
	 * @param idProdutoEdicao
	 * @param dataInicial
	 * @param dataFinal
	 * @param operacaoEstoque
	 * 
	 * @return BigInteger
	 */
	public BigDecimal obterValorTotalMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(
			Long idCota, 
			Long idFornecedor,
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
	public List<MovimentoEstoqueCota> obterMovimentoEstoqueCotaPor(ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor, Long idCota, GrupoNotaFiscal grupoNotaFiscal, List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques, Intervalo<Date> periodo, List<Long> listaFornecedores, List<Long> listaProduto);

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
	
	/**
	 * Obtém o Movimento de Estoque das cotas pelo Tipo de Movimento.
	 * 
	 * @param data
	 * @param idCota
	 * @param grupoMovimentoEstoque
	 * 
	 * @return List - MovimentoEstoqueCota
	 */
	public List<MovimentoEstoqueCotaDTO> obterMovimentoCotasPorTipoMovimento(Date data, List<Integer> numCotas, GrupoMovimentoEstoque grupoMovimentoEstoque);
	
	/**
	 * Obtém movimentos de estoque da cota que ainda não geraram movimento financeiro
	 * Considera movimentos de estoque provenientes dos fluxos de Expedição e Conferência de Encalhe
	 * @param idCota
	 * @param dataControleConferencia
	 * @return List<MovimentoEstoqueCota>
	 */
	public List<MovimentoEstoqueCota> obterMovimentosPendentesGerarFinanceiro(Long idCota, Date dataControleConferencia);
	
	/**
	 * Obtém movimentos de estoque da cota que forão estornados
	 * Considera movimentos de estoque provenientes dos fluxos de Venda de Encalhe e Suplementar
	 * @param idCota
	 * @return List<MovimentoEstoqueCota>
	 */
	public List<MovimentoEstoqueCota> obterMovimentosEstornados(Long idCota);

	public List<MovimentoEstoqueCota> obterPorLancamento(Long idLancamento);

	/**
	 * 
	 * @param numeroCota
	 * @param idProdutoEdicao
	 * @param dataOperacao
	 * @return ValoresAplicados
	 */
	public ValoresAplicados obterValoresAplicadosProdutoEdicao(Integer numeroCota, Long idProdutoEdicao, Date dataOperacao);
	
	
	Long obterIdProdutoEdicaoPorControleConferenciaEncalhe(Long idControleConferenciaEncalheCota);
	
	List<MovimentoEstoqueCota> obterMovimentoCotaLancamentoPorTipoMovimento(Date dataLancamento, 
			Long idCota, 
			GrupoMovimentoEstoque grupoMovimentoEstoque);

	/**
	 * 
	 * @param idEstudo
	 */
	public abstract void removerMovimentoEstoqueCotaPorEstudo(Long idEstudo);


}
