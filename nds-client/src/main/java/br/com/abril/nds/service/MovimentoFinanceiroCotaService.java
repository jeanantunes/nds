package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.seguranca.Usuario;

public interface MovimentoFinanceiroCotaService {

	void gerarMovimentoFinanceiroDebitoCredito(
							Cota cota, GrupoMovimentoFinaceiro grupoMovimentoFinanceiro,
							Usuario usuario, BigDecimal valor, Date dataOperacao);
	
}
