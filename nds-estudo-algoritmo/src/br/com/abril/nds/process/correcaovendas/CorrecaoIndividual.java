package br.com.abril.nds.process.correcaovendas;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - {@link CorrecaoVendas}
 * 
 * Processo Anterior: N/A
 * Próximo Processo: {@link CorrecaoTendencia}</p>
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
        // TODO: implementar método calcular do SunProcesso CorrecaoIndividual
    }
    
}
