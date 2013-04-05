package br.com.abril.nds.process.redutorautomatico;

import java.math.BigDecimal;
import java.math.BigInteger;

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

    private BigDecimal menorVenda = BigDecimal.ZERO;

    public BigDecimal getMenorVenda() {
	return menorVenda;
    }

    public void setMenorVenda(BigDecimal menorVenda) {
	this.menorVenda = menorVenda;
    }

    @Override
    public void executar(EstudoTransient estudo) {
	calcularMenorVenda(estudo);
	calcularRedutorAutomatico(estudo);
    }

    public void calcularRedutorAutomatico(EstudoTransient estudo) {
	for (CotaEstudo cota : estudo.getCotas()) {
	    if (cota.getVendaEdicaoMaisRecenteFechada() != null) {
		if ((cota.getVendaMedia().compareTo(menorVenda) <= 0) && (cota.getVendaEdicaoMaisRecenteFechada().compareTo(BigDecimal.ZERO) == 0)) {
		    cota.setReparteCalculado(BigInteger.ZERO);
		    cota.setClassificacao(ClassificacaoCota.RedutorAutomatico);
		}
		if (cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
			|| cota.getClassificacao().equals(ClassificacaoCota.BancaSoComEdicaoBaseAberta)
			|| cota.getClassificacao().equals(ClassificacaoCota.RedutorAutomatico)) {
		    estudo.setReparteDistribuir(estudo.getReparteDistribuir().subtract(cota.getReparteCalculado()));
		}
	    }
	}
    }

    public void calcularMenorVenda(EstudoTransient estudo) {
	estudo.setExcedente(new BigDecimal(estudo.getReparteDistribuir()).subtract(estudo.getSomatoriaVendaMedia()));
	BigDecimal percentualExcedente = BigDecimal.ZERO;
	if (estudo.getSomatoriaVendaMedia().compareTo(BigDecimal.ZERO) > 0) {
	    percentualExcedente = estudo.getExcedente().divide(estudo.getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
	}

	menorVenda = BigDecimal.ZERO;
	if ((percentualExcedente.compareTo(BigDecimal.valueOf(0.4)) > 0) && (percentualExcedente.compareTo(BigDecimal.valueOf(0.6)) < 0)) {
	    menorVenda = BigDecimal.valueOf(0.25d);
	} else if (percentualExcedente.compareTo(BigDecimal.valueOf(0.4)) < 0) {
	    menorVenda = BigDecimal.valueOf(0.5d);
	}
    }
}
