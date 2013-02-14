package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.dao.EstoqueProdutoCotaDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;
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

    public CorrecaoTendencia(Cota cota) {
	super(cota);
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
	cota.setEstoqueProdutoCotas(new EstoqueProdutoCotaDAO()
		.getByCotaId(cota.getId()));

	BigDecimal totalReparte = BigDecimal.ZERO;
	BigDecimal totalVenda = BigDecimal.ZERO;

	int iEdicaoBase = 0;
	while (iEdicaoBase < cota.getEstoqueProdutoCotas().size()) {

	    EstoqueProdutoCota estoqueProdutoCota = cota
		    .getEstoqueProdutoCotas().get(iEdicaoBase);

	    BigInteger quantidadeRecebida = estoqueProdutoCota
		    .getQuantidadeRecebida().toBigInteger();
	    BigInteger quantidadeDevolvida = estoqueProdutoCota
		    .getQuantidadeDevolvida().toBigInteger();

	    totalReparte = totalReparte.add(new BigDecimal(quantidadeRecebida));

	    BigInteger vendaCota = quantidadeRecebida
		    .subtract(quantidadeDevolvida);

	    totalVenda = totalVenda.add(new BigDecimal(vendaCota));

	    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(
		    estoqueProdutoCota);
	    correcaoIndividual.executar();

	    cota.getEstoqueProdutoCotas().set(iEdicaoBase,
		    (EstoqueProdutoCota) correcaoIndividual.getGenericDTO());

	    iEdicaoBase++;
	}

	if (totalVenda.compareTo(BigDecimal.ZERO) != 0) {

	    BigDecimal percentualVenda = totalVenda.divide(totalReparte, 1,
		    BigDecimal.ROUND_FLOOR);

	    BigDecimal oneCompare = BigDecimal.ONE;
	    oneCompare = oneCompare.divide(new BigDecimal(1), 1,
		    BigDecimal.ROUND_FLOOR);

	    if (percentualVenda.compareTo(oneCompare) == 0) {
		indiceCorrecaoTendencia = indiceCorrecaoTendencia
			.add(new BigDecimal(0.2));
	    } else {

		BigDecimal decimalCompare = new BigDecimal(0.9);
		decimalCompare = decimalCompare.divide(new BigDecimal(1), 1,
			BigDecimal.ROUND_FLOOR);

		if (percentualVenda.compareTo(decimalCompare) >= 0) {
		    indiceCorrecaoTendencia = indiceCorrecaoTendencia
			    .add(new BigDecimal(0.1));
		}
	    }
	}

	indiceCorrecaoTendencia = indiceCorrecaoTendencia.divide(
		new BigDecimal(1), 1, BigDecimal.ROUND_FLOOR);

	cota.setIndiceCorrecaoTendencia(indiceCorrecaoTendencia);

    }

}
