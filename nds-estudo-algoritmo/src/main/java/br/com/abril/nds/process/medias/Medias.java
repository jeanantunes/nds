package br.com.abril.nds.process.medias;

import java.math.BigDecimal;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
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
public class Medias {

    public void executar(CotaEstudo cota) {

	BigDecimal vendaMediaCorrigida = BigDecimal.ZERO;

	BigDecimal totalPeso = BigDecimal.ZERO;
	BigDecimal totalVendaMultiplyPeso = BigDecimal.ZERO;

	TreeMap<BigDecimal, BigDecimal> treeVendaPeso = new TreeMap<>();

	if (cota.getEdicoesRecebidas() != null) {
	    for (ProdutoEdicaoEstudo edicao : cota.getEdicoesRecebidas()) {
		if (edicao.getVendaCorrigida() != null && edicao.getIndicePeso() != null) {
		    treeVendaPeso.put(edicao.getVendaCorrigida(), edicao.getIndicePeso());

		    totalPeso = totalPeso.add(edicao.getIndicePeso());
		    totalVendaMultiplyPeso = totalVendaMultiplyPeso.add(edicao.getVendaCorrigida().multiply(edicao.getIndicePeso()));
		}
	    }
	}

	if (totalPeso.compareTo(BigDecimal.ONE) == 1) {
	    if (cota.getEdicoesRecebidas().size() < 3) {
		vendaMediaCorrigida = totalVendaMultiplyPeso.divide(totalPeso, 2, BigDecimal.ROUND_HALF_UP);
	    } else {
		BigDecimal menorValor = treeVendaPeso.firstEntry().getKey();
		BigDecimal menorPeso = treeVendaPeso.firstEntry().getValue();
		BigDecimal menorMultiply = menorValor.multiply(menorPeso);
		
		cota.setMenorVenda(menorValor);
		cota.setPesoMenorVenda(menorPeso);

		vendaMediaCorrigida = totalVendaMultiplyPeso.subtract(menorMultiply).divide(totalPeso.subtract(menorPeso), 2, BigDecimal.ROUND_HALF_UP);
	    }
	}
	if ((vendaMediaCorrigida != null) && (vendaMediaCorrigida.compareTo(BigDecimal.ZERO) > 0)) {
	    cota.setVendaMediaCorrigida(vendaMediaCorrigida); // utilizada apenas como controle no resumo final
	    cota.setVendaMedia(vendaMediaCorrigida);
	}
    }
}
