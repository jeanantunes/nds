package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;

public interface MovimentoFinanceiroCotaService {

	void gerarMovimentoFinanceiroDebitoCredito(MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO);
	
	List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	
	Integer obterContagemMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	
	void removerMovimentoFinanceiroCota(Long idMovimento);
	
	MovimentoFinanceiroCota obterMovimentoFinanceiroCotaPorId(Long idMovimento);
	
	BigDecimal obterSomatorioValorMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	
}
