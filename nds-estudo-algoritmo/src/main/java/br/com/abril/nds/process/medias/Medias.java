package br.com.abril.nds.process.medias;

import java.math.BigDecimal;
import java.util.List;
import java.util.TreeMap;

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
public class Medias extends ProcessoAbstrato {

    public Medias(Cota cota) {
	super(cota);
    }

    @Override
    protected void executarProcesso() {

	BigDecimal vendaMediaCorrigida = BigDecimal.ZERO;

	Cota cota = (Cota) super.genericDTO;

	List<ProdutoEdicao> listProdutoEdicao = cota.getEdicoesRecebidas();

	int qtdeEdicaoBase = listProdutoEdicao.size();

	BigDecimal totalPeso = BigDecimal.ZERO;
	BigDecimal totalVendaMultiplyPeso = BigDecimal.ZERO;

	TreeMap<BigDecimal, BigDecimal> treeVendaPeso = new TreeMap<BigDecimal, BigDecimal>();

	int iProdutoEdicao = 0;
	while (iProdutoEdicao < qtdeEdicaoBase) {

	    ProdutoEdicao produtoEdicao = listProdutoEdicao.get(iProdutoEdicao);

	    BigDecimal peso = new BigDecimal(produtoEdicao.getPeso());
	    BigDecimal vendaCorrigida = produtoEdicao.getVendaCorrigida();

	    if (vendaCorrigida != null && peso != null) {

		treeVendaPeso.put(vendaCorrigida, peso);

		totalPeso = totalPeso.add(peso);
		totalVendaMultiplyPeso = totalVendaMultiplyPeso.add(vendaCorrigida.multiply(peso));
	    }

	    iProdutoEdicao++;
	}

	if (totalPeso.compareTo(BigDecimal.ONE) == 1) {
	    if (qtdeEdicaoBase < 3) {
		vendaMediaCorrigida = totalVendaMultiplyPeso.divide(totalPeso, 2, BigDecimal.ROUND_FLOOR);
	    } else {

		BigDecimal menorValor = treeVendaPeso.firstEntry().getKey();
		BigDecimal menorPeso = treeVendaPeso.firstEntry().getValue();
		BigDecimal menorMultiply = menorValor.multiply(menorPeso);

		vendaMediaCorrigida = totalVendaMultiplyPeso.subtract(menorMultiply).divide(totalPeso.subtract(menorPeso), 2, BigDecimal.ROUND_FLOOR);
	    }
	}

	cota.setVendaMedia(vendaMediaCorrigida);
    }

}
