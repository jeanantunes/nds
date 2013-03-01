package br.com.abril.nds.process.calculoreparte;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CalcularReparte}
 * 
 * Processo Anterior: {@link GravarReparteJuramentado} Próximo Processo:
 * {@link ReparteComplementarPorCota}
 * </p>
 */
public class AjusteFinalReparte extends ProcessoAbstrato {

    public AjusteFinalReparte(Estudo estudo) {
	super(estudo);
    }

    @Override
    protected void executarProcesso() {
    	
    	// Variável AjusteReparte modificada no faq FAQF2-53
    	// Se ReservaAjuste > 0
		if (getEstudo().getAjusteReparte().compareTo(BigDecimal.ZERO) == 1){
			
//			Verificar Cota a Cota
//    		Se Repcalculado < Venda (�ltima edi��o fechada, sem corre��o)
			
			
			for(Cota cota:getEstudo().getCotas()){
//    		Se Repcalculado < Venda (Última edição fechada, sem correção)
//    				Se Cota <> FX / MM / MX / RD / PR
				if(cota.getReparteCalculado().compareTo(getEstudo().getAjusteReparte())==-1 ||
						cota.getReparteCalculado().compareTo(getEstudo().getAjusteReparte())==0 &&
						(!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
								&& !cota.getClassificacao().equals(ClassificacaoCota.MaximoMinimo)
								&& !cota.getClassificacao().equals(ClassificacaoCota.CotaMix)
								&& !cota.getClassificacao().equals(ClassificacaoCota.RedutorAutomatico)
								&& !cota.getClassificacao().equals(ClassificacaoCota.BancaSoComEdicaoBaseAberta)
								)){
					
//					RepCalculado Cota = RepCalculado Cota + 1
					cota.setReparteCalculado(cota.getReparteCalculado().add(BigDecimal.ONE));
//    				ReservaAjuste = ReservaAjuste � 1
					getEstudo().setAjusteReparte(getEstudo().getAjusteReparte().subtract(BigDecimal.ONE));
					
					if(getEstudo().getAjusteReparte().compareTo(BigDecimal.ZERO)==0
							|| getEstudo().getAjusteReparte().compareTo(BigDecimal.ZERO)==-1) break;
				}
			}
			
		}
		
		if(getEstudo().getAjusteReparte().compareTo(BigDecimal.ZERO)==1){
			
			Comparator<Cota> orderCotaDesc = new Comparator<Cota>(){
				@Override
				public int compare(Cota c1, Cota c2) {
					return c2.getReparteCalculado().compareTo(c1.getReparteCalculado());
				}
			};
			
			Collections.sort(getEstudo().getCotas(),orderCotaDesc);
			
			for(Cota cota:getEstudo().getCotas()){
				if (!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
						&& !cota.getClassificacao().equals(	ClassificacaoCota.MaximoMinimo)
						&& !cota.getClassificacao().equals(	ClassificacaoCota.CotaMix)) 
					cota.setReparteCalculado(cota.getReparteCalculado().add(BigDecimal.ONE));
			}
			
		}
		
		
		getEstudo().setAjusteReparte(getEstudo().getAjusteReparte());
    }
    

}
