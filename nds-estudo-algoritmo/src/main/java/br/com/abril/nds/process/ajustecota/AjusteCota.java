package br.com.abril.nds.process.ajustecota;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.bonificacoes.Bonificacoes;
import br.com.abril.nds.process.jornaleirosnovos.JornaleirosNovos;

/**
 * Processo que tem como objetivo efetuar o calculo da divisao do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link Bonificacoes} Próximo Processo:
 * {@link JornaleirosNovos}
 * </p>
 */
public class AjusteCota extends ProcessoAbstrato {

    public AjusteCota(Estudo estudo) {
		super(estudo);
	}

	@Override
    protected void executarProcesso() {
    }

}
