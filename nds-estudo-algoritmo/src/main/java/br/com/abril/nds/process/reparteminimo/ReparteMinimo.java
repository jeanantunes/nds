package br.com.abril.nds.process.reparteminimo;

import java.math.BigDecimal;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.redutorautomatico.RedutorAutomatico;
import br.com.abril.nds.process.reparteproporcional.ReparteProporcional;

/**
 * Processo que faz o ajuste do Reparte Mínimo para as cotas de acordo com os parâmetros do setup, com 
 * o pacote padrão e com a quantidade de reparte que ele deverá receber, para evitar que ele receba uma
 * quantidade menor que o mínimo configurado ou que o pacote padrão do produto.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link RedutorAutomatico}
 * Próximo Processo: {@link ReparteProporcional}</p>
 */
public class ReparteMinimo extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() throws Exception {
        // TODO: concluir desenvolvimento do processo ReparteMinimo
    	// TODO: ainda resta efetuar a consulta dos parâmetros que alimentam o método
    	if (estudo.getParametro().isDistribuicaoPorMultiplos()) {
    		BigDecimal somatoriaReparteMinimo = new BigDecimal(0);
    		for (Cota cota : estudo.getCotas()) {
    			cota.setReparteMinimo(cota.getReparteMinimo().divide(estudo.getParametro().getPacotePadrao(), 0, BigDecimal.ROUND_FLOOR).multiply(estudo.getParametro().getPacotePadrao()));
    			if (cota.getReparteMinimo().equals(BigDecimal.ZERO)) {
    				cota.setReparteMinimo(estudo.getParametro().getPacotePadrao());
    			}
    			somatoriaReparteMinimo.add(cota.getReparteMinimo());
    		}
    		if (somatoriaReparteMinimo.divide(estudo.getReparteDistribuir(), 0, BigDecimal.ROUND_FLOOR).doubleValue() > new BigDecimal(0.75f).doubleValue()) {
    			throw new Exception("O estudo não pode ser concluído pois o percentual do reparte mínimo é maior que 75% do reparte total à distribuir.\n" +
    					"Desmarque a opção de reparte mínimo ou escolha uma quantidade menor.");
    			// A EMS 2050 descrevia que ao ocorrer esse erro deveria ser exibida uma tela para o usuário e após isso o cáculo prosseguir
    			// por motivos de estrutura esse cálculo não consegue disparar a exibição de uma tela, portanto, essa funcionalidade não foi
    			// implementada.
    		}
    	}
    }
}
