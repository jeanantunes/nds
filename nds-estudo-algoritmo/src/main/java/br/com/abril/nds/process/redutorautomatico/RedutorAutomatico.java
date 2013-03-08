package br.com.abril.nds.process.redutorautomatico;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustereparte.AjusteReparte;
import br.com.abril.nds.process.reparteminimo.ReparteMinimo;

/**
 * Processo que efetua cálculos para definir um percentual de excedente que a
 * cota está tendo e efetuar uma redução no reparte dela de acordo com as vendas.
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 	- N/A
 * Processo Pai:
 * 	- N/A
 * 
 * Processo Anterior: {@link AjusteReparte}
 * Próximo Processo: {@link ReparteMinimo}
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
    protected void executarProcesso() {
	calcularMenorVenda();
	calcularRedutorAutomatico();
    }

    public void calcularRedutorAutomatico() {
	for (Cota cota : getEstudo().getCotas()) {
	    if ((cota.getVendaMedia().compareTo(menorVenda) <= 0) && (cota.getVendaEdicaoMaisRecenteFechada().compareTo(BigDecimal.ZERO) == 0)) {
		cota.setReparteCalculado(BigDecimal.ZERO);
		cota.setClassificacao(ClassificacaoCota.RedutorAutomatico);
	    }
	    if (cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
		    || cota.getClassificacao().equals(ClassificacaoCota.BancaSoComEdicaoBaseAberta)
		    || cota.getClassificacao().equals(ClassificacaoCota.RedutorAutomatico))
		getEstudo().setReparteDistribuir(getEstudo().getReparteDistribuir().subtract(cota.getReparteCalculado()));
	}
    }

    public void calcularMenorVenda() {
	getEstudo().setExcedente(getEstudo().getReparteDistribuir().subtract(getEstudo().getSomatoriaVendaMedia()));
	BigDecimal percentualExcedente = BigDecimal.ZERO;
	if (getEstudo().getSomatoriaVendaMedia().doubleValue() > 0) {
	    percentualExcedente = getEstudo().getExcedente().divide(getEstudo().getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
	}

	menorVenda = BigDecimal.ZERO;
	if ((percentualExcedente.doubleValue() > 0.4d) && (percentualExcedente.doubleValue() < 0.6d)) {
	    menorVenda = BigDecimal.valueOf(0.25d);
	} else if (percentualExcedente.doubleValue() < 0.4d) {
	    menorVenda = BigDecimal.valueOf(0.5d);
	}
    }
}
