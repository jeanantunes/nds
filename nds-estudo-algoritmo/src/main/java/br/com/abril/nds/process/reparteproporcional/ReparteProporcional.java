package br.com.abril.nds.process.reparteproporcional;

import java.math.BigDecimal;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
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
	
	Estudo estudo = (Estudo) super.genericDTO;
	
        // TODO: concluir implementação do método calcular do Processo ReparteProporcional
    	// TODO: ainda resta efetuar a consulta dos parâmetros que alimentam o método
    	boolean temEdicaoBaseAberta = false;
    	for (ProdutoEdicao edicao : estudo.getEdicoesBase()) {
    		if (edicao.isEdicaoAberta()) {
    			temEdicaoBaseAberta = true;
    			break;
    		}
    	}
    	BigDecimal somaReparteProporcional = new BigDecimal(0);
    	BigDecimal indiceReparteEdicoesAbertas = estudo.getReparteDistribuirInicial().divide(estudo.getSomatoriaReparteEdicoesAbertas());
    	for (Cota cota : estudo.getCotas()) {
    		if (temEdicaoBaseAberta && cota.isCotaSoRecebeuEdicaoAberta()) {
    			BigDecimal repCalculado = cota.getSomaReparteEdicoesAbertas().multiply(indiceReparteEdicoesAbertas);
    			// FIXME: checar se o arredondamento está correto (Processo: Reparte Proporcional)
    			repCalculado = new BigDecimal(Math.floor(repCalculado.doubleValue()));
    			cota.setReparteCalculado(repCalculado);
    			cota.setClassificacao(ClassificacaoCota.BancaSoComEdicaoBaseAberta);
    			somaReparteProporcional.add(cota.getReparteCalculado());
    		}
    	}
    	
    	super.genericDTO = estudo;
    }
}
