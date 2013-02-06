package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o c�lculo da divis�o do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em considera��o todas as vari�veis tamb�m definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - {@link CorrecaoIndividual}
 *      - {@link CorrecaoTendencia}
 *      - {@link VendaCrescente}
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link MontaTabelaEstudos}
 * Pr�ximo Processo: {@link Medias}</p>
 */
public class CorrecaoVendas extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() throws Exception {
        calcular();
        new CorrecaoIndividual().executar(estudo);
        new CorrecaoTendencia().executar(estudo);
        new VendaCrescente().executar(estudo);
    }

    @Override
    protected void calcular() {
        // TODO: implementar m�todo calcular do Processo CorrecaoVendas
    }
    
}
