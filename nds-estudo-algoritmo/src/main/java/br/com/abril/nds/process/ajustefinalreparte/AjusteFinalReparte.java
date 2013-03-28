package br.com.abril.nds.process.ajustefinalreparte;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;
import br.com.abril.nds.process.calculoreparte.GravarReparteJuramentado;

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
@Component
public class AjusteFinalReparte extends ProcessoAbstrato {

	@Autowired
	private ReparteComplementarPorCota reparteComplementarPorCota;

	@Autowired
	private GravarReparteFinalCota gravarReparteFinalCota;

	@Override
	public void executar(EstudoTransient estudo) throws Exception {

		// Variável AjusteReparte modificada no faq FAQF2-53
		// Se ReservaAjuste > 0
		if (estudo.getAjusteReparte() != null) {
			if (estudo.getAjusteReparte().compareTo(BigInteger.ZERO) == 1) {

				// Verificar Cota a Cota
				// Se Repcalculado < Venda (última edição fechada, sem correção)

				for(CotaEstudo cota : estudo.getCotas()){
					// Se Repcalculado < Venda (Última edição fechada, sem correção)
					// Se Cota <> FX / MM / MX / RD / PR
					if ((cota.getReparteCalculado().compareTo(estudo.getAjusteReparte()) <= 0) &&
							(cota.getClassificacao().notIn(ClassificacaoCota.ReparteFixado,
									ClassificacaoCota.MaximoMinimo, ClassificacaoCota.CotaMix,
									ClassificacaoCota.RedutorAutomatico, ClassificacaoCota.BancaSoComEdicaoBaseAberta))) {
						// RepCalculado Cota = RepCalculado Cota + 1
						cota.setReparteCalculado(cota.getReparteCalculado().add(BigInteger.ONE));
						// ReservaAjuste = ReservaAjuste + 1
						estudo.setAjusteReparte(estudo.getAjusteReparte().subtract(BigInteger.ONE));

						if (estudo.getAjusteReparte().compareTo(BigInteger.ZERO) <= 0) {
							break;
						}
					}
				}
			}

			if(estudo.getAjusteReparte().compareTo(BigInteger.ZERO)==1){

				Comparator<CotaEstudo> orderCotaDesc = new Comparator<CotaEstudo>(){
					@Override
					public int compare(CotaEstudo c1, CotaEstudo c2) {
						return c2.getReparteCalculado().compareTo(c1.getReparteCalculado());
					}
				};

				Collections.sort(estudo.getCotas(), orderCotaDesc);

				for (CotaEstudo cota : estudo.getCotas()) {
					if (cota.getClassificacao().notIn(ClassificacaoCota.ReparteFixado, ClassificacaoCota.MaximoMinimo, ClassificacaoCota.CotaMix)) 
						cota.setReparteCalculado(cota.getReparteCalculado().add(BigInteger.ONE));
				}
			}
		}
		reparteComplementarPorCota.executar(estudo);

		gravarReparteFinalCota.executar(estudo);
	}
}
