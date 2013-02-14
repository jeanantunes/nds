package br.com.abril.nds.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.dao.DefinicaoBasesDAO;
import br.com.abril.nds.model.ProdutoEdicao;

public class PreparaEstudoService {
    
    private static final Logger log = LoggerFactory.getLogger(PreparaEstudoService.class);

    private DefinicaoBasesDAO definicaoBasesDAO = new DefinicaoBasesDAO();
   
    public List<ProdutoEdicao> buscaEdicoesPorLancamento(ProdutoEdicao edicao) {
	log.info("Buscando edições para estudo.");
	return definicaoBasesDAO.listaEdicoesPorLancamento(edicao);
    }
   
    public List<ProdutoEdicao> buscaEdicoesAnosAnteriores(ProdutoEdicao edicao) {
	List<ProdutoEdicao> listaEdicoesAnosAnterioresMesmoMes = definicaoBasesDAO.listaEdicoesAnosAnteriores(edicao, true);
	
	if(!listaEdicoesAnosAnterioresMesmoMes.isEmpty()) {
	    return listaEdicoesAnosAnterioresMesmoMes;
	}
	
	return definicaoBasesDAO.listaEdicoesAnosAnteriores(edicao, false);
    }
   
}
