package br.com.abril.nds.process.complementarautomatico;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustefinalreparte.ReparteComplementarPorCota;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;
import br.com.abril.nds.process.encalhemaximo.EncalheMaximo;

/**
 * Este processo tem como objetivo calcular o reparteComplementar que será
 * distribuído posteriormente no SubProcesso {@link ReparteComplementarPorCota}
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link EncalheMaximo} Próximo Processo:
 * {@link CalcularReparte}
 * </p>
 */
@Component
public class ComplementarAutomatico extends ProcessoAbstrato {

    @Override
    public void executar(EstudoTransient estudo) {
	if ((estudo.getProdutoEdicaoEstudo() != null) && (estudo.getEdicoesBase() != null)) {
            if ((estudo.isComplementarAutomatico())
                && ((estudo.getProdutoEdicaoEstudo().getNumeroEdicao() == 1L) || (!estudo.getProdutoEdicaoEstudo()
                        .isColecao()))) {
		estudo.setExcedente(new BigDecimal(estudo.getReparteDistribuir()).subtract(estudo.getSomatoriaVendaMedia()));
		estudo.setPercentualExcedente(BigDecimal.ZERO);
		if (estudo.getSomatoriaVendaMedia().compareTo(BigDecimal.ZERO) > 0) {
		    estudo.setPercentualExcedente(estudo.getExcedente().divide(estudo.getSomatoriaVendaMedia(), 2, BigDecimal.ROUND_HALF_UP));
		}
		estudo.setReparteComplementar(BigInteger.ZERO);
		if (estudo.getPercentualExcedente().compareTo(BigDecimal.ONE) > 0) {
                    // Este cálculo sofreu alterações no trac FAQF2-57
                    // %Abrangência =
                    // (QtdeDeBancasDoEstudo(SemLegendaDeExclusão) /
                    // TotalCotasAtivas) * 100
		    
			int contadorAtivas = 0;
		    
		    for (CotaEstudo cota : estudo.getCotas()) {
				if (!cota.getClassificacao().equals(ClassificacaoCota.BancaSuspensa)) {
				    contadorAtivas++;
				}
		    }
		    
		    for (CotaEstudo cota : estudo.getCotasExcluidas()) {
				if (!cota.getClassificacao().equals(ClassificacaoCota.BancaSuspensa)) {
				    contadorAtivas++;
				}
		    }
		    
		    BigDecimal percentualAbrangencia = BigDecimal.valueOf(estudo.getCotas().size()).divide(BigDecimal.valueOf(contadorAtivas), 2, BigDecimal.ROUND_HALF_UP);
		    
		    percentualAbrangencia = percentualAbrangencia.multiply(BigDecimal.valueOf(100));
		    
		    BigDecimal excedenteAMais = estudo.getExcedente().subtract(estudo.getSomatoriaVendaMedia());

            // Calculo1 = ExcedenteAmais * (1 – (((0,6 * %Abrangência) + 40) / 100))
		    BigDecimal calculo1 = BigDecimal.valueOf(0.8).multiply(percentualAbrangencia).add(BigDecimal.valueOf(20));
		    
		    calculo1 = excedenteAMais.multiply(BigDecimal.ONE.subtract(calculo1.divide(BigDecimal.valueOf(100), 3, BigDecimal.ROUND_HALF_UP)));
		    
		    // Calculo2 = 2% do Excedente
		    BigDecimal calculo2 = estudo.getExcedente().multiply(BigDecimal.valueOf(0.02));
		    
		    BigDecimal calculo3 = BigDecimal.ZERO;
		    BigDecimal calculo4 = BigDecimal.ZERO;
		    
		    // Calculo3 = pacote padrao
		    if (estudo.isDistribuicaoPorMultiplos() &&
			    (new BigDecimal(estudo.getPacotePadrao()).divide(estudo.getExcedente(), 2, BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.valueOf(0.1)) < 0)) {
		    	calculo3 = new BigDecimal(estudo.getPacotePadrao());
		    }
		    
		    // Calculo4 = 2 exemplares
		    if (BigDecimal.valueOf(2).divide(estudo.getExcedente(), 2, BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.valueOf(0.1)) < 0) {
		    	calculo4 = BigDecimal.valueOf(2);
		    }
		    
		    estudo.setReparteComplementar(calculo1.max(calculo2).setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger());
		    estudo.setReparteComplementar(new BigDecimal(estudo.getReparteComplementar()).max(calculo3).setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger());
		    estudo.setReparteComplementar(new BigDecimal(estudo.getReparteComplementar()).max(calculo4).setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger());
		
		} else if (estudo.getPercentualExcedente().compareTo(BigDecimal.valueOf(0.6)) > 0) {
		
			// Calculo1 = 2% do Excedente
		    BigDecimal calculo1 = estudo.getExcedente().multiply(BigDecimal.valueOf(0.02));
		    BigDecimal calculo2 = BigDecimal.ZERO;
		    BigDecimal calculo3 = BigDecimal.ZERO;
		    
		    // Calculo2 = pacote padrao
		    if (estudo.isDistribuicaoPorMultiplos() &&
			    (new BigDecimal(estudo.getPacotePadrao()).divide(estudo.getExcedente(), 2, BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.valueOf(0.1)) < 0)) {
		    	calculo2 = new BigDecimal(estudo.getPacotePadrao());
		    }

		    // Calculo3 = 2 exemplares
		    if (BigDecimal.valueOf(2).divide(estudo.getExcedente(), 2, BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.valueOf(10)) < 0) {
		    	calculo3 = BigDecimal.valueOf(2);
		    }
		    
		    estudo.setReparteComplementar(calculo1.max(calculo2).toBigInteger());
		    estudo.setReparteComplementar(new BigDecimal(estudo.getReparteComplementar()).max(calculo3).toBigInteger());
		}
		
		// se for distribuicao por multiplos, faz arredondamento para o pacote padrao
		if (estudo.isDistribuicaoPorMultiplos() && estudo.getPacotePadrao() != null && estudo.getPacotePadrao().compareTo(BigInteger.ZERO) > 0) {
			if(estudo.getReparteMinimo() != null && estudo.getReparteMinimo().compareTo(estudo.getPacotePadrao()) >= 0){
				estudo.setReparteComplementar(new BigDecimal(estudo.getReparteComplementar()).divide(new BigDecimal(estudo.getReparteMinimo()), 0, BigDecimal.ROUND_HALF_UP).toBigInteger().multiply(estudo.getReparteMinimo()));
			}else{
				estudo.setReparteComplementar(new BigDecimal(estudo.getReparteComplementar()).divide(new BigDecimal(estudo.getPacotePadrao()), 0, BigDecimal.ROUND_HALF_UP).toBigInteger().multiply(estudo.getPacotePadrao()));
			}
        } else {
        	estudo.setReparteComplementar(new BigDecimal(estudo.getReparteComplementar()).setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger());
        }
		
//		estudo.setReparteComplementar(EstudoAlgoritmoService.arredondarPacotePadrao(estudo, new BigDecimal(estudo.getReparteComplementar())));

		estudo.setReparteComplementarInicial(estudo.getReparteComplementar());
		estudo.setReparteDistribuir(estudo.getReparteDistribuir().subtract(estudo.getReparteComplementar()));
	    }
	}
    }
}
