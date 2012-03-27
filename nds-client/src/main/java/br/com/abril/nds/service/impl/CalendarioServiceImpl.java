package br.com.abril.nds.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.repository.FeriadoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.util.DateUtil;

/**
 * Classe de implementação de serviços referentes
 * funcionalidades de calendário.
 * 
 * @author Discover Technology
 */
@Service
public class CalendarioServiceImpl implements CalendarioService {

	@Autowired
	private FeriadoRepository feriadoRepository;
	
	@Override
	public Date adicionarDiasUteis(Date data, int numDias) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		
		if (numDias == 0) {
			
			// Verifica se o dia informado é util.
			// Caso não seja, incrementa até encontrar o primeiro dia útil.
			while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal)) {
				cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
			}
			
		} else {
			
			// Adiciona o número de dias úteis informado.
			for (int i = 0; i < numDias; i++) {
				
				cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
				
				while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal)) {
					cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
				}
			}
		}
		
		return cal.getTime();
	}
	
	@Override
	public Date subtrairDiasUteis(Date data, int numDias) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		
		for (int i = 0; i < numDias; i++) {
			
			cal.setTime(DateUtil.subtrairDias(cal.getTime(), 1));
			
			while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal)) {
				cal.setTime(DateUtil.subtrairDias(cal.getTime(), 1));
			}
		}
		
		return cal.getTime();
	}
	
	@Override
	public Date obterProximoDiaUtil(Date data) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		
		cal.setTime(DateUtil.adicionarDias(data, 1));
		
		while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal)) {
			cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
		}
		
		return cal.getTime();
	}
	
	private boolean isFeriado(Calendar cal) {
		
		Feriado feriado = null;
		
		if (cal != null) {
			
			feriado = feriadoRepository.obterPorData(cal.getTime());
		}
		
		return (feriado != null) ? true : false;
	}
	
}
