package br.com.abril.nds.service;

import java.math.BigDecimal;

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;

public class CotaServiceEstudo {

    public static void calculate(CotaEstudo cota) {
	// Cálculo da Venda Média Final
	BigDecimal soma = BigDecimal.ZERO;

	// Verificação se a cota só recebeu edições abertas e somatória delas
	cota.setCotaSoRecebeuEdicaoAberta(true);
	cota.setSomaReparteEdicoesAbertas(BigDecimal.ZERO);
	if (cota.getEdicoesRecebidas() != null) {
	    for (ProdutoEdicaoEstudo edicao : cota.getEdicoesRecebidas()) {
		soma = soma.add(edicao.getVenda());
		if (edicao.isEdicaoAberta()) {
		    cota.setSomaReparteEdicoesAbertas(cota.getSomaReparteEdicoesAbertas().add(edicao.getReparte()));
		} else {
		    if (cota.getVendaEdicaoMaisRecenteFechada() == null) {
			cota.setVendaEdicaoMaisRecenteFechada(edicao.getVenda());
		    }
		    cota.setCotaSoRecebeuEdicaoAberta(false);
		}
	    }
	    if (cota.getEdicoesRecebidas().size() != 0) {
		cota.setVendaMediaNominal(soma.divide(new BigDecimal(cota.getEdicoesRecebidas().size()), 2, BigDecimal.ROUND_HALF_UP));
	    }
	    cota.setVendaMedia(cota.getVendaMediaNominal());
	}
    }
}
