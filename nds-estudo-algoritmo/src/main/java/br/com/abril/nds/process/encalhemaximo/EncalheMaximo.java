package br.com.abril.nds.process.encalhemaximo;

import java.math.BigDecimal;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.complementarautomatico.ComplementarAutomatico;
import br.com.abril.nds.process.reparteproporcional.ReparteProporcional;

/**
 * Este processo tem como objetivo calcular o reparte das cotas de acordo com o percentual de encalhe máximo
 * configurado na tela Ajuste de Reparte, se houver essa configuração.
 * <p style="white-space: pre-wrap;">
 * SubProcessos:
 * 		- N/A
 * Processo Pai:
 * 		- N/A
 * 
 * Processo Anterior: {@link ReparteProporcional}
 * Próximo Processo: {@link ComplementarAutomatico}
 * </p>
 */
public class EncalheMaximo extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
    	// TODO: ainda resta efetuar a consulta dos parâmetros que alimentam o método
    	for (Cota cota : estudo.getCotas()) {
    		BigDecimal encalhe = estudo.getSomatoriaVendaMedia().divide(estudo.getReparteDistribuir(), 2, BigDecimal.ROUND_FLOOR).multiply(new BigDecimal(100));
    		if ((cota.getPercentualEncalheMaximo().doubleValue() > 0)
    				&& (cota.getPercentualEncalheMaximo().doubleValue() < encalhe.doubleValue())) {
    			BigDecimal percentual = new BigDecimal(100).subtract(cota.getPercentualEncalheMaximo()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_FLOOR);
    			cota.setReparteCalculado(cota.getVendaMedia().divide(percentual));
    		}
    	}
    }

}
