package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;

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
	void gerarMovimentoFinanceiroCotaRecolhimento(ControleConferenciaEncalheCota controleConferenciaEncalheCota);
	
	/**
	 * Gera movimento financeiro para cota na Expedição.
	 * @param movimentosEstoqueCota
	 * @param dataOperacao
	 * @param usuario
	 */
	void gerarMovimentoFinanceiroCotaExpedicao(MovimentoEstoqueCota movimentosEstoqueCota,
									           Date dataOperacao,
									           Usuario usuario);

}
