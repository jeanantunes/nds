package br.com.abril.nds.process.calculoreparte;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo ajustar o reparte definido na cota entre o mínimo e o máximo configurado
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CalcularReparte}
 * 
 * Processo Anterior: N/A Próximo Processo: {@link GravarReparteJuramentado}
 * </p>
 */
@Component
public class MinimoMaximo extends ProcessoAbstrato {

    @Override
    public void executar(EstudoTransient estudo) throws Exception {

	for (CotaEstudo cota : estudo.getCotas()) {
	    if ((cota.getReparteMinimo() != null) && (cota.getReparteMaximo() != null)) {
		if (cota.getReparteMinimo().compareTo(cota.getReparteMaximo()) > 0) {
		    throw new Exception(String.format("O reparte mínimo da cota %s está maior que o reparte máximo.", cota.getId()));
		}
		if (cota.getReparteCalculado().compareTo(cota.getReparteMinimo()) < 0) {
		    cota.setReparteCalculado(cota.getReparteMinimo(), estudo);

		    if (cota.isMix()) {
			cota.setClassificacao(ClassificacaoCota.CotaMix);
		    } else {
			cota.setClassificacao(ClassificacaoCota.MaximoMinimo);
		    }
		} else if (cota.getReparteCalculado().compareTo(cota.getReparteMaximo()) > 0) {
		    cota.setReparteCalculado(cota.getReparteMaximo(), estudo);

		    if (cota.isMix()) {
			cota.setClassificacao(ClassificacaoCota.CotaMix);
		    } else {
			cota.setClassificacao(ClassificacaoCota.MaximoMinimo);
		    }
		}
	    }
	}
    }
}
