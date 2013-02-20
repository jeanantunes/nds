package br.com.abril.nds.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
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
   
    public List<ProdutoEdicao> buscaEdicoesAnosAnterioresVeraneio(ProdutoEdicao edicao) {
	List<ProdutoEdicao> listaEdicoesAnosAnterioresMesmoMes = definicaoBasesDAO.listaEdicoesAnosAnteriores(edicao, true, getDatasPeriodoVeraneio(edicao));
	
	if(!listaEdicoesAnosAnterioresMesmoMes.isEmpty()) {
	    return listaEdicoesAnosAnterioresMesmoMes;
	}
	
	return definicaoBasesDAO.listaEdicoesAnosAnteriores(edicao, false, getDatasPeriodoVeraneio(edicao));
    }
    
    public List<ProdutoEdicao> buscaEdicoesAnosAnterioresSaidaVeraneio(ProdutoEdicao edicao) {
	return definicaoBasesDAO.listaEdicoesAnosAnteriores(edicao, false, getDatasPeriodoSaidaVeraneio(edicao));
    }

    private List<LocalDate> getDatasPeriodoSaidaVeraneio(ProdutoEdicao edicao) {
	// TODO Auto-generated method stub
	return null;
    }

    private List<LocalDate> getDatasPeriodoVeraneio(ProdutoEdicao edicao) {
	List<LocalDate> periodoVeraneio = new ArrayList<>();
	return periodoVeraneio;
    }
   
}
