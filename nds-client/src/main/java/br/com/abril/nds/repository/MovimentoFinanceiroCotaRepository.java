package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;


public interface MovimentoFinanceiroCotaRepository extends Repository<MovimentoFinanceiroCota, Long> {


	List<MovimentoFinanceiroCota> obterMovimentoFinanceiroCotaDataOperacao(Long idCota, Date dataAtual);	

	List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);	
	
	Integer obterContagemMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
	

	Long obterQuantidadeMovimentoFinanceiroDataOperacao(Date dataAtual);


	BigDecimal obterSomatorioValorMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);

}
