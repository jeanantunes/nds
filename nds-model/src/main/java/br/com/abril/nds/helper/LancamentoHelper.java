package br.com.abril.nds.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import br.com.abril.nds.model.planejamento.StatusLancamento;

public class LancamentoHelper {

	public static Collection<StatusLancamento> getStatusLancamentosPreExpedicao() {
		
		return Arrays.asList(StatusLancamento.CONFIRMADO
				, StatusLancamento.PLANEJADO
				, StatusLancamento.EM_BALANCEAMENTO);		
	}
	
	public static Collection<String> getStatusLancamentosPreExpedicaoString() {
		
		Collection<String> statusString = new ArrayList<>(); 
		for(StatusLancamento sl : getStatusLancamentosPreExpedicao()) {
			statusString.add(sl.name());
		}
		return statusString;
	}
	
	public static Collection<StatusLancamento> getStatusLancamentosPosBalanceamentoLancamento() {
		
		return Arrays.asList(StatusLancamento.BALANCEADO, StatusLancamento.EXPEDIDO);		
	}
	
	public static Collection<String> getStatusLancamentosPosBalanceamentoLancamentoString() {
		
		Collection<String> statusString = new ArrayList<>(); 
		for(StatusLancamento sl : getStatusLancamentosPosBalanceamentoLancamento()) {
			statusString.add(sl.name());
		}
		return statusString;
	}
	
	public static Collection<StatusLancamento> getStatusLancamentosPosExpedicao() {
		
		return Arrays.asList(StatusLancamento.EXPEDIDO
				, StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO
				, StatusLancamento.EM_RECOLHIMENTO
				, StatusLancamento.RECOLHIDO
				, StatusLancamento.FECHADO);		
	}
	
	public static Collection<String> getStatusLancamentosPosExpedicaoString() {
		
		Collection<String> statusString = new ArrayList<>(); 
		for(StatusLancamento sl : getStatusLancamentosPosExpedicao()) {
			statusString.add(sl.name());
		}
		return statusString;	
	}
	
}