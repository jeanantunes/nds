package br.com.abril.nds.process.encalhemaximo;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.reparteproporcional.ReparteProporcional;

/**
 * Este processo tem como objetivo calcular o reparte das cotas de acordo com o percentual de encalhe máximo configurado na tela
 * Ajuste de Reparte, se houver essa configuração.
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 	- N/A
 * Processo Pai:
 * 	- N/A
 * 
 * Processo Anterior: {@link ReparteProporcional}
 * Próximo Processo: {@link ComplementarAutomaticoTest}
 * </p>
 */
@Component
public class EncalheMaximo extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
	for (Cota cota : getEstudo().getCotas()) {
	    BigDecimal encalhe = null;
	    if (getEstudo().getReparteDistribuir().compareTo(BigDecimal.ZERO) > 0) {
		encalhe = getEstudo().getSomatoriaVendaMedia().divide(getEstudo().getReparteDistribuir(), 2, BigDecimal.ROUND_HALF_UP)
			.multiply(BigDecimal.valueOf(100));
	    }
	    if ((cota.getPercentualEncalheMaximo() != null) && (encalhe != null)) {
		if ((cota.getPercentualEncalheMaximo().compareTo(BigDecimal.ZERO) > 0) && (cota.getPercentualEncalheMaximo().compareTo(encalhe) < 0)) {
		    BigDecimal percentual = BigDecimal.valueOf(100).subtract(cota.getPercentualEncalheMaximo())
			    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
		    cota.setReparteCalculado(cota.getVendaMedia().divide(percentual, 0, BigDecimal.ROUND_HALF_UP));

		    getEstudo().setReparteDistribuir(getEstudo().getReparteDistribuir().subtract(cota.getReparteCalculado()));
		}
	    }
	}
    }
}
