package br.com.abril.nds.service;

import java.math.BigDecimal;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;

public class CotaServiceEstudo {

    public static void calculate(Cota cota) {
	// Cálculo da Venda Média Final
	BigDecimal soma = BigDecimal.ZERO;

	// Verificação se a cota só recebeu edições abertas e somatória delas
	cota.setCotaSoRecebeuEdicaoAberta(true);
	cota.setSomaReparteEdicoesAbertas(BigDecimal.ZERO);
	if (cota.getEdicoesRecebidas() != null) {
	    for (ProdutoEdicao edicao : cota.getEdicoesRecebidas()) {
		soma = soma.add(edicao.getVenda());
		if (edicao.isEdicaoAberta()) {
		    cota.setSomaReparteEdicoesAbertas(cota.getSomaReparteEdicoesAbertas().add(edicao.getReparte()));
		} else {
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
