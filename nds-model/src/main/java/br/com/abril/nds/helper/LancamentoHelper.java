package br.com.abril.nds.helper;

import java.util.Arrays;
import java.util.Collection;

import br.com.abril.nds.model.planejamento.StatusLancamento;

public class LancamentoHelper {

	public static Collection<StatusLancamento> getStatusLancamentosPreExpedicao() {
		
		return Arrays.asList(StatusLancamento.CONFIRMADO
				, StatusLancamento.PLANEJADO
				, StatusLancamento.EM_BALANCEAMENTO);		
	}
	
	public static Collection<StatusLancamento> getStatusLancamentosPosBalanceamentoLancamento() {
		
		return Arrays.asList(StatusLancamento.BALANCEADO, StatusLancamento.EXPEDIDO);		
	}
	
	public static Collection<StatusLancamento> getStatusLancamentosPosExpedicao() {
		
		return Arrays.asList(StatusLancamento.EXPEDIDO
				, StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO
				, StatusLancamento.EM_RECOLHIMENTO
				, StatusLancamento.RECOLHIDO
				, StatusLancamento.FECHADO);		
	}
	
}