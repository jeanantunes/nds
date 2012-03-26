package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;


public interface MovimentoFinanceiroCotaRepository extends Repository<MovimentoFinanceiroCota, Long> {


	List<MovimentoFinanceiroCota> obterMovimentoFinanceiroCotaDataOperacao(Long idCota);	

	List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);	


	
	List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
}