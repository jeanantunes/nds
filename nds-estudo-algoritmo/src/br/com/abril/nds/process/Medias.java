package br.com.abril.nds.process;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.correcaovendas.CorrecaoVendas;

/**
 * Processo que tem como objetivo efetuar o c�lculo da divis�o do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em considera��o todas as vari�veis tamb�m definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link CorrecaoVendas}
 * Pr�ximo Processo: {@link Bonificacoes}</p>
 */
public class Medias extends ProcessoAbstrato {

    public Medias(Estudo estudo) {
    	super(estudo);
	}

	@Override
    protected void executarProcesso() {
		System.out.println("Medias : " + this.getEstudo());
    }

    @Override
    protected void calcular() {
        // TODO: implementar m�todo calcular do Processo Medias
    }
    
}
