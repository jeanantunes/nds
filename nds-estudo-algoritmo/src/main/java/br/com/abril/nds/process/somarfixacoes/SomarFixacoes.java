package br.com.abril.nds.process.somarfixacoes;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.definicaobases.DefinicaoBases;
import br.com.abril.nds.process.verificartotalfixacoes.VerificarTotalFixacoes;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link DefinicaoBases} Próximo Processo:
 * {@link VerificarTotalFixacoes}
 * </p>
 */
public class SomarFixacoes extends ProcessoAbstrato {

    public SomarFixacoes(Estudo estudo) {
	super(estudo);
    }

    @Override
    protected void executarProcesso() {
    }

}
