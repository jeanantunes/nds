package br.com.abril.nds.process.complementarautomatico;

import java.math.BigDecimal;

import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;
import br.com.abril.nds.process.encalhemaximo.EncalheMaximo;

/**
 * Este processo tem como objetivo
 * <p style="white-space: pre-wrap;">
 * SubProcessos:
 * 		- N/A
 * Processo Pai:
 * 		- N/A
 * 
 * Processo Anterior: {@link EncalheMaximo}
 * Próximo Processo: {@link CalcularReparte}
 * </p>
 */
public class ComplementarAutomatico extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
    	if ((getEstudo().isComplementarAutomatico()) && (getEstudo().getEdicoesBaseInsercaoManual().size() == 1) && (getEstudo().getProduto().isColecao())) {
    		BigDecimal excedente = getEstudo().getReparteDistribuir().subtract(getEstudo().getSomatoriaVendaMedia());
    		BigDecimal percentualExcedente = excedente.divide(getEstudo().getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_FLOOR);
    		BigDecimal reparteComplementar = BigDecimal.ZERO;
    		if (percentualExcedente.doubleValue() > BigDecimal.ONE.doubleValue()) {
    			BigDecimal percentualAbrangencia = new BigDecimal(0);
    			BigDecimal excedenteAMais = excedente.subtract(getEstudo().getSomatoriaVendaMedia());
    			//BigDecimal temp = BigDecimal.ONE.subtract(new BigDecimal(0.6).multiply(percentualAbrangencia))
    			//reparteComplementar = excedenteAMais.multiply(multiplicand)
    		} else if (percentualExcedente.doubleValue() > new BigDecimal(0.6).doubleValue()) {
    			if (getEstudo().isDistribuicaoPorMultiplos()) {
    				reparteComplementar = getEstudo().getPacotePadrao();
    			} else {
    				// RepComplementar = Excedente * 2%
    				if (getEstudo().isDistribuicaoPorMultiplos()) {
    					reparteComplementar = getEstudo().getPacotePadrao();
    				} else {
    				//	reparteComplementar = 
    				}
    			}
    		}
    		getEstudo().setReparteDistribuir(getEstudo().getReparteDistribuir().subtract(reparteComplementar));
    	}
    }
}
