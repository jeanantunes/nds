package br.com.abril.nds.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional(readOnly=true)
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
	@Transactional(readOnly=true)
	public Date adicionarDiasRetornarDiaUtil(Date data, int numDias) {

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
			}
		}
		
		return cal.getTime();
	}
	
	@Override
	@Transactional(readOnly=true)
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
	
	@Transactional
	public boolean isDiaUtil(Date data) {
		
		if (data == null) {
			
			return false;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		
		return !(DateUtil.isSabadoDomingo(cal) || isFeriado(cal));
	}
	
	@Override
	public Date adicionarDiasUteis(Date data, int numDias, List<Integer> diasSemanaConcentracaoCobranca, Integer diaMesConcentracaoCobranca) {
		
		if (diasSemanaConcentracaoCobranca == null || diasSemanaConcentracaoCobranca.isEmpty() && (diasSemanaConcentracaoCobranca == null)){
			
			return this.adicionarDiasUteis(data, numDias);
		}
		
		if (diasSemanaConcentracaoCobranca != null && !diasSemanaConcentracaoCobranca.isEmpty()){
			
			Calendar dataBase = Calendar.getInstance();
			dataBase.setTime(data);
			dataBase.add(Calendar.DAY_OF_MONTH, numDias);
			
			boolean dataValida = false;
			
			while (!dataValida){
				while (!diasSemanaConcentracaoCobranca.contains(dataBase.get(Calendar.DAY_OF_WEEK))){
					dataBase.add(Calendar.DAY_OF_MONTH, 1);
				}
				
				dataBase.setTime(this.adicionarDiasUteis(dataBase.getTime(), 0));
				
				dataValida = diasSemanaConcentracaoCobranca.contains(dataBase.get(Calendar.DAY_OF_WEEK));
			}
			
			return dataBase.getTime();
		} else if (diaMesConcentracaoCobranca != null){
			
			if (Calendar.getInstance().getLeastMaximum(Calendar.DAY_OF_MONTH) > diaMesConcentracaoCobranca){
				
				diaMesConcentracaoCobranca = Calendar.getInstance().getLeastMaximum(Calendar.DAY_OF_MONTH);
			}
			
			Calendar dataVencimento = Calendar.getInstance();
			
			while (dataVencimento.get(Calendar.DAY_OF_MONTH) < diaMesConcentracaoCobranca){
				
				dataVencimento.setTime(this.adicionarDiasUteis(dataVencimento.getTime(), 1));
			}
			
			return dataVencimento.getTime();
		}
		
		return Calendar.getInstance().getTime();
	}
	
	private boolean isFeriado(Calendar cal) {
		
		Feriado feriado = null;
		
		if (cal != null) {
			
			feriado = feriadoRepository.obterPorData(cal.getTime());
		}
		
		return (feriado != null) ? true : false;
	}
	
}
