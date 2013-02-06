package br.com.abril.nds.process;


/**
 * Processo que tem como objetivo efetuar o c�lculo da divis�o do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * considera��o todas as vari�veis tamb�m definidas no setup. <p
 * style="white-space: pre-wrap;">SubProcessos:
 *      - {@link BaseParaVeraneio}
 *      - {@link BaseParaSaidaVeraneio}
 * Processo Pai:
 *      - N/A
 *
 * Processo Anterior: N/A
 * Pr�ximo Processo: {@link SomarFixacoes}</p>
 */
public class DefinicaoBases extends ProcessoAbstrato {

    @Override
    public void executarProcesso() throws Exception {
        calcular();
        new BaseParaVeraneio().executar(estudo);
        new BaseParaSaidaVeraneio().executar(estudo);
    }

    @Override
    public void calcular() {
        // TODO: implementar m�todo calcular do Processo DefinicaoBases
    }
}
