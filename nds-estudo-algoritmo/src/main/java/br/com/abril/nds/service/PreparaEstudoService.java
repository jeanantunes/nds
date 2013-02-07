package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.EstoqueProdutoCotaDAO;
import br.com.abril.nds.model.Cota;

public class PreparaEstudoService {

    private CotaDAO cotaDAO = new CotaDAO();
    private EstoqueProdutoCotaDAO estoqueProdutoCotaDAO = new EstoqueProdutoCotaDAO();
    
    public List<Cota> populaCotasParaEstudo() {
	List<Cota> cotas = cotaDAO.getCotas();
	for (Cota cota : cotas) {
	    cota.setEstoqueProdutoCotas(estoqueProdutoCotaDAO.getByCotaId(cota.getId()));
	}
	return cotas;
    }
}
