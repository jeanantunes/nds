package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CorrecaoVendas}
 * 
 * Processo Anterior: {@link CorrecaoIndividual} Próximo Processo: {@link VendaCrescente} </p>
 */
public class CorrecaoTendencia extends ProcessoAbstrato {

    private BigDecimal totalReparte;
    private BigDecimal totalVenda;

    public CorrecaoTendencia(Cota cota, BigDecimal totalReparte, BigDecimal totalVenda) {
	super(cota);
	this.totalReparte = totalReparte;
	this.totalVenda = totalVenda;
    }

    /**
     * <h2>Sub Processo: Correção de Tendência</h2>
     * <p>
     * <b>Aplicar para cada cota</b>
     * </p>
     * <p>%Venda = TOTAL REP por cota / TOTAL VDA por cota</p>
     * <p>ÍndiceCorrTendência = 1</p>
     * <pre>Se %Venda = 1</pre>
     * <pre><pre>ÍndiceCorrTendência = 1,2</pre></pre>
     * <pre>Senão</pre>
     * <pre><pre><pre>Se %Venda >= 0,9</pre></pre></pre>
     * <pre><pre><pre><pre>ÍndiceCorrTendência = 1,1</pre></pre></pre></pre>
     * <pre><pre><pre>Endif</pre></pre></pre>
     * <pre>Endif</pre>
     */
    @Override
    protected void executarProcesso() throws Exception {

	BigDecimal indiceCorrecaoTendencia = BigDecimal.ONE;

	Cota cota = (Cota) super.genericDTO;

	if (this.totalVenda.compareTo(BigDecimal.ZERO) != 0) {

	    BigDecimal percentualVenda = this.totalVenda.divide(this.totalReparte, 1, BigDecimal.ROUND_FLOOR);

	    if (percentualVenda.compareTo(BigDecimal.ONE) == 0) {
		indiceCorrecaoTendencia = indiceCorrecaoTendencia.add(new BigDecimal(0.2).divide(
			BigDecimal.ONE, 1, BigDecimal.ROUND_FLOOR));
	    } else {

		BigDecimal decimalCompare = new BigDecimal(0.9).divide(BigDecimal.ONE, 1, BigDecimal.ROUND_FLOOR);

		if (percentualVenda.compareTo(decimalCompare) >= 0) {
		    indiceCorrecaoTendencia = indiceCorrecaoTendencia.add(new BigDecimal(0.1).divide(
				BigDecimal.ONE, 1, BigDecimal.ROUND_FLOOR));
		}
	    }
	}

	cota.setIndiceCorrecaoTendencia(indiceCorrecaoTendencia);

    }

}
