package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.strategy.importacao.input.HistoricoFinanceiroInput;

public interface MovimentoFinanceiroCotaService {
	
	List<MovimentoFinanceiroCota> gerarMovimentosFinanceirosDebitoCredito(MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO);
	
	List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	
	Integer obterContagemMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	
	void removerMovimentoFinanceiroCota(Long idMovimento);
	
	MovimentoFinanceiroCota obterMovimentoFinanceiroCotaPorId(Long idMovimento);
	
	BigDecimal obterSomatorioValorMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	
	/**
	 * Obtém valores dos faturamentos bruto ou liquido das cotas no período
	 * @param cotas
	 * @param baseCalculo
	 * @param dataInicial
	 * @param dataFinal
	 * @return Map<Long,BigDecimal>: Faturamentos das cotas
	 */
	Map<Long,BigDecimal> obterFaturamentoCotasPeriodo(List<Cota> cotas, BaseCalculo baseCalculo, Date dataInicial, Date dataFinal);
	
	/**
	 * Gera movimento financeiro para cota a vista (crédito)
	 * @param controleConferenciaEncalheCota
	 */
	void gerarMovimentoFinanceiroCota(
			Cota cota,
			Date dataOperacao,
			Usuario usuario,
			Long idControleConferenciaEncalheCota,
			FormaComercializacao formaComercializacaoProduto);

	void processarRegistrohistoricoFinanceiro(
			HistoricoFinanceiroInput vendaInput, Date dataOperacao);

	boolean existeOutrosMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCredito, Long idMovimentoFinanceiroAtual);

	void removerPostergadosDia(Long idCota,
			List<TipoMovimentoFinanceiro> tiposMovimentoPostergado, Date dataOperacao);
	
	List<GrupoMovimentoFinaceiro> getGrupoMovimentosFinanceirosDebitosCreditos();

}