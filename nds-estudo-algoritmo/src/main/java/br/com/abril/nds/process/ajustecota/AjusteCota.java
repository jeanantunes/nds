package br.com.abril.nds.process.ajustecota;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.bonificacoes.Bonificacoes;
import br.com.abril.nds.process.jornaleirosnovos.JornaleirosNovos;

/**
 * Processo que tem como objetivo efetuar o calculo da divisao do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link Bonificacoes} Próximo Processo: {@link JornaleirosNovos}
 * </p>
 */
@Component
public class AjusteCota extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {

	Cota cota = (Cota) super.genericDTO;
	BigDecimal indiceAjusteCota = BigDecimal.ONE;

	if (cota.getAjusteReparte() != null && cota.getAjusteReparte().compareTo(indiceAjusteCota) == 1) {
	    indiceAjusteCota = cota.getAjusteReparte();
	}
	if (cota.getAjusteReparte() != null && cota.getAjusteReparte().compareTo(indiceAjusteCota) == 1) {
	    indiceAjusteCota = cota.getAjusteReparte();
	}
	cota.setIndiceAjusteCota(indiceAjusteCota);
    }
}
