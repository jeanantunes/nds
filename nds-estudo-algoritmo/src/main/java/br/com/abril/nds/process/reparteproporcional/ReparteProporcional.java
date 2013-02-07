package br.com.abril.nds.process.reparteproporcional;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.encalhemaximo.EncalheMaximo;
import br.com.abril.nds.process.reparteminimo.ReparteMinimo;

/**
 * Processo que tem por objetivo fazer um reparte proporcional entre as cotas, se houver alguma edição aberta nas bases
 * e essa cota só recebeu edições abertas.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link ReparteMinimo}
 * Próximo Processo: {@link EncalheMaximo}</p>
 */
public class ReparteProporcional extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
        // TODO: concluir implementação do método calcular do Processo ReparteProporcional
    	// TODO: ainda resta efetuar a consulta dos parâmetros que alimentam o método
    	boolean temEdicaoBaseAberta = false;
    	for (ProdutoEdicao edicao : estudo.getEdicoesBase()) {
    		if (edicao.isEdicaoAberta()) {
    			temEdicaoBaseAberta = true;
    			break;
    		}
    	}
    	for (Cota cota : estudo.getCotas()) {
    		if (temEdicaoBaseAberta && cota.isCotaSoRecebeuEdicaoAberta()) {
    			// TODO: desenvolver lógica!
    		}
    	}
    }
}
