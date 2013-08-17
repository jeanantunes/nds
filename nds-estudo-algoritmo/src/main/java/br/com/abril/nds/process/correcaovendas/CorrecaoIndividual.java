package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CorrecaoVendas}
 * 
 * Processo Anterior: N/A Próximo Processo: {@link CorrecaoTendencia}
 * </p>
 */
@Component
public class CorrecaoIndividual {

    /**
     * Sub Processo: Correção Individual
     * 
     * Aplicar para cada edição-base, cota a cota
     * 
     * %Venda = Venda / Reparte ÍndiceCorreção = 1
     * 
     * Se %Venda = 1 ÍndiceCorreção = 1,2 Senão Se %Venda >= 0,9 ÍndiceCorreção = 1,1 Endif Endif
     * 
     * VendaCorrigida = Venda * ÍndiceCorreção Gravar VendaCorrig para cada edição-base de cada cota.
     */
    public void executar(ProdutoEdicaoEstudo produtoEdicao) throws Exception {

	BigDecimal indiceCorrecao = BigDecimal.ONE;

	if (produtoEdicao.getVenda().compareTo(BigDecimal.ZERO) == 1) {
	    BigDecimal percentualVenda = produtoEdicao.getVenda().divide(produtoEdicao.getReparte(), 3, BigDecimal.ROUND_HALF_UP);

	    if (percentualVenda.compareTo(BigDecimal.ONE) == 0) {
		indiceCorrecao = indiceCorrecao.add(BigDecimal.valueOf(0.2).divide(BigDecimal.ONE, 3, BigDecimal.ROUND_HALF_UP));
	    } else {

		BigDecimal decimalCompare = BigDecimal.valueOf(0.9).divide(BigDecimal.ONE, 3, BigDecimal.ROUND_HALF_UP);

		if (percentualVenda.compareTo(decimalCompare) >= 0) {
		    indiceCorrecao = indiceCorrecao.add(BigDecimal.valueOf(0.1).divide(BigDecimal.ONE, 3, BigDecimal.ROUND_HALF_UP));
		}
	    }
	}
	produtoEdicao.setIndiceCorrecao(indiceCorrecao);
	produtoEdicao.setVendaCorrigida(produtoEdicao.getVenda().multiply(indiceCorrecao).divide(BigDecimal.ONE, 3, BigDecimal.ROUND_HALF_UP));
    }

}
