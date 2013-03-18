package br.com.abril.nds.process.bonificacoes;

import java.math.BigDecimal;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustecota.AjusteCota;
import br.com.abril.nds.process.medias.Medias;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link Medias} Próximo Processo: {@link AjusteCota} </p>
 */
public class Bonificacoes extends ProcessoAbstrato {

    public Bonificacoes(Cota cota) {
	super(cota);
    }

    @Override
    protected void executarProcesso() {

	Cota cota = (Cota) super.genericDTO;

	BigDecimal indiceTratamentoRegional = BigDecimal.ONE;

	cota.setIndiceTratamentoRegional(indiceTratamentoRegional);

    }

}
