package br.com.abril.nds.process.reparteminimo;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.redutorautomatico.RedutorAutomatico;
import br.com.abril.nds.process.reparteproporcional.ReparteProporcional;

/**
 * Processo que faz o ajuste do Reparte Mínimo para as cotas de acordo com os parâmetros do setup, com o pacote padrão e com a
 * quantidade de reparte que ele deverá receber, para evitar que ele receba uma quantidade menor que o mínimo configurado ou que o
 * pacote padrão do produto.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link RedutorAutomatico} Próximo Processo: {@link ReparteProporcional}
 * </p>
 */
@Component
public class ReparteMinimo extends ProcessoAbstrato {

    @Override
    public void executar(EstudoTransient estudo) throws Exception {
	if (estudo.isDistribuicaoPorMultiplos() && (estudo.getPacotePadrao() != null)) {
	    BigInteger somatoriaReparteMinimo = BigInteger.ZERO;
	    for (CotaEstudo cota : estudo.getCotas()) {
		BigInteger reparteMinimo = null;
		if (cota.getReparteMinimo().compareTo(estudo.getReparteMinimo()) > 0) {
		    reparteMinimo = cota.getReparteMinimo();
		} else {
		    reparteMinimo = estudo.getReparteMinimo();
		}
		// variável usada apenas para facilitar leitura
		BigInteger pacPadrao = estudo.getPacotePadrao();
		if (estudo.getPacotePadrao().compareTo(BigInteger.ZERO) > 0) {
		    reparteMinimo = reparteMinimo.divide(pacPadrao).multiply(pacPadrao);
		}
		if (reparteMinimo.compareTo(BigInteger.ZERO) == 0) {
		    reparteMinimo = pacPadrao;
		}
		if (cota.getReparteMinimo().compareTo(reparteMinimo) < 0) {
		    cota.setReparteMinimo(reparteMinimo);
		}
		somatoriaReparteMinimo = somatoriaReparteMinimo.add(cota.getReparteMinimo());
	    }
	    if (estudo.getReparteDistribuir().compareTo(BigInteger.ZERO) > 0) {
		if (new BigDecimal(somatoriaReparteMinimo).divide(new BigDecimal(estudo.getReparteDistribuir()), 2, BigDecimal.ROUND_HALF_UP)
			.compareTo(BigDecimal.valueOf(0.75)) > 0) {
		    throw new Exception(
			    "O estudo não pode ser concluído pois o percentual do reparte mínimo é maior que 75% do reparte total à distribuir.\n"
				    + "Desmarque a opção de reparte mínimo ou escolha uma quantidade menor.");
		    // A EMS 2050 descrevia que ao ocorrer esse erro deveria ser
		    // exibida uma tela para o usuário e após isso o cáculo
		    // prosseguir por motivos de estrutura esse cálculo não
		    // consegue disparar a
		    // exibição de uma tela, portanto, essa funcionalidade não
		    // foi implementada.
		}
	    }
	    // RepDistribuir = RepDistribuir - ΣReparteParaMínimo
	    estudo.setReparteDistribuir(estudo.getReparteDistribuir().subtract(somatoriaReparteMinimo));
	}
    }
}
