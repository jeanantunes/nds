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
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 	- N/A
 * Processo Pai:
 * 	- {@link CalcularReparte}
 * 
 * Processo Anterior: {@link GravarReparteJuramentado}
 * Próximo Processo: {@link ReparteComplementarPorCota}
 * </p>
 */
public class AjusteFinalReparte extends ProcessoAbstrato {

    public AjusteFinalReparte(Estudo estudo) {
	super(estudo);
    }

    @Override
    protected void executarProcesso() throws Exception {

	// Variável AjusteReparte modificada no faq FAQF2-53
	// Se ReservaAjuste > 0
	if (getEstudo().getAjusteReparte() != null) {
	    if (getEstudo().getAjusteReparte().compareTo(BigDecimal.ZERO) == 1) {

		// Verificar Cota a Cota
		// Se Repcalculado < Venda (última edição fechada, sem correção)

		for(Cota cota : getEstudo().getCotas()){
		    // Se Repcalculado < Venda (Última edição fechada, sem correção)
		    // Se Cota <> FX / MM / MX / RD / PR
		    if ((cota.getReparteCalculado().compareTo(getEstudo().getAjusteReparte()) <= 0) &&
			    (cota.getClassificacao().notIn(ClassificacaoCota.ReparteFixado,
				    ClassificacaoCota.MaximoMinimo, ClassificacaoCota.CotaMix,
				    ClassificacaoCota.RedutorAutomatico, ClassificacaoCota.BancaSoComEdicaoBaseAberta))) {
			// RepCalculado Cota = RepCalculado Cota + 1
			cota.setReparteCalculado(cota.getReparteCalculado().add(BigDecimal.ONE));
			// ReservaAjuste = ReservaAjuste � 1
			getEstudo().setAjusteReparte(getEstudo().getAjusteReparte().subtract(BigDecimal.ONE));

			if (getEstudo().getAjusteReparte().compareTo(BigDecimal.ZERO) <= 0) {
			    break;
			}
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

		Collections.sort(getEstudo().getCotas(), orderCotaDesc);

		for (Cota cota : getEstudo().getCotas()) {
		    if (cota.getClassificacao().notIn(ClassificacaoCota.ReparteFixado, ClassificacaoCota.MaximoMinimo, ClassificacaoCota.CotaMix)) 
			cota.setReparteCalculado(cota.getReparteCalculado().add(BigDecimal.ONE));
		}
	    }
	}
	
	
	ReparteComplementarPorCota reparteComplementarPorCota = new ReparteComplementarPorCota(getEstudo());
	reparteComplementarPorCota.executar();

	GravarReparteFinalCota gravarReparteFinalCota = new GravarReparteFinalCota(getEstudo());
	gravarReparteFinalCota.executar();
    }
}