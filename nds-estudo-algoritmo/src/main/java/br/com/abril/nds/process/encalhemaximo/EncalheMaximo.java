package br.com.abril.nds.process.encalhemaximo;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.complementarautomatico.ComplementarAutomatico;
import br.com.abril.nds.process.reparteproporcional.ReparteProporcional;

/**
 * Este processo tem como objetivo calcular o reparte das cotas de acordo com o
 * percentual de encalhe máximo configurado na tela Ajuste de Reparte, se houver
 * essa configuração.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link ReparteProporcional} Próximo Processo:
 * {@link ComplementarAutomatico}
 * </p>
 */
@Component
public class EncalheMaximo extends ProcessoAbstrato {

    @Override
    public void executar(EstudoTransient estudo) {
	for (CotaEstudo cota : estudo.getCotas()) {
	    BigDecimal percentualVenda = null;
	    if (estudo.getReparteDistribuir().compareTo(BigInteger.ZERO) > 0) {
		// percentualVenda = 1 - (VENDA / REPDISTRIB) * 100
		percentualVenda = BigDecimal.ONE.subtract(
			estudo.getSomatoriaVendaMedia().divide(new BigDecimal(estudo.getReparteDistribuir()), 2, BigDecimal.ROUND_HALF_UP)
			.multiply(BigDecimal.valueOf(100)));
	    }
	    if ((cota.getPercentualEncalheMaximo() != null) && (percentualVenda != null)) {
		if ((cota.getPercentualEncalheMaximo().compareTo(BigDecimal.ZERO) > 0) && (cota.getPercentualEncalheMaximo().compareTo(percentualVenda) < 0)) {
		    // VENDA_MEDIA / ((100 - PERCENTUAL_ENCALHE_COTA) / 100)
		    BigDecimal percentual = BigDecimal.valueOf(100).subtract(cota.getPercentualEncalheMaximo())
			    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
		    cota.setReparteCalculado(BigInteger.valueOf(cota.getVendaMedia().divide(percentual, 0, BigDecimal.ROUND_HALF_UP).longValue()), estudo);
		}
	    }
	}
    }
}
