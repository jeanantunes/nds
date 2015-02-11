package br.com.abril.nds.process.redutorautomatico;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustereparte.AjusteReparte;
import br.com.abril.nds.process.reparteminimo.ReparteMinimo;

/**
 * Processo que efetua cálculos para definir um percentual de excedente que a cota está tendo e efetuar uma redução no reparte
 * dela de acordo com as vendas.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link AjusteReparte} Próximo Processo: {@link ReparteMinimo}
 * </p>
 */
@Component
public class RedutorAutomatico extends ProcessoAbstrato {

    @Override
    public void executar(EstudoTransient estudo) {
	calcularMenorVenda(estudo);
	calcularRedutorAutomatico(estudo);
    }

    // não aplicar redutor automatico para MIX
    
    public void calcularRedutorAutomatico(EstudoTransient estudo) {
	
    	List<CotaEstudo> cotasForRemove = new ArrayList<>();
    	
    	for (CotaEstudo cota : estudo.getCotas()) {
    		
    		if (cota.getVendaEdicaoMaisRecenteFechada() != null) {
			
    			if ((cota.getVendaMedia().compareTo(estudo.getMenorVenda()) <= 0) && (cota.getVendaEdicaoMaisRecenteFechada().compareTo(BigInteger.ZERO) == 0)) {
				    cota.setReparteCalculado(BigInteger.ZERO, estudo);
				    cota.setClassificacao(ClassificacaoCota.RedutorAutomatico);
//				    cota.setClassificacao(ClassificacaoCota.BancaComVendaZero);
				    
				    cotasForRemove.add(cota);
				    
				    estudo.getCotasComReparteJaCalculado().add(cota);
			    
				}
	    	}
    	}
    	
    	estudo.getCotas().removeAll(cotasForRemove);
    	
    }

    public void calcularMenorVenda(EstudoTransient estudo) {
	estudo.setExcedente(new BigDecimal(estudo.getReparteDistribuir()).subtract(estudo.getSomatoriaVendaMedia()));
	estudo.setPercentualExcedente(BigDecimal.ZERO);
	if (estudo.getSomatoriaVendaMedia().compareTo(BigDecimal.ZERO) > 0) {
	    estudo.setPercentualExcedente(estudo.getExcedente().divide(estudo.getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP));
	}

	estudo.setMenorVenda(BigDecimal.ZERO);
	if ((estudo.getPercentualExcedente().compareTo(BigDecimal.valueOf(0.4)) > 0) && (estudo.getPercentualExcedente().compareTo(BigDecimal.valueOf(0.6)) < 0)) {
	    estudo.setMenorVenda(BigDecimal.valueOf(0.25d));
	} else if (estudo.getPercentualExcedente().compareTo(BigDecimal.valueOf(0.4)) < 0) {
	    estudo.setMenorVenda(BigDecimal.valueOf(0.5d));
	}
    }
}
