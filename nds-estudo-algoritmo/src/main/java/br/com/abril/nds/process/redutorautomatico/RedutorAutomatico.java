package br.com.abril.nds.process.redutorautomatico;

import java.math.BigDecimal;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustereparte.AjusteReparte;
import br.com.abril.nds.process.reparteminimo.ReparteMinimo;

/**
 * Processo que efetua cálculos para definir um percentual de excedente que a cota está tendo e efetuar uma
 * redução no reparte dela de acordo com as vendas.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link AjusteReparte}
 * Próximo Processo: {@link ReparteMinimo}</p>
 */
public class RedutorAutomatico extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
    	// TODO: ainda resta efetuar a consulta dos parâmetros que alimentam o método
    	BigDecimal excedente = estudo.getReparteDistribuir().subtract(estudo.getSomatoriaVendaMediaFinal());
    	BigDecimal percentualExcedente = excedente.divide(estudo.getSomatoriaVendaMediaFinal(), 2, BigDecimal.ROUND_FLOOR);
    	BigDecimal menorVenda = new BigDecimal(0);

    	if ((percentualExcedente.doubleValue() > 0.4d) && (percentualExcedente.doubleValue() < 0.6d)) {
    		menorVenda = new BigDecimal(0.25d);
    	} else if (percentualExcedente.doubleValue() < 0.4d) {
    		menorVenda = new BigDecimal(0.5d);
    	}
    	
    	for (Cota cota : estudo.getCotas()) {
    		if ((cota.getVendaMediaNominalCota().doubleValue() <= menorVenda.doubleValue())
    				&& (cota.getVendaEdicaoMaisRecenteFechada().equals(BigDecimal.ZERO))) {
    			cota.setReparteCalculado(new BigDecimal(0));
    		}
    	}
    	//TODO: verificar qual cota terá sua classificação alterada de acordo com o trac FAQF2-28
    	//FIXME: checar de acordo com a resposta do trac FAQF2-28 se a conta abaixo está correta (se deve ser subtraído o valor do ReparteCalculado da cota mesmo ou se é outra informação)
    	for (Cota cota : estudo.getCotas()) {
    		if (cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
    				|| cota.getClassificacao().equals(ClassificacaoCota.BancaSoComEdicaoBaseAberta)
    				|| cota.getClassificacao().equals(ClassificacaoCota.RedutorAutomatico))
    		estudo.setReparteDistribuir(estudo.getReparteDistribuir().subtract(cota.getReparteCalculado()));
    	}
    }
}
