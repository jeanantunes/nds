package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.BaixaAutomatica;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.seguranca.Usuario;

public interface MovimentoFinanceiroCotaService {

	void gerarMovimentoFinanceiroDebitoCredito(
							Cota cota, GrupoMovimentoFinaceiro grupoMovimentoFinanceiro,
							Usuario usuario, BigDecimal valor, Date dataOperacao,
							BaixaAutomatica baixaAutomatica);
	
	List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO);
}
