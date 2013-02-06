package br.com.abril.nds.process.correcaovendas;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o c�lculo da divis�o do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em considera��o todas as vari�veis tamb�m definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - {@link CorrecaoVendas}
 * 
 * Processo Anterior: N/A
 * Pr�ximo Processo: {@link CorrecaoTendencia}</p>
 */
public class CorrecaoIndividual extends ProcessoAbstrato {
	
    public CorrecaoIndividual(Estudo estudo) {
		super(estudo);
	}

	@Override
    protected void executarProcesso() {
		System.out.println("CorrecaoIndividual : " + this.getEstudo());
    }

    @Override
    protected void calcular() {
        // TODO: implementar m�todo calcular do SunProcesso CorrecaoIndividual
    }
    
}
