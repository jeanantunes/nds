package br.com.abril.nds.process.calculoreparte;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o c√°lculo da divis√£o do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * considera√ß√£o todas as vari√°veis tamb√©m definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CalcularReparte}
 * 
 * Processo Anterior: {@link GravarReparteJuramentado} Pr√≥ximo Processo:
 * {@link ReparteComplementarPorCota}
 * </p>
 */
public class AjusteFinalReparte extends ProcessoAbstrato {

    public AjusteFinalReparte(Estudo estudo) {
	super(estudo);
    }

    @Override
    protected void executarProcesso() {
    	

//		Se ReservaAjuste > 0
//			Verificar Cota a Cota
		
    	BigDecimal reservaAjuste = BigDecimal.ONE;
		for(Cota cota:estudo.getCotas()){
//    		Se Repcalculado < Venda (˙ltima ediÁ„o fechada, sem correÁ„o)
//    				Se Cota <> FX / MM / MX / RD / PR
    		if(cota.getReparteCalculado().compareTo(reservaAjuste)==-1 ||
    				cota.getReparteCalculado().compareTo(reservaAjuste)==0 &&
    				(!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
    				 && !cota.getClassificacao().equals(ClassificacaoCota.MaximoMinimo)
    				 && !cota.getClassificacao().equals(ClassificacaoCota.CotaMix)
    				 && !cota.getClassificacao().equals(ClassificacaoCota.RedutorAutomatico)
    			     && !cota.getClassificacao().equals(ClassificacaoCota.BancaSoComEdicaoBaseAberta)
    				 )){
    			
    			cota.setReparteCalculado(cota.getReparteCalculado().subtract(reservaAjuste));
//    			ReservaAjuste = ReservaAjuste ñ 1
    			//TODO: Fazer essa linha:
    			if(reservaAjuste.compareTo(BigDecimal.ZERO)==0) break;
    		}
    	}
    	
    	
    	Comparator<Cota> orderCotaDesc = new Comparator<Cota>(){
			@Override
			public int compare(Cota c1, Cota c2) {
				return c2.getReparteCalculado().compareTo(c1.getReparteCalculado());
			}
		};
		
		Collections.sort(estudo.getCotas(),orderCotaDesc);
		
		for(Cota cota:estudo.getCotas()){
			if (!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
					&& !cota.getClassificacao().equals(	ClassificacaoCota.MaximoMinimo)
					&& !cota.getClassificacao().equals(	ClassificacaoCota.CotaMix)) 
				cota.setReparteCalculado(cota.getReparteCalculado().add(reservaAjuste));
		}
    	
    }

}
