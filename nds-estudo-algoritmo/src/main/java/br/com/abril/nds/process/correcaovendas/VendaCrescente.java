package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;

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
@Component
public class VendaCrescente {

    protected void executar(CotaEstudo cota) {

	BigDecimal indiceVendaCrescente = BigDecimal.ONE;
	List<Boolean> listDivBoolean = new ArrayList<Boolean>();
	if (cota.getEdicoesRecebidas() != null && cota.getEdicoesRecebidas().size() >= 4) {

	    int iProdutoEdicao = 0;
	    while (iProdutoEdicao < cota.getEdicoesRecebidas().size()) {
		ProdutoEdicaoEstudo produtoEdicao = cota.getEdicoesRecebidas().get(iProdutoEdicao);
		ProdutoEdicaoEstudo previousProdutoEdicao = null;
		if (iProdutoEdicao > 0) {
		    previousProdutoEdicao = cota.getEdicoesRecebidas().get(iProdutoEdicao - 1);

		    if (previousProdutoEdicao.getProduto().getId().equals(produtoEdicao.getProduto().getId()) && !previousProdutoEdicao.isEdicaoAberta()
			    && !produtoEdicao.isEdicaoAberta()) {
			if (produtoEdicao.getVenda().compareTo(BigDecimal.ZERO) == 1) {
			    BigDecimal divVendaEdicao = previousProdutoEdicao.getVenda().divide(produtoEdicao.getVenda(), 4, BigDecimal.ROUND_HALF_UP);
			    previousProdutoEdicao.setDivisaoVendaCrescente(divVendaEdicao);
			    Boolean boo = divVendaEdicao.compareTo(BigDecimal.ONE) == 1;
			    listDivBoolean.add(boo);
			}
		    }
		}
		iProdutoEdicao++;
	    }

	    if (!listDivBoolean.isEmpty() && !listDivBoolean.contains(Boolean.FALSE)) {
		indiceVendaCrescente = indiceVendaCrescente.add(new BigDecimal(0.1).divide(BigDecimal.ONE, 1, BigDecimal.ROUND_HALF_UP));
	    }
	}
	cota.setIndiceVendaCrescente(indiceVendaCrescente);
    }
}
