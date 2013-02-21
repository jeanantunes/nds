package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
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

	List<ProdutoEdicao> listProdutoEdicao = cota.getEdicoesRecebidas();

	List<Boolean> listDivBoolean = new ArrayList<Boolean>();

	if (listProdutoEdicao != null && listProdutoEdicao.size() >= 4) {

	    int iProdutoEdicao = 0;
	    while (iProdutoEdicao < listProdutoEdicao.size()) {

		ProdutoEdicao produtoEdicao = listProdutoEdicao.get(iProdutoEdicao);
		ProdutoEdicao previousProdutoEdicao = null;

		if (iProdutoEdicao > 0) {

		    previousProdutoEdicao = listProdutoEdicao.get(iProdutoEdicao - 1);

		    if (previousProdutoEdicao.getIdProduto().equals(produtoEdicao.getIdProduto()) && !previousProdutoEdicao.isEdicaoAberta()
			    && !produtoEdicao.isEdicaoAberta()) {

			if (previousProdutoEdicao.getVenda().compareTo(BigDecimal.ZERO) == 1) {
			    BigDecimal divVendaEdicao = produtoEdicao.getVenda().divide(previousProdutoEdicao.getVenda(), 2, BigDecimal.ROUND_FLOOR);

			    listDivBoolean.add(divVendaEdicao.compareTo(BigDecimal.ONE) == 1);
			}
		    }
		}

		iProdutoEdicao++;
	    }

	    if (!listDivBoolean.isEmpty() && !listDivBoolean.contains(Boolean.FALSE)) {
		indiceVendaCrescente = indiceVendaCrescente.add(new BigDecimal(0.1).divide(BigDecimal.ONE, 1, BigDecimal.ROUND_FLOOR));
	    }
	}
	
	cota.setIndiceVendaCrescente(indiceVendaCrescente);
    }
}
