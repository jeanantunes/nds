package br.com.abril.nds.service;

import java.math.BigDecimal;

import br.com.abril.nds.dao.EstudoDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;

public class EstudoServiceEstudo {

    private EstudoDAO estudoDAO = new EstudoDAO();
    private ProdutoEdicaoDAO produtoEdicaoDAO = new ProdutoEdicaoDAO();
    
    public static void calculate(Estudo estudo) {
	// Somatória da venda média de todas as cotas e
	// Somatória de reparte das edições abertas de todas as cotas
	estudo.setSomatoriaVendaMediaFinal(BigDecimal.ZERO);
	estudo.setSomatoriaReparteEdicoesAbertas(BigDecimal.ZERO);
	for (Cota cota : estudo.getCotas()) {
	    CotaServiceEstudo.calculate(cota);
	    if (!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
		    || !cota.getClassificacao().equals(ClassificacaoCota.BancaSoComEdicaoBaseAberta)
		    || !cota.getClassificacao().equals(ClassificacaoCota.RedutorAutomatico)) {
		estudo.setSomatoriaVendaMediaFinal(estudo.getSomatoriaVendaMedia().add(cota.getVendaMedia()));
	    }
	    estudo.setSomatoriaReparteEdicoesAbertas(estudo.getSomatoriaReparteEdicoesAbertas().add(cota.getSomaReparteEdicoesAbertas()));
	}
    }

    public void carregarParametros(Estudo estudo) {
	estudoDAO.carregarParametros(estudo);

	estudo.setProduto(produtoEdicaoDAO.getLastProdutoEdicaoByIdProduto(estudo.getProduto().getCodigoProduto()));
    }

    public void gravarEstudo(Estudo estudo) {
	estudoDAO.gravarEstudo(estudo);
    }
}
