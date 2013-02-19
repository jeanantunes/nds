package br.com.abril.nds.service;

import java.math.BigDecimal;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;

public class EstudoService {

    public static void calculate(Estudo estudo) {
	// Somatória da venda média de todas as cotas e
	// Somatória de reparte das edições abertas de todas as cotas
	estudo.setSomatoriaVendaMediaFinal(BigDecimal.ZERO);
	estudo.setSomatoriaReparteEdicoesAbertas(BigDecimal.ZERO);
	for (Cota cota : estudo.getCotas()) {
	    CotaService.calculate(cota);
	    if (!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
		    || !cota.getClassificacao().equals(ClassificacaoCota.BancaSoComEdicaoBaseAberta)
		    || !cota.getClassificacao().equals(ClassificacaoCota.RedutorAutomatico)) {
		estudo.setSomatoriaVendaMediaFinal(estudo.getSomatoriaVendaMedia().add(cota.getVendaMedia()));
	    }
	    estudo.setSomatoriaReparteEdicoesAbertas(estudo.getSomatoriaReparteEdicoesAbertas().add(cota.getSomaReparteEdicoesAbertas()));
	}
    }
}
