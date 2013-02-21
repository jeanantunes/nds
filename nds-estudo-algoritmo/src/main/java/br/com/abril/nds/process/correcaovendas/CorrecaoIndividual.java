package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;

import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CorrecaoVendas}
 * 
 * Processo Anterior: N/A Próximo Processo: {@link CorrecaoTendencia} </p>
 */
public class CorrecaoIndividual extends ProcessoAbstrato {

    public CorrecaoIndividual(ProdutoEdicao produtoEdicao) {
	super(produtoEdicao);
    }

    /**
     * Sub Processo: Correção Individual
     * 
     * Aplicar para cada edição-base, cota a cota
     * 
     * %Venda = Venda / Reparte ÍndiceCorreção = 1
     * 
     * Se %Venda = 1 ÍndiceCorreção = 1,2 Senão Se %Venda >= 0,9 ÍndiceCorreção
     * = 1,1 Endif Endif
     * 
     * VendaCorrigida = Venda * ÍndiceCorreção Gravar VendaCorrig para cada
     * edição-base de cada cota.
     */
    @Override
    protected void executarProcesso() throws Exception {

	ProdutoEdicao produtoEdicao = (ProdutoEdicao) super.genericDTO;

	BigDecimal indiceCorrecao = BigDecimal.ONE;

	if (produtoEdicao.getVenda().compareTo(BigDecimal.ZERO) == 1) {
	    BigDecimal percentualVenda = produtoEdicao.getVenda().divide(
		    produtoEdicao.getReparte(), 1, BigDecimal.ROUND_FLOOR);

	    if (percentualVenda.compareTo(BigDecimal.ONE) == 0) {
		indiceCorrecao = indiceCorrecao.add(new BigDecimal(0.2).divide(
			BigDecimal.ONE, 1, BigDecimal.ROUND_FLOOR));
	    } else {

		BigDecimal decimalCompare = new BigDecimal(0.9).divide(
			BigDecimal.ONE, 1, BigDecimal.ROUND_FLOOR);

		if (percentualVenda.compareTo(decimalCompare) >= 0) {
		    indiceCorrecao = indiceCorrecao.add(new BigDecimal(0.1)
			    .divide(BigDecimal.ONE, 1, BigDecimal.ROUND_FLOOR));
		}
	    }
	}

	produtoEdicao.setIndiceCorrecao(indiceCorrecao);

	produtoEdicao.setVendaCorrigida(produtoEdicao.getVenda().multiply(
		indiceCorrecao));
    }

}
