package br.com.abril.nds.process.complementarautomatico;

import java.math.BigDecimal;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;
import br.com.abril.nds.process.encalhemaximo.EncalheMaximo;

/**
 * Este processo tem como objetivo
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 		- N/A
 * Processo Pai:
 * 		- N/A
 * 
 * Processo Anterior: {@link EncalheMaximo}
 * Próximo Processo: {@link CalcularReparte}
 * </p>
 */
public class ComplementarAutomatico extends ProcessoAbstrato {

	public ComplementarAutomatico(Estudo estudo) {
		super(estudo);
	}
	
    @Override
    protected void executarProcesso() {
    	if ((getEstudo().getProduto() != null) && (getEstudo().getEdicoesBase() != null)) {
        	if ((getEstudo().isComplementarAutomatico()) && (getEstudo().getEdicoesBase().size() == 1) && (getEstudo().getProduto().isColecao())) {
        		getEstudo().setExcedente(getEstudo().getReparteDistribuir().subtract(getEstudo().getSomatoriaVendaMedia()));
        		BigDecimal percentualExcedente = BigDecimal.ZERO;
        		if (!getEstudo().getSomatoriaVendaMedia().equals(BigDecimal.ZERO)) {
        			percentualExcedente = getEstudo().getExcedente().divide(getEstudo().getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
        		}
        		BigDecimal reparteComplementar = BigDecimal.ZERO;
        		if (percentualExcedente.doubleValue() > BigDecimal.ONE.doubleValue()) {
        			// %Abrangência = (QtdeDeBancasDoEstudo(SemLegendaDeExclusão) / TotalCotasAtivas + CotasSuspensasDaPraça) * 100'
        			int contadorSuspensas = 0;
        			int contadorAtivas = 0;
        			for (Cota cota : getEstudo().getCotas()) {
        				if (cota.getClassificacao().equals(ClassificacaoCota.BancaSuspensa)) {
        					contadorSuspensas++;
        				} else {
        					contadorAtivas++;
        				}
        			}
        			BigDecimal percentualAbrangencia = BigDecimal.valueOf(getEstudo().getCotas().size() / contadorAtivas + contadorSuspensas);
        			BigDecimal excedenteAMais = getEstudo().getExcedente().subtract(getEstudo().getSomatoriaVendaMedia());
        			
        			// RepComplementar = ExcedenteAmais * (1 – (((0,6 * %Abrangência) + 40) / 100))
        			BigDecimal temp = BigDecimal.valueOf(0.6).multiply(percentualAbrangencia).add(BigDecimal.valueOf(40));
        			temp = excedenteAMais.multiply(BigDecimal.ONE.subtract(temp)).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
    
        			// RepComplementar = ExcedenteAmais * (1 – (((0,6 * %Abrangência) + 40) / 100))
    				if (getEstudo().isDistribuicaoPorMultiplos()) {
    					reparteComplementar = getEstudo().getPacotePadrao();
    				} else {
    					if (BigDecimal.valueOf(200).divide(getEstudo().getExcedente(), 2, BigDecimal.ROUND_FLOOR).doubleValue() < 10) {
    						if (2 > getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.02)).doubleValue()) {
    							reparteComplementar = BigDecimal.valueOf(2);
    						} else {
    							reparteComplementar = getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.02));
    						}
    					} else {
    						reparteComplementar = getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.02));
    					}
    				}
        		} else if (percentualExcedente.doubleValue() > BigDecimal.valueOf(0.6).doubleValue()) {
        			if (getEstudo().isDistribuicaoPorMultiplos()) {
        				reparteComplementar = getEstudo().getPacotePadrao();
        			} else {
        				// RepComplementar = Excedente * 2%
        				if (BigDecimal.valueOf(100).divide(getEstudo().getExcedente(), 2, BigDecimal.ROUND_FLOOR).doubleValue() < 10) {
    						if (2 > getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.02)).doubleValue()) {
    							reparteComplementar = BigDecimal.valueOf(2);
    						} else {
    							reparteComplementar = getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.02));
    						}
    					} else {
    						reparteComplementar = getEstudo().getExcedente().multiply(BigDecimal.valueOf(0.02));
    					}
        			}
        		}
        		getEstudo().setReparteDistribuir(getEstudo().getReparteDistribuir().subtract(reparteComplementar));
        	}
    	}
    }
}
