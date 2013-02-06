package br.com.abril.nds.process;

import br.com.abril.nds.model.Estrategia;


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
    public void executarProcesso() {
        executar();
        new BaseParaVeraneio().executar(estudo);
        new BaseParaSaidaVeraneio().executar(estudo);
    }

    @Override
    public void executar() {
        // TODO: implementar m�todo calcular do Processo DefinicaoBases
    	Estrategia estrategia;
    	
//    	estrategia.getEdicaoBases();
    }
}
