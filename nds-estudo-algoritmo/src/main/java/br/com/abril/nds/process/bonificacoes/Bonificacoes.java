package br.com.abril.nds.process.bonificacoes;

<<<<<<< HEAD
import java.math.BigDecimal;

import br.com.abril.nds.model.Cota;
=======
import org.springframework.stereotype.Component;

>>>>>>> branch 'master' of https://adenilton@bitbucket.org/pedroxs/nds.git
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
@Component
public class Bonificacoes extends ProcessoAbstrato {
<<<<<<< HEAD

    public Bonificacoes(Cota cota) {
	super(cota);
    }
=======
>>>>>>> branch 'master' of https://adenilton@bitbucket.org/pedroxs/nds.git

    @Override
    protected void executarProcesso() {

	Cota cota = (Cota) super.genericDTO;

	BigDecimal indiceTratamentoRegional = BigDecimal.ONE;

	cota.setIndiceTratamentoRegional(indiceTratamentoRegional);

    }

}
