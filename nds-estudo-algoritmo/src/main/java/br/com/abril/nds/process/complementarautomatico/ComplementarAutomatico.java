package br.com.abril.nds.process.complementarautomatico;

import java.math.BigDecimal;

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
    	if ((getEstudo().isComplementarAutomatico()) && (getEstudo().getEdicoesBase().size() == 1) && (getEstudo().getProduto().isColecao())) {
    		getEstudo().setExcedente(getEstudo().getReparteDistribuir().subtract(getEstudo().getSomatoriaVendaMedia()));
    		BigDecimal percentualExcedente = BigDecimal.ZERO;
    		if (!getEstudo().getSomatoriaVendaMedia().equals(BigDecimal.ZERO)) {
    			percentualExcedente = getEstudo().getExcedente().divide(getEstudo().getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP);
    		}
    		BigDecimal reparteComplementar = BigDecimal.ZERO;
    		if (percentualExcedente.doubleValue() > BigDecimal.ONE.doubleValue()) {
    			// %Abrangência = (QtdeDeBancasDoEstudo(SemLegendaDeExclusão) / TotalCotasAtivas + CotasSuspensasDaPraça ) * 100
    			// TODO: descobrir qual será a origem desses valores e efetuar o cálculo correto
    			BigDecimal percentualAbrangencia = BigDecimal.ZERO;
    			BigDecimal excedenteAMais = getEstudo().getExcedente().subtract(getEstudo().getSomatoriaVendaMedia());
    			
    			// RepComplementar = ExcedenteAmais * (1 – (((0,6 * %Abrangência) + 40) / 100))
    			BigDecimal temp = new BigDecimal(0.6).multiply(percentualAbrangencia).add(new BigDecimal(40)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
    			temp = excedenteAMais.multiply(BigDecimal.ONE.subtract(temp));

    			// RepComplementar = ExcedenteAmais * (1 – (((0,6 * %Abrangência) + 40) / 100))
				if (getEstudo().isDistribuicaoPorMultiplos()) {
					reparteComplementar = getEstudo().getPacotePadrao();
				} else {
					if (new BigDecimal(200).divide(getEstudo().getExcedente(), 2, BigDecimal.ROUND_FLOOR).doubleValue() < 10) {
						if (2 > getEstudo().getExcedente().multiply(new BigDecimal(0.02)).doubleValue()) {
							reparteComplementar = new BigDecimal(2);
						} else {
							reparteComplementar = getEstudo().getExcedente().multiply(new BigDecimal(0.02));
						}
					} else {
						reparteComplementar = getEstudo().getExcedente().multiply(new BigDecimal(0.02));
					}
				}
    		} else if (percentualExcedente.doubleValue() > new BigDecimal(0.6).doubleValue()) {
    			if (getEstudo().isDistribuicaoPorMultiplos()) {
    				reparteComplementar = getEstudo().getPacotePadrao();
    			} else {
    				// RepComplementar = Excedente * 2%
    				if (new BigDecimal(100).divide(getEstudo().getExcedente(), 2, BigDecimal.ROUND_FLOOR).doubleValue() < 10) {
						if (2 > getEstudo().getExcedente().multiply(new BigDecimal(0.02)).doubleValue()) {
							reparteComplementar = new BigDecimal(2);
						} else {
							reparteComplementar = getEstudo().getExcedente().multiply(new BigDecimal(0.02));
						}
					} else {
						reparteComplementar = getEstudo().getExcedente().multiply(new BigDecimal(0.02));
					}
    			}
    		}
    		getEstudo().setReparteDistribuir(getEstudo().getReparteDistribuir().subtract(reparteComplementar));
    	}
    }
}
