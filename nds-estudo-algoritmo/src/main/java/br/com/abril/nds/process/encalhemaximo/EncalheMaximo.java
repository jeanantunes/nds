package br.com.abril.nds.process.encalhemaximo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.ClassificacaoCota;
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
	
	LinkedList<CotaEstudo> cotasComRepJaCalculado = new LinkedList<>();
	
    @Override
    public void executar(EstudoTransient estudo) {
		
    	for (CotaEstudo cota : estudo.getCotas()) {
		
    		if(cota.getPercentualEncalheMaximo() != null){
    		
	    		BigDecimal percentualVenda = null;
			    
	    		if (estudo.getReparteDistribuir().compareTo(BigInteger.ZERO) > 0) {
	    			// percentualVenda = ((1 - (VENDA / REPDISTRIB)) * 100)
	    			percentualVenda = (BigDecimal.ONE.subtract(estudo.getSomatoriaVendaMedia().divide(new BigDecimal(estudo.getReparteDistribuir()), 2, BigDecimal.ROUND_HALF_UP))).multiply(BigDecimal.valueOf(100));
	    			
	    		}
	    		
	    		if (percentualVenda != null) {
					if ((cota.getPercentualEncalheMaximo().compareTo(BigDecimal.ZERO) > 0) && (cota.getPercentualEncalheMaximo().compareTo(percentualVenda) < 0)) {
					    
						// VENDA_MEDIA / ((100 - PERCENTUAL_ENCALHE_COTA) / 100)
						//RepFinal Cota = VendaMédiaFinal Cota / ((100 - %EncalheMáximo Cota ) / 100)
					    
						BigDecimal percentual = BigDecimal.valueOf(100).subtract(cota.getPercentualEncalheMaximo()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
					    
						BigDecimal repartePreCalculado = cota.getVendaMedia().divide(percentual, 0, BigDecimal.ROUND_HALF_UP);
						
						BigInteger reparte;
						
						if(estudo.getPacotePadrao() != null && estudo.getPacotePadrao().compareTo(BigInteger.ZERO) > 0){
			    			
						    	if (repartePreCalculado.toBigInteger().compareTo(estudo.getPacotePadrao()) > 0) {
						    		
						    		BigDecimal verificador = repartePreCalculado.divide(new BigDecimal(estudo.getPacotePadrao()), 0, BigDecimal.ROUND_HALF_UP);
						    		
						    		reparte = BigInteger.valueOf(verificador.intValue()).multiply(estudo.getPacotePadrao());
						    		
								} else {
									reparte = estudo.getPacotePadrao();
								}
						    	
		    			}else{
		    				reparte = repartePreCalculado.toBigInteger();
		    			}
						
						cota.setReparteCalculado(reparte, estudo);
						cota.setClassificacao(ClassificacaoCota.Ajuste);
						cotasComRepJaCalculado.add(cota);
					}
			    }
    		}
		}
	
	estudo.getCotasComReparteJaCalculado().addAll(new LinkedList<>(cotasComRepJaCalculado));
	estudo.getCotas().removeAll(this.cotasComRepJaCalculado);
	
	this.cotasComRepJaCalculado.clear();
	
    }
}
