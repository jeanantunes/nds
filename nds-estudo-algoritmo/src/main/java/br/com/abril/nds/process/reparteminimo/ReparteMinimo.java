package br.com.abril.nds.process.reparteminimo;

import java.math.BigDecimal;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.redutorautomatico.RedutorAutomatico;
import br.com.abril.nds.process.reparteproporcional.ReparteProporcional;

/**
 * Processo que faz o ajuste do Reparte Mínimo para as cotas de acordo com os parâmetros do setup, com o pacote padrão e com a
 * quantidade de reparte que ele deverá receber, para evitar que ele receba uma quantidade menor que o mínimo configurado ou que o
 * pacote padrão do produto.
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 	- N/A
 * Processo Pai:
 * 	- N/A
 * 
 * Processo Anterior: {@link RedutorAutomatico}
 * Próximo Processo: {@link ReparteProporcional}
 * </p>
 */
public class ReparteMinimo extends ProcessoAbstrato {

    public ReparteMinimo(Estudo estudo) {
	super(estudo);
    }

    @Override
    protected void executarProcesso() throws Exception {
	if (getEstudo().isDistribuicaoPorMultiplos()) {
	    BigDecimal somatoriaReparteMinimo = BigDecimal.ZERO;
	    BigDecimal reparteMinimo = BigDecimal.ZERO;
	    for (Cota cota : getEstudo().getCotas()) {
		if (getEstudo().getPacotePadrao().compareTo(BigDecimal.ZERO) > 0) {
		    reparteMinimo = cota.getReparteMinimo().divide(getEstudo().getPacotePadrao(), 0, BigDecimal.ROUND_FLOOR)
			    .multiply(getEstudo().getPacotePadrao());
		}
		if (cota.getReparteMinimo().compareTo(BigDecimal.ZERO) == 0) {
		    reparteMinimo = getEstudo().getPacotePadrao();
		}
		if (cota.getReparteMinimo().compareTo(reparteMinimo) < 0) {
		    cota.setReparteMinimo(reparteMinimo);
		}
		somatoriaReparteMinimo = somatoriaReparteMinimo.add(cota.getReparteMinimo());
	    }
	    if (getEstudo().getReparteDistribuir().compareTo(BigDecimal.ZERO) > 0) {
		if (somatoriaReparteMinimo.divide(getEstudo().getReparteDistribuir(), 2, BigDecimal.ROUND_HALF_UP)
			.compareTo(BigDecimal.valueOf(0.75)) > 0) {
		    throw new Exception(
			    "O estudo não pode ser concluído pois o percentual do reparte mínimo é maior que 75% do reparte total à distribuir.\n"
				    + "Desmarque a opção de reparte mínimo ou escolha uma quantidade menor.");
		    // A EMS 2050 descrevia que ao ocorrer esse erro deveria ser
		    // exibida uma tela para o usuário e após isso o cáculo
		    // prosseguir por motivos de estrutura esse cálculo não consegue disparar a
		    // exibição de uma tela, portanto, essa funcionalidade não foi implementada.
		}
	    }
	    // RepDistribuir = RepDistribuir - ΣReparteParaMínimo
	    getEstudo().setReparteDistribuir(getEstudo().getReparteDistribuir().subtract(somatoriaReparteMinimo));
	}
    }
}
