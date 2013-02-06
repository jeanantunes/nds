package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - {@link CorrecaoIndividual}
 *      - {@link CorrecaoTendencia}
 *      - {@link VendaCrescente}
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link MontaTabelaEstudos}
 * Próximo Processo: {@link Medias}</p>
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
        // TODO: implementar método calcular do Processo CorrecaoVendas
    }
    
}
