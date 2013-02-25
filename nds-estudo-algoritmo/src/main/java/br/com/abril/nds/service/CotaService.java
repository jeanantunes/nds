package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;

public class CotaService {

    public static void calculate(Cota cota) {
    	// Cálculo da Venda Média Final
    	BigDecimal soma = BigDecimal.ZERO;
    	List<ProdutoEdicao> edicoesRecebidas = cota.getEdicoesRecebidas();
    	if (edicoesRecebidas != null) {
        	for (ProdutoEdicao edicao : edicoesRecebidas) {
        	    soma = soma.add(edicao.getVenda());
        	}
        	if (edicoesRecebidas.size() != 0) {
        	    cota.setVendaMedia(soma.divide(new BigDecimal(edicoesRecebidas.size()), 2, BigDecimal.ROUND_HALF_UP));
        	}
        
        	// Verificação se a cota só recebeu edições abertas e somatória delas
        	// TODO: confirmar se é para verificar em todas as edições que a cota recebeu mesmo ou somente nas edições bases
        	cota.setCotaSoRecebeuEdicaoAberta(true);
        	cota.setSomaReparteEdicoesAbertas(BigDecimal.ZERO);
        	for (ProdutoEdicao edicao : edicoesRecebidas) {
        	    if (edicao.isEdicaoAberta()) {
        		cota.setSomaReparteEdicoesAbertas(cota.getSomaReparteEdicoesAbertas().add(edicao.getReparte()));
        	    } else {
        		cota.setCotaSoRecebeuEdicaoAberta(false);
        	    }
        	}
    	}
    }
}
