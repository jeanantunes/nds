package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
 * Processo Anterior: {@link CorrecaoTendencia} Próximo Processo: N/A
 * </p>
 */
public class VendaCrescente extends ProcessoAbstrato {

    public VendaCrescente(Cota cota) {
	super(cota);
    }

    @Override
    protected void executarProcesso() {

	BigDecimal indiceVendaCrescente = BigDecimal.ONE;

	Cota cota = (Cota) super.getGenericDTO();

	List<EstoqueProdutoCota> listEstoqueProdutoCota = cota
		.getEstoqueProdutoCotas();

	List<Boolean> listDivBoolean = new ArrayList<Boolean>();

	if (listEstoqueProdutoCota != null
		&& listEstoqueProdutoCota.size() >= 4) {

	    int iEstoqueProdutoCota = 0;
	    while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota
			.get(iEstoqueProdutoCota);

		EstoqueProdutoCota previousEstoqueProdutoCota = null;
		if (iEstoqueProdutoCota > 0) {

		    previousEstoqueProdutoCota = listEstoqueProdutoCota
			    .get(iEstoqueProdutoCota-1);

		    BigDecimal quantidadeRecebida = estoqueProdutoCota
			    .getQuantidadeRecebida();
		    BigDecimal quantidadeDevolvida = estoqueProdutoCota
			    .getQuantidadeDevolvida();

		    BigDecimal vendaEdicao = quantidadeRecebida
			    .subtract(quantidadeDevolvida);

		    BigDecimal previousQuantidadeRecebida = previousEstoqueProdutoCota
			    .getQuantidadeRecebida();
		    BigDecimal previousQuantidadeDevolvida = previousEstoqueProdutoCota
			    .getQuantidadeDevolvida();

		    BigDecimal previousVendaEdicao = previousQuantidadeRecebida
			    .subtract(previousQuantidadeDevolvida);

		    if (previousEstoqueProdutoCota
			    .getProdutoEdicao()
			    .getNome()
			    .equalsIgnoreCase(
				    estoqueProdutoCota.getProdutoEdicao()
					    .getNome())
			    && !estoqueProdutoCota.getProdutoEdicao()
				    .isEdicaoAberta()) {

			if (previousVendaEdicao.compareTo(BigDecimal.ZERO) == 1) {
			    BigDecimal divVendaEdicao = vendaEdicao.divide(
				    previousVendaEdicao, 2,
				    BigDecimal.ROUND_FLOOR);

			    listDivBoolean.add(divVendaEdicao
				    .compareTo(BigDecimal.ONE) == 1);
			}

		    }
		}

		iEstoqueProdutoCota++;
	    }

	    if (!listDivBoolean.isEmpty()
		    && !listDivBoolean.contains(Boolean.FALSE)) {
		indiceVendaCrescente = indiceVendaCrescente.add(new BigDecimal(
			0.1));
	    }

	    indiceVendaCrescente = indiceVendaCrescente.divide(
		    new BigDecimal(1), 2, BigDecimal.ROUND_FLOOR);

	}

	cota.setIndiceVendaCrescente(indiceVendaCrescente);

	super.genericDTO = cota;
    }
}
