package br.com.abril.nds.process.complementarautomatico;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustefinalreparte.ReparteComplementarPorCota;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;
import br.com.abril.nds.process.encalhemaximo.EncalheMaximo;

/**
 * Este processo tem como objetivo calcular o reparteComplementar que será distribuído posteriormente 
 * no SubProcesso {@link ReparteComplementarPorCota}   
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 		- N/A
 * Processo Pai:
 * 		- N/A
 * 
 * Processo Anterior: {@link EncalheMaximo}
 * Próximo Processo: {@link CalcularReparte}
 * </p>
 */
@Component
public class ComplementarAutomatico extends ProcessoAbstrato {
	
    @Override
    protected void executarProcesso() {
    	if ((getEstudo().getProduto() != null) && (getEstudo().getEdicoesBase() != null)) {
        	if ((getEstudo().isComplementarAutomatico()) && (getEstudo().getEdicoesBase().size() == 1) && (getEstudo().getProduto().isColecao())) {
        		getEstudo().setExcedente(getEstudo().getReparteDistribuir().subtract(getEstudo().getSomatoriaVendaMedia()));
        		BigDecimal percentualExcedente = BigDecimal.ZERO;
        		if (getEstudo().getSomatoriaVendaMedia().compareTo(BigDecimal.ZERO) > 0) {
        			percentualExcedente = getEstudo().getExcedente().divide(getEstudo().getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
        		}
        		getEstudo().setReparteComplementar(BigDecimal.ZERO);
        		if (percentualExcedente.compareTo(BigDecimal.ONE) > 0) {
        			// Este cálculo sofreu alterações no trac FAQF2-57
        			// %Abrangência = (QtdeDeBancasDoEstudo(SemLegendaDeExclusão) / TotalCotasAtivas) * 100'
        			int contadorAtivas = 0;
        			for (Cota cota : getEstudo().getCotas()) {
        				if (!cota.getClassificacao().equals(ClassificacaoCota.BancaSuspensa)) {
        					contadorAtivas++;
        				}
        			}
        			BigDecimal percentualAbrangencia = BigDecimal.valueOf(getEstudo().getCotas().size() / contadorAtivas);
        			BigDecimal excedenteAMais = getEstudo().getExcedente().subtract(getEstudo().getSomatoriaVendaMedia());
        			
        			// RepComplementar = ExcedenteAmais * (1 – (((0,6 * %Abrangência) + 40) / 100))
        			BigDecimal temp = BigDecimal.valueOf(0.6).multiply(percentualAbrangencia).add(BigDecimal.valueOf(40));
        			temp = excedenteAMais.multiply(BigDecimal.ONE.subtract(temp)).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
    
        			// RepComplementar = ExcedenteAmais * (1 – (((0,6 * %Abrangência) + 40) / 100))
    				if (getEstudo().isDistribuicaoPorMultiplos()) {
    					getEstudo().setReparteComplementar(getEstudo().getPacotePadrao());
    				} else {
    					if (BigDecimal.valueOf(200).divide(getEstudo().getExcedente(), 2, BigDecimal.ROUND_FLOOR).doubleValue() < 10) {
    						if (2 > getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.02)).doubleValue()) {
    							getEstudo().setReparteComplementar(BigDecimal.valueOf(2));
    						} else {
    							getEstudo().setReparteComplementar(getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.02)));
    						}
    					} else {
    						getEstudo().setReparteComplementar(getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.02)));
    					}
    				}
        		} else if (percentualExcedente.doubleValue() > BigDecimal.valueOf(0.6).doubleValue()) {
        			if (getEstudo().isDistribuicaoPorMultiplos()) {
        				getEstudo().setReparteComplementar(getEstudo().getPacotePadrao());
        			} else {
        				// RepComplementar = Excedente * 2%
        				if (BigDecimal.valueOf(100).divide(getEstudo().getExcedente(), 2, BigDecimal.ROUND_FLOOR).doubleValue() < 10) {
    						if (2 > getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.02)).doubleValue()) {
    							getEstudo().setReparteComplementar(BigDecimal.valueOf(2));
    						} else {
    							getEstudo().setReparteComplementar(getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.02)));
    						}
    					} else {
    						getEstudo().setReparteComplementar(getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.02)));
    					}
        			}
        		}
        		getEstudo().setReparteDistribuir(getEstudo().getReparteDistribuir().subtract(getEstudo().getReparteComplementar()));
        	}
    	}
    }
}
