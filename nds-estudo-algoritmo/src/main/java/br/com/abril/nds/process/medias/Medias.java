package br.com.abril.nds.process.medias;

import java.math.BigDecimal;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.bonificacoes.Bonificacoes;
import br.com.abril.nds.process.correcaovendas.CorrecaoVendas;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link CorrecaoVendas} Próximo Processo: {@link Bonificacoes} </p>
 */
@Component
public class Medias extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {

	BigDecimal vendaMediaCorrigida = BigDecimal.ZERO;

	Cota cota = (Cota) super.genericDTO;
	
	BigDecimal totalPeso = BigDecimal.ZERO;
	BigDecimal totalVendaMultiplyPeso = BigDecimal.ZERO;

	TreeMap<BigDecimal, BigDecimal> treeVendaPeso = new TreeMap<>();

	for (ProdutoEdicao edicao : cota.getEdicoesRecebidas()) {
	    if (edicao.getVendaCorrigida() != null && edicao.getPeso() != null) {
		treeVendaPeso.put(edicao.getVendaCorrigida(), edicao.getPeso());

		totalPeso = totalPeso.add(edicao.getPeso());
		totalVendaMultiplyPeso = totalVendaMultiplyPeso.add(edicao.getVendaCorrigida().multiply(edicao.getPeso()));
	    }
	}

	if (totalPeso.compareTo(BigDecimal.ONE) == 1) {
	    if (cota.getEdicoesRecebidas().size() < 3) {
		vendaMediaCorrigida = totalVendaMultiplyPeso.divide(totalPeso, 2, BigDecimal.ROUND_FLOOR);
	    } else {
		BigDecimal menorValor = treeVendaPeso.firstEntry().getKey();
		BigDecimal menorPeso = treeVendaPeso.firstEntry().getValue();
		BigDecimal menorMultiply = menorValor.multiply(menorPeso);

		vendaMediaCorrigida = totalVendaMultiplyPeso.subtract(menorMultiply).divide(totalPeso.subtract(menorPeso), 2, BigDecimal.ROUND_FLOOR);
	    }
	}
	if ((vendaMediaCorrigida != null) && (vendaMediaCorrigida.compareTo(BigDecimal.ZERO) > 0)) {
	    cota.setVendaMedia(vendaMediaCorrigida);
	}
    }
}
