package br.com.abril.nds.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.repository.FeriadoRepository;
import br.com.abril.nds.service.FeriadoService;
import br.com.abril.nds.util.DateUtil;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Feriado}  
 * 
 * @author Discover Technology
 */
@Service
public class FeriadoServiceImpl implements FeriadoService {

	@Autowired
	private FeriadoRepository feriadoRepository;
	
	public Date adicionarDiasUteis(Date data, int numDias) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		
		for (int i = 0; i < numDias; i++) {
			
			cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
			
			while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal)) {
				cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
			}
		}
		
		return cal.getTime();
	}
	
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
	
	public boolean isFeriado(Calendar cal) {
		
		Feriado feriado = null;
		
		if (cal != null) {
			
			feriado = feriadoRepository.obterPorData(cal.getTime());
		}
		
		return (feriado != null) ? true : false;
	}
	
}
