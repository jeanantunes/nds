package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.CotaEstudo;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CorrecaoVendas}
 * 
 * Processo Anterior: {@link CorrecaoIndividual} Próximo Processo: {@link VendaCrescente}
 * </p>
 */
@Component
public class CorrecaoTendencia {

    /**
     * <h2>Sub Processo: Correção de Tendência</h2>
     * <p>
     * <b>Aplicar para cada cota</b>
     * </p>
     * <p>
     * %Venda = TOTAL REP por cota / TOTAL VDA por cota
     * </p>
     * <p>
     * ÍndiceCorrTendência = 1
     * </p>
     * 
     * <pre>
     * Se %Venda = 1
     * </pre>
     * 
     * <pre>
     * 
     * <pre>
     * ÍndiceCorrTendência = 1,2
     * </pre>
     * 
     * </pre>
     * 
     * <pre>
     * Senão
     * </pre>
     * 
     * <pre>
     * 
     * <pre>
     * 
     * <pre>
     * Se %Venda >= 0,9
     * </pre>
     * 
     * </pre></pre>
     * 
     * <pre>
     * 
     * <pre>
     * 
     * <pre>
     * 
     * <pre>
     * ÍndiceCorrTendência = 1,1
     * </pre>
     * 
     * </pre></pre></pre>
     * 
     * <pre>
     * 
     * <pre>
     * 
     * <pre>
     * Endif
     * </pre>
     * 
     * </pre></pre>
     * 
     * <pre>
     * Endif
     * </pre>
     */
    public void executar(CotaEstudo cota, BigDecimal totalReparte, BigDecimal totalVenda) throws Exception {

	BigDecimal indiceCorrecaoTendencia = BigDecimal.ONE;

	if (totalVenda.compareTo(BigDecimal.ZERO) != 0) {
	    BigDecimal percentualVenda = totalVenda.divide(totalReparte, 4, BigDecimal.ROUND_HALF_UP);

	    if (percentualVenda.compareTo(BigDecimal.ONE) == 0) {
		indiceCorrecaoTendencia = indiceCorrecaoTendencia.add(new BigDecimal(0.2).divide(BigDecimal.ONE, 3, BigDecimal.ROUND_HALF_UP));
	    } else {
		BigDecimal decimalCompare = new BigDecimal(0.9).divide(BigDecimal.ONE, 3, BigDecimal.ROUND_HALF_UP);

		if (percentualVenda.compareTo(decimalCompare) >= 0) {
		    indiceCorrecaoTendencia = indiceCorrecaoTendencia.add(new BigDecimal(0.1).divide(BigDecimal.ONE, 3, BigDecimal.ROUND_HALF_UP));
		}
	    }
	}
	cota.setIndiceCorrecaoTendencia(indiceCorrecaoTendencia);
    }
}
