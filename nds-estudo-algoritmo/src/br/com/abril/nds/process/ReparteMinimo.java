package br.com.abril.nds.process;

import java.math.BigDecimal;

import br.com.abril.nds.model.Cota;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
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
        executar();
    }

    @Override
    protected void executar() throws Exception {
        // TODO: implementar método calcular do Processo ReparteMinimo
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
    			throw new Exception("");
    		}
    	}
    }
    
}
